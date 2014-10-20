/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.richfaces.model.selection.Selection;

/**
 *
 * @author Alexandre
 */
public class ConexaoBean implements Serializable {

    private Conexao conexao;
    private String driver = "jdbc:jtds:sqlserver://";
    private Conexao extraConn;
    private List<Conexao> connList;
    private Selection selectConn;
    private Boolean conected = false;

    public ConexaoBean() {
        conexao = new Conexao();
        extraConn = new Conexao();
        extraConn.CleanConexao();
        connList = new ArrayList<Conexao>();
    }

    public void reConstroi() {
        connList = new ArrayList<Conexao>();
        ConsultaConexoes();
    }

    public void ConsultaConexoes() {
        Banco banco = new Banco();
        if (banco.hasConnection) {
            connList = banco.consultaConexoes();
        }
    }

    public void showEditarConexao() {
        extraConn = new Conexao();
        extraConn.CleanConexao();
        Integer idConn = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idConn"));
        Banco banco = new Banco();
        extraConn = banco.consultaConexao(idConn);
    }

    public void adicionar() {
        Boolean flag = false;
        Banco banco = new Banco();
        flag = banco.addConexao(extraConn);
        extraConn.CleanConexao();
        ConsultaConexoes();
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Conexão adicionada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao adicionar conexão!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void editar() {
        Boolean flag = false;
        Banco banco = new Banco();
        flag = banco.editConexao(extraConn);
        extraConn.CleanConexao();
        ConsultaConexoes();
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Conexão editada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao editar conexão!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void excluir() {
        Boolean flag = false;
        Banco banco = new Banco();
        flag = banco.deleteConexao(extraConn);
        extraConn.CleanConexao();
        ConsultaConexoes();
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Conexão excluida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao excluir conexão!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void salvar() throws SQLException, ClassNotFoundException {
        String urlConexao = driver + conexao.getServer() + ":"
                + conexao.getPort() + "/"
                + conexao.getDatabase()
                + ";user=" + conexao.getUsuario()
                + ";password=" + conexao.getSenha();
        Boolean flag = Conectar(urlConexao, conexao.getServer(), conexao.getPort(), conexao.getDatabase(), conexao.getUsuario(), conexao.getSenha());
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Conectado sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro ao conectar!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        //return "navegarAdministrador";
    }

/*    public void salvar2() throws SQLException, ClassNotFoundException {
        String urlConexao = driver + conexao.getServer() + ":"
                + conexao.getPort() + "/"
                + conexao.getDatabase()
                + ";user=" + conexao.getUsuario()
                + ";password=" + conexao.getSenha();
        Boolean flag = Conectar(urlConexao, conexao.getServer(), conexao.getPort(), conexao.getDatabase(), conexao.getUsuario(), conexao.getSenha());
        conected = flag;
    }*/

    private Boolean Conectar(String url, String server, String port, String database, String usuario, String senha) throws SQLException {
        Connection c = null;
        boolean hasConexao = true;
        try {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                hasConexao = false;
            }
            c = DriverManager.getConnection(url);
        } catch (SQLException cnfe) {
            FacesMessage msgErro = new FacesMessage("Falha na Conexão!" + cnfe.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            hasConexao = false;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }

        if (hasConexao) {
            FacesMessage msgErro = new FacesMessage("Conexão estabelecida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            Metodos.setServerConexao(server);
            Metodos.setPortConexao(port);
            Metodos.setDataBaseConexao(database);
            Metodos.setUserConexao(usuario);
            Metodos.setPasswordConexao(senha);

            UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
            usuarioBean.setIsAtivo(true);
        } //else {
           // UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
           // usuarioBean.setIsAtivo(false);
        //}
        return hasConexao;
    }

    public void salvar(String arquivo, String conteudo)
            throws IOException {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(arquivo));
            out.write(conteudo);
            out.close();
        } catch (IOException e) {
        }
    }

    public Conexao getConexao() {
        return conexao;
    }

    public void setConexao(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<Conexao> getConnList() {
        return connList;
    }

    public void setConnList(List<Conexao> connList) {
        this.connList = connList;
    }

    public Conexao getExtraConn() {
        return extraConn;
    }

    public void setExtraConn(Conexao extraConn) {
        this.extraConn = extraConn;
    }

    public Selection getSelectConn() {
        return selectConn;
    }

    public void setSelectConn(Selection selectConn) {
        this.selectConn = selectConn;
    }

    public Boolean getConected() {
        return conected;
    }

    public void setConected(Boolean conected) {
        this.conected = conected;
    }
}
