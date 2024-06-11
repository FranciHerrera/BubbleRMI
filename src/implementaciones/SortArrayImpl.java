package implementaciones;

import interfaces.SortArray;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortArrayImpl extends UnicastRemoteObject implements SortArray {

    private Map<Integer, int[]> clientArrays;
    private List<Integer> clientIds;
    private int nextClientId;
    Secuencial s;
    ForkJoin f;
    ExeServ exe;

    public SortArrayImpl() throws RemoteException {
        super();
        clientArrays = new HashMap<>();
        clientIds = new ArrayList<>();
        nextClientId = 1;
    }

    @Override
    public synchronized int registerClient() throws RemoteException {
        int clientId = nextClientId++;
        clientIds.add(clientId);
        System.out.println("Cliente: " + clientId + " conectado");
        return clientId;
    }

    @Override
    public synchronized void sendArray(int clientId, int[] array) throws RemoteException {
        clientArrays.put(clientId, array);
        System.out.println("Cliente " + clientId + " mando su arreglo");
    }

    @Override
    public synchronized int[] getSortedArrayBySecuencial() throws RemoteException {
        List<Integer> combinedList = new ArrayList<>();
        for (int[] array : clientArrays.values()) {
            for (int num : array) {
                combinedList.add(num);
            }
        }
        int[] combinedArray = combinedList.stream().mapToInt(Integer::intValue).toArray();
        //Arrays.sort(combinedArray);
        s = new Secuencial(combinedArray);
        s.ordenar();
        return s.getArreglo();
    }

    @Override
    public synchronized int[] getSortedArrayByFork() throws RemoteException {
        List<Integer> combinedList = new ArrayList<>();
        for (int[] array : clientArrays.values()) {
            for (int num : array) {
                combinedList.add(num);
            }
        }
        int[] combinedArray = combinedList.stream().mapToInt(Integer::intValue).toArray();
        f = new ForkJoin(combinedArray);
        f.ordenar();
        return f.getArreglo();
    }

    @Override
    public synchronized int[] getSortedArrayByExecutor() throws RemoteException {
        List<Integer> combinedList = new ArrayList<>();
        for (int[] array : clientArrays.values()) {
            for (int num : array) {
                combinedList.add(num);
            }
        }
        int[] combinedArray = combinedList.stream().mapToInt(Integer::intValue).toArray();
        //Arrays.sort(combinedArray);
        exe = new ExeServ(combinedArray);
        exe.ordenar();
        return exe.getArreglo();
    }

    @Override
    public synchronized boolean allClientsSentArrays() {
        return clientArrays.size() == clientIds.size();
    }
}
