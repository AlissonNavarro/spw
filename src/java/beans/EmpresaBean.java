package beans;

import manageBean.EmpresaMB;
import entidades.Empresa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class EmpresaBean implements Serializable {

    private List<SelectItem> empresaList;
    private Integer empresaSelecionada;
    private Empresa empresa;
    private Empresa empresaNova;
    private Empresa empresaEdit;

    public EmpresaBean() {

        empresa = new Empresa();
        empresaEdit = new Empresa();
        consultaEmpresa();
        empresaSelecionada = -1;
    }

    public void showAdicionar() {
        empresaNova = new Empresa();
    }

    public void showEditar() {
        EmpresaMB empresaMB = new EmpresaMB();
        empresaEdit = new Empresa();
        empresaEdit = empresaMB.consultarEmpresaPorId(empresaSelecionada);

    }

    public void salvar() {
        EmpresaMB empresaMB = new EmpresaMB();
        boolean sucesso = empresaMB.inserir(empresa);
        if (sucesso) {
            FacesMessage msgErro = new FacesMessage("Empresa cadastrada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            consultaEmpresa();
        } else {
            FacesMessage msgErro = new FacesMessage("Erro inesperado! Favor tentar novamente ou contactar o administrador");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void atualizar() {
        EmpresaMB empresaMB = new EmpresaMB();
        boolean sucesso = empresaMB.alterar(empresaEdit);
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
        empresa = empresaMB.consultarEmpresaPorId(empresaSelecionada);
    }

    public void excluirEmpresa() {
        boolean flag = false;
        EmpresaMB empresaMB = new EmpresaMB();

        if (empresaSelecionada != -1) {
            flag = empresaMB.excluir(empresaSelecionada);

            if (flag == true) {
                empresaSelecionada = -1;
                FacesMessage msgErro = new FacesMessage("Empresa excluida com sucesso!");
                //Metodos.setLogInfo("Excuir Cargo - Cargo: "+cargoASerExcluido);
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == false) {
                FacesMessage msgErro = new FacesMessage("A empresa não pode ser excluida!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            empresaList = empresaMB.consultaEmpresaOrdernado();
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione uma empresa válida!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

    }

    public List<SelectItem> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<SelectItem> empresaList) {
        this.empresaList = empresaList;
    }

    public Integer getEmpresaSelecionada() {
        return empresaSelecionada;
    }

    public void setEmpresaSelecionada(Integer empresaSelecionada) {
        this.empresaSelecionada = empresaSelecionada;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresaNova() {
        return empresaNova;
    }

    public void setEmpresaNova(Empresa empresaNova) {
        this.empresaNova = empresaNova;
    }

    public Empresa getEmpresaEdit() {
        return empresaEdit;
    }

    public void setEmpresaEdit(Empresa empresaEdit) {
        this.empresaEdit = empresaEdit;
    }

}
