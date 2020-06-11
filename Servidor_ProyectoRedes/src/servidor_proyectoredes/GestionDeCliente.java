/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_proyectoredes;

import domain.Fichero;
import domain.pideRuta;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

class GestionDeCliente extends Thread {

    Socket socket;
    private ArrayList<GestionDeCliente> gestionClienteArray;

    public GestionDeCliente(Socket socket, ArrayList gestionClienteArray) {

        this.socket = socket;

        this.gestionClienteArray = gestionClienteArray;

    }

    @Override
    public void run() {
        escucha(socket);

    }

    public void escucha(Socket cliente) {
        try {

            //System.out.println("Aceptado cliente");
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());

            while (true) {

                Object mensaje = ois.readObject();

                if (mensaje instanceof pideRuta) {
                    //System.out.println("Me piden: "
                    // + ((pideRuta) mensaje).nombreFichero);
                    enviaFichero(((pideRuta) mensaje).nombreFichero,
                            new ObjectOutputStream(cliente.getOutputStream()));

                } else if (mensaje instanceof Fichero) {
                    //System.out.println("eeeeeeeeeeeeeeeeee");
                    Fichero mensajeRecibido = (Fichero) mensaje;

                    System.out.println(Arrays.toString(mensajeRecibido.contenidoFichero));

                    FileOutputStream fos = new FileOutputStream(mensajeRecibido.nombreFichero);

                    fos.write(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos);

                    Object mensajeAux;
                    do {
                        //System.out.println(Arrays.toString(mensajeRecibido.contenidoFichero));
                        mensajeAux = ois.readObject();

                        if (mensajeAux instanceof Fichero) {

                            mensajeRecibido = (Fichero) mensajeAux;

                            //System.out.print(new String(
                            //mensajeRecibido.contenidoFichero, 0,mensajeRecibido.bytesValidos));
                            fos.write(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos);
                        } else {

                            System.err.println("Mensaje no esperado "
                                    + mensajeAux.getClass().getName());
                            break;
                        }

                    } while (!mensajeRecibido.ultimoMensaje);

                    //fos.close();
                    //ois.close();
                } else {
                    // Si no es el mensaje esperado, se avisa y se sale todo.
                    System.err.println(
                            "Mensaje no esperado " + mensaje.getClass().getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void enviaFichero(String fichero, ObjectOutputStream oos) {
        try {
            //System.out.println("sssssssssssssssssssss");
            boolean enviadoUltimo = false;
            // Se abre el fichero.
            FileInputStream fis = new FileInputStream(fichero);

            // Se instancia y rellena un mensaje de envio de fichero
            Fichero mensaje = new Fichero();
            mensaje.nombreFichero = fichero;

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

}
