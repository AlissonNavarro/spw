/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ppccardoso
 */
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
    
    public Boolean prepareVerba() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "if not exists (select * from sys.objects where object_id = OBJECT_ID(N'[dbo].[Verba]') and type=(N'U')) "
               +" begin CREATE TABLE [dbo].[Verba]( [empresa] [int] NOT NULL, [adicionalNoturno] [int] NOT NULL, [atrasos] [int] NOT NULL, [atrasosMenorHora] [int] NOT NULL, "
               +" [atrasosMaiorHora] [int] NOT NULL, [FeriadoCritico] [int] NOT NULL, [DSR] [int] NOT NULL, [Faltas] [int] NOT NULL "
               +" ) ON [PRIMARY] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_Empresa]  DEFAULT ((0)) FOR [empresa] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_adicionalNoturno]  DEFAULT ((0)) FOR [adicionalNoturno] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_atrasos]  DEFAULT ((0)) FOR [atrasos] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_atrasosMenorHora]  DEFAULT ((0)) FOR [atrasosMenorHora] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_atrasosMaiorHora]  DEFAULT ((0)) FOR [atrasosMaiorHora] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_FeriadoCritico]  DEFAULT ((0)) FOR [FeriadoCritico] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_DSR]  DEFAULT ((0)) FOR [DSR] "
                +" ALTER TABLE [dbo].[Verba] ADD  CONSTRAINT [DF_Verba_Faltas]  DEFAULT ((0)) FOR [Faltas] "
                +" end else "
                    + "begin"
                    + "	if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'Empresa') "
                    + "     alter table dbo.VERBA add Empresa int not null default ((0))"
                    + " if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'Empresa' and (xtype <> TYPE_ID('int') or isnullable<>0)) "
                    + "     alter table dbo.Verba alter column Empresa int not null"
                    + "	if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'AdicionalNoturno')"
                    + "     alter table dbo.VERBA add AdicionalNoturno int not null default ((0))"
                    + "	if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'AdicionalNoturno' and (xtype <> TYPE_ID('int') or isnullable<>0) )"
                    + "     alter table dbo.Verba alter column AdicionalNorturno int not null"
                    + "	if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'Atrasos')"
                    + "     alter table dbo.VERBA add Atrasos int not null default ((0)) "
                    + " if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'Atrasos' and (xtype <> TYPE_ID('int') or isnullable<>0) )"
                    + "     alter table dbo.Verba alter column Atrasos int not null	"
                    + "	if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'AtrasosMenorHora')"
                    + "     alter table dbo.VERBA add AtrasosMenorHora int not null default ((0))"
                    + "	if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'AtrasosMenorHora' and (xtype <> TYPE_ID('int') or isnullable<>0) )"
                    + "     alter table dbo.Verba alter column AtrasosMenorHora int not null"
                    + "	if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'AtrasosMaiorHora')"
                    + "     alter table dbo.VERBA add AtrasosMaiorHora int not null default ((0)) "
                    + " if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'AtrasosMaiorHora' and (xtype <> TYPE_ID('int') or isnullable<>0) )"
                    + "     alter table dbo.Verba alter column AtrasosMaiorHora int not null "
                    + " if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'FeriadoCritico') "
                    + "     alter table dbo.VERBA add FeriadoCritico int not null default ((0)) "
                    + "	if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'FeriadoCritico' and (xtype <> TYPE_ID('int') or isnullable<>0) )"
                    + "     alter table dbo.Verba alter column FeriadoCritico int not null"
                    + " if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'DSR')"
                    + "     alter table dbo.VERBA add DSR int not null default ((0))"
                    + " if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'DSR' and (xtype <> TYPE_ID('int') or isnullable<>0) )"
                    + "     alter table dbo.Verba alter column DSR int not null	"
                    + " if not exists (select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'Faltas') "
		    + "     alter table dbo.VERBA add Faltas int not null default ((0)) "
                    + " if exists(select * from syscolumns where id = OBJECT_ID(N'[dbo].[Verba]') and name = 'Faltas' and (xtype <> TYPE_ID('int') or isnullable<>0) ) "
                    + " alter table dbo.Verba alter column Faltas int not null end ";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
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
                if (c != null) {
                    c.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return ok;
    }
    
    public Boolean prepareTipoHoraExtra() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
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
                pstmt.setFloat(4, Float.parseFloat("0.5") );
                pstmt.executeUpdate();

                pstmt.setInt(1, 2);
                pstmt.setString(2, "H. E. 100%");
                pstmt.setBoolean(3, false);
                pstmt.setFloat(4, 1);
                pstmt.executeUpdate();

                pstmt.setInt(1, 2);
                pstmt.setString(2, "H. E. 50%");
                pstmt.setBoolean(3, true);
                pstmt.setFloat(4, Float.parseFloat("0.5") );
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
    
    public Boolean prepareRegime() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
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
                String sqlCargo = "INSERT INTO regime_horaextra (nome, feriadoCritico, modoTolerancia, tolerancia) VALUES (?,?,?)";
                pstmt = c.prepareStatement(sqlCargo);
                pstmt.setString(1, "CELETISTA");
                pstmt.setBoolean(2, true);
                pstmt.setInt(3, 1);
                pstmt.setInt(4, 10);
                pstmt.executeUpdate();
                
                sqlCargo = "INSERT INTO regime_horaextra (nome, feriadoCritico, modoTolerancia, tolerancia) VALUES (?,?,?)";
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

    public Boolean preparePerfil() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {

            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
            Statement stmt;
            ResultSet rs;
            String sql = "select * from perfil";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fazer = false;
            }
            if (fazer) {
                //Insere o primeiro Perfil
                String sqlPerfil = "INSERT INTO perfil (nome_perfil, cadastrosEConfiguracoes, permissoes, funcionarios, deptos, "
                        + " cargos, feriados, justificativas, tituloDoRelatorio, horaExtraEGratificacoes, consInd, freqnComEscala, "
                        + " freqnSemEscala, horaExtra, horCronoJorn, horarios, cronogramas, jornadas, relatorios, relatorioMensComEscala, "
                        + " relatorioMensSemEscala, relatorioDeResumoDeEscalas, relatorioDeResumoDeFrequencias, listaDePresenca, abonos, "
                        + " solicitacao, diasEmAberto, abonoRapido, historicoAbono, abonosEmMassa, exclusaoAbono, rankingAbono, "
                        + " afdt, acjef, espelhoDePonto, bancoDeDados, logs, relatorioConfiguracao, categoriaAfastamento, afastamento, "
                        + " empresas, verbas, cadastroDeFuncionarios, showResumo, relatorioCatracas, pesquisarData) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = c.prepareStatement(sqlPerfil);
                pstmt.setString(1, "AdminSPW");
                for (int i = 2; i <= 46; i++) {
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

    public Boolean prepareCargo() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
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

    public Boolean prepareDepartamento() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
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

    public Boolean prepareusuario() {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
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

    public Boolean preparaRepTipo() {
        System.out.println("repTIpo");
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {
            if (c.isClosed()) {
                Conectar();
            }
            //Verifica se ja foi feito
            Boolean fazer = true;
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
