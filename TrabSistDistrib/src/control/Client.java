package control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Client implementation.
 *
 * @author João Víctor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Client {

    /**
     * Main method.
     *
     * @param args Command line arguments (ignored).
     */
    public static void main(final String[] args) {
        int somatorio = 0;
        try {
            byte[] dado = new byte[Server.TAMANHO_MAX];
            String nomeServidor = "127.0.0.2";
            InetAddress ip = InetAddress.getByName(nomeServidor);
            DatagramPacket pacoteEnvio = new DatagramPacket(dado, dado.length, ip, Server.PORT);
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.send(pacoteEnvio); // criação do endpoint
                DatagramPacket pacoteRecebido = new DatagramPacket(dado, dado.length);
                socket.receive(pacoteRecebido);
                dado = pacoteRecebido.getData();
                String valor = new String(dado);
                int valorInteiro = Integer.parseInt(valor.trim());
                somatorio += valorInteiro;
            } catch (final IOException | NumberFormatException ex) {
                System.out.println("[Client ERROR] " + ex);
            }
        } catch (final UnknownHostException ex) {
            System.out.println("[Client ERROR] " + ex);
        }
    }

}
