package laboratoriosemana1;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Abril
 */
public class PalindromoAir {

    public static final int CAPACIDAD = 30;
    public static final int FILAS = 5;
    public static final int COLUMNAS = 6;
    public static final double PRECIO_BASE = 100.00;
    public static final double PORCENTAJE_DESCUENTO = 0.20;
    public static final String LETRAS_FILAS = "ABCDE";

    public static final Color COLOR_DISPONIBLE = new Color(76, 175, 80);    // verde
    public static final Color COLOR_OCUPADO = new Color(211, 47, 47);       // rojo
    public static final Color COLOR_PALINDROMO = new Color(255, 193, 7);    // dorado
    public static final Color COLOR_SELECCIONADO = new Color(120, 120, 120);// gris
    public static final Color COLOR_ENCONTRADO = Color.MAGENTA;

    private final Ticket[] avion = new Ticket[CAPACIDAD]; // arreglo de asientos
    private JButton[] botonesAsientos;                    // arreglo visual (uno por asiento)
    private JTextPane consola;                            // consola de la interfaz
    private final List<String> historialDespachos = new ArrayList<>();

    public void conectarInterfaz(JButton[] botones, JTextPane consolaInterfaz) {
        this.botonesAsientos = botones;
        this.consola = consolaInterfaz;
    }

    public Ticket[] getAvion() {
        return avion;
    }

    public List<String> getHistorialDespachos() {
        return historialDespachos;
    }

    public static String codigoAsiento(int indice) {
        return LETRAS_FILAS.charAt(indice / COLUMNAS) + "" + (indice % COLUMNAS + 1);
    }

    public static String dinero(double monto) {
        return String.format(Locale.US, "$%.2f", monto);
    }

   
    public int firstAvailable(int indice) {
        if (indice >= CAPACIDAD) {
            return -1;
        }
        if (avion[indice] == null) {
            return indice;
        }
        return firstAvailable(indice + 1);
    }

  
    public int searchPassenger(String name, int indice) {
        if (indice >= CAPACIDAD) {
            return -1;
        }
        if (avion[indice] != null && nombresIguales(avion[indice].getNombre(), name)) {
            return indice;
        }
        return searchPassenger(name, indice + 1);
    }

    public boolean isPalindromo(String name) {
        String texto = normalizar(name);
        if (texto.isEmpty()) {
            return false;
        }
        return esPalindromoRecursivo(texto, 0, texto.length() - 1);
    }

    private boolean esPalindromoRecursivo(String texto, int izquierda, int derecha) {
        if (izquierda >= derecha) {
            return true;
        }
        if (texto.charAt(izquierda) != texto.charAt(derecha)) {
            return false;
        }
        return esPalindromoRecursivo(texto, izquierda + 1, derecha - 1);
    }

   
    public void printPassengers(int indice) {
        if (!hayPasajeros(0)) {
            mostrarMensaje("Sin pasajeros registrados.", true);
            return;
        }
        imprimirPasajeros(indice);
    }

    private boolean hayPasajeros(int indice) {
        if (indice >= CAPACIDAD) {
            return false;
        }
        if (avion[indice] != null) {
            return true;
        }
        return hayPasajeros(indice + 1);
    }

    private void imprimirPasajeros(int indice) {
        if (indice >= CAPACIDAD) {
            return;
        }
        if (avion[indice] != null) {
            Ticket t = avion[indice];
            mostrarMensaje(codigoAsiento(indice) + " | " + t.getNombre()
                    + " | Pagó: " + dinero(t.getMontoPagado())
                    + " | Original: " + dinero(t.getMontoOriginal())
                    + " | Palíndromo: " + (t.isEsPalindromo() ? "Sí" : "No"), false);
        }
        imprimirPasajeros(indice + 1);
    }

    public double income(int indice) {
        if (indice >= CAPACIDAD) {
            return 0;
        }
        double total = (avion[indice] != null) ? avion[indice].getMontoPagado() : 0;
        return total + income(indice + 1);
    }

    public double ahorroTotal(int indice) {
        if (indice >= CAPACIDAD) {
            return 0;
        }
        double ahorro = (avion[indice] != null)
                ? (avion[indice].getMontoOriginal() - avion[indice].getMontoPagado()) : 0;
        return ahorro + ahorroTotal(indice + 1);
    }

    public int contarLibres(int indice) {
        if (indice >= CAPACIDAD) {
            return 0;
        }
        return (avion[indice] == null ? 1 : 0) + contarLibres(indice + 1);
    }

    public int contarOcupados(int indice) {
        if (indice >= CAPACIDAD) {
            return 0;
        }
        return (avion[indice] != null ? 1 : 0) + contarOcupados(indice + 1);
    }

    public int contarPalindromos(int indice) {
        if (indice >= CAPACIDAD) {
            return 0;
        }
        return (avion[indice] != null && avion[indice].isEsPalindromo() ? 1 : 0) + contarPalindromos(indice + 1);
    }

    
    public void reset(int indice) {
        if (indice >= CAPACIDAD) {
            return;
        }
        avion[indice] = null;
        actualizarBoton(indice);
        reset(indice + 1);
    }

    public boolean sellTicket(String name) {
        return venderBoleto(name, firstAvailable(0));
    }

    public boolean sellTicketEnAsiento(String name, int indiceAsiento) {
        return venderBoleto(name, indiceAsiento);
    }

    private boolean venderBoleto(String name, int indiceAsiento) {
        if (!nombreValido(name)) {
            mostrarMensaje("Ingrese un nombre válido.", true);
            return false;
        }
        if (indiceAsiento < 0 || indiceAsiento >= CAPACIDAD) {
            mostrarMensaje("Avión lleno. No se puede vender el boleto.", true);
            return false;
        }
        if (avion[indiceAsiento] != null) {
            mostrarMensaje("El asiento " + codigoAsiento(indiceAsiento) + " ya está ocupado. Elija otro.", true);
            return false;
        }
        if (searchPassenger(name, 0) != -1) {
            mostrarMensaje("El pasajero ya tiene un asiento asignado.", true);
            return false;
        }
        boolean palindromo = isPalindromo(name);
        double montoPagado = palindromo ? PRECIO_BASE * (1 - PORCENTAJE_DESCUENTO) : PRECIO_BASE;
        avion[indiceAsiento] = new Ticket(name, PRECIO_BASE, montoPagado, palindromo);
        actualizarBoton(indiceAsiento);
        int porDescuento = (int) Math.round(PORCENTAJE_DESCUENTO * 100);
        mostrarMensaje("Vendido: " + name + " en " + codigoAsiento(indiceAsiento)
                + ", pagó " + dinero(montoPagado)
                + (palindromo ? " (palíndromo, " + porDescuento + "% de descuento)." : "."), false);
        return true;
    }

    public boolean cancelTicket(String name) {
        if (!nombreValido(name)) {
            mostrarMensaje("Ingrese un nombre válido.", true);
            return false;
        }
        int posicion = searchPassenger(name, 0);
        if (posicion == -1) {
            mostrarMensaje("No se encontró al pasajero. No se pudo cancelar.", true);
            return false;
        }
        double reembolso = avion[posicion].getMontoPagado();
        String nombre = avion[posicion].getNombre();
        avion[posicion] = null;
        actualizarBoton(posicion);
        mostrarMensaje("Cancelado: " + nombre + ". Reembolso de " + dinero(reembolso) + ".", false);
        return true;
    }

    public void dispatch() {
        int opcion = JOptionPane.showConfirmDialog(null,
                "¿Despachar el vuelo? Los asientos quedarán libres.",
                "Despachar vuelo", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        double ingresos = income(0);
        historialDespachos.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())
                + " - Ingresos: " + dinero(ingresos));
        mostrarMensaje("Ingresos totales: " + dinero(ingresos) + ". Vuelo despachado, asientos reiniciados.", false);
        reset(0);
    }


    public boolean nombreValido(String name) {
        return !normalizar(name).isEmpty();
    }

    private boolean nombresIguales(String a, String b) {
        return normalizar(a).equals(normalizar(b));
    }

    private String normalizar(String texto) {
        return normalizar(texto, 0, new StringBuilder());
    }

    private String normalizar(String texto, int indice, StringBuilder resultado) {
        if (indice >= texto.length()) {
            return resultado.toString();
        }
        char c = quitarTilde(Character.toLowerCase(texto.charAt(indice)));
        if (Character.isLetterOrDigit(c)) {
            resultado.append(c);
        }
        return normalizar(texto, indice + 1, resultado);
    }

    private char quitarTilde(char c) {
        switch (c) {
            case 'á': return 'a';
            case 'é': return 'e';
            case 'í': return 'i';
            case 'ó': return 'o';
            case 'ú': return 'u';
            case 'ü': return 'u';
            default: return c;
        }
    }

    public void actualizarBoton(int indice) {
        if (botonesAsientos == null) {
            return;
        }
        JButton boton = botonesAsientos[indice];
        Ticket t = avion[indice];
        if (t == null) {
            boton.setBackground(COLOR_DISPONIBLE);
            boton.setText(codigoAsiento(indice));
            boton.setToolTipText(codigoAsiento(indice) + " - Disponible");
        } else {
            boton.setBackground(t.isEsPalindromo() ? COLOR_PALINDROMO : COLOR_OCUPADO);
            boton.setText("<html><center>" + codigoAsiento(indice) + "<br>" + t.getNombre()
                    + "<br>" + dinero(t.getMontoPagado()) + "</center></html>");
            boton.setToolTipText(codigoAsiento(indice) + " - " + t.getNombre() + " - "
                    + dinero(t.getMontoPagado()) + (t.isEsPalindromo() ? " (palíndromo)" : ""));
        }
    }

    public void mostrarMensaje(String texto, boolean esError) {
        System.out.println(texto);
        if (consola == null) {
            return;
        }
        StyledDocument documento = consola.getStyledDocument();
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, esError ? new Color(255, 90, 90) : new Color(120, 255, 120));
        try {
            documento.insertString(documento.getLength(), texto + "\n", estilo);
        } catch (Exception e) {
        }
        consola.setCaretPosition(documento.getLength());
    }

    public boolean exportarPasajeros(String ruta) {
        if (!hayPasajeros(0)) {
            mostrarMensaje("Sin pasajeros registrados.", true);
            return false;
        }
        try (PrintWriter escritor = new PrintWriter(new File(ruta))) {
            exportar(0, escritor);
            mostrarMensaje("Lista de pasajeros exportada en: " + ruta, false);
            return true;
        } catch (FileNotFoundException e) {
            mostrarMensaje("No se pudo exportar la lista de pasajeros.", true);
            return false;
        }
    }

    private boolean exportar(int indice, PrintWriter escritor) {
        if (indice >= CAPACIDAD) {
            return true;
        }
        if (avion[indice] != null) {
            Ticket t = avion[indice];
            escritor.println(codigoAsiento(indice) + ";" + t.getNombre() + ";"
                    + String.format(Locale.US, "%.2f", t.getMontoPagado()) + ";"
                    + (t.isEsPalindromo() ? "Si" : "No"));
        }
        return exportar(indice + 1, escritor);
    }
}
