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
    private double cantidad;

    public CantidadProducto() {
    }

    public CantidadProducto(double cantidad) {
        setCantidad(cantidad);
    }
    
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
}
