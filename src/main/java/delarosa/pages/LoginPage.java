package delarosa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private By emailField = By.id("userEmail");
    private By passwordField = By.id("userPassword");
    private By loginButton = By.id("login");
    private By toast = By.id("toast-container");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void fillLoginForm(String email, String password) {
        safeType(emailField, email);
        safeType(passwordField, password);
    }

    public void clickLogin() {
        safeClick(loginButton);
    }

    public String getToastMessage() throws InterruptedException {
        getElement(toast);
        Thread.sleep(1000);
        return getText(toast);
    }
}
