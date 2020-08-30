package control;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 30.
 */
public class Server {

    /**
     * URL to access Incrementa.
     */
    public static final String HOST_URL = "rmi://localhost/control/Incrementa";

    /**
     * URL to access StackManager.
     */
    public static final String HOST_URL_PUSH = "rmi://localhost/control/StackManagerImpl";

    /**
     * Executes this class.
     *
     * @param args Command line arguments (ignored).
     */
    public static void main(final String[] args) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Incrementa c = new Incrementa();
            Naming.bind(HOST_URL, c);

            System.out.println("[Server] Registrando StackManager...");
            StackManagerImpl manager = new StackManagerImpl();
            Naming.bind(HOST_URL_PUSH, manager);
            System.out.println("[Server] ok");
        } catch (final RemoteException | AlreadyBoundException | MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
}
