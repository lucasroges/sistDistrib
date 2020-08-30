/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;
import model.Message;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 28.
 */
public class Producer {

    private static final short MIN = 1, MAX = 32767;

    private final String host;
    private final int port;

    /**
     * Creates a producer.
     *
     * @param host Client host.
     * @param port Port to send.
     */
    public Producer(final String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Execute the producer.
     *
     * @throws UnknownHostException In case of unknown host.
     * @throws IOException In case of IO error.
     */
    public void execute() throws UnknownHostException, IOException {
        System.out.println("[Producer] executando client (consumidor)");
        int i = 0;
        Message msg;
        short randomNum = 0;
        while (true) {
            if (++i > 4) {
                randomNum = (short) ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
            }
            msg = new Message(Message.TYPE.PUSH, (short) randomNum);
            try (Socket client = new Socket(host, port);
                    ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream objectIn = new ObjectInputStream(client.getInputStream())) {
                System.out.println("[Producer] preparação ok...");
                System.out.println("[Producer] envia mensagem " + msg + " ao servidor");
                // enviar solicitação ao servidor
                objectOut.writeObject(msg);

                System.out.println("[Producer] fecha conexão");
                client.close();
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
        new Producer("127.0.0.1", 12345).execute();
    }

}
