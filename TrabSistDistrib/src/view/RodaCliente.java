package view;

import control.Client;
import java.io.IOException;
import java.net.UnknownHostException;

public class RodaCliente {

    public static void main(final String[] args) throws UnknownHostException, IOException {
        new Client("127.0.0.1", 12345).executa();
    }
}
