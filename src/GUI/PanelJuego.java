package GUI;

import logica.Battleship;
import model.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
import GUI.PanelMenuPrincipal;

public class PanelJuego extends JPanel {
    
    // ═══════════════════════════════════════════════════════════
    //                    VARIABLES DE INSTANCIA
    // ═══════════════════════════════════════════════════════════
    
    private final CardLayout cardLayout;
    private final JPanel panelContenido;
    
    private BufferedImage imagenFondo;
    
    // Datos del juego
    private Player jugador1;
    private Player jugador2;
    private String[][] tableroJ1;
    private String[][] tableroJ2;
    private boolean turnoJ1 = true;
    private int barcosRestantesJ1;
    private int barcosRestantesJ2;
    
    // Fase del juego
    private static final int FASE_COLOCACION_J1 = 0;
    private static final int FASE_COLOCACION_J2 = 1;
    private static final int FASE_COMBATE = 2;
    private int faseActual = FASE_COLOCACION_J1;
    
    // Variables de colocación
    private int barcosColocadosJ1 = 0;
    private int barcosColocadosJ2 = 0;
    private ArrayList<String> barcosUsadosJ1 = new ArrayList<>();
    private ArrayList<String> barcosUsadosJ2 = new ArrayList<>();
    
    // Componentes GUI
    private JPanel panelTableroJ1;
    private JPanel panelTableroJ2;
    private JButton[][] botonesTableroJ1;
    private JButton[][] botonesTableroJ2;
    private JLabel lblEstado;
    private JLabel lblTurno;
    private JLabel lblBarcosJ1;
    private JLabel lblBarcosJ2;
    private JButton btnRendirse;
    private JButton btnVolverMenu;
    
    private boolean juegoTerminado = false;
    
   
    public PanelJuego(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        
        cargarImagenFondo();
        configurarPanel();
        crearComponentes();
    }
    
    private void cargarImagenFondo() {
        try {
            URL url = getClass().getResource("/imagenes/fondo_juego.jpg");
            if (url != null) {
                imagenFondo = ImageIO.read(url);
            } else {
                System.err.println("No se encontró la imagen en: /imagenes/fondo_juego.jpg");
                imagenFondo = null;
            }
        } catch (IOException e) {
            System.err.println("Error al leer la imagen: " + e.getMessage());
            imagenFondo = null;
        }
    }
    
    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(20, 30, 48));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    
    private void crearComponentes() {
        // Panel superior (información)
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central (tableros)
        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior (botones)
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setOpaque(false);
        
        // Estado del juego
        lblEstado = new JLabel("Preparando partida...", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 20));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(0, 0, 0, 180));
        panel.add(lblEstado);
        
        // Turno actual
        lblTurno = new JLabel("", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTurno.setForeground(Color.YELLOW);
        lblTurno.setOpaque(true);
        lblTurno.setBackground(new Color(0, 0, 0, 180));
        panel.add(lblTurno);
        
        // Contador de barcos
        JPanel panelContadores = new JPanel(new GridLayout(1, 2, 10, 0));
        panelContadores.setOpaque(false);
        
        lblBarcosJ1 = new JLabel("", SwingConstants.CENTER);
        lblBarcosJ1.setFont(new Font("Arial", Font.BOLD, 14));
        lblBarcosJ1.setForeground(Color.CYAN);
        lblBarcosJ1.setOpaque(true);
        lblBarcosJ1.setBackground(new Color(0, 0, 0, 180));
        panelContadores.add(lblBarcosJ1);
        
        lblBarcosJ2 = new JLabel("", SwingConstants.CENTER);
        lblBarcosJ2.setFont(new Font("Arial", Font.BOLD, 14));
        lblBarcosJ2.setForeground(Color.ORANGE);
        lblBarcosJ2.setOpaque(true);
        lblBarcosJ2.setBackground(new Color(0, 0, 0, 180));
        panelContadores.add(lblBarcosJ2);
        
        panel.add(panelContadores);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        
        // Tablero Jugador 1
        JPanel contenedorJ1 = new JPanel(new BorderLayout(5, 5));
        contenedorJ1.setOpaque(false);
        
        JLabel lblJ1 = new JLabel("JUGADOR 1", SwingConstants.CENTER);
        lblJ1.setFont(new Font("Arial", Font.BOLD, 16));
        lblJ1.setForeground(Color.CYAN);
        lblJ1.setOpaque(true);
        lblJ1.setBackground(new Color(0, 0, 0, 180));
        contenedorJ1.add(lblJ1, BorderLayout.NORTH);
        
        panelTableroJ1 = crearTablero(true);
        contenedorJ1.add(panelTableroJ1, BorderLayout.CENTER);
        
        panel.add(contenedorJ1);
        
        // Tablero Jugador 2
        JPanel contenedorJ2 = new JPanel(new BorderLayout(5, 5));
        contenedorJ2.setOpaque(false);
        
        JLabel lblJ2 = new JLabel("JUGADOR 2", SwingConstants.CENTER);
        lblJ2.setFont(new Font("Arial", Font.BOLD, 16));
        lblJ2.setForeground(Color.ORANGE);
        lblJ2.setOpaque(true);
        lblJ2.setBackground(new Color(0, 0, 0, 180));
        contenedorJ2.add(lblJ2, BorderLayout.NORTH);
        
        panelTableroJ2 = crearTablero(false);
        contenedorJ2.add(panelTableroJ2, BorderLayout.CENTER);
        
        panel.add(contenedorJ2);
        
        return panel;
    }
    
    private JPanel crearTablero(boolean esJugador1) {
        JPanel panel = new JPanel(new GridLayout(8, 8, 2, 2));
        panel.setBackground(new Color(0, 50, 100, 200));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        
        JButton[][] botones = new JButton[8][8];
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton boton = new JButton("~");
                boton.setFont(new Font("Arial", Font.BOLD, 16));
                boton.setBackground(new Color(0, 119, 190));
                boton.setForeground(Color.WHITE);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createLineBorder(new Color(0, 90, 150), 1));
                
                final int fila = i;
                final int columna = j;
                
                boton.addActionListener(e -> manejarClicCelda(fila, columna, esJugador1));
                
                botones[i][j] = boton;
                panel.add(boton);
            }
        }
        
        if (esJugador1) {
            botonesTableroJ1 = botones;
        } else {
            botonesTableroJ2 = botones;
        }
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        
        btnRendirse = new JButton("🏳 Rendirse");
        btnRendirse.setFont(new Font("Arial", Font.BOLD, 14));
        btnRendirse.setBackground(new Color(231, 76, 60));
        btnRendirse.setForeground(Color.WHITE);
        btnRendirse.setFocusPainted(false);
        btnRendirse.setPreferredSize(new Dimension(150, 40));
        btnRendirse.addActionListener(e -> manejarRendicion());
        panel.add(btnRendirse);
        
        btnVolverMenu = new JButton("🏠 Volver al Menú");
        btnVolverMenu.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolverMenu.setBackground(new Color(52, 73, 94));
        btnVolverMenu.setForeground(Color.WHITE);
        btnVolverMenu.setFocusPainted(false);
        btnVolverMenu.setPreferredSize(new Dimension(180, 40));
        btnVolverMenu.setEnabled(false);
        btnVolverMenu.addActionListener(e -> volverAlMenu());
        panel.add(btnVolverMenu);
        
        return panel;
    }
   
    public void iniciarPartida() {
        tableroJ1 = Battleship.getTableroJugador1();
        tableroJ2 = Battleship.getTableroJugador2();
        
        jugador1 = Battleship.getUsuarioActual();
        jugador2 = Battleship.getJugador2();
        
        // Resetear variables
        faseActual = FASE_COLOCACION_J1;
        barcosColocadosJ1 = 0;
        barcosColocadosJ2 = 0;
        barcosUsadosJ1.clear();
        barcosUsadosJ2.clear();
        turnoJ1 = true;
        juegoTerminado = false; 
        
        barcosRestantesJ1 = Battleship.getCantidadBarcos();
        barcosRestantesJ2 = Battleship.getCantidadBarcos();
        
        btnVolverMenu.setEnabled(false);
        btnRendirse.setEnabled(true);
        
        actualizarTableros();
        actualizarEstado();
        iniciarFaseColocacion();
    }
    
    private void iniciarFaseColocacion() {
        lblEstado.setText("FASE DE COLOCACIÓN DE BARCOS");
        lblTurno.setText("Jugador 1: Coloque sus barcos");
        
        JOptionPane.showMessageDialog(this,
            "Bienvenido a Battleship Dinámico!\n\n" +
            "Configuración actual:\n" +
            "• Dificultad: " + Battleship.getDificultad() + "\n" +
            "• Barcos por jugador: " + Battleship.getCantidadBarcos() + "\n" +
            "• Modo: " + Battleship.getModoJuego() + "\n\n" +
            "El Jugador 1 colocará sus barcos primero.",
            "Inicio del Juego",
            JOptionPane.INFORMATION_MESSAGE);
        
        mostrarInstruccionesColocacion();
    }
    
    private void mostrarInstruccionesColocacion() {
        String instrucciones = "COLOCAR BARCOS:\n\n" +
            "1. Seleccione el tipo de barco (PA, AZ, SM, DT)\n" +
            "2. Haga clic en una celda del tablero\n" +
            "3. Elija orientación (Horizontal o Vertical)\n\n" +
            "Tipos de barcos:\n" +
            "• PA (Portaaviones): 5 celdas\n" +
            "• AZ (Acorazado): 4 celdas\n" +
            "• SM (Submarino): 3 celdas\n" +
            "• DT (Destructor): 2 celdas\n\n" +
            "Barcos a colocar: " + (Battleship.getCantidadBarcos() - 
                (faseActual == FASE_COLOCACION_J1 ? barcosColocadosJ1 : barcosColocadosJ2));
        
        JOptionPane.showMessageDialog(this,
            instrucciones,
            "Instrucciones",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void manejarClicCelda(int fila, int columna, boolean esJugador1) {
        if (juegoTerminado) {
            return;
        }
        
        if (faseActual == FASE_COLOCACION_J1 && esJugador1) {
            colocarBarcoEnCelda(fila, columna, tableroJ1, barcosUsadosJ1, true);
        } else if (faseActual == FASE_COLOCACION_J2 && !esJugador1) {
            colocarBarcoEnCelda(fila, columna, tableroJ2, barcosUsadosJ2, false);
        } else if (faseActual == FASE_COMBATE) {
            manejarBombardeo(fila, columna, esJugador1);
        }
    }
    
    private void colocarBarcoEnCelda(int fila, int columna, String[][] tablero, 
                                     ArrayList<String> barcosUsados, boolean esJ1) {
        
        String[] tiposBarcos = {"PA (Portaaviones - 5)", "AZ (Acorazado - 4)", 
                                "SM (Submarino - 3)", "DT (Destructor - 2)"};
        
        int seleccion = JOptionPane.showOptionDialog(this,
            "Seleccione el tipo de barco:",
            "Tipo de Barco",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            tiposBarcos,
            tiposBarcos[0]);
        
        if (seleccion == -1) return;
        
        String codigoBarco = seleccion == 0 ? "PA" :
                            seleccion == 1 ? "AZ" :
                            seleccion == 2 ? "SM" : "DT";
        
        if (barcosUsados.contains(codigoBarco)) {
            if (!Battleship.getDificultad().equals("EASY") || !codigoBarco.equals("DT")) {
                JOptionPane.showMessageDialog(this,
                    "Ya ha colocado un barco de este tipo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (codigoBarco.equals("DT")) {
                int contadorDT = 0;
                for (String barco : barcosUsados) {
                    if (barco.equals("DT")) contadorDT++;
                }
                
                if (contadorDT >= 2) {
                    JOptionPane.showMessageDialog(this,
                        "Ya ha colocado el máximo de destructores (2).",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        String[] orientaciones = {"Horizontal", "Vertical"};
        int orientacion = JOptionPane.showOptionDialog(this,
            "Seleccione la orientación:",
            "Orientación",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            orientaciones,
            orientaciones[0]);
        
        if (orientacion == -1) return;
        
        boolean horizontal = (orientacion == 0);
        
        if (Battleship.colocarBarco(tablero, codigoBarco, fila, columna, horizontal)) {
            barcosUsados.add(codigoBarco);
            
            if (esJ1) {
                barcosColocadosJ1++;
            } else {
                barcosColocadosJ2++;
            }
            
            actualizarTableros();
            
            if (esJ1 && barcosColocadosJ1 >= Battleship.getCantidadBarcos()) {
                finalizarColocacionJ1();
            } else if (!esJ1 && barcosColocadosJ2 >= Battleship.getCantidadBarcos()) {
                finalizarColocacionJ2();
            } else {
                int restantes = esJ1 ? 
                    (Battleship.getCantidadBarcos() - barcosColocadosJ1) :
                    (Battleship.getCantidadBarcos() - barcosColocadosJ2);
                    
                lblTurno.setText("Barcos restantes por colocar: " + restantes);
            }
            
        } else {
            JOptionPane.showMessageDialog(this,
                "No se puede colocar el barco en esa posición.\n" +
                "Verifique que:\n" +
                "• El barco quepa en el tablero\n" +
                "• No haya otro barco en esas celdas",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void finalizarColocacionJ1() {
        JOptionPane.showMessageDialog(this,
            "Jugador 1 ha terminado de colocar sus barcos.\n\n" +
            "Ahora es el turno del Jugador 2.",
            "Fase Completada",
            JOptionPane.INFORMATION_MESSAGE);
        
        faseActual = FASE_COLOCACION_J2;
        lblTurno.setText("Jugador 2: Coloque sus barcos");
        mostrarInstruccionesColocacion();
    }
    
    private void finalizarColocacionJ2() {
        JOptionPane.showMessageDialog(this,
            "Ambos jugadores han colocado sus barcos.\n\n" +
            "¡QUE COMIENCE LA BATALLA!",
            "Inicio del Combate",
            JOptionPane.INFORMATION_MESSAGE);
        
        faseActual = FASE_COMBATE;
        turnoJ1 = true;
        lblEstado.setText("¡COMBATE INICIADO!");
        lblTurno.setText("Turno del Jugador 1");
        actualizarContadores();
    }
  
    private void manejarBombardeo(int fila, int columna, boolean clickEnTableroJ1) {
        boolean bombardeaJ1 = turnoJ1 && !clickEnTableroJ1;
        boolean bombardeaJ2 = !turnoJ1 && clickEnTableroJ1;

        if (!bombardeaJ1 && !bombardeaJ2) {
            JOptionPane.showMessageDialog(this,
                "Debe bombardear el tablero del oponente, no el suyo.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[][] tableroObjetivo = turnoJ1 ? tableroJ2 : tableroJ1;
        Battleship.limpiarMarcasFallidas(tableroObjetivo);

        String resultado = Battleship.bombardear(tableroObjetivo, fila, columna);

        procesarResultadoBombardeo(resultado, tableroObjetivo);

        actualizarTableros();
        actualizarContadores();

        if (Battleship.juegoTerminado()) {
            finalizarJuego();
        } else {
            turnoJ1 = !turnoJ1;
            lblTurno.setText("Turno del " + (turnoJ1 ? "Jugador 1" : "Jugador 2"));
        }
    }
    
    private void procesarResultadoBombardeo(String resultado, String[][] tableroObjetivo) {
        if (resultado.equals("AGUA")) {
            JOptionPane.showMessageDialog(this,
                "💦 ¡Agua! El tiro falló.",
                "Fallo",
                JOptionPane.INFORMATION_MESSAGE);
                
        } else if (resultado.equals("YA_BOMBARDEADO")) {
            JOptionPane.showMessageDialog(this,
                "⚠ Esta celda ya fue bombardeada anteriormente.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
                
        } else if (resultado.startsWith("IMPACTO_")) {
            String codigoBarco = resultado.substring(8);
            String nombreBarco = obtenerNombreBarco(codigoBarco);
            
            JOptionPane.showMessageDialog(this,
                "💥 ¡IMPACTO!\n\n" +
                "Se ha bombardeado un " + nombreBarco + "!\n\n" +
                "⚡ El tablero se regenerará...",
                "¡Impacto!",
                JOptionPane.INFORMATION_MESSAGE);
                
        } else if (resultado.startsWith("HUNDIDO_")) {
            String codigoBarco = resultado.substring(8);
            String nombreBarco = obtenerNombreBarco(codigoBarco);
            
            JOptionPane.showMessageDialog(this,
                "🔥 ¡¡¡HUNDIDO!!!\n\n" +
                "Se ha hundido el " + nombreBarco + " del " +
                (turnoJ1 ? "Jugador 2" : "Jugador 1") + "!",
                "¡Barco Hundido!",
                JOptionPane.INFORMATION_MESSAGE);
            
            JOptionPane.showMessageDialog(this,
                "⚡ ¡EL TABLERO SE HA REGENERADO!\n\n" +
                "Los barcos del " + (turnoJ1 ? "Jugador 2" : "Jugador 1") +
                " han sido reposicionados aleatoriamente.",
                "Regeneración",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private String obtenerNombreBarco(String codigo) {
        return codigo.equals("PA") ? "Portaaviones" :
               codigo.equals("AZ") ? "Acorazado" :
               codigo.equals("SM") ? "Submarino" :
               codigo.equals("DT") ? "Destructor" : "Barco";
    }
    
    
    private void actualizarTableros() {
        boolean modoTutorial = Battleship.getModoJuego().equals("TUTORIAL");
        
        // Actualizar tablero J1
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String celda = tableroJ1[i][j];
                JButton boton = botonesTableroJ1[i][j];
                
                if (faseActual == FASE_COMBATE && !turnoJ1 && !modoTutorial) {
                    if (!celda.equals("~") && !celda.equals("F") && !celda.equals("X")) {
                        boton.setText("~");
                        boton.setBackground(new Color(0, 119, 190));
                        boton.setForeground(Color.WHITE);
                    } else {
                        actualizarBotonSegunCelda(boton, celda);
                    }
                } else {
                    actualizarBotonSegunCelda(boton, celda);
                }
            }
        }
        
        // Actualizar tablero J2
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String celda = tableroJ2[i][j];
                JButton boton = botonesTableroJ2[i][j];
                
                if (faseActual == FASE_COMBATE && turnoJ1 && !modoTutorial) {
                    if (!celda.equals("~") && !celda.equals("F") && !celda.equals("X")) {
                        boton.setText("~");
                        boton.setBackground(new Color(0, 119, 190));
                    } else {
                        actualizarBotonSegunCelda(boton, celda);
                    }
                } else {
                    actualizarBotonSegunCelda(boton, celda);
                }
            }
        }
    }
    
    private void actualizarBotonSegunCelda(JButton boton, String celda) {
        // Resetear
        boton.setText("");
        boton.setBackground(new Color(0, 119, 190));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        
        switch (celda) {
            case "~": // Agua
                boton.setText("~");
                boton.setBackground(new Color(0, 119, 190));
                break;
                
            case "F": // Fallo
                boton.setText("F");
                boton.setBackground(new Color(173, 216, 230)); // Azul claro
                boton.setForeground(Color.DARK_GRAY);
                break;
                
            case "X": // Impacto
                boton.setText("X");
                boton.setBackground(new Color(220, 20, 60)); // Rojo crimson
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 20));
                break;
                
            case "PA": // Portaaviones
                boton.setText("PA");
                boton.setBackground(new Color(46, 204, 113)); // Verde
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 14));
                break;
                
            case "AZ": // Acorazado
                boton.setText("AZ");
                boton.setBackground(new Color(155, 89, 182)); // Morado
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 14));
                break;
                
            case "SM": // Submarino
                boton.setText("SM");
                boton.setBackground(new Color(230, 126, 34)); // Naranja
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 14));
                break;
                
            case "DT": // Destructor
                boton.setText("DT");
                boton.setBackground(new Color(241, 196, 15)); // Amarillo
                boton.setForeground(Color.BLACK);
                boton.setFont(new Font("Arial", Font.BOLD, 14));
                break;
                
            default:
                boton.setText(celda);
                break;
        }
    }
    
    private void actualizarContadores() {
        barcosRestantesJ1 = Battleship.contarBarcosRestantes(tableroJ1);
        barcosRestantesJ2 = Battleship.contarBarcosRestantes(tableroJ2);
        
        lblBarcosJ1.setText("Jugador 1: " + barcosRestantesJ1 + " barcos");
        lblBarcosJ2.setText("Jugador 2: " + barcosRestantesJ2 + " barcos");
    }
    
    private void actualizarEstado() {
        switch (faseActual) {
            case FASE_COLOCACION_J1:
                lblEstado.setText("COLOCACIÓN - JUGADOR 1");
                break;
            case FASE_COLOCACION_J2:
                lblEstado.setText("COLOCACIÓN - JUGADOR 2");
                break;
            default:
                lblEstado.setText("¡COMBATE!");
                break;
        }
    }
    
    
    private void finalizarJuego() {
        juegoTerminado = true;
        
        deshabilitarTableros();
        
        String ganador;
        String perdedor;
        Player jugadorGanador;
        Player jugadorPerdedor;
        
        if (barcosRestantesJ1 == 0) {
            ganador = jugador2.getUsername();
            perdedor = jugador1.getUsername();
            jugadorGanador = jugador2;
            jugadorPerdedor = jugador1;
        } else {
            ganador = jugador1.getUsername();
            perdedor = jugador2.getUsername();
            jugadorGanador = jugador1;
            jugadorPerdedor = jugador2;
        }
        
        jugadorGanador.agregarPuntos(3);
        
        String log = ganador + " hundió todos los barcos de " + perdedor + 
                     " en modo " + Battleship.getDificultad() + ".";
        jugadorGanador.agregarLog(log);
        
        JOptionPane.showMessageDialog(this,
            "🏆 ¡JUEGO TERMINADO! 🏆\n\n" +
            "Ganador: " + ganador + "\n\n" +
            ganador + " hundió todos los barcos de " + perdedor + "\n\n" +
            "+3 puntos para " + ganador,
            "¡Victoria!",
            JOptionPane.INFORMATION_MESSAGE);
        
        btnRendirse.setEnabled(false);
        btnVolverMenu.setEnabled(true);
        lblEstado.setText("JUEGO TERMINADO - " + ganador.toUpperCase() + " GANÓ");
    }
    
    private void manejarRendicion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea rendirse?\n\n" +
            "El otro jugador ganará automáticamente.",
            "Confirmar Rendición",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            juegoTerminado = true;
            
            deshabilitarTableros();
            
            String ganador = turnoJ1 ? jugador2.getUsername() : jugador1.getUsername();
            String perdedor = turnoJ1 ? jugador1.getUsername() : jugador2.getUsername();
            Player jugadorGanador = turnoJ1 ? jugador2 : jugador1;
            
            jugadorGanador.agregarPuntos(3);
            
            String log = perdedor + " se retiró del juego dejando como ganador a " + ganador + ".";
            jugadorGanador.agregarLog(log);
            
            JOptionPane.showMessageDialog(this,
                perdedor + " se ha rendido.\n\n" +
                "¡" + ganador + " gana por retiro!",
                "Victoria por Retiro",
                JOptionPane.INFORMATION_MESSAGE);
            
            btnRendirse.setEnabled(false);
            btnVolverMenu.setEnabled(true);
            lblEstado.setText("JUEGO TERMINADO - RENDICIÓN");
        }
    }
    
    private void deshabilitarTableros() {
        // Deshabilitar tablero J1
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botonesTableroJ1[i][j].setEnabled(false);
            }
        }
        
        // Deshabilitar tablero J2
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botonesTableroJ2[i][j].setEnabled(false);
            }
        }
    }
    
    private void volverAlMenu() {
        cardLayout.show(panelContenido, "MENU");
        
        for (Component comp : panelContenido.getComponents()) {
            if (comp instanceof PanelMenuPrincipal) {
                ((PanelMenuPrincipal) comp).actualizarBienvenida();
            }
        }
    }
}