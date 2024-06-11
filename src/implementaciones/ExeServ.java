package implementaciones;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExeServ implements ExecutorService {

    private ExecutorService executor;
    private int[] arreglo;
    private int tamano;
    private int rango;

    public ExeServ(int tamano, int rango) {
        this.arreglo = new int[tamano];
        this.tamano = tamano;
        this.rango = rango;
        executor = Executors.newFixedThreadPool(2);
    }

    public ExeServ(int[] arreglo) {
        this.arreglo = arreglo;
        executor = Executors.newFixedThreadPool(2);

    }

    public int[] getArreglo() {
        return arreglo;
    }
    
    
    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
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
        final int numeroDeHilos = Runtime.getRuntime().availableProcessors();
        final int tamanoFragmento = tamano / numeroDeHilos;

        if (tamano < 100) {
            executor.submit(() -> ordenarPorBubble());
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            for (int i = 0; i < numeroDeHilos; i++) {
                final int inicio = i * tamanoFragmento;
                final int fin = (i == numeroDeHilos - 1) ? tamano : (i + 1) * tamanoFragmento;
                executor.submit(() -> ordenarPorBurbuja(inicio, fin));
            }
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            fusionarFragmentos(numeroDeHilos, tamanoFragmento);
        }
    }

    private void fusionarFragmentos(int numeroDeFragmentos, int tamañoFragmento) {
        int[] temp = new int[tamano];
        int[] resultado = new int[tamano];
        int indiceActual = 0;
        int[] indices = new int[numeroDeFragmentos];
        for (int i = 0; i < numeroDeFragmentos; i++) {
            indices[i] = i * tamañoFragmento;
        }
        while (indiceActual < tamano) {
            int indiceMinimo = -1;
            int valorMinimo = Integer.MAX_VALUE;
            for (int i = 0; i < numeroDeFragmentos; i++) {
                if (indices[i] < (i + 1) * tamañoFragmento && arreglo[indices[i]] < valorMinimo) {
                    indiceMinimo = i;
                    valorMinimo = arreglo[indices[i]];
                }
            }
            temp[indiceActual++] = arreglo[indices[indiceMinimo]++];
            if (indices[indiceMinimo] % tamañoFragmento == 0 && indices[indiceMinimo] < (indiceMinimo + 1) * tamañoFragmento) {
                indices[indiceMinimo] = (indiceMinimo + 1) * tamañoFragmento;
            }
        }
        System.arraycopy(temp, 0, arreglo, 0, tamano);
    }

    public void ordenarPorBurbuja(int inicio, int fin) {
        for (int i = inicio; i < fin; i++) {
            for (int j = inicio; j < fin - 1; j++) {
                if (arreglo[j] > arreglo[j + 1]) {
                    int temp = arreglo[j];
                    arreglo[j] = arreglo[j + 1];
                    arreglo[j + 1] = temp;
                }
            }
        }
    }

    public void ordenarPorBubble() {
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

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Future<?> submit(Runnable task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
