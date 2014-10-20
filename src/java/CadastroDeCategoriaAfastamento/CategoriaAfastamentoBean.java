/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroDeCategoriaAfastamento;

import Metodos.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author amsgama
 */
public class CategoriaAfastamentoBean implements Serializable {

    private List<CategoriaAfastamento> categoriaAfastamentoList;
    private CategoriaAfastamento editCategoriaAfastamento;
    private CategoriaAfastamento novaCategoriaAfastamento;

    public CategoriaAfastamentoBean() {
        editCategoriaAfastamento = new CategoriaAfastamento();
        novaCategoriaAfastamento = new CategoriaAfastamento();
        categoriaAfastamentoList = new ArrayList<CategoriaAfastamento>();
        Banco banco = new Banco();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public void showInserir() {
        novaCategoriaAfastamento = new CategoriaAfastamento();
    }

    public void inserir() {
        Banco banco = new Banco();
        boolean flag = banco.inserirNovaCategoria(novaCategoriaAfastamento);
        if (flag) {
            Metodos.setLogInfo("Adicionar Categoria de Afastamento - Nome: " + novaCategoriaAfastamento.getDescCategoriaAfastamento());
            FacesMessage msgErro = new FacesMessage("Categoria inserida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da categoria já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        categoriaAfastamentoList = new ArrayList<CategoriaAfastamento>();
        banco = new Banco();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public void showEditar() {
        editCategoriaAfastamento = new CategoriaAfastamento();
        String categoriaAfastamento_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("categoriaAfastamento_id");
        editCategoriaAfastamento = consultaCategoriaAfastamentoList(Integer.parseInt(categoriaAfastamento_id));
    }

    public void editar() {
        Banco banco = new Banco();
        if (null != editCategoriaAfastamento.getDescCategoriaAfastamento()) {
            banco.editarCategoriaAfastamento(editCategoriaAfastamento);
            Metodos.setLogInfo("Editar Categoria Afastamento - Nome: " + editCategoriaAfastamento.getDescCategoriaAfastamento());
            FacesMessage msgErro = new FacesMessage("Categoria de afastamento alterada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da categoria não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new Banco();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public void deletar() {
        String categoriaAfastamento_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("categoriaAfastamento_id");

        Banco banco = new Banco();
        CategoriaAfastamento categoriaAfastamento = banco.consultaCategoriaAfastamento(Integer.parseInt(categoriaAfastamento_id));
        banco = new Banco();
        boolean flag = banco.deletarCategoriaAfastamento(Integer.parseInt(categoriaAfastamento_id));

        if (flag) {
            Metodos.setLogInfo("Excluir Categoria de Afastamento - Nome: " + categoriaAfastamento.getDescCategoriaAfastamento());
        } else {
            FacesMessage msgErro = new FacesMessage("A categoria não pode ser deletado por está sendo utilizada em algum afastamento.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        categoriaAfastamentoList = new ArrayList<CategoriaAfastamento>();
        banco = new Banco();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
    }

    public CategoriaAfastamento consultaCategoriaAfastamentoList(Integer cod_categoria) {
        CategoriaAfastamento ca = new CategoriaAfastamento();
        for (Iterator<CategoriaAfastamento> it = categoriaAfastamentoList.iterator(); it.hasNext();) {
            CategoriaAfastamento categoriaAfastamento = it.next();
            if (categoriaAfastamento.getCategoriaAfastamentoID().equals(cod_categoria)) {
                ca = categoriaAfastamento;
            }
        }
        return ca;
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
