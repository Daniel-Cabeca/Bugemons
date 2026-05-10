# Génie logiciel et gestion de projets - Bugémon

## Pré-requis
- Java 18.0 ou supérieur

## Dépendances 

Le projet utilise JavaFX.

Pour l'installer :
https://gluonhq.com/products/javafx/

Le projet utilise également `maven`. Pour l'installer :
- sur Linux : `sudo apt install git maven openjdk-25-jdk`
- sur MacOS : 
  - installer `homebrew` sur https://brew.sh/
  - `brew install git maven openjdk`
- sur Windows : https://git-scm.com/install/windows

## Démarrage
Pour démarrer l'application, il faut lancer le serveur avant de lancer le client (en attendant que le serveur est bien lancé, càd lorsqu'il affiche "SERVER ON").

### Créer les fichier .jar
- Créer le fichier .jar server : `mvn package -P server`
- Créer le fichier .jar client : `mvn package -P client`

### Démarrage via .jar
- Pour lancer le serveur : `java -jar server.jar`
- Pour lancer le client : `java -jar client.jar`

### Démarrage via commande
- Pour compiler: `mvn compile`
- Pour lancer l'application côté client : `mvn javafx:run -P client`
- Pour lancer l'application côté serveur : `mvn exec:java -P server`

### Supprimer la base de donnée
`mvn clean`
(la base de donnée)

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