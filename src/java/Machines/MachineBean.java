/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Machines;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.richfaces.model.selection.Selection;

/**
 *
 * @author Pedro
 */
public class MachineBean implements Serializable {

    private List<Machine> machineList;
    private List<SelectItem> machineTypeList;
    private Machine machine;
    private Selection selectMac;
    private Integer page;

    public MachineBean() {
        machine = new Machine();
        machineList = new ArrayList<Machine>();
        machineTypeList = new ArrayList<SelectItem>();
        ConsultaRelogios();
    }

    public void reConstroi() {
        machineList = new ArrayList<Machine>();
        machineTypeList = new ArrayList<SelectItem>();
        ConsultaRelogios();
    }

    public void ConsultaRelogios() {
        MachineBanco banco = new MachineBanco();
        machineList = banco.consultaRelogios();
        machineTypeList = banco.consultaTiposRelogio();

    }

    public void showEditarRelogio() {
        machine = new Machine();
        Integer idMac = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idMac"));
        MachineBanco banco = new MachineBanco();
        machine = banco.consultaRelogio(idMac);
    }

    public void adicionar() {
        MachineBanco banco = new MachineBanco();
        banco.addRelogio(machine);
        machine = new Machine();
        ConsultaRelogios();
        FacesMessage msgErro = new FacesMessage("Relógio adicionado com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public void editar() {
        MachineBanco banco = new MachineBanco();
        banco.editRelogio(machine);
        machine = new Machine();
        ConsultaRelogios();
        FacesMessage msgErro = new FacesMessage("Relógio editado com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public void excluir() {
        MachineBanco banco = new MachineBanco();
        banco.deleteRelogio(machine);
        machine = new Machine();
        ConsultaRelogios();
        FacesMessage msgErro = new FacesMessage("Relógio excluido com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public List<SelectItem> getMachineTypeList() {
        return machineTypeList;
    }

    public void setMachineTypeList(List<SelectItem> machineTypeList) {
        this.machineTypeList = machineTypeList;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Selection getSelectMac() {
        return selectMac;
    }

    public void setSelectMac(Selection selectMac) {
        this.selectMac = selectMac;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
