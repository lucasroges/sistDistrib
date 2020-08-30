package control;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 30.
 */
public class StackManagerImpl extends UnicastRemoteObject implements StackManager {

    private static final byte MAX = 15;
    private final Stack<Short> stack = new Stack<>();

    StackManagerImpl() throws RemoteException {
        // empty
    }

    @Override
    public void push(short value) throws RemoteException {
        if (stack.size() < MAX) {
            stack.push(value);
            print_q();
        }
    }

    @Override
    public short pop() throws RemoteException {
        if (!stack.isEmpty()) {
            Short val = stack.pop();
            print_q();
            return val;
        }
        return 0;
    }

    /**
     * Prints the stack.
     */
    private synchronized void print_q() {
        System.out.println("---Stack elements----");

        for (Short s : stack) {
            System.out.print(s + " | ");
        }
        System.out.println("\n-------------");
    }

}
