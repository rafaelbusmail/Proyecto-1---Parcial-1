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

public class PanelJuego extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel panelContenido;

    private BufferedImage imagenFondo;

    private Player jugador1;
    private Player jugador2;
    private String[][] tableroJ1;
    private String[][] tableroJ2;
    private boolean turnoJ1 = true;
    private int barcosRestantesJ1;
    private int barcosRestantesJ2;

    private static final int FASE_COLOCACION_J1 = 0;
    private static final int FASE_COLOCACION_J2 = 1;
    private static final int FASE_COMBATE       = 2;
    private int faseActual = FASE_COLOCACION_J1;

    private int barcosColocadosJ1 = 0;
    private int barcosColocadosJ2 = 0;
    private ArrayList<String> barcosUsadosJ1 = new ArrayList<>();
    private ArrayList<String> barcosUsadosJ2 = new ArrayList<>();

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
        this.cardLayout     = cardLayout;
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
                imagenFondo = null;
            }
        } catch (IOException e) {
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
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(),  BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setOpaque(false);

        lblEstado = new JLabel("", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 20));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(0, 0, 0, 180));
        panel.add(lblEstado);

        lblTurno = new JLabel("", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTurno.setForeground(Color.YELLOW);
        lblTurno.setOpaque(true);
        lblTurno.setBackground(new Color(0, 0, 0, 180));
        panel.add(lblTurno);

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
        JPanel panelContenedor = new JPanel(new BorderLayout(5, 5));
        panelContenedor.setOpaque(false);

        JPanel panelConCoordenadas = new JPanel(new BorderLayout(0, 0));
        panelConCoordenadas.setOpaque(false);

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 0));
        panelSuperior.setOpaque(false);
        JLabel esquina = new JLabel("");
        esquina.setPreferredSize(new Dimension(30, 30));
        esquina.setOpaque(false);
        panelSuperior.add(esquina, BorderLayout.WEST);

        JPanel panelColumnas = new JPanel(new GridLayout(1, 8, 2, 2));
        panelColumnas.setOpaque(false);
        String[] columnas = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String col : columnas) {
            JLabel lbl = new JLabel(col, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0, 0, 0, 150));
            lbl.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
            panelColumnas.add(lbl);
        }
        panelSuperior.add(panelColumnas, BorderLayout.CENTER);
        panelConCoordenadas.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(0, 0));
        panelCentral.setOpaque(false);

        JPanel panelFilas = new JPanel(new GridLayout(8, 1, 2, 2));
        panelFilas.setOpaque(false);
        for (int i = 1; i <= 8; i++) {
            JLabel lbl = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0, 0, 0, 150));
            lbl.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
            lbl.setPreferredSize(new Dimension(30, 0));
            panelFilas.add(lbl);
        }
        panelCentral.add(panelFilas, BorderLayout.WEST);

        JPanel panelTablero = new JPanel(new GridLayout(8, 8, 2, 2));
        panelTablero.setBackground(new Color(0, 50, 100, 200));
        panelTablero.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

        JButton[][] botones = new JButton[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton boton = new JButton("~");
                boton.setFont(new Font("Arial", Font.BOLD, 16));
                boton.setBackground(new Color(0, 119, 190));
                boton.setForeground(Color.WHITE);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createLineBorder(new Color(0, 90, 150), 1));

                final int fila    = i;
                final int columna = j;
                boton.addActionListener(e -> manejarClicCelda(fila, columna, esJugador1));

                botones[i][j] = boton;
                panelTablero.add(boton);
            }
        }
        panelCentral.add(panelTablero, BorderLayout.CENTER);
        panelConCoordenadas.add(panelCentral, BorderLayout.CENTER);
        panelContenedor.add(panelConCoordenadas, BorderLayout.CENTER);

        if (esJugador1) {
            botonesTableroJ1 = botones;
        } else {
            botonesTableroJ2 = botones;
        }

        return panelContenedor;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        btnRendirse = new JButton("Rendirse");
        btnRendirse.setFont(new Font("Arial", Font.BOLD, 14));
        btnRendirse.setBackground(new Color(231, 76, 60));
        btnRendirse.setForeground(Color.WHITE);
        btnRendirse.setFocusPainted(false);
        btnRendirse.setPreferredSize(new Dimension(150, 40));
        btnRendirse.addActionListener(e -> manejarRendicion());
        panel.add(btnRendirse);

        btnVolverMenu = new JButton("Volver al Menu");
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

        faseActual        = FASE_COLOCACION_J1;
        barcosColocadosJ1 = 0;
        barcosColocadosJ2 = 0;
        barcosUsadosJ1.clear();
        barcosUsadosJ2.clear();
        turnoJ1        = true;
        juegoTerminado = false;

        barcosRestantesJ1 = Battleship.getCantidadBarcos();
        barcosRestantesJ2 = Battleship.getCantidadBarcos();

        btnVolverMenu.setEnabled(false);
        btnRendirse.setEnabled(true);

        habilitarTableros();
        actualizarTableros();
        actualizarEstado();

        JOptionPane.showMessageDialog(this,
            "El Jugador 1 (" + jugador1.getUsername() + ") colocará sus barcos primero.\n\n" +
            "Haga clic en una celda de SU tablero para colocar cada barco.",
            "Inicio del Juego",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void manejarClicCelda(int fila, int columna, boolean esJugador1) {
        if (juegoTerminado) return;

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

        String[] tiposBarcos = {
            "PA - Portaaviones (5 celdas)",
            "AZ - Acorazado    (4 celdas)",
            "SM - Submarino    (3 celdas)",
            "DT - Destructor   (2 celdas)"
        };

        int seleccion = JOptionPane.showOptionDialog(this,
            "Seleccione el tipo de barco:",
            "Tipo de Barco",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, tiposBarcos, tiposBarcos[0]);

        if (seleccion == JOptionPane.CLOSED_OPTION) return;

        String codigoBarco = seleccion == 0 ? "PA" :
                             seleccion == 1 ? "AZ" :
                             seleccion == 2 ? "SM" : "DT";

        if (barcosUsados.contains(codigoBarco)) {
            boolean esEasy = Battleship.getDificultad().equals("EASY");

            if (esEasy && codigoBarco.equals("DT")) {
                int contadorDT = 0;
                for (String b : barcosUsados) {
                    if (b.equals("DT")) contadorDT++;
                }
                if (contadorDT >= 2) {
                    JOptionPane.showMessageDialog(this,
                        "Ya colocó el máximo de Destructores permitidos (2).",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Ya colocó un barco de este tipo.\n" +
                    (esEasy ? "Solo puede repetir el Destructor (DT)." : ""),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String[] orientaciones = {"Horizontal", "Vertical"};
        int orientacion = JOptionPane.showOptionDialog(this,
            "Seleccione la orientación del barco:",
            "Orientación",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, orientaciones, orientaciones[0]);

        if (orientacion == JOptionPane.CLOSED_OPTION) return;

        boolean horizontal = (orientacion == 0);

        if (Battleship.colocarBarco(tablero, codigoBarco, fila, columna, horizontal)) {
            barcosUsados.add(codigoBarco);
            if (esJ1) barcosColocadosJ1++; else barcosColocadosJ2++;

            actualizarTableros();

            if (esJ1 && barcosColocadosJ1 >= Battleship.getCantidadBarcos()) {
                finalizarColocacionJ1();
            } else if (!esJ1 && barcosColocadosJ2 >= Battleship.getCantidadBarcos()) {
                finalizarColocacionJ2();
            } else {
                int restantes = esJ1
                    ? Battleship.getCantidadBarcos() - barcosColocadosJ1
                    : Battleship.getCantidadBarcos() - barcosColocadosJ2;
                lblTurno.setText("Barcos restantes por colocar: " + restantes);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "No se puede colocar el barco en esa posición.\n" +
                "Verifique que el barco quepa en el tablero y no solape otro.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarColocacionJ1() {
        faseActual = FASE_COLOCACION_J2;
        actualizarEstado();
        JOptionPane.showMessageDialog(this,
            "Jugador 1 terminó de colocar sus barcos.\n\n" +
            "Ahora el Jugador 2 (" + jugador2.getUsername() + ") colocará los suyos.\n" +
            "Haga clic en su tablero (derecho) para colocar cada barco.",
            "Turno del Jugador 2",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void finalizarColocacionJ2() {
        faseActual = FASE_COMBATE;
        turnoJ1 = true;
        actualizarContadores();
        actualizarEstado();
        JOptionPane.showMessageDialog(this,
            "Ambos jugadores han colocado sus barcos.\n\n" +
            "¡Que comience la batalla!\nTurno del Jugador 1: " + jugador1.getUsername(),
            "¡Combate!",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void manejarBombardeo(int fila, int columna, boolean clickEnTableroJ1) {
        boolean bombardeaJ1 = turnoJ1  && !clickEnTableroJ1;
        boolean bombardeaJ2 = !turnoJ1 &&  clickEnTableroJ1;

        if (!bombardeaJ1 && !bombardeaJ2) {
            JOptionPane.showMessageDialog(this,
                "Debes bombardear el tablero del oponente, no el tuyo.",
                "Acción inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[][] tableroObjetivo = turnoJ1 ? tableroJ2 : tableroJ1;

        String resultado = Battleship.bombardear(tableroObjetivo, fila, columna);

        actualizarTableros();
        actualizarContadores();
        procesarResultadoBombardeo(resultado);

        if (resultado.equals("YA_BOMBARDEADO")) return;

        if (Battleship.juegoTerminado()) {
            finalizarJuego();
        } else {
            turnoJ1 = !turnoJ1;
            actualizarEstado();
        }
    }

    private void procesarResultadoBombardeo(String resultado) {
        switch (resultado) {
            case "AGUA":
                JOptionPane.showMessageDialog(this,
                    "¡Agua! El tiro cayó al mar.",
                    "Fallo", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "YA_BOMBARDEADO":
                JOptionPane.showMessageDialog(this,
                    "Esa celda ya fue bombardeada. Elige otra.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                break;
            default:
                if (resultado.startsWith("IMPACTO_")) {
                    String nombre = obtenerNombreBarco(resultado.substring(8));
                    JOptionPane.showMessageDialog(this,
                        "¡Impacto en el " + nombre + "!",
                        "¡Impacto!", JOptionPane.INFORMATION_MESSAGE);
                } else if (resultado.startsWith("HUNDIDO_")) {
                    String nombre = obtenerNombreBarco(resultado.substring(8));
                    JOptionPane.showMessageDialog(this,
                        "¡" + nombre + " HUNDIDO!\n\nEl tablero del oponente se regeneró.",
                        "¡Barco Hundido!", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
        }
    }

    private String obtenerNombreBarco(String codigo) {
        return codigo.equals("PA") ? "Portaaviones" :
               codigo.equals("AZ") ? "Acorazado"   :
               codigo.equals("SM") ? "Submarino"    :
               codigo.equals("DT") ? "Destructor"   : "Barco desconocido";
    }

    private void finalizarJuego() {
        juegoTerminado = true;
        deshabilitarTableros();

        String ganador, perdedor;
        Player jugadorGanador, jugadorPerdedor;

        if (barcosRestantesJ1 == 0) {
            ganador         = jugador2.getUsername();
            perdedor        = jugador1.getUsername();
            jugadorGanador  = jugador2;
            jugadorPerdedor = jugador1;
        } else {
            ganador         = jugador1.getUsername();
            perdedor        = jugador2.getUsername();
            jugadorGanador  = jugador1;
            jugadorPerdedor = jugador2;
        }

        jugadorGanador.agregarPuntos(3);

        String logG = ganador + " hundió todos los barcos de " + perdedor +
                      " en modo " + Battleship.getDificultad() + ". +3pts";
        String logP = perdedor + " fue derrotado por " + ganador +
                      " en modo " + Battleship.getDificultad() + ". 0pts";
        jugadorGanador.agregarLog(logG);
        jugadorPerdedor.agregarLog(logP);

        JOptionPane.showMessageDialog(this,
            "¡JUEGO TERMINADO!\n\n" +
            "Ganador: " + ganador + "\n" +
            ganador + " hundió todos los barcos de " + perdedor + "\n\n" +
            "+3 puntos para " + ganador,
            "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);

        btnRendirse.setEnabled(false);
        btnVolverMenu.setEnabled(true);
        lblEstado.setText("JUEGO TERMINADO - " + ganador.toUpperCase() + " GANÓ");
    }

    private void manejarRendicion() {
        int conf = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea rendirse?\nEl otro jugador ganará automáticamente.",
            "Confirmar Rendición", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf == JOptionPane.YES_OPTION) {
            juegoTerminado = true;
            deshabilitarTableros();

            String ganador   = turnoJ1 ? jugador2.getUsername() : jugador1.getUsername();
            String perdedor  = turnoJ1 ? jugador1.getUsername() : jugador2.getUsername();
            Player jGanador  = turnoJ1 ? jugador2 : jugador1;
            Player jPerdedor = turnoJ1 ? jugador1 : jugador2;

            jGanador.agregarPuntos(3);

            String logG = ganador  + " ganó por retiro de " + perdedor +
                          " en modo " + Battleship.getDificultad() + ". +3pts";
            String logP = perdedor + " se retiró del juego dejando como ganador a " + ganador +
                          " en modo " + Battleship.getDificultad() + ". 0pts";
            jGanador.agregarLog(logG);
            jPerdedor.agregarLog(logP);

            JOptionPane.showMessageDialog(this,
                perdedor + " se ha rendido.\n¡" + ganador + " gana por retiro!",
                "Victoria por Retiro", JOptionPane.INFORMATION_MESSAGE);

            btnRendirse.setEnabled(false);
            btnVolverMenu.setEnabled(true);
            lblEstado.setText("JUEGO TERMINADO - RENDICIÓN DE " + perdedor.toUpperCase());
        }
    }

    private void actualizarTableros() {
        boolean modoTutorial = Battleship.getModoJuego().equals("TUTORIAL");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String celda = tableroJ1[i][j];
                JButton boton = botonesTableroJ1[i][j];
                
                boolean esBarco = !celda.equals("~") && !celda.equals("F") && !celda.equals("X");

                if (faseActual == FASE_COMBATE && esBarco) {
                    if (turnoJ1 || modoTutorial) {
                        actualizarBotonSegunCelda(boton, celda);
                    } else {
                        actualizarBotonSegunCelda(boton, "~");
                    }
                } else {
                    actualizarBotonSegunCelda(boton, celda);
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String celda = tableroJ2[i][j];
                JButton boton = botonesTableroJ2[i][j];
                boolean esBarco = !celda.equals("~") && !celda.equals("F") && !celda.equals("X");

                if (faseActual == FASE_COMBATE && esBarco) {
                    if (!turnoJ1 || modoTutorial) {
                        actualizarBotonSegunCelda(boton, celda);
                    } else {
                        actualizarBotonSegunCelda(boton, "~");
                    }
                } else {
                    actualizarBotonSegunCelda(boton, celda);
                }
            }
        }
    }

    private void actualizarBotonSegunCelda(JButton boton, String celda) {
        boton.setFont(new Font("Arial", Font.BOLD, 16));

        switch (celda) {
            case "~":
                boton.setText("~");
                boton.setBackground(new Color(0, 119, 190));
                boton.setForeground(Color.WHITE);
                break;
            case "F":
                boton.setText("F");
                boton.setBackground(new Color(135, 206, 250));
                boton.setForeground(new Color(70, 130, 180));
                break;
            case "X":
                boton.setText("X");
                boton.setBackground(new Color(178, 34, 34));
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 22));
                break;
            case "PA":
                boton.setText("PA");
                boton.setBackground(new Color(25, 25, 112));
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 13));
                break;
            case "AZ":
                boton.setText("AZ");
                boton.setBackground(new Color(105, 105, 105));
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 13));
                break;
            case "SM":
                boton.setText("SM");
                boton.setBackground(new Color(85, 107, 47));
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 13));
                break;
            case "DT":
                boton.setText("DT");
                boton.setBackground(new Color(139, 69, 19));
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Arial", Font.BOLD, 13));
                break;
            default:
                boton.setText(celda);
                boton.setBackground(new Color(0, 119, 190));
                break;
        }
    }

    private void actualizarContadores() {
        barcosRestantesJ1 = Battleship.contarBarcosRestantes(tableroJ1);
        barcosRestantesJ2 = Battleship.contarBarcosRestantes(tableroJ2);
        lblBarcosJ1.setText(jugador1.getUsername() + ": " + barcosRestantesJ1 + " barcos");
        lblBarcosJ2.setText(jugador2.getUsername() + ": " + barcosRestantesJ2 + " barcos");
    }

    private void actualizarEstado() {
        switch (faseActual) {
            case FASE_COLOCACION_J1:
                lblEstado.setText("COLOCACIÓN - JUGADOR 1: " + jugador1.getUsername());
                lblTurno.setText("Haz clic en TU tablero (izquierdo) para colocar un barco.");
                lblBarcosJ1.setText("");
                lblBarcosJ2.setText("");
                break;
            case FASE_COLOCACION_J2:
                lblEstado.setText("COLOCACIÓN - JUGADOR 2: " + jugador2.getUsername());
                lblTurno.setText("Haz clic en TU tablero (derecho) para colocar un barco.");
                lblBarcosJ1.setText("");
                lblBarcosJ2.setText("");
                break;
            case FASE_COMBATE:
                lblEstado.setText("¡COMBATE!");
                String nombreTurno = turnoJ1 ? jugador1.getUsername() : jugador2.getUsername();
                lblTurno.setText("Turno de: " + nombreTurno + " — ataca el tablero del oponente.");
                actualizarContadores();
                break;
            default:
                lblEstado.setText("Estado desconocido");
                break;
        }
    }

    private void deshabilitarTableros() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botonesTableroJ1[i][j].setEnabled(false);
                botonesTableroJ2[i][j].setEnabled(false);
            }
        }
    }

    private void habilitarTableros() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botonesTableroJ1[i][j].setEnabled(true);
                botonesTableroJ2[i][j].setEnabled(true);
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