/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import cliente_proyectoredes.myClient;
import data.Conexion;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Graciela Porras
 */
public class window1Client extends JInternalFrame implements ActionListener, Runnable {

    JButton btnInitSesion;
    JLabel lbUsuario, lbContrasenna;
    JTextField tfUsuario;
    JPasswordField tfContrasenna;
    myClient Client;
    Conexion c;

    public window1Client(myClient Client) {
        super();
        this.Client = Client;
        init();
    }//Constructor

    public void init() {

        this.setVisible(true);
        this.setLayout(null);
        this.setClosable(true);
        this.setBackground(new java.awt.Color(255, 255, 255));

        this.setBounds(500, 100, 30, 10);

        this.lbUsuario = new JLabel("Nombre de Usuario");
        this.lbContrasenna = new JLabel("Contraseña");
        this.btnInitSesion = new JButton("Iniciar Sesión");

        this.tfUsuario = new JTextField();
        this.tfContrasenna = new JPasswordField();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.lbUsuario.setBounds(50, 50, 150, 20);
        this.add(this.lbUsuario);

        this.lbContrasenna.setBounds(50, 100, 150, 20);
        this.add(this.lbContrasenna);

        this.tfUsuario.setBounds(200, 50, 150, 20);
        this.add(this.tfUsuario);

        this.tfContrasenna.setBounds(200, 100, 150, 20);
        this.add(this.tfContrasenna);

        this.btnInitSesion.setBounds(100, 150, 165, 20);
        this.add(this.btnInitSesion);
        this.btnInitSesion.addActionListener(this);
        c = new Conexion();
        c.conectar();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(btnInitSesion)) {
            try {
                if (c.verificarUsuario(tfUsuario.getText(), tfContrasenna.getText()) == true) {
                    tfUsuario.setText("");
                    tfContrasenna.setText("");
                    JFrame jFrame = new JFrame("Iniciar Sesiones");

                    jFrame.setPreferredSize(new Dimension(700, 400));

                    jFrame.add(new window2Client(Client));
                    jFrame.pack();

                    jFrame.setLocationRelativeTo(null);
                    jFrame.setResizable(false);

                    jFrame.setVisible(true);
                    
                }else{
                    tfUsuario.setText("");
                    tfContrasenna.setText("");
                    JOptionPane.showMessageDialog(null, "Los datos ingresados son incorrectos");
                }
            } catch (SQLException ex) {
                Logger.getLogger(window1Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
