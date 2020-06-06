/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente_proyectoredes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        try {
            InetAddress address = InetAddress.getLocalHost();
//            InetAddress address = InetAddress.getByName("192.168.43.26");
            Socket socket = new Socket(address, this.socketPortNumber);//el objeto scket tiene una direccion y un numero de puerto

            send = new PrintStream(socket.getOutputStream());//objeto que permite hacer envio
            receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Objeto que recibo 
            
            while (true) {
               System.out.println(receive.readLine());
               

            }

//            socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PrintStream getSend() {
        return send;
    }

    public void setSend(PrintStream send) {
        this.send = send;
    }

    public BufferedReader getReceive() {
        return receive;
    }

    public void setReceive(BufferedReader receive) {
        this.receive = receive;
    }
    
    
}
