/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHoraExtra;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class RegimeHoraExtra implements Serializable {

    private Integer cod;
    private String nome;
    private Boolean feriadoCritico;
    private Integer modoTolerancia;
    private Integer tolerancia;
    private Integer limiteEntrada;
    private List<TipoHoraExtra> tipoHoraExtraList;
    private String regime_css;


    public RegimeHoraExtra() {
        feriadoCritico = false;
        tolerancia = 0;
    }

    public RegimeHoraExtra(Integer cod, String nome,Boolean feriadoCritico) {
        this.cod = cod;
        this.nome = nome;
        this.feriadoCritico = feriadoCritico;
        regime_css = "label";
    }

    public Boolean hasRegime(){
        return cod != null ? true : false;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<TipoHoraExtra> getTipoHoraExtraList() {
        return tipoHoraExtraList;
    }

    public void setTipoHoraExtraList(List<TipoHoraExtra> tipoHoraExtraList) {
        this.tipoHoraExtraList = tipoHoraExtraList;
    }

    public String getRegime_css() {
        return regime_css;
    }

    public void setRegime_css(String regime_css) {
        this.regime_css = regime_css;
    }

    public Boolean getFeriadoCritico() {
        return feriadoCritico;
    }

    public void setFeriadoCritico(Boolean feriadoCritico) {
        this.feriadoCritico = feriadoCritico;
    }

    public Integer getLimiteEntrada() {
        return limiteEntrada;
    }

    public void setLimiteEntrada(Integer limiteEntrada) {
        this.limiteEntrada = limiteEntrada;
    }

    public Integer getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(Integer tolerancia) {
        this.tolerancia = tolerancia;
    }

    public Integer getModoTolerancia() {
        return modoTolerancia;
    }

    public void setModoTolerancia(Integer modoTolerancia) {
        this.modoTolerancia = modoTolerancia;
    }
}
