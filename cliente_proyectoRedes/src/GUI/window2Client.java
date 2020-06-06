package GUI;

import cliente_proyectoredes.myClient;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.filechooser.FileNameExtensionFilter;

public class window2Client extends JInternalFrame implements ActionListener, Runnable {
    JButton btnAgregaArchivo, btnEnviarArchivo;
    JLabel lbArchivo, lbContrasenna;
    JTextField tfArchivo, tfContrasenna;
     private JFileChooser jfileChooser;

    public window2Client(myClient Client) {
        super();
        
        init();
    }//Constructor

    public void init() {

        this.setVisible(true);
        this.setLayout(null);
        this.setClosable(true);
        this.setBackground(new java.awt.Color(255, 255, 255));

        this.setBounds(500, 100, 30, 10);

        this.lbArchivo = new JLabel("Buscar Archivo");
        this.tfArchivo = new JTextField();
        this.btnAgregaArchivo = new JButton("Buscar");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.lbArchivo.setBounds(50, 50, 150, 20);
        this.add(this.lbArchivo);

        this.tfArchivo.setBounds(150, 50, 150, 20);
        this.add(this.tfArchivo);
      
        this.btnAgregaArchivo.setBounds(320, 50, 80, 20);
        this.add(this.btnAgregaArchivo);
        this.btnAgregaArchivo.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(btnAgregaArchivo)) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    

            int result = fileChooser.showOpenDialog(this);

            if (result != JFileChooser.CANCEL_OPTION) {

                File fileName = fileChooser.getSelectedFile();

                if ((fileName == null) || (fileName.getName().equals(""))) {
                    tfArchivo.setText("...");
                } else {
                    tfArchivo.setText(fileName.getAbsolutePath());
                }
            }

        }

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
