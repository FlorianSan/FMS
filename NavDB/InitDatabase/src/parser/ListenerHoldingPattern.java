/*
 * Class Listener Holding Pattern defines action to make for each Holding Pattern parsed line 
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
 * Class Listener Holding Pattern defines action to make for each Holding
 * Pattern parsed line
 *
 * @author yoann
 */
public class ListenerHoldingPattern implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record Holding Pattern Continuation
     */
    private Schema schemaHoldingPatternContinuation;

    /**
     * Listener Holding Pattern Constructor
     *
     * @param con Database Connection
     * @param schemaHoldingPatternContinuation Schema record Holding Pattern
     * Continuation
     */
    public ListenerHoldingPattern(Connection con, Schema schemaHoldingPatternContinuation) {
        this.con = con;
        this.schemaHoldingPatternContinuation = schemaHoldingPatternContinuation;
    }

    /**
     * Action to make for each Holding Pattern parsed line
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
        if (l.getLineType().equals("primary") && code.equals("E") && subcode.equals("P") && cont == 0) {

            String regionCode = l.getCell("REGN CODE").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE").getStringValue().trim();
            String dupIdentifier = l.getCell("DUP IDENT").getStringValue();
            String fixIdentifier = l.getCell("FIX IDENT").getStringValue().trim();
            String icaoBalise = l.getCell("ICAO CODE2").getStringValue().trim();
            String secCodeBalise = l.getCell("SEC CODE2").getStringValue();
            String subCodeBalise = l.getCell("SUB CODE2").getStringValue();
            String InboundHoldingCourse = l.getCell("IB HOLD CRS").getStringValue().trim();
            String turnDirection = l.getCell("TURN DIR").getStringValue().trim();
            String legLength = l.getCell("LEG LENGTH").getStringValue().trim();
            String legTime = l.getCell("LEG TIME").getStringValue().trim();
            String minAltitude = l.getCell("MINIMUM ALTITUDE").getStringValue().trim();
            String maxAltitude = l.getCell("MAXIMUM ALTITUDE").getStringValue().trim();
            String holdSpeed = l.getCell("HOLD SPEED").getStringValue().trim();
            String rNP = l.getCell("RNP").getStringValue().trim();
            String arcRadius = l.getCell("ARC RADIUS").getStringValue().trim();
            String name = l.getCell("NAME").getStringValue().trim();

            HoldingPattern.addSql(ListenerHoldingPattern.this.con, regionCode, icaoCode, dupIdentifier, fixIdentifier, icaoBalise, secCodeBalise, subCodeBalise, InboundHoldingCourse, turnDirection, legLength, legTime, minAltitude, maxAltitude, holdSpeed, rNP, arcRadius, name);



        } else {
            System.out.println("ListenerHoldingPattern autre type non pris en charge");
        }
    }

    /**
     * Action to make for each Holding Pattern failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerHoldingPattern : probleme lecture ligne");
    }
}
