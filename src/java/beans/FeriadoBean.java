package beans;

import entidades.Feriado;
import manageBean.FeriadoMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FeriadoBean implements Serializable {

    private List<Feriado> feriadoList;
    private Feriado feriado;
    private Feriado novoFeriado;
    private Feriado editFeriado;
    private Locale objLocale;

    public FeriadoBean() {
        Locale.setDefault(new Locale("pt", "BR"));
        objLocale = new Locale("pt", "BR");
        feriado = new Feriado();
        novoFeriado = new Feriado();
        editFeriado = new Feriado();
        FeriadoMB banco = new FeriadoMB();
        feriadoList = new ArrayList<Feriado>();
        feriadoList = banco.consultaFeriados();
    }

    public void addNovoFeriado() {
        FeriadoMB banco = new FeriadoMB();
        if (!novoFeriado.getNome().equals("")) {
            int flag = banco.inserir(novoFeriado);

            if (flag == 0) {
                //SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
                //Metodos.setLogInfo("Editar Feriado - Nome: "+novoFeriado.getNome()+" Data: "+sf.format(novoFeriado.getData())+" Oficial: "+(novoFeriado.getIsOficial()?"Sim":"Não"));
                FacesMessage msgErro = new FacesMessage("Feriado adicionado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 2) {
                FacesMessage msgErro = new FacesMessage("Dados Inválidos");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new FeriadoMB();
            feriadoList = new ArrayList<Feriado>();
            feriadoList = banco.consultaFeriados();
        } else {
            FacesMessage msgErro = new FacesMessage("O nome do feriado deve ser preenchido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        novoFeriado = new Feriado();
    }

    public void editarFeriado() {
        FeriadoMB banco = new FeriadoMB();
        int flag = banco.alterar(editFeriado);
       
        if (flag == 0) {
            //SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
            //Metodos.setLogInfo("Editar Feriado - Nome: "+editFeriado.getNome()+" Data: "+sf.format(editFeriado.getData())+" Oficial: "+(editFeriado.getIsOficial()?"Sim":"Não"));
            FacesMessage msgErro = new FacesMessage("Alteração feita com sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        feriadoList = new ArrayList<Feriado>();
        feriadoList = banco.consultaFeriados();
        editFeriado = new Feriado();
    }

    public void deletarFeriado() {
        String feriado_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("feriado_id_delete");
        FeriadoMB banco = new FeriadoMB();
        boolean flag = banco.deletar(Integer.parseInt(feriado_id));
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Feriado deletado com sucesso!");
            //SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
            //Metodos.setLogInfo("Excluir Feriado - Nome: "+feriadoASerDeletado.getNome()+" Data: "+sf.format(feriadoASerDeletado.getData())+" Oficial: "+(feriadoASerDeletado.getIsOficial()?"Sim":"Não"));
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro inesperado ao deletar o feriado! Tente novamente!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        feriadoList = banco.consultaFeriados();
    }

    public void showAdicionarNovoFeriado() {
        novoFeriado = new Feriado();
        novoFeriado.setData(new Date());
    }

    public void showEditarNovoFeriado() {
        FeriadoMB banco = new FeriadoMB();
        String feriado_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("feriado_id_editar");
        editFeriado = new Feriado();
        editFeriado = banco.consultaFeriado(Integer.parseInt(feriado_id));
    }

    public void selecinarLinha() {
        String feriado_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("feriado_id");
        FeriadoMB banco = new FeriadoMB();
        feriado = new Feriado();
        feriado = banco.consultaFeriado(Integer.parseInt(feriado_id));
    }

    public void limpar() {
        feriado = new Feriado();
    }

    public Feriado getEditFeriado() {
        return editFeriado;
    }

    public void setEditFeriado(Feriado editFeriado) {
        this.editFeriado = editFeriado;
    }

    public Feriado getFeriado() {
        return feriado;
    }

    public void setFeriado(Feriado feriado) {
        this.feriado = feriado;
    }

    public List<Feriado> getFeriadoList() {
        return feriadoList;
    }

    public void setFeriadoList(List<Feriado> feriadoList) {
        this.feriadoList = feriadoList;
    }

    public Feriado getNovoFeriado() {
        return novoFeriado;
    }

    public void setNovoFeriado(Feriado novoFeriado) {
        this.novoFeriado = novoFeriado;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }
}
