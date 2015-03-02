package entidades;

import java.io.Serializable;

public class Verba implements Serializable{

    private int empresa;
    private int adicionalNoturno;
    private int atrasos;
    private int atrasosMenorHora;
    private int atrasosMaiorHora;
    private int feriadoCritico;
    private int dsr;
    private int faltas;

    public Verba() {
    }

    public Verba(int empresa, int adicionalNoturno, int atrasos, int atrasosMenorHora, int atrasosMaiorHora, int feriadoCritico, int dsr, int faltas) {
        this.empresa = empresa;
        this.adicionalNoturno = adicionalNoturno;
        this.atrasos = atrasos;
        this.atrasosMenorHora = atrasosMenorHora;
        this.atrasosMaiorHora = atrasosMaiorHora;
        this.feriadoCritico = feriadoCritico;
        this.dsr = dsr;
        this.faltas = faltas;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public int getAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(int adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    public int getAtrasos() {
        return atrasos;
    }

    public void setAtrasos(int atrasos) {
        this.atrasos = atrasos;
    }

    public int getAtrasosMenorHora() {
        return atrasosMenorHora;
    }

    public void setAtrasosMenorHora(int atrasosMenorHora) {
        this.atrasosMenorHora = atrasosMenorHora;
    }

    public int getAtrasosMaiorHora() {
        return atrasosMaiorHora;
    }

    public void setAtrasosMaiorHora(int atrasosMaiorHora) {
        this.atrasosMaiorHora = atrasosMaiorHora;
    }

    public int getFeriadoCritico() {
        return feriadoCritico;
    }

    public void setFeriadoCritico(int feriadoCritico) {
        this.feriadoCritico = feriadoCritico;
    }

    public int getDsr() {
        return dsr;
    }

    public void setDsr(int dsr) {
        this.dsr = dsr;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

}
