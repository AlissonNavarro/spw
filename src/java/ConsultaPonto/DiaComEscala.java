/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import CadastroHoraExtra.DetalheGratificacao;
import CadastroHoraExtra.Gratificacao;
import CadastroHoraExtra.RegimeHoraExtra;
import CadastroHoraExtra.TipoHoraExtra;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author amsgama
 */
public class DiaComEscala implements Serializable {

    //Registros do primeiro horário do dia
    private Registro entrada_1;
    private Registro entrada_Descanso_1_1;
    private Registro saida_Descanso_1_1;
    private Registro entrada_Interjornada_1;
    private Registro saida_Interjornada_1;
    private Registro entrada_Descanso_2_1;
    private Registro saida_Descanso_2_1;
    private Registro saida_1;
    //Registro do segundo horario do dia
    private Registro saida_2;
    private Registro entrada_Descanso_1_2;
    private Registro saida_Descanso_1_2;
    private Registro entrada_Interjornada_2;
    private Registro saida_Interjornada_2;
    private Registro entrada_Descanso_2_2;
    private Registro saida_Descanso_2_2;
    private Registro entrada_2;
    //Outras variaveis
    private String definicao = "";
    private String atraso = "";
    private String justificativa = "";
    private String atrasoDiaStr;
    private String faltaDiaStr;
    private Date dia;
    private String diaString;
    private List<SituacaoPonto> situacaoPonto;
    private String colorDia;
    private String saldoStr = "";
    private String horasTrabalhadas = "";
    private Integer saldo = 0;
    private Integer saldoDiurno = 0;
    private Integer saldoNoturno = 0;
    private Integer qntAtraso16_59;
    private Integer qntAtrasoMaiorUmaHora;
    private Integer saldoDiaCritico = 0;
    private Integer adicionalNoturno;
    private List<Jornada> jornadaList;
    private Map<String, Integer> tipo_valorHorasExtra;
    private RegimeHoraExtra regimeHoraExtra;
    private Boolean isDiaTodoHoraExtra;
    private Boolean isFeriado;
    private Boolean isDiaCritico;
    private Boolean temIntervalo = true;
    private Boolean presente;
    private Boolean isPendente;
    private Boolean isAfastado;
    private HashMap<Integer, Integer> saidaGratHash;
    private Integer cod_funcionario = 0;

    public DiaComEscala() {
        isFeriado = false;
        situacaoPonto = new ArrayList<SituacaoPonto>();
        saidaGratHash = new HashMap<Integer, Integer>();
        //Instancia dos valores do primeiro horário
        entrada_1 = new Registro();
        entrada_Descanso_1_1 = new Registro();
        saida_Descanso_1_1 = new Registro();
        entrada_Interjornada_1 = new Registro();
        saida_Interjornada_1 = new Registro();
        entrada_Descanso_2_1 = new Registro();
        saida_Descanso_2_1 = new Registro();
        saida_1 = new Registro();
        //Instancia dos valores do segundo horário
        entrada_2 = new Registro();
        entrada_Descanso_1_2 = new Registro();
        saida_Descanso_1_2 = new Registro();
        entrada_Interjornada_2 = new Registro();
        saida_Interjornada_2 = new Registro();
        entrada_Descanso_2_2 = new Registro();
        saida_Descanso_2_2 = new Registro();
        saida_2 = new Registro();

        isAfastado = false;
        qntAtraso16_59 = 0;
        qntAtrasoMaiorUmaHora = 0;
        presente = true;
    }

    public void calcSaldoTurnoSemHoraExtra(Jornada jornada, RegimeHoraExtra regimeHoraExtra, List<Gratificacao> gratificacaoList) {
        Integer saldo1 = 0;
        Integer saldo2 = 0;
        Integer saldo1Diurno = 0;
        Integer saldo1Noturno = 0;
        Integer saldo2Diurno = 0;
        Integer saldo2Noturno = 0;
        Integer saldoDescanco1 = 0;
        Integer saldoInterJornada = 0;
        Integer saldoDescanco2 = 0;
        boolean puleSaida1 = false;

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");

        String inicioJornada1;
        if (jornada.getSoSaida()) {
            inicioJornada1 = "00:00:00";
        } else {
            inicioJornada1 = sdfHora.format(jornada.getInicioJornada());
        }

        String terminioJornada1;
        if (jornada.getSoEntrada()) {
            terminioJornada1 = jornada.getTerminioJornada_();
        } else {
            terminioJornada1 = sdfHora.format(jornada.getTerminioJornada());
        }

        String entrada1Fix = entrada_1.getRegistro();
        String saida1Fix = saida_1.getRegistro();

        if (!(entrada_1.getRegistro().equals("")) && (!inicioJornada1.equals(""))) {
            saldo1 = calcSaldoRegistroEntrada(entrada1Fix, inicioJornada1);
            saldo1Diurno = calcSaldoRegistroEntradaDiurno(entrada1Fix, inicioJornada1);
            saldo1Noturno = calcSaldoRegistroEntradaNoturno(entrada1Fix, inicioJornada1);
        } else {
            if (jornada.getSoSaida()) {
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            } else {
                saldo1 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                puleSaida1 = true;
            }
        }

        if (!(saida_1.getRegistro().equals("")) && (!terminioJornada1.equals("")) && !(puleSaida1)) {
            saldo2 = calcSaldoRegistroSaida(saida1Fix, terminioJornada1);
            saldo2Diurno = calcSaldoRegistroSaidaDiurno(saida1Fix, terminioJornada1);
            saldo2Noturno = calcSaldoRegistroSaidaNoturno(saida1Fix, terminioJornada1);
        } else {
            if (jornada.getSoEntrada()) {
                saldo2 = 0;
                saldo2Diurno = 0;
                saldo2Noturno = 0;
            } else if (!puleSaida1) {
                saldo2 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            }
        }

        //Calcula os saldos de descanso
        if (jornada.getInicioDescanso1() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso1()), sdfHora.format(jornada.getFimDescanso1()), true);
            String entradaDescanco1 = entrada_Descanso_1_1.getRegistro();
            String saidaDescanco1 = "";
            if (jornada.getCombinarEntrada() != null) {
                if (jornada.getCombinarEntrada()) {
                    saidaDescanco1 = sdfHora.format(jornada.getFimDescanso1());
                } else {
                    saidaDescanco1 = saida_Descanso_1_1.getRegistro();
                }
            }
            if (jornada.getDeduzirDescanco1() != null) {
                if (jornada.getDeduzirDescanco1()) {
                    saldoDescanco1 = calcHorasDia(entradaDescanco1, saidaDescanco1, true);
                    saldoDescanco1 -= tempoDeDescanso;
                }
            }
        }
        if (jornada.getInicioIntrajornada() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioIntrajornada()), sdfHora.format(jornada.getFimIntrajornada()), true);
            String entradaInterjornada;
            String saidaInterjornada;
            if (jornada.getCombinarEntrada()) {
                entradaInterjornada = sdfHora.format(jornada.getInicioIntrajornada());
            } else {
                entradaInterjornada = entrada_Interjornada_1.getRegistro();
            }
            if (jornada.getCombinarSaida()) {
                saidaInterjornada = sdfHora.format(jornada.getFimIntrajornada());
            } else {
                saidaInterjornada = saida_Interjornada_1.getRegistro();
            }
            if (jornada.getDeduzirIntrajornada()) {
                saldoInterJornada = calcHorasDia(entradaInterjornada, saidaInterjornada, true);
                saldoInterJornada -= tempoDeDescanso;
            }
        }
        if (jornada.getInicioDescanso2() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso2()), sdfHora.format(jornada.getFimDescanso2()), true);
            String entradaDescanco2 = "";
            if (jornada.getCombinarSaida() != null) {
                if (jornada.getCombinarSaida()) {
                    entradaDescanco2 = sdfHora.format(jornada.getInicioDescanso2());
                } else {
                    entradaDescanco2 = entrada_Descanso_2_1.getRegistro();
                }
            }
            String saidaDescanco2 = saida_Descanso_2_1.getRegistro();
            if (jornada.getDeduzirDescanco2() != null) {
                if (jornada.getDeduzirDescanco2()) {
                    saldoDescanco2 = calcHorasDia(entradaDescanco2, saidaDescanco2, true);
                    saldoDescanco2 -= tempoDeDescanso;
                }
            }
        }

        atraso = calcAtraso(saldo1);
        if (entrada_1.getRegistro().equals("") && saida_1.getRegistro().equals("")) {
            atraso = "0 minutos";
        }
        qntAtraso16_59(saldo1);
        qntAtrasoMaiorUmaHora(saldo1);
        faltaStr();
        atrasoStr(saldo1);

        /* Integer saldoFalta = calcTotalSegundosFalta1Turno(jornada);
         saldo1 = calcTolerancia1Turno(regimeHoraExtra, saldo1, saldoFalta);
         saldo2 = calcTolerancia1Turno(regimeHoraExtra, saldo2, saldoFalta);*/
        Integer tolerancia = regimeHoraExtra.getTolerancia();
        saldo1 = Math.abs(saldo1) > tolerancia * 60 ? saldo1 : 0;
        saldo2 = Math.abs(saldo2) > tolerancia * 60 ? saldo2 : 0;

        /*
         if (saldoFalta.equals(saldo1)) {
         saldo2 = 0;
         }
         if (saldoFalta.equals(saldo2)) {
         saldo1 = 0;
         }*/
        //saldo = (saldo1 + saldo2) / 60;
        saldo = (saldo1 + saldo2 + saldoDescanco1 + saldoInterJornada + saldoDescanco2) / 60;
        saldoDiurno = (saldo1Diurno + saldo2Diurno) / 60;
        saldoNoturno = (saldo1Noturno + saldo2Noturno) / 60;

        Integer jornadaTurno1 = calcHorasDia(inicioJornada1, terminioJornada1, false) / 60;

        if (jornada.getDeduzirDescanco1() != null && jornada.getDeduzirDescanco1()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornada.getInicioDescanso1()), sdfHora.format(jornada.getFimDescanso1()), false) / 60);
        }
        if (jornada.getDeduzirIntrajornada() != null && jornada.getDeduzirIntrajornada()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornada.getInicioIntrajornada()), sdfHora.format(jornada.getFimIntrajornada()), false) / 60);
        }
        if (jornada.getDeduzirDescanco2() != null && jornada.getDeduzirDescanco2()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornada.getInicioDescanso2()), sdfHora.format(jornada.getFimDescanso2()), false) / 60);
        }
        Integer jornadaTotal = jornadaTurno1;
        horasTrabalhadas = transformaEmHora(jornadaTotal, saldo);

        if (isDiaTodoHoraExtra) {
            saldo = jornadaTotal + saldo;
        }

        adicionalNoturno = calcAdicionalNoturno(entrada_1, saida_1, terminioJornada1, jornada);
        if (isDiaCritico) {
            saldoDiaCritico = calcDiaCritico(entrada_1, saida_1, jornada);
        } else {
            saldoDiaCritico = 0;
            calcGratificacao(entrada_1, saida_1, jornada, gratificacaoList);
        }

        Banco b = new Banco();
        Long milisegundos = b.consultaDeslocadosHoraContratada(cod_funcionario, dia);
        Integer minutosContratados = (int) TimeUnit.MILLISECONDS.toMinutes(milisegundos);
        if ((saldo - minutosContratados) < 11 && (saldo - minutosContratados) >= 0) {
            saldoStr = transformaMinutosEmHora(0);
        } else {
            saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
        }
        //saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
    }

    public void calcSaldoTurno(Jornada jornada, List<TipoHoraExtra> tipoHoraExtra, RegimeHoraExtra regimeHoraExtra,
            List<Gratificacao> gratificacaoList) {
        Integer saldo1 = 0;
        Integer saldo2 = 0;
        Integer saldo1Diurno = 0;
        Integer saldo1Noturno = 0;
        Integer saldo2Diurno = 0;
        Integer saldo2Noturno = 0;
        Integer saldoDescanco1 = 0;
        Integer saldointerJornada = 0;
        Integer saldoDescanco2 = 0;
        boolean puleSaida1 = false;

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");

        String inicioJornada1;
        if (jornada.getSoSaida()) {
            inicioJornada1 = "00:00:00";
        } else {
            inicioJornada1 = sdfHora.format(jornada.getInicioJornada());
        }

        String terminioJornada1;
        if (jornada.getSoEntrada()) {
            terminioJornada1 = jornada.getTerminioJornada_();
        } else {
            terminioJornada1 = sdfHora.format(jornada.getTerminioJornada());
        }

        String entrada1Fix = entrada_1.getRegistro();
        String saida1Fix = saida_1.getRegistro();

        if (!(entrada_1.getRegistro().equals("")) && (!inicioJornada1.equals(""))) {
            saldo1 = calcSaldoRegistroEntrada(entrada1Fix, inicioJornada1);
            saldo1Diurno = calcSaldoRegistroEntradaDiurno(entrada1Fix, inicioJornada1);
            saldo1Noturno = calcSaldoRegistroEntradaNoturno(entrada1Fix, inicioJornada1);
        } else {
            if (jornada.getSoSaida()) {
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            } else {
                saldo1 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                puleSaida1 = true;
            }
        }

        if (!(saida_1.getRegistro().equals("")) && (!terminioJornada1.equals("")) && !(puleSaida1)) {
            saldo2 = calcSaldoRegistroSaida(saida1Fix, terminioJornada1);
            saldo2Diurno = calcSaldoRegistroSaidaDiurno(saida1Fix, terminioJornada1);
            saldo2Noturno = calcSaldoRegistroSaidaNoturno(saida1Fix, terminioJornada1);
        } else {
            if (jornada.getSoEntrada()) {
                saldo2 = 0;
                saldo2Diurno = 0;
                saldo2Noturno = 0;
            } else if (!puleSaida1) {
                saldo2 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            }
        }

        if (jornada.getInicioDescanso1() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso1()), sdfHora.format(jornada.getFimDescanso1()), true);
            String entradaDescanco1 = entrada_Descanso_1_1.getRegistro();
            String saidaDescanco1;
            if (jornada.getCombinarEntrada()) {
                saidaDescanco1 = sdfHora.format(jornada.getFimDescanso1());
            } else {
                saidaDescanco1 = saida_Descanso_1_1.getRegistro();
            }
            if (jornada.getDeduzirDescanco1()) {
                saldoDescanco1 = calcHorasDia(entradaDescanco1, saidaDescanco1, true);
                saldoDescanco1 -= tempoDeDescanso;
            }
        }
        if (jornada.getInicioIntrajornada() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioIntrajornada()), sdfHora.format(jornada.getFimIntrajornada()), true);
            String entradaInterjornada;
            String saidaInterjornada;
            if (jornada.getCombinarEntrada()) {
                entradaInterjornada = sdfHora.format(jornada.getInicioIntrajornada());
            } else {
                entradaInterjornada = entrada_Interjornada_1.getRegistro();
            }
            if (jornada.getCombinarSaida()) {
                saidaInterjornada = sdfHora.format(jornada.getFimIntrajornada());
            } else {
                saidaInterjornada = saida_Interjornada_1.getRegistro();
            }
            if (jornada.getDeduzirIntrajornada()) {
                saldointerJornada = calcHorasDia(entradaInterjornada, saidaInterjornada, true);
                saldointerJornada -= tempoDeDescanso;
            }
        }
        if (jornada.getInicioDescanso2() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso2()), sdfHora.format(jornada.getFimDescanso2()), true);
            String entradaDescanco2;
            if (jornada.getCombinarSaida()) {
                entradaDescanco2 = sdfHora.format(jornada.getInicioDescanso2());
            } else {
                entradaDescanco2 = entrada_Descanso_2_1.getRegistro();
            }
            String saidaDescanco2 = saida_Descanso_2_1.getRegistro();
            if (jornada.getDeduzirDescanco2()) {
                saldoDescanco2 = calcHorasDia(entradaDescanco2, saidaDescanco2, true);
                saldoDescanco2 -= tempoDeDescanso;
            }
        }

        atraso = calcAtraso(saldo1);
        qntAtraso16_59(saldo1);
        qntAtrasoMaiorUmaHora(saldo1);
        faltaStr();
        atrasoStr(saldo1);

        /* Integer saldoFalta = calcTotalSegundosFalta1Turno(jornada);
         saldo1 = calcTolerancia1Turno(regimeHoraExtra, saldo1, saldoFalta);
         saldo2 = calcTolerancia1Turno(regimeHoraExtra, saldo2, saldoFalta);*/
        Integer tolerancia = regimeHoraExtra.getTolerancia();
        saldo1 = Math.abs(saldo1) > tolerancia * 60 ? saldo1 : 0;
        saldo2 = Math.abs(saldo2) > tolerancia * 60 ? saldo2 : 0;

        /*
         if (saldoFalta.equals(saldo1)) {
         saldo2 = 0;
         }
         if (saldoFalta.equals(saldo2)) {
         saldo1 = 0;
         }*/
        //saldo = (saldo1 + saldo2) / 60;
        saldo = (saldo1 + saldo2 + saldoDescanco1 + saldointerJornada + saldoDescanco2) / 60;

        saldoDiurno = (saldo1Diurno + saldo2Diurno) / 60;
        saldoDiurno = Math.abs(saldoDiurno) > tolerancia ? saldoDiurno : 0;
        saldoNoturno = (saldo1Noturno + saldo2Noturno) / 60;
        saldoNoturno = Math.abs(saldoNoturno) > tolerancia ? saldoNoturno : 0;

        Integer jornadaTurno1 = calcHorasDia(inicioJornada1, terminioJornada1, false) / 60;
        if (jornada.getDeduzirDescanco1()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornada.getInicioDescanso1()), sdfHora.format(jornada.getFimDescanso1()), false) / 60);
        }
        if (jornada.getDeduzirIntrajornada()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornada.getInicioIntrajornada()), sdfHora.format(jornada.getFimIntrajornada()), false) / 60);
        }
        if (jornada.getDeduzirDescanco2()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornada.getInicioDescanso2()), sdfHora.format(jornada.getFimDescanso2()), false) / 60);
        }
        Integer jornadaTotal = jornadaTurno1;
        horasTrabalhadas = transformaEmHora(jornadaTotal, saldo);

        if (isDiaTodoHoraExtra) {
            saldo = jornadaTotal + saldo;
        }

        List<String> nomeTiposHoraExtra = new ArrayList<String>();
        for (Iterator<TipoHoraExtra> it = tipoHoraExtra.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra1 = it.next();
            nomeTiposHoraExtra.add(tipoHoraExtra1.getNome());
        }
        int diaSemana = converterDiaSemana();
        tipoHoraExtra = ordenarHoraExtra(tipoHoraExtra, diaSemana);

        Banco b = new Banco();
        Long milisegundos = b.consultaDeslocadosHoraContratada(cod_funcionario, dia);
        Integer minutosContratados = (int) TimeUnit.MILLISECONDS.toMinutes(milisegundos);
        Integer reparaDesvioDeSaldo = 0;
        if (!b.getExtraAntesDeDeslocamento(cod_funcionario, dia).isEmpty()) {
            List<Date> date = b.getExtraAntesDeDeslocamento(cod_funcionario, dia);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.get(0));
            reparaDesvioDeSaldo = ((24 - cal.get(Calendar.HOUR_OF_DAY)) * 60 - cal.get(Calendar.MINUTE));
            calcHoraExtra(diaSemana, (saldo > 0) ? saldo - reparaDesvioDeSaldo : 0, tipoHoraExtra, nomeTiposHoraExtra);
            if ((saldo - reparaDesvioDeSaldo) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
            }
            //saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
        } else if (!b.getExtraDepoisDeDeslocamento(cod_funcionario, dia).isEmpty()) {
            List<Date> date = b.getExtraDepoisDeDeslocamento(cod_funcionario, dia);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.get(1));
            reparaDesvioDeSaldo = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
            calcHoraExtra(diaSemana, (saldo > 0) ? saldo - reparaDesvioDeSaldo : 0, tipoHoraExtra, nomeTiposHoraExtra);
            if ((saldo - reparaDesvioDeSaldo) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
            }
            //saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
        } else {
            calcHoraExtra(diaSemana, (saldo > 0) ? saldo - minutosContratados : 0, tipoHoraExtra, nomeTiposHoraExtra);
            if ((saldo - minutosContratados) < regimeHoraExtra.getTolerancia() && (saldo - minutosContratados) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
            }
            //saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
        }
        adicionalNoturno = calcAdicionalNoturno(entrada_1, saida_1, terminioJornada1, jornada);

        if (isDiaCritico) {
            Banco b1 = new Banco();
            List<Date> listDates = b1.consultaDiaComHoraExtra(cod_funcionario, dia);
            if (listDates.size() <= 2) {
                if (listDates.size() != 0) {
                    Date inicio = listDates.get(0);
                    Date termino = listDates.get(1);
                    Calendar cal1 = new GregorianCalendar();
                    Calendar cal2 = new GregorianCalendar();
                    Calendar jorn1 = new GregorianCalendar();
                    Calendar jorn2 = new GregorianCalendar();
                    cal1.setTime(inicio);
                    cal2.setTime(termino);
                    if (jornada.getInicioJornada() != null) {
                        jorn1.setTime(jornada.getInicioJornada());
                    } else {
                        jorn1.set(Calendar.HOUR_OF_DAY, 0);
                    }

                    if (jornada.getTerminioJornada() != null) {
                        jorn2.setTime(jornada.getTerminioJornada());
                    } else {
                        jorn2.set(Calendar.HOUR_OF_DAY, 24);
                    }
                    if (jornada.getInicioJornada() != null && jornada.getTerminioJornada() != null) { //Se nenhuma batida for foda da data
                        if (cal1.get(Calendar.HOUR_OF_DAY) != jorn1.get(Calendar.HOUR_OF_DAY) && cal2.get(Calendar.HOUR_OF_DAY) != jorn2.get(Calendar.HOUR_OF_DAY)) {
                            Registro terminoJornada_1 = new Registro();
                            terminoJornada_1.setRegistro(terminioJornada1);
                            saldoDiaCritico = calcDiaCritico(entrada_1, terminoJornada_1, jornada);
                        }
                    }
                } else {
                    if (!terminioJornada1.equals("")) {
                        Registro terminoJornada_1 = new Registro();
                        terminoJornada_1.setRegistro(terminioJornada1);
                        saldoDiaCritico = calcDiaCritico(entrada_1, terminoJornada_1, jornada);
                    } else {
                        saldoDiaCritico = calcDiaCritico(entrada_1, saida_1, jornada);
                    }
                }
            }
        } else {
            calcGratificacao(entrada_1, saida_1, jornada, gratificacaoList);
            saldoDiaCritico = 0;
        }

    }

    public void calcSaldoTurnoSemHoraExtra(List<Jornada> jornadaDiaList, RegimeHoraExtra regimeHoraExtra, List<Gratificacao> gratificacaoList) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String inicioJornada1 = "";
        String terminioJornada1 = "";
        String inicioJornada2 = "";
        String terminioJornada2 = "";
        Jornada jornada;

        if (jornadaDiaList.get(0).getTemVirada()) {
            if (jornadaDiaList.get(0).getSoEntrada()) {
                inicioJornada1 = sdfHora.format(jornadaDiaList.get(0).getInicioJornada());
                terminioJornada1 = "24:00:00";
            } else {
                inicioJornada1 = "00:00:00";
                terminioJornada1 = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
            }
        } else {
            inicioJornada1 = sdfHora.format(jornadaDiaList.get(0).getInicioJornada());
            terminioJornada1 = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
        }

        if (jornadaDiaList.get(1).getTemVirada()) {
            if (jornadaDiaList.get(1).getSoEntrada()) {
                inicioJornada2 = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
                terminioJornada2 = "24:00:00";
            } else {
                inicioJornada2 = "00:00:00";
                terminioJornada2 = sdfHora.format(jornadaDiaList.get(1).getTerminioJornada());
            }
        } else {
            if (jornadaDiaList.get(1).getInicioJornada() != null) {
                inicioJornada2 = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
            }
            if (jornadaDiaList.get(1).getTerminioJornada() != null) {
                terminioJornada2 = sdfHora.format(jornadaDiaList.get(1).getTerminioJornada());
            }
        }

        Integer saldo1 = 0;
        Integer saldo2 = 0;
        Integer saldo3 = 0;
        Integer saldo4 = 0;
        Integer saldo1Diurno = 0;
        Integer saldo1Noturno = 0;
        Integer saldo2Diurno = 0;
        Integer saldo2Noturno = 0;
        Integer saldo3Diurno = 0;
        Integer saldo3Noturno = 0;
        Integer saldo4Diurno = 0;
        Integer saldo4Noturno = 0;
        Integer saldo1Descanco1 = 0;
        Integer saldo1InterJornada = 0;
        Integer saldo1Descanco2 = 0;
        Integer saldo2Descanco1 = 0;
        Integer saldo2InterJornada = 0;
        Integer saldo2Descanco2 = 0;

        boolean puleSaida1 = false;
        boolean puleSaida2 = false;
        boolean semAtraso = false;

        String entrada1Fix = entrada_1.getRegistro();
        String saida1Fix = saida_1.getRegistro();
        String entrada2Fix = entrada_2.getRegistro();
        String saida2Fix = saida_2.getRegistro();

        if (!(entrada_1.getRegistro().equals("")) && (!inicioJornada1.equals(""))) {
            saldo1 = calcSaldoRegistroEntrada(entrada1Fix, inicioJornada1);
            saldo1Diurno = calcSaldoRegistroEntradaDiurno(entrada1Fix, inicioJornada1);
            saldo1Noturno = calcSaldoRegistroEntradaNoturno(entrada1Fix, inicioJornada1);
        } else {
            if (jornadaDiaList.get(0).getSoSaida()) {
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            } else {
                saldo1 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                puleSaida1 = true;
            }
        }

        if (!(saida_1.getRegistro().equals("")) && (!terminioJornada1.equals("")) && !(puleSaida1)) {
            saldo2 = calcSaldoRegistroSaida(saida1Fix, terminioJornada1);
            saldo2Diurno = calcSaldoRegistroSaidaDiurno(saida1Fix, terminioJornada1);
            saldo2Noturno = calcSaldoRegistroSaidaNoturno(saida1Fix, terminioJornada1);
        } else {
            if (jornadaDiaList.get(0).getSoEntrada()) {
                saldo2 = 0;
                saldo2Diurno = 0;
                saldo2Noturno = 0;
            } else {
                saldo2 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            }
        }
        Integer adicionalNoturno1 = calcAdicionalNoturno(entrada_1, saida_1, terminioJornada1, jornadaDiaList.get(0));

        if (isDiaCritico) {
            saldoDiaCritico = calcDiaCritico(entrada_1, saida_1, jornadaDiaList.get(0));
        } else {
            calcGratificacao(entrada_1, saida_1, jornadaDiaList.get(0), gratificacaoList);
            saldoDiaCritico = 0;
        }

        if (!(entrada_2.getRegistro().equals("")) && (!inicioJornada2.equals(""))) {
            saldo3 = calcSaldoRegistroEntrada(entrada2Fix, inicioJornada2);
            saldo3Diurno = calcSaldoRegistroEntradaDiurno(entrada2Fix, inicioJornada2);
            saldo3Noturno = calcSaldoRegistroEntradaNoturno(entrada2Fix, inicioJornada2);
        } else {
            if (terminioJornada2.equals("24:00:00")) {
                semAtraso = true;
            }
            saldo3 = calcHorasDia(inicioJornada2, terminioJornada2, true);
            puleSaida2 = true;
        }
        if (!(saida_2.getRegistro().equals("")) && (!terminioJornada2.equals("")) && !(puleSaida2)) {
            saldo4 = calcSaldoRegistroSaida(saida2Fix, terminioJornada2);
            saldo4Diurno = calcSaldoRegistroSaidaDiurno(saida2Fix, terminioJornada2);
            saldo4Noturno = calcSaldoRegistroSaidaNoturno(saida2Fix, terminioJornada2);
        } else {
            if (jornadaDiaList.get(1).getSoEntrada()) {
                saldo4 = 0;
                saldo4Diurno = 0;
                saldo4Noturno = 0;
            } else {
                saldo4 = calcHorasDia(inicioJornada2, terminioJornada2, true);
                saldo3 = 0;
                saldo3Diurno = 0;
                saldo3Noturno = 0;
            }
        }
        Integer adicionalNoturno2 = calcAdicionalNoturno(entrada_2, saida_2, terminioJornada2, jornadaDiaList.get(1));
        calcGratificacao(entrada_2, saida_2, jornadaDiaList.get(1), gratificacaoList);

        if (!semAtraso) {
            atraso = calcAtraso(saldo1, saldo3);
        } else {
            atraso = "0 minutos";
        }
        qntAtraso16_59(saldo1, saldo3);
        qntAtrasoMaiorUmaHora(saldo1, saldo3);
        faltaStr();
        atrasoStr(saldo1, saldo3);

        /*  Integer saldoFalta = calcTotalSegundosFalta2Turnos(jornadaDiaList);
        
         saldo1 = calcTolerancia2Turnos(regimeHoraExtra, saldo1, saldo2, saldo3, saldo4, saldoFalta);
         saldo2 = calcTolerancia2Turnos(regimeHoraExtra, saldo2, saldo1, saldo3, saldo4, saldoFalta);
         saldo3 = calcTolerancia2Turnos(regimeHoraExtra, saldo3, saldo1, saldo2, saldo4, saldoFalta);
         saldo4 = calcTolerancia2Turnos(regimeHoraExtra, saldo4, saldo1, saldo2, saldo3, saldoFalta);*/
        Integer tolerancia = regimeHoraExtra.getTolerancia();
        saldo1 = Math.abs(saldo1) > tolerancia * 60 ? saldo1 : 0;
        saldo2 = Math.abs(saldo2) > tolerancia * 60 ? saldo2 : 0;
        saldo3 = Math.abs(saldo3) > tolerancia * 60 ? saldo3 : 0;
        saldo4 = Math.abs(saldo4) > tolerancia * 60 ? saldo4 : 0;

        /*      if (saldoFalta.equals(saldo1)) {
         saldo2 = 0;
         saldo3 = 0;
         saldo4 = 0;
         }
         if (saldoFalta.equals(saldo2)) {
         saldo1 = 0;
         saldo3 = 0;
         saldo4 = 0;
         }
         if (saldoFalta.equals(saldo3)) {
         saldo1 = 0;
         saldo2 = 0;
         saldo4 = 0;
         }
         if (saldoFalta.equals(saldo4)) {
         saldo1 = 0;
         saldo2 = 0;
         saldo3 = 0;
         }
         */
        //Saldo dos descansos da primeira jornada
        jornada = jornadaDiaList.get(0);
        if (jornada.getInicioDescanso1() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso1()), sdfHora.format(jornada.getFimDescanso1()), true);
            String entradaDescanco1 = entrada_Descanso_1_1.getRegistro();
            String saidaDescanco1 = "";
            if (jornada.getCombinarEntrada() != null) {
                if (jornada.getCombinarEntrada()) {
                    saidaDescanco1 = sdfHora.format(jornada.getFimDescanso1());
                } else {
                    saidaDescanco1 = saida_Descanso_1_1.getRegistro();
                }
            }
            if (jornada.getDeduzirDescanco1() != null) {
                if (jornada.getDeduzirDescanco1()) {
                    saldo1Descanco1 = calcHorasDia(entradaDescanco1, saidaDescanco1, true);
                    saldo1Descanco1 -= tempoDeDescanso;
                }
            }
        }
        if (jornada.getInicioIntrajornada() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioIntrajornada()), sdfHora.format(jornada.getFimIntrajornada()), true);
            String entradaInterjornada;
            String saidaInterjornada;
            if (jornada.getCombinarEntrada()) {
                entradaInterjornada = sdfHora.format(jornada.getInicioIntrajornada());
            } else {
                entradaInterjornada = entrada_Interjornada_1.getRegistro();
            }
            if (jornada.getCombinarSaida()) {
                saidaInterjornada = sdfHora.format(jornada.getFimIntrajornada());
            } else {
                saidaInterjornada = saida_Interjornada_1.getRegistro();
            }
            if (jornada.getDeduzirIntrajornada()) {
                saldo1InterJornada = calcHorasDia(entradaInterjornada, saidaInterjornada, true);
                saldo1InterJornada -= tempoDeDescanso;
            }
        }
        if (jornada.getInicioDescanso2() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso2()), sdfHora.format(jornada.getFimDescanso2()), true);
            String entradaDescanco2 = "";
            if (jornada.getCombinarSaida() != null) {
                if (jornada.getCombinarSaida()) {
                    entradaDescanco2 = sdfHora.format(jornada.getInicioDescanso2());
                } else {
                    entradaDescanco2 = entrada_Descanso_2_1.getRegistro();
                }
            }
            String saidaDescanco2 = saida_Descanso_2_1.getRegistro();
            if (jornada.getDeduzirDescanco2() != null) {
                if (jornada.getDeduzirDescanco2()) {
                    saldo1Descanco2 = calcHorasDia(entradaDescanco2, saidaDescanco2, true);
                    saldo1Descanco2 -= tempoDeDescanso;
                }
            }
        }

        //Saldo dos descansos da segunda jornada
        jornada = jornadaDiaList.get(1);
        if (jornada.getInicioDescanso1() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso1()), sdfHora.format(jornada.getFimDescanso1()), true);
            String entradaDescanco1 = entrada_Descanso_1_2.getRegistro();
            String saidaDescanco1 = "";
            if (jornada.getCombinarEntrada() != null) {
                if (jornada.getCombinarEntrada()) {
                    saidaDescanco1 = sdfHora.format(jornada.getFimDescanso1());
                } else {
                    saidaDescanco1 = saida_Descanso_1_2.getRegistro();
                }
            }
            if (jornada.getDeduzirDescanco1() != null) {
                if (jornada.getDeduzirDescanco1()) {
                    saldo2Descanco1 = calcHorasDia(entradaDescanco1, saidaDescanco1, true);
                    saldo2Descanco1 -= tempoDeDescanso;
                }
            }
        }
        if (jornada.getInicioIntrajornada() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioIntrajornada()), sdfHora.format(jornada.getFimIntrajornada()), true);
            String entradaInterjornada;
            String saidaInterjornada;
            if (jornada.getCombinarEntrada()) {
                entradaInterjornada = sdfHora.format(jornada.getInicioIntrajornada());
            } else {
                entradaInterjornada = entrada_Interjornada_2.getRegistro();
            }
            if (jornada.getCombinarSaida()) {
                saidaInterjornada = sdfHora.format(jornada.getFimIntrajornada());
            } else {
                saidaInterjornada = saida_Interjornada_2.getRegistro();
            }
            if (jornada.getDeduzirIntrajornada()) {
                saldo2InterJornada = calcHorasDia(entradaInterjornada, saidaInterjornada, true);
                saldo2InterJornada -= tempoDeDescanso;
            }
        }
        if (jornada.getInicioDescanso2() != null) {
            Integer tempoDeDescanso = calcHorasDia(sdfHora.format(jornada.getInicioDescanso2()), sdfHora.format(jornada.getFimDescanso2()), true);
            String entradaDescanco2 = "";
            if (jornada.getCombinarSaida() != null) {
                if (jornada.getCombinarSaida()) {
                    entradaDescanco2 = sdfHora.format(jornada.getInicioDescanso2());
                } else {
                    entradaDescanco2 = entrada_Descanso_2_2.getRegistro();
                }
            }
            String saidaDescanco2 = saida_Descanso_2_2.getRegistro();
            if (jornada.getDeduzirDescanco2() != null) {
                if (jornada.getDeduzirDescanco2()) {
                    saldo2Descanco2 = calcHorasDia(entradaDescanco2, saidaDescanco2, true);
                    saldo2Descanco2 -= tempoDeDescanso;
                }
            }
        }

        saldo = (saldo1 + saldo2 + saldo3 + saldo4 + saldo1Descanco1 + saldo1InterJornada
                + saldo1Descanco2 + saldo2Descanco1 + saldo2InterJornada + saldo2Descanco2) / 60;

        saldoDiurno = (saldo1Diurno + saldo2Diurno + saldo3Diurno + saldo4Diurno) / 60;
        saldoDiurno = Math.abs(saldoDiurno) > tolerancia ? saldoDiurno : 0;
        saldoNoturno = (saldo1Noturno + saldo2Noturno + saldo3Noturno + saldo4Noturno) / 60;
        saldoNoturno = Math.abs(saldoNoturno) > tolerancia ? saldoNoturno : 0;

        Integer jornadaTurno1 = calcHorasDia(inicioJornada1, terminioJornada1, false) / 60;
        if (jornadaDiaList.get(0).getDeduzirDescanco1() != null && jornadaDiaList.get(0).getDeduzirDescanco1()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()), sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()), false) / 60);
        }
        if (jornadaDiaList.get(0).getDeduzirIntrajornada() != null && jornadaDiaList.get(0).getDeduzirIntrajornada()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornadaDiaList.get(0).getInicioIntrajornada()), sdfHora.format(jornadaDiaList.get(0).getFimIntrajornada()), false) / 60);
        }
        if (jornadaDiaList.get(0).getDeduzirDescanco2() != null && jornadaDiaList.get(0).getDeduzirDescanco2()) {
            jornadaTurno1 -= (calcHorasDia(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()), sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()), false) / 60);
        }
        Integer jornadaTurno2 = calcHorasDia(inicioJornada2, terminioJornada2, false) / 60;
        if (jornadaDiaList.get(1).getDeduzirDescanco1() != null && jornadaDiaList.get(1).getDeduzirDescanco1()) {
            jornadaTurno2 -= (calcHorasDia(sdfHora.format(jornadaDiaList.get(1).getInicioDescanso1()), sdfHora.format(jornadaDiaList.get(1).getFimDescanso1()), false) / 60);
        }
        if (jornadaDiaList.get(1).getDeduzirIntrajornada() != null && jornadaDiaList.get(1).getDeduzirIntrajornada()) {
            if (jornadaDiaList.get(1).getInicioIntrajornada() != null && jornadaDiaList.get(1).getFimIntrajornada() != null) {
                jornadaTurno2 -= (calcHorasDia(sdfHora.format(jornadaDiaList.get(1).getInicioIntrajornada()), sdfHora.format(jornadaDiaList.get(1).getFimIntrajornada()), false) / 60);
            }
        }
        if (jornadaDiaList.get(1).getDeduzirDescanco2() != null && jornadaDiaList.get(1).getDeduzirDescanco2()) {
            if (jornadaDiaList.get(1).getInicioDescanso2() != null && jornadaDiaList.get(1).getFimDescanso2() != null) {
                jornadaTurno2 -= (calcHorasDia(sdfHora.format(jornadaDiaList.get(1).getInicioDescanso2()), sdfHora.format(jornadaDiaList.get(1).getFimDescanso2()), false) / 60);
            }
        }
        Integer jornadaTotal = (jornadaTurno1 + jornadaTurno2);

        horasTrabalhadas = transformaEmHora(jornadaTotal, saldo);

        if (isDiaTodoHoraExtra) {
            saldo = jornadaTotal + saldo;
        }

        adicionalNoturno = adicionalNoturno1 + adicionalNoturno2;

        if (isDiaCritico) {
            saldoDiaCritico += calcDiaCritico(entrada_2, saida_2, jornadaDiaList.get(1));
        } else {
            saldoDiaCritico += 0;
        }

        Banco b = new Banco();
        Long milisegundos = b.consultaDeslocadosHoraContratada(cod_funcionario, dia);
        Integer minutosContratados = (int) TimeUnit.MILLISECONDS.toMinutes(milisegundos);
        Integer reparaDesvioDeSaldo = 0;

        if (!b.getExtraAntesDeDeslocamento(cod_funcionario, dia).isEmpty()) {
            List<Date> date = b.getExtraAntesDeDeslocamento(cod_funcionario, dia);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.get(0));
            reparaDesvioDeSaldo = ((24 - cal.get(Calendar.HOUR_OF_DAY)) * 60 - cal.get(Calendar.MINUTE));
            if ((saldo - reparaDesvioDeSaldo) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
            }
            //saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
        } else if (!b.getExtraDepoisDeDeslocamento(cod_funcionario, dia).isEmpty()) {
            List<Date> date = b.getExtraDepoisDeDeslocamento(cod_funcionario, dia);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.get(1));
            reparaDesvioDeSaldo = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
            if ((saldo - reparaDesvioDeSaldo) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
            }
            //saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
        } else {
            if ((saldo - minutosContratados) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
            }
            //saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
        }
    }

    public void calcSaldoTurno(List<Jornada> jornadaDiaList, List<TipoHoraExtra> tipoHoraExtra, RegimeHoraExtra regimeHoraExtra, List<Gratificacao> gratificacaoList) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");

        String inicioJornada1;
        String terminioJornada1;
        String inicioJornada2;
        String terminioJornada2;

        if (jornadaDiaList.get(0).getTemVirada()) {
            if (jornadaDiaList.get(0).getSoEntrada()) {
                inicioJornada1 = sdfHora.format(jornadaDiaList.get(0).getInicioJornada());
                terminioJornada1 = "24:00:00";
            } else {
                inicioJornada1 = "00:00:00";
                terminioJornada1 = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
            }
        } else {
            inicioJornada1 = sdfHora.format(jornadaDiaList.get(0).getInicioJornada());
            terminioJornada1 = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
        }

        if (jornadaDiaList.get(1).getTemVirada()) {
            if (jornadaDiaList.get(1).getSoEntrada()) {
                inicioJornada2 = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
                terminioJornada2 = "24:00:00";
            } else {
                inicioJornada2 = "00:00:00";
                terminioJornada2 = sdfHora.format(jornadaDiaList.get(1).getTerminioJornada());
            }
        } else {
            inicioJornada2 = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
            terminioJornada2 = sdfHora.format(jornadaDiaList.get(1).getTerminioJornada());
        }

        Integer saldo1 = 0;
        Integer saldo2 = 0;
        Integer saldo3 = 0;
        Integer saldo4 = 0;
        Integer saldo1Diurno = 0;
        Integer saldo1Noturno = 0;
        Integer saldo2Diurno = 0;
        Integer saldo2Noturno = 0;
        Integer saldo3Diurno = 0;
        Integer saldo3Noturno = 0;
        Integer saldo4Diurno = 0;
        Integer saldo4Noturno = 0;
        Integer saldoDescanco1 = 0;
        Integer saldoDescanco2 = 0;

        boolean puleSaida1 = false;
        boolean puleSaida2 = false;

        String entrada1Fix = entrada_1.getRegistro();
        String saida1Fix = saida_1.getRegistro();
        String entrada2Fix = entrada_2.getRegistro();
        String saida2Fix = saida_2.getRegistro();

        if (!(entrada_1.getRegistro().equals("")) && (!inicioJornada1.equals(""))) {
            saldo1 = calcSaldoRegistroEntrada(entrada1Fix, inicioJornada1);
            saldo1Diurno = calcSaldoRegistroEntradaDiurno(entrada1Fix, inicioJornada1);
            saldo1Noturno = calcSaldoRegistroEntradaNoturno(entrada1Fix, inicioJornada1);
        } else {
            if (jornadaDiaList.get(0).getSoSaida()) {
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            } else {
                saldo1 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                puleSaida1 = true;
            }
        }

        if (!(saida_1.getRegistro().equals("")) && (!terminioJornada1.equals("")) && !(puleSaida1)) {
            saldo2 = calcSaldoRegistroSaida(saida1Fix, terminioJornada1);
            saldo2Diurno = calcSaldoRegistroSaidaDiurno(saida1Fix, terminioJornada1);
            saldo2Noturno = calcSaldoRegistroSaidaNoturno(saida1Fix, terminioJornada1);
        } else {
            if (jornadaDiaList.get(0).getSoEntrada()) {
                saldo2 = 0;
                saldo2Diurno = 0;
                saldo2Noturno = 0;
            } else {
                saldo2 = calcHorasDia(inicioJornada1, terminioJornada1, true);
                saldo1 = 0;
                saldo1Diurno = 0;
                saldo1Noturno = 0;
            }
        }
        Integer adicionalNoturno1 = calcAdicionalNoturno(entrada_1, saida_1, terminioJornada1, jornadaDiaList.get(0));

        if (isDiaCritico) {
            saldoDiaCritico = calcDiaCritico(entrada_1, saida_1, jornadaDiaList.get(0));
        } else {
            calcGratificacao(entrada_1, saida_1, jornadaDiaList.get(0), gratificacaoList);
            saldoDiaCritico = 0;
        }

        if (!(entrada_2.getRegistro().equals("")) && (!inicioJornada2.equals(""))) {
            saldo3 = calcSaldoRegistroEntrada(entrada2Fix, inicioJornada2);
            saldo3Diurno = calcSaldoRegistroEntradaDiurno(entrada2Fix, inicioJornada2);
            saldo3Noturno = calcSaldoRegistroEntradaNoturno(entrada2Fix, inicioJornada2);
        } else {
            saldo3 = calcHorasDia(inicioJornada2, terminioJornada2, true);
            puleSaida2 = true;
        }
        if (!(saida_2.getRegistro().equals("")) && (!terminioJornada2.equals("")) && !(puleSaida2)) {
            saldo4 = calcSaldoRegistroSaida(saida2Fix, terminioJornada2);
            saldo4Diurno = calcSaldoRegistroSaidaDiurno(saida2Fix, terminioJornada2);
            saldo4Noturno = calcSaldoRegistroSaidaNoturno(saida2Fix, terminioJornada2);
        } else {
            if (jornadaDiaList.get(1).getSoEntrada()) {
                saldo4 = 0;
                saldo4Diurno = 0;
                saldo4Noturno = 0;
            } else {
                saldo4 = calcHorasDia(inicioJornada2, terminioJornada2, true);
                saldo3 = 0;
                saldo3Diurno = 0;
                saldo3Noturno = 0;
            }
        }

        Integer adicionalNoturno2 = calcAdicionalNoturno(entrada_2, saida_2, terminioJornada2, jornadaDiaList.get(1));

        if (isDiaCritico) {
            Banco b = new Banco();
            List<Date> listDates = b.consultaDiaComHoraExtra(cod_funcionario, dia);
            if (listDates.size() <= 2) {
                if (listDates.size() != 0) {
                    Date inicio = listDates.get(0);
                    Date termino = listDates.get(1);
                    Calendar cal1 = new GregorianCalendar();
                    Calendar cal2 = new GregorianCalendar();
                    Calendar jorn1 = new GregorianCalendar();
                    Calendar jorn2 = new GregorianCalendar();
                    cal1.setTime(inicio);
                    cal2.setTime(termino);
                    if (jornadaDiaList.get(1).getInicioJornada() != null) {
                        jorn1.setTime(jornadaDiaList.get(1).getInicioJornada());
                    }
                    if (jornadaDiaList.get(1).getTerminioJornada() != null) {
                        jorn2.setTime(jornadaDiaList.get(1).getTerminioJornada());
                    }
                    if (cal1.get(Calendar.HOUR_OF_DAY) != jorn1.get(Calendar.HOUR_OF_DAY) && cal2.get(Calendar.HOUR_OF_DAY) != jorn2.get(Calendar.HOUR_OF_DAY)) {
                        saldoDiaCritico += calcDiaCritico(entrada_2, saida_2, jornadaDiaList.get(1));
                    }
                } else {
                    saldoDiaCritico += calcDiaCritico(entrada_2, saida_2, jornadaDiaList.get(1));
                }
            }
        } else {
            calcGratificacao(entrada_2, saida_2, jornadaDiaList.get(1), gratificacaoList);
            saldoDiaCritico += 0;
        }

        atraso = calcAtraso(saldo1, saldo3);
        qntAtraso16_59(saldo1);
        qntAtrasoMaiorUmaHora(saldo1, saldo3);
        faltaStr();
        atrasoStr(saldo1, saldo3);

        /*  Integer saldoFalta = calcTotalSegundosFalta2Turnos(jornadaDiaList);
        
         saldo1 = calcTolerancia2Turnos(regimeHoraExtra, saldo1, saldo2, saldo3, saldo4, saldoFalta);
         saldo2 = calcTolerancia2Turnos(regimeHoraExtra, saldo2, saldo1, saldo3, saldo4, saldoFalta);
         saldo3 = calcTolerancia2Turnos(regimeHoraExtra, saldo3, saldo1, saldo2, saldo4, saldoFalta);
         saldo4 = calcTolerancia2Turnos(regimeHoraExtra, saldo4, saldo1, saldo2, saldo3, saldoFalta);*/
        Integer tolerancia = regimeHoraExtra.getTolerancia();
        saldo1 = Math.abs(saldo1) > tolerancia * 60 ? saldo1 : 0;
        saldo2 = Math.abs(saldo2) > tolerancia * 60 ? saldo2 : 0;
        saldo3 = Math.abs(saldo3) > tolerancia * 60 ? saldo3 : 0;
        saldo4 = Math.abs(saldo4) > tolerancia * 60 ? saldo4 : 0;

        /*      if (saldoFalta.equals(saldo1)) {
         saldo2 = 0;
         saldo3 = 0;
         saldo4 = 0;
         }
         if (saldoFalta.equals(saldo2)) {
         saldo1 = 0;
         saldo3 = 0;
         saldo4 = 0;
         }
         if (saldoFalta.equals(saldo3)) {
         saldo1 = 0;
         saldo2 = 0;
         saldo4 = 0;
         }
         if (saldoFalta.equals(saldo4)) {
         saldo1 = 0;
         saldo2 = 0;
         saldo3 = 0;
         }
         */
        saldo = (saldo1 + saldo2 + saldo3 + saldo4) / 60;

        saldoDiurno = (saldo1Diurno + saldo2Diurno + saldo3Diurno + saldo4Diurno) / 60;
        saldoDiurno = Math.abs(saldoDiurno) > tolerancia ? saldoDiurno : 0;
        saldoNoturno = (saldo1Noturno + saldo2Noturno + saldo3Noturno + saldo4Noturno) / 60;
        saldoNoturno = Math.abs(saldoNoturno) > tolerancia ? saldoNoturno : 0;

        Integer jornadaTurno1 = calcHorasDia(inicioJornada1, terminioJornada1, false) / 60;
        Integer jornadaTurno2 = calcHorasDia(inicioJornada2, terminioJornada2, false) / 60;
        Integer jornadaTotal = (jornadaTurno1 + jornadaTurno2);
        horasTrabalhadas = transformaEmHora(jornadaTotal, saldo);

        if (isDiaTodoHoraExtra) {
            saldo = jornadaTotal + saldo;
        }

        List<String> nomeTiposHoraExtra = new ArrayList<String>();
        for (Iterator<TipoHoraExtra> it = tipoHoraExtra.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra1 = it.next();
            nomeTiposHoraExtra.add(tipoHoraExtra1.getNome());
        }
        int diaSemana = converterDiaSemana();
        tipoHoraExtra = ordenarHoraExtra(tipoHoraExtra, diaSemana);

        Banco b = new Banco();
        Long milisegundos = b.consultaDeslocadosHoraContratada(cod_funcionario, dia);
        Integer minutosContratados = (int) TimeUnit.MILLISECONDS.toMinutes(milisegundos);
        Integer reparaDesvioDeSaldo = 0;
        if (!b.getExtraAntesDeDeslocamento(cod_funcionario, dia).isEmpty()) {
            List<Date> date = b.getExtraAntesDeDeslocamento(cod_funcionario, dia);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.get(0));
            reparaDesvioDeSaldo = ((24 - cal.get(Calendar.HOUR_OF_DAY)) * 60 - cal.get(Calendar.MINUTE));
            calcHoraExtra(diaSemana, (saldo > 0) ? saldo - reparaDesvioDeSaldo : 0, tipoHoraExtra, nomeTiposHoraExtra);
            if ((saldo - reparaDesvioDeSaldo) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
            }
            //saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
        } else if (!b.getExtraDepoisDeDeslocamento(cod_funcionario, dia).isEmpty()) {
            List<Date> date = b.getExtraDepoisDeDeslocamento(cod_funcionario, dia);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.get(1));
            reparaDesvioDeSaldo = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
            calcHoraExtra(diaSemana, (saldo > 0) ? saldo - reparaDesvioDeSaldo : 0, tipoHoraExtra, nomeTiposHoraExtra);
            if ((saldo - reparaDesvioDeSaldo) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
            }
            //saldoStr = transformaMinutosEmHora(saldo - reparaDesvioDeSaldo);
        } else {
            calcHoraExtra(diaSemana, (saldo > 0) ? saldo - minutosContratados : 0, tipoHoraExtra, nomeTiposHoraExtra);
            if ((saldo - minutosContratados) < regimeHoraExtra.getTolerancia() && (saldo - reparaDesvioDeSaldo) >= 0) {
                saldoStr = transformaMinutosEmHora(0);
            } else {
                saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
            }
            //saldoStr = transformaMinutosEmHora(saldo - minutosContratados);
        }

        adicionalNoturno = adicionalNoturno1 + adicionalNoturno2;

    }

    private String calcAtraso(Integer saldo1) {
        Integer atrasoInt = new Integer(0);
        if (saldo1 < 0) {
            atrasoInt += saldo1 / 60;
        }
        String atraso_;
        if (atrasoInt == 1 || atrasoInt == -1) {
            atraso_ = Math.abs(atrasoInt) + " minuto";
        } else if (atrasoInt == 0) {
            atraso_ = new Integer(Math.abs(atrasoInt)).toString();
        } else {
            atraso_ = Math.abs(atrasoInt) + " minutos";
        }
        return atraso_;
    }

    private String calcAtraso(Integer saldo1, Integer saldo3) {
        Integer atrasoInt = new Integer(0);
        if (saldo1 < 0) {
            atrasoInt += saldo1 / 60;
        }
        if (saldo3 < 0) {
            atrasoInt += saldo3 / 60;
        }
        String atraso_;
        if (atrasoInt == 1 || atrasoInt == -1) {
            atraso_ = Math.abs(atrasoInt) + " minuto";
        } else if (atrasoInt == 0) {
            atraso_ = new Integer(Math.abs(atrasoInt)).toString();
        } else {
            atraso_ = Math.abs(atrasoInt) + " minutos";
        }
        return atraso_;
    }

    private void calcHoraExtra(Integer dia, Integer saldo, List<TipoHoraExtra> tipoHoraExtraList,
            List<String> nomeTiposHoraExtra) {
        tipo_valorHorasExtra = new HashMap<String, Integer>();
        Float saldoParam = Float.parseFloat(saldo.toString());
        Float saldoTipoAnterior = null;
        for (Iterator<TipoHoraExtra> it = tipoHoraExtraList.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra1 = it.next();
            Float saldoTipo;
            if (!isFeriado) {
                saldoTipo = tipoHoraExtra1.getDetalheHoraExtra().get(dia - 1).getInicio() * 3600;
            } else {
                saldoTipo = tipoHoraExtra1.getDetalheHoraExtra().get(7).getInicio() * 3600;
            }
            if (saldoParam > saldoTipo) {
                if (saldoTipoAnterior == null) {
                    Float saldoFloat = saldoParam - saldoTipo;
                    Integer saldoInt = saldoFloat.intValue();
                    tipo_valorHorasExtra.put(tipoHoraExtra1.getNome(), saldoInt);
                    saldoTipoAnterior = saldoTipo;
                } else {
                    Float saldoFloat = saldoTipoAnterior - saldoParam;
                    Integer saldoInt = saldoFloat.intValue();
                    tipo_valorHorasExtra.put(tipoHoraExtra1.getNome(), saldoInt);
                    saldoTipoAnterior = saldoTipo;
                }
            }
        }
        for (Iterator<String> it = nomeTiposHoraExtra.iterator(); it.hasNext();) {
            String nomeTipo = it.next();
            if (tipo_valorHorasExtra.get(nomeTipo) == null) {
                tipo_valorHorasExtra.put(nomeTipo, 0);
            }
        }
    }

    private void calcHoraExtraTeste(Integer dia, Integer saldo, List<TipoHoraExtra> tipoHoraExtraList,
            List<String> nomeTiposHoraExtra) {
        tipo_valorHorasExtra = new HashMap<String, Integer>();
        Float saldoParam = Float.parseFloat(saldo.toString());
        Float saldoTipoAnterior = null;
        for (Iterator<TipoHoraExtra> it = tipoHoraExtraList.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra1 = it.next();
            Float saldoTipo;
            if (!isFeriado) {
                saldoTipo = tipoHoraExtra1.getDetalheHoraExtra().get(dia - 1).getInicio() * 3600;
            } else {
                saldoTipo = tipoHoraExtra1.getDetalheHoraExtra().get(7).getInicio() * 3600;
            }
            if (saldoParam > saldoTipo) {
                if (saldoTipoAnterior == null) {
                    Float saldoFloat = saldoParam - saldoTipo;
                    Integer saldoInt = saldoFloat.intValue();
                    tipo_valorHorasExtra.put(tipoHoraExtra1.getNome(), saldoInt);
                    saldoTipoAnterior = saldoTipo;
                } else {
                    Float saldoFloat = saldoTipoAnterior - saldoParam;
                    Integer saldoInt = saldoFloat.intValue();
                    tipo_valorHorasExtra.put(tipoHoraExtra1.getNome(), saldoInt);
                    saldoTipoAnterior = saldoTipo;
                }
            }
        }
        for (Iterator<String> it = nomeTiposHoraExtra.iterator(); it.hasNext();) {
            String nomeTipo = it.next();
            if (tipo_valorHorasExtra.get(nomeTipo) == null) {
                tipo_valorHorasExtra.put(nomeTipo, 0);
            }
        }
    }

    public List<TipoHoraExtra> ordenarHoraExtra(List<TipoHoraExtra> tipoHoraExtraList, final Integer dia) {

        List<TipoHoraExtra> tipoHoraExtraList_ = new ArrayList<TipoHoraExtra>();
        Boolean isDiaInteiro = false;
        if (isFeriado) {
            isDiaInteiro = true;
        }
        for (Iterator<TipoHoraExtra> it = tipoHoraExtraList.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra = it.next();
            if (tipoHoraExtra.getDetalheHoraExtra().get(dia - 1).getIsDiaInteiro() && !tipoHoraExtra.getIsPadrao()) {
                isDiaInteiro = true;
            }
        }
        for (Iterator<TipoHoraExtra> it = tipoHoraExtraList.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra = it.next();
            if (!isFeriado) {
                if (isDiaInteiro && !tipoHoraExtra.getIsPadrao()) {
                    if (tipoHoraExtra.getDetalheHoraExtra().get(dia - 1).getIsDiaInteiro()) {
                        tipoHoraExtraList_.add(tipoHoraExtra);
                    }
                } else if ((!tipoHoraExtra.getDetalheHoraExtra().get(dia - 1).getInicio().equals(0.0f)) && !tipoHoraExtra.getIsPadrao()) {
                    tipoHoraExtraList_.add(tipoHoraExtra);
                }
            } else {
                if (tipoHoraExtra.getDetalheHoraExtra().get(7).getIsDiaInteiro()) {
                    tipoHoraExtraList_.add(tipoHoraExtra);
                }
            }

            if (tipoHoraExtra.getIsPadrao() && !isDiaInteiro) {
                tipoHoraExtraList_.add(tipoHoraExtra);
            }
        }
        Collections.sort(tipoHoraExtraList_, new Comparator() {
            public int compare(Object o1, Object o2) {
                TipoHoraExtra p1 = (TipoHoraExtra) o1;
                TipoHoraExtra p2 = (TipoHoraExtra) o2;
                return p1.getDetalheHoraExtra().get(dia - 1).getInicio() < p2.getDetalheHoraExtra().get(dia - 1).getInicio() ? +1
                        : (p1.getDetalheHoraExtra().get(dia - 1).getInicio() > p2.getDetalheHoraExtra().get(dia - 1).getInicio() ? -1 : 0);
            }
        });
        return tipoHoraExtraList_;
    }

    private Integer calcSaldoRegistroEntrada(String registroReal, String registroBase) throws NumberFormatException {

        Integer saldo_;

        Long saldoFloat = (Time.valueOf(registroBase.replace("*", "")).getTime() - Time.valueOf(registroReal.replace("*", "")).getTime()) / 1000;

        saldo_ = Integer.parseInt(saldoFloat.toString());

        return saldo_;
    }

    private Integer calcSaldoRegistroEntradaNoturno(String registroReal, String registroBase) throws NumberFormatException {
        Integer saldo_ = 0;

        long real = Time.valueOf(registroReal.replace("*", "")).getTime();
        long _22horas = Time.valueOf("22:00:00").getTime();
        long _05horas = Time.valueOf("05:00:00").getTime();
        long base = Time.valueOf(registroBase.replace("*", "")).getTime();

        Long temp = 0L;

        if (real <= _05horas) {
            if (base <= _05horas) {
                temp = base - real;
            } else {
                temp = _05horas - real;
            }
        } else {
            if (base < _05horas) {
                temp = base - _05horas;
            }
        }

        if (real >= _22horas) {
            if (base >= _22horas) {
                temp = base - real;
            } else {
                temp = _22horas - real;
            }
        } else {
            if (base > _22horas) {
                temp = base - _22horas;
            }
        }
        temp = temp / 1000;
        saldo_ = Integer.parseInt(temp.toString());
        return saldo_;
    }

    private Integer calcSaldoRegistroEntradaDiurno(String registroReal, String registroBase) throws NumberFormatException {
        Integer saldo_ = 0;

        long real = Time.valueOf(registroReal.replace("*", "")).getTime();
        long _22horas = Time.valueOf("22:00:00").getTime();
        long _05horas = Time.valueOf("05:00:00").getTime();
        long base = Time.valueOf(registroBase.replace("*", "")).getTime();

        Long temp = 0L;

        if (real >= _05horas && real <= _22horas) {
            if (base >= _05horas && base <= _22horas) {
                temp = base - real;
            } else {
                if (base < _05horas) {
                    temp = _05horas - real;
                }
                if (base > _22horas) {
                    temp = _22horas - real;
                }
            }
        } else {
            if (real < _05horas) {
                if (base > _05horas && base < _22horas) {
                    temp = base - _05horas;
                }
            }

            if (real > _22horas) {
                if (base > _05horas && base < _22horas) {
                    temp = base - _22horas;
                }
            }
        }

        temp = temp / 1000;
        saldo_ = Integer.parseInt(temp.toString());
        return saldo_;
    }

    private Integer calcSaldoRegistroSaidaNoturno(String registroReal, String registroBase) throws NumberFormatException {
        Integer saldo_ = 0;

        long real = Time.valueOf(registroReal.replace("*", "")).getTime();
        long _22horas = Time.valueOf("22:00:00").getTime();
        long _05horas = Time.valueOf("05:00:00").getTime();
        long base = Time.valueOf(registroBase.replace("*", "")).getTime();

        Long temp = 0L;

        if (real <= _05horas) {
            if (base <= _05horas) {
                temp = real - base;
            } else {
                temp = real - _05horas;
            }
        } else {
            if (base < _05horas) {
                temp = _05horas - base;
            }
        }

        if (real >= _22horas) {
            if (base >= _22horas) {
                temp = real - base;
            } else {
                temp = real - _22horas;
            }
        } else {
            if (base > _22horas) {
                temp = _22horas - base;
            }
        }
        temp = temp / 1000;
        saldo_ = Integer.parseInt(temp.toString());
        return saldo_;
    }

    private Integer calcSaldoRegistroSaidaDiurno(String registroReal, String registroBase) throws NumberFormatException {
        Integer saldo_ = 0;

        long real = Time.valueOf(registroReal.replace("*", "")).getTime();
        long _22horas = Time.valueOf("22:00:00").getTime();
        long _05horas = Time.valueOf("05:00:00").getTime();
        long base = Time.valueOf(registroBase.replace("*", "")).getTime();

        Long temp = 0L;

        if (real >= _05horas && real <= _22horas) {
            if (base >= _05horas && base <= _22horas) {
                temp = real - base;
            } else {
                if (base < _05horas) {
                    temp = real - _05horas;
                }
                if (base > _22horas) {
                    temp = real - _22horas;
                }
            }
        } else {
            if (real < _05horas) {
                if (base >= _05horas && base <= _22horas) {
                    temp = _05horas - base;
                }
            }

            if (real > _22horas) {
                if (base >= _05horas && base <= _22horas) {
                    temp = _22horas - base;
                }
            }
        }

        temp = temp / 1000;
        saldo_ = Integer.parseInt(temp.toString());
        return saldo_;
    }

    private Integer calcAdicionalNoturno(Registro registroEntradaReal_,
            Registro registroSaidaReal_, String registroSaidaBase, Jornada jornada) throws NumberFormatException {

        Integer saldo1 = 0;
        Integer saldo2 = 0;

        String registroEntradaReal = registroEntradaReal_.getRegistro();
        String registroSaidaReal = registroSaidaReal_.getRegistro();

        /* if (registroEntradaReal_.getIsAbonado() && !registroEntradaReal_.getIsTotal()) {
         return 0;
         }

         if (registroSaidaReal_.getIsAbonado() && !registroSaidaReal_.getIsTotal()) {
         return 0;
         }*/
        if (jornada.getSoEntrada()) {
            registroSaidaReal = "24:00:00";

        }

        if (jornada.getSoSaida()) {
            registroEntradaReal = "00:00:00";
        }

        if ((registroEntradaReal_.getRegistro().equals("") && !jornada.getSoSaida()) || (registroSaidaReal_.getRegistro().equals("") && !jornada.getSoEntrada())) {
            return 0;
        }

        Long saldoFloatI = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("22:00:00").getTime()) / 1000;
        //Verifica se a saida está dentro do periodo de adicional noturno (entre 22hs e 24hs)
        if (saldoFloatI > 0) {
            saldoFloatI = (Time.valueOf("22:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
            //Verifica se a entrada se deu anterior ao início do limite de adicional noturno (22hs)
            if (saldoFloatI > 0) {
                //Contabiliza somente o periodo do horário posterior à 22hs
                saldoFloatI = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("22:00:00").getTime()) / 1000;
                saldo1 = Integer.parseInt(saldoFloatI.toString());
            } else {
                saldoFloatI = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
                saldo1 = Integer.parseInt(saldoFloatI.toString());
            }
        } else {
            saldo1 = 0;
        }

        Long saldoFloatF = (Time.valueOf("05:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
        //Verifica se a entrada está dentro do periodo de adicional noturno (entre 00hs e 05hs)
        if (saldoFloatF > 0) {
            saldoFloatF = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("05:00:00").getTime()) / 1000;
            //Verifica se a saida se deu posterior ao fim do limite de adicional noturno (05hs)
            if (saldoFloatF > 0) {
                //Contabiliza somente o periodo do horário anterior à 05hs
                saldoFloatF = (Time.valueOf("05:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
                saldo2 = Integer.parseInt(saldoFloatF.toString());
            } else {
                saldoFloatF = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
                saldo2 = Integer.parseInt(saldoFloatF.toString());
            }
        } else {
            saldo2 = 0;
        }
        /*Long saldoFloatF = (Time.valueOf("05:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
         //Verifica se a entrada está dentro do periodo de adicional noturno (entre 00hs e 05hs)
         if (saldoFloatF > 0) {
         saldoFloatF = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("05:00:00").getTime()) / 1000;
         //Verifica se a saida se deu posterior ao fim do limite de adicional noturno (05hs)
         if (saldoFloatF > 0 && !(Time.valueOf(registroSaidaBase.replace("*", "")).getTime() <= Time.valueOf("05:00:00").getTime()
         && Time.valueOf(registroSaidaReal.replace("*", "")).getTime() > Time.valueOf("05:00:00").getTime())) {

         saldoFloatF = (Time.valueOf("05:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
         saldo2 += Integer.parseInt(saldoFloatF.toString());

         } else {

         saldoFloatF = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
         saldo2 += Integer.parseInt(saldoFloatF.toString());

         }
         } else {
         saldo2 = 0;
         }*/

        /*Double x = (saldo1 + saldo2) * 1.142857142857143;
         x = Math.ceil(x);
         Integer saida = x.intValue();*/
        Integer saida = saldo1 + saldo2;
        return saida;
    }

    /* Alexandre - Regra do adicional antes do concerto em relação computo do adicional com abonos não totais 
     * 
     * private Integer calcAdicionalNoturno(Registro registroEntradaReal_,
     Registro registroSaidaReal_, String registroSaidaBase, Jornada jornada) throws NumberFormatException {
    
     Integer saldo1 = 0;
     Integer saldo2 = 0;
     Boolean isRegistroEntradaAbonodonaoTotal = false;
     Boolean isRegistroSaidaAbonodonaoTotal = false;
     String registroEntradaReal = registroEntradaReal_.getRegistro();
     String registroSaidaReal = registroSaidaReal_.getRegistro();
    
     if (registroEntradaReal_.getIsAbonado() && !registroEntradaReal_.getIsTotal()) {
     isRegistroEntradaAbonodonaoTotal = true;
     }
    
     if (registroSaidaReal_.getIsAbonado() && !registroSaidaReal_.getIsTotal()) {
     isRegistroSaidaAbonodonaoTotal = false;
     }
    
     if (jornada.getSoEntrada()) {
     registroSaidaReal = "24:00:00";
     }
    
     if (jornada.getSoSaida()) {
     registroEntradaReal = "00:00:00";
     }
    
     if ((registroEntradaReal_.getRegistro().equals("") && !jornada.getSoSaida()) || (registroSaidaReal_.getRegistro().equals("") && !jornada.getSoEntrada())) {
     return 0;
     }
    
     Long saldoFloatI = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("22:00:00").getTime()) / 1000;
     if (saldoFloatI > 0) {
     saldoFloatI = (Time.valueOf("22:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
     if (saldoFloatI > 0) {
     if (isRegistroSaidaAbonodonaoTotal) {
     saldo1 = 0;
     } else {
     saldoFloatI = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("22:00:00").getTime()) / 1000;
     saldo1 = Integer.parseInt(saldoFloatI.toString());
     }
     } else {
     if (isRegistroEntradaAbonodonaoTotal) {
     saldo1 = 0;
     } else {
     saldoFloatI = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
     saldo1 = Integer.parseInt(saldoFloatI.toString());
     }
     }
     } else {
     saldo1 = 0;
     }
    
    
     Long saldoFloatF = (Time.valueOf("05:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
     if (saldoFloatF > 0) {
     saldoFloatF = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf("05:00:00").getTime()) / 1000;
     if (saldoFloatF > 0 && !(Time.valueOf(registroSaidaBase.replace("*", "")).getTime() <= Time.valueOf("05:00:00").getTime()
     && Time.valueOf(registroSaidaReal.replace("*", "")).getTime() > Time.valueOf("05:00:00").getTime())) {
     if (isRegistroSaidaAbonodonaoTotal) {
     saldo2 = 0;
     } else {
     saldoFloatF = (Time.valueOf("05:00:00").getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
     saldo2 += Integer.parseInt(saldoFloatF.toString());
     }
     } else {
     if (isRegistroEntradaAbonodonaoTotal) {
     saldo2 = 0;
     } else {
     saldoFloatF = (Time.valueOf(registroSaidaReal.replace("*", "")).getTime() - Time.valueOf(registroEntradaReal.replace("*", "")).getTime()) / 1000;
     saldo2 += Integer.parseInt(saldoFloatF.toString());
     }
     }
     } else {
     saldo2 = 0;
     }
     Double x = (saldo1 + saldo2) * 1.142857142857143;
     x = Math.ceil(x);
     Integer saida = x.intValue();
     return saida;
     }*/
    private HashMap<Integer, Integer> calcGratificacao(Registro registroEntradaReal_,
            Registro registroSaidaReal_, Jornada jornada, List<Gratificacao> gratificacaoList) throws NumberFormatException {

        String registroEntradaReal = registroEntradaReal_.getRegistro();
        String registroSaidaReal = registroSaidaReal_.getRegistro();

        if (registroEntradaReal_.getIsAbonado() && !registroEntradaReal_.getIsTotal()) {
            registroEntradaReal = "";
        }

        if (registroSaidaReal_.getIsAbonado() && !registroSaidaReal_.getIsTotal()) {
            registroSaidaReal = "";
        }

        for (Iterator<Gratificacao> it = gratificacaoList.iterator(); it.hasNext();) {
            Gratificacao gratificacao1 = it.next();

            if (saidaGratHash.get(gratificacao1.getCod_gratificacao()) == null) {
                saidaGratHash.put(gratificacao1.getCod_gratificacao(), 0);
            }

            if (jornada.getSoEntrada()) {
                registroSaidaReal = "24:00:00";
            }

            if (jornada.getSoSaida()) {
                registroEntradaReal = "00:00:00";
            }

            if (!(registroEntradaReal.equals("") || registroSaidaReal.equals(""))) {

                List<DetalheGratificacao> detalheGratificacao = new ArrayList<DetalheGratificacao>();
                detalheGratificacao = gratificacao1.getDetalheGratificacaoList();
                for (Iterator<DetalheGratificacao> it1 = detalheGratificacao.iterator(); it1.hasNext();) {
                    DetalheGratificacao detalheGratificacao1 = it1.next();
                    GregorianCalendar g = new GregorianCalendar();
                    g.setTime(dia);
                    Integer diaSemana = g.get(Calendar.DAY_OF_WEEK);
                    if (diaSemana.equals(detalheGratificacao1.getDia()) && (detalheGratificacao1.getInicio() != null && detalheGratificacao1.getFim() != null)) {

                        String gratificacaoInicio = detalheGratificacao1.getInicio();
                        String gratificacaoFim = detalheGratificacao1.getFim().equals("00:00:00") ? "24:00:00" : detalheGratificacao1.getFim();

                        Long gratInicioTime = Time.valueOf(gratificacaoInicio).getTime();
                        Long gratFimTime = Time.valueOf(gratificacaoFim).getTime();

                        Long saidaRealTime = Time.valueOf(registroSaidaReal.replace("*", "")).getTime();
                        Long entradaReaTime = Time.valueOf(registroEntradaReal.replace("*", "")).getTime();

                        Banco b = new Banco();
                        List<Date> listDates = b.consultaDiaComHoraExtra(cod_funcionario, dia);
                        for (Iterator<Date> it3 = listDates.iterator(); it3.hasNext();) {
                            Date data = it3.next();
                        }
                        if (listDates.size() <= 2) {
                            if (listDates.size() != 0) {
                                Date inicio = listDates.get(0);
                                Date termino = listDates.get(1);
                                Calendar cal1 = new GregorianCalendar();
                                Calendar cal2 = new GregorianCalendar();
                                Calendar jorn1 = new GregorianCalendar();
                                Calendar jorn2 = new GregorianCalendar();
                                cal1.setTime(inicio);
                                cal2.setTime(termino);
                                if (jornada.getInicioJornada() != null) {
                                    jorn1.setTime(jornada.getInicioJornada());
                                }
                                if (jornada.getTerminioJornada() != null) {
                                    jorn2.setTime(jornada.getTerminioJornada());
                                }
                                if (cal1.get(Calendar.HOUR_OF_DAY) != jorn1.get(Calendar.HOUR_OF_DAY) && cal2.get(Calendar.HOUR_OF_DAY) != jorn2.get(Calendar.HOUR_OF_DAY)) {
                                    if (!((gratFimTime < entradaReaTime) || (gratInicioTime > saidaRealTime))) {
                                        if (gratInicioTime < entradaReaTime) {
                                            if (gratFimTime < saidaRealTime) {
                                                Long resultadoLong = (gratFimTime - entradaReaTime) / 1000;
                                                Integer resultado = Integer.parseInt(resultadoLong.toString());
                                                saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                            } else {
                                                Long resultadoLong = (saidaRealTime - entradaReaTime) / 1000;
                                                Integer resultado = Integer.parseInt(resultadoLong.toString());
                                                saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                            }
                                        } else {
                                            if (gratFimTime < saidaRealTime) {
                                                Long resultadoLong = (gratFimTime - gratInicioTime) / 1000;
                                                Integer resultado = Integer.parseInt(resultadoLong.toString());
                                                saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                            } else {
                                                Long resultadoLong = (saidaRealTime - gratInicioTime) / 1000;
                                                Integer resultado = Integer.parseInt(resultadoLong.toString());
                                                saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (!((gratFimTime < entradaReaTime) || (gratInicioTime > saidaRealTime))) {
                                    if (gratInicioTime < entradaReaTime) {
                                        if (gratFimTime < saidaRealTime) {
                                            Long resultadoLong = (gratFimTime - entradaReaTime) / 1000;
                                            Integer resultado = Integer.parseInt(resultadoLong.toString());
                                            saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                        } else {
                                            Long resultadoLong = (saidaRealTime - entradaReaTime) / 1000;
                                            Integer resultado = Integer.parseInt(resultadoLong.toString());
                                            saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                        }
                                    } else {
                                        if (gratFimTime < saidaRealTime) {
                                            Long resultadoLong = (gratFimTime - gratInicioTime) / 1000;
                                            Integer resultado = Integer.parseInt(resultadoLong.toString());
                                            saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                        } else {
                                            Long resultadoLong = (saidaRealTime - gratInicioTime) / 1000;
                                            Integer resultado = Integer.parseInt(resultadoLong.toString());
                                            saidaGratHash.put(gratificacao1.getCod_gratificacao(), saidaGratHash.get(gratificacao1.getCod_gratificacao()) + resultado);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return saidaGratHash;
    }

    private Integer calcDiaCritico(Registro registroEntradaReal_,
            Registro registroSaidaReal_, Jornada jornada) {

        String registroEntradaReal = registroEntradaReal_.getRegistro();
        String registroSaidaReal = registroSaidaReal_.getRegistro();

        if (registroEntradaReal_.getIsAbonado() && !registroEntradaReal_.getIsTotal()) {
            registroEntradaReal = "";
        }

        if (registroSaidaReal_.getIsAbonado() && !registroSaidaReal_.getIsTotal()) {
            registroSaidaReal = "";
        }

        if (jornada.getSoEntrada()) {
            registroSaidaReal = "24:00:00";
        }

        if (jornada.getSoSaida()) {
            registroEntradaReal = "00:00:00";
        }
        if (registroEntradaReal.equals("") || registroSaidaReal.equals("")) {
            return 0;
        } else {
            Long saidaRealTime = Time.valueOf(registroSaidaReal.replace("*", "")).getTime();
            Long entradaReaTime = Time.valueOf(registroEntradaReal.replace("*", "")).getTime();

            Long saida = (saidaRealTime - entradaReaTime) / 1000;
            Integer resultado = Integer.parseInt(saida.toString());
            return resultado;
        }
    }

    private Integer calcSaldoRegistroSaida(String registroReal, String registroBase) throws NumberFormatException {
        Integer saldo_;

        Long saldoFloat = (Time.valueOf(registroReal.replace("*", "")).getTime() - Time.valueOf(registroBase.replace("*", "")).getTime()) / 1000;
        saldo_ = Integer.parseInt(saldoFloat.toString());
        return saldo_;
    }

    /*private Integer calcSaldoRegistroSaidaHoraExtraNoturna(String registroReal, String registroBase) throws NumberFormatException {
     Integer saldo_;

     Long saldoFloatI = (Time.valueOf(registroReal.replace("*", "")).getTime() - Time.valueOf("22:00:00").getTime()) / 1000;
     if (saldoFloatI > 0) {
     }

     Long saldoFloat = (Time.valueOf(registroReal.replace("*", "")).getTime() - Time.valueOf(registroBase.replace("*", "")).getTime()) / 1000;
     saldo_ = Integer.parseInt(saldoFloat.toString());
     return saldo_;
     }

     private Integer calcTolerancia1Turno(RegimeHoraExtra regimeHoraExtra, Integer saldo, Integer saldoFalta) {
     Integer saida = 0;
     if (regimeHoraExtra.getModoTolerancia() == null || regimeHoraExtra.getModoTolerancia() == 1) {
     saida = calcRegraCLT1Turno(saldo);
     } else if (regimeHoraExtra.getModoTolerancia() == 2) {
     saida = calcRegraEstatutario(saldo, saldoFalta);
     }
     return saida;
     }

     private Integer calcTolerancia2Turnos(RegimeHoraExtra regimeHoraExtra, Integer saldoAnalisado,
     Integer saldo_2, Integer saldo_3, Integer saldo_4, Integer saldoFalta) {
     Integer saida = 0;
     if (regimeHoraExtra.getModoTolerancia() == null || regimeHoraExtra.getModoTolerancia() == 1) {
     saida = calcRegraCLT2Turnos(saldoAnalisado, saldo_2, saldo_3, saldo_4);
     } else if (regimeHoraExtra.getModoTolerancia() == 2) {
     saida = calcRegraEstatutario(saldoAnalisado, saldoFalta);
     }
     return saida;
     }*/
    private Integer calcRegraEstatutario(Integer saldo, Integer saldoFalta) {

        if (saldo < - 8160) {
            saldo = saldoFalta;
        }
        if (saldo >= -8160 && saldo < -4560) {
            saldo = -7200;
        } else if (saldo >= -4560 && saldo < -960) {
            saldo = -3600;
        } else if (saldo >= -960 && saldo < 960) {
            saldo = 0;
        } else if (saldo >= 960 && saldo < 4560) {
            saldo = 3600;
        } else if (saldo >= 4560 && saldo < 8160) {
            saldo = 7200;
        } else if (saldo >= 8160 && saldo < 11760) {
            saldo = 10800;
        } else if (saldo >= 11760 && saldo < 15360) {
            saldo = 14400;
        } else if (saldo >= 15360 && saldo < 18960) {
            saldo = 18000;
        }

        return saldo;
    }

    private Integer calcRegraCLT1Turno(Integer saldo) {
        Integer saida = 0;
        saida = Math.abs(saldo) > 300 ? saldo : 0;
        return saida;
    }

    private Integer calcRegraCLT2Turnos(Integer saldoAnalisado, Integer saldo_2, Integer saldo_3, Integer saldo_4) {

        Integer saida = 0;

        Integer saidaNegativo = 0;
        Integer saidaPositivo = 0;

        if (saldoAnalisado > 0) {
            saidaPositivo += saldoAnalisado;
        } else {
            saidaNegativo += saldoAnalisado;
        }
        if (saldo_2 > 0) {
            saidaPositivo += saldo_2;
        } else {
            saidaNegativo += saldo_2;
        }
        if (saldo_3 > 0) {
            saidaPositivo += saldo_3;
        } else {
            saidaNegativo += saldo_3;
        }
        if (saldo_4 > 0) {
            saidaPositivo += saldo_4;
        } else {
            saidaNegativo += saldo_4;
        }

        if (saldoAnalisado > 0) {
            if (saidaPositivo > 600) {
                saida = saldoAnalisado;
            } else {
                saida = Math.abs(saldoAnalisado) > 300 ? saldoAnalisado : 0;
            }
        } else {
            if (saidaNegativo < -600) {
                saida = saldoAnalisado;
            } else {
                saida = Math.abs(saldoAnalisado) > 300 ? saldoAnalisado : 0;
            }
        }
        return saida;
    }

    public Integer calcHorasDia(String inicioJornada, String terminioJornada, boolean ehParaSubtrair) {
        Integer saldo_ = 0;
        if (inicioJornada != "" && terminioJornada != "") {
            String[] inicioJornadaArray = inicioJornada.split(":");
            String[] terminioJornadaArray = terminioJornada.split(":");

            Integer parteHoraI = Integer.parseInt(inicioJornadaArray[0]) * 3600;
            Integer parteMinutosI = (Integer.parseInt(inicioJornadaArray[1]) * 60);
            Integer parteSegundosI = (Integer.parseInt(inicioJornadaArray[2]));

            Integer parteHoraT = Integer.parseInt(terminioJornadaArray[0]) * 3600;
            Integer parteMinutosT = (Integer.parseInt(terminioJornadaArray[1]) * 60);
            Integer parteSegundosT = (Integer.parseInt(terminioJornadaArray[2]));

            Integer inicioJornadaFloat = parteHoraI + parteMinutosI + parteSegundosI;
            Integer terminioJornadaFloat = parteHoraT + parteMinutosT + parteSegundosT;

            if (ehParaSubtrair) {
                saldo_ = inicioJornadaFloat - terminioJornadaFloat;
            } else {
                saldo_ = terminioJornadaFloat - inicioJornadaFloat;
            }
        }
        return saldo_;
    }

    /*private Integer calcTotalSegundosFalta2Turnos(List<Jornada> jornadaDiaList) {

     SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
     String inicioJornada1;
     String terminioJornada1;
     String inicioJornada2;
     String terminioJornada2;

     if (jornadaDiaList.get(0).getTemVirada()) {
     if (jornadaDiaList.get(0).getSoEntrada()) {
     inicioJornada1 = sdfHora.format(jornadaDiaList.get(0).getInicioJornada());
     terminioJornada1 = "24:00:00";
     } else {
     inicioJornada1 = "00:00:00";
     terminioJornada1 = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
     }
     } else {
     inicioJornada1 = sdfHora.format(jornadaDiaList.get(0).getInicioJornada());
     terminioJornada1 = sdfHora.format(jornadaDiaList.get(0).getTerminioJornada());
     }

     if (jornadaDiaList.get(1).getTemVirada()) {
     if (jornadaDiaList.get(1).getSoEntrada()) {
     inicioJornada2 = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
     terminioJornada2 = "24:00:00";
     } else {
     inicioJornada2 = "00:00:00";
     terminioJornada2 = sdfHora.format(jornadaDiaList.get(1).getTerminioJornada());
     }
     } else {
     inicioJornada2 = sdfHora.format(jornadaDiaList.get(1).getInicioJornada());
     terminioJornada2 = sdfHora.format(jornadaDiaList.get(1).getTerminioJornada());
     }

     Integer saldo1 = calcHorasDia(inicioJornada1, terminioJornada1, true);
     Integer saldo2 = calcHorasDia(inicioJornada2, terminioJornada2, true);
     return saldo1 + saldo2;

     }*/

    /*private Integer calcTotalSegundosFalta1Turno(Jornada jornada) {

     SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");

     String inicioJornada1;
     if (jornada.getSoSaida()) {
     inicioJornada1 = "00:00:00";
     } else {
     inicioJornada1 = sdfHora.format(jornada.getInicioJornada());
     }

     String terminioJornada1;
     if (jornada.getSoEntrada()) {
     terminioJornada1 = jornada.getTerminioJornada_();
     } else {
     terminioJornada1 = sdfHora.format(jornada.getTerminioJornada());
     }
     Integer saldo1 = calcHorasDia(inicioJornada1, terminioJornada1, true);

     return saldo1;

     }*/
    public void setCorDia(List<Jornada> jornadaDiaList) {

        String entradaTurno1 = this.entrada_1.getRegistro();
        String saidaTurno1 = this.saida_1.getRegistro();

        String entradaTurno2 = this.entrada_2.getRegistro();
        String saidaTurno2 = this.saida_2.getRegistro();

        boolean entrada1Boolean = entradaTurno1.equals("");
        boolean saida1Boolean = saidaTurno1.equals("");
        boolean entrada2Boolean = entradaTurno2.equals("");
        boolean saida2Boolean = saidaTurno2.equals("");
        boolean ok1 = false;
        boolean ok2 = false;

        if (jornadaDiaList.size() == 2) {
            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {
                    if (!entrada1Boolean) {
                        ok1 = true;
                    }
                } else if (!saida1Boolean) {
                    ok1 = true;
                }
            } else {
                if (!entrada1Boolean && !saida1Boolean) {
                    ok1 = true;
                }
            }

            if (jornadaDiaList.get(1).getTemVirada()) {
                if (jornadaDiaList.get(1).getSoEntrada()) {
                    if (!entrada2Boolean) {
                        ok2 = true;
                    }
                } else if (!saida2Boolean) {
                    ok2 = true;
                }
            } else {
                if (!entrada2Boolean && !saida2Boolean) {
                    ok2 = true;
                }
            }

            if (ok1 && ok2) {
                this.setColorDia("panelBranco");
                this.setDefinicao("Regular");
            } else {
                this.setColorDia("panelVermelho");
                this.setDefinicao("Irregular");
            }
        } else {
            if (jornadaDiaList.get(0).getTemVirada()) {
                if (jornadaDiaList.get(0).getSoEntrada()) {
                    if (((entrada1Boolean))) {
                        this.setColorDia("panelVermelho");
                        this.setDefinicao("Irregular");
                    } else if (!entrada1Boolean) {
                        this.setColorDia("panelBranco");
                        this.setDefinicao("Regular");
                    }
                } else if (jornadaDiaList.get(0).getSoSaida()) {
                    if (((saida1Boolean))) {
                        this.setColorDia("panelVermelho");
                        this.setDefinicao("Irregular");
                    } else if (!saida1Boolean) {
                        this.setColorDia("panelBranco");
                        this.setDefinicao("Regular");
                    }
                }
            } else {
                if (((entrada1Boolean) || (saida1Boolean))) {
                    this.setColorDia("panelVermelho");
                    this.setDefinicao("Irregular");
                } else if (!entrada1Boolean && !saida1Boolean) {
                    this.setColorDia("panelBranco");
                    this.setDefinicao("Regular");

                }
            }
        }
    }

    private String transformaEmHora(Integer tempo1, Integer tempo2) {

        Integer saida = tempo1 + tempo2;

        Integer horas = Math.abs(saida / 60);
        Integer minutos = Math.abs(saida % 60);
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

        return horaSrt + "h:" + minutosSrt + "m";
    }

    private long calcAdicionalNoturnoRelogio() {
        if (!isFeriado) {
            Double saidaDouble = adicionalNoturno / 1.142857142857143;
            Integer saida = Math.round(saidaDouble.floatValue());
            long minutos = Math.abs((saida / 60));
            return minutos;
        } else {
            return 0;
        }
    }

    private void qntAtraso16_59(Integer saldo) {
        if (!isDiaTodoHoraExtra && presente) {
            Integer saida = 0;
            if (saldo >= -3600 && saldo < -960) {
                saida = 1;
            }
            qntAtraso16_59 = saida;
        }
    }

    private void qntAtraso16_59(Integer saldo1, Integer saldo2) {
        if (!isDiaTodoHoraExtra && presente) {
            Integer temp1 = 0;
            Integer temp2 = 0;
            if (saldo1 >= -3600 && saldo1 < -960) {
                temp1 = 1;
            }
            if (saldo2 >= -3600 && saldo2 < -960) {
                temp2 = 1;
            }
            qntAtraso16_59 = temp1 + temp2;
        }
    }

    private void qntAtrasoMaiorUmaHora(Integer saldo) {
        if (!isDiaTodoHoraExtra && presente) {
            Integer saida = 0;
            if (saldo < -3600) {
                saida = 1;
            }
            qntAtrasoMaiorUmaHora = saida;
        }
    }

    private void qntAtrasoMaiorUmaHora(Integer saldo1, Integer saldo2) {
        if (!isDiaTodoHoraExtra && presente) {
            Integer temp1 = 0;
            Integer temp2 = 0;
            if (saldo1 < -3600) {
                temp1 = 1;
            }
            if (saldo2 < -3600) {
                temp2 = 1;
            }
            qntAtrasoMaiorUmaHora = temp1 + temp2;
        }
    }

    private void faltaStr() {
        faltaDiaStr = null;
        if (!presente) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            faltaDiaStr = sdf.format(dia);
        }
    }

    private void atrasoStr(Integer saldo1, Integer saldo3) {
        atrasoDiaStr = null;
        if (!isDiaTodoHoraExtra && presente) {
            Boolean teste1_16_59 = (saldo1 >= -3600 && saldo1 < -960);
            Boolean teste2_16_59 = (saldo3 >= -3600 && saldo3 < -960);
            Boolean teste1_maior_1_hora = (saldo1 < -3600);
            Boolean teste2_maior_1_hora = (saldo3 < -3600);
            if (teste1_16_59 && !teste2_16_59) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                atrasoDiaStr = sdf.format(dia) + transformaMinutosEmHora(saldo1 / 60);
            }
            if (teste1_16_59 && teste2_16_59) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                atrasoDiaStr = sdf.format(dia) + transformaMinutosEmHora(saldo1 / 60) + "\n"
                        + sdf.format(dia) + transformaMinutosEmHora(saldo3 / 60);
            }
            if (teste2_16_59 && !teste1_16_59) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                atrasoDiaStr = sdf.format(dia) + transformaMinutosEmHora(saldo3 / 60);
            }

            if (teste1_maior_1_hora && !teste2_maior_1_hora) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                if (atrasoDiaStr == null) {
                    atrasoDiaStr = "";
                } else {
                    atrasoDiaStr += "\n";
                }
                atrasoDiaStr += sdf.format(dia) + transformaMinutosEmHora(saldo1 / 60);
            }

            if (teste1_maior_1_hora && teste2_maior_1_hora) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                if (atrasoDiaStr == null) {
                    atrasoDiaStr = "";
                } else {
                    atrasoDiaStr += "\n";
                }
                atrasoDiaStr += sdf.format(dia) + transformaMinutosEmHora(saldo1 / 60) + "\n"
                        + sdf.format(dia) + transformaMinutosEmHora(saldo3 / 60);
            }

            if (teste2_maior_1_hora && !teste1_maior_1_hora) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                if (atrasoDiaStr == null) {
                    atrasoDiaStr = "";
                } else {
                    atrasoDiaStr += "\n";
                }
                atrasoDiaStr += sdf.format(dia) + transformaMinutosEmHora(saldo3 / 60);
            }
        }
    }

    private void atrasoStr(Integer saldo1) {
        atrasoDiaStr = null;
        if (!isDiaTodoHoraExtra && presente) {
            if (qntAtraso16_59 == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                atrasoDiaStr = sdf.format(dia) + transformaMinutosEmHora(saldo1 / 60);
            }

            if (qntAtrasoMaiorUmaHora == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                if (atrasoDiaStr == null) {
                    atrasoDiaStr = "";
                }
                atrasoDiaStr += sdf.format(dia) + transformaMinutosEmHora(saldo1 / 60);
            }
        }
    }

    public String getAdicionalNoturnoRelogio() {
        if (!isFeriado) {
            long saidaMinutos = calcAdicionalNoturnoRelogio();
            Integer saida = new Long(saidaMinutos).intValue();
            if (saldoNoturno > 0) {
                saida = saida - saldoNoturno;
            }
            String saidaStr = transformaMinutosEmHora(saida);
            return saidaStr;
        } else {
            return "00h:00m";
        }
    }

    public String getHorasDiurnasTrabalhadasNaoExtraordinarias() {
        if (!isFeriado) {
            Long horasTotalMinutos = transformaHoraEmMinutos(horasTrabalhadas.replace("h", "").replace("m", ""));
            Long horasNoturnas = calcAdicionalNoturnoRelogio();
            Long saida = horasTotalMinutos - horasNoturnas;
            Integer saidaInt = saida.intValue();
            if (saldoDiurno > 0) {
                saidaInt = saidaInt - saldoDiurno;
            }
            String saidaStr = transformaMinutosEmHora(saidaInt);
            return saidaStr;
        } else {
            return "00:00";
        }
    }

    private static String transformaMinutosEmHora(Integer time) {

        Integer saida = time;
        Integer horas = Math.abs(saida / 60);
        Integer minutos = Math.abs((saida) % 60);
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

        if (time < 0) {
            horaSrt = "- " + horaSrt;
        }

        return horaSrt + "h:" + minutosSrt + "m";
    }

    public long transformaEmMilisegundos(String ponto) {

        String[] pontoArray = ponto.split(":");

        Integer hora = Integer.parseInt(pontoArray[0]) * 3600;
        Integer minutos = (Integer.parseInt(pontoArray[1]) * 60);
        Integer segundos = (Integer.parseInt(pontoArray[2].replace("*", "")));

        long horaMilisegundos = hora + minutos + segundos;

        return horaMilisegundos;
    }

    public long transformaHoraEmMinutos(String ponto) {

        String[] pontoArray = ponto.split(":");

        Integer hora = Integer.parseInt(pontoArray[0]) * 60;
        Integer minutos = (Integer.parseInt(pontoArray[1]));

        long saida = hora + minutos;

        return saida;
    }

    public String getDefinicao() {
        return definicao;
    }

    public void setDefinicao(String definicao) {
        this.definicao = definicao;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getDiaString() {
        return diaString;
    }

    public void setDiaString(String diaString) {
        this.diaString = diaString;
    }

    public List<SituacaoPonto> getSituacaoPonto() {
        return situacaoPonto;
    }

    public void setSituacaoPonto(Ponto ponto, String local, String situacao) {
        SituacaoPonto situacao_ = new SituacaoPonto(ponto, local, situacao);
        if (ponto.getIsAbonado()) {
            situacao_.setIsAbonado(true);
        } else {
            situacao_.setIsAbonado(false);
        }
        if (ponto.getIsPreAssinalado()) {
            situacao_.setIsPreAssinalado(true);
        } else {
            situacao_.setIsPreAssinalado(false);
        }
        if (situacao.lastIndexOf("Descanso") != -1) {
            situacao_.setIsDescanso(true);
        } else {
            situacao_.setIsDescanso(false);
        }
        situacaoPonto.add(situacao_);
    }

    public void delSituacaoPonto() {
        situacaoPonto = new ArrayList<SituacaoPonto>();
    }

    public Boolean getTemIntervalo() {
        return temIntervalo;
    }

    public void setTemIntervalo(Boolean temIntervalo) {
        this.temIntervalo = temIntervalo;
    }

    public String getColorDia() {
        return colorDia;
    }

    public void setColorDia(String colorDia) {
        this.colorDia = colorDia;
    }

    public String getSaldoHoras() {
        return saldoStr;
    }

    public void setSaldoHoras(String saldoHoras) {
        this.saldoStr = saldoHoras;
    }

    public String getAtraso() {
        return atraso;
    }

    public void setAtraso(String atraso) {
        this.atraso = atraso;
    }

    public String getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(String horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Integer getMinutosTrabalhados() {
        return saldo;
    }

    public void setMinutosTrabalhados(Integer minutosTrabalhados) {
        this.saldo = minutosTrabalhados;
    }

    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    public Map<String, Integer> getTipo_valorHorasExtra() {
        return tipo_valorHorasExtra;
    }

    public void setTipo_valorHorasExtra(Map<String, Integer> tipo_valorHorasExtra) {
        this.tipo_valorHorasExtra = tipo_valorHorasExtra;
    }

    public Boolean getIsDiaTodoHoraExtra() {
        return isDiaTodoHoraExtra;
    }

    public void setIsDiaTodoHoraExtra(Boolean isDiaTodoHoraExtra) {
        this.isDiaTodoHoraExtra = isDiaTodoHoraExtra;
    }

    public Boolean getIsFeriado() {
        return isFeriado;
    }

    public void setIsFeriado(Boolean isFeriado) {
        this.isFeriado = isFeriado;
    }

    public Integer getAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(Integer adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    public HashMap<Integer, Integer> getSaidaGratHash() {
        return saidaGratHash;
    }

    public void setSaidaGratHash(HashMap<Integer, Integer> saidaGratHash) {
        this.saidaGratHash = saidaGratHash;
    }

    private Integer converterDiaSemana() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dia);
        int diaSemana = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
        switch (diaSemana) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            default:
                return null;
        }
    }

    public Boolean getIsPendente() {
        return isPendente;
    }

    public void setIsPendente(Boolean isPendente) {
        this.isPendente = isPendente;
    }

    public Boolean getIsDiaCritico() {
        return isDiaCritico;
    }

    public void setIsDiaCritico(Boolean isDiaCritico) {
        this.isDiaCritico = isDiaCritico;
    }

    public Integer getSaldoDiaCritico() {
        return saldoDiaCritico;
    }

    public void setSaldoDiaCritico(Integer saldoDiaCritico) {
        this.saldoDiaCritico = saldoDiaCritico;
    }

    public Registro getEntrada_1() {
        return entrada_1;
    }

    public void setEntrada_1(Registro entrada_1) {
        this.entrada_1 = entrada_1;
    }

    public Registro getEntrada_2() {
        return entrada_2;
    }

    public void setEntrada_2(Registro entrada_2) {
        this.entrada_2 = entrada_2;
    }

    public Registro getSaida_1() {
        return saida_1;
    }

    public void setSaida_1(Registro saida_1) {
        this.saida_1 = saida_1;
    }

    public Registro getSaida_2() {
        return saida_2;
    }

    public void setSaida_2(Registro saida_2) {
        this.saida_2 = saida_2;
    }

    public Integer getSaldoDiurno() {
        return saldoDiurno;
    }

    public void setSaldoDiurno(Integer saldoDiurno) {
        this.saldoDiurno = saldoDiurno;
    }

    public Integer getSaldoNoturno() {
        return saldoNoturno;
    }

    public void setSaldoNoturno(Integer saldoNoturno) {
        this.saldoNoturno = saldoNoturno;
    }

    public Boolean getIsAfastado() {
        return isAfastado;
    }

    public void setIsAfastado(Boolean isAfastado) {
        this.isAfastado = isAfastado;
    }

    public static void main(String[] args) throws SQLException {
        Double d = Math.ceil(999.1);
        System.out.print(d);
    }

    public Integer getQntAtraso16_59() {
        return qntAtraso16_59;
    }

    public void setQntAtraso16_59(Integer qntAtraso16_59) {
        this.qntAtraso16_59 = qntAtraso16_59;
    }

    public Integer getQntAtrasoMaiorUmaHora() {
        return qntAtrasoMaiorUmaHora;
    }

    public void setQntAtrasoMaiorUmaHora(Integer qntAtrasoMaiorUmaHora) {
        this.qntAtrasoMaiorUmaHora = qntAtrasoMaiorUmaHora;
    }

    public String getAtrasoDiaStr() {
        return atrasoDiaStr;
    }

    public void setAtrasoDiaStr(String atrasoDiaStr) {
        this.atrasoDiaStr = atrasoDiaStr;
    }

    public String getFaltaDiaStr() {
        return faltaDiaStr;
    }

    public void setFaltaDiaStr(String faltaDiaStr) {
        this.faltaDiaStr = faltaDiaStr;
    }

    public RegimeHoraExtra getRegimeHoraExtra() {
        return regimeHoraExtra;
    }

    public void setRegimeHoraExtra(RegimeHoraExtra regimeHoraExtra) {
        this.regimeHoraExtra = regimeHoraExtra;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public Registro getEntrada_Descanso_1_1() {
        return entrada_Descanso_1_1;
    }

    public void setEntrada_Descanso_1_1(Registro entrada_Descanso_1_1) {
        this.entrada_Descanso_1_1 = entrada_Descanso_1_1;
    }

    public Registro getSaida_Descanso_1_1() {
        return saida_Descanso_1_1;
    }

    public void setSaida_Descanso_1_1(Registro saida_Descanso_1_1) {
        this.saida_Descanso_1_1 = saida_Descanso_1_1;
    }

    public Registro getEntrada_Interjornada_1() {
        return entrada_Interjornada_1;
    }

    public void setEntrada_Interjornada_1(Registro entrada_Interjornada_1) {
        this.entrada_Interjornada_1 = entrada_Interjornada_1;
    }

    public Registro getSaida_Interjornada_1() {
        return saida_Interjornada_1;
    }

    public void setSaida_Interjornada_1(Registro saida_Interjornada_1) {
        this.saida_Interjornada_1 = saida_Interjornada_1;
    }

    public Registro getEntrada_Descanso_2_1() {
        return entrada_Descanso_2_1;
    }

    public void setEntrada_Descanso_2_1(Registro entrada_Descanso_2_1) {
        this.entrada_Descanso_2_1 = entrada_Descanso_2_1;
    }

    public Registro getSaida_Descanso_2_1() {
        return saida_Descanso_2_1;
    }

    public void setSaida_Descanso_2_1(Registro saida_Descanso_2_1) {
        this.saida_Descanso_2_1 = saida_Descanso_2_1;
    }

    public Registro getEntrada_Descanso_1_2() {
        return entrada_Descanso_1_2;
    }

    public void setEntrada_Descanso_1_2(Registro entrada_Descanso_1_2) {
        this.entrada_Descanso_1_2 = entrada_Descanso_1_2;
    }

    public Registro getSaida_Descanso_1_2() {
        return saida_Descanso_1_2;
    }

    public void setSaida_Descanso_1_2(Registro saida_Descanso_1_2) {
        this.saida_Descanso_1_2 = saida_Descanso_1_2;
    }

    public Registro getEntrada_Interjornada_2() {
        return entrada_Interjornada_2;
    }

    public void setEntrada_Interjornada_2(Registro entrada_Interjornada_2) {
        this.entrada_Interjornada_2 = entrada_Interjornada_2;
    }

    public Registro getSaida_Interjornada_2() {
        return saida_Interjornada_2;
    }

    public void setSaida_Interjornada_2(Registro saida_Interjornada_2) {
        this.saida_Interjornada_2 = saida_Interjornada_2;
    }

    public Registro getEntrada_Descanso_2_2() {
        return entrada_Descanso_2_2;
    }

    public void setEntrada_Descanso_2_2(Registro entrada_Descanso_2_2) {
        this.entrada_Descanso_2_2 = entrada_Descanso_2_2;
    }

    public Registro getSaida_Descanso_2_2() {
        return saida_Descanso_2_2;
    }

    public void setSaida_Descanso_2_2(Registro saida_Descanso_2_2) {
        this.saida_Descanso_2_2 = saida_Descanso_2_2;
    }

}
