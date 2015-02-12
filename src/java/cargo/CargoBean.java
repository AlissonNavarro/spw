package cargo;

import Metodos.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class CargoBean implements Serializable {

    private List<SelectItem> cargolist;
    private Integer cargoSelecionado;
    private Cargo cargoEdit;
    private Cargo cargoNovo;

    public CargoBean() {
        cargoEdit = new Cargo();
        cargoNovo = new Cargo();
        cargolist = new ArrayList<SelectItem>();
        consultaCargo();
    }

    public void consultaCargo() {
        CargoMB cargoMB = new CargoMB();
        cargolist = new ArrayList<SelectItem>();
        cargolist = cargoMB.consultaCargoOrdernado();
    }

    public void showAdicionar() {
        cargoNovo = new Cargo();
    }

    public void showEditar() {
        cargoEdit = new Cargo();
        cargoEdit.setId(cargoSelecionado);
        if (cargolist.size() > 0) {
            int i = 0;
            while (!cargolist.get(i).getValue().equals(cargoSelecionado)) {
                i++;
            }
            String label = cargolist.get(i).getLabel();
            cargoEdit.setNomeCargo(label);
        }
    }

    public void salvarNovoCargo() {
        CargoMB cargoMB = new CargoMB();
        cargoNovo.setNomeCargo(cargoNovo.getNomeCargo().toUpperCase());
        int flag = cargoMB.salvarNovoCargo(cargoNovo.getNomeCargo());

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
        cargolist = cargoMB.consultaCargoOrdernado();
    }

    public void excluirCargo() {
        boolean flag = false;
        CargoMB cargoMB = new CargoMB();

        if (cargoSelecionado != -1) {
            String cargoASerExcluido = Metodos.buscaRotulo(cargoSelecionado.toString(), cargolist);
            flag = cargoMB.excluirCargo(cargoSelecionado);

            if (flag == true) {
                FacesMessage msgErro = new FacesMessage("Cargo excluido com sucesso!");
                //Metodos.setLogInfo("Excuir Cargo - Cargo: "+cargoASerExcluido);
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == false) {
                FacesMessage msgErro = new FacesMessage("O cargo não pode ser excluido!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            cargolist = cargoMB.consultaCargoOrdernado();
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione um cargo válido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

    }

    public void salvarEditCargo() {
        CargoMB cargoMB = new CargoMB();
        cargoEdit.setNomeCargo(cargoEdit.getNomeCargo().toUpperCase());
        int flag = cargoMB.salvarEditCargo(cargoEdit);

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
        cargolist = cargoMB.consultaCargoOrdernado();
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
