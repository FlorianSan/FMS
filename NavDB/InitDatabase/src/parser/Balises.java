/*
 * Class to manage and to get marker information
 */
package parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get marker information
 *
 * @author yoann
 */
public class Balises {

    /**
     * Latitude
     */
    private String latitude;
    /**
     * Longitude
     */
    private String longitude;
    /**
     * ICAO Marker Identifier
     */
    private String identifiant;
    /**
     * Marker Name
     */
    private String nom;
    /**
     * ICAO Airport Identifier
     */
    private String aeroport;
    /**
     * ICAO Code of Airport
     */
    private String icaoAeroport;
    /**
     * ICAO Code of Marker
     */
    private String icaoCode;
    /**
     * Magnetic Variations
     */
    private String magneticVariation;
    /**
     * Datum Code
     */
    private String datum;
    /**
     * FIR identifier
     */
    private String firIdentifier;
    /**
     * Uir identifier
     */
    private String uirIdentifier;
    /**
     * Start and End indicator
     */
    private String sEIndicator;
    /**
     * Start and End date
     */
    private String sEdate;
    /**
     * Marker Elevation
     */
    private String elevation;

    /**
     * Marker Constructir
     *
     * @param aeroport ICAO Airport Identifier
     * @param datum Datum Code
     * @param icaoAeroport ICAO Code Airport
     * @param icaoCode ICAO Code Marker
     * @param identifiant Marker Identifier
     * @param latitude Latitude
     * @param longitude Longitude
     * @param magneticVariation Magnetic Variations
     * @param nom Name
     * @param elevation Marker Elevation
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEdate Star End Date
     * @param sEIndicator Star End Indicator
     */
    public Balises(String aeroport, String datum, String icaoAeroport, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom, String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator) {
        this.aeroport = aeroport;
        this.datum = datum;
        this.icaoAeroport = icaoAeroport;
        this.icaoCode = icaoCode;
        this.identifiant = identifiant;
        this.latitude = latitude;
        this.longitude = longitude;
        this.magneticVariation = magneticVariation;
        this.nom = nom;
        this.elevation = elevation;
        this.firIdentifier = firIdentifier;
        this.uirIdentifier = uirIdentifier;
        this.sEIndicator = sEIndicator;
        this.sEdate = sEdate;
    }

    /**
     * Marker Constructor
     *
     * @param icaoCode ICAO Code Marker
     * @param identifiant Marker Identifier
     */
    public Balises(String icaoCode, String identifiant) {
        this.icaoCode = icaoCode;
        this.identifiant = identifiant;
    }

    /**
     * Get ICAO Airport Identifier
     *
     * @return ICAO Airport Identifier
     */
    public String getAeroport() {
        return this.aeroport;
    }

    /**
     * Get Datum Code
     *
     * @return Datum Code
     */
    public String getDatum() {
        return this.datum;
    }

    /**
     * Get Facility Elevation
     *
     * @return Facility Elevation
     */
    public String getFacilityElevation() {
        return this.elevation;
    }

    /**
     * Get FIR Identifier
     *
     * @return FIR Identifier
     */
    public String getFirIdentifier() {
        return this.firIdentifier;
    }

    /**
     * Get Airport ICAO Code
     *
     * @return Airport ICAO Code
     */
    public String getIcaoAeroport() {
        return this.icaoAeroport;
    }

    /**
     * Get Marker ICAO Code
     *
     * @return Marker ICAO Code
     */
    public String getIcaoCode() {
        return this.icaoCode;
    }

    /**
     * Get Marker Identifier
     *
     * @return Marker Identifier
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Get Marker latitude
     *
     * @return Marker Latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * Get Marker Longitude
     *
     * @return Marker Longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     * Get Magnetic Variations
     *
     * @return Magnetic Variations
     */
    public String getMagneticVariation() {
        return this.magneticVariation;
    }

    /**
     * Get Marker Name
     *
     * @return Marker Name
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Get Start End Indicator
     *
     * @return Start End Indicator
     */
    public String getSEIndicator() {
        return this.sEIndicator;
    }

    /**
     * Get Start End Date
     *
     * @return Start End Date
     */
    public String getSEDate() {
        return this.sEdate;
    }

    /**
     * Get UIR Identifier
     *
     * @return UIR Identifier
     */
    public String getUirIdentifier() {
        return this.uirIdentifier;
    }

    /**
     * Set Facility Elevation
     *
     * @param facElev Facility Elevation
     */
    public void setFacilityElevation(String facElev) {
        this.elevation = facElev;
    }

    /**
     * Set FIR Identifier
     *
     * @param firIdent FIR Identifier
     */
    public void setFirIdentifier(String firIdent) {
        this.firIdentifier = firIdent;
    }

    /**
     * Set UIR Identifier
     *
     * @param uirIdent UIR Identifier
     */
    public void setUirIdentifier(String uirIdent) {
        this.uirIdentifier = uirIdent;
    }

    /**
     * Set Start End Indicator
     *
     * @param ind Start End Indicator
     */
    public void setSEIndicator(String ind) {
        this.sEIndicator = ind;
    }

    /**
     * Set Start End Date
     *
     * @param date Start End Date
     */
    public void setStartEndDate(String date) {
        this.sEdate = date;
    }

    /**
     * Set Marker Latitude
     *
     * @param lat Marker Latitude
     */
    public void setLatitude(String lat) {
        this.latitude = lat;
    }

    /**
     * Set Marker Longitude
     *
     * @param longi Marker Longitude
     */
    public void setLongitude(String longi) {
        this.longitude = longi;
    }

    /**
     * Set Magnetic Variation
     *
     * @param mag Magnetic Variation
     */
    public void setMagneticVariation(String mag) {
        this.magneticVariation = mag;
    }

    /**
     * Set Datum Code
     *
     * @param datum Datum Code
     */
    public void setDatumCode(String datum) {
        this.datum = datum;
    }

    /**
     * Set Marker Name
     *
     * @param id Marker Name
     */
    public void setNom(String id) {
        this.nom = id;
    }

    /**
     * Get Marker Type
     *
     * @return Marker Type
     */
    public String getTypeBalise() {
        return "balise";
    }

    /**
     * Display Marker Information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherBalise(OutputStreamWriter fw) throws IOException {
        fw.write("Name: " + this.nom + "\n");
        fw.write("ICAO Code: " + this.icaoCode + "\n");
        fw.write("Identifier: " + this.identifiant + "\n");
        fw.write("Latitude: " + this.latitude + "\n");
        fw.write("Longitude: " + this.longitude + "\n");
        fw.write("Elevation: " + this.elevation + "\n");
        fw.write("FIR Identifier: " + this.firIdentifier + "\n");
        fw.write("UIR Identifier: " + this.uirIdentifier + "\n");
        fw.write("Start/End Indicator: " + this.sEIndicator + "\n");
        fw.write("Start/End Date: " + this.sEdate + "\n");
        fw.write("Dynamic Mag. Variation: " + this.magneticVariation + "\n");
        fw.write("Airport Name: " + this.aeroport + "\n");
        fw.write("Airport ICAO Identifier: " + this.icaoAeroport + "\n");
        fw.write("Datum Code: " + this.datum + "\n");

        if (this instanceof WayPoint) {
            ((WayPoint) this).afficherWayPoint(fw);
        } else if (this instanceof Vor) {
            ((Vor) this).afficherVor(fw);
        } else if (this instanceof VorDme) {
            ((VorDme) this).afficherVorDme(fw);
        } else if (this instanceof Dme) {
            ((Dme) this).afficherDme(fw);
        } else if (this instanceof IlsDme) {
            ((IlsDme) this).afficherIlsDme(fw);
        } else if (this instanceof Tacan) {
            ((Tacan) this).afficherTacan(fw);
        } else if (this instanceof Ndb) {
            ((Ndb) this).afficherNdb(fw);
        } else if (this instanceof Ils) {
            ((Ils) this).afficherIls(fw);
        }
    }

    /**
     * Get the id of the last marker record
     *
     * @param con DataBase Connection
     * @return Id of the last marker record
     */
    protected static int lastRecord(Connection con) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int result = 0;
        try {
            pst = con.prepareStatement("SELECT max(id) from balises*");
            rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    /**
     * Get the last marker type record
     *
     * @param con Database Connection
     * @param id Id of the last marker record
     * @return The Marker type
     */
    protected static String lastType(Connection con, int id) {
        String result = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("SELECT p.relname FROM balises* b, pg_class p WHERE b.id = ? and b.tableoid = p.oid;");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    /**
     * Update the record by adding marker continuation
     *
     * @param con Database Connection
     * @param firIdentifier FIR Identifier
     * @param UirIdentifier UIR Identifier
     * @param sEIndicator Start End Indicator
     * @param sEDate Start End Date
     * @param id Id of the record
     */
    protected static void addContinuationBalises(Connection con, String firIdentifier, String UirIdentifier, String sEIndicator, String sEDate, int id) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set firIdentifier=?, UirIdentifier=?, sEIndicator=?, sEdate=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, firIdentifier);
            pst.setString(2, UirIdentifier);
            pst.setString(3, sEIndicator);
            pst.setString(4, sEDate);
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Update Datum in SQL Marker record
     *
     * @param con DataBase connection
     * @param id Id of the record
     * @param datum Datum Code
     */
    protected static void modifySqlDatum(Connection con, int id, String datum) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set datum=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, datum);
            pst.setInt(2, id);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Latitude in SQL Marker record
     *
     * @param con Database connection
     * @param id Id of the record
     * @param latitude Marker latitude
     */
    protected static void modifySqlLatitude(Connection con, int id, String latitude) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set latitude=? WHERE id=?";
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
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Longitude in SQL Marker record
     *
     * @param con Database Connection
     * @param id Id of the record
     * @param longitude Marker Longitude
     */
    protected static void modifySqlLongitude(Connection con, int id, String longitude) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set longitude=? WHERE id=?";
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
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Magnetic Variations in SQL Marker record
     *
     * @param con Database Connection
     * @param id Id of the last record
     * @param magneticVariation Magnetic Variations
     */
    protected static void modifySqlMagneticVariation(Connection con, int id, String magneticVariation) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set magneticVariation=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, magneticVariation);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Facility Elevation in SQL Marker record
     *
     * @param con Database Connection
     * @param id Id of the last record
     * @param elevation Facility Elevation
     */
    protected static void modifySqlElevation(Connection con, int id, String elevation) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set elevation=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, elevation);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update Marker Name in SQL Marker record
     *
     * @param con DataBase Connection
     * @param id Id of the last record
     * @param name Marker name
     */
    protected static void modifySqlName(Connection con, int id, String name) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE balises* set nom=? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, name);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ndb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get Marker information
     *
     * @param identifiant Marker identifier
     * @return List of 3 row Array (Marker Type, Identifier, IcaoCode)
     */
    public static ArrayList<String[]> listStringBalises(String identifiant) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String[]> a = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT p.relname as type, identifiant, icaoCode  from pg_class p, balises* b where b.tableoid=p.oid and identifiant like ?");
            pst.setString(1, identifiant);
            rs = pst.executeQuery();
            while (rs.next()) {
                String[] b = new String[3];
                b[0] = rs.getString("type").toUpperCase(Locale.FRENCH);
                b[1] = rs.getString("identifiant");
                b[2] = rs.getString("icaoCode");
                a.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }

    /**
     * Check if the marker exists in the SQl DataBase
     *
     * @param identifiant Marker Identifier
     * @return True or False
     */
    public static boolean isPresent(String identifiant) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT identifiant from balises* where identifiant like ?");
            pst.setString(1, identifiant);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    /**
     * Get Marker latitude and longitude
     *
     * @param identifiant Marker Identifier
     * @param icaoCode Marker ICAO Code
     * @return Marker Latitude and longitude
     */
    public static LatitudeLongitude getLatitudeLongitude(String type, String identifiant, String icaoCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            switch (type) {
                case "VOR":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM vor WHERE identifiant like ? and icaoCode like ?");
                    break;
                case "DME":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM dme WHERE identifiant like ? and icaoCode like ?");
                    break;
                case "VORDME":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM vordme WHERE identifiant like ? and icaoCode like ?");
                    break;
                case "ILSDME":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM ilsdme WHERE identifiant like ? and icaoCode like ?");
                    break;
                case "TACAN":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM tacan WHERE identifiant like ? and icaoCode like ?");
                    break;
                case "NDB":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM ndb WHERE identifiant like ? and icaoCode like ?");
                    break;
                case "WAYPOINT":
                    pst = con.prepareStatement("SELECT latitude, longitude FROM waypoint WHERE identifiant like ? and icaoCode like ?");
                    break;
                default:
                    System.out.println("Balises : impossible");
                    break;
            }
            if (pst != null) {
                pst.setString(1, identifiant);
                pst.setString(2, icaoCode);
                rs = pst.executeQuery();
                if (rs.next()) {
                    return new LatitudeLongitude(rs.getString("latitude"), rs.getString("longitude"));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    /**
     * Get the identifier of a navaid thanks to the frequency
     *
     * @param latitude Latitude in degrees
     * @param longitude Longitude in degrees
     * @param frequence Frequency of Navaid
     * @return Identifier or frequency in case of failure
     */
    public static String getIdentifierWithFrequence(double latitude, double longitude, String frequence) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int freq = Integer.parseInt(frequence.replace(".", ""));
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("((select identifiant, distance(?, ?, latitude, longitude) as distance from vor where cast(frequence as integer) = ?) UNION (select identifiant, distance(?, ?, latitude, longitude) as distance from vordme where cast(frequence as integer) = ?) UNION (select identifiant, distance(?, ?, latitude, longitude) as distance from dme where cast( frequence as integer) = ?)UNION (select identifiant, distance(?, ?, latitude, longitude) as distance from ils where cast(frequence as integer) = ?)) order by distance");
            pst.setDouble(1, latitude);
            pst.setDouble(2, longitude);
            pst.setInt(3, freq);

            pst.setDouble(4, latitude);
            pst.setDouble(5, longitude);
            pst.setInt(6, freq);

            pst.setDouble(7, latitude);
            pst.setDouble(8, longitude);
            pst.setInt(9, freq);

            pst.setDouble(10, latitude);
            pst.setDouble(11, longitude);
            pst.setInt(12, freq);

            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("identifiant");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Balises.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return frequence;
    }
}
