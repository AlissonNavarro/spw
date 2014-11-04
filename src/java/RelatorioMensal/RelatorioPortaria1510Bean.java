/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import ConsultaPonto.Jornada;
import ConsultaPonto.SituacaoPonto;
import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.SQLException;
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
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author amsgama
 */
public class RelatorioPortaria1510Bean implements Serializable {

    private Integer departamento;
    private Integer cod_funcionario;
    private Integer razaoSocial;
    private Integer cod_justificativa_selecionada;
    private List<SelectItem> departamentolist;
    private List<SelectItem> funcionarioList;
    private Boolean incluirSubSetores;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private Boolean isAdministradorVisivel;
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

    public RelatorioPortaria1510Bean() {

        inicializarAtributos();
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        objLocale = new Locale("pt", "BR");
        consultaDepartamento();
        Locale.setDefault(new Locale("pt", "BR"));
        inicializaAtributos();
    }

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

    public void inicializaAtributos() {
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
    }

    public void consultaFuncionario() throws SQLException {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        if (departamento != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario2(departamento, incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }
      
            funcionarioList = filtrarFuncionario();
       
    }

    /*
    public void consultaFuncionario() throws SQLException {
    Banco banco = new Banco();
    funcionarioList = new ArrayList<SelectItem>();
    funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));

    funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);

    }
     *
     */
    public void geraRelatorio() {
        ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

        Date dataInicio_ = (Date) dataInicio.clone();
        Date dataFim_ = (Date) dataFim.clone();

        c.setDataInicio(dataInicio_);
        c.setDataFim(dataFim_);
        c.setCod_funcionario(cod_funcionario);
        c.consultaDiasSemMsgErro();
        List<DiaComEscala> diasComEscalaList = c.getDiasList();
        RelatorioPortaria1510 relatorioPortaria;
        List<RelatorioPortaria1510> relatorioPortariaList = new ArrayList<RelatorioPortaria1510>();
        List<HorarioContratual> horarioContratualList = new ArrayList<HorarioContratual>();
        Integer i = 1;
        for (Iterator<DiaComEscala> it = diasComEscalaList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            if (!diaComEscala.getIsFeriado()) {
                relatorioPortaria = new RelatorioPortaria1510();
                //Setando o dia
                Date data = diaComEscala.getDia();
                String dia = getDia(data);
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
                relatorioPortariaList.add(relatorioPortaria);
            }
        }

        if (!diasComEscalaList.isEmpty()) {
            Banco banco = new Banco();
            banco.insertRelatorioPortaria1510(horarioContratualList, relatorioPortariaList);
            RelatorioPortaria1510Cabecalho relatorioPortaria1510Cabecalho = banco.consultaRelatorioPortaria1510Cabecalho(cod_funcionario, razaoSocial);

            try {
                Impressao.geraRelatorioPortaria1510(relatorioPortaria1510Cabecalho.getEmpregador(), relatorioPortaria1510Cabecalho.getEndereco(),
                        relatorioPortaria1510Cabecalho.getEmpregado(), relatorioPortaria1510Cabecalho.getAdmissao(), dataInicio, dataFim);
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                Metodos.setLogInfo("Imprime Relatorio Espelho de Ponto - Funcionário: " + Metodos.buscaRotulo(cod_funcionario.toString(), funcionarioList) + " Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));
            } catch (JRException ex) {
                System.out.println("RelatorioMensal: geraRelatorio 1: "+ex);
                //Logger.getLogger(RelatorioPortaria1510Bean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                System.out.println("RelatorioMensal: geraRelatorio 2: "+ex);
                //Logger.getLogger(RelatorioPortaria1510Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Nao existe dados a serem gerados para este funcionário no intervalo de tempo selecionado!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    private String getDia(Date dia) {
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

    private String[] getEntradaESaidas(DiaComEscala diaComEscala) {

        List<SituacaoPonto> situacaoPontos = diaComEscala.getSituacaoPonto();
        String[] entradaESaidaList = new String[6];

        for (Iterator<SituacaoPonto> it = situacaoPontos.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            String ponto = situacaoPonto.getSituacao();

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
        for (int i = 0; i < entradaESaidaList.length; i++) {
            if (entradaESaidaList[i] == null) {
                entradaESaidaList[i] = "";
            }
        }

        return entradaESaidaList;
    }

    private HorarioContratual getHorarioContratual(DiaComEscala diaComEscala) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        HorarioContratual horarioContratual = new HorarioContratual();
        List<Jornada> jornadaDiaList = diaComEscala.getJornadaList();
        if (jornadaDiaList.size() == 2) {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            horarioContratual.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornadaL()));
            horarioContratual.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornadaL()));

        } else if (jornadaDiaList.size() == 1) {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            if(jornadaDiaList.get(0).getInicioDescanso1() != null)
                horarioContratual.setEntradaD1(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()));
            if(jornadaDiaList.get(0).getFimDescanso1() != null)
                horarioContratual.setSaidaD1(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()));
            if(jornadaDiaList.get(0).getInicioDescanso2() != null)
                horarioContratual.setEntradaD2(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()));
            if(jornadaDiaList.get(0).getFimDescanso2() != null)
                horarioContratual.setSaidaD2(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()));
        }
        return horarioContratual;
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
        }
        return data;
    }

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        cargoSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
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
        cod_funcionarioCargoHashMap= b.getcod_funcionarioCargo();

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
            if (!funcionario_.getValue().toString().equals("-1")&&!funcionario_.getValue().toString().equals("0")) {
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

    private static Date getHoje() {
        return new Date();
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public Integer getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(Integer razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public Integer getCod_justificativa_selecionada() {
        return cod_justificativa_selecionada;
    }

    public void setCod_justificativa_selecionada(Integer cod_justificativa_selecionada) {
        this.cod_justificativa_selecionada = cod_justificativa_selecionada;
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

    public Boolean getIsAdministradorVisivel() {
        return isAdministradorVisivel;
    }

    public void setIsAdministradorVisivel(Boolean isAdministradorVisivel) {
        this.isAdministradorVisivel = isAdministradorVisivel;
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
    
}
