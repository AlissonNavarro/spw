/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;


/**
 *
 * @author amsgama
 */
public class BackupConfig {

    private String empresa;
    private String caminho;
    private String smtp;
    private String port;
    private String remetente;
    private String destinatario;
    private String senha;
    private Boolean ssl;
    private Boolean diferencial;
    private Integer intervaloBackup;
    private String horaBackup;
    private String instancia;
    private String ip;
    private String user;
    private String pass;
    private String database;

    public BackupConfig(String empresa, String caminho, String smtp, String port,
            String remetente, String destinatario, String senha, Boolean ssl,
            Integer intervaloBackup) {
        this.empresa = empresa;
        this.caminho = caminho;
        this.smtp = smtp;
        this.port = port;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.senha = senha;
        this.ssl = ssl;
        this.intervaloBackup = intervaloBackup;
    }

    public BackupConfig() {
        intervaloBackup = 1;
    }

    public Boolean isValido() {
        if ((empresa == null) || (caminho == null) || (smtp == null) || (port == null) ||
                (remetente == null) || (destinatario == null) || (senha == null) || (ssl == null) || (intervaloBackup == null)) {
            return false;
        } else {
            return true;
        }
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public String getHoraBackup() {
        return horaBackup;
    }

    public void setHoraBackup(String horaBackup) {
        this.horaBackup = horaBackup;
    }

    public Integer getIntervaloBackup() {
        return intervaloBackup;
    }

    public void setIntervaloBackup(Integer intervaloBackup) {
        this.intervaloBackup = intervaloBackup;
    }

    public String getInstancia() {
        return instancia;
    }

    public void setInstancia(String instancia) {
        this.instancia = instancia;
    }

    public Boolean getDiferencial() {
        return diferencial;
    }

    public void setDiferencial(Boolean diferencial) {
        this.diferencial = diferencial;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    
}
