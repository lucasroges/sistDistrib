package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import model.StackManager;

/**
 * Implementation of server.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Server {

    private final int port;
    // TODO: ter um manager com os dispositivos da rede aqui
    private final StackManager stack;

    /**
     * Creates a server.
     *
     * @param port Port to listen.
     */
    public Server(int port) {
        this.port = port;
        stack = new StackManager();
    }

    /**
     * @return The references stack from this server.
     */
    public StackManager getStack() {
        return stack;
    }

    /**
     * Execute the server.
     *
     * @throws IOException In case of IO error.
     */
    public void execute() throws IOException {
        System.out.println("[Server] executa servidor");
        try (ServerSocket servidor = new ServerSocket(this.port)) {
            System.out.println("[Server] Porta " + port + " aberta!");

            while (true) {
                Socket client = servidor.accept();
                System.out.println("[Server] Nova conexão com o cliente " + client.getInetAddress().getHostAddress());

                ProxyClient tc = new ProxyClient(client, this);
                System.out.println("[Server] start no proxy client");
                new Thread(tc).start();

                try {
                    System.out.println("[Server] espera 3s");
                    Thread.sleep(3000);
                    System.out.println("[Server] acordou");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
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
