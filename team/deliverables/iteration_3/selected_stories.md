# Histoires de l'itération 3

Ce document détaille les histoires choisies pour l'itération 3. Les histoires 25 et 26 n'ont pas pu être complètement terminées.
Elles sont donc reportées pour l'itération suivante.

## Histoire 11 : Récompenses d’étage
### Description
Après certains combats dans un étage, le joueur obtient une récompense. Trois options
sont proposées, parmi lesquelles le joueur doit en choisir une :
Types de récompenses possibles :
- Objet de combat (Baie Tonique, Gel Défensif, etc.)
- Enseigner une nouvelle attaque à un Bugémon (remplace une attaque existante)
- Bonus de statistiques permanent pour un Bugémon (+10 HP, +5 Attaque, +5 Défense,
etc.)

- Lorsqu’une récompense de statistiques ou d’attaque est choisie, le joueur doit sélectionner
quel Bugémon de son équipe la reçoit.
#### Priorité Client: 1
#### Risque développeurs: 3
#### Temps estimé : 8h
#### Temps mis pendant cette itération : 6h20
#### État : terminé

## Histoire 25 : Défier un autre joueur
### Description
En mode multi-joueurs, chaque joueur peut défier un autre joueur connecté. Si ce dernier
accepte le défi, le combat se lance.
#### Priorité Client: 3
#### Risque développeurs: 2
#### Temps estimé : 15h
#### Temps mis pendant cette itération : 25h45
#### État : en cours
    La transition du code vers une architecture serveur-client a pris tout le temps attribué à cette histoire.
    Il reste à faire toute la fonctionnalité de défier un autre joueur.

## Histoire 26 : Chat
### Description
En mode multi-joueurs, un salon de chat est disponible pour pouvoir communiquer avec
les autres joueurs à partir du menu principal, et pendant un combat.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 25h
#### Temps mis pendant cette itération : 9h
#### État : en cours
    Comme l'histoire 25 (défier un joueur) n'a pas pu être terminée, il reste à implémenter le chat lors d'un combat multijoueur.

## Histoire complémentaire B - Créer une base de données
### Description
Pour implémenter les histoires de sauvegarde, progression ou multijoueur (e.g., sauvegarder et charger une équipe (H-2),
Bugédex (H-12), arbre de compétences (H-17), chat (H-26), leaderboard (H-28)), il est nécessaire d'avoir une base de données
fonctionnelle afin de pouvoir sauvegarder et charger les données nécessaires.
#### Risque développeurs: 1
#### Temps estimé : 15h
#### Temps mis pendant cette itération : 16h
#### État : terminé

## Histoire complémentaire D - Amis
### Description
Le joueur peut ajouter d'autres joueurs en tant qu'amis pour pouvoir ensuite utiliser le chat ou défier un autre joueur.
#### Risque développeurs: 2
#### Temps estimé : 14h
#### Temps mis pendant cette itération : 9h45
#### État : terminé

## Refactoring
#### Temps estimé : 10h
#### Temps mis pendant cette itération : 28h
