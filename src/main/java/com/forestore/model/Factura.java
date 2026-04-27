package com.forestore.model;

public class Factura {

    private Producto[] productos; // reemplaza p1, p2, p3
    private double subtotal;
    private double iva;
    private double total;

    public Factura(Producto[] productos) { //  recibe el arreglo completo
        this.productos = productos;
    }

    public double calcularSubTotal() {
        subtotal = 0;
        for (int i = 0; i < productos.length; i++) {       // bucle
            if (productos[i] != null) {
                subtotal += productos[i].calcularPrecioTotal(); // La sumatoria
            }
        }
        return subtotal;
    }

    public double calcularIVA() {
        this.iva = subtotal * 0.15;
        return this.iva;
    }

    public double calcularTotal() {
        this.total = subtotal + iva;
        return this.total;
    }

    public Producto[] getProductos() {
        return productos;
    }

    public void setProductos(Producto[] productos) {
        this.productos = productos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
