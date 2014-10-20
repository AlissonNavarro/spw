package backup;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amsgama
 */
public class Banco {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("backup: Conectar: " + cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("backup: Banco: " + ex);
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String buscaInstanciaSql() {
        String retorno = "";
        PreparedStatement pstmt = null;
        try {
            pstmt = c.prepareStatement("select @@servername as instancia");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retorno = rs.getString("instancia");
                retorno = retorno.substring(retorno.indexOf("\\")+1);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    public BackupConfig getBackupConfig() {

        BackupConfig backupConfig = new BackupConfig();

        PreparedStatement pstmt = null;
        try {

            String query = "Select * from Backup_Config";

            pstmt = c.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

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
            System.out.println(e.getMessage());

        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return backupConfig;
    }

    public boolean updateBackupConfig(BackupConfig backupConfig) throws SQLException {
        boolean ok = true;
        PreparedStatement pstmt = null;

        String query = "Select * from Backup_Config";

        pstmt = c.prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        Boolean isBancoVazio = true;
        while (rs.next()) {
            isBancoVazio = false;
        }

        if (isBancoVazio) {

            query = "insert into Backup_Config(empresa,caminho,port,remetente,destinatario,senha,smtp,ssl,horas,horaBackup,instancia,alteracao) values(?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, backupConfig.getEmpresa());
                pstmt.setString(2, backupConfig.getCaminho());
                pstmt.setString(3, backupConfig.getPort());
                pstmt.setString(4, backupConfig.getRemetente());
                pstmt.setString(5, backupConfig.getDestinatario());
                pstmt.setString(6, backupConfig.getSenha());
                pstmt.setString(7, backupConfig.getSmtp());
                if (backupConfig.getSsl()) {
                    pstmt.setInt(8, 1);
                } else {
                    pstmt.setInt(8, 0);
                }
                pstmt.setInt(9, backupConfig.getIntervaloBackup());
                pstmt.setString(10, backupConfig.getHoraBackup());
                pstmt.setString(11, backupConfig.getInstancia());
                if (backupConfig.getDiferencial()) {
                    pstmt.setInt(12, 1);
                } else {
                    pstmt.setInt(12, 0);
                }

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                ok = false;
            } finally {
                try {
                    if (c != null) {
                        pstmt.close();
                        c.close();
                    }
                } catch (SQLException e) {
                }
            }
        } else {

            query = "update Backup_Config set empresa=?, caminho=?, port=?, remetente=?, destinatario=?,"
                    + " senha=?,smtp=?, ssl=?,horas=?,horaBackup=?,instancia=?,alteracao=?";

            try {
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, backupConfig.getEmpresa());
                pstmt.setString(2, backupConfig.getCaminho());
                pstmt.setString(3, backupConfig.getPort());
                pstmt.setString(4, backupConfig.getRemetente());
                pstmt.setString(5, backupConfig.getDestinatario());
                pstmt.setString(6, backupConfig.getSenha());
                pstmt.setString(7, backupConfig.getSmtp());
                if (backupConfig.getSsl()) {
                    pstmt.setString(8, "1");
                } else {
                    pstmt.setString(8, "0");
                }
                pstmt.setInt(9, backupConfig.getIntervaloBackup());
                pstmt.setString(10, backupConfig.getHoraBackup());
                pstmt.setString(11, backupConfig.getInstancia());
                if (backupConfig.getDiferencial()) {
                    pstmt.setInt(12, 1);
                } else {
                    pstmt.setInt(12, 0);
                }

                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                ok = false;
            } finally {
                try {
                    if (c != null) {
                        pstmt.close();
                        c.close();
                    }
                } catch (SQLException e) {
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
    }
}
