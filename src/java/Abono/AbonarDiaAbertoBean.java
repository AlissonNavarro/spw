/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import ConsultaPonto.Jornada;
import ConsultaPonto.RegistroAdicionado;
import ConsultaPonto.SituacaoPonto;
import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class AbonarDiaAbertoBean implements Serializable {

    private Integer cod_funcionario;
    private String dataStr;
    private String nomeFuncionario;
    private String departamento;
    private String justificativa;
    private String detalheJustificativa;
    private String hora;
    private Date data;
    private List<String> horaAbonoList;
    private List<SelectItem> horaSelectAbonoList;
    private List<SelectItem> justificativaList;
    private UsuarioBean usuarioBean;
    private List<TabelaHorario> tabelaHorarioList;
    private DiaComEscala diaAcesso;
    private ConsultaFrequenciaComEscalaBean c;
    private List<SituacaoPonto> situacaoList;
    private List<TabelaFuncionario> tabelaFuncionario;
    private List<SolicitacaoAbono> solicitacaoAbono;

    public AbonarDiaAbertoBean() {

        String cod_funcionario_ = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_funcionario");
        cod_funcionario = Integer.parseInt(cod_funcionario_);
        nomeFuncionario = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nomeFuncionario");
        departamento = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("departamento");
        dataStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dataStr");
        String pontosAbertos = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pontosAbertos");

        tabelaFuncionario = new ArrayList<TabelaFuncionario>();

        tabelaFuncionario.add(new TabelaFuncionario(nomeFuncionario, departamento));

        Banco banco = new Banco();
        justificativaList = new ArrayList<SelectItem>();
        justificativaList = banco.consultaJustificativasAbono();

        banco = new Banco();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));

        String[] horasAbonoArray = pontosAbertos.split(" - ");
        horaSelectAbonoList = new ArrayList<SelectItem>();

        for (int i = 0; i < horasAbonoArray.length; i++) {
            String horaAbono = horasAbonoArray[i];
            horaSelectAbonoList.add(new SelectItem(horaAbono, horaAbono));
        }
        
        solicitacaoAbono = new ArrayList<SolicitacaoAbono>();
        ConsultaPonto.Banco b = new ConsultaPonto.Banco();
        solicitacaoAbono = b.consultaSolicitacaoAbonoFuncionario(cod_funcionario, dataStr);
        getJornadas();
        listagemPonto();

    }

    public String enviar() {
        Banco banco = new Banco();

        Integer justificaticaInt = Integer.parseInt(justificativa);

        if (justificaticaInt != -1) {
            Long registroLong;
            Long dataLong = getData().getTime();
            boolean ok = false;
            boolean isDescricaoObrigatoria = banco.isDescricaoObrigatoria(justificaticaInt);
            if (!(isDescricaoObrigatoria && detalheJustificativa.equals(""))) {
                for (int i = 0; i < horaAbonoList.size(); i++) {
                    registroLong = horaToLong(horaAbonoList.get(i));
                    if (registroLong != null) {
                        ok = banco.abonarDiaEmAberto(cod_funcionario,
                                new Date(dataLong + registroLong),
                                justificaticaInt, detalheJustificativa, usuarioBean.getUsuario().getLogin());
                    } else {
                        FacesMessage msgErro = new FacesMessage("Hora inválida!");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        break;
                    }
                }
                if (horaAbonoList.size() != 0 && ok) {
                    String just = Metodos.buscaRotulo(justificativa, justificativaList);
                    dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
                    Metodos.setLogInfo("Abonar funcionário: \"" + nomeFuncionario + "\" Data: " + dataStr + " Justificativa: \"" + just + "\"");
                    return "navegarAbono";
                } else if (horaAbonoList.size() == 0) {
                    FacesMessage msgErro = new FacesMessage("Você deve selecionar pelo menos um registro a ser abonado!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    return "";
                } else {
                    FacesMessage msgErro = new FacesMessage("Erro ao inserir o abono!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    return "";
                }
            } else {
                FacesMessage msgErro = new FacesMessage("A justificativa escolhida requer uma descrição!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                return "";
            }

        } else {
            FacesMessage msgErro = new FacesMessage("Categoria Inválida!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return "";
        }
    }

    public void addRegistro() {
        if (horaToLong(hora) != null) {
            SelectItem selectItem = new SelectItem(hora, hora);
            horaSelectAbonoList.add(selectItem);
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

    private void getJornadas() {

        tabelaHorarioList = new ArrayList<TabelaHorario>();
        GregorianCalendar diaHora = new GregorianCalendar();
        Locale.setDefault(new Locale("pt", "BR"));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dt = null;
        try {
            dt = df.parse(dataStr);
        } catch (ParseException ex) {
            //   Logger.getLogger(DetalheDiaBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        diaHora.setTimeInMillis(dt.getTime());

        c = new ConsultaFrequenciaComEscalaBean("");
        c.setDataInicio(dt);
        c.setDataFim(dt);

        c.setCod_funcionario(cod_funcionario);
        c.consultaDiasSemMsgErro();
        diaAcesso = new DiaComEscala();
        diaAcesso = c.getDiaAcessoMap().get(dataStr);

        List<Jornada> jornadaDiaList = c.getJornadaList();

        TabelaHorario tabelaHorario;

        tabelaHorario = new TabelaHorario();
        int diaSemanaNumber = diaHora.get(Calendar.DAY_OF_WEEK) - 1;

        switch (diaSemanaNumber) {
            case 1:
                tabelaHorario.setDiaSemana("Segunda-Feira");
                break;
            case 2:
                tabelaHorario.setDiaSemana("Terça-Feira");
                break;
            case 3:
                tabelaHorario.setDiaSemana("Quarta-Feira");
                break;
            case 4:
                tabelaHorario.setDiaSemana("Quinta-Feira");
                break;
            case 5:
                tabelaHorario.setDiaSemana("Sexta-Feira");
                break;
            case 6:
                tabelaHorario.setDiaSemana("Sábado");
                break;
            case 7:
                tabelaHorario.setDiaSemana("Domingo");
                break;
        }

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

        String faixaEntrada1;
        String faixaSaida1;

        if (jornadaDiaList.size() == 2) {
            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {
                } else {
                    faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada1("");
                    tabelaHorario.setFaixaEntrada1("");
                    tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                    tabelaHorario.setFaixaSaida1(faixaSaida1);
                }
            } else {
                faixaEntrada1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada()) + ")";

                faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";

                tabelaHorario.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()));
                tabelaHorario.setFaixaEntrada1(faixaEntrada1);
                tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                tabelaHorario.setFaixaSaida1(faixaSaida1);
            }

            if (jornadaDiaList.get(1).getTemVirada()) {
                if (jornadaDiaList.get(1).getSoEntrada()) {

                    String faixaEntrada2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()));
                    tabelaHorario.setSaida2("");
                    tabelaHorario.setFaixaEntrada2(faixaEntrada2);
                    tabelaHorario.setFaixaSaida2("");
                } else {

                    String faixaSaida2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada2("");
                    tabelaHorario.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()));
                    tabelaHorario.setFaixaEntrada2("");
                    tabelaHorario.setFaixaSaida2(faixaSaida2);
                }
            } else {
                String faixaEntrada2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada()) + ")";
                String faixaSaida2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada()) + ")";
                tabelaHorario.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()));
                tabelaHorario.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()));
                tabelaHorario.setFaixaEntrada2(faixaEntrada2);
                tabelaHorario.setFaixaSaida2(faixaSaida2);
            }
        } else {

            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {

                    faixaEntrada1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()));
                    tabelaHorario.setSaida1("");
                    tabelaHorario.setFaixaEntrada1(faixaEntrada1);
                    tabelaHorario.setFaixaSaida1("");

                } else {
                    faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada1("");
                    tabelaHorario.setFaixaEntrada1("");
                    tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                    tabelaHorario.setFaixaSaida1(faixaSaida1);
                }
            } else {
                faixaEntrada1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada()) + ")";

                faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";

                tabelaHorario.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()));
                tabelaHorario.setFaixaEntrada1(faixaEntrada1);
                tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                tabelaHorario.setFaixaSaida1(faixaSaida1);
            }

        }

        tabelaHorarioList.add(tabelaHorario);
    }

    private List<Jornada> getJornada(ConsultaFrequenciaComEscalaBean consultaFrequenciaComEscalaBean, Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String diaAtual = sdf.format(data.getTime());
        for (Iterator<DiaComEscala> it = consultaFrequenciaComEscalaBean.getDiasList().iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            String diaPesquisa = sdf.format(diaComEscala.getDia());
            if (diaAtual.equals(diaPesquisa)) {
                return diaComEscala.getJornadaList();
            }
        }
        return null;
    }

    private void listagemPonto() {
        situacaoList = new ArrayList<SituacaoPonto>();
        if (diaAcesso != null) {
            situacaoList = ordenarSituacao(diaAcesso.getSituacaoPonto());
        }
        List<SituacaoPonto> situacaoListTemp = new ArrayList<SituacaoPonto>();

        for (Iterator<SituacaoPonto> it = situacaoList.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            RegistroAdicionado ra = containsRegistro(situacaoPonto.getHoraPontoObj().getPonto().getTime(), c.getPontosAdicionados());
            if (ra == null) {
                situacaoListTemp.add(situacaoPonto);
            } else {
                String hora = situacaoPonto.getHoraPonto().replace("*", "");
                situacaoPonto.setHoraPonto(hora + "*");
                situacaoPonto.setJustificativaAbono("(" + ra.getCategoriaJustificativa() + ")");
                situacaoListTemp.add(situacaoPonto);
            }
        }
        getJornadas();
        situacaoList = situacaoListTemp;
    }

    private List<SituacaoPonto> ordenarSituacao(List<SituacaoPonto> situacaoList) {
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

    private RegistroAdicionado containsRegistro(Long ponto, List<RegistroAdicionado> registroAdicionado) {

        Timestamp registroTime = new Timestamp(ponto);

        RegistroAdicionado registroAdicionadoReturn = null;

        for (Iterator<RegistroAdicionado> it = registroAdicionado.iterator(); it.hasNext();) {
            RegistroAdicionado registroAdicionado_ = it.next();
            if (registroAdicionado_.getCheckTime().equals(registroTime)) {
                registroAdicionadoReturn = registroAdicionado_;
            }
        }
        return registroAdicionadoReturn;
    }

    private String horaSegundos(String hora) {
        return hora += ":00";
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public List<SelectItem> getJustificativaList() {
        return justificativaList;
    }

    public void setJustificativaList(List<SelectItem> justificativaList) {
        this.justificativaList = justificativaList;
    }

    public List<SelectItem> getHoraSelectAbonoList() {
        return horaSelectAbonoList;
    }

    public void setHoraSelectAbonoList(List<SelectItem> horaSelectAbonoList) {
        this.horaSelectAbonoList = horaSelectAbonoList;
    }

    public String getDetalheJustificativa() {
        return detalheJustificativa;
    }

    public void setDetalheJustificativa(String detalheJustificativa) {
        this.detalheJustificativa = detalheJustificativa;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public List<String> getHoraAbonoList() {
        return horaAbonoList;
    }

    public void setHoraAbonoList(List<String> horaAbonoList) {
        this.horaAbonoList = horaAbonoList;
    }

    public Date getData() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            data = sdf.parse(dataStr);
            return data;
        } catch (ParseException ex) {
            return null;
        }
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public List<TabelaHorario> getTabelaHorarioList() {
        return tabelaHorarioList;
    }

    public void setTabelaHorarioList(List<TabelaHorario> tabelaHorarioList) {
        this.tabelaHorarioList = tabelaHorarioList;
    }

    public ConsultaFrequenciaComEscalaBean getC() {
        return c;
    }

    public void setC(ConsultaFrequenciaComEscalaBean c) {
        this.c = c;
    }

    public DiaComEscala getDiaAcesso() {
        return diaAcesso;
    }

    public void setDiaAcesso(DiaComEscala diaAcesso) {
        this.diaAcesso = diaAcesso;
    }

    public List<SituacaoPonto> getSituacaoList() {
        return situacaoList;
    }

    public void setSituacaoList(List<SituacaoPonto> situacaoList) {
        this.situacaoList = situacaoList;
    }

    public List<TabelaFuncionario> getTabelaFuncionario() {
        return tabelaFuncionario;
    }

    public void setTabelaFuncionario(List<TabelaFuncionario> tabelaFuncionario) {
        this.tabelaFuncionario = tabelaFuncionario;
    }

    public List<SolicitacaoAbono> getSolicitacaoAbono() {
        return solicitacaoAbono;
    }

    public void setSolicitacaoAbono(List<SolicitacaoAbono> solicitacaoAbono) {
        this.solicitacaoAbono = solicitacaoAbono;
    }

    public class TabelaFuncionario implements Serializable {

        private String nome;
        private String departamento;

        public TabelaFuncionario(String nome, String departamento) {
            this.nome = nome;
            this.departamento = departamento;
        }

        public String getDepartamento() {
            return departamento;
        }

        public void setDepartamento(String departamento) {
            this.departamento = departamento;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }
    

    public class TabelaHorario implements Serializable {

        private String diaSemana;
        private String entrada1;
        private String saida1;
        private String entrada2;
        private String saida2;
        private String faixaEntrada1;
        private String faixaSaida1;
        private String faixaEntrada2;
        private String faixaSaida2;

        public String getDiaSemana() {
            return diaSemana;
        }

        public void setDiaSemana(String diaSemana) {
            this.diaSemana = diaSemana;
        }

        public String getEntrada1() {
            return entrada1;
        }

        public void setEntrada1(String entrada1) {
            this.entrada1 = entrada1;
        }

        public String getEntrada2() {
            return entrada2;
        }

        public void setEntrada2(String entrada2) {
            this.entrada2 = entrada2;
        }

        public String getSaida1() {
            return saida1;
        }

        public void setSaida1(String saida1) {
            this.saida1 = saida1;
        }

        public String getSaida2() {
            return saida2;
        }

        public void setSaida2(String saida2) {
            this.saida2 = saida2;
        }

        public String getFaixaEntrada1() {
            return faixaEntrada1;
        }

        public void setFaixaEntrada1(String faixaEntrada1) {
            this.faixaEntrada1 = faixaEntrada1;
        }

        public String getFaixaEntrada2() {
            return faixaEntrada2;
        }

        public void setFaixaEntrada2(String faixaEntrada2) {
            this.faixaEntrada2 = faixaEntrada2;
        }

        public String getFaixaSaida1() {
            return faixaSaida1;
        }

        public void setFaixaSaida1(String faixaSaida1) {
            this.faixaSaida1 = faixaSaida1;
        }

        public String getFaixaSaida2() {
            return faixaSaida2;
        }

        public void setFaixaSaida2(String faixaSaida2) {
            this.faixaSaida2 = faixaSaida2;
        }
    }
}
