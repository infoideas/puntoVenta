package entidades;
// Generated 26 ago. 2022 21:12:34 by Hibernate Tools 4.3.1



/**
 * Empresa generated by hbm2java
 */
public class Empresa  implements java.io.Serializable {


     private Integer id;
     private Pais pais;
     private Provincia provincia;
     private String nombreFantasia;
     private String razonSocial;
     private String cuit;
     private String direccion;
     private String telefonoFijo;
     private String telefonoMovil;
     private String contacto;
     private String email;
     private char estado;
     private String sitioWeb;
     private byte[] logo;
     private String emailSistemas;
     private String claveEmail;
     private String hostSmtp;
     private Short puertoEmail;
     private String startTls;
     private String auth;
     private String observaciones;

    public Empresa() {
    }

	
    public Empresa(Pais pais, Provincia provincia, String nombreFantasia, char estado) {
        this.pais = pais;
        this.provincia = provincia;
        this.nombreFantasia = nombreFantasia;
        this.estado = estado;
    }
    public Empresa(Pais pais, Provincia provincia, String nombreFantasia, String razonSocial, String cuit, String direccion, String telefonoFijo, String telefonoMovil, String contacto, String email, char estado, String sitioWeb, byte[] logo, String emailSistemas, String claveEmail, String hostSmtp, Short puertoEmail, String startTls, String auth, String observaciones) {
       this.pais = pais;
       this.provincia = provincia;
       this.nombreFantasia = nombreFantasia;
       this.razonSocial = razonSocial;
       this.cuit = cuit;
       this.direccion = direccion;
       this.telefonoFijo = telefonoFijo;
       this.telefonoMovil = telefonoMovil;
       this.contacto = contacto;
       this.email = email;
       this.estado = estado;
       this.sitioWeb = sitioWeb;
       this.logo = logo;
       this.emailSistemas = emailSistemas;
       this.claveEmail = claveEmail;
       this.hostSmtp = hostSmtp;
       this.puertoEmail = puertoEmail;
       this.startTls = startTls;
       this.auth = auth;
       this.observaciones = observaciones;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Pais getPais() {
        return this.pais;
    }
    
    public void setPais(Pais pais) {
        this.pais = pais;
    }
    public Provincia getProvincia() {
        return this.provincia;
    }
    
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    public String getNombreFantasia() {
        return this.nombreFantasia;
    }
    
    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
    }
    public String getRazonSocial() {
        return this.razonSocial;
    }
    
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    public String getCuit() {
        return this.cuit;
    }
    
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getTelefonoFijo() {
        return this.telefonoFijo;
    }
    
    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }
    public String getTelefonoMovil() {
        return this.telefonoMovil;
    }
    
    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }
    public String getContacto() {
        return this.contacto;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public char getEstado() {
        return this.estado;
    }
    
    public void setEstado(char estado) {
        this.estado = estado;
    }
    public String getSitioWeb() {
        return this.sitioWeb;
    }
    
    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }
    public byte[] getLogo() {
        return this.logo;
    }
    
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    public String getEmailSistemas() {
        return this.emailSistemas;
    }
    
    public void setEmailSistemas(String emailSistemas) {
        this.emailSistemas = emailSistemas;
    }
    public String getClaveEmail() {
        return this.claveEmail;
    }
    
    public void setClaveEmail(String claveEmail) {
        this.claveEmail = claveEmail;
    }
    public String getHostSmtp() {
        return this.hostSmtp;
    }
    
    public void setHostSmtp(String hostSmtp) {
        this.hostSmtp = hostSmtp;
    }
    public Short getPuertoEmail() {
        return this.puertoEmail;
    }
    
    public void setPuertoEmail(Short puertoEmail) {
        this.puertoEmail = puertoEmail;
    }
    public String getStartTls() {
        return this.startTls;
    }
    
    public void setStartTls(String startTls) {
        this.startTls = startTls;
    }
    public String getAuth() {
        return this.auth;
    }
    
    public void setAuth(String auth) {
        this.auth = auth;
    }
    public String getObservaciones() {
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }




}

