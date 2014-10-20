/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import Metodos.Metodos;
import java.io.Serializable;

/**
 *
 * @author Alexandre
 */
public class Conexao implements Serializable{

    private String server;
    private String port;
    private String database;
    private String usuario;
    private String senha;
    private String stream;
    
    private int connid;
    private String desc;

    public Conexao() {
        server = Metodos.getServerConexao();
        port = Metodos.getPortConexao();
        database = Metodos.getDatabaseConexao();
        usuario = Metodos.getUserConexao();
        senha = Metodos.getPasswordConexao();
        stream = Metodos.getUrlConexao();
    }
    
    public Conexao (int connid, String server, String port, String database, String usuario, String senha, String desc) {
        this.connid = connid;
        this.server = server;
        this.port = port;
        this.database = database;
        this.usuario = usuario;
        this.senha = senha;
        this.desc = desc;
    }
    
    public void CleanConexao() {
        server = "";
        port = "";
        database = "";
        usuario = "";
        senha = "";
        stream = "";
        desc = "";
    }
    

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
    
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getConnid() {
        return connid;
    }

    public void setConnid(int connid) {
        this.connid = connid;
    }
    
}
