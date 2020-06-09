/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;

/**
 *
 * @author ronal
 */
public class Fichero implements Serializable{
    public String nombreFichero="";
    
    public boolean ultimoMensaje=true;
    
    public int bytesValidos=0;
    
    public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];
    
    public final static int LONGITUD_MAXIMA=10;
    
    public String ruta_server="";
}
