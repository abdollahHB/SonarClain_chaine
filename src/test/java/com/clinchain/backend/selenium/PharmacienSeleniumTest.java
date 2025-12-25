package com.clinchain.backend.selenium;

import com.clinchain.backend.selenium.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PharmacienSeleniumTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
    }

    @Test
    void testPharmacienLogin() {

        loginPage.navigateTo("http://localhost:64059");

        loginPage.login("pharmacien", "password");

        assertTrue(
                loginPage.isLoginSuccessful(),
                "Le pharmacien doit être connecté avec succès"
        );
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
