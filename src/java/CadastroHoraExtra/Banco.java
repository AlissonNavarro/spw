package CadastroHoraExtra;

import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Banco implements Serializable {

    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public List<RegimeHoraExtra> consultaRegimeHoraExtra() {
        List<RegimeHoraExtra> regimeHoraExtraList = new ArrayList<RegimeHoraExtra>();
        try {
            String sql = "select * from Regime_HoraExtra ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer cod = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                Boolean feriadoCritico = rs.getBoolean("feriadoCritico");
                RegimeHoraExtra regimeHoraExtra = new RegimeHoraExtra(cod, nome, feriadoCritico);
                regimeHoraExtraList.add(regimeHoraExtra);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return regimeHoraExtraList;
    }

    public List<Justificativa> consultaJustificativasHoraExtra(Integer cod_regime) {

        List<Justificativa> justificativaList = new ArrayList<Justificativa>();
        try {
            String sql = "select * from categoriaHoraExtra "
                    + "where cod_regime = " + cod_regime
                    + " ORDER BY descricao";
            ResultSet rs = con.executeQuery(sql);
            justificativaList = new ArrayList<Justificativa>();
            while (rs.next()) {
                Integer cod = rs.getInt("cod");
                String descricao = rs.getString("descricao");
                justificativaList.add(new Justificativa(cod, descricao));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return justificativaList;
    }

    public List<Gratificacao> consultaGratificacao(Integer cod_regime) {
        List<Gratificacao> gratificacaoList = new ArrayList<Gratificacao>();
        try {
            String sql = "select * from gratificacao "
                    + "where cod_regime = " + cod_regime
                    + " ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
            gratificacaoList = new ArrayList<Gratificacao>();
            while (rs.next()) {
                Integer cod_gratificacao = rs.getInt("cod_gratificacao");
                String nome = rs.getString("nome");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                gratificacaoList.add(new Gratificacao(cod_gratificacao, cod_regime, nome, valor, verba));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return gratificacaoList;
    }

    public Boolean inserirNovoRegime(String nome_Regime) {
        Boolean flag = true;
        if (nome_Regime.equals("")) {
            return false;
        }

        try {
            String sql = "select nome from Regime_HoraExtra "
                    + " where nome = '" + nome_Regime + "'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = false;
            }
            rs.close();
            if (flag) {
                String query = "insert into Regime_HoraExtra(nome, feriadoCritico) values(?, ?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, nome_Regime);
                    con.pstmt.setBoolean(2, true);
                    con.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean editRegimeHoraExtra(Integer cod_regime, String editNomeRegime) {
        Boolean flag = true;
        try {
            String sql = "select nome from Regime_HoraExtra "
                    + " where nome = '" + editNomeRegime + "'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = false;
            }
            rs.close();
            if (flag) {
                String query = "update Regime_HoraExtra set nome = ?"
                        + " where cod_regime = ?";

                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, editNomeRegime);
                    con.pstmt.setInt(2, cod_regime);
                    con.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public String consultaEditRegimeHoraExtra(Integer cod_regime) {
        String nome = null;
        try {

            String sql = " select * from Regime_HoraExtra "
                    + " where cod_regime = " + cod_regime;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return nome;
    }

    public Boolean deleteRegimeHoraExtra(Integer cod_regime) {
        Boolean flag = true;
        try {
            String query = "delete from Regime_HoraExtra"
                    + " where cod_regime = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod_regime);
            }
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public List<TipoHoraExtra> consultaTipoHoraExtra(Integer cod_regime) {

        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        try {
            String sql = " select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime
                    + " ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
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

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return tipoHoraExtraList;
    }

    public String consultaTipoHoraExtra(Integer cod_regime, Integer cod_tipoHoraExtra) {
        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        String nome = null;
        try {

            String sql = " select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime + " and cod_tipo = " + cod_tipoHoraExtra
                    + " ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return nome;
    }

    public TipoHoraExtra consultaTipoHoraExtraTipo(Integer cod_regime, Integer cod_tipoHoraExtra) {

        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<TipoHoraExtra>();
        TipoHoraExtra tipo = new TipoHoraExtra();
        try {
            String sql = " select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime + " and cod_tipo = " + cod_tipoHoraExtra
                    + " ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return tipo;
    }

    public Boolean inserirNovoTipo(TipoHoraExtra tipoHoraExtra) {
        Boolean flag = true;
        if (tipoHoraExtra.getNome().equals("")) {
            return false;
        }
        try {
            String sql = "select nome from Tipo_HoraExtra "
                    + " where cod_regime = " + tipoHoraExtra.getCod_regime() + " and"
                    + " nome = '" + tipoHoraExtra.getNome() + "'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = false;
            }
            rs.close();
            if (flag) {
                sql = "select count(*) as quant from Tipo_HoraExtra "
                        + " where cod_regime = " + tipoHoraExtra.getCod_regime();
                rs = con.executeQuery(sql);
                Integer quant = 0;
                while (rs.next()) {
                    quant = rs.getInt("quant");
                }
                rs.close();

                if (quant.equals(0)) {
                    String query = "insert into Tipo_HoraExtra(cod_regime,nome,valor,verba,padrao) values(?,?,?,?,?)";
                    if (con.prepareStatement(query)) {
                        con.pstmt.setInt(1, tipoHoraExtra.getCod_regime());
                        con.pstmt.setString(2, tipoHoraExtra.getNome());
                        con.pstmt.setFloat(3, tipoHoraExtra.getValor());
                        con.pstmt.setString(4, tipoHoraExtra.getVerba());
                        con.pstmt.setString(5, "1");
                        con.executeUpdate();

                    }
                } else {
                    String query = "insert into Tipo_HoraExtra(cod_regime,nome,valor,verba) values(?,?,?,?)";
                    if (con.prepareStatement(query)) {
                        con.pstmt.setInt(1, tipoHoraExtra.getCod_regime());
                        con.pstmt.setString(2, tipoHoraExtra.getNome());
                        con.pstmt.setFloat(3, tipoHoraExtra.getValor());
                        con.pstmt.setString(4, tipoHoraExtra.getVerba());
                        con.executeUpdate();
                    }
                }

                sql = "select cod_tipo from Tipo_HoraExtra "
                        + " where cod_regime = " + tipoHoraExtra.getCod_regime() + " and"
                        + " nome = '" + tipoHoraExtra.getNome() + "'";

                rs = con.executeQuery(sql);
                Integer cod_tipo = null;
                while (rs.next()) {
                    cod_tipo = rs.getInt("cod_tipo");
                }
                rs.close();

                for (int i = 1; i <= 8; i++) {
                    String query = "insert into Detalhe_HoraExtra(cod_tipo,dia) values(?,?)";
                    if (con.prepareStatement(query)) {
                        con.pstmt.setInt(1, cod_tipo);
                        con.pstmt.setInt(2, i);
                        con.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public void editDetalheInicio(Integer cod_tipo, Integer dia, Float inicio) {
        try {
            String query = "update Detalhe_HoraExtra set inicio = ?"
                    + " where cod_tipo = ? and "
                    + " dia = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setFloat(1, inicio);
                con.pstmt.setInt(2, cod_tipo);
                con.pstmt.setInt(3, dia);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }

    }

    public Boolean inserirGratificacao(Gratificacao gratificacao) {
        Boolean flag = true;
        if (gratificacao.getNome().equals("")) {
            return false;
        }
        try {
            String sql = "select nome from gratificacao "
                    + " where cod_regime = " + gratificacao.getCod_regime() + " and"
                    + " nome = '" + gratificacao.getNome() + "'";

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = false;
            }
            rs.close();
            if (flag) {

                String query = "insert into gratificacao(cod_regime,nome,valor,verba) values(?,?,?,?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setInt(1, gratificacao.getCod_regime());
                    con.pstmt.setString(2, gratificacao.getNome());
                    con.pstmt.setFloat(3, gratificacao.getValor());
                    con.pstmt.setString(4, gratificacao.getVerba());
                    con.executeUpdate();
                }

                sql = "select cod_gratificacao from gratificacao "
                        + " where cod_regime = " + gratificacao.getCod_regime() + " and"
                        + " nome = '" + gratificacao.getNome() + "'";
                rs = con.executeQuery(sql);
                Integer cod_gratificacao = null;
                while (rs.next()) {
                    cod_gratificacao = rs.getInt("cod_gratificacao");
                }
                rs.close();
                for (int i = 1; i <= 7; i++) {
                    String query2 = "insert into DetalheGratificacao(cod_gratificacao,diaSemana) values(?,?)";
                    if (con.prepareStatement(query2)) {
                        con.pstmt.setInt(1, cod_gratificacao);
                        con.pstmt.setInt(2, i);
                        con.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean inserirNovaJustificativaHoraExtra(Integer cod_regime, String descricaoJustificativa) {
        Boolean flag = true;
        if (descricaoJustificativa.equals("")) {
            return false;
        }
        try {
            String sql = "select nome from Tipo_HoraExtra "
                    + " where cod_regime = " + cod_regime + " and"
                    + " nome = '" + descricaoJustificativa + "'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = false;
            }
            rs.close();
            if (flag) {
                String query = "insert categoriaHoraExtra(cod_regime,descricao) values(?,?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setInt(1, cod_regime);
                    con.pstmt.setString(2, descricaoJustificativa);
                    con.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean editJustificativaHoraExtra(Justificativa editJustificativa) {
        Boolean flag = true;
        try {
            String query = "update categoriaHoraExtra set descricao = ? "
                    + " where cod = ? ";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, editJustificativa.getDescricao());
                con.pstmt.setInt(2, editJustificativa.getCod_justificativa());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean editTipoHoraExtra(Integer cod_regime, Integer cod_tipo, TipoHoraExtra tipoHoraExtra) {
        Boolean flag = true;
        try {
            String sql = "select nome from Tipo_HoraExtra "
                    + " where cod_regime = " + cod_regime + " and"
                    + " nome = '" + tipoHoraExtra.getNome() + "' and"
                    + " cod_tipo != " + tipoHoraExtra.getCod_tipo();

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                flag = false;
            }
            rs.close();

            if (flag) {
                String query = "update Tipo_HoraExtra set nome = ?,valor = ?,verba=?"
                        + " where cod_tipo = ?";

                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, tipoHoraExtra.getNome());
                    con.pstmt.setFloat(2, tipoHoraExtra.getValor());
                    con.pstmt.setString(3, tipoHoraExtra.getVerba());
                    con.pstmt.setInt(4, cod_tipo);
                    con.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean editGratificacao(Integer cod_regime, Integer cod_gratificacao, Gratificacao gratificacao) {
        Boolean flag = true;
        try {
            String sql = "select nome from Gratificacao "
                    + " where cod_regime = " + cod_regime + " and"
                    + " nome = '" + gratificacao.getNome() + "' and"
                    + " cod_gratificacao != " + gratificacao.getCod_gratificacao();
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = false;
            }
            rs.close();
            if (flag) {
                String query = "update Gratificacao set nome = ?,valor = ?,verba=?"
                        + " where cod_gratificacao = ?";

                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, gratificacao.getNome());
                    con.pstmt.setFloat(2, gratificacao.getValor());
                    con.pstmt.setString(3, gratificacao.getVerba());
                    con.pstmt.setInt(4, cod_gratificacao);
                    con.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public void editDetalheGratificacao(Integer cod_gratificacao, Integer dia, String inicio, String fim) {
        try {
            String query = "update DetalheGratificacao set inicio = ?, fim = ?"
                    + " where cod_gratificacao = ? and "
                    + " diaSemana = ?";

            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, inicio);
                con.pstmt.setString(2, fim);
                con.pstmt.setInt(3, cod_gratificacao);
                con.pstmt.setInt(4, dia);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            con.Desconectar();
        }

    }

    public void editDetalheDiaInteiro(Integer cod_tipo, Integer dia, Boolean isDiaInteiro) {
        try {
            String query = "update Detalhe_HoraExtra set dia_inteiro = ?"
                    + " where cod_tipo = ? and "
                    + " dia = ?";

            if (con.prepareStatement(query)) {
                con.pstmt.setBoolean(1, isDiaInteiro);
                con.pstmt.setInt(2, cod_tipo);
                con.pstmt.setInt(3, dia);
                con.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }

    }

    public void editDetalheGratificacaoInicio(Integer cod_gratificacao, Integer dia, String inicio) {

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

            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, inicio);
                con.pstmt.setInt(2, cod_gratificacao);
                con.pstmt.setInt(3, dia);
                con.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public void editDetalheGratificacaoFim(Integer cod_gratificacao, Integer dia, String fim) {

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

            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, fim);
                con.pstmt.setInt(2, cod_gratificacao);
                con.pstmt.setInt(3, dia);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public TipoHoraExtra consultaEditTipoHoraExtra(Integer cod_tipo) {
        TipoHoraExtra tipoHoraExtra = new TipoHoraExtra();
        try {
            String sql = " select cod_tipo,nome,valor,verba from Tipo_HoraExtra "
                    + " where cod_tipo = " + cod_tipo;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                String nome = rs.getString("nome");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                tipoHoraExtra.setNome(nome);
                tipoHoraExtra.setValor(valor);
                tipoHoraExtra.setVerba(verba);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return tipoHoraExtra;
    }

    public Gratificacao consultaEditGratificacao(Integer cod_gratificacao) {
        Gratificacao gratificacao = new Gratificacao();
        try {

            String sql = " select cod_gratificacao,cod_regime,nome,valor,verba from Gratificacao "
                    + " where cod_gratificacao = " + cod_gratificacao;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                String nome = rs.getString("nome");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                gratificacao.setNome(nome);
                gratificacao.setValor(valor);
                gratificacao.setVerba(verba);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return gratificacao;
    }

    public void setTipoPadrao(Integer cod_regime, Integer cod_tipo) {
        try {
            String query = " update Tipo_HoraExtra set padrao = 0 "
                    + " where cod_regime = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod_regime);
                con.executeUpdate();
            }
            query = "update Tipo_HoraExtra set padrao = 1 "
                    + " where cod_tipo = ? and "
                    + " cod_regime = ?";

            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod_tipo);
                con.pstmt.setInt(2, cod_regime);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public String deleteTipoHoraExtra(Integer cod_regime, Integer cod_tipo) {
        String flagStr = "0";
        try {
            String query = "select * from Tipo_HoraExtra"
                    + " where cod_regime = " + cod_regime;

            con.prepareStatement(query);
            ResultSet rs = con.pstmt.executeQuery();
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

                if (con.prepareStatement(query)) {
                    con.pstmt.setInt(1, cod_tipo);
                    con.executeUpdate();
                }
                query = "delete from Tipo_HoraExtra"
                        + " where cod_tipo = ?";

                if (con.prepareStatement(query)) {
                    con.pstmt.setInt(1, cod_tipo);
                    con.executeUpdate();
                }
                flagStr = "0";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flagStr = "1";
        } finally {
            con.Desconectar();
        }
        return flagStr;
    }

    public String deleteGratificacao(Integer cod_gratificacao) {
        String flagStr;
        try {
            String query = "delete from DetalheGratificacao"
                    + " where cod_gratificacao = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod_gratificacao);
                con.executeUpdate();
            }
            query = "delete from Gratificacao"
                    + " where cod_gratificacao = ?";

            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod_gratificacao);
                con.executeUpdate();
            }
            flagStr = "0";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flagStr = "1";
        } finally {
            con.Desconectar();
        }
        return flagStr;
    }

    public Boolean deleteJustificativaHoraExtra(Integer cod) {
        Boolean flagStr = true;
        try {
            String query = "delete from categoriaHoraExtra"
                    + " where cod = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod);
            }
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flagStr = false;
        } finally {
            con.Desconectar();
        }
        return flagStr;
    }

    public List<DetalheHoraExtra> consultaDetalheHoraExtra(Integer cod_tipo) {
        List<DetalheHoraExtra> detalheHoraExtraList = new ArrayList<DetalheHoraExtra>();
        try {
            String sql = " select * from Detalhe_HoraExtra"
                    + " where cod_tipo =" + cod_tipo
                    + " ORDER BY dia";
            ResultSet rs = con.executeQuery(sql);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return detalheHoraExtraList;
    }

    public void updateFeriadoCritico(Integer cod_regime, Boolean feriadoCritico) {
        try {
            String sql = "update Regime_HoraExtra set feriadoCritico = ?"
                    + " where cod_regime = ?";

            if (con.prepareStatement(sql)) {
                con.pstmt.setBoolean(1, feriadoCritico);
                con.pstmt.setInt(2, cod_regime);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public List<DetalheGratificacao> consultaDetalheGratificacao(Integer cod_gratificacao) {
        List<DetalheGratificacao> detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
        try {
            String sql = " select * from DetalheGratificacao"
                    + " where cod_gratificacao =" + cod_gratificacao
                    + " ORDER BY diaSemana";

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer dia = rs.getInt("diaSemana");
                String inicio = rs.getString("inicio");
                String fim = rs.getString("fim");

                DetalheGratificacao detalheGratificacao = new DetalheGratificacao(dia, inicio, fim, cod_gratificacao);
                detalheGratificacaoList.add(detalheGratificacao);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return detalheGratificacaoList;
    }

    public RegimeHoraExtra getRegimeHoraExtra(Integer cod_regime) {
        RegimeHoraExtra regimeHoraExtra = new RegimeHoraExtra();
        try {
            String sql = " select r.*"
                    + " from Regime_HoraExtra r"
                    + " where cod_regime = " + cod_regime;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                regimeHoraExtra.setCod(cod_regime);
                String nome = rs.getString("nome");
                regimeHoraExtra.setNome(nome);
                Integer modoTolerancia = rs.getInt("modoTolerancia");
                regimeHoraExtra.setModoTolerancia(modoTolerancia);
                Integer tolerancia = rs.getInt("tolerancia");
                regimeHoraExtra.setTolerancia(tolerancia);
                Integer limiteEntrada = rs.getInt("limiteEntrada");
                limiteEntrada = (limiteEntrada == null) ? 0 : limiteEntrada;
                regimeHoraExtra.setLimiteEntrada(limiteEntrada);
                Boolean feriadoCritico = rs.getBoolean("feriadoCritico");
                regimeHoraExtra.setFeriadoCritico(feriadoCritico);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return regimeHoraExtra;
    }

    public void mudarModoRegime(Integer cod_regime, Integer modoRegimeInt) {
        try {
            String sql = "update Regime_HoraExtra set modoTolerancia = ?"
                    + " where cod_regime = ?";
            if (con.prepareStatement(sql)) {
                con.pstmt.setInt(1, modoRegimeInt);
                con.pstmt.setInt(2, cod_regime);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public void mudarFeriadoCritico(Integer cod_regime, Boolean feriadoCritico) {
        try {
            String sql = "update Regime_HoraExtra set feriadoCritico = ?"
                    + " where cod_regime = ?";

            if (con.prepareStatement(sql)) {
                con.pstmt.setBoolean(1, feriadoCritico);
                con.pstmt.setInt(2, cod_regime);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public void mudarTolerancia(Integer cod_regime, Integer tolerancia) {
        try {
            String sql = "update Regime_HoraExtra set tolerancia = ?"
                    + " where cod_regime = ?";

            if (con.prepareStatement(sql)) {
                con.pstmt.setInt(1, tolerancia);
                con.pstmt.setInt(2, cod_regime);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    String consultaJustificativa(Integer cod_regime, int cod_justificativa) {
        String descricao = null;
        try {
            String sql = "select * from categoriaHoraExtra "
                    + "where cod_regime = " + cod_regime + " and cod = " + cod_justificativa
                    + " ORDER BY descricao";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                descricao = rs.getString("descricao");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return descricao;
    }

    String consultaGratificacao(Integer cod_regime, int cod_gratificacao) {
        String nome = null;
        try {
            String sql = "select * from gratificacao "
                    + "where cod_regime = " + cod_regime + " and cod_gratificacao = " + cod_gratificacao
                    + " ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return nome;
    }

}
