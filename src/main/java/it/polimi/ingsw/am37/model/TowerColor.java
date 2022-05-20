package it.polimi.ingsw.am37.model;

/**
 * In matches with two or three players each player control towers with a specific color, 'NONE' is used where a tower
 * hasn't been set yet
 */
public enum TowerColor {
    WHITE("default"),
    BLACK("black"),
    GRAY("white"),
    NONE(null);

    /**
     * Represent the color name as indicated in Jansi library
     * @see org.fusesource.jansi.Ansi.Color
     */
    public final String color;

    TowerColor(String color) {
        this.color = color;
    }
}