package manageBean;

import comunicacao.AcessoBD;
import entidades.Verba;
import java.sql.ResultSet;

public class VerbaMB {

    private AcessoBD con;

    public VerbaMB() {
        con = new AcessoBD();
    }

    public Verba consultaVerba() {
        Verba verba = new Verba();
        try {
            String sql = "Select * from verba";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                Integer empresa = rs.getInt("empresa");
                Integer adicionalNoturno = rs.getInt("adicionalNoturno");
                Integer atrasos = rs.getInt("atrasos");
                Integer atrasosMenorHora = rs.getInt("atrasosMenorHora");
                Integer atrasosMaiorHora = rs.getInt("atrasosMaiorHora");
                Integer feriadoCritico = rs.getInt("FeriadoCritico");
                Integer dsr = rs.getInt("DSR");
                Integer faltas = rs.getInt("Faltas");
                verba = new Verba(empresa, adicionalNoturno, atrasos, atrasosMenorHora, atrasosMaiorHora, feriadoCritico, dsr, faltas);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return verba;
    }

    public boolean alterar(Verba verba) {
        boolean flag = false;
        try {
            String sql = "update verba set empresa=?,adicionalNoturno=?, atrasos=?, atrasosMenorHora=?, atrasosMaiorHora=?, FeriadoCritico=?,"
                    + " DSR=?,Faltas=?";
            if (con.prepareStatement(sql)) {
                con.pstmt.setInt(1, verba.getEmpresa());
                con.pstmt.setInt(2, verba.getAdicionalNoturno());
                con.pstmt.setInt(3, verba.getAtrasos());
                con.pstmt.setInt(4, verba.getAtrasosMenorHora());
                con.pstmt.setInt(5, verba.getAtrasosMaiorHora());
                con.pstmt.setInt(6, verba.getFeriadoCritico());
                con.pstmt.setInt(7, verba.getDsr());
                con.pstmt.setInt(8, verba.getFaltas());
                con.executeUpdate();
                flag = true;
            }
        } catch (Exception ex) {
            flag = false;
            System.out.println("VerbaMB alterar "+ex);
        } finally {
            con.Desconectar();
        }
        return flag;
    }
}
