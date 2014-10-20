/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHorario;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author amsgama
 */
public class Horario implements Serializable {

    private Integer horario_id;
    private String nome;
    private String legenda;
    private Date entrada;
    private Date saida;
    private Date inicioFaixaEntrada;
    private Date fimFaixaEntrada;
    private Date inicioFaixaSaida;
    private Date fimFaixaSaida;
    private Integer toleranciaEntrada;
    private Integer toleranciaSaida;
    private Boolean isEntradaObrigatoria;
    private Boolean isSaidaObrigatoria;
    private String horarioDescanso;
    private String entradaDescanso1;
    private String saidaDescanso1;
    private String entradaDescanso2;
    private String saidaDescanso2;
    private String horarioIntrajornada;
    private String entradaIntrajornada;
    private String saidaIntrajornada;
    private Integer toleranciaDesc;
    private String type;
    private Boolean ativo;
    private Boolean deduzirDescanso1;
    private Boolean deduzirDescanso2;
    private Boolean deduzirIntrajornada;
    private Boolean combinarEntrada; //Combina entrada da interjornada com saida do primeiro descanso
    private Boolean combinarSaida; //Combina saida da interjornada com entrada do segundo descanso
    private Integer qtdMarcacoes;

    public Horario() {
        isEntradaObrigatoria = true;
        isSaidaObrigatoria = true;
        deduzirDescanso1 = false;
        deduzirDescanso2 = false;
        deduzirIntrajornada = false;
        combinarEntrada = false;
        combinarSaida = false;
        type = null;
        toleranciaDesc = 0;
    }

    public Horario(Integer horario_id, String nome, String legenda, String entrada, String saida,
            String inicioFaixaEntrada, String fimFaixaEntrada, String inicioFaixaSaida, String fimFaixaSaida, Boolean isEntradaObrigatoria,
            Boolean isSaidaObrigatoria, String entradaDescanso1, String saidaDescanso1, String entradaDescanso2,
            String saidaDescanso2, Integer toleranciaDesc, Boolean ativo, Boolean deduzirDescanso1, Boolean deduzirDescanso2,
            String entradaIntrajornada, String saidaIntrajornada, Boolean deduzirIntrajornada, Boolean combinarEntrada, Boolean combinarSaida) {

        this.qtdMarcacoes = 2;

        this.horario_id = horario_id;
        this.nome = nome;
        this.legenda = legenda;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            this.entrada = sdf.parse(entrada);
            this.saida = sdf.parse(saida);
            this.inicioFaixaEntrada = sdf.parse(inicioFaixaEntrada);
            this.fimFaixaEntrada = sdf.parse(fimFaixaEntrada);
            this.inicioFaixaSaida = sdf.parse(inicioFaixaSaida);
            this.fimFaixaSaida = sdf.parse(fimFaixaSaida);
        } catch (ParseException e) {
            System.out.println("teste");
            System.out.println(e.getMessage());
        }
        this.isEntradaObrigatoria = isEntradaObrigatoria;
        this.isSaidaObrigatoria = isSaidaObrigatoria;
        this.entradaDescanso1 = entradaDescanso1;
        this.saidaDescanso1 = saidaDescanso1;
        this.entradaDescanso2 = entradaDescanso2;
        this.saidaDescanso2 = saidaDescanso2;
        this.horarioDescanso = "";
        if (entradaDescanso1 == null && entradaDescanso2 == null) {
            this.horarioDescanso = "";
            this.type = "Expediente";
        } else if (entradaDescanso1 != null) {
            this.horarioDescanso = entradaDescanso1 + " - " + saidaDescanso1;
            this.type = "Plantonista12";
            this.qtdMarcacoes += 2;
            if (entradaDescanso2 != null) {
                this.horarioDescanso = this.horarioDescanso + " / " + entradaDescanso2 + " - " + saidaDescanso2;
                this.type = "Plantonista24";
                this.qtdMarcacoes += 2;
            }
        }
        this.entradaIntrajornada = entradaIntrajornada;
        this.saidaIntrajornada = saidaIntrajornada;
        if (entradaIntrajornada != null) {
            this.horarioDescanso = "";
            this.horarioIntrajornada = entradaIntrajornada + " - " + saidaIntrajornada;
            if (entradaDescanso1 != null) {
                this.horarioDescanso = entradaDescanso1 + " - " + saidaDescanso1;
            }
            this.horarioDescanso = this.horarioDescanso + " / " + this.horarioIntrajornada;
            if (entradaDescanso2 != null) {
                this.horarioDescanso = this.horarioDescanso + " / " + entradaDescanso2 + " - " + saidaDescanso2;
            }

            this.type = "CallCenter";
            this.qtdMarcacoes += 2;
        }
        this.toleranciaDesc = toleranciaDesc;
        this.ativo = ativo;
        this.deduzirDescanso1 = deduzirDescanso1;
        this.deduzirDescanso2 = deduzirDescanso2;
        this.deduzirIntrajornada = deduzirIntrajornada;
        this.combinarEntrada = combinarEntrada;
        if (this.combinarEntrada) {
            this.qtdMarcacoes -= 2;
            this.saidaDescanso1 = entradaIntrajornada;
        }
        this.combinarSaida = combinarSaida;
        if (this.combinarSaida) {
            this.qtdMarcacoes -= 2;
            this.entradaDescanso2 = saidaIntrajornada;
        }
    }

    public Boolean estaPreenchidoCorretamento() {
        if (!nome.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getFimFaixaEntrada() {
        return fimFaixaEntrada;
    }

    public void setFimFaixaEntrada(Date fimFaixaEntrada) {
        this.fimFaixaEntrada = fimFaixaEntrada;
    }

    public Date getFimFaixaSaida() {
        return fimFaixaSaida;
    }

    public void setFimFaixaSaida(Date fimFaixaSaida) {
        this.fimFaixaSaida = fimFaixaSaida;
    }

    public Date getInicioFaixaEntrada() {
        return inicioFaixaEntrada;
    }

    public void setInicioFaixaEntrada(Date inicioFaixaEntrada) {
        this.inicioFaixaEntrada = inicioFaixaEntrada;
    }

    public Date getInicioFaixaSaida() {
        return inicioFaixaSaida;
    }

    public void setInicioFaixaSaida(Date inicioFaixaSaida) {
        this.inicioFaixaSaida = inicioFaixaSaida;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public Integer getToleranciaEntrada() {
        return toleranciaEntrada;
    }

    public void setToleranciaEntrada(Integer toleranciaEntrada) {
        this.toleranciaEntrada = toleranciaEntrada;
    }

    public Integer getHorario_id() {
        return horario_id;
    }

    public void setHorario_id(Integer horario_id) {
        this.horario_id = horario_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getToleranciaSaida() {
        return toleranciaSaida;
    }

    public void setToleranciaSaida(Integer toleranciaSaida) {
        this.toleranciaSaida = toleranciaSaida;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public Boolean getIsEntradaObrigatoria() {
        return isEntradaObrigatoria;
    }

    public void setIsEntradaObrigatoria(Boolean isEntradaObrigatoria) {
        this.isEntradaObrigatoria = isEntradaObrigatoria;
    }

    public Boolean getIsSaidaObrigatoria() {
        return isSaidaObrigatoria;
    }

    public void setIsSaidaObrigatoria(Boolean isSaidaObrigatoria) {
        this.isSaidaObrigatoria = isSaidaObrigatoria;
    }

    public String getEntradaDescanso1() {
        return entradaDescanso1;
    }

    public void setEntradaDescanso1(String entradaDescanso1) {
        this.entradaDescanso1 = entradaDescanso1;
    }

    public String getEntradaDescanso2() {
        return entradaDescanso2;
    }

    public void setEntradaDescanso2(String entradaDescanso2) {
        this.entradaDescanso2 = entradaDescanso2;
    }

    public String getSaidaDescanso1() {
        return saidaDescanso1;
    }

    public void setSaidaDescanso1(String saidaDescanso1) {
        this.saidaDescanso1 = saidaDescanso1;
    }

    public String getSaidaDescanso2() {
        return saidaDescanso2;
    }

    public void setSaidaDescanso2(String saidaDescanso2) {
        this.saidaDescanso2 = saidaDescanso2;
    }

    public String getHorarioDescanso() {
        return horarioDescanso;
    }

    public void setHorarioDescanso(String horarioDescanso) {
        this.horarioDescanso = horarioDescanso;
    }

    public Integer getToleranciaDesc() {
        return toleranciaDesc;
    }

    public void setToleranciaDesc(Integer toleranciaDesc) {
        this.toleranciaDesc = toleranciaDesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getDeduzirDescanso1() {
        return deduzirDescanso1;
    }

    public void setDeduzirDescanso1(Boolean deduzirDescanso1) {
        this.deduzirDescanso1 = deduzirDescanso1;
    }

    public Boolean getDeduzirDescanso2() {
        return deduzirDescanso2;
    }

    public void setDeduzirDescanso2(Boolean deduzirDescanso2) {
        this.deduzirDescanso2 = deduzirDescanso2;
    }

    public String getEntradaIntrajornada() {
        return entradaIntrajornada;
    }

    public void setEntradaIntrajornada(String entradaIntrajornada) {
        if (combinarEntrada) {
            entradaIntrajornada = saidaDescanso1;
        }
        this.entradaIntrajornada = entradaIntrajornada;
    }

    public String getSaidaIntrajornada() {
        return saidaIntrajornada;
    }

    public void setSaidaIntrajornada(String saidaIntrajornada) {
        if (combinarSaida) {
            saidaIntrajornada = entradaDescanso2;
        }
        this.saidaIntrajornada = saidaIntrajornada;
    }

    public Boolean getDeduzirIntrajornada() {
        return deduzirIntrajornada;
    }

    public void setDeduzirIntrajornada(Boolean deduzirIntrajornada) {
        this.deduzirIntrajornada = deduzirIntrajornada;
    }

    public Boolean getCombinarEntrada() {
        return combinarEntrada;
    }

    public void setCombinarEntrada(Boolean combinarEntrada) {
        if (combinarEntrada) {
            entradaIntrajornada = saidaDescanso1;
        }
        this.combinarEntrada = combinarEntrada;
    }

    public Boolean getCombinarSaida() {
        return combinarSaida;
    }

    public void setCombinarSaida(Boolean combinarSaida) {
        if (combinarSaida) {
            saidaIntrajornada = entradaDescanso2;
        }
        this.combinarSaida = combinarSaida;
    }

    public String getHorarioIntrajornada() {
        return horarioIntrajornada;
    }

    public void setHorarioIntrajornada(String horarioIntrajornada) {
        this.horarioIntrajornada = horarioIntrajornada;
    }

    public Integer getQtdMarcacoes() {
        return qtdMarcacoes;
    }

    public void setQtdMarcacoes(Integer qtdMarcacoes) {
        this.qtdMarcacoes = qtdMarcacoes;
    }

}
