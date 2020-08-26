package control;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class TratadorDeMensagemDoCliente implements Runnable {

    private Socket cliente;
    private Server servidor;

    public TratadorDeMensagemDoCliente(Socket cliente, Server servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        try (Scanner s = new Scanner(this.cliente.getInputStream())) {
            while (s.hasNextLine()) {
                String msg = s.nextLine();
                System.out.println("servidor distribui " + msg);
                servidor.distribuiMensagem(this.cliente, msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
