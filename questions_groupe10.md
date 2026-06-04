1. Ce projet propose `DatabaseRepository`, `JsonRepository`, et `InMemoryRepository` pour la même interface. Expliquez les cas d'usage de chaque implémentation. 

2. Comment se placent les services dans votre architecture ? Et les managers ?

3. Dans le package DTO, certains objets sont implémentés sous forme de record (BugemonDTO, TeamDTO, StatsDTO, ItemDTO, AbilityDTO), tandis que d'autres sont implémentés sous forme de classes classiques (PlayerDTO, EffectDTO, EffectHealDTO, EffectStatModifierDTO). Quelle est la différence entre un record et une classe Java classique ? Quels critères devraient guider le choix entre ces deux approches ? Pourquoi avoir utilisé les deux au sein d'un même package ?


4. `Player.getUserId()` retourne `Optional<Integer>`. Pourquoi ne pas simplement retourner `int` ou `Integer` ? 

5. Que pensez-vous de la qualité de code de cette méthode dans `Team` ?
```java
	public boolean add(Bugemon bugemon) {
		if (bugemon == null) {
			return false;
		}

		if (this.members.size() >= MAX_PARTY_SIZE) {
			return false;
		}

		if (containsName(bugemon.getName())) {
			return false;
		}

		this.members.add(bugemon);
		return true;2
	}
```

6. Expliquez pourquoi les valeurs des attributs sont passées par constructeur. Quels sont les avantages ?

```java
public class ChatService {
	private final ChatRepository repository;
	private final InappropriateWordFilter inappropriateWordFilter;

	public ChatService(ChatRepository repository) {
		this(repository, new InappropriateWordFilter());
	}

	public ChatService(ChatRepository repository, InappropriateWordFilter inappropriateWordFilter) {
		this.repository = repository;
		this.inappropriateWordFilter = inappropriateWordFilter;
	}
    //...
}
```

7. Ce projet a 329 fichiers de tests. Expliquez ce que signifie le TDD. Quelle est la différence entre un test unitaire et un test d'intégration ?

8. `HasId` et `IdSet<T extends HasId> implements Iterable<T>` : expliquez comment la généricité est utilisée dans votre projet. Quel est son but et quels avantages offre-t-elle ?

9. Dans la classe `SocialPanelController`, on trouve de la gestion de threads. Cette responsabilité est-elle conforme à ce rôle ? Si ce n'est pas le cas, quel composant devrait être chargé de la gestion des threads ?

10. Vous utilisez de manière très fréquente la classe `Exception` pour gérer les exceptions. Pourquoi cette pratique est-elle généralement déconseillée ? Quels problèmes peuvent survenir lorsqu'on utilise systématiquement `Exception` au lieu d'exceptions plus spécifiques ?
