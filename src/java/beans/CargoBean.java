package beans;

import entidades.Cargo;
import Metodos.Metodos;
import manageBean.CargoMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class CargoBean implements Serializable {

    private List<SelectItem> cargolist;
    private int cargoSelecionado;
    private Cargo cargoEdit;
    private Cargo cargoNovo;

    public CargoBean() {
        cargoSelecionado = -1;
        cargoEdit = new Cargo();
        cargoNovo = new Cargo();
        cargolist = new ArrayList<SelectItem>();
        consultaCargo();
    }

    public void consultaCargo() {
        CargoMB cargoMB = new CargoMB();
        cargolist = cargoMB.listar();
    }

    public void showAdicionar() {
        cargoNovo = new Cargo();
    }

    public void showEditar() {
        CargoMB cargoMB = new CargoMB();
        cargoEdit = new Cargo();
        cargoEdit = cargoMB.consultar(cargoSelecionado);
    }

    public void salvarNovoCargo() {
        CargoMB cargoMB = new CargoMB();
        cargoNovo.setNome(cargoNovo.getNome().toUpperCase());
        int flag = cargoMB.inserir(cargoNovo);

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Cargo adicionado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            //Metodos.setLogInfo("Adicionar Cargo - Cargo: "+cargoNovo.getNomeCargo());
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("O nome do Cargo já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        cargolist = cargoMB.listar();
    }

    public void excluirCargo() {
        CargoMB cargoMB = new CargoMB();

        if (cargoSelecionado != -1) {
            //String cargoASerExcluido = Metodos.buscaRotulo(cargoSelecionado.toString(), cargolist);
            boolean flag = cargoMB.excluir(cargoSelecionado);

            if (flag == true) {
                FacesMessage msgErro = new FacesMessage("Cargo excluido com sucesso!");
                //Metodos.setLogInfo("Excuir Cargo - Cargo: "+cargoASerExcluido);
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == false) {
                FacesMessage msgErro = new FacesMessage("O cargo não pode ser excluido!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            cargolist = cargoMB.listar();
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione um cargo válido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

    }

    public void salvarEditCargo() {
        CargoMB cargoMB = new CargoMB();
        cargoEdit.setNome(cargoEdit.getNome().toUpperCase());
        int flag = cargoMB.alterar(cargoEdit);

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Cargo atualizado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            //Metodos.setLogInfo("Editar Cargo - Cargo: "+cargoEdit.getNomeCargo());
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("O nome do cargo já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        cargoNovo = new Cargo();
        cargolist = cargoMB.listar();
    }

    public Cargo getCargoEdit() {
        return cargoEdit;
    }

    public void setCargoEdit(Cargo cargoEdit) {
        this.cargoEdit = cargoEdit;
    }

    public Cargo getCargoNovo() {
        return cargoNovo;
    }

    public void setCargoNovo(Cargo cargoNovo) {
        this.cargoNovo = cargoNovo;
    }

    public Integer getCargoSelecionado() {
        return cargoSelecionado;
    }

    public void setCargoSelecionado(Integer cargoSelecionado) {
        this.cargoSelecionado = cargoSelecionado;
    }

    public List<SelectItem> getCargolist() {
        return cargolist;
    }

    public void setCargolist(List<SelectItem> cargolist) {
        this.cargolist = cargolist;
    }

}
