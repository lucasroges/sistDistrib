package model;

import java.io.Serializable;

/**
 * Representing a message.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Message implements Serializable {

    /**
     * Types: POP, PUSH, RET_POP
     */
    public TYPE type;

    /**
     * Messages types allowed.
     */
    public enum TYPE {
        POP("POP"), PUSH("PUSH"), RET_POP("RET_POP");

        private final String key;

        private TYPE(final String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }

    }
    public short value;

    /**
     * Creates a message.
     *
     * @param type Message type.
     * @param value Message value.
     */
    public Message(final TYPE type, short value) {
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
    public void setValue(short n) {
        value = n;
    }

    /**
     * @return The message value.
     */
    public short getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "msg(" + type + ", " + value + ")";
    }

}
