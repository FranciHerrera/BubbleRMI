package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SortArray extends Remote {

    int registerClient() throws RemoteException;

    void sendArray(int clientId, int[] array) throws RemoteException;

    int[] getSortedArrayBySecuencial() throws RemoteException;
    
    int[] getSortedArrayByFork() throws RemoteException;
    
    int[] getSortedArrayByExecutor() throws RemoteException;

    boolean allClientsSentArrays() throws RemoteException;
}
