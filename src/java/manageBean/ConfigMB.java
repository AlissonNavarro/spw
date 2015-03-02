package manageBean;

import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigMB {
    
    private AcessoBD con;
    
    public ConfigMB() {
        con = new AcessoBD();
    }

    public boolean insertADIP(String ip) {
        try {
            String query = " UPDATE config SET IpAd = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, ip);
                return (con.executeUpdate() > 0);
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB insertADIP " + ex);
        }
        return false;
    }

    public boolean insertDigitIP(String ipSender, String ipCatcher) {
        try {
            String query = " UPDATE config SET ipDigitCatcher = ? , ipDigitSender = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, ipCatcher);
                con.pstmt.setString(2, ipSender);
                return con.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB insertDigitIP " + ex);
        }
        return false;
    }

    public String getServidorAD() {
        try {
            String sql = "select IpAD from config";
            ResultSet rs = con.executeQuery(sql);
            String servidor = "";
            if (rs.next()) {
                servidor = rs.getString("IpAd");
            }
            rs.close();
            if (servidor == null) {
                servidor = "";
            }
            return servidor;
        } catch (SQLException ex) {
            System.out.println("EmpresaMB getServidorAD " + ex);
        } finally {
            con.Desconectar();
        }
        return "";
    }

    public String getServidorDigitSender() {
        try {
            String sql = "select ipDigitSender from config";
            ResultSet rs = con.executeQuery(sql);
            String servidor = "";
            if (rs.next()) {
                servidor = rs.getString("ipDigitSender");
            }
            rs.close();
            if (servidor == null) {
                servidor = "";
            }
            return servidor;
        } catch (SQLException ex) {
            System.out.println("EmpresaMB getServidorDigitSender " + ex);
        } finally {
            con.Desconectar();
        }
        return "";
    }

    public String getServidorDigitCatcher() {
        try {
            String sql = "select ipDigitCatcher from config";
            ResultSet rs = con.executeQuery(sql);
            String servidor = "";
            if (rs.next()) {
                servidor = rs.getString("ipDigitCatcher");
            }
            rs.close();
            if (servidor == null) {
                servidor = "";
            }
            return servidor;
        } catch (SQLException ex) {
            System.out.println("EmpresaMB getServidorDigitCatcher " + ex);
        } finally {
            con.Desconectar();
        }
        return "";
    }

}
