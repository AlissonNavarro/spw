/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

/**
 *
 * @author Alexandre
 */
import Metodos.Metodos;
import ConsultaPonto.ConsultaFrequenciaSemEscalaBean;
import Funcionario.Funcionario;
import Usuario.UsuarioBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

public class RelatorioMensalSemEscalaBean implements Serializable {

    private String departamentoSelecionado;
    private Integer escalaSelecionada;
    private List<SelectItem> departamentosSelecItem;
    private List<SelectItem> escalasSelecItem;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private Boolean incluirSubSetores;
    private List<SelectItem> funcionarioList;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;

    public RelatorioMensalSemEscalaBean() {
        inicializarAtributos();
        departamentosSelecItem = new ArrayList<SelectItem>();
        objLocale = new Locale("pt", "BR");
        dataInicio = getFirstDay();
        dataFim = getLastDay();
        consultaDepartamentos();
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
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date data = calendar.getTime();
        return data;
    }

    public void imprimirDepartamento() throws FileNotFoundException, JRException, Exception {

        try {
            if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

                ConsultaFrequenciaSemEscalaBean pontoAcessoTotal = new ConsultaFrequenciaSemEscalaBean("");

                Date dataInicio_ = (Date) dataInicio.clone();
                Date dataFim_ = (Date) dataFim.clone();

                pontoAcessoTotal.setDataInicio(dataInicio_);
                pontoAcessoTotal.setDataFim(dataFim_);
                List<Integer> matriculasList = new ArrayList<Integer>();
                List<Integer> matriculasMergeList = new ArrayList<Integer>();
                List<Integer> matriculasSemRegistroList = new ArrayList<Integer>();
                matriculasList = getFuncionarios();
                Map<Integer, Funcionario> matriculaFuncionarioHashmap = preencheIntegerFuncionarioHashMap();

                for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                    dataInicio_ = (Date) dataInicio.clone();
                    dataFim_ = (Date) dataFim.clone();
                    pontoAcessoTotal.setDataInicio(dataInicio_);
                    pontoAcessoTotal.setDataFim(dataFim_);
                    Integer matricula = it.next();
                    pontoAcessoTotal.setCod_funcionario(matricula);
                    pontoAcessoTotal.consultaDiasSemMsgErro();
                    boolean isVazio = pontoAcessoTotal.imprimirPorDepartamento(pontoAcessoTotal);
                    if (!isVazio) {
                        matriculasMergeList.add(matricula);
                    } else {
                        matriculasSemRegistroList.add(matricula);
                    }
                }
                //Preparado para se verificar quem não tem ponto
                /**  if (!matriculasSemRegistroList.isEmpty()) {
                Banco b = new Banco();
                List<Funcionario> funcionarioList_  = getFuncionariosHashMap(matriculasSemRegistroList, matriculaFuncionarioHashmap);
                b.insertRelatorioFuncionarioSemEscala(funcionarioList_);
                Impressao.geraRelatorioListaFuncionarioSemRegistrodePonto(funcionarioList_.size());
                }*/
                if (!matriculasMergeList.isEmpty()) {
                    //    MergePDF.gerarPDFUnicoHorizontalComListaDeFuncinarioSemRegistro(matriculasMergeList, "FrequenciaMensal",!matriculasSemRegistroList.isEmpty());
                    MergePDF.gerarPDFUnicoDepartamento(matriculasMergeList);
                    Impressao.popUpRelatorio();
                    Metodos.remover(new File(Metodos.getPath() + "tempSE"));
                    Metodos.criarPasta(Metodos.getPath() + "tempSE");
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                    Metodos.setLogInfo("Impressão Relatório mensal sem escala - Departamento: " + Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem).replaceAll("&nbsp;", "") + " Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));
                } else {
                    FacesMessage msgErro = new FacesMessage("Não existem relatórios a serem gerados");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            } else {
                FacesMessage msgErro = new FacesMessage("Entrada de dados inválidos");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

        } catch (Exception e) {
            FacesMessage msgErro = new FacesMessage("Sobrecarga de dados. Entre em contato com o administrador. \n"
                    + "Erro:" + e);
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
    }

    public void consultaFuncionario() {
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList = banco.consultaFuncionario2(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
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
        cod_funcionarioGestorHashMap = b.getcod_funcionarioSubordinacaoDepartamento(Integer.parseInt(departamentoSelecionado));
        List<SelectItem> funcionarioList_ = new ArrayList<SelectItem>();

        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario_ = it.next();
            if (!funcionario_.getValue().toString().equals("-1")) {
                Boolean criterioRegime = isFuncionarioDentroCriterioRegime(funcionario_);
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario_);

                if (criterioGestor && criterioRegime) {
                    funcionarioList_.add(funcionario_);
                }
            } else {
                funcionarioList_.add(funcionario_);
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

    private Map<Integer, Funcionario> preencheIntegerFuncionarioHashMap() {
        Map<Integer, Funcionario> matriculaFuncionarioHashmap = new HashMap<Integer, Funcionario>();
        Banco banco = new Banco();
        matriculaFuncionarioHashmap = banco.consultaDadosFuncionario(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
        return matriculaFuncionarioHashmap;
    }

    private List<Funcionario> getFuncionariosHashMap(List<Integer> matriculasSemRegistroList,
            Map<Integer, Funcionario> matriculaFuncionarioHashmap) {

        List<Funcionario> funcionarioList_ = new ArrayList<Funcionario>();
        for (Iterator<Integer> it = matriculasSemRegistroList.iterator(); it.hasNext();) {
            Integer matricula = it.next();
            Funcionario f = new Funcionario();
            f = matriculaFuncionarioHashmap.get(matricula);
            funcionarioList_.add(f);
        }
        return funcionarioList_;
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

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Integer getEscalaSelecionada() {
        return escalaSelecionada;
    }

    public void setEscalaSelecionada(Integer escalaSelecionada) {
        this.escalaSelecionada = escalaSelecionada;
    }

    public List<SelectItem> getEscalasSelecItem() {
        return escalasSelecItem;
    }

    public void setEscalasSelecItem(List<SelectItem> escalasSelecItem) {
        this.escalasSelecItem = escalasSelecItem;
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
}
