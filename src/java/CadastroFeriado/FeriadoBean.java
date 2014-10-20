/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroFeriado;

import Metodos.Metodos;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author amsgama
 */
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
        Banco banco = new Banco();
        feriadoList = new ArrayList<Feriado>();
        feriadoList = banco.consultaFeriados();
    }

    public void addNovoFeriado() {
        Banco banco = new Banco();
        if (novoFeriado.estaPreenchidoCorretamento()) {
            int flag = banco.addFeriado(novoFeriado);

            if (flag == 0) {
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
                //Metodos.setLogInfo("Editar Feriado - Nome: "+novoFeriado.getNome()+" Data: "+sf.format(novoFeriado.getData())+" Oficial: "+(novoFeriado.getIsOficial()?"Sim":"Não"));
                FacesMessage msgErro = new FacesMessage("Feriado adicionado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 2) {
                FacesMessage msgErro = new FacesMessage("Dados Inválidos");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new Banco();
            feriadoList = new ArrayList<Feriado>();
            feriadoList = banco.consultaFeriados();
        } else {
            FacesMessage msgErro = new FacesMessage("O nome do feriado deve ser preenchido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        novoFeriado = new Feriado();
    }

    public void editarFeriado() {
        Banco banco = new Banco();

        int flag = banco.editFeriado(editFeriado);
        

        if (flag == 0) {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
            //Metodos.setLogInfo("Editar Feriado - Nome: "+editFeriado.getNome()+" Data: "+sf.format(editFeriado.getData())+" Oficial: "+(editFeriado.getIsOficial()?"Sim":"Não"));
            FacesMessage msgErro = new FacesMessage("Alteração feita com sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new Banco();
        feriadoList = new ArrayList<Feriado>();
        feriadoList = banco.consultaFeriados();
        editFeriado = new Feriado();
    }

    public void deletarFeriado() {
        String feriado_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("feriado_id_delete");
        Feriado feriadoASerDeletado = new Feriado();
        Banco banco = new Banco();
        feriadoASerDeletado = banco.consultaFeriado(Integer.parseInt(feriado_id));
        banco = new Banco();
        boolean flag = banco.deleteFeriado(Integer.parseInt(feriado_id));

        if (flag) {
            FacesMessage msgErro = new FacesMessage("Feriado deletado com sucesso!");
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
            //Metodos.setLogInfo("Excluir Feriado - Nome: "+feriadoASerDeletado.getNome()+" Data: "+sf.format(feriadoASerDeletado.getData())+" Oficial: "+(feriadoASerDeletado.getIsOficial()?"Sim":"Não"));
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro inesperado ao deletar o feriado! Tente novamente!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        banco = new Banco();
        feriadoList = banco.consultaFeriados();
    }

    public void showAdicionarNovoFeriado() {
        novoFeriado = new Feriado();
        novoFeriado.setData(new Date());
    }

    public void showEditarNovoFeriado() {
        Banco banco = new Banco();
        String feriado_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("feriado_id_editar");
        editFeriado = new Feriado();
        editFeriado = banco.consultaFeriado(Integer.parseInt(feriado_id));
    }

    public void selecinarLinha() {
        String feriado_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("feriado_id");
        Banco banco = new Banco();
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
