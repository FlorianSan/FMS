/*
 * Class to manage and to get information about WayPoint And terminal Waypoint
 */
package parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get information about WayPoint And terminal Waypoint
 *
 * @author yoann
 */
public class WayPoint extends Balises {

    /**
     * WayPoint type
     */
    private String type;
    /**
     * Waypoint usage
     */
    private String useage;
    /**
     * Waypoint name Indicator
     */
    private String nameIndicator;

    /**
     * Waypoint constructor
     *
     * @param aeroport ICAO Airport Identifier
     * @param datum Datum Code
     * @param icaoAeroport Airport ICAO Code
     * @param icaoCode WayPoint ICAO Code
     * @param identifiant Identifier
     * @param latitude latitude
     * @param longitude longitude
     * @param magneticVariation Magnetic Variations
     * @param nom WayPoint Name
     * @param type Waypoint Type
     * @param useage Waypoint Usage
     * @param elevation WayPoint elevation
     * @param nameIndicator Name indicator
     */
    private WayPoint(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
            String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator, String nameIndicator, String type, String useage) {
        //En route  = aeroport = ENRT
        super(aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, firIdentifier, uirIdentifier, sEdate, sEIndicator);
        this.type = type;
        this.useage = useage;
        this.nameIndicator = nameIndicator;
    }

    /**
     * Create Waypoint
     *
     * @param identifiant WayPoint Identifier
     * @param icaoCode ICAO Code
     * @param aeroport ICAO Airport Identifier
     * @return WayPoint Object
     */
    public static WayPoint createWayPoint(String identifiant, String icaoCode, String aeroport) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from waypoint where identifiant like ? and icaoCode like ? and aeroport like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            pst.setString(3, aeroport);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new WayPoint(rs.getString("aeroport"), rs.getString("icaoAeroport"), rs.getString("datum"), rs.getString("icaoCode"), rs.getString("identifiant"), rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"), rs.getString("nom"),
                        rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("uirIdentifier"), rs.getString("sEdate"), rs.getString("sEIndicator"), rs.getString("nameIndicator"), rs.getString("type"), rs.getString("usage"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    /**
     * Compare two Waypoint
     *
     * @param o WayPoint
     * @return True or False
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof WayPoint && this.getIcaoCode().equals(((WayPoint) o).getIcaoCode()) && this.getIdentifiant().equals(((WayPoint) o)
                .getIdentifiant()));
    }

    /**
     * Hash Code
     *
     * @return Number
     */
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // any arbitrary constant will do 
    }

    /**
     * Get Waypoint type
     *
     * @return WayPoint Type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get WayPoint Usage
     *
     * @return WayPoint Usage
     */
    public String getuseage() {
        return this.useage;
    }

    /**
     * Get Name Indicator
     *
     * @return Name Indicator
     */
    public String getNameIndicator() {
        return this.nameIndicator;
    }

    /**
     * Set Waypoint Type
     *
     * @param type WayPoint Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set WayPoint Usage
     *
     * @param u WayPoint Usage
     */
    public void setUseage(String u) {
        this.useage = u;
    }

    /**
     * Set Name Indicator
     *
     * @param n Name Indicator
     */
    public void setNameIndicator(String n) {
        this.nameIndicator = n;
    }

    /**
     * Display waypoint information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherWayPoint(OutputStreamWriter fw) throws IOException {
        fw.write("Waypoint Type: " + this.type + "\n");
        fw.write("Waypoint Usage: " + this.useage + "\n");
        fw.write("Waypoint Name Format Indicator: " + this.nameIndicator + "\n");
    }

    /**
     * Add new Waypoint record in the SQl Database
     *
     * @param con Database connection
     * @param aeroport ICAO Airport Identifier
     * @param datum Datum Code
     * @param icaoAeroport Airport ICAO Code
     * @param icaoCode WayPoint ICAO Code
     * @param identifiant Identifier
     * @param latitude Latitude
     * @param longitude Longitude
     * @param magneticVariation Magnetic Variations
     * @param WayPointName WayPoint Name
     * @param elevation Elevation
     * @param type WayPoint Type
     * @param usage WayPoint Usage
     * @param nameIndicator Name Indicator
     */
    protected static void addSql(Connection con, String aeroport, String datum, String icaoAeroport, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String WayPointName, String elevation, String type, String usage, String nameIndicator) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO waypoint(icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, elevation, type, usage, nameIndicator) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiant);
            pst.setString(3, WayPointName);
            pst.setString(4, latitude);
            pst.setString(5, longitude);
            pst.setString(6, aeroport);
            pst.setString(7, icaoAeroport);
            pst.setString(8, magneticVariation);
            pst.setString(9, datum);
            pst.setString(10, elevation);
            pst.setString(11, type);
            pst.setString(12, usage);
            pst.setString(13, nameIndicator);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update the Waypoint record by adding continuation record
     *
     * @param con database connection
     * @param icaoCode WayPoint ICAO Code
     * @param identifiant Waypoint Identifier
     * @param firIdentifier FIR identifier
     * @param UirIdentifier UIR Identifier
     * @param sEIndicator Start/End Indicator
     * @param sEDate Start/End Date
     */
    protected static void addContinuationSql(Connection con, String icaoCode, String identifiant, String firIdentifier, String UirIdentifier, String sEIndicator, String sEDate) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE waypoint set firIdentifier=?, UirIdentifier=?, sEIndicator=?, sEdate=? WHERE icaoCode=? and identifiant=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, firIdentifier);
            pst.setString(2, UirIdentifier);
            pst.setString(3, sEIndicator);
            pst.setString(4, sEDate);
            pst.setString(5, icaoCode);
            pst.setString(6, identifiant);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Waypoint Type in WayPoint Record
     *
     * @param con database connection
     * @param id id of record
     * @param type Waypoint Type
     */
    protected static void modifySqlType(Connection con, int id, String type) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE waypoint set type=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, type);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Waypoint Usage in WayPoint Record
     *
     * @param con database connection
     * @param id id of record
     * @param useage Waypoint Usage
     */
    protected static void modifySqlUseage(Connection con, int id, String useage) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE waypoint set usage=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, useage);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Waypoint Name Indicator in WayPoint Record
     *
     * @param con database connection
     * @param id id of record
     * @param nameIndicator Waypoint Name Indicator
     */
    protected static void modifySqlNameIndicator(Connection con, int id, String nameIndicator) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE waypoint set nameIndicator=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, nameIndicator);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get all the name and icao Code of WayPoint
     *
     * @return Identifier + icaoCode
     */
    public static ArrayList<String[]> getWaypointTest() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String[]> a = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("select identifiant, icaoCode from waypoint");
            rs = pst.executeQuery();
            while (rs.next()) {
                String[] b = new String[2];
                b[0] = rs.getString("identifiant");
                b[1] = rs.getString("icaoCode");
                a.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return a;
    }

    /**
     * Get airport Identifier for terminal WayPoint or ENRT for Enroute WayPoint
     *
     * @param identifiant ICAO Airport Identifier
     * @param icaoCode ICAO Code
     * @return ICAO Airport Identifier or ENRT
     */
    public static String getAeroportWayPoint(String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT aeroport from waypoint where identifiant like ? and icaoCode like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("aeroport");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WayPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "ENRT";
    }
}
