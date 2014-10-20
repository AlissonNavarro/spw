/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Abono;

import Metodos.Metodos;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ppccardoso
 */
public class AbonoBanco implements Serializable {
    
    Driver d;
    Connection c;
    
    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Abono: Conectar: "+cnfe);
        }
    }

    public AbonoBanco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("Abono: Banco: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
