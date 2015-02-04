package Empresa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class EmpresaBean implements Serializable {

    private List<SelectItem> empresaList;
    private String empresaSelecionada = "-1";
    private Empresa empresa;
    private String IpAD;
    private String cnpj;
    private String IpDigitCatcher;
    private String IpDigitSender;
    private String FullNameIpCatcher;
    private String FullNameIpSender;

    public EmpresaBean() {

        empresa = new Empresa();
        empresaList = new ArrayList<SelectItem>();
        consultaEmpresa();

        Banco banco = new Banco();
        IpAD = banco.getServidorAD();
        if (IpAD == null || IpAD.equals("")) {
            IpAD = "";
        }
        IpDigitSender = banco.getServidorDigitSender();
        if (IpDigitSender == null || IpDigitSender.equals("")) {
            IpDigitSender = "";
        } else {
            FullNameIpSender = "http://" + IpDigitSender + "/sgnclockmanager/SendUsers.aspx";
        }
        IpDigitCatcher = banco.getServidorDigitCatcher();
        if (IpDigitCatcher == null || IpDigitCatcher.equals("")) {
            IpDigitCatcher = "";
        } else {
            FullNameIpCatcher = "http://" + IpDigitCatcher + "/digit/Scanner.aspx";
        }
    }

    public void salvar(){
        Banco b = new Banco();
        System.out.println("salvou yay");
        
        Boolean sucesso = b.updateEmpresa(empresa.getRazaoSocial(), empresa.getCnpj(), empresa.getAddress(), empresa.getId() );
        if (sucesso) {
            FacesMessage msgErro = new FacesMessage("Empresa atualizada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            consultaEmpresa();
        } else {
            FacesMessage msgErro = new FacesMessage("Erro inesperado! Favor tentar novamente ou contactar o administrador");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }
    
    public void consultaEmpresa() {
        Banco banco = new Banco();
        empresaList = new ArrayList<SelectItem>();
        empresaList = banco.consultaEmpresaOrdernado();
    }

    public void consultaDetalhesEmpresa() {
        Banco banco = new Banco();
        empresa = banco.consultaDetalhesEmpresa(empresaSelecionada);
    }

    public void salvarIpAD() {
        Banco banco = new Banco();
        Boolean flag = banco.insertADIP(IpAD);

        if (flag && verificarADServer()) {
            FacesMessage msgErro = new FacesMessage("Ip Cadastrado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao cadastrar IP");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void salvarIpDigitSenderCatcher() {
        Banco banco = new Banco();
        Boolean flag = banco.insertDigitIP(IpDigitSender, IpDigitCatcher);

        if (flag) {
            FacesMessage msgErro = new FacesMessage("Ips Cadastrados com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao cadastrar IP");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public Boolean verificarADServer() {

        Banco banco = new Banco();
        String ip = banco.getServidorAD();
        if (ip == null || ip.equals("") || ip.equals("000.000.000.000")) {
            return false;
        }
        return true;
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

    public List<SelectItem> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<SelectItem> empresaList) {
        this.empresaList = empresaList;
    }

    public String getEmpresaSelecionada() {
        return empresaSelecionada;
    }

    public void setEmpresaSelecionada(String empresaSelecionada) {
        this.empresaSelecionada = empresaSelecionada;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
