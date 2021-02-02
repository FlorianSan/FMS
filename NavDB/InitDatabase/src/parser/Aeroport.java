/*
 * Class to manage and to get Airport Information
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
 * Class to manage and to get Airport Information
 *
 * @author yoann
 */
public class Aeroport {

    /**
     * ICAO Code
     */
    private String icaoCode;
    /**
     * Airport ICAO Identifier
     */
    private String identifiant;
    /**
     * ATA/IATA Designator
     */
    private String ataIata;
    /**
     * Speed Limit Altitude
     */
    private String speedLimitAltitude;
    /**
     * Longest Runway
     */
    private String longestRwy;
    /**
     * IFR Capability
     */
    private String ifr;
    /**
     * Longway Surface Code
     */
    private String longRwy;
    /**
     * Airport Reference Pt Latitude
     */
    private String latitude;
    /**
     * Airport Reference Pt Longitude
     */
    private String longitude;
    /**
     * Magnetic Variation
     */
    private String magneticVariation;
    /**
     * Airport Elevation
     */
    private String elevation;
    /**
     * Speed Limit
     */
    private String speedLimit;
    /**
     * Recommended Navaid
     */
    private Balises receiverVhf;
    /**
     * Transitions Altitude
     */
    private String transAltitude;
    /**
     * Transition Level
     */
    private String transLevel;
    /**
     * Public Military Indicator
     */
    private String publicMilitaire;
    /**
     * Time Zone
     */
    private String timeZone;
    /**
     * DayLight Indicator
     */
    private String dayTime;
    /**
     * Magnetic/True Indicator
     */
    private String MTInd;
    /**
     * Datum Code
     */
    private String datum;
    /**
     * Airport Name
     */
    private String airportName;
    /**
     * FIR Identifier
     */
    private String firIdentifier;
    /**
     * UIR Identifier
     */
    private String uirIdentifier;
    /**
     * Start/End Indicator
     */
    private String sEIndicator;
    /**
     * Start/End Date
     */
    private String sEDate;
    /**
     * Controlled A/S Indicator
     */
    private String asInd;
    /**
     * Controlled A/S Arpt Ident
     */
    private String asArptIdent;
    /**
     * Controlled A/S Arpt ICAO
     */
    private String asIcaoCode;

    /**
     * Constructor Airport
     *
     * @param icaoCode Icao Code
     * @param identifiant Airport ICAO Identifier
     * @param ataIata ATA/IATA Designator
     * @param speedLimitAltitude Speed Limit Altitude
     * @param longestRwy Longest Runway
     * @param ifr IFR Capability
     * @param longRwy Longest Runway Surface Code
     * @param latitude Airport Reference Pt Latitude
     * @param longitude Aiport Reference Pt Longitude
     * @param magneticVariation Magnetic Variation
     * @param elevation Airport Elevation
     * @param speedLimit Speed Limit
     * @param receiverVhf Recommend Navaid
     * @param transAltitude Transition Altitude
     * @param transLevel Transition Level
     * @param publicMilitaire Public/Military Indicator
     * @param timeZone Time Zone
     * @param dayTime Daylight Indicator
     * @param MTInd Magnetic/True Indicator
     * @param datum Datum Code
     * @param airportName Airport Name
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEIndicator Start/End Indicator
     * @param sEDate Start/End Date
     * @param asInd Controlled A/S Indicator
     * @param asArptIdent Controlled A/S Arpt Ident
     * @param asIcaoCode Controlled A/S Arpt ICAO
     */
    public Aeroport(String icaoCode, String identifiant, String ataIata, String speedLimitAltitude, String longestRwy, String ifr,
            String longRwy, String latitude, String longitude, String magneticVariation, String elevation, String speedLimit,
            Balises receiverVhf, String transAltitude, String transLevel, String publicMilitaire, String timeZone, String dayTime,
            String MTInd, String datum, String airportName, String firIdentifier, String uirIdentifier, String sEIndicator,
            String sEDate, String asInd, String asArptIdent, String asIcaoCode) {
        this.icaoCode = icaoCode;
        this.identifiant = identifiant;
        this.ataIata = ataIata;
        this.speedLimitAltitude = speedLimitAltitude;
        this.longestRwy = longestRwy;
        this.ifr = ifr;
        this.longRwy = longRwy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.magneticVariation = magneticVariation;
        this.elevation = elevation;
        this.speedLimit = speedLimit;
        this.receiverVhf = receiverVhf;
        this.transAltitude = transAltitude;
        this.transLevel = transLevel;
        this.publicMilitaire = publicMilitaire;
        this.timeZone = timeZone;
        this.dayTime = dayTime;
        this.MTInd = MTInd;
        this.datum = datum;
        this.airportName = airportName;
        this.firIdentifier = firIdentifier;
        this.uirIdentifier = uirIdentifier;
        this.sEIndicator = sEIndicator;
        this.sEDate = sEDate;
        this.asInd = asInd;
        this.asArptIdent = asArptIdent;
        this.asIcaoCode = asIcaoCode;
    }

    /**
     * Constructor of airport
     *
     * @param identifiant Airport ICAO Identifier
     */
    public Aeroport(String identifiant) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from aeroport where identifiant like ?");
            pst.setString(1, identifiant);
            rs = pst.executeQuery();
            if (rs.next()) { // il y a un resultat
                this.icaoCode = rs.getString("icaoCode");
                this.identifiant = rs.getString("identifiant");
                this.ataIata = rs.getString("ataIata");
                this.speedLimitAltitude = rs.getString("speedLimitAltitude");
                this.longestRwy = rs.getString("longestRwy");
                this.ifr = rs.getString("ifr");
                this.longRwy = rs.getString("longRwy");
                this.latitude = rs.getString("latitude");
                this.longitude = rs.getString("longitude");
                this.magneticVariation = rs.getString("magneticVariation");
                this.elevation = rs.getString("elevation");
                this.speedLimit = rs.getString("speedLimit");
                this.receiverVhf = new Balises(rs.getString("recVhf"), rs.getString("icaoCodeVhf"));
                this.transAltitude = rs.getString("transAltitude");
                this.transLevel = rs.getString("transLevel");
                this.publicMilitaire = rs.getString("publicMilitaire");
                this.timeZone = rs.getString("timeZone");
                this.dayTime = rs.getString("dayTime");
                this.MTInd = rs.getString("MTInd");
                this.datum = rs.getString("datum");
                this.airportName = rs.getString("airportName");
                this.firIdentifier = rs.getString("firIdentifier");
                this.uirIdentifier = rs.getString("uirIdentifier");
                this.sEIndicator = rs.getString("sEIndicator");
                this.sEDate = rs.getString("sEDate");
                this.asInd = rs.getString("asInd");
                this.asArptIdent = rs.getString("asArptIdent");
                this.asIcaoCode = rs.getString("asIcaoCode");

            }

        } catch (SQLException ex) {
            Logger.getLogger(Aeroport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get Magnetic/True Indicator
     *
     * @return L'indicateur
     */
    public String getMTind() {
        return this.MTInd;
    }

    /**
     * Get Airport Name
     *
     * @return The Airport Name
     */
    public String getAirportName() {
        return this.airportName;
    }

    /**
     * Get Controlled A/S Identifier
     *
     * @return Controlled A/S Identifier
     */
    public String getAsAirportIdent() {
        return this.asArptIdent;
    }

    /**
     * Get Controlled A/S Arpt ICAO
     *
     * @return Controlled A/S Arpt ICAO
     */
    public String getAsIcaoCode() {
        return this.asIcaoCode;
    }

    /**
     * Get Controlled A/S Indicator
     *
     * @return Controlled A/S Indicator
     */
    public String getAsInd() {
        return this.asInd;
    }

    /**
     * Get ATA/IATA designator
     *
     * @return ATA/IATA designator
     */
    public String getAtaIata() {
        return this.ataIata;
    }

    /**
     * Get Datum code
     *
     * @return Datum Code
     */
    public String getDatum() {
        return this.datum;
    }

    /**
     * Get DayLight indicator
     *
     * @return DayLight Indicator
     */
    public String getDayTime() {
        return this.datum;
    }

    /**
     * Get Airport Elevation
     *
     * @return Altitude
     */
    public String getElevation() {
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
     * Get Icao Code Airport
     *
     * @return Icao Code
     */
    public String getIcaoCode() {
        return this.icaoCode;
    }

    /**
     * Get Airport ICAO Identifier
     *
     * @return Aiport ICAO Identifier
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Get IFR Capability
     *
     * @return IFR Capability
     */
    public String getIfr() {
        return this.ifr;
    }

    /**
     * Get Airport Reference Pt Latitude
     *
     * @return Latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * Get the Longest Runway Surface Code
     *
     * @return the runway number
     */
    public String getLongRwy() {
        return this.longRwy;
    }

    /**
     * Get the Longest Runway
     *
     * @return runway Number
     */
    public String getLongestRwy() {
        return this.longestRwy;
    }

    /**
     * Get Airport Reference Pt Longitude
     *
     * @return Longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     * Get Magnetic Variation
     *
     * @return magnetic Variation
     */
    public String getMagneticVariation() {
        return this.magneticVariation;
    }

    /**
     * Get Public/Military Indicator
     *
     * @return Indicator
     */
    public String getPublicMilitaire() {
        return this.publicMilitaire;
    }

    /**
     * Get Recommended navaid
     *
     * @return Name of the recommended Navaid
     */
    public Balises getReceiverVhf() {
        return this.receiverVhf;
    }

    /**
     * Get Start/End Date
     *
     * @return Start/End Date
     */
    public String getSEDate() {
        return this.sEDate;
    }

    /**
     * Get Start/End Indicator
     *
     * @return Start/End Indicator
     */
    public String getSEIndicator() {
        return this.sEIndicator;
    }

    /**
     * Get Speed Limit
     *
     * @return Speed Limit
     */
    public String getSpeedLimit() {
        return this.speedLimit;
    }

    /**
     * Get Speed Limit Altitude
     *
     * @return Speed Limit Altitude
     */
    public String getSpeedLimitAltitude() {
        return this.speedLimitAltitude;
    }

    /**
     * Get Time Zone
     *
     * @return Time Zone
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Get Transition Altitude
     *
     * @return Transition Altitude
     */
    public String getTransAltitude() {
        return this.transAltitude;
    }

    /**
     * Get Transition Level
     *
     * @return Transition Level
     */
    public String getTransLevel() {
        return this.transLevel;
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
     * Display Airport informations in a text file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherAeroport(OutputStreamWriter fw) throws IOException {
        fw.write("Airport ICAO Identifier : " + this.identifiant + "\n");
        fw.write("Airport Name : " + this.airportName + "\n");
        fw.write("ATA/IATA Designator : " + this.ataIata + "\n");
        fw.write("Datum Code : " + this.datum + "\n");
        fw.write("Time Zone : " + this.timeZone + "\n");
        fw.write("DayLight Indicator : " + this.dayTime + "\n");
        fw.write("Airport Elevation : " + this.elevation + "\n");
        fw.write("FIR Identifier : " + this.firIdentifier + "\n");
        fw.write("IFR Capability : " + this.ifr + "\n");
        fw.write("Airport Reference Pt. Latitude : " + this.latitude + "\n");
        fw.write("Longest Runway Surface Code : " + this.longRwy + "\n");
        fw.write("Longest Runway : " + this.longestRwy + "\n");
        fw.write("Airport Reference Pt. Longitude : " + this.longitude + "\n");
        fw.write("Magnetic/True Indicator : " + this.MTInd + "\n");
        fw.write("Magnetic Variations : " + this.magneticVariation + "\n");
        fw.write("Public/Military Indicator : " + this.publicMilitaire + "\n");
        fw.write("Start/End Date : " + this.sEDate + "\n");
        fw.write("Start/End Indicator : " + this.sEIndicator + "\n");
        fw.write("Speed Limit : " + this.speedLimit + "\n");
        fw.write("Speed Limit Altitude : " + this.speedLimitAltitude + "\n");
        fw.write("Time Zone : " + this.timeZone + "\n");
        fw.write("Transitions Altitude : " + this.transAltitude + "\n");
        fw.write("Transition Level : " + this.transLevel + "\n");
        fw.write("UIR Identifier : " + this.uirIdentifier + "\n");
    }

    /**
     * Add a new record in the SQL Airport Table
     *
     * @param con DataBase Connection
     * @param icaoCode Icao Code of the Airport
     * @param identifiant Airport ICAO Identifier
     * @param ataIata ATA/IATA Designator
     * @param speedLimitAltitude Speed Limit Altitude
     * @param longestRwy Longest Runway
     * @param ifr IFR Capability
     * @param longRwy Longest Runway Surface Code
     * @param latitude Airport Reference Pt Latitude
     * @param longitude Airport Reference Pt Longitude
     * @param magneticVariation Magnetic Variations
     * @param elevation Airport Elevation
     * @param speedLimit Speed Limit
     * @param recVhf Recommended Navaid
     * @param icaoCodeVhf Icao Code of Recommended Navaid
     * @param transAltitude Transition Altitude
     * @param transLevel Transition Level
     * @param publicMilitaire Public/Military Indicator
     * @param timeZone Time Zone
     * @param dayTime DayLight Indicator
     * @param MTInd Magnetic/True Indicator
     * @param datum Datum Code
     * @param airportName Airport Name
     */
    protected static void addSql(Connection con, String icaoCode, String identifiant, String ataIata, String speedLimitAltitude, String longestRwy, String ifr,
            String longRwy, String latitude, String longitude, String magneticVariation, String elevation, String speedLimit, String recVhf,
            String icaoCodeVhf, String transAltitude, String transLevel, String publicMilitaire, String timeZone, String dayTime, String MTInd,
            String datum, String airportName) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO aeroport (icaoCode, identifiant, ataIata, speedLimitAltitude, longestRwy, ifr, longRwy, latitude, longitude,magneticVariation,elevation,speedLimit,recVhf,icaoCodeVhf,transAltitude,transLevel,publicMilitaire,timeZone,dayTime,MTInd,datum, airportName) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiant);
            pst.setString(3, ataIata);
            pst.setString(4, speedLimitAltitude);
            pst.setString(5, longestRwy);
            pst.setString(6, ifr);
            pst.setString(7, longRwy);
            pst.setString(8, latitude);
            pst.setString(9, longitude);
            pst.setString(10, magneticVariation);
            pst.setString(11, elevation);
            pst.setString(12, speedLimit);
            pst.setString(13, recVhf);
            pst.setString(14, icaoCodeVhf);
            pst.setString(15, transAltitude);
            pst.setString(16, transLevel);
            pst.setString(17, publicMilitaire);
            pst.setString(18, timeZone);
            pst.setString(19, dayTime);
            pst.setString(20, MTInd);
            pst.setString(21, datum);
            pst.setString(22, airportName);
            pst.executeUpdate();



        } catch (SQLException ex) {
            Logger.getLogger(Aeroport.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();


                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Update the airport record by adding Continuation informations
     *
     * @param con DataBase Connection
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEIndicator Start/End Indicator
     * @param sEdate Start/End Date
     * @param asInd Controlled A/S Indicator
     * @param asArptIdent Controlled A/S Airport Indentifier
     * @param asIcaoCode Controlled A/S ICAO Code
     * @param id Id of the airport record
     */
    protected static void addSqlContinuation(Connection con, String firIdentifier, String uirIdentifier, String sEIndicator, String sEdate,
            String asInd, String asArptIdent, String asIcaoCode, int id) {
        PreparedStatement pst = null;
        try {
            String stm = "UPDATE aeroport set firIdentifier= ?, uirIdentifier= ?, sEIndicator= ?, sEdate= ?, asInd= ?, asArptIdent= ?, asIcaoCode= ? WHERE id=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, firIdentifier);
            pst.setString(2, uirIdentifier);
            pst.setString(3, sEIndicator);
            pst.setString(4, sEdate);
            pst.setString(5, asInd);
            pst.setString(6, asArptIdent);
            pst.setString(7, asIcaoCode);
            pst.setInt(8, id);
            pst.executeUpdate();




        } catch (SQLException ex) {
            Logger.getLogger(Aeroport.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();




                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Ckeck if the airport exists in the SQL database
     *
     * @param aeroport Airport ICAO Identifier
     * @return True or False
     */
    public static boolean isPresent(String aeroport) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT count(*) from aeroport where identifiant=?");
            pst.setString(1, aeroport);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                result = (rs.getInt(1) >= 1);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Aeroport.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    /**
     * Return the ICAO Code of an Airport
     *
     * @param aeroport ICAO Airport Identifier
     * @return An ArrayList of ICAO Code
     */
    public static ArrayList<String> listAirport(String aeroport) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> a = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT icaoCode from aeroport where identifiant=?");
            pst.setString(1, aeroport);
            rs = pst.executeQuery();
            while (rs.next()) {
                a.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Aeroport.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return a;
    }

    /**
     * Return the id of the last airport record in the database
     *
     * @param con DataBase Connection
     * @return The id of the last airport record in the database
     */
    protected static int lastRecord(Connection con) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int result = 0;
        try {
            pst = con.prepareStatement("SELECT max(id) from aeroport");
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aeroport.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Aeroport.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
}
