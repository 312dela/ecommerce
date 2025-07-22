package delarosa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ThanksPage extends BasePage {
    private By ordersNavbar = By.cssSelector("button[routerlink='/dashboard/myorders']");
    private By orderIdLabel = By.cssSelector(".ng-star-inserted label[class='ng-star-inserted']:first-of-type");

    public ThanksPage(WebDriver driver) {
        super(driver);
    }

    public void goToMyOrders() {
        safeClick(ordersNavbar);
    }

    public String getOrderId() {
        String text = getText(orderIdLabel);
        String orderId = text.split("\\|")[1].trim();
        return orderId;
    }
}
