package model;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Sep 21.
 */
public interface CausalMulticast {

    void mcsend(Message msg, ICausalMulticast cast);

}
