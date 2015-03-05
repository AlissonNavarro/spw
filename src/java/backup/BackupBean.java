package backup;

import Metodos.Metodos;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class BackupBean {

    Toolkit toolkit;
    Timer timer;
    public BackupConfig backupConfig;
    public Boolean isAtivo;
    private String database;

    public BackupBean() {
        isAtivo = false;
        Banco b = new Banco();
        backupConfig = b.getBackupConfig();
        //database = Metodos.getDatabaseConexao();
    }
    
    public boolean backup() {
        boolean flag = true;
        try {
            toolkit = Toolkit.getDefaultToolkit();
            timer = new Timer();
            Banco b = new Banco();
            BackupConfig backupConfig_ = b.getBackupConfig();
            timer.schedule(new Task(backupConfig_), new Date());
            timer.scheduleAtFixedRate(new Task(backupConfig_),
                    dataHora(backupConfig_.getHoraBackup()),
                    backupConfig_.getIntervaloBackup() * 1000 * 3600);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public void cancelar() {
        timer.cancel();
        isAtivo = false;
    }

    public void salvar() {
        Banco b = new Banco();
        if (backupConfig.isValido()) {
            boolean testeUpdate = false;
            try {
                backupConfig.setIp(Metodos.getServerConexao());
                backupConfig.setDatabase(Metodos.getDatabaseConexao());
                backupConfig.setUser(Metodos.getUserConexao());
                backupConfig.setPass(Metodos.getPasswordConexao());
                backupConfig.setInstancia(b.buscaInstanciaSql());
                testeUpdate = b.updateBackupConfig(backupConfig);
            } catch (SQLException ex) {
                System.out.println("salvar: " + ex);
                //Logger.getLogger(BackupBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                System.out.println("salvar: " + ex);
                //Logger.getLogger(BackupBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (testeUpdate) {
                if (backup()) {
                    setIsAtivo(true);
                    FacesMessage msgErro = new FacesMessage("Agendada tarefa com sucesso.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    setIsAtivo(false);
                    FacesMessage msgErro = new FacesMessage("Falha ao agendar a tarefa.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            } else {
                FacesMessage msgErro = new FacesMessage("Falha na alteração com o banco de dados.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

        } else {
            FacesMessage msgErro = new FacesMessage("Dados inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void teste() {
        backupConfig.setIp(Metodos.getServerConexao());
        backupConfig.setDatabase(Metodos.getDatabaseConexao());
        backupConfig.setUser(Metodos.getUserConexao());
        backupConfig.setPass(Metodos.getPasswordConexao());
        Banco b = new Banco();
        backupConfig.setInstancia(b.buscaInstanciaSql());
        Integer flag = testando();
        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Teste realizado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("Dados imcompletos!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Caminho de backup inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag == 3) {
            FacesMessage msgErro = new FacesMessage("Problemas com a conexão com a internet!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag == 4) {
            FacesMessage msgErro = new FacesMessage("Problemas com a comunicação com o banco de dados!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag == 5) {
            FacesMessage msgErro = new FacesMessage("Falha ao enviar email.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    class Task extends TimerTask {

        BackupConfig backupConfig;

        public Task(BackupConfig backupConfig) {
            this.backupConfig = backupConfig;
        }

        public Task() {
        }

        public void run() {

            Integer flag = 0;
            if (backupConfig.isValido()) {

                String empresa = backupConfig.getEmpresa();
                String caminho = backupConfig.getCaminho();
                File fileTeste = new File(caminho);
                if (!fileTeste.isDirectory()) {
                    flag = 2;
                } else {
                    caminho += "\\backup.bak";
                    String smtp = backupConfig.getSmtp();
                    String port = backupConfig.getPort();
                    String remetente = backupConfig.getRemetente();
                    String senha = backupConfig.getSenha();
                    String destinatario = backupConfig.getDestinatario();
                    Boolean ssl = backupConfig.getSsl();
                    Boolean diferencial = backupConfig.getDiferencial();
                    String instancia = backupConfig.getInstancia();
                    String ip = backupConfig.getIp();
                    String user = backupConfig.getUser();
                    String pass = backupConfig.getPass();
                    database = backupConfig.getDatabase();

                    File file = new File(caminho);
                    file.delete();
                    Process p = null;
                    try {
                        //p = Runtime.getRuntime().exec("sqlcmd.exe -S (local)\\SQLExpress -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "' WITH DIFFERENTIAL\"");
                        String tmp = "";
                        if (diferencial) {
                            tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "' WITH DIFFERENTIAL\" -U \"" + user + "\" -P \"" + pass + "\"";
                            //tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "' WITH DIFFERENTIAL\" ";
                        } else {
                            tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "'\" -U \"" + user + "\" -P \"" + pass + "\"";
                            //tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "'\" ";
                        }
                        p = Runtime.getRuntime().exec(tmp);
                    } catch (IOException ex) {
                        System.out.println("run 1: " + ex);
                        //Logger.getLogger(BackupBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        p.waitFor();
                    } catch (InterruptedException ex) {
                        System.out.println("run 2: " + ex);
                        //Logger.getLogger(BackupBean.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    File arquivoCompactado = Compactador.compactar(caminho, empresa);
                    if (arquivoCompactado != null) {
                        SendMail sendMail = new SendMail();

                        sendMail.setSmtp(smtp);
                        sendMail.setPort(port);
                        sendMail.setIsSSL(new Boolean(ssl));
                        boolean isEmailOK = sendMail.email(remetente, senha, destinatario, arquivoCompactado, titulo(empresa), corpo(arquivoCompactado, empresa));
                        if (!isEmailOK) {
                            flag = 3;
                        }
                    } else {
                        flag = 4;
                    }
                }
            } else {
                flag = 1;
            }
            if (flag == 0) {
                isAtivo = true;
            }
        }
    }

    private Integer testando() {
        Integer flag = 0;

        if (backupConfig.isValido()) {

            String empresa = backupConfig.getEmpresa();
            String caminho = backupConfig.getCaminho();
            File fileTeste = new File(caminho);
            if (!fileTeste.isDirectory()) {
                flag = 2;
            } else {
                caminho += "\\backup.bak";
                String smtp = backupConfig.getSmtp();
                String port = backupConfig.getPort();
                String remetente = backupConfig.getRemetente();
                String senha = backupConfig.getSenha();
                String destinatario = backupConfig.getDestinatario();
                Boolean ssl = backupConfig.getSsl();
                Boolean diferencial = backupConfig.getDiferencial();
                String instancia = backupConfig.getInstancia();
                String ip = backupConfig.getIp();
                String user = backupConfig.getUser();
                String pass = backupConfig.getPass();
                database = backupConfig.getDatabase();

                File file = new File(caminho);
                file.delete();

                //Exemplo do comando no DOS
                //C:\Windows\system32>sqlcmd -S (local)\sqlserver2008 -U sa -P "sgn@sql" -Q "BACKUP DATABASE card to disk='D:\teste.bkp'"
                Process p = null;
                try {
                    //p = Runtime.getRuntime().exec("sqlcmd.exe -S (local)\\SQLExpress -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "' WITH DIFFERENTIAL\"");
                    String tmp = "";
                    if (diferencial) {
                        tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "' WITH DIFFERENTIAL\" -U \"" + user + "\" -P \"" + pass + "\"";
                        //tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "' WITH DIFFERENTIAL\" ";
                    } else {
                        tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "'\" -U \"" + user + "\" -P \"" + pass + "\"";
                        //tmp = "sqlcmd -S " + ip + "\\" + instancia + " -Q \"BACKUP DATABASE " + database + " TO DISK='" + caminho + "'\" ";
                    }
                    p = Runtime.getRuntime().exec(tmp);
                } catch (IOException ex) {
                    System.out.println("testando 1: " + ex);
                    //Logger.getLogger(BackupBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    p.waitFor();
                } catch (InterruptedException ex) {
                    System.out.println("testando 2: " + ex);
                    //Logger.getLogger(BackupBean.class.getName()).log(Level.SEVERE, null, ex);
                }

                File arquivoCompactado = Compactador.compactar(caminho, empresa);
                if (arquivoCompactado != null) {
                    try {
                        SendMail sendMail = new SendMail();
                        sendMail.setSmtp(smtp);
                        sendMail.setPort(port);
                        sendMail.setIsSSL(ssl);
                        boolean isEmailOK = sendMail.teste(remetente, senha, destinatario);
                        if (!isEmailOK) {
                            flag = 3;
                        }
                    } catch (Exception ex) {
                        flag = 5;
                    }
                } else {
                    flag = 4;
                }
            }

        } else {
            flag = 1;
        }
        return flag;
    }

    private Date dataHora(String hora) {
        Date data = new Date(zeraData(new Date()).getTime() + horaToLong(hora));
        return data;
    }

    private Long horaToLong(String hora) {
        Long saida = null;
        try {
            hora += ":00";
            Time time = Time.valueOf(hora);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }
        return saida;
    }

    private Date zeraData(Date dataEntrada) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_ = sdf.format(dataEntrada.getTime());
        Date date = new Date();
        try {
            date = sdf.parse(data_);
        } catch (ParseException ex) {
        }
        return date;
    }

    private static String titulo(String empresa) {
        String saida = "[" + empresa + "] " + "backup";
        return saida;
    }

    private static String corpo(File arquivo, String empresa) {
        Float tamanho = (arquivo.length() / 1048576f);
        String saida = "Empresa: " + empresa + "\n"
                + "Tamanho do anexo: " + tamanho + " MB";
        return saida;
    }

    public static void main(String args[]) throws InterruptedException {
        Date d = new BackupBean().dataHora("23:00");
        d.getTime();
    }

    public BackupConfig getBackupConfig() {
        return backupConfig;
    }

    public void setBackupConfig(BackupConfig backupConfig) {
        this.backupConfig = backupConfig;
    }

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
