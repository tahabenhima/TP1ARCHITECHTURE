package com.tp1.reflection;

import com.tp1.reflection.annotations.Component;
import com.tp1.reflection.annotations.Inject;
import com.tp1.reflection.container.IoCContainer;
import com.tp1.reflection.examples.*;

/**
 * Tests pour valider le conteneur IoC et l'injection de dépendances.
 * Tests manuels simples sans framework de test.
 */
public class IoCContainerTest {
    
    public static void main(String[] args) {
        System.out.println("=== Tests du Conteneur IoC ===\n");
        
        int passed = 0;
        int total = 0;
        
        // Test 1: Création d'instance simple
        total++;
        try {
            IoCContainer container = new IoCContainer();
            container.scanPackage("com.tp1.reflection.examples");
            
            EmailService emailService = container.getInstance(EmailService.class);
            if (emailService != null) {
                System.out.println("✓ Test 1 réussi: Création d'instance simple");
                passed++;
            } else {
                System.out.println("✗ Test 1 échoué: Instance nulle");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 1 échoué: " + e.getMessage());
        }
        System.out.println();
        
        // Test 2: Injection par champ
        total++;
        try {
            IoCContainer container = new IoCContainer();
            container.scanPackage("com.tp1.reflection.examples");
            
            NotificationService notificationService = container.getInstance(NotificationService.class);
            if (notificationService != null) {
                notificationService.sendNotification("test@test.com", "Test message");
                System.out.println("✓ Test 2 réussi: Injection par champ");
                passed++;
            } else {
                System.out.println("✗ Test 2 échoué: Instance nulle");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 2 échoué: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
        
        // Test 3: Injection par constructeur
        total++;
        try {
            IoCContainer container = new IoCContainer();
            container.scanPackage("com.tp1.reflection.examples");
            
            UserService userService = container.getInstance(UserService.class);
            if (userService != null) {
                userService.createUser("TestUser");
                System.out.println("✓ Test 3 réussi: Injection par constructeur");
                passed++;
            } else {
                System.out.println("✗ Test 3 échoué: Instance nulle");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 3 échoué: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
        
        // Test 4: Singleton pattern
        total++;
        try {
            IoCContainer container = new IoCContainer();
            container.scanPackage("com.tp1.reflection.examples");
            
            EmailService instance1 = container.getInstance(EmailService.class);
            EmailService instance2 = container.getInstance(EmailService.class);
            
            if (instance1 == instance2) {
                System.out.println("✓ Test 4 réussi: Pattern Singleton (même instance retournée)");
                passed++;
            } else {
                System.out.println("✗ Test 4 échoué: Instances différentes");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 4 échoué: " + e.getMessage());
        }
        System.out.println();
        
        // Test 5: Enregistrement manuel d'implémentation
        total++;
        try {
            IoCContainer container = new IoCContainer();
            container.register(MessageService.class, SMSService.class);
            container.scanPackage("com.tp1.reflection.examples");
            
            MessageService messageService = container.getInstance(MessageService.class);
            if (messageService instanceof SMSService) {
                System.out.println("✓ Test 5 réussi: Enregistrement manuel d'implémentation");
                passed++;
            } else {
                System.out.println("✗ Test 5 échoué: Mauvaise implémentation");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 5 échoué: " + e.getMessage());
        }
        System.out.println();
        
        // Test 6: Enregistrement d'instance
        total++;
        try {
            IoCContainer container = new IoCContainer();
            LoggerService customLogger = new ConsoleLogger();
            container.registerInstance(LoggerService.class, customLogger);
            
            LoggerService retrievedLogger = container.getInstance(LoggerService.class);
            if (retrievedLogger == customLogger) {
                System.out.println("✓ Test 6 réussi: Enregistrement d'instance existante");
                passed++;
            } else {
                System.out.println("✗ Test 6 échoué: Instance différente");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 6 échoué: " + e.getMessage());
        }
        System.out.println();
        
        // Résumé
        System.out.println("=== Résumé des tests ===");
        System.out.println("Tests réussis: " + passed + "/" + total);
        System.out.println("Taux de réussite: " + (passed * 100 / total) + "%");
        
        if (passed == total) {
            System.out.println("\n✓ Tous les tests sont passés!");
        } else {
            System.out.println("\n✗ Certains tests ont échoué.");
        }
    }
}
