/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import Funcionario.Funcionario;
import Metodos.Metodos;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ppccardoso
 */
public class RelatorioCatracasBean implements Serializable {

    private String departamentoSelecionado;
    private List<SelectItem> departamentosSelecItem;
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

    public RelatorioCatracasBean() {
        inicializarAtributos();
        departamentosSelecItem = new ArrayList<SelectItem>();
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getUltimoDiaMes();
        consultaDepartamentos();
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

    public static Date getPrimeiroDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        return data;
    }

    public static Date getUltimoDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date data = calendar.getTime();
        return data;
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

    public void imprimirRelatorio() throws FileNotFoundException, Exception {
        if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            Date dataInicio_ = (Date) dataInicio.clone();
            Date dataFim_ = (Date) dataFim.clone();

            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            List<Integer> matriculasList = new ArrayList<Integer>();
            List<Integer> matriculasMergeList = new ArrayList<Integer>();
            List<Funcionario> funcionariosSemEscalaList = new ArrayList<Funcionario>();
            
            matriculasList = getFuncionarios();
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                dataInicio_ = (Date) dataInicio.clone();
                dataFim_ = (Date) dataFim.clone();
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio_);
                c.setDataFim(dataFim_);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                c.consultaDiasSemMsgErro();
                boolean isVazio = c.geraRelatorioCatracaPorDepartamento();
                if (!isVazio) {
                    matriculasMergeList.add(userid);
                } else {
                    Funcionario funcionario = c.getFuncionarioSelecionado();
                    funcionariosSemEscalaList.add(funcionario);
                }

            }
            if (!funcionariosSemEscalaList.isEmpty()) {
                Banco b = new Banco();
                b.insertRelatorioFuncionarioSemEscala(funcionariosSemEscalaList);
                Impressao.geraRelatorioListaFuncionarioSemEscala(funcionariosSemEscalaList.size());
            }
            if (!matriculasMergeList.isEmpty()) {
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                Metodos.setLogInfo("Imprime Relatorio Ponto e Catraca - Departamento: " + Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem).replaceAll("&nbsp;", "") + " Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));
                MergePDF.gerarPDFUnicoHorizontalComListaDeFuncinarioSemEscala(matriculasMergeList, "RelatorioCatracaPorDepartamento", !funcionariosSemEscalaList.isEmpty());
                Impressao.popUpRelatorio("RelatorioCatracaPorDepartamento");
                Metodos.remover(new File(Metodos.getPath() + "temp"));
                Metodos.criarPasta(Metodos.getPath() + "temp");
            } else {
                FacesMessage msgErro = new FacesMessage("Não existem relatórios a serem gerados");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Entrada de dados inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
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

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public HashMap<Integer, Integer> getCod_funcionarioGestorHashMap() {
        return cod_funcionarioGestorHashMap;
    }

    public void setCod_funcionarioGestorHashMap(HashMap<Integer, Integer> cod_funcionarioGestorHashMap) {
        this.cod_funcionarioGestorHashMap = cod_funcionarioGestorHashMap;
    }

    public HashMap<Integer, Integer> getCod_funcionarioRegimeHashMap() {
        return cod_funcionarioRegimeHashMap;
    }

    public void setCod_funcionarioRegimeHashMap(HashMap<Integer, Integer> cod_funcionarioRegimeHashMap) {
        this.cod_funcionarioRegimeHashMap = cod_funcionarioRegimeHashMap;
    }

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public Integer getRegimeSelecionadoOpcaoFiltroFuncionario() {
        return regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public void setRegimeSelecionadoOpcaoFiltroFuncionario(Integer regimeSelecionadoOpcaoFiltroFuncionario) {
        this.regimeSelecionadoOpcaoFiltroFuncionario = regimeSelecionadoOpcaoFiltroFuncionario;
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

    public List<SelectItem> getRegimeOpcaoFiltroFuncionarioList() {
        return regimeOpcaoFiltroFuncionarioList;
    }

    public void setRegimeOpcaoFiltroFuncionarioList(List<SelectItem> regimeOpcaoFiltroFuncionarioList) {
        this.regimeOpcaoFiltroFuncionarioList = regimeOpcaoFiltroFuncionarioList;
    }
}
