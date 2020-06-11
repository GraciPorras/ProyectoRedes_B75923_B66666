    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_proyectoredes;


import domain.Fichero;
import domain.pideRuta;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Graciela Porras
 */
public class EchoMultiServer extends Thread {
    
    private int socketPorttNumber;//numero de puerto tiene que ser igual cliente-servidor
    private InetAddress address;
    private boolean conectado;
    private ArrayList<GestionDeCliente> gestionClienteArray;

    public EchoMultiServer(int socketProntNumber) {
        super("Hilo Servidor");
        this.socketPorttNumber = socketProntNumber;
        this.gestionClienteArray = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.socketPorttNumber);
            
            do {
                System.out.println("Servidor ejecutando");
                Socket socket = serverSocket.accept();
                System.out.println("Cliente acceptado");
                GestionDeCliente gestionCliente = new GestionDeCliente(socket, gestionClienteArray);

                Thread hilo = new Thread((Runnable) gestionCliente);
                this.gestionClienteArray.add(gestionCliente);
//              
                hilo.start();
//                socket.close();
            } while (true);
            
        } catch (IOException ex) {
            Logger.getLogger(EchoMultiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
