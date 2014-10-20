/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHoraExtra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class Gratificacao implements Serializable{

    private Integer cod_gratificacao;
    private Integer cod_regime;
    private String nome;
    private Float valor;
    private String verba;
    private List<DetalheGratificacao> detalheGratificacaoList = new ArrayList<DetalheGratificacao>();

    public Gratificacao(Integer cod_gratificacao, Integer cod_regime, String nome, Float valor, String verba) {
        this.cod_gratificacao = cod_gratificacao;
        this.cod_regime = cod_regime;
        this.nome = nome;
        this.valor = valor;
        this.verba = verba;
    }

    public Gratificacao() {
       
    }

    public Integer getCod_gratificacao() {
        return cod_gratificacao;
    }

    public void setCod_gratificacao(Integer cod_gratificacao) {
        this.cod_gratificacao = cod_gratificacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getVerba() {
        return verba;
    }

    public void setVerba(String verba) {
        this.verba = verba;
    }

    public Integer getCod_regime() {
        return cod_regime;
    }

    public void setCod_regime(Integer cod_regime) {
        this.cod_regime = cod_regime;
    }

    public List<DetalheGratificacao> getDetalheGratificacaoList() {
        return detalheGratificacaoList;
    }

    public void setDetalheGratificacaoList(DetalheGratificacao detalheGratificacao) {
        this.detalheGratificacaoList.add(detalheGratificacao);
    }

}
