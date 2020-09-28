package CausalMulticast;

import model.Message;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Sep 21.
 */
public interface ICausalMulticast {

    /**
     * Method to deliver a message.
     *
     * @param msg Message to deliver.
     */
    void deliver(Message msg);

}
