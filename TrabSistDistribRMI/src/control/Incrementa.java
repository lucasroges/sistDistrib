/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author joao
 */
public class Incrementa extends UnicastRemoteObject implements IIncrementa {

    private int x = 0;

    public Incrementa() throws RemoteException {
        // empty
    }

    @Override
    public int inc() throws RemoteException {
        return ++x;
    }

}
