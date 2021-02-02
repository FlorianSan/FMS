/*
 * Class to manage and to get information about Star
 */
package parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get information about Star
 * @author yoann
 */
public class Star extends Procedure {

    /**
     * Route Type
     */
    private String routeType;
    /**
     * Transition Identifier
     */
    private String transitionIdentifier;
    /**
     * List of markers which blend the STAR
     */
    private ArrayList<Balises> balises;
    /**
     * Airport
     */
    private Aeroport arpt;

    /**
     * STAR Constructor, used for display
     * @param identifiant Star identifier
     * @param balises List of markers coordinates which blend the Star
     */
    private Star(String identifiant, ArrayList<LatitudeLongitude> balises) {
        super("STAR", identifiant, balises);
    }

    /**
     * Star constructor, empty for parsing
     */
    protected Star() {
    }

    /**
     * Star Constructor
     * @param identifiant Star Identifier 
     */
    public Star(String identifiant) {
        super("STAR", identifiant);
    }

    /**
     * Star Constructor used in flight planning
     * @param identifiant Star Identifier
     * @param arpt Airport
     */
    public Star(String identifiant, Aeroport arpt) {
        super("STAR", identifiant);
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from procedure where typeProcedure like 'STAR' and identifiant like ? and aeroportIdentifiant like ? order by aeroportIdentifiant, identifiant, sequenceNumber");
            pst.setString(1, identifiant);
            pst.setString(2, arpt.getIdentifiant());
            rs = pst.executeQuery();
            this.arpt = arpt;
            this.balises = new ArrayList<>();
            while (rs.next()) {
                this.routeType = rs.getString("routeType");
                this.transitionIdentifier = rs.getString("transitionIdentifier");
                // il faut recuperer le nom des balises et les construire
                pst2 = con.prepareStatement("SELECT p.relname from pg_class p, balises b where b.tableoid=p.oid and identifiant like ? and icaoCode like ?");
                pst2.setString(1, rs.getString("fixIdentifiant"));
                pst2.setString(2, rs.getString("icaoCodeFix"));
                rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    //Aigullage selon le nom de la table ou se trouve le fix
                    switch (rs2.getString(1)) {
                        case "waypoint":
                            WayPoint wp = null;
                            if (rs.getString("subCodeFix").equals("A") && rs.getString("secCodeFix").equals("E")) {
                                //Waypoint en route
                                System.out.println(" wp " + rs.getString("fixIdentifiant") + rs.getString("icaoCode"));
                                wp = WayPoint.createWayPoint(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"), "ENRT");
                            } else {
                                //Waypoint terminaux
                                wp = WayPoint.createWayPoint(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"), arpt.getIdentifiant());
                            }
                            this.balises.add(wp);
                            break;
                        case "vor":
                            // type vor
                            if (rs.getString("secCodeFix").equals("D")) {
                                Vor v = Vor.createVor(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                this.balises.add(v);
                            }
                            break;
                        case "dme":
                            // type dme
                            if (rs.getString("secCodeFix").equals("D")) {
                                Dme d = Dme.createDme(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                this.balises.add(d);
                            }
                            break;
                        case "vordme":
                            // type vorDme
                            if (rs.getString("secCodeFix").equals("D")) {
                                VorDme v = VorDme.createVorDme(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                this.balises.add(v);
                            }
                            break;
                        case "ilsdme":
                            // type ilsDme
                            if (rs.getString("secCodeFix").equals("D")) {
                                IlsDme i = IlsDme.createIlsDme(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                this.balises.add(i);
                            }
                            break;
                        case "ndb":
                            // type ndb
                            if (rs.getString("secCodeFix").equals("D") && rs.getString("subCodeFix").equals("B")) {
                                Ndb n = Ndb.createNdb(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                this.balises.add(n);
                            }
                            break;
                        case "tacan":
                            // type tacan
                            if (rs.getString("secCodeFix").equals("D")) {
                                Tacan t = Tacan.createTacan(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"));
                                this.balises.add(t);
                            }
                            break;
                        default:
                            System.out.println("Non reconnu");
                            break;
                    }

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs2 != null){
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(pst2 != null){
                try {
                    pst2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /**
     * Get Markers which blend the STAR
     * @return List of Markers
     */
    public ArrayList<Balises> getBalises() {
        return this.balises;
    }
    /**
     * Get Airport
     * @return Airport
     */
    public Aeroport getAeroport(){
        return this.arpt;
    }
    /**
     * Get Route Type
     * @return Route Type
     */
    public String getRouteType(){
        return this.routeType;
    }
    /**
     * Get Transition Identifier
     * @return Transition Identifier
     */
    public String getTransitionIdentifier(){
        return this.transitionIdentifier;
    }
    
    /**
     * Display STAR information in a file
     * @param fw
     * @throws IOException 
     */
    public void afficherStar(OutputStreamWriter fw) throws IOException {
        fw.write("SID Identifier: " + this.getIdentifiant() + "\n");
        fw.write("Airport ICAO Code: " + this.arpt.getIdentifiant() + "\n");
        fw.write("Route Type: " + this.routeType + "\n");
        fw.write("Transition Identifier: " + this.transitionIdentifier + "\n");
    }

    /**
     * Fill STAR table thanks procedure table
     *
     * @param con database connection
     */
    protected void creationStar(Connection con) {
        super.creationProcedures(con, "STAR");
    }

    /**
     * Get all the STAR of an airport
     *
     * @param aeroport ICAO Airport identifier
     * @return List of Star
     */
    public static ArrayList<Star> requestStars(String aeroport) {
        Connection con = null;
        String icaoCode = aeroport.substring(0, 2);
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Star> p = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT latLong, identifiant from star where aeroportIdentifiant like ? and icaoCode like ?");
            pst.setString(1, aeroport);
            pst.setString(2, icaoCode);
            rs = pst.executeQuery();

            while (rs.next()) {
                Array a = rs.getArray(1);
                String identifiant = rs.getString(2);
                String[][] val = (String[][]) a.getArray();
                ArrayList<LatitudeLongitude> l = new ArrayList<>(); // list des balises
                for (int i = 0; i < val.length; i++) {
                    l.add(new LatitudeLongitude(val[i][0], val[i][1]));
                }
                p.add(new Star(identifiant, l));

            }

        } catch (SQLException ex) {
            Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return p;
    }

    /**
     * Get STAR object
     *
     * @param aeroport ICAO Airport Identifier
     * @param identifiant STAR Identifier
     * @return Objet Star or null in case of failure
     */
    public static Star requestStar(String aeroport, String identifiant) {
        String icaoCode = aeroport.substring(0, 2);
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<LatitudeLongitude> l = new ArrayList<>(); // list des balises
        Connection con = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT latLong, identifiant from star where aeroportIdentifiant like ? and icaoCode like ? and identifiant like ?");
            pst.setString(1, aeroport);
            pst.setString(2, icaoCode);
            pst.setString(3, identifiant);
            rs = pst.executeQuery();

            if (rs.next()) {
                Array a = rs.getArray(1);
                String[][] val = (String[][]) a.getArray();

                for (int i = 0; i < val.length; i++) {
                    l.add(new LatitudeLongitude(val[i][0], val[i][1]));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return new Star(identifiant, l);
    }

    /**
     * List STAR name of an airport
     *
     * @param aeroport ICAO Airport Identifier
     * @return List of STAR name
     */
    public static ArrayList<String> listStringStar(String aeroport) {
        return Procedure.listStringProcedure(aeroport, "STAR");
    }

    /**
     * List runway name for a STAR
     * @param aeroport ICAO Airport Identifier
     * @param star STAR Identifier
     * @return List of runway names
     */
    public static ArrayList<String> listStringStarPiste(String aeroport, String star) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> l = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT runwayIdentifiant from star where identifiant like ? and aeroportIdentifiant like ?");
            pst.setString(1, star);
            pst.setString(2, aeroport);
            rs = pst.executeQuery();
            if (rs.next()) {
                String idPiste = rs.getString(1); // forme RW07 ou ALL

                if (idPiste.equals("ALL")) {
                    // toutes les pistes
                    l = Piste.requestPistesAeroport(aeroport);
                } else {
                    // piste format RW00
                    String qfu = idPiste.substring(2, 4);
                    System.out.println("QFU " + qfu);
                    l = Piste.requestPisteAeroportQfu(aeroport, qfu);
                }

            }


        } catch (SQLException ex) {
            Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return l;
    }
}
