package beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import manageBean.ConfigMB;

public class ConfigBean {

    private String IpAD;
    private String IpDigitCatcher;
    private String IpDigitSender;
    private String FullNameIpCatcher;
    private String FullNameIpSender;

    public ConfigBean() {
        ConfigMB configMB = new ConfigMB();
        IpAD = configMB.getServidorAD();
        IpDigitSender = configMB.getServidorDigitSender();
        if (IpDigitSender == null || IpDigitSender.equals("")) {
            IpDigitSender = "";
        } else {
            FullNameIpSender = "http://" + IpDigitSender + "/sgnclockmanager/SendUsers.aspx";
        }
        IpDigitCatcher = configMB.getServidorDigitCatcher();
        if (IpDigitCatcher == null || IpDigitCatcher.equals("")) {
            IpDigitCatcher = "";
        } else {
            FullNameIpCatcher = "http://" + IpDigitCatcher + "/digit/Scanner.aspx";
        }
    }

    public void salvarIpAD() {
        ConfigMB configMB = new ConfigMB();
        boolean flag = configMB.insertADIP(IpAD);

        if (flag && verificarADServer()) {
            FacesMessage msgErro = new FacesMessage("Ip Cadastrado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao cadastrar IP");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void salvarIpDigitSenderCatcher() {
        ConfigMB configMB = new ConfigMB();
        boolean flag = configMB.insertDigitIP(IpDigitSender, IpDigitCatcher);

        if (flag) {
            FacesMessage msgErro = new FacesMessage("Ips Cadastrados com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao cadastrar IP");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public boolean verificarADServer() {
        ConfigMB configMB = new ConfigMB();
        String ip = configMB.getServidorAD();
        return ip != null && !ip.equals("") && !ip.equals("000.000.000.000");
    }

    public String getIpAD() {
        return IpAD;
    }

    public void setIpAD(String IpAD) {
        this.IpAD = IpAD;
    }

    public String getIpDigitCatcher() {
        return IpDigitCatcher;
    }

    public void setIpDigitCatcher(String IpDigitCatcher) {
        this.IpDigitCatcher = IpDigitCatcher;
    }

    public String getIpDigitSender() {
        return IpDigitSender;
    }

    public void setIpDigitSender(String IpDigitSender) {
        this.IpDigitSender = IpDigitSender;
    }

    public String getFullNameIpCatcher() {
        return FullNameIpCatcher;
    }

    public void setFullNameIpCatcher(String FullNameIpCatcher) {
        this.FullNameIpCatcher = FullNameIpCatcher;
    }

    public String getFullNameIpSender() {
        return FullNameIpSender;
    }

    public void setFullNameIpSender(String FullNameIpSender) {
        this.FullNameIpSender = FullNameIpSender;
    }

}
