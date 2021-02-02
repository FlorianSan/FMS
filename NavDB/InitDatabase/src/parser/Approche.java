/**
 * Class to manage and to get approach information
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get approach information
 *
 * @author yoann
 */
public class Approche extends Procedure {

    /**
     * List of markers which blend the approach
     */
    private ArrayList<Balises> balises;
    /**
     * Route Type
     */
    private String routeType;
    /**
     * Runway
     */
    private Piste rwy;
    /**
     * Approach Type
     */
    private String typeAppr;

    /**
     * Use only for the display
     *
     * @param identifiantRunway Runway Identifier
     * @param balises List of Markers
     */
    private Approche(String identifiantRunway, ArrayList<LatitudeLongitude> balises) {
        super("APPR", identifiantRunway, balises);
    }

    /**
     * Empty Constructor, used for the parsing
     */
    protected Approche() {
    }

    /**
     * Approach Contructor
     *
     * @param s Star
     * @param rwy Runway
     */
    public Approche(Star s, Piste rwy) {
        super("APPR", rwy.getIdentifiant());
        this.rwy = rwy;
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
            pst = con.prepareStatement("SELECT lastFix from star where identifiant like ? and aeroportIdentifiant like ? order by aeroportIdentifiant, identifiant");
            System.out.println("identifiant star : " + s.getIdentifiant());
            System.out.println("identifiant runway : " + rwy.getIdentifiant());
            this.balises = new ArrayList<>();
            pst.setString(1, s.getIdentifiant());
            pst.setString(2, rwy.getAeroport().getIdentifiant());
            rs = pst.executeQuery(); // contient le lastFix
            String idPiste = rwy.getIdentifiant();
            String idSansRw = idPiste.substring(2, idPiste.length());
            if (rs.next()) {
                // on recupere le type d'approche
                pst4 = con.prepareStatement("SELECT typeApproche from appr where runwayIdentifiant like ? and aeroportIdentifiant like ? and premierPoint like ? order by typeApproche");
                pst4.setString(1, "_" + idSansRw);
                pst4.setString(2, rwy.getAeroport().getIdentifiant());
                pst4.setString(3, rs.getString("lastFix"));
                rs4 = pst4.executeQuery();
                if (rs4.next()) {
                    pst2 = con.prepareStatement("SELECT * from procedure where typeProcedure like 'APPR' and aeroportIdentifiant like ? and identifiant like ? and transitionIdentifier like ? order by aeroportIdentifiant, identifiant, sequenceNumber");
                    pst2.setString(1, s.getAeroport().getIdentifiant());
                    System.out.println("id sans rwy :" + idSansRw);
                    pst2.setString(2, rs4.getString("typeApproche") + idSansRw);
                    System.out.println("type approche :" + rs4.getString("typeApproche") + idSansRw);
                    pst2.setString(3, rs.getString("lastFix"));
                    rs2 = pst2.executeQuery();

                    this.typeAppr = rs4.getString("typeApproche");
                    while (rs2.next()) {
                        System.out.println("Fix :" + rs2.getString("fixIdentifiant"));
                        this.routeType = rs2.getString("routeType");
                        // il faut recuperer le nom des balises et les construire
                        pst3 = con.prepareStatement("SELECT p.relname from pg_class p, balises b where b.tableoid=p.oid and identifiant like ? and icaoCode like ?");
                        pst3.setString(1, rs2.getString("fixIdentifiant"));
                        pst3.setString(2, rs2.getString("icaoCodeFix"));
                        rs3 = pst3.executeQuery();
                        if (rs3.next()) {
                            //Aigullage selon le nom de la table ou se trouve le fix
                            switch (rs3.getString(1)) {
                                case "waypoint":
                                    WayPoint wp = null;
                                    if (rs2.getString("subCodeFix").equals("A") && rs2.getString("secCodeFix").equals("E")) {
                                        //Waypoint en route
                                        System.out.println(" wp " + rs2.getString("fixIdentifiant") + rs2.getString("icaoCode"));
                                        wp = WayPoint.createWayPoint(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"), "ENRT");
                                    } else {
                                        //Waypoint terminaux
                                        wp = WayPoint.createWayPoint(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"), rwy.getAeroport().getIdentifiant());
                                    }
                                    this.balises.add(wp);
                                    break;
                                case "vor":
                                    // type vor
                                    if (rs2.getString("secCodeFix").equals("D")) {
                                        Vor v = Vor.createVor(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"));
                                        this.balises.add(v);
                                    }
                                    break;
                                case "dme":
                                    // type dme
                                    if (rs2.getString("secCodeFix").equals("D")) {
                                        Dme d = Dme.createDme(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"));
                                        this.balises.add(d);
                                    }
                                    break;
                                case "vordme":
                                    // type vorDme
                                    if (rs2.getString("secCodeFix").equals("D")) {
                                        VorDme v = VorDme.createVorDme(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"));
                                        this.balises.add(v);
                                    }
                                    break;
                                case "ilsdme":
                                    // type ilsDme
                                    if (rs2.getString("secCodeFix").equals("D")) {
                                        IlsDme i = IlsDme.createIlsDme(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"));
                                        this.balises.add(i);
                                    }
                                    break;
                                case "ndb":
                                    // type ndb
                                    if (rs2.getString("secCodeFix").equals("D") && rs2.getString("subCodeFix").equals("B")) {
                                        Ndb n = Ndb.createNdb(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"));
                                        this.balises.add(n);
                                    }
                                    break;
                                case "tacan":
                                    // type tacan
                                    if (rs.getString("secCodeFix").equals("D")) {
                                        Tacan t = Tacan.createTacan(rs2.getString("fixIdentifiant"), rs2.getString("icaoCodeFix"));
                                        this.balises.add(t);
                                    }
                                    break;
                                default:
                                    System.out.println("Non reconnu");
                                    break;
                            }

                        }


                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs4 != null) {
                try {
                    rs4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst4 != null) {
                try {
                    pst4.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs3 != null) {
                try {
                    rs3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst3 != null) {
                try {
                    pst3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst2 != null) {
                try {
                    pst2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
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
     * Get the List of Markers
     *
     * @return The List of Markers
     */
    public ArrayList<Balises> getBalises() {
        return this.balises;
    }

    /**
     * Get the runway
     *
     * @return The Runway
     */
    public Piste getPiste() {
        return this.rwy;
    }

    /**
     * Display Approach Information in a file
     *
     * @param fw
     * @throws IOException
     */
    public void afficherApproche(OutputStreamWriter fw) throws IOException {
        fw.write("Approach Type: " + this.typeAppr + "\n");
        fw.write("Runway Identifier: " + this.rwy.getIdentifiant() + "\n");
        fw.write("Route Type: " + this.routeType + "\n");
    }

    /**
     * Fill the Approach table with the procedure data
     *
     * @param con Database Connection
     */
    protected void creationAppr(Connection con) {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT  identifiant, transitionIdentifier, fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix, aeroportIdentifiant, icaoCode, sequenceNumber FROM procedure where typeProcedure like 'APPR' ORDER BY aeroportIdentifiant, identifiant, transitionIdentifier ,sequenceNumber asc");
            ResultSet rs = pst.executeQuery();
            rs.next();
            String runwayIdentifiant = rs.getString(1);
            String transitionIdentifier = rs.getString(2);
            String aeroport = rs.getString(7);
            String icaoCode = rs.getString(8);
            String premierPoint = null;
            if (transitionIdentifier.equals("")) {
                premierPoint = rs.getString(3);
            } else {
                premierPoint = transitionIdentifier;
            }

            String sequenceNumber = rs.getString(9);
            List<LatitudeLongitude> couple = new ArrayList<>();
            do {
                System.out.println("Premier point" + premierPoint);

                if (!transitionIdentifier.equals(rs.getString(2)) && !sequenceNumber.equals(rs.getString(9))) {
                    System.out.println("Ajout bdd de " + premierPoint);
                    // ajout en bdd du precedent
                    PreparedStatement ajoutSid = con.prepareStatement("INSERT INTO appr (runwayIdentifiant, typeApproche, aeroportIdentifiant, icaoCode, premierPoint ,balises) VALUES (?,CAST(? AS typeAppr),?,?,?,?)");
                    ajoutSid.setString(1, runwayIdentifiant);
                    ajoutSid.setString(2, runwayIdentifiant.substring(0, 1));
                    ajoutSid.setString(3, aeroport);
                    ajoutSid.setString(4, icaoCode);
                    ajoutSid.setString(5, premierPoint);


                    String[][] tab = new String[couple.size()][2];
                    for (int i = 0; i < couple.size(); i++) {
                        tab[i][0] = couple.get(i).getLatitude();
                        tab[i][1] = couple.get(i).getLongitude();
                    }

                    Array arraySql = con.createArrayOf("text", tab);
                    ajoutSid.setArray(6, arraySql);
                    ajoutSid.executeUpdate();
                    couple.clear();

                    // on update l'identifiant ds la boucle
                    runwayIdentifiant = rs.getString(1);
                    transitionIdentifier = rs.getString(2);
                    aeroport = rs.getString(7);
                    icaoCode = rs.getString(8);
                    if (transitionIdentifier.equals("")) {
                        premierPoint = rs.getString(3);
                    } else {
                        premierPoint = transitionIdentifier;
                    }
                }

                ResultSet rsBalise = null;
                if (!rs.getString(3).equals("")) {
                    if (rs.getString(5).equals("D") && rs.getString(6).equals("B")) {
                        //NDB
                        PreparedStatement pstBalise = con.prepareStatement("SELECT latitude, longitude FROM ndb where identifiant like ? and icaoCode like ?");
                        pstBalise.setString(1, rs.getString(3));
                        pstBalise.setString(2, rs.getString(4));
                        rsBalise = pstBalise.executeQuery();
                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
                            System.out.println("NDB" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();

                    } else if (rs.getString(5).equals("E") && rs.getString(6).equals("A")) {
                        //waypoint en route
                        PreparedStatement pstBalise = con.prepareStatement("SELECT latitude, longitude FROM waypoint where identifiant like ? and icaoCode like ? and aeroport like 'ENRT'");
                        pstBalise.setString(1, rs.getString(3));
                        pstBalise.setString(2, rs.getString(4));
                        rsBalise = pstBalise.executeQuery();

                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
                            System.out.println("WayPoint" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();

                    } else if (rs.getString(5).equals("P") && rs.getString(6).equals("C")) {
                        //waypoint terminaux
                        PreparedStatement pstBalise = con.prepareStatement("SELECT latitude, longitude FROM waypoint where identifiant like ? and icaoCode like ? and aeroport like ?");
                        pstBalise.setString(1, rs.getString(3));
                        pstBalise.setString(2, rs.getString(4));
                        pstBalise.setString(3, rs.getString(7));
                        rsBalise = pstBalise.executeQuery();

                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
                            System.out.println("WayPoint" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();
                    } else if (rs.getString(5).equals("D") && rs.getString(6).equals("")) {
                        //VHF
                        PreparedStatement pstBalise = con.prepareStatement("(SELECT latitude, longitude from vor where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from vorDme where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from dme where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from tacan where identifiant=? and icaoCode=?)UNION(SELECT latitude,longitude from ilsdme where identifiant=? and icaoCode=?)");
                        pstBalise.setString(1, rs.getString(3));
                        pstBalise.setString(2, rs.getString(4));
                        pstBalise.setString(3, rs.getString(3));
                        pstBalise.setString(4, rs.getString(4));
                        pstBalise.setString(5, rs.getString(3));
                        pstBalise.setString(6, rs.getString(4));
                        pstBalise.setString(7, rs.getString(3));
                        pstBalise.setString(8, rs.getString(4));
                        pstBalise.setString(9, rs.getString(3));
                        pstBalise.setString(10, rs.getString(4));
                        rsBalise = pstBalise.executeQuery();
                        if (rsBalise.next()) {
                            couple.add(new LatitudeLongitude(rsBalise.getString(1), rsBalise.getString(2)));
                            System.out.println("VHF" + rs.getString(2));
                        }
                        rsBalise.close();
                        pstBalise.close();


                    }

                } else {
                    System.out.println("fix identifier vide");
                }
                sequenceNumber = rs.getString(9);
                System.out.println("FIN");
            } while (rs.next());

            PreparedStatement ajoutSid = con.prepareStatement("INSERT INTO appr (runwayIdentifiant, typeApproche, aeroportIdentifiant, icaoCode, premierPoint ,balises) VALUES (?,CAST(? AS typeAppr),?,?,?,?)");
            ajoutSid.setString(1, runwayIdentifiant);
            ajoutSid.setString(2, runwayIdentifiant.substring(0, 1));
            ajoutSid.setString(3, aeroport);
            ajoutSid.setString(4, icaoCode);
            ajoutSid.setString(5, premierPoint);

            String[][] tab = new String[couple.size()][2];
            for (int i = 0; i < couple.size(); i++) {
                tab[i][0] = couple.get(i).getLatitude();
                tab[i][1] = couple.get(i).getLongitude();
            }

            Array arraySql = con.createArrayOf("text", tab);
            ajoutSid.setArray(6, arraySql);
            ajoutSid.executeUpdate();
            couple.clear();

            rs.close();
            pst.close();


        } catch (SQLException ex) {
            Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /**
     * Get all the approach for an airport
     *
     * @param aeroport ICAO Airport Identifier
     * @return List of approach objects
     */
    public static ArrayList<Approche> requestApprs(String aeroport) {
        Connection con = null;
        String icaoCode = aeroport.substring(0, 2);
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Approche> p = new ArrayList<>();
        try {
            con = ParserGlobal.createSql();
            pst = con.prepareStatement("SELECT balises, runwayIdentifiant from appr where aeroportIdentifiant like ? and icaoCode like ? order by typeApproche");
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
                p.add(new Approche(identifiant, l));

            }
            return p;
        } catch (SQLException ex) {
            Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return p;
    }

    /**
     * Get the arrival path, STAR + Approach PATH until the runway
     *
     * @param aeroport ICAO Airport Identifier
     * @param star Star identifier
     * @param piste Runway Identifier
     * @return Approach object or null
     */
    public static Approche requestApprStar(String aeroport, String star, String piste) {
        Connection con = null;
        String icaoCode = aeroport.substring(0, 2);

        ArrayList<LatitudeLongitude> l = Star.requestStar(aeroport, star).getBalise();
        PreparedStatement reqStar = null;
        ResultSet rsStar = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement reqPiste = null;
        ResultSet resPiste = null;
        String identifiant = null;
        try {

            con = ParserGlobal.createSql();

            //récuperer le nom du dernier point de la star
            reqStar = con.prepareStatement("SELECT lastFix from star where aeroportIdentifiant like ? and icaoCode like ? and identifiant like ?");
            reqStar.setString(1, aeroport);
            reqStar.setString(2, icaoCode);
            reqStar.setString(3, star);
            rsStar = reqStar.executeQuery();

            if (rsStar.next()) {
                //récuperation de l'apporche correspondant à partir du lastfix.
                pst = con.prepareStatement("SELECT balises, runwayIdentifiant from appr where aeroportIdentifiant like ? and icaoCode like ? and runwayIdentifiant like ? and premierPoint like ? order by typeApproche");
                pst.setString(1, aeroport);
                pst.setString(2, icaoCode);
                pst.setString(3, "%" + piste);
                pst.setString(4, rsStar.getString(1));
                rs = pst.executeQuery();
                if (rs.next()) {
                    Array a = rs.getArray(1);
                    String[][] val = (String[][]) a.getArray();
                    // list des balises
                    for (int i = 0; i < val.length; i++) {
                        l.add(new LatitudeLongitude(val[i][0], val[i][1]));
                    }
                    identifiant = rs.getString(2);

                }

                //récupération des coordonnées de la piste
                reqPiste = con.prepareStatement("SELECT latitude, longitude from piste where identifiantRwy like ? and identifiantAeroport like ? and icaoCode like ?");
                reqPiste.setString(1, "%" + piste);
                reqPiste.setString(2, aeroport);
                reqPiste.setString(3, icaoCode);
                resPiste = reqPiste.executeQuery();
                if (resPiste.next()) {
                    l.add(new LatitudeLongitude(resPiste.getString(1), resPiste.getString(2)));

                }
            }



        } catch (SQLException ex) {
            Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resPiste != null) {
                try {
                    resPiste.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (reqPiste != null) {
                try {
                    reqPiste.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rsStar != null) {
                try {
                    rsStar.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (reqStar != null) {
                try {
                    reqStar.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Approche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


        return new Approche(identifiant, l);

    }
}
