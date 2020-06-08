/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente_proyectoredes;

import domain.Fichero;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Graciela Porras
 */
public class myClient  extends Thread{
    
    private int socketPortNumber;// numero donde se va a guardar puerto
    PrintStream send;
    BufferedReader receive;

    public myClient(int socketPortNumber) {
        this.socketPortNumber = socketPortNumber;
    }

    @Override
    public void run() {
        
    }
    public  void enviaFichero(String fichero, String servidor, int puerto){
        try
        {
            
            // Se abre el socket.
            Socket socket = new Socket(servidor, puerto);

            // Se env�a un mensaje de petici�n de fichero.
            ObjectOutputStream oos = new ObjectOutputStream(socket
                    .getOutputStream());
            
            boolean enviadoUltimo=false;
            // Se abre el fichero.
            FileInputStream fis = new FileInputStream(fichero);
//            System.out.println("llllllllll"+fis.read());
            // Se instancia y rellena un mensaje de envio de fichero
            Fichero mensaje = new Fichero();
            mensaje.nombreFichero = "C:/Users/ronal/Documents/NetBeansProjects/kitt12.png";
//            System.out.println("dddddddd"+mensaje.nombreFichero);
            
            // Se leen los primeros bytes del fichero en un campo del mensaje
            int leidos = fis.read(mensaje.contenidoFichero);
            
            // Bucle mientras se vayan leyendo datos del fichero
            while (leidos > -1)
            {
//                System.out.println("bits----> "+leidos);
                // Se rellena el n�mero de bytes leidos
                mensaje.bytesValidos = leidos;
//                 System.out.println("bits validos ----> "+mensaje.bytesValidos);
                // Si no se han leido el m�ximo de bytes, es porque el fichero
                // se ha acabado y este es el �ltimo mensaje
                if (leidos < Fichero.LONGITUD_MAXIMA)
                {
//                    System.out.println("bits----------> "+leidos);
                   
                    mensaje.ultimoMensaje = true;
                    enviadoUltimo=true;
                }
                else
                    mensaje.ultimoMensaje = false;
                
                // Se env�a por el socket
//                System.out.println("MENSje---> "+mensaje.toString());
                oos.writeObject(mensaje);
//                System.out.println("MENSjefffffffffffffffffffe---> "+mensaje.toString());
                // Si es el �ltimo mensaje, salimos del bucle.
                if (mensaje.ultimoMensaje)
                    break;
                
                // Se crea un nuevo mensaje
                mensaje = new Fichero();
                mensaje.nombreFichero = fichero;
                
                // y se leen sus bytes.
                leidos = fis.read(mensaje.contenidoFichero);
//                System.out.println("fiiiiiiiiiiiin bits----> "+leidos);
            }
            
            if (enviadoUltimo==false)
            {
                mensaje.ultimoMensaje=true;
                mensaje.bytesValidos=0;
                oos.writeObject(mensaje);
            }
            // Se cierra el ObjectOutputStream
            oos.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
