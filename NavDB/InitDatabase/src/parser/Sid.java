/*
 * Class to manage and to get information about Sid procedures
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
 * Class to manage and to get information about Sid procedures
 *
 * @author yoann
 */
public class Sid extends Procedure {

    /**
     * Route type
     */
    private String routeType;
    /**
     * Runway Type
     */
    private Piste rwy;
    /**
     * List of markers which blend the Sid
     */
    private ArrayList<Balises> balises;

    /**
     * Sid Constructor
     *
     * @param identifiant Sid identifier
     * @param rwy Runway
     */
    public Sid(String identifiant, Piste rwy) {
        super("SID", identifiant);
        this.rwy = rwy;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT * from procedure where typeProcedure like 'SID' and identifiant like ? and aeroportIdentifiant like ? and ((transitionIdentifier like ?) or transitionIdentifier like 'ALL') order by aeroportIdentifiant, identifiant, sequenceNumber");
            pst.setString(1, identifiant);
            pst.setString(2, rwy.getAeroport().getIdentifiant());
            pst.setString(3, rwy.getIdentifiant().substring(0, 4) + "%");
            rs = pst.executeQuery();
            this.balises = new ArrayList<>();
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
                            WayPoint wp = null;
                            if (rs.getString("subCodeFix").equals("A") && rs.getString("secCodeFix").equals("E")) {
                                //Waypoint en route
                                wp = WayPoint.createWayPoint(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"), "ENRT");
                            } else {
                                //Waypoint terminaux
                                wp = WayPoint.createWayPoint(rs.getString("fixIdentifiant"), rs.getString("icaoCodeFix"), rwy.getAeroport().getIdentifiant());
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
            Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst2 != null) {
                try {
                    pst2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Sid Constructor, used for display
     *
     * @param identifiant Sid identifier
     * @param balises List of markers coordinates
     */
    private Sid(String identifiant, ArrayList<LatitudeLongitude> balises) {
        super("SID", identifiant, balises);
    }

    /**
     * Sid Constructor, empty constructor, used during parsing
     */
    protected Sid() {
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
     * Get Runway
     *
     * @return Runway
     */
    public Piste getPiste() {
        return this.rwy;
    }

    /**
     * Get Markers which blend the SID
     *
     * @return List of markers
     */
    public ArrayList<Balises> getBalises() {
        return this.balises;
    }

    /**
     * Display SID information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherSid(OutputStreamWriter fw) throws IOException {
        fw.write("SID Identifier: " + this.getIdentifiant() + "\n");
        fw.write("Runway Identifier: " + this.rwy.getIdentifiant() + "\n");
        fw.write("Route Type: " + this.routeType + "\n");
    }

    /**
     * Fill SID table thanks to procedure table
     *
     * @param con Database connection
     */
    protected void creationSid(Connection con) {
        super.creationProcedures(con, "SID");
    }

    /**
     * Get all the SID of an airport
     *
     * @param aeroport Icao Airport Identifier
     * @return List of SIDs
     */
    public static ArrayList<Sid> requestSids(String aeroport) {
        Connection con = ParserGlobal.createSql();
        String icaoCode = aeroport.substring(0, 2);
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Sid> p = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT latLong, identifiant from sid where aeroportIdentifiant like ? and icaoCode like ?");
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
                p.add(new Sid(identifiant, l));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        }
        return p;
    }

    /**
     * Create Sid object
     *
     * @param aeroport ICAO Airport Identifier
     * @param identifiant Sid Identifier
     * @return SID object or null
     */
    public static Sid requestSid(String aeroport, String identifiant) {
        Connection con = null;
        String icaoCode = aeroport.substring(0, 2);
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<LatitudeLongitude> l = new ArrayList<>(); // list des balises
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT latLong, identifiant from sid where aeroportIdentifiant like ? and icaoCode like ? and identifiant like ?");
            pst.setString(1, aeroport);
            pst.setString(2, icaoCode);
            pst.setString(3, identifiant);
            rs = pst.executeQuery();

            if (rs != null && rs.next()) {
                Array a = rs.getArray(1);
                String[][] val = (String[][]) a.getArray();

                for (int i = 0; i < val.length; i++) {
                    l.add(new LatitudeLongitude(val[i][0], val[i][1]));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return new Sid(identifiant, l);
    }

    /**
     * List all the SID name of an airport
     *
     * @param aeroport Airport Identifier
     * @return List of SID name
     */
    public static ArrayList<String> listStringSid(String aeroport) {
        return Procedure.listStringProcedure(aeroport, "SID");
    }

    /**
     * List all the SID name of a runway
     *
     * @param aeroport ICAO Airport Identifier
     * @param piste Runway
     * @return List of Sid Name
     */
    public static ArrayList<String> listSidPiste(String aeroport, String piste) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> sid = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT identifiant from sid where runwayIdentifiant like ? and aeroportIdentifiant like ?");

            pst.setString(1, "RW" + piste.substring(0, 2) + "%");
            pst.setString(2, aeroport);
            rs = pst.executeQuery();
            while (rs.next()) {
                sid.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Sid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return sid;
    }
}
