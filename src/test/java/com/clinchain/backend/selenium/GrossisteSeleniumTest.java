package com.clinchain.backend.selenium;

import com.clinchain.backend.selenium.pages.DashboardPage;
import com.clinchain.backend.selenium.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Scénario 1: GROSSISTE - Création de lots
 * Flux: Connexion -> Créer un lot -> Voir le lot dans la liste -> Déconnexion
 */
public class GrossisteSeleniumTest extends BaseSeleniumTest {

    @Test
    public void testGrossisteCompleteFlow() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        // ÉTAPE 1: Connexion du GROSSISTE
        loginPage.navigateTo(BASE_URL);
        loginPage.login("grossiste", "password");

        // Vérifier la connexion réussie
        assertTrue(loginPage.isLoginSuccessful(), "Le GROSSISTE devrait être connecté avec succès");

        // ÉTAPE 2: Vérifier le rôle affiché
        dashboardPage.navigateTo(BASE_URL);
        String userRole = dashboardPage.getUserRole();
        assertTrue(userRole.contains("GROSSISTE"), "Le rôle devrait être GROSSISTE");

        // ÉTAPE 3: Vérifier que le bouton "Créer un lot" est visible (permission GROSSISTE)
        assertTrue(dashboardPage.isCreateLotButtonVisible(),
            "Le GROSSISTE devrait pouvoir créer des lots");

        // ÉTAPE 4: Créer un lot
        dashboardPage.clickCreateLot();
        dashboardPage.fillCreateLotForm("Aspirin", "1000", "2025-12-24");

        // Vérifier que le lot a été créé
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertTrue(dashboardPage.isLotCreatedSuccessfully(),
            "Le lot devrait être créé avec succès");

        // ÉTAPE 5: Vérifier que le lot apparaît dans la liste
        int lotsCount = dashboardPage.getLotsCount();
        assertTrue(lotsCount > 0, "Au moins un lot devrait être visible dans la liste");

        // ÉTAPE 6: Déconnexion
        dashboardPage.logout();
        assertTrue(driver.getCurrentUrl().contains("/login"),
            "Après déconnexion, l'utilisateur devrait être redirigé vers /login");
    }
}
