package cadastroVerba;

import backup.*;
import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author amsgama
 */
public class Banco {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("cadastroVerba: Conectar: "+cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("cadastroVerba: Banco: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Integer> getVerbas() {

        List<Integer> verbasList = new ArrayList<Integer>();

        PreparedStatement pstmt = null;
        try {

            String query = "Select * from verba";

            pstmt = c.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Integer empresa = rs.getInt("empresa");
                Integer adicionalNoturno = rs.getInt("adicionalNoturno");
                Integer atrasos = rs.getInt("atrasos");
                Integer atrasosMenorHora = rs.getInt("atrasosMenorHora");
                Integer atrasosMaiorHora = rs.getInt("atrasosMaiorHora");
                Integer FeriadoCritico = rs.getInt("FeriadoCritico");
                Integer DSR = rs.getInt("DSR");
                Integer Faltas = rs.getInt("Faltas");
                


                verbasList.add(empresa);
                verbasList.add(adicionalNoturno);
                verbasList.add(atrasos);
                verbasList.add(atrasosMenorHora);
                verbasList.add(atrasosMaiorHora);
                verbasList.add(FeriadoCritico);              
                verbasList.add(DSR);
                verbasList.add(Faltas);
                


            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return verbasList;
    }

    public Boolean updateVerbas(List<Integer> verbaList) {

        Boolean flag = true;
        PreparedStatement pstmt = null;


        String query = "update verba set empresa=?,adicionalNoturno=?, atrasos=?, atrasosMenorHora=?, atrasosMaiorHora=?, FeriadoCritico=?,"
                + " DSR=?,Faltas=?";

        try {
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, verbaList.get(0));
            pstmt.setInt(2, verbaList.get(1));
            pstmt.setInt(3, verbaList.get(2));
            pstmt.setInt(4, verbaList.get(3));
            pstmt.setInt(5, verbaList.get(4));
            pstmt.setInt(6, verbaList.get(5));
            pstmt.setInt(7, verbaList.get(6));
            pstmt.setInt(8, verbaList.get(7));
            
            
            pstmt.executeUpdate();
        } catch (Exception e) {
            flag = false;
            System.out.println(e.getMessage());
            System.out.print(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }
}
