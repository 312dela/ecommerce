package delarosa.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import delarosa.pages.RegistrationPage;
import delarosa.utils.EmailGenerator;

public class RegistrationFlow extends BaseTest {
    private String emailLower;
    private String emailUpper;
    private RegistrationPage registrationPage;

    @BeforeClass
    public void setupEmail() {
        emailLower = EmailGenerator.generateEmail();
        emailUpper = EmailGenerator.toUpperCaseFirstChar(emailLower);
    }

    @BeforeMethod
    public void initPage() {
        registrationPage = new RegistrationPage(driver);
    }

    @Test(priority = 1, description = "Register New Account - Lowercase Email")
    public void createAccountWithUnregisteredEmail() throws InterruptedException {
        registrationPage.fillRegistrationForm(emailLower);
        String toastText = registrationPage.getToastMessage();
        String cleanedText = toastText.trim().replaceAll("\\s+", " ");
        Assert.assertTrue(cleanedText.contains("Successfully"),
                "Expected contains 'Successfully' message, but got: " + cleanedText);
    }

    @Test(priority = 2, description = "Register Existing Account - Lowercase Email")
    public void createAccountWithRegisteredEmailLowercase() throws InterruptedException {
        registrationPage.fillRegistrationForm(emailLower);
        String toastText = registrationPage.getToastMessage();
        String cleanedText = toastText.trim().replaceAll("\\s+", " ");
        Assert.assertTrue(cleanedText.contains("already"),
                "Expected contains 'already' message, but got: " + cleanedText);
    }

    @Test(priority = 3, description = "Register Existing Account - Uppercase Email")
    public void createAccountWithRegisteredEmailUppercase() throws InterruptedException {
        registrationPage.fillRegistrationForm(emailUpper);
        String toastText = registrationPage.getToastMessage();
        String cleanedText = toastText.trim().replaceAll("\\s+", " ");
        Assert.assertTrue(cleanedText.contains("already"),
                "Expected contains 'already' message, but got: " + cleanedText);
    }
}