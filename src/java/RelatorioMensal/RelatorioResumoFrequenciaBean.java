/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import CadastroHoraExtra.Gratificacao;
import Metodos.Metodos;
import CadastroHoraExtra.TipoHoraExtra;
import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import ConsultaPonto.Ponto;
import ConsultaPonto.SituacaoPonto;
import Funcionario.Funcionario;
import Usuario.UsuarioBean;
import excel.Excel;
import excel.Linha;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author amsgama
 */
public class RelatorioResumoFrequenciaBean implements Serializable {

    private String departamentoSelecionado;
    private Integer escalaSelecionada;
    private List<SelectItem> departamentosSelecItem;
    private List<SelectItem> escalasSelecItem;
    private List<String> cabecalhoHoraExtraList;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private Boolean incluirSubSetores;
    private List<RelatorioResumoFrequencia> relatorioResumoFrequencia;
    private List<Movimento> movimentoList;
    private List<SelectItem> funcionarioList;
    private Integer cod_funcionario;
    private Integer page;
    private RelatorioResumoFrequenciaEscolhaExcel escolhaExcel;
    private Boolean isAdministradorVisivel;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private RelatorioResumoFrequenciaTotalizador relatorioResumoFrequenciaTotalizador;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    private List<Integer> verbasList;
    static Connection theConn;
    static String connWay = "D:\\BancoRH\\BDSGRH.MDB";

    public RelatorioResumoFrequenciaBean() {
        inicializarAtributos();
        relatorioResumoFrequencia = new ArrayList<RelatorioResumoFrequencia>();
        departamentosSelecItem = new ArrayList<SelectItem>();
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        cod_funcionario = -1;
        page = 1;
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        escolhaExcel = new RelatorioResumoFrequenciaEscolhaExcel();
        consultaDepartamento();
        verbasList = verbasList();
    }

    public void consultaFuncionario() throws SQLException {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        if (Integer.parseInt(departamentoSelecionado) != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }
        funcionarioList = filtrarFuncionario();
    }

    public void consultar() {
        relatorioResumoFrequencia = new ArrayList<RelatorioResumoFrequencia>();
        movimentoList = new ArrayList<Movimento>();
        page = 1;

        if (!(departamentoSelecionado.equals("-1") || cod_funcionario.equals(-1)) && valideData()) {

            ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

            Date dataInicio_ = (Date) dataInicio.clone();
            Date dataFim_ = (Date) dataFim.clone();

            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            List<Integer> matriculasList = new ArrayList<Integer>();
            relatorioResumoFrequencia = new ArrayList<RelatorioResumoFrequencia>();
            Banco banco = new Banco();
            if (!cod_funcionario.equals(0) && !cod_funcionario.equals(-1)) {
                matriculasList.add(cod_funcionario);
            } else {
                matriculasList = getFuncionarios();
            }

            cabecalhoHoraExtraList = new ArrayList<String>();
            for (Iterator<Integer> it = matriculasList.iterator(); it.hasNext();) {
                dataInicio_ = (Date) dataInicio.clone();
                dataFim_ = (Date) dataFim.clone();
                c = new ConsultaFrequenciaComEscalaBean("");
                c.setDataInicio(dataInicio_);
                c.setDataFim(dataFim_);
                Integer userid = it.next();
                c.setCod_funcionario(userid);
                c.consultaDiasSemMsgErro();

                if (!c.getDiasList().isEmpty()) {

                    banco = new Banco();
                    Funcionario funcionario = banco.consultaFuncionario(userid);
                    RelatorioResumoFrequencia relatorioResumoFrequencia_ = new RelatorioResumoFrequencia();
                    relatorioResumoFrequencia_.setUserid(userid);
                    relatorioResumoFrequencia_.setNome(funcionario.getNome());
                    relatorioResumoFrequencia_.setCargo(funcionario.getCargoStr());
                    relatorioResumoFrequencia_.setCpf(funcionario.getCpf());
                    if(funcionario.getMat_emcs() != 0)
                        relatorioResumoFrequencia_.setMatricula(funcionario.getMat_emcs().toString());
                    else
                        relatorioResumoFrequencia_.setMatricula(funcionario.getMatricula());
                    relatorioResumoFrequencia_.setDepartamento(funcionario.getDept());
                    relatorioResumoFrequencia_.setRegime(funcionario.getNome_regime());
                    List<String> horaExtraList = getTiposHoraExtra(c.getHoraExtra());
                    relatorioResumoFrequencia_.setHoraExtra(horaExtraList);
                    addCabecalhoHoraExtra(cabecalhoHoraExtraList, horaExtraList);

                    if (funcionario.getDataContratação() == null) {
                        relatorioResumoFrequencia_.setDataAdmicao("");
                    } else {
                        relatorioResumoFrequencia_.setDataAdmicao(new SimpleDateFormat("dd/MM/yyyy").format(funcionario.getDataContratação()));
                    }

                    relatorioResumoFrequencia_.setHoraPrevista(c.getHorasASeremTrabalhadasTotal());
                    relatorioResumoFrequencia_.setHoraTrabalhada(c.getHorasTotal());
                    relatorioResumoFrequencia_.setSaldo(c.getSaldo2());
                    //relatorioResumoFrequencia_.setSaldo(c.getSaldoTotal());           
                    relatorioResumoFrequencia_.setFeriadosTrabalhados(c.getSaldoDiaCriticoStr());
                    relatorioResumoFrequencia_.setQntDiasAddNoturno(c.getQnt_dias_adicional_noturno());
                    relatorioResumoFrequencia_.setTotalQntAtraso16_59(c.getTotalQntAtraso16_59());
                    relatorioResumoFrequencia_.setTotalQntAtrasoMaiorUmaHora(c.getTotalQntAtrasoMaiorUmaHora());
                    relatorioResumoFrequencia_.setDiasAtraso(c.getListagemAtraso());
                    relatorioResumoFrequencia_.setDiasFalta(c.getListagemFalta());
                    List<Date> diaSemEscalaTrabalhados = c.getListaDiasComRegistroPoremForaDaEscala();
                    String listagemDiaSemEscalaTrabalhados = c.getListagemDiasComRegistroPoremForaDaEscala(diaSemEscalaTrabalhados);
                    relatorioResumoFrequencia_.setQntDiasSemEscalasTrabalhados(diaSemEscalaTrabalhados.size());
                    relatorioResumoFrequencia_.setDiasSemEscalasTrabalhados(listagemDiaSemEscalaTrabalhados);

                    List<Ponto> pontoList = getPontosDescartados(c.getDiasList());
                    String listagemPontosDescartados = getListagePontoDescartados(pontoList);
                    relatorioResumoFrequencia_.setQntPontosForaDaFaixa(pontoList.size());
                    relatorioResumoFrequencia_.setDiasPontoForaDaFaixa(listagemPontosDescartados);

                    List<Movimento> movimentoList_ = addMovimentosFuncionarios(c, relatorioResumoFrequencia_);
                    relatorioResumoFrequencia.add(relatorioResumoFrequencia_);
                    movimentoList.addAll(movimentoList_);
                }
            }
            if (relatorioResumoFrequencia.isEmpty()) {
                FacesMessage msgErro = new FacesMessage("Não existem informações a serem analisadas!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco.fecharConexao();
        } else {
            if (departamentoSelecionado.equals("-1")) {
                FacesMessage msgErro = new FacesMessage("Selecione um departamento!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else if (cod_funcionario.equals(-1)) {
                FacesMessage msgErro = new FacesMessage("Selecione um funcionário!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Intervalo de datas inválidas!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
    }

    public void gerarFrequenciaParaFolha(ActionEvent event) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, Exception {
        try {
            // connection to an ACCESS MDB  
            theConn = getConnection();
            PreparedStatement pstmt = null;
            Statement stmt = null;
            ResultSet rs = null;

            Integer vinculo = 1;

            for (Iterator<RelatorioResumoFrequencia> it = relatorioResumoFrequencia.iterator(); it.hasNext();) {
                RelatorioResumoFrequencia relatorioResumoFrequencia_ = it.next();

                String sqlPessoa = "select [Codigo vinculo servidor] as vinculo from [pessoal] where mat_emcs = " + relatorioResumoFrequencia_.getMatricula();

                stmt = theConn.createStatement();
                rs = stmt.executeQuery(sqlPessoa);

                while (rs.next()) {
                    vinculo = rs.getInt("vinculo");
                }

                rs.close();

                
                Banco banco = new Banco();
                List<Timestamp> pontos = banco.consultaChechInOutTotal(relatorioResumoFrequencia_.getUserid(), dataInicio, dataFim);

                for (Iterator<Timestamp> it2 = pontos.iterator(); it2.hasNext();) {
                    Timestamp ponto = it2.next();


                    String sql;
                    sql = "insert into Frequencia([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                            + "[data do registro]) values(?,?,?,?)";
                    pstmt = theConn.prepareStatement(sql);
                    pstmt.setInt(1, 1);//
                    pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                    pstmt.setInt(3, vinculo);//
                    pstmt.setTimestamp(4, ponto);//

                    pstmt.executeUpdate();

                    pstmt.close();

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (theConn != null) {
                    theConn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void gerarFolha(ActionEvent event) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            // connection to an ACCESS MDB  
            theConn = getConnection();
            PreparedStatement pstmt = null;
            Statement stmt = null;
            Statement stmt2 = null;
            ResultSet rs = null;
            ResultSet rs2 = null;

            Statement stmtDelete = null;
            String sqlDelete = "delete from movimentoTEMP";
            stmtDelete = theConn.createStatement();
            stmtDelete.executeUpdate(sqlDelete);





            java.sql.Date data = new java.sql.Date(new java.util.Date().getTime());
            Integer folha = 1;
            Integer grupo = 0;
            String nivel = "";
            Integer vinculo = 1;

            String sqlVinculo = "select [data de referencia] as data, [tipo folha] as folha from [resumo ermpresa] where status = 1";
            stmt = theConn.createStatement();
            rs = stmt.executeQuery(sqlVinculo);

            while (rs.next()) {
                data = rs.getDate("data");
                folha = rs.getInt("folha");
            }
            rs.close();

            for (Iterator<RelatorioResumoFrequencia> it = relatorioResumoFrequencia.iterator(); it.hasNext();) {
                RelatorioResumoFrequencia relatorioResumoFrequencia_ = it.next();


                String sqlPessoa = "select [grupo de pagamento] as grupo, [Código nivel funcional] as nivel, [Codigo vinculo servidor] as vinculo from [pessoal] where mat_emcs = " + relatorioResumoFrequencia_.getMatricula();

                stmt2 = theConn.createStatement();
                rs2 = stmt2.executeQuery(sqlPessoa);

                while (rs2.next()) {
                    grupo = rs2.getInt("grupo");
                    nivel = rs2.getString("nivel");
                    vinculo = rs2.getInt("vinculo");
                }

                rs2.close();



                //Inserindo faltas na verba 551
                if (Integer.valueOf(relatorioResumoFrequencia_.getFaltas()) > 0) {
                    String sql;
                    sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                            + "[data do movimento],[tipo folha],[codigo da verba],"
                            + "[unidade de calculo],[valor da verba],[numero parcelas],"
                            + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                            + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = theConn.prepareStatement(sql);
                    pstmt.setInt(1, 1);//
                    pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                    pstmt.setInt(3, vinculo);//
                    pstmt.setDate(4, data);//
                    pstmt.setInt(5, folha);//
                    pstmt.setInt(6, 551);//
                    pstmt.setString(7, "Dia");//
                    pstmt.setInt(8, 0);//
                    pstmt.setInt(9, 1);//
                    pstmt.setInt(10, 0);//
                    pstmt.setString(11, nivel);//
                    pstmt.setInt(12, 0);//
                    pstmt.setInt(13, 2);//
                    pstmt.setInt(14, 0);//
                    pstmt.setInt(15, Integer.valueOf(relatorioResumoFrequencia_.getFaltas()));//
                    pstmt.setInt(16, grupo);//
                    pstmt.setInt(17, 0);//
                    pstmt.executeUpdate();

                    pstmt.close();
                }

                //Inserindo horas extras 50% na verba 450 e horas extras 100% na verba 452
                String horaExtra100 = "";
                String horaExtra50 = "";
                if (!relatorioResumoFrequencia_.getHoraExtra().isEmpty()) {
                    if (relatorioResumoFrequencia_.getHoraExtra().size() > 2) {
                        if (relatorioResumoFrequencia_.getHoraExtra().get(0).contains("100")) {
                            horaExtra100 = relatorioResumoFrequencia_.getHoraExtra().get(1).replace("m", "").replace("h:", ".");
                            horaExtra50 = relatorioResumoFrequencia_.getHoraExtra().get(3).replace("m", "").replace("h:", ".");
                        } else {
                            horaExtra100 = relatorioResumoFrequencia_.getHoraExtra().get(3).replace("m", "").replace("h:", ".");
                            horaExtra50 = relatorioResumoFrequencia_.getHoraExtra().get(1).replace("m", "").replace("h:", ".");
                        }
                    } else {
                        if (relatorioResumoFrequencia_.getHoraExtra().get(0).contains("100")) {
                            horaExtra100 = relatorioResumoFrequencia_.getHoraExtra().get(1).replace("m", "").replace("h:", ".");
                            horaExtra50 = "0";
                        } else {
                            horaExtra100 = "0";
                            horaExtra50 = relatorioResumoFrequencia_.getHoraExtra().get(1).replace("m", "").replace("h:", ".");
                        }
                    }

                    if (Double.valueOf(horaExtra100) > 0) {
                        String sql;
                        sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                                + "[data do movimento],[tipo folha],[codigo da verba],"
                                + "[unidade de calculo],[valor da verba],[numero parcelas],"
                                + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                                + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        pstmt = theConn.prepareStatement(sql);
                        pstmt.setInt(1, 1);//
                        pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                        pstmt.setInt(3, vinculo);//
                        pstmt.setDate(4, data);//
                        pstmt.setInt(5, folha);//
                        pstmt.setInt(6, 452);//
                        pstmt.setString(7, "Hora");//
                        pstmt.setInt(8, 0);//
                        pstmt.setInt(9, 1);//
                        pstmt.setInt(10, 0);//
                        pstmt.setString(11, nivel);//
                        pstmt.setDouble(12, 0);//
                        pstmt.setInt(13, 1);//
                        pstmt.setDouble(14, Double.valueOf(horaExtra100));//
                        pstmt.setInt(15, 0);//
                        pstmt.setInt(16, grupo);//
                        pstmt.setInt(17, 0);//
                        pstmt.executeUpdate();

                        pstmt.close();
                    }

                    if (Double.valueOf(horaExtra50) > 0) {
                        String sql;
                        sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                                + "[data do movimento],[tipo folha],[codigo da verba],"
                                + "[unidade de calculo],[valor da verba],[numero parcelas],"
                                + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                                + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        pstmt = theConn.prepareStatement(sql);
                        pstmt.setInt(1, 1);//
                        pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                        pstmt.setInt(3, vinculo);//
                        pstmt.setDate(4, data);//
                        pstmt.setInt(5, folha);//
                        pstmt.setInt(6, 450);//
                        pstmt.setString(7, "Hora");//
                        pstmt.setInt(8, 0);//
                        pstmt.setInt(9, 1);//
                        pstmt.setInt(10, 0);//
                        pstmt.setString(11, nivel);//
                        pstmt.setDouble(12, 0);//
                        pstmt.setInt(13, 1);//
                        pstmt.setDouble(14, Double.valueOf(horaExtra50));//
                        pstmt.setInt(15, 0);//
                        pstmt.setInt(16, grupo);//
                        pstmt.setInt(17, 0);//
                        pstmt.executeUpdate();

                        pstmt.close();
                    }
                }

                //inserindo feriado crítico na verba 63 / 43
                String feriadoCritico = relatorioResumoFrequencia_.getFeriadosTrabalhados().replace("m", "").replace("h:", ".");
                if (Double.valueOf(feriadoCritico) > 0) {
                    String sql;
                    sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                            + "[data do movimento],[tipo folha],[codigo da verba],"
                            + "[unidade de calculo],[valor da verba],[numero parcelas],"
                            + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                            + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = theConn.prepareStatement(sql);
                    pstmt.setInt(1, 1);//
                    pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                    pstmt.setInt(3, vinculo);//
                    pstmt.setDate(4, data);//
                    pstmt.setInt(5, folha);//
                    if (relatorioResumoFrequencia_.getCargo().toUpperCase().equals("MÉDICO")) {
                        pstmt.setInt(6, 43);//
                    } else {
                        pstmt.setInt(6, 63);
                    }
                    pstmt.setString(7, "Hora");//
                    pstmt.setInt(8, 0);//
                    pstmt.setInt(9, 1);//
                    pstmt.setInt(10, 0);//
                    pstmt.setString(11, nivel);//
                    pstmt.setDouble(12, 0);//
                    pstmt.setInt(13, 1);//
                    pstmt.setDouble(14, Double.valueOf(feriadoCritico));//
                    pstmt.setInt(15, 0);//
                    pstmt.setInt(16, grupo);//
                    pstmt.setInt(17, 0);//
                    pstmt.executeUpdate();

                    pstmt.close();
                }

                //inserindo adicional noturno na verba 36
                String adicionalNoturno = relatorioResumoFrequencia_.getTotalHorasReceberAddNoturno().replace("m", "").replace("h:", ".");
                if (Double.valueOf(adicionalNoturno) > 0) {
                    String sql;
                    sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                            + "[data do movimento],[tipo folha],[codigo da verba],"
                            + "[unidade de calculo],[valor da verba],[numero parcelas],"
                            + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                            + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = theConn.prepareStatement(sql);
                    pstmt.setInt(1, 1);//
                    pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                    pstmt.setInt(3, vinculo);//
                    pstmt.setDate(4, data);//
                    pstmt.setInt(5, folha);//
                    pstmt.setInt(6, 36);//
                    pstmt.setString(7, "Hora");//
                    pstmt.setInt(8, 0);//
                    pstmt.setInt(9, 1);//
                    pstmt.setInt(10, 0);//
                    pstmt.setString(11, nivel);//
                    pstmt.setDouble(12, 0);//
                    pstmt.setInt(13, 1);//
                    pstmt.setDouble(14, Double.valueOf(adicionalNoturno));//
                    pstmt.setInt(15, 0);//
                    pstmt.setInt(16, grupo);//
                    pstmt.setInt(17, 0);//
                    pstmt.executeUpdate();

                    pstmt.close();
                }

                //inserindo hora extra sobre adicional noturno na verba 451
                String horaExtraNoturna = relatorioResumoFrequencia_.getQntDiasAddNoturno().toString();
                if (Double.valueOf(horaExtraNoturna) > 0) {
                    String sql;
                    sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                            + "[data do movimento],[tipo folha],[codigo da verba],"
                            + "[unidade de calculo],[valor da verba],[numero parcelas],"
                            + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                            + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = theConn.prepareStatement(sql);
                    pstmt.setInt(1, 1);//
                    pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                    pstmt.setInt(3, vinculo);//
                    pstmt.setDate(4, data);//
                    pstmt.setInt(5, folha);//
                    pstmt.setInt(6, 451);//
                    pstmt.setString(7, "Hora");//
                    pstmt.setInt(8, 0);//
                    pstmt.setInt(9, 1);//
                    pstmt.setInt(10, 0);//
                    pstmt.setString(11, nivel);//
                    pstmt.setDouble(12, 0);//
                    pstmt.setInt(13, 1);//
                    pstmt.setDouble(14, Double.valueOf(horaExtraNoturna));//
                    pstmt.setInt(15, 0);//
                    pstmt.setInt(16, grupo);//
                    pstmt.setInt(17, 0);//
                    pstmt.executeUpdate();

                    pstmt.close();
                }

                //inserindo gratificacao na verba 41
                if (relatorioResumoFrequencia_.getCargo().toUpperCase().equals("MÉDICO")) {
                    if (!relatorioResumoFrequencia_.getGratificacao().equals(" Sem gratificações")) {
                        String[] gratificacaoSplit = relatorioResumoFrequencia_.getGratificacao().split(": ");
                        String gratificacao = gratificacaoSplit[1].replace("m", "").replace("h:", ".").replace(" ", "");
                        if (Double.valueOf(gratificacao) > 0) {
                            String sql;
                            sql = "insert into MovimentoTemp([codigo da empresa],[matricula],[Codigo vinculo servidor],"
                                    + "[data do movimento],[tipo folha],[codigo da verba],"
                                    + "[unidade de calculo],[valor da verba],[numero parcelas],"
                                    + "[parcelas pagas],[codigo nivel funcional],[fator],[tipo da verba],"
                                    + "[numero de horas],[numero de dias],[grupo de pagamento],[origem]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            pstmt = theConn.prepareStatement(sql);
                            pstmt.setInt(1, 1);//
                            pstmt.setString(2, relatorioResumoFrequencia_.getCpf().replace(".", "").replace("-", ""));//
                            pstmt.setInt(3, vinculo);//
                            pstmt.setDate(4, data);//
                            pstmt.setInt(5, folha);//
                            pstmt.setInt(6, 41);//
                            pstmt.setString(7, "Hora");//
                            pstmt.setInt(8, 0);//
                            pstmt.setInt(9, 1);//
                            pstmt.setInt(10, 0);//
                            pstmt.setString(11, nivel);//
                            pstmt.setDouble(12, 0);//
                            pstmt.setInt(13, 1);//
                            pstmt.setDouble(14, Double.valueOf(gratificacao));//
                            pstmt.setInt(15, 0);//
                            pstmt.setInt(16, grupo);//
                            pstmt.setInt(17, 0);//
                            pstmt.executeUpdate();

                            pstmt.close();
                        }
                    }
                }
            }
            System.out.println("Exportado!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (theConn != null) {
                    theConn.close();
                }
            } catch (Exception e) {
            }
        }


    }

    public static Connection getConnection() throws Exception {
        Driver d = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        Connection c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + connWay);
        return c;
        /* 
        To use an already defined ODBC Datasource :     
        
        String URL = "jdbc:odbc:myDSN"; 
        Connection c = DriverManager.getConnection(URL, "user", "pwd");  
        
         */
    }

    public void gerarMovimento(ActionEvent event) {
        String inicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
        String fimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
        String funcStr = Metodos.buscaRotulo(cod_funcionario.toString(), funcionarioList);
        Metodos.setLogInfo("Gerar Movimento - Funcionário: \"" + funcStr + "\" de " + inicioStr + " a " + fimStr);
        gerarArquivoMovimento(movimentoList);
        try {
            popUpMovimento();
        } catch (IOException ex) {
            System.out.println("RelatorioMensal: gerarMovimento 1: "+ex);
            //Logger.getLogger(RelatorioResumoFrequenciaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("RelatorioMensal: gerarMovimento 2: "+ex);
            //Logger.getLogger(RelatorioResumoFrequenciaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportarExcel(ActionEvent event) {
        List<Linha> linhasList = new ArrayList<Linha>();

        List<String> cabecalho = new ArrayList<String>();
        addCabecalho(cabecalho);
        for (Iterator<RelatorioResumoFrequencia> it = relatorioResumoFrequencia.iterator(); it.hasNext();) {
            RelatorioResumoFrequencia relatorioResumoFrequencia_ = it.next();
            List<String> c1List = new ArrayList<String>();

            Linha c1 = new Linha();
            addLinha(c1List, relatorioResumoFrequencia_);
            c1.setConteudoCelulasList(c1List);
            linhasList.add(c1);
        }

        Excel.gerarPlanilhaDownload("ResumoMensal.xls", cabecalho, linhasList);
        String inicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
        String fimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
        String funcStr = Metodos.buscaRotulo(cod_funcionario.toString(), funcionarioList);
        Metodos.setLogInfo("Exportar para excel - Funcionário: \"" + funcStr + "\" de " + inicioStr + " a " + fimStr);
    }

    private void addCabecalho(List<String> cabecalho) {
        if (escolhaExcel.getNome()) {
            cabecalho.add("NOME");
        }
        if (escolhaExcel.getCargo()) {
            cabecalho.add("CARGO");
        }
        if (escolhaExcel.getQntDiasAddNoturno()) {
            cabecalho.add("ADICIONAL NOTURNO (DIAS)");
        }
        if (escolhaExcel.getTotalHorasReceberAddNoturno()) {
            cabecalho.add("ADICIONAL NOTURNO (HORAS)");
        }
        if (escolhaExcel.getFaltas()) {
            cabecalho.add("FALTA");
        }
        if (escolhaExcel.getHoraExtra()) {
            for (Iterator<String> it = cabecalhoHoraExtraList.iterator(); it.hasNext();) {
                String string = it.next();
                cabecalho.add(string);
            }
        }
        if (escolhaExcel.getQntDiasSemEscalasTrabalhados()) {
            cabecalho.add("DIAS TRABALHADOS SEM ESCALA");
        }
        if (escolhaExcel.getDiasSemEscalasTrabalhados()) {
            cabecalho.add("LISTA DIAS TRABALHADOS SEM ESCALA");
        }
        if (escolhaExcel.getGratificacao()) {
            cabecalho.add("GRATIFICAÇÃO");
        }
        if (escolhaExcel.getFeriadosTrabalhados()) {
            cabecalho.add("FERIADO CRÍTICO");
        }
        if (escolhaExcel.getQntDSR()) {
            cabecalho.add("QUANTIDADE DE DSR");
        }
        if (escolhaExcel.getHoraPrevista()) {
            cabecalho.add("HORA PREVISTA");
        }
        if (escolhaExcel.getHoraTrabalhada()) {
            cabecalho.add("HORA TRABALHADA");
        }
        if (escolhaExcel.getAtrasos()) {
            cabecalho.add("DESCONTO");
        }
        if (escolhaExcel.getDiasFalta()) {
            cabecalho.add("LISTA DE DIAS FALTA");
        }
        if (escolhaExcel.getSaldo()) {
            cabecalho.add("SALDO");
        }
        if (escolhaExcel.getRegime()) {
            cabecalho.add("REGIME");
        }
        if (escolhaExcel.getDataAdmicao()) {
            cabecalho.add("DATA ADMISSÃO");
        }
        if (escolhaExcel.getTotalQntAtraso16_59()) {
            cabecalho.add("ATRASO < 1 HORA");
        }
        if (escolhaExcel.getTotalQntAtrasoMaiorUmaHora()) {
            cabecalho.add("ATRASO > 1 HORA");
        }
        if (escolhaExcel.getDiasAtraso()) {
            cabecalho.add("LISTA DE DIAS ATRASO");
        }
        if (escolhaExcel.getQntPontosForaDaFaixa()) {
            cabecalho.add("PONTOS FORA DA ESCALA");
        }

        if (escolhaExcel.getDiasPontoForaDaFaixa()) {
            cabecalho.add("LISTA PONTOS FORA DA ESCALA");
        }
        if (escolhaExcel.getCpf()) {
            cabecalho.add("CPF");
        }
        if (escolhaExcel.getMatricula()) {
            cabecalho.add("MATRÍCULA");
        }
        if (escolhaExcel.getDepartamento()) {
            cabecalho.add("DEPARTAMENTO");
        }
    }

    private void addLinha(List<String> c1List, RelatorioResumoFrequencia relatorioResumoFrequencia_) {

        if (escolhaExcel.getNome()) {
            c1List.add(relatorioResumoFrequencia_.getNome());
        }
        if (escolhaExcel.getCargo()) {
            c1List.add(relatorioResumoFrequencia_.getCargo());
        }
        if (escolhaExcel.getQntDiasAddNoturno()) {
            c1List.add(relatorioResumoFrequencia_.getQntDiasAddNoturno().toString());
        }
        if (escolhaExcel.getTotalHorasReceberAddNoturno()) {
            c1List.add(relatorioResumoFrequencia_.getTotalHorasReceberAddNoturno());
        }
        if (escolhaExcel.getFaltas()) {
            c1List.add(relatorioResumoFrequencia_.getFaltas());
        }
        if (escolhaExcel.getHoraExtra()) {
            Map<String, String> nomeHEValorHEMap = getHoraExtraHashMap(relatorioResumoFrequencia_.getHoraExtra());

            for (Iterator<String> it = cabecalhoHoraExtraList.iterator(); it.hasNext();) {
                String cabecalho = it.next();
                String thisCabecalho = nomeHEValorHEMap.get(cabecalho);
                if (thisCabecalho != null) {
                    c1List.add(thisCabecalho);
                } else {
                    c1List.add("");
                }
            }

        }
        if (escolhaExcel.getQntDiasSemEscalasTrabalhados()) {
            c1List.add(relatorioResumoFrequencia_.getQntDiasSemEscalasTrabalhados().toString());
        }
        if (escolhaExcel.getDiasSemEscalasTrabalhados()) {
            c1List.add(relatorioResumoFrequencia_.getDiasSemEscalasTrabalhados());
        }
        if (escolhaExcel.getGratificacao()) {
            c1List.add(relatorioResumoFrequencia_.getGratificacao());
        }
        if (escolhaExcel.getFeriadosTrabalhados()) {
            c1List.add(relatorioResumoFrequencia_.getFeriadosTrabalhados());
        }
        if (escolhaExcel.getQntDSR()) {
            c1List.add(relatorioResumoFrequencia_.getQntDSR().toString());
        }
        if (escolhaExcel.getHoraPrevista()) {
            c1List.add(relatorioResumoFrequencia_.getHoraPrevista());
        }
        if (escolhaExcel.getHoraTrabalhada()) {
            c1List.add(relatorioResumoFrequencia_.getHoraTrabalhada());
        }
        if (escolhaExcel.getAtrasos()) {
            c1List.add(relatorioResumoFrequencia_.getAtrasos());
        }
        if (escolhaExcel.getDiasFalta()) {
            c1List.add(relatorioResumoFrequencia_.getDiasFalta());
        }
        if (escolhaExcel.getSaldo()) {
            c1List.add(relatorioResumoFrequencia_.getSaldo());
        }
        if (escolhaExcel.getRegime()) {
            c1List.add(relatorioResumoFrequencia_.getRegime());
        }
        if (escolhaExcel.getDataAdmicao()) {
            c1List.add(relatorioResumoFrequencia_.getDataAdmicao());
        }
        if (escolhaExcel.getTotalQntAtraso16_59()) {
            c1List.add(relatorioResumoFrequencia_.getTotalQntAtraso16_59().toString());
        }

        if (escolhaExcel.getTotalQntAtrasoMaiorUmaHora()) {
            c1List.add(relatorioResumoFrequencia_.getTotalQntAtrasoMaiorUmaHora().toString());
        }

        if (escolhaExcel.getDiasAtraso()) {
            c1List.add(relatorioResumoFrequencia_.getDiasAtraso());
        }
        if (escolhaExcel.getQntPontosForaDaFaixa()) {
            c1List.add(relatorioResumoFrequencia_.getQntPontosForaDaFaixa().toString());
        }

        if (escolhaExcel.getDiasPontoForaDaFaixa()) {
            c1List.add(relatorioResumoFrequencia_.getDiasPontoForaDaFaixa());
        }
        if (escolhaExcel.getCpf()) {
            c1List.add(relatorioResumoFrequencia_.getCpf());
        }
        if (escolhaExcel.getMatricula()) {
            c1List.add(relatorioResumoFrequencia_.getMatricula());
        }
        if (escolhaExcel.getDepartamento()) {
            c1List.add(relatorioResumoFrequencia_.getDepartamento());
        }
    }

    private Map<String, String> getHoraExtraHashMap(List<String> hEList) {
        Map<String, String> nomeHEValorHEMap = new HashMap<String, String>();
        for (Iterator<String> it = hEList.iterator(); it.hasNext();) {
            String chave = it.next();
            String valor = it.next();
            nomeHEValorHEMap.put(chave, valor);
        }
        return nomeHEValorHEMap;
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

    private Boolean valideData() {
        if ((dataInicio == null || dataFim == null) || (dataInicio.getTime() > dataFim.getTime())) {
            return false;
        } else {
            return true;
        }
    }

    private static Date getHoje() {
        return new Date();
    }

    private void consultaDepartamento() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        String permissao = usuarioBean.getUsuario().getPermissao();
        Banco banco = new Banco();
        departamentosSelecItem = new ArrayList<SelectItem>();
        Integer dept = usuarioBean.getUsuario().getDepartamento();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        departamentosSelecItem = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }

    private List<Integer> verbasList() {
        List<Integer> verbaList = new ArrayList<Integer>();
        Banco banco = new Banco();
        verbaList = banco.getVerbas();
        return verbaList;

    }

    private List<Movimento> addMovimentosFuncionarios(ConsultaFrequenciaComEscalaBean c,
            RelatorioResumoFrequencia relatorioResumoFrequencia_) {

        List<Movimento> movimentoList_ = new ArrayList<Movimento>();

        Movimento movimentoDescontoSaldoSemFalta = descontoHora(c, relatorioResumoFrequencia_);
        if (movimentoDescontoSaldoSemFalta != null) {
            movimentoList_.add(movimentoDescontoSaldoSemFalta);
        }

        Movimento movimentoDescontoFalta = new Movimento();
        movimentoDescontoFalta = descontoFaltas(c, relatorioResumoFrequencia_);
        if (movimentoDescontoFalta != null) {
            movimentoList_.add(movimentoDescontoFalta);
        }

        Movimento movimentoAddNoturno = new Movimento();
        movimentoAddNoturno = addNoturno(c, relatorioResumoFrequencia_);
        if (movimentoAddNoturno != null) {
            movimentoList_.add(movimentoAddNoturno);
        }

        List<Movimento> movimentoHEList = new ArrayList<Movimento>();
        movimentoHEList = addHoraExtra(c, relatorioResumoFrequencia_);
        if (!movimentoHEList.isEmpty()) {
            movimentoList_.addAll(movimentoHEList);
        }

        List<Movimento> movimentoGratificacaoList = new ArrayList<Movimento>();
        movimentoGratificacaoList = addGratificacao(c, relatorioResumoFrequencia_);
        if (!movimentoGratificacaoList.isEmpty()) {
            movimentoList_.addAll(movimentoGratificacaoList);
        }

        List<Movimento> movimentoDiasCriticosList = new ArrayList<Movimento>();
        movimentoDiasCriticosList = addGratificacaoDiaCritico(c, relatorioResumoFrequencia_);
        if (!movimentoDiasCriticosList.isEmpty()) {
            movimentoList_.addAll(movimentoDiasCriticosList);
        }

        List<Movimento> movimentoDSRList = new ArrayList<Movimento>();
        movimentoDSRList = addDSR(c, relatorioResumoFrequencia_);
        if (!movimentoDSRList.isEmpty()) {
            movimentoList_.addAll(movimentoDSRList);
        }

        List<Movimento> movimentoQntHorasMenorUmaHoraList = new ArrayList<Movimento>();
        movimentoQntHorasMenorUmaHoraList = qntHorasMenorUmaHora(c);
        if (!movimentoQntHorasMenorUmaHoraList.isEmpty()) {
            movimentoList_.addAll(movimentoQntHorasMenorUmaHoraList);
        }

        List<Movimento> movimentoQntHorasMaiorUmaHoraList = new ArrayList<Movimento>();
        movimentoQntHorasMaiorUmaHoraList = qntHorasMaiorUmaHora(c);
        if (!movimentoQntHorasMaiorUmaHoraList.isEmpty()) {
            movimentoList_.addAll(movimentoQntHorasMaiorUmaHoraList);
        }


        return movimentoList_;
    }

    private void gerarArquivoMovimento(List<Movimento> movimentoList_) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String path = sc.getRealPath("relatorio/") + "\\";
            File arquivo = new File(path + "movimento.txt");
            // Cria o arquivo se este não existe ainda
            arquivo.delete();
            arquivo.createNewFile();
            FileOutputStream fos = new FileOutputStream(arquivo);
            for (Iterator<Movimento> it = movimentoList_.iterator(); it.hasNext();) {
                Movimento movimento = it.next();
                fos.write(movimento.printLineMovimento().getBytes());
            }
            fos.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    private Movimento descontoHora(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {
        Movimento movimento = new Movimento();
        String saldo = c.getSaldoSemFaltas().startsWith(" -") ? c.getSaldoSemFaltas() : "00h:00m";
        relatorioResumoFrequenciaTotalizador.setDescontoInt(transformaHoraEmMinutos(saldo));
        relatorioResumoFrequencia_.setAtrasos(saldo);
        String valor = formatarSaldoTotal(c.getSaldoSemFaltas());
        if (valor != null) {
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(2).toString(), 4));
            movimento.setValor(valor);
            //         movimento.setCod_aula_tarefa("000");
            //         movimento.setDiv_rh("000000000");
            return movimento;
        } else {
            return null;
        }
    }

    private Movimento descontoFaltas(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {
        Movimento movimento = new Movimento();
        Integer qntFaltas = c.getFaltas();
        relatorioResumoFrequencia_.setFaltas(qntFaltas.toString());
        String valor = formatarDia(c.getFaltas());
        if (!qntFaltas.equals(0)) {
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(7).toString(), 4));
            movimento.setValor(valor);
            //        movimento.setCod_aula_tarefa("000");
            //         movimento.setDiv_rh("000000000");
            return movimento;
        } else {
            return null;
        }
    }

    private Movimento addNoturno(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {
        Movimento movimento = new Movimento();
        relatorioResumoFrequencia_.setTotalHorasReceberAddNoturno(c.getAdicionalNoturnoStr());
        String valor = formatarAdicionalNoturno(c.getAdicionalNoturnoStr());
        if (valor != null) {
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(1).toString(), 4));
            movimento.setValor(valor);
            //        movimento.setCod_aula_tarefa("000");
            //         movimento.setDiv_rh("000000000");
            return movimento;
        } else {
            return null;
        }
    }

    private List<Movimento> addHoraExtra(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {
        List<Movimento> movimentoList_ = new ArrayList<Movimento>();
        //relatorioResumoFrequencia_.setHoraExtra();
        List<TipoHoraExtra> tipoHoraExtra = c.getTipoHoraExtra();
        Map<String, Integer> tipo_valorHorasExtra = c.getTipo_valorHorasExtra();
        for (Iterator<TipoHoraExtra> it = tipoHoraExtra.iterator(); it.hasNext();) {
            TipoHoraExtra tipoHoraExtra1 = it.next();
            Movimento movimento = new Movimento();
            Integer valor = tipo_valorHorasExtra.get(tipoHoraExtra1.getNome());
            if (!valor.equals(0)) {
                movimento.setValor(formatarHoraExtra(valor));
                //             movimento.setCod_aula_tarefa("000");
                //             movimento.setDiv_rh("000000000");
                movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
                movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
                movimento.setCod_evento(autoComplete(tipoHoraExtra1.getVerba(), 4));
                movimentoList_.add(movimento);
            }
        }
        return movimentoList_;
    }

    private List<Movimento> addGratificacao(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {

        List<Movimento> movimentoList_ = new ArrayList<Movimento>();
        List<Gratificacao> gratificacaoList = c.getGratificacaoList();
        Map<Integer, Integer> somaGratificacao = c.getSomaGratificacao();
        relatorioResumoFrequencia_.setGratificacao(c.getGratificacaoStr());
        for (Iterator<Gratificacao> it = gratificacaoList.iterator(); it.hasNext();) {
            Gratificacao gratificacao = it.next();
            Movimento movimento = new Movimento();
            Integer valor = somaGratificacao.get(gratificacao.getCod_gratificacao());
            if (valor != null && !valor.equals(0)) {
                movimento.setValor(formatarGratificacao(valor));
                //              movimento.setCod_aula_tarefa("000");
                //               movimento.setDiv_rh("000000000");
                movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
                movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
                movimento.setCod_evento(autoComplete(gratificacao.getVerba(), 4));
                movimentoList_.add(movimento);
            }
        }
        return movimentoList_;
    }

    private List<Movimento> addGratificacaoDiaCritico(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {
        List<Movimento> movimentoList_ = new ArrayList<Movimento>();

        Integer valor = c.getSaldoDiaCriticoInt();
        relatorioResumoFrequencia_.setGratificacaoDiaCritico(c.getSaldoDiaCriticoStr());
        Movimento movimento = new Movimento();

        if (!valor.equals(0)) {
            movimento.setValor(formatarGratificacao(valor));
            //          movimento.setCod_aula_tarefa("000");
            //           movimento.setDiv_rh("000000000");
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(5).toString(), 4));
            movimentoList_.add(movimento);
        }

        return movimentoList_;
    }

    private List<Movimento> addDSR(ConsultaFrequenciaComEscalaBean c, RelatorioResumoFrequencia relatorioResumoFrequencia_) {
        List<Movimento> movimentoList_ = new ArrayList<Movimento>();

        relatorioResumoFrequencia_.setQntDSR(c.getQntDSR());
        Integer qntDSR = c.getQntDSR();

        Movimento movimento = new Movimento();
        Integer valor = qntDSR;
        if (!valor.equals(0)) {
            movimento.setValor(formatarDia(valor));
            //      movimento.setCod_aula_tarefa("000");
            //      movimento.setDiv_rh("000000000");
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(6).toString(), 4));
            movimentoList_.add(movimento);
        }

        return movimentoList_;
    }

    private List<Movimento> qntHorasMenorUmaHora(ConsultaFrequenciaComEscalaBean c) {
        List<Movimento> movimentoList_ = new ArrayList<Movimento>();

        Integer qntHoraMenor = c.getTotalQntAtraso16_59();

        Movimento movimento = new Movimento();

        if (!qntHoraMenor.equals(0)) {
            movimento.setValor(formatarDia(qntHoraMenor));
            //          movimento.setCod_aula_tarefa("000");
            //          movimento.setDiv_rh("000000000");
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(3).toString(), 4));
            movimentoList_.add(movimento);
        }

        return movimentoList_;
    }

    private List<Movimento> qntHorasMaiorUmaHora(ConsultaFrequenciaComEscalaBean c) {
        List<Movimento> movimentoList_ = new ArrayList<Movimento>();

        Integer qntHoraMenor = c.getTotalQntAtrasoMaiorUmaHora();

        Movimento movimento = new Movimento();

        if (!qntHoraMenor.equals(0)) {
            movimento.setValor(formatarDia(qntHoraMenor));
            //         movimento.setCod_aula_tarefa("000");
            //         movimento.setDiv_rh("000000000");
            movimento.setCod_empresa(autoComplete(verbasList.get(0).toString(), 4));
            movimento.setCod_funcionario(formatarFuncionario(c.getCod_funcionario().toString()));
            movimento.setCod_evento(autoComplete(verbasList.get(4).toString(), 4));
            movimentoList_.add(movimento);
        }

        return movimentoList_;
    }

    public Integer transformaHoraEmMinutos(String ponto) {

        String[] pontoArray = ponto.split(":");

        String hora = pontoArray[0].replace(":", "").replace("h", "").replace("m", "");
        String minutos = pontoArray[1].replace(":", "").replace("h", "").replace("m", "");

        int i = 1;

        if (hora.startsWith(" -")) {
            i = -1;
            hora = hora.replace(" -", "").replace(" ", "");
        }

        Integer horaInt = Integer.parseInt(hora) * 60;
        Integer minutosInt = (Integer.parseInt(minutos));

        Integer saida = (horaInt + minutosInt) * i;

        return saida;
    }

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        relatorioResumoFrequenciaTotalizador = new RelatorioResumoFrequenciaTotalizador();
        iniciarOpcoesFiltro();
    }

    private void iniciarOpcoesFiltro() {
        gestorFiltroFuncionarioList = getOpcaoFiltroGestor();
        Banco b = new Banco();
        regimeOpcaoFiltroFuncionarioList = b.getRegimeSelectItem();
        cod_funcionarioRegimeHashMap = b.getcod_funcionarioRegime();

    }

    private List<SelectItem> getOpcaoFiltroGestor() {
        List<SelectItem> tipoFiltroGestorList = new ArrayList<SelectItem>();
        tipoFiltroGestorList.add(new SelectItem(-1, "TODOS"));
        tipoFiltroGestorList.add(new SelectItem(0, "GESTORES SUBORDINADOS AO DEPARTAMENTO"));
        tipoFiltroGestorList.add(new SelectItem(1, "GESTORES SUBORDINADOS DIRETAMENTE AO DEPARTAMENTO"));

        return tipoFiltroGestorList;
    }

    private List<SelectItem> filtrarFuncionario() {
        Banco b = new Banco();
        cod_funcionarioGestorHashMap = b.getcod_funcionarioSubordinacaoDepartamento(Integer.parseInt(departamentoSelecionado));
        List<SelectItem> funcionarioList_ = new ArrayList<SelectItem>();

        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario_ = it.next();
            if (!funcionario_.getValue().toString().equals("-1") && !funcionario_.getValue().toString().equals("0")) {
                Boolean criterioRegime = isFuncionarioDentroCriterioRegime(funcionario_);
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario_);

                if (criterioGestor && criterioRegime) {
                    funcionarioList_.add(funcionario_);
                }
            } else {
                funcionarioList_.add(funcionario_);
            }
        }
        return funcionarioList_;
    }

    private Boolean isFuncionarioDentroCriterioRegime(SelectItem funcionarioSelectItem) {
        Boolean criterioRegime = false;
        Integer regime = cod_funcionarioRegimeHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        criterioRegime = (regimeSelecionadoOpcaoFiltroFuncionario == -1) || regime.equals(regimeSelecionadoOpcaoFiltroFuncionario);

        return criterioRegime;
    }

    private Boolean isFuncionarioDentroCriterioGestor(SelectItem funcionarioSelectItem) {
        Integer tipoGestor = cod_funcionarioGestorHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        Boolean criterioGestor = false;
        if (tipoGestorSelecionadoOpcaoFiltroFuncionario == -1) {
            criterioGestor = true;
        } else if (tipoGestorSelecionadoOpcaoFiltroFuncionario == 0) {
            if (tipoGestor >= 0) {
                criterioGestor = true;
            }
        } else {
            if (tipoGestor == 1) {
                criterioGestor = true;
            }
        }
        return criterioGestor;
    }

    private List<Integer> getFuncionarios() {
        List<Integer> funcionarioList_ = new ArrayList<Integer>();
        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario = it.next();
            Integer matricula = Integer.parseInt(funcionario.getValue().toString());
            if (!(matricula.equals(-1) || matricula.equals(0))) {
                funcionarioList_.add(matricula);
            }
        }
        return funcionarioList_;
    }

    private List<Ponto> getPontosDescartados(List<DiaComEscala> diaComEscalaList) {

        List<Ponto> pontoList = new ArrayList<Ponto>();
        for (Iterator<DiaComEscala> it = diaComEscalaList.iterator(); it.hasNext();) {
            DiaComEscala diaComEscala = it.next();
            List<SituacaoPonto> situacaoPontoList = diaComEscala.getSituacaoPonto();
            for (Iterator<SituacaoPonto> it1 = situacaoPontoList.iterator(); it1.hasNext();) {
                SituacaoPonto situacaoPonto = it1.next();
                if (situacaoPonto.getSituacao().equals("Fora da faixa")) {
                    pontoList.add(situacaoPonto.getHoraPontoObj());
                }
            }
        }
        return pontoList;
    }

    private String getListagePontoDescartados(List<Ponto> pontoList) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String saida = "";
        for (Iterator<Ponto> it = pontoList.iterator(); it.hasNext();) {
            Ponto ponto = it.next();
            if (it.hasNext()) {
                saida += sdfHora.format(ponto.getPonto()) + "\n";
            } else {
                saida += sdfHora.format(ponto.getPonto());
            }
        }
        return saida;
    }

    private String formatarSaldoTotal(String saldo) {
        if (!saldo.startsWith(" -")) {
            return null;
        } else {
            saldo = saldo.replace("-", "").replace("h", "").replace("m", "").replace(" ", "").replace(":", "");
            saldo = autoComplete(saldo, 12);
            return saldo;
        }
    }

    private String formatarAdicionalNoturno(String saldo) {
        if (saldo.equals("00h:00m")) {
            return null;
        } else {
            saldo = saldo.replace("-", "").replace("h", "").replace("m", "").replace(" ", "").replace(":", "");
            saldo = autoComplete(saldo, 12);
            return saldo;
        }
    }

    private String formatarHoraExtra(Integer saldoMinutos) {
        String saida = transformaMinutosEmHora(saldoMinutos);
        saida = autoComplete(saida, 12);
        return saida;
    }

    private String formatarGratificacao(Integer saldoSegundos) {
        String saida = transformaSegundosEmHora(saldoSegundos);
        saida = autoComplete(saida, 12);
        return saida;
    }

    private String formatarDia(Integer qntDSR) {
        String saida = qntDSR.toString();
        saida = autoComplete(saida, 12);
        return saida;
    }

    private String transformaMinutosEmHora(Integer tempoemminutos_) {

        Long tempoemminutos = Long.parseLong(tempoemminutos_.toString());

        Long horas = tempoemminutos / 60;
        Long minutos = tempoemminutos % 60;

        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }

        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + minutosSrt;
    }

    private String transformaSegundosEmHora(Integer time) {

        Integer saida = time;
        Integer horas = Math.abs(saida / 3600);
        Integer minutos = Math.abs((saida / 60) % 60);
        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }

        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + minutosSrt;
    }

    public static void popUpMovimento() throws IOException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("relatorio/") + "\\";
        File file = new File(path + "movimento.txt");
        byte[] teste = getBytesFromFile(file);
        download(teste, "movimento.txt");
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static HttpServletResponse getServletResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static void download(byte[] arquivo, String nome) throws Exception {
        HttpServletResponse response = getServletResponse();
        response.setContentType(null);
        response.setContentLength(arquivo.length);
        response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + nome + "\"");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(arquivo, 0, arquivo.length);
        outputStream.flush();
        outputStream.close();
        getFacesContext().responseComplete();
    }

    private String formatarFuncionario(String funcionario) {
        funcionario = autoComplete(funcionario, 10);
        return funcionario;
    }

    private String autoComplete(String string, Integer tamanho) {
        Integer tamanhoString = string.length();
        for (int i = tamanhoString; i < tamanho; i++) {
            string = "0" + string;
        }
        return string;
    }

    private List<String> getTiposHoraExtra(String entrada) {
        List<String> stringList = new ArrayList<String>();
        String[] tiposHoraExtra = entrada.split(", ");
        if (tiposHoraExtra.length > 1) {
            for (int i = 0; i < tiposHoraExtra.length; i++) {
                String[] heArray = tiposHoraExtra[i].split(": ");
                String nomeHE = heArray[0].replace("[", "").replace("]", "");
                String valorHE = heArray[1].replace("[", "").replace("]", "");
                stringList.add(nomeHE);
                stringList.add(valorHE);
            }
        }
        return stringList;
    }

    private List<String> getCabecalhoHoraExtra(String entrada) {
        List<String> stringList = new ArrayList<String>();
        String[] tiposHoraExtra = entrada.split(", ");
        if (tiposHoraExtra.length > 1) {
            for (int i = 0; i < tiposHoraExtra.length; i++) {
                String[] heArray = tiposHoraExtra[i].split(": ");
                String nomeHE = heArray[0].replace("[", "").replace("]", "");
                //  String valorHE = heArray[1].replace("[", "").replace("]", "");
                stringList.add(nomeHE);
                //     stringList.add(valorHE);
            }
        }
        return stringList;
    }

    private void addCabecalhoHoraExtra(List<String> cabecalhoList, List<String> cabecalhoAdicionarList) {
        Boolean alt = true;
        for (Iterator<String> it = cabecalhoAdicionarList.iterator(); it.hasNext();) {
            String string = it.next();
            if (alt) {
                if (!cabecalhoList.contains(string)) {
                    cabecalhoList.add(string);
                }
                alt = false;
            } else {
                alt = true;
            }
        }
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

    public String getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(String departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public List<SelectItem> getDepartamentosSelecItem() {
        return departamentosSelecItem;
    }

    public void setDepartamentosSelecItem(List<SelectItem> departamentosSelecItem) {
        this.departamentosSelecItem = departamentosSelecItem;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Integer getEscalaSelecionada() {
        return escalaSelecionada;
    }

    public void setEscalaSelecionada(Integer escalaSelecionada) {
        this.escalaSelecionada = escalaSelecionada;
    }

    public List<SelectItem> getEscalasSelecItem() {
        return escalasSelecItem;
    }

    public void setEscalasSelecItem(List<SelectItem> escalasSelecItem) {
        this.escalasSelecItem = escalasSelecItem;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public List<RelatorioResumoFrequencia> getRelatorioResumoFrequencia() {
        return relatorioResumoFrequencia;
    }

    public void setRelatorioResumoFrequencia(List<RelatorioResumoFrequencia> relatorioResumoFrequencia) {
        this.relatorioResumoFrequencia = relatorioResumoFrequencia;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public RelatorioResumoFrequenciaEscolhaExcel getEscolhaExcel() {
        return escolhaExcel;
    }

    public void setEscolhaExcel(RelatorioResumoFrequenciaEscolhaExcel escolhaExcel) {
        this.escolhaExcel = escolhaExcel;
    }

    public Boolean getIsAdministradorVisivel() {
        return isAdministradorVisivel;
    }

    public void setIsAdministradorVisivel(Boolean isAdministradorVisivel) {
        this.isAdministradorVisivel = isAdministradorVisivel;
    }

    public List<SelectItem> getRegimeOpcaoFiltroFuncionarioList() {
        return regimeOpcaoFiltroFuncionarioList;
    }

    public void setRegimeOpcaoFiltroFuncionarioList(List<SelectItem> regimeOpcaoFiltroFuncionarioList) {
        this.regimeOpcaoFiltroFuncionarioList = regimeOpcaoFiltroFuncionarioList;
    }

    public Integer getRegimeSelecionadoOpcaoFiltroFuncionario() {
        return regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public void setRegimeSelecionadoOpcaoFiltroFuncionario(Integer regimeSelecionadoOpcaoFiltroFuncionario) {
        this.regimeSelecionadoOpcaoFiltroFuncionario = regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public RelatorioResumoFrequenciaTotalizador getRelatorioResumoFrequenciaTotalizador() {
        return relatorioResumoFrequenciaTotalizador;
    }

    public void setRelatorioResumoFrequenciaTotalizador(RelatorioResumoFrequenciaTotalizador relatorioResumoFrequenciaTotalizador) {
        this.relatorioResumoFrequenciaTotalizador = relatorioResumoFrequenciaTotalizador;
    }

    public List<String> getCabecalhoHoraExtraList() {
        return cabecalhoHoraExtraList;
    }

    public void setCabecalhoHoraExtraList(List<String> cabecalhoHoraExtraList) {
        this.cabecalhoHoraExtraList = cabecalhoHoraExtraList;
    }

    public HashMap<Integer, Integer> getCod_funcionarioGestorHashMap() {
        return cod_funcionarioGestorHashMap;
    }

    public void setCod_funcionarioGestorHashMap(HashMap<Integer, Integer> cod_funcionarioGestorHashMap) {
        this.cod_funcionarioGestorHashMap = cod_funcionarioGestorHashMap;
    }

    public HashMap<Integer, Integer> getCod_funcionarioRegimeHashMap() {
        return cod_funcionarioRegimeHashMap;
    }

    public void setCod_funcionarioRegimeHashMap(HashMap<Integer, Integer> cod_funcionarioRegimeHashMap) {
        this.cod_funcionarioRegimeHashMap = cod_funcionarioRegimeHashMap;
    }

    public List<SelectItem> getGestorFiltroFuncionarioList() {
        return gestorFiltroFuncionarioList;
    }

    public void setGestorFiltroFuncionarioList(List<SelectItem> gestorFiltroFuncionarioList) {
        this.gestorFiltroFuncionarioList = gestorFiltroFuncionarioList;
    }

    public Integer getTipoGestorSelecionadoOpcaoFiltroFuncionario() {
        return tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public void setTipoGestorSelecionadoOpcaoFiltroFuncionario(Integer tipoGestorSelecionadoOpcaoFiltroFuncionario) {
        this.tipoGestorSelecionadoOpcaoFiltroFuncionario = tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    /**
     * @return the connWay
     */
    public String getConnWay() {
        return connWay;
    }

    /**
     * @param connWay the connWay to set
     */
    public void setConnWay(String connWay) {
        this.connWay = connWay;
    }
}
