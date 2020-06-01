/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_proyectoredes;

import GUI.windowServer;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Graciela Porras
 */
public class Servidor_ProyectoRedes {

    public static void main(String[] args) {
        EchoMultiServer serverMulti=new EchoMultiServer(6666);
        serverMulti.start();
        
        JFrame jFrame = new JFrame("Hilos con Graficos");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setPreferredSize(new Dimension(400, 500));

        jFrame.add(new windowServer(serverMulti));
    
        jFrame.pack();
        jFrame.setBounds(20, 20, 400, 400);
//        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);

        jFrame.setVisible(true);
    }
    
}
