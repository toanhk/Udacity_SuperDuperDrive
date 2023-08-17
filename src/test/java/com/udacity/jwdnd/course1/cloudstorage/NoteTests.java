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
public class NoteTests {
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
    @Order(1)
    public void createNote() {

        homePage = new HomePage(driver);

        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addNoteBtn));
        homePage.addNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteTitle));
        homePage.addNoteData(title, description);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the note is added
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(homePage.addNoteBtn));

        Assertions.assertEquals(title, homePage.noteTitles.get(0).getText());
        Assertions.assertEquals(description, homePage.noteDescriptions.get(0).getText());

        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteDeleteBtns.get(0)));
        homePage.noteDeleteBtns.get(0).click();
        resultPage.clickSuccessBack();
    }

    @Test
    @Order(2)
    public void editNote() {
        // create note to edit
        homePage = new HomePage(driver);
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addNoteBtn));
        homePage.addNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteTitle));
        homePage.addNoteData(title, description);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        title = "Note 1 edited";
        description = "This is the edited note";

        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteEditBtns.get(0)));
        homePage.noteEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteTitle));
        homePage.addNoteData(title, description);

        // redirect to result page with successful message
        resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the note is edited
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(homePage.addNoteBtn));
        Assertions.assertEquals(title, homePage.noteTitles.get(0).getText());
        Assertions.assertEquals(description, homePage.noteDescriptions.get(0).getText());

        //delete note
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteDeleteBtns.get(0)));
        homePage.noteDeleteBtns.get(0).click();
        resultPage.clickSuccessBack();
    }

    @Test
    @Order(3)
    public void deleteNote() {
        // create note to delete
        homePage = new HomePage(driver);
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addNoteBtn));
        homePage.addNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteTitle));
        homePage.addNoteData(title, description);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // delete note
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteDeleteBtns.get(0)));
        homePage.noteDeleteBtns.get(0).click();

        // redirect to result page with successful message
        resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the note is deleted
        homePage.noteTab.click();
        Assertions.assertTrue(homePage.noteTitles.isEmpty());
        Assertions.assertTrue(homePage.noteDescriptions.isEmpty());
    }
}
