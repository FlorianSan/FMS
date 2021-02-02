/*
 * Class to manage and to get information about VorDme beacon
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
 * Class to manage and to get information about VorDme beacon
 *
 * @author yoann
 */
public class VorDme extends Balises {

    /**
     * Dme Identifier
     */
    private String identifiantDme;
    /**
     * Dme Latitude
     */
    private String latitudeDme;
    /**
     * Dme Longitude
     */
    private String longitudeDme;
    /**
     * frequency
     */
    private String frequence;
    /**
     * Frequency protection
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
     * VorDme Constructor
     *
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport ICAO Code
     * @param datum Datum Code
     * @param icaoCode ICAO Code
     * @param identifiant VOR Identifier
     * @param latitude Vor Latitude
     * @param longitude Vor Longitude
     * @param magneticVariation Magnetic Variations
     * @param nom Name
     * @param elevation Elevation
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEdate Start/End Date
     * @param sEIndicator Start/End Indicator
     * @param identifiantDme Dme Identifier
     * @param latitudeDme Dme Latitude
     * @param longitudeDme Dme Longitude
     * @param frequence Frequency
     * @param frequenceProtection Frequency protection
     * @param biais Biais
     * @param navaidMerit Navaid Merit
     * @param facilityCharacteristics Facility Characteristics
     */
    private VorDme(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
            String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator,
            String identifiantDme, String latitudeDme, String longitudeDme, String frequence, String frequenceProtection,
            String biais, String navaidMerit, String facilityCharacteristics) {

        super(aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, firIdentifier, uirIdentifier, sEdate, sEIndicator);
        this.identifiantDme = identifiantDme;
        this.latitudeDme = latitudeDme;
        this.longitudeDme = longitudeDme;
        this.frequence = frequence;
        this.frequenceProtection = frequenceProtection;
        this.biais = biais;
        this.navaidMerit = navaidMerit;
        this.facilityCharacteristics = facilityCharacteristics;

    }

    /**
     * Create VOR/DME object
     *
     * @param identifiant VOR/DME Identifier
     * @param icaoCode ICAO Code
     * @return Vor/Dme Object
     */
    public static VorDme createVorDme(String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from vordme where identifiant like ? and icaoCode like ?");
            pst.setString(1, identifiant);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new VorDme(rs.getString("aeroport"), rs.getString("icaoCode"), rs.getString("datum"), rs.getString("icaoCode"), rs.getString("identifiant"), rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"), rs.getString("nom"),
                        rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("UirIdentifier"), rs.getString("sEdate"), rs.getString("sEIndicator"),
                        rs.getString("identifiantDme"), rs.getString("latitudeDme"), rs.getString("longitudeDme"), rs.getString("frequence"), rs.getString("frequenceProtection"),
                        rs.getString("biais"), rs.getString("navaidMerit"), rs.getString("facilityCharacteristics"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    /**
     * Get Dme Identifier
     *
     * @return Dme Identifier
     */
    public String getIdentifiantDme() {
        return this.identifiantDme;
    }

    /**
     * Get Dme Latitude
     *
     * @return Dme latitude
     */
    public String getLatitudeDme() {
        return this.latitudeDme;
    }

    /**
     * Get Dme Longitude
     *
     * @return Dme Longitude
     */
    public String getLongitudeDme() {
        return this.longitudeDme;
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
     * Get frequency protection
     *
     * @return frequency protection
     */
    public String getFrequenceProtection() {
        return this.frequenceProtection;
    }

    /**
     * Get VOR DME Biais
     *
     * @return VOR/DME biais
     */
    public String getBiais() {
        return this.biais;
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
     * Get Facility Characteristics
     *
     * @return Facility Chracteristics
     */
    public String getFacilityCharacteristics() {
        return this.facilityCharacteristics;
    }

    /**
     * Display Vor/Dme information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherVorDme(OutputStreamWriter fw) throws IOException {
        fw.write("DME Ident: " + this.identifiantDme + "\n");
        fw.write("DME Latitude: " + this.latitudeDme + "\n");
        fw.write("DME Longitude: " + this.longitudeDme + "\n");
        fw.write("Frequency: " + this.frequence + "\n");
        fw.write("Frequency Protection: " + this.frequenceProtection + "\n");
        fw.write("Figure of Merit: " + this.navaidMerit + "\n");
        fw.write("Facility Characteristics: " + this.facilityCharacteristics + "\n");
        fw.write("Biais: " + this.biais + "\n");
    }

    /**
     * Add new Vor/Dme record in the SQL database
     *
     * @param con Database Connection
     * @param icaoCode ICAO Code
     * @param identifiantVor Vor Identifier
     * @param VorName Name
     * @param latitudeVor Vor Latitude
     * @param longitudeVor Vor Longitude
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport Airport ICAO Code
     * @param magneticVariation Magnetic Variation
     * @param datum Datum Code
     * @param DmeElevation Dme Elevation
     * @param identifiantDme Dme Identifier
     * @param latitudeDme Dme Latitude
     * @param longitudeDme Dme Longitude
     * @param frequence Frequency
     * @param frequenceProtection Frequency Protection
     * @param biais Biais
     * @param merit Navaid Merit
     */
    protected static void addSql(Connection con, String icaoCode, String identifiantVor, String VorName, String latitudeVor, String longitudeVor, String aeroport,
            String icaoAeroport, String magneticVariation, String datum, String DmeElevation, String identifiantDme, String latitudeDme, String longitudeDme, String frequence,
            String frequenceProtection, String biais, String merit) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO vorDme(icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, elevation, identifiantDme, latitudeDme, longitudeDme, frequence, frequenceProtection, biais, navaidMerit) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiantVor);
            pst.setString(3, VorName);
            pst.setString(4, latitudeVor);
            pst.setString(5, longitudeVor);
            pst.setString(6, aeroport);
            pst.setString(7, icaoAeroport);
            pst.setString(8, magneticVariation);
            pst.setString(9, datum);
            pst.setString(10, DmeElevation);
            pst.setString(11, identifiantDme);
            pst.setString(12, latitudeDme);
            pst.setString(13, longitudeDme);
            pst.setString(14, frequence);
            pst.setString(15, frequenceProtection);
            pst.setString(16, biais);
            pst.setString(17, merit);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Frequency in Vor/Dme Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param frequence Frequency
     */
    protected static void modifySqlFrequence(Connection con, int id, String frequence) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vorDme set frequence=? WHERE id=?";
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
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Biais in Vor/Dme Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param biais biais
     */
    protected static void modifySqlBiais(Connection con, int id, String biais) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vorDme set biais=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, biais);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Navaid Merit in Vor/Dme Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param merit Navaid Merit
     */
    protected static void modifySqlMerit(Connection con, int id, String merit) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vorDme set navaidMerit=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, merit);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Frequency Protection in VOR/DME Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param freqPro Frequence Protection
     */
    protected static void modifySqlFrequenceProtection(Connection con, int id, String freqPro) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vorDme set frequenceProtection=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, freqPro);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Dme latitude in VOR/DME Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param latitude Dme latitude
     */
    protected static void modifySqlDmeLatitude(Connection con, int id, String latitude) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vorDme set latitudeDme=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, latitude);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Dme Longitude in VOR/DME Record
     *
     * @param con Database connection
     * @param id Id of record
     * @param longitude Dme longitude
     */
    protected static void modifySqlDmeLongitude(Connection con, int id, String longitude) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE vorDme set longitudeDme=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, longitude);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VorDme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
