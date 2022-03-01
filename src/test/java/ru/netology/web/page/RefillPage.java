package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class RefillPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement refillButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");

    public DashboardPage refillCard(String amount, String from) {
        amountField.setValue(amount);
        fromField.setValue(from);
        refillButton.click();
        return new DashboardPage();
    }

    public DashboardPage refillCardTwo(String amount, DataHelper.Card card) {
        amountField.setValue(amount);
        fromField.setValue(card.getNumberOne());
        refillButton.click();
        return new DashboardPage();
    }



    public DashboardPage clearAccounts(){
        String cardFirstId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
        String cardSecondId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

        DashboardPage dashBoard = new DashboardPage();
        if (dashBoard.getCardBalance(cardFirstId) != dashBoard.getCardBalance(cardSecondId)){

        }
        return new DashboardPage();
    }


}
