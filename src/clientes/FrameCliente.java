package clientes;

import implementaciones.ExeServ;
import implementaciones.ForkJoin;
import implementaciones.Secuencial;
import interfaces.SortArray;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FrameCliente extends JFrame implements ActionListener {

    JButton btnSec, btnFork, btnExec, btnBorr, btnGenArr, btnSendArr, btnComb;
    JTextArea txtOrig, txtComb, txtRes, txtTam, txtRan;
    JScrollPane spOrig, spRes, spComb;
    JLabel lbOrig, lbComb, lbRes, lbTam, lbRan, lbTimeSec, lbTimeFork, lbTimeExec;
    Secuencial s;
    ForkJoin f;
    ExeServ ex;

    // RMI related fields
    private SortArray sortArrayService;
    private int clientId;
    int arreglo[];

    public FrameCliente() {
        this.setTitle("BubbleSortRMI");
        this.setSize(750, 500);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        //this.getContentPane().setBackground(Color.BLUE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnSec = new JButton("Secuencial");
        btnSec.setBounds(10, 300, 120, 40);
        btnSec.addActionListener(this);

        lbTimeSec = new JLabel("Tiempo Secuencial: ");
        lbTimeSec.setBounds(10, 350, 300, 40);

        btnFork = new JButton("Fork/Join");
        btnFork.setBounds(200, 300, 120, 40);
        btnFork.addActionListener(this);

        lbTimeFork = new JLabel("Tiempo ForkJoin: ");
        lbTimeFork.setBounds(10, 375, 300, 40);

        btnExec = new JButton("ExecutorService");
        btnExec.setBounds(400, 300, 150, 40);
        btnExec.addActionListener(this);

        lbTimeExec = new JLabel("Tiempo Executor: ");
        lbTimeExec.setBounds(10, 400, 300, 40);

        btnBorr = new JButton("Borrar");
        btnBorr.setBounds(600, 300, 80, 40);
        btnBorr.addActionListener(this);

        txtOrig = new JTextArea();
        txtOrig.setLineWrap(true);
        spOrig = new JScrollPane(txtOrig);
        spOrig.setBounds(10, 10, 200, 100);

        lbOrig = new JLabel("Original");
        lbOrig.setBounds(10, 101, 50, 50);

        txtRes = new JTextArea();
        txtRes.setLineWrap(true);
        spRes = new JScrollPane(txtRes);
        spRes.setBounds(430, 10, 200, 100);

        lbRes = new JLabel("Resultado");
        lbRes.setBounds(430, 101, 100, 50);

        txtComb = new JTextArea();
        txtComb.setLineWrap(true);
        spComb = new JScrollPane(txtComb);
        spComb.setBounds(220, 10, 200, 100);

        lbComb = new JLabel("Combinado");
        lbComb.setBounds(220, 101, 100, 50);

        txtTam = new JTextArea("50");
        txtTam.setBounds(100, 150, 100, 50);
        lbTam = new JLabel("Tamaño");
        lbTam.setBounds(10, 150, 100, 50);

        txtRan = new JTextArea("5");
        txtRan.setBounds(100, 230, 100, 50);
        lbRan = new JLabel("Rango");
        lbRan.setBounds(10, 230, 100, 50);

        btnGenArr = new JButton("Generar Arreglo");
        btnGenArr.setBounds(200, 180, 150, 40);
        btnGenArr.addActionListener(this);

        btnSendArr = new JButton("Enviar Arreglo");
        btnSendArr.setBounds(400, 180, 120, 40);
        btnSendArr.addActionListener(this);

        btnComb = new JButton("Obtener Arreglo");
        btnComb.setBounds(550, 180, 150, 40);
        btnComb.addActionListener(this);

        BackgroundPanel backgroundPanel = new BackgroundPanel("Bubbles.png");
        backgroundPanel.setLayout(null);
        this.setContentPane(backgroundPanel);

        this.add(btnGenArr);

        this.add(btnSec);
        this.add(lbTimeSec);

        this.add(btnFork);
        this.add(lbTimeFork);

        this.add(btnExec);
        this.add(lbTimeExec);

        this.add(btnBorr);
        this.add(btnSendArr);
        this.add(btnComb);

        this.add(spOrig);
        this.add(lbOrig);

        this.add(spRes);
        this.add(lbRes);

        this.add(spComb);
        this.add(lbComb);

        this.add(txtTam);
        this.add(lbTam);

        this.add(txtRan);
        this.add(lbRan);

        this.setVisible(true);

        try {
            //sortArrayService = (SortArray) Naming.lookup("//localhost/BubbleSortService");
            sortArrayService = (SortArray) Naming.lookup("//25.64.149.38/BubbleSortService");
            clientId = sortArrayService.registerClient();
            System.out.println("Registrado como cliente: " + clientId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenArr) {
            generarArreglo();
        } else if (e.getSource() == btnSendArr) {
            enviarArreglo();
        } else if (e.getSource() == btnSec) {
            ordenarArregloSecuencial();
        } else if (e.getSource() == btnFork) {
            ordenarArregloFork();
        } else if (e.getSource() == btnExec) {
            ordenarArregloExecutor();
        } else if (e.getSource() == btnBorr) {
            borrarArreglo();
        } else if (e.getSource() == btnComb) {
            obtenerArreglo();
        }
    }

    private void enviarArreglo() {
        try {
            int[] array = Arrays.stream(txtOrig.getText().replaceAll("\\[|\\]|\\s", "").split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            sortArrayService.sendArray(clientId, array);
            System.out.println("Arreglo enviado");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void obtenerArreglo() {
        try {
            int[] arregloCombinado = sortArrayService.getArray();
            txtComb.setText(Arrays.toString(arregloCombinado));
            arreglo = arregloCombinado;
        } catch (RemoteException ex) {
            Logger.getLogger(FrameCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ordenarArregloSecuencial() {
        s = new Secuencial(arreglo);
        long startTime = System.nanoTime();
        s.ordenar();
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1e6;
        lbTimeSec.setText("Tiempo Secuencial: " + duration + " ms");
        txtRes.setText(s.obtenerArreglo());
    }

    private void ordenarArregloFork() {
        f = new ForkJoin(arreglo);
        long startTime = System.nanoTime();
        f.ordenar();
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1e6;
        lbTimeFork.setText("Tiempo Fork: " + duration + " ms");
        txtRes.setText(f.obtenerArreglo());
    }

    private void ordenarArregloExecutor() {
        ex = new ExeServ(arreglo);
        long startTime = System.nanoTime();
        ex.ordenar();
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1e9;
        lbTimeExec.setText("Tiempo Executor: " + duration + " ms");
        txtRes.setText(ex.obtenerArreglo());
    }

    public void generarArreglo() {
        int tam = Integer.parseInt(txtTam.getText());
        int rango = Integer.parseInt(txtRan.getText());
        arreglo = new int[tam];
        Random rand = new Random();
        for (int i = 0; i < tam; i++) {
            arreglo[i] = rand.nextInt(rango);
        }
        txtOrig.setText(Arrays.toString(arreglo));
    }

    private void borrarArreglo() {
        txtOrig.setText("");
        txtComb.setText("");
        txtRes.setText("");
    }
}

class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    public BackgroundPanel(String fileName) {
        try {
            backgroundImage = ImageIO.read(getClass().getResource(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
