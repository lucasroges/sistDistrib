package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import model.Message;

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
        // TODO: ver slide 23
        int valorInteiro = 0;
        int somatorio = 0;
        System.out.println("executando client (consumidor)");
        while (true) {
            Message msg = new Message("POP", (short) 0);
            try (Socket client = new Socket(host, port);
                    ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream objectIn = new ObjectInputStream(client.getInputStream());) {
                System.out.println("preparação ok...");
                // TODO: precisa abrir dnv a conexão, ver slides
                System.out.println("cliente envia solicitacao ao servidor");
                // enviar solicitação ao servidor
                objectOut.writeObject(msg);

                System.out.println("espera resposta");
                // receber resposta e extrair valor
                msg = (Message) objectIn.readObject();
                if ((msg.getType()).equals("RET_POP")) {
                    valorInteiro = msg.getValue();
                    System.out.println("conseguiu: " + valorInteiro);
                }
                System.out.println("fecha conexão");
                client.close();
                somatorio += valorInteiro;

                if (valorInteiro == 0) {
                    System.out.println("pilha vazia!");
                    System.out.println("somatorio: " + somatorio);
                    return;
                }
            } catch (final Exception e) {
                e.printStackTrace();
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
