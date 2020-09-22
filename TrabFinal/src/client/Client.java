//package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.net.*;
import model.MCMessage;

/**
 * Client implementation.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @author Lucas Roges de Araujo (lraraujo@inf.ufsm.br)
 * @version 2020, Set 22.
 */
public class Client extends Thread {

    private final String host;
    private final int port;

    private List<String> ipAddresses = new ArrayList<String>();

    private final String ipMulticastAddress = "239.255.0.0";


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
     * Serialize object and send it as a byte array with IP multicast
     * 
     * @param ms Socket multicast
     * @param msg Object to send
     * @param group IP Address chosen to perform IP multicast operation
     * @throws UnknownHostException
     * @throws IOException 
     */
    public void sendObject(MulticastSocket ms, MCMessage msg, InetAddress group) throws UnknownHostException, IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(100);
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(bout));
        out.writeObject(msg);
        out.flush();
        byte[] buf = bout.toByteArray();
        DatagramPacket send = new DatagramPacket(buf, buf.length, group, 6789);
        ms.send(send);
        out.close();
    }

    /**
     * Receive byte array from IP multicast comm. and desserialize it
     * 
     * @param ms Socket multicast
     * @return Desserialized object
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public MCMessage recvObject(MulticastSocket ms) throws IOException, ClassNotFoundException {
        byte[] recvBuf = new byte[1000];
        DatagramPacket dp = new DatagramPacket(recvBuf, recvBuf.length);
        ms.receive(dp);
        ByteArrayInputStream bin = new ByteArrayInputStream(recvBuf);
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bin));
        MCMessage msg = (MCMessage) in.readObject();
        in.close();
        return msg;
    }

    /**
     * Manages the ip list that controls which nodes are running the middleware
     */
    @Override
    public void run() {
        MCMessage msg = new MCMessage(MCMessage.TYPE.JOIN, this.host);
        InetAddress group = null;
        MulticastSocket ms = null;
        try {
            group = InetAddress.getByName(this.ipMulticastAddress);
            ms = new MulticastSocket(6789);
            ms.joinGroup(group);
            sendObject(ms, msg, group);
            while(true) {
                MCMessage recv = recvObject(ms);
                // verifica se o ip já está na lista, se tiver ignora a mensagem
                if (this.ipAddresses.contains(recv.value)) {
                    continue;
                }
                // tratamento de acordo com o tipo de mensagem
                if (recv.type == MCMessage.TYPE.JOIN) {
                    this.ipAddresses.add(recv.value);
                    msg.type = MCMessage.TYPE.RET_JOIN;
                    sendObject(ms, msg, group);
                } else if (recv.type == MCMessage.TYPE.RET_JOIN) {
                    this.ipAddresses.add(recv.value);
                } else if (recv.type == MCMessage.TYPE.EXIT) {
                    this.ipAddresses.remove(recv.value);
                }
                // ordena o vetor pelos ips para auxiliar no controle dos outros vetores de processos
                java.util.Collections.sort(this.ipAddresses);
                // realizar modificações nos outros vetores
                // TODO
                // mostra a lista atual
                for(int i = 0; i < this.ipAddresses.size(); i++) {
                    System.out.println(i + ": " + this.ipAddresses.get(i));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
        }
    }
    /**
     * Execute the client.
     *
     * @throws UnknownHostException In case of unknown host.
     * @throws IOException In case of IO error.
     */
    public void execute() {
        Thread thread = new Thread(this);
        thread.start();
/*        int valorInteiro = 0;
        int somatorio = 0;
        System.out.println("[Client] executando client (consumidor)");
        while (true) {
            Message msg = new Message(Message.TYPE.POP, (short) 0);
            try (Socket client = new Socket(host, port);
                    ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream objectIn = new ObjectInputStream(client.getInputStream())) {
                System.out.println("[Client] preparação ok...");
                System.out.println("[Client] envia mensagem " + msg + " ao servidor");
                // enviar solicitação ao servidor
                objectOut.writeObject(msg);

                System.out.println("[Client] espera resposta");
                // receber resposta e extrair valor
                msg = (Message) objectIn.readObject();
                System.out.println("[Client] recebeu mensagem " + msg + " do servidor");
                if ((msg.getType()).equals(Message.TYPE.RET_POP)) {
                    valorInteiro = msg.getValue();
                    System.out.println("[Client] valor recebido: " + valorInteiro);
                }
                System.out.println("[Client] fecha conexão");
                client.close();
                if (valorInteiro >= 0) {
                    somatorio += valorInteiro;
                } else {
                    System.out.println("[Client] pilha vazia");
                }
                if (valorInteiro == 0) {
                    System.out.println("[Client] encontrou zero!");
                    System.out.println("[Client] somatorio: " + somatorio);
                    return;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * Executes this class.
     *
     * @param args Command line arguments.
     * @throws java.io.IOException In case of IO error.
     */
    public static void main(final String[] args) throws IOException {
        if (args.length > 0) {
            new Client(args[0], 12345).execute();
        } else {
            new Client("127.0.0.1", 12345).execute();
        }
    }
}