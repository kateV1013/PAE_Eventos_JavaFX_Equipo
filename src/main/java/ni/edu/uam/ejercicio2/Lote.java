package ni.edu.uam.ejercicio2;

public class Lote {

    private String codigo;
    private String productor;
    private String variedad;
    private double peso;
    private String fechaEntrega;
    private String estado;
    private String observaciones;

    public Lote(String codigo, String productor, String variedad, double peso, String fechaEntrega, String estado, String observaciones) {
        this.codigo = codigo;
        this.productor = productor;
        this.variedad = variedad;
        this.peso = peso;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProductor() {
        return productor;
    }

    public void setProductor(String productor) {
        this.productor = productor;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
