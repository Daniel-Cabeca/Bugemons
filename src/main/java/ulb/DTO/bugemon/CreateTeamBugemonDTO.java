package ulb.DTO.bugemon;

public class CreateTeamBugemonDTO {
    private String id;
    private String name;
    private String sprite;

    public CreateTeamBugemonDTO(String id, String name, String sprite) {
        this.id = id;
        this.name = name;
        this.sprite = sprite;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getSprite() {return sprite;}

    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setSprite(String sprite) {this.sprite = sprite;}
}
