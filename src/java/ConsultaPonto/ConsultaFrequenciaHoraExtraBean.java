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
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class ConsultaFrequenciaHoraExtraBean implements Serializable {

    private String nome;
    private Integer departamento;
    private String departamentoSrt;
    private String dataSelecionada;
    private String dataSelecionadaIngles;
    private Integer cod_funcionario;
    private Integer page = 1;
    private List<SelectItem> funcionarioList;
    private ArrayList<HoraExtra> diasList;
    private List<SelectItem> departamentolist;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private static UsuarioBean usuarioBean;
    private Boolean renderPanel = false;
    private Boolean incluirSubSetores;
    private Integer cod_categoria;
    private String justificativa;
    List<DiaComEscala> diasComEscalaList;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private List<DiaHoraExtra> diaHoraExtraList = new ArrayList<DiaHoraExtra>();
    private List<SelectItem> categoriaJustificativaList;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    //Filtro por cargo
    private List<SelectItem> cargoOpcaoFiltroFuncionarioList;
    private Integer cargoSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioCargoHashMap;

    public ConsultaFrequenciaHoraExtraBean() throws SQLException {

        inicializarAtributos();
        departamentolist = new ArrayList<SelectItem>();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        dataInicio = getFirstDay();
        dataFim = getLastDay();
        cod_funcionario = 0;
        objLocale = new Locale("pt", "BR");
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));

        if (usuarioBean != null) {
            consultaDepartamento(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
        Locale.setDefault(new Locale("pt", "BR"));
    }

    public void consultaDepartamento(Integer permissao) {
        Banco banco = new Banco();
        departamentolist = banco.consultaDepartamentoHierarquico(permissao);
    }

    /*

    public void consultaDepartamento(String permissao) {
    Banco banco = new Banco();
    departamentolist = new ArrayList<SelectItem>();
    Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
    Integer dept = usuarioBean.getUsuario().getDepartamento();
    departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
    isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }
     *
     */
    public String consultaNome(Integer deptid) {
        Banco banco = new Banco();
        return banco.consultaDepartamentoNome(deptid);
    }

    public void consultaFuncionario() throws SQLException {
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        String permissao = usuarioBean.getUsuario().getPermissao();
        funcionarioList = banco.consultaFuncionario(departamento, permissao, incluirSubSetores);
        departamentoSrt = consultaNome(departamento);
        inicializarAtributosConsultaDias();

        funcionarioList = filtrarFuncionario();

    }

    public void consultaDias() {
        Banco banco = new Banco();
        try {
            inicializarAtributosConsultaDias(); 
            if (isEntradaValida()) {
                if ((dataInicio.getTime() > dataFim.getTime())) {
                    FacesMessage msgErro = new FacesMessage("A data final deve ser maior que a data inicial.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    showHoraExtraEmMassa();
                    nome = banco.consultaNome(cod_funcionario);
                    ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");
                    c.setCod_funcionario(cod_funcionario);
                    c.setDataInicio(dataInicio);
                    c.setDataFim(dataFim);
                    c.consultaDiasSemMsgErro();
                    diasComEscalaList = new ArrayList<DiaComEscala>();
                    diasComEscalaList = c.getDiasList();
                    diasList.addAll(incluirDiasTrabalhados());

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

    private List<HoraExtra> incluirDiasTrabalhados() {

        List<HoraExtra> horaExtraList = new ArrayList<HoraExtra>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Banco banco = new Banco();
        List<String> diasComHorasExtraList = banco.consultaDiasHoraExtra(cod_funcionario, dataInicio, dataFim);
        banco.fecharConexao();
        for (Iterator<DiaComEscala> it = diasComEscalaList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();

            if (!diaComEscala.getDefinicao().equals("Feriado")) {
                HoraExtra horaExtra = new HoraExtra();
                horaExtra.setData(diaComEscala.getDia());
                String dataStr = sdf.format(diaComEscala.getDia());
                horaExtra.setDataString(dataStr);
                if (diasComHorasExtraList.contains(dataStr)) {
                    horaExtra.setImagem("../images/diaHoraExtra.png");
                } else {
                    horaExtra.setImagem("../images/diaNormal.png");
                }
                horaExtra.setSaldo(diaComEscala.getSaldoHoras());
                horaExtraList.add(horaExtra);
            }
        }
        return horaExtraList;
    }

    public void inserirHoraExtra() {
        if (cod_categoria != -1) {
            setDiaHoraExtra(diasComEscalaList);
            Banco banco = new Banco();
            if (!diaHoraExtraList.isEmpty()) {
                Boolean sucesso = banco.insertHoraExtraEmMassa(cod_funcionario, dataInicio, dataFim, diaHoraExtraList);
                if (sucesso) {
                    //      Metodos.setLogInfo("Adicionar Hora Extra - Funcionario: \""+nome+"\" Data: "+dataSrt);
                    FacesMessage msgErro = new FacesMessage("Hora extra inserida com sucesso.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    consultaDias();
                } else {
                    FacesMessage msgErro = new FacesMessage("Erro ao inserir hora extra.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Uma categoria de hora extra precisa ser escolhida!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void showHoraExtraEmMassa() {
        Banco banco = new Banco();
        categoriaJustificativaList = new ArrayList<SelectItem>();
        categoriaJustificativaList = banco.consultaCategoriaHoraExtra(cod_funcionario);
    }

    private void setDiaHoraExtra(List<DiaComEscala> diacomEscalaList) {
        diaHoraExtraList = new ArrayList<DiaHoraExtra>();
        for (Iterator<DiaComEscala> it = diacomEscalaList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            if (!diaComEscala.getIsFeriado()) {
                if (!isNaoPositivo(diaComEscala.getSaldoHoras())) {
                    DiaHoraExtra diaHoraExtra = new DiaHoraExtra();
                    diaHoraExtra.setUserid(cod_funcionario);
                    diaHoraExtra.setData(diaComEscala.getDia());
                    diaHoraExtra.setCod_categoria(cod_categoria);
                    diaHoraExtra.setJustificativa(justificativa);
                    diaHoraExtraList.add(diaHoraExtra);
                }
            }
        }
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

    public String navegar2DiaHoraExtraBean() {
        String saldo = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("saldo");
        if (!isNaoPositivo(saldo)) {
            return "diaHoraExtra";
        } else {
            FacesMessage msgErro = new FacesMessage("Não é possível adicionar horas extras em dias sem saldo positivo!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return "";
        }
    }

    private boolean isNaoPositivo(String saldo) {
        Boolean saida = true;
        try {
            saida = saldo.subSequence(0, 1).equals("-") || saldo.equals("00h:00m");
        } catch (Exception e) {
        }
        return saida;
    }

    private void inicializarAtributos() {
        cod_categoria = null;
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        cargoSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        categoriaJustificativaList = new ArrayList<SelectItem>();
        iniciarOpcoesFiltro();
    }

    private void inicializarAtributosConsultaDias() {
        diasList = new ArrayList<HoraExtra>();
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
        return (dataInicio != null) && (dataFim != null) && !cod_funcionario.equals(0) && !cod_funcionario.equals(-1);
    }

    private void iniciarOpcoesFiltro() {
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
        consultaDepartamento(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
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

    public List<HoraExtra> getDiasList() {
        return diasList;
    }

    public void setDiasList(ArrayList<HoraExtra> diasList) {
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

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        ConsultaFrequenciaHoraExtraBean.usuarioBean = usuarioBean;
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

    public Integer getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(Integer cod_categoria) {
        this.cod_categoria = cod_categoria;
    }

    public List<DiaComEscala> getDiasComEscalaList() {
        return diasComEscalaList;
    }

    public void setDiasComEscalaList(List<DiaComEscala> diasComEscalaList) {
        this.diasComEscalaList = diasComEscalaList;
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

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public List<SelectItem> getCategoriaJustificativaList() {
        return categoriaJustificativaList;
    }

    public void setCategoriaJustificativaList(List<SelectItem> categoriaJustificativaList) {
        this.categoriaJustificativaList = categoriaJustificativaList;
    }

    public List<DiaHoraExtra> getDiaHoraExtraList() {
        return diaHoraExtraList;
    }

    public void setDiaHoraExtraList(List<DiaHoraExtra> diaHoraExtraList) {
        this.diaHoraExtraList = diaHoraExtraList;
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

    public HashMap<Integer, Integer> getCod_funcionarioCargoHashMap() {
        return cod_funcionarioCargoHashMap;
    }

    public void setCod_funcionarioCargoHashMap(HashMap<Integer, Integer> cod_funcionarioCargoHashMap) {
        this.cod_funcionarioCargoHashMap = cod_funcionarioCargoHashMap;
    }
    
}
