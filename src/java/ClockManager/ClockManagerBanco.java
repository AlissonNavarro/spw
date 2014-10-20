/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockManager;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author ppccardoso
 */
public class ClockManagerBanco {
    
    Driver d;
    Connection c;
    private Boolean isAtivo = false;
    static Connection theConn;
    Boolean hasConnection = false;

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        hasConnection = true;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            hasConnection = false;
            System.out.println("ClockManager.Banco: Conectar(): " + cnfe);
        }
    }

    public ClockManagerBanco() {
        try {
            Conectar();
            isAtivo = true;
        } catch (SQLException ex) {
            hasConnection = false;
            System.out.println("ClockManager.Banco: Banco(): " + ex);
            isAtivo = false;
        }
    }
    
    public static Connection getConnection() throws Exception {
        Driver d = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
        ClockManagerBean clockManager = new ClockManagerBean();
        Connection c = DriverManager.getConnection("jdbc:sqlite://" + clockManager.getDbway());
        return c;
    }
    
    public void cadastrarFuncionarios() {
        try {
            theConn = getConnection();
            PreparedStatement pstmt = null;
            Statement stmt = null;
            Statement stmt2 = null;
            Statement stmt3 = null;
            Statement stmt4 = null;
            ResultSet rs = null;
            ResultSet rs2 = null;
            ResultSet rs3 = null;
            ResultSet rs4 = null;

            String sqlVinculo = "select employeename, pis from employee";

            stmt = theConn.createStatement();
            rs = stmt.executeQuery(sqlVinculo);

            int i = 0;
            String sqlSelect = "select max(userid) as u from USERINFO ";

            stmt2 = c.createStatement();
            rs2 = stmt2.executeQuery(sqlSelect);

            while (rs2.next()) {
                i = rs2.getInt("u");
            }

            int codRegime = 1;
            String sqlRegime = "select cod_regime from Regime_HoraExtra where (nome = 'CELETISTA' or nome = 'CLT')";

            stmt4 = c.createStatement();
            rs4 = stmt4.executeQuery(sqlRegime);

            while (rs4.next()) {
                codRegime = rs4.getInt("cod_regime");
            }

            String employee;
            String pis;
            //String employeeReg;
            while (rs.next()) {
                employee = rs.getString("employeename");
                pis = rs.getString("pis");
                //employeeReg = rs.getString("employeereg");

                boolean exists = false;
                if (pis.length() == 11) {
                    String sqlSsn = "select * from USERINFO where pis = '" + pis + "'";

                    stmt3 = c.createStatement();
                    rs3 = stmt3.executeQuery(sqlSsn);

                    while (rs3.next()) {
                        exists = true;
                    }


                    String sql;
                    if (!exists) {
                        sql = "insert into USERINFO(name,pis,cod_regime,perfil,ativo,badgenumber,userid) values(?,?,?,?,?,?,?)";
                        pstmt = c.prepareStatement(sql);
                        pstmt.setString(1, employee);
                        pstmt.setString(2, pis);
                        pstmt.setInt(3, codRegime);
                        pstmt.setInt(4, 1);
                        pstmt.setBoolean(5, true);
                        i++;
                        pstmt.setInt(6, i);
                        pstmt.setInt(7, i);
                        pstmt.executeUpdate();
                        pstmt.close();
                    }
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (theConn != null) {
                    theConn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void cadastrarMarcacoes() {
        try {
            theConn = getConnection();
            PreparedStatement pstmt = null;
            Statement stmt = null;
            Statement stmt2 = null;
            ResultSet rs = null;
            ResultSet rs2 = null;

            String sqlVinculo = "select regdatetime, pis from LOG03 order by regdatetime desc limit 10000";

            stmt = theConn.createStatement();
            rs = stmt.executeQuery(sqlVinculo);

            String dateStr;
            String pis;
            Integer userid;
            while (rs.next()) {
                dateStr = rs.getString("regdatetime");
                pis = rs.getString("pis");


                String sqlPis = "select * from USERINFO where pis = '" + pis + "'";

                stmt2 = c.createStatement();
                rs2 = stmt2.executeQuery(sqlPis);

                while (rs2.next()) {
                    userid = rs2.getInt("userid");
                    try {
                        String sql = "INSERT INTO CHECKINOUT values(?,?,?,?,?,?,?,?,?)";
                        pstmt = c.prepareStatement(sql);
                        pstmt.setInt(1, userid);
                        pstmt.setTimestamp(2, Timestamp.valueOf(dateStr));
                        pstmt.setString(3, "I");
                        pstmt.setInt(4, 0);
                        pstmt.setInt(5, 1);
                        pstmt.setString(6, null);
                        pstmt.setInt(7, 0);
                        pstmt.setString(8, null);
                        pstmt.setString(9, null);
                        pstmt.executeUpdate();
                    } catch (Exception e) {
                        System.out.println("Erro - Marcação já existente");
                    }
                }

            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (theConn != null) {
                    theConn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void cadastrarEmpregadores() {
        try {
            theConn = getConnection();
            PreparedStatement pstmt = null;
            Statement stmt = null;
            ResultSet rs = null;

            String sqlVinculo = "select companyname, identify, address, cei from company";

            stmt = theConn.createStatement();
            rs = stmt.executeQuery(sqlVinculo);

            String name;
            String cnpj;
            String address;
            Integer cei;
            while (rs.next()) {
                name = rs.getString("companyname");
                cnpj = rs.getString("identify");
                address = rs.getString("address");
                cei = rs.getInt("cei");
                try {
                    String sql = "INSERT INTO EMPREGADOR values(?,?,?,?)";
                    pstmt = c.prepareStatement(sql);
                    pstmt.setString(1, cnpj);
                    pstmt.setInt(2, cei);
                    pstmt.setString(3, name);
                    pstmt.setString(4, address);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    System.out.println("Erro - Empresa já existente");
                    String sql = "UPDATE empregador SET razao_social = ?, local = ?, cei = ? WHERE cnpj = ?";
                    pstmt.setString(1, name);
                    pstmt.setString(2, address);
                    pstmt.setInt(3, cei);
                    pstmt.setString(4, cnpj);
                    pstmt.executeUpdate();
                }


            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (theConn != null) {
                    theConn.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    public String getServidorDigitSender() {
        String servidor = "";

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select ipDigitSender from config";
            if (c.isClosed()) {
                Conectar();
            }
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                servidor = rs.getString("ipDigitSender");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        if (servidor == null) {
            servidor = "";
        }
        return servidor;
    }
    
    
}
