package com.clinchain.backend.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object pour le dashboard/gestion des lots
 */
public class DashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locateurs
    private By logoutButtonLocator = By.xpath("//button[contains(text(), 'Déconnexion')]");
    private By userRoleDisplayLocator = By.className("user-role");
    private By createLotButtonLocator = By.xpath("//button[contains(text(), 'Créer un lot')]");
    private By lotsTableLocator = By.id("lots-table");
    private By validateButtonLocator = By.xpath("//button[contains(text(), 'Valider')]");
    private By withdrawButtonLocator = By.xpath("//button[contains(text(), 'Retirer')]");
    private By stockButtonLocator = By.xpath("//button[contains(text(), 'En stock')]");
    private By administerButtonLocator = By.xpath("//button[contains(text(), 'Administrer')]");
    private By successNotificationLocator = By.className("success-notification");
    private By errorNotificationLocator = By.className("error-notification");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String baseUrl) {
        driver.navigate().to(baseUrl + "/dashboard");
    }

    public void logout() {
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(logoutButtonLocator));
        logoutButton.click();
    }

    public String getUserRole() {
        WebElement roleElement = driver.findElement(userRoleDisplayLocator);
        return roleElement.getText();
    }

    public boolean isCreateLotButtonVisible() {
        try {
            return driver.findElement(createLotButtonLocator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCreateLot() {
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(createLotButtonLocator));
        createButton.click();
    }

    public void fillCreateLotForm(String medName, String quantity, String expiryDate) {
        WebElement medNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[aria-label='lot-create-name']")));
        WebElement quantityInput = driver.findElement(By.cssSelector("input[aria-label='lot-create-qty']"));
        // Expiry date is not in the form in Flutter code? Let's check CreateLotScreen
        // logic.
        // Looking at CreateLotScreen.dart, it ONLY has Name and Quantity. NO Expiry
        // Date.
        // So we should remove expiryInput logic or it will fail.

        medNameInput.sendKeys(medName);
        quantityInput.sendKeys(quantity);

        // Expiry date ignored as it's not in the Flutter form currently.

        WebElement submitButton = driver.findElement(By.cssSelector("button[aria-label='lot-create-submit']"));
        submitButton.click();
    }

    public boolean isLotCreatedSuccessfully() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(successNotificationLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void validateLot(String lotId) {
        // Chercher le bouton valider pour ce lot
        WebElement validateButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[@data-lot-id='" + lotId + "']//button[contains(text(), 'Valider')]")));
        validateButton.click();

        // Remplir le formulaire de validation si nécessaire
        try {
            WebElement validationNotes = driver.findElement(By.id("validationNotes"));
            validationNotes.sendKeys("Validé par le système");
            WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
            submitButton.click();
        } catch (Exception e) {
            // Pas de formulaire de validation
        }
    }

    public void withdrawFromLot(String lotId, String quantity) {
        WebElement withdrawButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[@data-lot-id='" + lotId + "']//button[contains(text(), 'Retirer')]")));
        withdrawButton.click();

        WebElement quantityInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("withdrawQuantity")));
        quantityInput.sendKeys(quantity);

        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
    }

    public void markInStock(String lotId) {
        WebElement stockButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[@data-lot-id='" + lotId + "']//button[contains(text(), 'En stock')]")));
        stockButton.click();

        try {
            WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
            submitButton.click();
        } catch (Exception e) {
            // Pas de formulaire supplémentaire
        }
    }

    public void administerMedication(String lotId) {
        WebElement adminButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[@data-lot-id='" + lotId + "']//button[contains(text(), 'Administrer')]")));
        adminButton.click();

        WebElement patientIdInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("patientId")));
        patientIdInput.sendKeys("PAT-001");

        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
    }

    public boolean isOperationSuccessful() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(successNotificationLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOperationFailed() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(errorNotificationLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getLotsCount() {
        try {
            // Count cards with aria-label 'lot-item'
            List<WebElement> rows = driver.findElements(By.cssSelector("*[aria-label='lot-item']"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
