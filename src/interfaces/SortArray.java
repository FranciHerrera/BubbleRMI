package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SortArray extends Remote {

    int registerClient() throws RemoteException;

    void sendArray(int clientId, int[] array) throws RemoteException;
    
    int[] getArray() throws RemoteException;

    boolean allClientsSentArrays() throws RemoteException;    
}
