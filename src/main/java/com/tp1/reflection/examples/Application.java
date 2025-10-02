package com.tp1.reflection.examples;

import com.tp1.reflection.container.IoCContainer;

/**
 * Application principale qui démontre l'utilisation de la réflexion
 * et de l'injection de dépendances.
 */
public class Application {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Démonstration de la Programmation Dynamique en Java ===\n");
            
            // Créer le conteneur IoC
            IoCContainer container = new IoCContainer();
            
            // Scanner le package pour découvrir les composants
            System.out.println("1. Scanning des composants avec la réflexion...");
            container.scanPackage("com.tp1.reflection.examples");
            System.out.println("   Composants découverts et enregistrés.\n");
            
            // Exemple 1: Injection par champ
            System.out.println("2. Exemple d'injection par champ:");
            NotificationService notificationService = container.getInstance(NotificationService.class);
            notificationService.sendNotification("user@example.com", "Votre compte a été créé");
            System.out.println();
            
            // Exemple 2: Injection par constructeur
            System.out.println("3. Exemple d'injection par constructeur:");
            UserService userService = container.getInstance(UserService.class);
            userService.createUser("JohnDoe");
            System.out.println();
            
            // Exemple 3: Changement d'implémentation dynamique
            System.out.println("4. Changement d'implémentation avec la réflexion:");
            container.register(MessageService.class, SMSService.class);
            
            // Créer un nouveau conteneur pour voir le changement
            IoCContainer container2 = new IoCContainer();
            container2.scanPackage("com.tp1.reflection.examples");
            container2.register(MessageService.class, SMSService.class);
            
            NotificationService notificationService2 = container2.getInstance(NotificationService.class);
            notificationService2.sendNotification("+33612345678", "Notification par SMS");
            System.out.println();
            
            // Démonstration de la réflexion
            System.out.println("5. Informations sur les classes via la réflexion:");
            demonstrateReflection(NotificationService.class);
            
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Démontre l'utilisation de l'API de réflexion pour inspecter une classe.
     */
    private static void demonstrateReflection(Class<?> clazz) {
        System.out.println("   Classe: " + clazz.getName());
        System.out.println("   Package: " + clazz.getPackage().getName());
        
        System.out.println("   Champs:");
        for (var field : clazz.getDeclaredFields()) {
            System.out.println("      - " + field.getType().getSimpleName() + " " + field.getName());
        }
        
        System.out.println("   Méthodes:");
        for (var method : clazz.getDeclaredMethods()) {
            System.out.println("      - " + method.getName() + "()");
        }
        
        System.out.println("   Annotations:");
        for (var annotation : clazz.getAnnotations()) {
            System.out.println("      - @" + annotation.annotationType().getSimpleName());
        }
    }
}
