# réunion 4 | 3 mars
## Binômes
- comme décidé avant, on change toutes les semaines de binôme aléatoirement
## Répartition des tâches
- google doc (fait par Sasha) pour suivre les tâches qui sont à faire, en cours ou accomplies (+ binômes, heures, etc.), au lieu des channels discord
- ajouter une petite explication de ce qu'on a fait, surtout pour des classes/méthodes plus complexes
- si on fait du refactoring, expliquer ce qui a été changé et pourquoi
## UML
- sur draw.io (fait par Alexis) comme ça tout le monde peut modifier
- un diagramme général simplifié pour avoir une vue d'ensemble, et voir les classes qui sont reliées
- si besoin, ajouter un autre diagramme plus détaillé pour les classes/méthodes plus compliquées (dans un nouveau tab)
## Burndown chart
- Daniel C s'en occupe pour la 1e itération
## Conditions pour les commits
- ne pas commit son code (si possible) s'il ne passe pas les tests
- si on n'arrive pas à résoudre un bug, on peut commit le code (en mettant en commentaire la partie qui passe pas les tests) et on demande directement de l'aide dans <#1468666490852212760> (ou <#1468666591100145835> si moins important)
## Code
- créer une classe `Config` pour les constantes générales
- classe `GameData` pour charger les données
- faire des tests surtout pour model (et repository), pas obligatoire pour controller, pas besoin pour view
