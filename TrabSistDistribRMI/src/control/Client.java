/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao
 */
public class Client {

    public static void main(final String[] args) {
//        System.setSecurityManager(new SecurityManager());
        try {
            IIncrementa c = (IIncrementa) Naming.lookup("rmi://localhost/control/Incrementa");
            while (true) {
                System.out.println("incrementando = " + c.inc());
            }
        } catch (final NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
