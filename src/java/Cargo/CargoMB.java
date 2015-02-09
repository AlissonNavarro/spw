package Cargo;

import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class CargoMB implements Serializable {

    private AcessoBD con;
    // Cargo Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public CargoMB() {
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
            System.out.println("Cargo Banco consultaCargoOrdernado " + ex.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public Integer salvarNovoCargo(String novoCargoNome) {
        int flag = 0;
        try {
            String sql = "select nome from Cargo where nome = '" + novoCargoNome + "'";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
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
        } catch (SQLException ex) {
            System.out.println("Cargo Banco salvarNovoCargo " + ex.getMessage());
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
                    + " WHERE     (cod_cargo <> " + cargo.getId() + ") AND (nome = '" + cargo.getNomeCargo() + "') ";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
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
        } catch (SQLException ex) {
            System.out.println("Cargo Banco salvarEditCargo " + ex.getMessage());
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean excluirCargo(Integer cargoSelecionado) {
            con.prepareStatement("delete from Cargo where cod_cargo = " + cargoSelecionado);
            int r = con.executeUpdate();
            con.Desconectar();
            return (r > 0);
    }
}
