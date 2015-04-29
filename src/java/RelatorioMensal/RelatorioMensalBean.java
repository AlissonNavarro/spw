package RelatorioMensal;

import Metodos.Metodos;
import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

public class RelatorioMensalBean implements Serializable {

    private String departamentoSelecionado;
    private List<SelectItem> departamentosSelecItem;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private Boolean incluirSubSetores;
    private List<SelectItem> funcionarioList;
    //Filtro por regime
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    //Filtro por gestor
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    //Filtro por cargo
    private Integer cargoSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioCargoHashMap;
    private List<SelectItem> cargoOpcaoFiltroFuncionarioList;
    private String abaCorrente;

    public RelatorioMensalBean() {
        inicializarAtributos();
        departamentosSelecItem = new ArrayList<>();
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getUltimoDiaMes();
        consultaDepartamentos();
        abaCorrente = "tab1";
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

    public void imprimirRelatorio() throws FileNotFoundException, Exception {
        Banco banco = new Banco();
        int tipoRelatorio = banco.consultaTipoRelatorio();
        System.out.println("relatorio tipo: "+tipoRelatorio);
        switch (tipoRelatorio) {
            case 1:
                //mensal com escala
                imprimirDepartamentoPortariaResumo();
                break;
            case 2:
                imprimirDepartamentoPortariaResumoSemSaldo();
                break;
            case 3:
                imprimirDepartamentoPortaria1510();
                break;
        }
    }

    public void imprimirDepartamento() throws FileNotFoundException, JRException, Exception {

        if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            c.setDataInicio(dataInicio);
            c.setDataFim(dataFim);
            List<Integer> matriculasList = new ArrayList<>();
            List<Integer> matriculasMergeList = new ArrayList<>();
            Banco banco = new Banco();
            matriculasList = banco.consultaFuncionarioDepartamento(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio);
                c.setDataFim(dataFim);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                System.out.println("Matrícula funcionario: " + userid);
                //c.consultaDiasSemMsgErro();
                c.consultaDias();
                boolean isVazio = c.imprimirPorDepartamento(c);
                if (!isVazio) {
                    matriculasMergeList.add(userid);
                }
            }
            if (!matriculasMergeList.isEmpty()) {
                MergePDF.gerarPDFUnico(matriculasMergeList, "RelatorioFrequencia");
                Impressao.popUpRelatorio();
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

    public void imprimirDepartamentoPortariaResumo() throws FileNotFoundException, JRException, Exception {

        if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

            ConsultaFrequenciaComEscalaBean c;
            //List<Integer> matriculasList = new ArrayList<>();
            List<Integer> matriculasMergeList = new ArrayList<Integer>();
            List<Funcionario> funcionariosSemEscalaList = new ArrayList<>();
            SelectItem func;
            Integer userid = -1;
            //matriculasList = getFuncionarios();
            //for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
            for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
                func = it.next();
                userid = (Integer) func.getValue();
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio);
                c.setDataFim(dataFim);
                c.setCod_funcionario(userid);
                c.consultaDiasSemMsgErro();
                boolean isVazio = c.geraRelatorioPorDepartamento();
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
                Metodos.setLogInfo("Imprime Relatorio Espelho de Ponto - Departamento: " + Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem).replaceAll("&nbsp;", "") + " Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));
                MergePDF.gerarPDFUnicoHorizontalComListaDeFuncinarioSemEscala(matriculasMergeList, "RelatorioPorDepartamento", !funcionariosSemEscalaList.isEmpty());
                Impressao.popUpRelatorio("RelatorioPorDepartamento");
                Metodos.remover(new File(Metodos.getPath() + "temp"));
                Metodos.criarPasta(Metodos.getPath() + "temp");
            } else {
                System.out.println("RelatorioMensalBean.imprimirDepartamentoPortariaResumo:  matriculasMergeList vazio");
                FacesMessage msgErro = new FacesMessage("Não existem relatórios a serem gerados");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Entrada de dados inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void imprimirDepartamentoPortariaResumoSemSaldo() throws FileNotFoundException, JRException, Exception {

        if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            c.setDataInicio(dataInicio);
            c.setDataFim(dataFim);
            List<Integer> matriculasList = new ArrayList<>();
            List<Integer> matriculasMergeList = new ArrayList<>();
            List<Funcionario> funcionariosSemEscalaList = new ArrayList<>();

            matriculasList = getFuncionarios();
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio);
                c.setDataFim(dataFim);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                System.out.println("Matrícula funcionario: " + userid);
                c.consultaDiasSemMsgErro();
                boolean isVazio = c.geraRelatorioSemSaldoPorDepartamento();
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
                Metodos.setLogInfo("Imprime Relatorio Espelho de Ponto - Departamento: " + Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem).replaceAll("&nbsp;", "") + " Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));
                MergePDF.gerarPDFUnicoHorizontalComListaDeFuncinarioSemEscala(matriculasMergeList, "RelatorioPorDepartamento", !funcionariosSemEscalaList.isEmpty());
                Impressao.popUpRelatorio("RelatorioPorDepartamento");
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

    public void imprimirDepartamentoPortaria1510() throws FileNotFoundException, JRException, Exception {

        if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            Date dataInicio_ = (Date) dataInicio.clone();
            Date dataFim_ = (Date) dataFim.clone();

            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            List<Integer> matriculasList = new ArrayList<>();
            List<Integer> matriculasMergeList = new ArrayList<>();
            Banco banco = new Banco();
            matriculasList = banco.consultaFuncionarioDepartamento(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                dataInicio_ = (Date) dataInicio.clone();
                dataFim_ = (Date) dataFim.clone();
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio_);
                c.setDataFim(dataFim_);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                System.out.println("Matrícula funcionario: " + userid);
                c.consultaDiasSemMsgErro();
                boolean isVazio = c.geraRelatorio1510PorDepartamento();
                if (!isVazio) {
                    matriculasMergeList.add(userid);
                }
            }
            if (!matriculasMergeList.isEmpty()) {
                String dept = Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem);
                Metodos.setLogInfo("Imprime Relatorio Espelho de Ponto - Departamento: " + dept.replaceAll("&nbsp;", ""));
                MergePDF.gerarPDFUnicoHorizontal(matriculasMergeList, "RelatorioPorDepartamento");
                Impressao.popUpRelatorio("RelatorioPorDepartamento");
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

    public void imprimirDepartamentoPortaria() throws FileNotFoundException, JRException, Exception {

        if (!(departamentoSelecionado == null || dataInicio == null || dataFim == null)) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            c.setDataInicio(dataInicio);
            c.setDataFim(dataFim);
            List<Integer> matriculasList = new ArrayList<>();
            List<Integer> matriculasMergeList = new ArrayList<>();
            Banco banco = new Banco();
            matriculasList = banco.consultaFuncionarioDepartamento(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio);
                c.setDataFim(dataFim);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                System.out.println("Matrícula funcionario: " + userid);
                c.consultaDiasSemMsgErro();
                boolean isVazio = c.geraRelatorioPorDepartamento();
                if (!isVazio) {
                    matriculasMergeList.add(userid);
                }
            }
            if (!matriculasMergeList.isEmpty()) {
                String dept = Metodos.buscaRotulo(departamentoSelecionado, departamentosSelecItem);
                Metodos.setLogInfo("Imprime Relatorio Espelho de Ponto - Departamento: " + dept.replaceAll("&nbsp;", ""));
                MergePDF.gerarPDFUnicoHorizontal(matriculasMergeList, "RelatorioPorDepartamento");
                Impressao.popUpRelatorio("RelatorioPorDepartamento");
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

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));

    }

    public void consultaFuncionario() {
        Banco banco = new Banco();
        funcionarioList = new ArrayList<>();
        funcionarioList = banco.consultaFuncionario2(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
        funcionarioList = filtrarFuncionario();
        
    }

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        cargoSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario  = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        iniciarOpcoesFiltro();
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
        List<SelectItem> tipoFiltroGestorList = new ArrayList<>();
        tipoFiltroGestorList.add(new SelectItem(-1, "TODOS"));
        tipoFiltroGestorList.add(new SelectItem(0, "GESTORES SUBORDINADOS AO DEPARTAMENTO"));
        tipoFiltroGestorList.add(new SelectItem(1, "GESTORES SUBORDINADOS DIRETAMENTE AO DEPARTAMENTO"));

        return tipoFiltroGestorList;
    }

    private List<SelectItem> filtrarFuncionario() {
        Banco b = new Banco();
        cod_funcionarioGestorHashMap = b.getcod_funcionarioSubordinacaoDepartamento(Integer.parseInt(departamentoSelecionado));
        List<SelectItem> funcionarioList_ = new ArrayList<>();
        for (SelectItem funcionario_ : funcionarioList) {
            if (!funcionario_.getValue().toString().equals("-1")) {
                Boolean criterioRegime = isFuncionarioDentroCriterioRegime(funcionario_);
                Boolean criterioCargo = isFuncionarioDentroCriterioCargo(funcionario_);
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario_);

                if (criterioGestor && criterioRegime && criterioCargo) {
                    funcionarioList_.add(funcionario_);
                }
            } else {
                funcionarioList_.add(funcionario_);
            }
        }
        return funcionarioList_;
    }

    private Boolean isFuncionarioDentroCriterioRegime(SelectItem funcionarioSelectItem) {
        Integer regime = cod_funcionarioRegimeHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        Boolean criterioRegime = (regimeSelecionadoOpcaoFiltroFuncionario == -1) || regime.equals(regimeSelecionadoOpcaoFiltroFuncionario);
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


    private List<Integer> getFuncionarios() {
        List<Integer> funcionarioList_ = new ArrayList<>();
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

    public List<SelectItem> getCargoOpcaoFiltroFuncionarioList() {
        return cargoOpcaoFiltroFuncionarioList;
    }

    public void setCargoOpcaoFiltroFuncionarioList(List<SelectItem> cargoOpcaoFiltroFuncionarioList) {
        this.cargoOpcaoFiltroFuncionarioList = cargoOpcaoFiltroFuncionarioList;
    }

    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }
    
     public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        abaCorrente = tab;
    }

}
