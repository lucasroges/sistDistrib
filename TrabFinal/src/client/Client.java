package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.MulticastSocket;
import model.MCMessage;
import model.Message;
import CausalMulticast.ICausalMulticast;
import CausalMulticast.CMChannel;

/**
 * Client implementation.
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @author Lucas Roges de Araujo (lraraujo@inf.ufsm.br)
 * @version 2020, Set 22.
 */
public class Client extends Thread implements ICausalMulticast {

    private final String host;
    // TODO: não está sendo usado, remover
    private final int port;
    private final List<String> ipAddresses;

    private final String ipMulticastAddress = "239.255.0.0";

    private CMChannel channel;

    /**
     * Ordering control.
     */
    private final List<Integer> VC;

    /**
     * Discarding control.
     */
    private final List<List<Integer>> MC;

    /**
     * Creates a client.
     *
     * @param host Client host.
     * @param port Port to send.
     */
    public Client(final String host, int port) {
        if (host == null) {
            throw new IllegalArgumentException("Client can't be created with null parameters.");
        }
        this.host = host;
        this.port = port;
        this.ipAddresses = new ArrayList<String>() {{ add("172.31.17.152"); add("172.31.31.12"); add("172.31.19.92"); }};
        this.VC = new ArrayList<Integer>() {{ add(0); add(0); add(0); }};
        this.MC = new ArrayList<List<Integer>>();
        this.MC.add(new ArrayList<Integer>());
        this.MC.add(new ArrayList<Integer>());
        this.MC.add(new ArrayList<Integer>());
        this.MC.get(0).add(0);
        this.MC.get(0).add(0);
        this.MC.get(0).add(0);
        this.MC.get(1).add(0);
        this.MC.get(1).add(0);
        this.MC.get(1).add(0);
        this.MC.get(2).add(0);
        this.MC.get(2).add(0);
        this.MC.get(2).add(0);
    }

    /**
     * @return The host name.
     */
    public String getHost() {
        return host;
    }

    /**
     * @return A list with the IP addresses.
     */
    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    /**
     * @return The VC list.
     */
    public List<Integer> getVC() {
        return VC;
    }

    /**
     * @return The MC list.
     */
    public List<List<Integer>> getMC() {
        return MC;
    }

    /**
     * Manages the causal ordering.
     *
     * TODO: (talvez precise sincronizar o acesso ao VC).
     *
     * @param m Message to deliver.
     */
    @Override
    public void deliver(final Message m) {
        // conteudo da mensagem
        //channel.syncOutput("[Mensagem recebida]\nConteúdo: " + m.getMsg());
        // mostra o vetor de relógios
        String out = "[Vetor de relógios]\n";
        for (int i = 0; i < VC.size(); i++) {
            out = out + "| " + VC.get(i) + " |";
        }
        channel.syncOutput(out);
        // mostra a matriz de relógios
        out = "[Matriz de relógios]\n";
        for (int i = 0; i < MC.size(); i++) {
            for (int j = 0; j < MC.get(i).size(); j++) {
                out = out + "| " + MC.get(i).get(j) + " |";
            }
            out = out + "\n";
        }
        channel.syncOutput(out);
    }

    /**
     * Serialize object and send it as a byte array with IP multicast
     *
     * @param ms Socket multicast.
     * @param msg Object to send.
     * @param group IP Address chosen to perform IP multicast operation.
     * @throws UnknownHostException In case of unknown host.
     * @throws IOException In case of IO exception.
     */
    public void sendObject(final MulticastSocket ms, final MCMessage msg, final InetAddress group)
            throws UnknownHostException, IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(100);
        try (final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(bout))) {
            out.writeObject(msg);
            out.flush();
            byte[] buf = bout.toByteArray();
            DatagramPacket send = new DatagramPacket(buf, buf.length, group, 6789);
            ms.send(send);
        }
    }

    /**
     * Receive byte array from IP multicast command and deserializes it.
     *
     * @param ms Socket multicast.
     * @return Deserialized object.
     * @throws java.io.IOException In case of IO exception.
     * @throws ClassNotFoundException In case there is a class not found.
     */
    public MCMessage recvObject(final MulticastSocket ms) throws IOException, ClassNotFoundException {
        byte[] recvBuf = new byte[1000];
        DatagramPacket dp = new DatagramPacket(recvBuf, recvBuf.length);
        ms.receive(dp);
        ByteArrayInputStream bin = new ByteArrayInputStream(recvBuf);
        MCMessage msg;
        try (final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bin))) {
            msg = (MCMessage) in.readObject();
        }
        return msg;
    }

    /**
     * Manages the IP list that controls which nodes are running the middleware.
     */
    @Override
    public void run() {
        this.ipAddresses.add(this.host);
        this.VC.add(0);
        this.MC.add(new ArrayList<>());
        this.MC.get(0).add(0);
        MCMessage msg = new MCMessage(MCMessage.TYPE.JOIN, this.host);
        InetAddress group;
        MulticastSocket ms;
        try {
            group = InetAddress.getByName(this.ipMulticastAddress);
            ms = new MulticastSocket(6789);
            ms.joinGroup(group);
            sendObject(ms, msg, group);
            while (true) {
                MCMessage recv = recvObject(ms);
                // verifica se o ip já está na lista, se tiver ignora a mensagem
                if (this.ipAddresses.contains(recv.getClient())) {
                    continue;
                }
                // tratamento de acordo com o tipo de mensagem
                if (recv.getType() == MCMessage.TYPE.JOIN) {
                    this.ipAddresses.add(recv.getClient());
                    msg.setType(MCMessage.TYPE.RET_JOIN);
                    this.VC.add(0);
                    this.MC.add(new ArrayList<>());
                    for (int i = 0; i < this.ipAddresses.size(); i++) {
                        this.MC.get(i).add(0);
                        this.MC.get(this.MC.size() - 1).add(0);
                    }
                    this.MC.get(this.MC.size() - 1).remove(this.MC.get(this.MC.size() - 1).size() - 1);
                    sendObject(ms, msg, group);
                } else if (recv.getType() == MCMessage.TYPE.RET_JOIN) {
                    this.ipAddresses.add(recv.getClient());
                    this.VC.add(0);
                    this.MC.add(new ArrayList<>());
                    for (int i = 0; i < this.ipAddresses.size(); i++) {
                        this.MC.get(i).add(0);
                        this.MC.get(this.MC.size() - 1).add(0);
                    }
                    this.MC.get(this.MC.size() - 1).remove(this.MC.get(this.MC.size() - 1).size() - 1);
                }
                // ordena o vetor pelos ips para auxiliar no controle dos outros vetores de processos
                java.util.Collections.sort(this.ipAddresses);
            }
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute the client.
     *
     * @throws java.io.IOException In case of IO exception.
     * @throws java.lang.InterruptedException In case of thread interrupted.
     */
    public void execute() throws IOException, InterruptedException {
        // descobre ips com o middleware
        Thread thread = new Thread(this);
        thread.start();
        // aguarda a descoberta inicial
        Thread.sleep(3000);
        String str = "Lista de usuários utilizando o middleware:";
        for (int i = 0; i < this.ipAddresses.size(); i++) {
            str = str + "\n" + i + ": " + this.ipAddresses.get(i);
        }
        this.channel = new CMChannel(this);
        channel.syncOutput(str);
        int counter = 1;
        while (true) {
            // constroi msg e o timestamp
            Message msg = new Message(this.host, "M" + counter + " " + this.host);
            this.channel.mcsend(msg);
            counter++;
        }

    }

    /**
     * Executes this class.
     *
     * @param args Command line arguments.
     * @throws java.io.IOException In case of IO error.
     * @throws java.lang.InterruptedException In case of thread interrupted.
     */
    public static void main(final String[] args) throws IOException, InterruptedException {
        if (args.length > 0) {
            new Client(args[0], 12345).execute();
        } else {
            new Client("127.0.0.1", 12345).execute();
        }
    }
}
