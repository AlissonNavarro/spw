package RelatorioMensal;

import java.io.Serializable;

public class RelatorioResumoFrequenciaTotalizador implements Serializable {

    private String saldo;
    private String desconto;
    private String falta;
    private String horasExtra;
    private String Gratificacao;
    private String adicionalNoturno;
    private String descontoDSR;
    private Integer saldoInt;
    private Integer descontoInt;
    private Integer faltaInt;
    private Integer horasExtraInt;
    private Integer GratificacaoInt;
    private Integer adicionalNoturnoInt;
    private Integer descontoDSRInt;

    public String getGratificacao() {
        return Gratificacao;
    }

    public void setGratificacao(String Gratificacao) {
        this.Gratificacao = Gratificacao;
    }

    public String getAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(String adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    public String getDesconto() {
        desconto = transformaMinutosEmHora(descontoInt);
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getDescontoDSR() {
        return descontoDSR;
    }

    public void setDescontoDSR(String descontoDSR) {
        this.descontoDSR = descontoDSR;
    }

    public String getFalta() {
        return falta;
    }

    public void setFalta(String falta) {
        this.falta = falta;
    }

    public String getHorasExtra() {
        return horasExtra;
    }

    public void setHorasExtra(String horasExtra) {
        this.horasExtra = horasExtra;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public Integer getGratificacaoInt() {
        return GratificacaoInt;
    }

    public void setGratificacaoInt(Integer GratificacaoInt) {
        this.GratificacaoInt = GratificacaoInt;
    }

    public Integer getAdicionalNoturnoInt() {
        return adicionalNoturnoInt;
    }

    public void setAdicionalNoturnoInt(Integer adicionalNoturnoInt) {
        this.adicionalNoturnoInt = adicionalNoturnoInt;
    }

    public Integer getDescontoDSRInt() {
        return descontoDSRInt;
    }

    public void setDescontoDSRInt(Integer descontoDSRInt) {
        this.descontoDSRInt = descontoDSRInt;
    }

    public Integer getDescontoInt() {
        return descontoInt;
    }

    public void setDescontoInt(Integer descontoInt) {
        this.descontoInt = descontoInt;
    }

    public Integer getFaltaInt() {
        return faltaInt;
    }

    public void setFaltaInt(Integer faltaInt) {
        this.faltaInt = faltaInt;
    }

    public Integer getHorasExtraInt() {
        return horasExtraInt;
    }

    public void setHorasExtraInt(Integer horasExtraInt) {
        this.horasExtraInt = horasExtraInt;
    }

    public Integer getSaldoInt() {
        return saldoInt;
    }

    public void setSaldoInt(Integer saldoInt) {
        this.saldoInt = saldoInt;
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
}
