/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente_proyectoredes;

import GUI.window1Client;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;

/**
 *
 * @author Graciela Porras
 */
public class Cliente_proyectoRedes {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {

        myClient client = new myClient(6666);
        client.start();

        JFrame jFrame = new JFrame("Cliente FTP");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setPreferredSize(new Dimension(400, 500));

        jFrame.add(new window1Client(client));

        jFrame.pack();
        jFrame.setBounds(20, 20, 400, 400);
//        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);

        jFrame.setVisible(true);
    }

}

