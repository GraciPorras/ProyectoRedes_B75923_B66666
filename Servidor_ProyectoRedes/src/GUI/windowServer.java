
package GUI;

import Data.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import servidor_proyectoredes.EchoMultiServer;

/**
 *
 * @author Graciela Porras
 */
public class windowServer extends JInternalFrame implements ActionListener, Runnable {

    JButton btnRegistrar;
    JLabel lbUsuario, lbContrasenna;
    JTextField tfUsuario, tfContrasenna;

    public windowServer(EchoMultiServer myServer) {
        super();
        
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
        this.btnRegistrar = new JButton("Registrar");

        this.tfUsuario = new JTextField();
        this.tfContrasenna = new JTextField();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.lbUsuario.setBounds(50, 50, 150, 20);
        this.add(this.lbUsuario);

        this.lbContrasenna.setBounds(50, 100, 150, 20);
        this.add(this.lbContrasenna);

        this.tfUsuario.setBounds(200, 50, 150, 20);
        this.add(this.tfUsuario);

        this.tfContrasenna.setBounds(200, 100, 150, 20);
        this.add(this.tfContrasenna);

        this.btnRegistrar.setBounds(100, 150, 165, 20);
        this.add(this.btnRegistrar);
        this.btnRegistrar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(btnRegistrar)) {
            String ruta;
            //ruta ="C:/Users/Graciela Porras/Desktop/U/"+this.tfUsuario.getText(); // rutaGraciPorras
            ruta = "C:/Users/ronal/Desktop/VII Semestre 2020/Redes y Comunicaciones de datos/Proyecto/Usuarios/"+this.tfUsuario.getText();//Ruta Fronital
            File directorio = new File(ruta);
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    System.out.println("Directorio creado");
                    Conexion mysql= new Conexion();
                    try {
                        mysql.conectar();
                        mysql.insertarUsuario(this.tfUsuario.getText(), this.tfContrasenna.getText(), ruta);
                        mysql.selectUsuario();
                        this.tfUsuario.setText("");
                        this.tfContrasenna.setText("");
                    } catch (SQLException ex) {
                        Logger.getLogger(windowServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Error al crear directorio");
                }
            }
        }

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
