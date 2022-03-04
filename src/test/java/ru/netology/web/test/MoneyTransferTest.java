package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getCard;
import static ru.netology.web.data.DataHelper.getWrongCard;

public class MoneyTransferTest {

    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private DataHelper.AuthInfo authInfoInvalid = DataHelper.getInvalidAuthInfo(authInfo);
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

        public void refresh() {
            DashboardPage dashBoard = new DashboardPage();
            int diff = dashBoard.getCardBalance(1) - 10000;
            if (diff != 0) {
                if (diff < 0) {
                    String numberCardTwo = DataHelper.getCard(2).getCardNumber();
                    dashBoard.increaseBalance(1).refillCard(Integer.toString(Math.abs(diff)), numberCardTwo);
                } else {
                    String numberCardOne = DataHelper.getCard(1).getCardNumber();
                    dashBoard.increaseBalance(2).refillCard(Integer.toString(Math.abs(diff)), numberCardOne);
                }
            }
        }

        @BeforeEach
        void setup() {
            open("http://localhost:9999");
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(authInfo);
            verificationPage.validVerify(verificationCode);
            // Востановление к первоначальному состоянию системы, когда на картах по 10000 р.
            refresh();
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsFromSecondToFirst() {
            DashboardPage dashBoard = new DashboardPage();
            //сумма пополнения
            int amount = 5000;
            //Первичный баланс 1-ой и 2-ой карты до операции
            int primaryBalanceOne = dashBoard.getCardBalance(1);
            int primaryBalanceTwo = dashBoard.getCardBalance(2);
            // Выбор первой карты для пополнения
            var refillPage = dashBoard.increaseBalance(1);
            // Сколько и откуда пополнить
            refillPage.refillCard(Integer.toString(amount), getCard(2).getCardNumber());
            // Баланс первой карты после операции
            int balanceActualOne = dashBoard.getCardBalance(1);
            // Баланс второй карты после операции
            int balanceActualTwo = dashBoard.getCardBalance(2);
            assertEquals((primaryBalanceOne + amount), balanceActualOne);
            assertEquals((primaryBalanceTwo - amount), balanceActualTwo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsFromFirstToSecond() {
            DashboardPage dashBoard = new DashboardPage();
            int amount = 5000;
            int primaryBalanceOne = dashBoard.getCardBalance(1);
            int primaryBalanceTwo = dashBoard.getCardBalance(2);
            var refillPage = dashBoard.increaseBalance(2);
            refillPage.refillCard(Integer.toString(amount), getCard(1).getCardNumber());
            int balanceActualOne = dashBoard.getCardBalance(1);
            int balanceActualTwo = dashBoard.getCardBalance(2);
            assertEquals((primaryBalanceOne - amount), balanceActualOne);
            assertEquals((primaryBalanceTwo + amount), balanceActualTwo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsWhenNumberCardWrong() {
            DashboardPage dashBoard = new DashboardPage();
            int amount = 5000;
            var refillPage = dashBoard.increaseBalance(2);
            refillPage.refillCard(Integer.toString(amount), getWrongCard().getCardNumber());
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsIfNotEnoughBalanceAtFirstCard() {
            DashboardPage dashBoard = new DashboardPage();
            int amount = 15000;
            var refillPage = dashBoard.increaseBalance(1);
            refillPage.wrongRefillCard(Integer.toString(amount), getCard(2).getCardNumber());
            refillPage.checkErrorBalance();
        }
    }
}
