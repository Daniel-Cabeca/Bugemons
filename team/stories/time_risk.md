# Histoires - Estimation de la durée et du risque de développement

Ce document contient l'éstimation de la durée et du risque de développement pour chacune des 28 histoires.

## Histoires et estimation

|    ID | Titre                                            | Priorité client | Introduit dans l’itération |          État           | Durée estimée (h) | Risque estimé (1-3) |
|------:|--------------------------------------------------|:---------------:|:--------------------------:|:-----------------------:|:-----------------:|:-------------------:|
|  H-01 | Constituer une équipe                            |        1        |             1              | $${\color{green}Fini}$$ |        12         |          2          |
|  H-02 | Sauvegarder et charger une équipe                |        2        |             /              |            /            |         8         |          2          |
|  H-03 | Modifier équipes sauvegardées                    |        2        |             /              |            /            |         4         |          2          |
|  H-04 | Combat automatique                               |        1        |             1              | $${\color{green}Fini}$$ |        16         |          2          |
|  H-05 | Contrôle des actions en combat                   |        1        |            1-2             | $${\color{green}Fini}$$ |        15         |          2          |
|  H-06 | Calcul de dégâts avec statistiques               |        1        |            1-2             | $${\color{green}Fini}$$ |         7         |          2          |
|  H-07 | Expérience et montée de niveau                   |        1        |            1-2             | $${\color{green}Fini}$$ |        10         |          2          |
|  H-08 | Statuts                                          |        1        |             /              | $${\color{green}Fini}$$ |         8         |          2          |
|  H-09 | Structure de la Tour NO                          |        1        |            1-2             | $${\color{green}Fini}$$ |        31         |          2          |
|  H-10 | Utiliser un objet                                |        1        |             1              | $${\color{green}Fini}$$ |        11         |          2          |
|  H-11 | Récompenses d’étage                              |        1        |             /              |            /            |         8         |          3          |
|  H-12 | Bugédex                                          |        2        |             /              |            /            |         8         |          2          |
|  H-13 | Ajouter des Bugémons                             |        2        |             /              |            /            |         8         |          3          |
| H-14a | Exploration visuelle des étages                  |        1        |             /              |            /            |        14         |          1          |
| H-14b | Afficher l'état d'exploration                    |        1        |             /              |            /            |         4         |          1          |
|  H-15 | Animations de combat                             |        3        |             /              |            /            |         6         |          2          |
|  H-16 | Animations de déplacement en mode exploration    |        3        |             /              |            /            |         4         |          2          |
| H-17a | Systèmes de compétences                          |        2        |             /              |            /            |        15         |          2          |
| H-17b | Arbre de compétences                             |        2        |             /              |            /            |        15         |          2          |
|  H-18 | Jouer à la manette                               |        3        |             /              |            /            |        12         |          2          |
|  H-19 | Génération procédurale des étages                |        2        |             /              |            /            |        10         |          2          |
|  H-20 | Sauvegarder sa progression, et continuer         |        2        |             /              |            /            |        15         |          2          |
|  H-21 | Choisir un niveau de difficulté                  |        2        |             /              |            /            |         6         |          2          |
| H-22a | Intelligence artificielle de combat (attaque)    |        2        |             /              |            /            |         6         |          1          |
| H-22b | Intelligence artificielle de combat (objet soin) |        2        |             /              |            /            |         4         |          1          |
| H-22c | Intelligence artificielle de combat (swap)       |        2        |             /              |            /            |         8         |          1          |
|  H-23 | Générer une équipe dynamiquement                 |        2        |             /              |            /            |         8         |          2          |
|  H-24 | Musique d’ambiance                               |        3        |             /              |            /            |         4         |          3          |
|  H-25 | Défier un autre joueur                           |        3        |             /              |            /            |        15         |          2          |
|  H-26 | Chat                                             |        1        |             /              |            /            |        10         |          2          |
|  H-27 | Filtrer les messages inappropriés                |        3        |             /              |            /            |         5         |          3          |
|  H-28 | Leaderboard                                      |        1        |             /              |            /            |       6-10        |          2          |
|  HC-A | Établir une connexion serveur-client             |        1        |             2              | $${\color{green}Fini}$$ |        25         |          1          |
|  HC-B | Créer une base de données                        |        /        |             /              |            /            |        15         |          1          |
|  HC-C | Animation de sprites                             |        /        |             /              |            /            |         6         |          2          |
|  HC-D | Amis                                             |        /        |             /              |            /            |        14         |          2          |
|  HB-A | Abandonner la partie                             |        /        |             1              | $${\color{green}Fini}$$ |         4         |          2          |
|  HB-B | Charger de nouveaux sprites                      |        /        |             /              |            /            |        10         |          2          |
|  HB-C | Créer de nouvelles attaques                      |        /        |             /              |            /            |         6         |          2          |
|  HB-D | Créer la musique                                 |        /        |             /              |            /            |         5         |          1          |
|   R-1 | Refactoring                         			          |        /        |             2              | $${\color{green}Fini}$$ |        20         |          /          |
|   R-2 | Refactoring                         			          |        /        |             /              |            /            |        10         |          /          |

## Légende
- H = Histoire de l'énoncé
- HC = Histoire complémentaire (certaines histoires en dépendent)
- HB = Histoire bonus (ajout d'une fonctionnalité non-décrite dans l'énoncé)
- R = Refactoring

## Descriptions des histoires
### H-01 - Constituer une équipe

Le joueur peut constituer une équipe de 1 à 6 Bugémons à l’aide d’une interface qui montre au moins le nom et l’image de chaque Bugémon. 
Dans cet écran, le joueur doit pouvoir ajouter et retirer des Bugémons de son équipe facilement. Les Bugémons et leurs infos 
sont disponibles dans le fichier bugemons.json (dans un zip sur l’UV). Leurs sprites sont aussi disponibles au même endroit. 
Le joueur ne peut pas sélectionner plusieurs fois le même Bugémon (= même sprite), ce qui doit être représenté par un retour visuel.

### H-02 - Sauvegarder et charger une équipe

Le joueur doit pouvoir sauvegarder une équipe de Bugémons déjà constituée en lui donnant un nom. Dans le menu de constitution 
d’équipe, le joueur peut aussi charger une équipe sauvegardée précédemment.

### H-03 - Modifier équipes sauvegardées

Le joueur doit pouvoir modifier, supprimer et renommer les équipes déjà sauvegardées précédemment depuis un nouveau menu. 
Depuis ce menu, il peut aussi directement lancer une partie avec une équipe déjà créée ou simplement créer une nouvelle équipe à partir de zéro.

### H-04 - Combat automatique

Au lancement d’une nouvelle partie, le joueur constitue son équipe de Bugémons et un combat est automatiquement lancé contre une équipe adverse du même nombre de Bugémons générée aléatoirement.
Le combat se déroule semi-automatiquement :
- Au début, le premier Bugémon de chaque équipe entre en jeu
- À chaque tour, chaque Bugémon choisit automatiquement une attaque aléatoire parmi ses 3 attaques disponibles
- L’attaque réduit les PV de l’adversaire selon :
- Le Bugémon allié attaque toujours en premier PV_défenseur = PV_défenseur − attaque_puissance

Lorsqu’un Bugémon atteint 0 PV :
- Il est K.O. et retiré du combat
- Un autre Bugémon de la même équipe est automatiquement envoyé (choisi aléatoirement)
- Si aucun Bugémon n’est disponible, l’équipe perd

L’interface affiche :
- Les sprites des deux Bugémons actifs
- Leurs noms
- Leurs PV restants (sous forme de barre)
- Les messages décrivant les actions (« Florachu utilise Fouet-Liane ! Pyricore perd 40 PV ! »). 
- Le joueur doit cliquer sur un bouton pour passer à l’action suivante. À la fin, le jeu affiche « VICTOIRE » ou « DÉFAITE » et retourne au menu principal.

### H-05 - Contrôle des actions en combat

Le joueur peut maintenant contrôler les actions de ses Bugémons pendant le combat. À chaque tour, au lieu de choisir automatiquement, le jeu présente au joueur 4 options :
1. Attaquer : affiche un sous-menu avec les 3 attaques du Bugémon actif. Le joueur sélectionne l’attaque à utiliser.
2. Changer : affiche la liste des Bugémons disponibles dans l’équipe. Le joueur sélectionne quel Bugémon envoyer.
3. Objet : affiche l’inventaire (si la fonctionnalité est déjà implémentée) et permet de choisir un objet à utiliser.
4. Abandonner : termine immédiatement le combat (défaite).

Lorsqu’un Bugémon du joueur est K.O., le jeu affiche obligatoirement la liste des Bugémons disponibles pour que le joueur choisisse lequel envoyer. L’adversaire continue de jouer automatiquement (attaque aléatoire, changement aléatoire si K.O.).

### H-06 - Calcul de dégâts avec statistiques

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
- Les messages de combat indiquent l’efficacité : « C’est super efficace ! » / « Ce n’est pas très efficace… »

### H-07 - Expérience et montée de niveau

Les Bugémons gagnent de l’expérience (XP) après chaque combat remporté. Lorsqu’un Bugémon accumule suffisamment de XP, il monte de niveau. À chaque montée
de niveau :
- Ses PV sont automatiquement restaurés au maximum
- Le joueur choisit un bonus parmi 3 options proposées aléatoirement :
    - +20 HP maximum
    - +10 Attaque
    - +10 Défense
    - +20 Initiative
    - Combinaisons générées aléatoirement ; le total doit valoir 10 points et 1 point correspond à 2 HP ou 2 Initiative 
ou 1 Attaque ou 1 Défense (ex : +5 Attaque +5 Défense) 

Les règles concernant l’expérience gagnée sont disponibles à la section Section 3.2.4. Les niveaux des Bugémons sont affichés à côté de leur nom dans tous les menus et écrans

### H-08 - Statuts

Gérer les effets de statut soin et réduction des statistiques présentes dans attaques.json lors des attaques (voir aussi Section 3.2.2.1)

### H-09 - Structure de la Tour NO

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

### H-10 - Utiliser un objet

Un dresseur doit pouvoir utiliser un objet au cours d’un combat. Une liste non-exhaustive d’objets est fournie dans objets.json. Au début de chaque nouvelle partie, le joueur
démarre avec un inventaire contenant :
- 3x Baie Revigorante
- 2x Baie Tonique
- 1x Gel Défensif
- 1x Sérum Offensif 

Utiliser un objet consomme le tour du dresseur.

### H-11 - Récompenses d’étage

Après certains combats dans un étage, le joueur obtient une récompense. Trois options sont proposées, parmi lesquelles le joueur doit en choisir une :

Types de récompenses possibles :
- Objet de combat (Baie Tonique, Gel Défensif, etc.)
- Enseigner une nouvelle attaque à un Bugémon (remplace une attaque existante)
- Bonus de statistiques permanent pour un Bugémon (+10 HP, +5 Attaque, +5 Défense, etc.)

Lorsqu’une récompense de statistiques ou d’attaque est choisie, le joueur doit sélectionner quel Bugémon de son équipe la reçoit.

### H-12 - Bugédex

Au départ seulement les trois Bugémons starter (Florachu, Exceflam et Commmitide) sont disponibles (ainsi que leurs attaques uniquement). 
Lorsqu’un Bugémon est vaincu, il est ajouté au Bugédex et peut dès lors être choisi dans son équipe de départ. Ses attaques 
peuvent maintenant aussi être débloquées. Les Bugémons débloqués ainsi sont évidemment gardés après une défaite.

### H-13 - Ajouter des Bugémons

À l’aide d’une interface dédiée, le joueur peut ajouter de nouveaux Bugémons au jeu en fournissant ses caractéristiques, 
les attaques que celui-ci peut apprendre ainsi que les sprites nécessaires à son affichage. Le type, les attaques et les
sprites peuvent être choisi parmi une liste déjà établie.

### H-14a - Exploration visuelle des étages

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

Questions
- Quelle est la différence entre un combat et un boss ? 

### H-14b - Afficher l'état d'exploration
L'interface affiche:
- La position actuelle du joueur (icône mise en évidence)
- Les salles déjà visitées sont grisées ou marquées d’une coche

### H-15 - Animations de combat

Pour améliorer le feedback visuel, le jeu doit inclure des animations simples lors des combats.
- Lorsqu’un Bugémon reçoit des dégâts :
    - Le sprite clignote ou change brièvement de couleur (flash rouge/blanc)
    - Durée : 0.3 secondes
    - Animation accompagnée d’une réduction visible de la barre de PV
- Lors d’un K.O. :
    - Le sprite disparaît progressivement (fade out) sur 0.5 secondes
    - Ou tombe/s’affaisse avant de disparaître

### H-16 - Animations de déplacement en mode exploration

Pour améliorer le feedback visuel, le jeu doit inclure des animations simples lors de la navigation.
- Lorsque le joueur clique sur une salle adjacente, son icône se déplace de manière
fluide :
    - Interpolation linéaire de la position actuelle vers la nouvelle position
    - Durée : 0.5 secondes
    - Pas de téléportation instantanée
- La salle visitée change d’apparence (devient grisée) après l’arrivée du joueur

### H-17a - Système de compétences

Le joueur dispose d'une liste de compétences permanente accessible depuis le menu principal. Pour l'instant, il n'y a
pas de dépendances entre les compétences, ni de structure en arbre : toute compétence est disponible et peut être activée
à tout moment à condition d'avoir assez de points.

Points de compétence :
- Le joueur gagne 1 point par boss d’étage vaincu
- Les points persistent entre les runs
- Les points peuvent être dépensés et récupérés librement pour réorganiser l’arbre

Les bonus s’appliquent au début de chaque run.

Interface :
- Affichage de toutes les compétences
- Nœuds actifs : colorés/brillants
- Hover avec la souris sur un nœud : affiche description
- Clic gauche sur un nœud : ajoute un point
- Clic droit sur un nœud : retire un point
- Affichage du nombre de points disponibles

### H-17b - Arbre de compétences

Adapte le système de compétences pour suivre la structure de l'arbre.

Structure de l’arbre :
- L’arbre est fourni au format JSON (skill_tree.json)
- Chaque nœud a une position (x, y) pour l’affichage
- Les connexions entre nœuds sont définies par les prérequis
- Certains nœuds peuvent être améliorés plusieurs fois (max_niveau)

Règles :
- Un nœud ne peut être débloqué que si au moins un de ses prérequis est actif
- Le nœud « Départ » est toujours actif et gratuit
- Déplacer/retirer des points d’un nœud désactive aussi tous les nœuds qui en dépendent

Interface :
- Affichage graphique de l’arbre avec lignes reliant les nœuds
- Nœuds actifs : colorés/brillants
- Nœuds disponibles (prérequis satisfaits) : semi-transparents
- Nœuds verrouillés : grisés
- Hover avec la souris sur un nœud : affiche description
- Clic gauche sur un nœud : ajoute un point
- Clic droit sur un nœud : retire un point
- Affichage du nombre de points disponibles 

### H-18 - Jouer à la manette

Le joueur peut choisir d’utiliser le clavier et la souris, ou une manette pour jouer.

### H-19 - Génération procédurale des étages

Remplace la structure fixe en croix par une génération procédurale différente à chaque run.

Paramètres de génération :
- Grille de 5×5 cases
- Point de départ : centre de la grille
- Génération d’un arbre avec 3-4 branches partant du départ
- Profondeur maximale : 6 cases par branche

Contenu de chaque étage :
- 1 Boss (toujours au bout de la branche la plus longue)
- 4 à 6 Combats répartis aléatoirement
- 2 à 3 Bonus
- Contrainte : chaque Bonus doit être précédé d’au moins 1 Combat sur son chemin depuis le départ

Algorithme de génération :
1. Créer le nœud de départ au centre
2. Générer 3-4 branches avec croissance aléatoire (haut/bas/gauche/droite)
3. Éviter les chevauchements et les boucles
4. Placer le Boss au bout de la branche la plus longue
5. Placer aléatoirement les Combats sur les nœuds
6. Placer les Bonus uniquement sur des nœuds ayant au moins 1 Combat avant eux
7. Les nœuds sans contenu deviennent des cases vides (traversables)

Interface :
- Afficher uniquement les cases générées (pas toute la grille 5×5)
- Les cases non-générées n’apparaissent pas
- Le chemin parcouru est marqué visuellement
- Le Boss est clairement identifiable (icône distinctive)

### H-20 - Sauvegarder sa progression, et continuer

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

### H-21 - Choisir un niveau de difficulté

Lorsque le joueur lance une nouvelle partie, celui-ci peut décider d’un niveau de difficulté. Plus le niveau de difficulté est élevé, plus les adversaires seront coriaces.

Questions: 
- Combien de niveaux de difficulté y a-t-il ?
- Est-ce-que la difficulté consiste à ajouter un multiplicateur sur les stats des Bugémons de l'équipe de l'adversaire ?
- À quel point ces stats doivent être augmentées ?

### H-22a - Intelligence artificielle de combat (attaques)

L'ordinateur ne choisit pas les actions au hasard, mais choisi l'attaque faisant le plus de dégâts.

### H-22a - Intelligence artificielle de combat (objet soin)

En plus de choisir l'attaque, l'ordinateur peut également choisir d'utiliser un objet de type soin si le Bugémon actif
a moins de points de vie qu'un seuil prédéfini et s'il possède l'objet dans son inventaire.

### H-22a - Intelligence artificielle de combat (swap)
En plus du choix d'attaque et d'objet soin, l'ordinateur peut décider quel Bugémon envoyer lorsque l'actif est KO, ou comme
action pour que le type du Bugémon actif soit plus efficace contre celui du joueur (e.g., si le Bugémon actif du joueur
est de type aqua, l'ordinateur choisit d'envoyer un Bugémon de type flora, plutôt que de type pyro).

### H-23 - Générer une équipe dynamiquement

Chaque étage de la tour de combat est générée dynamiquement en fonction de la difficulté choisie. Cela doit alors prendre en compte les types de Bugémons dans l’équipe du joueur.

Questions
- Quelles sont les différences entre les difficultés ?

### H-24 - Musique d’ambiance

La musique du jeu varie en fonction de la situation dans laquelle le joueur se trouve (création d’équipe, combat, exploration).

### H-25 - Défier un autre joueur

En mode multi-joueurs, chaque joueur peut défier un autre joueur connecté. Si ce dernier accepte le défi, le combat se lance.

### H-26 - Chat

En mode multi-joueurs, un salon de chat est disponible pour pouvoir communiquer avec les autres joueurs.

### H-27 - Filtrer les messages inappropriés

Dans le chat, les messages inappropriés (obscènes, insultants, …) sont adoucis en remplaçant toutes les lettres par des astérisques, à l’exception de la première et de la dernière lettre des mots adoucis.

### H-28 - Leaderboard

En mode multijoueurs, à chaque fois que 2 personnes s’affrontent, celle qui emporte la victoire gagne 1 point. Il y a ensuite un classement qui permet de voir quels joueurs ont obtenu le plus de points.
L’estimation des heures dépend du moment où cette histoire est implémentée. Avant les histoires de base de données (HC-B) et de
défier un autre joueur (H-25), le temps est estimé à 6 points comme le leaderboard sera statique et basé sur des données fictives. 
Après leur mise en place, il faudra intégrer un système de points et connecter à la base de données, ce qui augmente le temps estimé à 10 points.

### Histoire complémentaire A - Établir la connexion serveur-client

Pour commencer l'aspect multijoueur de Bugémon, il faut d'abord établit une connexion entre le serveur et le client. Le 
terminal doit afficher les joueurs connectés.

### Histoire complémentaire B - Créer la base de données

Pour implémenter les histoires de sauvegarde, progression ou multijoueur (e.g., sauvegarder et charger une équipe (H-2), 
Bugédex (H-12), arbre de compétences (H-17), chat (H-26), leaderboard (H-28)), il est nécessaire d'avoir une base de données 
fonctionnelle afin de pouvoir sauvegarder et charger les données nécessaires.

### Histoire complémentaire C - Animation de sprites

Implémenter l'animation de déplacement lors d'une attaque pour se familiariser avec le fonctionnement des animations.
Lorsqu’un Bugémon attaque, son sprite effectue une courte animation :
- Déplacement vers l’avant (vers l’adversaire) sur 0.3 secondes
- Retour à la position initiale sur 0.2 secondes

### Histoire complémentaire D - Amis

Le joueur peut ajouter d'autres joueurs en tant qu'amis pour pouvoir ensuite utiliser le chat ou défier un autre joueur.

### Histoire bonus A - Abandonner la partie

Ajout d'un bouton permettant d’arrêter la partie (donc d’abandonner l'ascension de la tour NO). Il diffère du bouton “fuite” qui ne 
fait qu'abandonner le combat en cours, sans finir la partie.

### Histoire bonus B - Charger de nouveaux sprites

Lors de l'ajout d'un nouveau Bugémon (histoire 13), le joueur peut charger un fichier png à utiliser comme sprite, au 
lieu de choisir de la liste de sprites proposée.

### Histoire bonus C - Créer de nouvelles attaques

Lors de l'ajout d'un nouveau Bugémon (histoire 13), le joueur peut également créer une nouvelle attaque en précisant le
type (choix parmi une liste établie), l'effet (choix parmi une liste établie), la puissance et le nom. 

### Histoire bonus D - Créer la musique

Au lieu d'utiliser des musiques déjà existantes (histoire 24), nous créons nous-mêmes les musiques à utiliser.