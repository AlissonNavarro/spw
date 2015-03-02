package backup;

import Metodos.Metodos;
import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Banco {

    AcessoBD con;
    
    public Banco() {
        con = new AcessoBD();
    }

    public String buscaInstanciaSql() {
        String retorno = "";
        try {
            ResultSet rs = con.executeQuery("select @@servername as instancia");
            if (rs.next()) {
                retorno = rs.getString("instancia");
                retorno = retorno.substring(retorno.indexOf("\\")+1);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            con.Desconectar();
        }
        return retorno;
    }

    public BackupConfig getBackupConfig() {

        BackupConfig backupConfig = new BackupConfig();
        try {

            String query = "Select * from Backup_Config";
            ResultSet rs = con.executeQuery(query);

            while (rs.next()) {

                String empresa = rs.getString("empresa");
                String caminho = rs.getString("caminho");
                String port = rs.getString("port");
                String remetente = rs.getString("remetente");
                String destinatario = rs.getString("destinatario");
                String senha = rs.getString("senha");
                String smtp = rs.getString("smtp");
                String horaBackup = rs.getString("horaBackup");
                Integer horas = rs.getInt("horas");
                Boolean ssl = rs.getBoolean("ssl");
                Boolean alteracao = rs.getBoolean("alteracao");

                backupConfig.setEmpresa(empresa);
                backupConfig.setCaminho(caminho);
                backupConfig.setPort(port);
                backupConfig.setRemetente(remetente);
                backupConfig.setDestinatario(destinatario);
                backupConfig.setSenha(senha);
                backupConfig.setSmtp(smtp);
                backupConfig.setIntervaloBackup(horas);
                backupConfig.setSsl(ssl);
                backupConfig.setHoraBackup(horaBackup);
                backupConfig.setDiferencial(alteracao);
                backupConfig.setIp(Metodos.getServerConexao());
                backupConfig.setUser(Metodos.getUserConexao());
                backupConfig.setPass(Metodos.getPasswordConexao());
                backupConfig.setDatabase(Metodos.getDatabaseConexao());

            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return backupConfig;
    }

    public boolean updateBackupConfig(BackupConfig backupConfig) throws SQLException {
        boolean ok = true;
        String query = "Select * from Backup_Config";
        ResultSet rs = con.executeQuery(query);
        Boolean isBancoVazio = true;
        while (rs.next()) {
            isBancoVazio = false;
        }

        if (isBancoVazio) {

            query = "insert into Backup_Config(empresa,caminho,port,remetente,destinatario,senha,smtp,ssl,horas,horaBackup,instancia,alteracao) values(?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                con.prepareStatement(query);
                con.pstmt.setString(1, backupConfig.getEmpresa());
                con.pstmt.setString(2, backupConfig.getCaminho());
                con.pstmt.setString(3, backupConfig.getPort());
                con.pstmt.setString(4, backupConfig.getRemetente());
                con.pstmt.setString(5, backupConfig.getDestinatario());
                con.pstmt.setString(6, backupConfig.getSenha());
                con.pstmt.setString(7, backupConfig.getSmtp());
                if (backupConfig.getSsl()) {
                    con.pstmt.setInt(8, 1);
                } else {
                    con.pstmt.setInt(8, 0);
                }
                con.pstmt.setInt(9, backupConfig.getIntervaloBackup());
                con.pstmt.setString(10, backupConfig.getHoraBackup());
                con.pstmt.setString(11, backupConfig.getInstancia());
                if (backupConfig.getDiferencial()) {
                    con.pstmt.setInt(12, 1);
                } else {
                    con.pstmt.setInt(12, 0);
                }

                con.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e);
                ok = false;
            } finally {
                con.Desconectar();
            }
        } else {

            query = "update Backup_Config set empresa=?, caminho=?, port=?, remetente=?, destinatario=?,"
                    + " senha=?,smtp=?, ssl=?,horas=?,horaBackup=?,instancia=?,alteracao=?";

            try {
                con.prepareStatement(query);
                con.pstmt.setString(1, backupConfig.getEmpresa());
                con.pstmt.setString(2, backupConfig.getCaminho());
                con.pstmt.setString(3, backupConfig.getPort());
                con.pstmt.setString(4, backupConfig.getRemetente());
                con.pstmt.setString(5, backupConfig.getDestinatario());
                con.pstmt.setString(6, backupConfig.getSenha());
                con.pstmt.setString(7, backupConfig.getSmtp());
                if (backupConfig.getSsl()) {
                    con.pstmt.setString(8, "1");
                } else {
                    con.pstmt.setString(8, "0");
                }
                con.pstmt.setInt(9, backupConfig.getIntervaloBackup());
                con.pstmt.setString(10, backupConfig.getHoraBackup());
                con.pstmt.setString(11, backupConfig.getInstancia());
                if (backupConfig.getDiferencial()) {
                    con.pstmt.setInt(12, 1);
                } else {
                    con.pstmt.setInt(12, 0);
                }

                con.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e);;
                ok = false;
            } finally {
                con.Desconectar();
            }
        }
        return ok;
    }
}
