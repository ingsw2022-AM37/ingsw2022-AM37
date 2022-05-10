package it.polimi.ingsw.am37.message;

public class UpdatableObject {
    UpdatableType type;

    public UpdatableObject(UpdatableType type) {
        this.type = type;
    }

    public enum UpdatableType {
        ISLAND("islands"),
        CLOUD("clouds"),
        BOARD("boards"),
        PLAYER("players"),
        CHARACTER("characters");

        private final String label;

        UpdatableType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
