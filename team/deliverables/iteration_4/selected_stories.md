# Histoires de l'itération 4

Ce document détaille les histoires choisies pour l'itération 4.
Le temps restant a été utilisé pour refactor le code.

## Histoire 2 : Sauvegarder et charger une équipe
### Description
Le joueur doit pouvoir sauvegarder une équipe de Bugémons déjà constituée en lui
donnant un nom. Dans le menu de constitution d’équipe, le joueur peut aussi charger une
équipe sauvegardée précédemment.
#### Priorité Client: 2
#### Risque développeurs: 2
#### Temps estimé : 8h
#### Temps mis pendant cette itération : 9h
#### État : terminé

## Histoire 14a : Exploration visuelle des étages
### Description
Remplace la progression linéaire (cf. histoire « Structure de la Tour NO ») par une carte visuelle en forme de croix (dans le style de Dicey Dungeons).

Au lieu de suivre automatiquement la séquence, le joueur voit une carte avec sa position au centre (START) et peut cliquer sur les salles adjacentes pour choisir son parcours. Pour
chaque étage la structure est la suivante :

``` 
Bonus - Combat - Combat - START - Boss
                            |
                          Combat
                            |
                          Bonus
```

Le joueur commence au centre (START) et peut choisir librement dans quel ordre visiter
les salles adjacentes. L’interface affiche :
- Les salles disponibles avec leurs contenus

Le joueur clique sur une salle adjacente pour s’y déplacer.

Actions selon le type de salle :
- Combat : lance immédiatement un combat
- Bonus : affiche l’écran de choix de récompense (3 options) (cf. histoire « Récompenses d’étage »)
- Boss : lance le combat de boss et passe à l’étage suivant en cas de victoire
#### Priorité Client: 1
#### Risque développeurs: 1
#### Temps estimé : 15h
#### Temps mis pendant cette itération : 14h
#### État : terminé

## Histoire 14b : Afficher l'état d'exploration
### Description
L'interface affiche:
- La position actuelle du joueur (icône mise en évidence)
- Les salles déjà visitées sont grisées ou marquées d’une coche
#### Priorité Client: 1
#### Risque développeurs: 1
#### Temps estimé : 6h
#### Temps mis pendant cette itération : 4h30
#### État : terminé

## Histoire 16 : Animations de déplacement en mode exploration
### Description
Pour améliorer le feedback visuel, le jeu doit inclure des animations simples lors de la navigation.
- Lorsque le joueur clique sur une salle adjacente, son icône se déplace de manière
  fluide :
    - Interpolation linéaire de la position actuelle vers la nouvelle position
    - Durée : 0.5 secondes
    - Pas de téléportation instantanée
- La salle visitée change d’apparence (devient grisée) après l’arrivée du joueur
#### Priorité Client: 3
#### Risque développeurs: 2
#### Temps estimé : 4h
#### Temps mis pendant cette itération : 6h
#### État : terminé

## Histoire 20 : Sauvegarder sa progression, et continuer
### Description
Le jeu sauvegarde automatiquement la progression du joueur après chaque action (fin de combat, choix de récompense, changement d’étage).

La sauvegarde contient :
- L’équipe actuellement sélectionnée
- Les équipes enregistrées avec leurs noms
- L’étage actuel dans la Tour NO
- Les statistiques et niveaux actuels de chaque Bugémon
- L’inventaire d’objets
- Les Bugémons débloqués (Bugédex)

Lorsque le joueur lance le jeu, le bouton « Continuer » dans le menu principal charge automatiquement la dernière sauvegarde. Le bouton « Nouvelle Partie » réinitialise complètement la progression (confirmation requise si une sauvegarde existe).

En cas de défaite, la sauvegarde est réinitialisée : le joueur retourne à l’étage NO2 avec ses Bugémons au niveau 1.

#### Priorité Client: 2
#### Risque développeurs: 2
#### Temps estimé : 10h
#### Temps mis pendant cette itération : 15h30
#### État : terminé


## Histoire 25 : Défier un autre joueur
### Description
En mode multi-joueurs, chaque joueur peut défier un autre joueur connecté. Si ce dernier
accepte le défi, le combat se lance.
### Tâche restante pour terminer l'histoire :
Toute la fonctionnalité de défier un autre joueur comme l'itération précédente a été utilisée pour transformer le code en architecture serveur-client.
#### Priorité Client: 3
#### Risque développeurs: 2
#### Temps estimé pour les tâches restantes : 12h
#### Temps mis pendant cette itération : 22h
#### État : terminé

## Histoire 26 : Chat
### Description
En mode multi-joueurs, un salon de chat est disponible pour pouvoir communiquer avec
les autres joueurs à partir du menu principal, et pendant un combat.
### Tâche restante pour terminer l'histoire :
Pouvoir accéder au pendant un combat.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé pour la tâche restante : 12h
#### Temps mis pendant cette itération : 7h30
#### État : terminé

## Histoire 27 : Filtrer les messages inappropriés
### Description
Dans le chat, les messages inappropriés (obscènes, insultants, …) sont adoucis en remplaçant toutes les lettres par des astérisques, à l’exception de la première et de la dernière lettre des mots adoucis.
#### Priorité Client: 3
#### Risque développeurs: 3
#### Temps estimé : 5h
#### Temps mis pendant cette itération : 3h
#### État : terminé

## Histoire 28 : Leaderboard
### Description
En mode multijoueurs, à chaque fois que 2 personnes s’affrontent, celle qui emporte la victoire gagne 1 point. Il y a ensuite un classement qui permet de voir quels joueurs ont obtenu le plus de points.
#### Priorité Client: 1
#### Risque développeurs: 2
#### Temps estimé : 10h
#### Temps mis pendant cette itération : 5h
#### État : terminé

## Histoire complémentaire B - Animation de sprites
### Description
Implémenter l'animation de déplacement lors d'une attaque pour se familiariser avec le fonctionnement des animations.
Lorsqu’un Bugémon attaque, son sprite effectue une courte animation :
- Déplacement vers l’avant (vers l’adversaire) sur 0.3 secondes
- Retour à la position initiale sur 0.2 secondes
#### Risque développeurs: 2
#### Temps estimé : 6h
#### Temps mis pendant cette itération : 4h30
#### État : terminé
