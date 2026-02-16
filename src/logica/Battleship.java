package logica;

import model.Player;
import java.util.ArrayList;
import java.util.Random;

public class Battleship {

    private static final ArrayList<Player> jugadores = new ArrayList<>();
    private static Player usuarioActual = null;

    private static String dificultad = "NORMAL";
    private static String modoJuego  = "TUTORIAL";

    private static String[][] tableroJugador1;
    private static String[][] tableroJugador2;
    private static Player jugador1;
    private static Player jugador2;

    public static boolean registrarJugador(String username, String password) {
        if (buscarJugadorPorUsername(username) != null) return false;
        Player nuevoJugador = new Player(username, password);
        jugadores.add(nuevoJugador);
        usuarioActual = nuevoJugador;
        return true;
    }

    public static boolean login(String username, String password) {
        Player jugador = buscarJugadorPorUsername(username);
        if (jugador != null && jugador.getPassword().equals(password)) {
            usuarioActual = jugador;
            return true;
        }
        return false;
    }

    public static void logout() { usuarioActual = null; }

    public static Player getUsuarioActual() { return usuarioActual; }
    public static Player getJugador2()      { return jugador2; }

    public static Player buscarJugadorPorUsername(String username) {
        for (Player jugador : jugadores) {
            if (jugador.getUsername().equalsIgnoreCase(username)) return jugador;
        }
        return null;
    }
    
    public static Player obtenerJugador(String username){
    return buscarJugadorPorUsername(username);
    }   

    public static boolean actualizarUsername(String nuevoUsername) {
        if (usuarioActual == null) return false;
        Player existente = buscarJugadorPorUsername(nuevoUsername);
        if (existente != null && !existente.getUsername().equalsIgnoreCase(usuarioActual.getUsername())) {
            return false;
        }
        usuarioActual.setUsername(nuevoUsername);
        return true;
    }

    public static void actualizarPassword(String nuevoPassword) {
        if (usuarioActual != null) usuarioActual.setPassword(nuevoPassword);
    }

    public static boolean eliminarCuenta() {
        if (usuarioActual == null) return false;
        jugadores.remove(usuarioActual);
        usuarioActual = null;
        return true;
    }

    public static void setDificultad(String nuevaDificultad) { Battleship.dificultad = nuevaDificultad; }
    public static String getDificultad() { return dificultad; }

    public static int getCantidadBarcos() {
        switch (dificultad) {
            case "EASY":   return 5;
            case "NORMAL": return 4;
            case "EXPERT": return 2;
            case "GENIUS": return 1;
            default:       return 4;
        }
    }

    public static void setModoJuego(String modo) { Battleship.modoJuego = modo; }
    public static String getModoJuego() { return modoJuego; }

    public static String obtenerRankingJugadores() {
        ArrayList<Player> jugadoresOrdenados = new ArrayList<>(jugadores);

        for (int i = 0; i < jugadoresOrdenados.size() - 1; i++) {
            for (int j = 0; j < jugadoresOrdenados.size() - i - 1; j++) {
                if (jugadoresOrdenados.get(j).getPuntos() < jugadoresOrdenados.get(j + 1).getPuntos()) {
                    Player temp = jugadoresOrdenados.get(j);
                    jugadoresOrdenados.set(j, jugadoresOrdenados.get(j + 1));
                    jugadoresOrdenados.set(j + 1, temp);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("               RANKING DE JUGADORES\n");
        sb.append("═══════════════════════════════════════════════════════\n\n");

        int posicion = 1;
        for (Player jugador : jugadoresOrdenados) {
            sb.append(posicion).append(". ").append(jugador.toString()).append("\n");
            posicion++;
        }

        if (jugadoresOrdenados.isEmpty()) sb.append("   No hay jugadores registrados aún.\n");

        return sb.toString();
    }

    public static void inicializarJuego(Player j1, Player j2) {

        jugador1 = j1;
        jugador2 = j2;

        if (!jugadores.contains(j1)) jugadores.add(j1);
        if (!jugadores.contains(j2)) jugadores.add(j2);

        tableroJugador1 = new String[8][8];
        tableroJugador2 = new String[8][8];

        for (String[] fila : tableroJugador1)
            for (int j = 0; j < fila.length; j++)
                fila[j] = "~";

        for (String[] fila : tableroJugador2)
            for (int j = 0; j < fila.length; j++)
                fila[j] = "~";
    }



    public static String[][] getTableroJugador1() { return tableroJugador1; }
    public static String[][] getTableroJugador2() { return tableroJugador2; }

    public static boolean colocarBarco(String[][] tablero, String codigoBarco,
                                       int fila, int columna, boolean horizontal) {
        int tamanio = obtenerTamanioBarco(codigoBarco);
        int filas   = tablero.length;
        int cols    = tablero[0].length;

        if (horizontal) {
            if (columna + tamanio > cols || fila >= filas) return false;
        } else {
            if (fila + tamanio > filas || columna >= cols) return false;
        }

        if (horizontal) {
            for (int i = 0; i < tamanio; i++) {
                if (!tablero[fila][columna + i].equals("~")) return false;
            }
        } else {
            for (int i = 0; i < tamanio; i++) {
                if (!tablero[fila + i][columna].equals("~")) return false;
            }
        }

        if (horizontal) {
            for (int i = 0; i < tamanio; i++) tablero[fila][columna + i] = codigoBarco;
        } else {
            for (int i = 0; i < tamanio; i++) tablero[fila + i][columna] = codigoBarco;
        }

        return true;
    }

    public static int obtenerTamanioBarco(String codigo) {
        return codigo.equals("PA") ? 5 :
               codigo.equals("AZ") ? 4 :
               codigo.equals("SM") ? 3 :
               codigo.equals("DT") ? 2 : 0;
    }

    public static String bombardear(String[][] tablero, int fila, int columna) {
        String celda = tablero[fila][columna];

        if (celda.equals("~")) {
            tablero[fila][columna] = "F";
            return "AGUA";
        } else if (celda.equals("F") || celda.equals("X")) {
            return "YA_BOMBARDEADO";
        } else {
            String codigoBarco = celda;
            tablero[fila][columna] = "X";

            boolean hundido = barcoHundido(tablero, codigoBarco);

            if (hundido) {
                regenerarTablero(tablero);
                return "HUNDIDO_" + codigoBarco;
            } else {
                return "IMPACTO_" + codigoBarco;
            }
        }
    }

    public static boolean barcoHundido(String[][] tablero, String codigoBarco) {
        for (String[] fila : tablero) {
            for (String celda : fila) {
                if (celda.equals(codigoBarco)) return false;
            }
        }
        return true;
    }

    public static void regenerarTablero(String[][] tablero) {
        ArrayList<String> barcosVivos = new ArrayList<>();
        for (String[] fila : tablero) {
            for (String celda : fila) {
                if (!celda.equals("~") && !celda.equals("F") && !celda.equals("X")) {
                    if (!barcosVivos.contains(celda)) barcosVivos.add(celda);
                }
            }
        }

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                String celda = tablero[i][j];
                if (!celda.equals("F") && !celda.equals("X")) tablero[i][j] = "~";
            }
        }

        Random random = new Random();
        for (String codigoBarco : barcosVivos) {
            boolean colocado = false;
            int intentos = 0;
            while (!colocado && intentos < 200) {
                int fila    = random.nextInt(tablero.length);
                int columna = random.nextInt(tablero[0].length);
                boolean horiz = random.nextBoolean();
                colocado = colocarBarco(tablero, codigoBarco, fila, columna, horiz);
                intentos++;
            }
        }
    }

    public static boolean juegoTerminado() {
        return contarBarcosRestantes(tableroJugador1) == 0 ||
               contarBarcosRestantes(tableroJugador2) == 0;
    }

    public static int contarBarcosRestantes(String[][] tablero) {
        ArrayList<String> barcosUnicos = new ArrayList<>();
        for (String[] fila : tablero) {
            for (String celda : fila) {
                if (!celda.equals("~") && !celda.equals("F") && !celda.equals("X")) {
                    if (!barcosUnicos.contains(celda)) barcosUnicos.add(celda);
                }
            }
        }
        return barcosUnicos.size();
    }
}