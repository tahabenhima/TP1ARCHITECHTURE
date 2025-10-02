package com.tp1.reflection.examples;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Classe utilitaire démontrant diverses capacités de la réflexion Java.
 * Inclut des méthodes pour inspecter les classes, méthodes, champs, etc.
 */
public class ReflectionUtils {
    
    /**
     * Affiche des informations détaillées sur une classe.
     */
    public static void printClassInfo(Class<?> clazz) {
        System.out.println("=== Informations sur la classe: " + clazz.getSimpleName() + " ===");
        
        // Informations de base
        System.out.println("Nom complet: " + clazz.getName());
        System.out.println("Package: " + clazz.getPackage().getName());
        System.out.println("Modificateurs: " + Modifier.toString(clazz.getModifiers()));
        
        // Classe parente
        if (clazz.getSuperclass() != null) {
            System.out.println("Classe parente: " + clazz.getSuperclass().getSimpleName());
        }
        
        // Interfaces
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("Interfaces implémentées:");
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getSimpleName());
            }
        }
        
        // Annotations
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations.length > 0) {
            System.out.println("Annotations:");
            for (Annotation annotation : annotations) {
                System.out.println("  - @" + annotation.annotationType().getSimpleName());
            }
        }
        
        // Champs
        System.out.println("Champs:");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("  - " + Modifier.toString(field.getModifiers()) + " " 
                + field.getType().getSimpleName() + " " + field.getName());
        }
        
        // Constructeurs
        System.out.println("Constructeurs:");
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.print("  - " + Modifier.toString(constructor.getModifiers()) + " " 
                + clazz.getSimpleName() + "(");
            Class<?>[] paramTypes = constructor.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                System.out.print(paramTypes[i].getSimpleName());
                if (i < paramTypes.length - 1) System.out.print(", ");
            }
            System.out.println(")");
        }
        
        // Méthodes
        System.out.println("Méthodes:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.print("  - " + Modifier.toString(method.getModifiers()) + " " 
                + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
            Class<?>[] paramTypes = method.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                System.out.print(paramTypes[i].getSimpleName());
                if (i < paramTypes.length - 1) System.out.print(", ");
            }
            System.out.println(")");
        }
        
        System.out.println();
    }
    
    /**
     * Invoque une méthode par réflexion.
     */
    public static Object invokeMethod(Object obj, String methodName, Object... args) 
            throws Exception {
        Class<?> clazz = obj.getClass();
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        
        Method method = clazz.getMethod(methodName, paramTypes);
        return method.invoke(obj, args);
    }
    
    /**
     * Lit la valeur d'un champ privé par réflexion.
     */
    public static Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
    
    /**
     * Modifie la valeur d'un champ privé par réflexion.
     */
    public static void setPrivateField(Object obj, String fieldName, Object value) 
            throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    /**
     * Crée une instance d'une classe par réflexion.
     */
    public static Object createInstance(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor().newInstance();
    }
    
    /**
     * Vérifie si une classe a une annotation spécifique.
     */
    public static boolean hasAnnotation(Class<?> clazz, 
            Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }
    
    /**
     * Trouve toutes les méthodes avec une annotation spécifique.
     */
    public static Method[] findMethodsWithAnnotation(Class<?> clazz, 
            Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(annotationClass))
            .toArray(Method[]::new);
    }
    
    /**
     * Trouve tous les champs avec une annotation spécifique.
     */
    public static Field[] findFieldsWithAnnotation(Class<?> clazz, 
            Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(annotationClass))
            .toArray(Field[]::new);
    }
    
    /**
     * Exemple d'utilisation des utilitaires de réflexion.
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== Démonstration des Utilitaires de Réflexion ===\n");
            
            // Afficher des informations sur une classe
            printClassInfo(NotificationService.class);
            
            // Créer une instance dynamiquement
            System.out.println("Création dynamique d'une instance:");
            Object emailService = createInstance("com.tp1.reflection.examples.EmailService");
            System.out.println("Instance créée: " + emailService.getClass().getSimpleName());
            System.out.println();
            
            // Invoquer une méthode par réflexion
            System.out.println("Invocation de méthode par réflexion:");
            invokeMethod(emailService, "sendMessage", "Message envoyé via réflexion!");
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
