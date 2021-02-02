/*
 * Class to manage and to get information about procedure (Sid, Star, Approach)
 */
package parser;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get information about procedure (Sid, Star, Approach)
 *
 * @author yoann
 */
public class Procedure {

    /**
     * Procedure Identifier
     */
    private String identifiant;
    /**
     * Procedure type
     */
    private String type;
    /**
     * List of marker coordinates
     */
    private ArrayList<LatitudeLongitude> balises;

    /**
     * Procedure Constructor, empty constructor for parsing
     */
    public Procedure() {
    }

    /**
     * Procedure Constructor
     *
     * @param type Procedure Type
     * @param identifiant Procedure Identifier
     */
    public Procedure(String type, String identifiant) {
        this.type = type;
        this.identifiant = identifiant;
    }

    /**
     * Procedure Constructor
     *
     * @param type Procedure type
     * @param identifiant Procedure Identifier
     * @param balises List of Marker Coordinates
     */
    public Procedure(String type, String identifiant, ArrayList<LatitudeLongitude> balises) {
        this.type = type;
        this.identifiant = identifiant;
        this.balises = balises;
    }

    /**
     * Get Procedure Identifier
     *
     * @return Procedure identifier
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Get Procedure Type
     *
     * @return Procedure Type
     */
    public String getTypeProcedure() {
        return this.type;
    }

    /**
     * Get list of marker Coordinates
     *
     * @return List of Marker Coordinates
     */
    public ArrayList<LatitudeLongitude> getBalise() {
        return this.balises;
    }

    /**
     * Add new procedure record in the SQL database
     *
     * @param con Database Connection
     * @param typeProcedure SID, STAR, APPR
     * @param aeroportIdentifiant ICAO Airport Identifier
     * @param icaoCode ICAO Code
     * @param identifiant Identifier
     * @param routeType Route Type
     * @param transitionIdentifier Transition identifier
     * @param sequenceNumber Sequence Number
     * @param fixIdentifiant Fix identifier
     * @param icaoCodeFix ICAO Code of fix
     * @param secCodeFix Section Code of fix
     * @param subCodeFix SubSection code of fix
     * @param descriptionCode Description Code
     * @param turnDirection Turn Direction
     * @param requiredNavigationPerformance Required Navigation Performance
     * @param pathAndTerminaison Path And Terminaison
     * @param turnDirectionValide Turn Direction Valid
     * @param recommendedNavaid Recommened Navaid
     * @param icaoCodeNavaid ICAO Code of Navaid
     * @param arcRadius Arc Radius
     * @param theta Theta
     * @param rho Rho
     * @param magneticCruise Magnetic Cruise
     * @param routeDistance Route distance between waypoint and fix
     * @param secCodeRoute Section Code of Navaid
     * @param subCodeRoute SubSection Code of Navaid
     * @param altitudeDescription Altitude description
     * @param atc ATC
     * @param altitude Altitude
     * @param altitude2 Altitude
     * @param transAltitude Transition Altitude
     * @param speedLimit Speed Limit
     * @param verticalAngle Vertical Angle
     * @param centerFix Center Fix
     * @param multiCd Multiple Code or TAA Sector Identifier
     * @param icaoCodeCenter ICAO Code
     * @param secCodeCenter Section Code
     * @param subCodeCenter Sub Section Code
     * @param gnssFmsIndicator GNSS/FMS Indication
     * @param speedLmt Speed Limit
     * @param routeQual1 Apch Route Qualifier 1
     * @param routeQual2 Apch Route Qualifier 2
     */
    protected static void addSql(Connection con, String typeProcedure, String aeroportIdentifiant, String icaoCode, String identifiant, String routeType, String transitionIdentifier,
            String sequenceNumber, String fixIdentifiant, String icaoCodeFix, String secCodeFix, String subCodeFix, String descriptionCode,
            String turnDirection, String requiredNavigationPerformance, String pathAndTerminaison, String turnDirectionValide,
            String recommendedNavaid, String icaoCodeNavaid, String arcRadius, String theta, String rho, String magneticCruise,
            String routeDistance, String secCodeRoute, String subCodeRoute, String altitudeDescription, String atc,
            String altitude, String altitude2, String transAltitude, String speedLimit, String verticalAngle, String centerFix,
            String multiCd, String icaoCodeCenter, String secCodeCenter, String subCodeCenter, String gnssFmsIndicator,
            String speedLmt, String routeQual1, String routeQual2) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO procedure (typeProcedure, aeroportIdentifiant, icaoCode, identifiant, routeType, transitionIdentifier, sequenceNumber, fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix, descriptionCode, turnDirection, requiredNavigationPerformance , pathAndTerminaison, turnDirectionValide, recommendedNavaid, icaoCodeNavaid, arcRadius, theta, rho, magneticCruise, routeDistance, secCodeRoute, subCodeRoute, altitudeDescription, atc, altitude, altitude2, transAltitude, speedLimit, verticalAngle, centerFix, multiCd, icaoCodeCenter, secCodeCenter, subCodeCenter, gnssFmsIndicator, speedLmt, routeQual1, routeQual2) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, typeProcedure);
            pst.setString(2, aeroportIdentifiant);
            pst.setString(3, icaoCode);
            pst.setString(4, identifiant);
            pst.setString(5, routeType);
            pst.setString(6, transitionIdentifier);
            pst.setString(7, sequenceNumber);
            pst.setString(8, fixIdentifiant);
            pst.setString(9, icaoCodeFix);
            pst.setString(10, secCodeFix);
            pst.setString(11, subCodeFix);
            pst.setString(12, descriptionCode);
            pst.setString(13, turnDirection);
            pst.setString(14, requiredNavigationPerformance);
            pst.setString(15, pathAndTerminaison);
            pst.setString(16, turnDirectionValide);
            pst.setString(17, recommendedNavaid);
            pst.setString(18, icaoCodeNavaid);
            pst.setString(19, arcRadius);
            pst.setString(20, theta);
            pst.setString(21, rho);
            pst.setString(22, magneticCruise);
            pst.setString(23, routeDistance);
            pst.setString(24, secCodeRoute);
            pst.setString(25, subCodeRoute);
            pst.setString(26, altitudeDescription);
            pst.setString(27, atc);
            pst.setString(28, altitude);
            pst.setString(29, altitude2);
            pst.setString(30, transAltitude);
            pst.setString(31, speedLimit);
            pst.setString(32, verticalAngle);
            pst.setString(33, centerFix);
            pst.setString(34, multiCd);
            pst.setString(35, icaoCodeCenter);
            pst.setString(36, secCodeCenter);
            pst.setString(37, subCodeCenter);
            pst.setString(38, gnssFmsIndicator);
            pst.setString(39, speedLmt);
            pst.setString(40, routeQual1);
            pst.setString(41, routeQual2);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Fill table SID and STAR thanks to procedure table
     *
     * @param con Database Connection
     * @param type SID or STAR
     */
    protected void creationProcedures(Connection con, String type) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement ajoutSid = null;
        ResultSet rsBalise = null;
        PreparedStatement pstBalise = null;
        try {
            pst = con.prepareStatement("SELECT  identifiant,fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix, aeroportIdentifiant, icaoCode, recommendedNavaid, transitionIdentifier FROM procedure where typeProcedure like ? ORDER BY aeroportIdentifiant, identifiant, sequenceNumber asc ");
            pst.setString(1, type);
            rs = pst.executeQuery();
            rs.next();
            String identifiant = rs.getString(1);
            String aeroport = rs.getString(6);
            String icaoCode = rs.getString(7);
            String lastFix = null;
            String premierFix = null;
            String transitionIdentifier = rs.getString(9);
            int k = 0;
            boolean indicateurNavaid = false;
            List<LatitudeLongitude> couple = new ArrayList<>();

            do {
                if (indicateurNavaid) { // on utilise le navaid si le fix est vide
                    System.out.println("nava" + rs.getString(8));
                    premierFix = rs.getString(8);
                    indicateurNavaid = false;
                }

                if (!identifiant.equals(rs.getString(1))) {
//                    System.out.println("Ajout bdd de " + identifiant);
                    // ajout en bdd du precedent
                    if (type.equals("SID")) {
                        ajoutSid = con.prepareStatement("INSERT INTO sid (identifiant, icaoCode, aeroportIdentifiant, runwayIdentifiant, lastFix, firstFix, latLong) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    } else if (type.equals("STAR")) {
                        ajoutSid = con.prepareStatement("INSERT INTO star (identifiant, icaoCode, aeroportIdentifiant, runwayIdentifiant, lastFix, firstFix, latLong) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    }
                    ajoutSid.setString(1, identifiant);
                    ajoutSid.setString(2, icaoCode);
                    ajoutSid.setString(3, aeroport);
                    if (transitionIdentifier.length() > 0) {
                        ajoutSid.setString(4, transitionIdentifier);
                    } else {
                        ajoutSid.setString(4, "");
                    }
                    ajoutSid.setString(5, lastFix);

                    ajoutSid.setString(6, premierFix);



                    String[][] tab = new String[couple.size()][2];
                    for (int i = 0; i < couple.size(); i++) {
                        tab[i][0] = couple.get(i).getLatitude();
                        tab[i][1] = couple.get(i).getLongitude();
                    }

                    Array arraySql = con.createArrayOf("text", tab);
                    ajoutSid.setArray(7, arraySql);
                    ajoutSid.executeUpdate();
                    couple.clear();

                    // on update l'identifiant ds la boucle
                    identifiant = rs.getString(1);
                    aeroport = rs.getString(6);
                    icaoCode = rs.getString(7);
                    k = 0;
                    indicateurNavaid = false;
                    transitionIdentifier = rs.getString(9);
                }
//                System.out.println("Id :" + identifiant);

                if (!rs.getString(2).equals("")) {
                    if (rs.getString(4).equals("D") && rs.getString(5).equals("B")) {
                        //NDB
                        pstBalise = con.prepareStatement("SELECT latitude, longitude FROM ndb where identifiant like ? and icaoCode like ?");
                        pstBalise.setString(1, rs.getString(2));
                        pstBalise.setString(2, rs.getString(3));
                        rsBalise = pstBalise.executeQuery();
                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
//                            System.out.println("NDB" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();

                    } else if (rs.getString(4).equals("E") && rs.getString(5).equals("A")) {
                        //waypoint en route
                        pstBalise = con.prepareStatement("SELECT latitude, longitude FROM waypoint where identifiant like ? and icaoCode like ? and aeroport like 'ENRT'");
                        pstBalise.setString(1, rs.getString(2));
                        pstBalise.setString(2, rs.getString(3));
                        rsBalise = pstBalise.executeQuery();

                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
//                            System.out.println("WayPoint" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();

                    } else if (rs.getString(4).equals("P") && rs.getString(5).equals("C")) {
                        //waypoint terminaux
                        pstBalise = con.prepareStatement("SELECT latitude, longitude FROM waypoint where identifiant like ? and icaoCode like ? and aeroport like ?");
                        pstBalise.setString(1, rs.getString(2));
                        pstBalise.setString(2, rs.getString(3));
                        pstBalise.setString(3, rs.getString(6));
                        rsBalise = pstBalise.executeQuery();

                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
//                            System.out.println("WayPoint" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();
                    } else if (rs.getString(4).equals("D") && rs.getString(5).equals("")) {
                        //VHF
                        pstBalise = con.prepareStatement("(SELECT latitude, longitude from vor where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from vorDme where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from dme where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from tacan where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from ilsdme where identifiant=? and icaoCode=?)");
                        pstBalise.setString(1, rs.getString(2));
                        pstBalise.setString(2, rs.getString(3));
                        pstBalise.setString(3, rs.getString(2));
                        pstBalise.setString(4, rs.getString(3));
                        pstBalise.setString(5, rs.getString(2));
                        pstBalise.setString(6, rs.getString(3));
                        pstBalise.setString(7, rs.getString(2));
                        pstBalise.setString(8, rs.getString(3));
                        pstBalise.setString(9, rs.getString(2));
                        pstBalise.setString(10, rs.getString(3));
                        rsBalise = pstBalise.executeQuery();
                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
//                            System.out.println("VHF" + rs.getString(2));
                        }


                    }
                    if (k == 0) { // on retient le premierFix pour une star et une sid
                        premierFix = rs.getString(2);
                    }
                    k++; // on incremente le compteur
                } else {
//                    System.out.println("ICI");
                    System.out.println("k :" + k);
                    if (k == 0 && type.equals("SID")) {
                        indicateurNavaid = true;
                        System.out.println("on use le navaid");
                    }

//                    System.out.println("fix identifier vide");
                }
                lastFix = rs.getString(2);
//                System.out.println("FIN");
            } while (rs.next());
            if (type.equals("SID")) {
                ajoutSid = con.prepareStatement("INSERT INTO sid (identifiant, icaoCode, aeroportIdentifiant, runwayIdentifiant, lastFix, firstFix, latLong) VALUES (?, ?, ?, ?, ?, ?, ?)");
            } else if (type.equals("STAR")) {
                ajoutSid = con.prepareStatement("INSERT INTO star (identifiant, icaoCode, aeroportIdentifiant, runwayIdentifiant, lastFix, firstFix, latLong) VALUES (?, ?, ?, ?, ?, ?, ?)");
            }
            ajoutSid.setString(1, identifiant);
            ajoutSid.setString(2, icaoCode);
            ajoutSid.setString(3, aeroport);
            if (transitionIdentifier.length() > 0) {
                ajoutSid.setString(4, transitionIdentifier);
            } else {
                ajoutSid.setString(4, "");
            }
            ajoutSid.setString(5, lastFix);

            ajoutSid.setString(6, premierFix);



            String[][] tab = new String[couple.size()][2];
            for (int i = 0; i < couple.size(); i++) {
                tab[i][0] = couple.get(i).getLatitude();
                tab[i][1] = couple.get(i).getLongitude();
            }

            Array arraySql = con.createArrayOf("text", tab);
            ajoutSid.setArray(7, arraySql);
            ajoutSid.executeUpdate();
            couple.clear();


        } catch (SQLException ex) {
            Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ajoutSid != null) {
                try {
                    ajoutSid.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rsBalise != null) {
                try {
                    rsBalise.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pstBalise != null) {
                try {
                    pstBalise.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        }


    }

    /**
     * Get name of procedures of an airport
     *
     * @param aeroport ICAO Airport identifier
     * @param type SID or STAR
     * @return List of name
     */
    public static ArrayList<String> listStringProcedure(String aeroport, String type) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> tab = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            String icaoCode = aeroport.substring(0, 2);
            if (type.equals("SID")) {
                pst = con.prepareStatement("SELECT identifiant, runwayIdentifiant from sid where aeroportIdentifiant like ? and icaoCode like ?");
            } else if (type.equals("STAR")) {
                pst = con.prepareStatement("SELECT identifiant, runwayIdentifiant from star where aeroportIdentifiant like ? and icaoCode like ?");
            }
            if (pst != null) {
                pst.setString(1, aeroport);
                pst.setString(2, icaoCode);
                rs = pst.executeQuery();
            }

            while (rs != null && rs.next()) {
//                String piste = rs.getString(2);
//                if (!piste.equals("ALL")) {
//                    tab.add(rs.getString(1) + ", " + piste.substring(0, 4));
//                    
//                } else {
//                    tab.add(rs.getString(1) + ", " + piste);
//                }
                tab.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Procedure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return tab;
    }
}
