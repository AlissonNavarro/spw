/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Machines;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;

/**
 *
 * @author Pedro
 */
public class Machine implements Serializable {
    
    //Comum a todos os relógios
    private Integer repId;
    private String repAlias;
    private String repIp;
    private Integer repPort;
    private Integer repType;
    private Boolean repAtivo;
    
    //1 = IDData
    //2 = Trilobit
    //3 = ZK
    private MachineType type;
    
    //Independente de alguns relógios
    private String nfr;
    private Integer model;
    private String local;
    private Integer idCompany;
    private Integer commType;
    private Integer biometricModule;
    private Timestamp dateLastNSR;
    private Integer lastNSR;
    private String pathCollectFile;
    
    private String nsr;
    private String ipRoater;
    private String mask;
    private String senha;
    
    private Boolean online;
    //Dados de exibição
    private Integer qtdDigitais;
    private Integer qtdUsers;
    private Integer qtdBobina;
    private Integer qtdMemory;
    private Integer qtdBatery;
    private Integer maxUsers;
    private Integer maxDigitais;
    
    public Machine(){
        this.repId = null;
        this.repAlias = "";
        this.repIp = "";
        this.repPort = null;
        this.repType = null;
        this.repAtivo = false;
        
        this.type = new MachineType();
        //
        this.nfr = "";
        this.model = null;
        this.local = "";
        this.idCompany = null;
        this.commType = null;
        this.biometricModule = null;
        this.dateLastNSR = null;
        this.lastNSR = null;
        this.pathCollectFile = "";
        //
        this.nsr = "";
        this.ipRoater = "";
        this.mask = "";
        this.senha = "";
        
        this.online = null;
        this.qtdDigitais = 0;
        this.qtdUsers = 0;
        this.qtdBobina = 0;
        this.qtdBatery = 0;
        this.qtdMemory = 0;
        this.maxUsers = 100;
        this.maxDigitais = 100;
    }
    
    public Machine (Integer id, String alias, String ip, Integer port, Integer repType, Boolean enable, String nfr, Integer model, 
            String local, Integer idCompany, Integer commType, Integer biometricModule, Timestamp dateLastNSR, Integer lastNSR, 
            String pathCollectFile, String nsr, String ipRoater, String mask, String senha, MachineType type) {
        this.repId = id;
        this.repAlias = alias;
        this.repIp = ip;
        this.repPort = port;
        this.repType = repType;
        this.repAtivo = enable;
        
        this.type = type; //Tipo de relógio
        
        this.nfr = nfr;
        this.model = model;
        this.local = local;
        this.idCompany = idCompany;
        this.commType = commType;
        this.biometricModule = biometricModule;
        this.dateLastNSR = dateLastNSR;
        this.lastNSR = lastNSR;
        this.pathCollectFile = pathCollectFile;
        //
        this.nsr = nsr;
        this.ipRoater = ipRoater;
        this.mask = mask;
        this.senha = senha;
        
        this.online = null;
        this.qtdDigitais = 0;
        this.qtdUsers = 0;
        this.qtdBobina = 0;
        this.qtdBatery = 0;
        this.qtdMemory = 0;
        this.maxUsers = 100;
        this.maxDigitais = 100;
    }
    
    public Machine(String alias, String ip, Integer port, Integer repType, Boolean enable){
        this.repAlias = alias;
        this.repIp = ip;
        this.repPort = port;
        this.repType = repType;
        this.repAtivo = enable;
        
        this.type = new MachineType();
        //
        this.nfr = "";
        this.model = null;
        this.local = "";
        this.idCompany = null;
        this.commType = null;
        this.biometricModule = null;
        this.dateLastNSR = null;
        this.lastNSR = null;
        this.pathCollectFile = "";
        //
        this.nsr = "";
        this.ipRoater = "";
        this.mask = "";
        this.senha = "";
        
        this.online = null;
        this.qtdDigitais = 0;
        this.qtdUsers = 0;
        this.qtdBobina = 0;
        this.qtdBatery = 0;
        this.qtdMemory = 0;
        this.maxUsers = 100;
        this.maxDigitais = 100;
    }

    public Integer getRepId() {
        return repId;
    }

    public void setRepId(Integer repId) {
        this.repId = repId;
    }

    public String getRepAlias() {
        return repAlias;
    }

    public void setRepAlias(String repAlias) {
        this.repAlias = repAlias;
    }

    public String getRepIp() {
        return repIp;
    }

    public void setRepIp(String repIp) {
        this.repIp = repIp;
    }

    public Integer getRepPort() {
        return repPort;
    }

    public void setRepPort(Integer repPort) {
        this.repPort = repPort;
    }

    public Integer getRepType() {
        return repType;
    }

    public void setRepType(Integer repType) {
        this.repType = repType;
    }

    public Boolean getRepAtivo() {
        return repAtivo;
    }

    public void setRepAtivo(Boolean repAtivo) {
        this.repAtivo = repAtivo;
    }

    public MachineType getType() {
        return type;
    }

    public void setType(MachineType type) {
        this.type = type;
    }
    
    public String getNfr() {
        return nfr;
    }

    public void setNfr(String nfr) {
        this.nfr = nfr;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public Integer getCommType() {
        return commType;
    }

    public void setCommType(Integer commType) {
        this.commType = commType;
    }

    public Integer getBiometricModule() {
        return biometricModule;
    }

    public void setBiometricModule(Integer biometricModule) {
        this.biometricModule = biometricModule;
    }

    public Timestamp getDateLastNSR() {
        return dateLastNSR;
    }

    public void setDateLastNSR(Timestamp dateLastNSR) {
        this.dateLastNSR = dateLastNSR;
    }

    public Integer getLastNSR() {
        return lastNSR;
    }

    public void setLastNSR(Integer lastNSR) {
        this.lastNSR = lastNSR;
    }

    public String getPathCollectFile() {
        return pathCollectFile;
    }

    public void setPathCollectFile(String pathCollectFile) {
        this.pathCollectFile = pathCollectFile;
    }

    public String getNsr() {
        return nsr;
    }

    public void setNsr(String nsr) {
        this.nsr = nsr;
    }

    public String getIpRoater() {
        return ipRoater;
    }

    public void setIpRoater(String ipRoater) {
        this.ipRoater = ipRoater;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getQtdDigitais() {
        return qtdDigitais;
    }

    public void setQtdDigitais(Integer qtdDigitais) {
        this.qtdDigitais = qtdDigitais;
    }

    public Integer getQtdUsers() {
        return qtdUsers;
    }

    public void setQtdUsers(Integer qtdUsers) {
        this.qtdUsers = qtdUsers;
    }

    public Integer getQtdBobina() {
        return qtdBobina;
    }

    public void setQtdBobina(Integer qtdBobina) {
        this.qtdBobina = qtdBobina;
    }

    public Integer getQtdMemory() {
        return qtdMemory;
    }

    public void setQtdMemory(Integer qtdMemory) {
        this.qtdMemory = qtdMemory;
    }

    public Integer getQtdBatery() {
        return qtdBatery;
    }

    public void setQtdBatery(Integer qtdBatery) {
        this.qtdBatery = qtdBatery;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getMaxDigitais() {
        return maxDigitais;
    }

    public void setMaxDigitais(Integer maxDigitais) {
        this.maxDigitais = maxDigitais;
    }
    
}
