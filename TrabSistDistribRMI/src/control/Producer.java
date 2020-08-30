package control;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 30.
 */
public class Producer {

    private static final short MIN = 1, MAX = 10;

    /**
     * Executes this class.
     *
     * @param args Command line arguments (ignored).
     */
    public static void main(final String[] args) {
        try {
            int i = 0;
            short randomNum = 0;
            StackManager lookup = (StackManager) Naming.lookup(Server.HOST_URL_PUSH);
            while (true) {
                if (++i > 4) {
                    randomNum = (short) ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
                }
                System.out.println("[Producer] Push " + randomNum);
                lookup.push((short) randomNum);
                Thread.sleep(3000); // producer sleeps 3s before the next push
            }
        } catch (final InterruptedException | NotBoundException | MalformedURLException | RemoteException ex) {
            ex.printStackTrace();
        }
    }

}
