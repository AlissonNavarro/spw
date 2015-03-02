package entidades;

import java.io.Serializable;

public class Empresa implements Serializable{
   
    private int id;
    private String cnpj;
    private String razaoSocial;
    private String endereco;
    private int cei;
    
    public Empresa() {        
        id = 0;
        cnpj = "";
        razaoSocial = "";
        endereco = "";
        cei = 0;
    }
    
    public Empresa(int id, String cnpj, String razaoSocial, String endereco, int cei) {
        this.id = id;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
        this.cei = cei;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public int getCei() {
        return cei;
    }

    public void setCei(int cei) {
        this.cei = cei;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        cnpj = cnpj.replace(".", "");
        cnpj = cnpj.replace("/", "");
        cnpj = cnpj.replace("-", "");
        cnpj = cnpj.replace(" ", "");
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

}
