package it.polimi.ingsw.am37.message;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UpdatableObject {
    UpdatableType type();

    enum UpdatableType {
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
