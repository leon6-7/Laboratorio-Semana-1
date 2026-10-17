/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package laboratoriosemana1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * 
 * 
 * @author mario
 */
public class LaboratorioSemana1 extends JFrame {

    private final PalindromoAir avion = new PalindromoAir();
    private final JButton[] botonesAsientos = new JButton[PalindromoAir.CAPACIDAD];
    private final JTextField txtName = new JTextField(18);
    private final JTextPane consola = new JTextPane();

    private final JLabel lblDisponibles = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblOcupados = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblPalindromos = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblIngresos = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblAhorrado = new JLabel("", SwingConstants.CENTER);

    private int asientoSeleccionado = -1;
    private int asientoResaltado = -1;

    public LaboratorioSemana1() {
        setTitle("PalindromoAir - Venta y Control de Boletos");
        setSize(1050, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelContadores = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 8));
        panelContadores.add(lblDisponibles);
        panelContadores.add(lblOcupados);
        panelContadores.add(lblPalindromos);
        panelContadores.add(lblIngresos);
        panelContadores.add(lblAhorrado);

        JPanel panelVenta = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelVenta.add(new JLabel("Nombre del pasajero:"));
        panelVenta.add(txtName);

        JButton btnSell = new JButton("Sell Ticket");
        JButton btnCancel = new JButton("Cancel Ticket");
        JButton btnSearch = new JButton("Search Passenger");
        JButton btnPrint = new JButton("Print Passengers");
        JButton btnIncome = new JButton("View Income");
        JButton btnDispatch = new JButton("Dispatch");
        JButton btnExport = new JButton("Export Passengers");
        JButton btnHistorial = new JButton("Dispatch History");

        panelVenta.add(btnSell);
        panelVenta.add(btnCancel);
        panelVenta.add(btnSearch);
        panelVenta.add(btnPrint);
        panelVenta.add(btnIncome);
        panelVenta.add(btnDispatch);
        panelVenta.add(btnExport);
        panelVenta.add(btnHistorial);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelContadores, BorderLayout.NORTH);
        panelNorte.add(panelVenta, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelAsientos = new JPanel(new GridLayout(PalindromoAir.FILAS, PalindromoAir.COLUMNAS, 8, 8));
        panelAsientos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        for (int i = 0; i < PalindromoAir.CAPACIDAD; i++) {
            final int indice = i;
            JButton boton = new JButton();
            boton.setOpaque(true);
            boton.setBorderPainted(false);
            boton.setFocusPainted(false);
            boton.addActionListener(e -> clicAsiento(indice));
            botonesAsientos[i] = boton;
            panelAsientos.add(boton);
        }
        add(panelAsientos, BorderLayout.CENTER);

        consola.setEditable(false);
        consola.setBackground(Color.BLACK);
        consola.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(consola);
        scroll.setPreferredSize(new Dimension(1000, 160));
        scroll.setBorder(BorderFactory.createTitledBorder("Consola del sistema"));

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(scroll, BorderLayout.CENTER);
        panelSur.add(crearLeyenda(), BorderLayout.SOUTH);
        add(panelSur, BorderLayout.SOUTH);

        avion.conectarInterfaz(botonesAsientos, consola);

        btnSell.addActionListener(e -> vender());
        btnCancel.addActionListener(e -> cancelar());
        btnSearch.addActionListener(e -> buscar());
        btnPrint.addActionListener(e -> avion.printPassengers(0));
        btnIncome.addActionListener(e -> consultarIngresos());
        btnDispatch.addActionListener(e -> {
            avion.dispatch();
            asientoSeleccionado = -1;
            asientoResaltado = -1;
            refrescarInterfaz();
        });
        btnExport.addActionListener(e -> avion.exportarPasajeros("pasajeros.txt"));
        btnHistorial.addActionListener(e -> mostrarHistorial());

        txtName.addActionListener(e -> vender());
        txtName.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "vender");
        txtName.getActionMap().put("vender", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vender();
            }
        });

        avion.mostrarMensaje("Sistema PalindromoAir listo. Capacidad: "
                + PalindromoAir.CAPACIDAD + " asientos. Precio base: "
                + PalindromoAir.dinero(PalindromoAir.PRECIO_BASE) + ".", false);
        refrescarInterfaz();
    }

    private void vender() {
        boolean vendido;
        if (asientoSeleccionado != -1) {
            vendido = avion.sellTicketEnAsiento(txtName.getText(), asientoSeleccionado);
        } else {
            vendido = avion.sellTicket(txtName.getText());
        }
        if (vendido) {
            txtName.setText("");
            asientoSeleccionado = -1;
            asientoResaltado = -1;
        }
        refrescarInterfaz();
    }

    private void cancelar() {
        boolean cancelado = avion.cancelTicket(txtName.getText());
        if (cancelado) {
            txtName.setText("");
            asientoResaltado = -1;
        }
        refrescarInterfaz();
    }

    private void buscar() {
        String nombre = txtName.getText();
        if (!avion.nombreValido(nombre)) {
            avion.mostrarMensaje("Ingrese un nombre válido.", true);
            return;
        }
        int posicion = avion.searchPassenger(nombre, 0);
        if (posicion == -1) {
            avion.mostrarMensaje("No se encontró al pasajero.", true);
            asientoResaltado = -1;
        } else {
            avion.mostrarMensaje(nombre + " está en el asiento "
                    + PalindromoAir.codigoAsiento(posicion) + ".", false);
            asientoResaltado = posicion;
        }
        refrescarInterfaz();
    }

    private void consultarIngresos() {
        double total = avion.income(0);
        avion.mostrarMensaje("Ingresos actuales: " + PalindromoAir.dinero(total) + ".", false);
    }

    private void mostrarHistorial() {
        java.util.List<String> historial = avion.getHistorialDespachos();
        if (historial.isEmpty()) {
            avion.mostrarMensaje("Aún no se ha despachado ningún vuelo.", true);
            return;
        }
        avion.mostrarMensaje("Historial de despachos:", false);
        imprimirHistorial(0, historial);
    }

    private void imprimirHistorial(int indice, java.util.List<String> historial) {
        if (indice >= historial.size()) {
            return;
        }
        avion.mostrarMensaje("  " + historial.get(indice), false);
        imprimirHistorial(indice + 1, historial);
    }

    private void clicAsiento(int indice) {
        Ticket t = avion.getAvion()[indice];
        if (t != null) {
            JOptionPane.showMessageDialog(this,
                    "Asiento: " + PalindromoAir.codigoAsiento(indice) + "\n"
                    + "Pasajero: " + t.getNombre() + "\n"
                    + "Monto original: " + PalindromoAir.dinero(t.getMontoOriginal()) + "\n"
                    + "Monto pagado: " + PalindromoAir.dinero(t.getMontoPagado()) + "\n"
                    + "Palíndromo: " + (t.isEsPalindromo() ? "Sí (20% de descuento)" : "No"));
            return;
        }
        asientoSeleccionado = (asientoSeleccionado == indice) ? -1 : indice;
        asientoResaltado = -1;
        refrescarInterfaz();
    }

    private void refrescarInterfaz() {
        for (int i = 0; i < PalindromoAir.CAPACIDAD; i++) {
            avion.actualizarBoton(i);
            JButton boton = botonesAsientos[i];
            boton.setBorder(BorderFactory.createEmptyBorder());
            if (i == asientoSeleccionado) {
                boton.setBackground(PalindromoAir.COLOR_SELECCIONADO);
                boton.setText(PalindromoAir.codigoAsiento(i) + " (seleccionado)");
                boton.setToolTipText("Seleccionado - presione Sell Ticket");
            } else if (i == asientoResaltado) {
                boton.setBorder(BorderFactory.createLineBorder(PalindromoAir.COLOR_ENCONTRADO, 4));
            }
        }
        lblDisponibles.setText("Disponibles: " + avion.contarLibres(0));
        lblOcupados.setText("Ocupados: " + avion.contarOcupados(0));
        lblPalindromos.setText("Palíndromos vendidos: " + avion.contarPalindromos(0));
        lblIngresos.setText("Ingresos: " + PalindromoAir.dinero(avion.income(0)));
        lblAhorrado.setText("Ahorrado por descuentos: " + PalindromoAir.dinero(avion.ahorroTotal(0)));
    }

    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        leyenda.add(etiquetaLeyenda("Disponible", PalindromoAir.COLOR_DISPONIBLE));
        leyenda.add(etiquetaLeyenda("Ocupado (precio completo)", PalindromoAir.COLOR_OCUPADO));
        leyenda.add(etiquetaLeyenda("Palíndromo (20% descuento)", PalindromoAir.COLOR_PALINDROMO));
        leyenda.add(etiquetaLeyenda("Seleccionado", PalindromoAir.COLOR_SELECCIONADO));
        leyenda.add(etiquetaLeyenda("Pasajero encontrado", PalindromoAir.COLOR_ENCONTRADO));
        return leyenda;
    }

    private JLabel etiquetaLeyenda(String texto, Color color) {
        JLabel etiqueta = new JLabel(" " + texto + " ");
        etiqueta.setOpaque(true);
        etiqueta.setBackground(color);
        etiqueta.setForeground(color == PalindromoAir.COLOR_PALINDROMO ? Color.BLACK : Color.WHITE);
        return etiqueta;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LaboratorioSemana1().setVisible(true));
    }
}