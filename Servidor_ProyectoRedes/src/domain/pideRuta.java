package domain;

import java.io.Serializable;

/**
 * Mensaje para pedir un fichero.
 * @author Ronald
 *
 */
public class pideRuta implements Serializable
{
    /** path completo del fichero que se pide */
    public String nombreFichero;
}
