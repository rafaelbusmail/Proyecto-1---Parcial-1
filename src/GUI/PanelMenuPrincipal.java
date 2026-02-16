

package GUI;

import logica.Battleship;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import GUI.PanelJuego;
import java.io.IOException;  
import java.net.URL;

public class PanelMenuPrincipal extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel panelContenido;
    
    private BufferedImage imagenFondo;
    
    private JLabel lblBienvenida;
    private JLabel lblPuntos;
    private JButton btnJugar;
    private JButton btnConfiguracion;
    private JButton btnReportes;
    private JButton btnPerfil;
    private JButton btnCerrarSesion;
    
    public PanelMenuPrincipal(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        
        cargarImagenFondo();
        configurarPanel();
        crearComponentes();
    }
    
    private void cargarImagenFondo() {
    try {
        
        URL url = getClass().getResource("/imagenes/fondo_menu.jpg");
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
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(44, 62, 80));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void crearComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Panel semitransparente de los botones esos
        JPanel panelMenu = new JPanel(new GridBagLayout());
        panelMenu.setBackground(new Color(0, 0, 0, 180));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbcMenu = new GridBagConstraints();
        gbcMenu.insets = new Insets(12, 10, 12, 10);
        gbcMenu.fill = GridBagConstraints.HORIZONTAL;
        gbcMenu.gridx = 0;
        
        // Mensaje de bienvenida
        lblBienvenida = new JLabel();
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        gbcMenu.gridy = 0;
        panelMenu.add(lblBienvenida, gbcMenu);
        
        // Puntos del jugador
        lblPuntos = new JLabel();
        lblPuntos.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPuntos.setForeground(new Color(255, 215, 0)); // Dorado
        lblPuntos.setHorizontalAlignment(SwingConstants.CENTER);
        gbcMenu.gridy = 1;
        panelMenu.add(lblPuntos, gbcMenu);
        
        // Espacio
        gbcMenu.gridy = 2;
        panelMenu.add(Box.createVerticalStrut(20), gbcMenu);
        
        // Botón Jugar
        btnJugar = crearBotonMenu("🎮 JUGAR BATTLESHIP");
        btnJugar.setBackground(new Color(39, 174, 96));
        btnJugar.addActionListener(e -> iniciarJuego());
        gbcMenu.gridy = 3;
        panelMenu.add(btnJugar, gbcMenu);
        
        // Botón Configuración
        btnConfiguracion = crearBotonMenu("⚙ CONFIGURACIÓN");
        btnConfiguracion.setBackground(new Color(52, 152, 219));
        btnConfiguracion.addActionListener(e -> mostrarConfiguracion());
        gbcMenu.gridy = 4;
        panelMenu.add(btnConfiguracion, gbcMenu);
        
        // Botón Reportes
        btnReportes = crearBotonMenu("📊 REPORTES");
        btnReportes.setBackground(new Color(155, 89, 182));
        btnReportes.addActionListener(e -> mostrarReportes());
        gbcMenu.gridy = 5;
        panelMenu.add(btnReportes, gbcMenu);
        
        // Botón Perfil
        btnPerfil = crearBotonMenu("👤 MI PERFIL");
        btnPerfil.setBackground(new Color(230, 126, 34));
        btnPerfil.addActionListener(e -> mostrarPerfil());
        gbcMenu.gridy = 6;
        panelMenu.add(btnPerfil, gbcMenu);
        
        // Botón Cerrar Sesión
        btnCerrarSesion = crearBotonMenu("🚪 CERRAR SESIÓN");
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.addActionListener(e -> manejarCerrarSesion());
        gbcMenu.gridy = 7;
        panelMenu.add(btnCerrarSesion, gbcMenu);
        
        // Agregar panel al centro
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panelMenu, gbc);
    }
    
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(350, 60));
        boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            Color colorOriginal = boton.getBackground();
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal.brighter());
                boton.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal);
                boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
        });
        
        return boton;
    }
    
    public void actualizarBienvenida() {
        if (Battleship.getUsuarioActual() != null) {
            lblBienvenida.setText("Bienvenido, " + Battleship.getUsuarioActual().getUsername());
            lblPuntos.setText("⭐ Puntos: " + Battleship.getUsuarioActual().getPuntos());
        }
    }
    
    private void iniciarJuego() {
        // Pedir nombre del jugador 2
        String usernameJ2 = JOptionPane.showInputDialog(this,
            "Ingrese el username del Jugador 2:\n(Escriba EXIT para cancelar)",
            "Jugador 2",
            JOptionPane.QUESTION_MESSAGE);
        
        if (usernameJ2 == null || usernameJ2.trim().equalsIgnoreCase("EXIT")) {
            return; // Cancelar
        }
        
        usernameJ2 = usernameJ2.trim();
        
        // Validar que el jugador 2 existe
        model.Player jugador2 = Battleship.buscarJugadorPorUsername(usernameJ2);
        
        if (jugador2 == null) {
            JOptionPane.showMessageDialog(this,
                "El jugador '" + usernameJ2 + "' no existe.\nPor favor intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (jugador2.getUsername().equals(Battleship.getUsuarioActual().getUsername())) {
            JOptionPane.showMessageDialog(this,
                "No puedes jugar contra ti mismo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Inicializar juego
        Battleship.inicializarJuego(Battleship.getUsuarioActual(), jugador2);
        
        // Ir a pantalla de juego
        for (Component comp : panelContenido.getComponents()) {
            if (comp instanceof PanelJuego) {
                ((PanelJuego) comp).iniciarPartida();
            }
        }
        
        cardLayout.show(panelContenido, "JUEGO");
    }
    
    private void mostrarConfiguracion() {
        // Submenú de configuración
        String[] opciones = {"Cambiar Dificultad", "Cambiar Modo de Juego", "Volver"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "Configuración Actual:\n" +
            "Dificultad: " + Battleship.getDificultad() + " (" + Battleship.getCantidadBarcos() + " barcos)\n" +
            "Modo: " + Battleship.getModoJuego() + "\n\n" +
            "¿Qué desea hacer?",
            "⚙ Configuración",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        switch (seleccion) {
            case 0: // Cambiar dificultad
                cambiarDificultad();
                break;
            case 1: // Cambiar modo
                cambiarModoJuego();
                break;
            case 2: // Volver
            default:
                break;
        }
    }
    
    private void cambiarDificultad() {
        String[] dificultades = {"EASY (5 barcos)", "NORMAL (4 barcos)", "EXPERT (2 barcos)", "GENIUS (1 barco)"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "Seleccione la dificultad:",
            "Dificultad",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            dificultades,
            dificultades[1]);
        
        switch (seleccion) {
            case 0:
                Battleship.setDificultad("EASY");
                break;
            case 1:
                Battleship.setDificultad("NORMAL");
                break;
            case 2:
                Battleship.setDificultad("EXPERT");
                break;
            case 3:
                Battleship.setDificultad("GENIUS");
                break;
            default:
                return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Dificultad cambiada a: " + Battleship.getDificultad() + "\n" +
            "Barcos por partida: " + Battleship.getCantidadBarcos(),
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cambiarModoJuego() {
        String[] modos = {"TUTORIAL (barcos visibles)", "ARCADE (barcos ocultos)"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "Seleccione el modo de juego:",
            "Modo de Juego",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            modos,
            modos[0]);
        
        switch (seleccion) {
            case 0:
                Battleship.setModoJuego("TUTORIAL");
                break;
            case 1:
                Battleship.setModoJuego("ARCADE");
                break;
            default:
                return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Modo cambiado a: " + Battleship.getModoJuego(),
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarReportes() {
        String[] opciones = {"Mis Últimos 10 Juegos", "Ranking de Jugadores", "Volver"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "¿Qué reporte desea ver?",
            "📊 Reportes",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        switch (seleccion) {
            case 0: // Últimos 10 juegos
                mostrarUltimosJuegos();
                break;
            case 1: // Ranking
                mostrarRanking();
                break;
            case 2: // Volver
            default:
                break;
        }
    }
    
    private void mostrarUltimosJuegos() {
        String logs = Battleship.getUsuarioActual().obtenerUltimos10Juegos();
        
        JTextArea textArea = new JTextArea(logs);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Mis Últimos 10 Juegos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarRanking() {
        String ranking = Battleship.obtenerRankingJugadores();
        
        JTextArea textArea = new JTextArea(ranking);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Ranking de Jugadores",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarPerfil() {
        String[] opciones = {"Ver Mis Datos", "Modificar Datos", "Eliminar Cuenta", "Volver"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "Gestión de Perfil",
            "👤 Mi Perfil",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        switch (seleccion) {
            case 0: // Ver datos
                verDatos();
                break;
            case 1: // Modificar datos
                modificarDatos();
                break;
            case 2: // Eliminar cuenta
                eliminarCuenta();
                break;
            case 3: // Volver
            default:
                break;
        }
    }
    
    private void verDatos() {
        model.Player usuario = Battleship.getUsuarioActual();
    
    // 1. Declarar y construir la variable asteriscos primero
    String asteriscos = "";
    for (int i = 0; i < usuario.getPassword().length(); i++) {
        asteriscos += "●";
    }
    
    // 2. Declarar y construir la variable datos usando asteriscos
    String datos = "═══════════════════════════════════\n" +
                   "           MIS DATOS\n" +
                   "═══════════════════════════════════\n\n" +
                   "Username: " + usuario.getUsername() + "\n" +
                   "Puntos: " + usuario.getPuntos() + "\n" +
                   "Password: " + asteriscos + "\n";
    
    // 3. Mostrar el diálogo
    JOptionPane.showMessageDialog(this,
        datos,
        "Mis Datos",
        JOptionPane.INFORMATION_MESSAGE);
}
    
    private void modificarDatos() {
        String[] opciones = {"Cambiar Username", "Cambiar Password", "Cancelar"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "¿Qué desea modificar?",
            "Modificar Datos",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        switch (seleccion) {
            case 0: // Cambiar username
                String nuevoUsername = JOptionPane.showInputDialog(this,
                    "Ingrese el nuevo username:",
                    Battleship.getUsuarioActual().getUsername());
                
                if (nuevoUsername != null && !nuevoUsername.trim().isEmpty()) {
                    if (Battleship.actualizarUsername(nuevoUsername.trim())) {
                        JOptionPane.showMessageDialog(this,
                            "Username actualizado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                        actualizarBienvenida();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "El username ya existe. Intente con otro.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
                
            case 1: // Cambiar password
                String nuevoPassword = JOptionPane.showInputDialog(this,
                    "Ingrese el nuevo password:");
                
                if (nuevoPassword != null && nuevoPassword.length() >= 4) {
                    Battleship.actualizarPassword(nuevoPassword);
                    JOptionPane.showMessageDialog(this,
                        "Password actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else if (nuevoPassword != null) {
                    JOptionPane.showMessageDialog(this,
                        "El password debe tener al menos 4 caracteres",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 2: // Cancelar
            default:
                break;
        }
    }
    
    private void eliminarCuenta() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "⚠ ¿Está SEGURO que desea eliminar su cuenta?\n\n" +
            "Esta acción NO se puede deshacer.\n" +
            "Perderá todos sus puntos y estadísticas.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Segunda confirmación
            String username = JOptionPane.showInputDialog(this,
                "Para confirmar, escriba su username:");
            
            if (username != null && username.equals(Battleship.getUsuarioActual().getUsername())) {
                Battleship.eliminarCuenta();
                JOptionPane.showMessageDialog(this,
                    "Cuenta eliminada exitosamente.\nHasta pronto.",
                    "Cuenta Eliminada",
                    JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(panelContenido, "LOGIN");
            } else if (username != null) {
                JOptionPane.showMessageDialog(this,
                    "Username incorrecto. Eliminación cancelada.",
                    "Cancelado",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void manejarCerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            Battleship.logout();
            cardLayout.show(panelContenido, "LOGIN");
        }
    }
}