package delarosa.tests;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import delarosa.pages.CartPage;
import delarosa.pages.DashboardPage;
import delarosa.pages.MyOrdersPage;
import delarosa.pages.OrderDetailsPage;
import delarosa.pages.OrderPage;
import delarosa.pages.ThanksPage;
import delarosa.utils.AuthAPI;
import delarosa.utils.JSONDataLoader;
import delarosa.utils.OrderAPI;
import io.restassured.response.Response;

public class OrderFlow extends BaseTest {

    private String email;
    private String otherUserEmail;
    private String password;
    private String product1;
    private String product2;
    private String productId;
    private String insertLocation;
    private String selectLocation;

    @BeforeClass
    public void loadTestData() {
        email = JSONDataLoader.getLoginEmail();
        otherUserEmail = JSONDataLoader.getOtherUserEmail();
        password = JSONDataLoader.getLoginPassword();
        product1 = JSONDataLoader.getProduct1();
        product2 = JSONDataLoader.getProduct2();
        productId = JSONDataLoader.getProductId();
        insertLocation = JSONDataLoader.getInsertLocation();
        selectLocation = JSONDataLoader.getSelectLocation();
    }

    @BeforeMethod
    public void loginAPI() {
        String token = AuthAPI.loginAndGetToken(email, password);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.setItem('token', '" + token + "')");
        driver.get("https://rahulshettyacademy.com/client");
    }

    @Test(description = "Count the total price of added products in cart")
    public void countTotalPriceOfCart() throws InterruptedException {
        DashboardPage dashboardPage = new DashboardPage(driver);
        CartPage cartPage = new CartPage(driver);

        dashboardPage.addProductToCart(product1);
        dashboardPage.addProductToCart(product2);
        dashboardPage.goToCart();

        int expectedTotal = cartPage.calculateTotalPrice();
        int actualTotal = cartPage.getActualTotalPrice();
        Assert.assertEquals(actualTotal, expectedTotal,
                "Expected is '" + expectedTotal + "' but got '" + actualTotal + "'");

        test.pass("Actual total (" + actualTotal + ") matches Expected total (" + expectedTotal + ")");

    }

    @Test(description = "Create order with same email as the respective account")
    public void createOrderWithRespectiveEmail() throws InterruptedException {
        DashboardPage dashboardPage = new DashboardPage(driver);
        OrderPage orderPage = new OrderPage(driver);
        ThanksPage thanksPage = new ThanksPage(driver);
        MyOrdersPage myOrdersPage = new MyOrdersPage(driver);

        dashboardPage.addProductToCart(product1);
        dashboardPage.addProductToCart(product2);
        dashboardPage.goToCart();

        new CartPage(driver).clickCheckout();

        orderPage.inputCountryShippingInfo(insertLocation, selectLocation);
        orderPage.clickOrder();

        String orderId = thanksPage.getOrderId();
        thanksPage.goToMyOrders();
        myOrdersPage.clickViewOrder(orderId);

        String emailText = new OrderDetailsPage(driver).getEmailBillingAddress();
        String cleanedText = emailText.trim();
        Assert.assertTrue(cleanedText.contains(email),
                "Email billing address expected contains '" + email + "' message, but got: " + cleanedText);
    }

    @Test(description = "Create order with different email from the respective account")
    public void createOrderWithDifferentEmail() throws InterruptedException {
        DashboardPage dashboardPage = new DashboardPage(driver);
        OrderPage orderPage = new OrderPage(driver);
        ThanksPage thanksPage = new ThanksPage(driver);

        dashboardPage.addProductToCart(product1);
        dashboardPage.addProductToCart(product2);
        dashboardPage.goToCart();

        new CartPage(driver).clickCheckout();

        orderPage.changeEmailShippingInfo(email);
        String emailLabel = orderPage.getShippingEmailLabel();
        String cleanedEmailLabel = emailLabel.trim();
        Assert.assertTrue(cleanedEmailLabel.contains(otherUserEmail),
                "Email label expected contains '" + otherUserEmail + "' message, but got: "
                        + cleanedEmailLabel);

        orderPage.inputCountryShippingInfo(insertLocation, selectLocation);
        orderPage.clickOrder();

        String orderId = thanksPage.getOrderId();
        thanksPage.goToMyOrders();
        new MyOrdersPage(driver).clickViewOrder(orderId);

        String emailText = new OrderDetailsPage(driver).getEmailBillingAddress();
        String cleanedText = emailText.trim();
        Assert.assertTrue(cleanedText.contains(otherUserEmail),
                "Email billing address expected contains '" + otherUserEmail + "' message, but got: "
                        + cleanedText);
    }

    @Test(description = "Create order without shipping location provided")
    public void createOrderWithoutShippingLocation() throws InterruptedException {
        DashboardPage dashboardPage = new DashboardPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        dashboardPage.addProductToCart(product1);
        dashboardPage.addProductToCart(product2);
        dashboardPage.goToCart();

        new CartPage(driver).clickCheckout();

        orderPage.clickOrder();
        String toastText = orderPage.getToastMessage();
        String cleanedText = toastText.replaceAll("//s+", " ");
        Assert.assertTrue(cleanedText.equals("Please Enter Full Shipping Information"),
                "Toast expected to be '" + cleanedText + "' but got: " + toastText);

    }

    @Test(description = "View order created by different account from the respective account should return 403")
    public void viewOrderFromDifferentAccountShouldReturn403() {
        String userAToken = AuthAPI.loginAndGetToken(otherUserEmail, password);

        String orderId = OrderAPI.placeOrder(userAToken, productId);

        String userBToken = AuthAPI.loginAndGetToken(email, password);

        Response response = OrderAPI.getOrderDetails(userBToken,
                orderId);

        Assert.assertEquals(response.statusCode(), 403, "Expected 403 Forbidden when viewing other's order.");
    }
}