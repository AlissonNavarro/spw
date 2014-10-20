/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import Abono.Abono;
import Afastamento.Afastamento;

import ConsultaPonto.Escala;
import ConsultaPonto.Feriado;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class RelatorioResumoEscala implements Serializable {

    private String nome;
    private List<Escala> escalaList;
    private List<Abono> abonoList;
    private List<Feriado> feriadoList;
    private Integer mesEscala;
    private Integer ano;
    private Boolean hasEscala;
    private HashMap<Integer, String> schClassID_LegendaList;
    private Afastamento afastamento;

    public RelatorioResumoEscala(String nome, List<Escala> escalaList) {
        this.nome = nome;
        this.escalaList = escalaList;
    }

    public List<String> getEscalasSttr() {
        List<String> escalasListStr = new ArrayList<String>();

        for (int i = 1; i <= 31; i++) {
            //Verifica se é um feriado
            if (isFeriado(i)) {
                escalasListStr.add("");
            } else {
                //Verifica se é um periodoAbonodo
                Abono abono = getDiaComPeriodoAbono(i);
                if (abono != null && buscaEscala(i) != null) {
                    Integer codAbono = abono.getCod();
                    escalasListStr.add("F" + codAbono.toString());
                } else {
                    if (buscaEscala(i) == null) {
                        escalasListStr.add("");
                    } else {
                        Escala escala = buscaEscala(i);
                        String diaEscala = "";
                        for (Iterator<Integer> it = escala.getSchClassIDList().iterator(); it.hasNext();) {
                            Integer schClassID = it.next();
                            String legenda = schClassID_LegendaList.get(schClassID);
                            String turno = ajusteLegenda(legenda, schClassID);
                            diaEscala += turno + " ";
                        }
                        diaEscala = diaEscala.substring(0, diaEscala.length() - 1);
                        escalasListStr.add(diaEscala);
                    }
                }
            }
        }
        return escalasListStr;
    }
    //Com afastamento real

    public List<String> getEscalasStr2() {
        List<String> escalasListStr = new ArrayList<String>();

        for (int i = 1; i <= 31; i++) {
            //Verifica se é um feriado
            if (isFeriado(i)) {
                escalasListStr.add("");
            } else {
                //Verifica se é um periodoAbonodo
                Boolean teste = getDiaComAfastamento(i);
                if (teste) {
                    escalasListStr.add(afastamento.getCategoriaAfastamento().getLegenda());
                } else {
                    if (buscaEscala(i) == null) {
                        escalasListStr.add("");
                    } else {
                        Escala escala = buscaEscala(i);
                        String diaEscala = "";
                        for (Iterator<Integer> it = escala.getSchClassIDList().iterator(); it.hasNext();) {
                            Integer schClassID = it.next();
                            String legenda = schClassID_LegendaList.get(schClassID);
                            String turno = ajusteLegenda(legenda, schClassID);
                            diaEscala += turno + " ";
                        }
                        diaEscala = diaEscala.substring(0, diaEscala.length() - 1);
                        escalasListStr.add(diaEscala);
                    }
                }
            }
        }
        return escalasListStr;
    }

    private String ajusteLegenda(String legenda, Integer schClassID) {
        String saida = new String();
        if (legenda == null || legenda.equals("")) {
            saida = schClassID < 10 ? "0" + schClassID.toString() : schClassID.toString();
        } else {
            if (legenda.length() == 1) {
                saida = " " + legenda;
            } else {
                saida = legenda;
            }
        }
        return saida;
    }

    public List<String> getSemEscalasStr() {
        List<String> escalasListStr = new ArrayList<String>();

        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" S ");
        escalasListStr.add(" E ");
        escalasListStr.add(" M ");
        escalasListStr.add("   ");
        escalasListStr.add(" E ");
        escalasListStr.add(" S ");
        escalasListStr.add(" C ");
        escalasListStr.add(" A ");
        escalasListStr.add(" L ");
        escalasListStr.add(" A ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        escalasListStr.add(" * ");
        return escalasListStr;
    }

    private Abono getDiaComPeriodoAbono(Integer diaMes) {

        for (Iterator<Abono> it = abonoList.iterator(); it.hasNext();) {
            Abono abono = it.next();
            GregorianCalendar dia = new GregorianCalendar();
            dia.setTimeInMillis(abono.getDataInicioAbono().getTime());
            Integer mes_ = dia.get(Calendar.MONTH);

            Integer diaMes_ = dia.get(Calendar.DAY_OF_MONTH);
            if (diaMes.equals(diaMes_) && mes_.equals(mesEscala)) {
                return abono;
            }
        }
        return null;
    }

    private Boolean getDiaComAfastamento(Integer diaMes) {

        GregorianCalendar g = new GregorianCalendar();
        g.set(ano, mesEscala, diaMes, 0, 0, 0);
        Date data = zeraData(g.getTime());
        if (afastamento != null) {
            if ((afastamento.getDataInicio().before(data) || data.equals(afastamento.getDataInicio()))
                    && ((afastamento.getDataFim().after(data)) || data.equals(afastamento.getDataFim()))) {
                return true;
            }
        }
        return false;
    }

    private Date zeraData(Date dataEntrada) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_ = sdf.format(dataEntrada.getTime());
        Date date = new Date();
        try {
            date = sdf.parse(data_);
        } catch (ParseException ex) {
        }
        return date;
    }

    private Boolean isFeriado(Integer diaMes) {

        for (Iterator<Feriado> it = feriadoList.iterator(); it.hasNext();) {
            Feriado feriado = it.next();
            GregorianCalendar dia = new GregorianCalendar();
            dia.setTimeInMillis(feriado.getDia().getTime());
            Integer mes_ = dia.get(Calendar.MONTH);

            Integer diaMes_ = dia.get(Calendar.DAY_OF_MONTH);
            if (diaMes.equals(diaMes_) && mes_.equals(mesEscala)) {
                return true;
            }
        }
        return false;
    }

    private Escala buscaEscala(int i) {
        for (Iterator<Escala> it = escalaList.iterator(); it.hasNext();) {
            Escala escala = it.next();
            if (escala.getDiaMes().equals(i)) {
                return escala;
            }
        }
        return null;
    }

    public void setSchClassID_LegendaList(HashMap<Integer, String> schClassID_LegendaList_) {
        schClassID_LegendaList = schClassID_LegendaList_;
    }

    public List<Escala> getEscalaList() {
        return escalaList;
    }

    public void setEscalaList(List<Escala> escalaList) {
        this.escalaList = escalaList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getHasEscala() {
        return hasEscala;
    }

    public void setHasEscala(Boolean hasEscala) {
        this.hasEscala = hasEscala;
    }

    public List<Abono> getAbonoList() {
        return abonoList;
    }

    public void setAbonoList(List<Abono> abonoList) {
        this.abonoList = abonoList;
    }

    public List<Feriado> getFeriadoList() {
        return feriadoList;
    }

    public void setFeriadoList(List<Feriado> feriadoList) {
        this.feriadoList = feriadoList;
    }

    public Integer getMesEscala() {
        return mesEscala;
    }

    public void setMesEscala(Integer mesEscala) {
        this.mesEscala = mesEscala;
    }

    public Afastamento getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Afastamento afastamento) {
        this.afastamento = afastamento;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }
}
