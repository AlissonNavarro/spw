/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;

import java.io.Serializable;

/**
 *
 * @author prccardoso
 */
public class Empresa implements Serializable{
   
    private String razaoSocial;
    private String cnpj;
    private String address;
    private Integer cei;
    private Integer id;
    
    public Empresa(){
        
    }
    
    public Empresa(String cnpj, String razaoSocial, String address, Integer cei, Integer id) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.address = address;
        this.cei = cei;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    

    public Integer getCei() {
        return cei;
    }

    public void setCei(Integer cei) {
        this.cei = cei;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    
}
