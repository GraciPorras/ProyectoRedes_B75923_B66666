/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_proyectoredes;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        super("Hilo servidor");//estamos llamando el constructo de la clase padre (thread)
        this.socketProntNumber = socketProntNumber;

    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.socketProntNumber);//socket de tipo server, reconoce si recibio petición
            do {
                Socket socket = serverSocket.accept();
                send = new PrintStream(socket.getOutputStream());
                receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Objeto que recibo

                while (true) {
                    send.println("Holaaa");
                    System.out.println(receive.readLine());
                }
//                socket.close();
            } while (true);

        } catch (IOException ex) {
            Logger.getLogger(EchoMultiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
