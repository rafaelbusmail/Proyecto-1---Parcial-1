
package logica;

import model.Player;
import java.util.ArrayList;
import java.util.Random;

public class Battleship {
    
    // ═══════════════════════════════════════════════════════════
    //                    GESTIÓN DE JUGADORES
    // ═══════════════════════════════════════════════════════════
    
    private static ArrayList<Player> jugadores = new ArrayList<>();
    private static Player usuarioActual = null;
    
    // Configuración
    private static String dificultad = "NORMAL"; 
    private static String modoJuego = "TUTORIAL"; 
    
    // Variables del juego actual
    private static String[][] tableroJugador1;
    private static String[][] tableroJugador2;
    private static Player jugador1;
    private static Player jugador2;
    private static int barcosRestantesJ1;
    private static int barcosRestantesJ2;
    
    // ═══════════════════════════════════════════════════════════
    //                    AUTENTICACIÓN DE USUARIOS
    // ═══════════════════════════════════════════════════════════
    
    public static boolean registrarJugador(String username, String password) {
        // Validar que el username sea único
        if (buscarJugadorPorUsername(username) != null) {
            return false; // Username ya existe
        }
        
        // Crear nuevo jugador
        Player nuevoJugador = new Player(username, password);
        jugadores.add(nuevoJugador);
        usuarioActual = nuevoJugador; // Auto-login después del registro
        
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
    
    // ═══════════════════════════════════════════════════════════
    //                    PERFIL DE JUGADOR
    // ═══════════════════════════════════════════════════════════
    
    public static boolean actualizarUsername(String nuevoUsername) {
        if (usuarioActual == null) return false;
        
        // Verificar que el nuevo username sea único
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
    
    // ═══════════════════════════════════════════════════════════
    //                    CONFIGURACIÓN
    // ═══════════════════════════════════════════════════════════
    
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
    
    // ═══════════════════════════════════════════════════════════
    //                    REPORTES
    // ═══════════════════════════════════════════════════════════
    
    public static String obtenerRankingJugadores() {
        // Ordenar jugadores por puntos (descendente)
        ArrayList<Player> jugadoresOrdenados = new ArrayList<>(jugadores);
        
        // Bubble sort 
        for (int i = 0; i < jugadoresOrdenados.size() - 1; i++) {
            for (int j = 0; j < jugadoresOrdenados.size() - i - 1; j++) {
                if (jugadoresOrdenados.get(j).getPuntos() < jugadoresOrdenados.get(j + 1).getPuntos()) {
                    Player temp = jugadoresOrdenados.get(j);
                    jugadoresOrdenados.set(j, jugadoresOrdenados.get(j + 1));
                    jugadoresOrdenados.set(j + 1, temp);
                }
            }
        }
        
        // Construir string de ranking
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
    
    // ═══════════════════════════════════════════════════════════
    //                    LÓGICA DEL JUEGO
    // ═══════════════════════════════════════════════════════════
    
    public static void inicializarJuego(Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
        
        tableroJugador1 = new String[8][8];
        tableroJugador2 = new String[8][8];
        
      //tableros con agua (~)
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
        
        // Validar que el barco cabe en el tablero
        if (limiteColumna > 8 || limiteFila > 8) {
            return false;
        }
        
        // Validar que no hays otro barco en esas posiciones
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
        
        // Colocar el barco
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
    
    //Regenerar después de CADA impacto
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
            
            // "luego de que un barco es bombardeado, el tablero se REGENERA"
            regenerarTablero(tablero);
            
            return hundido ? ("HUNDIDO_" + codigoBarco) : ("IMPACTO_" + codigoBarco);
        }
    }
    
    public static boolean barcoHundido(String[][] tablero, String codigoBarco) {
        for (String[] fila : tablero) {
            for (String celda : fila) {
                if (celda.equals(codigoBarco)) {
                    return false; // partes sin hundir
                }
            }
        }
        return true; // Todo el barco está hundido
    }
    
    public static void regenerarTablero(String[][] tablero) {
        // Guarda los barcos actuales con sus códigos
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
        
        // Limpiar tablero
        for (String[] fila : tablero) {
            for (int j = 0; j < fila.length; j++) {
                fila[j] = "~";
            }
        }
        
        // Recoloca barcos aleatoriamente 
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
    
    //Limpiar marcas de fallo 
    public static void limpiarMarcasFallidas(String[][] tablero) {
        // ✅ USO DE FOREACH
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
    
    // Método de prueba (eliminar en producción)
    public static void crearJugadoresPrueba() {
        registrarJugador("admin", "1234");
        logout();
        registrarJugador("carlos", "pass");
        logout();
        registrarJugador("maria", "pass");
        logout();
        
        //puntos de prueba
        jugadores.get(0).setPuntos(15);
        jugadores.get(1).setPuntos(9);
        jugadores.get(2).setPuntos(6);
        
        //logs de prueba
        jugadores.get(0).agregarLog("admin hundió todos los barcos de carlos en modo NORMAL.");
        jugadores.get(0).agregarLog("admin hundió todos los barcos de maria en modo EASY.");
    }
}