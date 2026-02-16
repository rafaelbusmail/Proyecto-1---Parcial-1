package logica;

import model.Player;
import java.util.ArrayList;
import java.util.Random;

public class Battleship {
    
    private static ArrayList<Player> jugadores = new ArrayList<>();
    private static Player usuarioActual = null;
    
    private static String dificultad = "NORMAL";
    private static String modoJuego = "TUTORIAL";
    
    private static String[][] tableroJugador1;
    private static String[][] tableroJugador2;
    private static Player jugador1;
    private static Player jugador2;
    private static int barcosRestantesJ1;
    private static int barcosRestantesJ2;
    
    public static boolean registrarJugador(String username, String password) {
        if (buscarJugadorPorUsername(username) != null) {
            return false;
        }
        
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
    
    public static void logout() {
        usuarioActual = null;
    }
    
    public static Player getUsuarioActual() {
        return usuarioActual;
    }
    
    public static Player getJugador2() {
        return jugador2;
    }
    
    public static Player buscarJugadorPorUsername(String username) {
        for (Player jugador : jugadores) {
            if (jugador.getUsername().equalsIgnoreCase(username)) {
                return jugador;
            }
        }
        return null;
    }
    
    public static boolean actualizarUsername(String nuevoUsername) {
        if (usuarioActual == null) return false;
        
        if (buscarJugadorPorUsername(nuevoUsername) != null) {
            return false;
        }
        
        usuarioActual.setUsername(nuevoUsername);
        return true;
    }
    
    public static void actualizarPassword(String nuevoPassword) {
        if (usuarioActual != null) {
            usuarioActual.setPassword(nuevoPassword);
        }
    }
    
    public static boolean eliminarCuenta() {
        if (usuarioActual == null) return false;
        
        jugadores.remove(usuarioActual);
        usuarioActual = null;
        return true;
    }
    
    public static void setDificultad(String dificultad) {
        Battleship.dificultad = dificultad;
    }
    
    public static String getDificultad() {
        return dificultad;
    }
    
    public static int getCantidadBarcos() {
        return dificultad.equals("EASY") ? 5 :
               dificultad.equals("NORMAL") ? 4 :
               dificultad.equals("EXPERT") ? 2 : 1;
    }
    
    public static void setModoJuego(String modo) {
        Battleship.modoJuego = modo;
    }
    
    public static String getModoJuego() {
        return modoJuego;
    }
    
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
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("            RANKING DE JUGADORES\n");
        sb.append("═══════════════════════════════════════════════\n\n");
        
        int posicion = 1;
        for (Player jugador : jugadoresOrdenados) {
            sb.append(posicion).append(". ");
            sb.append(jugador.toString()).append("\n");
            posicion++;
        }
        
        return sb.toString();
    }
    
    public static void inicializarJuego(Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
        
        tableroJugador1 = new String[8][8];
        tableroJugador2 = new String[8][8];
        
        for (String[] fila : tableroJugador1) {
            for (int j = 0; j < fila.length; j++) {
                fila[j] = "~";
            }
        }
        
        for (String[] fila : tableroJugador2) {
            for (int j = 0; j < fila.length; j++) {
                fila[j] = "~";
            }
        }
        
        barcosRestantesJ1 = getCantidadBarcos();
        barcosRestantesJ2 = getCantidadBarcos();
    }
    
    public static String[][] getTableroJugador1() {
        return tableroJugador1;
    }
    
    public static String[][] getTableroJugador2() {
        return tableroJugador2;
    }
    
    public static boolean colocarBarco(String[][] tablero, String codigoBarco, int fila, int columna, boolean horizontal) {
        int tamanio = obtenerTamanioBarco(codigoBarco);
        
        int limiteColumna = horizontal ? columna + tamanio : columna + 1;
        int limiteFila = horizontal ? fila + 1 : fila + tamanio;
        
        if (limiteColumna > 8 || limiteFila > 8) {
            return false;
        }
        
        if (horizontal) {
            for (int i = 0; i < tamanio; i++) {
                if (!tablero[fila][columna + i].equals("~")) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < tamanio; i++) {
                if (!tablero[fila + i][columna].equals("~")) {
                    return false;
                }
            }
        }
        
        if (horizontal) {
            for (int i = 0; i < tamanio; i++) {
                tablero[fila][columna + i] = codigoBarco;
            }
        } else {
            for (int i = 0; i < tamanio; i++) {
                tablero[fila + i][columna] = codigoBarco;
            }
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
                if (celda.equals(codigoBarco)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void regenerarTablero(String[][] tablero) {
        ArrayList<String> barcosEnTablero = new ArrayList<>();
        
        for (String[] fila : tablero) {
            for (String celda : fila) {
                if (!celda.equals("~") && !celda.equals("F") && !celda.equals("X")) {
                    if (!barcosEnTablero.contains(celda)) {
                        barcosEnTablero.add(celda);
                    }
                }
            }
        }
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String celda = tablero[i][j];
                if (!celda.equals("F") && !celda.equals("X")) {
                    tablero[i][j] = "~";
                }
            }
        }
        
        Random random = new Random();
        for (String codigoBarco : barcosEnTablero) {
            boolean colocado = false;
            int intentos = 0;
            
            while (!colocado && intentos < 100) {
                int fila = random.nextInt(8);
                int columna = random.nextInt(8);
                boolean horizontal = random.nextBoolean();
                
                colocado = colocarBarco(tablero, codigoBarco, fila, columna, horizontal);
                intentos++;
            }
        }
    }
    
    public static void limpiarMarcasFallidas(String[][] tablero) {
        for (String[] fila : tablero) {
            for (int j = 0; j < fila.length; j++) {
                if (fila[j].equals("F")) {
                    fila[j] = "~";
                }
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
                    if (!barcosUnicos.contains(celda)) {
                        barcosUnicos.add(celda);
                    }
                }
            }
        }
        
        return barcosUnicos.size();
    }
}