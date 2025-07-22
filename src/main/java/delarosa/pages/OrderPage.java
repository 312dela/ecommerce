package delarosa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrderPage extends BasePage {
    private By shippingEmailLabel = By.cssSelector(".user__name label");
    private By shippingEmailField = By.cssSelector(".user__name input[type='text']");
    private By countryField = By.cssSelector("input[placeholder='Select Country']");
    private By orderButton = By.className("action__submit");
    private By toast = By.id("toast-container");

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    public void changeEmailShippingInfo(String email) {
        getElement(shippingEmailField).clear();
        safeType(shippingEmailField, email);

    }

    public void inputCountryShippingInfo(String locationInput, String selectLocation) {
        safeType(countryField, locationInput);
        By locationOption = By.xpath("//*[contains(text(),'" + selectLocation + "')]");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                locationOption, selectLocation));
        safeClick(locationOption);
    }

    public void clickOrder() {
        getElement(orderButton);
        safeClick(orderButton);
    }

    public String getShippingEmailLabel() {
        return getText(shippingEmailLabel);
    }

    public String getToastMessage() throws InterruptedException {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                toast, "Please Enter Full Shipping Information"));
        return getText(toast);
    }
}
