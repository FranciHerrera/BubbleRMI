package servidor;

import implementaciones.SortArrayImpl;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static void main(String[] args) {
        try {
            SortArrayImpl obj = new SortArrayImpl();

            LocateRegistry.createRegistry(1099);
            Naming.rebind("BubbleSortService", obj);
            System.out.println("BubbleSortService esta listo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
