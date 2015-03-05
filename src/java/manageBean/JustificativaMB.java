package manageBean;

import entidades.Justificativa;
import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JustificativaMB {

    private AcessoBD con;

    public JustificativaMB() {
        con = new AcessoBD();
    }

    public List<Justificativa> consultaJustificativas() {
        List<Justificativa> justificativaList = new ArrayList<Justificativa>();
        try {
            String sql = "select * from LeaveClass ORDER BY leavename";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                Boolean soAdministrador = rs.getBoolean("administrador");
                Boolean total = rs.getBoolean("total");
                Justificativa justificativa = new Justificativa();
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
                justificativa.setSoAdministrador(soAdministrador);
                justificativa.setIsTotal(total);
                justificativaList.add(justificativa);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("JustificativaMB consultaJustificativas " + ex);
        } finally {
            con.Desconectar();
        }
        return justificativaList;
    }

    public Justificativa consultaJustificativa(int justificativaId) {
        Justificativa justificativa = new Justificativa();
        try {
            String sql = "select * from LeaveClass where leaveid = " + justificativaId;
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                Boolean soAdministrador = rs.getBoolean("administrador");
                Boolean total = rs.getBoolean("total");
                Boolean descricaoObrigatoria = rs.getBoolean("descricaoObrigatoria");
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
                justificativa.setIsTotal(total);
                justificativa.setSoAdministrador(soAdministrador);
                justificativa.setIsDescricaoObrigatoria(descricaoObrigatoria);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("JustificativaMB consultaJustificativa " + ex);
        } finally {
            con.Desconectar();
        }
        return justificativa;
    }

    public boolean alterar(Justificativa justificativa) {
        boolean flag = false;
        try {
            String query = "update LeaveClass SET leaveName = ?, administrador = ?, total = ?, descricaoObrigatoria = ? "
                    + "where leaveid = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, justificativa.getJustificativaNome());
                con.pstmt.setBoolean(2, justificativa.getSoAdministrador());
                con.pstmt.setBoolean(3, justificativa.getIsTotal());
                con.pstmt.setBoolean(4, justificativa.getIsDescricaoObrigatoria());
                con.pstmt.setInt(5, justificativa.getJustificativaID());
                flag = con.executeUpdate() > 0;
            }

        } catch (Exception ex) {
            System.out.println("JustificativaMB alterar " + ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean excluir(int justificativaId) {
        boolean flag = true;
        try {
            String sql = "delete from LeaveClass where leaveid = " + justificativaId;
            flag = con.executeUpdate(sql) > 0;
        } catch (Exception ex) {
            System.out.println("JustificativaMB excluir " + ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean inserir(Justificativa novaJustificativa) {
        boolean flag = true;
        try {
            String sql = "select leaveName from LeaveClass where leaveName = ?";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, novaJustificativa.getJustificativaNome());
                ResultSet rs = con.executeQuery();
                if (rs.next()) {
                    flag = false;
                }
                if (flag) {
                    sql = "insert into LeaveClass (leavename,administrador,total,descricaoObrigatoria)  VALUES (?,?,?,?)";
                    if (con.prepareStatement(sql)) {
                        con.pstmt.setString(1, novaJustificativa.getJustificativaNome());
                        con.pstmt.setBoolean(2, novaJustificativa.getSoAdministrador());
                        con.pstmt.setBoolean(3, novaJustificativa.getIsTotal());
                        con.pstmt.setBoolean(4, novaJustificativa.getIsDescricaoObrigatoria());
                        con.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("JustificativaMB inserir "+ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }
}
