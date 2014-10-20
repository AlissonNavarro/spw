/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ListaPresenca;

import Metodos.Metodos;
import ConsultaPonto.Jornada;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;


public class ListaPresencaTotalBean implements Serializable {

    private List<SelectItem> departamentosSelecItem;
    private List<SelectItem> filtroSelecItem;
    private List<ListaPresenca> listaPresencaList;
    private String filtroNomePesquisa = "";
    private Integer departamentoSelecionado;
    private Integer filtroSelecionado = 1;
    private Integer page;
    private Integer totalFuncionarios;
    private Integer totalPresentes;
    private Integer totalAusentes;
    private Locale objLocale;
    private Date data;
    private String hora;
    private Date horaData;
    private UsuarioBean usuarioBean;
    private Boolean incluirSubSetores;
    private List<SelectItem> funcionarioList;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;

    public ListaPresencaTotalBean() {
        inicializarAtributos();
        departamentosSelecItem = new ArrayList<SelectItem>();
        objLocale = new Locale("pt", "BR");
        data = new Date();
        getHoraAtual();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        consultaDepartamentos();
    }

    public ListaPresencaTotalBean(String xc) {
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void getHoraAtual() {
        Date data_ = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(data_);
        Integer horaIn = calendar.get(Calendar.HOUR_OF_DAY);
        String horaInSrt = "";
        if (horaIn < 10) {
            horaInSrt = "0" + horaIn;
        } else {
            horaInSrt = horaIn.toString();
        }
        String minutoInSrt = "";
        Integer minutosIn = calendar.get(Calendar.MINUTE);
        if (minutosIn < 10) {
            minutoInSrt = "0" + minutosIn;
        } else {
            minutoInSrt = minutosIn.toString();
        }
        hora = horaInSrt + ":" + minutoInSrt;
    }

    public void consultar() {
        listaPresencaList = new ArrayList<ListaPresenca>();
        if (validarHora(hora)) {
            page = 0;
            Time time = Time.valueOf(hora + ":00");
            horaData = new Date();
            horaData.setTime(data.getTime() + time.getTime() - 10800000l);
            Banco banco = new Banco();
            List<Integer> funcionarioList_ = getFuncionarios();
            listaPresencaList = banco.consultaDadosPontoTotal(departamentoSelecionado, funcionarioList_, data, horaData, filtroNomePesquisa, incluirSubSetores);
            //listaPresencaList = getPresenca(listaPresencaList);

            List<ListaPresenca> listaPresencaAuxList = new ArrayList<ListaPresenca>();

            totalFuncionarios = 0;
            totalPresentes = 0;
            totalAusentes = 0;

            totalFuncionarios = listaPresencaList.size();

            for (Iterator<ListaPresenca> it = listaPresencaList.iterator(); it.hasNext();) {
                ListaPresenca listaPresenca = it.next();

                if (filtroSelecionado == 3) {
                    if (listaPresenca.getUltimoRegistro().after(data) && listaPresenca.getUltimoRegistro().before(horaData)) {
                        listaPresenca.setSituacao("Presente");
                        listaPresencaAuxList.add(listaPresenca);
                        totalPresentes++;
                    } else {
                        listaPresenca.setSituacao("Ausente");
                        listaPresencaAuxList.add(listaPresenca);
                        totalAusentes++;
                    }
                }
                if (filtroSelecionado == 1) {
                    if (listaPresenca.getUltimoRegistro().after(data) && listaPresenca.getUltimoRegistro().before(horaData)) {
                        listaPresenca.setSituacao("Presente");
                        totalPresentes++;
                        listaPresencaAuxList.add(listaPresenca);
                    } else {
                        totalAusentes++;
                    }
                }
                if (filtroSelecionado == 2) {
                    if (!(listaPresenca.getUltimoRegistro().after(data) && listaPresenca.getUltimoRegistro().before(horaData))) {
                        listaPresenca.setSituacao("Ausente");
                        listaPresencaAuxList.add(listaPresenca);
                        totalAusentes++;
                    } else {
                        totalPresentes++;
                    }
                }
            }

            listaPresencaList.clear();
            listaPresencaList = listaPresencaAuxList;

            if (listaPresencaList.isEmpty()) {
                FacesMessage msgErro = new FacesMessage("Nenhum funcionário encontrado.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                listaPresencaList = new ArrayList<ListaPresenca>();
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Hora Inválida.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            getHoraAtual();
        }
    }

    private Boolean validarHora(String hora_) {
        if ((hora_.length() == 5) && !hora_.contains("_")) {
            return true;
        } else {
            return false;
        }
    }

    public List<ListaPresenca> getPresenca(List<ListaPresenca> listaPresencaList) {

        List<ListaPresenca> listaPresencaSaida = new ArrayList<ListaPresenca>();

        for (Iterator<ListaPresenca> it = listaPresencaList.iterator(); it.hasNext();) {

            ListaPresenca listaPresenca = it.next();
            if (listaPresenca.getUltimoRegistro().after(data) && listaPresenca.getUltimoRegistro().before(horaData)) {
                listaPresenca.setSituacao("Presente");
            } else {
                listaPresenca.setSituacao("Ausente");
            }
            listaPresencaSaida.add(listaPresenca);
        }
        return listaPresencaSaida;
    }

    public Date timeToDate(Long tempo) {

        Date dateCheckInLimiteAntecipada = data;
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String hora_ = sdfHora.format(tempo);
        Time time = Time.valueOf(hora_);
        Date newData = new Date();
        newData.setTime(dateCheckInLimiteAntecipada.getTime() + time.getTime() - 10800000l);
        return newData;
    }

    public Jornada getPontoAnterior(List<Jornada> jornadaList) {

        List<String> horasList = new ArrayList<String>();

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (jornada.getInicioJornada() != null) {
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
                String hora_ = sdfHora.format(jornada.getCheckInLimiteAntecipada().getTime());
                horasList.add(hora_);
            }
        }

        List<Long> horasTimes = new ArrayList<Long>();
        for (Iterator<String> it = horasList.iterator(); it.hasNext();) {
            String _hora = it.next();
            Time time = Time.valueOf(_hora);
            horasTimes.add(time.getTime());
        }

        Time timeHora = Time.valueOf(hora + ":00");
        Long horaProcuradaLong = 0l;

        for (Iterator<Long> it = horasTimes.iterator(); it.hasNext();) {
            Long long1 = it.next();
            if (long1 <= timeHora.getTime()) {
                horaProcuradaLong = long1;
            }
        }

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String horaProcuradaString = sdfHora.format(horaProcuradaLong);

        Jornada jornada = new Jornada();

        jornada = getJornadaCerta(horaProcuradaString, jornadaList);

        return jornada;
    }

    private Jornada getJornadaCerta(String hora, List<Jornada> jornadaList) {
        Jornada jornadaCerta = new Jornada();

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (jornada.getInicioJornada() != null) {
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
                String hora_ = sdfHora.format(jornada.getCheckInLimiteAntecipada().getTime());
                if (hora_.equals(hora)) {
                    jornadaCerta = jornada;
                }
            }
        }
        return jornadaCerta;
    }

    public void consultaFuncionario() {
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList = banco.consultaFuncionario2(departamentoSelecionado, incluirSubSetores);
  
            funcionarioList = filtrarFuncionario();
      
    }

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
         tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        iniciarOpcoesFiltro();
    }

    private void iniciarOpcoesFiltro() {
        gestorFiltroFuncionarioList = getOpcaoFiltroGestor();
        Banco b = new Banco();
        regimeOpcaoFiltroFuncionarioList = b.getRegimeSelectItem();
        cod_funcionarioRegimeHashMap = b.getcod_funcionarioRegime();

    }

    private List<SelectItem> getOpcaoFiltroGestor() {
        List<SelectItem> tipoFiltroGestorList = new ArrayList<SelectItem>();
        tipoFiltroGestorList.add(new SelectItem(-1, "TODOS"));
        tipoFiltroGestorList.add(new SelectItem(0, "GESTORES SUBORDINADOS AO DEPARTAMENTO"));
        tipoFiltroGestorList.add(new SelectItem(1, "GESTORES SUBORDINADOS DIRETAMENTE AO DEPARTAMENTO"));

        return tipoFiltroGestorList;
    }

    private List<SelectItem> filtrarFuncionario() {
        Banco b = new Banco();
        cod_funcionarioGestorHashMap = b.getcod_funcionarioSubordinacaoDepartamento(departamentoSelecionado);
        List<SelectItem> funcionarioList_ = new ArrayList<SelectItem>();

        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario = it.next();
            if (!funcionario.getValue().toString().equals("-1")) {
                Boolean criterioRegime = isFuncionarioDentroCriterioRegime(funcionario);
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario);

                if (criterioGestor && criterioRegime) {
                    funcionarioList_.add(funcionario);
                }
            } else {
                funcionarioList_.add(funcionario);
            }
        }
        return funcionarioList_;
    }

    private Boolean isFuncionarioDentroCriterioRegime(SelectItem funcionarioSelectItem) {
        Boolean criterioRegime = false;
        Integer regime = cod_funcionarioRegimeHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        criterioRegime = (regimeSelecionadoOpcaoFiltroFuncionario == -1) || regime.equals(regimeSelecionadoOpcaoFiltroFuncionario);

        return criterioRegime;
    }

    private Boolean isFuncionarioDentroCriterioGestor(SelectItem funcionarioSelectItem) {
        Integer tipoGestor = cod_funcionarioGestorHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        Boolean criterioGestor = false;
        if (tipoGestorSelecionadoOpcaoFiltroFuncionario == -1) {
            criterioGestor = true;
        } else if (tipoGestorSelecionadoOpcaoFiltroFuncionario == 0) {
            if (tipoGestor >= 0) {
                criterioGestor = true;
            }
        } else {
            if (tipoGestor == 1) {
                criterioGestor = true;
            }
        }
        return criterioGestor;
    }

    private List<Integer> getFuncionarios() {
        List<Integer> funcionarioList_ = new ArrayList<Integer>();
        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario = it.next();
            Integer matricula = Integer.parseInt(funcionario.getValue().toString());
            if (!(matricula.equals(-1) || matricula.equals(0))) {
                funcionarioList_.add(matricula);
            }
        }
        return funcionarioList_;
    }

    public void voltar() {
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        //    FacesContext.getCurrentInstance().getExternalContext().redirect("/www/consultaFrequenciaAdmin.jsp?param=clear");

    }

    public void imprimir() throws JRException, Exception {

        List<Relatorio> relatorioList = new ArrayList<Relatorio>();
        for (Iterator<ListaPresenca> it = listaPresencaList.iterator(); it.hasNext();) {
            ListaPresenca listaPresenca = it.next();
            String cpf = listaPresenca.getCpf();
            String nome = listaPresenca.getNome();
            Date ultimoRegistro = listaPresenca.getUltimoRegistro();
            String situacao = listaPresenca.getSituacao();
            Relatorio relatorio = new Relatorio(cpf, nome, ultimoRegistro, situacao);
            relatorioList.add(relatorio);
        }
        Banco banco = new Banco();
        banco.insertRelatorio(relatorioList);
        banco = new Banco();
        String departamento = "";
        if (departamentoSelecionado == -1) {
            departamento = "Todos os departamentos";
        } else {
            departamento = banco.consultaDepartamentoo(departamentoSelecionado);
        }

        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String dataSrt = sdfHora.format(data.getTime());
        String filtro = sdfHora.format(data.getTime());

        if (filtroSelecionado == 3) {
            filtro = "Todos os funcionários";
        } else if (filtroSelecionado == 1) {
            filtro = "Presentes";
        } else if (filtroSelecionado == 2) {
            filtro = "Ausentes";
        }
        Metodos.setLogInfo("Impressao de Lista de presenca - Departamento: \"" + departamento.replaceAll("&nbsp;", "") + "\" Data: " + dataSrt + " " + hora + " Filtro: \"" + filtro + "\"");
        Impressao.geraRelatorio(departamento, dataSrt, hora, filtro, totalFuncionarios, totalPresentes, totalAusentes);
    }

    public List<ListaPresenca> getListaPresencaList() {
        return listaPresencaList;
    }

    public void setListaPresencaList(List<ListaPresenca> listaPresencaList) {
        this.listaPresencaList = listaPresencaList;
    }

    public Integer getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Integer departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public List<SelectItem> getDepartamentosSelecItem() {
        return departamentosSelecItem;
    }

    public void setDepartamentosSelecItem(List<SelectItem> departamentosSelecItem) {
        this.departamentosSelecItem = departamentosSelecItem;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<SelectItem> getFiltroSelecItem() {
        filtroSelecItem = new ArrayList<SelectItem>();

        filtroSelecItem.add(new SelectItem(1, "Presentes"));
        filtroSelecItem.add(new SelectItem(2, "Ausentes"));
        filtroSelecItem.add(new SelectItem(3, "Todos"));
        return filtroSelecItem;
    }

    public void setFiltroSelecItem(List<SelectItem> filtroSelecItem) {
        this.filtroSelecItem = filtroSelecItem;
    }

    public Integer getFiltroSelecionado() {
        return filtroSelecionado;
    }

    public void setFiltroSelecionado(Integer filtroSelecionado) {
        this.filtroSelecionado = filtroSelecionado;
    }

    public String getFiltroNomePesquisa() {
        return filtroNomePesquisa;
    }

    public void setFiltroNomePesquisa(String filtroNomePesquisa) {
        this.filtroNomePesquisa = filtroNomePesquisa;
    }

    public Integer getTotalAusentes() {
        return totalAusentes;
    }

    public void setTotalAusentes(Integer totalAusentes) {
        this.totalAusentes = totalAusentes;
    }

    public Integer getTotalFuncionarios() {
        return totalFuncionarios;
    }

    public void setTotalFuncionarios(Integer totalFuncionarios) {
        this.totalFuncionarios = totalFuncionarios;
    }

    public Integer getTotalPresentes() {
        return totalPresentes;
    }

    public void setTotalPresentes(Integer totalPresentes) {
        this.totalPresentes = totalPresentes;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public HashMap<Integer, Integer> getCod_funcionarioRegimeHashMap() {
        return cod_funcionarioRegimeHashMap;
    }

    public void setCod_funcionarioRegimeHashMap(HashMap<Integer, Integer> cod_funcionarioRegimeHashMap) {
        this.cod_funcionarioRegimeHashMap = cod_funcionarioRegimeHashMap;
    }

    public List<SelectItem> getRegimeOpcaoFiltroFuncionarioList() {
        return regimeOpcaoFiltroFuncionarioList;
    }

    public void setRegimeOpcaoFiltroFuncionarioList(List<SelectItem> regimeOpcaoFiltroFuncionarioList) {
        this.regimeOpcaoFiltroFuncionarioList = regimeOpcaoFiltroFuncionarioList;
    }

    public Integer getRegimeSelecionadoOpcaoFiltroFuncionario() {
        return regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public void setRegimeSelecionadoOpcaoFiltroFuncionario(Integer regimeSelecionadoOpcaoFiltroFuncionario) {
        this.regimeSelecionadoOpcaoFiltroFuncionario = regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public HashMap<Integer, Integer> getCod_funcionarioGestorHashMap() {
        return cod_funcionarioGestorHashMap;
    }

    public void setCod_funcionarioGestorHashMap(HashMap<Integer, Integer> cod_funcionarioGestorHashMap) {
        this.cod_funcionarioGestorHashMap = cod_funcionarioGestorHashMap;
    }

    public List<SelectItem> getGestorFiltroFuncionarioList() {
        return gestorFiltroFuncionarioList;
    }

    public void setGestorFiltroFuncionarioList(List<SelectItem> gestorFiltroFuncionarioList) {
        this.gestorFiltroFuncionarioList = gestorFiltroFuncionarioList;
    }

    public Integer getTipoGestorSelecionadoOpcaoFiltroFuncionario() {
        return tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public void setTipoGestorSelecionadoOpcaoFiltroFuncionario(Integer tipoGestorSelecionadoOpcaoFiltroFuncionario) {
        this.tipoGestorSelecionadoOpcaoFiltroFuncionario = tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public static void main(String[] x) {
        ListaPresencaTotalBean d = new ListaPresencaTotalBean("");
        d.setData(new Date());
        d.setHora("08:00");
        d.setDepartamentoSelecionado(2);
        d.consultar();
    }
}
