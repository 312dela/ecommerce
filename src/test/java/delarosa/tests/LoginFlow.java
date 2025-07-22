package delarosa.tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import delarosa.pages.LoginPage;
import delarosa.utils.JSONDataLoader;

public class LoginFlow extends BaseTest {
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        List<Map<String, String>> variants = JSONDataLoader.getLoginVariants();
        Object[][] data = new Object[variants.size()][3];

        for (int i = 0; i < variants.size(); i++) {
            Map<String, String> variant = variants.get(i);
            data[i][0] = variant.get("email");
            data[i][1] = variant.get("password");
            data[i][2] = variant.get("description");
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginTest(String email, String password, String description) throws InterruptedException {
        // test = extent.createTest(description);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLoginForm(email, password);
        loginPage.clickLogin();

        String toastText = loginPage.getToastMessage();
        String cleanedText = toastText.trim().replaceAll("\\s+", " ");
        Assert.assertTrue(cleanedText.contains("Successfully"),
                "Expected contains 'successfully' message, but got: " + cleanedText);
    }
}
