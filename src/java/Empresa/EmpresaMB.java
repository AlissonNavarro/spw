package Empresa;

import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class EmpresaMB implements Serializable {

    AcessoBD con;

    public EmpresaMB() {
        con = new AcessoBD();
    }

    public boolean salvarEmpresa(String razaoSocial, String cnpj, String endereco, Integer cei) {
        String query = "insert into empregador (CNPJ,RAZAO_SOCIAL, LOCAL, CEI) values (?,?,?,?)";
        try {
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, cnpj);
                con.pstmt.setString(2, razaoSocial);
                con.pstmt.setString(3, endereco);
                con.pstmt.setInt(4, cei);
                con.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB updateEmpresa " + ex);
        } finally {
            con.Desconectar();;
        }
        return false;
    }

    public boolean updateEmpresa(String razaoSocial, String cnpj, String endereco, Integer id) {
        String query = "update empregador set CNPJ=?, RAZAO_SOCIAL=?, LOCAL=? WHERE ID=?";
        try {
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, cnpj);
                con.pstmt.setString(2, razaoSocial);
                con.pstmt.setString(3, endereco);
                con.pstmt.setInt(4, id);
                con.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB updateEmpresa " + ex);
        } finally {
            con.Desconectar();;
        }
        return false;
    }

    public List<SelectItem> consultaEmpresaOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();

        try {
            String sql = "select id, razao_social from Empregador ORDER BY razao_social ";
            ResultSet rs = con.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione a empresa"));

            while (rs.next()) {
                String id = rs.getString("id");
                String razao_social = rs.getString("razao_social");
                saida.add(new SelectItem(id, razao_social));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("EmpresaMB consultaEmpresaOrdernado " + ex);
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public Empresa consultaDetalhesEmpresa(int item) {
        Empresa emp = new Empresa();
        try {
            String sql = "SELECT * FROM  EMPREGADOR  WHERE (CNPJ = '" + item + "')";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String cnpj = rs.getString("cnpj");
                String razaoSocial = rs.getString("razao_social");
                String address = rs.getString("local");
                Integer cei = rs.getInt("cei");
                Integer id = rs.getInt("id");
                emp = new Empresa(cnpj, razaoSocial, address, cei, id);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("EmpresaMB consultaDetalhesEmpresa " + ex);
        } finally {
            con.Desconectar();
        }
        return emp;
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

    public boolean excluirEmpresa(int empresaSelecionada) {
        con.prepareStatement("delete from empregador where id = " + empresaSelecionada);
        int r = con.executeUpdate();
        con.Desconectar();
        return (r > 0);
    }

}
