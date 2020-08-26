package control;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Thread to deal with the client message. This class should treat the clients massage.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class ProxyCliente implements Runnable {

    private final Socket client;
    private final Server server;

    /**
     * Default construct.
     *
     * @param client Referenced client.
     * @param server Referenced server.
     */
    public ProxyCliente(final Socket client, final Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try (Scanner s = new Scanner(client.getInputStream())) {
            // just sending the message from the input and sending to all the clients
            while (s.hasNextLine()) {
                String msg = s.nextLine();
                server.spreadMessage(client, msg);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
