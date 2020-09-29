package CausalMulticast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.ServerSocket;
import model.Message;
import client.Client;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @author Lucas Roges de Araujo (lraraujo@inf.ufsm.br)
 * @version 2020, Sep 21.
 */
public class CMChannel extends Thread {

    private final Client client;
    private List<Message> buffer;
    
    Boolean isBlocked;

    /**
     * Creates a communication channel.
     *
     * @param client Client to manage.
     */
    public CMChannel(final Client client) {
        if (client == null) {
            throw new IllegalArgumentException("CMChannel can't be created with invalid client.");
        }
        this.client = client;
        this.buffer = new ArrayList<>();
        this.isBlocked = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Method which outputs a string with synchronization
     * 
     * @param string String to output
     */
    public synchronized void syncOutput(String string) {
        System.out.println(string);
    }

    /**
     * Method which implements the causal ordering.
     *
     * @param message Message to treat.
     */
    public void causalOrdering(final Message message) {
        // atrasa entrega
        for (int i = 0; i < this.client.getVC().size(); i++) {
            this.isBlocked = true;
            while (message.getVC().get(i) > this.client.getVC().get(i));
            this.isBlocked = false;
        }
        // atualiza variável de controle
        if (!this.client.getHost().equals(message.getSender())) {
            int senderIndex = this.client.getIpAddresses().indexOf(message.getSender());
            synchronized(this) {
                this.client.getVC().set(senderIndex, this.client.getVC().get(senderIndex) + 1);
            }
        }
    }

    /**
     * Receive messages and create a thread to manage causal ordering and delivery for each message
     */
    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            // cria um server socket na porta 2020 para receber conexões
            ss = new ServerSocket(2020);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if (ss != null) {
            while (true) {
                Socket s;
                try {
                    // escuta por conexões e as recebe
                    s = ss.accept();
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    final Message m = (Message) in.readObject();
                    new Thread(() -> {
                        // ordenamento causal
                        causalOrdering(m);
                        // envia para a aplicação tratar a mensagem
                        client.deliver(m);
                    }).start();
                    // fecha o socket
                    s.close();
                } catch (final IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send a message to the given IP address.
     *
     * @param ip Given IP address to send the message.
     * @param message Message to send.
     * @throws IOException In case of IO exception.
     */
    public void send(final String ip, final Message message) throws IOException {
        try (Socket s = new Socket(ip, 2020)) {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(message);
            //String str = "[Mensagem enviada]\nConteúdo: " + message.getMsg();
            //syncOutput(str);
        }
    }

    /**
     * Method to send a message.
     *
     * @param msg Message to send.
     * @throws IOException In case of IO exception.
     * @throws UnknownHostException In case of unknown host.
     * @throws java.lang.InterruptedException In case of interruption during sleep
     */
    public void mcsend(final Message msg) throws IOException, UnknownHostException, InterruptedException {
        // para obter entrada do cliente
        Scanner sc = new Scanner(System.in);
        String in;
        // aguarda outros outputs
        Thread.sleep(1000);
        if (!this.isBlocked) {
            // enviar alguma mensagem do buffer, se houver
            if (!buffer.isEmpty()) {
                try {
                    syncOutput("Enviar alguma mensagem do buffer? (Se sim, digitar o índex a partir de 0)");
                    in = sc.nextLine();
                    if (Integer.parseInt(in) < buffer.size()) {
                        int bufferIndex = Integer.parseInt(in);
                        syncOutput("Qual o destino da mensagem? (Digitar o índex a partir de 0)");
                        in = sc.nextLine();
                        if (Integer.parseInt(in) < this.client.getIpAddresses().size()) {
                            send(this.client.getIpAddresses().get(Integer.parseInt(in)), buffer.get(bufferIndex));
                        }
                    }
                } catch (final NumberFormatException e) {
                    // ignora e segue, pois não quer enviar mensagem do buffer
                }
            }
            // multicast
            syncOutput("Enviar para todos? [Sim/Nao]");
            in = sc.nextLine();
            Boolean doMulticast = in.equalsIgnoreCase("Sim");
            int hostIndex = this.client.getIpAddresses().indexOf(this.client.getHost());
            // constroi o timestamp da msg (apenas após a resposta do usuário)
            msg.setVC(new ArrayList<>(this.client.getVC()));
            // adiciona msg ao buffer
            buffer.add(msg);
            for (String ip : this.client.getIpAddresses()) {
                if (doMulticast) {
                    send(ip, msg);
                } else {
                    syncOutput("Enviar para o usuario " + ip + "? [Sim/Nao]");
                    in = sc.nextLine();
                    if (in.equalsIgnoreCase("sim")) {
                        send(ip, msg);
                    }
                }
            }
            // atualizar o vetor de relógios
            this.client.getVC().set(hostIndex, this.client.getVC().get(hostIndex) + 1);
            // mostra o conteúdo do buffer de mensagens recebidas
            String out = "[Buffer]\n";
            for (Message m : this.buffer) {
                out = out + "| " + m.getMsg() + " |";
            }
            syncOutput(out);
        }
            
    }

}
