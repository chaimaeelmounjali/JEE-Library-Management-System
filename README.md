# JEE Library Management System

Application web Java EE de gestion de bibliothèque (catalogue de livres, membres, emprunts/retours), implémentée avec **Servlets + JSP + DAO + MySQL**.

## 1) Objectif du projet

Le projet vise à gérer le cycle de prêt d’une bibliothèque :
- administrer les **livres** (ajout, modification, suppression, recherche, disponibilité),
- administrer les **membres** (inscription, modification, activation/désactivation),
- gérer les **emprunts et retours** (suivi des statuts, retards, historique).

## 2) Stack et architecture réellement utilisées

### Technologies
- **Java 21** (`.classpath`, facet Eclipse)
- **Jakarta Servlet 6.0 / JSP** (`web.xml`, facette `jst.web` 6.0)
- **JSTL** (JARs présents dans `WEB-INF/lib`)
- **MySQL** (connexion JDBC + `mysql-connector-java-8.0.29.jar`)
- **Bootstrap 5** + Bootstrap Icons (CDN dans les JSP)

### Architecture applicative
Architecture en couches :
- **Controller (Servlets)** :
  - `controller/LivreServlet.java` (`/livres/*`)
  - `controller/MembreServlet.java` (`/membres/*`)
  - `controller/EmpruntServlet.java` (`/emprunts/*`)
- **Service (règles métier)** : `service/*Service.java`
- **DAO (accès base)** : `dao/*DAO.java`, `dao/DatabaseConnection.java`
- **Model (entités)** : `model/Livre.java`, `model/Membre.java`, `model/Emprunt.java`
- **Vue JSP** : `src/main/webapp/views/**` + `layout/**`

## 3) Fonctionnalités métier observées

### Livres
- CRUD de base (liste/ajout/modification/suppression)
- recherche par titre/auteur/isbn côté service
- suivi de disponibilité (`disponible`)
- unicité ISBN vérifiée

### Membres
- inscription et mise à jour
- activation/désactivation via statut booléen
- unicité email vérifiée

### Emprunts / Retours
- création d’emprunt avec contrôles :
  - livre disponible,
  - membre actif,
  - limite de **3 emprunts en cours** par membre
- retour d’ouvrage avec statut : `retourné` ou `en retard`
- détection de retard + calcul d’amende (2 DH/jour dans le modèle/service)
- filtres d’affichage : tous / en cours / en retard / retournés

## 4) Structure du repository

```text
.
├── GestionBibliotheque1/
│   └── GestionBibliotheque1/
│       ├── src/main/java/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── dao/
│       │   └── model/
│       ├── src/main/webapp/
│       │   ├── views/
│       │   ├── layout/
│       │   ├── css/
│       │   ├── js/
│       │   └── WEB-INF/
│       └── .settings/ (facettes/runtime Eclipse)
├── base de donnee.sql
└── class-diagram.png
```

## 5) Modèle de données (SQL réel)

Script : `base de donnee.sql`

- Table **Membre**
  - `id_membre` (PK), `nom`, `prenom`, `email` (unique), `telephone`, `date_inscription`, `statut`
- Table **Livre**
  - `id_livre` (PK), `titre`, `auteur`, `isbn` (unique), `disponible`, `date_ajout`
- Table **Emprunt**
  - `id_emprunt` (PK), `id_livre` (FK), `id_membre` (FK),
  - `date_emprunt`, `date_retour_prevue`, `date_retour_effective`,
  - `statut` ENUM(`en cours`, `retourné`, `en retard`)

Le script crée aussi des index et des données d’exemple.

## 6) Rôle de l’auteur

Le dépôt est porté par **@chaimaeelmounjali** (propriétaire du repository et auteur du commit initial visible dans l’historique local), avec une implémentation complète du socle Java EE (Servlets/JSP/DAO) pour la gestion d’une bibliothèque.

## 7) Résultats et workflows disponibles

### Résultats livrés dans ce dépôt
- application web fonctionnelle par modules : **Livres / Membres / Emprunts**,
- interface JSP Bootstrap structurée,
- script SQL prêt à initialiser la base,
- diagramme de classes (`class-diagram.png`).

### Workflows fonctionnels (application)
- Workflow livre : ajouter → consulter/lister → modifier → supprimer
- Workflow membre : inscrire → lister/filtrer → modifier → changer statut
- Workflow emprunt : nouveau prêt → suivi en cours/retard → retour

### CI/CD
- Aucun workflow GitHub Actions trouvé dans ce repository (`.github/workflows` absent).

## 8) Prérequis

- **JDK 21**
- **Apache Tomcat 10.1** (runtime défini dans les facettes Eclipse)
- **MySQL** accessible en local
- IDE Java EE (Eclipse WTP recommandé vu la structure du projet)

## 9) Configuration de la base

La connexion est centralisée dans :
`GestionBibliotheque1/GestionBibliotheque1/src/main/java/dao/DatabaseConnection.java`

Valeurs actuellement codées en dur :
- URL : `jdbc:mysql://localhost:3306/gestion_bibliotheque`
- User : `root`
- Password : `123456789`

Étapes :
1. Créer/importer la base avec `base de donnee.sql`
2. Adapter si nécessaire les identifiants JDBC dans `DatabaseConnection.java`

## 10) Déploiement / exécution

> Le projet ne contient pas de `pom.xml` Maven : il est structuré en **Dynamic Web Project** Eclipse.

1. Ouvrir le projet `GestionBibliotheque1/GestionBibliotheque1` dans Eclipse
2. Configurer le runtime **Tomcat 10.1**
3. Vérifier la base MySQL + script SQL importé
4. Publier et démarrer l’application sur Tomcat
5. Accéder à l’application via le contexte : `/GestionBibliotheque1`

## 11) Limites actuelles et pistes d’amélioration

Limites observées dans l’état actuel :
- paramètres BD sensibles codés en dur (`DatabaseConnection.java`),
- dépendances gérées par JARs manuels dans `WEB-INF/lib` (pas de build Maven/Gradle),
- certaines routes pointent vers des vues non présentes (`/views/emprunt/historique.jsp`, `/views/erreur.jsp`),
- statistiques de la page d’accueil simulées côté front (valeurs fixes JavaScript),
- pas de pipeline CI/CD versionné dans `.github/workflows`.

Améliorations recommandées :
- externaliser la configuration (variables d’environnement/properties),
- migrer vers Maven pour standardiser build/dépendances,
- compléter les vues manquantes,
- brancher les statistiques sur des données réelles DAO/service,
- ajouter tests (unitaires/intégration) et workflow CI.
