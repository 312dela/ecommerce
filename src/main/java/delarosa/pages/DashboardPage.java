package delarosa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    private By cartNavbar = By.cssSelector("button[routerlink='/dashboard/cart']");
    private By toast = By.id("toast-container");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void addProductToCart(String product) throws InterruptedException {
        getElement(By.xpath("//b[text()='" + product + "']"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                driver.findElement(By.xpath("//b[contains(text(),'" + product
                        + "')]/parent::h5/following-sibling::button[contains(text(),'Add To Cart')]")));

        waitForElementToDisappear(toast);
        Thread.sleep(2000);
    }

    public void goToCart() throws InterruptedException {
        getElement(cartNavbar);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(cartNavbar));
    }

}

