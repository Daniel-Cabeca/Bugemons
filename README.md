# INFO-F-307: Bugémon (Projet 2025-2026)

## Description
Bugémon est un jeu Java/JavaFX inspiré des combats de créatures au tour par tour. Le joueur constitue une équipe de Bugémons, affronte des adversaires dans la Tour NO ou en combat individuel et peut aussi interagir avec d'autres joueurs via les fonctionnalités multijoueurs.

Le projet a été développé dans le cadre du cours **INFO-F-307 - Génie logiciel et gestion de projets**. Il met l'accent sur l'orienté objet, une architecture MVC avancée et une gestion de projet en Extreme Programming.

## Fonctionnalités principales
D'après les consignes `/team/instructions/instructions.pdf`, les fonctionnalités qui ont été implémentées dans notre jeu sont les suivantes:
- Histoire 1 : Constituer une équipe
- Histoire 2 : Sauvegarder et charger une équipe
- Histoire 4 : Combat automatique
- Histoire 5 : Contrôle des actions en combat
- Histoire 6 : Calcul de dégâts avec statistiques
- Histoire 7 : Expérience et montée de niveau
- Histoire 8 : Statuts
- Histoire 9 : Structure de la Tour NO
- Histoire 10 : Utiliser un objet
- Histoire 11 : Récompenses d’étage
- Histoire 14 : Exploration visuelle des étages
- Histoire 16 : Animations de déplacement en mode exploration
- Histoire 20 : Sauvegarder sa progression, et continuer
- Histoire 25 : Défier un autre joueur
- Histoire 26 : Chat
- Histoire 27 : Filtrer les messages inappropriés
- Histoire 28 : Leaderboard

## Technologies utilisées

- Java 18 ou supérieur
- JavaFX 25
- Maven
- JSON
- SQLite

> JavaFX, jackson et SQLite sont déclarés comme dépendances Maven dans le `pom.xml`. Une installation séparée n'est donc normalement pas nécessaire pour les commandes Maven du projet.

## Structure du projet

```text
src/main/java/ulb/        Code source principal
src/main/resources/       FXML, CSS, images, JSON et SQL (pour initialiser)
src/test                  Tests unitaires et tests d'intégration
team/                     Documents de gestion de projet
pom.xml                   Configuration Maven
.gitlab-ci.yml            Configuration de l'intégration continue GitLab
```

## Pré-requis

Avant de lancer le projet, installer :

- un JDK Java 18 ou supérieur ;
- Maven ;
- Git, si vous clonez le dépôt depuis GitLab.

#### Linux (Ubuntu-like)

```bash
sudo apt update
sudo apt install git maven openjdk-25-jdk
```

#### macOS

```bash
brew install git maven openjdk
```

#### Windows

Installer Git depuis le site officiel (https://git-scm.com/install/windows), puis installer un JDK 18 ou supérieur et Maven. Vérifier ensuite que les commandes suivantes fonctionnent dans un terminal :

```bash
java --version
mvn --version
```

## Compilation et tests

Compiler le projet :

```bash
mvn compile
```

Lancer les tests :

```bash
mvn test
```

Nettoyer le projet et supprimer les fichiers générés par Maven :

```bash
mvn clean
```

La commande `mvn clean` supprime aussi les fichiers `.db`.

## Démarrage

#### Option 1: Lancement en développement

Le serveur doit être lancé avant le client. Attendre que le serveur affiche un message du type `Server is running ...` avant de démarrer le client.

Lancer le serveur :

```bash
mvn exec:java -P server
```

Dans un autre terminal, lancer le client :

```bash
mvn javafx:run -P client
```


#### Option 2: Lancement depuis les JAR

##### Création des fichiers JAR

Créer le JAR du serveur :

```bash
mvn clean package -P server
```

Créer le JAR du client :

```bash
mvn package -P client
```

Les fichiers générés se trouvent dans le dossier `target/` :

```text
target/server.jar
target/client.jar
```

##### Lancer les fichiers JAR

Lancer le serveur :

```bash
java -jar target/server.jar
```

Dans un autre terminal, lancer le client :

```bash
java -jar target/client.jar
```

## Intégration continue et releases

Le dépôt contient une configuration GitLab CI dans `.gitlab-ci.yml`. Elle lance les tests avec Maven et construit les JAR `server.jar` et `client.jar` lors des tags Git pour les releases.

## Auteurs
- Abdalrahman El Hussein - @aelh0063
- Alexis Smeets - @asme0008
- Clément SINON - @csin0003
- Daniel Azevedo Moreira Silveira Cabeça - @daze0002
- Daniel Blasiak - @dbla0005
- Oleksandra Omelyanyuk - @oome0001
- Rugile Banaityte - @rban0008
- Sam Nadif - @snad0007
- Wael Houmache - @whou0002
- ZIA GULZAR - @zgul0002

## Remerciements
Ce projet a été développé dans le cadre du cours **Génie logiciel et gestion de projets** (INFO-F-307) de l’Université libre de Bruxelles.

Nous remercions tout particulièrement **Frédéric Pluquet**, professeur à l’ULB, ainsi que **Martin Colot** et **Derar Alnakeb**, assistants à l’ULB, pour leur encadrement et leur soutien tout au long du projet.