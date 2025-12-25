package com.clinchain.backend.selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/login");

        // Attendre Flutter Web
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName("flt-glass-pane")
        ));
    }

    public void login(String username, String password) {

        Actions actions = new Actions(driver);

        // 1️⃣ Activer le focus global
        Dimension size = driver.manage().window().getSize();
        actions.moveByOffset(size.width / 2, size.height / 2)
                .click()
                .perform();

        sleep(300);

        // 2️⃣ TAB → champ username
        actions.sendKeys(Keys.TAB).perform();
        sleep(150);
        actions.sendKeys(username).perform();

        // 3️⃣ TAB → champ password
        actions.sendKeys(Keys.TAB).perform();
        sleep(150);
        actions.sendKeys(password).perform();

        // ✅ 4️⃣ ENTER SUR LE CHAMP PASSWORD (SUBMIT FLUTTER)
        sleep(200);
        actions.sendKeys(Keys.ENTER).perform();
    }

    public boolean isLoginSuccessful() {
        try {
            return wait.until(
                    ExpectedConditions.or(
                            ExpectedConditions.urlContains("/home"),
                            ExpectedConditions.urlContains("/dashboard")
                    )
            );
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
