/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Departamento;

import Administracao.PermissaoBean;
import Metodos.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author amvboas
 */
public class DepartamentoBean implements Serializable {

    private List<SelectItem> departamentolist;
    private List<SelectItem> superDepartamentolist;
    private Integer departamentoSelecionado;
    private Departamento deptoEdit;
    private Departamento deptoNovo;

    public DepartamentoBean() {
        departamentoSelecionado = 1;
        deptoNovo = new Departamento();
        deptoEdit = new Departamento();
        departamentolist = new ArrayList<SelectItem>();
        superDepartamentolist= new ArrayList<SelectItem>();
        consultaDepartamento();
    }

    public void consultaDepartamento() {
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        departamentolist = banco.consultaDepartamentoOrdernado();
    }
    @SuppressWarnings("element-type-mismatch")
    public void consultaSuperDepartamentoList() {
        Banco banco = new Banco();
        superDepartamentolist = new ArrayList<SelectItem>();
        superDepartamentolist.addAll(banco.consultaDepartamentoOrdernado());
        banco = new Banco();
        ArrayList<Integer> filhos = banco.getTodosOsDescendentes(departamentoSelecionado);
        filhos.add(departamentoSelecionado);
        for (int i = 0; i < superDepartamentolist.size(); i++) {
            SelectItem superDepartamento = superDepartamentolist.get(i);
            if(filhos.contains(superDepartamento.getValue())){
                superDepartamentolist.remove(i);
                i--;
            }
        }
    }

    public void showAdicionar() {
        deptoNovo = new Departamento();
    }

    public void showEditar() {
        consultaSuperDepartamentoList();
        deptoEdit = new Departamento();
        deptoEdit.setId(departamentoSelecionado);
        if (departamentolist.size() > 0) {
            int i = 0;
            while (!departamentolist.get(i).getValue().equals(departamentoSelecionado)) {
                i++;
            }
            String label = departamentolist.get(i).getLabel();
            while (label.startsWith("&nbsp;")) {
                label = label.substring(6);
            }
            deptoEdit.setNomeDepartamento(label);
            Banco banco = new Banco();
            Integer deptoPai = banco.consultaDeptoPai(departamentoSelecionado);
            deptoEdit.setSuperDeptoId(deptoPai);
        }

    }

    public void salvarNovoDepartamento() {
        if(departamentolist.size()==0){
            deptoNovo.setSuperDeptoId(0);
        }
        Banco banco = new Banco();
        deptoNovo.setNomeDepartamento(deptoNovo.getNomeDepartamento().toUpperCase());
        int flag = banco.salvarNovoDepartamento(deptoNovo.getNomeDepartamento(), deptoNovo.getSuperDeptoId());

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Departamento adicionado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            Metodos.setLogInfo("Adicionar Departamento - Departamento: "+deptoNovo.getNomeDepartamento()+" Alocado em: "+Metodos.buscaRotulo(deptoNovo.getSuperDeptoId().toString(), departamentolist ).replace("&nbsp;",""));
            deptoNovo = new Departamento();
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("O nome do departamento já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new Banco();
        departamentolist = banco.consultaDepartamentoOrdernado();
        banco = new Banco();      

    }

    public void excluirDepartamento() {
        Banco banco = new Banco();

        Boolean flag = false;
        if (departamentoSelecionado != 1 && (!banco.temFilhos(departamentoSelecionado) && !banco.temFuncionariosAlocados(departamentoSelecionado))) {
            String departamentoASerExcluido = Metodos.buscaRotulo(departamentoSelecionado.toString(), departamentolist);
            flag = banco.excluirDepartamento(departamentoSelecionado);

            if (flag == false) {
                FacesMessage msgErro = new FacesMessage("Departamento excluido com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                Metodos.setLogInfo("Excluir departamento - Departamento: "+departamentoASerExcluido.replace("&nbsp;",""));
            }

            if (flag == true) {
                FacesMessage msgErro = new FacesMessage("O departamento não pode ser excluido!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new Banco();
            departamentolist = banco.consultaDepartamentoOrdernado();
            banco = new Banco();
        } else {
            FacesMessage msgErro = new FacesMessage("O departamento não pode ser excluido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

    }

    public void salvarEditDepartamento() {
        Banco banco = new Banco();
        ArrayList<Integer> filhos = banco.getTodosOsDescendentes(departamentoSelecionado);
        banco = new Banco();
        if (filhos.contains(deptoEdit.getSuperDeptoId())) {
            FacesMessage msgErro = new FacesMessage("Não é possível fazer essa alocação");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (deptoEdit.getId() == deptoEdit.getSuperDeptoId()) {
            FacesMessage msgErro = new FacesMessage("Não é possível fazer essa alocação");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);

        } else {
            deptoEdit.setNomeDepartamento(deptoEdit.getNomeDepartamento().toUpperCase());
            int flag = banco.salvarEditDepartamento(deptoEdit);

            if (flag == 0) {
                Metodos.setLogInfo("Editar Departamento - Departamento: "+deptoEdit.getNomeDepartamento()+" Alocado em: "+Metodos.buscaRotulo(deptoEdit.getSuperDeptoId().toString(), departamentolist ).replace("&nbsp;",""));
                FacesMessage msgErro = new FacesMessage("Departamento atualizado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 1) {
                FacesMessage msgErro = new FacesMessage("O nome do departamento já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 2) {
                FacesMessage msgErro = new FacesMessage("Dados Inválidos");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new Banco();
            deptoNovo = new Departamento();
            departamentolist = banco.consultaDepartamentoOrdernado();
            banco = new Banco();

        }       
    }  

    public Integer getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Integer departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public Departamento getDeptoEdit() {
        return deptoEdit;
    }

    public void setDeptoEdit(Departamento deptoEdit) {
        this.deptoEdit = deptoEdit;
    }

    public Departamento getDeptoNovo() {
        return deptoNovo;
    }

    public List<SelectItem> getSuperDepartamentolist() {
        return superDepartamentolist;
    }

    public void setSuperDepartamentolist(List<SelectItem> superDepartamentolist) {
        this.superDepartamentolist = superDepartamentolist;
    }


    public void setDeptoNovo(Departamento deptoNovo) {
        this.deptoNovo = deptoNovo;
    }
}
