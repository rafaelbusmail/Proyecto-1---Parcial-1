package model;

public class Player {
    
    private String username;
    private String password;
    private int puntos;
    private String[] logsPartidas;
    private int contadorLogs;
    
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.logsPartidas = new String[10];
        this.contadorLogs = 0;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getPuntos() {
        return puntos;
    }
    
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    
    public String[] getLogsPartidas() {
        return logsPartidas;
    }
    
    public void agregarPuntos(int cantidad) {
        this.puntos += cantidad;
    }
    
    public void agregarLog(String log) {
        if (contadorLogs >= 10) {
            for (int i = 9; i > 0; i--) {
                logsPartidas[i] = logsPartidas[i - 1];
            }
            logsPartidas[0] = log;
        } else {
            for (int i = contadorLogs; i > 0; i--) {
                logsPartidas[i] = logsPartidas[i - 1];
            }
            logsPartidas[0] = log;
            contadorLogs++;
        }
    }
    
    public String obtenerUltimos10Juegos() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("    ÚLTIMOS 10 JUEGOS DE ").append(username).append("\n");
        sb.append("═══════════════════════════════════════════════\n\n");
        
        for (int i = 0; i < 10; i++) {
            sb.append((i + 1)).append("- ");
            if (logsPartidas[i] != null) {
                sb.append(logsPartidas[i]);
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Username: " + username + " | Puntos: " + puntos;
    }
}