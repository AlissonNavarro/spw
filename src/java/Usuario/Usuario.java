package Usuario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario implements Serializable {

    private BigDecimal nrId = null;
    private Integer login = null;
    private String ssn;
    private String senha;
    private String nome = "";
    private String permissao = "";
    private Integer departamento;
    private String deptStr;
    private String primeiroNome;
    private Boolean isAcessoTotal;
    private String cargo;
    private Integer codPerfil;
    private Date dataContratacao;
    private String UserAD;

    public String getUserAD() {
        return UserAD;
    }

    public void setUserAD(String UserAD) {
        this.UserAD = UserAD;
    }

    public BigDecimal getNrId() {
        return nrId;
    }

    public void setNrId(BigDecimal nrId) {
        this.nrId = nrId;
    }

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        geraPrimeiroNome();
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPermissao() {
        return permissao;
    }

    public void setPermissao(String permissao) {
        this.permissao = permissao;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public Boolean getIsAcessoTotal() {
        return isAcessoTotal;
    }

    public void setIsAcessoTotal(Boolean isAcessoTotal) {
        this.isAcessoTotal = isAcessoTotal;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDeptStr() {
        return deptStr;
    }

    public void setDeptStr(String deptStr) {
        this.deptStr = deptStr;
    }

    public Integer getCodPerfil() {
        return codPerfil;
    }

    public void setCodPerfil(Integer codPerfil) {
        this.codPerfil = codPerfil;
    }

    public Date getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(Date dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public String getDataContratacaoStr() {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("dd/MM/yyyy");
        String saida = sdfSemana.format(dataContratacao.getTime());
        return saida;
    }

    public void geraPrimeiroNome() {
        String[] nomeCompleto = getNome().split(" ");
        String[] primeiroNome_ = nomeCompleto[0].split("");
        String calda = "";
        for (int i = 2; i < primeiroNome_.length; i++) {
            calda += primeiroNome_[i].toLowerCase();
        }
        setPrimeiroNome(primeiroNome_[1].toUpperCase() + calda);
    }

}
