package CadastroHoraExtra;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class HoraExtraBean implements Serializable {

    private List<RegimeHoraExtra> regimeHoraExtraList;
    private RegimeHoraExtra editRegimeHoraExtra;
    private RegimeHoraExtra regimeHoraExtra;
    private List<TipoHoraExtra> tipoHoraExtraList;
    private TipoHoraExtra novoTipoHoraExtra;
    private TipoHoraExtra editTipoHoraExtra;
    private List<DetalheHoraExtra> detalheHoraExtraList;
    private List<DetalheGratificacao> detalheGratificacaoList;
    private List<Justificativa> justificativaList;
    private List<Gratificacao> gratificacaoList;
    private Gratificacao novaGratificacao;
    private Gratificacao editGratificacao;
    private String regime_param;
    private String cod_regime;
    private String tipo_param;
    private String gratificacao_param;
    private String insereNomeRegime;
    private String editRegime;
    private String justificativa;
    private Justificativa editJustificativa;
    private Boolean isPadrao;
    private Boolean feriadoCritico;
    private String migalhadePao;
    private String migalhadePaoRegime;
    private List<SelectItem> modoRegimeSelecItem;

    public HoraExtraBean() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (usuarioBean.getIsAtivo()) {
            Banco banco = new Banco();
            regimeHoraExtraList = new ArrayList<RegimeHoraExtra>();
            regimeHoraExtraList = banco.consultaRegimeHoraExtra();
            regimeHoraExtra = new RegimeHoraExtra();
            editRegimeHoraExtra = new RegimeHoraExtra();
            tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
            novoTipoHoraExtra = new TipoHoraExtra();
            novaGratificacao = new Gratificacao();
            editTipoHoraExtra = new TipoHoraExtra();
            detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
            detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
            editGratificacao = new Gratificacao();
            editJustificativa = new Justificativa();
            gratificacaoList = new ArrayList<Gratificacao>();
            migalhadePao = "";
        }
    }

    public void showInserir() {
        insereNomeRegime = "";
        novaGratificacao = new Gratificacao();
        tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
    }

    public void inserir() {
        tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        novaGratificacao = new Gratificacao();
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        Banco banco = new Banco();
        Boolean flag = banco.inserirNovoRegime(insereNomeRegime);
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Regime inserido com sucesso!");
            Metodos.setLogInfo("Adicionar Regime - Nome: " + insereNomeRegime);
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (insereNomeRegime.equals("")) {
                FacesMessage msgErro = new FacesMessage("Regime não inserido. O nome do regime não pode está em branco!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Regime não inserido. O nome do regime já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
        regimeHoraExtraList = new ArrayList<RegimeHoraExtra>();
        banco = new Banco();
        regimeHoraExtraList = banco.consultaRegimeHoraExtra();
    }

    public void showEditar() {
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        editRegimeHoraExtra = new RegimeHoraExtra();
        cod_regime = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_regime");
        Banco banco = new Banco();
        editRegime = banco.consultaEditRegimeHoraExtra(Integer.parseInt(cod_regime));
    }

    public void editar() {
        Banco banco = new Banco();
        if (null != editRegime) {          
            Boolean sucesso = banco.editRegimeHoraExtra(Integer.parseInt(cod_regime), editRegime);
            if (sucesso) {
                Metodos.setLogInfo("Editar Regime - Nome: " + editRegime);
                FacesMessage msgErro = new FacesMessage("Regime alterado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                regimeHoraExtraList = banco.consultaRegimeHoraExtra();
            } else {
                FacesMessage msgErro = new FacesMessage("Regime não alterado. O nome do regime já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("O nome do regime não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void deletar() {

        String cod_regime_ = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_regime");
        Banco banco = new Banco();
        String regimeDel = banco.consultaEditRegimeHoraExtra(Integer.parseInt(cod_regime_));
        banco = new Banco();
        boolean flag = banco.deleteRegimeHoraExtra(Integer.parseInt(cod_regime_));

        if (flag) {
            FacesMessage msgErro = new FacesMessage("Regime  deletado com sucesso.");
            Metodos.setLogInfo("Excluir Regime - Nome: " + regimeDel);
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            regime_param = null;
            migalhadePaoRegime = "";
            migalhadePao = "";
            tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
            detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
            detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        } else {
            FacesMessage msgErro = new FacesMessage("O regime não pode ser deletado por estar sendo utilizado em algum tipo de hora extra.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        regimeHoraExtraList = new ArrayList<RegimeHoraExtra>();
        banco = new Banco();
        regimeHoraExtraList = banco.consultaRegimeHoraExtra();
    }

    public List<RegimeHoraExtra> getRegimeHoraExtraList() {
        return regimeHoraExtraList;
    }

    public void consultaDetalheRegime() {

        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        getModoFiltroSelecItem();
        regime_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("regime_param");

        Integer regime_param_ = Integer.parseInt(regime_param);
        Banco banco = new Banco();

        regimeHoraExtra = banco.getRegimeHoraExtra(regime_param_);

        migalhadePaoRegime = regimeHoraExtra.getNome();
        migalhadePao = regimeHoraExtra.getNome();

        selecionarLinhaRegime(regime_param_);

        feriadoCritico = new Boolean(regimeHoraExtra.getFeriadoCritico());
        banco = new Banco();
        tipoHoraExtraList = banco.consultaTipoHoraExtra(regime_param_);
        banco = new Banco();
        justificativaList = banco.consultaJustificativasHoraExtra(regime_param_);
        banco = new Banco();
        gratificacaoList = banco.consultaGratificacao(regime_param_);
    }

    public void setFeriadoCritico() {
        Banco banco = new Banco();
        Integer cod_regime_ = Integer.parseInt(regime_param);
        banco.updateFeriadoCritico(cod_regime_, feriadoCritico);
        regimeHoraExtraList = banco.consultaRegimeHoraExtra();
    }

    public void showInserirTipo() {
        novoTipoHoraExtra = new TipoHoraExtra();
        novoTipoHoraExtra.setCod_regime(Integer.parseInt(regime_param));
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
    }

    public void inserirTipo() {
        Banco banco = new Banco();

        Boolean flag = banco.inserirNovoTipo(novoTipoHoraExtra);
        if (flag) {
            Metodos.setLogInfo("Alterar Regime - Adicionar Tipo Hora Extra - Nome do Regime: " + regimeHoraExtra.getNome() + " Nome Tipo Hora Extra: " + novoTipoHoraExtra.getNome());
            FacesMessage msgErro = new FacesMessage("Tipo inserido com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (novoTipoHoraExtra.getNome().equals("")) {
                FacesMessage msgErro = new FacesMessage("Tipo não inserido. O nome do tipo não pode está em branco!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Tipo não inserido. O nome do tipo já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
        tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        banco = new Banco();
        tipoHoraExtraList = banco.consultaTipoHoraExtra(Integer.parseInt(regime_param));
    }

    public void showInserirGratificacao() {
        novaGratificacao = new Gratificacao();
        novaGratificacao.setCod_regime(Integer.parseInt(regime_param));
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
    }

    public void inserirGratificacao() {
        Banco banco = new Banco();
        Boolean flag = banco.inserirGratificacao(novaGratificacao);
        if (flag) {
            Metodos.setLogInfo("Alterar Regime - Adicionar Gratificação - Nome Regime: " + regimeHoraExtra.getNome() + " Nome Gratificacao: " + novaGratificacao.getNome() + " Valor: " + novaGratificacao.getValor() + " Verba: " + novaGratificacao.getVerba());
            FacesMessage msgErro = new FacesMessage("Gratificacão inserida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (novaGratificacao.getNome().equals("")) {
                FacesMessage msgErro = new FacesMessage("Gratificacão não inserida. O nome da gratificação não pode estar em branco!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Gratificacão não inserida. O nome da gratificação já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
        gratificacaoList = new ArrayList<Gratificacao>();
        banco = new Banco();
        gratificacaoList = banco.consultaGratificacao(Integer.parseInt(regime_param));
    }

    public void showEditarTipo() {
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        editTipoHoraExtra = new TipoHoraExtra();
        Banco banco = new Banco();
        tipo_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_tipo");
        editTipoHoraExtra = banco.consultaEditTipoHoraExtra(Integer.parseInt(tipo_param));
    }

    public void editarTipo() {
        Banco banco = new Banco();
        if (!editTipoHoraExtra.getNome().equals("")) {
            Boolean sucesso = banco.editTipoHoraExtra(Integer.parseInt(regime_param), Integer.parseInt(tipo_param), editTipoHoraExtra);
            if (sucesso) {
                Metodos.setLogInfo("Alterar Regime - Editar Tipo Hora Extra - Nome do Regime: " + regimeHoraExtra.getNome() + " Nome do Tipo Hora Extra:" + editTipoHoraExtra.getNome() + " Valor: " + editTipoHoraExtra.getValor() + " Código da verba: " + editTipoHoraExtra.getVerba());
                FacesMessage msgErro = new FacesMessage("Tipo alterado com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Tipo não alterado. O nome do tipo já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("O nome do tipo não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        banco = new Banco();
        Integer param = Integer.parseInt(regime_param);
        tipoHoraExtraList = banco.consultaTipoHoraExtra(Integer.parseInt(regime_param));
        selecionarLinhaTipo(param);
    }

    public void showEditarGratificacao() {
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        editGratificacao = new Gratificacao();
        Banco banco = new Banco();
        gratificacao_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_gratificacao");
        editGratificacao = banco.consultaEditGratificacao(Integer.parseInt(gratificacao_param));
    }

    public void editarGratificacao() {
        Banco banco = new Banco();
        if (!editGratificacao.getNome().equals("")) {
            Boolean sucesso = banco.editGratificacao(Integer.parseInt(regime_param), Integer.parseInt(gratificacao_param), editGratificacao);
            if (sucesso) {
                Metodos.setLogInfo("Alterar Regime - Editar Gratificação - Nome Regime: " + regimeHoraExtra.getNome() + " Nome Gratificação: " + editGratificacao.getNome() + " Valor: " + editGratificacao.getValor() + " Verba: " + editGratificacao.getVerba());
                FacesMessage msgErro = new FacesMessage("Gratificação alterada com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Gratificação não alterada. O nome da gratificação já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da gratificação não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        gratificacaoList = new ArrayList<Gratificacao>();
        banco = new Banco();
        gratificacaoList = banco.consultaGratificacao(Integer.parseInt(regime_param));
    }

    public void deletarTipo() {
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        String cod_tipo = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_tipo");
        Banco banco = new Banco();
        String tipoDel = banco.consultaTipoHoraExtra(regimeHoraExtra.getCod(), Integer.parseInt(cod_tipo));
        banco = new Banco();
        String flag = banco.deleteTipoHoraExtra(Integer.parseInt(regime_param), Integer.parseInt(cod_tipo));

        if (flag.equals("0")) {
            Metodos.setLogInfo("Alterar Regime - Excluir Tipo Hora Extra - Nome do Regime: " + regimeHoraExtra.getNome() + " Nome do Tipo da Hora Extra: " + tipoDel); //regimeHoraExtraList tipoHoraExtraList
            FacesMessage msgErro = new FacesMessage("Tipo deletado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag.equals("1")) {
            FacesMessage msgErro = new FacesMessage("O tipo não pode ser deletado por está sendo utilizado!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag.equals("2")) {
            FacesMessage msgErro = new FacesMessage("O tipo não pode ser deletado por ser o padrão!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        banco = new Banco();
        Integer param = Integer.parseInt(regime_param);
        tipoHoraExtraList = banco.consultaTipoHoraExtra(Integer.parseInt(regime_param));
        selecionarLinhaTipo(param);
    }

    public void deletarGratificacao() {
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();

        String cod_gratificacao = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_gratificacao");

        Banco banco = new Banco();
        String gratificacaoDel = banco.consultaGratificacao(regimeHoraExtra.getCod(), Integer.parseInt(cod_gratificacao));

        banco = new Banco();
        String flag = banco.deleteGratificacao(Integer.parseInt(cod_gratificacao));

        if (flag.equals("0")) {
            Metodos.setLogInfo("Alterar Regime - Excluir Gratificação - Nome Regime: " + regimeHoraExtra.getNome() + " Nome Gratificacao: " + gratificacaoDel);
            FacesMessage msgErro = new FacesMessage("Gratificação deletada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (flag.equals("1")) {
            FacesMessage msgErro = new FacesMessage("A gratificação não pode ser deletada por estar sendo utilizada!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        gratificacaoList = new ArrayList<Gratificacao>();
        banco = new Banco();
        gratificacaoList = banco.consultaGratificacao(Integer.parseInt(regime_param));
    }

    public void consultaDetalheHoraExtra() {
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        Banco banco = new Banco();
        tipo_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_tipo");
        Integer param = Integer.parseInt(tipo_param);
        String tipo_nome_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo_nome_param");
        migalhadePao = migalhadePaoRegime + " >> " + tipo_nome_param;
        selecionarLinhaTipo(param);
        detalheHoraExtraList = banco.consultaDetalheHoraExtra(param);
    }

    public void consultaDetalheGratificacao() {
        detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        Banco banco = new Banco();
        gratificacao_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_gratificacao");
        Integer param = Integer.parseInt(gratificacao_param);
        String gratificacao_nome_param = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gratificacao_nome_param");
        migalhadePao = migalhadePaoRegime + " >> " + gratificacao_nome_param;
        selecionarLinhaTipo(param);
        detalheGratificacaoList = banco.consultaDetalheGratificacao(param);
    }

    public void editDetalhe() {

        Banco banco = new Banco();
        String diaStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dia");
        Integer dia = Integer.parseInt(diaStr);
        Integer cod_tipo = Integer.parseInt(tipo_param);
        banco.editDetalheInicio(cod_tipo, dia, detalheHoraExtraList.get(dia - 1).getInicio());
        Metodos.setLogInfo("Alterar Regime - Alterar Tipo Hora Extra - Alterar Regra - Dia: " + detalheHoraExtraList.get(dia - 1).getDiaStr() + " Inicio: " + detalheHoraExtraList.get(dia - 1).getInicio() + " Dia inteiro: " + ((detalheHoraExtraList.get(dia - 1).getIsDiaInteiro()) ? "Sim" : "Não"));
    }

    public void editDetalheInicio() {
        Banco banco = new Banco();
        String diaStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dia");
        Integer dia = Integer.parseInt(diaStr);
        Integer cod_gratificacao = Integer.parseInt(gratificacao_param);

        banco.editDetalheGratificacaoInicio(cod_gratificacao, dia, detalheGratificacaoList.get(dia - 1).getInicio());
        Metodos.setLogInfo("Alterar Regime - Alterar Gratificação - Alterar Regra - Dia: " + detalheGratificacaoList.get(dia - 1).getDiaStr() + " Inicio: " + detalheGratificacaoList.get(dia - 1).getInicio() + " Fim: " + detalheGratificacaoList.get(dia - 1).getFim());
    }

    public void editDetalheFim() {
        Banco banco = new Banco();
        String diaStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dia");
        Integer dia = Integer.parseInt(diaStr);
        Integer cod_gratificacao = Integer.parseInt(gratificacao_param);
        banco.editDetalheGratificacaoFim(cod_gratificacao, dia, detalheGratificacaoList.get(dia - 1).getFim());
        Metodos.setLogInfo("Alterar Regime - Alterar Gratificação - Alterar Regra - Dia: " + detalheGratificacaoList.get(dia - 1).getDiaStr() + " Inicio: " + detalheGratificacaoList.get(dia - 1).getInicio() + " Fim: " + detalheGratificacaoList.get(dia - 1).getFim());
    }

    public void editDetalheIsDiaInteiro() {
        Banco banco = new Banco();
        String diaStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dia");
        Integer dia = Integer.parseInt(diaStr);
        Integer cod_tipo = Integer.parseInt(tipo_param);
        banco.editDetalheDiaInteiro(cod_tipo, dia, detalheHoraExtraList.get(dia - 1).getIsDiaInteiro());
        Metodos.setLogInfo("Alterar Regime - Alterar Tipo Hora Extra - Alterar Regra - Dia: " + detalheHoraExtraList.get(dia - 1).getDiaStr() + " Inicio: " + detalheHoraExtraList.get(dia - 1).getInicio() + " Dia inteiro: " + ((detalheHoraExtraList.get(dia - 1).getIsDiaInteiro()) ? "Sim" : "Não"));
    }

    public void setTipoPadrao() {
        String cod_tipoStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_tipo");
        Integer cod_tipo = Integer.parseInt(cod_tipoStr);
        Banco banco = new Banco();
        Integer param_regime = Integer.parseInt(regime_param);
        banco.setTipoPadrao(param_regime, cod_tipo);
        tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        banco = new Banco();
        tipoHoraExtraList = banco.consultaTipoHoraExtra(Integer.parseInt(regime_param));
        banco = new Banco();
        TipoHoraExtra tipoDel = banco.consultaTipoHoraExtraTipo(regimeHoraExtra.getCod(), cod_tipo);
        Metodos.setLogInfo("Alterar Regime - Editar Tipo Hora Extra - Nome do Regime: " + regimeHoraExtra.getNome() + " Nome do Tipo Hora Extra:" + tipoDel.getNome() + " Tipo Padrão: " + (tipoDel.getIsPadrao() ? "Sim" : "Não"));
    }

    private void selecionarLinhaRegime(Integer param) {
        for (Iterator<RegimeHoraExtra> it = regimeHoraExtraList.iterator(); it.hasNext();) {
            RegimeHoraExtra regimeHoraExtra_ = it.next();
            if (regimeHoraExtra_.getCod().equals(param)) {
                regimeHoraExtra_.setRegime_css("selecionado");
            } else {
                regimeHoraExtra_.setRegime_css("label");
            }
        }
    }

    private void selecionarLinhaTipo(Integer param) {
        isPadrao = false;
        for (Iterator<TipoHoraExtra> it = tipoHoraExtraList.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra = it.next();
            if (tipoHoraExtra.getCod_tipo().equals(param)) {
                tipoHoraExtra.setTipo_css("selecionado");
                if (tipoHoraExtra.getIsPadrao()) {
                    isPadrao = true;
                }
            } else {
                tipoHoraExtra.setTipo_css("label");
            }
        }
    }

    public void inserirJustificativa() {
        Banco banco = new Banco();
        Boolean flag = banco.inserirNovaJustificativaHoraExtra(Integer.parseInt(regime_param), justificativa);
        if (flag) {
            Metodos.setLogInfo("Alterar Regime - Adicionar Justificativa Hora Extra - Nome do Regime: " + regimeHoraExtra + " Nome da Justificativa: " + justificativa);
            FacesMessage msgErro = new FacesMessage("Justificativa inserida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (justificativa.equals("")) {
                FacesMessage msgErro = new FacesMessage("Justificativa não inserida. A justificativa não pode estar em branco!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Justificativa não inserida. Ela já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
        justificativaList = new ArrayList<Justificativa>();
        banco = new Banco();
        justificativaList = banco.consultaJustificativasHoraExtra(Integer.parseInt(regime_param));
        justificativa = new String();

    }

    public void showEditarJustificativa() {
        editJustificativa = new Justificativa();
        String cod_justificativa = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_justificativa_param");
        String descricao_justificativa = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("descricao_justificativa_param");
        editJustificativa.setCod_justificativa(Integer.parseInt(cod_justificativa));
        editJustificativa.setCod_regime(Integer.parseInt(regime_param));
        editJustificativa.setDescricao(descricao_justificativa);
    }

    public void editarJustificativa() {
        Banco banco = new Banco();
        if (!editJustificativa.getDescricao().equals("")) {
            Boolean sucesso = banco.editJustificativaHoraExtra(editJustificativa);
            if (sucesso) {
                Metodos.setLogInfo("Alterar Regime - Alterar Justificativa Hora Extra - Nome do Regime: " + regimeHoraExtra.getNome() + " Nome da Justificativa: " + editJustificativa.getDescricao());
                FacesMessage msgErro = new FacesMessage("Justificativa alterada com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Justificativa não alterada. O nome da justificativa já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da justificativa não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        justificativaList = new ArrayList<Justificativa>();
        banco = new Banco();
        justificativaList = banco.consultaJustificativasHoraExtra(Integer.parseInt(regime_param));
    }

    public void deletarJustificativa() {
        String cod_justificativa = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_justificativa_param");
        Banco banco = new Banco();
        String justificativaDel = banco.consultaJustificativa(regimeHoraExtra.getCod(), Integer.parseInt(cod_justificativa));
        banco = new Banco();
        Boolean flag = banco.deleteJustificativaHoraExtra(Integer.parseInt(cod_justificativa));

        if (flag) {
            Metodos.setLogInfo("Alterar Regime - Excluir Justificativa Hora Extra - Nome do Regime: " + regimeHoraExtra.getNome() + " Nome da Justificativa: " + justificativaDel);
            FacesMessage msgErro = new FacesMessage("Justificativa deletada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("A justificativa não pode ser deletada por está sendo utilizada!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        justificativaList = new ArrayList<Justificativa>();
        banco = new Banco();
        justificativaList = banco.consultaJustificativasHoraExtra(Integer.parseInt(regime_param));

    }

    public void mudarModoTolerancia() {
        Banco banco = new Banco();
        Integer cod_regime_ = regimeHoraExtra.getCod();
        Integer modoRegimeInt = regimeHoraExtra.getModoTolerancia();
        banco.mudarModoRegime(cod_regime_, modoRegimeInt);
    }
    
    public void mudarFeriadoCritico(){
        Banco banco = new Banco();
        Integer cod_regime_ = regimeHoraExtra.getCod();
        Boolean modoRegimeBool = regimeHoraExtra.getFeriadoCritico();
        banco.mudarFeriadoCritico(cod_regime_, modoRegimeBool);
    }

    public void mudarTolerancia() {
        Banco banco = new Banco();
        Integer cod_regime_ = regimeHoraExtra.getCod();
        Integer tolerancia = regimeHoraExtra.getTolerancia();
        banco.mudarTolerancia(cod_regime_, tolerancia);
        Metodos.setLogInfo("Alterar Regime - Mudar Tempo de Tolerância - Nome: " + regimeHoraExtra.getNome() + " Tempo: " + tolerancia);
    }

    public List<SelectItem> getModoFiltroSelecItem() {
        modoRegimeSelecItem = new ArrayList<SelectItem>();
        modoRegimeSelecItem.add(new SelectItem(1, "Celetista"));
        modoRegimeSelecItem.add(new SelectItem(2, "Estatutário"));
        return modoRegimeSelecItem;
    }

    public void setRegimeHoraExtraList(List<RegimeHoraExtra> regimeHoraExtraList) {
        this.regimeHoraExtraList = regimeHoraExtraList;
    }

    public RegimeHoraExtra getEditRegimeHoraExtra() {
        return editRegimeHoraExtra;
    }

    public void setEditRegimeHoraExtra(RegimeHoraExtra editRegimeHoraExtra) {
        this.editRegimeHoraExtra = editRegimeHoraExtra;
    }

    public TipoHoraExtra getEditTipoHoraExtra() {
        return editTipoHoraExtra;
    }

    public void setEditTipoHoraExtra(TipoHoraExtra editTipoHoraExtra) {
        this.editTipoHoraExtra = editTipoHoraExtra;
    }

    public TipoHoraExtra getNovoTipoHoraExtra() {
        return novoTipoHoraExtra;
    }

    public void setNovoTipoHoraExtra(TipoHoraExtra novoTipoHoraExtra) {
        this.novoTipoHoraExtra = novoTipoHoraExtra;
    }

    public List<TipoHoraExtra> getTipoHoraExtraList() {
        return tipoHoraExtraList;
    }

    public void setTipoHoraExtraList(List<TipoHoraExtra> tipoHoraExtraList) {
        this.tipoHoraExtraList = tipoHoraExtraList;
    }

    public String getRegime_param() {
        return regime_param;
    }

    public void setRegime_param(String regime_param) {
        this.regime_param = regime_param;
    }

    public String getTipo_param() {
        return tipo_param;
    }

    public void setTipo_param(String tipo_param) {
        this.tipo_param = tipo_param;
    }

    public List<DetalheHoraExtra> getDetalheHoraExtraList() {
        return detalheHoraExtraList;
    }

    public void setDetalheHoraExtraList(List<DetalheHoraExtra> detalheHoraExtraList) {
        this.detalheHoraExtraList = detalheHoraExtraList;
    }

    public String getEditRegime() {
        return editRegime;
    }

    public void setEditRegime(String editRegime) {
        this.editRegime = editRegime;
    }

    public String getInsereNomeRegime() {
        return insereNomeRegime;
    }

    public void setInsereNomeRegime(String insereNomeRegime) {
        this.insereNomeRegime = insereNomeRegime;
    }

    public Boolean getIsPadrao() {
        return isPadrao;
    }

    public void setIsPadrao(Boolean isPadrao) {
        this.isPadrao = isPadrao;
    }

    public List<Justificativa> getJustificativaList() {
        return justificativaList;
    }

    public void setJustificativaList(List<Justificativa> justificativaList) {
        this.justificativaList = justificativaList;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Justificativa getEditJustificativa() {
        return editJustificativa;
    }

    public void setEditJustificativa(Justificativa editJustificativa) {
        this.editJustificativa = editJustificativa;
    }

    public String getMigalhadePao() {
        return migalhadePao;
    }

    public void setMigalhadePao(String migalhadePao) {
        this.migalhadePao = migalhadePao;
    }

    public List<Gratificacao> getGratificacaoList() {
        return gratificacaoList;
    }

    public void setGratificacaoList(List<Gratificacao> gratificacaoList) {
        this.gratificacaoList = gratificacaoList;
    }

    public Gratificacao getEditGratificacao() {
        return editGratificacao;
    }

    public void setEditGratificacao(Gratificacao editGratificacao) {
        this.editGratificacao = editGratificacao;
    }

    public Gratificacao getNovaGratificacao() {
        return novaGratificacao;
    }

    public void setNovaGratificacao(Gratificacao novaGratificacao) {
        this.novaGratificacao = novaGratificacao;
    }

    public List<DetalheGratificacao> getDetalheGratificacaoList() {
        return detalheGratificacaoList;
    }

    public void setDetalheGratificacaoList(List<DetalheGratificacao> detalheGratificacaoList) {
        this.detalheGratificacaoList = detalheGratificacaoList;
    }

    public Boolean getFeriadoCritico() {
        return feriadoCritico;
    }

    public void setFeriadoCritico(Boolean feriadoCritico) {
        this.feriadoCritico = feriadoCritico;
    }

    public List<SelectItem> getModoRegimeSelecItem() {
        return modoRegimeSelecItem;
    }

    public void setModoRegimeSelecItem(List<SelectItem> modoRegimeSelecItem) {
        this.modoRegimeSelecItem = modoRegimeSelecItem;
    }

    public RegimeHoraExtra getRegimeHoraExtra() {
        return regimeHoraExtra;
    }

    public void setRegimeHoraExtra(RegimeHoraExtra regimeHoraExtra) {
        this.regimeHoraExtra = regimeHoraExtra;
    }
}
