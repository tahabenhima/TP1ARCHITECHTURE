package com.tp1.reflection.examples;

import com.tp1.reflection.annotations.Component;

/**
 * Implémentation du service de journalisation.
 * Annotée avec @Component pour être gérée par le conteneur IoC.
 */
@Component
public class ConsoleLogger implements LoggerService {
    
    @Override
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
