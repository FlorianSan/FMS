/*
 * Class to manage and to get information about ILS
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
 * Class to manage and to get information about ILS
 *
 * @author yoann
 */
public class Ils extends Balises {

    /**
     * ILS Category
     */
    private String categorie;
    /**
     * ILS frequency
     */
    private String frequence;
    /**
     * Runway Identifier
     */
    private String runwayIdentifiant;
    /**
     * Localizer Bearing
     */
    private String locBearing;
    /**
     * Glide Slope latitude
     */
    private String gsLatitude;
    /**
     * Glide Slope Longitude
     */
    private String gsLongitude;
    /**
     * Localizer Position referecne
     */
    private String locFr;
    /**
     * Localizer Position Reference
     */
    private String localiserPositionReference;
    /**
     * Glide Slope Threshold
     */
    private String gsThres;
    /**
     * Localizer Width
     */
    private String locWidth;
    /**
     * Glide Slope Angle
     */
    private String gsAngle;
    /**
     * Glide Slope Height at Landing Threshold
     */
    private String tch;
    /**
     * Glide Slope Elevation
     */
    private String gsElev;
    /**
     * Facility
     */
    private String facility;
    /**
     * ICAO Code of facility
     */
    private String facilityIcao;
    /**
     * SectionCode of facility
     */
    private String facilitySec;
    /**
     * SubSection Code of facility
     */
    private String facilitySub;

    /**
     * ILS Constructor
     *
     * @param aeroport ICAO Airport Identifier
     * @param icaoAeroport ICAO Code Airport
     * @param datum Datum Code
     * @param icaoCode Icao Code
     * @param identifiant Ils Identifier
     * @param latitude Localizer Latitude
     * @param longitude Localizer Longitude
     * @param magneticVariation Magnetic Variation
     * @param nom Ils Name
     * @param elevation Elevation
     * @param firIdentifier FIR Identifier
     * @param uirIdentifier UIR Identifier
     * @param sEdate Start End Date
     * @param sEIndicator Start End Indicator
     * @param categorie ILS category
     * @param frequence Frequency
     * @param rwy Runway
     * @param locBearing Localizer Bearing
     * @param gsLatitude Glide Slope latitude
     * @param gsLongitude Glide Slope Longitude
     * @param locFr Localizer Position Reference
     * @param localiserPositionReference Localizer Position Reference
     * @param gsThres Glide Slope Threshold
     * @param locWidth Localizer Width
     * @param gsAngle Glide Slope Angle
     * @param tch Glide Slope Height at Landing Threshold
     * @param gsElev Glide Slope Elevation
     * @param facility Facility
     * @param facilityIcao ICAO Code of facility
     * @param facilitySec Section Code of facilty
     * @param facilitySub SubSection Code of facility
     */
    private Ils(String aeroport, String icaoAeroport, String datum, String icaoCode, String identifiant, String latitude, String longitude, String magneticVariation, String nom,
            String elevation, String firIdentifier, String uirIdentifier, String sEdate, String sEIndicator, String categorie,
            String frequence, String rwy, String locBearing, String gsLatitude, String gsLongitude, String locFr,
            String localiserPositionReference, String gsThres, String locWidth, String gsAngle, String tch, String gsElev,
            String facility, String facilityIcao, String facilitySec, String facilitySub) {
        super(aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, firIdentifier, uirIdentifier, sEdate, sEIndicator);
        this.categorie = categorie;
        this.frequence = frequence;
        this.runwayIdentifiant = rwy;
        this.locBearing = locBearing;
        this.gsLatitude = gsLatitude;
        this.gsLongitude = gsLongitude;
        this.locFr = locFr;
        this.localiserPositionReference = localiserPositionReference;
        this.gsThres = gsThres;
        this.locWidth = locWidth;
        this.gsAngle = gsAngle;
        this.tch = tch;
        this.gsElev = gsElev;
        this.facility = facility;
        this.facilityIcao = facilityIcao;
        this.facilitySec = facilitySec;
        this.facilitySub = facilitySub;

    }

    /**
     * Create ILS object
     *
     * @param rwy Runway Identifier
     * @param arpt Airport Object
     * @return ILS Object
     */
    protected static Ils createIls(String rwy, Aeroport arpt) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from ils where aeroport like ? and runwayIdentifiant like ?");
            pst.setString(1, arpt.getIdentifiant());
            pst.setString(2, rwy);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Ils(rs.getString("aeroport"), rs.getString("icaoAeroport"), rs.getString("datum"), rs.getString("icaoCode"), rs.getString("identifiant"), rs.getString("latitude"), rs.getString("longitude"), rs.getString("magneticVariation"), rs.getString("nom"),
                        rs.getString("elevation"), rs.getString("firIdentifier"), rs.getString("UirIdentifier"), rs.getString("sEdate"), rs.getString("sEIndicator"), rs.getString("categorie"),
                        rs.getString("frequence"), rwy, rs.getString("locBearing"), rs.getString("gslatitude"), rs.getString("gsLongitude"), rs.getString("locFr"),
                        rs.getString("localiserPositionReference"), rs.getString("gsThres"), rs.getString("locWidth"), rs.getString("gsAngle"), rs.getString("tch"), rs.getString("gsElev"),
                        rs.getString("facility"), rs.getString("facilityIcao"), rs.getString("facilitySec"), rs.getString("facilitySub"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    /**
     * Get ILS Category
     *
     * @return ILS category
     */
    public String getCategorie() {
        return this.categorie;
    }

    /**
     * Get Facility
     *
     * @return Facility
     */
    public String getFacility() {
        return this.facility;
    }

    /**
     * Get ICAO Code of facility
     *
     * @return ICAO Code of facility
     */
    public String getFacilityIcao() {
        return this.facilityIcao;
    }

    /**
     * Get SectionCode of facility
     *
     * @return Section Code of facility
     */
    public String getFacilitySec() {
        return this.facilitySec;
    }

    /**
     * Get SubSection Code of facility
     *
     * @return SubSectionCode facility
     */
    public String getFacilitySub() {
        return this.facilitySub;
    }

    /**
     * Get ILS frequency
     *
     * @return frequency
     */
    public String getFrequence() {
        return this.frequence;
    }

    /**
     * Get Glide Slope Angle
     *
     * @return Glide Slope Angle
     */
    public String getGsAngle() {
        return this.gsAngle;
    }

    /**
     * Get Glide Slope Elevation
     *
     * @return Glide Slope Elevation
     */
    public String getGsElev() {
        return this.gsElev;
    }

    /**
     * Get Glide Slope Latitude
     *
     * @return Glide Slope latitude
     */
    public String getGsLatitude() {
        return this.gsLatitude;
    }

    /**
     * Get Glide Slope Longitude
     *
     * @return Glide Slope Longitude
     */
    public String getGsLongitude() {
        return this.gsLongitude;
    }

    /**
     * Get Glide Slope Threshold
     *
     * @return Glide Slope Threshold
     */
    public String getGsThres() {
        return this.gsThres;
    }

    /**
     * Get Localizer Bearing
     *
     * @return Localizer Bearing
     */
    public String getLocBearing() {
        return this.locBearing;
    }

    /**
     * Get Localizer Position Reference
     *
     * @return Localizer Position Reference
     */
    public String getLocFr() {
        return this.locFr;
    }

    /**
     * Get Localizer Width
     *
     * @return Localizer Width
     */
    public String getLocWidth() {
        return this.locWidth;
    }

    /**
     * Get Localizer Position Reference
     *
     * @return Localiser Position Reference
     */
    public String getLocalizerPositionReference() {
        return this.localiserPositionReference;
    }

    /**
     * Get Runway Identifier
     *
     * @return Runway Identifier
     */
    public String getRunwayIdentifiant() {
        return this.runwayIdentifiant;
    }

    /**
     * Get Glide Slope Height at Landing Threshold
     *
     * @return Glide Slope Height at Landing Threshold
     */
    public String getTch() {
        return this.tch;
    }

    /**
     * Display ILS information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherIls(OutputStreamWriter fw) throws IOException {
        fw.write("ILS Category: " + this.categorie + "\n");
        fw.write("Supporting Facility ID: " + this.facility + "\n");
        fw.write("Supporting ICAO Code: " + this.facilityIcao + "\n");
        fw.write("Supporting Facility Section Code: " + this.facilitySec + "\n");
        fw.write("Supporting Facility Subsection Code: " + this.facilitySub + "\n");
        fw.write("Localizer Frequency: " + this.frequence + "\n");
        fw.write("Glide Slope Angle: " + this.gsAngle + "\n");
        fw.write("Glide Slope Elevation: " + this.gsElev + "\n");
        fw.write("Glide Slope Latitude: " + this.gsLatitude + "\n");
        fw.write("Glide Slope Longitude: " + this.gsLongitude + "\n");
        fw.write("Glide Slop Height at Landing Threshold: " + this.gsThres + "\n");
        fw.write("Localizer True Bearing: " + this.locBearing + "\n");
        fw.write("Localizer Position: " + this.locFr + "\n");
        fw.write("Localizer Width: " + this.locWidth + "\n");
        fw.write("Localizer Position Reference: " + this.localiserPositionReference + "\n");
        fw.write("Runway Identifier: " + this.runwayIdentifiant + "\n");
        fw.write("Threshold Crossing Height: " + this.tch + "\n");
    }

    /**
     * Add new ILS record in the SQL database
     *
     * @param con Database Connection
     * @param icaoCode ICAO Code
     * @param identifiant ILS Identifier
     * @param latitude Localizer Latitude
     * @param longitude Localizer Longitude
     * @param aeroport ICAO Airport Identifier
     * @param magneticVariation Magnetic Variation
     * @param categorie Category
     * @param frequence Frequency
     * @param runwayIdentifiant Runway Identifier
     * @param locBearing Localizer Bearing
     * @param gsLatitude Glide Slope Latitude
     * @param gsLongitude Glide Slope Longitude
     * @param locFr Loc Fr
     * @param localiserPositionReference Localizer Position Reference
     * @param gsThres Glide Slope ThresHold
     * @param locWidth Localizer Width
     * @param gsAngle Glide Slope Angle
     * @param tch Glide Slope Height at Landing Threshold
     * @param gsElev Glide Slope Elevation
     * @param facility Facility
     * @param facilityIcao ICAO Code of Facility
     * @param facilitySec SectionCode of facility
     * @param facilitySub SubSectionCode of facility
     */
    protected static void addSql(Connection con, String icaoCode, String identifiant, String latitude, String longitude, String aeroport, String magneticVariation,
            String categorie, String frequence, String runwayIdentifiant, String locBearing, String gsLatitude,
            String gsLongitude, String locFr, String localiserPositionReference, String gsThres, String locWidth, String gsAngle,
            String tch, String gsElev, String facility, String facilityIcao, String facilitySec, String facilitySub) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO ils (icaoCode, identifiant, nom, latitude, longitude, aeroport, icaoAeroport, magneticVariation, datum, categorie, frequence, runwayIdentifiant, locBearing, gsLatitude, gsLongitude, locFr, localiserPositionReference, gsThres, locWidth, gsAngle, tch, gsElev, facility, facilityIcao, facilitySec, facilitySub) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiant);
            pst.setString(3, "ILS PISTE");
            pst.setString(4, latitude);
            pst.setString(5, longitude);
            pst.setString(6, aeroport);
            pst.setString(7, icaoCode);
            pst.setString(8, magneticVariation);
            pst.setString(9, "WGE");
            pst.setString(10, categorie);
            pst.setString(11, frequence);
            pst.setString(12, runwayIdentifiant);
            pst.setString(13, locBearing);
            pst.setString(14, gsLatitude);
            pst.setString(15, gsLongitude);
            pst.setString(16, locFr);
            pst.setString(17, localiserPositionReference);
            pst.setString(18, gsThres);
            pst.setString(19, locWidth);
            pst.setString(20, gsAngle);
            pst.setString(21, tch);
            pst.setString(22, gsElev);
            pst.setString(23, facility);
            pst.setString(24, facilityIcao);
            pst.setString(25, facilitySec);
            pst.setString(26, facilitySub);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Ils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
