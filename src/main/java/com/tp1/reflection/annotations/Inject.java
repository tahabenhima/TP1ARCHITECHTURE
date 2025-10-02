package com.tp1.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour marquer un champ qui doit être injecté automatiquement
 * par le conteneur d'injection de dépendances.
 * Utilise la réflexion pour identifier et injecter les dépendances.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Inject {
}
