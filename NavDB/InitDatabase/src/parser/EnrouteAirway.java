/*
 * Class to manage and to get information about Enroute Airways
 */
package parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get information about Enroute Airways
 *
 * @author yoann
 */
public class EnrouteAirway {

    /**
     * Route Identifier
     */
    private String identifiant;
    /**
     * List of marker coordinates (Latitude, Longitude) for display
     */
    private ArrayList<LatitudeLongitude> balises;
    /**
     * List of Marker objects which blend the route
     */
    private ArrayList<Balises> bal;
    /**
     * Route Type
     */
    private String routeType;

    /**
     * EnrouteAirway Constructor used for display
     *
     * @param identifiant Route Identifier
     * @param balises List of Markers coordinates
     */
    public EnrouteAirway(String identifiant, ArrayList<LatitudeLongitude> balises) {
        this.identifiant = identifiant;
        this.balises = balises;
    }

    /**
     * EnrouteAirway constructor used for the flight planning
     *
     * @param identifiant Route identifier
     * @param baliseDebut First Marker of the route
     * @param baliseFin Last Marker of the route
     */
    public EnrouteAirway(String identifiant, String baliseDebut, String baliseFin) {
        this.identifiant = identifiant;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        PreparedStatement pst3 = null;
        ResultSet rs3 = null;
        PreparedStatement pst4 = null;
        ResultSet rs4 = null;
        try {
            con = ParserGlobal.createSql();
            pst3 = con.prepareStatement("SELECT sequenceNumber FROM route WHERE routeIdentifiant like ? and fixIdentifiant like ? order by sequenceNumber");
            pst3.setString(1, identifiant);
            pst3.setString(2, baliseDebut);
            rs3 = pst3.executeQuery();


            pst4 = con.prepareStatement("SELECT sequenceNumber FROM route WHERE routeIdentifiant like ? and fixIdentifiant like ? order by sequenceNumber");
            pst4.setString(1, identifiant);
            pst4.setString(2, baliseFin);
            rs4 = pst4.executeQuery();

            if (rs3.next() && rs4.next()) {
                // le min et le max en sequenceNumber
                String minSeqNumber = null;
                String maxSeqNumber = null;
                boolean reverseList = false;
                if (Integer.parseInt(rs4.getString("sequenceNumber")) > Integer.parseInt(rs3.getString("sequenceNumber"))) {
                    minSeqNumber = rs3.getString("sequenceNumber");
                    maxSeqNumber = rs4.getString("sequenceNumber");


                } else {
                    minSeqNumber = rs4.getString("sequenceNumber");
                    maxSeqNumber = rs3.getString("sequenceNumber");
                    reverseList = true;
                }

                pst = con.prepareStatement("SELECT fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix, routeType FROM route where routeIdentifiant like ? and sequenceNumber<=? and sequenceNumber>=? order by sequenceNumber");
                pst.setString(1, identifiant);
                pst.setString(2, maxSeqNumber);
                pst.setString(3, minSeqNumber);
                rs = pst.executeQuery();
                this.bal = new ArrayList<>();
                while (rs.next()) {
                    this.routeType = rs.getString("routeType");
                    // il faut recuperer le nom des balises et les construire
                    pst2 = con.prepareStatement("SELECT p.relname from pg_class p, balises b where b.tableoid=p.oid and identifiant like ? and icaoCode like ?");
                    pst2.setString(1, rs.getString("fixIdentifiant"));
                    pst2.setString(2, rs.getString("icaoCodeFix"));
                    rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        //Aigullage selon le nom de la table ou se trouve le fix
                        switch (rs2.getString(1)) {
                            case "waypoint":
                                if (rs.getString("subCodeFix").equals("A") && rs.getString("secCodeFix").equals("E")) {
                                    //Waypoint en route
                                    WayPoint wp = WayPoint.createWayPoint(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"), "ENRT");
                                    this.bal.add(wp);
                                }
                                break;
                            case "vor":
                                // type vor
                                if (rs.getString("secCodeFix").equals("D")) {
                                    Vor v = Vor.createVor(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                    this.bal.add(v);
                                }
                                break;
                            case "dme":
                                // type dme
                                if (rs.getString("secCodeFix").equals("D")) {
                                    Dme d = Dme.createDme(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                    this.bal.add(d);
                                }
                                break;
                            case "vordme":
                                // type vorDme
                                if (rs.getString("secCodeFix").equals("D")) {
                                    VorDme v = VorDme.createVorDme(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                    this.bal.add(v);
                                }
                                break;
                            case "ilsdme":
                                // type ilsDme
                                if (rs.getString("secCodeFix").equals("D")) {
                                    IlsDme i = IlsDme.createIlsDme(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                    this.bal.add(i);
                                }
                                break;
                            case "ndb":
                                // type ndb
                                if (rs.getString("secCodeFix").equals("D") && rs.getString("subCodeFix").equals("B")) {
                                    Ndb n = Ndb.createNdb(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                    this.bal.add(n);
                                }
                                break;
                            case "tacan":
                                // type tacan
                                if (rs.getString("secCodeFix").equals("D")) {
                                    Tacan t = Tacan.createTacan(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                    this.bal.add(t);
                                }
                                break;
                            default:
                                System.out.println("Non reconnu");
                                break;
                        }

                    }

                }
                // on regarde si la liste est dans le bon ordre
                if (reverseList) {
                    Collections.reverse(this.bal);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst2 != null) {
                try {
                    pst2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs3 != null) {
                try {
                    rs3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst3 != null) {
                try {
                    pst3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs4 != null) {
                try {
                    rs4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst4 != null) {
                try {
                    pst4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get Route identifier
     *
     * @return Route identifier
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Get the list of Marker coordinates
     *
     * @return List of Marker coordinates
     */
    public ArrayList<LatitudeLongitude> getBalise() {
        return this.balises;
    }

    /**
     * Get Route Type
     *
     * @return Route Type
     */
    public String getRouteType() {
        return this.routeType;
    }

    /**
     * Get Marker List
     *
     * @return Marker List
     */
    public ArrayList<Balises> getBalises() {
        return this.bal;
    }

    /**
     * Add new EnrouteAirway record in the database
     *
     * @param con Database Connection
     * @param routeIdentifiant Route Identifier
     * @param sequenceNumber Sequence Number
     * @param fixIdentifiant Fix identifier
     * @param icaoCodeFix Fix Icao Code
     * @param secCodeFix Section Code for the Fix
     * @param subCodeFix SubSection Code for the Fix
     * @param descriptionCode Description Code
     * @param boundaryCode Boundary Code
     * @param routeType Route Type
     * @param levelRoute Route Level
     * @param direct Direction Restriction
     * @param cruiseTableIdentifier Cruise Table Identifier
     * @param euIndicator EU Indicator
     * @param receiverVhf Recommended VHF
     * @param icaoCodeVhf ICAO Code of VHF
     * @param requiredNavigation
     * @param theta Theta
     * @param rho Rho
     * @param outboundMagneticCruise Outbound Magnetic Cruise
     * @param routeFromDistance Route From Distance
     * @param inboundMagneticCruise Inbound Magnetic Cruise
     * @param minAltitude Minimum Altitude
     * @param minAltitude2 Minimun Altitude
     * @param maxAltitude Maximum Altitude
     * @param fixRadius Fix Radius
     */
    protected static void addSql(Connection con, String routeIdentifiant, String sequenceNumber, String fixIdentifiant, String icaoCodeFix, String secCodeFix,
            String subCodeFix, String descriptionCode, String boundaryCode, String routeType, String levelRoute, String direct,
            String cruiseTableIdentifier, String euIndicator, String receiverVhf, String icaoCodeVhf, String requiredNavigation,
            String theta, String rho, String outboundMagneticCruise, String routeFromDistance, String inboundMagneticCruise,
            String minAltitude, String minAltitude2, String maxAltitude, String fixRadius) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO route (routeIdentifiant, sequenceNumber, fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix, descriptionCode, boundaryCode, routeType, levelRoute, direct, cruiseTableIdentifier, euIndicator, receiverVhf, icaoCodeVhf, requiredNavigation, theta, rho, outboundMagneticCruise, routeFromDistance, inboundMagneticCruise, minAltitude, minAltitude2, maxAltitude, fixRadius) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, routeIdentifiant);
            pst.setString(2, sequenceNumber);
            pst.setString(3, fixIdentifiant);
            pst.setString(4, icaoCodeFix);
            pst.setString(5, secCodeFix);
            pst.setString(6, subCodeFix);
            pst.setString(7, descriptionCode);
            pst.setString(8, boundaryCode);
            pst.setString(9, routeType);
            pst.setString(10, levelRoute);
            pst.setString(11, direct);
            pst.setString(12, cruiseTableIdentifier);
            pst.setString(13, euIndicator);
            pst.setString(14, receiverVhf);
            pst.setString(15, icaoCodeVhf);
            pst.setString(16, requiredNavigation);
            pst.setString(17, theta);
            pst.setString(18, rho);
            pst.setString(19, outboundMagneticCruise);
            pst.setString(20, routeFromDistance);
            pst.setString(21, inboundMagneticCruise);
            pst.setString(22, minAltitude);
            pst.setString(23, minAltitude2);
            pst.setString(24, maxAltitude);
            pst.setString(25, fixRadius);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Get the coordinate list of the marker which blend the route, Used only
     * for the display
     *
     * @param identifiant Route identifier
     * @return List of LatitudeLongitude objects
     */
    public static ArrayList<LatitudeLongitude> requestEnrouteAirway(String identifiant) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        ArrayList<LatitudeLongitude> l = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix FROM route where routeIdentifiant like ?");
            pst.setString(1, identifiant);
            rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getString(3).equals("D") && rs.getString(4).equals("B")) {
                    //NDB
                    pst2 = con.prepareStatement("SELECT latitude, longitude from ndb where identifiant=? and icaoCode=?");
                    pst2.setString(1, rs.getString(1));
                    pst2.setString(2, rs.getString(2));
                    rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                    }
                } else if (rs.getString(3).equals("E") && rs.getString(4).equals("A")) {
                    //Waypoint                  
                    pst2 = con.prepareStatement("SELECT latitude, longitude from waypoint where identifiant=? and icaoCode=? and aeroport like 'ENRT'");
                    pst2.setString(1, rs.getString(1));
                    pst2.setString(2, rs.getString(2));
                    rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                    }

                } else if (rs.getString(3).equals("P") && rs.getString(4).equals("C")) {
                    //Waypoint                  
                    pst2 = con.prepareStatement("SELECT latitude, longitude from waypoint where identifiant=? and icaoCode=? and not(aeroport like 'ENRT')");
                    pst2.setString(1, rs.getString(1));
                    pst2.setString(2, rs.getString(2));
                    rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                    }

                } else if (rs.getString(3).equals("D") && rs.getString(4).equals("")) {
                    //VHF
                    pst2 = con.prepareStatement("(SELECT latitude, longitude from vor where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from vorDme where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from dme where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from tacan where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from ilsdme where identifiant=? and icaoCode=?)");
                    pst2.setString(1, rs.getString(1));
                    pst2.setString(2, rs.getString(2));
                    pst2.setString(3, rs.getString(1));
                    pst2.setString(4, rs.getString(2));
                    pst2.setString(5, rs.getString(1));
                    pst2.setString(6, rs.getString(2));
                    pst2.setString(7, rs.getString(1));
                    pst2.setString(8, rs.getString(2));
                    pst2.setString(9, rs.getString(1));
                    pst2.setString(10, rs.getString(2));
                    rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                    }

                } else {
                    System.out.println("Enroute Airways Pas possible");
                }
            }


        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst2 != null) {
                try {
                    pst2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return l;
    }

    /**
     * Get the markers coordinates of a route
     *
     * @param identifiant Route Identifier
     * @param baliseDebut First marker of the route
     * @param baliseFin Last marker of the route
     * @return List of LatitudeLongitude objects
     */
    public static ArrayList<LatitudeLongitude> requestEnrouteAirway(String identifiant, String baliseDebut, String baliseFin) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        PreparedStatement pst3 = null;
        ResultSet rs3 = null;
        PreparedStatement pst4 = null;
        ResultSet rs4 = null;
        ArrayList<LatitudeLongitude> l = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();

            pst3 = con.prepareStatement("SELECT sequenceNumber FROM route where routeIdentifiant like ? and fixIdentifiant like ? order by sequenceNumber");
            pst3.setString(1, identifiant);
            pst3.setString(2, baliseDebut);
            rs3 = pst3.executeQuery();

            pst4 = con.prepareStatement("SELECT sequenceNumber FROM route where routeIdentifiant like ? and fixIdentifiant like ? order by sequenceNumber");
            pst4.setString(1, identifiant);
            pst4.setString(2, baliseFin);
            rs4 = pst4.executeQuery();

            if (rs4.next() && rs3.next()) {


                //Il faut trouver le min et le max des seq Number
                String maxSequenceNumber = null;
                String minSequenceNumber = null;
                boolean reverseList = false;

                if (Integer.parseInt(rs3.getString("sequenceNumber")) > Integer.parseInt(rs4.getString("sequenceNumber"))) {
                    maxSequenceNumber = rs3.getString("sequenceNumber");
                    minSequenceNumber = rs4.getString("sequenceNumber");
                    reverseList = true;
                } else {
                    maxSequenceNumber = rs4.getString("sequenceNumber");
                    minSequenceNumber = rs3.getString("sequenceNumber");

                }

                pst = con.prepareStatement("SELECT fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix FROM route where routeIdentifiant like ? and (sequenceNumber>= ? and sequenceNumber<= ?) ");
                pst.setString(1, identifiant);
                pst.setString(2, minSequenceNumber);
                pst.setString(3, maxSequenceNumber);
                rs = pst.executeQuery();

                while (rs.next()) {
                    if (rs.getString(3).equals("D") && rs.getString(4).equals("B")) {
                        //NDB
                        pst2 = con.prepareStatement("SELECT latitude, longitude from ndb where identifiant=? and icaoCode=?");
                        pst2.setString(1, rs.getString(1));
                        pst2.setString(2, rs.getString(2));
                        rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                        }
                    } else if (rs.getString(3).equals("E") && rs.getString(4).equals("A")) {
                        //Waypoint                  
                        pst2 = con.prepareStatement("SELECT latitude, longitude from waypoint where identifiant=? and icaoCode=? and aeroport like 'ENRT'");
                        pst2.setString(1, rs.getString(1));
                        pst2.setString(2, rs.getString(2));
                        rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                        }

                    } else if (rs.getString(3).equals("P") && rs.getString(4).equals("C")) {
                        //Waypoint                  
                        pst2 = con.prepareStatement("SELECT latitude, longitude from waypoint where identifiant=? and icaoCode=? and not(aeroport like 'ENRT')");
                        pst2.setString(1, rs.getString(1));
                        pst2.setString(2, rs.getString(2));
                        rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                        }

                    } else if (rs.getString(3).equals("D") && rs.getString(4).equals("")) {
                        //VHF
                        pst2 = con.prepareStatement("(SELECT latitude, longitude from vor where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from vorDme where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from dme where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from tacan where identifiant=? and icaoCode=?)UNION(SELECT latitude, longitude from ilsdme where identifiant=? and icaoCode=?)");
                        pst2.setString(1, rs.getString(1));
                        pst2.setString(2, rs.getString(2));
                        pst2.setString(3, rs.getString(1));
                        pst2.setString(4, rs.getString(2));
                        pst2.setString(5, rs.getString(1));
                        pst2.setString(6, rs.getString(2));
                        pst2.setString(7, rs.getString(1));
                        pst2.setString(8, rs.getString(2));
                        pst2.setString(9, rs.getString(1));
                        pst2.setString(10, rs.getString(2));
                        rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            l.add(new LatitudeLongitude(rs2.getString(1), rs2.getString(2)));
                        }

                    } else {
                        System.out.println("Enroute Airways Pas possible");
                    }
                }

                // on regarde si la liste est dans le bon ordre sinon on l'inverse
                if (reverseList) {
                    Collections.reverse(l);
                }
            }


        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst2 != null) {
                try {
                    pst2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs3 != null) {
                try {
                    rs3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst3 != null) {
                try {
                    pst3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs4 != null) {
                try {
                    rs4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst4 != null) {
                try {
                    pst4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return l;
    }

    /**
     * Check if a route exists
     *
     * @param route Route Identifier
     * @return True or False
     */
    public static boolean isPresent(String route) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT routeIdentifiant from route where routeIdentifiant like ?");
            pst.setString(1, route);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    /**
     * Ckeck if the identifier is a route or a marker
     *
     * @param identifiant Marker or route identifier
     * @return True or False
     */
    public static boolean isPresentRouteOrBalises(String identifiant) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT routeIdentifiant from route where routeIdentifiant like ? UNION SELECT identifiant FROM balises* where identifiant like ?");
            pst.setString(1, identifiant);
            pst.setString(2, identifiant);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    /**
     * Get the name of markers which blend the route
     *
     * @param route Route Identifier
     * @return List of marker names
     */
    public static ArrayList<String> listStringBalises(String route) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> a = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT fixIdentifiant FROM route WHERE routeIdentifiant like ? order by fixIdentifiant");
            pst.setString(1, route);
            rs = pst.executeQuery();
            while (rs.next()) {
                a.add(rs.getString("fixIdentifiant"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnrouteAirway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }
}