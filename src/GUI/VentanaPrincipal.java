package GUI;

import javax.swing.*;
import java.awt.*;
import GUI.PanelJuego;

public class VentanaPrincipal extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelContenido;
    
    public VentanaPrincipal() {
        configurarVentana();
        inicializarComponentes();
    }
    
    private void configurarVentana() {
        setTitle("Battleship Dinámico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        
        PanelLogin panelLogin = new PanelLogin(cardLayout, panelContenido);
        PanelRegistro panelRegistro = new PanelRegistro(cardLayout, panelContenido);
        PanelMenuPrincipal panelMenu = new PanelMenuPrincipal(cardLayout, panelContenido);
        PanelJuego panelJuego = new PanelJuego(cardLayout, panelContenido);
        
        panelContenido.add(panelLogin, "LOGIN");
        panelContenido.add(panelRegistro, "REGISTRO");
        panelContenido.add(panelMenu, "MENU");
        panelContenido.add(panelJuego, "JUEGO");
        
        add(panelContenido);
        
        cardLayout.show(panelContenido, "LOGIN");
    }
}