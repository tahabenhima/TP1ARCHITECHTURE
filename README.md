# TP1ARCHITECHTURE
TP 1 : Programmation Dynamique en Java - Réflexion et Injection de Dépendances

## Description

Ce projet démontre l'utilisation de la **programmation dynamique** (réflexion) en Java et l'implémentation d'un **conteneur d'injection de dépendances** (IoC - Inversion of Control). Il illustre comment la réflexion Java permet de créer des applications plus flexibles et modulaires.

## Concepts Clés

### 1. Réflexion Java
La réflexion permet d'examiner et de manipuler les classes, méthodes et champs à l'exécution :
- **Introspection** : examiner les métadonnées des classes
- **Instanciation dynamique** : créer des objets sans connaître leur type à la compilation
- **Accès aux membres privés** : modifier des champs privés de manière contrôlée
- **Annotations** : utiliser des métadonnées pour configurer le comportement

### 2. Injection de Dépendances (DI)
Pattern qui permet de :
- Découpler les composants de l'application
- Faciliter les tests unitaires
- Améliorer la maintenabilité
- Permettre le changement d'implémentation sans modification du code

### 3. Conteneur IoC
Le conteneur gère automatiquement :
- La découverte des composants via scanning
- L'instanciation des objets
- La résolution et l'injection des dépendances
- Le cycle de vie des objets (pattern Singleton)

## Structure du Projet

```
src/
├── main/java/com/tp1/reflection/
│   ├── annotations/
│   │   ├── Component.java      # Annotation pour marquer les composants
│   │   └── Inject.java         # Annotation pour l'injection
│   ├── container/
│   │   └── IoCContainer.java   # Conteneur IoC principal
│   └── examples/
│       ├── Application.java    # Application de démonstration
│       ├── MessageService.java # Interface de service
│       ├── EmailService.java   # Implémentation Email
│       ├── SMSService.java     # Implémentation SMS
│       ├── LoggerService.java  # Interface de logging
│       ├── ConsoleLogger.java  # Implémentation console
│       ├── NotificationService.java  # Service avec injection par champ
│       └── UserService.java    # Service avec injection par constructeur
└── test/java/com/tp1/reflection/
    └── IoCContainerTest.java   # Tests du conteneur
```

## Utilisation

### Compilation

```bash
# Compiler tous les fichiers Java
cd /home/runner/work/TP1ARCHITECHTURE/TP1ARCHITECHTURE
javac -d bin -sourcepath src/main/java src/main/java/com/tp1/reflection/**/*.java
```

### Exécution de l'Application de Démonstration

```bash
# Exécuter l'application principale
java -cp bin com.tp1.reflection.examples.Application
```

### Exécution des Tests

```bash
# Compiler les tests
javac -d bin -cp bin -sourcepath src/test/java src/test/java/com/tp1/reflection/*.java

# Exécuter les tests
java -cp bin com.tp1.reflection.IoCContainerTest
```

## Exemples d'Utilisation

### 1. Créer un Composant

```java
@Component
public class MyService implements ServiceInterface {
    // Implémentation
}
```

### 2. Injection par Champ

```java
@Component
public class MyController {
    @Inject
    private MyService myService;
    
    public void doSomething() {
        myService.performAction();
    }
}
```

### 3. Injection par Constructeur

```java
@Component
public class MyController {
    private final MyService myService;
    
    @Inject
    public MyController(MyService myService) {
        this.myService = myService;
    }
}
```

### 4. Utilisation du Conteneur

```java
// Créer et configurer le conteneur
IoCContainer container = new IoCContainer();

// Scanner un package pour découvrir les composants
container.scanPackage("com.example.myapp");

// Obtenir une instance (avec injection automatique)
MyController controller = container.getInstance(MyController.class);

// Enregistrer manuellement une implémentation
container.register(ServiceInterface.class, AlternativeService.class);
```

## Fonctionnalités du Conteneur IoC

1. **Scanning automatique** : Découverte des classes annotées avec `@Component`
2. **Injection par champ** : Injection via les champs annotés avec `@Inject`
3. **Injection par constructeur** : Injection via le constructeur annoté avec `@Inject`
4. **Résolution de dépendances** : Résolution récursive des dépendances
5. **Pattern Singleton** : Une seule instance par type dans le conteneur
6. **Enregistrement manuel** : Possibilité d'enregistrer des implémentations spécifiques
7. **Support des interfaces** : Mapping automatique interface → implémentation

## Avantages de l'Approche

### Pour le Développement
- **Flexibilité** : Changement facile d'implémentation
- **Modularité** : Composants indépendants et réutilisables
- **Testabilité** : Injection de mocks pour les tests
- **Maintenabilité** : Code plus propre et organisé

### Pour l'Architecture
- **Découplage** : Réduction des dépendances directes
- **Extensibilité** : Ajout de nouvelles fonctionnalités sans modification du code existant
- **Configuration centralisée** : Gestion des dépendances en un seul endroit

## Concepts Avancés Démontrés

1. **API de Réflexion Java**
   - `Class.forName()` : Chargement dynamique de classes
   - `Constructor.newInstance()` : Instanciation dynamique
   - `Field.set()` : Modification de champs
   - `Method.invoke()` : Invocation de méthodes

2. **Annotations Runtime**
   - `@Retention(RetentionPolicy.RUNTIME)`
   - `isAnnotationPresent()`
   - Traitement des annotations

3. **Patterns de Conception**
   - Inversion of Control (IoC)
   - Dependency Injection (DI)
   - Singleton
   - Factory Pattern

## Exercices Suggérés

1. Ajouter un support pour l'injection de dépendances par setter
2. Implémenter des scopes (Singleton, Prototype)
3. Ajouter la gestion du cycle de vie (init, destroy)
4. Créer un système de configuration via fichier
5. Implémenter l'injection de dépendances par nom
6. Ajouter le support des qualifiers pour désambiguïser les dépendances

## Références

- [Java Reflection API](https://docs.oracle.com/javase/tutorial/reflect/)
- [Dependency Injection](https://en.wikipedia.org/wiki/Dependency_injection)
- [Inversion of Control](https://en.wikipedia.org/wiki/Inversion_of_control)

## Auteur

Projet pédagogique pour comprendre la programmation dynamique et l'injection de dépendances en Java.
