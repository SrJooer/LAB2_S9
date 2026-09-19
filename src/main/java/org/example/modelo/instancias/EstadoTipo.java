package org.example.modelo.instancias;

public final class EstadoTipo {

    public static final EstadoTipo RECIBIDO = new EstadoTipo("En cola");
    public static final EstadoTipo ALMACENADO = new EstadoTipo("Almacenado");
    public static final EstadoTipo CLASIFICANDO = new EstadoTipo("Clasificando");
    public static final EstadoTipo CLASIFICADO = new EstadoTipo("Clasificado");
    public static final EstadoTipo EMPAQUETANDO = new EstadoTipo("Empaquetando");
    public static final EstadoTipo EMPAQUETADO = new EstadoTipo("Empaquetado");
    public static final EstadoTipo EN_EXPEDICION = new EstadoTipo("En expedición");
    public static final EstadoTipo EN_REPARTO = new EstadoTipo("En reparto");
    public static final EstadoTipo ENTREGADO = new EstadoTipo("Entregado");

    public final String estado;

    private EstadoTipo(String estado) {
        this.estado = estado;
    }

}
