/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaJornada;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class JornadaExibicao {

    private String nomeJornada;
    private List<String> funcSegunda;
    private List<String> funcTerca;
    private List<String> funcQuarta;
    private List<String> funcQuinta;
    private List<String> funcSexta;
    private List<String> funcSabado;
    private List<String> funcDomingo;

    private String segunda="";
    private String terca="";
    private String quarta="";
    private String quinta="";
    private String sexta="";
    private String sabado="";
    private String domingo="";

    public JornadaExibicao(String nomeJornada) {
        this.nomeJornada = nomeJornada;
        funcSegunda = new ArrayList<String>();
        funcTerca = new ArrayList<String>();
        funcQuarta = new ArrayList<String>();
        funcQuinta = new ArrayList<String>();
        funcSexta = new ArrayList<String>();
        funcSabado = new ArrayList<String>();
        funcDomingo = new ArrayList<String>();
    }

    public JornadaExibicao(String nomeJornada, String segunda, String terca, String quarta, String quinta, String sexta) {
        this.nomeJornada = nomeJornada;
        this.segunda = segunda;
        this.terca = terca;
        this.quarta = quarta;
        this.quinta = quinta;
        this.sexta = sexta;
    }

    

    public String getNomeJornada() {
        return nomeJornada;
    }

    public void setNomeJornada(String nomeJornada) {
        this.nomeJornada = nomeJornada;
    }

    public List<String> getFuncDomingo() {
        return funcDomingo;
    }

    public void setFuncDomingo(List<String> funcDomingo) {
        this.funcDomingo = funcDomingo;
    }

    public List<String> getFuncQuarta() {
        return funcQuarta;
    }

    public void setFuncQuarta(List<String> funcQuarta) {
        this.funcQuarta = funcQuarta;
    }

    public List<String> getFuncQuinta() {
        return funcQuinta;
    }

    public void setFuncQuinta(List<String> funcQuinta) {
        this.funcQuinta = funcQuinta;
    }

    public List<String> getFuncSabado() {
        return funcSabado;
    }

    public void setFuncSabado(List<String> funcSabado) {
        this.funcSabado = funcSabado;
    }

    public List<String> getFuncSegunda() {
        return funcSegunda;
    }

    public void setFuncSegunda(List<String> funcSegunda) {
        this.funcSegunda = funcSegunda;
    }

    public List<String> getFuncSexta() {
        return funcSexta;
    }

    public void setFuncSexta(List<String> funcSexta) {
        this.funcSexta = funcSexta;
    }

    public List<String> getFuncTerca() {
        return funcTerca;
    }

    public void setFuncTerca(List<String> funcTerca) {
        this.funcTerca = funcTerca;
    }

    public String getDomingo() {
        return domingo;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public String getQuarta() {
        for (Iterator<String> it = funcQuarta.iterator(); it.hasNext();) {
            String string = it.next();
            quarta+=string;
        }
        return quarta;
    }

    public void setQuarta(String quarta) {

        this.quarta = quarta;
    }

    public String getQuinta() {
        for (Iterator<String> it = funcQuinta.iterator(); it.hasNext();) {
            String string = it.next();
            quinta+=string;
        }

        return quinta;
    }

    public void setQuinta(String quinta) {
        this.quinta = quinta;
    }

    public String getSabado() {
        return sabado;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getSegunda() {
        for (Iterator<String> it = funcSegunda.iterator(); it.hasNext();) {
            String string = it.next();
            segunda+=string;
        }
        return segunda;
    }

    public void setSegunda(String segunda) {
        this.segunda = segunda;
    }

    public String getSexta() {
        for (Iterator<String> it = funcSexta.iterator(); it.hasNext();) {
            String string = it.next();
            sexta+=string;
        }
        return sexta;
    }

    public void setSexta(String sexta) {
        this.sexta = sexta;
    }

    public String getTerca() {
        for (Iterator<String> it = funcTerca.iterator(); it.hasNext();) {
            String string = it.next();
            terca+=string;
        }
        return terca;
    }

    public void setTerca(String terca) {
        this.terca = terca;
    }  
}
