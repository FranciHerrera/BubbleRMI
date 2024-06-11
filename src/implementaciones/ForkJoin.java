package implementaciones;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoin {

    private int[] arreglo;
    private int tamano;
    private int rango;
    private ForkJoinPool pool;

    public ForkJoin(int tamano, int rango) {
        this.arreglo = new int[tamano];
        this.tamano = tamano;
        this.rango = rango;
        this.pool = new ForkJoinPool();
    }

    public ForkJoin(int[] arreglo) {
        this.arreglo = arreglo;
        this.pool = new ForkJoinPool();
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
        pool.invoke(new BubbleSortTask(arreglo, 0, arreglo.length));
    }

    private class BubbleSortTask extends RecursiveAction {

        private static final int THRESHOLD = 100;
        private int[] arr;
        private int start, end;

        BubbleSortTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    for (int j = start; j < end - 1; j++) {
                        if (arr[j] > arr[j + 1]) {
                            int temp = arr[j];
                            arr[j] = arr[j + 1];
                            arr[j + 1] = temp;
                        }
                    }
                }
            } else {
                int mid = (start + end) >>> 1;
                BubbleSortTask left = new BubbleSortTask(arr, start, mid);
                BubbleSortTask right = new BubbleSortTask(arr, mid, end);
                invokeAll(left, right);
                merge(arr, start, mid, end);
            }
        }

        private void merge(int[] arr, int start, int mid, int end) {
            int[] temp = new int[end - start];
            int left = start;
            int right = mid;
            int index = 0;
            while (left < mid && right < end) {
                if (arr[left] <= arr[right]) {
                    temp[index++] = arr[left++];
                } else {
                    temp[index++] = arr[right++];
                }
            }
            System.arraycopy(arr, left, temp, index, mid - left);
            System.arraycopy(arr, right, temp, index, end - right);
            System.arraycopy(temp, 0, arr, start, temp.length);
        }
    }
}
