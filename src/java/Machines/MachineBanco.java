/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Machines;

import Metodos.Metodos;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;
import javax.faces.model.SelectItem;

/**
 *
 * @author Pedro
 */
public class MachineBanco implements Serializable {

    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Machines: Conectar: " + cnfe);
            //System.out.println(cnfe);
        }
    }

    public MachineBanco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("Machines: MachineBanco 1: " + ex);
            //Logger.getLogger(MachineBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addRelogio(Machine mac) {

        PreparedStatement pstmt = null;
        try {

            String query = "INSERT INTO rep (repAlias, repIp, repPort, repTipo, repAtivo) VALUES (?,?,?,?,?) ";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, mac.getRepAlias());
            pstmt.setString(2, mac.getRepIp());
            pstmt.setInt(3, mac.getRepPort());
            pstmt.setInt(4, mac.getRepType());
            pstmt.setBoolean(5, mac.getRepAtivo());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public void editRelogio(Machine mac) {

        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE rep SET repAlias = ?, repIp = ?, repPort = ?, repTipo = ?, repAtivo = ?, nfr = ?, "
                    + " model = ?, local = ?, idCompany = ?, commType = ?, biometricModule = ?, lastNSR = ?, "
                    + " pathCollectFile = ?, nsr = ?, ipRoater = ?, mask = ?, senha = ? WHERE repId = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, mac.getRepAlias());
            pstmt.setString(2, mac.getRepIp());
            pstmt.setInt(3, mac.getRepPort());
            pstmt.setInt(4, mac.getRepType());
            pstmt.setBoolean(5, mac.getRepAtivo());
            pstmt.setString(6, mac.getNfr());
            pstmt.setInt(7, mac.getModel());
            pstmt.setString(8, mac.getLocal());
            pstmt.setInt(9, mac.getIdCompany());
            pstmt.setInt(10, mac.getCommType());
            pstmt.setInt(11, mac.getBiometricModule());
            pstmt.setInt(12, mac.getLastNSR());
            pstmt.setString(13, mac.getPathCollectFile());
            pstmt.setString(14, mac.getNsr());
            pstmt.setString(15, mac.getIpRoater());
            pstmt.setString(16, mac.getMask());
            pstmt.setString(17, mac.getSenha());
            pstmt.setInt(18, mac.getRepId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public void deleteRelogio(Machine mac) {

        PreparedStatement pstmt = null;
        try {

            String query = "DELETE FROM rep WHERE repId = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, mac.getRepId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public Machine consultaRelogio(int mac_id) {
        Machine mac = new Machine();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from rep where repId = " + mac_id;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer id = rs.getInt("repid");
                String mac_alias = rs.getString("repAlias");
                String ip = rs.getString("repIp");
                Integer serialPort = rs.getInt("repPort");
                Integer repType = rs.getInt("repTipo");
                Boolean enable = rs.getBoolean("repAtivo");
                String nfr = rs.getString("nfr");
                Integer model = rs.getInt("model");
                String local = rs.getString("local");
                Integer idCompany = rs.getInt("idCompany");
                Integer commType = rs.getInt("commType");
                Integer biometricModule = rs.getInt("biometricModule");
                Timestamp dateLastNSR = null;
                Integer lastNSR = rs.getInt("lastNSR");
                String pathCollectFile = rs.getString("pathCollectFile");
                String nsr = rs.getString("nsr");
                String ipRoater = rs.getString("ipRoater");
                String mask = rs.getString("mask");
                String senha = rs.getString("senha");
                MachineType macType = consultaTipoRelogio(repType);
                mac = (new Machine(id, mac_alias, ip, serialPort, repType, enable, nfr, model, local, idCompany, commType,
                        biometricModule, dateLastNSR, lastNSR, pathCollectFile, nsr, ipRoater, mask, senha, macType));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mac;
    }

    public List<Machine> consultaRelogios() {
        List<Machine> saida = new ArrayList<Machine>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql = " select * from rep order by repAlias";

            if (c.isClosed()) {
                Conectar();
            }

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer id = rs.getInt("repid");
                String mac_alias = rs.getString("repAlias");
                String ip = rs.getString("repIp");
                Integer serialPort = rs.getInt("repPort");
                Integer repType = rs.getInt("repTipo");
                Boolean enable = rs.getBoolean("repAtivo");
                String nfr = rs.getString("nfr");
                Integer model = rs.getInt("model");
                String local = rs.getString("local");
                Integer idCompany = rs.getInt("idCompany");
                Integer commType = rs.getInt("commType");
                Integer biometricModule = rs.getInt("biometricModule");
                Timestamp dateLastNSR = null;
                Integer lastNSR = rs.getInt("lastNSR");
                String pathCollectFile = rs.getString("pathCollectFile");
                String nsr = rs.getString("nsr");
                String ipRoater = rs.getString("ipRoater");
                String mask = rs.getString("mask");
                String senha = rs.getString("senha");
                MachineType macType = consultaTipoRelogio(repType);
                saida.add(new Machine(id, mac_alias, ip, serialPort, repType, enable, nfr, model, local, idCompany, commType,
                        biometricModule, dateLastNSR, lastNSR, pathCollectFile, nsr, ipRoater, mask, senha, macType));
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
        return saida;
    }

    public MachineType consultaTipoRelogio(int tipoId) {
        MachineType macType = new MachineType();
        try {
            ResultSet rs;
            Statement stmt;
            String sql = "select * from repTipo where repTipoId = " + tipoId;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer id = rs.getInt("repTipoId");
                String repMarca = rs.getString("repMarca");
                Boolean nfrOn = rs.getBoolean("nfrOn");
                Boolean modelOn = rs.getBoolean("modelOn");
                Boolean localOn = rs.getBoolean("localOn");
                Boolean idCompanyOn = rs.getBoolean("idCompanyOn");
                Boolean commTypeOn = rs.getBoolean("commTypeOn");
                Boolean biometricModuleOn = rs.getBoolean("biometricModuleOn");
                Boolean dateLastNSROn = rs.getBoolean("dateLastNSROn");
                Boolean lastNSROn = rs.getBoolean("lastNSROn");
                Boolean pathCollectFileOn = rs.getBoolean("pathCollectFileOn");
                Boolean nsrOn = rs.getBoolean("nsrOn");
                Boolean ipRoaterOn = rs.getBoolean("ipRoaterOn");
                Boolean maskOn = rs.getBoolean("maskOn");
                Boolean senhaOn = rs.getBoolean("senhaOn");
                macType = (new MachineType(id, repMarca, nfrOn, modelOn, localOn, idCompanyOn, commTypeOn, biometricModuleOn, dateLastNSROn,
                        lastNSROn, pathCollectFileOn, nsrOn, ipRoaterOn, maskOn, senhaOn));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return macType;
    }

    public List<SelectItem> consultaTiposRelogio() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        try {
            if (c.isClosed()) {
                Conectar();
            }
            ResultSet rs;
            Statement stmt;
            String sql = "select * from repTipo ";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            saida.add(new SelectItem(0, "Selecione uma marca"));
            while (rs.next()) {
                Integer id = rs.getInt("repTipoId");
                String repMarca = rs.getString("repMarca");
                SelectItem SelectedMarca = new SelectItem(id, repMarca);
                saida.add(SelectedMarca);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return saida;
    }

    //Verifica se o NFR existe
    public int consultaRelogioIdByNFR(String nfr) {
        Integer id = 0;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            ResultSet rs;
            Statement stmt;

            String sql = "select repid from rep where nfr = '" + nfr + "'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt("repid");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    //Cadastra marcação do AFD
    public void cadastraCheckInOut(String userPis, Timestamp dateStr, int rep) {
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }

            if (userPis.length() == 12) {
                if (userPis.substring(0, 1).equals("0")) {
                    userPis = userPis.substring(1);
                }
            }

            String sql = "select userid from USERINFO where pis like '%" + userPis + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            int id = 0;

            if (rs.next()) {
                id = rs.getInt("userid");
                sql = "INSERT INTO CHECKINOUT values(?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setTimestamp(2, dateStr);
                pstmt.setString(3, "I");
                pstmt.setInt(4, 0);
                pstmt.setInt(5, 1);
                pstmt.setInt(6, 0);
                pstmt.setString(7, null);
                pstmt.setString(8, null);
                pstmt.setString(9, null);
                pstmt.setString(10, null);
                if (rep != 0) {
                    pstmt.setInt(11, rep);
                } else {
                    pstmt.setString(11, null);
                }

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            //System.out.println("Erro:" + e);
            //System.out.println(userPis + " " + dateStr);
            // System.out.println();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void criaDepartments() {
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }

            String sql = "select deptid from departments where deptname = 'Coletado via AFD'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                sql = "insert into departments (deptname, supdeptid) values ('Coletado via AFD', 1)";
                pstmt = c.prepareStatement(sql);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public void cadastraUserRepToBanco(String status, String pis, String nome, int rep) {
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }

            if (pis.length() == 12) {
                if (pis.substring(0, 1).equals("0")) {
                    pis = pis.substring(1);
                }
            }

            String sql = "";

            if (status.toUpperCase().equals("I")) {
                sql = "select userid from USERINFO where pis like '%" + pis + "'";
                stmt = c.createStatement();
                //System.out.print(pis+"       "+ nome);
                rs = stmt.executeQuery(sql);
                if (!rs.next()) {
                    sql = "insert into userinfo (pis, name, badgenumber, cod_regime, perfil, ativo, livreacesso, defaultdeptid) values "
                            + "(?,?,?,0,(select cod_perfil from perfil where nome_perfil = 'PADRÃO'),0,0,(select deptid from departments where deptname = 'Coletado via AFD'))";
                    pstmt = c.prepareStatement(sql);
                    pstmt.setString(1, pis);
                    pstmt.setString(2, nome);
                    pstmt.setString(3, pis);
                    pstmt.executeUpdate();
                    //System.out.print(" nao cadastrado");
                }
                //System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void Desonectar() {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
        }
    }

    //atualiza o ultimo NSR do rep
    public void updateLastNSR(int repId, int lastNSR) {

        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            String query = "UPDATE rep SET lastNSR = ? WHERE repId = ?";

            /*if (c.isClosed()) {
             Conectar();
             }*/
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, lastNSR);
            pstmt.setInt(2, repId);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }
}
