package com.tp1.reflection.container;

import com.tp1.reflection.annotations.Component;
import com.tp1.reflection.annotations.Inject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * Conteneur IoC (Inversion of Control) qui utilise la réflexion Java
 * pour gérer l'injection de dépendances.
 * 
 * Ce conteneur scanne les classes annotées avec @Component et gère
 * automatiquement leur instanciation et l'injection de leurs dépendances.
 */
public class IoCContainer {
    
    // Map pour stocker les instances singleton des composants
    private final Map<Class<?>, Object> instances = new HashMap<>();
    
    // Map pour stocker les types et leurs implémentations
    private final Map<Class<?>, Class<?>> implementations = new HashMap<>();
    
    /**
     * Enregistre manuellement une implémentation pour une interface ou classe abstraite.
     */
    public <T> void register(Class<T> interfaceType, Class<? extends T> implementationType) {
        implementations.put(interfaceType, implementationType);
    }
    
    /**
     * Enregistre manuellement une instance existante.
     */
    public <T> void registerInstance(Class<T> type, T instance) {
        instances.put(type, instance);
    }
    
    /**
     * Scanne un package pour trouver toutes les classes annotées avec @Component.
     * Utilise la réflexion pour découvrir les classes dynamiquement.
     */
    public void scanPackage(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        
        List<File> directories = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            directories.add(new File(resource.getFile()));
        }
        
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : directories) {
            classes.addAll(findClasses(directory, packageName));
        }
        
        // Enregistrer les classes annotées avec @Component
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                // Enregistrer la classe elle-même
                implementations.put(clazz, clazz);
                
                // Enregistrer aussi pour toutes les interfaces qu'elle implémente
                for (Class<?> interfaceType : clazz.getInterfaces()) {
                    if (!implementations.containsKey(interfaceType)) {
                        implementations.put(interfaceType, clazz);
                    }
                }
            }
        }
    }
    
    /**
     * Trouve récursivement toutes les classes dans un répertoire.
     */
    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException | NoClassDefFoundError e) {
                        // Ignorer les classes qui ne peuvent pas être chargées
                    }
                }
            }
        }
        return classes;
    }
    
    /**
     * Récupère ou crée une instance du type demandé.
     * Utilise la réflexion pour instancier et injecter les dépendances.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> type) throws Exception {
        // Si l'instance existe déjà, la retourner
        if (instances.containsKey(type)) {
            return (T) instances.get(type);
        }
        
        // Trouver la classe d'implémentation
        Class<?> implementationClass = implementations.getOrDefault(type, type);
        
        // Vérifier si une instance de l'implémentation existe
        if (instances.containsKey(implementationClass)) {
            T instance = (T) instances.get(implementationClass);
            instances.put(type, instance);
            return instance;
        }
        
        // Créer une nouvelle instance
        T instance = createInstance(implementationClass);
        
        // Stocker l'instance
        instances.put(type, instance);
        instances.put(implementationClass, instance);
        
        return instance;
    }
    
    /**
     * Crée une instance en utilisant la réflexion et injecte ses dépendances.
     */
    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<?> clazz) throws Exception {
        T instance;
        
        // Chercher un constructeur annoté avec @Inject
        Constructor<?> injectConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                injectConstructor = constructor;
                break;
            }
        }
        
        if (injectConstructor != null) {
            // Utiliser le constructeur avec injection
            Class<?>[] parameterTypes = injectConstructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = getInstance(parameterTypes[i]);
            }
            
            injectConstructor.setAccessible(true);
            instance = (T) injectConstructor.newInstance(parameters);
        } else {
            // Utiliser le constructeur par défaut
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            instance = (T) defaultConstructor.newInstance();
        }
        
        // Injecter les dépendances dans les champs annotés avec @Inject
        injectFields(instance, clazz);
        
        return instance;
    }
    
    /**
     * Injecte les dépendances dans les champs annotés avec @Inject.
     * Utilise la réflexion pour accéder et modifier les champs privés.
     */
    private void injectFields(Object instance, Class<?> clazz) throws Exception {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object dependency = getInstance(field.getType());
                field.set(instance, dependency);
            }
        }
        
        // Injecter aussi les champs de la classe parente
        if (clazz.getSuperclass() != null) {
            injectFields(instance, clazz.getSuperclass());
        }
    }
    
    /**
     * Retourne toutes les instances gérées par le conteneur.
     */
    public Map<Class<?>, Object> getAllInstances() {
        return new HashMap<>(instances);
    }
}
