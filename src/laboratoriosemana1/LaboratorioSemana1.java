/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package laboratoriosemana1;
import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author Abril
 */


public class Laboratoriosemana1 extends JFrame {

    private JButton[] seatButtons = new JButton[30];
    private JTextField txtName = new JTextField(18);
    private JTextArea consola = new JTextArea();
    private int asientoSeleccionado = -1;

    private static final Color DISPONIBLE = new Color(76, 175, 80);   
    private static final Color OCUPADO = new Color(211, 47, 47);      
    private static final Color PALINDROMO = new Color(255, 193, 7);   
    private static final Color SELECCIONADO = new Color(128, 128, 128); 

    public Laboratoriosemana1() {
        setTitle("PalindromoAir - Venta y Control de Boletos");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.add(new JLabel("Nombre del pasajero:"));
        topPanel.add(txtName);

        JButton btnSell = new JButton("Sell Ticket");
        JButton btnCancel = new JButton("Cancel Ticket");
        JButton btnDispatch = new JButton("Dispatch");
        JButton btnPrint = new JButton("Print Passengers");
        JButton btnIncome = new JButton("View Income");
        JButton btnSearch = new JButton("Search Passenger");

        topPanel.add(btnSell);
        topPanel.add(btnCancel);
        topPanel.add(btnDispatch);
        topPanel.add(btnPrint);
        topPanel.add(btnIncome);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(5, 6, 8, 8));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    public static void main(String[] args) {
        JFrame ventana = new Laboratoriosemana1();
        ventana.setVisible(true);
    }
}
