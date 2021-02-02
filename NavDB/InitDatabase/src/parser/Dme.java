/*
 * Class to manage and to get Dme Information
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
 * Class to manage and to get Dme Information
 *
 * @author yoann
 */
public class Dme extends Balises {

    /**
     * Dme Frequency
     */
    private String frequence;
    /**
     * Dme Frequence Protection
     */
    private String frequenceProtection;
    /**
     * Dme Biais
     */
    private String biais;
    /**
     * Navaid Merit
     */
    private String navaidMerit;
    /**
     * Facility Characteristics
     */
    private String facilityCharacteristics;

    /**
     * Dme Constructor
     *
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport ICAO Airport Code
     * @param datum Datum Code
     * @param icaoCode ICAO Code
     * @param identifiant
     * @param latitude Dme Latitude
     * @param longitude Dme Longitude
     * @param magneticVariation Magnetic Variations
     * @param nom Name
     * @param elevation Facility Elevation
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEdate Start End Date
     * @param sEIndicator Start End Indicator
     * @param frequence Dme Frequency
     * @param frequenceProtection Frequency Protection
     * @param biais Dme Biais
     * @param navaidMerit Navaid Merit
     * @param facilityCharacteristics Facility Characteristics
     */
    private Dme(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
            String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator, String frequence,
            String frequenceProtection, String biais, String navaidMerit, String facilityCharacteristics) {
        super(aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, firIdentifier, uirIdentifier, sEdate, sEIndicator);
        this.frequence = frequence;
        this.frequenceProtection = frequenceProtection;
        this.biais = biais;
        this.navaidMerit = navaidMerit;
        this.facilityCharacteristics = facilityCharacteristics;
    }

    /**
     * Create DME object
     *
     * @param identifiant DME Identifier
     * @param icaoCode ICAO Code
     * @return DME Object
     */
    public static Dme createDme(String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from dme where identifiant like ? and icaoCode like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Dme(rs.getString("aeroport"), rs.getString("icaoAeroport"), rs.getString("datum"), rs.getString("icaoCode"), rs.getString("identifiant"), rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"), rs.getString("nom"),
                        rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("UirIdentifier"), rs.getString("sEdate"), rs.getString("sEIndicator"), rs.getString("frequence"),
                        rs.getString("frequenceProtection"), rs.getString("biais"), rs.getString("navaidMerit"), rs.getString("facilityCharacteristics"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    /**
     * Get DME Biais
     *
     * @return DME Biais
     */
    public String getBiais() {
        return this.biais;
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
     * Get DME Frequency
     *
     * @return DME Frequency
     */
    public String getFrequence() {
        return this.frequence;
    }

    /**
     * Get Frequency Protection
     *
     * @return Frequency Protection
     */
    public String getFrequenceProtection() {
        return this.frequenceProtection;
    }

    /**
     * Get Navaid Merit
     *
     * @return Navaid Merit
     */
    public String getNavaidMerit() {
        return this.navaidMerit;
    }

    /**
     * Display DME Information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherDme(OutputStreamWriter fw) throws IOException {
        fw.write("Frequency: " + this.frequence + "\n");
        fw.write("Frequency Protection: " + this.frequenceProtection + "\n");
        fw.write("Figure of Merit: " + this.navaidMerit + "\n");
        fw.write("Facility Characteristics: " + this.facilityCharacteristics + "\n");
        fw.write("Biais: " + this.biais + "\n");
    }

    /**
     * Add new SQL DME record in the database
     *
     * @param con Database Connection
     * @param icaoCode ICAO Code
     * @param identifiant DME Identifier
     * @param nom DME Name
     * @param latitude Dme latitude
     * @param longitude Dme longitude
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport ICAO Airport Code
     * @param magneticVariation Magnetic Variations
     * @param datum Datum Code
     * @param elevation Facility Elevation
     * @param frequence Frequency
     * @param frequenceProtection Frequency Protection
     * @param biais Dme Biais
     * @param navaidMerit Navaid Merit
     */
    protected static void addSql(Connection con, String icaoCode, String identifiant, String nom, String latitude, String longitude,
            String aeroport, String icaoAeroport, String magneticVariation, String datum, String elevation, String frequence,
            String frequenceProtection, String biais, String navaidMerit) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO dme(icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, elevation, frequence, frequenceProtection, biais, navaidMerit) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiant);
            pst.setString(3, nom);
            pst.setString(4, latitude);
            pst.setString(5, longitude);
            pst.setString(6, aeroport);
            pst.setString(7, icaoAeroport);
            pst.setString(8, magneticVariation);
            pst.setString(9, datum);
            pst.setString(10, elevation);
            pst.setString(11, frequence);
            pst.setString(12, frequenceProtection);
            pst.setString(13, biais);
            pst.setString(14, navaidMerit);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


    }

    /**
     * Update Frequency in SQL Dme Record
     *
     * @param con DataBase Connection
     * @param id Id of Dme record
     * @param frequence Frequency
     */
    protected static void modifySqlFrequence(Connection con, int id, String frequence) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE dme set frequence=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, frequence);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Dme Biais in SQL Dme Record
     *
     * @param con DataBase Connection
     * @param id Id of Dme record
     * @param biais Dme Biais
     */
    protected static void modifySqlBiais(Connection con, int id, String biais) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE dme set biais=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, biais);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Navaid Merit in SQL Dme Record
     *
     * @param con Database Connection
     * @param id Id of the Dme Record
     * @param merit Navaid Merit
     */
    protected static void modifySqlMerit(Connection con, int id, String merit) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE dme set navaidMerit=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, merit);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Frequency Protection in SQL Dme Record
     *
     * @param con DataBase Connection
     * @param id Id of the Dme Record
     * @param freqPro Frequency Protection
     */
    protected static void modifySqlFrequenceProtection(Connection con, int id, String freqPro) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE dme set frequenceProtection=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, freqPro);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
