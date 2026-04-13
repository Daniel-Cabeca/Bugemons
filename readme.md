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

### Démarrage via .jar
- Pour lancer le serveur : `java --module-path <chemin_vers_lib_openjfx> --add-modules javafx.controls,javafx.fxml -jar target/info-F307-Groupe-10.jar --server`
- Pour lancer le client : `java --module-path <chemin_vers_lib_openjfx> --add-modules javafx.controls,javafx.fxml -jar target/info-F307-Groupe-10.jar` 

### Démarrage via commande
- Pour compiler: `mvn compile`
- Pour lancer l'application côté client : mvn javafx:run
- Pour lancer l'application côté serveur : mvn clean javafx:run -Djavafx.args="--server"

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