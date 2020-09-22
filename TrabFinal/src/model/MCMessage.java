package model;

import java.io.Serializable;

/**
 * Representing a message used in IP multicast.
 *
 * @author Lucas Roges de Araujo
 * @version 2020, Set 22.
 */
public class MCMessage implements Serializable {

    /**
     * Types: JOIN, RET_JOIN, EXIT
     */
    public TYPE type;

    /**
     * Messages types allowed.
     */
    public enum TYPE {
        JOIN("JOIN"), RET_JOIN("RET_JOIN"), EXIT("EXIT");

        private final String key;

        private TYPE(final String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }

    }
    
    public String value;

    /**
     * Creates a message.
     *
     * @param type Message type.
     * @param value Message value.
     */
    public MCMessage(final TYPE type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Sets the type.
     *
     * @param t Type to set.
     */
    public void setType(final TYPE t) {
        type = t;
    }

    /**
     * @return The message type.
     */
    public TYPE getType() {
        return type;
    }

    /**
     * Sets the value.
     *
     * @param n Value to set.
     */
    public void setValue(String n) {
        value = n;
    }

    /**
     * @return The message value.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "msg(" + type + ", " + value + ")";
    }

}
