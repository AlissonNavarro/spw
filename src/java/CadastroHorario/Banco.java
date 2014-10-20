/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHorario;

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
import java.text.SimpleDateFormat;
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

    public List<Horario> consultaHorarios() {

        List<Horario> horarioList = new ArrayList<Horario>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from schClass where active = 'true' ORDER BY schName";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer horario_id = rs.getInt("schClassid");
                String nome = rs.getString("schName");
                String legend = rs.getString("legend");
                Time entradaT = rs.getTime("startTime");
                String entrada = entradaT.toString().substring(0, entradaT.toString().length() - 3);
                Time saidaT = rs.getTime("endTime");
                String saida = saidaT.toString().substring(0, saidaT.toString().length() - 3);
                Boolean isEntradaObrigatoria = rs.getBoolean("checkin");
                Boolean isSaidaObrigatoria = rs.getBoolean("checkout");
                Time inicioFaixaEntradaT = rs.getTime("checkInTime1");
                String inicioFaixaEntrada = inicioFaixaEntradaT.toString().substring(0, inicioFaixaEntradaT.toString().length() - 3);
                Time fimFaixaEntradaT = rs.getTime("checkInTime2");
                String fimFaixaEntrada = fimFaixaEntradaT.toString().substring(0, fimFaixaEntradaT.toString().length() - 3);
                Time inicioFaixaSaidaT = rs.getTime("checkOutTime1");
                String inicioFaixaSaida = inicioFaixaSaidaT.toString().substring(0, inicioFaixaSaidaT.toString().length() - 3);
                Time fimFaixaSaidaT = rs.getTime("checkOutTime2");
                String fimFaixaSaida = fimFaixaSaidaT.toString().substring(0, fimFaixaSaidaT.toString().length() - 3);
                Time entradaDescanso1T = rs.getTime("restInTime1");
                String entradaDescanso1 = null;
                if (entradaDescanso1T != null) {
                    entradaDescanso1 = entradaDescanso1T.toString().substring(0, entradaDescanso1T.toString().length() - 3);
                }
                Time entradaDescanso2T = rs.getTime("restInTime2");
                String entradaDescanso2 = null;
                if (entradaDescanso2T != null) {
                    entradaDescanso2 = entradaDescanso2T.toString().substring(0, entradaDescanso2T.toString().length() - 3);
                }
                Time saidaDescanso1T = rs.getTime("restOutTime1");
                String saidaDescanso1 = null;
                if (saidaDescanso1T != null) {
                    saidaDescanso1 = saidaDescanso1T.toString().substring(0, saidaDescanso1T.toString().length() - 3);
                }
                Time saidaDescanso2T = rs.getTime("restOutTime2");
                String saidaDescanso2 = null;
                if (saidaDescanso2T != null) {
                    saidaDescanso2 = saidaDescanso2T.toString().substring(0, saidaDescanso2T.toString().length() - 3);
                }
                Integer toleranciaDesc = rs.getInt("restTolerance");
                Boolean ativo = rs.getBoolean("active");
                Boolean deduzirDescanso1 = rs.getBoolean("ReduceRestTime1");
                Boolean deduzirDescanso2 = rs.getBoolean("ReduceRestTime2");
                
                Time InterRestInT = rs.getTime("InterRestIn");
                String InterRestIn = null;
                if (InterRestInT != null) {
                    InterRestIn = InterRestInT.toString().substring(0, InterRestInT.toString().length() - 3);
                }
                Time InterRestOutT = rs.getTime("InterRestOut");
                String InterRestOut = null;
                if (InterRestOutT != null) {
                    InterRestOut = InterRestOutT.toString().substring(0, InterRestOutT.toString().length() - 3);
                }
                
                Boolean ReduceInterRest = rs.getBoolean("ReduceInterRest");
                Boolean CombineIn = rs.getBoolean("CombineIn");
                Boolean CombineOut = rs.getBoolean("CombineOut");

                Horario horario = new Horario(horario_id, nome, legend, entrada, saida, inicioFaixaEntrada,
                        fimFaixaEntrada, inicioFaixaSaida, fimFaixaSaida, isEntradaObrigatoria, isSaidaObrigatoria,
                        entradaDescanso1, saidaDescanso1, entradaDescanso2, saidaDescanso2, toleranciaDesc,
                        ativo, deduzirDescanso1, deduzirDescanso2, InterRestIn, InterRestOut, ReduceInterRest, CombineIn, CombineOut);

                horarioList.add(horario);
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
        return horarioList;
    }

    public Horario consultaHorario(Integer id) {

        Horario horario = new Horario();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from schClass "
                    + " where schClassid = " + id;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer horario_id = rs.getInt("schClassid");
                String nome = rs.getString("schName");
                String legend = rs.getString("legend");
                Time entradaT = rs.getTime("startTime");
                String entrada = entradaT.toString().substring(0, entradaT.toString().length() - 3);
                Time saidaT = rs.getTime("endTime");
                String saida = saidaT.toString().substring(0, saidaT.toString().length() - 3);
                Boolean isEntradaObrigatoria = rs.getBoolean("checkin");
                Boolean isSaidaObrigatoria = rs.getBoolean("checkout");
                Time inicioFaixaEntradaT = rs.getTime("checkInTime1");
                String inicioFaixaEntrada = inicioFaixaEntradaT.toString().substring(0, inicioFaixaEntradaT.toString().length() - 3);
                Time fimFaixaEntradaT = rs.getTime("checkInTime2");
                String fimFaixaEntrada = fimFaixaEntradaT.toString().substring(0, fimFaixaEntradaT.toString().length() - 3);
                Time inicioFaixaSaidaT = rs.getTime("checkOutTime1");
                String inicioFaixaSaida = inicioFaixaSaidaT.toString().substring(0, inicioFaixaSaidaT.toString().length() - 3);
                Time fimFaixaSaidaT = rs.getTime("checkOutTime2");
                String fimFaixaSaida = fimFaixaSaidaT.toString().substring(0, fimFaixaSaidaT.toString().length() - 3);
                Time entradaDescanso1T = rs.getTime("restInTime1");
                String entradaDescanso1 = null;
                if (entradaDescanso1T != null) {
                    entradaDescanso1 = entradaDescanso1T.toString().substring(0, entradaDescanso1T.toString().length() - 3);
                }
                Time entradaDescanso2T = rs.getTime("restInTime2");
                String entradaDescanso2 = null;
                if (entradaDescanso2T != null) {
                    entradaDescanso2 = entradaDescanso2T.toString().substring(0, entradaDescanso2T.toString().length() - 3);
                }
                Time saidaDescanso1T = rs.getTime("restOutTime1");
                String saidaDescanso1 = null;
                if (saidaDescanso1T != null) {
                    saidaDescanso1 = saidaDescanso1T.toString().substring(0, saidaDescanso1T.toString().length() - 3);
                }
                Time saidaDescanso2T = rs.getTime("restOutTime2");
                String saidaDescanso2 = null;
                if (saidaDescanso2T != null) {
                    saidaDescanso2 = saidaDescanso2T.toString().substring(0, saidaDescanso2T.toString().length() - 3);
                }
                Integer toleranciaDesc = rs.getInt("restTolerance");
                Boolean ativo = rs.getBoolean("active");
                Boolean deduzirDescanso1 = rs.getBoolean("ReduceRestTime1");
                Boolean deduzirDescanso2 = rs.getBoolean("ReduceRestTime2");
                
                Time InterRestInT = rs.getTime("InterRestIn");
                String InterRestIn = null;
                if (InterRestInT != null) {
                    InterRestIn = InterRestInT.toString().substring(0, InterRestInT.toString().length() - 3);
                }
                Time InterRestOutT = rs.getTime("InterRestOut");
                String InterRestOut = null;
                if (InterRestOutT != null) {
                    InterRestOut = InterRestOutT.toString().substring(0, InterRestOutT.toString().length() - 3);
                }
                
                Boolean ReduceInterRest = rs.getBoolean("ReduceInterRest");
                Boolean CombineIn = rs.getBoolean("CombineIn");
                Boolean CombineOut = rs.getBoolean("CombineOut");

                horario = new Horario(horario_id, nome, legend, entrada, saida, inicioFaixaEntrada,
                        fimFaixaEntrada, inicioFaixaSaida, fimFaixaSaida, isEntradaObrigatoria, isSaidaObrigatoria,
                        entradaDescanso1, saidaDescanso1, entradaDescanso2, saidaDescanso2, toleranciaDesc,
                        ativo, deduzirDescanso1, deduzirDescanso2, InterRestIn, InterRestOut, ReduceInterRest, CombineIn, CombineOut);

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
        return horario;
    }

    public int addHorario(Horario horario) {

        int flag = 0;
        PreparedStatement pstmt = null;
        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select schName from schClass "
                    + " where schName like '" + horario.getNome() + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "insert into schclass(schName, legend, startTime, endTime, "
                        + "checkin,checkout, checkInTime1, checkInTime2, checkOutTime1, checkOutTime2, "
                        + "restInTime1, restOutTime1, restInTime2, restOutTime2, restTolerance, "
                        + "ReduceRestTime1, ReduceRestTime2, InterRestIn, InterRestOut, ReduceInterRest, "
                        + "CombineIn, CombineOut) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, horario.getNome());
                if (horario.getLegenda().equals("")) {
                    pstmt.setString(2, null);
                } else {
                    pstmt.setString(2, horario.getLegenda());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                
                pstmt.setString(3, formatDate(sdf.format(horario.getEntrada())));
                pstmt.setString(4, formatDate(sdf.format(horario.getSaida())));
                pstmt.setBoolean(5, horario.getIsEntradaObrigatoria());
                pstmt.setBoolean(6, horario.getIsSaidaObrigatoria());
                pstmt.setString(7, formatDate(sdf.format(horario.getInicioFaixaEntrada())));
                pstmt.setString(8, formatDate(sdf.format(horario.getFimFaixaEntrada())));
                pstmt.setString(9, formatDate(sdf.format(horario.getInicioFaixaSaida())));
                pstmt.setString(10, formatDate(sdf.format(horario.getFimFaixaSaida())));
                if (horario.getEntradaDescanso1() == null || horario.getEntradaDescanso1().equals("")) {
                    pstmt.setString(11, null);
                } else {
                    pstmt.setString(11, formatDate(horario.getEntradaDescanso1()));
                }
                if (horario.getSaidaDescanso1() == null || horario.getSaidaDescanso1().equals("")) {
                    pstmt.setString(12, null);
                } else {
                    pstmt.setString(12, formatDate(horario.getSaidaDescanso1()));
                }
                if (horario.getEntradaDescanso2() == null || horario.getEntradaDescanso2().equals("")) {
                    pstmt.setString(13, null);
                } else {
                    pstmt.setString(13, formatDate(horario.getEntradaDescanso2()));
                }
                if (horario.getSaidaDescanso2() == null || horario.getSaidaDescanso2().equals("")) {
                    pstmt.setString(14, null);
                } else {
                    pstmt.setString(14, formatDate(horario.getSaidaDescanso2()));
                }
                
                if (horario.getToleranciaDesc() == null) {
                    pstmt.setInt(15, 0);
                } else {
                    pstmt.setInt(15, horario.getToleranciaDesc());
                }
                pstmt.setBoolean(16, horario.getDeduzirDescanso1());
                pstmt.setBoolean(17, horario.getDeduzirDescanso2());
                if (horario.getEntradaIntrajornada() == null || horario.getEntradaIntrajornada().equals("")) {
                    pstmt.setString(18, null);
                } else {
                    pstmt.setString(18, formatDate(horario.getEntradaIntrajornada()));
                }
                if (horario.getSaidaIntrajornada()== null || horario.getSaidaIntrajornada().equals("")) {
                    pstmt.setString(19, null);
                } else {
                    pstmt.setString(19, formatDate(horario.getSaidaIntrajornada()));
                }
                pstmt.setBoolean(20, horario.getDeduzirIntrajornada());
                pstmt.setBoolean(21, horario.getCombinarEntrada());
                pstmt.setBoolean(22, horario.getCombinarSaida());
                
                pstmt.executeUpdate();


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 2;
        } finally {
            try {
                if (c != null) {
                    if (flag != 1) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public String formatDate(String data) {
        String s = "30/12/1899 " + data + ":00";
        return s;
    }

    public int editHorario(Horario horario) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            String query = "update schclass set schName = ?, legend = ?,startTime = ?, "
                    + " endTime = ?,checkin = ?,checkout = ?, checkInTime1 = ?,"
                    + " checkInTime2 = ?, checkOutTime1 = ?, checkOutTime2= ?, "
                    + " restInTime1 = ?, restOutTime1 = ?, restInTime2 = ?, restOutTime2 = ?, "
                    + " restTolerance = ?, ReduceRestTime1 = ?, ReduceRestTime2 = ?, "
                    + " InterRestIn = ?, InterRestOut = ?, ReduceInterRest = ?, "
                    + " CombineIn = ?, CombineOut = ?"
                    + " where schclassid = ?";

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, horario.getNome());
            pstmt.setString(2, horario.getLegenda());
            pstmt.setString(3, formatDate(sdf.format(horario.getEntrada())));
            pstmt.setString(4, formatDate(sdf.format(horario.getSaida())));
            pstmt.setBoolean(5, horario.getIsEntradaObrigatoria());
            pstmt.setBoolean(6, horario.getIsSaidaObrigatoria());
            pstmt.setString(7, formatDate(sdf.format(horario.getInicioFaixaEntrada())));
            pstmt.setString(8, formatDate(sdf.format(horario.getFimFaixaEntrada())));
            pstmt.setString(9, formatDate(sdf.format(horario.getInicioFaixaSaida())));
            pstmt.setString(10, formatDate(sdf.format(horario.getFimFaixaSaida())));
            if (horario.getEntradaDescanso1() == null || horario.getEntradaDescanso1().equals("")) {
                pstmt.setString(11, null);
            } else {
                pstmt.setString(11, formatDate(horario.getEntradaDescanso1()));
            }
            if (horario.getSaidaDescanso1() == null || horario.getSaidaDescanso1().equals("")) {
                pstmt.setString(12, null);
            } else {
                pstmt.setString(12, formatDate(horario.getSaidaDescanso1()));
            }
            if (horario.getEntradaDescanso2() == null || horario.getEntradaDescanso2().equals("")) {
                pstmt.setString(13, null);
            } else {
                pstmt.setString(13, formatDate(horario.getEntradaDescanso2()));
            }
            if (horario.getSaidaDescanso2() == null || horario.getSaidaDescanso2().equals("")) {
                pstmt.setString(14, null);
            } else {
                pstmt.setString(14, formatDate(horario.getSaidaDescanso2()));
            }
            pstmt.setInt(15, horario.getToleranciaDesc());
            pstmt.setBoolean(16, horario.getDeduzirDescanso1());
            pstmt.setBoolean(17, horario.getDeduzirDescanso2());
            
            if (horario.getEntradaIntrajornada()== null || horario.getEntradaIntrajornada().equals("")) {
                pstmt.setString(18, null);
            } else {
                pstmt.setString(18, formatDate(horario.getEntradaIntrajornada()));
            }
            if (horario.getSaidaIntrajornada()== null || horario.getSaidaIntrajornada().equals("")) {
                pstmt.setString(19, null);
            } else {
                pstmt.setString(19, formatDate(horario.getSaidaIntrajornada()));
            }
            pstmt.setBoolean(20, horario.getDeduzirIntrajornada());
            pstmt.setBoolean(21, horario.getCombinarEntrada());
            pstmt.setBoolean(22, horario.getCombinarSaida());
            
            pstmt.setInt(23, horario.getHorario_id());
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 1;
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

    public boolean verificaHorario(Integer horario_id) {
        ResultSet rs;
        Statement stmt;
        Boolean flag = true;

        try {

            String queryVerify = "select * select count(*) "
                    + "from user_temp_sch uts, SchClass sc "
                    + "where uts.schclassid = sc.schclassid and "
                    + "uts.schclassid = " + horario_id;

            stmt = c.createStatement();
            rs = stmt.executeQuery(queryVerify);

            while (rs.next()) {
                System.out.println("Encontrou um Deslocamento vinculado");
                flag = false;
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;

        } finally {
            return flag;
        }
    }

    public boolean deleteHorario(Integer horario_id) {

        boolean flag = true;
        PreparedStatement pstmt = null;

        try {
            if (verificaHorario(horario_id)) {

                String query = "delete from schClass where schclassId = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, horario_id);

                pstmt.executeUpdate();

            } else {
                flag = false;
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

    public boolean desativarHorario(Integer horario_id) {

        boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            String query = "update schClass set active = 'false' "
                    + " where schclassId = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, horario_id);

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
}
