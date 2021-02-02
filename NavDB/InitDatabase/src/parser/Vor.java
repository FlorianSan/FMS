package parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Class to manage and to get information about Vor
 */
/**
 * Class to manage and to get information about Vor
 *
 * @author yoann
 */
public class Vor extends Balises {

    /**
     * Frequency
     */
    private String frequence;
    /**
     * Facility Characteristics
     */
    private String facilityCharacteristics;
    /**
     * Frequency Protection
     */
    private String frequenceProtection;
    /**
     * Navaid Merit
     */
    private String navaidMerit;

    /**
     * Vor Constructor
     *
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport Airport ICAO Code
     * @param datum Datum Code
     * @param icaoCode ICAO Code
     * @param identifiant Vor Identifier
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
     * @param facilityCharacteristics Facility Characteristics
     * @param frequenceProtection Frequence Protection
     * @param navaidMerit Navaid Merit
     */
    private Vor(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
            String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator,
            String frequence, String facilityCharacteristics, String frequenceProtection, String navaidMerit) {

        super(aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, firIdentifier, uirIdentifier, sEdate, sEIndicator);
        this.frequence = frequence;
        this.facilityCharacteristics = facilityCharacteristics;
        this.frequenceProtection = frequenceProtection;
        this.navaidMerit = navaidMerit;


    }

    /**
     * Create VOR object
     *
     * @param identifiant Vor Identifier
     * @param icaoCode ICAO Code
     * @return VOR Object
     */
    public static Vor createVor(String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from vor where identifiant like ? and icaoCode like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Vor(rs.getString("aeroport"), rs.getString("icaoAeroport"), rs.getString("datum"), rs.getString("icaoCode"), rs.getString("identifiant"),
                        rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"), rs.getString("nom"),
                        rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("UirIdentifier"), rs.getString("sEdate"),
                        rs.getString("sEIndicator"), rs.getString("frequence"), rs.getString("facilityCharacteristics"),
                        rs.getString("frequenceProtection"), rs.getString("navaidMerit"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    /**
     * Get Vor frequency
     *
     * @return Vor frequency
     */
    public String getFrequence() {
        return this.frequence;
    }

    /**
     * Get Facility Characteristics
     *
     * @return Facility Characteristics
     */
    public String getFacilityCharacteristic() {
        return this.facilityCharacteristics;
    }

    /**
     * Get Frequence Protection
     *
     * @return Frequence Protection
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
     * Display Vor information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherVor(OutputStreamWriter fw) throws IOException {
        fw.write("Frequency: " + this.frequence + "\n");
        fw.write("Frequency Protection: " + this.frequenceProtection + "\n");
        fw.write("Figure of Merit: " + this.navaidMerit + "\n");
        fw.write("Facility Characteristics: " + this.facilityCharacteristics + "\n");
    }

    /**
     * Get type Balise
     *
     * @return vor
     */
    @Override
    public String getTypeBalise() {
        return "vor";
    }

    /**
     * Add new VOR record in the SQl database
     *
     * @param con Database connection
     * @param icaoCode ICAO Code
     * @param identifiant Vor identifier
     * @param nom Name
     * @param latitude Latitude
     * @param longitude Longitude
     * @param aeroport ICAO Airport identifier
     * @param icaoAeroport Airport ICAO Code
     * @param magneticVariation Magnetic Variations
     * @param datum Datum Code
     * @param frequence Frequency
     * @param frequenceProtection Frequency Protection
     * @param navaidMerit Navaid Merit
     */
    protected static void addSql(Connection con, String icaoCode, String identifiant, String nom, String latitude, String longitude,
            String aeroport, String icaoAeroport, String magneticVariation, String datum, String frequence,
            String frequenceProtection, String navaidMerit) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO vor(icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, frequence, frequenceProtection, navaidMerit) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            pst.setString(10, frequence);
            pst.setString(11, frequenceProtection);
            pst.setString(12, navaidMerit);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


    }

    /**
     * Update Frequency in VOR Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param frequence Frequency
     */
    protected static void modifySqlFrequence(Connection con, int id, String frequence) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vor set frequence=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, frequence);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Navaid Merit in Vor Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param merit Navaid Merit
     */
    protected static void modifySqlMerit(Connection con, int id, String merit) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vor set navaidMerit=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, merit);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Frequency Protection in VOR Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param freqPro Frequence Protection
     */
    protected static void modifySqlFrequenceProtection(Connection con, int id, String freqPro) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vor set frequenceProtection=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, freqPro);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Vor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
