package Funcionario;

import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class FuncionarioBean implements Serializable {

    private List<SelectItem> departamentolist;
    private Boolean sucetivelAFeriado;
    //private List<SelectItem> sexosList;
    private List<SelectItem> cargosList;
    private List<SelectItem> departamentolistDestino;
    private List<SelectItem> funcionarioList;
    private Locale objLocale;
    private Boolean isAdministradorVisivel;
    private Integer cod_funcionario;
    private Integer departamento;
    private Integer departamentoDestino;
    private Boolean incluirSubSetores;
    private Funcionario funcionario;
    private Funcionario newFuncionario;
    //Filtro por regime
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    //Filtro por gestor
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    private List<SelectItem> regimelist;
    private String ip;
    //Filtro por cargo
    private Integer cargoSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioCargoHashMap;
    private List<SelectItem> cargoOpcaoFiltroFuncionarioList;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    

    public FuncionarioBean() {
        inicializarAtributos();
        departamentolist = new ArrayList<SelectItem>();
        funcionarioList = new ArrayList<SelectItem>();
//        sexosList = new ArrayList<SelectItem>();
        cargosList = new ArrayList<SelectItem>();
        departamentolistDestino = new ArrayList<SelectItem>();
        regimelist = new ArrayList<SelectItem>();



        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        departamento = null;
        objLocale = new Locale("pt", "BR");
        funcionario = new Funcionario();
        
        Banco b = new Banco();
        newFuncionario = new Funcionario(b.getNextPIN());
        
        consultaDepartamentoDestino();
        consultaDepartamento();
        //       consultaDepartamentos();
        consultaCargosList();
//        consultaSexosList();
        consultaRegimeList();
        Locale.setDefault(new Locale("pt", "BR"));
    }

    public void reConstroi() {

        departamentolist = new ArrayList<SelectItem>();
        cargosList = new ArrayList<SelectItem>();
        departamentolistDestino = new ArrayList<SelectItem>();
        consultaDepartamentoDestino();

        consultaDepartamento();

        consultaCargosList();
        consultaRegimeList();
    }

    public void createNewFunc() {
        Banco b = new Banco();
        newFuncionario = new Funcionario(b.getNextPIN());
        newFuncionario.setIsAtivo(true);
        System.out.println("novo funcionário");
    }

    public void addNewFunc() {
        Banco b = new Banco();
        Integer flag = b.cadastrarNovoFuncionario(newFuncionario);
        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Funcionário cadastrado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag == 1) {
            Integer flag2 = b.cadastrarNovoFuncionarioItdentity(newFuncionario);
            if (flag2 == 0) {
                FacesMessage msgErro = new FacesMessage("Funcionário cadastrado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Erro ao cadastrar funcionário!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
    }

/*    public void consultaSexosList() {
        sexosList = new ArrayList<SelectItem>();
        sexosList.add(new SelectItem(null, "Não Especificado"));
        sexosList.add(new SelectItem(1, "Masculino"));
        sexosList.add(new SelectItem(2, "Feminino"));
    }*/

    public void consultaCargosList() {
        Banco banco = new Banco();
        cargosList = new ArrayList<SelectItem>();
        cargosList = banco.consultaCargosList();
    }

    public void consultaRegimeList() {
        Banco banco = new Banco();
        regimelist = new ArrayList<SelectItem>();
        regimelist = banco.consultaRegimeList();
    }

    public void consultaDepartamento() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        Integer dept = usuarioBean.getUsuario().getDepartamento();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        String permissao = usuarioBean.getUsuario().getPermissao();
        departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentolist = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    /*
     public void consultaDepartamento() {
     UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
     String permissao = usuarioBean.getUsuario().getPermissao();
     Banco banco = new Banco();
     departamentolist = new ArrayList<SelectItem>();
     Integer dept = usuarioBean.getUsuario().getDepartamento();
     Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
     departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
     isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
     }
     *
     */
    public void inicializaAtributos() {
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
    }

    public void consultaDepartamentoDestino() {
        Banco banco = new Banco();
        departamentolistDestino = new ArrayList<SelectItem>();
        departamentolistDestino = banco.consultaDepartamentoOrdernado();
    }

    public void resetADLink() {
        Banco banco = new Banco();
        banco.removoLinkAD(funcionario.getADUsername());
        FacesMessage msgErro = new FacesMessage("Ligação do AD desfeita com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public void zerarSenha() {
        Banco banco = new Banco();
        banco.zerarSenhaUsuario(funcionario.getFuncionarioId());
        FacesMessage msgErro = new FacesMessage("Senha formatada com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
        //Metodos.setLogInfo("Formatar senha -  Funcionário: " + funcionario.getNome());
    }

    public void excluirFuncionario() throws SQLException {
        Banco banco = new Banco();
        banco.excluirFuncionario(funcionario.getFuncionarioId());
        funcionario = new Funcionario();
        consultaFuncionario();
        FacesMessage msgErro = new FacesMessage("Funcionário excluído com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public void insertFingerPrint() {
        Banco banco = new Banco();
        banco.prepararCapturaDigital(funcionario.getFuncionarioId());
    }

    /*   public void consultaFuncionario() throws SQLException {
     UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
     Banco banco = new Banco();
     String permissao = usuarioBean.getUsuario().getPermissao();
     funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
     }*/
    public void consultaFuncionario() throws SQLException {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        if (departamento != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }

        funcionarioList = filtrarFuncionario();

    }

    public void consultaDetalhesFuncionario() {
        Banco banco = new Banco();
        funcionario = banco.consultaDetalhesFuncionario(cod_funcionario);
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void salvarFuncionarioTransferencia() {
        Banco banco = new Banco();
        Integer flag = banco.salvarTransferencia(departamentoDestino, cod_funcionario);
        if (flag == 0) {

            FacesMessage msgErro = new FacesMessage("Selecione um Departamento Válido");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            funcionario.setCod_dept(departamentoDestino);
            FacesMessage msgErro = new FacesMessage("Funcionário transferido com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            //Metodos.setLogInfo("Transferir Funcionario - Funcionario: " + funcionario.getNome() + " Novo departamento: " + Metodos.buscaRotulo(departamentoDestino.toString(), departamentolistDestino).replace("&nbsp;", ""));
        }
        //    infoMessage = flag == 0 ? "Selecione um Departamento Válido" : "";
    }

    public void salvarFuncionarioAlteracoes() {
        Banco banco = new Banco();
        //String cargo = (funcionario.getCargo() == null || funcionario.getCargo() == 0) ? "Não Especificado" : banco.consultaCargo(funcionario.getCargo());

        Boolean flag = banco.salvarAlteracoes(funcionario);
        if (flag == true) {
            FacesMessage msgErro = new FacesMessage("Funcionário editado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            //Metodos.setLogInfo("Editar Funcionário - Funcionário: " + funcionario.getNome() + " CPF: " + funcionario.getCpf() + " Cargo: " + cargo + " Crachá: " + funcionario.getCracha() + " Departamento: " + Metodos.buscaRotulo(funcionario.getCod_dept().toString(), departamentolist).replace("&nbsp;", "") + " Matrícula: " + funcionario.getMatricula() + " PIS: " + funcionario.getPIS());
        } else {
            FacesMessage msgErro = new FacesMessage("Falha ao editar o funcionário!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        cargoSelecionadoOpcaoFiltroFuncionario = -1;
        cargoOpcaoFiltroFuncionarioList = new ArrayList<>();
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        iniciarOpcoesFiltro();
        cod_funcionario = null;
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

        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario_ = it.next();
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

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
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

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public List<SelectItem> getDepartamentolistDestino() {
        return departamentolistDestino;
    }

    public void setDepartamentolistDestino(List<SelectItem> departamentolistDestino) {
        this.departamentolistDestino = departamentolistDestino;
    }

    public Integer getDepartamentoDestino() {
        return departamentoDestino;
    }

    public void setDepartamentoDestino(Integer departamentoDestino) {
        this.departamentoDestino = departamentoDestino;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<SelectItem> getCargosList() {
        return cargosList;
    }

    public void setCargosList(List<SelectItem> cargosList) {
        this.cargosList = cargosList;
    }

/*    public List<SelectItem> getSexosList() {
        return sexosList;
    }

    public void setSexosList(List<SelectItem> sexosList) {
        this.sexosList = sexosList;
    }*/

    public Boolean getSucetivelAFeriado() {
        return sucetivelAFeriado;
    }

    public void setSucetivelAFeriado(Boolean sucetivelAFeriado) {
        this.sucetivelAFeriado = sucetivelAFeriado;
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

    public List<SelectItem> getRegimelist() {
        return regimelist;
    }

    public void setRegimelist(List<SelectItem> regimelist) {
        this.regimelist = regimelist;
    }

    public Funcionario getNewFuncionario() {
        return newFuncionario;
    }

    public void setNewFuncionario(Funcionario newFuncionario) {
        this.newFuncionario = newFuncionario;
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
    
    
}
