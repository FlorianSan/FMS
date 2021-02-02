/*
 * Class Listener Localizer defines action to make for each Runway parsed line 
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
 * Class Listener Localizer defines action to make for each Runway parsed line
 *
 * @author yoann
 */
public class ListenerPiste implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record runway Continuation
     */
    private Schema schemaRunwayContinuation;
    /**
     * Schema record runway Simulation
     */
    private Schema schemaRunwaySimulation;

    /**
     * ListenerPiste Constructor
     *
     * @param con Database Connection
     * @param schemaRunwayContinuation Schema record Runway Continuation
     * @param schemaRunwaySimulation Schema record Runway Simulation
     */
    public ListenerPiste(Connection con, Schema schemaRunwayContinuation, Schema schemaRunwaySimulation) {
        this.con = con;
        this.schemaRunwayContinuation = schemaRunwayContinuation;
        this.schemaRunwaySimulation = schemaRunwaySimulation;
    }

    /**
     * Action to make for each Runway parsed line
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
        if (l.getLineType().equals("primary") && code.equals("P") && subcode.equals("G") && cont == 1) {

            String identifiantAeroport = l.getCell("ARPT IDENT").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE").getStringValue().trim();
            String identifiantRwy = l.getCell("RUNWAY IDENT").getStringValue().trim();
            String runwayLength = l.getCell("RUNWAY LENGTH").getStringValue().trim();
            String runwayBearing = l.getCell("RUNWAY BEARING").getStringValue().trim();
            String latitude = l.getCell("LATITUDE").getStringValue().trim();
            String longitude = l.getCell("LONGITUDE").getStringValue().trim();
            String runwayGrad = l.getCell("RWY GRAD").getStringValue().trim();
            String ellipsoidHeight = l.getCell("ELLIPSOID HEIGHT").getStringValue().trim();
            String lndgThresElev = l.getCell("LNDG THRES ELEV").getStringValue().trim();
            String dsplcdThr = l.getCell("DSPLCD THR").getStringValue().trim();
            String tch = l.getCell("TCH").getStringValue().trim();
            String width = l.getCell("WIDTH").getStringValue().trim();
            String locGlsIdent = l.getCell("LOC GLS IDENT").getStringValue().trim();
            String categorieLoc = l.getCell("CAT/CLASS LOC GLS IDENT").getStringValue();
            String stopway = l.getCell("STOPWAY").getStringValue().trim();
            String secLocGlsIdent = l.getCell("SEC LOC GLS IDENT").getStringValue().trim();
            String categorieSecLoc = l.getCell("CAT/CLASS SEC LOC GLS IDENT").getStringValue();

            Piste.addSql(this.con, icaoCode, identifiantRwy, identifiantAeroport, runwayLength,
                    runwayBearing, latitude, longitude, runwayGrad, ellipsoidHeight, lndgThresElev,
                    dsplcdThr, tch, width, locGlsIdent, categorieLoc, stopway, secLocGlsIdent, categorieSecLoc);

        } else {
            System.out.println("ListenerPiste : Non pris en charge");
        }
    }

    /**
     * Action to make for each Runway failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerRunway : problème parsage de la ligne");
    }
}
