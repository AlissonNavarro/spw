/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class ConsultaDiaEmAbertoBean {

    private Integer departamento;
    private String permissao;
    private List<SelectItem> departamentolist;
    private List<SelectItem> funcionarioList;
    private List<SelectItem> justificativaList;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private Boolean incluirSubSetores;
    private static UsuarioBean usuarioBean;
    private Boolean isAdministradorVisivel;
    private List<DiaEmAberto> diaEmAbertoList;
    private String filtro;
    private Boolean isShowPesquisa;
    private Integer page = 1;
    

    public ConsultaDiaEmAbertoBean() {
        departamentolist = new ArrayList<SelectItem>();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        permissao = usuarioBean.getUsuario().getPermissao();
        
        consultaDepartamentos();
        filtro = "";
        isShowPesquisa = false;
        justificativaList = new ArrayList<SelectItem>();
        consultaJustificativas();
    }
    
    public void rotinasDeAtualização() {
        consultaJustificativas();
    }

    public void consultaDepartamento(String permissao) {
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        departamentolist = banco.consultaDepartamentoHierarquico(codigo_funcionario, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, Integer.parseInt(permissao));
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentolist = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void consultaJustificativas() {
        Banco banco = new Banco();
        justificativaList = new ArrayList<SelectItem>();
        justificativaList = banco.consultaJustificativasAbonoDescricaoNaoObrigatoria();
    }

    public void consultar() {

        diaEmAbertoList = new ArrayList<DiaEmAberto>();
        page = 1;
        if (!(departamento != null && departamento == -1) && valideData()) {
            isShowPesquisa = true;

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            Date dataInicio_ = (Date) dataInicio.clone();
            Date dataFim_ = (Date) dataFim.clone();

            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            List<Integer> matriculasList = new ArrayList<Integer>();
            diaEmAbertoList = new ArrayList<DiaEmAberto>();
            Banco banco = new Banco();
            
            matriculasList = banco.consultaFuncionarioDepartamento(departamento, Integer.parseInt(permissao), filtro, incluirSubSetores, usuarioBean.getUsuario().getLogin());

            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                dataInicio_ = (Date) dataInicio.clone();
                dataFim_ = (Date) dataFim.clone();
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio_);
                c.setDataFim(dataFim_);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                String departamentoStr = banco.consultaDepartamentoNomeByUserid(userid);
                c.setDepartamentoSrt(departamentoStr);

                c.consultaDiasSemMsgErro();
                List<DiaComEscala> diasList = c.getDiasList();
                for (Iterator<DiaComEscala> it1 = diasList.iterator(); it1.hasNext();) {
                    DiaComEscala diaComEscala = it1.next();
                    if (diaComEscala.getDefinicao().equals("Irregular")) {
                        String pontosEmAberto = getPontosPendentes(diaComEscala);
                        DiaEmAberto diaEmAberto = new DiaEmAberto(c.getCod_funcionario(), c.getNome(), c.getDepartamentoSrt(), diaComEscala.getDia(), pontosEmAberto);
                        if (banco.consultaHorarioIrregularSolicitado(c.getCod_funcionario(), diaComEscala.getDia().getTime())) {
                            diaEmAberto.setIsDeletavel(true);
                            diaEmAberto.setDescricao(banco.getDescricaoSolicitacao(c.getCod_funcionario(), diaComEscala.getDia().getTime()));
                        }
                        if (banco.getDiasEmAbertoAceitos(diaEmAberto.getCod_funcionario(), diaComEscala.getDia()) || 
                                banco.getDiasEmAbertoNegados(diaEmAberto.getCod_funcionario(), diaComEscala.getDia())){ 
                            }else{
                                diaEmAbertoList.add(diaEmAberto);
                        }


                    }
                }
            }
            if (diaEmAbertoList.isEmpty()) {
                FacesMessage msgErro = new FacesMessage("Nenhum ponto em aberto foi encontrado!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco.fecharConexao();
        } else {
            if (departamento == -1) {
                FacesMessage msgErro = new FacesMessage("Selecione um departamento!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Selecione um intervalo de datas válido!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            isShowPesquisa = false;
        }
    }

    public void consultarSemMSg() {

        diaEmAbertoList = new ArrayList<DiaEmAberto>();
        page = 1;

        if (!(departamento != null && departamento == -1) && valideData()) {
            isShowPesquisa = true;

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            Date dataInicio_ = (Date) dataInicio.clone();
            Date dataFim_ = (Date) dataFim.clone();

            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            List<Integer> matriculasList = new ArrayList<Integer>();
            diaEmAbertoList = new ArrayList<DiaEmAberto>();
            Banco banco = new Banco();
            matriculasList = banco.consultaFuncionarioDepartamento(departamento, Integer.parseInt(permissao), filtro, incluirSubSetores, usuarioBean.getUsuario().getLogin());
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                dataInicio_ = (Date) dataInicio.clone();
                dataFim_ = (Date) dataFim.clone();
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio_);
                c.setDataFim(dataFim_);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                String departamentoStr = banco.consultaDepartamentoNomeByUserid(userid);
                c.setDepartamentoSrt(departamentoStr);

                c.consultaDiasSemMsgErro();
                List<DiaComEscala> diasList = c.getDiasList();
                for (Iterator<DiaComEscala> it1 = diasList.iterator(); it1.hasNext();) {
                    DiaComEscala diaComEscala = it1.next();
                    if (diaComEscala.getDefinicao().equals("Irregular")) {
                        String pontosEmAberto = getPontosPendentes(diaComEscala);
                        DiaEmAberto diaEmAberto = new DiaEmAberto(c.getCod_funcionario(), c.getNome(), c.getDepartamentoSrt(), diaComEscala.getDia(), pontosEmAberto);
                        if (banco.consultaHorarioIrregularSolicitado(c.getCod_funcionario(), diaComEscala.getDia().getTime())) {
                            diaEmAberto.setIsDeletavel(true);
                            diaEmAberto.setDescricao(banco.getDescricaoSolicitacao(c.getCod_funcionario(), diaComEscala.getDia().getTime()));
                        }
                        if (banco.getDiasEmAbertoAceitos(diaEmAberto.getCod_funcionario(), diaComEscala.getDia()) || 
                                banco.getDiasEmAbertoNegados(diaEmAberto.getCod_funcionario(), diaComEscala.getDia())){ 
                            }else{
                                diaEmAbertoList.add(diaEmAberto);
                        }

                    }
                }
            }
            banco.fecharConexao();
        }
    }
    

    public void abonarEmMassa() {
        List<DiaEmAberto> diasemAbertoSelecionadosList = new ArrayList<DiaEmAberto>();
        for (Iterator<DiaEmAberto> it = diaEmAbertoList.iterator(); it.hasNext();) {
            DiaEmAberto diaEmAberto = it.next();
            if (diaEmAberto.getMarked()) {    
                String[] horasAbonoArray = diaEmAberto.getPontosAbertos().split(" - ");
                for (int i = 0; i < horasAbonoArray.length; i++) {
                    System.out.println("***");
                    String horaAbono = horasAbonoArray[i];
                    System.out.println(horaAbono);
                    Long registroLong = horaToLong(horaAbono);
                    Date date = zeraData(diaEmAberto.getData());
                    System.out.println(date.toString());
                    Long dataLong = date.getTime();
                    DiaEmAberto diaEmAbertoCopy = new DiaEmAberto();
                    diaEmAbertoCopy = diaEmAberto.copy();
                    diaEmAbertoCopy.setData(new Date(registroLong + dataLong));
                    diasemAbertoSelecionadosList.add(diaEmAbertoCopy);
                    System.out.println("***");
                    System.out.println();
                }
            }
        }
        if (diasemAbertoSelecionadosList.isEmpty()) {
            FacesMessage msgErro = new FacesMessage("Não existem pontos a serem abonados!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            Banco banco = new Banco();
            banco.abonarDiaEmAbertoEmMassa(diasemAbertoSelecionadosList, usuarioBean.getUsuario().getLogin());
            consultarSemMSg();
            FacesMessage msgErro = new FacesMessage("Abonos realizados com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            //dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
            String dataStr = "";
            String funcStr = "";

            for (int i = 0; i < diasemAbertoSelecionadosList.size(); i++) {
                DiaEmAberto diaEmAberto = diasemAbertoSelecionadosList.get(i);
                if (!(diaEmAberto.getDataStr().equals(dataStr)) || !(diaEmAberto.getNomeFuncionario().equals(funcStr))) {
                    dataStr = diaEmAberto.getDataStr();
                    funcStr = diaEmAberto.getNomeFuncionario();
                    Metodos.setLogInfo("Abono dia em aberto - Data: " + diaEmAberto.getDataStr()
                            + " Funcionário: " + diaEmAberto.getNomeFuncionario() + " Justificativa: \""
                            + Metodos.buscaRotulo(diaEmAberto.getJustificativa(), justificativaList));
                }
            }


        }
    }

    private Long horaToLong(String hora) {
        Long saida = null;
        try {
            hora += ":00";
            Time time = Time.valueOf(hora);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }
        return saida;
    }

    private Date zeraData(Date dataEntrada) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_ = sdf.format(dataEntrada.getTime());
        Date date = new Date();
        try {
            date = sdf.parse(data_);
        } catch (ParseException ex) {
            System.out.println("Abono: zeraData: "+ex);
            //Logger.getLogger(ConsultaDiaEmAbertoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    private String getPontosPendentes(DiaComEscala diaComEscala) {

        String retorno = "";
        String entradaTurno1 = diaComEscala.getEntrada_1().getRegistro();
        String entradaTurno2 = diaComEscala.getEntrada_2().getRegistro();
        String saidaTurno1 = diaComEscala.getSaida_1().getRegistro();
        String saidaTurno2 = diaComEscala.getSaida_2().getRegistro();

        boolean entrada1Boolean = entradaTurno1.equals("");
        boolean saida1Boolean = saidaTurno1.equals("");
        boolean entrada2Boolean = entradaTurno2.equals("");
        boolean saida2Boolean = saidaTurno2.equals("");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


        if (diaComEscala.getJornadaList().size() == 2) {
            if (diaComEscala.getJornadaList().get(0).getTemVirada()) {
                if (diaComEscala.getJornadaList().get(0).getSoEntrada()) {
                    if (entrada1Boolean) {
                        retorno += sdf.format(diaComEscala.getJornadaList().get(0).getInicioJornada()) + " - ";
                    }
                } else if (saida1Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(0).getTerminioJornada()) + " - ";
                }
            } else {
                if (entrada1Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(0).getInicioJornada()) + " - ";
                }
                if (saida1Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(0).getTerminioJornada()) + " - ";
                }
            }

            if (diaComEscala.getJornadaList().get(1).getTemVirada()) {
                if (diaComEscala.getJornadaList().get(1).getSoEntrada()) {
                    if (entrada2Boolean) {
                        retorno += sdf.format(diaComEscala.getJornadaList().get(1).getInicioJornada()) + " - ";
                    }
                } else if (saida2Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(1).getTerminioJornada()) + " - ";
                }
            } else {
                if (entrada2Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(1).getInicioJornada()) + " - ";
                }
                if (saida2Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(1).getTerminioJornada()) + " - ";
                }
            }

        } else {
            if (diaComEscala.getJornadaList().get(0).getTemVirada()) {
                if (diaComEscala.getJornadaList().get(0).getSoEntrada()) {
                    if (entrada1Boolean) {
                        retorno += sdf.format(diaComEscala.getJornadaList().get(0).getInicioJornada()) + " - ";
                    }
                } else if (diaComEscala.getJornadaList().get(0).getSoSaida()) {
                    if (((saida1Boolean))) {
                        retorno += sdf.format(diaComEscala.getJornadaList().get(0).getTerminioJornada()) + " - ";
                    }
                }
            } else {
                if (entrada1Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(0).getInicioJornada()) + " - ";
                }
                if (saida1Boolean) {
                    retorno += sdf.format(diaComEscala.getJornadaList().get(0).getTerminioJornada()) + " - ";
                }
            }
        }
        return retorno.substring(0, retorno.length() - 3);
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

    private Boolean valideData() {
        if ((dataInicio == null || dataFim == null) || (dataInicio.getTime() > dataFim.getTime())) {
            return false;
        } else {
            return true;
        }
    }

    public String navegaAbono() {
        return "navegarAbonarDiaAberto";
    }

    private static Date getHoje() {
        return new Date();
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

    public static UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public static void setUsuarioBean(UsuarioBean usuarioBean) {
        ConsultaDiaEmAbertoBean.usuarioBean = usuarioBean;
    }

    public Boolean getIsAdministradorVisivel() {
        return isAdministradorVisivel;
    }

    public void setIsAdministradorVisivel(Boolean isAdministradorVisivel) {
        this.isAdministradorVisivel = isAdministradorVisivel;
    }

    public List<DiaEmAberto> getDiaEmAbertoList() {
        return diaEmAbertoList;
    }

    public void setDiaEmAbertoList(List<DiaEmAberto> diaEmAbertoList) {
        this.diaEmAbertoList = diaEmAbertoList;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Boolean getIsShowPesquisa() {
        return isShowPesquisa;
    }

    public void setIsShowPesquisa(Boolean isShowPesquisa) {
        this.isShowPesquisa = isShowPesquisa;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<SelectItem> getJustificativaList() {
        return justificativaList;
    }

    public void setJustificativaList(List<SelectItem> justificativaList) {
        this.justificativaList = justificativaList;
    }
}
