package CausalMulticast;

import java.util.LinkedList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Queue;
import java.net.ServerSocket;
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

    // fila de mensagens enviadas
    public Queue<Message> buffer;

    /**
     * 
     */
    public CMChannel(Client client) {
        this.client = client;
        this.buffer = new LinkedList<Message>();
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
            while(message.VC.get(i) <= this.client.VC.get(i));
        }
        // atualiza variável de controle
        if (this.client.host != message.sender) {
            int senderIndex = this.client.ipAddresses.indexOf(message.sender);
            this.client.VC.set(senderIndex, this.client.VC.get(senderIndex) + 1);
        }
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

    public void mcsend(Message msg) throws IOException, UnknownHostException{
        // constroi o timestamp da msg
        msg.VC = new ArrayList<>(this.client.VC);
        // adiciona msg ao buffer
        buffer.add(msg);
        // multicast
        Scanner sc = new Scanner(System.in);
        for (String ip : this.client.ipAddresses) {
            System.out.println("Enviar para o usuario " + ip + "?");
            String in = sc.nextLine();
            if (in.equals("Sim")) {
                Socket s = new Socket(ip, 2020);
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(msg);
                System.out.println("[Mensagem enviada]"
                            + "\nConteúdo: " + msg.msg
                            + "\nOrigem: " + msg.sender);
                s.close();
            }
        }
        // atualizar o vetor de relógios
        int hostIndex = this.client.ipAddresses.indexOf(this.client.host);
        this.client.VC.set(hostIndex, this.client.VC.get(hostIndex) + 1);
        // mostra o conteúdo do buffer
        System.out.print("Conteúdo do buffer:");
        for (Message message : buffer) {
            System.out.print(message.msg + " | ");
        }
    }
        
}
