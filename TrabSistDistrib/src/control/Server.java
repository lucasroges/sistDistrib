package control;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of server.
 *
 * @author João Víctor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Server {

    private int porta;
    private List<Socket> clientes;

    public Server(int porta) {
        this.porta = porta;
        this.clientes = new ArrayList<>();
    }

    public void executa() throws IOException {
        try (ServerSocket servidor = new ServerSocket(this.porta)) {
            System.out.println("Porta 12345 aberta!");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Nova conexão com o cliente "
                        + cliente.getInetAddress().getHostAddress());

                this.clientes.add(cliente);

                TratadorDeMensagemDoCliente tc = new TratadorDeMensagemDoCliente(cliente, this);
                new Thread(tc).start();
            }
        }
    }

    public void distribuiMensagem(Socket clienteQueEnviou, String msg) {
        for (Socket cliente : this.clientes) {
            if (!cliente.equals(clienteQueEnviou)) {
                try {
                    PrintStream ps = new PrintStream(cliente.getOutputStream());
                    ps.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
