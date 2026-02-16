package GUI;

import logica.Battleship;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;  
import java.net.URL;          

public class PanelLogin extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel panelContenido;
    
    private BufferedImage imagenFondo;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JButton btnSalir;
    private JLabel lblError;
    
    public PanelLogin(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        
        cargarImagenFondo();
        configurarPanel();
        crearComponentes();
    }
    
    private void cargarImagenFondo() {
    try {
        
        
        URL url = getClass().getResource("/imagenes/fondo_login.jpg");
        if (url != null) {
            imagenFondo = ImageIO.read(url);
        } else {
            System.err.println("No se encontró la imagen en: /imagenes/fondo_login.jpg");
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
            // Dibujar imagen de fondo escalada
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Color de fondo por defecto si no hay imagen
            g.setColor(new Color(41, 128, 185));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void crearComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Panel semitransparente para los componentes
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(0, 0, 0, 180)); // Negro semi-transparente
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 10, 10, 10);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridx = 0;
        
        // Título
        JLabel lblTitulo = new JLabel("🚢 BATTLESHIP DINÁMICO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbcForm.gridy = 0;
        gbcForm.gridwidth = 2;
        panelFormulario.add(lblTitulo, gbcForm);
        gbcForm.gridwidth = 1;
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Iniciar Sesión");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbcForm.gridy = 1;
        gbcForm.gridwidth = 2;
        panelFormulario.add(lblSubtitulo, gbcForm);
        gbcForm.gridwidth = 1;
        
        // Username label
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsername.setForeground(Color.WHITE);
        gbcForm.gridx = 0;
        gbcForm.gridy = 2;
        panelFormulario.add(lblUsername, gbcForm);
        
        // Username field
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        gbcForm.gridx = 1;
        gbcForm.gridy = 2;
        panelFormulario.add(txtUsername, gbcForm);
        
        // Password label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setForeground(Color.WHITE);
        gbcForm.gridx = 0;
        gbcForm.gridy = 3;
        panelFormulario.add(lblPassword, gbcForm);
        
        // Password field
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbcForm.gridx = 1;
        gbcForm.gridy = 3;
        panelFormulario.add(txtPassword, gbcForm);
        
        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Arial", Font.ITALIC, 12));
        lblError.setForeground(new Color(255, 100, 100));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        gbcForm.gridx = 0;
        gbcForm.gridy = 4;
        gbcForm.gridwidth = 2;
        panelFormulario.add(lblError, gbcForm);
        gbcForm.gridwidth = 1;
        
        // Login button
        btnLogin = crearBoton("Iniciar Sesión", new Color(39, 174, 96));
        btnLogin.addActionListener(e -> manejarLogin());
        gbcForm.gridx = 0;
        gbcForm.gridy = 5;
        gbcForm.gridwidth = 2;
        panelFormulario.add(btnLogin, gbcForm);
        
        // Register button
        btnRegistro = crearBoton("Crear Cuenta", new Color(52, 152, 219));
        btnRegistro.addActionListener(e -> cardLayout.show(panelContenido, "REGISTRO"));
        gbcForm.gridy = 6;
        panelFormulario.add(btnRegistro, gbcForm);
        
        // Exit button
        btnSalir = crearBoton("Salir", new Color(231, 76, 60));
        btnSalir.addActionListener(e -> System.exit(0));
        gbcForm.gridy = 7;
        panelFormulario.add(btnSalir, gbcForm);
        
        // Agregar panel de formulario al panel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panelFormulario, gbc);
        
        // Add Enter key listener
        txtPassword.addActionListener(e -> manejarLogin());
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(300, 45));
        
        // Efecto hover
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
    
    private void manejarLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos");
            return;
        }
        
        if (Battleship.login(username, password)) {
            lblError.setText(" ");
            txtUsername.setText("");
            txtPassword.setText("");
            
            // Actualizar mensaje de bienvenida en el menú
            for (Component comp : panelContenido.getComponents()) {
                if (comp instanceof PanelMenuPrincipal) {
                    ((PanelMenuPrincipal) comp).actualizarBienvenida();
                }
            }
            
            cardLayout.show(panelContenido, "MENU");
        } else {
            mostrarError("Usuario o contraseña incorrectos");
        }
    }
    
    private void mostrarError(String mensaje) {
        lblError.setText("⚠ " + mensaje);
    }
}