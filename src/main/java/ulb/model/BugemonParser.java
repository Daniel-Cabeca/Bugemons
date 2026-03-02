package ulb.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Vector;

import ulb.model.type.Type;

public class BugemonParser {

    public static Vector<Bugemon> loadBugemons(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(path));
        JsonNode bugemonArray = root.get("bugemons");
        Vector<Bugemon> bugemons = new Vector<>();

        for (JsonNode node : bugemonArray) {
            String name = node.get("nom").asText();
            String sprite = node.get("sprite").asText();
            String typeStr = node.get("type").asText().toLowerCase();
            Type type = Type.valueOf(typeStr.toUpperCase());

            JsonNode statsNode = node.get("stats");
            int pv = statsNode.get("pv").asInt();
            int attack = statsNode.get("attaque").asInt();
            int defense = statsNode.get("defense").asInt();
            int initiative = statsNode.get("initiative").asInt();

            Vector<String> abilities = new Vector<>();
            for (JsonNode abilityNode : node.get("attaques")) {
                abilities.add(abilityNode.asText());
            }

            Bugemon bugemon = new Bugemon(
                    name,
                    sprite,
                    type,
                    pv,
                    attack,
                    defense,
                    initiative,
                    abilities,
                    1
            );

            bugemons.add(bugemon);
        }

        return bugemons;
    }
}
