package AFD;

import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Banco {
    
    AcessoBD con;

    public Banco(Boolean isMain) {
        con = new AcessoBD();
    }

    public void atualizaMarcacoes(RegistroMarcacaoDePonto registro) {
        Date marcacao = registro.getDataEHoraMarcacao();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String marcacaoStr = sdf.format(marcacao);
        String pis = registro.getPis();
        boolean ok = true;
        try {
            String query = "SELECT     USERID FROM USERINFO WHERE     (PIS = ?)";
            con.prepareStatement(query);
            con.pstmt.setString(1, pis);
            ResultSet rs = con.executeQuery();
            Integer userid = null;
            while (rs.next()) {
                userid = rs.getInt("userid");
            }
            
            query = "INSERT INTO CHECKINOUT values(?,?,?,?,?,?,?,?)";
            con.prepareStatement(query);
            con.pstmt.setInt(1, userid);
            con.pstmt.setString(2, marcacaoStr);
            con.pstmt.setString(3, "I");
            con.pstmt.setInt(4, 0);
            con.pstmt.setInt(5, 0);
            con.pstmt.setString(6, null);
            con.pstmt.setInt(7, 0);
            con.pstmt.setString(8, null);
            con.pstmt.executeUpdate();
            
        } catch (Exception e) {
            ok = false;
            System.out.println("Erro: " + e);
        } finally {
            con.Desconectar();
            if (ok) {
                System.out.println("Marcação - Pis: "+registro.getPis()+" hora: "+marcacaoStr);
            }
        }
    }

    public void atualizaUsuarios(RegistroEmpregado registroEmpregado) {
        boolean ok = true;
        String pis = registroEmpregado.getPis();
        char tipo = registroEmpregado.getTipoDaOperacao();
        String nome = registroEmpregado.getNomeEmpregado();
        try {
            
            String query;
            switch (tipo) {
                case 'I':
                    query = "INSERT INTO USERINFO (name,BADGENUMBER,SSN,PIS) values(?,?,?,?)";
                    con.prepareStatement(query);
                    con.pstmt.setString(1, nome);
                    con.pstmt.setString(2, "PIS"+pis);
                    con.pstmt.setString(3, "PIS"+pis);
                    con.pstmt.setString(4, pis);
                    con.pstmt.executeUpdate();
                    
                    break;
                case 'A':
                    query = "update USERINFO set name = ? where pis = ?";
                    con.prepareStatement(query);
                    con.pstmt.setString(1, nome);
                    con.pstmt.setString(2, pis);
                    con.pstmt.executeUpdate();
                    
                    break;
                case 'E':
                    query = "delete from USERINFO where pis = ?";
                    con.prepareStatement(query);
                    con.pstmt.setString(1, pis);
                    con.pstmt.executeUpdate();
                    
                    break;
            }
        } catch (Exception e) {
            ok=false;
            System.out.println("Erro: " + e);
        } finally{
            con.Desconectar();
            if(ok){
                System.out.println("Alteracao usuario - Operação: "+(tipo=='I'?"Inclusão":((tipo=='A')?"Alteração":"Exclusão"))+" Nome: "+nome);
            }
        }
    }
    
}
