package ConsultaPonto;

import Metodos.Metodos;
import Abono.Abono;
import Afastamento.Afastamento;
import CadastroHoraExtra.Gratificacao;
import CadastroHoraExtra.RegimeHoraExtra;
import CadastroHoraExtra.TipoHoraExtra;
import Funcionario.Funcionario;
import RelatorioMensal.RelatorioPortaria1510Cabecalho;
import Usuario.CatracaBanco;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author amsgama
 */
public class ConsultaFrequenciaComEscalaBean implements Serializable {

    private String nome;
    private Integer departamento;
    private String departamentoSrt;
    private String dataSelecionada;
    private String dataSelecionadaIngles;
    private String horasTotal;
    private String horasASeremTrabalhadasTotal;
    private String saldo2;
    private String saldoTotal;
    private String saldoSemFaltas;
    private String horaExtra;
    private Integer cod_funcionario;
    private Integer page = 1;
    private Integer contDiasATrabalhar = 0;
    private Integer contDiasTrabalhados = 0;
    private Integer faltas = 0;
    private Integer qntDSR = 0;
    private List<Resumo> resumoList;
    private ArrayList<DiaComEscala> diasList;
    private List<DiaSemEscala> diasComRegistroSemEscalaList;
    private List<SelectItem> departamentolist;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private List<SelectItem> gestorFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<String> horasTrabalhadasDiaList;
    private List<String> horasaSeremTrabalhadasDiaList;
    private HashMap<Integer, List<Integer>> sdiasHashMap;
    private HashMap<String, ArrayList<Jornada>> diasDeslocadosHashMap;
    private List<Abono> abonoList;
    private List<Feriado> feriadoList;
    private List<Integer> feriadoCriticosList;
    private List<Jornada> jornadaList;
    private List<SelectItem> funcionarioList;
    private List<RegistroAdicionado> pontosAdicionados;
    private List<Integer> diasDeslocadosAdicionais;
    private List<Integer> diasDeslocadoshoraExtra;
    private List<Integer> diasHoraExtra;
    private List<Integer> diasPendentesList;
    private List<TipoHoraExtra> tipoHoraExtra;
    private Map<String, DiaComEscala> diaAcessoMap;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private static UsuarioBean usuarioBean;
    private Boolean incluirSubSetores;
    private Boolean isAdministradorVisivel;
    private Map<String, Integer> tipo_valorHorasExtra;
    private List<String> diasComHorasExtraList;
    private String abaCorrente;
    private String adicionalNoturnoStr;
    private Integer adicionalNoturnoInt;
    private Integer adicionalNoturnoFinal;
    private String saldoDiaCriticoStr;
    private Integer saldoDiaCriticoInt;
    private List<Gratificacao> gratificacaoList;
    private Map<Integer, Integer> somaGratificacao;
    private String gratificacaoStr;
    private List<Escala> escalasList;
    private RegimeHoraExtra regimeHoraExtra;
    private List<Ponto> pontos;
    private Funcionario funcionarioSelecionado;
    private HashMap<String, DescolamentoTemporario> diasDeslocamentoTempHashMap;
    private Afastamento afastamento;
    private List<Afastamento> afastamentoList;
    private Integer columNumberComEscala;
    private Integer columNumberSemEscala;
    private Integer totalQntAtraso16_59;
    private Integer totalQntAtrasoMaiorUmaHora;
    private Integer qnt_dias_adicional_noturno;
    private String listagemAtraso;
    private String listagemFalta;
    //Filtro por cargo
    private Integer cargoSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioCargoHashMap;
    private List<SelectItem> cargoOpcaoFiltroFuncionarioList;

    public ConsultaFrequenciaComEscalaBean() throws SQLException {

        inicializarAtributos();

        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));

        if (usuarioBean != null) {
            if (usuarioBean.getEhAdministrador()) {
                consultaDepartamento(usuarioBean.getUsuario().getPermissao());
            } else {
                nome = usuarioBean.getUsuario().getNome();
                cod_funcionario = usuarioBean.getUsuario().getLogin();
                departamento = usuarioBean.getUsuario().getDepartamento();
                departamentoSrt = consultaNome(departamento);
                consultaDias();
            }
        }
        Locale.setDefault(new Locale("pt", "BR"));
        abaCorrente = "tab1";
    }

    public ConsultaFrequenciaComEscalaBean(String x) {
    }

    public void consultaDepartamento(String permissao) {
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        Integer dept = usuarioBean.getUsuario().getDepartamento();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }

    public String consultaNome(Integer deptid) {
        Banco banco = new Banco();
        return banco.consultaDepartamentoNome(deptid);
    }

    public void consultaFuncionario() throws SQLException {

        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        cod_funcionario = null;
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();

        if (departamento != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }
        departamentoSrt = consultaNome(departamento);
        inicializarAtributosConsultaDias();

        funcionarioList = filtrarFuncionario();

    }

    // Método principal para examinar os dias trabalhados no período de tempo especificado (dataInicio, dataFim)
    public void consultaDias() {

        Banco banco = new Banco();
        inicializarAtributosConsultaDias();

        try {
            //Validando entrada de dados
            if (isEntradaValida()) {
                //Verificando se a data de consulta inicial é menor ou igual a final
                if (isDataInicioMaiorQueDataFim()) {
                    FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {

                    // Buscando todas as jornadas associadas ao funcionário no período especificado
                    List<PeriodoJornada> dataPeriodoJornadaList = banco.consultaPeriodoJornada(cod_funcionario, dataInicio, dataFim);

                    //O attendance permite a associação da jornada de forma que a data inicial seja maior que a final. Então foi feito o tratamento.
                    if (isPeridoInvalido(dataPeriodoJornadaList)) {
                        FacesMessage msgErro = new FacesMessage("Erro: Na associação de escala a data final deve ser maior que a data inicial!");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    } else {
                        //Busca dados de regime e o departamento ao qual pertence o funcionários selecionado
                        funcionarioSelecionado = banco.consultaFuncionario(cod_funcionario);
                        nome = funcionarioSelecionado.getNome();

                        //Mapeia (põe em lista tipo hash) todos os dias entre os dias limites
                        HashMap<Integer, String> mapaDiaEraDataStr = mapearDiaEraDataStr(dataInicio, dataFim);

                        //Mapeia os detalhes de hora extra referente ao funcionário
                        tipoHoraExtra = banco.consultaTipoHoraExtra(cod_funcionario);
                        tipo_valorHorasExtra = new HashMap<String, Integer>();

                        //Mapeia os dias do tipo deslocamento temporario do usuário
                        diasDeslocadosHashMap = banco.consultaDiasDeslocados(cod_funcionario, dataInicio, dataFim);

                        //Mapeia os dias do tipo deslocamento temporario que são hora extra do usuário
                        diasDeslocadoshoraExtra = banco.consultaDiasDeslocadosHoraExtra(cod_funcionario, dataInicio, dataFim);

                        //Tras todos os deslocamentos temporários
                        diasDeslocamentoTempHashMap = banco.consultaDiasDeslocamentoTemp(cod_funcionario, dataInicio, dataFim);

                        //Consulta todos os afastamentos do usuário
                        setAfastamentoList(banco.consultaAfastamento(cod_funcionario, dataInicio, dataFim));

                        //Consulta horas extra
                        diasComHorasExtraList = banco.consultaDiasHoraExtra(cod_funcionario, dataInicio, dataFim);

                        //Mapeia (novamente) os dias do tipo deslocamento temporario que são hora extra do usuário
                        diasHoraExtra = banco.consultaDiasDeslocadosHoraExtra(cod_funcionario, dataInicio, dataFim);

                        //Mapeia todos os abonos do usuário dentro do periodo pesquisado
                        pontosAdicionados = banco.consultaRegistrosAdicionados(cod_funcionario, dataInicio, dataFim);

                        //Retorna em lista todas as gratificações do funcionário
                        gratificacaoList = banco.consultaGratificacao(cod_funcionario);

                        //Consulta solicitações de abonos pendentes no periodo selecionado
                        diasPendentesList = banco.consultaDiasPendentes(cod_funcionario, dataInicio, dataFim);

                        pontos = new ArrayList<>();
                        pontos.addAll(getRegistros(pontosAdicionados));

                        //Mapeia periodo de contrato do funcionário
                        List<Date> periodoPrecontrato = getPeriodoPreContrato();

                        //Lista os abonos
                        abonoList = banco.consultaAbono(cod_funcionario, dataInicio, dataFim);

                        //Põe em variavel global somente o regime hora  extra do funcionário
                        regimeHoraExtra = banco.getRegimeHoraExtra(cod_funcionario);

                        if (regimeHoraExtra.getFeriadoCritico()) {
                            feriadoCriticosList = banco.consultaFeriadosCriticos(dataInicio, dataFim);
                        }

                        if (periodoPrecontrato != null && !diasDeslocadosHashMap.isEmpty()) {
                            List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoPrecontrato.get(0), periodoPrecontrato.get(1));
                            pontos = addListPontoSemRedundancia(pontos, pontoList);
                            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoPrecontrato.get(0), periodoPrecontrato.get(1));
                            diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
                        }

                        for (PeriodoJornada periodoJornada : dataPeriodoJornadaList) {

                            //inicializarAtributosConsultaDias2();
                            jornadaList = new ArrayList<Jornada>();
                            //Date startDate = banco.consultaStartDate(num_jornada, periodoJornada.getInicioJornada(), dataInicio);
                            Date startDate = periodoJornada.getInicioJornada();

                            if (startDate.getTime() <= periodoJornada.getFim().getTime()) {

                                diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

                                Boolean feriadoInflui = banco.feriadoInflui(cod_funcionario);
                                if (feriadoInflui) {
                                    feriadoList.addAll(banco.consultaFeriados(periodoJornada.getInicio(), periodoJornada.getFim(), diasDeslocadosAdicionais));
                                }

                                List<Integer> diasDeslocadosFolgas = banco.consultaDiasDeslocadosSemTrabalho(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

                                List<Integer> diasTrabalhoList = banco.consultaDiasTrabalho(cod_funcionario, periodoJornada.getNum_jornada(), periodoJornada.getInicio(),
                                        periodoJornada.getFim(), startDate, diasDeslocadosFolgas, feriadoList);

                                addDiasDeslocados(diasTrabalhoList, diasDeslocadosAdicionais);

                                qntDSR += calcDSR(periodoJornada.getNum_jornada(), periodoJornada.getInicio(), periodoJornada.getFim(), startDate, diasDeslocadosFolgas, feriadoList);

                                List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

                                pontos = addListPontoSemRedundancia(pontos, pontoList);

                                jornadaList = banco.consultaRegraJornada(cod_funcionario, periodoJornada.getNum_jornada());

                                HashMap<Integer, List<Integer>> diaMesHash = banco.getDiaHashMap(cod_funcionario, periodoJornada.getNum_jornada(), periodoJornada.getInicio(), periodoJornada.getFim(), startDate);
                                sdiasHashMap.putAll(diaMesHash);

                                List<DiaComEscala> diasComEscala = apurarDiasTrabalhados(pontos, diasTrabalhoList, mapaDiaEraDataStr);
                                if (diasComEscala.size() > 0) {
                                    diasList.addAll(diasComEscala);
                                }
                            }
                        }

                        List<Date> periodoPoscontrato = getPeriodoPosContrato();

                        if (periodoPoscontrato != null && !diasDeslocadosHashMap.isEmpty()) {
                            List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoPoscontrato.get(0), periodoPoscontrato.get(1));
                            pontos = addListPontoSemRedundancia(pontos, pontoList);
                            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoPoscontrato.get(0), periodoPoscontrato.get(1));
                            diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
                        }

                        if (diasList.isEmpty() && !diasDeslocadosHashMap.isEmpty()) {
                            List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, dataInicio, dataFim);
                            pontos = addListPontoSemRedundancia(pontos, pontoList);
                            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, dataInicio, dataFim);
                            diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
                        }

                        if (diasList.isEmpty()) {
                            FacesMessage msgErro = new FacesMessage("O intervalo de tempo selecionado não possui dias de trabalho para o funcionário!");
                            FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        } else {
                            horasTotal = calcularHorasTrabalhadas(horasTrabalhadasDiaList);
                            horasASeremTrabalhadasTotal = calcularHorasTrabalhadas(horasaSeremTrabalhadasDiaList);

                            GregorianCalendar diaMudancaCalculo = new GregorianCalendar();
                            diaMudancaCalculo.set(2013, 04, 31);
                            if (dataFim.before(diaMudancaCalculo.getTime())) {
                                saldo2 = minutosIntToHoras(saldoDiferencialEmMinutos(horasTotal, horasASeremTrabalhadasTotal));
                            } else {
                                saldo2 = minutosIntToHoras(saldoSomatorioEmMinutos(diasList));
                            }
                            saldoTotal = calcHorasTrabalhadas(diasList)[0];
                            saldoSemFaltas = calcHorasTrabalhadas(diasList)[1];
                            calcAtrasos(diasList);
                        }
                        if (tipo_valorHorasExtra.isEmpty()) {
                            setHoraExtraVazia();
                        }
                        diasComRegistroSemEscalaList = getListaDiaSemEscalaPoremForaDaEscala();
                        horaExtra = gethoraExtra(tipo_valorHorasExtra);
                        adicionalNoturnoFinal = (int) ((double) adicionalNoturnoFinal * (60 / 52.5)); //Converte o valor bruto e minutos trabalhados para se adequar as regras de 52:30 minutos valendo como uma hora.
                        adicionalNoturnoStr = transformaSegundosEmHora(adicionalNoturnoFinal);
                        gratificacaoStr = resumoGratificacao(somaGratificacao);
                        saldoDiaCriticoStr = transformaSegundosEmHora(saldoDiaCriticoInt);
                    }
                }
            }
        } catch (NullPointerException e) {
            FacesMessage msgErro = new FacesMessage("Erro ao ler o período selecionado!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            System.out.println(e);
        } finally {
            try {
                banco.fecharConexao();
            } catch (Exception e) {
            }
        }
    }

    public void consultaDiasSemMsgErro() {
        Banco banco = new Banco();
        inicializarAtributosConsultaDias();

        try {
            //Validando entrada de dados
            if (isEntradaValida()) {
                //Verificando se a data de consulta inicial é menor ou igual a final
                if (!isDataInicioMaiorQueDataFim()) {

                    // Buscando todas as jornadas associadas ao funcionário no período especificado
                    List<PeriodoJornada> dataPeriodoJornadaList = banco.consultaPeriodoJornada(cod_funcionario, dataInicio, dataFim);

                    //O attendance permite a associação da jornada de forma que a data inicial seja maior que a final. Então foi feito o tratamento.
                    if (!isPeridoInvalido(dataPeriodoJornadaList)) {

                        funcionarioSelecionado = banco.consultaFuncionario(cod_funcionario);
                        nome = funcionarioSelecionado.getNome();
                        HashMap<Integer, String> mapaDiaEraDataStr = mapearDiaEraDataStr(dataInicio, dataFim);

                        tipoHoraExtra = banco.consultaTipoHoraExtra(cod_funcionario);
                        tipo_valorHorasExtra = new HashMap<String, Integer>();

                        diasDeslocadosHashMap = banco.consultaDiasDeslocados(cod_funcionario, dataInicio, dataFim);

                        diasDeslocadoshoraExtra = banco.consultaDiasDeslocadosHoraExtra(cod_funcionario, dataInicio, dataFim);

                        diasDeslocamentoTempHashMap = banco.consultaDiasDeslocamentoTemp(cod_funcionario, dataInicio, dataFim);

                        //afastamento = banco.consultaAfastamento(cod_funcionario, dataInicio, dataFim);
                        setAfastamentoList(banco.consultaAfastamento(cod_funcionario, dataInicio, dataFim));

                        diasComHorasExtraList = banco.consultaDiasHoraExtra(cod_funcionario, dataInicio, dataFim);

                        diasHoraExtra = banco.consultaDiasDeslocadosHoraExtra(cod_funcionario, dataInicio, dataFim);

                        pontosAdicionados = banco.consultaRegistrosAdicionados(cod_funcionario, dataInicio, dataFim);

                        gratificacaoList = banco.consultaGratificacao(cod_funcionario);

                        diasPendentesList = banco.consultaDiasPendentes(cod_funcionario, dataInicio, dataFim);

                        pontos = new ArrayList<Ponto>();
                        pontos.addAll(getRegistros(pontosAdicionados));
                        List<Date> periodoPrecontrato = getPeriodoPreContrato();

                        abonoList = banco.consultaAbono(cod_funcionario, dataInicio, dataFim);

                        regimeHoraExtra = banco.getRegimeHoraExtra(cod_funcionario);

                        if (regimeHoraExtra.getFeriadoCritico()) {
                            feriadoCriticosList = banco.consultaFeriadosCriticos(dataInicio, dataFim);
                        }

                        if (periodoPrecontrato != null && !diasDeslocadosHashMap.isEmpty()) {
                            pontos.addAll(banco.consultaChechInOut(cod_funcionario, periodoPrecontrato.get(0), periodoPrecontrato.get(1)));
                            ordenarPontos(pontos);
                            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoPrecontrato.get(0), periodoPrecontrato.get(1));
                            diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
                        }

                        for (PeriodoJornada periodoJornada : dataPeriodoJornadaList) {
                            inicializarAtributosConsultaDias2();
                            Integer num_jornada = periodoJornada.getNum_jornada();
                            //Date startDate = banco.consultaStartDate(num_jornada, periodoJornada.getInicioJornada(), dataInicio);
                            Date startDate = periodoJornada.getInicioJornada();

                            if (startDate.getTime() <= periodoJornada.getFim().getTime()) {

                                diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

                                Boolean feriadoInflui = banco.feriadoInflui(cod_funcionario);

                                if (feriadoInflui) {
                                    feriadoList.addAll(banco.consultaFeriados(periodoJornada.getInicio(), periodoJornada.getFim(), diasDeslocadosAdicionais));
                                }

                                List<Integer> diasDeslocadosFolgas = banco.consultaDiasDeslocadosSemTrabalho(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

                                List<Integer> diasTrabalhoList = banco.consultaDiasTrabalho(cod_funcionario, num_jornada, periodoJornada.getInicio(),
                                        periodoJornada.getFim(), startDate, diasDeslocadosFolgas, feriadoList);

                                addDiasDeslocados(diasTrabalhoList, diasDeslocadosAdicionais);

                                qntDSR += calcDSR(num_jornada, periodoJornada.getInicio(), periodoJornada.getFim(), startDate, diasDeslocadosFolgas, feriadoList);
                                List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());
                                pontos = addListPontoSemRedundancia(pontos, pontoList);
                                jornadaList = banco.consultaRegraJornada(cod_funcionario, num_jornada);
                                HashMap<Integer, List<Integer>> diaMesHash = banco.getDiaHashMap(cod_funcionario, num_jornada, periodoJornada.getInicio(), periodoJornada.getFim(), startDate);
                                sdiasHashMap.putAll(diaMesHash);
                                diasList.addAll(apurarDiasTrabalhados(pontos, diasTrabalhoList, mapaDiaEraDataStr));
                            }
                        }
                        List<Date> periodoPoscontrato = getPeriodoPosContrato();
                        if (periodoPoscontrato != null && !diasDeslocadosHashMap.isEmpty()) {
                            List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoPoscontrato.get(0), periodoPoscontrato.get(1));
                            pontos = addListPontoSemRedundancia(pontos, pontoList);
                            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoPoscontrato.get(0), periodoPoscontrato.get(1));
                            diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
                        }

                        if (diasList.isEmpty() && !diasDeslocadosHashMap.isEmpty()) {
                            List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, dataInicio, dataFim);
                            pontos = addListPontoSemRedundancia(pontos, pontoList);
                            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, dataInicio, dataFim);
                            diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
                        }

                        if (!diasList.isEmpty()) {
                            horasTotal = calcularHorasTrabalhadas(horasTrabalhadasDiaList);
                            horasASeremTrabalhadasTotal = calcularHorasTrabalhadas(horasaSeremTrabalhadasDiaList);

                            GregorianCalendar diaMudancaCalculo = new GregorianCalendar();
                            diaMudancaCalculo.set(2013, 04, 31);
                            if (dataFim.before(diaMudancaCalculo.getTime())) {
                                saldo2 = minutosIntToHoras(saldoDiferencialEmMinutos(horasTotal, horasASeremTrabalhadasTotal));
                            } else {
                                saldo2 = minutosIntToHoras(saldoSomatorioEmMinutos(diasList));
                            }
                            saldoTotal = calcHorasTrabalhadas(diasList)[0];
                            saldoSemFaltas = calcHorasTrabalhadas(diasList)[1];
                            calcAtrasos(diasList);
                        }

                        if (tipo_valorHorasExtra.isEmpty()) {
                            setHoraExtraVazia();
                        }
                        horaExtra = gethoraExtra(tipo_valorHorasExtra);
                        adicionalNoturnoStr = transformaSegundosEmHora(adicionalNoturnoFinal);
                        gratificacaoStr = resumoGratificacao(somaGratificacao);
                        saldoDiaCriticoStr = transformaSegundosEmHora(saldoDiaCriticoInt);
                    }

                }
            }
        } catch (NullPointerException e) {
            System.out.println("ConsultaPonto.ConsultaFrequenciaComEscalaBean.consultaDiasSemMsgErro: \n" + e);
        } finally {
            try {
                banco.fecharConexao();
            } catch (Exception e) {
            }
        }
    }

    /*
     // Examina da mesma forma que o método calcular dias, porém na exibe mensagens.
     public void consultaDiasSemMsgErro() {
     Banco banco = new Banco();
     inicializarAtributosConsultaDias();

     try {
     //Validando entrada de dados
     if (isEntradaValida()) {
     //Verificando se a data de consulta inicial é menor ou igual a final
     if (!isDataInicioMaiorQueDataFim()) {

     // Buscando todas as jornadas associadas ao funcionário no período especificado
     List<PeriodoJornada> dataPeriodoJornadaList = new ArrayList<PeriodoJornada>();
     dataPeriodoJornadaList = banco.consultaPeriodoJornada(cod_funcionario, dataInicio, dataFim);

     //O attendance permite a associação da jornada de forma que a data inicial seja maior que a final. Então foi feito o tratamento.
     if (!isPeridoInvalido(dataPeriodoJornadaList)) {

     funcionarioSelecionado = banco.consultaFuncionario(cod_funcionario);
     nome = funcionarioSelecionado.getNome();
     HashMap<Integer, String> mapaDiaEraDataStr = mapearDiaEraDataStr(dataInicio, dataFim);

     tipoHoraExtra = banco.consultaTipoHoraExtra(cod_funcionario);
     tipo_valorHorasExtra = new HashMap<String, Integer>();

     diasDeslocadosHashMap = new HashMap<String, ArrayList<Jornada>>();
     diasDeslocadosHashMap = banco.consultaDiasDeslocados(cod_funcionario, dataInicio, dataFim);

     diasDeslocadoshoraExtra = new ArrayList<Integer>();
     diasDeslocadoshoraExtra = banco.consultaDiasDeslocadosHoraExtra(cod_funcionario, dataInicio, dataFim);

     diasDeslocamentoTempHashMap = banco.consultaDiasDeslocamentoTemp(cod_funcionario, dataInicio, dataFim);

     //afastamento = banco.consultaAfastamento(cod_funcionario, dataInicio, dataFim);
     setAfastamentoList(banco.consultaAfastamento(cod_funcionario, dataInicio, dataFim));

     diasComHorasExtraList = banco.consultaDiasHoraExtra(cod_funcionario, dataInicio, dataFim);

     diasHoraExtra = new ArrayList<Integer>();
     diasHoraExtra = banco.consultaDiasDeslocadosHoraExtra(cod_funcionario, dataInicio, dataFim);

     pontosAdicionados = new ArrayList<RegistroAdicionado>();
     pontosAdicionados = banco.consultaRegistrosAdicionados(cod_funcionario, dataInicio, dataFim);

     gratificacaoList = banco.consultaGratificacao(cod_funcionario);

     diasPendentesList = banco.consultaDiasPendentes(cod_funcionario, dataInicio, dataFim);

     pontos = new ArrayList<Ponto>();
     pontos.addAll(getRegistros(pontosAdicionados));
     List<Date> periodoPrecontrato = getPeriodoPreContrato();

     abonoList = banco.consultaAbono(cod_funcionario, dataInicio, dataFim);

     regimeHoraExtra = banco.getRegimeHoraExtra(cod_funcionario);

     if (regimeHoraExtra.getFeriadoCritico()) {
     feriadoCriticosList = banco.consultaFeriadosCriticos(dataInicio, dataFim);
     }

     if (periodoPrecontrato != null && !diasDeslocadosHashMap.isEmpty()) {
     pontos.addAll(banco.consultaChechInOut(cod_funcionario, periodoPrecontrato.get(0), periodoPrecontrato.get(1)));
     ordenarPontos(pontos);
     diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoPrecontrato.get(0), periodoPrecontrato.get(1));
     diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
     }

     for (PeriodoJornada periodoJornada : dataPeriodoJornadaList) {
     inicializarAtributosConsultaDias2();
     Integer num_jornada = periodoJornada.getNum_jornada();
     //Date startDate = banco.consultaStartDate(num_jornada, periodoJornada.getInicioJornada(), dataInicio);
     Date startDate = periodoJornada.getInicioJornada();

     if (startDate.getTime() <= periodoJornada.getFim().getTime()) {

     diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

     Boolean feriadoInflui = banco.feriadoInflui(cod_funcionario);

     if (feriadoInflui) {
     feriadoList.addAll(banco.consultaFeriados(periodoJornada.getInicio(), periodoJornada.getFim(), diasDeslocadosAdicionais));
     }

     List<Integer> diasDeslocadosFolgas = banco.consultaDiasDeslocadosSemTrabalho(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());

     List<Integer> diasTrabalhoList = banco.consultaDiasTrabalho(cod_funcionario, num_jornada, periodoJornada.getInicio(),
     periodoJornada.getFim(), startDate, diasDeslocadosFolgas, feriadoList);

     addDiasDeslocados(diasTrabalhoList, diasDeslocadosAdicionais);

     qntDSR += calcDSR(num_jornada, periodoJornada.getInicio(), periodoJornada.getFim(), startDate, diasDeslocadosFolgas, feriadoList);
     List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoJornada.getInicio(), periodoJornada.getFim());
     pontos = addListPontoSemRedundancia(pontos, pontoList);
     jornadaList = banco.consultaRegraJornada(cod_funcionario, num_jornada);
     HashMap<Integer, List<Integer>> diaMesHash = banco.getDiaHashMap(cod_funcionario, num_jornada, periodoJornada.getInicio(), periodoJornada.getFim(), startDate);
     sdiasHashMap.putAll(diaMesHash);
     diasList.addAll(apurarDiasTrabalhados(pontos, diasTrabalhoList, mapaDiaEraDataStr));
     }
     }
     List<Date> periodoPoscontrato = getPeriodoPosContrato();
     if (periodoPoscontrato != null && !diasDeslocadosHashMap.isEmpty()) {
     List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, periodoPoscontrato.get(0), periodoPoscontrato.get(1));
     pontos = addListPontoSemRedundancia(pontos, pontoList);
     diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, periodoPoscontrato.get(0), periodoPoscontrato.get(1));
     diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
     }

     if (diasList.isEmpty() && !diasDeslocadosHashMap.isEmpty()) {
     List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, dataInicio, dataFim);
     pontos = addListPontoSemRedundancia(pontos, pontoList);
     diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, dataInicio, dataFim);
     diasList.addAll(apurarDiasTrabalhados(pontos, diasDeslocadosAdicionais, mapaDiaEraDataStr));
     }

     if (!diasList.isEmpty()) {
     horasTotal = calcularHorasTrabalhadas(horasTrabalhadasDiaList);
     horasASeremTrabalhadasTotal = calcularHorasTrabalhadas(horasaSeremTrabalhadasDiaList);

     GregorianCalendar diaMudancaCalculo = new GregorianCalendar();
     diaMudancaCalculo.set(2013, 04, 31);
     if (dataFim.before(diaMudancaCalculo.getTime())) {
     saldo2 = minutosIntToHoras(saldoDiferencialEmMinutos(horasTotal, horasASeremTrabalhadasTotal));
     } else {
     saldo2 = minutosIntToHoras(saldoSomatorioEmMinutos(diasList));
     }
     saldoTotal = calcHorasTrabalhadas(diasList)[0];
     saldoSemFaltas = calcHorasTrabalhadas(diasList)[1];
     calcAtrasos(diasList);
     }

     if (tipo_valorHorasExtra.isEmpty()) {
     setHoraExtraVazia();
     }
     horaExtra = gethoraExtra(tipo_valorHorasExtra);
     adicionalNoturnoStr = transformaSegundosEmHora(adicionalNoturnoFinal);
     gratificacaoStr = resumoGratificacao(somaGratificacao);
     saldoDiaCriticoStr = transformaSegundosEmHora(saldoDiaCriticoInt);
     }

     }
     }
     } catch (NullPointerException e) {
     System.out.println("ConsultaPonto.ConsultaFrequenciaComEscalaBean.consultaDiasSemMsgErro: \n" + e);
     } finally {
     try {
     banco.fecharConexao();
     } catch (Exception e) {
     }
     }
     }*/
    private List<DiaComEscala> apurarDiasTrabalhados(List<Ponto> pontos, List<Integer> diasTrabalhoList,
            HashMap<Integer, String> mapaDiaEraDataStr) {

        List<DiaComEscala> diasListt = new ArrayList<DiaComEscala>();
        List<Ponto> pontosDoDia = new ArrayList<Ponto>();
        DiaComEscala diaAcesso = new DiaComEscala();
        List<Jornada> jornadaDiaList = new ArrayList<Jornada>();
        boolean oFeriadoTemVirada = false;
        HashMap<Date, List<Ponto>> pontoPorDiaHashMap = separarPontosPorDia2(pontos);
        try {
            for (Iterator<Integer> it = diasTrabalhoList.iterator(); it.hasNext();) {
                int diaAno = it.next();

                String dataStr = mapaDiaEraDataStr.get(diaAno);
                Date data = dateStrToDate(dataStr);
                jornadaDiaList = getJornada(data);
                if (jornadaDiaList.isEmpty()) {
                    continue;
                }
                diaAcesso = new DiaComEscala();
                diaAcesso = setDiaFeriado(diaAno);

                diaAcesso.setJornadaList(jornadaDiaList);
                diaAcesso.setDia(data);

                diaAcesso.setRegimeHoraExtra(regimeHoraExtra);

                boolean isFeriadoDentroDeUmaVirada = true;

                // Desfeito o acesso direto pois ocorria uma exceção: IndexOutOfBoundsException
                List<Jornada> jornadas = diaAcesso.getJornadaList();
                if (jornadas.size() > 0) {
                    if (jornadas.get(0).getSoSaida()) {
                        isFeriadoDentroDeUmaVirada = false;
                    }
                }

                if (diaAcesso.getDefinicao().equals("Feriado") && !diasDeslocadosAdicionais.contains(diaAno)
                        && isFeriadoDentroDeUmaVirada) {
                    pontosDoDia = pontoPorDiaHashMap.get(zeraData(data));
                    //Adicionar os abonos em massa!
                    List<RegistroAdicionado> registroAbonoEmMassa = abonarPonto(diaAcesso, abonoList, jornadaDiaList);
                    if (!registroAbonoEmMassa.isEmpty()) {
                        pontosAdicionados.addAll(registroAbonoEmMassa);
                        List<Ponto> pontosEmMassa = registroAdicionadoToPonto(pontosAdicionados, diaAcesso);
                        if (pontosDoDia == null) {
                            pontosDoDia = new ArrayList<Ponto>();
                        }
                        pontosDoDia.addAll(pontosEmMassa);
                        ordenarPontos(pontosDoDia);
                    }
                    if (pontosDoDia != null && !isDiaFolga(data)) {
                        diaAcesso = apurarDia(pontosDoDia, jornadaDiaList);
                        diasListt.add(diaAcesso);
                    } else if (isDiaAfastado(data)) {
                        diaAcesso = apurarAfastamento(data, getAfastamentoList());
                        diasListt.add(diaAcesso);
                    } else {
                        if (!diaAcesso.getDefinicao().equals("Feriado")) {
                            diaAcesso = apurarDiaFalta(dataStr, jornadaDiaList);
                        }
                        diasListt.add(diaAcesso);
                    }
                    if (diaAcesso.getTemIntervalo()) {
                        oFeriadoTemVirada = true;
                    }
                } else {
                    if (oFeriadoTemVirada) {
                        if (jornadaDiaList.get(0).getSoSaida()) {
                            jornadaDiaList.remove(0);
                        }
                    }

                    setInfoEscala(jornadaDiaList, data);

                    oFeriadoTemVirada = false;
                    pontosDoDia = pontoPorDiaHashMap.get(zeraData(data));
                    //Adicionar os abonos em massa!
                    List<RegistroAdicionado> registroAbonoEmMassa = abonarPonto(diaAcesso, abonoList, jornadaDiaList);
                    if (!registroAbonoEmMassa.isEmpty()) {
                        pontosAdicionados.addAll(registroAbonoEmMassa);
                        List<Ponto> pontosEmMassa = registroAdicionadoToPonto(pontosAdicionados, diaAcesso);
                        if (pontosDoDia == null) {
                            pontosDoDia = new ArrayList<Ponto>();
                        }
                        pontosDoDia.addAll(pontosEmMassa);
                        ordenarPontos(pontosDoDia);
                    }
                    if (pontosDoDia != null && !isDiaFolga(data)) {
                        diaAcesso = apurarDia(pontosDoDia, jornadaDiaList);
                        diasListt.add(diaAcesso);
                    } else if (isDiaAfastado(data)) {
                        diaAcesso = apurarAfastamento(data, getAfastamentoList());
                        diasListt.add(diaAcesso);
                    } else {
                        if (!diaAcesso.getDefinicao().equals("Feriado")) {
                            diaAcesso = apurarDiaFalta(dataStr, jornadaDiaList);
                        }
                        diasListt.add(diaAcesso);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("ConsultaPonto.ConsultaFrequenciaComEscalaBean.apurarDiasTrabalhados: \n" + ex);
            //ex.printStackTrace();
        }
        return diasListt;
    }

    private DiaComEscala apurarDia(List<Ponto> pontos, List<Jornada> jornadaDiaList) {
        DiaComEscala diaAcesso = new DiaComEscala();

        GregorianCalendar diaHora = new GregorianCalendar();

        diaHora.setTimeInMillis(pontos.get(0).getPonto().getTime());
        diaAcesso.setDia(diaHora.getTime());
        addPontosPreAssinalados(diaAcesso, jornadaDiaList);

        Locale.setDefault(new Locale("pt", "BR"));

        SimpleDateFormat sdfSemana = new SimpleDateFormat("EE dd/MM/yyyy");
        diaAcesso.setDiaString(sdfSemana.format(diaHora.getTime()));
        boolean entrada1Guarda = true;
        boolean entrada2Guarda = true;

        //flags de para indicar se o registro foi liberado pelo gestor
        boolean entrada1Liberada = false;
        boolean entrada2Liberada = false;
        boolean saida1Liberada = false;
        boolean saida2Liberada = false;

        int qntTurnos = jornadaDiaList.size();
        for (Iterator<Ponto> it = pontos.iterator(); it.hasNext();) {
            Ponto pontoObj = it.next();
            Long ponto = pontoObj.getPonto().getTime();
            String local;
            if (pontoObj.getLocal() == null || pontoObj.getLocal().equals("")) {
                local = " - ";
            } else {
                local = pontoObj.getLocal();
            }

            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");

            String hora = sdfHora.format(ponto);
            String horaPonto = sdfHora.format(ponto);

            //Caso o tipoRegistroAbono for 0 é por que não é um registro abonado.
            //Caso for 1 é um registro abono sem inteferir em gratificações.
            //Caso 2 intefere em gratificações
            String tipoRegistroAbonoFlag = "0";
            if (containsRegistro(ponto, pontosAdicionados)) {
                horaPonto += "*";
                diaAcesso.setColorDia("pbodyAbono");
                RegistroAdicionado registroAdicionado = findRegistro(ponto, pontosAdicionados);
                String justificativaAbono = "";
                if (registroAdicionado.getJustificativa() != null) {
                    justificativaAbono = registroAdicionado.getJustificativa();
                }
                diaAcesso.setJustificativa(registroAdicionado.getCategoriaJustificativa() + " - " + justificativaAbono);
                diaAcesso.setDefinicao("Abonado");

                if (registroAdicionado.getIsTotal()) {
                    tipoRegistroAbonoFlag = "2";
                } else {
                    tipoRegistroAbonoFlag = "1";
                }

                pontoObj.setIsAbonado(true);
                pontoObj.setJustificativa(registroAdicionado.getCategoriaJustificativa());
                pontoObj.setTipoRegistro(registroAdicionado.getTipoRegistro());
                local = "Abonado";

            }

            if (pontoObj.getTipoRegistro() != 0) {
                if (pontoObj.getTipoRegistro().equals(1)) {
                    diaAcesso.getEntrada_1().setRegistro(horaPonto);
                    if (!tipoRegistroAbonoFlag.equals("0")) {
                        diaAcesso.getEntrada_1().setIsAbonado(true);
                    }
                    if (tipoRegistroAbonoFlag.equals("2")) {
                        diaAcesso.getEntrada_1().setIsTotal(true);
                    }
                    if (!diaAcesso.getSaida_1().getRegistro().equals("")) {
                        List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                        SituacaoPonto sit;
                        if (qntTurnos >= 2) {
                            sit = searchSituacao(situaçãoList, "Entrada do 1º turno");
                        } else {
                            sit = searchSituacao(situaçãoList, "Entrada");
                        }

                        sit.setSituacao("Descartado");
                    }

                    if (qntTurnos >= 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada");
                    }

                    entrada1Liberada = true;
                }
                if (pontoObj.getTipoRegistro().equals(2)) {
                    if (!diaAcesso.getSaida_1().getRegistro().equals("")) {
                        List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                        SituacaoPonto sit;
                        if (qntTurnos >= 2) {
                            sit = searchSituacao(situaçãoList, "Saida do 1º turno");
                        } else {
                            sit = searchSituacao(situaçãoList, "Saida");
                        }
                        sit.setSituacao("Descartado");
                    }
                    if (qntTurnos >= 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida");
                    }
                    diaAcesso.getSaida_1().setRegistro(horaPonto);
                    if (!tipoRegistroAbonoFlag.equals("0")) {
                        diaAcesso.getSaida_1().setIsAbonado(true);
                    }
                    if (tipoRegistroAbonoFlag.equals("2")) {
                        diaAcesso.getSaida_1().setIsTotal(true);
                    }
                    saida1Liberada = true;
                }
                if (pontoObj.getTipoRegistro().equals(3)) {
                    diaAcesso.getEntrada_2().setRegistro(horaPonto);
                    if (!tipoRegistroAbonoFlag.equals("0")) {
                        diaAcesso.getEntrada_2().setIsAbonado(true);
                    }
                    if (tipoRegistroAbonoFlag.equals("2")) {
                        diaAcesso.getEntrada_2().setIsTotal(true);
                    }
                    if (!diaAcesso.getEntrada_2().getRegistro().equals("")) {
                        List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                        SituacaoPonto sit;
                        if (qntTurnos == 2) {
                            sit = searchSituacao(situaçãoList, "Entrada do 2º turno");
                        } else {
                            sit = searchSituacao(situaçãoList, "Entrada");
                        }
                        sit.setSituacao("Descartado");
                    }
                    diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º turno");
                    entrada2Liberada = true;
                }
                if (pontoObj.getTipoRegistro().equals(4)) {
                    if (!diaAcesso.getSaida_2().getRegistro().equals("")) {
                        List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                        SituacaoPonto sit = searchSituacao(situaçãoList, "Saida do 2º turno");
                        sit.setSituacao("Descartado");
                    }
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida");
                    }

                    diaAcesso.getSaida_2().setRegistro(horaPonto);
                    if (!tipoRegistroAbonoFlag.equals("0")) {
                        diaAcesso.getSaida_2().setIsAbonado(true);
                    }
                    if (tipoRegistroAbonoFlag.equals("2")) {
                        diaAcesso.getSaida_2().setIsTotal(true);
                    }
                    saida2Liberada = true;
                }
                if (pontoObj.getTipoRegistro().equals(6)) {
                    diaAcesso.getEntrada_Descanso_1_1().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º Descanso do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(7)) {
                    diaAcesso.getSaida_Descanso_1_1().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º Descanso do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(8)) {
                    diaAcesso.getEntrada_Descanso_2_1().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º Descanso do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(9)) {
                    diaAcesso.getSaida_Descanso_2_1().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º Descanso do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(10)) {
                    diaAcesso.getEntrada_Descanso_1_2().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º Descanso do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(11)) {
                    diaAcesso.getSaida_Descanso_1_2().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º Descanso do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(12)) {
                    diaAcesso.getEntrada_Descanso_2_2().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º Descanso do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(13)) {
                    diaAcesso.getSaida_Descanso_2_2().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º Descanso do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º Descanso");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(14)) {
                    diaAcesso.getEntrada_Interjornada_1().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da Interjornada do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da Interjornada");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(15)) {
                    diaAcesso.getSaida_Interjornada_1().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da Interjornada do 1º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da Interjornada");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(16)) {
                    diaAcesso.getEntrada_Interjornada_2().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da Interjornada do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da Interjornada");
                    }
                }
                if (pontoObj.getTipoRegistro().equals(17)) {
                    diaAcesso.getSaida_Interjornada_2().setRegistro(horaPonto);
                    if (qntTurnos == 2) {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da Interjornada do 2º turno");
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da Interjornada");
                    }
                }
            } else {
                if (qntTurnos == 2) { // Dias com duas jornadas
                    String chegadaAnteciadaTurno1;
                    String chegadaAtrasadaTurno1;
                    String saidaAnteciadaTurno1;
                    String saidaAtrasadaTurno1;
                    Boolean isSaidaObrigatoria1 = false;
                    if (jornadaDiaList.get(0).getIsSaidaObrigatoria() != null) {
                        isSaidaObrigatoria1 = jornadaDiaList.get(0).getIsSaidaObrigatoria().equals(1);
                    }

                    String chegadaAnteciadaTurno2 = "";
                    String chegadaAtrasadaTurno2 = "";
                    Boolean isEntradaObrigatoria2 = false;
                    if (jornadaDiaList.get(1).getIsEntradaObrigatoria() != null) {
                        isEntradaObrigatoria2 = jornadaDiaList.get(1).getIsEntradaObrigatoria().equals(1);
                    }
                    String saidaAnteciadaTurno2 = "";
                    String saidaAtrasadaTurno2 = "";

                    String IniDesc1 = "";
                    String EndDesc1 = "";
                    String IniInter = "";
                    String EndInter = "";
                    String IniDesc2 = "";
                    String EndDesc2 = "";

                    if (jornadaDiaList.get(0).getTemVirada()) {
                        if (jornadaDiaList.get(0).getSoEntrada()) {
                            chegadaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada());
                            chegadaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada());
                            saidaAnteciadaTurno1 = "24:00:00";
                            saidaAtrasadaTurno1 = "24:00:00";
                        } else {
                            chegadaAnteciadaTurno1 = "00:00:00";
                            chegadaAtrasadaTurno1 = "00:00:00";
                            saidaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada());
                            saidaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada());
                        }
                    } else {
                        chegadaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada());
                        chegadaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada());
                        saidaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada());
                        saidaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada());
                    }

                    if (jornadaDiaList.get(1).getTemVirada()) {
                        if (jornadaDiaList.get(1).getSoEntrada()) {
                            if (jornadaDiaList.get(1).getCheckInLimiteAntecipada() != null) {
                                chegadaAnteciadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada());
                            }
                            if (jornadaDiaList.get(1).getCheckInLimiteAtrasada() != null) {
                                chegadaAtrasadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada());
                            }
                            saidaAnteciadaTurno2 = jornadaDiaList.get(1).getCheckOutLimiteAntecipada_();
                            saidaAtrasadaTurno2 = jornadaDiaList.get(1).getCheckOutLimiteAtrasada_();
                        } else {
                            chegadaAnteciadaTurno2 = jornadaDiaList.get(1).getCheckInLimiteAntecipada_();
                            chegadaAtrasadaTurno2 = jornadaDiaList.get(1).getCheckInLimiteAtrasada_();
                            if (jornadaDiaList.get(1).getCheckOutLimiteAntecipada() != null) {
                                saidaAnteciadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada());
                            }
                            if (jornadaDiaList.get(1).getCheckOutLimiteAtrasada() != null) {
                                saidaAtrasadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada());
                            }
                        }
                    } else {
                        if (jornadaDiaList.get(1).getCheckInLimiteAntecipada() != null) {
                            chegadaAnteciadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada());
                        }
                        if (jornadaDiaList.get(1).getCheckInLimiteAtrasada() != null) {
                            chegadaAtrasadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada());
                        }
                        if (jornadaDiaList.get(1).getCheckOutLimiteAntecipada() != null) {
                            saidaAnteciadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada());
                        }
                        if (jornadaDiaList.get(1).getCheckOutLimiteAtrasada() != null) {
                            saidaAtrasadaTurno2 = sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada());
                        }
                    }

                    if (compararHoras(chegadaAnteciadaTurno1, chegadaAtrasadaTurno1, hora)) {
                        if (!entrada1Liberada) {
                            if (entrada1Guarda) {
                                diaAcesso.getEntrada_1().setRegistro(horaPonto);
                                if (!tipoRegistroAbonoFlag.equals("0")) {
                                    diaAcesso.getEntrada_1().setIsAbonado(true);
                                }
                                if (tipoRegistroAbonoFlag.equals("2")) {
                                    diaAcesso.getEntrada_1().setIsTotal(true);
                                }
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º turno");
                            } else {
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                            }
                            entrada1Guarda = false;
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                        }
                    } else if (compararHoras(saidaAnteciadaTurno1, saidaAtrasadaTurno1, hora)) {
                        if (!saida1Liberada) {
                            if (!isSaidaObrigatoria1) {
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                            } else {
                                if (!diaAcesso.getSaida_1().getRegistro().equals("")) {
                                    List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                                    SituacaoPonto sit = searchSituacao(situaçãoList, "Saida do 1º turno");
                                    sit.setSituacao("Descartado");
                                }
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º turno");
                                diaAcesso.getSaida_1().setRegistro(horaPonto);
                                if (!tipoRegistroAbonoFlag.equals("0")) {
                                    diaAcesso.getSaida_1().setIsAbonado(true);
                                }
                                if (tipoRegistroAbonoFlag.equals("2")) {
                                    diaAcesso.getSaida_1().setIsTotal(true);
                                }
                            }
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                        }
                    } else if (compararHoras(chegadaAnteciadaTurno2, chegadaAtrasadaTurno2, hora)) {
                        if (!entrada2Liberada) {
                            if (!isEntradaObrigatoria2) {
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                            } else {
                                if (entrada2Guarda) {
                                    diaAcesso.getEntrada_2().setRegistro(horaPonto);
                                    if (!tipoRegistroAbonoFlag.equals("0")) {
                                        diaAcesso.getEntrada_2().setIsAbonado(true);
                                    }
                                    if (tipoRegistroAbonoFlag.equals("2")) {
                                        diaAcesso.getEntrada_2().setIsTotal(true);
                                    }
                                    diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º turno");
                                } else {
                                    diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                                }
                                entrada2Guarda = false;
                            }
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                        }
                    } else if (compararHoras(saidaAnteciadaTurno2, saidaAtrasadaTurno2, hora)) {
                        if (!saida2Liberada) {
                            if (!diaAcesso.getSaida_2().getRegistro().equals("")) {
                                List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                                SituacaoPonto sit = searchSituacao(situaçãoList, "Saida do 2º turno");
                                sit.setSituacao("Descartado");
                            }
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º turno");
                            diaAcesso.getSaida_2().setRegistro(horaPonto);
                            if (!tipoRegistroAbonoFlag.equals("0")) {
                                diaAcesso.getSaida_2().setIsAbonado(true);
                            }
                            if (tipoRegistroAbonoFlag.equals("2")) {
                                diaAcesso.getSaida_2().setIsTotal(true);
                            }
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                        }

                    } else if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                        if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                            IniDesc1 = sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1());
                        }
                        if (jornadaDiaList.get(0).getFimDescanso1() != null) {
                            EndDesc1 = sdfHora.format(jornadaDiaList.get(0).getFimDescanso1());
                        }
                        if (jornadaDiaList.get(0).getInicioIntrajornada() != null) {
                            IniInter = sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada());
                        }
                        if (jornadaDiaList.get(0).getFimIntrajornada() != null) {
                            EndInter = sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada());
                        }
                        if (jornadaDiaList.get(0).getInicioDescanso2() != null) {
                            IniDesc2 = sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2());
                        }
                        if (jornadaDiaList.get(0).getFimDescanso2() != null) {
                            EndDesc2 = sdfHora.format(jornadaDiaList.get(0).getFimDescanso2());
                        }

                        if (verificaDescanso(hora, IniDesc1, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getEntrada_Descanso_1_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º Descanso do 1º turno");
                        } else if (verificaDescanso(hora, EndDesc1, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getSaida_Descanso_1_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º Descanso do 1º turno");
                        } else if (verificaDescanso(hora, IniInter, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getEntrada_Interjornada_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da interjornada do 1º turno");
                        } else if (verificaDescanso(hora, EndInter, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getSaida_Interjornada_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da interjornada do 1º turno");
                        } else if (verificaDescanso(hora, IniDesc2, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getEntrada_Descanso_2_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º Descanso do 1º turno");
                        } else if (verificaDescanso(hora, EndDesc2, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getSaida_Descanso_2_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º Descanso do 1º turno");
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Fora da faixa");
                        }
                    } else if (jornadaDiaList.get(1).getInicioDescanso1() != null) {

                        String dataIntraJornada = "";
                        if (jornadaDiaList.get(1).getInicioIntrajornada() != null) {
                            dataIntraJornada = sdfHora.format(jornadaDiaList.get(1).getInicioIntrajornada());
                        }

                        String FimIntrajornada = "";
                        if (jornadaDiaList.get(1).getFimIntrajornada() != null) {
                            FimIntrajornada = sdfHora.format(jornadaDiaList.get(1).getFimIntrajornada());
                        }

                        String dataInicioDescanso = "";
                        if (jornadaDiaList.get(1).getInicioDescanso2() != null) {
                            dataInicioDescanso = sdfHora.format(jornadaDiaList.get(1).getInicioDescanso2());
                        }

                        String dataFimDescanso2 = "";
                        if (jornadaDiaList.get(1).getFimDescanso2() != null) {
                            dataFimDescanso2 = sdfHora.format(jornadaDiaList.get(1).getFimDescanso2());
                        }

                        if (verificaDescanso(hora, sdfHora.format(jornadaDiaList.get(1).getInicioDescanso1()), jornadaDiaList.get(1).getToleranciaDescanso())
                                && jornadaDiaList.get(1).getInicioDescanso1() != null) {
                            diaAcesso.getEntrada_Descanso_1_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 1º Descanso do 2º turno");
                        } else if (verificaDescanso(hora, sdfHora.format(jornadaDiaList.get(1).getFimDescanso1()), jornadaDiaList.get(1).getToleranciaDescanso())
                                && jornadaDiaList.get(1).getFimDescanso1() != null) {
                            diaAcesso.getSaida_Descanso_1_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 1º Descanso do 2º turno");
                        } else if (verificaDescanso(hora, dataIntraJornada, jornadaDiaList.get(1).getToleranciaDescanso())
                                && jornadaDiaList.get(1).getInicioIntrajornada() != null) {
                            diaAcesso.getEntrada_Interjornada_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da interjornada do 2º turno");
                        } else if (verificaDescanso(hora, FimIntrajornada, jornadaDiaList.get(1).getToleranciaDescanso())
                                && jornadaDiaList.get(1).getFimIntrajornada() != null) {
                            diaAcesso.getSaida_Interjornada_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da interjornada do 2º turno");
                        } else if (verificaDescanso(hora, dataInicioDescanso, jornadaDiaList.get(1).getToleranciaDescanso())
                                && jornadaDiaList.get(1).getInicioDescanso2() != null) {
                            diaAcesso.getEntrada_Descanso_2_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do 2º Descanso do 2º turno");
                        } else if (verificaDescanso(hora, dataFimDescanso2, jornadaDiaList.get(1).getToleranciaDescanso())
                                && jornadaDiaList.get(1).getFimDescanso2() != null) {
                            diaAcesso.getSaida_Descanso_2_2().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do 2º Descanso do 2º turno");
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Fora da faixa");
                        }
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Fora da faixa");
                    }

                    //---------------------------------------
                    //Um turno
                } else if (jornadaDiaList.size() == 1) { // Dias com uma jornada
                    String chegadaAnteciadaTurno1;
                    String chegadaAtrasadaTurno1;
                    String saidaAnteciadaTurno1;
                    String saidaAtrasadaTurno1;

                    String IniDesc1 = "";
                    String EndDesc1 = "";
                    String IniInter = "";
                    String EndInter = "";
                    String IniDesc2 = "";
                    String EndDesc2 = "";

                    if (jornadaDiaList.get(0).getTemVirada()) {
                        if (jornadaDiaList.get(0).getSoEntrada()) {
                            chegadaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada());
                            chegadaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada());
                            saidaAnteciadaTurno1 = jornadaDiaList.get(0).getCheckOutLimiteAntecipada_();
                            saidaAtrasadaTurno1 = jornadaDiaList.get(0).getCheckOutLimiteAtrasada_();
                        } else {
                            chegadaAnteciadaTurno1 = jornadaDiaList.get(0).getCheckInLimiteAntecipada_();
                            chegadaAtrasadaTurno1 = jornadaDiaList.get(0).getCheckInLimiteAtrasada_();
                            saidaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada());
                            saidaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada());
                        }
                    } else {
                        chegadaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada());
                        chegadaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada());
                        saidaAnteciadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada());
                        saidaAtrasadaTurno1 = sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada());
                    }
                    if (compararHoras(chegadaAnteciadaTurno1, chegadaAtrasadaTurno1, hora)) {
                        if (!entrada1Liberada) {
                            if (entrada1Guarda) {
                                diaAcesso.getEntrada_1().setRegistro(horaPonto);
                                if (!tipoRegistroAbonoFlag.equals("0")) {
                                    diaAcesso.getEntrada_1().setIsAbonado(true);
                                }
                                if (tipoRegistroAbonoFlag.equals("2")) {
                                    diaAcesso.getEntrada_1().setIsTotal(true);
                                }
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada");
                            } else {
                                diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                            }
                            entrada1Guarda = false;
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                        }
                    } else if (!jornadaDiaList.get(0).getSoEntrada() && compararHoras(saidaAnteciadaTurno1, saidaAtrasadaTurno1, hora)) {
                        if (!saida1Liberada) {
                            if (!diaAcesso.getSaida_1().getRegistro().equals("")) {
                                List<SituacaoPonto> situaçãoList = diaAcesso.getSituacaoPonto();
                                SituacaoPonto sit = searchSituacao(situaçãoList, "Saida");
                                sit.setSituacao("Descartado");
                            }
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida");
                            diaAcesso.getSaida_1().setRegistro(horaPonto);
                            if (!tipoRegistroAbonoFlag.equals("0")) {
                                diaAcesso.getSaida_1().setIsAbonado(true);
                            }
                            if (tipoRegistroAbonoFlag.equals("2")) {
                                diaAcesso.getSaida_1().setIsTotal(true);
                            }
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Descartado");
                        }
                    } else if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                        if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                            IniDesc1 = sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1());
                        }
                        if (jornadaDiaList.get(0).getFimDescanso1() != null) {
                            EndDesc1 = sdfHora.format(jornadaDiaList.get(0).getFimDescanso1());
                        }
                        if (jornadaDiaList.get(0).getInicioIntrajornada() != null) {
                            IniInter = sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada());
                        }
                        if (jornadaDiaList.get(0).getFimIntrajornada() != null) {
                            EndInter = sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada());
                        }
                        if (jornadaDiaList.get(0).getInicioDescanso2() != null) {
                            IniDesc2 = sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2());
                        }
                        if (jornadaDiaList.get(0).getFimDescanso2() != null) {
                            EndDesc2 = sdfHora.format(jornadaDiaList.get(0).getFimDescanso2());
                        }

                        if (verificaDescanso(hora, IniDesc1, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getEntrada_Descanso_1_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do Descanso 1");
                        } else if (verificaDescanso(hora, EndDesc1, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getSaida_Descanso_1_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do Descanso 1");
                        } else if (verificaDescanso(hora, IniInter, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getEntrada_Interjornada_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada da interjornada");
                        } else if (verificaDescanso(hora, EndInter, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getSaida_Interjornada_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida da interjornada");
                        } else if (verificaDescanso(hora, IniDesc2, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getEntrada_Descanso_2_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Entrada do Descanso 2");
                        } else if (verificaDescanso(hora, EndDesc2, jornadaDiaList.get(0).getToleranciaDescanso())) {
                            diaAcesso.getSaida_Descanso_2_1().setRegistro(horaPonto);
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Saida do Descanso 2");
                        } else {
                            diaAcesso.setSituacaoPonto(pontoObj, local, "Fora da faixa");
                        }
                    } else {
                        diaAcesso.setSituacaoPonto(pontoObj, local, "Fora da faixa");
                    }
                    diaAcesso.setTemIntervalo(false);
                }
            }
        }
        diaAcesso.setCorDia(jornadaDiaList);
        diaAcesso.setIsDiaTodoHoraExtra(isDiaTodoHoraExtra(diaAcesso));
        Integer diaAno = diaHora.get(Calendar.DAY_OF_YEAR) + diaHora.get(Calendar.YEAR) * 365;

        if (diasPendentesList.contains(diaAno)) {
            diaAcesso.setIsPendente(true);
        } else {
            diaAcesso.setIsPendente(false);
        }

        if (feriadoCriticosList.contains(diaAno)) {
            diaAcesso.setIsDiaCritico(true);
        } else {
            diaAcesso.setIsDiaCritico(false);
        }
        diaAcesso.setCod_funcionario(cod_funcionario);

        if (qntTurnos >= 2) {
            if (hasDiaHoraExtra(diaAcesso)) {
                diaAcesso.calcSaldoTurno(jornadaDiaList, tipoHoraExtra, regimeHoraExtra, gratificacaoList);
                calcSomahoraExtra(diaAcesso.getTipo_valorHorasExtra());
            } else {
                diaAcesso.calcSaldoTurnoSemHoraExtra(jornadaDiaList, regimeHoraExtra, gratificacaoList);
            }
        } else {
            if (hasDiaHoraExtra(diaAcesso)) {
                diaAcesso.calcSaldoTurno(jornadaDiaList.get(0), tipoHoraExtra, regimeHoraExtra, gratificacaoList);
                calcSomahoraExtra(diaAcesso.getTipo_valorHorasExtra());
            } else {
                diaAcesso.calcSaldoTurnoSemHoraExtra(jornadaDiaList.get(0), regimeHoraExtra, gratificacaoList);
            }
        }
        diaAcesso.setJornadaList(jornadaDiaList);
        addHorasASeremTrabalhadas(diaAcesso);
        addHorasTrabalhadas(diaAcesso);
        addAdicionalNoturno(diaAcesso);
        addGratificacao(diaAcesso.getSaidaGratHash());
        addDiaCritico(diaAcesso.getSaldoDiaCritico());

        if (!jornadaDiaList.get(0).getSoSaida() || (jornadaDiaList.size() > 1)) {
            contDiasTrabalhados++;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        diaAcessoMap.put(sdf.format(pontos.get(0).getPonto().getTime()), diaAcesso);
        return diaAcesso;
    }

    private DiaComEscala apurarDiaFalta(String dataStr, List<Jornada> jornadaDiaList) {

        DiaComEscala diaAcesso = new DiaComEscala();

        Date data = dateStrToDate(dataStr);

        GregorianCalendar diaHora = new GregorianCalendar();
        diaHora.setTimeInMillis(data.getTime());
        diaAcesso.setDia(diaHora.getTime());
        Locale.setDefault(new Locale("pt", "BR"));
        SimpleDateFormat sdfSemana = new SimpleDateFormat("EE dd/MM/yyyy");

        int qntTurnos = jornadaDiaList.size();
        diaAcesso.setIsDiaTodoHoraExtra(isDiaTodoHoraExtra(diaAcesso));
        Integer diaAno = diaHora.get(Calendar.DAY_OF_YEAR) + diaHora.get(Calendar.YEAR) * 365;
        if (diasPendentesList.contains(diaAno)) {
            diaAcesso.setIsPendente(true);
        } else {
            diaAcesso.setIsPendente(false);
        }
        diaAcesso.setIsDiaCritico(false);
        diaAcesso.setPresente(false);
        diaAcesso.setCod_funcionario(cod_funcionario);
        if (qntTurnos == 2) {
            diaAcesso.calcSaldoTurnoSemHoraExtra(jornadaDiaList, regimeHoraExtra, gratificacaoList);
        } else {
            if (!jornadaDiaList.isEmpty()) {
                diaAcesso.calcSaldoTurnoSemHoraExtra(jornadaDiaList.get(0), regimeHoraExtra, gratificacaoList);
            }
        }

        diaAcesso.setDiaString(sdfSemana.format(diaHora.getTime()));
        diaAcesso.setDefinicao("Irregular");
        if (!isDiaFolga(data)) {
            diaAcesso.setJustificativa("FALTA");
            diaAcesso.setColorDia("panelVermelho");
        } else {
            diaAcesso.setJustificativa("FOLGA COMPENSATÓRIA");
            diaAcesso.setColorDia("panelBranco");
        }

        addHorasASeremTrabalhadas(diaAcesso);

        if (!isDiaFolga(data)) {
            if (!jornadaDiaList.isEmpty()) {
                if (!jornadaDiaList.get(0).getSoSaida()) {
                    faltas++;
                }
            }
            if (jornadaDiaList.size() > 1) {
                if (jornadaDiaList.get(0).getTemVirada() && !jornadaDiaList.get(1).getSoSaida()) {
                    faltas++;
                }
            }
        }
        diaAcesso.setJornadaList(jornadaDiaList);
        return diaAcesso;
    }

    private DiaComEscala apurarAfastamento(Date date, List<Afastamento> afastamentoList) {

        DiaComEscala diaAcesso = new DiaComEscala();
        SimpleDateFormat sdfSemana = new SimpleDateFormat("EE dd/MM/yyyy");

        diaAcesso.setDia(date);
        Locale.setDefault(new Locale("pt", "BR"));
        diaAcesso.setIsDiaTodoHoraExtra(false);
        diaAcesso.setIsPendente(false);
        diaAcesso.setIsDiaCritico(false);
        diaAcesso.setDiaString(sdfSemana.format(date.getTime()));
        diaAcesso.setDefinicao("Afastado");
        diaAcesso.setIsAfastado(true);
        for (Iterator<Afastamento> it = afastamentoList.iterator(); it.hasNext();) {
            Afastamento afastamento = it.next();

            if ((afastamento.getDataInicio().before(date) || afastamento.getDataInicio().equals(date))
                    && ((afastamento.getDataFim().after(date)) || afastamento.getDataFim().equals(date))) {

                diaAcesso.setJustificativa(afastamento.getCategoriaAfastamento().getDescCategoriaAfastamento());
            }
        }
        diaAcesso.setColorDia("panelBranco");
        diaAcesso.setPresente(false);
        List<Jornada> jornadaDiaList = new ArrayList<Jornada>();
        diaAcesso.setJornadaList(jornadaDiaList);
        return diaAcesso;
    }

    private Boolean hasDiaHoraExtra(DiaComEscala diaAcesso) {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdfSemana.format(diaAcesso.getDia().getTime());
        if (diasComHorasExtraList.contains(data)) {
            return true;
        } else {
            return false;
        }

    }

    private SituacaoPonto searchSituacao(List<SituacaoPonto> situacaoPontoList, String palavra) {
        SituacaoPonto st = new SituacaoPonto();
        for (Iterator<SituacaoPonto> it = situacaoPontoList.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            if (situacaoPonto.getSituacao().equals(palavra)) {
                situacaoPonto.setSituacao("Descartado");
                st = situacaoPonto;
            }
        }
        return st;
    }

    private void setHoraExtraVazia() {
        List<String> nomeTiposHoraExtra = new ArrayList<String>();
        for (Iterator<TipoHoraExtra> it = tipoHoraExtra.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra1 = it.next();
            nomeTiposHoraExtra.add(tipoHoraExtra1.getNome());
        }

        for (Iterator<String> it = nomeTiposHoraExtra.iterator(); it.hasNext();) {
            String nomeTipo = it.next();
            if (tipo_valorHorasExtra.get(nomeTipo) == null) {
                tipo_valorHorasExtra.put(nomeTipo, 0);
            }
        }
    }

    private List<RegistroAdicionado> abonarPonto(DiaComEscala dia, List<Abono> abonoList, List<Jornada> jornadaList) {

        List<RegistroAdicionado> registroAdicionadoList = new ArrayList<RegistroAdicionado>();
        GregorianCalendar diaCorrente = new GregorianCalendar();
        diaCorrente.setTimeInMillis(dia.getDia().getTime());
        Integer diaAnoCorrente = diaCorrente.get(Calendar.DAY_OF_YEAR) + diaCorrente.get(Calendar.YEAR) * 365;

        SimpleDateFormat sdfHoraSegundos = new SimpleDateFormat("HH:mm:ss");

        for (Iterator<Abono> it = abonoList.iterator(); it.hasNext();) {
            Abono abono = it.next();
            if (diaAnoCorrente.equals(abono.getDiaAbono())) {
                for (int i = 0; i
                        < jornadaList.size(); i++) {
                    RegistroAdicionado registroAdicionado = new RegistroAdicionado();

                    if (jornadaList.get(i).getInicioJornada() != null
                            && compararHoras(abono.getDataInicioAbono(), abono.getDataFimAbono(),
                                    sdfHoraSegundos.format(jornadaList.get(i).getInicioJornada()))) {
                        if (i == 0) {
                            Timestamp dataAbono = new Timestamp(0);
                            dataAbono = dataAbono(abono.getDataInicioAbono(), jornadaList.get(i).getInicioJornada());
                            registroAdicionado = new RegistroAdicionado();
                            registroAdicionado.setCheckTime(dataAbono);
                            registroAdicionado.setCategoriaJustificativa(abono.getCategoriaJustificativa());
                            registroAdicionado.setJustificativa(abono.getJustificativa());
                            registroAdicionado.setIsTotal(true);
                            registroAdicionadoList.add(registroAdicionado);
                        }
                        if (dia.getTemIntervalo()) {
                            if (i == 1) {
                                Timestamp dataAbono = new Timestamp(0);
                                dataAbono = dataAbono(abono.getDataInicioAbono(), jornadaList.get(i).getInicioJornada());
                                registroAdicionado = new RegistroAdicionado();
                                registroAdicionado.setCheckTime(dataAbono);
                                registroAdicionado.setCategoriaJustificativa(abono.getCategoriaJustificativa());
                                registroAdicionado.setJustificativa(abono.getJustificativa());
                                registroAdicionado.setIsTotal(true);
                                registroAdicionadoList.add(registroAdicionado);
                            }
                        }
                    }
                    if (jornadaList.get(i).getTerminioJornada() != null
                            && compararHoras(abono.getDataInicioAbono(), abono.getDataFimAbono(),
                                    sdfHoraSegundos.format(jornadaList.get(i).getTerminioJornada()))) {
                        if (i == 0) {
                            Timestamp dataAbono = new Timestamp(0);
                            dataAbono = dataAbono(abono.getDataInicioAbono(), jornadaList.get(i).getTerminioJornada());
                            registroAdicionado = new RegistroAdicionado();
                            registroAdicionado.setCheckTime(dataAbono);
                            registroAdicionado.setCategoriaJustificativa(abono.getCategoriaJustificativa());
                            registroAdicionado.setJustificativa(abono.getJustificativa());
                            registroAdicionado.setIsTotal(true);
                            registroAdicionadoList.add(registroAdicionado);
                        }
                        if (dia.getTemIntervalo()) {
                            if (i == 1) {
                                Timestamp dataAbono = new Timestamp(0);
                                dataAbono = dataAbono(abono.getDataInicioAbono(), jornadaList.get(i).getTerminioJornada());
                                registroAdicionado = new RegistroAdicionado();
                                registroAdicionado.setCheckTime(dataAbono);
                                registroAdicionado.setCategoriaJustificativa(abono.getCategoriaJustificativa());
                                registroAdicionado.setJustificativa(abono.getJustificativa());
                                registroAdicionado.setIsTotal(true);
                                registroAdicionadoList.add(registroAdicionado);
                            }
                        }
                    }
                }
            }
        }
        return registroAdicionadoList;
    }

    private HashMap<Integer, List<Ponto>> separarPontosPorDia(List<Ponto> pontos) {

        HashMap<Integer, List<Ponto>> pontoPorDiaHashMap = new HashMap<Integer, List<Ponto>>();

        for (Iterator<Ponto> it = pontos.iterator(); it.hasNext();) {

            Ponto ponto_ = it.next();
            Timestamp ponto = ponto_.getPonto();

            Long diaGroup = ponto.getTime();
            GregorianCalendar dia = new GregorianCalendar();
            dia.setTimeInMillis(diaGroup);
            Integer diaAno = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

            List<Ponto> pontosDoDia = new ArrayList<Ponto>();
            pontosDoDia = pontoPorDiaHashMap.get(diaAno);

            if (pontosDoDia != null) {
                pontosDoDia.add(ponto_);
                pontoPorDiaHashMap.put(diaAno, pontosDoDia);

            } else {
                pontosDoDia = new ArrayList<Ponto>();
                pontosDoDia.add(ponto_);
                pontoPorDiaHashMap.put(diaAno, pontosDoDia);
            }
        }
        return pontoPorDiaHashMap;
    }

    private HashMap<Date, List<Ponto>> separarPontosPorDia2(List<Ponto> pontos) {

        HashMap<Date, List<Ponto>> pontoPorDiaHashMap = new HashMap<Date, List<Ponto>>();

        for (Iterator<Ponto> it = pontos.iterator(); it.hasNext();) {

            Ponto ponto_ = it.next();

            Date data = zeraData(ponto_.getPonto());

            List<Ponto> pontosDoDia = new ArrayList<Ponto>();
            pontosDoDia = pontoPorDiaHashMap.get(data);

            if (pontosDoDia != null) {
                pontosDoDia.add(ponto_);
                pontoPorDiaHashMap.put(data, pontosDoDia);

            } else {
                pontosDoDia = new ArrayList<Ponto>();
                pontosDoDia.add(ponto_);
                pontoPorDiaHashMap.put(data, pontosDoDia);
            }
        }
        return pontoPorDiaHashMap;
    }

    private HashMap<Integer, String> mapearDiaEraDataStr(Date dataInicio, Date dataFim) {

        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        HashMap<Integer, String> mapaDiaEraDataStr = new HashMap<>();

        GregorianCalendar diaHorainicio = new GregorianCalendar();
        diaHorainicio.setTime(dataInicio);
        int diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;

        GregorianCalendar diaHoraFim = new GregorianCalendar();
        diaHoraFim.setTime(dataFim);

        int diaFim = diaHoraFim.get(Calendar.DAY_OF_YEAR) + diaHoraFim.get(Calendar.YEAR) * 365;

        do {
            String dataStr = sdfHora.format(diaHorainicio.getTime());
            mapaDiaEraDataStr.put(diaInicio, dataStr);
            diaHorainicio.add(Calendar.DAY_OF_MONTH, 1);
            diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;
        } while (diaInicio != diaFim + 2);

        return mapaDiaEraDataStr;
    }

   /* private boolean containsIntervaloAbono(Integer dia) {
        for (Iterator<Abono> it = abonoList.iterator(); it.hasNext();) {
            Abono abono = it.next();
            if (dia.equals(abono.getDiaAbono())) {
                return true;
            }
        }
        return false;
    }*/

    private List<Integer> addDiasDeslocados(List<Integer> diasTrabalhoList, List<Integer> diasDeslocadosAdicionais) {

        for (Iterator<Integer> it = diasDeslocadosAdicionais.iterator(); it.hasNext();) {
            Integer diaInteger = it.next();
            if (!diasTrabalhoList.contains(diaInteger)) {
                diasTrabalhoList.add(diaInteger);
            }
        }

        Collections.sort(diasTrabalhoList);
        return diasTrabalhoList;
    }

    private List<Ponto> getRegistros(List<RegistroAdicionado> registroAdicionado) {

        List<Ponto> pontosList = new ArrayList<Ponto>();

        for (Iterator<RegistroAdicionado> it = registroAdicionado.iterator(); it.hasNext();) {
            RegistroAdicionado registroAdicionado_ = it.next();

            Ponto ponto = new Ponto();
            ponto.setPonto(registroAdicionado_.getCheckTime());
            pontosList.add(ponto);
        }
        return pontosList;
    }

    private DiaComEscala setDiaFeriado(Integer diaAno) {

        DiaComEscala diaComEscala = new DiaComEscala();
        for (Iterator<Feriado> it = feriadoList.iterator(); it.hasNext();) {
            Feriado feriado = it.next();
            if (diaAno.equals(feriado.getDiaFeriado())) {
                SimpleDateFormat sdf = new SimpleDateFormat("EE dd/MM/yyyy");
                diaComEscala.setDiaString(sdf.format(feriado.getDia().getTime()));
                diaComEscala.setColorDia("panelBranco");
                diaComEscala.setDefinicao("Feriado");
                GregorianCalendar diaHora = new GregorianCalendar();
                diaHora.setTimeInMillis(feriado.getDia().getTime());
                diaComEscala.setDia(diaHora.getTime());
                diaComEscala.setPresente(false);
                diaComEscala.setIsFeriado(true);
                diaComEscala.setJustificativa(feriado.getNome());
            }
        }
        return diaComEscala;
    }

    private Boolean containsRegistro(Long ponto, List<RegistroAdicionado> registroAdicionado) {
        Boolean constain = false;
        Timestamp registroTime = new Timestamp(ponto);

        for (Iterator<RegistroAdicionado> it = registroAdicionado.iterator(); it.hasNext();) {
            RegistroAdicionado registroAdicionado_ = it.next();
            if (registroAdicionado_.getCheckTime().equals(registroTime)) {
                constain = true;
            }
        }
        return constain;
    }

    private boolean isDataInicioMaiorQueDataFim() {
        return dataInicio.getTime() > dataFim.getTime();
    }

    private boolean isEntradaValida() {
        return (cod_funcionario != null && dataInicio != null) && (dataFim != null) && !cod_funcionario.equals(0) && !cod_funcionario.equals(-1);
    }

    private void addHorasTrabalhadas(DiaComEscala dia) {
        horasTrabalhadasDiaList.add(dia.getHorasTrabalhadas());
    }

    private void addHorasASeremTrabalhadas(DiaComEscala dia) {
        Banco b = new Banco();
        List<Jornada> jornadaDiaList = new ArrayList<Jornada>();
        //dia.getJornadaList trás somente os dias trabalhados
        if (dia.getJornadaList() == null) {
            Date date = new Date(dia.getDia().getTime());
            jornadaDiaList = getJornada(date);
        } else {
            jornadaDiaList = dia.getJornadaList();
        }
        Long milisegundosAseremTrabalhados = calcularHorasASeremTrabalhadasPorDia(jornadaDiaList);
        GregorianCalendar diaHoraExtra = new GregorianCalendar();
        diaHoraExtra.setTime(dia.getDia());
        //Integer diaInicio = diaHoraExtra.get(Calendar.DAY_OF_YEAR) + diaHoraExtra.get(Calendar.YEAR) * 365;
        /*System.out.println("-------");
         System.out.println("Dia: " + dia.getDia());
         //System.out.println("horas: " + transformaEmHoraLong(Math.abs(milisegundosAseremTrabalhados)));
         for (Iterator<Jornada> it = jornadaDiaList.iterator(); it.hasNext();) {
         Jornada jorn = it.next();
         String ini = "sem inicio";
         String fim = "sem fim";
         if (jorn.getInicioJornada() != null) {
         ini = jorn.getInicioJornada().toString();
         }
         if (jorn.getTerminioJornada() != null) {
         fim = jorn.getTerminioJornada().toString();
         }
         System.out.println("Jornada: " + ini + " , " + fim);
         System.out.println("overtime?: " + jorn.getIsOvertime());
         }*/

        Boolean addDia = false;
        for (Iterator<Jornada> it = jornadaDiaList.iterator(); it.hasNext();) {
            Jornada jorn = it.next();
            if (jorn.getInicioJornada() != null) {
                if (!jorn.getIsOvertime()) {
                    addDia = true;
                }
            }
        }
        //if (addHoras) {
        horasaSeremTrabalhadasDiaList.add(transformaEmHoraLong(Math.abs(milisegundosAseremTrabalhados)));
        if (addDia) {
            contDiasATrabalhar++;
        }
        // }
        /*if (!diasDeslocadoshoraExtra.contains(diaInicio)) {
         System.out.println("if");
         if ((b.consultaExtraAntesDeDeslocamento(cod_funcionario, dia.getDia())) || (b.consultaExtraDepoisDeDeslocamento(cod_funcionario, dia.getDia()))) {
         } else {
         horasaSeremTrabalhadasDiaList.add(transformaEmHoraLong(Math.abs(milisegundosAseremTrabalhados)));
         if (!jornadaDiaList.get(0).getSoSaida() || (jornadaDiaList.size() > 1)) {
         contDiasATrabalhar++;
         }
         }
         } else {
         System.out.println("else");
         Long horasContratadas = b.consultaDeslocadosHoraContratada(cod_funcionario, dia.getDia());
         if (horasContratadas > 0) {
         horasaSeremTrabalhadasDiaList.add(transformaEmHoraLong(Math.abs(horasContratadas)));
         if (!jornadaDiaList.get(0).getSoSaida() || (jornadaDiaList.size() > 1)) {
         contDiasATrabalhar++;
         }
         }
         }*/
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
            System.out.println("ConsultaPonto: getPrimeiroDiaMes: " + ex);
            //Logger.getLogger(ConsultaFrequenciaComEscalaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    private static Date getHoje() {
        return new Date();
    }

    private Long calcularHorasASeremTrabalhadasPorDia(List<Jornada> jornadaDiaList) {
        Long milisegundosAseremTrabalhados = 0l;
        for (int s = 0; s < jornadaDiaList.size(); s++) {
            if (!jornadaDiaList.get(s).getIsOvertime()) {
                long time = 0;
                if (jornadaDiaList.get(s).getTemVirada()) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date fimJornadaMeiaNoite = null;
                    java.util.Date inicioJornadaMeiaNoite = null;

                    try {
                        String aux = "";
                        if (jornadaDiaList.get(s).getInicioJornada() != null) {
                            aux = sdf.format(jornadaDiaList.get(s).getInicioJornada());
                        } else if (jornadaDiaList.get(s).getTerminioJornada() != null) {
                            aux = sdf.format(jornadaDiaList.get(s).getTerminioJornada());
                        } else {
                            aux = "30/12/1899";
                        }

                        fimJornadaMeiaNoite = df.parse(aux + " 24:00");
                        inicioJornadaMeiaNoite = df.parse(aux + " 00:00");
                        if (jornadaDiaList.get(s).getInicioJornada() != null) {
                            //inicioJornadaMeiaNoite =df.parse(jornadaDiaList.get(s).getInicioJornada());
                            inicioJornadaMeiaNoite = jornadaDiaList.get(s).getInicioJornada();
                        }
                        if (jornadaDiaList.get(s).getTerminioJornada() != null) {
                            //fimJornadaMeiaNoite = df.parse(jornadaDiaList.get(s).getTerminioJornada().toString());
                            fimJornadaMeiaNoite = jornadaDiaList.get(s).getTerminioJornada();
                        }
                    } catch (ParseException ex) {
                        System.out.println("ConsultaPonto: calcularHorasASeremTrabalhadasPorDia: " + ex);
                        //Logger.getLogger(DetalheDiaBean.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    /*if (jornadaDiaList.get(s).getSoEntrada()) {
                     time = Math.abs(fimJornadaMeiaNoite.getTime() - jornadaDiaList.get(s).getInicioJornada().getTime());
                     } else if (jornadaDiaList.get(s).getSoSaida()) {
                     time = Math.abs(inicioJornadaMeiaNoite.getTime() - jornadaDiaList.get(s).getTerminioJornada().getTime());
                     } else {
                     if (s == 0) {
                     time = Math.abs(fimJornadaMeiaNoite.getTime() - jornadaDiaList.get(s).getInicioJornada().getTime());
                     } else {
                     time = Math.abs(inicioJornadaMeiaNoite.getTime() - jornadaDiaList.get(s).getTerminioJornada().getTime());
                     }
                     }*/
                    if (jornadaDiaList.get(s).getSoEntrada()) {
                        time = Math.abs(fimJornadaMeiaNoite.getTime() - jornadaDiaList.get(s).getInicioJornada().getTime());
                    } else {
                        time = Math.abs(jornadaDiaList.get(s).getTerminioJornada().getTime() - inicioJornadaMeiaNoite.getTime());
                    }
                } else {
                    if (jornadaDiaList.get(s).getTerminioJornada() != null && jornadaDiaList.get(s).getInicioJornada() != null) {
                        time = Math.abs(jornadaDiaList.get(s).getTerminioJornada().getTime() - jornadaDiaList.get(s).getInicioJornada().getTime());
                    }
                }

                //Reduz da soma o tempo esperado de descanso caso o mesmo deva ser deduzido
                if (jornadaDiaList.get(s).getDeduzirDescanco1() != null && jornadaDiaList.get(s).getDeduzirDescanco1()) {
                    time -= Math.abs(jornadaDiaList.get(s).getFimDescanso1().getTime() - jornadaDiaList.get(s).getInicioDescanso1().getTime());
                }
                if (jornadaDiaList.get(s).getDeduzirIntrajornada() != null && jornadaDiaList.get(s).getDeduzirIntrajornada()) {
                    time -= Math.abs(jornadaDiaList.get(s).getFimIntrajornada().getTime() - jornadaDiaList.get(s).getInicioIntrajornada().getTime());
                }
                if (jornadaDiaList.get(s).getDeduzirDescanco2() != null && jornadaDiaList.get(s).getDeduzirDescanco2()) {
                    time -= Math.abs(jornadaDiaList.get(s).getFimDescanso2().getTime() - jornadaDiaList.get(s).getInicioDescanso2().getTime());
                }
                milisegundosAseremTrabalhados += time;
            }
        }
        return milisegundosAseremTrabalhados;
    }

    private String calcularHorasTrabalhadas(List<String> horasList) {

        Integer minutosAcumulados = 0;
        for (Iterator<String> it = horasList.iterator(); it.hasNext();) {

            String hora = it.next();
            String[] horaArray = hora.replace("h", "").replace("m", "").split(":");

            Integer parteHora1 = Integer.parseInt(horaArray[0]) * 60;
            Integer parteMunitos1 = (Integer.parseInt(horaArray[1]));
            Integer hora1Float = parteHora1 + parteMunitos1;
            minutosAcumulados += hora1Float;
        }
        Integer horas = minutosAcumulados / 60;
        Integer minutos = minutosAcumulados % 60;
        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }
        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + "h:" + minutosSrt + "m";
    }

    private List<Date> getPeriodoPreContrato() {
        List<Date> datasList = new ArrayList<Date>();
        if (cod_funcionario != -1) {
            Banco b = new Banco();
            List<Date> inicioEFimList = b.consultaInicioEFimContrato(cod_funcionario);

            if (inicioEFimList.size() == 2) {
                Date dataInicioContrato = inicioEFimList.get(0);
                if ((dataInicio.getTime() <= dataInicioContrato.getTime())) {
                    datasList.add(new Date(dataInicio.getTime()));
                    Date inicioDoContrato;
                    if (dataFim.getTime() < dataInicioContrato.getTime()) {
                        inicioDoContrato = dataFim;
                    } else {
                        inicioDoContrato = somarDias(dataInicioContrato, -1);
                    }

                    datasList.add(new Date(inicioDoContrato.getTime()));
                    return datasList;
                }
            }
        }
        return null;
    }

    private List<Date> getPeriodoPosContrato() {
        List<Date> datasList = new ArrayList<Date>();
        if (cod_funcionario != -1) {
            Banco b = new Banco();
            List<Date> inicioEFimList = new ArrayList<Date>();
            inicioEFimList = b.consultaInicioEFimContrato(cod_funcionario);

            if (inicioEFimList.size() == 2) {
                Date dataFimContrato = inicioEFimList.get(1);
                if ((dataFim.getTime() > dataFimContrato.getTime())) {
                    if (dataInicio.getTime() < dataFimContrato.getTime()) {
                        Date fimDoContrato = somarDias(dataFimContrato, 1);
                        datasList.add(new Date(fimDoContrato.getTime()));
                        datasList.add(new Date(dataFim.getTime()));
                        return datasList;
                    } else {
                        datasList.add(new Date(dataInicio.getTime()));
                        datasList.add(new Date(dataFim.getTime()));
                        return datasList;
                    }
                }
            }
        }
        return null;
    }

    private String[] calcHorasTrabalhadas(List<DiaComEscala> diasList) {

        Integer minutosAcumuladosTotal = 0;
        Integer minutosAcumuladosSemFalta = 0;

        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            //for (Iterator<Jornada> it2 = diaComEscala.getJornadaList().iterator(); it2.hasNext();) {
            //    Jornada j = it2.next();
            //    System.out.println(j.getInicioJornadaL());
            //    System.out.println(j.getTerminioJornadaL());
            //}
            if (!diaComEscala.getJustificativa().equals("FALTA")) {
                minutosAcumuladosSemFalta += diaComEscala.getMinutosTrabalhados();
            }
            minutosAcumuladosTotal += diaComEscala.getMinutosTrabalhados();
        }

        String[] saidas = new String[2];
        saidas[0] = minutosIntToHoras(minutosAcumuladosTotal);
        saidas[1] = minutosIntToHoras(minutosAcumuladosSemFalta);
        return saidas;
    }

    private void calcAtrasos(List<DiaComEscala> diasList) {
        totalQntAtraso16_59 = 0;
        totalQntAtrasoMaiorUmaHora = 0;
        listagemAtraso = "";
        listagemFalta = "";
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            totalQntAtraso16_59 += diaComEscala.getQntAtraso16_59();
            totalQntAtrasoMaiorUmaHora += diaComEscala.getQntAtrasoMaiorUmaHora();
            if (diaComEscala.getAtrasoDiaStr() != null) {
                listagemAtraso += diaComEscala.getAtrasoDiaStr() + "\n";
            }
            if (diaComEscala.getFaltaDiaStr() != null) {
                listagemFalta += diaComEscala.getFaltaDiaStr() + "\n";
            }
        }
    }

    private String minutosIntToHoras(Integer minutos_) {
        Integer horas = minutos_ / 60;
        Integer minutos = minutos_ % 60;
        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10 && horas > -10) {
            horaSrt = "0" + Math.abs(horas);
        } else {
            Integer horasInteger = Math.abs(horas);
            horaSrt = horasInteger.toString();
        }

        if (minutos < 10 && minutos > -10) {
            minutosSrt = "0" + Math.abs(minutos);
        } else {
            Integer minutosInteger = Math.abs(minutos);
            minutosSrt = minutosInteger.toString();
        }
        String out;

        if (minutos_ < 0) {
            out = " - " + horaSrt + "h:" + minutosSrt + "m";
        } else {
            out = horaSrt + "h:" + minutosSrt + "m";
        }
        return out;
    }

    private Date somarDias(Date data, Integer qnt) {
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(data);
        g.add(Calendar.DAY_OF_MONTH, qnt);
        Date dateAux = g.getTime();
        return dateAux;
    }

    private List<Jornada> getJornada(Date date) {
        List<Jornada> jornadaDiaList = new ArrayList<Jornada>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = sdf.format(date.getTime());
        boolean semHorarioNormal = false;

        GregorianCalendar diaCalendar = new GregorianCalendar();
        int tmpDiaInicio = 0;
        int tmpDiaFim = 0;
        int chaveData = 0;
        List<Integer> indexList = new ArrayList<Integer>();
        diaCalendar.setTime(dataInicio);
        tmpDiaInicio = diaCalendar.get(Calendar.DAY_OF_YEAR) + diaCalendar.get(Calendar.YEAR) * 365;
        diaCalendar.setTime(dataFim);
        tmpDiaFim = diaCalendar.get(Calendar.DAY_OF_YEAR) + diaCalendar.get(Calendar.YEAR) * 365;
        diaCalendar.setTime(date);
        chaveData = diaCalendar.get(Calendar.DAY_OF_YEAR) + diaCalendar.get(Calendar.YEAR) * 365;

        List<Jornada> deslocamentos = diasDeslocadosHashMap.get(dateStr);
        if (deslocamentos != null) {
            for (int x = 0; x < deslocamentos.size(); x++) {
                if (deslocamentos.get(x).getSchClassID() == -1) {
                    semHorarioNormal = true;
                    break;
                }
            }
        }

        if (!semHorarioNormal) {
            jornadaDiaList = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
            if (jornadaDiaList.size() == 1 && jornadaDiaList.get(0).getTemVirada() && jornadaDiaList.get(0).getSoSaida()) {
                GregorianCalendar d = new GregorianCalendar();
                d.setTime(diaCalendar.getTime());
                d.add(Calendar.DAY_OF_MONTH, -1);
                List<Jornada> tmpDeslocamentos = diasDeslocadosHashMap.get(sdf.format(d.getTime()));
                if (tmpDeslocamentos != null) {
                    for (int x = 0; x < tmpDeslocamentos.size(); x++) {
                        if (tmpDeslocamentos.get(x).getSchClassID() == -1) {
                            jornadaDiaList.remove(0);
                            break;
                        }
                    }
                }
            }
        }

        if (diasDeslocadosHashMap.get(dateStr) != null) {

            jornadaDiaList.addAll(deslocamentos);

            for (int x = 0; x < jornadaDiaList.size(); x++) {
                /*if (jornadaDiaList.get(x).getSchClassID() == -1) {
                 jornadaDiaList.remove(x);
                 semHorarioNormal = true;
                 continue;
                 }
                 jornadaDiaList.get(x).setIsDeslocamento(true);

                 if (!semHorarioNormal) {

                 if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoEntrada()) {
                 List<Jornada> jorn = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
                 for (int f = 0; f < jorn.size(); f++) {
                 if (jorn.get(f).getSoEntrada()) {
                 jorn.remove(f);
                 }
                 }
                 jornadaDiaList.addAll(jorn);
                 }
                 if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoSaida()) {
                 List<Jornada> jorn = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
                 for (int f = 0; f < jorn.size(); f++) {
                 if (jorn.get(f).getSoSaida()) {
                 jorn.remove(f);
                 }
                 }
                 jornadaDiaList.addAll(jorn);
                 }
                 }*/

                if (tmpDiaInicio == chaveData) {
                    if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoSaida()) {
                        indexList.add(x);
                    }
                }

                if (tmpDiaFim == chaveData) {
                    if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoEntrada()) {
                        indexList.add(x);
                    }
                }
            }

        }
        /*
         if (diasDeslocadosHashMap.get(dateStr) != null) {
         jornadaDiaList = diasDeslocadosHashMap.get(dateStr);
         for (int x = 0; x < jornadaDiaList.size(); x++) {
         if (jornadaDiaList.get(x).getSchClassID() == -1) {
         jornadaDiaList.remove(x);
         semHorarioNormal = true;
         continue;
         }
         jornadaDiaList.get(x).setIsDeslocamento(true);

         if (!semHorarioNormal) {

         if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoEntrada()) {
         List<Jornada> jorn = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
         for (int f = 0; f < jorn.size(); f++) {
         if (jorn.get(f).getSoEntrada()) {
         jorn.remove(f);
         }
         }
         jornadaDiaList.addAll(jorn);
         }
         if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoSaida()) {
         List<Jornada> jorn = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
         for (int f = 0; f < jorn.size(); f++) {
         if (jorn.get(f).getSoSaida()) {
         jorn.remove(f);
         }
         }
         jornadaDiaList.addAll(jorn);
         }
         }

         if (tmpDiaInicio == chaveData) {
         if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoSaida()) {
         indexList.add(x);
         }
         }

         if (tmpDiaFim == chaveData) {
         if (jornadaDiaList.get(x).getTemVirada() && jornadaDiaList.get(x).getSoEntrada()) {
         indexList.add(x);
         }
         }
         }

         } else {
         jornadaDiaList = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
         }*/

        for (int x = 0; x < indexList.size(); x++) {
            jornadaDiaList.remove(indexList.get(x));
        }

        /*if ((jornadaDiaList.size() == 1) && (jornadaDiaList.get(0).getTemVirada() && jornadaDiaList.get(0).getSoSaida()) ){
         List<Jornada> jornList = Metodos.getJornadaDia(jornadaList, date, sdiasHashMap);
         jornadaDiaList.add(jornList.get(0));
         }*/
        if (date.toString().equals(dataInicio.toString())) {
            if (jornadaDiaList.size() >= 2) {
                if (jornadaDiaList.get(0).getTemVirada()) {
                    if (!jornadaDiaList.get(0).getSoEntrada()) {
                        jornadaDiaList.set(0, jornadaDiaList.get(1));
                        jornadaDiaList.remove(1);
                    }
                }
            }
        }
        if (dataFim.before(date)) {
            if (jornadaDiaList.size() >= 2) {
                if (jornadaDiaList.get(0).getTemVirada()) {
                    if (!jornadaDiaList.get(0).getSoEntrada()) {
                        jornadaDiaList.remove(1);
                    }
                }
            }
        }

        Collections.sort(jornadaDiaList, new Comparator<Jornada>() {
            public int compare(Jornada o1, Jornada o2) {
                long t1;
                long t2;

                if (o1.getSoSaida()) {
                    t1 = o1.getTerminioJornada().getTime();
                } else {
                    if (o1.getInicioJornada() != null) {
                        t1 = o1.getInicioJornada().getTime();
                    } else {
                        t1 = 0;
                    }
                }

                if (o2.getSoSaida()) {
                    t2 = o2.getTerminioJornada().getTime();
                } else {
                    if (o2.getInicioJornada() != null) {
                        t2 = o2.getInicioJornada().getTime();
                    } else {
                        t2 = 0;
                    }
                }

                if (t1 < t2) {
                    return -1;
                } else {
                    if (t1 > t2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        return jornadaDiaList;
    }

    static boolean compararHoras(Date checkInLimite, Date checkOutLimite, String jornada_hora) {

        if (jornada_hora == null) {
            return false;
        }

        long checkInLimiteTstamp = checkInLimite.getTime();

        long checkOutLimiteTstamp = checkOutLimite.getTime();

        GregorianCalendar checkInLimiteCalendar = new GregorianCalendar();
        GregorianCalendar checkOutLimiteCalendar = new GregorianCalendar();
        checkInLimiteCalendar.setTimeInMillis(checkInLimiteTstamp);
        checkOutLimiteCalendar.setTimeInMillis(checkOutLimiteTstamp);

        int horaIn = checkInLimiteCalendar.get(Calendar.HOUR_OF_DAY);
        int minutosIn = checkInLimiteCalendar.get(Calendar.MINUTE);
        int segundosIn = checkInLimiteCalendar.get(Calendar.SECOND);

        String checkInLimiteSrt = horaIn + ":" + minutosIn + ":" + segundosIn;

        int horaOut = checkOutLimiteCalendar.get(Calendar.HOUR_OF_DAY);
        int minutosOut = checkOutLimiteCalendar.get(Calendar.MINUTE);
        int segundosOut = checkOutLimiteCalendar.get(Calendar.SECOND);

        String checkOutLimiteSrt = horaOut + ":" + minutosOut + ":" + segundosOut;

        Time timeIn = Time.valueOf(checkInLimiteSrt);
        Time timeOut = Time.valueOf(checkOutLimiteSrt);
        Time timeHora = Time.valueOf(jornada_hora);

        if ((timeIn.getTime() <= timeHora.getTime()) && (timeOut.getTime() >= timeHora.getTime())) {
            return true;
        }
        return false;
    }

    private Date dateStrToDate(String dateStr) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dt = null;
        try {
            dt = df.parse(dateStr);
        } catch (ParseException ex) {
        }
        return dt;
    }

    private Timestamp dataAbono(Date dataEntrada, Timestamp hora) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_ = sdf.format(dataEntrada.getTime());
        Date date = new Date();
        try {
            date = sdf.parse(data_);
        } catch (ParseException ex) {
        }

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String horaStr = sdfHora.format(new Date(hora.getTime()));
        Long saida = null;
        try {
            Time time = Time.valueOf(horaStr);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }

        return new Timestamp(date.getTime() + saida);
    }

    private RegistroAdicionado findRegistro(Long ponto, List<RegistroAdicionado> registroAdicionado) {

        Timestamp registroTime = new Timestamp(ponto);

        for (Iterator<RegistroAdicionado> it = registroAdicionado.iterator(); it.hasNext();) {
            RegistroAdicionado registroAdicionado_ = it.next();

            if (registroAdicionado_.getCheckTime().equals(registroTime)) {
                return registroAdicionado_;
            }
        }
        return null;
    }

    public void imprimir() throws JRException, Exception {
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (!diasList.isEmpty()) {
            List<Relatorio> relatorioList = new ArrayList<Relatorio>();
            for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
                DiaComEscala dia = it.next();
                String data = dia.getDiaString();
                String definicao = dia.getDefinicao();

                String entrada1 = dia.getEntrada_1().getRegistro();
                String saida1 = dia.getSaida_1().getRegistro();
                String entrada2 = dia.getEntrada_2().getRegistro();
                String saida2 = dia.getSaida_2().getRegistro();
                String horasTrabalhadas = dia.getHorasTrabalhadas();
                String saldo = dia.getSaldoHoras();
                String observacao = dia.getJustificativa();
                Relatorio relatorio = new Relatorio(data, definicao, entrada1, saida1, entrada2, saida2,
                        horasTrabalhadas, saldo, observacao);
                relatorioList.add(relatorio);
            }
            Banco banco = new Banco();
            banco.insertRelatorio(relatorioList);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            banco = new Banco();
            List<String> userInfoList = new ArrayList<String>();
            userInfoList = banco.consultaInfoUsuario(cod_funcionario);

            //<log>
            String nomelog = usuarioBean.getUsuario().getNome();
            String cpf_funcionario = usuarioBean.getUsuario().getSsn();
            //String msg = "Impressão - frequencia com escala. Requerente - "+cpf_funcionario+" "+nomelog+". Requerido - "+userInfoList.get(2)+" "+userInfoList.get(1)+".";
            Metodos.setLogPrint(cpf_funcionario, nomelog, userInfoList.get(2), userInfoList.get(1));
            //</log>

            Impressao.geraRelatorio(sdf.format(dataInicio.getTime()), sdf.format(dataFim.getTime()),
                    Integer.parseInt(userInfoList.get(0)), userInfoList.get(1), userInfoList.get(2),
                    userInfoList.get(3), userInfoList.get(4), userInfoList.get(5), horasASeremTrabalhadasTotal, horasTotal,
                    saldo2/*saldoTotal*/, contDiasATrabalhar.toString(), contDiasTrabalhados.toString(),
                    getPrimeiroNome(usuarioBean.getUsuario().getNome()), faltas, adicionalNoturnoStr, horaExtra, gratificacaoStr);
        } else {
            FacesMessage msgErro = new FacesMessage("Não há informações a serem impressas!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void imprimirRelatorio() {
        if (isEntradaValida()) {
            Banco banco = new Banco();
            int tipoRelatorio = banco.consultaTipoRelatorio();
            switch (tipoRelatorio) {
                case 1:
                    geraRelatorio();
                    break;
                case 2:
                    geraRelatorioSemSaldo();
                    break;
                case 3:
                    geraRelatorioPortaria1510();
                    break;
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Entrada de dados inválida!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public boolean geraRelatorio() {

        RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioPortaria1510>();
        List<HorarioContratual> horarioContratualList = new ArrayList();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            relatorioPortaria = new RelatorioPortaria1510();
            //Setando o dia
            Date data = diaComEscala.getDia();
            String dia = getDia(data);
            relatorioPortaria.setDia(dia);
            relatorioPortaria.setData(data);
            if (diaComEscala.getIsFeriado() || diaComEscala.getIsAfastado()) {
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList.add(new PontoIrreal("", "", diaComEscala.getJustificativa()));
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            } else {
                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);

                relatorioPortaria.setHorasDias(diaComEscala.getHorasTrabalhadas());
                relatorioPortaria.setSaldo(diaComEscala.getSaldoHoras());
                //Verificando tipos de horários
                HorarioContratual horarioContratual = new HorarioContratual();
                horarioContratual = getHorarioContratual(diaComEscala);
                Boolean contemHorario = containsHorarioContratual(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod == null ? "" : cod.toString());
                //Setando lista de pontos irreais
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList = getPontoIrreais(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                if (!diaComEscala.getPresente()) {
                    pontoIrrealList.add(new PontoIrreal("", "", diaComEscala.getJustificativa()));
                }
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        List<RelatorioPortaria1510> relatorioPortariaList_ = insertDiasSemEscalaEComRegistros();
        relatorioPortariaList.addAll(relatorioPortariaList_);
        ordenarRelatorioPortaria1510(relatorioPortariaList);

        if (relatorioPortariaList.isEmpty()) {
            FacesMessage msgErro = new FacesMessage("Não há informações a serem impressas!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return true;
        }

        Banco banco = new Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);

        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo
                = banco.relatorioPortaria1510CabecalhoResumo(cod_funcionario);

        //<log>
        String nomelog = usuarioBean.getUsuario().getNome();
        String cpf_funcionario = usuarioBean.getLogin();
        //String msg = "Impressão - frequencia com escala. Requerente - "+cpf_funcionario+" "+nomelog+". Requerido - "+userInfoList.get(2)+" "+userInfoList.get(1)+".";
        Metodos.setLogPrint(cpf_funcionario, nomelog, relatorioPortaria1510CabecalhoResumo.getCpf(), relatorioPortaria1510CabecalhoResumo.getNome());
        //</log>

        try {
            Impressao.geraRelatorioPortaria1510Resumo(relatorioPortaria1510CabecalhoResumo, dataInicio, dataFim, horasASeremTrabalhadasTotal, horasTotal,
                    saldo2/*saldoTotal*/, contDiasATrabalhar.toString(), contDiasTrabalhados.toString(),
                    getPrimeiroNome(usuarioBean.getUsuario().getNome()), faltas, adicionalNoturnoStr, horaExtra, gratificacaoStr);
        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return false;
    }

    public boolean geraRelatorioSemSaldo() {

        RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioPortaria1510>();
        List<HorarioContratual> horarioContratualList = new ArrayList();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();

            relatorioPortaria = new RelatorioPortaria1510();
            //Setando o dia
            Date data = diaComEscala.getDia();
            String dia = getDia(data);
            relatorioPortaria.setDia(dia);
            relatorioPortaria.setData(data);
            if (diaComEscala.getIsFeriado() || diaComEscala.getIsAfastado()) {
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList.add(new PontoIrreal("", "", diaComEscala.getJustificativa()));
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            } else {

                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);

                relatorioPortaria.setHorasDias(diaComEscala.getHorasTrabalhadas());
                relatorioPortaria.setSaldo(diaComEscala.getSaldoHoras());
                //Verificando tipos de horários
                HorarioContratual horarioContratual = new HorarioContratual();
                horarioContratual = getHorarioContratual(diaComEscala);
                Boolean contemHorario = containsHorarioContratual(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod.toString());
                //Setando lista de pontos irreais
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList = getPontoIrreais(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                if (!diaComEscala.getPresente()) {
                    pontoIrrealList.add(new PontoIrreal("", "", "FALTA"));
                }
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        List<RelatorioPortaria1510> relatorioPortariaList_ = insertDiasSemEscalaEComRegistros();
        relatorioPortariaList.addAll(relatorioPortariaList_);
        ordenarRelatorioPortaria1510(relatorioPortariaList);

        if (relatorioPortariaList.isEmpty()) {
            FacesMessage msgErro = new FacesMessage("Não há informações a serem impressas!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return true;
        }

        Banco banco = new Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);

        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo
                = banco.relatorioPortaria1510CabecalhoResumo(cod_funcionario);

        //<log>
        String nomelog = usuarioBean.getUsuario().getNome();
        String cpf_funcionario = usuarioBean.getLogin();
        String dataContratacao = usuarioBean.getUsuario().getDataContratacaoStr();
        //String msg = "Impressão - frequencia com escala. Requerente - "+cpf_funcionario+" "+nomelog+". Requerido - "+userInfoList.get(2)+" "+userInfoList.get(1)+".";
        Metodos.setLogPrint(cpf_funcionario, nomelog, relatorioPortaria1510CabecalhoResumo.getCpf(), relatorioPortaria1510CabecalhoResumo.getNome());
        //</log>

        try {
            Impressao.geraRelatorioPortaria1510ResumoSemSaldo(relatorioPortaria1510CabecalhoResumo, dataInicio, dataFim, dataContratacao);
        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return false;
    }

    public boolean geraRelatorioPorDepartamento() {

        RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioPortaria1510> relatorioPortariaList = new ArrayList<>();
        List<HorarioContratual> horarioContratualList = new ArrayList();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();

            relatorioPortaria = new RelatorioPortaria1510();
            //Setando o dia
            Date data = diaComEscala.getDia();
            String dia = getDia(data);
            relatorioPortaria.setDia(dia);
            relatorioPortaria.setData(data);
            if (diaComEscala.getIsFeriado() || diaComEscala.getIsAfastado()) {
                List<PontoIrreal> pontoIrrealList = new ArrayList<>();
                pontoIrrealList.add(new PontoIrreal("", "", diaComEscala.getJustificativa()));
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            } else {
                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);

                relatorioPortaria.setHorasDias(diaComEscala.getHorasTrabalhadas());
                relatorioPortaria.setSaldo(diaComEscala.getSaldoHoras());
                //Verificando tipos de horários
                HorarioContratual horarioContratual = new HorarioContratual();
                horarioContratual = getHorarioContratual(diaComEscala);
                Boolean contemHorario = containsHorarioContratual(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod.toString());
                //Setando lista de pontos irreais
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList = getPontoIrreais(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                if (!diaComEscala.getPresente()) {
                    pontoIrrealList.add(new PontoIrreal("", "", "FALTA"));
                }
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        List<RelatorioPortaria1510> relatorioPortariaList_ = insertDiasSemEscalaEComRegistros();
        relatorioPortariaList.addAll(relatorioPortariaList_);
        ordenarRelatorioPortaria1510(relatorioPortariaList);

        if (relatorioPortariaList.isEmpty()) {
            return true;
        }
        Banco banco = new Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);
        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo = banco.relatorioPortaria1510CabecalhoResumo(cod_funcionario);

        try {
            Impressao.geraRelatorioPortaria1510ResumoPorDepartamento(relatorioPortaria1510CabecalhoResumo,
                    dataInicio, dataFim, horasASeremTrabalhadasTotal, horasTotal,
                    saldo2/*saldoTotal*/, contDiasATrabalhar.toString(), contDiasTrabalhados.toString(),
                    getPrimeiroNome(usuarioBean.getUsuario().getNome()), faltas, adicionalNoturnoStr, horaExtra, gratificacaoStr);

        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return false;
    }

    public boolean geraRelatorioCatracaPorDepartamento() {

        RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioPortaria1510>();
        List<HorarioContratual> horarioContratualList = new ArrayList();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();

            relatorioPortaria = new RelatorioPortaria1510();
            //Setando o dia
            Date data = diaComEscala.getDia();
            String dia = getDia(data);
            relatorioPortaria.setDia(dia);
            relatorioPortaria.setData(data);
            if (diaComEscala.getIsFeriado() || diaComEscala.getIsAfastado()) {
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList.add(new PontoIrreal("", "", diaComEscala.getJustificativa()));
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            } else {
                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando Entradas e Saidas nas catracas
                CatracaBanco catBanco = new CatracaBanco();
                String CEntradas = catBanco.getEntradas(diaComEscala, cod_funcionario);
                String CSaidas = catBanco.getSaidas(diaComEscala, cod_funcionario);
                relatorioPortaria.setEntradasCatraca(CEntradas);
                relatorioPortaria.setSaidasCatraca(CSaidas);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);
                //Setando Saldos
                relatorioPortaria.setHorasDias(diaComEscala.getHorasTrabalhadas());
                relatorioPortaria.setSaldo(diaComEscala.getSaldoHoras());
                //Verificando tipos de horários
                HorarioContratual horarioContratual = new HorarioContratual();
                horarioContratual = getHorarioContratual(diaComEscala);
                Boolean contemHorario = containsHorarioContratual(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod.toString());
                //Setando lista de pontos irreais
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList = getPontoIrreais(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                if (!diaComEscala.getPresente()) {
                    pontoIrrealList.add(new PontoIrreal("", "", "FALTA"));
                }
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        List<RelatorioPortaria1510> relatorioPortariaList_ = insertDiasSemEscalaEComRegistros();
        relatorioPortariaList.addAll(relatorioPortariaList_);
        ordenarRelatorioPortaria1510(relatorioPortariaList);

        if (relatorioPortariaList.isEmpty()) {
            return true;
        }
        Banco banco = new Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);
        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo = banco.relatorioPortaria1510CabecalhoResumo(cod_funcionario);

        try {
            Impressao.geraRelatorioCatracaPorDepartamento(relatorioPortaria1510CabecalhoResumo,
                    dataInicio, dataFim, horasASeremTrabalhadasTotal, horasTotal,
                    saldo2/*saldoTotal*/, contDiasATrabalhar.toString(), contDiasTrabalhados.toString(),
                    getPrimeiroNome(usuarioBean.getUsuario().getNome()), faltas, adicionalNoturnoStr, horaExtra, gratificacaoStr);

        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return false;
    }

    public boolean geraRelatorioSemSaldoPorDepartamento() {

        RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioPortaria1510>();
        List<HorarioContratual> horarioContratualList = new ArrayList();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();

            relatorioPortaria = new RelatorioPortaria1510();
            //Setando o dia
            Date data = diaComEscala.getDia();
            String dia = getDia(data);
            relatorioPortaria.setDia(dia);
            relatorioPortaria.setData(data);
            if (diaComEscala.getIsFeriado() || diaComEscala.getIsAfastado()) {
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList.add(new PontoIrreal("", "", diaComEscala.getJustificativa()));
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            } else {
                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);

                relatorioPortaria.setHorasDias(diaComEscala.getHorasTrabalhadas());
                relatorioPortaria.setSaldo(diaComEscala.getSaldoHoras());
                //Verificando tipos de horários
                HorarioContratual horarioContratual = new HorarioContratual();
                horarioContratual = getHorarioContratual(diaComEscala);
                Boolean contemHorario = containsHorarioContratual(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod.toString());
                //Setando lista de pontos irreais
                List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
                pontoIrrealList = getPontoIrreais(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                if (!diaComEscala.getPresente()) {
                    pontoIrrealList.add(new PontoIrreal("", "", "FALTA"));
                }
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        List<RelatorioPortaria1510> relatorioPortariaList_ = insertDiasSemEscalaEComRegistros();
        relatorioPortariaList.addAll(relatorioPortariaList_);
        ordenarRelatorioPortaria1510(relatorioPortariaList);

        if (relatorioPortariaList.isEmpty()) {
            return true;
        }
        Banco banco = new Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);
        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo = banco.relatorioPortaria1510CabecalhoResumo(cod_funcionario);
        String dataContratacao = usuarioBean.getUsuario().getDataContratacaoStr();
        try {
            Impressao.geraRelatorioSemSaldoPortaria1510ResumoPorDepartamento(relatorioPortaria1510CabecalhoResumo,
                    dataInicio, dataFim, dataContratacao);
        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return false;
    }

    public void geraRelatorioPortaria1510() {

        RelatorioMensal.RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioMensal.RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioMensal.RelatorioPortaria1510>();
        List<RelatorioMensal.HorarioContratual> horarioContratualList = new ArrayList<RelatorioMensal.HorarioContratual>();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            if (!diaComEscala.getIsFeriado() && !diaComEscala.getIsAfastado()) {
                relatorioPortaria = new RelatorioMensal.RelatorioPortaria1510();
                //Setando o dia
                Date data = diaComEscala.getDia();
                String dia = getDia1510(data);
                relatorioPortaria.setDia(dia);
                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);
                //Verificando tipos de horários
                RelatorioMensal.HorarioContratual horarioContratual = new RelatorioMensal.HorarioContratual();
                horarioContratual = getHorarioContratual1510(diaComEscala);
                Boolean contemHorario = containsHorarioContratual1510(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios1510(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod.toString());
                //Setando lista de pontos irreais
                List<RelatorioMensal.PontoIrreal> pontoIrrealList = new ArrayList<RelatorioMensal.PontoIrreal>();
                pontoIrrealList = getPontoIrreais1510(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            }
        }
        RelatorioMensal.Banco banco = new RelatorioMensal.Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);
        RelatorioPortaria1510Cabecalho relatorioPortaria1510Cabecalho = banco.consultaRelatorioPortaria1510Cabecalho(cod_funcionario, 0);

        try {
            RelatorioMensal.Impressao.geraRelatorioPortaria1510(relatorioPortaria1510Cabecalho.getEmpregador(), relatorioPortaria1510Cabecalho.getEndereco(),
                    relatorioPortaria1510Cabecalho.getEmpregado(), relatorioPortaria1510Cabecalho.getAdmissao(), dataInicio, dataFim);
        } catch (JRException ex) {
        } catch (Exception ex) {
        }
    }

    public boolean geraRelatorio1510PorDepartamento() {

        RelatorioMensal.RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioMensal.RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioMensal.RelatorioPortaria1510>();
        List<RelatorioMensal.HorarioContratual> horarioContratualList = new ArrayList<RelatorioMensal.HorarioContratual>();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            if (!diaComEscala.getIsFeriado() && !diaComEscala.getIsAfastado()) {
                relatorioPortaria = new RelatorioMensal.RelatorioPortaria1510();
                //Setando o dia
                Date data = diaComEscala.getDia();
                String dia = getDia1510(data);
                relatorioPortaria.setDia(dia);
                //Setando marcações
                String marcacoes = getMarcacoes(diaComEscala);
                relatorioPortaria.setMarcacoesRegistradas(marcacoes);
                //Setando entradas e saidas
                String[] entradasESaidas = new String[6];
                entradasESaidas = getEntradaESaidas(diaComEscala);
                relatorioPortaria.setEntrada1(entradasESaidas[0]);
                relatorioPortaria.setSaida1(entradasESaidas[1]);
                relatorioPortaria.setEntrada2(entradasESaidas[2]);
                relatorioPortaria.setSaida2(entradasESaidas[3]);
                relatorioPortaria.setEntrada3(entradasESaidas[4]);
                relatorioPortaria.setSaida3(entradasESaidas[5]);
                //Verificando tipos de horários
                RelatorioMensal.HorarioContratual horarioContratual = new RelatorioMensal.HorarioContratual();
                horarioContratual = getHorarioContratual1510(diaComEscala);
                Boolean contemHorario = containsHorarioContratual1510(horarioContratual, horarioContratualList);
                if (!contemHorario) {
                    horarioContratual.setCod_horario(i.toString());
                    horarioContratualList.add(horarioContratual);
                    i++;
                }
                //Setando código horário (ch)
                Integer cod = enumerarHorarios1510(horarioContratual, horarioContratualList);
                relatorioPortaria.setCh(cod.toString());
                //Setando lista de pontos irreais
                List<RelatorioMensal.PontoIrreal> pontoIrrealList = new ArrayList<RelatorioMensal.PontoIrreal>();
                pontoIrrealList = getPontoIrreais1510(diaComEscala);
                relatorioPortaria.setPontoIrrealList(pontoIrrealList);
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        if (diasList.isEmpty()) {
            return true;
        }

        RelatorioMensal.Banco banco = new RelatorioMensal.Banco();
        banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);
        RelatorioPortaria1510Cabecalho relatorioPortaria1510Cabecalho = banco.consultaRelatorioPortaria1510Cabecalho(cod_funcionario, 0);

        try {
            RelatorioMensal.Impressao.geraRelatorioPortaria1510PorDepartamento(relatorioPortaria1510Cabecalho.getEmpregador(), relatorioPortaria1510Cabecalho.getEndereco(),
                    relatorioPortaria1510Cabecalho.getEmpregado(), relatorioPortaria1510Cabecalho.getAdmissao(), dataInicio, dataFim, cod_funcionario);
        } catch (JRException ex) {
            System.out.print(ex);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return false;
    }

    private String getDia(Date dia) {
        GregorianCalendar diaGregorian = new GregorianCalendar();
        diaGregorian.setTime(dia);
        SimpleDateFormat sdfSemana = new SimpleDateFormat("EE dd");
        return sdfSemana.format(diaGregorian.getTime());
    }

    private String getDia(String dia) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date data = null;
        try {
            data = df.parse(dia);

        } catch (ParseException ex) {
            System.out.println("ConsultaPonto: getDia: " + ex);
            //Logger.getLogger(DetalheDiaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        GregorianCalendar diaGregorian = new GregorianCalendar();
        diaGregorian.setTime(data);
        SimpleDateFormat sdfSemana = new SimpleDateFormat("EE dd");
        return sdfSemana.format(diaGregorian.getTime());
    }

    private List<RelatorioPortaria1510> insertDiasSemEscalaEComRegistros() {
        List<RelatorioPortaria1510> relatorioPortariaList_ = new ArrayList<RelatorioPortaria1510>();
        Banco banco = new Banco();
        List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, dataInicio, dataFim);
        banco.fecharConexao();
        List<Date> diasSemEscalaEComPontoList = getListaDiasComRegistroPoremForaDaEscala(pontoList);

        for (Iterator<Date> it = diasSemEscalaEComPontoList.iterator(); it.hasNext();) {
            Date data = it.next();

            RelatorioPortaria1510 relatorioPortaria1510 = new RelatorioPortaria1510();
            relatorioPortaria1510.setDia(getDia(data));
            relatorioPortaria1510.setData(data);
            relatorioPortaria1510.setMarcacoesRegistradas(getMarcacoes(zeraDataStr(data), pontoList));
            relatorioPortariaList_.add(relatorioPortaria1510);
        }
        return relatorioPortariaList_;
    }

    public List<Date> getListaDiasComRegistroPoremForaDaEscala() {
        Banco banco = new Banco();
        List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, dataInicio, dataFim);
        banco.fecharConexao();
        List<Date> diasSemEscalaEComPontoList = getListaDiasComRegistroPoremForaDaEscala(pontoList);
        return diasSemEscalaEComPontoList;
    }

    public String getListagemDiasComRegistroPoremForaDaEscala(List<Date> datas) {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("dd/MM/yyyy");
        String saida = "";
        for (Iterator<Date> it = datas.iterator(); it.hasNext();) {
            Date date = it.next();
            String dataStr = sdfSemana.format(date.getTime());
            if (it.hasNext()) {
                saida += dataStr + "\n";
            } else {
                saida += dataStr;
            }
        }
        return saida;
    }

    private String getData(Date dia) {
        GregorianCalendar diaGregorian = new GregorianCalendar();
        diaGregorian.setTime(dia);
        SimpleDateFormat sdfSemana = new SimpleDateFormat("dd/MM/yyyy");
        return sdfSemana.format(diaGregorian.getTime());
    }

    /**
     * private List<String>
     * getListaDiasComRegistroPoremForaDaEscala(List<RelatorioPortaria1510>
     * relatorioPortariaList) { List<String> datasList = new
     * ArrayList<String>(); for (Iterator<Ponto> it = pontos.iterator();
     * it.hasNext();) { Ponto ponto = it.next(); String data = getData(new
     * Date(ponto.getPonto().getTime())); //1 - Evita repeticao, 2 - Somente se
     * o dia for sem escala, 3 - o ponto tem que estar no intervale selecionado.
     * //Isso pq se pega os ponto tb do fim seguinte de datafim if
     * (!datasList.contains(data) && isDataSemEscala(data,
     * relatorioPortariaList) &&
     * verificaPontoNoIntervaloSelecionado(ponto.getPonto().getTime(),
     * dataInicio.getTime(), dataFim.getTime())) { datasList.add(data); } }
     * return datasList; }
     */
    private List<Date> getListaDiasComRegistroPoremForaDaEscala(List<Ponto> pontoList) {
        List<Date> datasList = new ArrayList<Date>();
        for (Iterator<Ponto> it = pontoList.iterator(); it.hasNext();) {
            Ponto ponto = it.next();
            Date data = zeraData(ponto.getPonto());
            //1 - Evita repeticao, 2 - Somente se o dia for sem escala, 3 - o ponto tem que estar no intervale selecionado.
            //Isso pq se pega os ponto tb do fim seguinte de datafim
            if (!datasList.contains(data) && isDataSemEscala(data)
                    && verificaPontoNoIntervaloSelecionado(ponto.getPonto().getTime(), dataInicio.getTime(), dataFim.getTime())) {
                datasList.add(data);
            }
        }
        return datasList;
    }

    private List<DiaSemEscala> getListaDiaSemEscalaPoremForaDaEscala() {
        List<DiaSemEscala> diasSemEscalaList = new ArrayList<DiaSemEscala>();
        DiaSemEscala diaSemEscala = new DiaSemEscala();
        Banco banco = new Banco();
        List<Ponto> pontoList = banco.consultaChechInOut(cod_funcionario, dataInicio, dataFim);
        banco.fecharConexao();
        HashMap<Date, List<Ponto>> pontoPorDiaHashMap = separarPontosPorDia2(pontoList);
        List<Date> dateList = getListaDiasComRegistroPoremForaDaEscala(pontoList);
        for (Iterator<Date> it = dateList.iterator(); it.hasNext();) {
            Date date = it.next();
            diaSemEscala = new DiaSemEscala();
            diaSemEscala.setData(date);
            diaSemEscala.setHorasList(convertPontosToDate(pontoPorDiaHashMap.get(date)));
            diasSemEscalaList.add(diaSemEscala);
        }
        return diasSemEscalaList;
    }

    private List<Date> convertPontosToDate(List<Ponto> pontoList) {
        List<Date> dateList = new ArrayList<Date>();
        for (Iterator<Ponto> it = pontoList.iterator(); it.hasNext();) {
            Ponto ponto = it.next();
            dateList.add(ponto.getPonto());
        }
        return dateList;
    }

    private boolean isDataSemEscala(String data, List<RelatorioPortaria1510> relatorioPortariaList) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Iterator<RelatorioPortaria1510> it = relatorioPortariaList.iterator(); it.hasNext();) {
            RelatorioPortaria1510 relatorioPortaria1510 = it.next();
            String dataStr = sdf.format(relatorioPortaria1510.getData().getTime());
            if (dataStr.equals(data)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDataSemEscala(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala_ = it.next();
            String dataStr1 = sdf.format(diaComEscala_.getDia());
            String dataStr2 = sdf.format(data);
            if (dataStr1.equals(dataStr2)) {
                return false;
            }
        }
        return true;
    }

    private String getDia1510(Date dia) {
        GregorianCalendar diaGregorian = new GregorianCalendar();
        diaGregorian.setTime(dia);
        Integer dia_ = diaGregorian.get(Calendar.DAY_OF_MONTH);
        String saida = dia_.toString();
        if (saida.length() == 1) {
            saida = "0" + saida;
        }
        return saida;
    }

    private String getMarcacoes(DiaComEscala diaComEscala) {
        List<SituacaoPonto> situacaoPontos = diaComEscala.getSituacaoPonto();
        situacaoPontos = ordenarSituacao(situacaoPontos);
        String marcacoes = new String();
        for (Iterator<SituacaoPonto> it = situacaoPontos.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            if (!(situacaoPonto.getIsAbonado() || situacaoPonto.getIsPreAssinalado())) {
                marcacoes += situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
            }
        }
        return marcacoes;
    }

    private boolean verificaPontoNoIntervaloSelecionado(long base, long inicio, long fim) {
        if (base > inicio && base < getProximoDia(new Date(fim)).getTime()) {
            return true;
        } else {
            return false;
        }
    }

    private String getMarcacoes(String data, List<Ponto> pontos) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String saida = new String();
        for (Iterator<Ponto> it = pontos.iterator(); it.hasNext();) {
            Ponto ponto = it.next();
            Date date_ = new Date(ponto.getPonto().getTime());
            if (data.equals(sdf.format(date_.getTime()))) {
                saida += sdfHora.format(date_.getTime()) + " ";
            }
        }
        return saida;
    }

    private List<RelatorioPortaria1510> ordenarRelatorioPortaria1510(List<RelatorioPortaria1510> relatorioPortaria1510List) {
        Collections.sort(relatorioPortaria1510List, new Comparator() {
            public int compare(Object o1, Object o2) {
                RelatorioPortaria1510 p1 = (RelatorioPortaria1510) o1;
                RelatorioPortaria1510 p2 = (RelatorioPortaria1510) o2;
                return p1.getData().getTime() < p2.getData().getTime()
                        ? -1 : (p1.getData().getTime() > p2.getData().getTime()
                        ? +1 : 0);
            }
        });
        return relatorioPortaria1510List;
    }

    private String[] getEntradaESaidas(DiaComEscala diaComEscala) {

        List<SituacaoPonto> situacaoPontos = diaComEscala.getSituacaoPonto();
        String[] entradaESaidaList = new String[6];

        for (Iterator<SituacaoPonto> it = situacaoPontos.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            String ponto = situacaoPonto.getSituacao();

            if (!situacaoPonto.getIsDescanso()) {
                if (ponto.equals("Entrada do 1º turno")) {
                    entradaESaidaList[0] = situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
                } else if (ponto.contains("Saida do 1º turno")) {
                    entradaESaidaList[1] = situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
                } else if (ponto.contains("Entrada do 2º turno")) {
                    entradaESaidaList[2] = situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
                } else if (ponto.contains("Saida do 2º turno")) {
                    entradaESaidaList[3] = situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
                } else if (ponto.contains("Entrada")) {
                    entradaESaidaList[0] = situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
                } else if (ponto.contains("Saida")) {
                    entradaESaidaList[1] = situacaoPonto.getHoraPonto().substring(0, situacaoPonto.getHoraPonto().length() - 3) + " ";
                }
            }
        }
        for (int i = 0; i < entradaESaidaList.length; i++) {
            if (entradaESaidaList[i] == null) {
                entradaESaidaList[i] = "";
            }
        }

        return entradaESaidaList;
    }

    private void addAdicionalNoturnoo(Integer adicionalNoturno) {
        adicionalNoturnoInt += adicionalNoturno;
        if (adicionalNoturno > 0) {
            qnt_dias_adicional_noturno++;
        }
    }

    private void addAdicionalNoturno(DiaComEscala diaComEscala) {
        Integer adicionalNoturno = diaComEscala.getAdicionalNoturno();
        adicionalNoturnoInt += adicionalNoturno;
        if (adicionalNoturnoInt > 0) {
            qnt_dias_adicional_noturno = adicionalNoturnoInt / (8 * 3600);
        }
        Banco banco = new Banco();
        Funcionario func = banco.consultaFuncionario(cod_funcionario);
        /*if (func.getNome() != null && func.getNome_regime() != null && func.getNome_regime().toUpperCase().replace(" ", "").contains("CELETISTA")) {
         adicionalNoturnoFinal = adicionalNoturnoInt - (qnt_dias_adicional_noturno * 3600);
         } else {*/
        adicionalNoturnoFinal = adicionalNoturnoInt;
        //}
        //System.out.println("adicionalFinal: "+transformaSegundosEmHora(adicionalNoturnoInt));

        /*for (Iterator<Jornada> it = diaComEscala.getJornadaList().iterator(); it.hasNext();) {
         Jornada jornada = it.next();
         if (jornada.isJornadaNoturna()) {
         if (!jornada.getSoSaida()) {
         if (adicionalNoturno > 0) {
         qnt_dias_adicional_noturno++ ;
         }
         }
         }
         }*/
    }

    private void addDiaCritico(Integer saldoDiaCritico) {
        this.saldoDiaCriticoInt += saldoDiaCritico;
    }

    private void addGratificacao(HashMap<Integer, Integer> somaGratificacao) {

        for (Iterator it = somaGratificacao.keySet().iterator(); it.hasNext();) {
            Integer cod_gratificacao = (Integer) it.next();
            Integer valor = (Integer) somaGratificacao.get(cod_gratificacao);
            if (this.somaGratificacao.get(cod_gratificacao) != null) {
                this.somaGratificacao.put(cod_gratificacao, this.somaGratificacao.get(cod_gratificacao) + valor);
            } else {
                this.somaGratificacao.put(cod_gratificacao, valor);
            }
        }
    }

    private String resumoGratificacao(Map<Integer, Integer> somaGratificacao) {
        String saidaResumoGratificacao = "";
        for (Iterator it = somaGratificacao.keySet().iterator(); it.hasNext();) {
            Integer cod_gratificacao = (Integer) it.next();
            Integer valor = (Integer) somaGratificacao.get(cod_gratificacao);
            saidaResumoGratificacao += getNomeGratificacao(cod_gratificacao) + ": " + transformaSegundosEmHora(valor) + "; ";
        }

        if (saidaResumoGratificacao.length() > 3) {
            saidaResumoGratificacao = saidaResumoGratificacao.substring(0, saidaResumoGratificacao.length() - 2);
        } else {
            saidaResumoGratificacao = " Sem gratificações";
        }
        return saidaResumoGratificacao;
    }

    private String getNomeGratificacao(Integer cod_gratificacao) {
        String nomeGratificacao = "";
        for (Iterator<Gratificacao> it = gratificacaoList.iterator(); it.hasNext();) {
            Gratificacao gratificacao = it.next();
            if (gratificacao.getCod_gratificacao().equals(cod_gratificacao)) {
                nomeGratificacao = gratificacao.getNome();
            }
        }
        return nomeGratificacao;
    }

    private Boolean isDiaTodoHoraExtra(DiaComEscala diaAcesso) {
        GregorianCalendar dia = new GregorianCalendar();
        dia.setTimeInMillis(diaAcesso.getDia().getTime());
        Integer diaAno = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;
        if (diasDeslocadoshoraExtra.contains(diaAno)) {
            return true;
        } else {
            return false;
        }
    }

    private void calcSomahoraExtra(Map<String, Integer> tipo_valorHorasExtra) {
        if (!tipo_valorHorasExtra.isEmpty()) {
            Set<String> tiposHoraExtra = tipo_valorHorasExtra.keySet();
            for (Iterator<String> iterator = tiposHoraExtra.iterator(); iterator.hasNext();) {
                String tipo = iterator.next();
                if (this.tipo_valorHorasExtra.get(tipo) != null) {
                    Integer horasExtra = tipo_valorHorasExtra.get(tipo);
                    Integer horaExtraTotal = this.tipo_valorHorasExtra.get(tipo);
                    Integer horaExtraResultado = horaExtraTotal + horasExtra;
                    this.tipo_valorHorasExtra.put(tipo, horaExtraResultado);
                } else {
                    Integer horasExtra = tipo_valorHorasExtra.get(tipo);
                    this.tipo_valorHorasExtra.put(tipo, horasExtra);
                }
            }
        }
    }

    private String gethoraExtra(Map<String, Integer> tipo_valorHorasExtra) {
        String horaExtra_ = "";
        if (tipo_valorHorasExtra.isEmpty()) {
            horaExtra_ = "00h:00m";
        }

        for (Iterator it = tipo_valorHorasExtra.keySet().iterator(); it.hasNext();) {
            String nomeTipo = (String) it.next();
            Integer minutosTrabalhados = tipo_valorHorasExtra.get(nomeTipo);

            if (it.hasNext()) {
                horaExtra_ += nomeTipo + ": " + transformaEmHoraPrint(minutosTrabalhados) + ", ";
            } else {
                horaExtra_ += nomeTipo + ": " + transformaEmHoraPrint(minutosTrabalhados);
            }
        }
        return horaExtra_;
    }

    private String transformaEmHoraPrint(Integer tempoemminutos_) {

        Long tempoemminutos = Long.parseLong(tempoemminutos_.toString());

        Long horas = tempoemminutos / 60;
        Long minutos = tempoemminutos % 60;

        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }

        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + "h:" + minutosSrt + "m";
    }

    private HorarioContratual getHorarioContratual(DiaComEscala diaComEscala) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        HorarioContratual horarioContratual = new HorarioContratual();
        List<Jornada> jornadaDiaList = diaComEscala.getJornadaList();
        if (jornadaDiaList.size() == 2) {
            if (jornadaDiaList.get(0).getSoEntrada()) {
                horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            } else if (jornadaDiaList.get(0).getSoSaida()) {
                horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            } else {
                if (jornadaDiaList.get(0).getInicioJornadaL() != null) {
                    horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
                }
                if (jornadaDiaList.get(0).getTerminioJornadaL() != null) {
                    horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
                }
            }

            if (jornadaDiaList.get(1).getSoEntrada()) {
                horarioContratual.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornadaL()));
            } else if (jornadaDiaList.get(1).getSoSaida()) {
                horarioContratual.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornadaL()));
            } else {
                if (jornadaDiaList.get(1).getInicioJornadaL() != null) {
                    horarioContratual.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornadaL()));
                }
                if (jornadaDiaList.get(1).getTerminioJornadaL() != null) {
                    horarioContratual.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornadaL()));
                }
            }
        } else {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));

            if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                horarioContratual.setEntradaD1(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()));
            }
            if (jornadaDiaList.get(0).getFimDescanso1() != null) {
                horarioContratual.setSaidaD1(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()));
            }
            if (jornadaDiaList.get(0).getInicioDescanso2() != null) {
                horarioContratual.setEntradaD2(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()));
            }
            if (jornadaDiaList.get(0).getFimDescanso2() != null) {
                horarioContratual.setSaidaD2(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()));
            }
        }

        return horarioContratual;
    }

    private RelatorioMensal.HorarioContratual getHorarioContratual1510(DiaComEscala diaComEscala) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        RelatorioMensal.HorarioContratual horarioContratual = new RelatorioMensal.HorarioContratual();
        List<Jornada> jornadaDiaList = diaComEscala.getJornadaList();
        if (jornadaDiaList.size() == 2) {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            horarioContratual.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornadaL()));
            horarioContratual.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornadaL()));

        } else {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                horarioContratual.setEntradaD1(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()));
            }
            if (jornadaDiaList.get(0).getFimDescanso1() != null) {
                horarioContratual.setSaidaD1(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()));
            }
            if (jornadaDiaList.get(0).getInicioDescanso2() != null) {
                horarioContratual.setEntradaD2(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()));
            }
            if (jornadaDiaList.get(0).getFimDescanso2() != null) {
                horarioContratual.setSaidaD2(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()));
            }
        }
        return horarioContratual;
    }

    private Boolean containsHorarioContratual1510(RelatorioMensal.HorarioContratual horarioContratual, List<RelatorioMensal.HorarioContratual> horarioContratualList) {

        for (Iterator<RelatorioMensal.HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
            RelatorioMensal.HorarioContratual horarioContratual1 = it.next();
            if (horarioContratual.getEntrada1().equals(horarioContratual1.getEntrada1())
                    && horarioContratual.getSaida1().equals(horarioContratual1.getSaida1())
                    && horarioContratual.getEntrada2().equals(horarioContratual1.getEntrada2())
                    && horarioContratual.getSaida2().equals(horarioContratual1.getSaida2())) {
                return true;
            }
        }
        return false;
    }

    private Boolean containsHorarioContratual(HorarioContratual horarioContratual, List<HorarioContratual> horarioContratualList) {

        for (Iterator<HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
            HorarioContratual horarioContratual1 = it.next();
            if (horarioContratual.getEntrada1().equals(horarioContratual1.getEntrada1())
                    && horarioContratual.getSaida1().equals(horarioContratual1.getSaida1())
                    && horarioContratual.getEntrada2().equals(horarioContratual1.getEntrada2())
                    && horarioContratual.getSaida2().equals(horarioContratual1.getSaida2())) {
                return true;
            }
        }
        return false;
    }

    private List<PontoIrreal> getPontoIrreais(DiaComEscala diaComEscala) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        List<PontoIrreal> pontoIrrealList = new ArrayList<PontoIrreal>();
        List<SituacaoPonto> situacaoPontos = diaComEscala.getSituacaoPonto();
        situacaoPontos = ordenarSituacao(situacaoPontos);
        for (Iterator<SituacaoPonto> it = situacaoPontos.iterator(); it.hasNext();) {
            PontoIrreal pontoIrreal = new PontoIrreal();
            SituacaoPonto situacaoPonto = it.next();

            if (situacaoPonto.getSituacao().equals("Fora da faixa")) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("D");
                pontoIrreal.setMotivo("Marcação não Autorizada");
                pontoIrrealList.add(pontoIrreal);
            } else if (situacaoPonto.getSituacao().equals("Descartado")) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("D");
                pontoIrreal.setMotivo("Marcação Redundante");
                pontoIrrealList.add(pontoIrreal);
            } else if ((situacaoPonto.getIsAbonado())) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("I");
                pontoIrrealList.add(pontoIrreal);
                pontoIrreal.setMotivo(situacaoPonto.getHoraPontoObj().getJustificativa());
            } else if ((situacaoPonto.getIsPreAssinalado())) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("P");
                pontoIrreal.setMotivo("");
                pontoIrrealList.add(pontoIrreal);
            } else if (situacaoPonto.getSituacao().contains("Descanso")) {
                if (situacaoPonto.getSituacao().contains("Entrada")) {
                    pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                    pontoIrreal.setTipo("P");
                    pontoIrreal.setMotivo("Entrada de Descaso");
                    pontoIrrealList.add(pontoIrreal);
                } else if (situacaoPonto.getSituacao().contains("Saida")) {
                    pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                    pontoIrreal.setTipo("P");
                    pontoIrreal.setMotivo("Saida de Descaso");
                    pontoIrrealList.add(pontoIrreal);
                }
            }
        }
        return pontoIrrealList;
    }

    private List<RelatorioMensal.PontoIrreal> getPontoIrreais1510(DiaComEscala diaComEscala) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        List<RelatorioMensal.PontoIrreal> pontoIrrealList = new ArrayList<RelatorioMensal.PontoIrreal>();
        List<SituacaoPonto> situacaoPontos = diaComEscala.getSituacaoPonto();
        situacaoPontos = ordenarSituacao(situacaoPontos);
        for (Iterator<SituacaoPonto> it = situacaoPontos.iterator(); it.hasNext();) {
            RelatorioMensal.PontoIrreal pontoIrreal = new RelatorioMensal.PontoIrreal();
            SituacaoPonto situacaoPonto = it.next();

            if (situacaoPonto.getSituacao().equals("Fora da faixa")) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("D");
                pontoIrreal.setMotivo("Marcação não Autorizada");
                pontoIrrealList.add(pontoIrreal);
            } else if (situacaoPonto.getSituacao().equals("Descartado")) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("D");
                pontoIrreal.setMotivo("Marcação Redundante");
                pontoIrrealList.add(pontoIrreal);
            } else if ((situacaoPonto.getIsAbonado())) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("I");
                pontoIrrealList.add(pontoIrreal);
                pontoIrreal.setMotivo(situacaoPonto.getHoraPontoObj().getJustificativa());
            } else if ((situacaoPonto.getIsPreAssinalado())) {
                pontoIrreal.setHora(sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto()));
                pontoIrreal.setTipo("P");
                pontoIrreal.setMotivo("");
                pontoIrrealList.add(pontoIrreal);
            }
        }
        return pontoIrrealList;
    }

    private Integer enumerarHorarios(HorarioContratual horarioContratual, List<HorarioContratual> horarioContratualList) {
        for (Iterator<HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
            HorarioContratual horarioContratual1 = it.next();
            if (horarioContratual.isEqual(horarioContratual1)) {
                Integer cod = Integer.parseInt(horarioContratual1.getCod_horario());
                return cod;
            }
        }
        return null;
    }

    private Integer enumerarHorarios1510(RelatorioMensal.HorarioContratual horarioContratual, List<RelatorioMensal.HorarioContratual> horarioContratualList) {
        for (Iterator<RelatorioMensal.HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
            RelatorioMensal.HorarioContratual horarioContratual1 = it.next();
            if (horarioContratual.isEqual(horarioContratual1)) {
                Integer cod = Integer.parseInt(horarioContratual1.getCod_horario());
                return cod;
            }
        }
        return null;
    }

    public List<SituacaoPonto> ordenarSituacao(List<SituacaoPonto> situacaoList) {
        Collections.sort(situacaoList, new Comparator() {
            public int compare(Object o1, Object o2) {
                SituacaoPonto p1 = (SituacaoPonto) o1;
                SituacaoPonto p2 = (SituacaoPonto) o2;
                return p1.getHoraPontoObj().getPonto().getTime() < p2.getHoraPontoObj().getPonto().getTime()
                        ? -1 : (p1.getHoraPontoObj().getPonto().getTime() > p2.getHoraPontoObj().getPonto().getTime()
                        ? +1 : 0);
            }
        });
        return situacaoList;
    }

    public boolean imprimirPorDepartamento(ConsultaFrequenciaComEscalaBean c) throws JRException, Exception {

        List<Relatorio> relatorioList = new ArrayList<Relatorio>();

        boolean isVazio = true;

        for (Iterator<DiaComEscala> it = c.getDiasList().iterator(); it.hasNext();) {
            DiaComEscala dia = it.next();
            String data = dia.getDiaString();
            String definicao = dia.getDefinicao();
            String entrada1 = dia.getEntrada_1().getRegistro();
            String saida1 = dia.getSaida_1().getRegistro();
            String entrada2 = dia.getEntrada_2().getRegistro();
            String saida2 = dia.getSaida_2().getRegistro();
            String horasTrabalhadas = dia.getHorasTrabalhadas();
            String saldo = dia.getSaldoHoras();
            String observacao = dia.getJustificativa();
            Relatorio relatorio = new Relatorio(data, definicao, entrada1, saida1, entrada2, saida2,
                    horasTrabalhadas, saldo, observacao);
            relatorioList.add(relatorio);
            isVazio = false;
        }
        if (!isVazio) {
            Banco banco = new Banco();
            banco.insertRelatorio(relatorioList);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            banco = new Banco();
            List<String> userInfoList = new ArrayList<String>();
            userInfoList = banco.consultaInfoUsuario(c.getCod_funcionario());

            Impressao.geraRelatorioDepartamento(sdf.format(c.getDataInicio().getTime()), sdf.format(c.getDataFim().getTime()),
                    Integer.parseInt(userInfoList.get(0)), userInfoList.get(1), userInfoList.get(2),
                    userInfoList.get(3), userInfoList.get(4), userInfoList.get(5), c.getHorasASeremTrabalhadasTotal(), c.getHorasTotal(),
                    c.getSaldo2()/*c.getSaldoTotal()*/, c.getContDiasATrabalhar().toString(), c.getContDiasTrabalhados().toString(),
                    getPrimeiroNome(usuarioBean.getUsuario().getNome()), c.getFaltas(), adicionalNoturnoStr, horaExtra, gratificacaoStr);
        }
        return isVazio;
    }

    private Boolean isPeridoInvalido(List<PeriodoJornada> dataPeriodoJornadaList) {
        for (Iterator<PeriodoJornada> it = dataPeriodoJornadaList.iterator(); it.hasNext();) {
            PeriodoJornada periodoJornada = it.next();
            if (periodoJornada.getInvalido()) {
                return true;
            }
        }
        return false;
    }

    public String getPrimeiroNome(String nome) {

        String[] nomeCompleto = nome.split(" ");
        String[] primeiroNome_ = nomeCompleto[0].split("");
        String calda = "";

        for (int i = 2; i
                < primeiroNome_.length; i++) {
            calda += primeiroNome_[i].toLowerCase();
        }
        return primeiroNome_[1].toUpperCase() + calda;
    }

    private String transformaEmHoraLong(Long tempoemminutos) {

        tempoemminutos = tempoemminutos / 1000;

        Long horas = tempoemminutos / 3600;
        Long restoHoras = tempoemminutos % 3600;
        Long minutos = restoHoras / 60;
        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }

        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + ":" + minutosSrt;
    }

    private String transformaSegundosEmHora(Integer time) {

        Integer saida = time;
        Integer horas = Math.abs(saida / 3600);
        Integer minutos = Math.abs((saida / 60) % 60);
        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }

        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + "h:" + minutosSrt + "m";
    }

    static boolean compararHoras(String checkInLimite, String checkOutLimite, String hora) {

        String[] checkInLimiteArray = checkInLimite.split(":");
        String[] checkOutLimiteArray = checkOutLimite.split(":");
        String[] horaArray = hora.split(":");

        float intervalo1 = Integer.parseInt(checkInLimiteArray[0]) + (Integer.parseInt(checkInLimiteArray[1]) / 60f) + (Integer.parseInt(checkInLimiteArray[2].replace(".0", "")) / 3600f);
        float intervalo2 = Integer.parseInt(checkOutLimiteArray[0]) + (Integer.parseInt(checkOutLimiteArray[1]) / 60f) + (Integer.parseInt(checkOutLimiteArray[2].replace(".0", "")) / 3600f);
        float intervaloTeste = Integer.parseInt(horaArray[0]) + (Integer.parseInt(horaArray[1]) / 60f) + (Integer.parseInt(horaArray[2]) / 3600f);

        if (intervalo1 <= intervaloTeste && intervalo2 >= intervaloTeste) {
            return true;
        }
        return false;
    }

    static boolean verificaDescanso(String batida, String hora, float tolerancia) {
        if (!batida.equals("") && !hora.equals("")) {
            String[] horaArray = hora.split(":");
            String[] batidaArray = batida.split(":");

            float batidaFloat = Integer.parseInt(batidaArray[0]) + (Integer.parseInt(batidaArray[1]) / 60f) + (Integer.parseInt(batidaArray[2]) / 3600f);

            float horaFloat = Integer.parseInt(horaArray[0]) + (Integer.parseInt(horaArray[1]) / 60f) + (Integer.parseInt(horaArray[2]) / 3600f);
            float intervaloSup = horaFloat + (tolerancia / 60f);
            float intervaloInf = horaFloat - (tolerancia / 60f);

            if (intervaloInf <= batidaFloat && intervaloSup >= batidaFloat) {
                return true;
            }
        }
        return false;
    }

    private Integer calcDSR(Integer num_jornada, Date dataInicio, Date dataFim, Date startDate, List<Integer> diasDeslocadosFolgas, List<Feriado> feriadosList) {

        Banco banco = new Banco();
        dataInicio = getUltimaSegundaFeira(dataInicio);
        dataFim = getUltimoDomingo(dataFim);

        Integer qntDescontos = 0;
        if (dataInicio.getTime() < dataFim.getTime()) {

            HashMap<Integer, Integer> diaSemanaHashMap = semanaDsrHashMap(dataInicio, dataFim);
            HashMap<Integer, Boolean> semanaDsrHashMap = new HashMap<Integer, Boolean>();
            HashMap<Integer, List<Integer>> dsrQntFeriadoHashMap = semanaDsrQntFeriadoHashMap(dataInicio, dataFim);

            List<Integer> diasTrabalhoList = banco.consultaDiasTrabalho(cod_funcionario, num_jornada, dataInicio, dataFim, startDate,
                    diasDeslocadosFolgas, feriadoList);
            diasDeslocadosAdicionais = banco.consultaDiasDeslocadosAdicionais(cod_funcionario, dataInicio, dataFim);
            addDiasDeslocados(diasTrabalhoList, diasDeslocadosAdicionais);
            List<Integer> diasPresenteList = banco.consultaDiasNaoFalta(cod_funcionario, dataInicio, dataFim);
            List<Integer> diasAbonoList = banco.consultaDiasAbono(cod_funcionario, dataInicio, dataFim);
            List<Integer> diasAbonoPeriodoList = banco.consultaDiasAbonoPeriodo(cod_funcionario, dataInicio, dataFim);
            addDiasDeslocados(diasPresenteList, diasAbonoList);
            addDiasDeslocados(diasPresenteList, diasAbonoPeriodoList);

            for (Iterator<Integer> it = diasTrabalhoList.iterator(); it.hasNext();) {
                Integer diaTrabalho = it.next();
                if (!diasPresenteList.contains(diaTrabalho)) {
                    Integer semanaNumber = diaSemanaHashMap.get(diaTrabalho);
                    Boolean isDsrSemanaDescontado = semanaDsrHashMap.get(semanaNumber) == null ? false : semanaDsrHashMap.get(semanaNumber);
                    if (!isDsrSemanaDescontado) {
                        if (!hasFeriado(diaTrabalho, feriadosList)) {
                            qntDescontos++;
                            semanaDsrHashMap.put(semanaNumber, true);
                            List<Integer> feriadosSemana = dsrQntFeriadoHashMap.get(semanaNumber);
                            if (feriadosSemana != null) {
                                for (Iterator<Integer> it1 = feriadosSemana.iterator(); it1.hasNext();) {
                                    Integer diaFeriado = it1.next();
                                    if (!diasPresenteList.contains(diaFeriado)) {
                                        qntDescontos++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return qntDescontos;
    }

    private void setInfoEscala(List<Jornada> jornadasList, Date data) {
        if (!isDiaAfastado(data)) {
            GregorianCalendar diaHora = new GregorianCalendar();
            diaHora.setTimeInMillis(data.getTime());
            Integer diaDoMes = diaHora.get(Calendar.DAY_OF_MONTH);
            List<Integer> schClassIDMap = getSchClassIDs(jornadasList);

            if (!schClassIDMap.isEmpty()) {
                String dataDesl = zeraDataStr(data);
                DescolamentoTemporario deslT = diasDeslocamentoTempHashMap.get(dataDesl);
                Escala escala = new Escala(diaDoMes, schClassIDMap, data);
                escala.setDeslT(deslT);
                escalasList.add(escala);
            }
        }
    }

    private List<Integer> getSchClassIDs(List<Jornada> jornadasList) {
        List<Integer> schClassIDMap = new ArrayList<Integer>();
        for (Iterator<Jornada> it = jornadasList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (!jornada.getSoSaida()) {
                schClassIDMap.add(jornada.getSchClassID());
            }
        }
        return schClassIDMap;
    }

    private Date getUltimaSegundaFeira(Date data) {
        GregorianCalendar dia = new GregorianCalendar();
        dia.setTime(data);
        int diaSemana = dia.get(Calendar.DAY_OF_WEEK);
        while (diaSemana != 2) {
            dia.add(Calendar.DAY_OF_MONTH, -1);
            diaSemana = dia.get(Calendar.DAY_OF_WEEK);
        }
        return dia.getTime();
    }

    private Date getUltimoDomingo(Date data) {
        GregorianCalendar dia = new GregorianCalendar();
        dia.setTime(data);
        int diaSemana = dia.get(Calendar.DAY_OF_WEEK);
        while (diaSemana != 1) {
            dia.add(Calendar.DAY_OF_MONTH, -1);
            diaSemana = dia.get(Calendar.DAY_OF_WEEK);
        }
        return dia.getTime();
    }

    private HashMap<Integer, Integer> semanaDsrHashMap(Date dataInicio, Date dataFim) {

        HashMap<Integer, Integer> dsrHashMap = new HashMap<Integer, Integer>();

        GregorianCalendar diaInicio = new GregorianCalendar();
        diaInicio.setTimeInMillis(dataInicio.getTime());
        Integer diaAnoInicio = diaInicio.get(Calendar.DAY_OF_YEAR) + diaInicio.get(Calendar.YEAR) * 365;

        GregorianCalendar diaFim = new GregorianCalendar();
        diaFim.setTimeInMillis(dataFim.getTime());
        Integer diaAnoFim = diaFim.get(Calendar.DAY_OF_YEAR) + diaFim.get(Calendar.YEAR) * 365;

        int diaSemana = diaInicio.get(Calendar.DAY_OF_WEEK);
        Integer semanaNumber = 0;
        while (!diaAnoInicio.equals(diaAnoFim + 1)) {
            if (diaSemana == 1) {
                semanaNumber++;
                diaInicio.add(Calendar.DAY_OF_MONTH, 1);
                diaAnoInicio = diaInicio.get(Calendar.DAY_OF_YEAR) + diaInicio.get(Calendar.YEAR) * 365;
                diaSemana = diaInicio.get(Calendar.DAY_OF_WEEK);
            } else {
                dsrHashMap.put(diaAnoInicio, semanaNumber);
                diaInicio.add(Calendar.DAY_OF_MONTH, 1);
                diaAnoInicio = diaInicio.get(Calendar.DAY_OF_YEAR) + diaInicio.get(Calendar.YEAR) * 365;
                diaSemana = diaInicio.get(Calendar.DAY_OF_WEEK);
            }
        }
        return dsrHashMap;
    }

    private HashMap<Integer, List<Integer>> semanaDsrQntFeriadoHashMap(Date dataInicio, Date dataFim) {

        HashMap<Integer, List<Integer>> dsrQntFeriadoHashMap = new HashMap<Integer, List<Integer>>();

        GregorianCalendar diaInicio = new GregorianCalendar();
        diaInicio.setTimeInMillis(dataInicio.getTime());
        Integer diaAnoInicio = diaInicio.get(Calendar.DAY_OF_YEAR) + diaInicio.get(Calendar.YEAR) * 365;

        GregorianCalendar diaFim = new GregorianCalendar();
        diaFim.setTimeInMillis(dataFim.getTime());
        Integer diaAnoFim = diaFim.get(Calendar.DAY_OF_YEAR) + diaFim.get(Calendar.YEAR) * 365;

        int diaSemana = diaInicio.get(Calendar.DAY_OF_WEEK);
        Integer semanaNumber = 0;
        while (!diaAnoInicio.equals(diaAnoFim + 1)) {
            if (diaSemana == 1) {
                semanaNumber++;
                diaInicio.add(Calendar.DAY_OF_MONTH, 1);
                diaAnoInicio = diaInicio.get(Calendar.DAY_OF_YEAR) + diaInicio.get(Calendar.YEAR) * 365;
                diaSemana = diaInicio.get(Calendar.DAY_OF_WEEK);
            } else {
                if (hasFeriado(diaAnoInicio, feriadoList)) {
                    List<Integer> feriadosSemana = new ArrayList<Integer>();
                    feriadosSemana.add(diaAnoInicio);
                    if (dsrQntFeriadoHashMap.get(semanaNumber) != null) {
                        feriadosSemana.addAll(dsrQntFeriadoHashMap.get(semanaNumber));
                    }
                    dsrQntFeriadoHashMap.put(semanaNumber, feriadosSemana);
                }
                diaInicio.add(Calendar.DAY_OF_MONTH, 1);
                diaAnoInicio = diaInicio.get(Calendar.DAY_OF_YEAR) + diaInicio.get(Calendar.YEAR) * 365;
                diaSemana = diaInicio.get(Calendar.DAY_OF_WEEK);
            }
        }
        return dsrQntFeriadoHashMap;
    }

    private Boolean hasFeriado(Integer dia, List<Feriado> feriadoList) {
        for (Iterator<Feriado> it = feriadoList.iterator(); it.hasNext();) {
            Feriado feriado = it.next();
            if (dia.equals(feriado.getDiaFeriado())) {
                return true;
            }
        }
        return false;
    }

    private void addPontosPreAssinalados(DiaComEscala diaComEscala, List<Jornada> jornadaDiaList) {
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
            Integer qntTurnos = jornadaDiaList.size();

            if (qntTurnos == 2) {

                if (!jornadaDiaList.get(0).getIsSaidaObrigatoria().equals(1)) {
                    String ponto = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
                    diaComEscala.getSaida_1().setRegistro(ponto);
                    Ponto pontoObj = new Ponto();
                    Timestamp temp = new Timestamp(calcTime(diaComEscala, jornadaDiaList.get(0).getTerminioJornada().getTime()));
                    pontoObj.setPonto(temp);
                    pontoObj.setIsPreAssinalado(true);
                    diaComEscala.setSituacaoPonto(pontoObj, "Pré-Assinalado", "Saida do 1º turno");
                }

                if (jornadaDiaList.get(1).getIsEntradaObrigatoria() != null) {
                    if (!jornadaDiaList.get(1).getIsEntradaObrigatoria().equals(1)) {
                        String ponto = "";
                        if (jornadaDiaList.get(1).getInicioJornada() != null) {
                            ponto = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
                        }
                        diaComEscala.getEntrada_2().setRegistro(ponto);
                        Ponto pontoObj = new Ponto();
                        long tempo = 0;
                        if (jornadaDiaList.get(1).getInicioJornada() != null) {
                            tempo = jornadaDiaList.get(1).getInicioJornada().getTime();
                        }
                        Timestamp temp = new Timestamp(calcTime(diaComEscala, tempo));

                        pontoObj.setPonto(temp);
                        pontoObj.setIsPreAssinalado(true);
                        diaComEscala.setSituacaoPonto(pontoObj, "Pré-Assinalado", "Entrada do 2º turno");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("ConsultaPonto.ConsultaFrequenciaComEscalaBean.addPontosPreAssinalados: \n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Boolean isDiaAfastado(Date data) {
        Boolean afastou = false;
        for (Iterator<Afastamento> it = afastamentoList.iterator(); it.hasNext();) {

            Afastamento afastamento = it.next();
            if (afastamento != null) {
                afastou = ((afastamento.getDataInicio().before(data) || afastamento.getDataInicio().equals(data))
                        && ((afastamento.getDataFim().after(data)) || afastamento.getDataFim().equals(data)));
                if (afastou) {
                    break;
                }
            }
        }
        return afastou;
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

    private String zeraDataStr(Date dataEntrada) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdf.format(dataEntrada.getTime());
        return data;
    }

    private boolean isDiaFolga(Date dia) {
        String dataStr = zeraDataStr(dia);
        DescolamentoTemporario dt = diasDeslocamentoTempHashMap.get(dataStr);
        if (dt == null) {
            return false;
        } else {
            if (dt.getFolga()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private Date getProximoDia(Date data) {
        data = zeraData(data);
        GregorianCalendar d = new GregorianCalendar();
        d.setTimeInMillis(data.getTime());
        d.add(Calendar.DAY_OF_MONTH, 1);
        return d.getTime();
    }

    private Long calcTime(DiaComEscala diaComEscala, Long jornadaLong) {
        Long timeBase = zeraData(diaComEscala.getDia()).getTime();
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String hora = sdfHora.format(jornadaLong);
        Long saida = null;
        try {
            Time time = Time.valueOf(hora);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }

        return saida + timeBase;
    }

    public List<Ponto> ordenarPontos(List<Ponto> pontosList) {
        Collections.sort(pontosList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Ponto p1 = (Ponto) o1;
                Ponto p2 = (Ponto) o2;
                return p1.getPonto().getTime() < p2.getPonto().getTime() ? -1 : (p1.getPonto().getTime() > p2.getPonto().getTime() ? +1 : 0);
            }
        });
        return pontosList;
    }

    private List<Ponto> registroAdicionadoToPonto(List<RegistroAdicionado> registroAdicionadoList, DiaComEscala diaComEscala) {
        List<Ponto> pontoList = new ArrayList<Ponto>();
        Ponto ponto;
        for (Iterator<RegistroAdicionado> it = registroAdicionadoList.iterator(); it.hasNext();) {
            RegistroAdicionado registroAdicionado = it.next();
            if (zeraData(new Date(registroAdicionado.getCheckTime().getTime())).equals(diaComEscala.getDia())) {
                ponto = new Ponto();
                ponto.setPonto(registroAdicionado.getCheckTime());
                ponto.setTipoRegistro(0);
                pontoList.add(ponto);
            }
        }
        return pontoList;
    }

    private List<Ponto> addListPontoSemRedundancia(List<Ponto> pontoAtualList, List<Ponto> pontoAdicionadoList) {

        for (Iterator<Ponto> it = pontoAdicionadoList.iterator(); it.hasNext();) {
            Ponto ponto = it.next();
            if (!buscaPonto(pontoAtualList, ponto.getPonto())) {
                pontoAtualList.add(ponto);
            }
        }
        ordenarPontos(pontoAtualList);
        return pontoAtualList;
    }

    private boolean buscaPonto(List<Ponto> pontoList, Timestamp ponto) {

        for (Iterator<Ponto> it = pontoList.iterator(); it.hasNext();) {
            Ponto ponto1 = it.next();
            if (ponto1.getPonto().equals(ponto)) {
                return true;
            }
        }
        return false;
    }

    private void iniciarOpcoesFiltro() {
        System.out.println("teste de filtro");
        gestorFiltroFuncionarioList = getOpcaoFiltroGestor();
        Banco b = new Banco();
        regimeOpcaoFiltroFuncionarioList = b.getRegimeSelectItem();
        cargoOpcaoFiltroFuncionarioList = b.getCargoSelectItem();
        cod_funcionarioRegimeHashMap = b.getcod_funcionarioRegime();
        cod_funcionarioCargoHashMap = b.getcod_funcionarioCargo();

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
                Boolean criterioCargo = isFuncionarioDentroCriterioCargo(funcionario);
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario);
                if (criterioGestor && criterioRegime && criterioCargo) {
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

    private Boolean isFuncionarioDentroCriterioCargo(SelectItem funcionarioSelectItem) {
        Boolean criterioCargo = false;
        Integer cargo = cod_funcionarioCargoHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        criterioCargo = (cargoSelecionadoOpcaoFiltroFuncionario == -1) || cargo.equals(cargoSelecionadoOpcaoFiltroFuncionario);
        return criterioCargo;
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

    public void construtor() {
        iniciarOpcoesFiltro();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        consultaDepartamento(usuarioBean.getUsuario().getPermissao());
    }

    public String navegarDiaPonto() {
        String dataParam = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("diaParam");
        String data[] = dataParam.split(" ");
        dataSelecionada = data[1];
        return "consultaDia";
    }

    public Integer saldoDiferencialEmMinutos(String horasTotal, String horasASeremTrabalhadasTotal) {

        horasTotal = horasTotal.replace("m", "");
        String[] horasTotalSplit = horasTotal.split("h:");
        Integer horasTotalInt = (Integer.parseInt(horasTotalSplit[0]) * 60) + Integer.parseInt(horasTotalSplit[1]);

        horasASeremTrabalhadasTotal = horasASeremTrabalhadasTotal.replace("m", "");
        String[] horasASeremTrabalhadasTotalSplit = horasASeremTrabalhadasTotal.split("h:");
        Integer horasTrabalhadasInt = (Integer.parseInt(horasASeremTrabalhadasTotalSplit[0]) * 60) + Integer.parseInt(horasASeremTrabalhadasTotalSplit[1]);

        Integer saldo = horasTotalInt - horasTrabalhadasInt;

        return saldo;
    }

    public Integer saldoSomatorioEmMinutos(List<DiaComEscala> diasList) {

        Integer saldo = 0;
        String saldoTemp = "";
        for (Iterator<DiaComEscala> it = diasList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            if (!diaComEscala.getSaldoHoras().equals("")) {
                saldoTemp = diaComEscala.getSaldoHoras();
                saldoTemp = saldoTemp.replace("m", "");
                saldoTemp = saldoTemp.replace(" ", "");
                String[] saldoSplit = saldoTemp.split("h:");
                if (saldoSplit[0].contains("-")) {
                    saldoSplit[0] = saldoSplit[0].replace("-", "");
                    Integer temp = (Integer.parseInt(saldoSplit[0]) * 60) + Integer.parseInt(saldoSplit[1]);
                    saldo = saldo - temp;
                } else {
                    Integer temp = (Integer.parseInt(saldoSplit[0]) * 60) + Integer.parseInt(saldoSplit[1]);
                    saldo = saldo + temp;
                }
            }
        }

        return saldo;
    }

    private void inicializarAtributos() {
        departamentolist = new ArrayList<SelectItem>();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        cod_funcionario = 0;
        diasList = new ArrayList<DiaComEscala>();
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        cargoSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        departamento = null;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        diasDeslocamentoTempHashMap = new HashMap<String, DescolamentoTemporario>();
        afastamento = new Afastamento();
        setAfastamentoList(new ArrayList<Afastamento>());
        resumoList = new ArrayList<Resumo>();
        totalQntAtraso16_59 = 0;
        totalQntAtrasoMaiorUmaHora = 0;
        qnt_dias_adicional_noturno = 0;
        iniciarOpcoesFiltro();
    }

    private void inicializarAtributosConsultaDias() {
        diasList = new ArrayList<DiaComEscala>();
        diasComRegistroSemEscalaList = new ArrayList<DiaSemEscala>();
        abonoList = new ArrayList<Abono>();
        feriadoList = new ArrayList<Feriado>();
        jornadaList = new ArrayList<Jornada>();
        horasTrabalhadasDiaList = new ArrayList<String>();
        horasaSeremTrabalhadasDiaList = new ArrayList<String>();
        diaAcessoMap = new HashMap<String, DiaComEscala>();
        sdiasHashMap = new HashMap<Integer, List<Integer>>();
        diasComHorasExtraList = new ArrayList<String>();
        gratificacaoList = new ArrayList<Gratificacao>();
        horasTotal = "";
        horasASeremTrabalhadasTotal = "";
        saldo2 = new String();
        saldoTotal = new String();
        faltas = 0;
        contDiasATrabalhar = 0;
        contDiasTrabalhados = 0;
        qntDSR = 0;
        adicionalNoturnoInt = new Integer(0);
        adicionalNoturnoFinal = new Integer(0);
        qnt_dias_adicional_noturno = 0;
        page = 1;
        somaGratificacao = new HashMap<Integer, Integer>();
        saldoDiaCriticoInt = new Integer(0);
        diasDeslocadosAdicionais = new ArrayList<Integer>();
        escalasList = new ArrayList<Escala>();
        feriadoCriticosList = new ArrayList<Integer>();
        funcionarioSelecionado = new Funcionario();
        diasDeslocamentoTempHashMap = new HashMap<String, DescolamentoTemporario>();
    }

    private void inicializarAtributosConsultaDias2() {
        jornadaList = new ArrayList<Jornada>();
    }

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDepartamentoSrt() {
        return departamentoSrt;
    }

    public void setDepartamentoSrt(String departamentoSrt) {
        this.departamentoSrt = departamentoSrt;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public List<DiaComEscala> getDiasList() {
        return diasList;
    }

    public void setDiasList(ArrayList<DiaComEscala> diasList) {
        this.diasList = diasList;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public String getDataSelecionada() {
        return dataSelecionada;
    }

    public void setDataSelecionada(String dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Map<String, DiaComEscala> getDiaAcessoMap() {
        return diaAcessoMap;
    }

    public void setDiaAcessoMap(Map<String, DiaComEscala> diaAcessoMap) {
        this.diaAcessoMap = diaAcessoMap;
    }

    public String getHorasTotal() {
        return horasTotal;
    }

    public void setHorasTotal(String horasTotal) {
        this.horasTotal = horasTotal;
    }

    public String getHorasASeremTrabalhadasTotal() {
        return horasASeremTrabalhadasTotal;
    }

    public void setHorasASeremTrabalhadasTotal(String horasASeremTrabalhadasTotal) {
        this.horasASeremTrabalhadasTotal = horasASeremTrabalhadasTotal;
    }

    public String getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(String saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public List<String> getHorasTrabalhadasDiaList() {
        return horasTrabalhadasDiaList;
    }

    public void setHorasTrabalhadasDiaList(List<String> horasTrabalhadasDiaList) {
        this.horasTrabalhadasDiaList = horasTrabalhadasDiaList;
    }

    public String getDataSelecionadaIngles() {
        return dataSelecionadaIngles;
    }

    public void setDataSelecionadaIngles(String dataSelecionadaIngles) {
        this.dataSelecionadaIngles = dataSelecionadaIngles;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getContDiasATrabalhar() {
        return contDiasATrabalhar;
    }

    public void setContDiasATrabalhar(Integer contDiasATrabalhar) {
        this.contDiasATrabalhar = contDiasATrabalhar;
    }

    public Integer getContDiasTrabalhados() {
        return contDiasTrabalhados;
    }

    public void setContDiasTrabalhados(Integer contDiasTrabalhados) {
        this.contDiasTrabalhados = contDiasTrabalhados;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public HashMap<Integer, List<Integer>> getSdiasHashMap() {
        return sdiasHashMap;
    }

    public void setSdiasHashMap(HashMap<Integer, List<Integer>> sdiasHashMap) {
        this.sdiasHashMap = sdiasHashMap;
    }

    public HashMap<String, ArrayList<Jornada>> getDiasDeslocadosHashMap() {
        return diasDeslocadosHashMap;
    }

    public void setDiasDeslocadosHashMap(HashMap<String, ArrayList<Jornada>> diasDeslocadosHashMap) {
        this.diasDeslocadosHashMap = diasDeslocadosHashMap;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public List<RegistroAdicionado> getPontosAdicionados() {
        return pontosAdicionados;
    }

    public void setPontosAdicionados(List<RegistroAdicionado> pontosAdicionados) {
        this.pontosAdicionados = pontosAdicionados;
    }

    public List<Integer> getDiasDeslocadoshoraExtra() {
        return diasDeslocadoshoraExtra;
    }

    public void setDiasDeslocadoshoraExtra(List<Integer> diasDeslocadoshoraExtra) {
        this.diasDeslocadoshoraExtra = diasDeslocadoshoraExtra;
    }

    public List<Feriado> getFeriadoList() {
        return feriadoList;
    }

    public void setFeriadoList(List<Feriado> feriadoList) {
        this.feriadoList = feriadoList;
    }

    public List<Integer> getDiasHoraExtra() {
        return diasHoraExtra;
    }

    public void setDiasHoraExtra(List<Integer> diasHoraExtra) {
        this.diasHoraExtra = diasHoraExtra;
    }

    public String getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(String horaExtra) {
        this.horaExtra = horaExtra;
    }

    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }

    public String getAdicionalNoturnoStr() {
        return adicionalNoturnoStr;
    }

    public void setAdicionalNoturnoStr(String adicionalNoturnoStr) {
        this.adicionalNoturnoStr = adicionalNoturnoStr;
    }

    public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        abaCorrente = tab;
    }

    public Integer getQntDSR() {
        return qntDSR;
    }

    public void setQntDSR(Integer qntDSR) {
        this.qntDSR = qntDSR;
    }

    public String getGratificacaoStr() {
        return gratificacaoStr;
    }

    public void setGratificacaoStr(String gratificacaoStr) {
        this.gratificacaoStr = gratificacaoStr;
    }

    public List<TipoHoraExtra> getTipoHoraExtra() {
        return tipoHoraExtra;
    }

    public void setTipoHoraExtra(List<TipoHoraExtra> tipoHoraExtra) {
        this.tipoHoraExtra = tipoHoraExtra;
    }

    public Map<String, Integer> getTipo_valorHorasExtra() {
        return tipo_valorHorasExtra;
    }

    public void setTipo_valorHorasExtra(Map<String, Integer> tipo_valorHorasExtra) {
        this.tipo_valorHorasExtra = tipo_valorHorasExtra;
    }

    public List<Gratificacao> getGratificacaoList() {
        return gratificacaoList;
    }

    public void setGratificacaoList(List<Gratificacao> gratificacaoList) {
        this.gratificacaoList = gratificacaoList;
    }

    public Map<Integer, Integer> getSomaGratificacao() {
        return somaGratificacao;
    }

    public void setSomaGratificacao(Map<Integer, Integer> somaGratificacao) {
        this.somaGratificacao = somaGratificacao;
    }

    public List<Integer> getFeriadoCriticosList() {
        return feriadoCriticosList;
    }

    public void setFeriadoCriticosList(List<Integer> feriadoCriticosList) {
        this.feriadoCriticosList = feriadoCriticosList;
    }

    public Integer getSaldoDiaCriticoInt() {
        return saldoDiaCriticoInt;
    }

    public void setSaldoDiaCriticoInt(Integer saldoDiaCriticoInt) {
        this.saldoDiaCriticoInt = saldoDiaCriticoInt;
    }

    public String getSaldoDiaCriticoStr() {
        return saldoDiaCriticoStr;
    }

    public void setSaldoDiaCriticoStr(String saldoDiaCriticoStr) {
        this.saldoDiaCriticoStr = saldoDiaCriticoStr;
    }

    public String getSaldoSemFaltas() {
        return saldoSemFaltas;
    }

    public void setSaldoSemFaltas(String saldoSemFaltas) {
        this.saldoSemFaltas = saldoSemFaltas;
    }

    public List<Escala> getEscalasList() {
        return escalasList;
    }

    public void setEscalasList(List<Escala> escalasList) {
        this.escalasList = escalasList;
    }

    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    public List<Abono> getAbonoList() {
        return abonoList;
    }

    public void setAbonoList(List<Abono> abonoList) {
        this.abonoList = abonoList;
    }

    public List<SelectItem> getRegimeOpcaoFiltroFuncionarioList() {
        return regimeOpcaoFiltroFuncionarioList;
    }

    public List<SelectItem> getCargoOpcaoFiltroFuncionarioList() {
        return cargoOpcaoFiltroFuncionarioList;
    }

    public void setCargoOpcaoFiltroFuncionarioList(List<SelectItem> cargoOpcaoFiltroFuncionarioList) {
        this.cargoOpcaoFiltroFuncionarioList = cargoOpcaoFiltroFuncionarioList;
    }

    public Integer getCargoSelecionadoOpcaoFiltroFuncionario() {
        return cargoSelecionadoOpcaoFiltroFuncionario;
    }

    public void setCargoSelecionadoOpcaoFiltroFuncionario(Integer cargoSelecionadoOpcaoFiltroFuncionario) {
        this.cargoSelecionadoOpcaoFiltroFuncionario = cargoSelecionadoOpcaoFiltroFuncionario;
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

    public List<Ponto> getPontos() {
        return pontos;
    }

    public void setPontos(List<Ponto> pontos) {
        this.pontos = pontos;
    }

    public Funcionario getFuncionarioSelecionado() {
        return funcionarioSelecionado;
    }

    public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
        this.funcionarioSelecionado = funcionarioSelecionado;
    }

    public List<String> getDiasComHorasExtraList() {
        return diasComHorasExtraList;
    }

    public void setDiasComHorasExtraList(List<String> diasComHorasExtraList) {
        this.diasComHorasExtraList = diasComHorasExtraList;
    }

    public List<Afastamento> getAfastamentoList() {
        return afastamentoList;
    }

    public void setAfastamentoList(List<Afastamento> afastamentoList) {
        this.afastamentoList = afastamentoList;
        if (afastamentoList.size() > 0) {
            this.afastamento = afastamentoList.get(afastamentoList.size() - 1);
        }
    }

    public Afastamento getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Afastamento afastamento) {
        this.afastamento = afastamento;
    }

    public List<DiaSemEscala> getDiasComRegistroSemEscalaList() {
        return diasComRegistroSemEscalaList;
    }

    public void setDiasComRegistroSemEscalaList(List<DiaSemEscala> diasComRegistroSemEscalaList) {
        this.diasComRegistroSemEscalaList = diasComRegistroSemEscalaList;
    }

    public Integer getColumNumberComEscala() {
        if (diasList.size() <= 5) {
            columNumberComEscala = diasList.size();
        } else {
            columNumberComEscala = 5;
        }
        return columNumberComEscala;
    }

    public void setColumNumberComEscala(Integer columNumberComEscala) {
        this.columNumberComEscala = columNumberComEscala;
    }

    public Integer getColumNumberSemEscala() {
        if (diasComRegistroSemEscalaList.size() <= 5) {
            columNumberSemEscala = diasComRegistroSemEscalaList.size();
        } else {
            columNumberSemEscala = 5;
        }
        return columNumberSemEscala;
    }

    public void setColumNumberSemEscala(Integer columNumberSemEscala) {
        this.columNumberSemEscala = columNumberSemEscala;
    }

    public List<Resumo> getResumoList() {
        resumoList = new ArrayList<Resumo>();
        Resumo resumo = new Resumo();
        resumo.setHorasTrabalhadas(horasTotal);
        resumo.setHorasContratadas(horasASeremTrabalhadasTotal);
        resumo.setSaldo(saldo2);
        //resumo.setSaldo(saldoTotal);
        resumo.setHoraExtra(horaExtra);
        resumo.setFaltas(faltas);
        resumo.setDiasTrabalhados(contDiasTrabalhados);
        resumo.setDiasContratados(contDiasATrabalhar);
        resumo.setAdicionalNoturno(adicionalNoturnoStr);
        resumoList.add(resumo);
        return resumoList;
    }

    public void setResumoList(List<Resumo> resumoList) {
        this.resumoList = resumoList;
    }

    public Integer getTotalQntAtraso16_59() {
        return totalQntAtraso16_59;
    }

    public void setTotalQntAtraso16_59(Integer totalQntAtraso16_59) {
        this.totalQntAtraso16_59 = totalQntAtraso16_59;
    }

    public Integer getTotalQntAtrasoMaiorUmaHora() {
        return totalQntAtrasoMaiorUmaHora;
    }

    public void setTotalQntAtrasoMaiorUmaHora(Integer totalQntAtrasoMaiorUmaHora) {
        this.totalQntAtrasoMaiorUmaHora = totalQntAtrasoMaiorUmaHora;
    }

    public String getListagemAtraso() {
        if (listagemAtraso.endsWith("\n")) {
            listagemAtraso = listagemAtraso.substring(0, listagemAtraso.length() - 1);
        }
        return listagemAtraso;
    }

    public void setListagemAtraso(String listagemAtraso) {
        this.listagemAtraso = listagemAtraso;
    }

    public String getListagemFalta() {
        if (listagemFalta.endsWith("\n")) {
            listagemFalta = listagemFalta.substring(0, listagemFalta.length() - 1);
        }
        return listagemFalta;
    }

    public void setListagemFalta(String listagemFalta) {
        this.listagemFalta = listagemFalta;
    }

    public Integer getQnt_dias_adicional_noturno() {
        return qnt_dias_adicional_noturno;
    }

    public void setQnt_dias_adicional_noturno(Integer qnt_dias_adicional_noturno) {
        this.qnt_dias_adicional_noturno = qnt_dias_adicional_noturno;
    }

    public RegimeHoraExtra getRegimeHoraExtra() {
        return regimeHoraExtra;
    }

    public void setRegimeHoraExtra(RegimeHoraExtra regimeHoraExtra) {
        this.regimeHoraExtra = regimeHoraExtra;
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

    public String getSaldo2() {
        return saldo2;
    }

    public void setSaldo2(String saldo2) {
        this.saldo2 = saldo2;
    }
}
