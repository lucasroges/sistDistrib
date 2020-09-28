package model;

import java.io.Serializable;

/**
 * Representing a message used in IP multicast.
 *
 * @author Lucas Roges de Araujo
 * @version 2020, Set 22.
 */
public class MCMessage implements Serializable {

    private TYPE type;
    private String client;

    /**
     * Messages types allowed.
     */
    public enum TYPE {
        /**
         * Type to JOIN in a group.
         */
        JOIN("JOIN"),
        /**
         * Type for response a JOIN message.
         */
        RET_JOIN("RET_JOIN");

        private final String key;

        private TYPE(final String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }

    }

    /**
     * Creates a message.
     *
     * @param type Message type.
     * @param client Client host.
     */
    public MCMessage(final TYPE type, final String client) {
        this.type = type;
        this.client = client;
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
     * Sets a new client.
     *
     * @param client Client to set.
     */
    public void setClient(final String client) {
        this.client = client;
    }

    /**
     * @return The message client.
     */
    public String getClient() {
        return client;
    }
}
