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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Graciela Porras
 */
public class EchoMultiServer extends Thread {
    
    private int socketProntNumber;//numero de puerto tiene que ser igual cliente-servidor
    PrintStream send;
    BufferedReader receive;

    public EchoMultiServer(int socketProntNumber) {
        this.socketProntNumber = socketProntNumber;

    }

    @Override
    public void run() {
        escucha(socketProntNumber);
    }
    public void escucha(int puerto)
    {
        try
        {
            // Se abre el socket servidor
            ServerSocket socketServidor = new ServerSocket(puerto);

            // Se espera un cliente
            System.out.println("Esperando a que se conecte un cliente");
            Socket cliente = socketServidor.accept();

            // Llega un cliente.
            System.out.println("Aceptado cliente");

            // Cuando se cierre el socket, esta opci�n hara que el cierre se
            // retarde autom�ticamente hasta 10 segundos dando tiempo al cliente
            // a leer los datos.
            cliente.setSoLinger(true, 10);

            // Se lee el mensaje de petici�n de fichero del cliente.
            ObjectInputStream ois = new ObjectInputStream(cliente
                    .getInputStream());
            Object mensaje = ois.readObject();
            System.out.println("sssssssssssssssssssss");
            if (mensaje instanceof pideRuta)
            {
                
                
                // Se muestra en pantalla el fichero pedido y se envia
                System.out.println("Me piden: "
                        + ((pideRuta) mensaje).nombreFichero);
                enviaFichero(((pideRuta) mensaje).nombreFichero,
                       new ObjectOutputStream(cliente.getOutputStream()));
                
            }else  if(mensaje instanceof Fichero){
                System.out.println("eeeeeeeeeeeeeeeeee");
                Fichero mensajeRecibido = (Fichero) mensaje;
                // Se abre un fichero para empezar a copiar lo que se reciba.
                FileOutputStream fos = new FileOutputStream(mensajeRecibido.nombreFichero);
                Object mensajeAux;
                do
                {
                    // Se lee el mensaje en una variabla auxiliar
                    mensajeAux = ois.readObject();

                    // Si es del tipo esperado, se trata
                    if (mensajeAux instanceof Fichero)
                    {
                        mensajeRecibido = (Fichero) mensajeAux;
                        // Se escribe en pantalla y en el fichero
                        //System.out.print(new String(mensajeRecibido.contenidoFichero, 0,mensajeRecibido.bytesValidos));
                        fos.write(mensajeRecibido.contenidoFichero, 0,
                                mensajeRecibido.bytesValidos);
                    } else
                    {
                        // Si no es del tipo esperado, se marca error y se termina
                        // el bucle
                        System.err.println("Mensaje no esperado "
                                + mensajeAux.getClass().getName());
                        break;
                    }
                } while (!mensajeRecibido.ultimoMensaje);
//                do
//                {
                    // Se escribe en pantalla y en el fichero
//                    System.out.print(new String(
//                            mensajeRecibido.contenidoFichero, 0,
//                            mensajeRecibido.bytesValidos));
//                    fos.write(mensajeRecibido.contenidoFichero, 0,
//                            mensajeRecibido.bytesValidos);
                    
//                } while (!mensajeRecibido.ultimoMensaje);

                // Se cierra socket y fichero
                fos.close();
                ois.close();

                // Se muestra en pantalla el fichero pedido y se envia
//                System.out.println("Me piden: "
//                        + ((MensajeDameFichero) mensaje).nombreFichero);
//                enviaFichero(((MensajeDameFichero) mensaje).nombreFichero,
//                        new ObjectOutputStream(cliente.getOutputStream()));
                
                
            }
            else
            {
                // Si no es el mensaje esperado, se avisa y se sale todo.
                System.err.println (
                        "Mensaje no esperado "+mensaje.getClass().getName());
            }
            
            // Cierre de sockets 
            cliente.close();
            socketServidor.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
       
    private void enviaFichero(String fichero, ObjectOutputStream oos)
    {
        try
        {
            System.out.println("sssssssssssssssssssss");
            boolean enviadoUltimo=false;
            // Se abre el fichero.
            FileInputStream fis = new FileInputStream(fichero);
            
            // Se instancia y rellena un mensaje de envio de fichero
            Fichero mensaje = new Fichero();
            mensaje.nombreFichero = fichero;
            
            // Se leen los primeros bytes del fichero en un campo del mensaje
            int leidos = fis.read(mensaje.contenidoFichero);
            
            // Bucle mientras se vayan leyendo datos del fichero
            while (leidos > -1)
            {
                
                // Se rellena el n�mero de bytes leidos
                mensaje.bytesValidos = leidos;
                
                // Si no se han leido el m�ximo de bytes, es porque el fichero
                // se ha acabado y este es el �ltimo mensaje
                if (leidos < Fichero.LONGITUD_MAXIMA)
                {
                    mensaje.ultimoMensaje = true;
                    enviadoUltimo=true;
                }
                else{
                    mensaje.ultimoMensaje = false;
                }
                // Se env�a por el socket
                oos.writeObject(mensaje);
                
                // Si es el �ltimo mensaje, salimos del bucle.
                if (mensaje.ultimoMensaje)
                    break;
                
                // Se crea un nuevo mensaje
                mensaje = new Fichero();
                mensaje.nombreFichero = fichero;
                
                // y se leen sus bytes.
                leidos = fis.read(mensaje.contenidoFichero);
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
