/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ListaPresenca;

import ConsultaPonto.Jornada;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class ListaPresenca implements Serializable{

    private Integer userid;
    private String cpf;
    private String nome;
    private Date ultimoRegistro;
    private String situacao;
    private List<Jornada> jornadaList;

    public ListaPresenca(Integer userid,String cpf, String nome, Date ultimoRegistro) {
        this.cpf = cpf;
        this.nome = nome;
        this.ultimoRegistro = ultimoRegistro;
        this.userid = userid;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getUltimoRegistro() {
        return ultimoRegistro;
    }

    public void setUltimoRegistro(Date ultimoRegistro) {
        this.ultimoRegistro = ultimoRegistro;
    }

    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
