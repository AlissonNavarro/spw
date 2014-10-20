package Abono;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import excel.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Aloisio
 *
 */
public class topAbonoBean implements Serializable {

    private List<SelectItem> departamentolist;
    private List<SelectItem> criteriosList;
    private Integer criterio;
    private List<RelatorioAbono> relatoriosList;
    private Integer departamento;
    private Date dataInicio;
    private Date dataFim;
    private UsuarioBean usuarioBean;
    private Integer quantidadeLinhas;
    private Boolean incluirSubSetores;
    private Locale objLocale;

    public topAbonoBean() {
        Banco banco = new Banco();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Integer permissao;
        if (usuarioBean.getEhSuperAdministrador()) {
            permissao = 1;
        } else {
            permissao = Integer.parseInt(usuarioBean.getUsuario().getPermissao());
        }
        departamentolist = new ArrayList<SelectItem>();
        departamentolist = banco.consultaDepartamentoHierarquico(permissao);
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        criterio = 1;
        geraCriterios();
        quantidadeLinhas=10;

        //  consultaRelatorio();


    }

    private void geraCriterios() {
        criteriosList = new ArrayList<SelectItem>();
        criteriosList.add(new SelectItem(1, "Por Funcionario"));
        criteriosList.add(new SelectItem(2, "Por Responsável"));
        

    }

    private static Date getPrimeiroDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String dataPrimeiroDiaStr = sdfHora.format(data.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            data = df.parse(dataPrimeiroDiaStr);
        } catch (ParseException ex) {
        }
        return data;
    }

    public List<RelatorioAbono> ordenarRelatorios(List<RelatorioAbono> relatoriosList) {
        Collections.sort(relatoriosList, new Comparator() {

            public int compare(Object o1, Object o2) {
                RelatorioAbono p1 = (RelatorioAbono) o1;
                RelatorioAbono p2 = (RelatorioAbono) o2;
                return p1.getTotalAbonos() < p2.getTotalAbonos()
                        ? +1 : (p1.getTotalAbonos() > p2.getTotalAbonos()
                        ? -1 : 0);
            }
        });
        return relatoriosList;
    }

    public List<funcionarioEDetalhes> ordenarValores(List<funcionarioEDetalhes> inteiroEStringList) {
        Collections.sort(inteiroEStringList, new Comparator() {

            public int compare(Object o1, Object o2) {
                funcionarioEDetalhes p1 = (funcionarioEDetalhes) o1;
                funcionarioEDetalhes p2 = (funcionarioEDetalhes) o2;
                return p1.getDetalhes() < p2.getDetalhes()
                        ? +1 : (p1.getDetalhes() > p2.getDetalhes()
                        ? -1 : 0);
            }
        });
        return inteiroEStringList;
    }

    public void consultaRelatoriosList() {
        relatoriosList = new ArrayList<RelatorioAbono>();
        List<HistoricoAbono> historicoAbonoList = new ArrayList<HistoricoAbono>();
        if (isConsultaValida()) {
            Banco banco = new Banco();
            java.sql.Date dataInicio_ = new java.sql.Date(dataInicio.getTime());
            java.sql.Date dataFim_ = new java.sql.Date(dataFim.getTime());
            historicoAbonoList = banco.consultaHistoricoDetalhado(-1, incluirSubSetores, departamento, dataInicio_, dataFim_);

            HashMap relatorioMap = new HashMap<Integer, RelatorioAbono>();

            if (criterio == 1) {
                for (int i = 0; i < historicoAbonoList.size(); i++) {
                    HistoricoAbono historicoAbono = historicoAbonoList.get(i);
                    RelatorioAbono relatorioAbono = (RelatorioAbono) relatorioMap.get(historicoAbono.getCod_funcionario());
                    if (relatorioAbono == null) {
                        String categoriaJustificativa = historicoAbono.getCategoriaJustificativa();
                        Integer cod_funcionario = historicoAbono.getCod_funcionario();
                        String nomeFuncionario = historicoAbono.getFuncionario();
                        HashMap<String, Integer> qntPorCategoria = new HashMap<String, Integer>();
                        qntPorCategoria.put(categoriaJustificativa, 1);
                        relatorioAbono = new RelatorioAbono(qntPorCategoria, cod_funcionario, nomeFuncionario, 1);
                        List historicoAbonosPessoais = new ArrayList<HistoricoAbono>();
                        historicoAbonosPessoais.add(historicoAbono);
                        

                    } else {
                        String categoriaJustificativa = historicoAbono.getCategoriaJustificativa();
                        Integer cod_funcionario = historicoAbono.getCod_funcionario();
                        String nomeFuncionario = historicoAbono.getFuncionario();
                        HashMap<String, Integer> qntPorCategoria = relatorioAbono.getQntPorCategorias();
                        Integer qnt = qntPorCategoria.get(categoriaJustificativa);
                        
                        if (qnt == null) {
                            qnt = 1;
                            qntPorCategoria.put(categoriaJustificativa, qnt);
                        } else {
                            qnt++;
                            qntPorCategoria.put(categoriaJustificativa, qnt);
                        }
                        relatorioAbono.setQntPorCategorias(qntPorCategoria);
                        relatorioAbono.setTotalAbonos(relatorioAbono.getTotalAbonos() + 1);
                    }
                    relatorioMap.put(historicoAbono.getCod_funcionario(), relatorioAbono);
                }
                List<RelatorioAbono> listaDeValores = new ArrayList<RelatorioAbono>();
                listaDeValores.addAll(relatorioMap.values());
                listaDeValores = ordenarRelatorios(listaDeValores);
                relatoriosList = listaDeValores;
            } else {
                for (int i = 0; i < historicoAbonoList.size(); i++) {
                    HistoricoAbono historicoAbono = historicoAbonoList.get(i);
                    RelatorioAbono relatorioAbono = (RelatorioAbono) relatorioMap.get(historicoAbono.getCod_responsavel());
                    if (relatorioAbono == null) {
                        String categoriaJustificativa = historicoAbono.getFuncionario();
                        Integer cod_funcionario = historicoAbono.getCod_responsavel();
                        String nomeFuncionario = historicoAbono.getResponsavel();
                        HashMap<String, Integer> qntPorCategoria = new HashMap<String, Integer>();
                        qntPorCategoria.put(categoriaJustificativa, 1);
                        relatorioAbono = new RelatorioAbono(qntPorCategoria, cod_funcionario, nomeFuncionario, 1);
                        List historicoAbonosPessoais = new ArrayList<HistoricoAbono>();
                        historicoAbonosPessoais.add(historicoAbono);
                        

                    } else {
                        String categoriaJustificativa = historicoAbono.getFuncionario();
                        Integer cod_funcionario = historicoAbono.getCod_responsavel();
                        String nomeFuncionario = historicoAbono.getResponsavel();
                        HashMap<String, Integer> qntPorCategoria = relatorioAbono.getQntPorCategorias();
                        Integer qnt = qntPorCategoria.get(categoriaJustificativa);
                        
                        if (qnt == null) {
                            qnt = 1;
                            qntPorCategoria.put(categoriaJustificativa, qnt);
                        } else {
                            qnt++;
                            qntPorCategoria.put(categoriaJustificativa, qnt);
                        }
                        relatorioAbono.setQntPorCategorias(qntPorCategoria);
                        relatorioAbono.setTotalAbonos(relatorioAbono.getTotalAbonos() + 1);
                    }
                    relatorioMap.put(historicoAbono.getCod_responsavel(), relatorioAbono);
                }
                List<RelatorioAbono> listaDeValores = new ArrayList<RelatorioAbono>();
                listaDeValores.addAll(relatorioMap.values());
                listaDeValores = ordenarRelatorios(listaDeValores);
                relatoriosList = listaDeValores;

            }
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione uma data válida!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        showApresentacao();
    }

    public void gerar(){

        List<Linha> linhasList = new ArrayList<Linha>();
        List<String> cabecalho = new ArrayList<String>();
        List <MergeRegion> MergeRegionList = new ArrayList<MergeRegion>();
        Integer linhaInicioRegiao= 1;
        Integer linhaAtual= 1;
        Integer linhaFimRegiao= 1;
        if(criterio==1){
            cabecalho.add("Nome do Funcionário");
            cabecalho.add("Categoria");
            cabecalho.add("Quantidade");
            cabecalho.add("Total de Abonos");
        }else{
             cabecalho.add("Nome do Responsável");
            cabecalho.add("Funcionário");
            cabecalho.add("Quantidade");
            cabecalho.add("Total de Abonos");
        }
            for (int i = 0; i < relatoriosList.size(); i++) {

                RelatorioAbono relatorio = relatoriosList.get(i);

                List<funcionarioEDetalhes> detalhesList = relatorio.getQntPorCategoriaList();
                linhaInicioRegiao= linhaAtual;
                int j =0;
                while(j<detalhesList.size()&&j<quantidadeLinhas){                
                    ArrayList<String> conteudo = new ArrayList<String>();
                    conteudo.add(relatorio.getFuncionario());
                    funcionarioEDetalhes eDetalhes = detalhesList.get(j);
                    conteudo.add(eDetalhes.getFuncionario());
                    conteudo.add(eDetalhes.getDetalhes().toString());
                    conteudo.add(relatorio.getTotalAbonos().toString());
                    Linha linha = new Linha();
                    linha.setConteudoCelulasList(conteudo);
                    linhasList.add(linha);
                    linhaAtual++;
                    j++;
                }
                linhaFimRegiao=linhaAtual-1;
                MergeRegion mr = new MergeRegion(linhaInicioRegiao, linhaFimRegiao, 0, 0);
                MergeRegion mr2 = new MergeRegion(linhaInicioRegiao, linhaFimRegiao, 3, 3);
                MergeRegionList.add(mr);
                MergeRegionList.add(mr2);
            }SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        Metodos.setLogInfo("Gerar Excel Ranking Abono - Departamento: "+Metodos.buscaRotulo(departamento.toString(), departamentolist)+" Data Inicial: "+sf.format(dataInicio)+" Data Fim: "+sf.format(dataFim)+" Criterio: "+Metodos.buscaRotulo(criterio.toString(), criteriosList));
        Excel.gerarPlanilhaDownload("ResumoMensal.xls", cabecalho, linhasList,MergeRegionList);
    }

    public void showApresentacao() {
        if (relatoriosList != null) {
            for (int i = 0; i < relatoriosList.size(); i++) {
                RelatorioAbono relatorioAbono = relatoriosList.get(i);
                HashMap<String, Integer> qntPorCategorias = relatorioAbono.getQntPorCategorias();
                String s = "Sem abonos solicitados";
                Integer total = 0;
                ArrayList<funcionarioEDetalhes> listaDeValores = new ArrayList<funcionarioEDetalhes>();
                Set<String> conjunto = qntPorCategorias.keySet();
                for (Iterator<String> it = conjunto.iterator(); it.hasNext();) {
                    String categoria = it.next();
                    Integer qnt = qntPorCategorias.get(categoria);
                    total = total + qnt;
                    listaDeValores.add(new funcionarioEDetalhes(qnt, categoria));
                }

                List lista = ordenarValores(listaDeValores);
                ArrayList<funcionarioEDetalhes> valoresOrdenados = new ArrayList<funcionarioEDetalhes>();
                valoresOrdenados.addAll(lista);
                relatoriosList.get(i).setQntPorCategoriaList(valoresOrdenados);
            }
            for (int i = 0; i < relatoriosList.size(); i++) {
                RelatorioAbono relatorioAbono = relatoriosList.get(i);
                HashMap<String, Integer> qntPorCategorias = relatorioAbono.getQntPorCategorias();
                String s = "Sem abonos solicitados";
                Integer total = 0;
                ArrayList<funcionarioEDetalhes> listaDeValores = new ArrayList<funcionarioEDetalhes>();
                Set<String> conjunto = qntPorCategorias.keySet();
                for (Iterator<String> it = conjunto.iterator(); it.hasNext();) {
                    String categoria = it.next();
                    Integer qnt = qntPorCategorias.get(categoria);
                    total = total + qnt;
                    listaDeValores.add(new funcionarioEDetalhes(qnt, categoria));
                }

                List lista = ordenarValores(listaDeValores);
                ArrayList<funcionarioEDetalhes> valoresOrdenados = new ArrayList<funcionarioEDetalhes>();
                valoresOrdenados.addAll(lista);
                for (int j = 0; j < valoresOrdenados.size(); j++) {
                    funcionarioEDetalhes inteiroEString = listaDeValores.get(j);
                    if (j == 0) {
                        s = inteiroEString.funcionario + ": " + inteiroEString.getDetalhes();
                    } else {
                        s = s + "\n" + inteiroEString.funcionario + ": " + inteiroEString.getDetalhes();
                    }
                }
                relatorioAbono.setDetalhesApresentacao(s);
            }
        }
    }

    private Boolean isConsultaValida() {
        if ((null != dataInicio) && (null != dataFim)) {
            return true;
        } else {
            return false;
        }
    }

    private static Date getHoje() {
        return new Date();
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public Integer getCriterio() {
        return criterio;
    }

    public void setCriterio(Integer criterio) {
        this.criterio = criterio;
    }

    public List<SelectItem> getCriteriosList() {
        return criteriosList;
    }

    public void setCriteriosList(List<SelectItem> criteriosList) {
        this.criteriosList = criteriosList;
    }

    public List<RelatorioAbono> getRelatoriosList() {
        return relatoriosList;
    }

    public void setRelatoriosList(List<RelatorioAbono> relatoriosList) {
        this.relatoriosList = relatoriosList;
    }
    

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public Integer getQuantidadeLinhas() {
        return quantidadeLinhas;
    }

    public void setQuantidadeLinhas(Integer quantidadeLinhas) {
        this.quantidadeLinhas = quantidadeLinhas;
    }
    
}


