/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;

/**
 * Class to manage NDB
 * @author edouard.ladeira
 */
public class Ndb {

    /**
     * Ndb propreties
     */
    

    /**
     * Create SQL Connection
     * @return SQL connection
     */
    public static Connection creatSqlConnect() {
        String url = "jdbc:postgresql://localhost:5432/navdb";
        String user = "user_nd";
        String password = "nd";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            //System.out.println("Connection Failed!" + e);
            return null;
        }
    }

    /**
     * Check data existence in Ndb
     * @param data Ndb data to check
     * @param table Ndb table where to search
     * @param column Ndb column where to try to find data
     * @return boolean
     */
    public static boolean checkExist(String data, String table, String column) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        
        /** 
         * Create SQL Connection and format the SQL query
         */
        try {
            con = Ndb.creatSqlConnect();
            pst = con.prepareStatement("SELECT * from "+table+" where "+column+" = ?");
            pst.setString(1, data);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                result = true; //(rs.getInt(1) >= 1);
            }
        } catch (SQLException ex) {
            //System.out.println(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
        }
        return result;
    }

    /**
     * Check Waypoint existence in Airway with Ndb
     *
     * @param awyId Airway identifier
     * @param wptId Waypoint identifier
     * @return boolean
     */
    public static boolean checkWptInAwy(String awyId, String wptId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        
        /** 
         * Create SQL Connection and format the SQL query
         */
        if (!awyId.equals("DIRECT")) {
            try {
                con = Ndb.creatSqlConnect();
                pst = con.prepareStatement("SELECT * from route where routeidentifiant=? and fixidentifiant=?");
                pst.setString(1, awyId);
                pst.setString(2, wptId);
                rs = pst.executeQuery();

                if (rs != null && rs.next()) {
                    result = true; //(rs.getInt(1) >= 1);
                }
            } catch (SQLException ex) {
                //System.out.println(ex);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        //System.out.println(ex);
                    }
                }
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (SQLException ex) {
                        //System.out.println(ex);
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        //System.out.println(ex);
                    }
                }
            }
        } else {
            result = true;
        }
        return result;
    }
    
    /**
     * Search possible airways between two waypoints 
     * @param wptId1
     * @param wptId2
     * @return list of possible airways (if any airway exist return the list ["DIRECT"])
     */
    public static ArrayList<String> searchReachableAwy(String wptId1, String wptId2) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<>();
        int cpt = 0;
        
        /** 
         * Create SQL Connection and format the SQL query
         */
        try {
            con = Ndb.creatSqlConnect();
            pst = con.prepareStatement("select routeidentifiant from route where routeidentifiant in (select routeidentifiant from route where fixidentifiant=?) and fixidentifiant=?");
            pst.setString(1, wptId1);
            pst.setString(2, wptId2);
            rs = pst.executeQuery();

            while (rs != null && rs.next()) {
                result.add(rs.getString("routeidentifiant")); //request result element is added to ou list
                cpt += 1;
            }
            
            if (cpt == 0) {
                result.add("DIRECT");
            }
            
        } catch (SQLException ex) {
            //System.out.println(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
        }
        return result;
    }
    
    /**
     * Search possible waypoint  between two waypoints to reach the route
     * @param wptDepId
     * @param wptArrId
     * @param awyId
     * @return list of possible waypoints
     */
    public static ArrayList<String> searchReachableWpt(String wptDepId, String wptArrId, String awyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<>();
        String seqNbWptDep = "";
        String seqNbWptArr = "";
        
        /** 
         * Create SQL Connection and format the SQL query
         */
        try {
            con = Ndb.creatSqlConnect();    
            
            //Get the sequence number of wptDepId
            pst = con.prepareStatement("select sequencenumber from route where routeidentifiant=? and fixidentifiant=?");
            pst.setString(1, awyId);
            pst.setString(2, wptDepId);
            rs = pst.executeQuery();

            if (rs != null && rs.next()) {
                seqNbWptDep = rs.getString("sequencenumber");
            }
            
            //Get the sequence number of wptArrId
            pst = con.prepareStatement("select sequencenumber from route where routeidentifiant=? and fixidentifiant=?");
            pst.setString(1, awyId);
            pst.setString(2, wptArrId);
            rs = pst.executeQuery();

            if (rs != null && rs.next()) {
                seqNbWptArr = rs.getString("sequencenumber");
            }
            
            //Get the list of waypoint with taking into account the flight direction
            if (Integer.valueOf(seqNbWptDep)<Integer.valueOf(seqNbWptArr)) {
                pst = con.prepareStatement("select sequencenumber, fixidentifiant from route where (routeidentifiant=?) and (sequencenumber between ? and ?) and (sequencenumber!=?) order by sequencenumber");
                pst.setString(1, awyId);
                pst.setString(2, seqNbWptDep);
                pst.setString(3, seqNbWptArr);
                pst.setString(4,seqNbWptDep);
            }
            else {
                pst = con.prepareStatement("select sequencenumber, fixidentifiant from route where (routeidentifiant=?) and (sequencenumber between ? and ?) and (sequencenumber!=?) order by sequencenumber desc");
                pst.setString(1, awyId);
                pst.setString(2, seqNbWptArr);
                pst.setString(3, seqNbWptDep);
                pst.setString(4,seqNbWptDep);
            }
            rs = pst.executeQuery();
            
            while (rs != null && rs.next()) {
                result.add(rs.getString("fixidentifiant")); //request result element is added to ou list
            }
            
        } catch (SQLException ex) {
            //System.out.println(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
        }
        return result;
    }
    
    /**
     * Search possible waypoints to replace the current waypoint of a section
     * @param refWptfId
     * @param currentWptId
     * @param awyId
     * @return list of possible waypoints
     */
    public static ArrayList<String> searchPossibleWpt(String refWptfId, String currentWptId, String awyId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<>();
        String seqNbRefWpt = "";
        String seqNbCurrentWpt = "";
        
        /** 
         * Create SQL Connection and format the SQL query
         */
        try {
            con = Ndb.creatSqlConnect();    
            
            //Get the sequence number of wptDepId
            pst = con.prepareStatement("select sequencenumber from route where routeidentifiant=? and fixidentifiant=?");
            pst.setString(1, awyId);
            pst.setString(2, refWptfId);
            rs = pst.executeQuery();

            if (rs != null && rs.next()) {
                seqNbRefWpt = rs.getString("sequencenumber");
            }
            
            //Get the sequence number of wptArrId
            pst = con.prepareStatement("select sequencenumber from route where routeidentifiant=? and fixidentifiant=?");
            pst.setString(1, awyId);
            pst.setString(2, currentWptId);
            rs = pst.executeQuery();

            if (rs != null && rs.next()) {
                seqNbCurrentWpt = rs.getString("sequencenumber");
            }
            
            //Get the list of waypoint with taking into account the flight direction
            if (Integer.valueOf(seqNbRefWpt)<Integer.valueOf(seqNbCurrentWpt)) {
                pst = con.prepareStatement("select sequencenumber, fixidentifiant from route where routeidentifiant=? and sequencenumber>? and sequencenumber!=? order by sequencenumber");
                pst.setString(1, awyId);
                pst.setString(2, seqNbRefWpt);
                pst.setString(3, seqNbCurrentWpt);
            }
            else {
                pst = con.prepareStatement("select sequencenumber, fixidentifiant from route where routeidentifiant=? and sequencenumber<? and sequencenumber!=? order by sequencenumber desc");
                pst.setString(1, awyId);
                pst.setString(2, seqNbRefWpt);
                pst.setString(3, seqNbCurrentWpt);
            }
            rs = pst.executeQuery();
            
            while (rs != null && rs.next()) {
                result.add(rs.getString("fixidentifiant")); //request result element is added to ou list
            }
            
        } catch (SQLException ex) {
            //System.out.println(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    //System.out.println(ex);
                }
            }
        }
        return result;
    }
}

