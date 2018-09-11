package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.utils.Properties;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PlaceOrderTest extends BaseTest {

    @DataProvider
    public Object[][] getCustomerData() {
        return new Object[][]{
                {"Mark", "Shmitt", "mark@test.com", "Cherkassy str.", "18000", "Cherkassy"},
        };
    }

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        driver.get(Properties.getBaseUrl());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(driver.findElement(By.xpath("//div[@id = '_mobile_logo']/a")).isDisplayed(), "used mobile-version");
        softAssert.assertTrue(driver.findElement(By.xpath("//div[@id= 'carousel']")).isDisplayed(), "used desktop-version");
        softAssert.assertAll();
    }

    @Test(dataProvider = "getCustomerData")
    public void createNewOrder(String firstname, String lastname, String email, String address1, String postcode, String city)
            throws InterruptedException {
        // TODO implement order creation test
        driver.get(Properties.getBaseUrl());
        driver.findElement(By.cssSelector("a[class='all-product-link pull-xs-left pull-md-right h4']")).click();
        // open random product
        actions.openRandomProduct();
        // save product parameters
        actions.getOpenedProductInfo();
        // add product to Cart and validate product information in the Cart
        driver.findElement(By.cssSelector("button[class='btn btn-primary add-to-cart']")).click();

        actions.waitForContentLoad(By.xpath("//a[@class='btn btn-primary']"));
        driver.findElement(By.xpath("//a[@class='btn btn-primary']")).click();

        actions.checkProductOptions();
        // proceed to order creation, fill required information
        driver.findElement(By.cssSelector("a[class='btn btn-primary']")).click();
        actions.createNewOrder(firstname, lastname, email, address1, postcode, city);
        // place new order and validate order summary
        actions.checkCreationNewOrder();
        // check updated In Stock value
        actions.checkAllQtyProduct();

    }

}
