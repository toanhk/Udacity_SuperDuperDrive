package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;
    WebDriverWait webDriverWait;

    String username = "Test";
    String password = "123";
    String title = "Note 1";
    String description = "This is the note 1";

    static int number = 0;
    HomePage homePage;

    public void signupAndLogin() {
        // access signup page
        driver.get("http://localhost:" + this.port + "/signup");
        number++;
        username = username + number;


        SignupPage signupPage = new SignupPage(driver);
        signupPage.userRegistration("test", "test", username, password);

        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("Login", driver.getTitle());
        username = "Test1";
        password = "123";
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
    }

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, 2);

        signupAndLogin();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createCredential() {
        String url = "http://localhost:8080/login";

        homePage = new HomePage(driver);

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addCredentialBtn));
        homePage.addCredentialBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        homePage.addCredentialData(url, username, password);

        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialEditBtns.get(0)));
        Assertions.assertNotEquals(password, homePage.credentialPasswords.get(0).getText());

        homePage.credentialEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        Assertions.assertEquals(url, homePage.credentialUrl.getAttribute("value"));
        Assertions.assertEquals(username, homePage.credentialUsername.getAttribute("value"));
        Assertions.assertEquals(password, homePage.credentialPassword.getAttribute("value"));
        homePage.closeCredentialModal.click();

        //delete after create
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialDeleteBtns.get(0)));
        homePage.credentialDeleteBtns.get(0).click();

        resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();
    }

    @Test
    public void editCredential() throws InterruptedException {
        //createCredential();

        String url = "http://localhost:8080/login";

        homePage = new HomePage(driver);

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addCredentialBtn));
        homePage.addCredentialBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        homePage.addCredentialData(url, username, password);

        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialEditBtns.get(0)));
        homePage.credentialEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        homePage.addCredentialData(url, username, password);

        resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialEditBtns.get(0)));
        Assertions.assertNotEquals(password, homePage.credentialPasswords.get(0).getText());

        homePage.credentialEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        Assertions.assertEquals(url, homePage.credentialUrl.getAttribute("value"));
        Assertions.assertEquals(username, homePage.credentialUsername.getAttribute("value"));
        Assertions.assertEquals(password, homePage.credentialPassword.getAttribute("value"));
        homePage.closeCredentialModal.click();

        //delete after create
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialDeleteBtns.get(0)));
        homePage.credentialDeleteBtns.get(0).click();

        resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();
    }

    @Test
    public void deleteCredential() throws InterruptedException {
        String url = "http://localhost:8080/login";

        homePage = new HomePage(driver);

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addCredentialBtn));
        homePage.addCredentialBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        homePage.addCredentialData(url, username, password);

        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();

        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialDeleteBtns.get(0)));
        homePage.credentialDeleteBtns.get(0).click();

        resultPage = new ResultPage(driver);
        resultPage.clickSuccessBack();

        homePage.credentialTab.click();
        Assertions.assertTrue(homePage.credentialUrls.isEmpty());
        Assertions.assertTrue(homePage.credentialUsernames.isEmpty());
        Assertions.assertTrue(homePage.credentialPasswords.isEmpty());
    }
}
