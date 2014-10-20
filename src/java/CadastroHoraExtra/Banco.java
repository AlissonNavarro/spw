/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHoraExtra;

import CadastroHoraExtra.TipoHoraExtra;
import Metodos.Metodos;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class Banco implements Serializable {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
        }
    }

    public List<RegimeHoraExtra> consultaRegimeHoraExtra() {

        List<RegimeHoraExtra> regimeHoraExtraList = new ArrayList<RegimeHoraExtra>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from Regime_HoraExtra ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer cod = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                Boolean feriadoCritico = rs.getBoolean("feriadoCritico");
                RegimeHoraExtra regimeHoraExtra = new RegimeHoraExtra(cod, nome, feriadoCritico);
                regimeHoraExtraList.add(regimeHoraExtra);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return regimeHoraExtraList;
    }

    public List<Justificativa> consultaJustificativasHoraExtra(Integer cod_regime) {

        List<Justificativa> justificativaList = new ArrayList<Justificativa>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from categoriaHoraExtra "
                    + "where cod_regime = " + cod_regime
                    + " ORDER BY descricao";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            justificativaList = new ArrayList<Justificativa>();
            while (rs.next()) {
                Integer cod = rs.getInt("cod");
                String descricao = rs.getString("descricao");
                justificativaList.add(new Justificativa(cod, descricao));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return justificativaList;
    }

    public List<Gratificacao> consultaGratificacao(Integer cod_regime) {

        List<Gratificacao> gratificacaoList = new ArrayList<Gratificacao>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from gratificacao "
                    + "where cod_regime = " + cod_regime
                    + " ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            gratificacaoList = new ArrayList<Gratificacao>();
            while (rs.next()) {
                Integer cod_gratificacao = rs.getInt("cod_gratificacao");
                String nome = rs.getString("nome");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                gratificacaoList.add(new Gratificacao(cod_gratificacao, cod_regime, nome, valor, verba));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return gratificacaoList;
    }

    public Boolean inserirNovoRegime(String nome_Regime) {

        Boolean flag = true;
        PreparedStatement pstmt = null;
        if (nome_Regime.equals("")) {
            return false;
        }

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Regime_HoraExtra "
                    + " where nome = '" + nome_Regime + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {
                String query = "insert into Regime_HoraExtra(nome, feriadoCritico) values(?, ?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, nome_Regime);
                pstmt.setBoolean(2, true);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    if (c != null) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public Boolean editRegimeHoraExtra(Integer cod_regime, String editNomeRegime) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Regime_HoraExtra "
                    + " where nome = '" + editNomeRegime + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {
                String query = "update Regime_HoraExtra set nome = ?"
                        + " where cod_regime = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, editNomeRegime);
                pstmt.setInt(2, cod_regime);

                pstmt.executeUpdate();
                 pstmt.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } 
        return flag;
    }

    public String consultaEditRegimeHoraExtra(Integer cod_regime) {

        String nome = null;
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select * from Regime_HoraExtra "
                    + " where cod_regime = " + cod_regime;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return nome;
    }

    public Boolean deleteRegimeHoraExtra(Integer cod_regime) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            String query = "delete from Regime_HoraExtra"
                    + " where cod_regime = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_regime);
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public List<TipoHoraExtra> consultaTipoHoraExtra(Integer cod_regime) {

        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime
                    + " ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer cod_tipo = rs.getInt("cod_tipo");
                String nome = rs.getString("nome");
                String isPadraoStr = rs.getString("padrao");
                Boolean isPadrao;
                if (isPadraoStr.equals("0")) {
                    isPadrao = false;
                } else {
                    isPadrao = true;
                }
                TipoHoraExtra tipoHoraExtra = new TipoHoraExtra(cod_regime, cod_tipo, nome, isPadrao);
                tipoHoraExtraList.add(tipoHoraExtra);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return tipoHoraExtraList;
    }

    public String consultaTipoHoraExtra(Integer cod_regime, Integer cod_tipoHoraExtra) {

        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        String nome = null;
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime + " and cod_tipo = " +cod_tipoHoraExtra
                    + " ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                
                nome = rs.getString("nome");
                
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return nome;
    }
    public TipoHoraExtra consultaTipoHoraExtraTipo(Integer cod_regime, Integer cod_tipoHoraExtra) {

        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        TipoHoraExtra tipo = new TipoHoraExtra();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime + " and cod_tipo = " +cod_tipoHoraExtra
                    + " ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tipo = new TipoHoraExtra();
                String nome = rs.getString("nome");
                Boolean padrao = rs.getBoolean("padrao");
                // dá pra acrescentar mais se quiser
                tipo.setCod_regime(cod_regime);
                tipo.setCod_tipo(cod_tipoHoraExtra);
                tipo.setIsPadrao(padrao);
                tipo.setNome(nome);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return tipo;
    }

    public Boolean inserirNovoTipo(TipoHoraExtra tipoHoraExtra) {

        Boolean flag = true;
        PreparedStatement pstmt = null;
        if (tipoHoraExtra.getNome().equals("")) {
            return false;
        }
        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Tipo_HoraExtra "
                    + " where cod_regime = " + tipoHoraExtra.getCod_regime() + " and"
                    + " nome = '" + tipoHoraExtra.getNome() + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {

                sql = "select count(*) as quant from Tipo_HoraExtra "
                        + " where cod_regime = " + tipoHoraExtra.getCod_regime();

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                Integer quant = 0;
                while (rs.next()) {
                    quant = rs.getInt("quant");
                }
                rs.close();
                stmt.close();

                if (quant.equals(0)) {
                    String query = "insert into Tipo_HoraExtra(cod_regime,nome,valor,verba,padrao) values(?,?,?,?,?)";
                    pstmt = c.prepareStatement(query);
                    pstmt.setInt(1, tipoHoraExtra.getCod_regime());
                    pstmt.setString(2, tipoHoraExtra.getNome());
                    pstmt.setFloat(3, tipoHoraExtra.getValor());
                    pstmt.setString(4, tipoHoraExtra.getVerba());
                    pstmt.setString(5, "1");
                    pstmt.executeUpdate();
                    pstmt = null;
                } else {
                    String query = "insert into Tipo_HoraExtra(cod_regime,nome,valor,verba) values(?,?,?,?)";
                    pstmt = c.prepareStatement(query);
                    pstmt.setInt(1, tipoHoraExtra.getCod_regime());
                    pstmt.setString(2, tipoHoraExtra.getNome());
                    pstmt.setFloat(3, tipoHoraExtra.getValor());
                    pstmt.setString(4, tipoHoraExtra.getVerba());
                    pstmt.executeUpdate();
                    pstmt = null;
                }

                sql = "select cod_tipo from Tipo_HoraExtra "
                        + " where cod_regime = " + tipoHoraExtra.getCod_regime() + " and"
                        + " nome = '" + tipoHoraExtra.getNome() + "'";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                Integer cod_tipo = null;
                while (rs.next()) {
                    cod_tipo = rs.getInt("cod_tipo");
                }
                rs.close();
                stmt.close();

                for (int i = 1; i <= 8; i++) {
                    String query = "insert into Detalhe_HoraExtra(cod_tipo,dia) values(?,?)";
                    pstmt = c.prepareStatement(query);
                    pstmt.setInt(1, cod_tipo);
                    pstmt.setInt(2, i);
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    if (c != null) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public void editDetalheInicio(Integer cod_tipo, Integer dia, Float inicio) {


        PreparedStatement pstmt = null;
        try {

            String query = "update Detalhe_HoraExtra set inicio = ?"
                    + " where cod_tipo = ? and "
                    + " dia = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setFloat(1, inicio);
            pstmt.setInt(2, cod_tipo);
            pstmt.setInt(3, dia);

            pstmt.executeUpdate();


        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public Boolean inserirGratificacao(Gratificacao gratificacao) {

        Boolean flag = true;
        PreparedStatement pstmt = null;
        if (gratificacao.getNome().equals("")) {
            return false;
        }
        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from gratificacao "
                    + " where cod_regime = " + gratificacao.getCod_regime() + " and"
                    + " nome = '" + gratificacao.getNome() + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {

                String query = "insert into gratificacao(cod_regime,nome,valor,verba) values(?,?,?,?)";
                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, gratificacao.getCod_regime());
                pstmt.setString(2, gratificacao.getNome());
                pstmt.setFloat(3, gratificacao.getValor());
                pstmt.setString(4, gratificacao.getVerba());
                pstmt.executeUpdate();
                pstmt = null;

                sql = "select cod_gratificacao from gratificacao "
                        + " where cod_regime = " + gratificacao.getCod_regime() + " and"
                        + " nome = '" + gratificacao.getNome() + "'";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                Integer cod_gratificacao = null;
                while (rs.next()) {
                    cod_gratificacao = rs.getInt("cod_gratificacao");
                }
                rs.close();
                stmt.close();

                for (int i = 1; i <= 7; i++) {
                    String query2 = "insert into DetalheGratificacao(cod_gratificacao,diaSemana) values(?,?)";
                    pstmt = c.prepareStatement(query2);
                    pstmt.setInt(1, cod_gratificacao);
                    pstmt.setInt(2, i);
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    if (c != null) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public Boolean inserirNovaJustificativaHoraExtra(Integer cod_regime, String descricaoJustificativa) {

        Boolean flag = true;
        PreparedStatement pstmt = null;
        if (descricaoJustificativa.equals("")) {
            return false;
        }
        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Tipo_HoraExtra "
                    + " where cod_regime = " + cod_regime + " and"
                    + " nome = '" + descricaoJustificativa + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {
                String query = "insert categoriaHoraExtra(cod_regime,descricao) values(?,?)";
                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, cod_regime);
                pstmt.setString(2, descricaoJustificativa);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    if (c != null) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public Boolean editJustificativaHoraExtra(Justificativa editJustificativa) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            String query = "update categoriaHoraExtra set descricao = ? "
                    + " where cod = ? ";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, editJustificativa.getDescricao());
            pstmt.setInt(2, editJustificativa.getCod_justificativa());
            pstmt.executeUpdate();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public Boolean editTipoHoraExtra(Integer cod_regime, Integer cod_tipo, TipoHoraExtra tipoHoraExtra) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Tipo_HoraExtra "
                    + " where cod_regime = " + cod_regime + " and"
                    + " nome = '" + tipoHoraExtra.getNome() + "' and"
                    + " cod_tipo != " + tipoHoraExtra.getCod_tipo();

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {
                String query = "update Tipo_HoraExtra set nome = ?,valor = ?,verba=?"
                        + " where cod_tipo = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, tipoHoraExtra.getNome());
                pstmt.setFloat(2, tipoHoraExtra.getValor());
                pstmt.setString(3, tipoHoraExtra.getVerba());
                pstmt.setInt(4, cod_tipo);

                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public Boolean editGratificacao(Integer cod_regime, Integer cod_gratificacao, Gratificacao gratificacao) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Gratificacao "
                    + " where cod_regime = " + cod_regime + " and"
                    + " nome = '" + gratificacao.getNome() + "' and"
                    + " cod_gratificacao != " + gratificacao.getCod_gratificacao();

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();
            stmt.close();

            if (flag) {
                String query = "update Gratificacao set nome = ?,valor = ?,verba=?"
                        + " where cod_gratificacao = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, gratificacao.getNome());
                pstmt.setFloat(2, gratificacao.getValor());
                pstmt.setString(3, gratificacao.getVerba());
                pstmt.setInt(4, cod_gratificacao);

                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public void editDetalheGratificacao(Integer cod_gratificacao, Integer dia, String inicio, String fim) {

        PreparedStatement pstmt = null;
        try {

            String query = "update DetalheGratificacao set inicio = ?, fim = ?"
                    + " where cod_gratificacao = ? and "
                    + " diaSemana = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, inicio);
            pstmt.setString(2, fim);
            pstmt.setInt(3, cod_gratificacao);
            pstmt.setInt(4, dia);

            pstmt.executeUpdate();


        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public void editDetalheDiaInteiro(Integer cod_tipo, Integer dia, Boolean isDiaInteiro) {


        PreparedStatement pstmt = null;
        try {

            String query = "update Detalhe_HoraExtra set dia_inteiro = ?"
                    + " where cod_tipo = ? and "
                    + " dia = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setBoolean(1, isDiaInteiro);
            pstmt.setInt(2, cod_tipo);
            pstmt.setInt(3, dia);

            pstmt.executeUpdate();


        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public void editDetalheGratificacaoInicio(Integer cod_gratificacao, Integer dia, String inicio) {

        PreparedStatement pstmt = null;
        try {

            String query = "update DetalheGratificacao set inicio = ?"
                    + " where cod_gratificacao = ? and "
                    + " diaSemana = ?";

            try {
                inicio += ":00";
                Time timeInicio = Time.valueOf(inicio);
            } catch (Exception e) {
                inicio = null;
            }

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, inicio);
            pstmt.setInt(2, cod_gratificacao);
            pstmt.setInt(3, dia);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void editDetalheGratificacaoFim(Integer cod_gratificacao, Integer dia, String fim) {

        PreparedStatement pstmt = null;
        try {

            String query = "update DetalheGratificacao set fim = ?"
                    + " where cod_gratificacao = ? and "
                    + " diaSemana = ?";

            try {
                fim += ":00";
                Time timeFim = Time.valueOf(fim);
            } catch (Exception e) {
                fim = null;
            }

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, fim);
            pstmt.setInt(2, cod_gratificacao);
            pstmt.setInt(3, dia);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public TipoHoraExtra consultaEditTipoHoraExtra(Integer cod_tipo) {

        TipoHoraExtra tipoHoraExtra = new TipoHoraExtra();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select cod_tipo,nome,valor,verba from Tipo_HoraExtra "
                    + " where cod_tipo = " + cod_tipo;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String nome = rs.getString("nome");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                tipoHoraExtra.setNome(nome);
                tipoHoraExtra.setValor(valor);
                tipoHoraExtra.setVerba(verba);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return tipoHoraExtra;
    }

    public Gratificacao consultaEditGratificacao(Integer cod_gratificacao) {

        Gratificacao gratificacao = new Gratificacao();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select cod_gratificacao,cod_regime,nome,valor,verba from Gratificacao "
                    + " where cod_gratificacao = " + cod_gratificacao;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String nome = rs.getString("nome");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                gratificacao.setNome(nome);
                gratificacao.setValor(valor);
                gratificacao.setVerba(verba);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return gratificacao;
    }

    public void setTipoPadrao(Integer cod_regime, Integer cod_tipo) {

        PreparedStatement pstmt = null;

        try {
            String query = " update Tipo_HoraExtra set padrao = 0 "
                    + " where cod_regime = ?";
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_regime);
            pstmt.executeUpdate();

            pstmt = null;

            query = "update Tipo_HoraExtra set padrao = 1 "
                    + " where cod_tipo = ? and "
                    + " cod_regime = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_tipo);
            pstmt.setInt(2, cod_regime);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public String deleteTipoHoraExtra(Integer cod_regime, Integer cod_tipo) {

        String flagStr = "0";
        PreparedStatement pstmt = null;

        try {

            String query = "select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime;

            pstmt = c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            Boolean flag = true;
            Integer count = 0;
            while (rs.next()) {
                count++;
                String padrao = rs.getString("padrao");
                Integer cod_tipo_ = rs.getInt("cod_tipo");
                if (padrao.equals("1") && cod_tipo.equals(cod_tipo_)) {
                    flag = false;
                    flagStr = "2";
                }
            }

            if (flag || (count == 1)) {
                query = "delete from Detalhe_HoraExtra"
                        + " where cod_tipo = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, cod_tipo);
                pstmt.executeUpdate();

                pstmt = null;

                query = "delete from Tipo_HoraExtra"
                        + " where cod_tipo = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, cod_tipo);
                pstmt.executeUpdate();
                flagStr = "0";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flagStr = "1";
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flagStr;
    }

    public String deleteGratificacao(Integer cod_gratificacao) {

        PreparedStatement pstmt = null;
        String flagStr;
        try {

            String query = "delete from DetalheGratificacao"
                    + " where cod_gratificacao = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_gratificacao);
            pstmt.executeUpdate();

            pstmt = null;

            query = "delete from Gratificacao"
                    + " where cod_gratificacao = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_gratificacao);
            pstmt.executeUpdate();
            flagStr = "0";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flagStr = "1";
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flagStr;
    }

    public Boolean deleteJustificativaHoraExtra(Integer cod) {

        Boolean flagStr = true;
        PreparedStatement pstmt = null;

        try {

            String query = "delete from categoriaHoraExtra"
                    + " where cod = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod);
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flagStr = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flagStr;
    }

    public List<DetalheHoraExtra> consultaDetalheHoraExtra(Integer cod_tipo) {

        List<DetalheHoraExtra> detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select * from Detalhe_HoraExtra"
                    + " where cod_tipo =" + cod_tipo
                    + " ORDER BY dia";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer dia = rs.getInt("dia");
                Float inicio = rs.getFloat("inicio");
                Integer isDiaInteiroInt = rs.getInt("dia_inteiro");
                Boolean isDiaInteiro;
                if (isDiaInteiroInt.equals(0)) {
                    isDiaInteiro = false;
                } else {
                    isDiaInteiro = true;
                }
                DetalheHoraExtra detalheHoraExtra = new DetalheHoraExtra(cod_tipo, dia, inicio, isDiaInteiro);
                detalheHoraExtraList.add(detalheHoraExtra);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return detalheHoraExtraList;
    }

    public void updateFeriadoCritico(Integer cod_regime, Boolean feriadoCritico) {

        PreparedStatement pstmt = null;
        try {

            String sql = "update Regime_HoraExtra set feriadoCritico = ?"
                    + " where cod_regime = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setBoolean(1, feriadoCritico);
            pstmt.setInt(2, cod_regime);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<DetalheGratificacao> consultaDetalheGratificacao(Integer cod_gratificacao) {

        List<DetalheGratificacao> detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select * from DetalheGratificacao"
                    + " where cod_gratificacao =" + cod_gratificacao
                    + " ORDER BY diaSemana";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer dia = rs.getInt("diaSemana");
                String inicio = rs.getString("inicio");
                String fim = rs.getString("fim");

                DetalheGratificacao detalheGratificacao = new DetalheGratificacao(dia, inicio, fim, cod_gratificacao);
                detalheGratificacaoList.add(detalheGratificacao);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return detalheGratificacaoList;
    }

    public RegimeHoraExtra getRegimeHoraExtra(Integer cod_regime) {

        RegimeHoraExtra regimeHoraExtra = new RegimeHoraExtra();
        try {

            ResultSet rs;
            Statement stmt;
            String sql;

            sql = " select r.*"
                    + " from Regime_HoraExtra r"
                    + " where cod_regime = " + cod_regime ;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                regimeHoraExtra.setCod(cod_regime);
                String nome = rs.getString("nome");
                regimeHoraExtra.setNome(nome);
                Integer modoTolerancia = rs.getInt("modoTolerancia");
                regimeHoraExtra.setModoTolerancia(modoTolerancia);
                Integer tolerancia = rs.getInt("tolerancia");
                regimeHoraExtra.setTolerancia(tolerancia);
                Integer limiteEntrada = rs.getInt("limiteEntrada");
                limiteEntrada = (limiteEntrada == null) ?  0 : limiteEntrada;
                regimeHoraExtra.setLimiteEntrada(limiteEntrada);
                Boolean feriadoCritico = rs.getBoolean("feriadoCritico");
                regimeHoraExtra.setFeriadoCritico(feriadoCritico);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return regimeHoraExtra;
    }

     public void mudarModoRegime(Integer cod_regime, Integer modoRegimeInt) {

        PreparedStatement pstmt = null;
        try {

            String sql = "update Regime_HoraExtra set modoTolerancia = ?"
                    + " where cod_regime = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, modoRegimeInt);
            pstmt.setInt(2, cod_regime);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
     }
     
     public void mudarFeriadoCritico(Integer cod_regime, Boolean feriadoCritico) {
        PreparedStatement pstmt = null;
        try {

            String sql = "update Regime_HoraExtra set feriadoCritico = ?"
                    + " where cod_regime = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setBoolean(1, feriadoCritico);
            pstmt.setInt(2, cod_regime);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
     }

     public void mudarTolerancia(Integer cod_regime, Integer tolerancia) {

        PreparedStatement pstmt = null;
        try {

            String sql = "update Regime_HoraExtra set tolerancia = ?"
                    + " where cod_regime = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, tolerancia);
            pstmt.setInt(2, cod_regime);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
     }

    String consultaJustificativa(Integer cod_regime, int cod_justificativa) {

        String descricao = null;
        try {
            ResultSet rs;
            Statement stmt;
            

            String sql;

            sql = "select * from categoriaHoraExtra "
                    + "where cod_regime = " + cod_regime + " and cod = "+ cod_justificativa
                    + " ORDER BY descricao";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                
                descricao = rs.getString("descricao");
                
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return descricao;
    }

    String consultaGratificacao(Integer cod_regime, int cod_gratificacao) {
        String nome = null;
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from gratificacao "
                    + "where cod_regime = " + cod_regime +" and cod_gratificacao = "+cod_gratificacao
                    + " ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {

                nome = rs.getString("nome");

            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return nome;
    }

     public void fecharConexao() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
        }
    }
}
