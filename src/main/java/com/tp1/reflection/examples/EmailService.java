package com.tp1.reflection.examples;

import com.tp1.reflection.annotations.Component;

/**
 * Implémentation concrète du service de messagerie par email.
 * Annotée avec @Component pour être gérée par le conteneur IoC.
 */
@Component
public class EmailService implements MessageService {
    
    @Override
    public void sendMessage(String message) {
        System.out.println("[EMAIL] Envoi du message: " + message);
    }
}
