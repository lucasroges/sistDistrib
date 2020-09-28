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

    // sender ip
    public String sender;
    
    // information?
    public String msg;
    
    // VC
    public List<Integer> VC;

    /**
     * Creates a message.
     *
     * @param sender
     * @param msg
     */
    public Message(String sender, String msg) {
        this.sender = sender;
        this.msg = msg;
    }
}
