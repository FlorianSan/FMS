/*
 * Class Listener Localizer defines action to make for each Procedure parsed line 
 */
package parser;

import java.sql.Connection;
import org.jsapar.JSaParException;
import org.jsapar.Line;
import org.jsapar.input.LineErrorEvent;
import org.jsapar.input.LineParsedEvent;
import org.jsapar.input.ParseException;
import org.jsapar.input.ParsingEventListener;
import org.jsapar.io.MaxErrorsExceededException;
import org.jsapar.schema.Schema;

/**
 * Class Listener Localizer defines action to make for each Procedure parsed
 * line
 *
 * @author yoann
 */
public class ListenerProcedure implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record Procedure Primary
     */
    private Schema schemaProcedurePrimary;
    /**
     * Schema record Procedure Continuation
     */
    private Schema schemaProcedureContinuation;
    /**
     * Schema record Procedure Flight
     */
    private Schema schemaProcedureFlight;

    /**
     * ListenerProcedure Constructor
     *
     * @param con Database Connection
     * @param schemaProcedurePrimary Schema record Procedure Primary
     * @param schemaProcedureContinuation Schema record Procedure Continuation
     * @param schemaProcedureFlight Schema record Procedure Flight
     */
    public ListenerProcedure(Connection con, Schema schemaProcedurePrimary, Schema schemaProcedureContinuation, Schema schemaProcedureFlight) {
        this.con = con;
        this.schemaProcedurePrimary = schemaProcedurePrimary;
        this.schemaProcedureContinuation = schemaProcedureContinuation;
        this.schemaProcedureFlight = schemaProcedureFlight;
    }

    /**
     * Action to make for each Procedure parsed line
     *
     * @param lpe Line Parsed Event
     * @throws JSaParException
     */
    @Override
    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
        Line l = lpe.getLine();
        int cont = l.getIntCellValue("CONT NR");
        String code = l.getStringCellValue("SEC CODE");
        String subcode = l.getStringCellValue("SUB CODE");
        if (l.getLineType().equals("primary") && ((code.equals("P") && subcode.equals("D")) || (code.equals("P") && subcode.equals("E")) || (code.equals("P") && subcode.equals("F"))) && (cont == 1 || cont == 0)) {

            String type = null;
            if (code.equals("P") && subcode.equals("D")) {
                //c'est une SID
                type = "SID";

            } else if (code.equals("P") && subcode.equals("E")) {
                //c'est une STAR
                type = "STAR";

            } else if (code.equals("P") && subcode.equals("F")) {
                //c'est une approach
                type = "APPR";

            }

            String aeroportIdentifiant = l.getCell("ARPT IDENT").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE").getStringValue().trim();
            String identifiant = l.getCell("SID/STAR/APP IDENT").getStringValue().trim();
            String routeType = l.getCell("RT TYPE").getStringValue().trim();
            String transitionIdentifier = l.getCell("TRANS IDENT").getStringValue().trim();
            String sequenceNumber = l.getCell("SEQ NR").getStringValue().trim();
            String fixIdentifiant = l.getCell("FIX IDENT").getStringValue().trim();
            String icaoCodeFix = l.getCell("ICAO CODE FIX").getStringValue().trim();
            String secCodeFix = l.getCell("SEC CODE FIX").getStringValue().trim();
            String subCodeFix = l.getCell("SUB CODE FIX").getStringValue().trim();
            String descriptionCode = l.getCell("DESC CODE").getStringValue();
            String turnDirection = l.getCell("TURN DR").getStringValue().trim();
            String requiredNavigationPerformance = l.getCell("RNP").getStringValue().trim();
            String pathAndTerminaison = l.getCell("PATH TERM").getStringValue().trim();
            String turnDirectionValide = l.getCell("TDV").getStringValue().trim();
            String recommendedNavaid = l.getCell("RECD NAVAID").getStringValue().trim();
            String icaoCodeNavaid = l.getCell("ICAO CODE NAVAID").getStringValue().trim();
            String arcRadius = l.getCell("ARC RADIUS").getStringValue().trim();
            String theta = l.getCell("THETA").getStringValue().trim();
            String rho = l.getCell("RHO").getStringValue().trim();
            String magneticCruise = l.getCell("MAG CRS").getStringValue().trim();
            String routeDistance = l.getCell("RTE DIST HOLD").getStringValue().trim();
            String secCodeRoute = l.getCell("SEC CODE HOLD").getStringValue().trim();
            String subCodeRoute = l.getCell("SUB CODE HOLD").getStringValue().trim();
            String altitudeDescription = l.getCell("ALT DESC").getStringValue().trim();
            String atc = l.getCell("ATC").getStringValue().trim();
            String altitude = l.getCell("ALTITUDE").getStringValue().trim();
            String altitude2 = l.getCell("ALTITUDE2").getStringValue().trim();
            String transAltitude = l.getCell("TRANS ALTITUDE").getStringValue().trim();
            String speedLimit = l.getCell("SPEED LIMIT").getStringValue().trim();
            String verticalAngle = l.getCell("VERT ANGLE").getStringValue().trim();
            String centerFix = l.getCell("CENTER FIX OR TAA PT").getStringValue().trim();
            String multiCd = l.getCell("MULTI CD").getStringValue().trim();
            String icaoCodeCenter = l.getCell("ICAO CODE CENTER").getStringValue().trim();
            String secCodeCenter = l.getCell("SEC CODE CENTER").getStringValue().trim();
            String subCodeCenter = l.getCell("SUB CODE CENTER").getStringValue().trim();
            String gnssFmsIndicator = l.getCell("GNSS/FMS IND").getStringValue().trim();
            String speedLmt = l.getCell("SPD LMT").getStringValue().trim();
            String routeQual1 = l.getCell("RTE QUAL1").getStringValue().trim();
            String routeQual2 = l.getCell("RTE QUAL2").getStringValue().trim();

            Procedure.addSql(this.con, type, aeroportIdentifiant, icaoCode, identifiant, routeType, transitionIdentifier,
                    sequenceNumber, fixIdentifiant, icaoCodeFix, secCodeFix, subCodeFix, descriptionCode, turnDirection,
                    requiredNavigationPerformance, pathAndTerminaison, turnDirectionValide, recommendedNavaid, icaoCodeNavaid,
                    arcRadius, theta, rho, magneticCruise, routeDistance, secCodeRoute, subCodeRoute, altitudeDescription, atc,
                    altitude, altitude2, transAltitude, speedLimit, verticalAngle, centerFix, multiCd, icaoCodeCenter,
                    secCodeCenter, subCodeCenter, gnssFmsIndicator, speedLmt, routeQual1, routeQual2);


        } else {
            // continuite des enregistrements
            String sub = l.getCell("DESC CODE").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();

            for (int i = 0; i < l.getNumberOfCells(); i++) {
                newString = newString.append(l.getCell(i).getStringValue());
            }
            if (sub.equals("P")) {
                //DONNEES PAS INTERESSANTES
//                System.out.println("Flight");
            } else if (sub.equals("N")) {
                // NON PRESENT
//                System.out.println("Continuation");
            } else {
                System.out.println("Listener Procedure Autre Enregistrement non pris en charge :" + sub);
            }
        }

    }

    /**
     * Action to make for each Procedure failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerProcedure : probleme lors du parsage de la ligne");
    }
}
