package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    private String cardOneId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private String cardTwoId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private DataHelper.AuthInfo authInfoInvalid = DataHelper.getInvalidAuthInfo(authInfo);
    private DataHelper.Card cardNumber = DataHelper.getNumberCard(authInfo);
    private DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);

    @Nested
    public class ShouldAuth {

        @BeforeEach
        void setup() {
            open("http://localhost:9999");
        }

        @Test
        void shouldAuthIfUserExist() {
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(authInfo);
            verificationPage.validVerify(verificationCode);
        }

        @Test
        void shouldAuthIfUserNotExist() {
            var loginPage = new LoginPage();
            loginPage.invalidLogin(authInfoInvalid);
        }
    }

    @Nested
    public class TransferMoney {

        @BeforeEach
        void setup() {
            open("http://localhost:9999");
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(authInfo);
            var dashBoard = verificationPage.validVerify(verificationCode);
            // Востановление к первоначальному состоянию системы, когда на картах по 10000 р.
            dashBoard.refresh(cardOneId, cardTwoId, authInfo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsFromSecondToFirst() {
            DashboardPage dashBoard = new DashboardPage();
            // Выбор первой карты для пополнения
            var refillPage = dashBoard.increaseBalance(cardOneId);
            // Сколько и откуда пополнить
            refillPage.refillCard("5000", cardNumber.getNumberTwo());
            // Баланс первой карты после операции
            int balanceActualOne = dashBoard.getCardBalance(cardOneId);
            // Баланс второй карты после операции
            int balanceActualTwo = dashBoard.getCardBalance(cardTwoId);
            assertEquals(15000, balanceActualOne);
            assertEquals(5000, balanceActualTwo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsFromFirstToSecond() {
            DashboardPage dashBoard = new DashboardPage();
            var refillPage = dashBoard.increaseBalance(cardTwoId);
            refillPage.refillCard("5000", cardNumber.getNumberOne());
            int balanceActualOne = dashBoard.getCardBalance(cardOneId);
            int balanceActualTwo = dashBoard.getCardBalance(cardTwoId);
            assertEquals(5000, balanceActualOne);
            assertEquals(15000, balanceActualTwo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsIfNotEnoughBalance() {
            DashboardPage dashBoard = new DashboardPage();
            dashBoard.refresh(cardOneId, cardTwoId, authInfo);
            var refillPage = dashBoard.increaseBalance(cardTwoId);
            refillPage.refillCard("15000", cardNumber.getNumberOne());
            int balanceActualOne = dashBoard.getCardBalance(cardOneId);
            int balanceActualTwo = dashBoard.getCardBalance(cardTwoId);
            assertEquals(10000, balanceActualOne);
            assertEquals(10000, balanceActualTwo);
        }
    }
}
