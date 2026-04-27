package com.forestore.model;

public class Producto {

    private String nombre;
    private int cantidad;
    private double precio;

    public Producto(String nombre, int cantidad, double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public double calcularPrecioTotal(){
        double total =this.cantidad*this.precio;
        if (this.cantidad > 6){
            total*=0.85;
        }
        return total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Nombre: "   + getNombre()                          + "\n" +
                "Cantidad: " + getCantidad()                        + "\n" +
                "Precio:   " + String.format("%.2f", getPrecio())   + "\n" +
                "Total:    " + String.format("%.2f", calcularPrecioTotal()) + "\n";
    }

}
