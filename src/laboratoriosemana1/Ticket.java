package laboratoriosemana1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author USER
 */
public class Ticket {
    private String Nombre;
    private double montoOriginal;
    private double montoPagado;
    private boolean esPalindromo;
    
    public Ticket(String Name, double FinalAmount, double OriginalAmount, boolean isPalindrome){
        this.Nombre=Nombre;
        this.montoOriginal=montoOriginal;
        this.montoPagado=montoPagado;
        this.esPalindromo=esPalindromo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public double getMontoOriginal() {
        return montoOriginal;
    }

    public void setMontoOriginal(double montoOriginal) {
        this.montoOriginal = montoOriginal;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public boolean isEsPalindromo() {
        return esPalindromo;
    }

    public void setEsPalindromo(boolean esPalindromo) {
        this.esPalindromo = esPalindromo;
    }
    
    

    
    
    
    
    
    
}
