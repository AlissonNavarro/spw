package Administracao;

import Usuario.UsuarioBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import manageBean.PerfilMB;

//@KeepAlive(ajaxOnly=true)
public class PermissaoBean implements Serializable {

    private List<Permissao> permissaoList;
    private List<SelectItem> departamentolist;
    private List<Integer> departamentolistDosFuncionarios;
    private List<SelectItem> regimelist;
    private List<SelectItem> perfillist;
    private String filterValue = "";
    private int page = 1;

    public PermissaoBean() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (usuarioBean.getIsAtivo()) {
            permissaoList = new ArrayList<Permissao>();
            Banco banco = new Banco();
            
            departamentolist = new ArrayList<SelectItem>();
            departamentolist = banco.consultaDepartamentoHierarquico();
            
            regimelist = new ArrayList<SelectItem>();
            regimelist = banco.consultaRegime();
            
            perfillist = new ArrayList<SelectItem>();
            PerfilMB perfilMB = new PerfilMB();
            perfillist = perfilMB.consultaPerfis();
            
            consultaDepartamentosListDosFuncionarios();
            if (usuarioBean != null) {
                if (usuarioBean.getEhSuperAdministrador()) {
                    permissaoList = banco.usuarioPermissaoSuperAdmin(filterValue);
                } else {
                    permissaoList = banco.usuarioPermissao(filterValue, departamentolistDosFuncionarios);
                }
            }
        }
    }

    public void reConstroi() {
        PerfilMB perfilMB = new PerfilMB();
        perfillist = new ArrayList<SelectItem>();
        perfillist = perfilMB.consultaPerfis();
    }

    public void teste() {
        permissaoList = new ArrayList<Permissao>();
        Banco banco = new Banco();
        departamentolist = new ArrayList<SelectItem>();
        departamentolist = banco.consultaDepartamentoHierarquico();
        regimelist = new ArrayList<SelectItem>();
        regimelist = banco.consultaRegime();
        permissaoList = banco.usuarioPermissao(filterValue, departamentolistDosFuncionarios);
    }

    public void consultaDepartamentosListDosFuncionarios() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentolistDosFuncionarios = banco.getTodosOsDescendentes(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
            departamentolistDosFuncionarios.add(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void filtrar() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        permissaoList = new ArrayList<Permissao>();
        if (usuarioBean.getUsuario().getPermissao().equals("")) {
            permissaoList = banco.usuarioPermissaoSuperAdmin(filterValue);
        } else {
            permissaoList = banco.usuarioPermissao(filterValue, departamentolistDosFuncionarios);
        }
    }

    public List<Permissao> getPermissaoList() {
        return permissaoList;
    }

    public void setPermissaoList(List<Permissao> permissaoList) {
        this.permissaoList = permissaoList;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<SelectItem> getRegimelist() {
        return regimelist;
    }

    public void setRegimelist(List<SelectItem> regimelist) {
        this.regimelist = regimelist;
    }

    public List<SelectItem> getPerfillist() {
        return perfillist;
    }

    public void setPerfillist(List<SelectItem> perfillist) {
        this.perfillist = perfillist;
    }
}
