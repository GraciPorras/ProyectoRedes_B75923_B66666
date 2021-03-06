package GUI;

import cliente_proyectoredes.myClient;
import data.Conexion;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class window2Client extends JInternalFrame implements ActionListener, Runnable {

    JButton btnAgregaArchivo, btnEnviarArchivo, btnpedirRepositorio;
    JLabel lbArchivo, lbContrasenna;
    JTextField tfArchivo, tfContrasenna;
    private JFileChooser jfileChooser;
    myClient client;
    String rutaArchivo, rutaArchivoBD;
    int idUsuario;
    DefaultTableModel modelo = new DefaultTableModel();
    JTable tabla;
    Conexion c;
    ArrayList<String> repositorios;
    String listaRepositorios,nombreRepos;
    
    public window2Client(myClient Client, String rutaBD, int idUsuario,String listaRepositorios,String nombreRepos) throws SQLException {
        super("Mi tabla");
        this.client = Client;
        this.rutaArchivoBD = rutaBD;
        this.idUsuario = idUsuario;
        this.listaRepositorios = listaRepositorios;
        this.nombreRepos = nombreRepos;
        init();
    }//Constructor

    public void init() throws SQLException {

        this.setVisible(true);
        this.setLayout(null);
        this.setClosable(true);
        this.setBackground(new java.awt.Color(255, 255, 255));
        this.setBounds(500, 100, 30, 10);
        tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        modelo.addColumn("Id");
        modelo.addColumn("Repositorio");
        tabla.getColumnModel().getColumn(0).setPreferredWidth(10);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(280);

        c = new Conexion();
        repositorios=sacarVectorRepositorios(listaRepositorios);
        String repo[] = new String[2];
        for (int i = 0; i < repositorios.size(); i++) {
            repo[0] = String.valueOf(i + 1);
            repo[1] = repositorios.get(i);
            modelo.addRow(repo);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        tabla.setBounds(50, 100, 300, 200);
        setTitle("Sistema de control de asistencia laboral");
        scroll.setBounds(50, 100, 300, 200);
        add(scroll);
        //this.add(this.tabla);

        this.lbArchivo = new JLabel("Buscar Archivo");
        this.tfArchivo = new JTextField();
        this.btnAgregaArchivo = new JButton("Buscar");
        this.btnEnviarArchivo = new JButton("Enviar");
        this.btnpedirRepositorio = new JButton("Pedir repositorio");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.lbArchivo.setBounds(50, 50, 150, 20);
        this.add(this.lbArchivo);

        this.tfArchivo.setBounds(150, 50, 150, 20);
        this.add(this.tfArchivo);

        this.btnAgregaArchivo.setBounds(320, 50, 80, 20);
        this.add(this.btnAgregaArchivo);
        this.btnAgregaArchivo.addActionListener(this);

        this.btnEnviarArchivo.setBounds(450, 50, 80, 20);
        this.add(this.btnEnviarArchivo);
        this.btnEnviarArchivo.addActionListener(this);

        this.btnpedirRepositorio.setBounds(500, 300, 150, 20);
        this.add(this.btnpedirRepositorio);
        this.btnpedirRepositorio.addActionListener(this);
    }
    public ArrayList<String> sacarVectorRepositorios(String listaRepositorios){
        ArrayList<String> listaRepos = new ArrayList<String>();
        int cont = 0;
        for (int i = 0; i < listaRepositorios.length(); i++) {
            if (listaRepositorios.charAt(i) == ':') {
                cont++;
            }
        }
        System.out.println("Hay " + cont + " de :");
        String[] partes = listaRepositorios.split(":");
        for (int i = 0; i < cont; i++) {
            listaRepos.add(partes[i]);
            System.out.println("El nombre del archivo es: " + listaRepos.get(i));
        }
        return listaRepos;
    }
    public void listar() throws SQLException {
        System.out.println("El tamaño del array es:"+modelo.getRowCount());
        while(modelo.getRowCount()>0){
            System.out.println("Tamanno>"+modelo.getRowCount());
            modelo.removeRow(0);
        }
        repositorios.clear();
        String lista="";
        lista= client.pideRepositorio(nombreRepos, client.getSocket());
        repositorios = sacarVectorRepositorios(lista);
        String repo[] = new String[2];
        for (int i = 0; i < repositorios.size(); i++) {
            repo[0] = String.valueOf(i + 1);
            repo[1] = repositorios.get(i);
            modelo.addRow(repo);
        }
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
                    this.rutaArchivo = tfArchivo.getText();
                }
            }

        } else if (ae.getSource().equals(btnEnviarArchivo)) {
            tfArchivo.setText("");
            client.enviaFichero(this.rutaArchivo, client.getSocket(), this.rutaArchivoBD, this.idUsuario);
            try {
                listar();
                System.out.println("llegaaaaaaa");
            } catch (SQLException ex) {
                Logger.getLogger(window2Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (ae.getSource().equals(btnpedirRepositorio)) {
            int filaSelect = tabla.getSelectedRow();
            if (filaSelect >= 0) {
                modelo.getValueAt(filaSelect, 1);
                System.out.println("ESTO TIENE>" + this.rutaArchivoBD + "/" + modelo.getValueAt(filaSelect, 1));
                String user = System.getProperty("user.name");
               
                String url = "C:\\Users\\" + user + "\\Documents\\Servidor_ProyectoRedes\\"+this.rutaArchivoBD + "\\" + modelo.getValueAt(filaSelect, 1);
//C:\Users\ronal\Documents\NetBeansProjects\ProyectoRedes_B75923_B66666\Servidor_ProyectoRedes\CarpetasUsuario\Ronald
                client.pide(url, client.getSocket());
                System.out.println("Ya pedi y me llego");
            }
        } else {
            JOptionPane.showMessageDialog(null, "La tabla esta vacia o no selecciono ningún empleado");
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
