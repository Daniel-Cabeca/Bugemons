package ulb.DTO.team;

import ulb.DTO.bugemon.BugemonDTO;

import java.io.Serializable;
import java.util.List;

public record TeamDTO (int id, String teamName, List<BugemonDTO> members) implements Serializable {}
