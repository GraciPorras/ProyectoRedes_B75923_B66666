/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.Statement;
import domain.Usuario;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ronal
 */
public class Conexion {
    private static Connection con;
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user = "laboratorios";
    private static final String pass = "UCRSA.118";
    private static final String url= "jdbc:mysql://163.178.107.10:3306/proyecto_redes_b66666_b75923";
    
    public void conectar(){
        con=null;
        try{
            Class.forName(driver);
            //nos conectamos a la base de datos
            con=(Connection) DriverManager.getConnection(url,user,pass);
            if(con!=null){
                System.out.println("Conexion establecida con mysql");
            }
        }catch(ClassNotFoundException | SQLException e){
            System.err.println("Error de conexion"+e);
        }
    }
    public void selectUsuario() throws SQLException{
        con=(Connection) DriverManager.getConnection(url,user,pass);
        Statement s = (Statement) con.createStatement();
        ResultSetImpl rs = (ResultSetImpl) s.executeQuery("SELECT * FROM usuario");
        Usuario u;
        while (rs.next()){
            u = new Usuario(rs.getInt(1), rs.getString (2), rs.getString (3), rs.getString (4));
            System.out.println (rs.getInt (1) + " " +rs.getString (2) + " " + rs.getString(3) +" "+ rs.getString(4));
            System.out.println(u.toString());
        }
        
        con.close();
    }
    public void insertarUsuario(String nombre,String contrasenna,String rutaArchivo) throws SQLException {
        con=(Connection) DriverManager.getConnection(url,user,pass);
        Statement instruccion = (Statement) con.createStatement();
        instruccion.execute("CALL sp_insertarUsuario ('"+nombre+"','"+contrasenna+"','"+rutaArchivo+"');");
        con.close();
    }
    public void deleteUsuario(String nombre,String contrasenna) throws SQLException{
        con=(Connection) DriverManager.getConnection(url,user,pass);
        Statement instruccion = (Statement) con.createStatement();
        instruccion.execute("CALL sp_borrarUsuario ('"+nombre+"','"+contrasenna+"')");
        con.close();
    }
    public void updateUsuario(String nombreCondicion,String contrasennaCondicion,String nombre,String contrasenna,String rutaArchivo) throws SQLException{
        con=(Connection) DriverManager.getConnection(url,user,pass);
        Statement instruccion = (Statement) con.createStatement();
        instruccion.execute("CALL sp_actualizarUsuario ('"+nombreCondicion+"','"+contrasennaCondicion+"','"+nombre+"','"+contrasenna+"','"+rutaArchivo+"');");
        con.close();
    }
    
}
