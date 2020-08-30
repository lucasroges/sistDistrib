package control;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 30.
 */
public interface StackManager extends Remote {

    /**
     * Pushes a value into the stack.
     *
     * @param value Value to be added.
     * @throws RemoteException In case of exception during the remote request.
     */
    public void push(short value) throws RemoteException;

    /**
     * Removes a value from the stack.
     *
     * @return Removed value.
     * @throws RemoteException In case of exception during the remote request.
     */
    public short pop() throws RemoteException;

}
