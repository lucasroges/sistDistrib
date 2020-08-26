package control;

/**
 * Client implementation.
 *
 * @author João Víctor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private final String host;
    private final int porta;

    public Client(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executa() throws UnknownHostException, IOException {
        try (Socket cliente = new Socket(host, porta);
                Scanner teclado = new Scanner(System.in);
                PrintStream saida = new PrintStream(cliente.getOutputStream())) {
            System.out.println("O cliente se conectou ao servidor!");

            RecebedorDeMensagemDoServidor r = new RecebedorDeMensagemDoServidor(cliente.getInputStream());
            new Thread(r).start();

            while (teclado.hasNextLine()) {
                saida.println(teclado.nextLine());
            }
        }
    }
}
