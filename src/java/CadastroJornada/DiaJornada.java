/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJornada;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Amvboas
 */
public class DiaJornada implements Serializable {

    private Boolean marked;
    private Boolean hasVirada;
    private Integer diaInt;
    private String diaString;
    private String horarios;
    private String descansos;
    private ArrayList<HorariosXdia> hxd;

    public DiaJornada(ArrayList<HorariosXdia> hxd, Integer dia, Integer unidade) {
        this.marked = false;
        this.diaInt = dia;
        this.diaString = diaIntToString(dia, unidade);
        this.hxd = hxd;
        initStringHorarios();
        initStringDescansos();
        hasVirada = calculaVirada();
    }

    public void change() {
        marked = !marked;
    }

    private String diaIntToString(Integer dia, Integer unidade) {
        String s = "";


        if (unidade == 1) {
            switch (dia % 7) {
                case 1:
                    s = "Segunda";
                    break;
                case 2:
                    s = "Terça";
                    break;
                case 3:
                    s = "Quarta";
                    break;
                case 4:
                    s = "Quinta";
                    break;
                case 5:
                    s = "Sexta";
                    break;
                case 6:
                    s = "Sábado";
                    break;
                case 0:
                    s = "Domingo";
                    break;
                default:
                    s = "Sétima";
                    break;
            }
        } else if (unidade == 2) {
            dia = dia + 1;
            if (dia > 31) {
                if (dia % 31 != 0) {
                    dia = dia % 31;
                } else {
                    dia = 31;
                }
            }
            s = "Dia " + dia;
        } else {
            dia = dia + 1;
            s = "Dia " + dia;
        }



        return s;
    }

    public void initStringHorarios() {
        String s = "";
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < hxd.size(); i++) {
            HorariosXdia horariosXdia = hxd.get(i);

            String entrada = sdfHora.format(horariosXdia.getEntrada().getTime());
            String saida = sdfHora.format(horariosXdia.getSaida().getTime());
            s += "(" + entrada + " - " + saida + ") ";
        }
        horarios = s;
    }

    public void initStringDescansos() {
        String s = "";
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < hxd.size(); i++) {
            HorariosXdia horariosXdia = hxd.get(i);
            if (horariosXdia.getInicioPrimeiroDescanso() != null) {
                String entrada1 = sdfHora.format(horariosXdia.getInicioPrimeiroDescanso().getTime());
                String saida1 = sdfHora.format(horariosXdia.getFimPrimeiroDescanso().getTime());
                s += "(" + entrada1 + " - " + saida1 + ") ";
                if (horariosXdia.getInicioInterjornada() != null){
                    String entradaInter = sdfHora.format(horariosXdia.getInicioInterjornada().getTime());
                    String saidaInter = sdfHora.format(horariosXdia.getFimInterjornada().getTime());
                    s += "(" + entradaInter + " - " + saidaInter + ") ";
                }
                if (horariosXdia.getInicioSegundoDescanso() != null) {
                    String entrada2 = sdfHora.format(horariosXdia.getInicioSegundoDescanso().getTime());
                    String saida2 = sdfHora.format(horariosXdia.getFimSegundoDescanso().getTime());
                    s += "(" + entrada2 + " - " + saida2 + ") ";
                }
            }
        }
        descansos = s;
    }

    public void limparTurno() {
        this.hxd = new ArrayList<HorariosXdia>();
        this.horarios = "";
        this.descansos = "";
        this.hasVirada = false;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
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

    public String getDescansos() {
        return descansos;
    }

    public void setDescansos(String descansos) {
        this.descansos = descansos;
    }

    public Integer getDiaInt() {
        return diaInt;
    }

    public void setDiaInt(Integer diaInt) {
        this.diaInt = diaInt;
    }

    public ArrayList<HorariosXdia> getHxd() {
        return hxd;
    }

    public void setHxd(ArrayList<HorariosXdia> hxd) {
        this.hxd = hxd;
    }

    public Boolean getHasVirada() {
        return hasVirada;
    }

    public void setHasVirada(Boolean hasVirada) {
        this.hasVirada = hasVirada;
    }

    public Boolean calculaVirada() {
        Boolean virada = false;
        for (int i = 0; i < hxd.size(); i++) {
            HorariosXdia horariosXdia = hxd.get(i);
            if (!virada) {
                virada = horariosXdia.getSaida().before(horariosXdia.getEntrada());
            }
        }
        return virada;
    }
}
