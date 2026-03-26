# Réunion Coach 1 | 17 mars

## Organisation
- Il faut mettre un `readme.md` pour savoir comment exécuter le code, quelles sont les versions utilisées…
- Il faut plus communiquer avec le client hors des réunions dans le cas où on ne finit pas une histoire
- Si les histoires sont trop conséquentes, les diviser en sous-histoire avant de voir le client
- En moyenne, il faut prendre 20% du temps de chaque itération à organiser le code, l’architecture, le groupe, …

## Code
- Ne pas utiliser de RunTimeException car elles se font à l’exécution du code plutôt qu’à la compilation → Privilégier les exceptions qui se font lors de la compilation
- Clean le code avant de le rendre : retirer les prints, mettre des docstrings, …
- Les classes ne doivent pas dépasser les 300-350 lignes (sans compter la javadoc) et rendre le nom des classes plus compréhensibles de ce qu’elles font
- Les méthodes ne doivent pas dépasser les 15 lignes (le coach a juste dit que certaines sont trop longues, mais le prof avait précisé pendant le cours qu’une méthode ne devrait pas faire plus de 15 lignes)
- Retirer les classes qui sont vides, si elles le sont c’est qu’elles ne sont pas nécessaires (ex : `Action`). Pareil pour les dossiers vides 
- C’est mieux d’avoir un nom (de classe, méthode, …) un peu plus long mais explicite plutôt que court et ambigu

## Tests
- Certains testent plus d’une chose, ce qui va à l’encontre du principe de division des pouvoirs de l’orienté objet
- C’est plus important de faire des tests pour le Model plutôt que pour le Controller.
- Certains tests sont vides (ne font pas de `assert`), il faut soit les corriger, soit les retirer

## MVC
### Model
- Contient toute la logique du code, c’est lui qui applique complètement les actions qui ont été demandées par le joueur (ex : le joueur choisit une ability : le modèle regarde s’il peut l’utiliser, l’applique, calcul les dégâts infligés, …)
- Dans la tour du NO, il décide quand l’étage est fini, quel est le prochain combat / récompense, …
- Le coach n'avait pas de commentaires sur le Model (à part les commentaires généraux sur le code)

### View
- Doit exclusivement afficher des choses que lui envoie le Controller et le notifier lorsque le joueur fait un input (en spécifiant seulement l’input qui à été fait)
- Notre View gère trop de choses qui devrait être laissé à l’application ou à des classes supérieurs (`extends Application` dans `MainWindow`, création de thread), c’est le rôle de la classe `Main`.
- On ne doit pas non plus instancier de Models ni de Controllers, mais les recevoir d’autres classes (`Main `ou autre)
- Dans le cas où on veut accéder à des données du Model (`Bugemon`, `Ability`, ...), il vaut mieux utiliser des DTO via des interfaces afin de limiter l’accès de la View à des getters.

### Controller
- Doit simplement servir d’intermédiaire entre View et Model. Doit récupérer l’input de la View, comprendre ce que c’est et dire l’action à fait au Model.
- Il ne doit contenir aucun code faisant référence à du FXML
- Va décider ce que la View doit afficher, griser, … Cependant, il ne doit pas communiquer en FXML mais en messages que la View va devoir interpréter

## Client - Server
- C’est bien d’utiliser des DTO qui implémente `serializable` et qui sont des autres classes que les objets qu’on utilise déjà dans le Model (créer une autre classe pour chaque objet spécial pour être envoyé via le socket)

## Utils
- Contient le code qui ne peut pas être classé dans le MVC, qui fait plus partie de l’application en elle-même que du jeu (comme le code qui gère la taille des boutons en fonction de la taille de la fenêtre)