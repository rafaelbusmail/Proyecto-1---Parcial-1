package GUI;

import logica.Battleship;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class PanelRegistro extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel panelContenido;
    
    private BufferedImage imagenFondo;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnRegistrar;
    private JButton btnVolver;
    private JLabel lblError;
    
    public PanelRegistro(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        
        cargarImagenFondo();
        configurarPanel();
        crearComponentes();
    }
    
    private void cargarImagenFondo() {
        try {
            URL url = getClass().getResource("/imagenes/fondo_registro.jpg");
            if (url != null) {
                imagenFondo = ImageIO.read(url);
            } else {
                System.err.println("No se encontró la imagen en: /imagenes/fondo_registro.jpg");
                imagenFondo = null;
            }
        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            imagenFondo = null;
        }
    }
    
    private void configurarPanel() {
        setLayout(new GridBagLayout());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(52, 152, 219));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void crearComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(0, 0, 0, 180));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 10, 10, 10);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridx = 0;
        
        JLabel lblTitulo = new JLabel("🚢 CREAR CUENTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbcForm.gridy = 0;
        gbcForm.gridwidth = 2;
        panelFormulario.add(lblTitulo, gbcForm);
        gbcForm.gridwidth = 1;
        
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsername.setForeground(Color.WHITE);
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        panelFormulario.add(lblUsername, gbcForm);
        
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        gbcForm.gridx = 1;
        gbcForm.gridy = 1;
        panelFormulario.add(txtUsername, gbcForm);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setForeground(Color.WHITE);
        gbcForm.gridx = 0;
        gbcForm.gridy = 2;
        panelFormulario.add(lblPassword, gbcForm);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbcForm.gridx = 1;
        gbcForm.gridy = 2;
        panelFormulario.add(txtPassword, gbcForm);
        
        JLabel lblConfirmar = new JLabel("Confirmar:");
        lblConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        lblConfirmar.setForeground(Color.WHITE);
        gbcForm.gridx = 0;
        gbcForm.gridy = 3;
        panelFormulario.add(lblConfirmar, gbcForm);
        
        txtConfirmarPassword = new JPasswordField(20);
        txtConfirmarPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbcForm.gridx = 1;
        gbcForm.gridy = 3;
        panelFormulario.add(txtConfirmarPassword, gbcForm);
        
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Arial", Font.ITALIC, 12));
        lblError.setForeground(new Color(255, 100, 100));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        gbcForm.gridx = 0;
        gbcForm.gridy = 4;
        gbcForm.gridwidth = 2;
        panelFormulario.add(lblError, gbcForm);
        gbcForm.gridwidth = 1;
        
        btnRegistrar = crearBoton("Crear Cuenta", new Color(39, 174, 96));
        btnRegistrar.addActionListener(e -> manejarRegistro());
        gbcForm.gridx = 0;
        gbcForm.gridy = 5;
        gbcForm.gridwidth = 2;
        panelFormulario.add(btnRegistrar, gbcForm);
        
        btnVolver = crearBoton("Volver al Login", new Color(149, 165, 166));
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            cardLayout.show(panelContenido, "LOGIN");
        });
        gbcForm.gridy = 6;
        panelFormulario.add(btnVolver, gbcForm);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panelFormulario, gbc);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(300, 45));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            Color colorOriginal = color;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal);
            }
        });
        
        return boton;
    }
    
    private void manejarRegistro() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmarPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
            mostrarError("Por favor complete todos los campos");
            return;
        }
        
        if (username.length() < 3) {
            mostrarError("El username debe tener al menos 3 caracteres");
            return;
        }
        
        if (password.length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres");
            return;
        }
        
        if (!password.equals(confirmar)) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }
        
        if (Battleship.registrarJugador(username, password)) {
            JOptionPane.showMessageDialog(this,
                "¡Cuenta creada exitosamente!\nBienvenido " + username,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            
            for (Component comp : panelContenido.getComponents()) {
                if (comp instanceof PanelMenuPrincipal) {
                    ((PanelMenuPrincipal) comp).actualizarBienvenida();
                }
            }
            
            cardLayout.show(panelContenido, "MENU");
        } else {
            mostrarError("El username ya existe. Intente con otro.");
        }
    }
    
    private void mostrarError(String mensaje) {
        lblError.setText("⚠ " + mensaje);
    }
    
    private void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmarPassword.setText("");
        lblError.setText(" ");
    }
}