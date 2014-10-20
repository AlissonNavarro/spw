/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AFD;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author amvboas
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
            System.out.println("AFD: Conectar: "+cnfe);
        }
    }

    public void ConectarMain() throws SQLException {
        String url = "jdbc:jtds:sqlserver://FS-DESEN-28:1904/HUSE;user=user_web;password=123@net";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
    }

    public Banco(Boolean isMain) {
        try {
            if (isMain) {
                ConectarMain();
            } else {
                Conectar();
            }
        } catch (SQLException ex) {
            System.out.println("AFD: Banco: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void atualizaMarcacoes(RegistroMarcacaoDePonto registro) {
        PreparedStatement pstmt = null;
        Date marcacao = registro.getDataEHoraMarcacao();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String marcacaoStr = sdf.format(marcacao);
        String pis = registro.getPis();
        boolean ok = true;
        try {
            ResultSet rs = null;
            String query = "SELECT     USERID FROM USERINFO WHERE     (PIS = ?)";
            pstmt = c.prepareStatement(query);
            pstmt.setString(1, pis);
            rs = pstmt.executeQuery();
            Integer userid = null;
            while (rs.next()) {
                userid = rs.getInt("userid");
            }
            pstmt.close();
            query = "INSERT INTO CHECKINOUT values(?,?,?,?,?,?,?,?)";
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, userid);
            pstmt.setString(2, marcacaoStr);
            pstmt.setString(3, "I");
            pstmt.setInt(4, 0);
            pstmt.setInt(5, 0);
            pstmt.setString(6, null);
            pstmt.setInt(7, 0);
            pstmt.setString(8, null);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            ok = false;
            System.out.println("Erro: " + e);
        } finally {
            if (ok) {
                System.out.println("Marcação - Pis: "+registro.getPis()+" hora: "+marcacaoStr);
            }
        }
    }

    public void atualizaUsuarios(RegistroEmpregado registroEmpregado) {
        boolean ok = true;
        String pis = registroEmpregado.getPis();
        char tipo = registroEmpregado.getTipoDaOperacao();
        String nome = registroEmpregado.getNomeEmpregado();
        try {
            PreparedStatement pstmt = null;
            String query;
            switch (tipo) {
                case 'I':
                    query = "INSERT INTO USERINFO (name,BADGENUMBER,SSN,PIS) values(?,?,?,?)";
                    pstmt = c.prepareStatement(query);
                    pstmt.setString(1, nome);
                    pstmt.setString(2, "PIS"+pis);
                    pstmt.setString(3, "PIS"+pis);
                    pstmt.setString(4, pis);
                    pstmt.executeUpdate();
                    pstmt.close();
                    break;
                case 'A':
                    query = "update USERINFO set name = ? where pis = ?";
                    pstmt = c.prepareStatement(query);
                    pstmt.setString(1, nome);
                    pstmt.setString(2, pis);
                    pstmt.executeUpdate();
                    pstmt.close();
                    break;
                case 'E':
                    query = "delete from USERINFO where pis = ?";
                    pstmt = c.prepareStatement(query);
                    pstmt.setString(1, pis);
                    pstmt.executeUpdate();
                    pstmt.close();
                    break;
            }
        } catch (Exception e) {
            ok=false;
            System.out.println("Erro: " + e);
        } finally{
            if(ok){
                System.out.println("Alteracao usuario - Operação: "+(tipo=='I'?"Inclusão":((tipo=='A')?"Alteração":"Exclusão"))+" Nome: "+nome);
            }
        }
    }

    public void fecharConexao() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("AFD: fecharConexao: "+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
