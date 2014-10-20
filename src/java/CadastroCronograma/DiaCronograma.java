/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroCronograma;

//import CadastroJornada.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author Amvboas
 */
public class DiaCronograma implements Serializable {

    private Boolean marked;
    private Boolean hasVirada;
    private Boolean houveAlteracao;
    private Integer ordem_dia;
    private Date dia;
    private String diaString;
    private String horarios;
    private List<String> horarioStrList;
    ArrayList<Horario> horariosList;
    ArrayList<Horario> horariosListBkp;
    private String diames;
    private Boolean isOverTime;
    private Boolean isFolga;
    private String cssHorario;
    private Boolean isDeslTemp;
    private Boolean temHorario;
    private Boolean isDesfazer;
    private int acao;
    private boolean retirarHorarioDoDia;
    
    public DiaCronograma(ArrayList<Horario> horariosList, Date dia, Integer ordem_dia) {
        this.marked = false;
        this.isOverTime = false;
        this.houveAlteracao = false;
        this.horarios = "";
        this.isDeslTemp = false;
        this.dia = dia;
        this.ordem_dia = ordem_dia;
        this.diaString = diaDateToString(dia);
        this.horariosList = horariosList;
        this.isFolga = false;
        temHorario = false;
        horarioStrList = new ArrayList<String>();
        initStringHorarios();
        hasVirada = calculaVirada();
        this.horariosListBkp = new ArrayList<Horario>();
        this.acao = -1;
        this.retirarHorarioDoDia = false;
    }

    public DiaCronograma(Integer dia) {
        horariosList = new ArrayList<Horario>();
        temHorario = false;
        this.marked = false;
        this.houveAlteracao = false;
        this.isDeslTemp = false;
        horarioStrList = new ArrayList<String>();
        initStringHorarios();
        hasVirada = calculaVirada();
        this.horariosListBkp = new ArrayList<Horario>();

    }

    public void change() {
        marked = !marked;
    }

    private String diaDateToString(Date dia) {
        String diasemana = "";
        SimpleDateFormat sdfa = new SimpleDateFormat("dd/MM");
        diames = sdfa.format(dia);

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(dia);
        int diaSemanaNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (diaSemanaNumber) {
            case 0:
                diasemana = "Dom";
                break;
            case 1:
                diasemana = "Seg";
                break;
            case 2:
                diasemana = "Ter";
                break;
            case 3:
                diasemana = "Qua";
                break;
            case 4:
                diasemana = "Qui";
                break;
            case 5:
                diasemana = "Sex";
                break;
            case 6:
                diasemana = "Sáb";
                break;

        }

        String diaStr = diasemana + " - " + diames;

        return diaStr;
    }

    public void initStringHorarios() {
        String temp = "";
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < horariosList.size(); i++) {
            Horario horario = horariosList.get(i);

            String entrada = sdfHora.format(horario.getEntrada().getTime());
            String saida = sdfHora.format(horario.getSaida().getTime());
            if (horario.isIsOverTime()) {
                temp = "[" + entrada + " - " + saida + "] ";
                horarioStrList.add(temp);
            } else {
                temp = "(" + entrada + " - " + saida + ") ";
                horarioStrList.add(temp);
            }
        }
    }

    public void atribuiFolga() {
        this.cssHorario = "color: blue";
        this.houveAlteracao = true;
        this.isFolga = true;
        this.isDeslTemp = false;
        this.isDesfazer = true;      
        this.acao = 1;
    }
    
    public void limparTurno() {
        this.horariosList.clear();
        this.horarioStrList.clear();
        this.horarioStrList.add("( Horário removido )");
        this.cssHorario = "color: #B0B0B0";
        this.houveAlteracao = true;
        this.isDeslTemp = false;
        this.isFolga = false;
        this.isDesfazer = true;
        this.acao = 2;
        retirarHorarioDoDia = true;
    }
    
  
    public void desfazHorario() {
        this.horariosList.clear();
        this.horariosList = new ArrayList<Horario>(horariosListBkp);
        this.horarioStrList.clear();
        this.cssHorario = "color: black";
        this.horarios = "";
        initStringHorarios();
        this.houveAlteracao = true;
        this.marked = false;
        this.isDeslTemp = false;
        this.isFolga = false;
        this.isDesfazer = false;
        this.acao = 3;
        retirarHorarioDoDia = false;
    }
    
    public void ordenarHorarioList() {
        if (!horariosList.isEmpty()) {
            Collections.sort(horariosList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Horario h1 = (Horario) o1;
                    Horario h2 = (Horario) o2;
                    return h1.getEntrada().compareTo(h2.getEntrada());
                }
            });
        }
    }



    public Boolean getTemHorario() {
        if (horariosList != null) {
            temHorario = (horariosList.size() > 0);
        }
        return temHorario;
    }

    public void setTemHorario(Boolean temHorario) {
        this.temHorario = temHorario;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public Boolean getHouveAlteracao() {
        return houveAlteracao;
    }

    public void setHouveAlteracao(Boolean houveAlteracao) {
        this.houveAlteracao = houveAlteracao;
    }

    public String getDiaString() {
        return diaString;
    }

    public void setDiaString(String diaString) {
        this.diaString = diaString;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public ArrayList<Horario> getHorariosList() {
        return horariosList;
    }

    
    public void setHorariosList(ArrayList<Horario> horariosList) {
        this.horariosList = horariosList;
        initStringHorarios();
        hasVirada = calculaVirada();
    }

    public Boolean getHasVirada() {
        return hasVirada;
    }

    public void setHasVirada(Boolean hasVirada) {
        this.hasVirada = hasVirada;
    }

    public Boolean calculaVirada() {
        Boolean virada = false;
        for (int i = 0; i < horariosList.size(); i++) {
            Horario horario = horariosList.get(i);
            if (!virada) {
                virada = horario.getSaida().before(horario.getEntrada());
            }
        }
        return virada;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getDiames() {
        return diames;
    }

    public void setDiames(String diames) {
        this.diames = diames;
    }

    public Integer getOrdem_dia() {
        return ordem_dia;
    }

    public void setOrdem_dia(Integer ordem_dia) {
        this.ordem_dia = ordem_dia;
    }

    public Boolean getIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(Boolean isOverTime) {
        this.isOverTime = isOverTime;
    }

    public Boolean getIsFolga() {
        return isFolga;
    }

    public void setIsFolga(Boolean isFolga) {
        this.isFolga = isFolga;
    }

    public String getCssHorario() {
        return cssHorario;
    }

    public void setCssHorario(String cssHorario) {
        this.cssHorario = cssHorario;
    }

    public List<String> getHorarioStrList() {
        return horarioStrList;
    }

    public void setHorarioStrList(List<String> horarioStrList) {
        this.horarioStrList = horarioStrList;
    }

    public Boolean getIsDeslTemp() {
        return isDeslTemp;
    }

    public void setIsDeslTemp(Boolean isDeslTemp) {
        this.isDeslTemp = isDeslTemp;
    }

    public ArrayList<Horario> getHorariosListBkp() {
        return horariosListBkp;
    }

    public void setHorariosListBkp(ArrayList<Horario> horariosListBkp) {
        this.horariosListBkp = horariosListBkp;
    }

    public int getAcao() {
        return acao;
    }

    public void setAcao(int acao) {
        this.acao = acao;
    }

    public Boolean getIsDesfazer() {
        return isDesfazer;
    }
    
    public void setIsDesfazer(boolean isDesfazer) {
        this.isDesfazer = isDesfazer;
    }    

    public void setRetirarHorarioDoDia(Boolean retirarHorarioDoDia) {
        this.retirarHorarioDoDia = retirarHorarioDoDia;
    }

    public boolean isRetirarHorarioDoDia() {
        return retirarHorarioDoDia;
    }
    
}
