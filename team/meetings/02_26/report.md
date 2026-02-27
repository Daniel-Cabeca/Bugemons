# Réunion 3 | 26 février
## Décisions prises
### Branches GitLab
- Une branche par groupe de fonctionnalités (par exemple: équipe, combat, tour NO)
- On merge dans main après avoir fini une histoire
- Avant de merge dans main, il faut merge dans notre branche
- Essayer de merge main dans notre branche le plus souvent possible
### Conventions de code
- Configurer dans nos IDE l'utilisation de tab
- Pour les getters/setters simples, on les fait sur une ligne
- Si on veut modifier les conventions, on demande dans le channel code et on fait un poll
- Est ce qu'on veut maintenir un UML pour avoir une visualisation du code?

## Rôles/tâches attribués
- Faire un résumé de la 1e réunion avec le client en reprenant les notes de tout le monde - Rugilė
- Bookkeeper des réunions avec le client - Alexis
- Organiser les user stories (fichier Markdown, préparation pour la réunion avec le client, etc.) - Abdal
- Ajouter les tâches des histoires sur GitLab - Wael (puis chacun modifie pour s'attribuer la tâche)
## Tâches pour les histoires
### Histoire 1 : Constituer une équipe
- Parse fichiers json des bugemons, charger les sprites
- Classe `Team`
- Assurer l'unicité des bugemons
- Connecter avec interface
### Histoire 4 : Combat automatique
- Générer une équipe adverse aléatoire
- Contrôle de combat (classe `Battle`)
    - Gestion des attaques
    - Gestion des dégâts
    - Initiative/qui joue en premier
    - Gestion des états (Bugemon KO, fin de partie, etc.)
- Contrôle de combat (Controller)
    - Sélectionner une attaque aléatoire
- Gestion fin de combat (XP, stats, etc.)
- Interface
    - Générer messages/log
    - Afficher nom, sprite, PV
### Histoire 5 : Contrôle des actions en combat
- A voir après avoir fait le combat automatique
- Controller pour choix d'action
- Interface
### Histoire 6 : Calcul de dégâts avec statistiques
- Calcul -> directement dans l'histoire 4 (gestion des dégâts)
- Interface
    - types de bugemon et attaques
    - indicateur d'efficacité + messages de combat
### Histoire 7 : Expérience et montée de niveau
- Calcul -> directement dans l'histoire 4 (gestion fin de combat)
- Bonus de montée de niveau
- Interface
    - Niveau du bugemon
    - Menu de choix de bonus
### Histoire 10 : Utiliser un objet
- Parse fichier json objets
- Controller : utilisation des objets + inventaire
- Gestion des effets des objets
- Interface : bouton pour utiliser un objet + menu choix d'objet
### Histoire additionnelle: abandonner partie
- Bouton pour revenir en arrière (pour l'instant c'est revenir au menu principal)