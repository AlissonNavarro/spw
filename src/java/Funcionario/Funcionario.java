package Funcionario;

import java.io.Serializable;
import java.util.Date;

public class Funcionario implements Serializable {

    //private Integer userid;
    private Integer funcionarioId;
    private String matricula;
    private String dept;
    private Integer cod_dept;
    private String cpf;
    private String PIS;
    private String nome;
    private Integer sexo;
    private Integer cargo;
    private String cargoStr;
    private String cracha;
    private Date dataNascimento;
    private Date dataContratação;
    private Boolean sucetivelAFeriado;
    private Integer cod_regime;
    private String nome_regime;
    private Integer mat_emcs;
    //login de active Directory
    private String ADUsername;
    //Se está demitido ou não
    private Boolean isAtivo;
    //Se tem acesso livre nas catracas
    private Boolean livreAcesso;
    
    public Funcionario() {
    }
    
    public Funcionario(String pin) {
        this.matricula = pin;
    }

    public Funcionario(Integer funcionarioId, String matricula, String cpf, String PIS, String nome,
            Integer sexo, Integer cargo, String cracha, Date dataNascimento, Date dataContratação, 
            Boolean sucetivelAFeriado, Integer dept,Integer cod_regime,Integer mat_emcs, String ADUsername, 
            Boolean livreAcesso) {
        //this.userid = userid;
        this.funcionarioId = funcionarioId;
        this.matricula = matricula;
        this.cpf = cpf;
        this.PIS = PIS;
        this.nome = nome;
        this.sexo = sexo;
        this.cargo = cargo;
        this.cracha = cracha;
        this.dataNascimento = dataNascimento;
        this.dataContratação = dataContratação;
        this.sucetivelAFeriado = sucetivelAFeriado;
        this.cod_dept = dept;
        this.cod_regime = cod_regime;
        this.mat_emcs = mat_emcs;
        this.ADUsername = ADUsername;
        this.livreAcesso = livreAcesso;
    }
/*public void atualizarRegime() {
        Banco banco = new Banco();
        String regime = banco.consultaRegime(cod_regime);
        Boolean teste = banco.updateRegime(funcionarioId, cod_regime);
        if (teste) {
            FacesMessage msgErro = new FacesMessage("Regime alterado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            regime = regime == null ? "Sem regime" : regime;
            Metodos.setLogInfo("Alteração de Regime - Funcionário: " + getNome() + " Regime: " + regime);
        }
    }*/
    
    @Override
    public String toString(){
        return nome;
    }

    public String getCargoStr() {
        return cargoStr;
    }

    public void setCargoStr(String cargoStr) {
        this.cargoStr = cargoStr;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getCod_dept() {
        return cod_dept;
    }

    public void setCod_dept(Integer cod_dept) {
        this.cod_dept = cod_dept;
    }

    public String getPIS() {
        return PIS;
    }

    public void setPIS(String PIS) {
        this.PIS = PIS;
    }

    public Integer getCargo() {
        return cargo;
    }

    public void setCargo(Integer cargo) {
        this.cargo = cargo;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCracha() {
        return cracha;
    }

    public void setCracha(String cracha) {
        this.cracha = cracha;
    }

    public Date getDataContratação() {
        return dataContratação;
    }

    public void setDataContratação(Date dataContratação) {
        this.dataContratação = dataContratação;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public Boolean getSucetivelAFeriado() {
        return sucetivelAFeriado;
    }

    public void setSucetivelAFeriado(Boolean sucetivelAFeriado) {
        this.sucetivelAFeriado = sucetivelAFeriado;
    }

    public Integer getCod_regime() {
        return cod_regime;
    }

    public void setCod_regime(Integer cod_regime) {
        this.cod_regime = cod_regime;
    }


    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public String getNome_regime() {
        return nome_regime;
    }

    public void setNome_regime(String nome_regime) {
        this.nome_regime = nome_regime;
    }
    public Integer getMat_emcs() {
        return mat_emcs;
    }

    public void setMat_emcs(Integer mat_emcs) {
        this.mat_emcs = mat_emcs;
    }

    public String getADUsername() {
        return ADUsername;
    }

    public void setADUsername(String ADUsername) {
        this.ADUsername = ADUsername;
    }

    public Boolean getLivreAcesso() {
        return livreAcesso;
    }

    public void setLivreAcesso(Boolean livreAcesso) {
        this.livreAcesso = livreAcesso;
    }
    
}
