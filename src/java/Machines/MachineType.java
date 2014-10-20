/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Machines;

import java.io.Serializable;

/**
 *
 * @author ppccardoso
 */
public class MachineType implements Serializable {
    
    private Integer repTipoId;
    private String repMarca;
    private Boolean nfrOn;
    private Boolean modelOn;
    private Boolean localOn;
    private Boolean idCompanyOn;
    private Boolean commTypeOn;
    private Boolean biometricModuleOn;
    private Boolean dateLastNSROn;
    private Boolean lastNSROn;
    private Boolean pathCollectFileOn;
    private Boolean nsrOn;
    private Boolean ipRoaterOn;
    private Boolean maskOn;
    private Boolean senhaOn;
    
    public MachineType () {
        this.repTipoId = null;
        this.repMarca = "";
        this.nfrOn = false;
        this.modelOn = false;
        this.localOn = false;
        this.idCompanyOn = false;
        this.commTypeOn = false;
        this.biometricModuleOn = false;
        this.dateLastNSROn = false;
        this.lastNSROn = false;
        this.pathCollectFileOn = false;
        this.ipRoaterOn = false;
        this.maskOn = false;
        this.senhaOn = false;
    }
    
    public MachineType (Integer id, String marca, Boolean nfr, Boolean model, Boolean local, Boolean idCompany, 
            Boolean commType, Boolean biometricModule, Boolean dateLastNSR, Boolean lastNSR, Boolean pathCollectFile, 
            Boolean nsr, Boolean iprRoater, Boolean mask, Boolean senha) {
        this.repTipoId = id;
        this.repMarca = marca;
        this.nfrOn = nfr;
        this.modelOn = model;
        this.localOn = local;
        this.idCompanyOn = idCompany;
        this.commTypeOn = commType;
        this.biometricModuleOn = biometricModule;
        this.dateLastNSROn = dateLastNSR;
        this.lastNSROn = lastNSR;
        this.pathCollectFileOn = pathCollectFile;
        this.nsrOn = nsr;
        this.ipRoaterOn = iprRoater;
        this.maskOn = mask;
        this.senhaOn = senha;
    }

    public Integer getRepTipoId() {
        return repTipoId;
    }

    public void setRepTipoId(Integer repTipoId) {
        this.repTipoId = repTipoId;
    }

    public String getRepMarca() {
        return repMarca;
    }

    public void setRepMarca(String repMarca) {
        this.repMarca = repMarca;
    }

    public Boolean getNfrOn() {
        return nfrOn;
    }

    public void setNfrOn(Boolean nfrOn) {
        this.nfrOn = nfrOn;
    }

    public Boolean getModelOn() {
        return modelOn;
    }

    public void setModelOn(Boolean modelOn) {
        this.modelOn = modelOn;
    }

    public Boolean getLocalOn() {
        return localOn;
    }

    public void setLocalOn(Boolean localOn) {
        this.localOn = localOn;
    }

    public Boolean getIdCompanyOn() {
        return idCompanyOn;
    }

    public void setIdCompanyOn(Boolean idCompanyOn) {
        this.idCompanyOn = idCompanyOn;
    }

    public Boolean getCommTypeOn() {
        return commTypeOn;
    }

    public void setCommTypeOn(Boolean commTypeOn) {
        this.commTypeOn = commTypeOn;
    }

    public Boolean getBiometricModuleOn() {
        return biometricModuleOn;
    }

    public void setBiometricModuleOn(Boolean biometricModuleOn) {
        this.biometricModuleOn = biometricModuleOn;
    }

    public Boolean getDateLastNSROn() {
        return dateLastNSROn;
    }

    public void setDateLastNSROn(Boolean dateLastNSROn) {
        this.dateLastNSROn = dateLastNSROn;
    }

    public Boolean getLastNSROn() {
        return lastNSROn;
    }

    public void setLastNSROn(Boolean lastNSROn) {
        this.lastNSROn = lastNSROn;
    }

    public Boolean getPathCollectFileOn() {
        return pathCollectFileOn;
    }

    public void setPathCollectFileOn(Boolean pathCollectFileOn) {
        this.pathCollectFileOn = pathCollectFileOn;
    }

    public Boolean getNsrOn() {
        return nsrOn;
    }

    public void setNsrOn(Boolean nsrOn) {
        this.nsrOn = nsrOn;
    }

    public Boolean getIpRoaterOn() {
        return ipRoaterOn;
    }

    public void setIpRoaterOn(Boolean ipRoaterOn) {
        this.ipRoaterOn = ipRoaterOn;
    }

    public Boolean getMaskOn() {
        return maskOn;
    }

    public void setMaskOn(Boolean maskOn) {
        this.maskOn = maskOn;
    }

    public Boolean getSenhaOn() {
        return senhaOn;
    }

    public void setSenhaOn(Boolean senhaOn) {
        this.senhaOn = senhaOn;
    }
}