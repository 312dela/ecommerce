package delarosa.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {
    private By productPrice = By.className("prodTotal");
    private By checkoutButton = By.cssSelector(".totalRow button");
    private By actualTotalPrice = By.xpath("//span[text()='Total']/following-sibling::span[1]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void clickCheckout() throws InterruptedException {

        getElement(checkoutButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                driver.findElement(checkoutButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(checkoutButton));
    }

    public int calculateTotalPrice() {
        getElement(productPrice);
        List<WebElement> allProductsPrice = driver.findElements(productPrice);
        int expectedTotal = 0;
        for (WebElement el : allProductsPrice) {
            String text = el.getText();
            String[] textParts = text.trim().split(" ");
            int pricePerProduct = Integer.parseInt(textParts[1]);
            expectedTotal += pricePerProduct;
        }
        return expectedTotal;
    }

    public int getActualTotalPrice() {
        String totalPrice = getText(actualTotalPrice);
        String price = totalPrice.trim().split("\\$")[1];
        int actualTotal = Integer.parseInt(price);
        return actualTotal;
    }

}
