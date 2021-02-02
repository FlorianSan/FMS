/*
 * Class to manage and to get information about NDB
 */
package parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get information about NDB
 *
 * @author yoann
 */
public class Ndb extends Balises {

    /**
     * Frequency NDB
     */
    private String frequence;
    /**
     * Class of NDB
     */
    private String classNdb;
    /**
     * Facility Characteristics
     */
    private String facilityCharacteristics;

    /**
     * Ndb Constructor
     *
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport Airport ICAO Code
     * @param datum Datum Code
     * @param icaoCode ICAO Code
     * @param identifiant Identifier
     * @param latitude Latitude
     * @param longitude Longitude
     * @param magneticVariation Magnetic Variation
     * @param nom Name
     * @param elevation Elevation
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEdate Start/End Date
     * @param sEIndicator Start/End Indicator
     * @param frequence Frequency
     * @param classNdb Class NDB
     * @param facilityCharacteristics Facility Characteristics
     */
    private Ndb(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
            String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator, String frequence, String classNdb, String facilityCharacteristics) {
        super(aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, firIdentifier, uirIdentifier, sEdate, sEIndicator);
        this.frequence = frequence;
        this.classNdb = classNdb;
        this.facilityCharacteristics = facilityCharacteristics;
    }

    /**
     * Create NDB
     *
     * @param identifiant Identifier
     * @param icaoCode ICAO Code
     * @return NDB Object
     */
    public static Ndb createNdb(String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from ndb where identifiant like ? and icaoCode like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Ndb(rs.getString("aeroport"), rs.getString("icaoAeroport"), rs.getString("datum"), rs.getString("icaoCode"),
                        rs.getString("identifiant"), rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"),
                        rs.getString("nom"), rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("UirIdentifier"),
                        rs.getString("sEdate"), rs.getString("SEIndicator"), rs.getString("frequence"), rs.getString("classNdb"), rs.getString("facilityCharacteristics"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }



        return null;
    }

    /**
     * Check the equality between 2 objets
     *
     * @param o Object
     * @return True or False
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Ndb && this.getIcaoCode().equals(((Ndb) o).getIcaoCode()) && this.getIdentifiant().equals(((Ndb) o)
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
     * Get Class NDB
     *
     * @return Class NDB
     */
    public String getClassNdb() {
        return this.classNdb;
    }

    /**
     * Get Facility Characteristics
     *
     * @return Facility Characteristics
     */
    public String getFacilityCharacteristics() {
        return this.facilityCharacteristics;
    }

    /**
     * Get frequency
     *
     * @return frequency
     */
    public String getFrequence() {
        return this.frequence;
    }

    /**
     * Set Facility Characteristics
     *
     * @param facChar Facility Characteristics
     */
    public void setFacilityCharacteristics(String facChar) {
        this.facilityCharacteristics = facChar;
    }

    /**
     * Set Frequency
     *
     * @param freq Frequency
     */
    public void setFrequence(String freq) {
        this.frequence = freq;
    }

    /**
     * Set Class NDB
     *
     * @param ndbClass Class NDB
     */
    public void setClass(String ndbClass) {
        this.classNdb = ndbClass;
    }

    /**
     * Display NDB information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherNdb(OutputStreamWriter fw) throws IOException {
        fw.write("NDB Frequency: " + this.frequence + "\n");
        fw.write("NDB Class: " + this.classNdb + "\n");
        fw.write("Facility Characteristics: " + this.facilityCharacteristics + "\n");
    }

    /**
     * Add new Ndb record in the SQL database
     *
     * @param con Database Connection
     * @param aeroport ICAO Airport Identifier
     * @param classNdb NDB Class
     * @param datum Datum Code
     * @param frequence Frequency
     * @param icaoAeroport ICAO Code of Airport
     * @param icaoCode ICAO Code
     * @param identifiant Identifier
     * @param latitude Latitude
     * @param longitude Longitude
     * @param magneticVariation Magnetic Variation
     * @param ndbName NDB Name
     */
    protected static void addSql(Connection con, String aeroport, String classNdb, String datum, String frequence, String icaoAeroport, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String ndbName) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO ndb(icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, frequence, classNdb) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiant);
            pst.setString(3, ndbName);
            pst.setString(4, latitude);
            pst.setString(5, longitude);
            pst.setString(6, aeroport);
            pst.setString(7, icaoAeroport);
            pst.setString(8, magneticVariation);
            pst.setString(9, datum);
            pst.setString(10, frequence);
            pst.setString(11, classNdb);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update NDB record in the SQL Database
     *
     * @param con Database Connection
     * @param icaoCode ICAO Code
     * @param identifiant Identifier
     * @param firIdentifier FIR Identifier
     * @param UirIdentifier UIR Identifier
     * @param sEIndicator Start/End Indicator
     * @param sEDate Start/End Date
     */
    protected static void addContinuationSql(Connection con, String icaoCode, String identifiant, String firIdentifier, String UirIdentifier, String sEIndicator, String sEDate) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE ndb set firIdentifier=?, UirIdentifier=?, sEIndicator=?, sEdate=? WHERE icaoCode=? and identifiant=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, firIdentifier);
            pst.setString(2, UirIdentifier);
            pst.setString(3, sEIndicator);
            pst.setString(4, sEDate);
            pst.setString(5, icaoCode);
            pst.setString(6, identifiant);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update frequency in a NDB record
     *
     * @param con DataBase connection
     * @param id Id of the record
     * @param frequence frequency
     */
    protected static void modifySqlFrequence(Connection con, int id, String frequence) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE ndb set frequence=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, frequence);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Class NDB in a NDB record
     *
     * @param con Database Connection
     * @param id Id of the record
     * @param ndbClass NDB Class
     */
    protected static void modifySqlClass(Connection con, int id, String ndbClass) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE dme set classNdb=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, ndbClass);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get the identifier of a navaid thanks to the frequency
     *
     * @param latitude Latitude in degrees
     * @param longitude Longitude in degrees
     * @param frequence Frequency of Navaid (format 450)
     * @return Identifier or frequency in case of failure
     */
    public static String getIdentifierWithFrequence(double latitude, double longitude, String frequence) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int freq = Integer.parseInt(frequence);
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("select identifiant, distance(?, ?, latitude, longitude) as distance from ndb where cast(frequence as integer) = ?  order by distance");
            pst.setDouble(1, latitude);
            pst.setDouble(2, longitude);
            pst.setInt(3, freq);

            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("identifiant");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return frequence;
    }
}
