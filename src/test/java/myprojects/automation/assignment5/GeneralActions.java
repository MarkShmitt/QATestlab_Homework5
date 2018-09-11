package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() throws InterruptedException {
        // TODO implement logic to open random product before purchase
        List<WebElement> links = driver.findElements(By.xpath("//div[@class='thumbnail-container']/a"));
        int count = links.size();
        Random randomItem = new Random();
        int linkNumber = randomItem.nextInt(count);
        WebElement link = links.get(linkNumber);
        System.out.println(link.getText());
        Thread.sleep(2000);
        link.click();
        //links.get(randomItem.nextInt(count)).click();
        if (count == 0) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    private String productName;
    private int productQty;
    private float productPrice;
    private String productURL;

    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        // TODO extract data from opened page
        List<WebElement> links = driver.findElements(By.xpath("//div//ol/li/a"));
        productURL = links.get(links.size()-1).getAttribute("href");

        productName = driver.findElement(By.cssSelector("h1[class='h1']")).getText();

        productPrice = DataConverter.parsePriceValue(driver.findElement(By.xpath("//div[@class='current-price']/span")).getText());
        driver.findElement(By.xpath("//div//a[@href='#product-details']")).click();
        waitForContentLoad(By.xpath("//div[@class='product-quantities']/span"));
        productQty = (DataConverter.parseStockValue(driver.findElement(By.xpath("//div[@class='product-quantities']/span"))
                .getText()));

        if (productName == null || productQty == 0 || productPrice == 0) {
            throw new UnsupportedOperationException();
        }
        return new ProductData(productName, productQty, productPrice);
    }

    public void checkProductOptions() {
        CustomReporter.logAction("Add product and get information about opened product");
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='product-line-info']/a"))
                .getText().toUpperCase(), productName);
        List<WebElement> itemsCount = driver.findElements(By.xpath("//div[@class='product-line-grid']"));
        Assert.assertEquals(itemsCount.size(), 1);
        Assert.assertEquals(DataConverter.parsePriceValue(driver.findElement(By.cssSelector("span[class='product-price']"))
                .getText()), productPrice);
    }



    public void createNewOrder(String firstname, String lastname, String email, String address1, String postcode, String city)  {
        waitForContentLoad(By.xpath("//button[@name='continue']"));
        driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys(firstname);
        driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(lastname);
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("button[class='continue btn btn-primary pull-xs-right'")).click();

        driver.findElement(By.cssSelector("input[name='address1']")).sendKeys(address1);
        driver.findElement((By.cssSelector("input[name='postcode']"))).sendKeys(postcode);
        driver.findElement(By.cssSelector("input[name='city']")).sendKeys(city);
        driver.findElement(By.cssSelector("button[name='confirm-addresses']")).click();

        driver.findElement(By.cssSelector("button[name='confirmDeliveryOption']")).click();

        driver.findElement(By.xpath("//input[@id='payment-option-2']")).click();
        driver.findElement(By.xpath("//input[@id='conditions_to_approve[terms-and-conditions]']")).click();
        driver.findElement(By.xpath("//button[@class='btn btn-primary center-block']")).click();
    }

    public void checkCreationNewOrder() {

        waitForContentLoad(By.id("main"));
        Assert.assertTrue(driver.findElement(By.xpath("//i[@class='material-icons done']")).isDisplayed());
        Assert.assertEquals(DataConverter.parseStockValue(driver.findElement(By.cssSelector("div[class='col-xs-2']"))
                .getText()), 1);
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='col-sm-4 col-xs-9 details']/span"))
                .getText(), productName);
        Assert.assertEquals(DataConverter.parsePriceValue(driver.findElement(By.className("col-xs-5 text-sm-right text-xs-left"))
                .getText()), productPrice);
    }

    public void checkAllQtyProduct() {
        driver.get(productURL);
        driver.findElement(By.xpath("//div//a[@href='#product-details']")).click();
        waitForContentLoad(By.xpath("//div[@class='product-quantities']/span"));
        productQty = (DataConverter.parseStockValue(driver.findElement(By.xpath("//div[@class='product-quantities']/span"))
                .getText()));
        Assert.assertEquals(productQty, productQty - 1);
    }


    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad(By locator) {
        // TODO implement generic method to wait until page content is loaded

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        // ...
    }
}