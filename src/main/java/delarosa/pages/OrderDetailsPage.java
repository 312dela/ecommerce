package delarosa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderDetailsPage extends BasePage {
    private By emailBillingAddress = By.xpath("//div[contains(text(),'Billing Address')]/following-sibling::p[1]");

    public OrderDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getEmailBillingAddress() {
        return getText(emailBillingAddress);
    }
}
