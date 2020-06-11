/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente_proyectoredes;

import data.Conexion;
import domain.Fichero;
import domain.MiRepositorio;
import domain.pideRuta;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
public class myClient extends Thread {

    private int socketPortNumber;// numero donde se va a guardar puerto
    public Socket socket;
    public String address;
    Conexion c;
    ObjectOutputStream oos;

    public myClient(int socketPortNumber) throws IOException {
        this.socketPortNumber = socketPortNumber;
        this.address = "localhost";
        this.socket = new Socket(address, this.socketPortNumber);
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {

    }

    public void enviaFichero(String fichero, Socket socket, String rutaBDServer, int idUsuario) {
        try {

            String nombreArchivo = sacarNombreArchivo(fichero);
            c = new Conexion();
            c.insertarUsuarioArchivo(idUsuario, nombreArchivo);
            System.out.println("El fichero es: " + fichero);

            //oos = new ObjectOutputStream(socket.getOutputStream());
            boolean enviadoUltimo = false;
            FileInputStream fis = new FileInputStream(fichero);

            // Se instancia y rellena un mensaje de envio de fichero
            Fichero mensaje = new Fichero();
            mensaje.nombreFichero = rutaBDServer + "/" + nombreArchivo;
            //mensaje.nombreFichero = fichero;

            // Se leen los primeros bytes del fichero en un campo del mensaje
            int leidos = fis.read(mensaje.contenidoFichero);

            // Bucle mientras se vayan leyendo datos del fichero
            while (leidos > -1) {

                // Se rellena el n�mero de bytes leidos
                mensaje.bytesValidos = leidos;

                // Si no se han leido el m�ximo de bytes, es porque el fichero
                // se ha acabado y este es el �ltimo mensaje
                if (leidos < Fichero.LONGITUD_MAXIMA) {
                    mensaje.ultimoMensaje = true;
                    enviadoUltimo = true;
                } else {
                    mensaje.ultimoMensaje = false;
                }
                // Se env�a por el socket
                oos.writeObject(mensaje);

                // Si es el �ltimo mensaje, salimos del bucle.
                if (mensaje.ultimoMensaje) {
                    break;
                }

                // Se crea un nuevo mensaje
                mensaje = new Fichero();
                mensaje.nombreFichero = fichero;

                // y se leen sus bytes.
                leidos = fis.read(mensaje.contenidoFichero);
            }

            if (enviadoUltimo == false) {
                mensaje.ultimoMensaje = true;
                mensaje.bytesValidos = 0;
                oos.writeObject(mensaje);
            }
            // Se cierra el ObjectOutputStream
            //oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sacarNombreArchivo(String fichero) {
        String nombreArchivo = "";
        int cont = 0;
        for (int i = 0; i < fichero.length(); i++) {
            if (fichero.charAt(i) == '\\' || fichero.charAt(i) == '/') {
                cont++;
            }
        }
        String nuevoFichero;
        nuevoFichero = fichero.replace('\\', '/');
        System.out.println(">>" + nuevoFichero);
        System.out.println("Hay " + cont + " de /");
        String[] partes = nuevoFichero.split("/");
        nombreArchivo = partes[cont];
        System.out.println("El nombre del archivo es: " + nombreArchivo);

        return nombreArchivo;
    }

    public void pide(String fichero, Socket socket) {
        try {

            // Se env�a un mensaje de petici�n de fichero.
            pideRuta mensaje = new pideRuta();
            mensaje.nombreFichero = fichero;
            oos.writeObject(mensaje);

            // Se abre un fichero para empezar a copiar lo que se reciba.
            System.out.println("fich--->" + fichero);
            String nombreArchivo = sacarNombreArchivo(fichero);
            System.out.println("nombreeeeee" + nombreArchivo);
            FileOutputStream fos = new FileOutputStream("MisArchivos/" + nombreArchivo);

            // Se crea un ObjectInputStream del socket para leer los mensajes
            // que contienen el fichero.
            System.out.println("1");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("2");
            Fichero mensajeRecibido;
            Object mensajeAux;
            do {
                // Se lee el mensaje en una variabla auxiliar
                mensajeAux = ois.readObject();

                //System.out.println(">>>>>"+ois.readObject().toString());
                // Si es del tipo esperado, se trata
                if (mensajeAux instanceof Fichero) {
                    System.out.println("aaaaaaaaaaaaaaaa");
                    mensajeRecibido = (Fichero) mensajeAux;
                    // Se escribe en pantalla y en el fichero
                    System.out.print(new String(
                            mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos));
                    fos.write(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos);
                } else {
                    // Si no es del tipo esperado, se marca error y se termina
                    // el bucle
                    System.err.println("Mensaje no esperado "
                            + mensajeAux.getClass().getName());
                    break;
                }
            } while (!mensajeRecibido.ultimoMensaje);

            // Se cierra socket y fichero
            fos.close();
            //ois.close();
            //socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String pideRepositorio(String repositorio, Socket socket) {
        String listaRepositorio="";
        try {
            
            // Se env�a un mensaje de petici�n de fichero.
            System.out.println("1");
            MiRepositorio mensaje = new MiRepositorio();
            mensaje.repositorioUsuario= repositorio;
            System.out.println("Escribi el mensaje");
            oos.writeObject(mensaje);
            System.out.println("Ahora continuo");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Object mensajeAux;
            mensajeAux = ois.readObject();

            if (mensajeAux instanceof MiRepositorio) {
                System.out.println("NOMMBRE QUE ENVIARE");
                MiRepositorio listaRepo = (MiRepositorio) mensajeAux;
                System.out.println("Lista->" + listaRepo.listaRepositorios);
                listaRepositorio= listaRepo.listaRepositorios;
            } else {

                System.err.println("Mensaje no esperado "
                        + mensajeAux.getClass().getName());
            }
         } catch (Exception e) {
            e.printStackTrace();
        }
         return listaRepositorio;
    }

    public int getSocketPortNumber() {
        return socketPortNumber;
    }

    public void setSocketPortNumber(int socketPortNumber) {
        this.socketPortNumber = socketPortNumber;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
