/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import Abono.Abono;
import entidades.Afastamento;
import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import ConsultaPonto.Jornada;
import Usuario.Usuario;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import Metodos.Metodos;
import java.util.Locale;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import manageBean.CategoriaAfastamentoMB;
import entidades.CategoriaAfastamento;

/**
 *
 * @author amsgama
 */
public class RelatorioResumoEscalaBean implements Serializable {

    private Integer mesSelecionado;
    private List<SelectItem> mesesSelecItem;
    private String departamentoSelecionado;
    private List<SelectItem> departamentosSelecItem;
    private Locale objLocale;
    private Boolean incluirSubSetores;
    private Integer ano;
    private List<Usuario> usuarioList;

    public RelatorioResumoEscalaBean() {
        departamentosSelecItem = new ArrayList<SelectItem>();
        usuarioList = new ArrayList<Usuario>();
        objLocale = new Locale("pt", "BR");
        consultaDepartamentos();
        consultaMes();
        ano = getAnoAtual();
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
    }

    public void consultaMes() {
        mesesSelecItem = new ArrayList<SelectItem>();
        mesesSelecItem.add(new SelectItem("0", "Janeiro"));
        mesesSelecItem.add(new SelectItem("1", "Fevereiro"));
        mesesSelecItem.add(new SelectItem("2", "Março"));
        mesesSelecItem.add(new SelectItem("3", "Abril"));
        mesesSelecItem.add(new SelectItem("4", "Maio"));
        mesesSelecItem.add(new SelectItem("5", "Junho"));
        mesesSelecItem.add(new SelectItem("6", "Julho"));
        mesesSelecItem.add(new SelectItem("7", "Agosto"));
        mesesSelecItem.add(new SelectItem("8", "Setembro"));
        mesesSelecItem.add(new SelectItem("9", "Outubro"));
        mesesSelecItem.add(new SelectItem("10", "Novembro"));
        mesesSelecItem.add(new SelectItem("11", "Dezembro"));
    }

    public void consultar() {

        List<RelatorioResumoEscala> relatorioResumoEscalaList = new ArrayList<RelatorioResumoEscala>();
        List<Jornada> jornadaList = new ArrayList<Jornada>();
        List<Afastamento> afastamentoList = new ArrayList<Afastamento>();
        usuarioList = new ArrayList<Usuario>();

        if (!(departamentoSelecionado.equals("-1"))) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            List<Date> extremosMes = getExtremosMes();
            Date dataInicio_ = (Date) extremosMes.get(0);
            Date dataFim_ = (Date) extremosMes.get(1);

            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            List<Integer> matriculasList = new ArrayList<Integer>();
            Banco banco = new Banco();
            matriculasList = banco.consultaFuncionarioDepartamento(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
            int i = 0;
            int j = 0;
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                extremosMes = getExtremosMes();
                dataInicio_ = (Date) extremosMes.get(0);
                dataFim_ = (Date) extremosMes.get(1);
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio_);
                c.setDataFim(dataFim_);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                c.consultaDiasSemMsgErro();
                RelatorioResumoEscala relatorioResumoEscala = new RelatorioResumoEscala(c.getNome(), c.getEscalasList());
                if (!c.getDiasList().isEmpty()) {
                    List<Jornada> jornadaList_ = getJornadaDiasList(c.getDiasList());
                    jornadaList.addAll(jornadaList_);

                    relatorioResumoEscala.setAbonoList(c.getAbonoList());
                    relatorioResumoEscala.setFeriadoList(c.getFeriadoList());
                    relatorioResumoEscala.setMesEscala(mesSelecionado);
                    relatorioResumoEscala.setAno(ano);
                    relatorioResumoEscala.setAfastamento(c.getAfastamento());
                    if (c.getAfastamento() != null) {
                        afastamentoList.add(c.getAfastamento());
                    }
                    relatorioResumoEscala.setHasEscala(true);
                    relatorioResumoEscalaList.add(relatorioResumoEscala);
                    i++;
                } else {
                    relatorioResumoEscala.setHasEscala(false);
                    relatorioResumoEscalaList.add(relatorioResumoEscala);
                    j++;
                }
            }
            banco = new Banco();
            banco.insertRelatorioResumoEscala(relatorioResumoEscalaList);

            if (!relatorioResumoEscalaList.isEmpty()) {
                String deptStr = Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem);
                String mesStr = Metodos.buscaRotulo(mesSelecionado.toString(), mesesSelecItem);
                Metodos.setLogInfo(" Relatorio de Resumo de Escalas - Departamento: \"" + deptStr.replaceAll("&nbsp;", "") + "\". Mês: " + mesStr + "\\" + ano.toString());

                String legendasSigla = getLegendasSigla(jornadaList);
                String legendasNumero = getLegendasNumero(jornadaList);
                String legendasAfastamento = getLegendasAfastamento(afastamentoList);
                imprimir(legendasSigla, legendasNumero, legendasAfastamento);
            } else {
                FacesMessage msgErro = new FacesMessage(" Não existem escalas a serem geradas");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage(" Nenhum departamento selecionado. Selecione um departamento!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    private void imprimir(String legendaSigla, String legendaNumero, String legendaAfastamento) {
        List<String> diasDoMesList = gerarCabelhacoDiasDoMes();
        List<String> diasDaSemanaList = gerarCabelhacoDiasDaSemana();
        String dataHora = getDataHoraAtual();
        String mesAno = getMesAno();
        String departamento = getDept();

        try {
            Impressao.geraRelatorioResumoEscala(diasDoMesList, diasDaSemanaList, dataHora, mesAno, departamento, legendaSigla,
                    legendaNumero, legendaAfastamento);
        /*} catch (ClassNotFoundException ex) {
            Logger.getLogger(RelatorioResumoEscalaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioResumoEscalaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(RelatorioResumoEscalaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RelatorioResumoEscalaBean.class.getName()).log(Level.SEVERE, null, ex);*/
        } catch (Exception ex) {
            System.out.println("RelatorioMensal: imprimir: "+ex);
            //Logger.getLogger(RelatorioResumoEscalaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Jornada> getJornadaDiasList(List<DiaComEscala> diasComEscalaList) {
        List<Jornada> jornadaList = new ArrayList<Jornada>();
        for (Iterator<DiaComEscala> it = diasComEscalaList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            jornadaList.addAll(diaComEscala.getJornadaList());
        }
        return jornadaList;
    }

    private List<String> gerarCabelhacoDiasDoMes() {
        List<String> diasDoMesList = new ArrayList<String>();
        Calendar cal = new GregorianCalendar(ano, mesSelecionado, 1);
        Integer fimMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= 31; i++) {
            if (i <= fimMes) {
                diasDoMesList.add(new Integer(i).toString());
            } else {
                diasDoMesList.add("");
            }
        }
        return diasDoMesList;
    }

    private List<String> gerarCabelhacoDiasDaSemana() {
        List<String> diasDoMesList = new ArrayList<String>();
        Calendar cal = new GregorianCalendar(ano, mesSelecionado, 1);
        Integer fimMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= 31; i++) {
            if (i <= fimMes) {
                cal = new GregorianCalendar(ano, mesSelecionado, i);
                int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
                diasDoMesList.add(pesquisarDiaSemana(diaSemana));
            } else {
                diasDoMesList.add("");
            }
        }
        return diasDoMesList;
    }

    public String pesquisarDiaSemana(int _diaSemana) {
        String diaSemana = null;

        switch (_diaSemana) {

            case 1: {
                diaSemana = "D";
                break;
            }
            case 2: {
                diaSemana = "S";
                break;
            }
            case 3: {
                diaSemana = "T";
                break;
            }
            case 4: {
                diaSemana = "Q";
                break;
            }
            case 5: {
                diaSemana = "Q";
                break;
            }
            case 6: {
                diaSemana = "S";
                break;
            }
            case 7: {
                diaSemana = "S";
                break;
            }

        }
        return diaSemana;

    }

    private List<Date> getExtremosMes() {
        List<Date> extremosList = new ArrayList<Date>();
        Calendar cal = new GregorianCalendar(ano, mesSelecionado, 1);
        extremosList.add(cal.getTime());
        Integer fimMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal = new GregorianCalendar(ano, mesSelecionado, fimMes);
        extremosList.add(cal.getTime());
        return extremosList;
    }

    private Integer getAnoAtual() {
        Calendar calAno = Calendar.getInstance();
        calAno.setTime(new Date());
        Integer ano_ = calAno.get(Calendar.YEAR);
        return ano_;
    }

    private String getDataHoraAtual() {
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataStr = sdf.format(data.getTime());
        return dataStr;
    }

    private String getMesAno() {
        String mesAno = getMes(mesSelecionado);
        mesAno += "/" + getAnoAtual();
        return mesAno;
    }

    private String getMes(Integer mesInt) {
        String mes;
        switch (mesInt) {
            case 0:
                mes = "Janeiro";
                break;
            case 1:
                mes = "Fevereiro";
                break;
            case 2:
                mes = "Março";
                break;
            case 3:
                mes = "Abril";
                break;
            case 4:
                mes = "Maio";
                break;
            case 5:
                mes = "Junho";
                break;
            case 6:
                mes = "Julho";
                break;
            case 7:
                mes = "Agosto";
                break;
            case 8:
                mes = "Setembro";
                break;
            case 9:
                mes = "Outubro";
                break;
            case 10:
                mes = "Novembro";
                break;
            default:
                mes = "Dezembro";
                break;
        }
        return mes;
    }

    private String getDept() {
        for (Iterator<SelectItem> it = departamentosSelecItem.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            if (departamentoSelecionado.equals(selectItem.getValue().toString())) {
                return selectItem.getLabel().replace("&nbsp;", "");
            }
        }
        return null;
    }

    private String getLegendasSigla(List<Jornada> jornadaList) {
        jornadaList = ordenarJornadaPorSchClassID(jornadaList);

        List<Integer> schClassIDList = new ArrayList<Integer>();
        String legenda = "";

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            Integer schClassID = jornada.getSchClassID();
            if (!schClassIDList.contains(schClassID)) {
                schClassIDList.add(schClassID);
                String legenda_ = jornada.getLegendaSigla();
                if (!legenda_.equals("")) {
                    legenda += legenda_ + ", ";
                }
            }
        }
        if (legenda.length() > 2) {
            legenda = legenda.substring(0, legenda.length() - 2);
        }
        return legenda;
    }

    private String getLegendasNumero(List<Jornada> jornadaList) {
        jornadaList = ordenarJornadaPorSchClassID(jornadaList);

        List<Integer> schClassIDList = new ArrayList<Integer>();
        String legenda = "";

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            Integer schClassID = jornada.getSchClassID();
            String legenda_ = jornada.getLegendaSigla();
            if (!schClassIDList.contains(schClassID) && (legenda_.equals(""))) {
                schClassIDList.add(schClassID);
                legenda += jornada.getLegendaNumero() + ", ";
            }
        }
        if (legenda.length() > 3) {
            legenda = legenda.substring(0, legenda.length() - 2);
        }
        return legenda;
    }

    private String getLegendasAfastamento(List<Afastamento> afastamentoList) {

        List<String> legendaList = new ArrayList<String>();
        String legenda = "";

        CategoriaAfastamentoMB caMB = new CategoriaAfastamentoMB();
        CategoriaAfastamento ca;
        
        for (Iterator<Afastamento> it = afastamentoList.iterator(); it.hasNext();) {
            Afastamento afastamento = it.next();
            ca = caMB.consultaCategoriaAfastamento(afastamento.getCodCategoriaAfastamento());
            //String legenda_ = afastamento.getCategoriaAfastamento().getLegenda();
            String legenda_ = ca.getLegenda();
            if (!legendaList.contains(legenda_)&&!ca.getLegenda().equals("X")) {
                legendaList.add(legenda_);
                legenda += ca.getSignificadoLegenda() + ", ";
            }
        }
        if (legenda.length() > 2) {
            legenda = legenda.substring(0, legenda.length() - 2);
        }
        return legenda;
    }

    private String getLegendasAfastamentoAntigo(List<Abono> abonoList) {
        abonoList = ordenarAbonoPorCod(abonoList);
        List<Integer> cod_abonoList = new ArrayList<Integer>();
        String legenda = "";

        for (Iterator<Abono> it = abonoList.iterator(); it.hasNext();) {
            Abono abono = it.next();
            Integer cod_abono = abono.getCod();
            if (!cod_abonoList.contains(cod_abono)) {
                cod_abonoList.add(cod_abono);
                legenda += abono.getLegenda() + ", ";
            }
        }
        if (legenda.length() > 2) {
            legenda = legenda.substring(0, legenda.length() - 2);
        }
        return legenda;
    }

    private List<Jornada> ordenarJornadaPorSchClassID(List<Jornada> jornadaList) {
        Collections.sort(jornadaList, new Comparator() {

            public int compare(Object o1, Object o2) {
                Jornada p1 = (Jornada) o1;
                Jornada p2 = (Jornada) o2;
                return p1.getSchClassID() > p2.getSchClassID() ? +1
                        : (p1.getSchClassID() < p2.getSchClassID() ? -1 : 0);
            }
        });
        return jornadaList;
    }

    private List<Abono> ordenarAbonoPorCod(List<Abono> abonoList) {
        Collections.sort(abonoList, new Comparator() {

            public int compare(Object o1, Object o2) {
                Abono p1 = (Abono) o1;
                Abono p2 = (Abono) o2;
                return p1.getCod() > p2.getCod() ? +1
                        : (p1.getCod() < p2.getCod() ? -1 : 0);
            }
        });
        return abonoList;
    }

    public String getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(String departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public List<SelectItem> getDepartamentosSelecItem() {
        return departamentosSelecItem;
    }

    public void setDepartamentosSelecItem(List<SelectItem> departamentosSelecItem) {
        this.departamentosSelecItem = departamentosSelecItem;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void teste() {
        System.out.print(ano);
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Integer getMesSelecionado() {
        return mesSelecionado;
    }

    public void setMesSelecionado(Integer mesSelecionado) {
        this.mesSelecionado = mesSelecionado;
    }

    public List<SelectItem> getMesesSelecItem() {
        return mesesSelecItem;
    }

    public void setMesesSelecItem(List<SelectItem> mesesSelecItem) {
        this.mesesSelecItem = mesesSelecItem;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }
}
