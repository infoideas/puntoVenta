/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author rafael
 */
public class CantidadProducto {
    private int cantidad;

    public CantidadProducto() {
    }

    public CantidadProducto(int cantidad) {
        setCantidad(cantidad);
    }
    
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
}
