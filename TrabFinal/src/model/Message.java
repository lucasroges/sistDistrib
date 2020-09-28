package model;

import java.io.Serializable;
import java.util.List;

/**
 * Representing a message.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Message implements Serializable {

    private final String sender;
    private final String msg;
    private List<Integer> VC;

    /**
     * Creates a message.
     *
     * @param sender Sender for this message.
     * @param msg String message to send.
     */
    public Message(final String sender, final String msg) {
        this.sender = sender;
        this.msg = msg;
    }

    /**
     * Gets the sender.
     *
     * @return The sender string.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the message.
     *
     * @return The message string.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Gets the VC list.
     *
     * @return The VC list.
     */
    public List<Integer> getVC() {
        return VC;
    }

    /**
     * Sets a new list for VC.
     *
     * @param VC New list to set.
     */
    public void setVC(final List<Integer> VC) {
        this.VC = VC;
    }

}
