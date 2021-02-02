/*
 * Class to manage and to get information about Tacan Markers
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
 * Class to manage and to get information about Tacan Markers
 * @author yoann
 */
public class Tacan extends Balises {
    
    /**
     * Frequency
     */
    private String frequence;
    /**
     * frequency protection
     */
    private String frequenceProtection;
    /**
     * Biais
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
     * Tacan Constructor
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport Airport ICAO Code
     * @param datum Datum Code
     * @param icaoCode ICAO Code
     * @param identifiant Tacan Identifier
     * @param latitude Latitude
     * @param longitude Longitude
     * @param magneticVariation Magnetic Variations
     * @param nom Name
     * @param elevation Elevation
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEdate Start/End Date
     * @param sEIndicator Start/End Indicator
     * @param frequence Frequency
     * @param frequenceProtection Frequency Protection
     * @param biais Biais
     * @param navaidMerit Navaid Merit
     * @param facilityCharacteristics  Facility Characteristics
     */
    private Tacan(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
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
     * Create TACAN
     * @param identifiant Tacan Identifier
     * @param icaoCode ICAO Code
     * @return TACAN Object
     */
    public static Tacan createTacan(String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from tacan where identifiant like ? and icaoCode like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Tacan(rs.getString("aeroport"), rs.getString("icaoAeroport"), rs.getString("datum"), rs.getString("icaoCode"), rs.getString("identifiant"), rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"), rs.getString("nom"),
                        rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("UirIdentifier"), rs.getString("sEdate"), rs.getString("sEIndicator"), rs.getString("frequence"),
                        rs.getString("frequenceProtection"), rs.getString("biais"), rs.getString("navaidMerit"), rs.getString("facilityCharacteristics"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    
    /**
     * Get Tacan Biais
     * @return Tacan Biais
     */
    public String getBiais(){
        return this.biais;
    }
    /**
     * Get Facility Characteristics
     * @return Facility Chracteristics
     */
    public String getFacilityCharacteristics(){
        return this.facilityCharacteristics;
    }
    /**
     * Get Frequency
     * @return Frequency
     */
    public String getFrequence(){
        return this.frequence;
    }
    /**
     * Get Frequence Protection
     * @return Frequence Protection
     */
    public String getFrequenceProtection(){
        return this.frequenceProtection;
    }
    /**
     * Get Navaid Merit
     * @return Navaid Merit 
     */
    public String getFrequenceNavaidMerit(){
        return this.navaidMerit;
    }
    
    /**
     * Display TACAN information in a file
     * @param fw
     * @throws IOException 
     */
   public void afficherTacan(OutputStreamWriter fw) throws IOException {
        fw.write("Frequency: " + this.frequence + "\n");
        fw.write("Frequency Protection: " + this.frequenceProtection + "\n");
        fw.write("Figure of Merit: " + this.navaidMerit + "\n");
        fw.write("Facility Characteristics: " + this.facilityCharacteristics + "\n");
        fw.write("Biais: " + this.biais + "\n");
    }
    
   /**
    * Add new TACAN record in the SQL database
    * @param con Database Connection
    * @param icaoCode ICAO Code
    * @param identifiant Tacan Identifier
    * @param nom Name
    * @param latitude Latitude
    * @param longitude Longitude
    * @param aeroport ICAO Airport Identifier
    * @param icaoAeroport Airport ICAO Code
    * @param magneticVariation Magnetic Variations
    * @param datum Datum Code
    * @param elevation Elevation
    * @param frequence Frequency
    * @param frequenceProtection Frequency Protection
    * @param biais Biais
    * @param navaidMerit Navaid Merit 
    */
    protected static void addSql(Connection con, String icaoCode, String identifiant, String nom, String latitude, String longitude,
            String aeroport, String icaoAeroport, String magneticVariation, String datum, String elevation, String frequence,
            String frequenceProtection, String biais, String navaidMerit) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO tacan(icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, elevation, frequence, frequenceProtection, biais, navaidMerit) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
    }
    
    /**
     * Update Frequency in Tacan Record
     * @param con Database connection
     * @param id Id of record
     * @param frequence  Frequency
     */
    protected static void modifySqlFrequence(Connection con, int id, String frequence) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE ils set frequence=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, frequence);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Update Navaid Merit in Tacan Record
     * @param con Database connection
     * @param id Id of record
     * @param merit Navaid Merit
     */
    protected static void modifySqlMerit(Connection con, int id, String merit) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE tacan set navaidMerit=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, merit);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Update Frequency Protection in Tacan Record
     * @param con Database connection
     * @param id Id of record
     * @param freqPro Frequence Protection
     */
    protected static void modifySqlFrequenceProtection(Connection con, int id, String freqPro) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE tacan set frequenceProtection=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, freqPro);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    /**
     * Update Biais in Tacan Record
     * @param con Database connection
     * @param id id of record
     * @param biais Biais
     */
    protected static void modifySqlBiais(Connection con, int id, String biais) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE tacan set biais=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, biais);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Tacan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
