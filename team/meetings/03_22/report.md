# Réunion 7 | 22 mars 

## MVC
- voir l'onglet MVC du draw.io dans le channel Discord <#documents> 
- on utilise `Message` comme dans client-serveur pour toute interaction entre la vue et le contrôleur (plus facile pour maintenir le code par après avec le multijoueur)
- on utilise des DTO à mettre dans les messages
- bouger les `switchToWindow` dans par exemple `ViewController` (qui fait partie de view) pour ne plus avoir de javafx/fxml dans le contrôleur
- commencer par remplacer les interactions view <-> controller et puis passer à controller <-> model
- avoir plus de contrôleurs (un par window?)
- la vue demande toutes les informations dont elle a besoin pour l'affichage au contrôleur qui les envoie à la vue
- noter dans le google doc dans la section refactor sur quoi on va travailler dès qu'on commence une session pour éviter que plusieurs binômes travaillent sur la même chose

## Tâches qui restent pour les histoires (à faire après le refactor mvc)
- finir d'établir la connexion pour le multijoueur
- connecter le level up du bugemon (fenêtre `LevelUpWindow`) avec le reste du code pour pouvoir choisir et recevoir une récompense

## Estimation des heures
- réunion pour expliquer/se mettre d'accord sur les estimations **dimanche 29 mars à 14h**
- remplir le google sheets avec nos estimations + penser à comment diviser les histoires plus grandes avant la réunion

## Divers
- on garde les mêmes binômes que cette semaine

dites moi si j'ai mal compris quelque chose ou s'il manque des infos pour le mvc, je suis pas sûre d'avoir tout bien compris :)