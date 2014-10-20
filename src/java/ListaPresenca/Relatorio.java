/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ListaPresenca;

import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class Relatorio {


    private String cpf;
    private String nome;
    private Date ultimoRegistro;
    private String situacao;

    public Relatorio(String cpf, String nome, Date ultimoRegistro, String situacao) {
        this.cpf = cpf;
        this.nome = nome;
        this.ultimoRegistro = ultimoRegistro;
        this.situacao = situacao;
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
}
