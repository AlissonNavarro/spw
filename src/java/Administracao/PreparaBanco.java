package Administracao;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PreparaBanco {

    Connection c;

    public PreparaBanco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("Administracao: Banco 1: " + ex.getMessage());
        }
    }

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Administracao: Conectar: " + cnfe);
        }
    }

    public boolean prepareVerba() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from verba";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere a verba
                String sqlCargo = "INSERT INTO verba (empresa, adicionalNoturno, atrasos, atrasosMenorHora, "
                        + " atrasosMaiorHora, FeriadoCritico, DSR, Faltas) VALUES (?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sqlCargo);
                pstmt.setInt(1, 1);
                pstmt.setInt(2, 36);
                pstmt.setInt(3, 552);
                pstmt.setInt(4, 557);
                pstmt.setInt(5, 558);
                pstmt.setInt(6, 138);
                pstmt.setInt(7, 453);
                pstmt.setInt(8, 551);
                pstmt.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return ok;
    }

    public boolean prepareTipoHoraExtra() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from tipo_horaextra";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere os tipo Hora Extra
                String sqlTipoHoraExtra = "INSERT INTO tipo_horaextra (cod_regime, nome, padrao, valor ) VALUES (?,?,?,?)";
                pstmt = c.prepareStatement(sqlTipoHoraExtra);

                pstmt.setInt(1, 1);
                pstmt.setString(2, "H E 100%");
                pstmt.setBoolean(3, true);
                pstmt.setFloat(4, 1);
                pstmt.executeUpdate();

                pstmt.setInt(1, 1);
                pstmt.setString(2, "H E 50%");
                pstmt.setBoolean(3, false);
                pstmt.setFloat(4, Float.parseFloat("0.5"));
                pstmt.executeUpdate();

                pstmt.setInt(1, 2);
                pstmt.setString(2, "H. E. 100%");
                pstmt.setBoolean(3, false);
                pstmt.setFloat(4, 1);
                pstmt.executeUpdate();

                pstmt.setInt(1, 2);
                pstmt.setString(2, "H. E. 50%");
                pstmt.setBoolean(3, true);
                pstmt.setFloat(4, Float.parseFloat("0.5"));
                pstmt.executeUpdate();

                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean prepareRegime() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from regime_horaextra";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere os regimes
                String sqlCargo = "INSERT INTO regime_horaextra (nome, feriadoCritico, modoTolerancia, tolerancia) VALUES (?,?,?,?)";
                pstmt = c.prepareStatement(sqlCargo);
                pstmt.setString(1, "CELETISTA");
                pstmt.setBoolean(2, true);
                pstmt.setInt(3, 1);
                pstmt.setInt(4, 10);
                pstmt.executeUpdate();

                sqlCargo = "INSERT INTO regime_horaextra (nome, feriadoCritico, modoTolerancia, tolerancia) VALUES (?,?,?,?)";
                pstmt = c.prepareStatement(sqlCargo);
                pstmt.setString(1, "ESTATUTARIO");
                pstmt.setBoolean(2, true);
                pstmt.setInt(3, 2);
                pstmt.setInt(4, 9);
                pstmt.executeUpdate();

                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean preparePerfil() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {

            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from perfil";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere o primeiro Perfil
                String sqlPerfil = "INSERT INTO perfil (nome_perfil "
                        + " , cadastrosEConfiguracoes "
                        + " , permissoes "
                        + " , funcionarios "
                        + " , deptos "
                        + " , cargos "
                        + " , feriados "
                        + " , justificativas "
                        + " , tituloDoRelatorio "
                        + " , horaExtraEGratificacoes "
                        + " , consInd "
                        + " , freqnComEscala "
                        + " , freqnSemEscala "
                        + " , horaExtra "
                        + " , horCronoJorn "
                        + " , horarios "
                        + " , cronogramas "
                        + " , jornadas "
                        + " , relatorios "
                        + " , relatorioMensComEscala "
                        + " , relatorioMensSemEscala "
                        + " , relatorioDeResumoDeEscalas "
                        + " , relatorioDeResumoDeFrequencias "
                        + " , listaDePresenca "
                        + " , abonos "
                        + " , solicitacao "
                        + " , diasEmAberto "
                        + " , abonoRapido "
                        + " , historicoAbono "
                        + " , abonosEmMassa "
                        + " , exclusaoAbono "
                        + " , rankingAbono "
                        + " , afdt "
                        + " , acjef "
                        + " , espelhoDePonto "
                        + " , bancoDeDados "
                        + " , logs "
                        + " , relatorioConfiguracao "
                        + " , categoriaAfastamento "
                        + " , afastamento "
                        + " , empresas "
                        + " , verbas "
                        + " , manutencao "
                        + " , showResumo "
                        + " , relatorioCatracas "
                        + " , pesquisarData "
                        + " , consultaHoraExtra "
                        + " , consultaIrregulares "
                        + " , presenca "
                        + " , listaRelogios "
                        + " , scanIps "
                        + " , downloadAfd "
                        + " , autenticaSerial "
                        + " , relogioPonto ) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sqlPerfil);
                pstmt.setString(1, "AdminSPW");
                for (int i = 2; i <= 54; i++) {
                    pstmt.setBoolean(i, true);
                }
                pstmt.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean prepareCargo() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from cargo";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere o primeiro Cargo
                String sqlCargo = "INSERT INTO cargo (nome) VALUES (?)";
                pstmt = c.prepareStatement(sqlCargo);
                pstmt.setString(1, "Admin SPW");
                pstmt.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean prepareDepartamento() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from DEPARTMENTS";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere o primeiro Departamento
                String sqlDept = "INSERT INTO DEPARTMENTS (DEPTNAME, SUPDEPTID) VALUES (?,?)";
                pstmt = c.prepareStatement(sqlDept);
                pstmt.setString(1, "SPW");
                pstmt.setInt(2, 0);
                pstmt.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean prepareusuario() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from userinfo";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere o primeiro Uuário
                Integer dept = 0;
                Integer cargo = 0;
                Integer perfil = 0;

                sql = "select deptid from DEPARTMENTS where DEPTNAME like '%SPW%'";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    dept = rs.getInt("deptid");
                }
                sql = "select cod_cargo from Cargo where nome like '%SPW%'";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    cargo = rs.getInt("cod_cargo");
                }
                sql = "select cod_perfil from Perfil where nome_perfil like '%AdminSPW%'";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    perfil = rs.getInt("cod_perfil");
                }
                rs.close();
                stmt.close();

                String sqlUser = "INSERT INTO userinfo (SSN, NAME, DEFAULTDEPTID, permissao, cod_regime, CARGO, Perfil, ativo) "
                        + " VALUES (?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sqlUser);
                pstmt.setString(1, "304.201.570-00");
                pstmt.setString(2, "Admin SPW");
                pstmt.setInt(3, dept);
                pstmt.setInt(4, 1);
                pstmt.setInt(5, 0);
                pstmt.setInt(6, cargo);
                pstmt.setInt(7, perfil);
                pstmt.setBoolean(8, true);
                pstmt.executeUpdate();

                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean preparaRepTipo() {
        System.out.println("repTIpo");
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from RepTipo";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                String sqlUser = "INSERT INTO RepTipo (repTipoId, repMarca, nfrOn, modelOn, localOn, idcompanyOn, commtypeOn, biometricModuleOn,"
                        + "dateLastNsrOn, lastNsrOn, pathCollectFileOn, nsrOn, ipRoaterOn, maskOn, senhaOn) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sqlUser);

                pstmt.setInt(1, 1);
                pstmt.setString(2, "Iddata");
                for (int x = 3; x <= 11; x++) {
                    pstmt.setBoolean(x, true);
                }
                for (int x = 12; x <= 15; x++) {
                    pstmt.setBoolean(x, false);
                }
                pstmt.executeUpdate();

                pstmt.setInt(1, 2);
                pstmt.setString(2, "Trilobit");
                for (int x = 3; x <= 11; x++) {
                    pstmt.setBoolean(x, false);
                }
                for (int x = 12; x <= 15; x++) {
                    pstmt.setBoolean(x, true);
                }
                pstmt.executeUpdate();

                pstmt.setInt(1, 3);
                pstmt.setString(2, "Zk");
                for (int x = 3; x <= 15; x++) {
                    pstmt.setBoolean(x, false);
                }
                pstmt.executeUpdate();

                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public boolean prepareConfig() {
        boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from config";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere o primeiro Cargo
                String sqlCargo = "INSERT INTO config VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sqlCargo);
                pstmt.setString(1, null);
                pstmt.setString(2, "ol7LiUsfRxg=");
                pstmt.setInt(3, 1);
                pstmt.setBytes(4, null);
                pstmt.setString(5, null);
                pstmt.setString(6, null);
                pstmt.setString(7, null);
                pstmt.setString(8, "ip_sender");
                pstmt.setString(9, "ip_digitais");
                pstmt.setInt(10, 0);
                pstmt.setString(11, null);
                pstmt.setString(12, null);
                pstmt.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        }
        return ok;
    }

    public void fecharConexao() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("Administracao: fecharConexao: " + ex);
            }
        }
    }
}
