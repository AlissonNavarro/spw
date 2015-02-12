package perfil2;

import Usuario.UsuarioBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class PerfilBean implements Serializable {

    private List<SelectItem> perfisList;
    private Integer perfilSelecionado;
    private Perfil perfilEdit;
    private String novoPerfil;
    private String nomeEditPerfil;

    //construtor Provisorio
    public PerfilBean() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (usuarioBean.getIsAtivo()) {
            perfisList = new ArrayList<SelectItem>();
            perfilEdit = new Perfil();
            consultaPerfis();
        }
    }

    public void consultaPermissoesPerfil() {
        PerfilMB banco = new PerfilMB();
        perfilEdit = banco.consultaPermissoesPerfil(perfilSelecionado);
    }

    public void consultaPerfis() {
        PerfilMB banco = new PerfilMB();
        perfisList = new ArrayList<SelectItem>();
        perfisList = banco.consultaPerfis();
    }

    public void showPerfilNovo() {
        novoPerfil = "";
    }

    public void showEditar() {
        nomeEditPerfil = perfilEdit.getNome_perfil();
    }

    public void salvarEditPerfil() {
        PerfilMB banco = new PerfilMB();
        perfilEdit.setNome_perfil(nomeEditPerfil.toUpperCase());
        int flag = banco.salvarEditPerfil(perfilEdit);

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Perfil atualizado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("O nome do perfil já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new PerfilMB();
        perfisList = banco.consultaPerfis();
        banco = new PerfilMB();

    }

    public void setaSuperAbas() {

        perfilEdit.setCadastrosEConfiguracoes(perfilEdit.getPermissoes()
                || perfilEdit.getFuncionarios()
                || perfilEdit.getDeptos()
                || perfilEdit.getEmpresas()
                || perfilEdit.getCargos()
                || perfilEdit.getFeriados()
                || perfilEdit.getJustificativas()
                || perfilEdit.getVerbas()
                || perfilEdit.getTituloDoRelatorio()
                || perfilEdit.getHoraExtraEGratificacoes());

        perfilEdit.setConsInd(perfilEdit.getFreqnComEscala()
                || perfilEdit.getFreqnSemEscala()
                || perfilEdit.getHoraExtra());

        perfilEdit.setHorCronoJorn(perfilEdit.getHorarios()
                || perfilEdit.getJornadas()
                || perfilEdit.getCronogramas()
                || perfilEdit.getAfastamento());

        perfilEdit.setRelatorios(perfilEdit.getRelatorioMensComEscala()
                || perfilEdit.getRelatorioMensSemEscala()
                || perfilEdit.getRelatorioDeResumodeEscalas()
                || perfilEdit.getRelatorioDeResumodeFrequencias()
                || perfilEdit.getRelatorioCatracas()
                || perfilEdit.getAfdt()
                || perfilEdit.getAcjef()
                || perfilEdit.getEspelhoDePonto()
                || perfilEdit.getRelatorioLogDeOperacoes());
        
        perfilEdit.setPresenca(perfilEdit.getListaDePresenca()
                || perfilEdit.getListaDePresenca()
                || perfilEdit.getConsultaIrregulares()
                || perfilEdit.getConsultaHoraExtra());

        perfilEdit.setAbonos(perfilEdit.getSolicitacao()
                || perfilEdit.getDiasEmAberto()
                || perfilEdit.getAbonosRapidos()
                || perfilEdit.getHistoricoAbono()
                || perfilEdit.getAbonosEmMassa()
                || perfilEdit.getRankingAbono());
        
        perfilEdit.setManutencao(perfilEdit.getScanIP()
                || perfilEdit.getDownloadAfd()
                || perfilEdit.getListaRelogios()
                || perfilEdit.getRelogioPonto());

    }

    public void salvarAlteracoesPerfil() {

        setaSuperAbas();
        PerfilMB banco = new PerfilMB();
        Integer flag = banco.salvarAlteracoesPerfil(perfilEdit);

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Perfil atualizado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void salvarNovoPerfil() {

        PerfilMB banco = new PerfilMB();
        Integer flag = banco.adicionarPerfil(novoPerfil.toUpperCase());

        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Perfil adicionado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("O nome do Perfil já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new PerfilMB();
        perfisList = banco.consultaPerfis();
        banco = new PerfilMB();

        /*
         * banco.consultaPerfil(perfilSelecionado);
         *
         */
    }

    public void excluirPerfil() {
        PerfilMB banco = new PerfilMB();

        Boolean flag = false;
        if (perfilSelecionado != -1) {
            flag = banco.excluirPerfil(perfilSelecionado);

            if (flag == false) {
                FacesMessage msgErro = new FacesMessage("Perfil excluido com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == true) {
                FacesMessage msgErro = new FacesMessage("O perfil não pode ser excluido!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new PerfilMB();
            perfisList = banco.consultaPerfis();
            banco = new PerfilMB();
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione um perfil válido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

    }

    public Perfil getPerfilEdit() {
        return perfilEdit;
    }

    public void setPerfilEdit(Perfil perfilEdit) {
        this.perfilEdit = perfilEdit;
    }

    public Integer getPerfilSelecionado() {
        return perfilSelecionado;
    }

    public void setPerfilSelecionado(Integer perfilSelecionado) {
        this.perfilSelecionado = perfilSelecionado;
    }

    public List<SelectItem> getPerfisList() {
        return perfisList;
    }

    public void setPerfisList(List<SelectItem> perfisList) {
        this.perfisList = perfisList;
    }

    public String getNovoPerfil() {
        return novoPerfil;
    }

    public void setNovoPerfil(String novoPerfil) {
        this.novoPerfil = novoPerfil;
    }

    public String getNomeEditPerfil() {
        return nomeEditPerfil;
    }

    public void setNomeEditPerfil(String nomeEditPerfil) {
        this.nomeEditPerfil = nomeEditPerfil;
    }
}




