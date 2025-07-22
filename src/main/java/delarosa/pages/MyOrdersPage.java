package delarosa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyOrdersPage extends BasePage {
    public MyOrdersPage(WebDriver driver) {
        super(driver);
    }

    public void clickViewOrder(String orderId) {
        safeClick(By.xpath("//th[text()='" + orderId + "']/parent::tr//button[contains(@class,'btn-primary')]"));
    }
}
