package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    String cardOne = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    String cardTwo = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id){
        val text = cards.findBy(attribute(id)).text();
        //$$(".list__item").findBy(cssClass("[data-test-id]");
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
