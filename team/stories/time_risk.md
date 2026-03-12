# User stories - Estimation Durée et Risque de développement

Ce document contient l'éstimation de la durée et du risque de développement pour chacune des 28 User Stories.

## Stories et Estimation

| ID    | Titre                                         | Priorité client | Introduit dans l’itération |     État     | Durée estimée (h) | Risque estimé (1-3) |temps mis (h)|
|------:|-----------------------------------------------|:---------------:|:--------------------------:|:------------:|:-----------------:|:-------------------:|:-----------:|
| US-01 | Constituer une équipe                         |        1        |             1              | $${\color{green}Fini}$$ |        12         |          2                  | 20:15|
| US-02 | Sauvegarder et charger une équipe             |        2        |             /              |      /       |         16         |          3          | / |
| US-03 | Modifier équipes sauvegardées                 |        2        |             /              |      /       |         15         |          3          | /|
| US-04 | Combat automatique                            |        1        |             1              | $${\color{green}Fini}$$ |        16         |          2                 |21|
| US-05 | Contrôle des actions en combat                |        1        |             1              | $${\color{blue}En \space Progrès}$$ |        1         |          2                 |7|
| US-06 | Calcul de dégâts avec statistiques            |        1        |             1              | $${\color{blue}En \space Progrès}$$ |         1         |          2                 |2:45|
| US-07 | Expérience et montée de niveau                |        1        |             1              | $${\color{blue}En \space Progrès}$$ |         10         |          2                 |2|
| US-08 | Statuts                                       |        1        |             /              |      /       |         22         |          2          |/|
| US-09 | Structure de la Tour NO                       |        1        |             1              |     $${\color{red}À \space Faire}$$       |        51         |          2          |0|
| US-10 | Utiliser un objet                             |        1        |             1              |  $${\color{blue}En \space Progrès}$$ |        21         |          2                 |11:10 |
| US-11 | Récompenses d’étage                           |        1        |             /              |      /       |         18         |          3          |/|
| US-12 | Bugédex                                       |        2        |             /              |      /       |        21         |          2          |/|
| US-13 | Ajouter des Bugémons                          |        2        |             /              |      /       |        26         |          3          |/|
| US-14 | Exploration visuelle des étages               |        1        |             /              |      /       |        48         |          1          |/|
| US-15 | Animations de combat                          |        3        |             /              |      /       |        29         |          2          |/|
| US-16 | Animations de déplacement en mode exploration |        3        |             /              |      /       |        29        |          2          |/|
| US-17 | Arbre de compétences                          |        2        |             /              |      /       |        40         |          1          |/|
| US-18 | Jouer à la manette                            |        3        |             /              |      /       |         18         |          2          |/|
| US-19 | Génération procédurale des étages             |        2        |             /              |      /       |        43        |          2          |/|
| US-20 | Sauvegarder sa progression, et continuer      |        2        |             /              |      /       |        38         |          2          |/|
| US-21 | Choisir un niveau de difficulté               |        2        |             /              |      /       |         21         |          2          |/|
| US-22 | Intelligence artificielle de combat           |        2        |             /              |      /       |        49         |          1          |/|
| US-23 | Générer une équipe dynamiquement              |        2        |             /              |      /       |        38         |          2          |/|
| US-24 | Musique d’ambiance                            |        3        |             /              |      /       |        25         |          2          |/|
| US-25 | Défier un autre joueur                        |        3        |             /              |      /       |        52         |          2          |/|
| US-26 | Chat                                          |        1        |             /              |       /     |        46         |          1            |/|
| US-27 | Filtrer les messages inappropriés             |        3        |             /              |      /       |         16         |          3          |/|
| US-28 | Leaderboard                                   |        1        |             /              |      /       |         31         |          2          |/|
| US-A  |  Abandonner la partie                         |        1        |             1              |      $${\color{red}À \space Faire}$$  |         30         |          2          |0|
| US-R  |  Refactoring                         			|        /        |             Tout              |      $${\color{blue}En \space Progrès}$$  |         /         |          /          |12:15|

## Descriptions des User Stories
### US-01 - Constituer une équipe

Le joueur peut constituer une équipe de 1 à 6 Bugémons à l’aide d’une interface qui montre au moins le nom et l’image de chaque Bugémon. Dans cet écran, le joueur doit pouvoir ajouter et retirer des Bugémons de son équipe facilement. Les Bugémons et leurs infos sont disponibles dans le fichier bugemons.json (dans un zip sur l’UV). Leurs sprites sont aussi disponibles au même endroit. Le joueur ne peut pas sélectionner plusieurs fois le même Bugémon (= même sprite), ce qui doit être représenté par un retour visuel.

### US-02 - Sauvegarder et charger une équipe

Le joueur doit pouvoir sauvegarder une équipe de Bugémons déjà constituée en lui donnant un nom. Dans le menu de constitution d’équipe, le joueur peut aussi charger une équipe sauvegardée précédemment.

### US-03 - Modifier équipes sauvegardées

Le joueur doit pouvoir modifier, supprimer et renommer les équipes déjà sauvegardées précédemment depuis un nouveau menu. Depuis ce menu il peut aussi directement lancer une partie avec une équipe déjà créée ou simplement créer une nouvelle équipe à partir de zéro.

### US-04 - Combat automatique

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
- Les messages décrivant les actions (« Florachu utilise Fouet-Liane ! Pyricore perd 40
PV ! »). Le joueur doit cliquer sur un bouton pour passer à l’action suivante. À la fin, le jeu affiche « VICTOIRE » ou « DÉFAITE » et retourne au menu principal.

---

A faire:

- les actions du joueur se font automatiquement avec un certain temps d’attente entre chaque action (par ex : 1 seconde).

### US-05 - Contrôle des actions en combat

Le joueur peut maintenant contrôler les actions de ses Bugémons pendant le combat. À chaque tour, au lieu de choisir automatiquement, le jeu présente au joueur 4 options :
1. Attaquer : affiche un sous-menu avec les 3 attaques du Bugémon actif. Le joueur sélectionne l’attaque à utiliser.
2. Changer : affiche la liste des Bugémons disponibles dans l’équipe. Le joueur sélectionne quel Bugémon envoyer.
3. Objet : affiche l’inventaire (si la fonctionnalité est déjà implémentée) et permet de choisir un objet à utiliser.
4. Abandonner : termine immédiatement le combat (défaite).

Lorsqu’un Bugémon du joueur est K.O., le jeu affiche obligatoirement la liste des Bugémons disponibles pour que le joueur choisisse lequel envoyer. L’adversaire continue de jouer automatiquement (attaque aléatoire, changement aléatoire si K.O.).

### US-06 - Calcul de dégâts avec statistiques

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

### US-07 - Expérience et montée de niveau

Les Bugémons gagnent de l’expérience (XP) après chaque combat remporté. Lorsqu’un Bugémon accumule suffisamment d’XP, il monte de niveau. À chaque montée
de niveau :
- Ses PV sont automatiquement restaurés au maximum
- Le joueur choisit un bonus parmi 3 options proposées aléatoirement :
    - +20 HP maximum
    - +10 Attaque
    - +10 Défense
    - +20 Initiative
    - Combinaisons générée aléatoirement; le total doit valoir 10 points et 1 point correspond à 2 HP ou 2 Initiative ou 1 Attaque ou 1 Défense(ex: +5 Attaque et de combat.
+5 Défense) 

Les règles concernant l’expérience gagnée sont disponibles à la section Section 3.2.4. Les niveaux des Bugémons sont affichés à côté de leur nom dans tous les menus et écrans

### US-08 - Statuts

Gérer les effets de statut soin et réduction des statistiques présentes dans attaques.json lors des attaques (voir aussi Section 3.2.2.1)

### US-09 - Structure de la Tour NO

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

### US-10 - Utiliser un objet

Un dresseur doit pouvoir utiliser un objet au cours d’un combat. Une liste non-exhaustive d’objets est fournie dans objets.json. Au début de chaque nouvelle partie, le joueur
démarre avec un inventaire contenant :
- 3x Baie Revigorante
- 2x Baie Tonique
- 1x Gel Défensif
- 1x Sérum Offensif 

Utiliser un objet consomme le tour du dresseur.

### US-11 - Récompenses d’étage

Après certains combats dans un étage, le joueur obtient une récompense. Trois options sont proposées, parmi lesquelles le joueur doit en choisir une :

Types de récompenses possibles :
- Objet de combat (Baie Tonique, Gel Défensif, etc.)
- Enseigner une nouvelle attaque à un Bugémon (remplace une attaque existante)
- Bonus de statistiques permanent pour un Bugémon (+10 HP, +5 Attaque, +5 Défense, etc.)

Lorsqu’une récompense de statistiques ou d’attaque est choisie, le joueur doit sélectionner quel Bugémon de son équipe la reçoit.

### US-12 - Bugédex

Au départ seulement les trois Bugémons starter (Florachu, Exceflam et Commmitide) sont disponibles (ainsi que leurs attaques uniquement). Lorsqu’un Bugémon est vaincu, il est ajouté au Bugédex et peut dés lors être choisi dans son équipe de départ. Ses attaques peuvent maintenant aussi être débloquées. Les Bugémons débloqués ainsi sont évidemment gardés après une défaite.

### US-13 - Ajouter des Bugémons

À l’aide d’une interface dédiée, le joueur peut ajouter de nouveaux Bugémons au jeu en fournissant ses caractéristiques, les attaques que celui-ci peut apprendre ainsi que les sprites nécessaires à son affichage.

### US-14 - Exploration visuelle des étages

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
- La position actuelle du joueur (icône mise en évidence)
- Les salles disponibles avec leurs contenus
- Les salles déjà visitées sont grisées ou marquées d’une coche

Le joueur clique sur une salle adjacente pour s’y déplacer.

Actions selon le type de salle :
- Combat : lance immédiatement un combat
- Bonus : affiche l’écran de choix de récompense (3 options) (cf. histoire « Récompenses d’étage »)
- Boss : lance le combat de boss et passe à l’étage suivant en cas de victoire

### US-15 - Animations de combat

Pour améliorer le feedback visuel, le jeu doit inclure des animations simples lors des combats et de la navigation.
- Lorsqu’un Bugémon attaque, son sprite effectue une courte animation :
    - Déplacement vers l’avant (vers l’adversaire) sur 0.3 secondes
    - Retour à la position initiale sur 0.2 secondes
- Lorsqu’un Bugémon reçoit des dégâts :
    - Le sprite clignote ou change brièvement de couleur (flash rouge/blanc)
    - Durée : 0.3 secondes
    - Animation accompagnée d’une réduction visible de la barre de PV
- Lors d’un K.O. :
    - Le sprite disparaît progressivement (fade out) sur 0.5 secondes
    - Ou tombe/s’affaisse avant de disparaître

### US-16 - Animations de déplacement en mode exploration

Pour améliorer le feedback visuel, le jeu doit inclure des animations simples lors des combats et de la navigation.
- Lorsque le joueur clique sur une salle adjacente, son icône se déplace de manière
fluide :
    - Interpolation linéaire de la position actuelle vers la nouvelle position
    - Durée : 0.5 secondes
    - Pas de téléportation instantanée
- La salle visitée change d’apparence (devient grisée) après l’arrivée du joueur

### US-17 - Arbre de compétences

Le joueur dispose d’un arbre de compétences permanent accessible depuis le menu principal.

Points de compétence :
- Le joueur gagne 1 point par boss d’étage vaincu
- Les points persistent entre les runs
- Les points peuvent être dépensés et récupérés librement pour réorganiser l’arbre

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

Les bonus s’appliquent au début de chaque run.

### US-18 - Jouer à la manette

Le joueur peut choisir d’utiliser le clavier et la souris, ou une manette pour jouer.

### US-19 - Génération procédurale des étages

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

### US-20 - Sauvegarder sa progression, et continuer

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

### US-21 - Choisir un niveau de difficulté

Lorsque le joueur lance un nouvelle partie, celui-ci peut décider d’un niveau de difficulté. Plus le niveau de difficulté est élevé, plus les adversaires seront coriaces.

### US-22 - Intelligence artificielle de combat

L’ordinateur doit s’adapter à la situation du combat de sorte à effectuer une action pertinente à chaque tour, y compris les objets et le changement de Bugémon sur le champ de bataille.

### US-23 - Générer une équipe dynamiquement

Chaque étage de la tour de combat est générée dynamiquement en fonction de la difficulté choisie. Cela doit alors prendre en compte les types de Bugémons dans l’équipe du joueur.

### US-24 - Musique d’ambiance

La musique du jeu varie en fonction de la situation dans laquelle le joueur se trouve (création d’équipe, combat, exploration).

### US-25 - Défier un autre joueur

En mode multi-joueurs, chaque joueur peut défier un autre joueur connecté. Si ce dernier accepte le défi, le combat se lance.

### US-26 - Chat

En mode multi-joueurs, un salon de chat est disponible pour pouvoir communiquer avec les autres joueurs.

### US-27 - Filtrer les messages inappropriés

Dans le chat, les messages inappropriés (obscènes, insultants, …) sont adoucis en remplaçant toutes les lettres par des astérisques, à l’exception de la première et de la dernière lettre des mots adoucis.

### US-28 - Leaderboard

En mode multijoueurs, à chaque fois que 2 personnes s’affrontent, celle qui emporte la victoire gagne 1 point. Il y a ensuite un classement qui permet de voir quels joueurs ont obtenu le plus de points.

### US-A - Abandonner la partie

Bouton permettant d’arrêter la partie (donc d’abandonner l'ascension de la tour NO). Diffère du bouton “fuite” qui ne fait que abandonner le combat en cours, sans finir la partie.

### US-R - Refactoring

**Refactoring - general**:

- Mettre à jour JUnit 5 sur toutes les branches
- Mettre à jour Google Docs
- Update checkTeamKO(), gère des équipes de 1 à 6 maintenant
- homogenéiser effects (value/modifier -> modifiers, String -> ENUM)
- refactor tests objets avec samples et les decaler de TestBattleController a TestItems
- Supprimer Bugemon.getId(), ou le renommer en Bugemon.getSpeciesId() (petite tâche)

**Refactoring − Repository & Services**:

- Déplacer les parsers dans la couche repository (temps mis : 5min)
- Chargement des items
- Chargement des abilités
- Chargement des bugémons
- Créer un nouveau sous-module json.parser pour mieux marquer la distinction entre loaders et parsers
- BugemonService
- ItemService
- Chargement et injection des services (besoin de tirer au clair l’architecture du programme avant de faire ça)
- Supprimer BugemonDatabase, AbilityDatabase, model.sample, etc. (grosse tâche)
- Supprimer les constructeurs dépréciés de Bugemon (grosse tâche)

**Refactoring - Battle et BattleController**

- Refactor BattleController, BattleSnashot et Battle en fonction du diagramme
- Créer useAction dans Battle
- Afficher les logs (“bugemon attaque, fait x dégats, …”) dans la view
- instancier BattleController et battle (via des threads pour BattleController)
- Déplacer le choix random des actions dans Strategy et l’instancier au début du combat
- Griser les actions non disponibles dans la view
