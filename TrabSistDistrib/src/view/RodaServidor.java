package view;

import control.Server;
import java.io.IOException;

public class RodaServidor {

    public static void main(final String[] args) throws IOException {
        new Server(12345).executa();
    }
}
