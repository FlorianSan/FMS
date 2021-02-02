/*
 * Class to manage and to get informationa about runway
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
 * Class to manage and to get information about runway
 *
 * @author yoann
 */
public class Piste {

    /**
     * ICAO Code
     */
    private String icaoCode;
    /**
     * Runway Identifier
     */
    private String identifiant;
    /**
     * ICAO Airport Identifier
     */
    private Aeroport arpt;
    /**
     * Runway Length
     */
    private String runwayLength;
    /**
     * Runway Bearing
     */
    private String runwayBearing;
    /**
     * Runway Latitude
     */
    private String latitude;
    /**
     * Runway Longitude
     */
    private String longitude;
    /**
     * Runway Gradient
     */
    private String runwayGrad;
    /**
     * Ellipsoid Height
     */
    private String ellipsoidHeight;
    /**
     * Landing Threshold Elevation
     */
    private String lndgThresElev;
    /**
     * Displaced Threshold Distance
     */
    private String dsplcdThr;
    /**
     * TCH Value Indicator
     */
    private String tch;
    /**
     * Runway width
     */
    private String width;
    /**
     * Localizer Glide Slope
     */
    private Ils locGlsIdent;
    /**
     * Localizer category
     */
    private String categorieLoc;
    /**
     * Stopway
     */
    private String stopway;
    /**
     * SectionCode Localizer Glide Slope Identifier
     */
    private String secLocGlsIdent;
    /**
     * SectionCode Localizer Category
     */
    private String categorieSecLoc;

    /**
     * Runway Constructor
     *
     * @param identifiant Runway Identifier
     * @param arpt Airport Object
     */
    public Piste(String identifiant, Aeroport arpt) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from piste where identifiantRwy like ? and identifiantAeroport like ?");
            pst.setString(1, "RW" + identifiant);
            pst.setString(2, arpt.getIdentifiant());
            rs = pst.executeQuery();
            if (rs.next()) {
                this.icaoCode = rs.getString("icaoCode");
                this.identifiant = rs.getString("identifiantRwy");
                this.arpt = arpt;
                this.runwayLength = rs.getString("runwayLength");
                this.runwayBearing = rs.getString("runwayBearing");
                this.latitude = rs.getString("latitude");
                this.longitude = rs.getString("longitude");
                this.runwayGrad = rs.getString("runwayGrad");
                this.ellipsoidHeight = rs.getString("ellipsoidHeight");
                this.lndgThresElev = rs.getString("lndgThresElev");
                this.dsplcdThr = rs.getString("dsplcdThr");
                this.tch = rs.getString("tch");
                this.width = rs.getString("width");
                if (rs.getString("locglsident").equals("")) {
                    this.locGlsIdent = null;
                } else { // Si ILS
                    this.locGlsIdent = Ils.createIls(this.identifiant, arpt);
                }

                this.categorieLoc = rs.getString("categorieLoc");
                this.stopway = rs.getString("stopway");
                this.secLocGlsIdent = rs.getString("secLocGlsIdent");
                this.categorieSecLoc = rs.getString("categorieSecLoc");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get Airport
     *
     * @return Airport
     */
    public Aeroport getAeroport() {
        return this.arpt;
    }

    /**
     * Get Localizer Category
     *
     * @return Localizer Category
     */
    public String getCategorieLoc() {
        return this.categorieLoc;
    }

    /**
     * Get Localizer Category Subsection
     *
     * @return Localizer Category Subsection
     */
    public String getCategorieSecLoc() {
        return this.categorieSecLoc;
    }

    /**
     * Get Displaced Threshold Distance
     *
     * @return Displaced Threshold Distance
     */
    public String getDsplcdThr() {
        return this.dsplcdThr;
    }

    /**
     * Get Ellipsoid Height
     *
     * @return Ellipsoid Height
     */
    public String getEllipsoidHeight() {
        return this.ellipsoidHeight;
    }

    /**
     * Get ICAO Code
     *
     * @return ICAO Code
     */
    public String getIcaoCode() {
        return this.icaoCode;
    }

    /**
     * Get Runway Identifier
     *
     * @return Runway Identifier
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Get latitude
     *
     * @return Latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * Get Longitude
     *
     * @return Longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     * Get Landing Threshold Elevation
     *
     * @return Landing Threshold Elevation
     */
    public String getLndgThresElev() {
        return this.lndgThresElev;
    }

    /**
     * Get Localizer Glide Slope
     *
     * @return Localizer Glide Slope
     */
    public Ils getLocalizer() {
        return this.locGlsIdent;
    }

    /**
     * Get Runway Bearing
     *
     * @return Runway Bearing
     */
    public String getRunwayBearing() {
        return this.runwayBearing;
    }

    /**
     * Get Runway Gradient
     *
     * @return Runway Gradient
     */
    public String getRunwayGrad() {
        return this.runwayGrad;
    }

    /**
     * Get Runway Length
     *
     * @return Runway length
     */
    public String getRunwayLength() {
        return this.runwayLength;
    }

    /**
     * Get Section Code Localizer Glide Slope
     *
     * @return Section Code Localizer Glide Slope
     */
    public String getSecLocGlsIdent() {
        return this.secLocGlsIdent;
    }

    /**
     * Get Stop way
     *
     * @return StopWay
     */
    public String getStopWay() {
        return this.stopway;
    }

    /**
     * Get TCH Value Indicator
     *
     * @return TCH Value Indicator
     */
    public String getTch() {
        return this.tch;
    }

    /**
     * Get runway width
     *
     * @return runway Width
     */
    public String getWidth() {
        return this.width;
    }

    /**
     * Display runway information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherPiste(OutputStreamWriter fw) throws IOException {
        fw.write("Runway Identifier: " + this.identifiant + "\n");
        fw.write("Airport ICAO Identifier: " + this.arpt.getIdentifiant() + "\n");
        fw.write("Runway Latitude: " + this.latitude + "\n");
        fw.write("Runway Longitude: " + this.longitude + "\n");
        fw.write("Runway True Bearing: " + this.runwayBearing + "\n");
        fw.write("Runway Gradient: " + this.runwayGrad + "\n");
        fw.write("Runway Length: " + this.runwayLength + "\n");
        fw.write("Runway Width: " + this.width + "\n");
        fw.write("ICAO Code: " + this.icaoCode + "\n");
        fw.write("Displaced Threshold Distance: " + this.dsplcdThr + "\n");
        fw.write("Landing Threshold Elevation: " + this.lndgThresElev + "\n");
        fw.write("(LTP) Ellipsoid Height: " + this.ellipsoidHeight + "\n");
        fw.write("Stopway: " + this.stopway + "\n");
        fw.write("TCH Value Indicator: " + this.tch + "\n");
        fw.write("Localizer/MLS/GLS Category/Class: " + this.categorieLoc + "\n");
        fw.write("Second Localizer/MLS/GLS Category/Class: " + this.categorieSecLoc + "\n");
        fw.write("Second Localizer/MLS/GLS Ref Path Ident: " + this.secLocGlsIdent + "\n");
        if (locGlsIdent != null) {
            fw.write("\n** Localizer/MLS/GLS Data:\n");
            locGlsIdent.afficherBalise(fw);
        }
    }

    /**
     * Add new Piste record in the SQL database
     *
     * @param con Database connection
     * @param icaoCode ICAO Code
     * @param identifiantRwy Runway identifier
     * @param identifiantAeroport ICAO Airport Identifier
     * @param runwayLength Runway Length
     * @param runwayBearing Runway Bearing
     * @param latitude Latitude
     * @param longitude Longitude
     * @param runwayGrad Runway Gradient
     * @param ellipsoidHeight Ellipsoid Height
     * @param lndgThresElev Landing Threshold Elevation
     * @param dsplcdThr Displaced Threshold Distance
     * @param tch TCH Value Indicator
     * @param width Runway Width
     * @param locGlsIdent Localizer Glide Slope Identifier
     * @param categorieLoc Localizer category
     * @param stopway Stopway
     * @param secLocGlsIdent Section Code Localizer Glide Slope
     * @param categorieSecLoc Category Section Code localizer
     */
    protected static void addSql(Connection con, String icaoCode, String identifiantRwy, String identifiantAeroport, String runwayLength,
            String runwayBearing, String latitude, String longitude, String runwayGrad, String ellipsoidHeight, String lndgThresElev,
            String dsplcdThr, String tch, String width, String locGlsIdent, String categorieLoc, String stopway, String secLocGlsIdent,
            String categorieSecLoc) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO piste (icaoCode, identifiantRwy, identifiantAeroport, runwayLength, runwayBearing, latitude, longitude, runwayGrad, ellipsoidHeight, lndgThresElev, dsplcdThr, tch, width, locGlsIdent, categorieLoc, stopway, secLocGlsIdent, categorieSecLoc) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, icaoCode);
            pst.setString(2, identifiantRwy);
            pst.setString(3, identifiantAeroport);
            pst.setString(4, runwayLength);
            pst.setString(5, runwayBearing);
            pst.setString(6, latitude);
            pst.setString(7, longitude);
            pst.setString(8, runwayGrad);
            pst.setString(9, ellipsoidHeight);
            pst.setString(10, lndgThresElev);
            pst.setString(11, dsplcdThr);
            pst.setString(12, tch);
            pst.setString(13, width);
            pst.setString(14, locGlsIdent);
            pst.setString(15, categorieLoc);
            pst.setString(16, stopway);
            pst.setString(17, secLocGlsIdent);
            pst.setString(18, categorieSecLoc);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


    }

    /**
     * Runway Identifier list for an airport
     *
     * @param aeroport ICAO Airport Identifier
     * @return list of runway
     */
    public static ArrayList<String> requestPistesAeroport(String aeroport) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> piste = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT identifiantRwy from piste where identifiantAeroport like ?");
            pst.setString(1, aeroport);
            rs = pst.executeQuery();
            while (rs.next()) {
                int length = rs.getString(1).length();
                piste.add(rs.getString(1).substring(2, length));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return piste;
    }

    /**
     * Runway Identifier list for an airport and a QFU
     *
     * @param aeroport
     * @param qfu
     * @return Runway Identifier list
     */
    public static ArrayList<String> requestPisteAeroportQfu(String aeroport, String qfu) {
        ArrayList<String> l = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        Connection con = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT identifiantRwy from piste where identifiantAeroport like ? and identifiantRwy like ?");
            pst.setString(1, aeroport);
            pst.setString(2, "RW" + qfu + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                int length = rs.getString(1).length();

                l.add(rs.getString(1).substring(2, length));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Piste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return l;
    }
}
