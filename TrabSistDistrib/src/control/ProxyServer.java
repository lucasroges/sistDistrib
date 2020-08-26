package control;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Thread to deal with the server message. This class should receive the message from the server.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class ProxyServer implements Runnable {

    private final InputStream server;

    /**
     * Default construct.
     *
     * @param server Referenced server stream.
     */
    public ProxyServer(final InputStream server) {
        this.server = server;
    }

    @Override
    public void run() {
        // just receiving messages from the server and printing on the screen
        try (Scanner s = new Scanner(this.server)) {
            while (s.hasNextLine()) {
                System.out.println(s.nextLine());
            }
        }
    }
}
