package com.forestore.ui;

import javax.swing.*; // ventana, texto, campo de entrada
import java.awt.*; // para definir colores y fuentes * -> importar paquete
import com.forestore.model.Producto;
import com.forestore.model.Factura;

public class StarterForm extends JFrame {

    //  Arreglo de productos
    private Producto[] productos = new Producto[5];
    private int cont = 0;

    // Componentes
    private JLabel     lblPaso;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JButton    btnSiguiente;
    private JButton    btnSaltar;

    public StarterForm() { //inicializar constructor
        initComponents();
    }

    //  CONSTRUCCIÓN DE LA VENTANA

    private void initComponents() {
        setTitle("Facturador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Si aplastas x el programa se detiene
        setSize(420, 320);
        setLocationRelativeTo(null); //Centra en la pantalla
        setResizable(false);
        setLayout(new BorderLayout(10, 10)); //espacios en blanco entre zonas

        // Título
        JLabel lblTitulo = new JLabel("FACTURADOR", SwingConstants.CENTER); //Etiqueta
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24)); //Negrita
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); //MARGIN
        add(lblTitulo, BorderLayout.NORTH); //Parte superior de la aplicacion

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 12));//Jpanel es una caja invisible y GridLayou hace cuadricula perfecta
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        lblPaso = new JLabel("Producto 1 de 5");
        lblPaso.setFont(new Font("Arial", Font.ITALIC, 13));
        panelForm.add(lblPaso);
        panelForm.add(new JLabel()); //Salta a la fila 2

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelForm.add(txtPrecio);

        panelForm.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelForm.add(txtCantidad);

        add(panelForm, BorderLayout.CENTER); //Anclar al centro

        // Botones
        btnSiguiente = new JButton("Siguiente →");
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 14));
        btnSiguiente.setPreferredSize(new Dimension(160, 38));

        // Botón Saltar (oculto al inicio)
        btnSaltar = new JButton("Saltar Producto");
        btnSaltar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnSaltar.setPreferredSize(new Dimension(150, 38)); // Respeta tamaño
        btnSaltar.setVisible(false);   // oculto hasta que se guarde el 1er producto

        JPanel panelBtn = new JPanel(); //meter ambos botones, por defecto FlowLayout
        panelBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelBtn.add(btnSaltar);       //  se añade al panel
        panelBtn.add(btnSiguiente);
        add(panelBtn, BorderLayout.SOUTH);

        btnSiguiente.addActionListener(e -> guardarYAvanzar());
        btnSaltar.addActionListener(e -> saltarProducto());   //  NUEVO listener
    }

    //  LOGICA: saltar el producto actual e ir directo a la factura

    private void saltarProducto() {
        // Solo permite saltar si ya hay al menos 1 producto guardado (cont >= 1)
        mostrarFactura(); //Final del proceso
    }

    //  LOGICA: guardar producto y avanzar al siguiente

    private void guardarYAvanzar() {
        //Extrae lo que el usuario escribio en el interfaz
        String nombre      = txtNombre.getText().trim();
        String precioStr   = txtPrecio.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();// trim -< Evita errores de formato
        //Campos Vacios
        if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, //Ventana de error
                    "Por favor completa todos los campos.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        //Error de formato en numeros, atrapa antes que se rompa el programa
        double precio;
        int cantidad;
        try {
            precio   = Double.parseDouble(precioStr.replace(",", ".")); // pasa de string a double
            cantidad = Integer.parseInt(cantidadStr); //String a int
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio debe ser número decimal (ej: 1.50)\nCantidad debe ser número entero.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return; //Sale del metodo y no guarda nada
        }
        //Guardar los productos
        productos[cont] = new Producto(nombre, cantidad, precio);
        cont++;

        // Mostrar botón Saltar en cuanto hay al menos 1 producto
        btnSaltar.setVisible(true);

        if (cont < 5) {
            limpiarCampos();
            lblPaso.setText("Producto " + (cont + 1) + " de 5"); //Actualiza titulo segun el producto que pongas

            if (cont == 4) {
                btnSiguiente.setText("Ver Factura ✓");
            }
        } else {
            mostrarFactura();
        }
    }

    //  VENTANA NUEVA: mostrar la factura

    private void mostrarFactura() {

        Factura factura = new Factura(productos);
        factura.calcularSubTotal();
        factura.calcularIVA();
        factura.calcularTotal();

        StringBuilder sb = new StringBuilder(); //Transfora textos largos de forma eficiente
        sb.append("============================================\n");
        sb.append("                  FACTURA                  \n");
        sb.append("============================================\n");
        sb.append(String.format("%-15s %6s %9s %10s\n",
                "Nombre", "Cant.", "P.Unit", "Total"));
        sb.append("--------------------------------------------\n");

        for (Producto p : productos) { //Cambia el for ya que ya esta creado
            if (p != null) { //Ignota el producto y no se rompe
                String descuento = p.getCantidad() > 6 ? " (-15%)" : ""; //Operador ternario
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

        JDialog dialog = new JDialog(this, "Factura", true); //Jdialog ventana secundaria
        dialog.setSize(460, 280); //Tamaño
        dialog.setLocationRelativeTo(this); //Centrada
        dialog.setLayout(new BorderLayout(10, 10)); //misma organizacion por zonas

        JTextArea txtFactura = new JTextArea(sb.toString());  //Recibe el texto
        txtFactura.setFont(new Font("Monospaced", Font.PLAIN, 13)); //Mismo ancho
        txtFactura.setEditable(false); //No es modificable
        txtFactura.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(new JScrollPane(txtFactura), BorderLayout.CENTER); //Por si el texto es mas largo se alinea

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose()); //dispose destuye la ventana
        JPanel panelBtn = new JPanel();
        panelBtn.add(btnCerrar);
        dialog.add(panelBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    //  UTILIDAD: limpiar los 3 campos
    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtNombre.requestFocus();
    }
}
