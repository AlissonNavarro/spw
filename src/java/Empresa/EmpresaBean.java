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
        consultaEmpresa();
        EmpresaMB empresaMB = new EmpresaMB();
        IpAD = empresaMB.getServidorAD();
        IpDigitSender = empresaMB.getServidorDigitSender();
        if (IpDigitSender == null || IpDigitSender.equals("")) {
            IpDigitSender = "";
        } else {
            FullNameIpSender = "http://" + IpDigitSender + "/sgnclockmanager/SendUsers.aspx";
        }
        IpDigitCatcher = empresaMB.getServidorDigitCatcher();
        if (IpDigitCatcher == null || IpDigitCatcher.equals("")) {
            IpDigitCatcher = "";
        } else {
            FullNameIpCatcher = "http://" + IpDigitCatcher + "/digit/Scanner.aspx";
        }
    }

    public void salvar(){
        EmpresaMB empresaMB = new EmpresaMB();
        boolean sucesso = empresaMB.updateEmpresa(empresa.getRazaoSocial(), empresa.getCnpj(), empresa.getAddress(), empresa.getId() );
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
        EmpresaMB empresaMB = new EmpresaMB();
        empresaList = new ArrayList<SelectItem>();
        empresaList = empresaMB.consultaEmpresaOrdernado();
    }

    public void consultaDetalhesEmpresa() {
        EmpresaMB empresaMB = new EmpresaMB();
        empresa = empresaMB.consultaDetalhesEmpresa(empresaSelecionada);
    }

    public void salvarIpAD() {
        EmpresaMB empresaMB = new EmpresaMB();
        boolean flag = empresaMB.insertADIP(IpAD);

        if (flag && verificarADServer()) {
            FacesMessage msgErro = new FacesMessage("Ip Cadastrado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao cadastrar IP");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void salvarIpDigitSenderCatcher() {
        EmpresaMB empresaMB = new EmpresaMB();
        boolean flag = empresaMB.insertDigitIP(IpDigitSender, IpDigitCatcher);

        if (flag) {
            FacesMessage msgErro = new FacesMessage("Ips Cadastrados com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao cadastrar IP");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public boolean verificarADServer() {
        EmpresaMB empresaMB = new EmpresaMB();
        String ip = empresaMB.getServidorAD();
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
