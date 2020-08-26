package control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Implementation of server.
 *
 * @author João Víctor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class Server {

    public static final byte TAMANHO_MAX = 100; // TODO

    public static final short PORT = 2020;

    /**
     * Main method.
     *
     * @param args Command line arguments (ignored).
     */
    public static void main(final String[] args) {
        DatagramSocket socket;
        byte[] dado;
        while (true) {
            try {
                socket = new DatagramSocket(PORT); // criação do endpoint na porta 2020
                dado = new byte[TAMANHO_MAX];
                DatagramPacket pacoteRecebido = new DatagramPacket(dado, dado.length);
                try {
                    socket.receive(pacoteRecebido);
                } catch (final IOException ex) {
                    System.out.println("[Server ERROR] " + ex);
                }
                ProxyConsumidor pc = new ProxyConsumidor(pacoteRecebido.getAddress(), pacoteRecebido.getPort());
                pc.run();
            } catch (final SocketException ex) {
                System.out.println("[Server ERROR] " + ex);
            }
        }
    }

    private String processReceivePackage(final DatagramPacket pacoteRecebido) {
        // recuperação de uma cadeia de caracteres a partir da cadeia de bytes
        String valor = new String(pacoteRecebido.getData());
        // transformação do String em valor inteiro
        int valorInteiro = Integer.parseInt(valor.trim());
        // transformação de valor inteiro para String
        return String.valueOf(valorInteiro);
    }

    private static class ProxyConsumidor implements Runnable {

        private ProxyConsumidor(final InetAddress address, final int port) {
            // TODO
        }

        @Override
        public void run() {
            // TODO
        }
    }

}
