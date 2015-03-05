package manageBean;

import entidades.Cargo;
import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class CargoMB implements Serializable {

    private AcessoBD con;
    // Cargo Hieraquico
    //List<Integer> deptList = new ArrayList<Integer>();

    public CargoMB() {
        con = new AcessoBD();
    }

    public int inserir(Cargo cargo) {
        int flag = 0;
        try {
            String sql = "select nome from Cargo where nome = '" + cargo.getNome() + "'";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                flag = 1;
            }
            rs.close();

            if (flag == 0) {
                String query = "insert into Cargo(Nome) values(?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, cargo.getNome());
                    con.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println("CargoMB inserir " + ex.getMessage());
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public int alterar(Cargo cargo) {
        int flag = 0;
        try {
            String sql = "select nome from Cargo "
                    + " WHERE     (cod_cargo <> " + cargo.getId() + ") AND (nome = '" + cargo.getNome() + "') ";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                flag = 1;
            }
            rs.close();
            if (flag == 0) {
                String query = "UPDATE Cargo SET nome = ? WHERE cod_cargo = ?";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, cargo.getNome());
                    con.pstmt.setInt(2, cargo.getId());
                    con.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println("CargoMB alterar " + ex.getMessage());
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean excluir(int codCargo) {
        con.prepareStatement("delete from Cargo where cod_cargo = " + codCargo);
        int r = con.executeUpdate();
        con.Desconectar();
        return (r > 0);
    }

    public Cargo consultar(int codCargo) {
        Cargo c = new Cargo();
        try {
            String sql = "select nome from Cargo where cod_cargo = " + codCargo;
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                String nome = rs.getString("nome");
                c.setId(codCargo);
                c.setNome(nome);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("CargoMB consultaCargoOrdernado " + ex.getMessage());
        } finally {
            con.Desconectar();
        }
        return c;
    }

    public Cargo consultar(String nome) {
        Cargo c = new Cargo();
        try {
            String sql = "select * from Cargo where nome = ? ";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, nome);
                ResultSet rs = con.executeQuery();
                if (rs.next()) {
                    Integer id = rs.getInt("cod_cargo");
                    nome = rs.getString("nome");
                    c.setId(id);
                    c.setNome(nome);
                }
                rs.close();
            }
        } catch (Exception ex) {
            System.out.println("CargoMB consultaCargoOrdernado " + ex.getMessage());
        } finally {
            con.Desconectar();
        }
        return c;
    }

    public List<SelectItem> listar() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        try {
            String sql = "select cod_cargo,nome from Cargo ORDER BY nome ";
            ResultSet rs = con.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione o cargo"));

            while (rs.next()) {
                Integer codCargo = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                saida.add(new SelectItem(codCargo, nome));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("CargoMB consultaCargoOrdernado " + ex.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }
}
