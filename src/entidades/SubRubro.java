package entidades;
// Generated 26 ago. 2022 21:12:34 by Hibernate Tools 4.3.1



/**
 * SubRubro generated by hbm2java
 */
public class SubRubro  implements java.io.Serializable {


     private Integer id;
     private Rubro rubro;
     private String nombre;

    public SubRubro() {
    }

    public SubRubro(Rubro rubro, String nombre) {
       this.rubro = rubro;
       this.nombre = nombre;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Rubro getRubro() {
        return this.rubro;
    }
    
    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}


