/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

/**
 *
 * @author Propietario
 */
public class ItemMenu {
    private String ventana;
    private String descripcion;

    public ItemMenu() {
    }

    public ItemMenu(String ventana, String descripcion) {
        this.ventana = ventana;
        this.descripcion = descripcion;
    }

    public String getVentana() {
        return ventana;
    }

    public void setVentana(String ventana) {
        this.ventana = ventana;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
