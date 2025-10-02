# Guide de Démonstration - TP1 Programmation Dynamique

## Introduction

Ce guide présente les différentes démonstrations disponibles dans ce projet pour comprendre la réflexion Java et l'injection de dépendances.

## 1. Application Principale (Application.java)

**Commande:** `java -cp bin com.tp1.reflection.examples.Application`

**Démontre:**
- Scanning automatique des composants avec @Component
- Injection par champ avec @Inject
- Injection par constructeur avec @Inject
- Changement dynamique d'implémentation
- Introspection de classe via la réflexion

**Concepts clés:**
- Le conteneur IoC découvre automatiquement les classes annotées
- Les dépendances sont résolues et injectées automatiquement
- Pattern Singleton: une seule instance par type

## 2. Tests du Conteneur (IoCContainerTest.java)

**Commande:** `java -cp bin com.tp1.reflection.IoCContainerTest`

**Tests effectués:**
1. ✓ Création d'instance simple
2. ✓ Injection par champ
3. ✓ Injection par constructeur
4. ✓ Pattern Singleton
5. ✓ Enregistrement manuel d'implémentation
6. ✓ Enregistrement d'instance existante

**Taux de réussite:** 100% (6/6 tests)

## 3. Utilitaires de Réflexion (ReflectionUtils.java)

**Commande:** `java -cp bin com.tp1.reflection.examples.ReflectionUtils`

**Fonctionnalités:**
- Affichage détaillé des informations de classe
- Création d'instance dynamique
- Invocation de méthode par réflexion
- Accès et modification de champs privés
- Recherche d'annotations
- Inspection complète des métadonnées

## 4. Script de Compilation et d'Exécution (run.sh)

**Commande:** `./run.sh`

**Actions:**
1. Compile tous les fichiers sources
2. Compile tous les tests
3. Exécute la suite de tests
4. Exécute l'application de démonstration

## Architecture du Projet

```
Annotations
├── @Component - Marque une classe comme composant géré
└── @Inject - Marque un point d'injection

Conteneur IoC
├── Scanning de packages
├── Résolution de dépendances
├── Instanciation dynamique
└── Gestion du cycle de vie

Services Exemples
├── MessageService (interface)
│   ├── EmailService (implémentation)
│   └── SMSService (implémentation)
├── LoggerService (interface)
│   └── ConsoleLogger (implémentation)
├── NotificationService (injection par champ)
└── UserService (injection par constructeur)
```

## Concepts de Réflexion Démontrés

### 1. Introspection
```java
// Obtenir des informations sur une classe
Class<?> clazz = obj.getClass();
Field[] fields = clazz.getDeclaredFields();
Method[] methods = clazz.getDeclaredMethods();
```

### 2. Instanciation Dynamique
```java
// Créer une instance sans connaître le type à la compilation
Class<?> clazz = Class.forName(className);
Object instance = clazz.getDeclaredConstructor().newInstance();
```

### 3. Accès aux Membres Privés
```java
// Accéder à un champ privé
Field field = clazz.getDeclaredField(fieldName);
field.setAccessible(true);
Object value = field.get(instance);
```

### 4. Traitement des Annotations
```java
// Vérifier et traiter les annotations
if (clazz.isAnnotationPresent(Component.class)) {
    // Traiter le composant
}
```

## Patterns de Conception Utilisés

1. **Inversion of Control (IoC)**
   - Le conteneur contrôle la création et le cycle de vie des objets
   - Les objets ne créent pas leurs dépendances

2. **Dependency Injection (DI)**
   - Les dépendances sont fournies de l'extérieur
   - Découplage entre les composants

3. **Singleton**
   - Une seule instance par type dans le conteneur
   - Partage de l'instance entre tous les consommateurs

4. **Factory Pattern**
   - Le conteneur agit comme une factory pour créer des objets
   - Abstraction de la création d'objets

## Exercices Pratiques Suggérés

1. **Ajouter un nouveau service**
   - Créer une interface `DatabaseService`
   - Implémenter `MySQLService` et `PostgreSQLService`
   - Utiliser l'injection de dépendances

2. **Implémenter l'injection par setter**
   - Créer une annotation `@InjectSetter`
   - Modifier le conteneur pour supporter les setters

3. **Ajouter des scopes**
   - Implémenter `@Singleton` et `@Prototype`
   - Modifier le cycle de vie des objets

4. **Créer un système de configuration**
   - Charger la configuration depuis un fichier
   - Mapper les interfaces aux implémentations via config

5. **Ajouter des qualifiers**
   - Créer une annotation `@Named`
   - Permettre plusieurs implémentations d'une même interface

## Ressources Additionnelles

- **Java Reflection Tutorial:** https://docs.oracle.com/javase/tutorial/reflect/
- **Dependency Injection Patterns:** Martin Fowler's articles
- **Spring Framework:** Voir comment Spring implémente ces concepts
- **Google Guice:** Alternative légère pour DI

## Dépannage

### Problème: ClassNotFoundException
**Solution:** Vérifier que le classpath inclut le répertoire `bin`

### Problème: IllegalAccessException
**Solution:** Le conteneur utilise `setAccessible(true)` pour accéder aux membres privés

### Problème: NoSuchMethodException
**Solution:** Vérifier que la classe a un constructeur par défaut ou un constructeur annoté @Inject

## Conclusion

Ce projet démontre comment la réflexion Java permet de créer des frameworks flexibles et modulaires. L'injection de dépendances améliore la testabilité et la maintenabilité du code en réduisant le couplage entre les composants.
