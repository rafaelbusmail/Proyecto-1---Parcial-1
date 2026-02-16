/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        setUndecorated(false); // Poner en 'true' si quiero quitar la barra de título de Windows
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        
        // Los paneles
        PanelLogin panelLogin = new PanelLogin(cardLayout, panelContenido);
        PanelRegistro panelRegistro = new PanelRegistro(cardLayout, panelContenido);
        PanelMenuPrincipal panelMenu = new PanelMenuPrincipal(cardLayout, panelContenido);
        PanelJuego panelJuego = new PanelJuego(cardLayout, panelContenido);
        
        // Agregar paneles al CardLayout
        panelContenido.add(panelLogin, "LOGIN");
        panelContenido.add(panelRegistro, "REGISTRO");
        panelContenido.add(panelMenu, "MENU");
        panelContenido.add(panelJuego, "JUEGO");
        
        add(panelContenido);
        
        // Mostrar login primero
        cardLayout.show(panelContenido, "LOGIN");
    }
   
}