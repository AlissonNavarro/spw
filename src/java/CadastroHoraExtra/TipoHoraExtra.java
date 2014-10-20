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
public class TipoHoraExtra implements Serializable {

    private Integer cod_regime;
    private Integer cod_tipo;
    private String nome;
    private Float valor;
    private String verba;
    private List<DetalheHoraExtra> detalheHoraExtra;
    private String tipo_css;
    private Boolean isPadrao;

    public TipoHoraExtra() {
    }

    public TipoHoraExtra(Integer cod_regime, Integer cod_tipo, String nome, Boolean isPadrao) {
        this.cod_regime = cod_regime;
        this.cod_tipo = cod_tipo;
        this.nome = nome;
        this.isPadrao = isPadrao;
        tipo_css = "label";
    }

    public Integer getCod_regime() {
        return cod_regime;
    }

    public void setCod_regime(Integer cod_regime) {
        this.cod_regime = cod_regime;
    }

    public Integer getCod_tipo() {
        return cod_tipo;
    }

    public void setCod_tipo(Integer cod_tipo) {
        this.cod_tipo = cod_tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<DetalheHoraExtra> getDetalheHoraExtra() {
        return detalheHoraExtra;
    }

    public void setDetalheHoraExtra(List<DetalheHoraExtra> detalheHoraExtra) {
        this.detalheHoraExtra = detalheHoraExtra;
    }

    public String getTipo_css() {
        return tipo_css;
    }

    public void setTipo_css(String tipo_css) {
        this.tipo_css = tipo_css;
    }

    public Boolean getIsPadrao() {
        return isPadrao;
    }

    public void setIsPadrao(Boolean isPadrao) {
        this.isPadrao = isPadrao;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        TipoHoraExtra other = (TipoHoraExtra) o;
        return this.cod_tipo == other.cod_tipo;
    }

    public String getVerba() {
        return verba;
    }

    public void setVerba(String verba) {
        this.verba = verba;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }
}
