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
    private double precioTotal;

    public CantidadProducto() {
    }

    public CantidadProducto(double cantidad) {
        setCantidad(cantidad);
    }

    public CantidadProducto(double cantidad, double precioTotal) {
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }
    
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
    
}
