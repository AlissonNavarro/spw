package manageBean;

import entidades.CategoriaAfastamento;
import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaAfastamentoMB {

    AcessoBD con;

    public CategoriaAfastamentoMB() {
        con = new AcessoBD();
    }

    public List<CategoriaAfastamento> consultaCategoriaAfastamento() {
        List<CategoriaAfastamento> categoriaAfastamentoList = new ArrayList<CategoriaAfastamento>();
        try {
            String sql = "select * from categoriaAfastamento ORDER BY descAfastamento";
            ResultSet rs = con.executeQuery(sql);
            CategoriaAfastamento ca;
            while (rs.next()) {
                Integer cod_categoria = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String legendaAfastamento = rs.getString("legendaAfastamento");
                ca = new CategoriaAfastamento(cod_categoria, descAfastamento, legendaAfastamento);
                categoriaAfastamentoList.add(ca);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("CategoriaAfastamentoMB consultaCategoriaAfastamento " + ex);
        } finally {
            con.Desconectar();
        }
        return categoriaAfastamentoList;
    }

    public CategoriaAfastamento consultaCategoriaAfastamento(int categoriaAfastamentoId) {
        CategoriaAfastamento ca = new CategoriaAfastamento();
        try {
            String sql = "select * from CategoriaAfastamento where cod_categoria = " + categoriaAfastamentoId;
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                Integer cod_categoria = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String legendaAfastamento = rs.getString("legendaAfastamento");
                ca = new CategoriaAfastamento(cod_categoria, descAfastamento, legendaAfastamento);
            }
        } catch (SQLException ex) {
            System.out.println("CategoriaAfastamento consultaCatgoriaAfastamento " + ex);
        } finally {
            con.Desconectar();
        }
        return ca;
    }

    public void alterar(CategoriaAfastamento categoriaAfastamento) {
        try {
            String sql = "update categoriaAfastamento SET descAfastamento = ?, legendaAfastamento = ?"
                    + "where cod_categoria = ?";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, categoriaAfastamento.getDescricao());
                con.pstmt.setString(2, categoriaAfastamento.getLegenda());
                con.pstmt.setInt(3, categoriaAfastamento.getId());
                con.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("CategoriaAfastamentoMB alterar " + ex);
        } finally {
            con.Desconectar();
        }
    }

    public boolean excluir(int codCategoria) {
        boolean flag = true;
        try {
            String sql = "delete from categoriaAfastamento where cod_categoria = " + codCategoria;
            flag = con.executeUpdate(sql) > 0;
        } catch (Exception ex) {
            System.out.println("CategoriaAfastamento excluir " + ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean inserir(CategoriaAfastamento novaCategoria) {
        boolean flag = true;
        try {
            String sql = "select descAfastamento from CategoriaAfastamento where descAfastamento = ? ";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, novaCategoria.getDescricao());
                ResultSet rs = con.executeQuery();
                if (rs.next()) {
                    flag = false;
                }
                if (flag) {
                    sql = "insert into CategoriaAfastamento (descAfastamento,legendaAfastamento) VALUES (?,?)";
                    if (con.prepareStatement(sql)) {
                        con.pstmt.setString(1, novaCategoria.getDescricao());
                        con.pstmt.setString(2, novaCategoria.getLegenda());
                        con.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("CategoriaAfastamentoMB inserir "+ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }
}
