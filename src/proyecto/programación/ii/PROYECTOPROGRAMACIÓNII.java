package proyecto.programación.ii;

import GUI.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class PROYECTOPROGRAMACIÓNII {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}