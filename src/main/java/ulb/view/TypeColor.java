package ulb.view;

import ulb.model.type.Type;

public class TypeColor {

    /**
     * Maps a bugemon type to a UI color code.
     *
     * @param type The bugemon type
     * @return The corresponding hex color string
     */
    public static String getTypeColor(Type type) {
        return switch (type) {
            case PYRO -> "#ED2424";
            case FLORA -> "#50A346";
            case AQUA -> "#51B0F0";
            case LITHO -> "#807979";
            default -> "#ced4da";
        };
    }

}
