package com.tp1.reflection.examples;

import com.tp1.reflection.annotations.Component;
import com.tp1.reflection.annotations.Inject;

/**
 * Service de notification qui utilise l'injection de dépendances.
 * Les dépendances sont injectées automatiquement via les champs annotés @Inject.
 */
@Component
public class NotificationService {
    
    @Inject
    private MessageService messageService;
    
    @Inject
    private LoggerService loggerService;
    
    /**
     * Envoie une notification en utilisant les services injectés.
     */
    public void sendNotification(String recipient, String message) {
        loggerService.log("Préparation de l'envoi à " + recipient);
        messageService.sendMessage("À: " + recipient + " - " + message);
        loggerService.log("Notification envoyée avec succès");
    }
}
