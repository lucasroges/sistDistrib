package control;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 30.
 */
public class Client {

    /**
     * Executes this class.
     *
     * @param args Command line arguments (ignored).
     */
    public static void main(final String[] args) {
        try {
            StackManager lookup = (StackManager) Naming.lookup(Server.HOST_URL_PUSH);
            short pop;
            int sum = 0;
            while (true) {
                pop = lookup.pop();
                if (pop >= 0) {
                    sum += pop;
                    System.out.println("[Client] Pop " + pop);
                    if (pop == 0) {
                        System.out.println("[Client] Somatório: " + sum);
                        break;
                    }
                } else {
                    System.out.println("[Client] Pilha vazia");
                }
                Thread.sleep(1000); // producer sleeps 1s before the next pop
            }
        } catch (final InterruptedException | NotBoundException | MalformedURLException | RemoteException ex) {
            ex.printStackTrace();
        }
    }

}
