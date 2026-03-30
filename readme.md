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
- Pour compiler: `mvn compile`
- Pour lancer l'application sans connection client-serveur : `mvn javafx:run`
- Pour lancer l'application côté client : mvn javafx:run -Djavafx.args="--client"
- Pour lancer l'application côté serveur : mvn javafx:run -Djavafx.args="--server"

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