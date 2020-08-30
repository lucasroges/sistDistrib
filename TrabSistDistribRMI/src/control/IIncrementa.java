/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author joao
 */
public interface IIncrementa extends Remote {

    public int inc() throws RemoteException;

}
