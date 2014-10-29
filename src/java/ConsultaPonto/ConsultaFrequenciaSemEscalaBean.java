/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

/**
 *
 * @author Alexandre
 */
import Usuario.UsuarioBean;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author amsgama
 */
public class ConsultaFrequenciaSemEscalaBean {

    private String nome;
    private Integer departamento;
    private String departamentoSrt;
    private String dataSelecionada;
    private String dataSelecionadaIngles;
    private Integer cod_funcionario;
    private Integer page = 1;
    private List<SelectItem> funcionarioList;
    private ArrayList<DiaSemEscala> diasList;
    private List<String> diasComPontosBatidos;
    private List<SelectItem> departamentolist;
    List<Feriado> feriadoList;
    List<Integer> diasTrabalhoList;
    private Map<String, DiaComEscala> diaAcessoMap;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private static UsuarioBean usuarioBean;
    private Boolean renderPanel = false;
    private Boolean incluirSubSetores;
    private Boolean isAdministradorVisivel;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;

    public ConsultaFrequenciaSemEscalaBean() throws SQLException {

        inicializarAtributos();
        departamentolist = new ArrayList<SelectItem>();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        dataInicio = getFirstDay();
        dataFim = getLastDay();
        cod_funcionario = 0;
        diasList = new ArrayList<DiaSemEscala>();

        objLocale = new Locale("pt", "BR");

        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));

        if (usuarioBean != null) {
            if (!usuarioBean.getUsuario().getPermissao().equals("0")) {
                consultaDepartamento(usuarioBean.getUsuario().getPermissao());
            } else if (usuarioBean.getUsuario().getPermissao().equals("0")) {
                nome = usuarioBean.getUsuario().getNome();
                cod_funcionario = usuarioBean.getUsuario().getLogin();
                departamento = usuarioBean.getUsuario().getDepartamento();
                departamentoSrt = consultaNome(departamento);
                consultaDias();
            }
        }
        Locale.setDefault(new Locale("pt", "BR"));
    }

    public ConsultaFrequenciaSemEscalaBean(String x) {
    }

    public void consultaDepartamento(String permissao) {
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        Integer dept = usuarioBean.getUsuario().getDepartamento();
        departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }

    public String consultaNome(Integer deptid) {
        Banco banco = new Banco();
        return banco.consultaDepartamentoNome(deptid);
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
        departamentoSrt = consultaNome(departamento);
        inicializarAtributosConsultaDias();

        funcionarioList = filtrarFuncionario();

    }

    public void consultaDias() {
        Banco banco = new Banco();
        try {
            inicializarAtributosConsultaDias();
            if (((isEntradaValida()))) {
                if ((dataInicio.getTime() > dataFim.getTime())) {
                    FacesMessage msgErro = new FacesMessage("A data final deve ser maior que a data inicial.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {

                    diasComPontosBatidos.addAll(banco.consultaDiasPontoBatido(cod_funcionario, dataInicio, dataFim));

                    nome = banco.consultaNome(cod_funcionario);

                    inicializarAtributosConsultaDias2();

                    List<Timestamp> pontos = banco.consultaChechInOutTotal(cod_funcionario, dataInicio, dataFim);

                    diasList.addAll(incluirDiasTrabalhados(pontos));

                    if (diasList.isEmpty()) {
                        FacesMessage msgErro = new FacesMessage("Não existe registro de ponto do funcionário neste intervalo de tempo!");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    }
                }
            }
        } catch (NullPointerException e) {
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
        try {
            inicializarAtributosConsultaDias();
            if (isEntradaValida()) {
                if ((dataInicio.getTime() <= dataFim.getTime())) {

                    diasComPontosBatidos.addAll(banco.consultaDiasPontoBatido(cod_funcionario, dataInicio, dataFim));

                    nome = banco.consultaNome(cod_funcionario);

                    inicializarAtributosConsultaDias2();

                    List<Timestamp> pontos = banco.consultaChechInOutTotal(cod_funcionario, dataInicio, dataFim);

                    diasList.addAll(incluirDiasTrabalhados(pontos));
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        } finally {
            try {
                banco.fecharConexao();
            } catch (Exception e) {
            }
        }
    }

    private List<DiaSemEscala> incluirDiasTrabalhados(List<Timestamp> pontos) {

        List<DiaSemEscala> pontoAcessoTotalList = new ArrayList<DiaSemEscala>();
        DiaSemEscala pontoAcessoTotal = new DiaSemEscala();
        List<String> dataStringList = new ArrayList<String>();
        List<Long> diaTimestampList = new ArrayList<Long>();

        for (Iterator<Timestamp> it = pontos.iterator(); it.hasNext();) {

            Timestamp diaEmMiliTimes = it.next();

            Long diaGroup = diaEmMiliTimes.getTime();

            GregorianCalendar dia = new GregorianCalendar();
            dia.setTimeInMillis(diaGroup);

            Locale.setDefault(new Locale("pt", "BR"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataCorrente = sdf.format(diaGroup);

            if (diasComPontosBatidos.contains(dataCorrente)) {

                if (containsDia(dataCorrente, dataStringList) || diaTimestampList.isEmpty()) {
                    diaTimestampList.add(diaGroup);
                } else {
                    pontoAcessoTotal = setPontoAcesso(pontoAcessoTotal, diaTimestampList);
                    if (!diaTimestampList.isEmpty()) {
                        pontoAcessoTotalList.add(pontoAcessoTotal);
                    }
                    diaTimestampList = new ArrayList<Long>();
                    pontoAcessoTotal = new DiaSemEscala();
                }

                if (!containsDia(dataCorrente, dataStringList)) {
                    dataStringList.add(dataCorrente);
                    if (!diaTimestampList.contains(diaGroup)) {
                        diaTimestampList.add(diaGroup);
                    }
                }

                if (!it.hasNext()) {
                    pontoAcessoTotal = setPontoAcesso(pontoAcessoTotal, diaTimestampList);
                    pontoAcessoTotalList.add(pontoAcessoTotal);
                }
            }
        }
        return pontoAcessoTotalList;
    }

    public boolean containsDia(String dia, List<String> diaAcesso) {
        for (Iterator<String> it = diaAcesso.iterator(); it.hasNext();) {
            String diaItem = it.next();

            if (dia.equals(diaItem)) {
                return true;
            }
        }
        return false;
    }

    private DiaSemEscala setPontoAcesso(DiaSemEscala pontoAcessoTotal, List<Long> pontos) {

        GregorianCalendar diaHora = new GregorianCalendar();
        diaHora.setTimeInMillis(pontos.get(0));

        pontoAcessoTotal = new DiaSemEscala();

        pontoAcessoTotal.setData(diaHora.getTime());

        Locale.setDefault(new Locale("pt", "BR"));

        for (Iterator<Long> it = pontos.iterator(); it.hasNext();) {
            Long ponto = it.next();
            pontoAcessoTotal.setHorasList(new Date(ponto));
        }
        return pontoAcessoTotal;
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

    public ArrayList<DiaComEscala> diasATrabalhar() {

        ArrayList<DiaComEscala> diasAtrabalhar = new ArrayList<DiaComEscala>();

        GregorianCalendar diaHorainicio = new GregorianCalendar();
        diaHorainicio.setTime(dataInicio);
        Integer diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;

        GregorianCalendar diaHoraFim = new GregorianCalendar();
        diaHoraFim.setTime(dataFim);


        int diaFim = diaHoraFim.get(Calendar.DAY_OF_YEAR) + diaHoraFim.get(Calendar.YEAR) * 365;

        Locale.setDefault(new Locale("pt", "BR"));
        SimpleDateFormat sdf = new SimpleDateFormat("EE dd/MM/yyyy");

        do {
            int diaSemana = diaHorainicio.get(Calendar.DAY_OF_WEEK) - 1;

            if (diasTrabalhoList.contains(diaSemana)) {
                DiaComEscala acesso = new DiaComEscala();

                acesso.setDiaString(sdf.format(diaHorainicio.getTime()));
                acesso.setColorDia("panelBranco");
                acesso.setDia(diaHorainicio.getTime());

                for (Iterator<Feriado> it = feriadoList.iterator(); it.hasNext();) {
                    Feriado feriado = it.next();

                    if (diaInicio.equals(feriado.getDiaFeriado())) {
                        GregorianCalendar diaHora = new GregorianCalendar();
                        diaHora.setTimeInMillis(feriado.getDia().getTime());
                        acesso.setJustificativa(feriado.getNome());
                        acesso.setDefinicao("Feriado");
                    }
                }
                diasAtrabalhar.add(acesso);
            }
            diaHorainicio.add(Calendar.DAY_OF_MONTH, 1);
            diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;
        } while (diaInicio != diaFim + 1);

        return diasAtrabalhar;
    }

    public ArrayList<DiaComEscala> getResumoDia(ArrayList<DiaComEscala> diasTeoricos, ArrayList<DiaComEscala> diasReais) {

        ArrayList<DiaComEscala> saidaList = new ArrayList<DiaComEscala>();

        boolean flag;

        for (int i = 0; i < diasTeoricos.size(); i++) {
            flag = true;


            for (int j = 0; j < diasReais.size(); j++) {
                if (diasTeoricos.get(i).getDiaString().equals(diasReais.get(j).getDiaString())) {
                    saidaList.add(diasReais.get(j));
                    GregorianCalendar diaHora = new GregorianCalendar();
                    diaHora.setTimeInMillis(diasReais.get(j).getDia().getTime());
                    Locale.setDefault(new Locale("pt", "BR"));
                    SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
                    String data = sdfHora.format(diasReais.get(j).getDia().getTime());

                    if (!diasComPontosBatidos.contains(data)) {
                        diasReais.get(j).delSituacaoPonto();
                    }
                    if (diasReais.get(j).getDefinicao().equals("")) {
                        diasReais.get(j).setDefinicao("Regular");
                    }
                    break;
                }
            }
            if (flag) {
                diasTeoricos.get(i).setPresente(false);


                if (diasTeoricos.get(i).getJustificativa().equals("")) {

                    GregorianCalendar diaHora = new GregorianCalendar();
                    diaHora.setTimeInMillis(diasTeoricos.get(i).getDia().getTime());

                    diasTeoricos.get(i).setDefinicao("Irregular");
                    diasTeoricos.get(i).setJustificativa("FALTA");
                    diasTeoricos.get(i).setColorDia("panelVermelho");
                }
                saidaList.add(diasTeoricos.get(i));
            }
        }
        return saidaList;
    }

    public static Date getFirstDay() {
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        return data;
    }

    public static Date getLastDay() {
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        Date data = calendar.getTime();
        return data;
    }

    public Date ultimoDiaDoMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));   
        Date data = calendar.getTime();
        return data;
    }
    
    static boolean compararHoras(Date checkInLimite, Date checkOutLimite, String jornada_hora) {

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

    public String navegarDiaPonto() {
        String dataParam = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("diaParam");
        String data[] = dataParam.split(" ");
        dataSelecionada = data[1];
        return "consultaDia";
    }

    public String transformaEmHora(Integer tempoemminutos) {

        Integer horas = tempoemminutos / 3600;
        Integer minutos = tempoemminutos % 3600;
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

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        iniciarOpcoesFiltro();
    }

    private void inicializarAtributosConsultaDias() {
        diasList = new ArrayList<DiaSemEscala>();
        feriadoList = new ArrayList<Feriado>();
        diasTrabalhoList = new ArrayList<Integer>();
        diaAcessoMap = new HashMap<String, DiaComEscala>();
        diasComPontosBatidos = new ArrayList<String>();
    }

    private void inicializarAtributosConsultaDias2() {
        diasTrabalhoList = new ArrayList<Integer>();
    }

    public String getPrimeiroNome(String nome) {
        String[] nomeCompleto = nome.split(" ");

        String[] primeiroNome_ = nomeCompleto[0].split("");

        String calda = "";


        for (int i = 2; i < primeiroNome_.length; i++) {
            calda += primeiroNome_[i].toLowerCase();


        }
        return primeiroNome_[1].toUpperCase() + calda;
    }

    private boolean isEntradaValida() {
        return (!dataInicio.equals(null)) && (!dataFim.equals(null)) && !cod_funcionario.equals(0) && !cod_funcionario.equals(-1);
    }

    public void imprimir() throws JRException, Exception {
        if (!diasList.isEmpty()) {
            List<DiaSemEscala> pontoAcessoTotalList = new ArrayList<>();
            for (Iterator<DiaSemEscala> it = diasList.iterator(); it.hasNext();) {
                DiaSemEscala dia = it.next();
                Date data = dia.getData();
                List<Date> pontosBatidos = dia.getHorasList();

                DiaSemEscala pontoAcessoTotal = new DiaSemEscala(data, pontosBatidos);
                pontoAcessoTotalList.add(pontoAcessoTotal);
            }
            Banco banco = new Banco();
            banco.insertRelatorioSemEscala(pontoAcessoTotalList);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            banco = new Banco();
            List<String> userInfoList = new ArrayList<>();
            userInfoList = banco.consultaInfoUsuario(cod_funcionario);

            Impressao.geraRelatorioSemEscala(sdf.format(dataInicio.getTime()), sdf.format(dataFim.getTime()),
                    Integer.parseInt(userInfoList.get(0)), userInfoList.get(1), userInfoList.get(2),
                    userInfoList.get(3), userInfoList.get(4), userInfoList.get(5), getPrimeiroNome(usuarioBean.getUsuario().getNome()));
        } else {
            FacesMessage msgErro = new FacesMessage("Não há informações a serem impressas!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public Boolean imprimirPorDepartamento(ConsultaFrequenciaSemEscalaBean p) throws JRException, Exception {

        List<DiaSemEscala> pontoAcessoTotalList = new ArrayList<DiaSemEscala>();

        boolean isVazio = true;
        for (Iterator<DiaSemEscala> it = p.getDiasList().iterator(); it.hasNext();) {
            DiaSemEscala dia = it.next();
            Date data = dia.getData();
            List<Date> pontosBatidos = dia.getHorasList();

            DiaSemEscala pontoAcessoTotal = new DiaSemEscala(data, pontosBatidos);
            pontoAcessoTotalList.add(pontoAcessoTotal);
            isVazio = false;
        }
        if (!isVazio) {
            Banco banco = new Banco();
            banco.insertRelatorioSemEscala(pontoAcessoTotalList);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            List<String> userInfoList = new ArrayList<String>();

            banco = new Banco();
            userInfoList = banco.consultaInfoUsuario(p.getCod_funcionario());

            Impressao.geraRelatorioSemEscalaDepartamento(sdf.format(p.getDataInicio().getTime()), sdf.format(p.getDataFim().getTime()),
                    Integer.parseInt(userInfoList.get(0)), userInfoList.get(1), userInfoList.get(2),
                    userInfoList.get(3), userInfoList.get(4), userInfoList.get(5), getPrimeiroNome(usuarioBean.getUsuario().getNome()));
        }
        return isVazio;
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

    public void construtor() {
        iniciarOpcoesFiltro();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        consultaDepartamento(usuarioBean.getUsuario().getPermissao());
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

    public List<DiaSemEscala> getDiasList() {
        return diasList;
    }

    public void setDiasList(ArrayList<DiaSemEscala> diasList) {
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

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public Boolean getRenderPanel() {
        return renderPanel;
    }

    public void setRenderPanel(Boolean renderPanel) {
        this.renderPanel = renderPanel;
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

    public String getDepartamentoSrt() {
        return departamentoSrt;
    }

    public void setDepartamentoSrt(String departamentoSrt) {
        this.departamentoSrt = departamentoSrt;
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

    public Integer getTipoGestorSelecionadoOpcaoFiltroFuncionario() {
        return tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public void setTipoGestorSelecionadoOpcaoFiltroFuncionario(Integer tipoGestorSelecionadoOpcaoFiltroFuncionario) {
        this.tipoGestorSelecionadoOpcaoFiltroFuncionario = tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public List<SelectItem> getGestorFiltroFuncionarioList() {
        return gestorFiltroFuncionarioList;
    }

    public void setGestorFiltroFuncionarioList(List<SelectItem> gestorFiltroFuncionarioList) {
        this.gestorFiltroFuncionarioList = gestorFiltroFuncionarioList;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Long saldoFloat = (Time.valueOf("22:01:00").getTime() - Time.valueOf("22:00:00").getTime()) / 1000;
        System.out.print(saldoFloat);
    }
}
