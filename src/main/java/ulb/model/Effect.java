package ulb.model;



public class Effect {
    private final String type;
    private final String targetType;
    private final int value;



    public Effect(String type, String targetType, int value) {
        this.type = type;
        this.targetType = targetType;
        this.value = value;
    }
    // Getters
    public String getType() { return this.type; }
    public String getTargetType() { return this.targetType; }
    public int getValue() { return this.value; }

    public int apply(Bugemon target) {return 0;}
}