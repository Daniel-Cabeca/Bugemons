# Histoires de l'itération 2

Ce document détaille les histoires choisies pour l'itération 2.

## Histoire 5 : Contrôle des actions en combat
### Description
Le joueur peut maintenant contrôler les actions de ses Bugémons pendant le combat.
À chaque tour, au lieu de choisir automatiquement, le jeu présente au joueur 4 options :
1. Attaquer : affiche un sous-menu avec les 3 attaques du Bugémon actif. Le joueur
   sélectionne l’attaque à utiliser.
2. Changer : affiche la liste des Bugémons disponibles dans l’équipe. Le joueur sélectionne
   quel Bugémon envoyer.
3. Objet : affiche l’inventaire (si la fonctionnalité est déjà implémentée) et permet de
   choisir un objet à utiliser.
4. Abandonner : termine immédiatement le combat (défaite).

Lorsqu’un Bugémon du joueur est K.O., le jeu affiche obligatoirement la liste des Bugémons
disponibles pour que le joueur choisisse lequel envoyer.
L’adversaire continue de jouer automatiquement (attaque aléatoire, changement aléatoire
si K.O.).
### Tâche restante pour terminer l'histoire :
Lorsqu’un Bugémon du joueur est K.O., le jeu affiche obligatoirement la liste des Bugémons
disponibles pour que le joueur choisisse lequel envoyer.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé pour la tâche restante : 1h
#### Temps mis pendant cette itération : 2h40
#### État : terminé

## Histoire 6 : Calcul de dégâts avec statistiques
### Description
Le calcul de dégâts est remplacé par la formule complète définie dans Section 3.2.3, prenant
en compte :
- L’Attaque de l’attaquant
- La Défense du défenseur
- L’efficacité de type (Flora > Aqua > Pyro > Litho > Flora)
- Les coups critiques (chaque attaque à 10% de chance de faire 150% de dégats)
- L’Initiative pour déterminer qui attaque en premier

Affichage :
- Le type de chaque Bugémon est visible (icône ou couleur)
- Le type de chaque attaque est affiché dans le menu de sélection (icône ou couleur)
- Lors de la sélection d’une attaque, un indicateur montre l’efficacité prévue :
    - « Super efficace ! » (vert) si multiplicateur = 1.5
    - « Peu efficace… » (rouge) si multiplicateur = 0.75
    - Rien de spécial si multiplicateur = 1.0
- Les messages de combat indiquent l’efficacité : « C’est super efficace ! » / « Ce n’est
  pas très efficace… »
### Tâche restante pour terminer l'histoire :
Afficher les messages d'efficacité.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé pour la tâche restante : 1h
#### Temps mis pendant cette itération : 35min
#### État : terminé

## Histoire 7 : Expérience et montée de niveau
### Description
Les Bugémons gagnent de l’expérience (XP) après chaque combat remporté.
Lorsqu’un Bugémon accumule suffisamment d’XP, il monte de niveau. À chaque montée
de niveau :
- Ses PV sont automatiquement restaurés au maximum
- Le joueur choisit un bonus parmi 3 options proposées aléatoirement :
    - +20 HP maximum
    - +10 Attaque
    - +10 Défense
    - +20 Initiative
    - Combinaisons générée aléatoirement; le total doit valoir 10 points et 1 point correspond à 2 HP ou 2 Initiative ou 1 Attaque ou 1 Défense(ex: +5 Attaque et +5 Défense)

Les règles concernant l’expérience gagnée sont disponibles à la section Section 3.2.4.
Les niveaux des Bugémons sont affichés à côté de leur nom dans tous les menus et écrans
de combat.
### Tâche restante pour terminer l'histoire :
Gestion des récompenses.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé pour la tâche restante : 10h
#### Temps mis pendant cette itération : 6h
#### État : terminé

## Histoire 9 : Structure de la Tour NO
### Description
La Tour NO est composée de 9 étages (NO2 à NO9, puis le Boss final). Chaque étage suit
une structure fixe :
1. Combat obligatoire
2. Récompense (choix parmi 3 options) (cf. histoire « Récompenses d’étage »)
3. Combat obligatoire
4. Combat obligatoire
5. Récompense (choix parmi 3 options)
6. Boss d’étage

Les équipes adverses sont générées aléatoirement avec un nombre de Bugémons égal à celui de l’équipe du joueur.
Battre le boss d’un étage débloque l’étage suivant. En cas de défaite contre n’importe quel adversaire, le joueur est renvoyé au menu principal.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 31h
#### Temps mis pendant cette itération : 10h30
#### État : terminé

## Histoire bonus A : Abandonner la partie
### Description
Bouton permettant d’arrêter la partie (donc d’abandonner l'ascension de la tour NO).
Diffère du bouton “fuite” qui ne fait qu'abandonner le combat en cours, sans finir la partie.
#### Risque développeurs: 2
#### Temps estimé : 4h
#### Temps mis pendant cette itération : 2h
#### État : terminé

## Histoire complémentaire A - Établir la connexion serveur-client
### Description
Pour commencer l'aspect multijoueur de Bugémon, il faut d'abord établit une connexion entre le serveur et le client. Le
terminal doit afficher les joueurs connectés.
#### Risque développeurs: 1
#### Temps estimé : 25h
#### Temps mis pendant cette itération : 12h
#### État : terminé

## Refactoring
#### Temps estimé : 20h
#### Temps mis pendant cette itération : 54h
