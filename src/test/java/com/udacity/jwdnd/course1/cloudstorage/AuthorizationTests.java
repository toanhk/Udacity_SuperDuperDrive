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
public class AuthorizationTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;
    WebDriverWait webDriverWait;
    @BeforeAll
    static void beforeAll() {

        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, 2);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }
    @Test
    public void userSignupLoginAndLogout() {
        String username = "admin";
        String password = "1";

        driver.get("http://localhost:" + this.port + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.userRegistration("test", "test", username, password);

        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

        //wait for redirect to login page
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("Login", driver.getTitle());

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());

        // logout
        HomePage homePage = new HomePage(driver);
        homePage.logout();
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void unauthorizedUserCanOnlyAccessLoginPageAndSignupPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }
}
