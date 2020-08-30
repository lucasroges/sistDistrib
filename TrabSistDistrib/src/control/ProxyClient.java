package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import model.Message;

/**
 * Thread to deal with the client message. This class should treat the clients massage.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class ProxyClient implements Runnable {

    private final Socket client;
    private final Server server;

    /**
     * Default construct.
     *
     * @param client Referenced client.
     * @param server Referenced server.
     */
    public ProxyClient(final Socket client, final Server server) {
        this.client = client;
        this.server = server;

        System.out.println("[ProxyClient] Nova thread");
    }

    @Override
    public void run() {
        System.out.println("[ProxyClient] rodando thread proxy client");
        Message msg;

        // ligação de streams de entrada e saída
        try (ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream objectIn = new ObjectInputStream(client.getInputStream())) {
            System.out.println("[ProxyClient] espera mensagem");
            // lê solicitação de serviço
            msg = (Message) objectIn.readObject();
            System.out.println("[ProxyClient] recebeu mensagem " + msg);
            switch (msg.getType()) {
                case POP:
                    System.out.println("[ProxyClient] retira da pilha e envia o valor ao consumidor");
                    // enviar resposta ao servidor
                    msg.setType(Message.TYPE.RET_POP);
                    msg.setValue(server.getStack().pop());
                    objectOut.writeObject(msg);
                    break;

                case PUSH:
                    server.getStack().push(msg.getValue());
                default:
                    break;
            }
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
