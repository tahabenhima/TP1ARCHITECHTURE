# Architecture du Projet TP1 - Diagrammes

## Vue d'Ensemble du Système

```
┌─────────────────────────────────────────────────────────────────┐
│                    Application Principale                        │
│                      (Application.java)                          │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 │ utilise
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Conteneur IoC                                │
│                  (IoCContainer.java)                             │
│                                                                   │
│  Fonctionnalités:                                                │
│  • scanPackage()      - Découverte des composants                │
│  • getInstance()      - Obtention d'instances                    │
│  • register()         - Enregistrement manuel                    │
│  • injectFields()     - Injection par champ                      │
│  • createInstance()   - Création avec réflexion                  │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 │ scanne et instancie
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Composants Annotés                          │
│                                                                   │
│  @Component EmailService                                         │
│  @Component SMSService                                           │
│  @Component ConsoleLogger                                        │
│  @Component NotificationService                                  │
│  @Component UserService                                          │
└─────────────────────────────────────────────────────────────────┘
```

## Flux d'Injection de Dépendances

```
1. Scanning
   ┌──────────────┐
   │ scanPackage()│
   └──────┬───────┘
          │
          ▼
   Trouve classes avec @Component
          │
          ▼
   Enregistre dans Map<Class, Class>

2. Résolution
   ┌──────────────┐
   │getInstance() │
   └──────┬───────┘
          │
          ▼
   Vérifie si instance existe
          │
          ├─→ OUI: retourne instance
          │
          ▼ NON
   Créer nouvelle instance
          │
          ▼
   Injecter les dépendances
          │
          ▼
   Stocker dans Map<Class, Object>
          │
          ▼
   Retourner instance
```

## Exemple: NotificationService

```
┌──────────────────────────────────────────┐
│     @Component                            │
│     NotificationService                   │
│                                           │
│     @Inject                               │
│     private MessageService messageService │
│                                           │
│     @Inject                               │
│     private LoggerService loggerService   │
│                                           │
│     sendNotification(...)                 │
└─────────────┬────────────────────────────┘
              │
              │ dépend de
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌─────────┐      ┌─────────────┐
│MessageSe│      │LoggerService│
│rvice    │      │             │
│(interfa)│      │ (interface) │
└────┬────┘      └──────┬──────┘
     │                  │
     │ implémenté par   │ implémenté par
     │                  │
┌────┴────┬────────┐    │
│         │        │    │
▼         ▼        │    ▼
@Component         │  @Component
EmailSe   SMSSe    │  ConsoleLo
rvice     rvice    │  gger
                   │
```

## Cycle de Vie d'un Composant

```
1. Déclaration
   ↓
   @Component
   public class MyService { ... }

2. Découverte (Scanning)
   ↓
   container.scanPackage("com.tp1.reflection.examples")
   → Trouve MyService via réflexion
   → Enregistre dans la map d'implémentations

3. Demande d'Instance
   ↓
   MyService service = container.getInstance(MyService.class)
   
4. Vérification
   ↓
   Instance existe déjà ?
   └─→ OUI: retourner instance existante (Singleton)
   └─→ NON: continuer

5. Création
   ↓
   • Obtenir le constructeur
   • Résoudre les dépendances du constructeur
   • Instancier avec Constructor.newInstance()

6. Injection des Champs
   ↓
   • Scanner les champs avec @Inject
   • Résoudre chaque dépendance
   • Injecter via Field.set()

7. Stockage
   ↓
   • Stocker dans Map<Class, Object>
   • Pattern Singleton: même instance pour tous

8. Retour
   ↓
   return instance;
```

## Architecture en Couches

```
┌─────────────────────────────────────────────┐
│         Couche Application                   │
│  • Application.java                          │
│  • ReflectionUtils.java                      │
│  • IoCContainerTest.java                     │
└────────────────┬────────────────────────────┘
                 │
┌────────────────┴────────────────────────────┐
│         Couche Services (Exemples)           │
│  • NotificationService                       │
│  • UserService                               │
│  • EmailService / SMSService                 │
│  • ConsoleLogger                             │
└────────────────┬────────────────────────────┘
                 │
┌────────────────┴────────────────────────────┐
│         Couche Conteneur IoC                 │
│  • IoCContainer                              │
│    - Scanning                                │
│    - Résolution de dépendances               │
│    - Instanciation                           │
│    - Injection                               │
└────────────────┬────────────────────────────┘
                 │
┌────────────────┴────────────────────────────┐
│         Couche Annotations                   │
│  • @Component                                │
│  • @Inject                                   │
└─────────────────────────────────────────────┘
```

## Concepts de Réflexion Utilisés

```
┌────────────────────────────────────────────┐
│            API de Réflexion Java            │
├────────────────────────────────────────────┤
│ Class.forName()                             │
│   └─→ Chargement dynamique de classes      │
│                                             │
│ Class.getDeclaredFields()                   │
│   └─→ Obtenir les champs d'une classe      │
│                                             │
│ Field.setAccessible(true)                   │
│   └─→ Accéder aux membres privés           │
│                                             │
│ Field.set(object, value)                    │
│   └─→ Modifier la valeur d'un champ        │
│                                             │
│ Constructor.newInstance(args...)            │
│   └─→ Créer une instance dynamiquement     │
│                                             │
│ Method.invoke(object, args...)              │
│   └─→ Invoquer une méthode                 │
│                                             │
│ Class.isAnnotationPresent()                 │
│   └─→ Vérifier présence d'annotation       │
│                                             │
│ Class.getAnnotation()                       │
│   └─→ Récupérer une annotation             │
└────────────────────────────────────────────┘
```

## Patterns de Conception

```
┌─────────────────────────────────────────────┐
│ 1. Inversion of Control (IoC)               │
│    ┌─────────┐      ┌──────────┐           │
│    │Container│──→   │Components│           │
│    │contrôle │      │          │           │
│    └─────────┘      └──────────┘           │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ 2. Dependency Injection (DI)                │
│    ┌─────────┐      ┌──────────┐           │
│    │ Service │◄─────│Dependency│           │
│    │         │ inject│          │           │
│    └─────────┘      └──────────┘           │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ 3. Singleton                                │
│    ┌──────────┐                             │
│    │Container │                             │
│    │          │                             │
│    │ [Map]    │                             │
│    │ Service1 → instance1 (réutilisée)     │
│    │ Service2 → instance2 (réutilisée)     │
│    └──────────┘                             │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ 4. Factory Pattern                          │
│    ┌──────────┐      ┌──────────┐          │
│    │Container │ crée │ Objets   │          │
│    │(Factory) │─────→│          │          │
│    └──────────┘      └──────────┘          │
└─────────────────────────────────────────────┘
```

## Exemple Concret d'Exécution

```
Étape 1: Initialisation
────────────────────────
IoCContainer container = new IoCContainer();

Étape 2: Scanning
────────────────────────
container.scanPackage("com.tp1.reflection.examples");
    │
    ├─→ Trouve: @Component EmailService
    ├─→ Trouve: @Component SMSService  
    ├─→ Trouve: @Component ConsoleLogger
    ├─→ Trouve: @Component NotificationService
    └─→ Trouve: @Component UserService

Étape 3: Demande d'Instance
────────────────────────
NotificationService service = 
    container.getInstance(NotificationService.class);
    │
    ├─→ Créer NotificationService
    │   │
    │   ├─→ Détecter champ: @Inject MessageService
    │   │   └─→ Résoudre: getInstance(MessageService.class)
    │   │       └─→ Créer EmailService (première impl trouvée)
    │   │
    │   └─→ Détecter champ: @Inject LoggerService
    │       └─→ Résoudre: getInstance(LoggerService.class)
    │           └─→ Créer ConsoleLogger
    │
    └─→ Retourner NotificationService (avec dépendances injectées)

Étape 4: Utilisation
────────────────────────
service.sendNotification("user@example.com", "Hello!");
    │
    ├─→ loggerService.log("Préparation...")
    │   └─→ [LOG] Préparation de l'envoi à user@example.com
    │
    ├─→ messageService.sendMessage("...")
    │   └─→ [EMAIL] Envoi du message: À: user@example.com - Hello!
    │
    └─→ loggerService.log("Notification envoyée...")
        └─→ [LOG] Notification envoyée avec succès
```
