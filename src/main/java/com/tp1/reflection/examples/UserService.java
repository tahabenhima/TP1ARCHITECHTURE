package com.tp1.reflection.examples;

import com.tp1.reflection.annotations.Component;
import com.tp1.reflection.annotations.Inject;

/**
 * Service utilisateur qui utilise l'injection par constructeur.
 * Démontre l'utilisation de @Inject sur un constructeur.
 */
@Component
public class UserService {
    
    private final LoggerService loggerService;
    private final MessageService messageService;
    
    /**
     * Constructeur avec injection de dépendances.
     * Le conteneur IoC détectera ce constructeur et injectera automatiquement
     * les dépendances nécessaires.
     */
    @Inject
    public UserService(LoggerService loggerService, MessageService messageService) {
        this.loggerService = loggerService;
        this.messageService = messageService;
        loggerService.log("UserService initialisé avec injection par constructeur");
    }
    
    /**
     * Crée un nouvel utilisateur.
     */
    public void createUser(String username) {
        loggerService.log("Création de l'utilisateur: " + username);
        messageService.sendMessage("Bienvenue " + username + "!");
    }
}
