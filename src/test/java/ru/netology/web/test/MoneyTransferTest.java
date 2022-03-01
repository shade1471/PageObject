package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    String cardOneId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    String cardTwoId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";
//    String CardOne = DataHelper.getNumberCardFirst(authInfo);
//    String CardTwo = getNumberCardSecond(authInfo);

    @Test
    void shouldTransferMoneyBetweenOwnCards() {

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashBoard = verificationPage.validVerify(verificationCode);

        dashBoard.refresh(cardOneId, cardTwoId, authInfo);
        var refillPage = dashBoard.increaseBalance(cardOneId);

        var cardNumber = DataHelper.getNumberCard(authInfo);
        refillPage.refillCard("5000", cardNumber.getNumberTwo());
        dashBoard.increaseBalance(cardOneId);
        refillPage.refillCard("5000", cardNumber.getNumberTwo());
        int balanceActual = dashBoard.getCardBalance(cardTwoId);

        assertEquals(20000, balanceActual);


    }

}
