package beans;

import entidades.CategoriaAfastamento;
import manageBean.CategoriaAfastamentoMB;
import Metodos.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class CategoriaAfastamentoBean implements Serializable {

    private List<CategoriaAfastamento> categoriaAfastamentoList;
    private CategoriaAfastamento editCategoriaAfastamento;
    private CategoriaAfastamento novaCategoriaAfastamento;

    public CategoriaAfastamentoBean() {
        editCategoriaAfastamento = new CategoriaAfastamento();
        novaCategoriaAfastamento = new CategoriaAfastamento();
        categoriaAfastamentoList = new ArrayList<CategoriaAfastamento>();
        CategoriaAfastamentoMB banco = new CategoriaAfastamentoMB();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public void showInserir() {
        novaCategoriaAfastamento = new CategoriaAfastamento();
    }

    public void inserir() {
        CategoriaAfastamentoMB banco = new CategoriaAfastamentoMB();
        boolean flag = banco.inserir(novaCategoriaAfastamento);
        if (flag) {
            Metodos.setLogInfo("Adicionar Categoria de Afastamento - Nome: " + novaCategoriaAfastamento.getDescricao());
            FacesMessage msgErro = new FacesMessage("Categoria inserida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da categoria já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public void showEditar() {
        editCategoriaAfastamento = new CategoriaAfastamento();
        String categoriaAfastamentoId = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("categoriaAfastamento_id");
        editCategoriaAfastamento = consultaCategoriaAfastamentoList(Integer.parseInt(categoriaAfastamentoId));
    }

    public void editar() {
        CategoriaAfastamentoMB banco = new CategoriaAfastamentoMB();
        if (null != editCategoriaAfastamento.getDescricao()) {
            banco.alterar(editCategoriaAfastamento);
            Metodos.setLogInfo("Editar Categoria Afastamento - Nome: " + editCategoriaAfastamento.getDescricao());
            FacesMessage msgErro = new FacesMessage("Categoria de afastamento alterada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da categoria não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public void deletar() {
        String categoriaAfastamentoId = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("categoriaAfastamento_id");
        CategoriaAfastamentoMB banco = new CategoriaAfastamentoMB();
        CategoriaAfastamento categoriaAfastamento = banco.consultaCategoriaAfastamento(Integer.parseInt(categoriaAfastamentoId));
        boolean flag = banco.excluir(Integer.parseInt(categoriaAfastamentoId));
        if (flag) {
            Metodos.setLogInfo("Excluir Categoria de Afastamento - Nome: " + categoriaAfastamento.getDescricao());
        } else {
            FacesMessage msgErro = new FacesMessage("A categoria não pode ser deletado por está sendo utilizada em algum afastamento.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public CategoriaAfastamento consultaCategoriaAfastamentoList(int codCategoria) {
        CategoriaAfastamentoMB banco = new CategoriaAfastamentoMB();
        return banco.consultaCategoriaAfastamento(codCategoria);
    }

    public List<CategoriaAfastamento> getCategoriaAfastamentoList() {
        return categoriaAfastamentoList;
    }

    public void setCategoriaAfastamentoList(List<CategoriaAfastamento> categoriaAfastamentoList) {
        this.categoriaAfastamentoList = categoriaAfastamentoList;
    }

    public CategoriaAfastamento getEditCategoriaAfastamento() {
        return editCategoriaAfastamento;
    }

    public void setEditCategoriaAfastamento(CategoriaAfastamento editCategoriaAfastamento) {
        this.editCategoriaAfastamento = editCategoriaAfastamento;
    }

    public CategoriaAfastamento getNovaCategoriaAfastamento() {
        return novaCategoriaAfastamento;
    }

    public void setNovaCategoriaAfastamento(CategoriaAfastamento novaCategoriaAfastamento) {
        this.novaCategoriaAfastamento = novaCategoriaAfastamento;
    }
}
