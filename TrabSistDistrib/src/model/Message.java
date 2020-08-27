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
    public String tipo;
    public short numero;

    /**
     * Creates a message.
     *
     * @param type Message type.
     * @param value Message value.
     */
    public Message(final String type, short value) {
        this.tipo = type;
        this.numero = value;
    }

    /**
     * Creates an empty message.
     */
    public Message() {
        this("", (short) 0);
    }

    /**
     * Sets the type.
     *
     * @param t Type to set.
     */
    public void setType(final String t) {
        tipo = t;
    }

    /**
     * @return The message type.
     */
    public String getType() {
        return tipo;
    }

    /**
     * Sets the value.
     *
     * @param n Value to set.
     */
    public void setValue(short n) {
        numero = n;
    }

    /**
     * @return The message value.
     */
    public short getValue() {
        return numero;
    }

}
