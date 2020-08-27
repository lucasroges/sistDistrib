package control;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
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

    private static final short MIN = 1, MAX = 32767;

    private final int port;
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
        System.out.println("executa servidor");
        System.out.println("inicializa pilha");
        for (byte i = 1; i <= 4; i++) {
            stack.push((short) 0); // 4 zeros at the begin
        }

        for (short i = 1; i < 20; i++) {
            short randomNum = (short) ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
            stack.push(randomNum);
        }

        stack.print_q();
        try (ServerSocket servidor = new ServerSocket(this.port)) {
            System.out.println("Porta " + port + " aberta!");

            while (true) {
                Socket client = servidor.accept();
                System.out.println("Nova conexão com o cliente " + client.getInetAddress().getHostAddress());

                ProxyClient tc = new ProxyClient(client, this);
                System.out.println("start no proxy cliente (produtor)");
                new Thread(tc).start();
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
