package manageBean;

import entidades.Empresa;
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

    public boolean inserir(Empresa empresa) {
        String query = "insert into empregador (CNPJ,RAZAO_SOCIAL, LOCAL, CEI) values (?,?,?,?)";
        try {
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, empresa.getCnpj());
                con.pstmt.setString(2, empresa.getRazaoSocial());
                con.pstmt.setString(3, empresa.getEndereco());
                con.pstmt.setInt(4, empresa.getCei());
                con.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB inserir " + ex);
        } finally {
            con.Desconectar();;
        }
        return false;
    }

    public boolean alterar(Empresa empresa) {
        String query = "update empregador set CNPJ=?, RAZAO_SOCIAL=?, LOCAL=?, CEI=? WHERE ID=?";
        try {
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, empresa.getCnpj());
                con.pstmt.setString(2, empresa.getRazaoSocial());
                con.pstmt.setString(3, empresa.getEndereco());
                con.pstmt.setInt(4, empresa.getCei());
                con.pstmt.setInt(5, empresa.getId());
                con.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB alterar " + ex);
        } finally {
            con.Desconectar();;
        }
        return false;
    }

    public boolean excluir(int idEmpresa) {
        con.prepareStatement("delete from empregador where id = " + idEmpresa);
        int r = con.executeUpdate();
        con.Desconectar();
        return (r > 0);
    }

    public Empresa consultarEmpresaPorId(int id) {
        Empresa e = new Empresa();
        try {
            String sql = "select * from empregador where id = " + id;
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                e.setId(rs.getInt("id"));
                e.setEndereco(rs.getString("local"));
                e.setCnpj(rs.getString("cnpj"));
                e.setRazaoSocial(rs.getString("razao_social"));
                e.setCei(rs.getInt("cei"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("EmpresaMB consultarEmpresaPorId " + ex);
        } finally {
            con.Desconectar();
        }
        return e;
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

    public Empresa consultarEmpresaPorCnpj(String cnpj) {
        Empresa emp = new Empresa();
        try {
            String sql = "SELECT * FROM  EMPREGADOR  WHERE (CNPJ = ?)";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, cnpj);
                ResultSet rs = con.executeQuery();
                if (rs.next()) {
                    Integer id = rs.getInt("id");
                    String cnpj_ = rs.getString("cnpj");
                    String razaoSocial = rs.getString("razao_social");
                    String endereco = rs.getString("local");
                    Integer cei = rs.getInt("cei");
                    emp = new Empresa(id, cnpj_, razaoSocial, endereco, cei);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            System.out.println("EmpresaMB consultaDetalhesEmpresa " + ex);
        } finally {
            con.Desconectar();
        }
        return emp;
    }

}
