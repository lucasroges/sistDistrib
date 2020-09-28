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
import java.lang.NumberFormatException;
import model.Message;
import client.Client;

/**
 *
 * @author Joao Victor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Sep 21.
 */
public class CMChannel extends Thread {

    // 
    public Client client;

    // lista de mensagens enviadas
    public List<Message> buffer;

    // lista de mensagens recebidas
    public List<Message> recvBuffer;

    /**
     * 
     */
    public CMChannel(Client client) {
        this.client = client;
        this.buffer = new ArrayList<Message>();
        this.recvBuffer = new ArrayList<Message>();
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * 
     * @param message
     */
    public void causalOrdering(Message message) {
        // atrasa entrega
        for(int i = 0; i < this.client.VC.size(); i++) {
            while(message.VC.get(i) > this.client.VC.get(i));
        }
        // atualiza variável de controle
        if (this.client.host != message.sender) {
            int senderIndex = this.client.ipAddresses.indexOf(message.sender);
            this.client.VC.set(senderIndex, this.client.VC.get(senderIndex) + 1);
        }
    }

    public void stabilizer(Message message) {
        this.recvBuffer.add(message);
        int senderIndex = this.client.ipAddresses.indexOf(message.sender);
        this.client.MC.set(senderIndex, message.MC);
        int hostIndex = this.client.ipAddresses.indexOf(this.client.host);
        if (hostIndex != senderIndex) {
            this.client.MC.get(hostIndex).set(senderIndex, this.client.MC.get(hostIndex).get(senderIndex) + 1);
        }
        // descarte
        for (int i = 0; i < this.recvBuffer.size(); i++) {
            senderIndex = this.client.ipAddresses.indexOf(recvBuffer.get(i).sender);
            int min = this.client.MC.get(0).get(senderIndex);
            for (int j = 1; j < this.client.ipAddresses.size(); j++) {
                if (this.client.MC.get(j).get(senderIndex) < min) {
                    min = this.client.MC.get(j).get(senderIndex);
                }
            }
            if (recvBuffer.get(i).MC.get(senderIndex) <= min) {
                this.recvBuffer.remove(i);
            }
        }
        // mostra o conteúdo do buffer de mensagens recebidas
        System.out.print("Conteúdo do buffer de recebidas: ");
        for (Message m : this.recvBuffer) {
            System.out.print(m.msg + " | ");
        }
        System.out.println();
    }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            // cria um server socket na porta 2020 para receber conexões
            ss = new ServerSocket(2020);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            Socket s = null;
            try {
                    // escuta por conexões e as recebe
                    s = ss.accept();
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    final Message m = (Message) in.readObject();
                    new Thread(new Runnable() {
                        public void run() {
                            // ordenamento causal
                            causalOrdering(m);
                            // estabilização
                            stabilizer(m);
                            // envia para a aplicação tratar a mensagem
                            client.deliver(m);
                        }
                    }).start();
                    // fecha o socket
                    s.close();
            } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
            }  
        }
    }

    public void send(String ip, Message message) throws IOException {
        Socket s = new Socket(ip, 2020);
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.writeObject(message);
        System.out.println("[Mensagem enviada]"
                    + "\nConteúdo: " + message.msg
                    + "\nOrigem: " + message.sender);
        s.close();
    }

    public void mcsend(Message msg) throws IOException, UnknownHostException{
        // para obter entrada do cliente
        Scanner sc = new Scanner(System.in);
        String in = "";
        // enviar alguma mensagem do buffer, se houver
        if (!buffer.isEmpty()) {
            try {
                System.out.println("Enviar alguma mensagem do buffer? (Se sim, digitar o índex a partir de 0)");
                in = sc.nextLine();
                if (Integer.parseInt(in) < buffer.size()) {
                    int bufferIndex = Integer.parseInt(in);
                    System.out.println("Qual o destino da mensagem? (Digitar o índex a partir de 0)");
                    in = sc.nextLine();
                    if (Integer.parseInt(in) < this.client.ipAddresses.size()) {
                        send(this.client.ipAddresses.get(Integer.parseInt(in)), buffer.get(bufferIndex));
                    }
                }
            } catch (NumberFormatException e) {
                // ignora e segue, pois não quer enviar mensagem do buffer
            }
            
        }
        int hostIndex = this.client.ipAddresses.indexOf(this.client.host);
        // constroi o timestamp da msg
        msg.VC = new ArrayList<>(this.client.VC);
        msg.MC = new ArrayList<>(this.client.MC.get(hostIndex));
        // adiciona msg ao buffer
        buffer.add(msg);
        // multicast
        System.out.println("Enviar para todos?");
        in = sc.nextLine();
        Boolean doMulticast = in.equals("Sim");
        for (String ip : this.client.ipAddresses) {
            if (doMulticast) {
                send(ip, msg);
            } else {
                System.out.println("Enviar para o usuario " + ip + "?");
                in = sc.nextLine();
                if (in.equals("Sim")) {
                    send(ip, msg);
                }
            }
        }
        // atualizar o vetor de relógios
        this.client.VC.set(hostIndex, this.client.VC.get(hostIndex) + 1);
        this.client.MC.get(hostIndex).set(hostIndex, this.client.MC.get(hostIndex).get(hostIndex) + 1);
        // mostra o conteúdo do buffer de mensagens enviadas
        System.out.print("Conteúdo do buffer de envios: ");
        for (Message message : buffer) {
            System.out.print(message.msg + " | ");
        }
        System.out.println();
    }
        
}
