package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static dataProviders.datasetFileReader.getDatasetValue;
import static org.junit.Assert.*;

import static dataProviders.configFileReader.getPropertyValue;
import static dataProviders.repositoryFileReader.findElementRepo;
import static dataProviders.repositoryFileReader.constructElement;

public class webSteps {

    WebDriver driver;
    private Scenario scenario;
    private WebDriverWait wait;

    @Before
    public void setup(Scenario scenario){

        String browser = getPropertyValue("browser");
        switch (browser) {
            case "chrome":
                driver = WebDriverManager.chromedriver().create();
                break;

            case "firefox":
                driver = WebDriverManager.firefoxdriver().create();
                break;

            default:
                throw new RuntimeException("Browser is not supported");
        }

        scenario.log("Scenario executed on "+browser+" browser");
        this.scenario = scenario;
    }

    @After
    public void afterScenario(Scenario scenario) throws IOException {

        if(scenario.isFailed()){
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            byte[] fileContent = FileUtils.readFileToByteArray(screenshot);
            scenario.attach(fileContent, "image/png", "Failed step screenshot");
        }

        driver.quit();
    }

    @Given("I have opened the system")
    public void iHaveOpenedTheSystem() {
        driver.get(getPropertyValue("baseURL"));
    }

    @When("I set the browser to full screen")
    public void iSetTheBrowserToFullScreen() {
        driver.manage().window().fullscreen();
    }

    @When("I type {string} to the {string}")
    public void iTypeToThe(String text, String locator) {
        By element = constructElement(findElementRepo(locator));
        String value = getDatasetValue(text);
        scenario.log(text+" : "+value);
        driver.findElement(element).sendKeys(value);

        System.out.println("Test");
    }

    @When("I upload an image by clicking {String}")
    public void uploadAnImage(String locator) throws InterruptedException, AWTException {
        By element = constructElement(findElementRepo(locator));
        driver.findElement(element).click();

        Thread.sleep(3000);

        StringSelection selection = new StringSelection("C:\\Users\\Sachintha\\Downloads\\sample.jpg");

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    @When("I check for success message {string} on the popup with locator {string}")
    public void iCheckForSuccessMessageOnThePopup(String expectedMessage, String locator) {
        By element = By.xpath(locator); // assuming locator is provided as XPath; adjust if needed
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(element));

        String actualMessage = popup.getText();
        if (actualMessage.contains(expectedMessage)) {
            System.out.println("Success message found: " + actualMessage);
        } else {
            throw new AssertionError("Expected message '" + expectedMessage + "' not found in actual message: " + actualMessage);
        }
    }

    @And("I click on {string}")
    public void iClickOn(String locator) {
        By element = constructElement(findElementRepo(locator));
        driver.findElement(element).click();
    }

    @And("I see the exact {string} displays on {string}")
    public void iSeeTheExactDisplaysOn(String text, String locator) {
        By element = constructElement(findElementRepo(locator));
        String value = getDatasetValue(text);
        scenario.log(text+" : "+value);
        assertEquals("Assertion failed text did not match", value, driver.findElement(element).getText());
    }

    @And("I wait few seconds")
    public void iWaitFewSeconds() throws Exception {
        int retry = 5;
        boolean is_Passed = false;
        Exception exception = null;
        while (retry>0 && !is_Passed){
            try {
                Thread.sleep(5000);
                is_Passed = true;
                break;
            }
            catch (Exception e){
                exception = e;
                retry --;
                Thread.sleep(3000);
                is_Passed=false;
            }
        }
        if (!is_Passed){
            throw new Exception(exception);
        }

    }

}
