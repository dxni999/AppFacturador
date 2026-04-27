package com.forestore.ui;

import javax.swing.*;
import java.awt.*;
import com.forestore.model.Producto;
import com.forestore.model.Factura;

public class StarterForm extends JFrame {

    // ── Arreglo de productos ──────────────────────────────────────
    private Producto[] productos = new Producto[3];
    private int cont = 0;

    // ── Componentes ───────────────────────────────────────────────
    private JLabel     lblPaso;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JButton    btnSiguiente;
    private JButton    btnSaltar;      // ← NUEVO

    public StarterForm() {
        initComponents();
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE LA VENTANA
    // ─────────────────────────────────────────────────────────────
    private void initComponents() {
        setTitle("Facturador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // ── Título ────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("FACTURADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // ── Formulario ────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 12));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        lblPaso = new JLabel("Producto 1 de 3");
        lblPaso.setFont(new Font("Arial", Font.ITALIC, 13));
        panelForm.add(lblPaso);
        panelForm.add(new JLabel());

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelForm.add(txtPrecio);

        panelForm.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelForm.add(txtCantidad);

        add(panelForm, BorderLayout.CENTER);

        // ── Botones ───────────────────────────────────────────────
        btnSiguiente = new JButton("Siguiente →");
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 14));
        btnSiguiente.setPreferredSize(new Dimension(160, 38));

        // ── Botón Saltar (oculto al inicio) ───────────────────────
        btnSaltar = new JButton("Saltar Producto");
        btnSaltar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnSaltar.setPreferredSize(new Dimension(150, 38));
        btnSaltar.setVisible(false);   // oculto hasta que se guarde el 1er producto

        JPanel panelBtn = new JPanel();
        panelBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelBtn.add(btnSaltar);       // ← se añade al panel
        panelBtn.add(btnSiguiente);
        add(panelBtn, BorderLayout.SOUTH);

        btnSiguiente.addActionListener(e -> guardarYAvanzar());
        btnSaltar.addActionListener(e -> saltarProducto());   // ← NUEVO listener
    }

    // ─────────────────────────────────────────────────────────────
    //  LÓGICA: saltar el producto actual e ir directo a la factura
    // ─────────────────────────────────────────────────────────────
    private void saltarProducto() {
        // Solo permite saltar si ya hay al menos 1 producto guardado (cont >= 1)
        mostrarFactura();
    }

    // ─────────────────────────────────────────────────────────────
    //  LÓGICA: guardar producto y avanzar al siguiente
    // ─────────────────────────────────────────────────────────────
    private void guardarYAvanzar() {

        String nombre      = txtNombre.getText().trim();
        String precioStr   = txtPrecio.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();

        if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio;
        int cantidad;
        try {
            precio   = Double.parseDouble(precioStr.replace(",", "."));
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio debe ser número decimal (ej: 1.50)\nCantidad debe ser número entero.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        productos[cont] = new Producto(nombre, cantidad, precio);
        cont++;

        // ── Mostrar botón Saltar en cuanto hay al menos 1 producto ──
        btnSaltar.setVisible(true);   // ← NUEVO

        if (cont < 3) {
            limpiarCampos();
            lblPaso.setText("Producto " + (cont + 1) + " de 3");

            if (cont == 2) {
                btnSiguiente.setText("Ver Factura ✓");
            }
        } else {
            mostrarFactura();
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  VENTANA NUEVA: mostrar la factura
    // ─────────────────────────────────────────────────────────────
    private void mostrarFactura() {

        Factura factura = new Factura(productos);
        factura.calcularSubTotal();
        factura.calcularIVA();
        factura.calcularTotal();

        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("                  FACTURA                  \n");
        sb.append("============================================\n");
        sb.append(String.format("%-15s %6s %9s %10s\n",
                "Nombre", "Cant.", "P.Unit", "Total"));
        sb.append("--------------------------------------------\n");

        for (Producto p : productos) {
            if (p != null) {
                String descuento = p.getCantidad() > 6 ? " (-15%)" : "";
                sb.append(String.format("%-15s %6d %9.2f %10.2f%s\n",
                        p.getNombre(),
                        p.getCantidad(),
                        p.getPrecio(),
                        p.calcularPrecioTotal(),
                        descuento));
            }
        }

        sb.append("--------------------------------------------\n");
        sb.append(String.format("%-30s %11.2f\n", "Subtotal:", factura.getSubtotal()));
        sb.append(String.format("%-30s %11.2f\n", "IVA (15%):", factura.getIva()));
        sb.append(String.format("%-30s %11.2f\n", "TOTAL:", factura.getTotal()));
        sb.append("============================================\n");

        JDialog dialog = new JDialog(this, "Factura", true);
        dialog.setSize(460, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTextArea txtFactura = new JTextArea(sb.toString());
        txtFactura.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtFactura.setEditable(false);
        txtFactura.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(new JScrollPane(txtFactura), BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());
        JPanel panelBtn = new JPanel();
        panelBtn.add(btnCerrar);
        dialog.add(panelBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────
    //  UTILIDAD: limpiar los 3 campos
    // ─────────────────────────────────────────────────────────────
    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtNombre.requestFocus();
    }
}
