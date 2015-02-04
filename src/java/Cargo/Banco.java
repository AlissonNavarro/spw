package Cargo;

import Comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

class Banco implements Serializable {

    AcessoBD con;
    // Cargo Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public Banco() {
        con = new AcessoBD();
    }

    public List<SelectItem> consultaCargoOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        try {
            String sql = "select cod_cargo,nome from Cargo ORDER BY nome ";

            ResultSet rs = con.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione o cargo"));

            while (rs.next()) {
                Integer cargoID = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                saida.add(new SelectItem(cargoID, nome));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public int salvarNovoCargo(String novoCargoNome) {
        int flag = 0;
        try {
            String sql = "select nome from Cargo "
                    + " where nome = '" + novoCargoNome + "'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = 1;
            }
            rs.close();

            if (flag == 0) {
                String query = "insert into Cargo(Nome) values(?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, novoCargoNome);
                    con.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Integer salvarEditCargo(Cargo cargo) {
        int flag = 0;

        try {
            String sql = "select nome from Cargo "
                    + " WHERE (cod_cargo <> " + cargo.getId() + ") AND (nome = '" + cargo.getNomeCargo() + "') ";

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = 1;
            }
            rs.close();

            if (flag == 0) {
                String query = "UPDATE Cargo SET nome = ? WHERE cod_cargo = ?";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, cargo.getNomeCargo());
                    con.pstmt.setInt(2, cargo.getId());
                    con.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean excluirCargo(Integer cargoSelecionado) {
        String queryDelete = "delete from Cargo where cod_cargo = " + cargoSelecionado;
        return con.executeUpdate(queryDelete) == 1;
    }
}
