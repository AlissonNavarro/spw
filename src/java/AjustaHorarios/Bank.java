package AjustaHorarios;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
//import java.util.logging.Logger;

public class Bank implements Serializable {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        //String url = "jdbc:jtds:sqlserver://FS-DESEN-28:1904/SGN;user=user_web;password=123@net";
        try {
            String url = "jdbc:jtds:sqlserver://FS-DESEN-05:1904/huse2;user=user_web;password=123@net";
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("AjustaHorarios: Conectar: "+cnfe);
        }
    }

    public Bank() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("AjustaHorarios: Bank: "+ex);
            //Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<Horario> consultaHorariosDia() throws SQLException {
        ArrayList<Horario> horariosList = new ArrayList<Horario>();
        try {
            ResultSet rs = null;
            Statement stmt = null;
            String sql;
            sql = "SELECT     schClassid, schName, StartTime, EndTime, CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend FROM SchClass ";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {

                Integer horarioID = rs.getInt("schClassid");
                String schName = rs.getString("schName");
                Date StartTime = rs.getTimestamp("StartTime");
                Date EndTime = rs.getTimestamp("EndTime");
                Integer CheckIn = rs.getInt("CheckIn");
                Integer CheckOut = rs.getInt("CheckOut");
                Date CheckInTime1 = rs.getTimestamp("CheckInTime1");
                Date CheckInTime2 = rs.getTimestamp("CheckInTime2");
                Date CheckOutTime1 = rs.getTimestamp("CheckOutTime1");
                Date CheckOutTime2 = rs.getTimestamp("CheckOutTime2");
                String Legend = rs.getString("Legend");

                Horario horario = new Horario(schName, horarioID, StartTime, EndTime, CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend);
                horariosList.add(horario);
            }
            //public Horario(Integer horarioId, Date entrada, Date saida, Integer CheckIn, Integer CheckOut, Date inicioFaixaEntrada, Date fimFaixaEntrada, Date inicioFaixaSaida, Date fimFaixaSaida, String Legend)
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
        return horariosList;

    }

    public HashMap<Integer, Integer>  consultaQuantidadeJornada(List<Horario> horariosOriginais) {
        HashMap<Integer, Integer> mapaDaQuantidade = new HashMap<Integer, Integer>();
        for (int i = 0; i < horariosOriginais.size(); i++) {
            Horario horario = horariosOriginais.get(i);
            Integer index = horario.getHorarioId();
            Integer qnt = consultaQntNumDeil(index);
            mapaDaQuantidade.put(index, qnt);

        }

        return mapaDaQuantidade;
    }
    
    
    public HashMap<Integer, Integer>  consultaQuantidadeDesloc(List<Horario> horariosOriginais) {
        HashMap<Integer, Integer> mapaDaQuantidade = new HashMap<Integer, Integer>();
        for (int i = 0; i < horariosOriginais.size(); i++) {
            Horario horario = horariosOriginais.get(i);
            Integer index = horario.getHorarioId();
            Integer qnt = consultaQntDesloc(index);
            mapaDaQuantidade.put(index, qnt);
        }

        return mapaDaQuantidade;
    }


    public Integer consultaQntNumDeil(Integer id) {
        
        Integer qnt = 0;
        try {
            ResultSet rs;
            Statement stmt;
            String sql;
            sql = "SELECT     NUM_RUNID, STARTTIME, ENDTIME, SDAYS, EDAYS, SCHCLASSID, OverTime " +
                    "FROM         NUM_RUN_DEIL " +
                    "WHERE     (SCHCLASSID = "+id +")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                qnt++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } 
        return qnt;
    }
    public Integer consultaQntDesloc(Integer id) {

        Integer qnt = 0;
        try {
            ResultSet rs;
            Statement stmt;
            String sql;
            sql = "SELECT     USERID, COMETIME, LEAVETIME, OVERTIME, TYPE, FLAG, SCHCLASSID " +
                    "FROM         USER_TEMP_SCH " +
                    "WHERE     (SCHCLASSID = "+id +")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                qnt++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } 
        return qnt;
    }

    
    public List<Horario> adicionaHorarios(List<Horario> horariosList) {
        for (int i = 0; i < horariosList.size(); i++) {
            Horario horario = horariosList.get(i);
            addHorario(horario);
            Integer IdNovo = consultaMaiorId();
            horario.setHorarioId(IdNovo);
            horariosList.set(i, horario);
        }
        try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                System.out.println("Não adicionou");
            }
        return horariosList;
    }



    public Integer substituiHorarios(HashMap<Integer, Horario> mapaDeTransicao, List<Integer> listaDasChaves)  {
        try {
            c.setAutoCommit(false);
            for (int i = 0; i < listaDasChaves.size(); i++) {
                Integer index = listaDasChaves.get(i);
                editHorarioUserTempSch(index, mapaDeTransicao.get(index).getHorarioId());
            }
            c.commit();
            for (int i = 0; i < listaDasChaves.size(); i++) {
                Integer index = listaDasChaves.get(i);
                Horario horarioNovo = consultaHorario(index);
                editHorarioNumRunDeail(index,horarioNovo.getEntrada(),horarioNovo.getSaida(), mapaDeTransicao.get(index).getHorarioId());
            
            }
            c.commit();
            for (int i = 0; i < listaDasChaves.size(); i++) {
                Integer index = listaDasChaves.get(i);
                deleteHorario(index);
            }
            c.commit();
            c.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                System.out.println("não consseguiu dar rollback");
            }
            System.out.println("Connection rollback...");
            System.out.println(e.getMessage());
        }
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            //c.rollback();
            System.out.println("Erro: Não conseguiu fechar a conexão");
        }
        return 0;
    }

    public Integer adicionaHorarios(HashMap<Integer, Horario> mapaDeTransicao, List<Integer> listaDasChaves) {
        Integer maior = consultaMaiorId();
        for (int i = 0; i < listaDasChaves.size(); i++) {
            Integer index = listaDasChaves.get(i);
            Horario horario = mapaDeTransicao.get(index);
            addHorario(horario);
            Integer IdNovo = consultaMaiorId();
            horario.setHorarioId(IdNovo);
            mapaDeTransicao.put(index, horario);
        }
        return maior;
    }
    

    public int addHorario(Horario horario) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            String query = "insert into schclass(schName, legend,startTime,endTime,checkin,checkout, checkInTime1, checkInTime2, checkOutTime1, "
                    + "checkOutTime2) values(?,?,?,?,?,?,?,?,?,?)";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, horario.getNome());
            if (horario.getLegend().equals("")) {
                pstmt.setString(2, null);
            } else {
                pstmt.setString(2, horario.getLegend());
            }
            pstmt.setString(3, formatDate(horario.getEntrada()));
            pstmt.setString(4, formatDate(horario.getSaida()));
            pstmt.setInt(5, horario.getCheckIn());
            pstmt.setInt(6, horario.getCheckOut());
            pstmt.setString(7, formatDate(horario.getInicioFaixaEntrada()));
            pstmt.setString(8, formatDate(horario.getFimFaixaEntrada()));
            pstmt.setString(9, formatDate(horario.getInicioFaixaSaida()));
            pstmt.setString(10, formatDate(horario.getFimFaixaSaida()));
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 2;
        }
        return flag;
    }

    /*public void corrigeUserTempSch(HashMap<Integer, Horario> mapaDeTransicao, List<Integer> listaDasChaves) {
    }

     * 
     */
    public int editHorarioUserTempSch(Integer horarioAntigo, Integer horarioNovo) throws SQLException {

        int flag = 0;
        PreparedStatement pstmt = null;
        String query = "UPDATE    USER_TEMP_SCH SET SCHCLASSID = ? WHERE (SCHCLASSID = ?)";
        pstmt = c.prepareStatement(query);
        pstmt.setInt(1, horarioNovo);
        pstmt.setInt(2, horarioAntigo);
        pstmt.executeUpdate();
        System.out.println("temp "+horarioAntigo+" -> "+horarioNovo);
        return flag;

    }

    public int editHorarioNumRunDeail(Integer horarioAntigo,  Date startDateNovo, Date endDateNovo, Integer horarioNovo) throws SQLException {

        int flag = 0;
        PreparedStatement pstmt = null;
        String query = "UPDATE  num_run_deil SET SCHCLASSID = ?,STARTTIME = ?, ENDTIME = ? WHERE (SCHCLASSID = ?)";
        pstmt = c.prepareStatement(query);
        pstmt.setInt(1, horarioNovo);
        pstmt.setString(2, formatDate(startDateNovo));
        pstmt.setString(3, formatDate(endDateNovo));
        pstmt.setInt(4, horarioAntigo);
        pstmt.executeUpdate();
        System.out.println("deail "+horarioAntigo+" -> "+horarioNovo);
        return flag;
    }
/*
    public int editStartDateEEndDateHorarioNumRunDeail(Integer horarioAntigo, Date startDateNovo, Date endDateNovo) throws SQLException {

        int flag = 0;
        PreparedStatement pstmt = null;
        String query = "UPDATE  num_run_deil SET  STARTTIME = ?, ENDTIME = ? WHERE (SCHCLASSID = ?)";
        pstmt = c.prepareStatement(query);
        pstmt.setString(1, formatDate(startDateNovo));
        pstmt.setString(2, formatDate(endDateNovo));
        pstmt.executeUpdate();
        
        return flag;
    }
 *
 */
    public Horario consultaHorario(Integer id) throws SQLException {
        Horario horario = new Horario();
            ResultSet rs = null;
            Statement stmt = null;
            String sql;
            sql = "SELECT     schClassid, schName, StartTime, EndTime, CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend FROM SchClass where schClassid = "+id;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {

                Integer horarioID = rs.getInt("schClassid");
                String schName = rs.getString("schName");
                Date StartTime = rs.getTimestamp("StartTime");
                Date EndTime = rs.getTimestamp("EndTime");
                Integer CheckIn = rs.getInt("CheckIn");
                Integer CheckOut = rs.getInt("CheckOut");
                Date CheckInTime1 = rs.getTimestamp("CheckInTime1");
                Date CheckInTime2 = rs.getTimestamp("CheckInTime2");
                Date CheckOutTime1 = rs.getTimestamp("CheckOutTime1");
                Date CheckOutTime2 = rs.getTimestamp("CheckOutTime2");
                String Legend = rs.getString("Legend");

                horario = new Horario(schName, horarioID, StartTime, EndTime, CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend);


            //public Horario(Integer horarioId, Date entrada, Date saida, Integer CheckIn, Integer CheckOut, Date inicioFaixaEntrada, Date fimFaixaEntrada, Date inicioFaixaSaida, Date fimFaixaSaida, String Legend)

        }
        return horario;

    }

    public int editHorario(Horario horario) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            String query = "update schclass set schName = ?, legend = ?,startTime = ?, endTime = ?,checkin = ?,checkout = ?, checkInTime1 = ?,"
                    + " checkInTime2 = ?, checkOutTime1 = ?, checkOutTime2= ? "
                    + " where schclassid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, horario.getNome());
            pstmt.setString(2, horario.getLegend());
            pstmt.setString(3, formatDate(horario.getEntrada()));
            pstmt.setString(4, formatDate(horario.getSaida()));
            pstmt.setInt(5, horario.getCheckIn());
            pstmt.setInt(6, horario.getCheckOut());
            pstmt.setString(7, formatDate(horario.getInicioFaixaEntrada()));
            pstmt.setString(8, formatDate(horario.getFimFaixaEntrada()));
            pstmt.setString(9, formatDate(horario.getInicioFaixaSaida()));
            pstmt.setString(10, formatDate(horario.getFimFaixaSaida()));
            pstmt.setInt(11, horario.getHorarioId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 1;
        }
        return flag;
    }

    public boolean deleteHorario(Integer horario_id) throws SQLException {

        boolean flag = true;
        PreparedStatement pstmt = null;



        String query = "delete from schClass"
                + " where schclassId = ?";

        pstmt = c.prepareStatement(query);
        pstmt.setInt(1, horario_id);

        pstmt.executeUpdate();


        return flag;
    }

    public Integer consultaMaiorId() {
        /*
         * SELECT     MAX(schClassid) AS maximo FROM         SchClass
         */
        Integer max = -1;
        try {
            ResultSet rs = null;
            Statement stmt = null;
            String sql;
            sql = "SELECT MAX(schClassid) AS maximo FROM         SchClass";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //
                max = rs.getInt("maximo");
            }
            //public Horario(Integer horarioId, Date entrada, Date saida, Integer CheckIn, Integer CheckOut, Date inicioFaixaEntrada, Date fimFaixaEntrada, Date inicioFaixaSaida, Date fimFaixaSaida, String Legend)
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return max;
    }

    public String formatDate(Date data) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        String s = "30/12/1899 " + sf.format(data) + ":00";
        return s;
    }


}
