package Departamento;

import Metodos.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class DepartamentoBean implements Serializable {

    private List<SelectItem> departamentolist;
    private List<SelectItem> superDepartamentolist;
    private int departamentoSelecionado;
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
        DepartamentoMB banco = new DepartamentoMB();
        departamentolist = new ArrayList<SelectItem>();
        departamentolist = banco.consultaDepartamentoOrdernado();
    }
    
    @SuppressWarnings("element-type-mismatch")
    public void consultaSuperDepartamentoList() {
        DepartamentoMB banco = new DepartamentoMB();
        superDepartamentolist = new ArrayList<SelectItem>();
        superDepartamentolist.addAll(banco.consultaDepartamentoOrdernado());
        banco = new DepartamentoMB();
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
            deptoEdit.setNome(label);
            DepartamentoMB banco = new DepartamentoMB();
            int deptoPai = banco.consultaDeptoPai(departamentoSelecionado);
            deptoEdit.setSuperDeptoId(deptoPai);
        }

    }

    public void salvarNovoDepartamento() {
        if(departamentolist.size()==0){
            deptoNovo.setSuperDeptoId(0);
        }
        DepartamentoMB banco = new DepartamentoMB();
        deptoNovo.setNome(deptoNovo.getNome().toUpperCase());
        int flag = banco.salvarNovoDepartamento(deptoNovo.getNome(), deptoNovo.getSuperDeptoId());

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Departamento adicionado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            //Metodos.setLogInfo("Adicionar Departamento - Departamento: "+deptoNovo.getNome()+" Alocado em: "+Metodos.buscaRotulo(deptoNovo.getSuperDeptoId(), departamentolist ).replace("&nbsp;",""));
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
        banco = new DepartamentoMB();
        departamentolist = banco.consultaDepartamentoOrdernado();
        banco = new DepartamentoMB();      

    }

    public void excluirDepartamento() {
        DepartamentoMB banco = new DepartamentoMB();

        Boolean flag = false;
        if (departamentoSelecionado != 1 && (!banco.temFilhos(departamentoSelecionado) && !banco.temFuncionariosAlocados(departamentoSelecionado))) {
            //String departamentoASerExcluido = Metodos.buscaRotulo(departamentoSelecionado.toString(), departamentolist);
            flag = banco.excluirDepartamento(departamentoSelecionado);

            if (flag == true) {
                FacesMessage msgErro = new FacesMessage("Departamento excluido com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
//                Metodos.setLogInfo("Excluir departamento - Departamento: "+departamentoASerExcluido.replace("&nbsp;",""));
            }

            if (flag == false) {
                FacesMessage msgErro = new FacesMessage("O departamento não pode ser excluido!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new DepartamentoMB();
            departamentolist = banco.consultaDepartamentoOrdernado();
            banco = new DepartamentoMB();
        } else {
            FacesMessage msgErro = new FacesMessage("O departamento não pode ser excluido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

    }

    public void salvarEditDepartamento() {
        DepartamentoMB banco = new DepartamentoMB();
        ArrayList<Integer> filhos = banco.getTodosOsDescendentes(departamentoSelecionado);
        banco = new DepartamentoMB();
        if (filhos.contains(deptoEdit.getSuperDeptoId())) {
            FacesMessage msgErro = new FacesMessage("Não é possível fazer essa alocação");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (deptoEdit.getId() == deptoEdit.getSuperDeptoId()) {
            FacesMessage msgErro = new FacesMessage("Não é possível fazer essa alocação");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);

        } else {
            deptoEdit.setNome(deptoEdit.getNome().toUpperCase());
            int flag = banco.salvarEditDepartamento(deptoEdit);

            if (flag == 0) {
//                Metodos.setLogInfo("Editar Departamento - Departamento: "+deptoEdit.getNome()+" Alocado em: "+Metodos.buscaRotulo(deptoEdit.getSuperDeptoId().toString(), departamentolist ).replace("&nbsp;",""));
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
            banco = new DepartamentoMB();
            deptoNovo = new Departamento();
            departamentolist = banco.consultaDepartamentoOrdernado();
            banco = new DepartamentoMB();

        }       
    }  

    public int getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(int departamentoSelecionado) {
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
