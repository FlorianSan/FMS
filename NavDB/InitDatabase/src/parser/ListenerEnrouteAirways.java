/*
 * Class Listener Enroute Airways defines action to make for each Enroute Airways parsed line 
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
 * Class Listener Enroute Airways defines action to make for each Enroute
 * Airways parsed line
 *
 * @author yoann
 */
public class ListenerEnrouteAirways implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record Enroute Airways Primary
     */
    private Schema schemaEnrouteAirwaysPrimary;
    /**
     * Schema record Enroute Airways Continuation
     */
    private Schema schemaEnrouteAirwaysContinuation;
    /**
     * Schema record Enroute Airways Flight
     */
    private Schema schemaEnrouteAirwaysFlight;

    /**
     * Listener Enroute Airways Constructor
     *
     * @param con DataBase Connection
     * @param schemaEnrouteAirwaysPrimary Schema record Enroute Airways Primary
     * @param schemaEnrouteAirwaysContinuation Schema record Enroute Airways
     * Continuation
     * @param schemaEnrouteAirwaysFlight Schema record Enroute Airways Flight
     */
    public ListenerEnrouteAirways(Connection con, Schema schemaEnrouteAirwaysPrimary, Schema schemaEnrouteAirwaysContinuation, Schema schemaEnrouteAirwaysFlight) {
        this.con = con;
        this.schemaEnrouteAirwaysPrimary = schemaEnrouteAirwaysPrimary;
        this.schemaEnrouteAirwaysContinuation = schemaEnrouteAirwaysContinuation;
        this.schemaEnrouteAirwaysFlight = schemaEnrouteAirwaysFlight;
    }

    /**
     * Action to make for each Enroute Airways parsed line
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
        if (l.getLineType().equals("primary") && code.equals("E") && subcode.equals("R") && (cont == 1 || cont == 0)) {

            String routeIdentifiant = l.getCell("ROUTE IDENT").getStringValue().trim();
            String sequenceNumber = l.getCell("SEQ NUMBER").getStringValue().trim();
            String fixIdentifiant = l.getCell("FIX IDENT").getStringValue().trim();
            String icaoCodeFix = l.getCell("ICAO CODE FIX").getStringValue().trim();
            String secCodeFix = l.getCell("SEC CODE FIX").getStringValue().trim();
            String subCodeFix = l.getCell("SUB CODE FIX").getStringValue().trim();
            String descriptionCode = l.getCell("DESC CODE").getStringValue();
            String boundaryCode = l.getCell("BDY CODE").getStringValue().trim();
            String routeType = l.getCell("RT TYPE").getStringValue().trim();
            String levelRoute = l.getCell("LEVEL").getStringValue().trim();
            String direct = l.getCell("DIRECT").getStringValue().trim();
            String cruiseTableIdentifier = l.getCell("TC IND").getStringValue();
            String euIndicator = l.getCell("EU IND").getStringValue().trim();
            String receiverVhf = l.getCell("RECD VHF").getStringValue().trim();
            String icaoCodeVhf = l.getCell("ICAO CODE VHF").getStringValue().trim();
            String requiredNavigation = l.getCell("RNP").getStringValue();
            String theta = l.getCell("THETA").getStringValue().trim();
            String rho = l.getCell("RHO").getStringValue().trim();
            String outboundMagneticCruise = l.getCell("OB MAG CRS").getStringValue().trim();
            String routeFromDistance = l.getCell("ROUTE FROM DIST").getStringValue().trim();
            String inboundMagneticCruise = l.getCell("IB MAG CRS").getStringValue().trim();
            String minAltitude = l.getCell("MIN ALTITUDE").getStringValue().trim();
            String minAltitude2 = l.getCell("MIN ALTITUDE2").getStringValue().trim();
            String maxAltitude = l.getCell("MAX ALTITUDE").getStringValue().trim();
            String fixRadius = l.getCell("FIX RADIUS").getStringValue().trim();

            EnrouteAirway.addSql(this.con, routeIdentifiant, sequenceNumber, fixIdentifiant, icaoCodeFix, secCodeFix,
                    subCodeFix, descriptionCode, boundaryCode, routeType, levelRoute, direct,
                    cruiseTableIdentifier, euIndicator, receiverVhf, icaoCodeVhf, requiredNavigation,
                    theta, rho, outboundMagneticCruise, routeFromDistance, inboundMagneticCruise,
                    minAltitude, minAltitude2, maxAltitude, fixRadius);

        } else {
            // continuite des enregistrements
            String sub = l.getCell("DESC CODE").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();
            System.out.println("Autre type" + sub);
        }

    }

    /**
     * Action to make for each Enroute Airways failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerEnrouteAirways : Erreur de parsage de la ligne");
    }
}
