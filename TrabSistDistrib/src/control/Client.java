package control;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Client implementation.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Client {

    private final String host;
    private final int port;

    /**
     * Creates a client.
     *
     * @param host Client host.
     * @param port Port to send.
     */
    public Client(final String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Execute the client.
     *
     * @throws UnknownHostException In case of unknown host.
     * @throws IOException In case of IO error.
     */
    public void execute() throws UnknownHostException, IOException {
        try (Socket cliente = new Socket(host, port);
                Scanner teclado = new Scanner(System.in);
                PrintStream saida = new PrintStream(cliente.getOutputStream())) {
            System.out.println("O cliente se conectou ao servidor!");

            ProxyServer r = new ProxyServer(cliente.getInputStream());
            new Thread(r).start();

            while (teclado.hasNextLine()) {
                saida.println(teclado.nextLine());
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
        new Client("127.0.0.1", 12345).execute();
    }
}
