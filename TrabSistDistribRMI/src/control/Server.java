/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author joao
 */
public class Server {

    private static final String HOST_URL = "rmi://localhost/control/Incrementa";

    public static void main(final String[] args) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Incrementa c = new Incrementa();
            Naming.bind(HOST_URL, c);
            Thread.sleep(1000);
        } catch (final InterruptedException | RemoteException | AlreadyBoundException | MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
}
