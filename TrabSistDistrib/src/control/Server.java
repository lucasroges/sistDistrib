package control;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of server.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Server {

    private final int port;
    private final List<Socket> clients = new ArrayList<>();

    /**
     * Creates a server.
     *
     * @param port Port to listen.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Execute the server.
     *
     * @throws IOException In case of IO error.
     */
    public void execute() throws IOException {
        try (ServerSocket servidor = new ServerSocket(this.port)) {
            System.out.println("Porta " + port + " aberta!");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Nova conexão com o cliente "
                        + cliente.getInetAddress().getHostAddress());

                this.clients.add(cliente);

                ProxyCliente tc = new ProxyCliente(cliente, this);
                new Thread(tc).start();
            }
        }
    }

    /**
     * Spread the message to all the clients on this server, except to the one who sent it.
     *
     * @param clientWhoSent Client who sent the message, so we don't send to him.
     * @param msg Message to send.
     */
    public void spreadMessage(final Socket clientWhoSent, final String msg) {
        for (Socket client : clients) {
            if (!client.equals(clientWhoSent)) {
                try {
                    PrintStream ps = new PrintStream(client.getOutputStream());
                    ps.println(msg);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Executes this class.
     *
     * @param args Command line arguments (ignored).
     * @throws java.io.IOException In case of IO error.
     */
    public static void main(final String[] args) throws IOException {
        new Server(12345).execute();
    }

}
