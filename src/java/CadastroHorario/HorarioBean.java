/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHorario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 *
 * @author amsgama
 */
public class HorarioBean implements Serializable {

    private List<Horario> horarioList;
    private Horario horario;
    private Horario novoHorario;
    private Horario editHorario;

    public HorarioBean() {
        horario = new Horario();
        novoHorario = new Horario();
        //editHorario = new Horario();
        Banco banco = new Banco();
        horarioList = new ArrayList<Horario>();
        horarioList = banco.consultaHorarios();
    }
    
    public void startou(){
        System.out.println("Startou");
    }
    public void finalizou(){
        System.out.println("Finalizou");
    }

    public void addNovoHorario() {
        Banco banco = new Banco();
        if (novoHorario.estaPreenchidoCorretamento()) {
            int flag = banco.addHorario(novoHorario);

            if (flag == 0) {
                FacesMessage msgErro = new FacesMessage("Horário adicionado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                Metodos.Metodos.setLogInfo("Horario Adicionado - " + novoHorario.getNome());
            }

            if (flag == 1) {
                FacesMessage msgErro = new FacesMessage("O nome do horário já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 2) {
                FacesMessage msgErro = new FacesMessage("Dados Inválidos");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new Banco();
            horarioList = new ArrayList<Horario>();
            horarioList = banco.consultaHorarios();
        } else {
            FacesMessage msgErro = new FacesMessage("O nome do horário deve ser preenchido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        novoHorario = new Horario();
    }

    public void editarHorario() {
        Banco banco = new Banco();

        int flag = banco.editHorario(editHorario);

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Alteração feita com sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            Metodos.Metodos.setLogInfo("Editar Horario - " + editHorario.getNome());
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new Banco();
        horarioList = new ArrayList<Horario>();
        horarioList = banco.consultaHorarios();
        editHorario = new Horario();
    }
    
    public void deletarLinha() {
        String horario_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("horario_id_delete");
        Banco banco = new Banco();
        boolean flag = banco.deleteHorario(Integer.parseInt(horario_id));

        if (!flag) {
            //Caso não consiga deletar, irá tentar desativar o horário
            flag = banco.desativarHorario(Integer.parseInt(horario_id));
            if (!flag) {
                FacesMessage msgErro = new FacesMessage("O horário não pode ser deletado porque está sendo utilizado em alguma jornada.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                String nomeHorario = buscaRotulo(horario_id, horarioList);
                Metodos.Metodos.setLogInfo("Desativação de horário - " + nomeHorario);
                FacesMessage msgErro = new FacesMessage("O horário foi desativado com sucesso.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            String nomeHorario = buscaRotulo(horario_id, horarioList);
            Metodos.Metodos.setLogInfo("Exclusão de horário - " + nomeHorario);
            FacesMessage msgErro = new FacesMessage("O horário foi excluído com sucesso.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        banco = new Banco();
        horarioList = banco.consultaHorarios();
    }

    public static String buscaRotulo(String index, List<Horario> list) {
        int j = 0;
        String label = " ";
        while (j < list.size() && !(list.get(j).getHorario_id().toString().equals(index))) {
            j++;
        }
        if (j < list.size()) {
            label = list.get(j).getNome();
        }
        return label;
    }

    public void showAdicionarNovoHorario() {
        novoHorario = new Horario();
    }

    public void showEditarNovoHorario() {
        Banco banco = new Banco();
        String horario_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("horario_id_editar");
        editHorario = new Horario();
        editHorario = banco.consultaHorario(Integer.parseInt(horario_id));
    }

    public void selecinarLinha() {
        String horario_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("horario_id");
        Banco banco = new Banco();
        horario = new Horario();
        horario = banco.consultaHorario(Integer.parseInt(horario_id));
    }
    
    public void setRestType(String type){
        novoHorario.setType(type);
    }

    public void limpar() {
        horario = new Horario();
    }

    public List<Horario> getHorarioList() {
        return horarioList;
    }

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Horario getNovoHorario() {
        return novoHorario;
    }

    public void setNovoHorario(Horario novoHorario) {
        this.novoHorario = novoHorario;
    }

    public Horario getEditHorario() {
        return editHorario;
    }

    public void setEditHorario(Horario editHorario) {
        this.editHorario = editHorario;
    }
}
