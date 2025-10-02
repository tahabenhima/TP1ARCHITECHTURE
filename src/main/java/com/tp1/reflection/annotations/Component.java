package com.tp1.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour marquer une classe comme composant géré par le conteneur IoC.
 * Les classes annotées avec @Component seront automatiquement instanciées
 * et gérées par le conteneur de dépendances.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    /**
     * Nom optionnel du composant pour l'identification.
     */
    String value() default "";
}
