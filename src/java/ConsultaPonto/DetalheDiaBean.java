/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import Abono.SolicitacaoAbono;
import Usuario.UsuarioBean;
import ConsultaPonto.TabelaHorario;
import java.awt.event.ActionListener;
import java.io.Serializable;
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
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class DetalheDiaBean implements Serializable {

    private ConsultaFrequenciaComEscalaBean pontoAcessoDataGridBean;
    private UsuarioBean usuarioBean;
    private List<TabelaFuncionario> tabelaFuncionario;
    private List<TabelaHorario> tabelaHorarioList;
    private List<String> horaSelecionadasAbonoList;
    private List<SituacaoPonto> situacaoList;
    private List<SelectItem> horaAAbonarCheckBoxList;
    private List<SolicitacaoAbono> solicitacaoAbono;
    private DiaComEscala diaAcesso;
    private String descricaoInputText;
    private String dataSelecionada;
    private String horaAdicionada;
    private String[] horaSeleciinadaAbonoArray;
    private Integer codigo_funcionario;
    private List<Jornada> jornadaDiaList;
    private Boolean podeAbonar;
    private List<SelectItem> tipoLiberacaoList;

    public DetalheDiaBean() {

        horaSelecionadasAbonoList = new ArrayList<String>();
        horaSeleciinadaAbonoArray = new String[4];
        horaAAbonarCheckBoxList = new ArrayList<SelectItem>();

        pontoAcessoDataGridBean = (ConsultaFrequenciaComEscalaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("consultaFrequenciaComEscalaBean");

        usuarioBean = (UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean");

        podeAbonar = temPermissaoParaAbonar();

        tabelaFuncionario = new ArrayList<TabelaFuncionario>();
        tabelaFuncionario.add(new TabelaFuncionario(pontoAcessoDataGridBean.getNome(), pontoAcessoDataGridBean.getDepartamentoSrt()));

        tabelaHorarioList = new ArrayList<TabelaHorario>();

        dataSelecionada = pontoAcessoDataGridBean.getDataSelecionada();
        diaAcesso = new DiaComEscala();
        diaAcesso = pontoAcessoDataGridBean.getDiaAcessoMap().get(dataSelecionada);

        codigo_funcionario = pontoAcessoDataGridBean.getCod_funcionario();

        solicitacaoAbono = new ArrayList<SolicitacaoAbono>();
        Banco banco = new Banco();
        solicitacaoAbono = banco.consultaSolicitacaoAbonoFuncionario(codigo_funcionario, dataSelecionada);

        situacaoList = new ArrayList<SituacaoPonto>();
        if (diaAcesso != null) {
            situacaoList = ordenarSituacao(diaAcesso.getSituacaoPonto());
        }
        List<SituacaoPonto> situacaoListTemp = new ArrayList<SituacaoPonto>();

        for (Iterator<SituacaoPonto> it = situacaoList.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            RegistroAdicionado ra = containsRegistro(situacaoPonto.getHoraPontoObj().getPonto().getTime(), pontoAcessoDataGridBean.getPontosAdicionados());
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
        setTipoLiberacao(tabelaHorarioList);

    }

    public void construtor() {
        horaSelecionadasAbonoList = new ArrayList<String>();
        horaSeleciinadaAbonoArray = new String[4];
        horaAAbonarCheckBoxList = new ArrayList<SelectItem>();

        pontoAcessoDataGridBean = (ConsultaFrequenciaComEscalaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("consultaFrequenciaComEscalaBean");
        pontoAcessoDataGridBean.consultaDiasSemMsgErro();

        usuarioBean = (UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean");

        podeAbonar = temPermissaoParaAbonar();

        tabelaFuncionario = new ArrayList<TabelaFuncionario>();
        tabelaFuncionario.add(new TabelaFuncionario(pontoAcessoDataGridBean.getNome(), pontoAcessoDataGridBean.getDepartamentoSrt()));

        tabelaHorarioList = new ArrayList<TabelaHorario>();

        dataSelecionada = pontoAcessoDataGridBean.getDataSelecionada();
        diaAcesso = new DiaComEscala();
        diaAcesso = pontoAcessoDataGridBean.getDiaAcessoMap().get(dataSelecionada);

        codigo_funcionario = pontoAcessoDataGridBean.getCod_funcionario();

        solicitacaoAbono = new ArrayList<SolicitacaoAbono>();
        Banco banco = new Banco();
        solicitacaoAbono = banco.consultaSolicitacaoAbonoFuncionario(codigo_funcionario, dataSelecionada);

        situacaoList = new ArrayList<SituacaoPonto>();
        if (diaAcesso != null) {
            situacaoList = ordenarSituacao(diaAcesso.getSituacaoPonto());
        }
        List<SituacaoPonto> situacaoListTemp = new ArrayList<SituacaoPonto>();

        for (Iterator<SituacaoPonto> it = situacaoList.iterator(); it.hasNext();) {
            SituacaoPonto situacaoPonto = it.next();
            RegistroAdicionado ra = containsRegistro(situacaoPonto.getHoraPontoObj().getPonto().getTime(), pontoAcessoDataGridBean.getPontosAdicionados());
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
        setTipoLiberacao(tabelaHorarioList);

    }

    private void setTipoLiberacao(List<TabelaHorario> tabelaHorarioList) {

        tipoLiberacaoList = new ArrayList<SelectItem>();
        String entrada1 = tabelaHorarioList.get(0).getEntrada1();
        String saida1 = tabelaHorarioList.get(0).getSaida1();
        String entrada2 = tabelaHorarioList.get(0).getEntrada2();
        String saida2 = tabelaHorarioList.get(0).getSaida2();
        String entrada3 = tabelaHorarioList.get(0).getEntrada3();
        String faixa1Desc1Entrada = tabelaHorarioList.get(0).getFaixa1Desc1Entrada();
        String faixa1Desc1Saida = tabelaHorarioList.get(0).getFaixa1Desc1Saida();
        String faixa1Desc2Entrada = tabelaHorarioList.get(0).getFaixa1Desc2Entrada();
        String faixa1Desc2Saida = tabelaHorarioList.get(0).getFaixa1Desc2Saida();
        String faixa2Desc1Entrada = tabelaHorarioList.get(0).getFaixa2Desc1Entrada();
        String faixa2Desc1Saida = tabelaHorarioList.get(0).getFaixa2Desc1Saida();
        String faixa2Desc2Entrada = tabelaHorarioList.get(0).getFaixa2Desc2Entrada();
        String faixa2Desc2Saida = tabelaHorarioList.get(0).getFaixa2Desc2Saida();
        String interJornada1Entrada = tabelaHorarioList.get(0).getInterJornada1Entrada();
        String interJornada1Saida = tabelaHorarioList.get(0).getInterJornada1Saida();
        String interJornada2Entrada = tabelaHorarioList.get(0).getInterJornada2Entrada();
        String interJornada2Saida = tabelaHorarioList.get(0).getInterJornada2Saida();

        tipoLiberacaoList.add(new SelectItem(0, "Sem alteração"));
        if (entrada1 != null && !entrada1.equals("")) {
            tipoLiberacaoList.add(new SelectItem(1, "Turno 1 - Entrada"));
        }

        if (saida1 != null && !saida1.equals("")) {
            tipoLiberacaoList.add(new SelectItem(2, "Turno 1 - Saída"));
        }

        if (entrada2 != null && !entrada2.equals("")) {
            tipoLiberacaoList.add(new SelectItem(3, "Turno 2 - Entrada"));
        }

        if (saida2 != null && !saida2.equals("")) {
            tipoLiberacaoList.add(new SelectItem(4, "Turno 2 - Saída"));
        }

        if (entrada3 != null && !entrada3.equals("")) {
            tipoLiberacaoList.add(new SelectItem(5, "Turno 3 - Entrada"));
        }

        //liberação dos descansos
        if (faixa1Desc1Entrada != null && !faixa1Desc1Entrada.equals("")) {
            tipoLiberacaoList.add(new SelectItem(6, "1º turno 1º Descanso - Entrada"));
        }

        if (faixa1Desc1Saida != null && !faixa1Desc1Saida.equals("")) {
            tipoLiberacaoList.add(new SelectItem(7, "1º turno 1º Descanso - Saida"));
        }

        if (faixa1Desc2Entrada != null && !faixa1Desc2Entrada.equals("")) {
            tipoLiberacaoList.add(new SelectItem(8, "1º turno 2º Descanso - Entrada"));
        }

        if (faixa1Desc2Saida != null && !faixa1Desc2Saida.equals("")) {
            tipoLiberacaoList.add(new SelectItem(9, "1º turno 2º Descanso - Saida"));
        }

        if (faixa2Desc1Entrada != null && !faixa2Desc1Entrada.equals("")) {
            tipoLiberacaoList.add(new SelectItem(10, "2º turno 1º Descanso - Entrada"));
        }

        if (faixa2Desc1Saida != null && !faixa2Desc1Saida.equals("")) {
            tipoLiberacaoList.add(new SelectItem(11, "2º turno 1º Descanso - Saida"));
        }

        if (faixa2Desc2Entrada != null && !faixa2Desc2Entrada.equals("")) {
            tipoLiberacaoList.add(new SelectItem(12, "2º turno 2º Descanso - Entrada"));
        }

        if (faixa2Desc2Saida != null && !faixa2Desc2Saida.equals("")) {
            tipoLiberacaoList.add(new SelectItem(13, "2º turno 2º Descanso - Saida"));
        }

        if (interJornada1Entrada != null && !interJornada1Entrada.equals("")) {
            tipoLiberacaoList.add(new SelectItem(14, "1º turno Interjornada - Entrada"));
        }

        if (interJornada1Saida != null && !interJornada1Saida.equals("")) {
            tipoLiberacaoList.add(new SelectItem(15, "1º turno Interjornada - Saida"));
        }

        if (interJornada2Entrada != null && !interJornada2Entrada.equals("")) {
            tipoLiberacaoList.add(new SelectItem(16, "2º turno Interjornada - Entrada"));
        }

        if (interJornada2Saida != null && !interJornada2Saida.equals("")) {
            tipoLiberacaoList.add(new SelectItem(17, "2º turno Interjornada - Saida"));
        }
        /*System.out.println("size: "+tipoLiberacaoList.size());
         for (int i = 0; i < tipoLiberacaoList.size(); i++){
         System.out.println("posição: "+ i +" - situação: "+tipoLiberacaoList.get(i).getLabel());
         }*/
    }

    private void getJornadas() {

        GregorianCalendar diaHora = new GregorianCalendar();
        Locale.setDefault(new Locale("pt", "BR"));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dt = null;
        try {
            dt = df.parse(dataSelecionada);
        } catch (ParseException ex) {
            System.out.println("ConsultaPonto: getJornadas: " + ex);
            //Logger.getLogger(DetalheDiaBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        diaHora.setTimeInMillis(dt.getTime());
        Date date = new Date(dt.getTime());

        jornadaDiaList = new ArrayList<Jornada>();
        jornadaDiaList = getJornada(pontoAcessoDataGridBean, date);

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
            //Duas jornadas
            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {
                } else {
                    faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";
                    tabelaHorario.setEntrada1("");
                    tabelaHorario.setFaixaEntrada1("");
                    tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                    tabelaHorario.setFaixaSaida1(faixaSaida1);
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(0));
                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(0).getTerminioJornada())));
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
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()), sdfHora.format(jornadaDiaList.get(0).getInicioJornada())));
                addDescansoParaAbonar(sdfHora, jornadaDiaList.get(0));
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(0).getTerminioJornada())));
            }

            if (jornadaDiaList.get(0).getToleranciaDescanso() != null) {
                if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                    tabelaHorario.setFaixa1Desc1Entrada(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()));
                }
                if (jornadaDiaList.get(0).getFimDescanso1() != null) {
                    tabelaHorario.setFaixa1Desc1Saida(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()));
                }
                if (jornadaDiaList.get(0).getInicioIntrajornada() != null) {
                    tabelaHorario.setInterJornada1Entrada(sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada()));
                }
                if (jornadaDiaList.get(0).getFimIntrajornada() != null) {
                    tabelaHorario.setInterJornada1Saida(sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada()));
                }
                if (jornadaDiaList.get(0).getInicioDescanso2() != null) {
                    tabelaHorario.setFaixa1Desc2Entrada(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()));
                }
                if (jornadaDiaList.get(0).getFimDescanso2() != null) {
                    tabelaHorario.setFaixa1Desc2Saida(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()));
                }
            }

            if (jornadaDiaList.get(1).getTemVirada()) {
                if (jornadaDiaList.get(1).getSoEntrada()) {
                    String faixaEntrada2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()));
                    tabelaHorario.setSaida2("");
                    tabelaHorario.setFaixaEntrada2(faixaEntrada2);
                    tabelaHorario.setFaixaSaida2("");

                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()), sdfHora.format(jornadaDiaList.get(1).getInicioJornada())));
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(1));
                } else {
                    String faixaSaida2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada2("");
                    tabelaHorario.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()));
                    tabelaHorario.setFaixaEntrada2("");
                    tabelaHorario.setFaixaSaida2(faixaSaida2);
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(1));
                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(1).getTerminioJornada())));
                }
            } else {
                if (jornadaDiaList.get(1).getCheckInLimiteAntecipada() != null) {
                    String faixaEntrada2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada()) + ")";
                    String faixaSaida2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada()) + ")";
                    tabelaHorario.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()));
                    tabelaHorario.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()));
                    tabelaHorario.setFaixaEntrada2(faixaEntrada2);
                    tabelaHorario.setFaixaSaida2(faixaSaida2);
                }

                if (jornadaDiaList.get(1).getInicioJornada() != null) {
                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()), sdfHora.format(jornadaDiaList.get(1).getInicioJornada())));
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(1));
                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(1).getTerminioJornada())));
                }
            }

            if (jornadaDiaList.get(1).getToleranciaDescanso() != null) {
                if (jornadaDiaList.get(1).getInicioDescanso1() != null) {
                    tabelaHorario.setFaixa2Desc1Entrada(sdfHora.format(jornadaDiaList.get(1).getInicioDescanso1()));
                }
                if (jornadaDiaList.get(1).getFimDescanso1() != null) {
                    tabelaHorario.setFaixa2Desc1Saida(sdfHora.format(jornadaDiaList.get(1).getFimDescanso1()));
                }
                if (jornadaDiaList.get(1).getInicioIntrajornada() != null) {
                    tabelaHorario.setInterJornada2Entrada(sdfHora.format(jornadaDiaList.get(1).getInicioIntrajornada()));
                }
                if (jornadaDiaList.get(1).getFimIntrajornada() != null) {
                    tabelaHorario.setInterJornada2Saida(sdfHora.format(jornadaDiaList.get(1).getFimIntrajornada()));
                }
                if (jornadaDiaList.get(1).getInicioDescanso2() != null) {
                    tabelaHorario.setFaixa2Desc2Entrada(sdfHora.format(jornadaDiaList.get(1).getInicioDescanso2()));
                }
                if (jornadaDiaList.get(1).getFimDescanso2() != null) {
                    tabelaHorario.setFaixa2Desc2Saida(sdfHora.format(jornadaDiaList.get(1).getFimDescanso2()));
                }
            }
        } else if (jornadaDiaList.size() == 1) {
            //Uma jornada
            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {
                    faixaEntrada1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckInLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()));
                    tabelaHorario.setSaida1("");
                    tabelaHorario.setFaixaEntrada1(faixaEntrada1);
                    tabelaHorario.setFaixaSaida1("");

                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()), sdfHora.format(jornadaDiaList.get(0).getInicioJornada())));
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(0));

                } else {
                    faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada1("");
                    tabelaHorario.setFaixaEntrada1("");
                    tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                    tabelaHorario.setFaixaSaida1(faixaSaida1);
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(0));
                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(0).getTerminioJornada())));
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

                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getInicioJornada()), sdfHora.format(jornadaDiaList.get(0).getInicioJornada())));
                addDescansoParaAbonar(sdfHora, jornadaDiaList.get(0));
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(0).getTerminioJornada())));
            }

            if (jornadaDiaList.get(0).getToleranciaDescanso() != null) {
                if (jornadaDiaList.get(0).getInicioDescanso1() != null) {
                    tabelaHorario.setFaixa1Desc1Entrada(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()));
                }
                if (jornadaDiaList.get(0).getFimDescanso1() != null) {
                    tabelaHorario.setFaixa1Desc1Saida(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()));
                }
                if (jornadaDiaList.get(0).getInicioIntrajornada() != null) {
                    tabelaHorario.setInterJornada1Entrada(sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada()));
                }
                if (jornadaDiaList.get(0).getFimIntrajornada() != null) {
                    tabelaHorario.setInterJornada1Saida(sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada()));
                }
                if (jornadaDiaList.get(0).getInicioDescanso2() != null) {
                    tabelaHorario.setFaixa1Desc2Entrada(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()));
                }
                if (jornadaDiaList.get(0).getFimDescanso2() != null) {
                    tabelaHorario.setFaixa1Desc2Saida(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()));
                }
            }

        } else {
            //possivelmente Três jornadas
            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {
                } else {
                    faixaSaida1 = "(" + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(0).getCheckOutLimiteAtrasada()) + ")";
                    tabelaHorario.setEntrada1("");
                    tabelaHorario.setFaixaEntrada1("");
                    tabelaHorario.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()));
                    tabelaHorario.setFaixaSaida1(faixaSaida1);
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(0));
                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(0).getTerminioJornada())));
                }
            }
            if (jornadaDiaList.get(1).getTemVirada()) {
            } else {
                String faixaEntrada2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(1).getCheckInLimiteAtrasada()) + ")";
                String faixaSaida2 = "(" + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAntecipada()) + " - "
                        + sdfHora.format(jornadaDiaList.get(1).getCheckOutLimiteAtrasada()) + ")";
                tabelaHorario.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()));
                tabelaHorario.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()));
                tabelaHorario.setFaixaEntrada2(faixaEntrada2);
                tabelaHorario.setFaixaSaida2(faixaSaida2);
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(1).getInicioJornada()), sdfHora.format(jornadaDiaList.get(1).getInicioJornada())));
                addDescansoParaAbonar(sdfHora, jornadaDiaList.get(1));
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(1).getTerminioJornada()), sdfHora.format(jornadaDiaList.get(1).getTerminioJornada())));
            }
            if (jornadaDiaList.get(2).getTemVirada()) {
                if (jornadaDiaList.get(2).getSoEntrada()) {
                    String faixaEntrada3 = "(" + sdfHora.format(jornadaDiaList.get(2).getCheckInLimiteAntecipada()) + " - "
                            + sdfHora.format(jornadaDiaList.get(2).getCheckInLimiteAtrasada()) + ")";

                    tabelaHorario.setEntrada3(sdfHora.format(jornadaDiaList.get(2).getInicioJornada()));
                    tabelaHorario.setFaixaEntrada3(faixaEntrada3);

                    horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(2).getInicioJornada()), sdfHora.format(jornadaDiaList.get(2).getInicioJornada())));
                    addDescansoParaAbonar(sdfHora, jornadaDiaList.get(2));
                }
            }
        }
        tabelaHorarioList.add(tabelaHorario);
    }

    private void addDescansoParaAbonar(SimpleDateFormat sdfHora, Jornada j) {
        if (j != null) {/*
            if (j.getInicioDescanso1() != null) {
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(j.getInicioDescanso1()), sdfHora.format(j.getInicioDescanso1())));
            }
            if (j.getFimDescanso1() != null) {
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(j.getFimDescanso1()), sdfHora.format(j.getFimDescanso1())));
            }
            if (j.getInicioIntrajornada() != null) {
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(j.getInicioIntrajornada()), sdfHora.format(j.getInicioIntrajornada())));
            }
            if (j.getFimIntrajornada() != null) {
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(j.getFimIntrajornada()), sdfHora.format(j.getFimIntrajornada())));
            }
            if (j.getInicioDescanso2() != null) {
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(j.getInicioDescanso2()), sdfHora.format(j.getInicioDescanso2())));
            }
            if (j.getFimDescanso2() != null) {
                horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(j.getFimDescanso2()), sdfHora.format(j.getFimDescanso2())));
            }*/
        }
        /*
         horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()), sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1())));
         horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()), sdfHora.format(jornadaDiaList.get(0).getFimDescanso1())));

         horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada()), sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada())));
         horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada()), sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada())));

         horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()), sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2())));
         horaAAbonarCheckBoxList.add(new SelectItem(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()), sdfHora.format(jornadaDiaList.get(0).getFimDescanso2())));
         */
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

    public String solicitarAbono() {

        Banco banco = new Banco();

        String entrada1 = null;
        String saida1 = null;
        String entrada2 = null;
        String saida2 = null;

        String entrada1_;
        String saida1_;
        String entrada2_;
        String saida2_;

        try {
            entrada1_ = horaAAbonarCheckBoxList.get(0).getLabel();
        } catch (Exception e) {
            entrada1_ = "";
        }

        try {
            saida1_ = horaAAbonarCheckBoxList.get(1).getLabel();
        } catch (Exception e) {
            saida1_ = "";
        }

        try {
            entrada2_ = horaAAbonarCheckBoxList.get(2).getLabel();
        } catch (Exception e) {
            entrada2_ = "";
        }

        try {
            saida2_ = horaAAbonarCheckBoxList.get(3).getLabel();
        } catch (Exception e) {
            saida2_ = "";
        }

        try {
            for (int i = 0; i < horaSelecionadasAbonoList.size(); i++) {
                if (horaSelecionadasAbonoList.get(i).equals(entrada1_)) {
                    entrada1 = horaSelecionadasAbonoList.get(i);
                }
                if (horaSelecionadasAbonoList.get(i).equals(saida1_)) {
                    saida1 = horaSelecionadasAbonoList.get(i);
                }
                if (jornadaDiaList.size() == 2) {
                    if (horaSelecionadasAbonoList.get(i).equals(entrada2_)) {
                        entrada2 = horaSelecionadasAbonoList.get(i);
                    }
                    if (horaSelecionadasAbonoList.get(i).equals(saida2_)) {
                        saida2 = horaSelecionadasAbonoList.get(i);
                    }
                }
            }

        } catch (Exception e) {
            FacesMessage msgErro = new FacesMessage("Erro inesperado");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        boolean ok = banco.solicitacaoAbono(codigo_funcionario, dataSelecionada, entrada1,
                saida1, entrada2,
                saida2, descricaoInputText, new java.util.Date());

        if (ok) {
            FacesMessage msgErro = new FacesMessage("Solicitação de abono realizada com sucesso.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao solicitar abono.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        String datasSolicitadas = "";
        datasSolicitadas += (entrada1 == null) ? "" : " " + entrada1;
        datasSolicitadas += (saida1 == null) ? "" : " " + saida1;
        datasSolicitadas += (entrada2 == null) ? "" : " " + entrada2;
        datasSolicitadas += (saida2 == null) ? "" : " " + saida2;

        Metodos.Metodos.setLogInfo("Solicitação Abono - " + pontoAcessoDataGridBean.getDataSelecionada() + " -" + datasSolicitadas);
        return "consultaDiaPonto";
    }

    public void deletarSolicitacao() {
        Banco banco = new Banco();
        String cod_solicitacao = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_solicitacao");
        Boolean flag = banco.deleteSolicitacaoAbono(Integer.parseInt(cod_solicitacao));
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Solicitação deletada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao deletar a solicitação. Tente novamente!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);

        }
        solicitacaoAbono = new ArrayList<SolicitacaoAbono>();
        banco = new Banco();
        solicitacaoAbono = banco.consultaSolicitacaoAbonoFuncionario(codigo_funcionario, dataSelecionada);
    }

    private Boolean temPermissaoParaAbonar() {
        return usuarioBean.getUsuario().getLogin().equals(pontoAcessoDataGridBean.getCod_funcionario());
    }

    public String navegar() {
        pontoAcessoDataGridBean.consultaDiasSemMsgErro();
        Boolean isAdministrador = usuarioBean.getEhAdministrador();
        if (isAdministrador == true) {
            return "consultaFrequenciaAdmin";
        } else {
            return "consultaFrequencia";
        }
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

    public ConsultaFrequenciaComEscalaBean getPontoAcessoDataGridBean() {
        return pontoAcessoDataGridBean;
    }

    public void setPontoAcessoDataGridBean(ConsultaFrequenciaComEscalaBean pontoAcessoDataGridBean) {
        this.pontoAcessoDataGridBean = pontoAcessoDataGridBean;
    }

    public List<TabelaFuncionario> getTabelaFuncionario() {
        return tabelaFuncionario;
    }

    public DiaComEscala getDiaAcesso() {
        return diaAcesso;
    }

    public void setDiaAcesso(DiaComEscala diaAcesso) {
        this.diaAcesso = diaAcesso;
    }

    public void setTabelaFuncionario(List<TabelaFuncionario> tabelaFuncionario) {
        this.tabelaFuncionario = tabelaFuncionario;
    }

    public List<TabelaHorario> getTabelaHorarioList() {
        return tabelaHorarioList;
    }

    public void setTabelaHorarioList(List<TabelaHorario> tabelaHorarioList) {
        this.tabelaHorarioList = tabelaHorarioList;
    }

    public List<SituacaoPonto> getSituacaoList() {
        return situacaoList;
    }

    public void setSituacaoList(List<SituacaoPonto> situacaoList) {
        this.situacaoList = situacaoList;
    }

    public String getDescricaoInputText() {
        return descricaoInputText;
    }

    public void setDescricaoInputText(String descricaoInputText) {
        this.descricaoInputText = descricaoInputText;
    }

    public List<SelectItem> getHoraAAbonarCheckBoxList() {
        return horaAAbonarCheckBoxList;
    }

    public void setHoraAAbonarCheckBoxList(List<SelectItem> horaAAbonarCheckBoxList) {
        this.horaAAbonarCheckBoxList = horaAAbonarCheckBoxList;
    }

    public List<String> getHoraSelecionadasAbonoList() {
        return horaSelecionadasAbonoList;
    }

    public void setHoraSelecionadasAbonoList(List<String> horaSelecionadasAbonoList) {
        this.horaSelecionadasAbonoList = horaSelecionadasAbonoList;
    }

    public String[] getHoraSeleciinadaAbonoArray() {
        return horaSeleciinadaAbonoArray;
    }

    public void setHoraSeleciinadaAbonoArray(String[] horaSeleciinadaAbonoArray) {
        this.horaSeleciinadaAbonoArray = horaSeleciinadaAbonoArray;
    }

    public List<SolicitacaoAbono> getSolicitacaoAbono() {
        return solicitacaoAbono;
    }

    public void setSolicitacaoAbono(List<SolicitacaoAbono> solicitacaoAbono) {
        this.solicitacaoAbono = solicitacaoAbono;
    }

    public String getHoraAdicionada() {
        return horaAdicionada;
    }

    public void setHoraAdicionada(String horaAdicionada) {
        this.horaAdicionada = horaAdicionada;
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

    public Boolean getPodeAbonar() {
        return podeAbonar;
    }

    public void setPodeAbonar(Boolean podeAbonar) {
        this.podeAbonar = podeAbonar;
    }

    public List<SelectItem> getTipoLiberacaoList() {
        return tipoLiberacaoList;
    }

    public void setTipoLiberacaoList(List<SelectItem> tipoLiberacaoList) {
        this.tipoLiberacaoList = tipoLiberacaoList;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }
}
