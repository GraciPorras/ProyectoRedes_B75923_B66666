/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ronal
 */
public class Conexion {

    private static Connection con;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "laboratorios";
    private static final String pass = "UCRSA.118";
    private static final String url = "jdbc:mysql://163.178.107.10:3306/proyecto_redes_b66666_b75923";

    public void conectar() {
        con = null;
        try {
            Class.forName(driver);
            //nos conectamos a la base de datos
            con = (Connection) DriverManager.getConnection(url, user, pass);
            if (con != null) {
                System.out.println("Conexion establecida con mysql");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error de conexion" + e);
        }
    }

    public boolean verificarUsuario(String usuario, String contrasenna) throws SQLException {
        boolean salida = false;
        con = (Connection) DriverManager.getConnection(url, user, pass);
        Statement s = (Statement) con.createStatement();
        ResultSetImpl rs = (ResultSetImpl) s.executeQuery("CALL sp_verificarUsuario ('" + usuario + "','" + contrasenna + "');");
        while (rs.next()) {
            if (rs.getInt(1) == 1) {
                System.out.println("Si esta correcto");
                salida = true;
            } else {
                System.out.println("Usuario incorrecto");
                salida = false;
            }
        }
        con.close();
        return salida;
    }
}
