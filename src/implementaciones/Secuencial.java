package implementaciones;

import java.util.Random;

public class Secuencial {

    private int[] arreglo;
    private int tamano;
    private int rango;

    public Secuencial(int tamano, int rango) {
        this.arreglo = new int[tamano];
        this.tamano = tamano;
        this.rango = rango;
    }

    public Secuencial(int[] arreglo) {
        this.arreglo = arreglo;
    }

    public int[] getArreglo() {
        return arreglo;
    }
    
    
    public void generarArreglo() {
        Random rand = new Random();
        for (int i = 0; i < tamano; i++) {
            arreglo[i] = rand.nextInt(rango);
        }
    }

    public String obtenerArreglo() {
        StringBuilder sb = new StringBuilder();
        for (int num : arreglo) {
            sb.append(num).append(" , ");
        }
        return sb.toString();
    }

    public void ordenar() {
        int n = arreglo.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arreglo[j] > arreglo[j + 1]) {
                    int temp = arreglo[j];
                    arreglo[j] = arreglo[j + 1];
                    arreglo[j + 1] = temp;
                }
            }
        }
    }
}
