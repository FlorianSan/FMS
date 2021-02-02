/**
 * Class to manage and to get information about Holding Pattern
 */
package parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage and to get information about Holding Pattern
 *
 * @author yoann
 */
public class HoldingPattern {

    /**
     * Region Code
     *
     */
    private String regionCode;
    /**
     * ICAO Code
     */
    private String icaoCode;
    /**
     * DUP Identifier
     */
    private String dupIdentifier;
    /**
     * Fix Identifier
     */
    private String fixIdentifier;
    /**
     * ICAO Code of fix
     */
    private String icaoCodeBalise;
    /**
     * SectionCode of fix
     */
    private String secCodeBalise;
    /**
     * SubSection Code of fix
     */
    private String subCodeBalise;
    /**
     * Inbound Holding Course
     */
    private String inboundHoldingCourse;
    /**
     * Turn Direction
     */
    private String turnDirection;
    /**
     * Leg Length
     */
    private String legLength;
    /**
     * Leg Time
     */
    private String legTime;
    /**
     * minimum altitude
     */
    private String minAltitude;
    /**
     * maxi altitude
     */
    private String maxAltitude;
    /**
     * Hold Speed
     */
    private String holdSpeed;
    /**
     * Required Navigation Performance
     */
    private String rNP;
    /**
     * Arc radius
     */
    private String arcRadius;
    /**
     * Name
     */
    private String name;
    /**
     * Marker
     */
    private Balises b;
    /**
     * Marker Type
     */
    private int typeBalise;

    /**
     * Holding Pattern Constructor
     *
     * @param regionCode Region Code
     * @param icaoCode ICAO Code
     * @param dupIdentifier DUP Identifier
     * @param fixIdentifier Fix Identifier
     * @param icaoBalise ICAO Code of fix
     * @param secCodeBalise SectionCode of fix
     * @param subCodeBalise SubSectionCode of fix
     * @param InboundHoldingCourse Inbound Holding Course
     * @param turnDirection Turn Direction
     * @param legLength Leg length
     * @param legTime Leg Time
     * @param minAltitude Minimum Altitude
     * @param maxAltitude Maximum Altitude
     * @param holdSpeed Hold Speed
     * @param rNP RNP
     * @param arcRadius Arc Radius
     * @param name Holding Pattern name
     */
    public HoldingPattern(String regionCode, String icaoCode, String dupIdentifier, String fixIdentifier, String icaoBalise, String secCodeBalise, String subCodeBalise, String InboundHoldingCourse, String turnDirection, String legLength, String legTime, String minAltitude, String maxAltitude, String holdSpeed, String rNP, String arcRadius, String name) {
        this.regionCode = regionCode;
        this.icaoCode = icaoCode;
        this.dupIdentifier = dupIdentifier;
        this.fixIdentifier = fixIdentifier;
        this.icaoCodeBalise = icaoBalise;
        this.secCodeBalise = secCodeBalise;
        this.subCodeBalise = subCodeBalise;
        this.inboundHoldingCourse = InboundHoldingCourse;
        this.turnDirection = turnDirection;
        this.legLength = legLength;
        this.legTime = legTime;
        this.minAltitude = minAltitude;
        this.maxAltitude = maxAltitude;
        this.holdSpeed = holdSpeed;
        this.rNP = rNP;
        this.arcRadius = arcRadius;
        this.name = name;
    }

    /**
     * Get Region Code
     *
     * @return Region Code
     */
    public String getRegionCode() {
        return this.regionCode;
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
     * Get Inbound Holding Course
     *
     * @return Inbound Holding Course
     */
    public String getIBHoldCourse() {
        return this.inboundHoldingCourse;
    }

    /**
     * Get turn Direction
     *
     * @return Turn Direction
     */
    public String getTurnDirection() {
        return this.turnDirection;
    }

    /**
     * Get Leg length
     *
     * @return Leg length
     */
    public String getLegLength() {
        return this.legLength;
    }

    /**
     * Get Leg Time
     *
     * @return Leg Time
     */
    public String getLegTime() {
        return this.legTime;
    }

    /**
     * Get Minimum Altitude
     *
     * @return Minimim Altitude
     */
    public String getMinAltitude() {
        return this.minAltitude;
    }

    /**
     * Get Maximum Altitude
     *
     * @return Maximum Altitude
     */
    public String getMaxAltitude() {
        return this.maxAltitude;
    }

    /**
     * Get Hold Speed
     *
     * @return Hold Speed
     */
    public String getHoldSpeed() {
        return this.holdSpeed;
    }

    /**
     * Get RNP
     *
     * @return RNP
     */
    public String getRNP() {
        return this.rNP;
    }

    /**
     * Get Arc radius
     *
     * @return Arc Radius
     */
    public String getArcRadius() {
        return this.arcRadius;
    }

    /**
     * Get Holding pattern Name
     *
     * @return Holding Pattern name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get Marker
     *
     * @return Marker
     */
    public Balises getBalise() {
        return this.b;
    }

    /**
     * Get marker type
     *
     * @return Marker Type
     */
    public int getTypeBalise() {
        return this.typeBalise;
    }

    /**
     * Link the holding pattern with a marker ( fill the field balises and
     * typeBalise in SQL)
     *
     * @param con Database Connection
     */
    protected static void updateHoldingPattern(Connection con) {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT dupIdentifier, fixIdentifier, icaoBalise, secCodeBalise, subCodeBalise FROM holdingpattern");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getString(4).equals("D") && rs.getString(5).equals("B")) {
                    //NDB
                    PreparedStatement pst2 = con.prepareStatement("SELECT id from ndb where identifiant=? and icaoCode=?");
                    pst2.setString(1, rs.getString(2));
                    pst2.setString(2, rs.getString(3));
                    ResultSet rs2 = pst2.executeQuery();
                    rs2.next();
                    PreparedStatement pst3 = con.prepareStatement("UPDATE holdingpattern set typeBalise=?, rattachBalise=? WHERE dupIdentifier=? and fixIdentifier=? and icaoBalise= ? and  secCodeBalise= ? and subCodeBalise=?");

                    pst3.setString(1, "NDB");

                    pst3.setInt(2, rs2.getInt(1));
                    pst3.setString(3, rs.getString(1));
                    pst3.setString(4, rs.getString(2));
                    pst3.setString(5, rs.getString(3));
                    pst3.setString(6, rs.getString(4));
                    pst3.setString(7, rs.getString(5));
                    pst3.executeUpdate();
                    rs2.close();
                    pst2.close();
                    pst3.close();

                } else if ((rs.getString(4).equals("E") && rs.getString(5).equals("A")) || (rs.getString(4).equals("P") && rs.getString(5).equals("C"))) {
                    //Waypoint   + waypoint terminal                  
                    PreparedStatement pst2 = con.prepareStatement("SELECT id from waypoint where identifiant=? and icaoCode=?");
                    pst2.setString(1, rs.getString(2));
                    pst2.setString(2, rs.getString(3));
                    ResultSet rs2 = pst2.executeQuery();
                    rs2.next();
                    PreparedStatement pst3 = con.prepareStatement("UPDATE holdingpattern set typeBalise=?, rattachBalise=? WHERE dupIdentifier=? and fixIdentifier=? and icaoBalise= ? and  secCodeBalise= ? and subCodeBalise=?");
                    pst3.setString(1, "WAYPOINT");
                    pst3.setInt(2, rs2.getInt(1));
                    pst3.setString(3, rs.getString(1));
                    pst3.setString(4, rs.getString(2));
                    pst3.setString(5, rs.getString(3));
                    pst3.setString(6, rs.getString(4));
                    pst3.setString(7, rs.getString(5));
                    pst3.executeUpdate();
                    rs2.close();
                    pst2.close();
                    pst3.close();
                } else if (rs.getString(4).equals("D") && rs.getString(5).equals(" ")) {
                    //VHF
                    PreparedStatement pst2 = con.prepareStatement("(SELECT id from vor where identifiant=? and icaoCode=?)UNION(SELECT id from vorDme where identifiant=? and icaoCode=?)UNION(SELECT id from dme where identifiant=? and icaoCode=?)UNION(SELECT id from tacan where identifiant=? and icaoCode=?)UNION(SELECT id from ilsdme where identifiant=? and icaoCode=?)");
                    pst2.setString(1, rs.getString(2));
                    pst2.setString(2, rs.getString(3));
                    pst2.setString(3, rs.getString(2));
                    pst2.setString(4, rs.getString(3));
                    pst2.setString(5, rs.getString(2));
                    pst2.setString(6, rs.getString(3));
                    pst2.setString(7, rs.getString(2));
                    pst2.setString(8, rs.getString(3));
                    pst2.setString(9, rs.getString(2));
                    pst2.setString(10, rs.getString(3));
                    ResultSet rs2 = pst2.executeQuery();
                    rs2.next();
                    PreparedStatement pst3 = con.prepareStatement("UPDATE holdingpattern set typeBalise=?, rattachBalise=? WHERE dupIdentifier=? and fixIdentifier=? and icaoBalise= ? and  secCodeBalise= ? and subCodeBalise=?");
                    pst3.setString(1, "VHF");
                    pst3.setInt(2, rs2.getInt(1));
                    pst3.setString(3, rs.getString(1));
                    pst3.setString(4, rs.getString(2));
                    pst3.setString(5, rs.getString(3));
                    pst3.setString(6, rs.getString(4));
                    pst3.setString(7, rs.getString(5));
                    pst3.executeUpdate();
                    rs2.close();
                    pst2.close();
                    pst3.close();
                } else {
                    System.out.println("Holding pattern update autre type :" + rs.getString(4) + rs.getString(5));
                }

            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoldingPattern.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /**
     * Add Holding Pattern Record in SQL database
     *
     * @param con DataBase Connection
     * @param regionCode Region Code
     * @param icaoCode ICAO Code
     * @param dupIdentifier Dup Identifier
     * @param fixIdentifier Fix identifier
     * @param icaoBalise ICAO Code of fix
     * @param secCodeBalise Section Code of Fix
     * @param subCodeBalise SubSection Code of Fix
     * @param InboundHoldingCourse Inbound Holding Course
     * @param turnDirection Turn Direction
     * @param legLength Leg length
     * @param legTime Leg Time
     * @param minAltitude Minimum Altitude
     * @param maxAltitude Max Altitude
     * @param holdSpeed Hold Speed
     * @param rNP RNP
     * @param arcRadius Arc Radius
     * @param name Hold Pattern Name
     */
    protected static void addSql(Connection con, String regionCode, String icaoCode, String dupIdentifier, String fixIdentifier, String icaoBalise, String secCodeBalise, String subCodeBalise, String InboundHoldingCourse, String turnDirection, String legLength, String legTime, String minAltitude, String maxAltitude, String holdSpeed, String rNP, String arcRadius, String name) {
        PreparedStatement pst = null;
        try {
            String stm = "INSERT INTO holdingpattern(regionCode, icaoCode, dupIdentifier,fixIdentifier, icaoBalise, secCodeBalise, subCodeBalise, InboundHoldingCourse, turnDirection, legLength, legTime, minAltitude, maxAltitude, holdSpeed, rNP, arcRadius, name) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, regionCode);
            pst.setString(2, icaoCode);
            pst.setString(3, dupIdentifier);
            pst.setString(4, fixIdentifier);
            pst.setString(5, icaoBalise);
            pst.setString(6, secCodeBalise);
            pst.setString(7, subCodeBalise);
            pst.setString(8, InboundHoldingCourse);
            pst.setString(9, turnDirection);
            pst.setString(10, legLength);
            pst.setString(11, legTime);
            pst.setString(12, minAltitude);
            pst.setString(13, maxAltitude);
            pst.setString(14, holdSpeed);
            pst.setString(15, rNP);
            pst.setString(16, arcRadius);
            pst.setString(17, name);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(HoldingPattern.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(HoldingPattern.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
