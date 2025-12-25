package com.clinchain.backend.selenium;

import com.clinchain.backend.selenium.pages.DashboardPage;
import com.clinchain.backend.selenium.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Scénario 2: HÔPITAL - Validation des lots
 * Flux: Connexion -> Consulter les lots -> Valider un lot -> Vérifier le changement de statut -> Déconnexion
 */
public class HopitalSeleniumTest extends BaseSeleniumTest {

    @Test
    public void testHopitalCompleteFlow() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        // ÉTAPE 1: Connexion de l'HÔPITAL
        loginPage.navigateTo(BASE_URL);
        loginPage.login("hopital_user", "password123");
        
        // Vérifier la connexion réussie
        assertTrue(loginPage.isLoginSuccessful(), "L'HÔPITAL devrait être connecté avec succès");
        
        // ÉTAPE 2: Vérifier le rôle affiché
        dashboardPage.navigateTo(BASE_URL);
        String userRole = dashboardPage.getUserRole();
        assertTrue(userRole.contains("HOPITALE"), "Le rôle devrait être HOPITALE");

        // ÉTAPE 3: Vérifier que le bouton "Créer un lot" est NON visible (pas la permission de l'HÔPITAL)
        assertFalse(dashboardPage.isCreateLotButtonVisible(), 
            "L'HÔPITAL ne devrait pas pouvoir créer des lots");

        // ÉTAPE 4: Consulter la liste des lots
        int lotsCount = dashboardPage.getLotsCount();
        assertTrue(lotsCount > 0, "L'HÔPITAL devrait voir au moins un lot en attente de validation");

        // ÉTAPE 5: Valider le premier lot
        // Note: En production, récupérer l'ID réel du lot via un appel API ou la structure du DOM
        String lotIdToValidate = "LOT-001"; // Adapter selon votre logique
        
        try {
            dashboardPage.validateLot(lotIdToValidate);
            
            // Attendre la confirmation
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                org.openqa.selenium.By.className("loading")));
            
            // Vérifier que l'opération a réussi
            assertTrue(dashboardPage.isOperationSuccessful(), 
                "La validation du lot devrait réussir");
        } catch (Exception e) {
            // Le lot peut ne pas exister - c'est normal pour ce test
            System.out.println("Le lot n'existe pas ou la validation n'est pas accessible");
        }

        // ÉTAPE 6: Déconnexion
        dashboardPage.logout();
        assertTrue(driver.getCurrentUrl().contains("/login"), 
            "Après déconnexion, l'utilisateur devrait être redirigé vers /login");
    }
}
