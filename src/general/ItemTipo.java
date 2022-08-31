/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

/**
 *
 * @author rafaelg
 */
public class ItemTipo{
    private char tipo;
    private String descripcion;

    public ItemTipo() {
    }

    public ItemTipo(char tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return this.descripcion;
    }
}
