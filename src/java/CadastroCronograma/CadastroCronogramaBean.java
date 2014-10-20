/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroCronograma;

import CadastroJornada.DiaJornada;
import CadastroJornada.HorariosXdia;
import CadastroJornada.Turno;
import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DescolamentoTemporario;
import ConsultaPonto.Escala;
import ConsultaPonto.Feriado;
import ConsultaPonto.PeriodoJornada;
import ConsultaPonto.Turnos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;
import ConsultaPonto.RelatorioPortaria1510CabecalhoResumo;
import Funcionario.Funcionario;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.richfaces.model.selection.SimpleSelection;

/**
 *
 * @author Amvboas
 */
public class CadastroCronogramaBean implements Serializable {

    private List<SelectItem> departamentolist;
    private List<SelectItem> funcionarioList;
    private List<Funcionario> funcionarioObjList;
    private Integer cod_funcionario;
    private List<Cronograma> cronogramasList;
    private SimpleSelection cronogramaSelecionado;
    private List<SelectItem> jornadasList;
    private Integer jornadaSelecionada;
    private String jornadaSelecionadas;
    private List<DiaCronograma> diasCronogramaList;
    private List<DiaCronograma> diasDeslocamentos;
    private List<Turno> horariosList;
    private Locale objLocale;
    private UsuarioBean usuarioBean;
    private Date dataInicio;
    private Date dataFim;
    private Date dataInicioJornada;
    private Date dataInicioNewJornada;
    private Date dataFimNewJornada;
    private Date dataInicioEditJornada;
    private Date dataFimEditJornada;
    private Integer jornadaEditSelecionada;
    private Date dataFimJornada;
    private Cronograma cronogramaAnterior;
    private Boolean overtime;
    private Boolean visualOvertime;
    private Boolean isAdministradorVisivel;
    private Integer departamento;
    private Boolean incluirSubSetores;
    private String tituloDeslocTemp;
    private ConsultaFrequenciaComEscalaBean c;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    private List<Jornada> jornadaList;
    private boolean isOverTime = false;
    private boolean showDiasJornada = false;
    private List<DiaJornada> diasJornadaList;

    public CadastroCronogramaBean() {
        resetPage();
    }

    public void resetPage() {
        inicializarAtributos();
        tituloDeslocTemp = "";
        departamentolist = new ArrayList<SelectItem>();
        if ((funcionarioList == null) || (funcionarioList.size() <= 1)) {
            funcionarioList = new ArrayList<SelectItem>();
            funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        }
        dataInicio = getPrimeiroDiaMes();
        dataInicioJornada = getPrimeiroDiaMes();
        dataFim = getLastDay();
        dataFimJornada = getLastDay();
        overtime = false;
        visualOvertime = false;
        objLocale = new Locale("pt", "BR");
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (usuarioBean != null) {
            consultaDepartamento(usuarioBean.getUsuario().getPermissao());
        }
        Locale.setDefault(new Locale("pt", "BR"));
        geraJornadasList();
    }

    public void getAbaCorrente() {
        int u = 0;
    }

    public void showAdicionarJornada() {
        dataInicioNewJornada = getPrimeiroDiaMes();
        dataFimNewJornada = getLastDay();
        jornadaSelecionadas = "";
        diasJornadaList = new ArrayList<DiaJornada>();
        geraJornadasList();
    }

    public void showEditarJornada() {
        Integer index = -1;
        for (Iterator<Object> it = cronogramaSelecionado.getKeys(); it.hasNext();) {
            index = (Integer) it.next();
        }

        dataInicioEditJornada = cronogramasList.get(index).getInicio();
        dataFimEditJornada = cronogramasList.get(index).getFim();
        jornadaEditSelecionada = cronogramasList.get(index).id_jornada;
        cronogramaAnterior = cronogramasList.get(index);
        geraJornadasList();
        jornadaSelecionadas = jornadaEditSelecionada.toString();
        showDiasJornada();
    }

    public void showDiasJornada() {
        showDiasJornada = true;
        Integer selecionada;
        if (jornadaSelecionadas != "") {
            selecionada = Integer.parseInt(jornadaSelecionadas);
        } else {
            selecionada = 0;
        }
        ArrayList<HorariosXdia> horariosXdias = new ArrayList<HorariosXdia>();
        Banco banco = new Banco();
        horariosXdias = banco.consultaHorariosXJornada(selecionada);

        diasJornadaList = new ArrayList<DiaJornada>();
        HashMap<Integer, ArrayList<HorariosXdia>> horariosXjornadaCompleta = new HashMap<Integer, ArrayList<HorariosXdia>>();

        //Gera o número de dias
        Integer unidade = horariosXdias.get(0).getUnits();
        Integer nroCiclos = horariosXdias.get(0).getCyle();
        Integer deslocI = 0;
        if (horariosXdias.get(0).getSaida() == null) {
            horariosXdias.remove(0);
        }

        if (unidade == 0) {
            for (int i = 0; i <= nroCiclos; i++) {
                horariosXjornadaCompleta.put(i, new ArrayList<HorariosXdia>());
            }

        } else if (unidade == 2) {
            for (int i = 0; i <= nroCiclos * 31; i++) {
                horariosXjornadaCompleta.put(i, new ArrayList<HorariosXdia>());
            }
        } else {
            for (int i = 1; i <= nroCiclos * 7; i++) {
                horariosXjornadaCompleta.put(i, new ArrayList<HorariosXdia>());
            }
            deslocI = 1;
        }
        //
        //Coloca os Horários nos dias
        for (int i = 0; i < horariosXdias.size(); i++) {
            HorariosXdia horariosXdia = horariosXdias.get(i);
            Integer dia = horariosXdia.getDia();
            ArrayList<HorariosXdia> hXd_aux = horariosXjornadaCompleta.get(dia);
            hXd_aux.add(horariosXdia);
            horariosXjornadaCompleta.put(dia, hXd_aux);
        }

        //Monta tabela de Apresentação
        int qntCiclos = 0;
        if (unidade == 0) {
            qntCiclos = nroCiclos;
        } else if (unidade == 2) {
            qntCiclos = nroCiclos * 31;
        } else {
            qntCiclos = nroCiclos * 7;
        }
        for (int i = 0 + deslocI; i < qntCiclos + deslocI; i++) {
            ArrayList<HorariosXdia> hXd_aux = horariosXjornadaCompleta.get(i);
            DiaJornada dj_aux = new DiaJornada(hXd_aux, i, unidade);
            diasJornadaList.add(dj_aux);
        }
    }

    public void salvarNovaJornada() {

        Boolean valido = true;
        int i = 0;

        dataInicioJornada = getDataInicio();
        dataFimJornada = getDataFim();

        jornadaSelecionada = Integer.parseInt(jornadaSelecionadas);
        if (jornadaSelecionada == 0) {
            FacesMessage msgErro = new FacesMessage("Jornada Inválida");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (dataFimNewJornada.before(dataInicioNewJornada)) {
                valido = false;
            }
            while (valido && i < cronogramasList.size()) {
                if (cronogramasList.get(i).getInicio().before(dataInicioNewJornada)) {
                    if (cronogramasList.get(i).getFim().after(dataInicioNewJornada)) {
                        valido = false;
                    }
                } else if (cronogramasList.get(i).getInicio().before(dataFimNewJornada)) {
                    valido = false;
                }
                i++;
            }
            if (valido) {

                // setLogCronograma("Adicionar", jornadaSelecionada);
                Banco banco = new Banco();
                valido = banco.addJornada(cod_funcionario, jornadaSelecionada, dataInicioNewJornada, dataFimNewJornada);

                if (valido) {
                    FacesMessage msgErro = new FacesMessage("Jornada adicionada com sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    FacesMessage msgErro = new FacesMessage("Dados inválidos");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
                consultaCronogramas();

            } else {
                FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            cronogramaSelecionado = null;
        }
    }

    public void salvarNovaJornadaGroup() {

        Boolean valido = true;

        if (jornadaSelecionada == 0) {
            FacesMessage msgErro = new FacesMessage("Jornada não selecionada!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (dataFimJornada.before(dataInicioJornada)) {
                valido = false;
            }
            if (valido) {
                //setLogCronograma("Adicionar", jornadaSelecionada);
                Banco banco = new Banco();
                jornadaList = banco.getJornadas(funcionarioList);
                List<Jornada> jornadaAtualList = getJornadaList(jornadaList, jornadaSelecionada, dataInicioJornada, dataFimJornada);
                if (jornadaAtualList.isEmpty()) {
                    FacesMessage msgErro = new FacesMessage("Não há jornadas as serem alteradas");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    valido = banco.inserirJornadasGrupo(jornadaAtualList);
                    jornadaList = banco.getJornadas(funcionarioList);
                    banco.fechaConexao();
                    if (valido) {
                        FacesMessage msgErro = new FacesMessage("Jornadas adicionada com sucesso");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    } else {
                        FacesMessage msgErro = new FacesMessage("Dados inválidos");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    }
                    consultaCronogramas();
                }

            } else {
                FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            cronogramaSelecionado = null;
            //Metodos.setLogInfo("Adicionar a jornada " + buscaNomeJornada(jornadaSelecionada) + " do departamento " + buscaNomeDepartamento(departamento, incluirSubSetores));
        }
    }

    //<log>
    public void setLogCronograma(String operacao, Integer idjornada) {
        int j = 0;
        String nomeJornada = " ";
        while (j < jornadasList.size() && jornadasList.get(j).getValue() != idjornada) {
            j++;
        }
        if (j < jornadasList.size()) {
            nomeJornada = jornadasList.get(j).getLabel();
        }
        j = 1;
        String nomeUsuario = " ";
        while (j < funcionarioList.size() && !funcionarioList.get(j).getValue().equals(cod_funcionario.toString())) {
            j++;
        }
        if (j < funcionarioList.size()) {
            nomeUsuario = funcionarioList.get(j).getLabel();
        }

        //Metodos.setLogInfo(operacao + " Cronograma  - \"" + nomeJornada + "\" Funcionario: \"" + nomeUsuario + "\"");
        //</log>
    }

    public void setLogCronograma(String operacao, Integer idjornada, Integer cod_funcionario) {
        int j = 0;
        String nomeJornada = " ";
        while (j < jornadasList.size() && jornadasList.get(j).getValue() != idjornada) {
            j++;
        }
        if (j < jornadasList.size()) {
            nomeJornada = jornadasList.get(j).getLabel();
        }
        j = 1;
        String nomeUsuario = " ";
        while (j < funcionarioList.size() && !funcionarioList.get(j).getValue().equals(cod_funcionario.toString())) {
            j++;
        }
        if (j < funcionarioList.size()) {
            nomeUsuario = funcionarioList.get(j).getLabel();
        }

        //Metodos.setLogInfo(operacao + " Cronograma  - \"" + nomeJornada + "\" Funcionario: \"" + nomeUsuario + "\"");
        //</log>
    }

    /*public void setLogDeslocTemp(String operacao, Date inicio, Date fim) {
     int j = 0;
     j = 0;
     String nomeUsuario = " ";
     SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
     String iniciostr = sf.format(inicio);
     String fimstr = sf.format(fim);
             
     while (j < funcionarioList.size() && !funcionarioList.get(j).getValue().equals(cod_funcionario.toString())) {
     j++;
     }
     if (j < funcionarioList.size()) {
     nomeUsuario = funcionarioList.get(j).getLabel();
     }
     }*/
    public void salvarEditarJornada() {
        if (jornadaEditSelecionada == 0) {
            FacesMessage msgErro = new FacesMessage("Jornada Inválida");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            Banco banco = new Banco();
            boolean flag = banco.deleteCronograma(cronogramaAnterior);
            if (!flag) {
                FacesMessage msgErro = new FacesMessage("A Jornada não pode ser editada!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                cronogramasList = banco.consultaCronogramas(cod_funcionario);
                Boolean valido = true;
                int i = 0;
                if (dataFimEditJornada.before(dataInicioEditJornada)) {
                    valido = false;
                }
                while (valido && i < cronogramasList.size()) {
                    if (cronogramasList.get(i).getInicio().before(dataInicioEditJornada)) {
                        if (cronogramasList.get(i).getFim().after(dataInicioEditJornada)) {
                            valido = false;
                        }
                    } else if (cronogramasList.get(i).getInicio().before(dataFimEditJornada)) {
                        valido = false;
                    }
                    i++;
                }
                if (valido) {
                    setLogCronograma("Editar", jornadaEditSelecionada);
                    banco = new Banco();
                    banco.addJornada(cronogramaAnterior.getId_usuario(), jornadaEditSelecionada, dataInicioEditJornada, dataFimEditJornada);
                    FacesMessage msgErro = new FacesMessage("Jornada editada com sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);

                } else {
                    banco = new Banco();
                    banco.addJornada(cronogramaAnterior.getId_usuario(), cronogramaAnterior.getId_jornada(), cronogramaAnterior.getInicio(), cronogramaAnterior.getFim());
                    FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
                cronogramaSelecionado = null;
            }
        }
        consultaCronogramas();
    }

    public void excluirJornada() {
        Banco banco = new Banco();
        Cronograma cronograma = new Cronograma();
        Integer index = null;
        for (Iterator<Object> it = cronogramaSelecionado.getKeys(); it.hasNext();) {
            index = (Integer) it.next();
        }

        if (index == null) {
            FacesMessage msgErro = new FacesMessage("Nenhuma linha selecionada para remoção!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            cronograma = cronogramasList.get(index);

            boolean flag = banco.deleteCronograma(cronograma);

            if (!flag) {
                FacesMessage msgErro = new FacesMessage("A Jornada não pode ser excluida!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                setLogCronograma("Excluir", cronograma.id_jornada);
                FacesMessage msgErro = new FacesMessage("Jornada excluida com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            banco = new Banco();
            cronogramasList = banco.consultaCronogramas(cod_funcionario);
            cronogramaSelecionado = null;
        }
    }

    private void geraJornadasList() {
        Banco banco = new Banco();
        jornadasList = banco.geraJornadasList();
    }

    private static Date getPrimeiroDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String dataPrimeiroDiaStr = sdfHora.format(data.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        try {
            data = df.parse(dataPrimeiroDiaStr);
        } catch (ParseException ex) {
            System.out.println("CadastroCronograma: getPrimeiroDiaMes: " + ex);
            //Logger.getLogger(ConsultaFrequenciaComEscalaBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public static Date getLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getUltimoDiaMes());
        Date data = calendar.getTime();
        return data;
    }

    public static Date getUltimoDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date data = calendar.getTime();
        return data;
    }

    public void geraDiasCronogramaList() {

        Banco banco = new Banco();
        ConsultaPonto.Banco b = new ConsultaPonto.Banco();

        diasCronogramaList = new ArrayList<DiaCronograma>();
        diasDeslocamentos = new ArrayList<DiaCronograma>();

        List<Escala> escalasList = new ArrayList<Escala>();

        GregorianCalendar d1;
        GregorianCalendar d2;
        GregorianCalendar calendar = new GregorianCalendar();

        if (dataInicio.before(dataFim) || dataInicio.equals(dataFim)) {

            calendar.setTime(dataFim);
            calendar.setTime(dataInicio);

            //gera escala do funcionario
            b = new ConsultaPonto.Banco();

            List<PeriodoJornada> listaJornadas = b.consultaPeriodoJornada(cod_funcionario, dataInicio, dataFim);
            if (listaJornadas.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                FacesMessage msgErro = new FacesMessage("Funcionário não possui jornada no intervalo " + sdf.format(dataInicio) + " a " +sdf.format(dataFim));
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            d1 = new GregorianCalendar();
            d2 = new GregorianCalendar();
            d2.setTime(dataInicio);

            for (int x = 0; x < listaJornadas.size(); x++) {
                PeriodoJornada p = listaJornadas.get(x);
                List<Turnos> listaTurnos = b.consultaTurnos(p.getNum_jornada());
                long diaAtualCiclo = -1;

                d1.setTime(p.getInicioJornada());

                while (d2.getTime().compareTo(d1.getTime()) < 0) {
                    d2.add(Calendar.DAY_OF_MONTH, 1);
                }

                long diferencaDias = ((d1.getTimeInMillis() - d2.getTimeInMillis()) / (24 * 60 * 60 * 1000)) * -1;

                if (diferencaDias < 0) {
                    FacesMessage msgErro = new FacesMessage("Falha na jornada do funcionário");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    long limite = 0;
                    boolean temHorario = false;
                    Escala e;
                    int flagInicialCiclo = -1;

                    if (p.getTipo() == 0) {
                        diaAtualCiclo = diferencaDias % p.getCiclo();
                        limite = Long.parseLong(p.getCiclo() + "") - 1;
                    }
                    if (p.getTipo() == 1) {
                        flagInicialCiclo = d1.get(Calendar.DAY_OF_WEEK);
                        diaAtualCiclo = diferencaDias % (p.getCiclo() * 7) + flagInicialCiclo - 1;
                        if (diaAtualCiclo > (p.getCiclo() * 7)) {
                            diaAtualCiclo = diaAtualCiclo - (p.getCiclo() * 7);
                        }
                        limite = Long.parseLong((p.getCiclo() * 7) + "");
                    }
                    if (p.getTipo() == 2) {
                        flagInicialCiclo = d1.get(Calendar.DAY_OF_MONTH);
                        diaAtualCiclo = diferencaDias % (p.getCiclo() * 31) + flagInicialCiclo - 1;
                        if (diaAtualCiclo > (p.getCiclo() * 31)) {
                            diaAtualCiclo = diaAtualCiclo - (p.getCiclo() * 31);
                        }
                        limite = Long.parseLong((p.getCiclo() * 31) + "");
                    }

                    for (long y = diaAtualCiclo; y <= limite; y++) {
                        List<Integer> horariosId = new ArrayList<>();
                        for (Iterator<Turnos> it = listaTurnos.iterator(); it.hasNext();) {
                            Turnos t = it.next();
                            if (t.getSdays() == y) {
                                horariosId.add(t.getSchclassid());
                                temHorario = true;
                            }
                        }
                        if (temHorario) {
                            e = new Escala(d2.get(Calendar.DAY_OF_MONTH), horariosId, d2.getTime());
                            escalasList.add(e);
                            temHorario = false;
                        }
                        d2.add(Calendar.DAY_OF_MONTH, 1);
                        if (y == limite) {
                            if (p.getTipo() == 0) {
                                y = -1;
                            } else {
                                y = 0;
                            }
                        }
                        if (p.getFim().before(dataFim)) {
                            if (d2.getTime().compareTo(p.getFim()) > 0) {
                                y = limite + 1;
                                break;
                            }
                        } else {
                            if (d2.getTime().compareTo(dataFim) > 0) {
                                y = limite + 1;
                                break;
                            }
                        }
                    }
                }
            }

            b = new ConsultaPonto.Banco();
            ArrayList<DescolamentoTemporario> listaDeslocamentos = b.consultaDeslocamentoTemp(cod_funcionario, dataInicio, dataFim);
            ArrayList<DescolamentoTemporario> listaHorariosRemover = b.consultaHorarioRemover(cod_funcionario, dataInicio, dataFim);
            Afastamento.Banco afasBD = new Afastamento.Banco();
            List<Afastamento.Afastamento> listaAfasamento = afasBD.consultaAfastamentoByFuncionario(cod_funcionario, dataInicio, dataFim);
            CadastroFeriado.Banco bFeriados = new CadastroFeriado.Banco();
            ArrayList<CadastroFeriado.Feriado> listaFeriados = bFeriados.consultaFeriadosPorData(dataInicio, dataFim);
            banco = new Banco();
            DescolamentoTemporario deslTmp;

            int ponteiroEscala = 0;

            d2 = new GregorianCalendar();
            d2.setTime(dataInicio);

            Integer ordem = 0;

            //percorre os dias alocando a escala e deslocamentos temporario no cronograma
            do {
                diasCronogramaList.add(new DiaCronograma(new ArrayList<Horario>(), calendar.getTime(), ordem));

                //aloca a escala
                if (!escalasList.isEmpty()) {
                    if (ponteiroEscala < escalasList.size()) {
                        Escala escala = escalasList.get(ponteiroEscala);
                        if (diasCronogramaList.get(ordem).getDia().getTime() == escala.getDia().getTime()) {
                            ArrayList<Horario> horariosLista = banco.consultaHorariosDia(cod_funcionario, escala);
                            diasCronogramaList.get(ordem).setHorariosList(horariosLista);
                            //variavel abaixo necessaria para nao criar a mesma referencia nas variaveis horariosLista e horariosListaBkp
                            ArrayList<Horario> horariosListaBkp = banco.consultaHorariosDia(cod_funcionario, escala);
                            diasCronogramaList.get(ordem).setHorariosListBkp(horariosListaBkp);
                            ponteiroEscala++;
                        }
                    }
                }

                //retira os horarios da escala que foram removidos por causa do deslocamento temporario
                for (Iterator<DescolamentoTemporario> dt = listaHorariosRemover.iterator(); dt.hasNext();) {
                    deslTmp = dt.next();
                    if (diasCronogramaList.get(ordem).getDia().getTime() == deslTmp.getDataInicio().getTime()) {
                        if (deslTmp.getSchClassId() == -1) {
                            diasCronogramaList.get(ordem).setIsFolga(false);
                            diasCronogramaList.get(ordem).setCssHorario("color: #B0B0B0");
                            diasCronogramaList.get(ordem).getHorariosList().clear();
                            diasCronogramaList.get(ordem).getHorarioStrList().clear();
                            diasCronogramaList.get(ordem).getHorarioStrList().add("( Horário Removido )");
                            diasCronogramaList.get(ordem).setTemHorario(false);
                            diasCronogramaList.get(ordem).setIsDeslTemp(true);
                            diasCronogramaList.get(ordem).setIsDesfazer(true);
                        }
                    }
                }

                //coloca os feriados no cronograma
                for (Iterator<CadastroFeriado.Feriado> dt = listaFeriados.iterator(); dt.hasNext();) {
                    CadastroFeriado.Feriado f = dt.next();
                    if (diasCronogramaList.get(ordem).getDia().getTime() == f.getData().getTime()) {
                        diasCronogramaList.get(ordem).getHorariosList().clear();
                        diasCronogramaList.get(ordem).getHorariosListBkp().clear();
                        diasCronogramaList.get(ordem).getHorarioStrList().clear();
                        diasCronogramaList.get(ordem).getHorarioStrList().add("( " + f.getNome() + " )");
                    }
                }
                
                //coloca os afastamentos do funcionario
                for (Iterator<Afastamento.Afastamento> afas = listaAfasamento.iterator(); afas.hasNext();) {
                    Afastamento.Afastamento af = afas.next();
                    if (diasCronogramaList.get(ordem).getDia().getTime() >= af.getDataInicio().getTime() && diasCronogramaList.get(ordem).getDia().getTime() <= af.getDataFim().getTime()) {
                        diasCronogramaList.get(ordem).getHorariosList().clear();
                        diasCronogramaList.get(ordem).getHorariosListBkp().clear();
                        diasCronogramaList.get(ordem).getHorarioStrList().clear();
                        diasCronogramaList.get(ordem).getHorarioStrList().add("( " + af.getCategoriaAfastamento().getDescCategoriaAfastamento() + " )");
                    }
                }
                
                //coloca os deslocamentos temporarios
                for (Iterator<DescolamentoTemporario> dt = listaDeslocamentos.iterator(); dt.hasNext();) {
                    deslTmp = dt.next();
                    if (diasCronogramaList.get(ordem).getDia().getTime() == deslTmp.getDataInicio().getTime()) {

                        if (deslTmp.getFolga()) {
                            diasCronogramaList.get(ordem).setIsFolga(true);
                            diasCronogramaList.get(ordem).setCssHorario("color: blue");
                            diasCronogramaList.get(ordem).setTemHorario(true);
                            diasCronogramaList.get(ordem).setIsDeslTemp(true);
                            diasCronogramaList.get(ordem).setIsDesfazer(true);
                        } else {
                            diasCronogramaList.get(ordem).setIsFolga(false);
                            diasCronogramaList.get(ordem).setCssHorario("color: red");
                            ArrayList<Horario> horario = banco.consultaHorariosDiaPorSchClassId(cod_funcionario, deslTmp.getSchClassId());

                            //por conta da estrutura interna do sistema, 
                            //esta cambiara foi o meio mas rapido de colocar a horaExtra
                            if (deslTmp.getIsDiaExtra()) {
                                for (int w = 0; w < horario.size(); w++) {
                                    horario.get(w).setIsOverTime(true);
                                }
                                diasCronogramaList.get(ordem).setIsOverTime(true);
                            }

                            diasCronogramaList.get(ordem).getHorariosList().addAll(horario);
                            diasCronogramaList.get(ordem).ordenarHorarioList();
                            diasCronogramaList.get(ordem).setTemHorario(true);
                            diasCronogramaList.get(ordem).setIsDeslTemp(true);
                            diasCronogramaList.get(ordem).setIsDesfazer(true);
                            diasCronogramaList.get(ordem).getHorarioStrList().clear();
                            diasCronogramaList.get(ordem).initStringHorarios();

                        }

                    }

                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                ordem++;
            } while (calendar.getTime().compareTo(dataFim) <= 0);
        } else {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        
        tituloDeslocTemp = "Deslocamento Temporário - "+banco.getNomeFuncionario(cod_funcionario);
        
        banco.fechaConexao();
        b.fecharConexao();
    }

    public List<Jornada> getJornadaList(List<Jornada> jornadaAntigaList, Integer cod_jornada, Date inicio, Date fim) {

        List<Jornada> aJornadaList = new ArrayList<Jornada>();
        List<Jornada> jornadaFuncionarioList;
        try {
            consultaFuncionarioGrupo2();
        } catch (SQLException ex) {
            System.out.println("CadastroCronograma: getJornadaList: " + ex);
            //Logger.getLogger(CadastroCronogramaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<SelectItem> funcionarioList_ = removeCabecalho(funcionarioList);
        for (Iterator<SelectItem> it = funcionarioList_.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            jornadaFuncionarioList = new ArrayList<Jornada>();
            jornadaFuncionarioList = buscaJornadaList(jornadaAntigaList, Integer.parseInt(selectItem.getValue().toString()));
            if (jornadaFuncionarioList.isEmpty()) {
                Jornada jornada = new Jornada(Integer.parseInt(selectItem.getValue().toString()), cod_jornada, inicio, fim, 1);
                jornada.setInicioNovo(inicio);
                jornada.setFimNovo(fim);
                jornada.setCod_funcionario(getCodFuncionario((Integer.parseInt(selectItem.getValue().toString()))));
                jornada.setNomeFuncionario(selectItem.getLabel());
                jornada.setNomeJornada(getNomeJornada(cod_jornada));
                if (!constainJornada(aJornadaList, jornada)) {
                    aJornadaList.add(jornada);
                }
            } else {
                for (Iterator<Jornada> it1 = jornadaFuncionarioList.iterator(); it1.hasNext();) {
                    Jornada jornada = it1.next();

                    //<----------|  {*************} |---------->
                    if (inicio.getTime() > jornada.getFimAntigo().getTime() || fim.getTime() < jornada.getInicioAntigo().getTime()) {
                        Jornada jornada_ = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                cod_jornada, inicio, fim, jornada.getInicioAntigo(), jornada.getFimAntigo(), 1);
                        jornada_.setDadosJornada(jornada);
                        if (!constainJornada(aJornadaList, jornada_)) {
                            aJornadaList.add(jornada_);
                        }
                        //<----|{---->*************}
                    } else if (inicio.getTime() <= jornada.getInicioAntigo().getTime()
                            && (fim.getTime() <= jornada.getFimAntigo().getTime())) {

                        if (!(inicio.getTime() == jornada.getInicioAntigo().getTime()
                                && (fim.getTime() == jornada.getFimAntigo().getTime()))) {

                            if (fim.getTime() != jornada.getInicioAntigo().getTime()) {

                                Jornada jornada_2 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                        jornada.getCod_jornada(), addDia(fim), jornada.getFimAntigo(),
                                        jornada.getInicioAntigo(), jornada.getFimAntigo(), 2);
                                jornada_2.setDadosJornada(jornada);
                                if (!constainJornada(aJornadaList, jornada_2)) {
                                    aJornadaList.add(jornada_2);
                                }
                            }

                            Jornada jornada_1 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    cod_jornada, inicio, fim, jornada.getInicioAntigo(), jornada.getFimAntigo(), 1);
                            jornada_1.setDadosJornada(jornada);
                            if (!constainJornada(aJornadaList, jornada_1)) {
                                aJornadaList.add(jornada_1);
                            }

                        } else if (!cod_jornada.equals(jornada.getCod_jornada())) {
                            Jornada jornada_2 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    cod_jornada, jornada.getInicioAntigo(), jornada.getFimAntigo(), jornada.getInicioAntigo(), jornada.getFimAntigo(), 2);
                            jornada_2.setDadosJornada(jornada);
                            if (!constainJornada(aJornadaList, jornada_2)) {
                                aJornadaList.add(jornada_2);
                            }
                        }
                        //{*******<---------------->******}
                    } else if (inicio.getTime() >= jornada.getInicioAntigo().getTime()
                            && (fim.getTime() >= jornada.getInicioAntigo().getTime() && fim.getTime() <= jornada.getFimAntigo().getTime())) {

                        if (inicio.getTime() != jornada.getInicioAntigo().getTime()) {
                            Jornada jornada_1 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    jornada.getCod_jornada(), jornada.getInicioAntigo(), subDia(inicio), jornada.getInicioAntigo(), jornada.getFimAntigo(), 2);
                            jornada_1.setDadosJornada(jornada);
                            if (!constainJornada(aJornadaList, jornada_1)) {
                                aJornadaList.add(jornada_1);
                            }
                        }

                        Jornada jornada_2 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                cod_jornada, inicio, fim, jornada.getInicioAntigo(), jornada.getFimAntigo(), 1);
                        jornada_2.setDadosJornada(jornada);

                        if (!constainJornada(aJornadaList, jornada_2)) {
                            aJornadaList.add(jornada_2);
                        }

                        if (fim.getTime() != jornada.getInicioAntigo().getTime()) {
                            Jornada jornada_3 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    jornada.getCod_jornada(), addDia(fim), jornada.getFimAntigo(), jornada.getInicioAntigo(), jornada.getFimAntigo(), 1);
                            if (!constainJornada(aJornadaList, jornada_3)) {
                                aJornadaList.add(jornada_3);
                            }
                        }
                    } // {*******|----} -->
                    else if ((inicio.getTime() <= jornada.getFimAntigo().getTime() && inicio.getTime() >= jornada.getInicioAntigo().getTime())
                            && fim.getTime() >= jornada.getFimAntigo().getTime()) {
                        Jornada jornada_1 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                cod_jornada, inicio, fim, jornada.getInicioAntigo(), jornada.getFimAntigo(), 1);
                        jornada_1.setDadosJornada(jornada);
                        if (!constainJornada(aJornadaList, jornada_1)) {
                            aJornadaList.add(jornada_1);
                        }

                        if (inicio.getTime() != jornada.getInicioAntigo().getTime()) {
                            Jornada jornada_2 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    jornada.getCod_jornada(), jornada.getInicioAntigo(), subDia(inicio), jornada.getInicioAntigo(), jornada.getFimAntigo(), 2);
                            jornada_2.setDadosJornada(jornada);
                            if (!constainJornada(aJornadaList, jornada_2)) {
                                aJornadaList.add(jornada_2);
                            }
                        }
                        // <--{*******}-->
                    } else {
                        if (inicio.getTime() <= jornada.getInicioAntigo().getTime() && fim.getTime() >= jornada.getFimAntigo().getTime()) {

                            Jornada jornada_2 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    jornada.getCod_jornada(), jornada.getInicioAntigo(),
                                    subDia(inicio), jornada.getInicioAntigo(), jornada.getFimAntigo(), 3);
                            jornada_2.setDadosJornada(jornada);
                            if (!constainJornada(aJornadaList, jornada_2)) {
                                aJornadaList.add(jornada_2);
                            }

                            Jornada jornada_1 = new Jornada(Integer.parseInt(selectItem.getValue().toString()),
                                    cod_jornada, inicio, fim, jornada.getInicioAntigo(), jornada.getFimAntigo(), 1);
                            jornada_1.setDadosJornada(jornada);
                            if (!constainJornada(aJornadaList, jornada_1)) {
                                aJornadaList.add(jornada_1);
                            }
                        }
                    }
                }
            }
        }
        //     aJornadaList = checkErros(aJornadaList, aJornadaList);
        return aJornadaList;
    }

    private List<Jornada> buscaJornadaList(List<Jornada> jornadaList, Integer cod_funcionario) {
        List<Jornada> aJornadaList = new ArrayList<Jornada>();
        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (cod_funcionario.equals(jornada.getCod_funcionario())) {
                aJornadaList.add(jornada);
            }
        }
        return aJornadaList;
    }

    private Date addDia(Date data) {
        Date data_ = (Date) data.clone();
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(data_);
        g.add(Calendar.DAY_OF_MONTH, 1);
        Date dateAux = g.getTime();
        return dateAux;
    }

    private Date subDia(Date data) {
        Date data_ = (Date) data.clone();
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(data_);
        g.add(Calendar.DAY_OF_MONTH, -1);
        Date dateAux = g.getTime();
        return dateAux;
    }

    private Boolean constainJornada(List<Jornada> aJornadaList, Jornada jornada) {
        for (Iterator<Jornada> it = aJornadaList.iterator(); it.hasNext();) {
            Jornada jornada1 = it.next();
            if (jornada1.equal(jornada)) {
                return true;
            }
        }
        return false;
    }

    private Boolean constainJornada2(List<Jornada> aJornadaList, Jornada jornada) {
        for (Iterator<Jornada> it = aJornadaList.iterator(); it.hasNext();) {
            Jornada jornada1 = it.next();
            if (jornada1.equal2(jornada)) {
                return true;
            }
        }
        return false;
    }

    private String getNomeJornada(Integer cod_jornada) {
        for (Iterator<SelectItem> it = jornadasList.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            if (cod_jornada.toString().equals(selectItem.getValue())) {
                return selectItem.getValue().toString();
            }

        }
        return "";
    }

    private Integer getCodFuncionario(Integer userid) {
        for (Iterator<Funcionario> it = funcionarioObjList.iterator(); it.hasNext();) {
            Funcionario f = it.next();
            if (f.getFuncionarioId().equals(userid)) {
                return f.getFuncionarioId();
            }
        }
        return 0;
    }

    public void deletarJornada() {
        String cod_funcionario_ = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_funcionario");
        String cod_jornada = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_jornada");
        String dataInicio_ = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dataInicio");
        dataInicio_ = converterDate(dataInicio_);

        Banco b = new Banco();
        Boolean flag = b.deletarJornada(cod_funcionario_, cod_jornada, dataInicio_);
        if (!flag) {
            FacesMessage msgErro = new FacesMessage("A jornada não pode ser excluída!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Jornada excluída com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        Banco banco = new Banco();
        funcionarioObjList = banco.getFuncionarios();
        jornadaList = banco.getJornadas(funcionarioList);
        setLogCronograma("Excluir", Integer.parseInt(cod_jornada), Integer.parseInt(cod_funcionario_));
    }

    public void deletarTodasJornadas() {

        Banco b = new Banco();
        Boolean flag = b.deletarTodasJornadas(jornadaList);
        if (!flag) {
            FacesMessage msgErro = new FacesMessage("As jornadas não puderam ser excluídas!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Jornadas excluídas com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        Banco banco = new Banco();
        funcionarioObjList = banco.getFuncionarios();
        jornadaList = banco.getJornadas(funcionarioList);
        //Metodos.setLogInfo("Excluir todas as jornadas do departamento " + buscaNomeDepartamento(departamento, incluirSubSetores));
    }

    public void consultaDepartamento(String permissao) {
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        Integer dept = usuarioBean.getUsuario().getDepartamento();
        departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }

    public void consultaFuncionario() throws SQLException {
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        if (departamento != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }
        inicializaAtributos();
        funcionarioList = filtrarFuncionario();

    }

    public void consultaFuncionarioGrupo() throws SQLException {
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        if (departamento != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }
        inicializaAtributos();
        funcionarioList = filtrarFuncionario();
        banco = new Banco();
        funcionarioObjList = banco.getFuncionarios();
        jornadaList = banco.getJornadas(funcionarioList);
        banco.fechaConexao();

    }

    public void consultaFuncionarioGrupo2() throws SQLException {
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        if (departamento != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }
        //     inicializaAtributos();
        funcionarioList = filtrarFuncionario();
        banco = new Banco();
        funcionarioObjList = banco.getFuncionarios();
        jornadaList = banco.getJornadas(funcionarioList);
        banco.fechaConexao();

    }

    public void inicializaAtributos() throws SQLException {
        //  Banco banco = new Banco();
        //  funcionarioList = new ArrayList<SelectItem>();
        //  funcionarioList = banco.consultaFuncionarioTotal(departamento, incluirSubSetores);
        cod_funcionario = -1;
        cronogramasList = new ArrayList<Cronograma>();
        diasCronogramaList = new ArrayList<DiaCronograma>();
        diasDeslocamentos = new ArrayList<DiaCronograma>();
        //     turnosMarcados = new ArrayList<Integer>();
        //    diasMarcados = new ArrayList<Integer>();

        dataInicio = getPrimeiroDiaMes();
        dataInicioJornada = getPrimeiroDiaMes();

        dataFim = getLastDay();
        dataFimJornada = getLastDay();
        overtime = false;
        visualOvertime = false;
        //     inicializarAtributosConsultaDias();
    }

    public void consultaCronogramas() {
        Banco banco = new Banco();
        cronogramasList = banco.consultaCronogramas(cod_funcionario);
        geraDiasCronogramaList();
        consultaHorariosList();
        cronogramaSelecionado = null;
    }

    public void consultaHorariosList() {
        horariosList = new ArrayList<Turno>();
        Banco banco = new Banco();
        horariosList = banco.consultaHorariosList();
    }

    public void salvarTurnos() {
        Banco banco = new Banco();
        //setLogDeslocTemp("Alterar Deslocamento Temporario", dataInicio, dataFim);
        boolean flag = false;
        for (int i = 0; i < diasCronogramaList.size(); i++) {
            if (diasCronogramaList.get(i).getHouveAlteracao()) {
                diasDeslocamentos.add(diasCronogramaList.get(i));
            }
        }
        flag = banco.salvarTurnos(diasDeslocamentos, cod_funcionario, dataInicio, dataFim);
        if (!flag) {
            FacesMessage msgErro = new FacesMessage("Os horários não puderam ser salvos!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Os horários foram alterados com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        consultaCronogramas();
    }

    public void excluirDeslocTempTurnos() {
        //setLogDeslocTemp("Limpar Deslocamentos Temporarios", dataInicio, dataFim);
        Banco banco = new Banco();
        boolean flag = banco.excluirDeslocTempTurnos(diasCronogramaList, cod_funcionario, dataInicio, dataFim, overtime);
        if (!flag) {
            FacesMessage msgErro = new FacesMessage("Os deslocamentos não puderam ser excluídos!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Os deslocamentos foram excluídos com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        consultaCronogramas();
    }

    private String converterDate(String data) {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dt = null;
        try {
            dt = df1.parse(data);
        } catch (ParseException ex) {
        }
        String dataStr = df2.format(dt.getTime());
        return dataStr;
    }

    public void limparIntervalo() {
        for (int i = 0; i < diasCronogramaList.size(); i++) {
            DiaCronograma dia = diasCronogramaList.get(i);
            dia.limparTurno();
        }
    }

    public void adicionarTurnos() {
        ArrayList<Turno> turnosList = new ArrayList<Turno>();
        int turnosMarcados = 0;

        // teste de validação 1 : colocar virada anterior a um dia que já contem 2 marcados
        int maiorDosProximos = 0;

        Integer turnoVirada = 0;

        //Contabiliza os turnos marcados
        for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
            Turno turno = it.next();
            if (turno.getMarked()) {
                turnosMarcados++;
                if (turnoVirada == 0) {
                    turnoVirada = (turno.getSaida().before(turno.getEntrada())) ? 1 : 0;
                }
            }
        }

        //Contabiliza os dias marcados
        for (int i = 0; i
                < diasCronogramaList.size() - 1; i++) {
            if (diasCronogramaList.get(i).getMarked()) {
                DiaCronograma dc = diasCronogramaList.get(i + 1);
                if (maiorDosProximos < dc.horariosList.size()) {
                    maiorDosProximos = dc.horariosList.size();
                }
            }
        }
        // teste de validação 2 : dia com mais de 2 marcados ou com uma virada no dia anterior
        int viradaAnterior = 0;

        for (int i = 1; i
                < diasCronogramaList.size(); i++) {
            if (diasCronogramaList.get(i).getMarked()) {
                DiaCronograma dc = diasCronogramaList.get(i - 1);
                if (viradaAnterior == 0) {
                    viradaAnterior = (dc.getHasVirada()) ? 1 : 0;
                }
            }
        }

        int maior = 0;

        for (int i = 0; i
                < diasCronogramaList.size(); i++) {
            if (diasCronogramaList.get(i).getMarked()) {
                DiaCronograma dc = diasCronogramaList.get(i);
                if (dc.horariosList.size() + turnosMarcados >= maior) {
                    maior = dc.horariosList.size() + turnosMarcados;
                }
            }
        }

        if (viradaAnterior + maior > 2 || maiorDosProximos + turnoVirada > 2) {
            FacesMessage msgErro = new FacesMessage("Um dia não pode conter mais de dois horários!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            // Testa se existem horarios em conflito
            SimpleDateFormat sfHora = new SimpleDateFormat("HH:mm");
            String horario1 = "";
            String iniciofaixaEntrada1 = "";
            String fimfaixaEntrada1 = "";
            String iniciofaixaSaida1 = "";
            String fimfaixaSaida1 = "";
            String horario2 = "";
            String iniciofaixaEntrada2 = "";
            String fimfaixaEntrada2 = "";
            String iniciofaixaSaida2 = "";
            String fimfaixaSaida2 = "";

            Boolean conflito = false;
            ArrayList<Turno> aSeremAdicionados = new ArrayList<Turno>();
            ArrayList<Turno> aSeremAdicionadosComVirada = new ArrayList<Turno>();
            ArrayList<Horario> horariosJaUtilizados = new ArrayList<Horario>();

            //teste 1: conflito entre dois turnos a serem adicionados
            for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
                Turno turno = it.next();
                if (turno.getMarked()) {
                    if (turno.hasVirada) {
                        aSeremAdicionadosComVirada.add(turno);
                    } else {
                        aSeremAdicionados.add(turno);
                    }
                }
            }
            for (int i = 0; i
                    < aSeremAdicionados.size() - 1; i++) {
                Turno turno = aSeremAdicionados.get(i);
                for (int j = i + 1; j
                        < aSeremAdicionados.size(); j++) {
                    Turno turno1 = aSeremAdicionados.get(j);

                    if (turno1.getInicioFaixaEntrada().before(turno.getInicioFaixaEntrada())) {
                        if (turno1.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                            horario1 = turno1.getNome();
                            fimfaixaSaida1 = sfHora.format(new Date(turno1.getFimFaixaSaida().getTime()));
                            horario2 = turno.getNome();
                            iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                            conflito = true;
                        }
                    } else {
                        if (turno1.getInicioFaixaEntrada().before(turno.getFimFaixaSaida())) {
                            horario1 = turno1.getNome();
                            iniciofaixaEntrada1 = sfHora.format(new Date(turno1.getInicioFaixaEntrada().getTime()));
                            horario2 = turno.getNome();
                            fimfaixaSaida2 = sfHora.format(new Date(turno.getFimFaixaSaida().getTime()));
                            conflito = true;
                        }
                    }
                }
            }
            for (int i = 0; i
                    < aSeremAdicionados.size(); i++) {
                Turno turno = aSeremAdicionados.get(i);

                for (int j = 0; j
                        < aSeremAdicionadosComVirada.size(); j++) {
                    Turno turnoComVirada = aSeremAdicionadosComVirada.get(j);

                    if (turno.getInicioFaixaEntrada().after(turnoComVirada.getInicioFaixaEntrada())) {
                        horario1 = turno.getNome();
                        iniciofaixaEntrada1 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));

                        horario2 = turnoComVirada.getNome();
                        iniciofaixaEntrada2 = sfHora.format(new Date(turnoComVirada.getInicioFaixaEntrada().getTime()));

                        conflito = true;
                    }
                }
            }
            for (int i = 0; i
                    < aSeremAdicionadosComVirada.size(); i++) {
                Turno turnoComVirada = aSeremAdicionadosComVirada.get(i);

                for (int j = 0; j
                        < diasCronogramaList.size() - 1; j++) {
                    DiaCronograma dc1 = diasCronogramaList.get(j);
                    DiaCronograma dc2 = diasCronogramaList.get(j + 1);

                    if (dc1.getMarked() && dc2.getMarked()) {
                        for (int k = 0; k
                                < dc2.getHorariosList().size(); k++) {
                            Horario horario = dc2.getHorariosList().get(k);

                            if (turnoComVirada.getFimFaixaSaida().after(horario.getInicioFaixaEntrada())) {
                                horario1 = turnoComVirada.getNome();
                                horario2 = horario.getNome();
                                fimfaixaSaida1 = sfHora.format(new Date(turnoComVirada.getFimFaixaSaida().getTime()));
                                iniciofaixaEntrada2 = sfHora.format(new Date(horario.getInicioFaixaEntrada().getTime()));
                                conflito = true;
                            }
                        }
                    }
                }
            }

            //Teste 2: Se existe conflito entre os horarios a serem adicionados e os que já estão sendo utilizados
            for (int i = 0; i
                    < diasCronogramaList.size(); i++) {
                DiaCronograma dj = diasCronogramaList.get(i);

                if (dj.getMarked()) {
                    for (int j = 0; j
                            < dj.getHorariosList().size(); j++) {
                        Horario horario = dj.getHorariosList().get(j);
                        horariosJaUtilizados.add(horario);
                    }
                }
            }
            for (int i = 0; i
                    < aSeremAdicionados.size(); i++) {
                Turno turno = aSeremAdicionados.get(i);

                for (int j = 0; j
                        < horariosJaUtilizados.size(); j++) {
                    Horario horario = horariosJaUtilizados.get(j);

                    if (horario.getInicioFaixaEntrada().before(turno.getInicioFaixaEntrada())) {
                        if (horario.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                            horario1 = horario.getNome();
                            horario2 = turno.getNome();
                            fimfaixaSaida1 = sfHora.format(new Date(horario.getFimFaixaSaida().getTime()));
                            iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                            conflito = true;
                        }
                    } else {
                        if (horario.getInicioFaixaEntrada().before(turno.getFimFaixaSaida())) {
                            horario1 = horario.getNome();
                            horario2 = turno.getNome();
                            iniciofaixaEntrada1 = sfHora.format(new Date(horario.getInicioFaixaEntrada().getTime()));
                            fimfaixaSaida2 = sfHora.format(new Date(turno.getFimFaixaSaida().getTime()));
                            conflito = true;
                        }
                    }
                }
            }
            for (int i = 0; i
                    < aSeremAdicionadosComVirada.size(); i++) {
                Turno turno = aSeremAdicionadosComVirada.get(i);

                for (int j = 0; j
                        < horariosJaUtilizados.size(); j++) {
                    Horario horario = horariosJaUtilizados.get(j);

                    if (horario.getInicioFaixaEntrada().after(turno.getInicioFaixaEntrada())) {
                        horario1 = horario.getNome();
                        horario2 = turno.getNome();
                        iniciofaixaEntrada1 = sfHora.format(new Date(horario.getInicioFaixaEntrada().getTime()));
                        iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                        conflito = true;
                    }
                }
            }

            //Teste 3: testa se existem horarios que chocam com a virada do dia anterior
            for (int i = 1; i
                    < diasCronogramaList.size(); i++) {
                if (diasCronogramaList.get(i).getMarked()) {
                    DiaCronograma dc = diasCronogramaList.get(i - 1);

                    if (dc.getHasVirada()) {
                        Horario horarioVirada = new Horario();
                        for (int j = 0; j
                                < dc.getHorariosList().size(); j++) {
                            if (dc.getHorariosList().get(j).getEntrada().after(dc.getHorariosList().get(j).getSaida())) {
                                horarioVirada = dc.getHorariosList().get(j);
                            }
                        }
                        for (int j = 0; j
                                < aSeremAdicionados.size(); j++) {
                            Turno turno = aSeremAdicionados.get(j);

                            if (horarioVirada.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                                horario1 = horarioVirada.getNome();
                                horario2 = turno.getNome();
                                fimfaixaSaida1 = sfHora.format(new Date(horarioVirada.getFimFaixaSaida().getTime()));
                                iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                                conflito = true;
                            }
                        }
                        for (int j = 0; j
                                < aSeremAdicionadosComVirada.size(); j++) {
                            Turno turno = aSeremAdicionadosComVirada.get(j);

                            if (horarioVirada.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                                horario1 = horarioVirada.getNome();
                                horario2 = turno.getNome();

                                fimfaixaSaida1 = sfHora.format(new Date(horarioVirada.getFimFaixaSaida().getTime()));
                                iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));

                                conflito = true;
                            }
                        }
                    }
                }
            }

            //Teste4: testa se existem horarios a serem adicionados com virada e que chocam com horarios já existentes no próximo dia
            for (int i = 0; i
                    < diasCronogramaList.size() - 1; i++) {
                if (diasCronogramaList.get(i).getMarked()) {
                    DiaCronograma dj = diasCronogramaList.get(i + 1);

                    for (int j = 0; j
                            < aSeremAdicionadosComVirada.size(); j++) {
                        Turno turnoComVirada = aSeremAdicionadosComVirada.get(j);
                        ArrayList<Horario> HorariosDiaSeguinte = dj.getHorariosList();

                        for (int k = 0; k
                                < HorariosDiaSeguinte.size(); k++) {
                            Horario horario = HorariosDiaSeguinte.get(k);

                            if (turnoComVirada.getFimFaixaSaida().after(horario.getInicioFaixaEntrada())) {
                                horario1 = horario.getNome();
                                horario2 = turnoComVirada.getNome();
                                iniciofaixaEntrada1 = sfHora.format(new Date(horario.getInicioFaixaEntrada().getTime()));

                                fimfaixaSaida2 = sfHora.format(new Date(turnoComVirada.getFimFaixaSaida().getTime()));
                                conflito = true;
                            }
                        }
                    }
                }
            }

            if (conflito) {
                String ie1 = (iniciofaixaEntrada1.equals("") ? "" : "Inicio da faixa de entrada do horário (" + horario1 + "): " + iniciofaixaEntrada1);
                String fe1 = (fimfaixaEntrada1.equals("") ? "" : "Fim da faixa de entrada do horário (" + horario1 + "): " + fimfaixaEntrada1);
                String is1 = (iniciofaixaSaida1.equals("") ? "" : "Inicio da faixa de saida do horário (" + horario1 + "): " + iniciofaixaSaida1);
                String fs1 = (fimfaixaSaida1.equals("") ? "" : "Fim da faixa de saida do horário (" + horario1 + "): " + fimfaixaSaida1);
                String ie2 = (iniciofaixaEntrada2.equals("") ? "" : "Inicio da faixa de entrada do horário (" + horario2 + "): " + iniciofaixaEntrada2);
                String fe2 = (fimfaixaEntrada2.equals("") ? "" : "Fim da faixa de entrada do horário (" + horario2 + "): " + fimfaixaEntrada2);
                String is2 = (iniciofaixaSaida2.equals("") ? "" : "Inicio da faixa de saida do horário (" + horario2 + "): " + iniciofaixaSaida2);
                String fs2 = (fimfaixaSaida2.equals("") ? "" : "Fim da faixa de saida do horário (" + horario2 + "): " + fimfaixaSaida2);

                FacesMessage msgErro = new FacesMessage("Existe conflito entre os horários: (" + horario1 + ") e (" + horario2 + ")  Verifique se existe conflito nas faixas de entrada e saída");
                /*FacesMessage msgErro = new FacesMessage("Existe conflito entre os horários: (" + horario1 + ") e (" + horario2 + ") ----------------------------------------------------------------------------------------------------------------------------------------------------------- " + ie1+fe1+is1+fs1+ " ----------------------------------------------------------------------------------------------------------------------------------------------------------- " +ie2+fe2+is2+fs2);*/
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                //monta selecionados
                //         infoMessage = "";

                for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
                    Turno turno = it.next();
                    if (turno.getMarked()) {
                        turnosList.add(turno);
                    }
                }
                for (int i = 0; i < diasCronogramaList.size(); i++) {
                    //DiaCronograma dc = diasCronogramaList.get(i);
                    //if (dc.getMarked()) {
                    if (diasCronogramaList.get(i).getMarked()) {
                        Horario hd = new Horario();
                        for (int j = 0; j < turnosList.size(); j++) {
                            Turno t = turnosList.get(j);
                            hd = new Horario(t.getNome(), t.getId(), t.getEntrada(), t.getSaida(), 1, 1, t.getInicioFaixaEntrada(), t.getFimFaixaEntrada(), t.getInicioFaixaSaida(), t.getFimFaixaSaida(), "", isOverTime);

                            if (diasCronogramaList.get(i).getIsFolga()) {
                                diasCronogramaList.get(i).getHorariosList().clear();
                            }
                            diasCronogramaList.get(i).getHorarioStrList().clear();
                            diasCronogramaList.get(i).getHorariosList().add(hd);
                            ordenaHorarios(diasCronogramaList.get(i).getHorariosList());
                            diasCronogramaList.get(i).initStringHorarios();
                            diasCronogramaList.get(i).setHasVirada(diasCronogramaList.get(i).calculaVirada());
                            diasCronogramaList.get(i).setCssHorario("color: red");
                            diasCronogramaList.get(i).setIsDeslTemp(true);
                            diasCronogramaList.get(i).setHouveAlteracao(true);
                            diasCronogramaList.get(i).setIsFolga(false);
                            diasCronogramaList.get(i).setTemHorario(true);
                            diasCronogramaList.get(i).setAcao(4);
                            diasCronogramaList.get(i).setIsDesfazer(true);
                        }
                    }
                }
                ArrayList<Horario> horariosTodosList = new ArrayList<Horario>();
                for (int i = 0; i < diasCronogramaList.size(); i++) {
                    DiaCronograma dc = diasCronogramaList.get(i);
                    ArrayList<Horario> horariosXDiaList = dc.getHorariosList();
                    for (int j = 0; j < horariosXDiaList.size(); j++) {
                        Horario horario = horariosXDiaList.get(j);
                        Date entrada = horario.getEntrada();
                        Date saida = horario.getSaida();
                        Horario h = new Horario();
                        if (entrada.before(saida)) {
                            long tentrada = entrada.getTime() + (86400000 * i);
                            long tsaida = saida.getTime() + (86400000 * i);
                            h.setEntrada(new Date(tentrada));
                            h.setSaida(new Date(tsaida));
                        } else {
                            long tentrada = entrada.getTime() + (86400000 * i);
                            long tsaida = saida.getTime() + (86400000 * (i + 1));
                            h.setEntrada(new Date(tentrada));
                            h.setSaida(new Date(tsaida));
                        }
                        horariosTodosList.add(h);
                    }
                }
                Collections.sort(horariosTodosList, new HorarioComparator());
                boolean ultimaPausaFoiMenosDe11Horas = false;
                boolean alerta11horas = false;
                Date inicioDescanso = new Date();
                Date fimDescanso = new Date();
                for (int i = 0; i < horariosTodosList.size() - 1; i++) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    inicioDescanso = horariosTodosList.get(i).getSaida();
                    fimDescanso = horariosTodosList.get(i + 1).getEntrada();
                    if (fimDescanso.getTime() - inicioDescanso.getTime() < 39600000) {
                        if (ultimaPausaFoiMenosDe11Horas) {
                            alerta11horas = true;
                        } else {
                            ultimaPausaFoiMenosDe11Horas = true;
                        }
                    } else {
                        ultimaPausaFoiMenosDe11Horas = false;
                    }
                }
                if (alerta11horas) {
                    FacesMessage msgErro = new FacesMessage("Atenção! Existem dias na jornada"
                            + " que não possuem o descanso mínimo de 11 horas!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            }
        }

        //   turnosMarcados.clear();
        //   diasMarcados.clear();
    }

    /**
     * @return the jornadaSelecionadas
     */
    public String getJornadaSelecionadas() {
        return jornadaSelecionadas;
    }

    /**
     * @param jornadaSelecionadas the jornadaSelecionadas to set
     */
    public void setJornadaSelecionadas(String jornadaSelecionadas) {
        System.out.println("jornada selecionada: " + jornadaSelecionadas);
        this.jornadaSelecionadas = jornadaSelecionadas;
    }

    public class HorarioComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Horario h1 = ((Horario) o1);
            Horario h2 = ((Horario) o2);
            if (h1.getEntrada().after(h2.getEntrada())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public void imprimir() {
        Banco b = new Banco();
        b.inserirRelatorioIndividual(diasCronogramaList);
        ConsultaPonto.Banco b2 = new ConsultaPonto.Banco();
        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo
                = b2.relatorioPortaria1510CabecalhoResumo(cod_funcionario);
        String horasPrevistas = c.getHorasASeremTrabalhadasTotal();
        try {
            Impressao.geraRelatorioEscalaIndividual(dataInicio, dataFim, horasPrevistas, relatorioPortaria1510CabecalhoResumo);
        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public ArrayList<Horario> ordenaHorarios(ArrayList<Horario> hxd) {
        if (hxd.size() > 1) {
            if (hxd.get(1).getEntrada().before((hxd.get(0).getEntrada()))) {
                Horario hd0 = hxd.get(0);
                Horario hd1 = hxd.get(1);
                hxd.clear();
                hxd.add(hd1);
                hxd.add(hd0);
            }
        }
        return hxd;
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

    private List<Jornada> checkErros(List<Jornada> jAntigoList, List<Jornada> jList) {
        for (Iterator<Jornada> it = jList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (constainJornada2(jAntigoList, jornada) && !jornada.getFlag().equals(3)) {
                it.remove();
            }
        }
        return jList;
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
        cod_funcionarioGestorHashMap = b.getcod_funcionarioSubordinacaoDepartamento(departamento);
        List<SelectItem> funcionarioList_ = new ArrayList<SelectItem>();
        cod_funcionario = null;
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

    public String voltar() {
        geraDiasCronogramaList();
        return "navegarEscala";
    }

    private String buscaNomeDepartamento(Integer cod_departamento, Boolean incluirSuBSetores) {

        for (Iterator<SelectItem> it = departamentolist.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            if (cod_departamento.equals(Integer.parseInt(selectItem.getValue().toString()))) {
                return selectItem.getLabel().toString() + " (subsetores " + (incluirSuBSetores == true ? "" : "não") + " incluídos)";
            }

        }
        return "";
    }

    private List<SelectItem> removeCabecalho(List<SelectItem> list) {
        List<SelectItem> listCopy = new ArrayList<SelectItem>();
        for (Iterator<SelectItem> it = list.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            if (selectItem.getValue() != null
                    && !selectItem.getValue().toString().equals("0")
                    && !selectItem.getValue().toString().equals("-1")) {
                listCopy.add(selectItem);
            }
        }
        return listCopy;

    }

    private String buscaNomeJornada(Integer cod_jornada) {

        for (Iterator<SelectItem> it = jornadasList.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            if (cod_jornada.equals(Integer.parseInt(selectItem.getValue().toString()))) {
                return selectItem.getLabel().toString();
            }
        }
        return "";
    }

    public List<Cronograma> getCronogramasList() {
        return cronogramasList;
    }

    public void setCronogramasList(List<Cronograma> cronogramasList) {
        this.cronogramasList = cronogramasList;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Boolean getIsAdministradorVisivel() {
        return isAdministradorVisivel;
    }

    public void setIsAdministradorVisivel(Boolean isAdministradorVisivel) {
        this.isAdministradorVisivel = isAdministradorVisivel;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public List<DiaCronograma> getDiasCronogramaList() {
        return diasCronogramaList;
    }

    public void setDiasCronogramaList(List<DiaCronograma> diasCronogramaList) {
        this.diasCronogramaList = diasCronogramaList;
    }

    public List<DiaCronograma> getDiasDeslocamentos() {
        return diasDeslocamentos;
    }

    public void setDiasDeslocamentos(List<DiaCronograma> diasDeslocamentos) {
        this.diasDeslocamentos = diasDeslocamentos;
    }

    public List<Turno> getHorariosList() {
        return horariosList;
    }

    public void setHorariosList(List<Turno> horariosList) {
        this.horariosList = horariosList;
    }

    public Boolean getVisualOvertime() {
        return visualOvertime;
    }

    public void setVisualOvertime(Boolean visualOvertime) {
        this.visualOvertime = visualOvertime;
    }

    public Boolean getOvertime() {
        return overtime;
    }

    public void setOvertime(Boolean overtime) {
        this.overtime = overtime;
    }

    public void changeVFovertime() {
        overtime = !overtime;
    }

    public SimpleSelection getCronogramaSelecionado() {
        return cronogramaSelecionado;
    }

    public void setCronogramaSelecionado(SimpleSelection cronogramaSelecionado) {
        this.cronogramaSelecionado = cronogramaSelecionado;
    }

    public Integer getJornadaSelecionada() {
        return jornadaSelecionada;
    }

    public void setJornadaSelecionada(Integer jornadaSelecionada) {
        this.jornadaSelecionada = jornadaSelecionada;
    }

    public List<SelectItem> getJornadasList() {
        return jornadasList;
    }

    public void setJornadasList(List<SelectItem> jornadasList) {
        this.jornadasList = jornadasList;
    }

    public Date getDataFimJornada() {
        return dataFimJornada;
    }

    public void setDataFimJornada(Date dataFimJornada) {
        this.dataFimJornada = dataFimJornada;
    }

    public Date getDataInicioJornada() {
        return dataInicioJornada;
    }

    public void setDataInicioJornada(Date dataInicioJornada) {
        this.dataInicioJornada = dataInicioJornada;
    }

    public Cronograma getCronogramaAnterior() {
        return cronogramaAnterior;
    }

    public void setCronogramaAnterior(Cronograma cronogramaAnterior) {
        this.cronogramaAnterior = cronogramaAnterior;
    }

    public Date getDataFimEditJornada() {
        return dataFimEditJornada;
    }

    public void setDataFimEditJornada(Date dataFimEditJornada) {
        this.dataFimEditJornada = dataFimEditJornada;
    }

    public Date getDataInicioNewJornada() {
        return dataInicioNewJornada;
    }

    public void setDataInicioNewJornada(Date dataInicioNewJornada) {
        this.dataInicioNewJornada = dataInicioNewJornada;
    }

    public Date getDataFimNewJornada() {
        return dataFimNewJornada;
    }

    public void setDataFimNewJornada(Date dataFimNewJornada) {
        this.dataFimNewJornada = dataFimNewJornada;
    }

    public Date getDataInicioEditJornada() {
        return dataInicioEditJornada;
    }

    public void setDataInicioEditJornada(Date dataInicioEditJornada) {
        this.dataInicioEditJornada = dataInicioEditJornada;
    }

    public Integer getJornadaEditSelecionada() {
        return jornadaEditSelecionada;
    }

    public void setJornadaEditSelecionada(Integer jornadaEditSelecionada) {
        this.jornadaEditSelecionada = jornadaEditSelecionada;
    }

    public String getTituloDeslocTemp() {
        return tituloDeslocTemp;
    }

    public void setTituloDeslocTemp(String tituloDeslocTemp) {
        this.tituloDeslocTemp = tituloDeslocTemp;
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

    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    public boolean isIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(boolean isOverTime) {
        this.isOverTime = isOverTime;
    }

    public boolean isShowDiasJornada() {
        return showDiasJornada;
    }

    public void setShowDiasJornada(boolean showDiasJornada) {
        this.showDiasJornada = showDiasJornada;
    }

    public List<DiaJornada> getDiasJornadaList() {
        return diasJornadaList;
    }

    public void setDiasJornadaList(List<DiaJornada> diasJornadaList) {
        this.diasJornadaList = diasJornadaList;
    }
}
