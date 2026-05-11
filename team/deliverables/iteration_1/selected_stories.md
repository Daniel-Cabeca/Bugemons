# Histoires de l'itération 1

Ce document détaille les histoires choisies pour l'itération 1.
Les histoires 9 et A n'ont pas pu être faites dû à un manque de temps. 
Les histoires 5, 6 et 7 n'ont pas pu être complètement terminées.
Elles sont donc reportées pour l'itération suivante.
L'histoire 8 (Statuts) a été fait en même temps que les histoires 5 et 10. 
Le temps restant a été utilisé pour refactor le code.

## Histoire 1 : Constituer une équipe
### Decription
Le joueur peut constituer une équipe de 1 à 6 Bugémons à l’aide d’une interface qui montre
au moins le nom et l’image de chaque Bugémon. Dans cet écran, le joueur doit pouvoir
ajouter et retirer des Bugémons de son équipe facilement. Les Bugémons et leurs infos
sont disponibles dans le fichier bugemons.json (dans un zip sur l’UV). Leurs sprites sont
aussi disponibles au même endroit.
Le joueur ne peut pas sélectionner plusieurs fois le même Bugémon, ce qui doit être
représenté par un retour visuel
#### Priorité Client : 1
#### Risque développeurs : 2
#### Temps estimé : 12h
#### Temps mis pendant cette itération : 20h15
#### État : terminé

## Histoire 4 : Combat automatique
### Description
Au lancement d’une nouvelle partie, le joueur constitue son équipe de Bugémons et
un combat est automatiquement lancé contre une équipe adverse du même nombre de
Bugémons générée aléatoirement.
Le combat se déroule semi-automatiquement :
- Au début, le premier Bugémon de chaque équipe entre en jeu
- À chaque tour, chaque Bugémon choisit automatiquement une attaque aléatoire
parmi ses 3 attaques disponibles
- L’attaque réduit les PV de l’adversaire selon : PV_défenseur = PV_défenseur − Puissance_attaque
- Le Bugémon allié attaque toujours en premier

Lorsqu’un Bugémon atteint 0 PV :
- Il est K.O. et retiré du combat
- Un autre Bugémon de la même équipe est automatiquement envoyé (choisi aléatoirement)
- Si aucun Bugémon n’est disponible, l’équipe perd 

L’interface affiche :
- Les sprites des deux Bugémons actifs
- Leurs noms
- Leurs PV restants (sous forme de barre)
- Les messages décrivant les actions (« Florachu utilise Fouet-Liane ! Pyricore perd 40
PV ! »). Le joueur doit cliquer sur un bouton pour passer à l’action suivante.
À la fin, le jeu affiche « VICTOIRE » ou « DÉFAITE » et retourne au menu principal.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 16h
#### Temps mis pendant cette itération : 21h
#### État : terminé

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
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 15h
#### Temps mis pendant cette itération : 7h
#### État : en cours
    Il reste à afficher la liste de Bugémons disponibles quand un Bugémon est KO.

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
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 7h
#### Temps mis pendant cette itération : 2h45
#### État : en cours
    Il reste à afficher le message d'efficacité.

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
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 8h
#### Temps mis pendant cette itération : 2h
#### État : en cours
    Il reste à faire la gestion des récompenses.

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
#### Temps estimé : 15h
#### Temps mis pendant cette itération : 0h
#### État : pas commencé

## Histoire 10 : Utiliser un objet
### Description
Un dresseur doit pouvoir utiliser un objet au cours d’un combat. Une liste non-exhaustive
d’objets est fournie dans objets.json. Au début de chaque nouvelle partie, le joueur
démarre avec un inventaire contenant :
- 3x Baie Revigorante
- 2x Baie Tonique
- 1x Gel Défensif
- 1x Sérum Offensif

Utiliser un objet consomme le tour du dresseur.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 11h
#### Temps mis pendant cette itération : 11h10
#### État : terminé

## Histoire bonus A : Abandonner la partie
### Description
Bouton permettant d’arrêter la partie (donc d’abandonner l'ascension de la tour NO). 
Diffère du bouton “fuite” qui ne fait qu'abandonner le combat en cours, sans finir la partie.
#### Risque développeurs: 2
#### Temps estimé : 4h
#### Temps mis pendant cette itération : 0h
#### État : pas commencé