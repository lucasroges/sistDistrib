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
    }

    @Override
    public void run() {
        System.out.println("[ProxyClient] rodando thread proxy client (consumidor)");
        Message msg;

        // ligação de streams de entrada e saída
        try (ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream objectIn = new ObjectInputStream(client.getInputStream())) {
            System.out.println("[ProxyClient] espera mensagem do consumidor");
            // lê solicitação de serviço
            msg = (Message) objectIn.readObject();
            System.out.println("[ProxyClient] recebeu mensagem " + msg.getType() + " do consumidor");
            if ((msg.getType()).equals("POP")) {
                System.out.println("[ProxyClient] retira da pilha e envia o valor ao consumidor");
                // enviar resposta ao servidor
                msg.setType("RET_POP");
                msg.setValue(server.getStack().pop());

                server.getStack().print_q();
                objectOut.writeObject(msg);
            }
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // TODO: ver slide 25
    }
}
