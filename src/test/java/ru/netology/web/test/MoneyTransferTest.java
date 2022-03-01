package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {
    String cardOne = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    @Test
    void shouldTransferMoneyBetweenOwnCards() {

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var dashboardPage = new DashboardPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var balanceCard1 = dashboardPage.getCardBalance(cardOne);
    }

}
