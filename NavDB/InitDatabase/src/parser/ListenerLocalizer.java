/*
 * Class Listener Localizer defines action to make for each Localizer Glide Slope parsed line 
 */
package parser;

import java.io.StringReader;
import java.sql.Connection;
import org.jsapar.JSaParException;
import org.jsapar.Line;
import org.jsapar.input.LineErrorEvent;
import org.jsapar.input.LineParsedEvent;
import org.jsapar.input.ParseException;
import org.jsapar.input.Parser;
import org.jsapar.input.ParsingEventListener;
import org.jsapar.io.MaxErrorsExceededException;
import org.jsapar.schema.Schema;

/**
 * Class Listener Localizer defines action to make for each Localizer Glide
 * Slope parsed line
 *
 * @author yoann
 */
public class ListenerLocalizer implements ParsingEventListener {

    /**
     * Schema record Localizer Continuation
     */
    private Schema schemaLocalizerContinuation;
    /**
     * Schema record Localizer Simulation
     */
    private Schema schemaLocalizerSimulation;
    /**
     * DataBase Connection
     */
    private Connection con;

    /**
     * Listener Localizer Constructor
     *
     * @param con Database Connection
     * @param schemaLocalizerContinuation Schema record Localizer Continuation
     * @param schemaLocalizerSimulation Schema record Localizer Simulation
     */
    public ListenerLocalizer(Connection con, Schema schemaLocalizerContinuation, Schema schemaLocalizerSimulation) {
        this.con = con;
        this.schemaLocalizerContinuation = schemaLocalizerContinuation;
        this.schemaLocalizerSimulation = schemaLocalizerSimulation;
    }

    /**
     * Action to make for each Localizer parsed line
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
        if (l.getLineType().equals("primary") && code.equals("P") && subcode.equals("I") && cont == 0) {

            String aeroport = l.getCell("ARPT IDENT").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE").getStringValue().trim();
            String identifiant = l.getCell("LOC IDENT").getStringValue().trim();
            String categorie = l.getCell("CAT").getStringValue();
            String frequence = l.getCell("FREQ").getStringValue().trim();
            String runwayIdentifiant = l.getCell("RUNWAY IDENT").getStringValue().trim();
            String locLatitude = l.getCell("LOC LATITUDE").getStringValue().trim();
            String locLongitude = l.getCell("LOC LONGITUDE").getStringValue().trim();
            String locBearing = l.getCell("LOC BRG").getStringValue().trim();
            String gsLatitude = l.getCell("GS LATITUDE").getStringValue().trim();
            String gsLongitude = l.getCell("GS LONGITUDE").getStringValue().trim();
            String locFr = l.getCell("LOC FR RW END").getStringValue().trim();
            String localiserPositionReference = l.getCell("+/-/@").getStringValue().trim();
            String gsThres = l.getCell("GS FR RW THRES").getStringValue().trim();
            String locWidth = l.getCell("LOC WIDTH").getStringValue().trim();
            String gsAngle = l.getCell("GS ANGLE").getStringValue().trim();
            String declinaisonMagnetique = l.getCell("STA DECL").getStringValue().trim();
            String tch = l.getCell("TCH").getStringValue().trim();
            String gsElev = l.getCell("GS ELEV").getStringValue().trim();
            String facility = l.getCell("SUPPORT FACILITY").getStringValue().trim();
            String facilityIcao = l.getCell("ICAO CODE FACILITY").getStringValue().trim();
            String facilitySec = l.getCell("SEC CODE FACILITY").getStringValue().trim();
            String facilitySub = l.getCell("SUB CODE FACILITY").getStringValue().trim();

            Ils.addSql(this.con, icaoCode, identifiant, locLatitude, locLongitude, aeroport, declinaisonMagnetique,
                    categorie, frequence, runwayIdentifiant, locBearing, gsLatitude,
                    gsLongitude, locFr, localiserPositionReference, gsThres, locWidth, gsAngle,
                    tch, gsElev, facility, facilityIcao, facilitySec, facilitySub);


        } else {
            // continuite des enregistrements
            String sub = l.getCell("FREQ").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();

            for (int i = 0; i < l.getNumberOfCells(); i++) {
                newString = newString.append(l.getCell(i).getStringValue());
            }
            switch (sub) {
                case "N": {
                    //continuation
                    Parser p = new Parser(this.schemaLocalizerContinuation);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            throw new UnsupportedOperationException("ListenerLocalizer Continuation Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            throw new UnsupportedOperationException("ListenerLocalizer Continuation Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    };
                    p.addParsingEventListener(a);
                    p.parse(new StringReader(newString.toString()));
                    break;
                }
                case "S": {
                    Parser p = new Parser(this.schemaLocalizerSimulation);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            throw new UnsupportedOperationException("ListenerLocalizer Simulation Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            throw new UnsupportedOperationException("ListenerLocalizer Simulation Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    };
                    p.addParsingEventListener(a);
                    p.parse(new StringReader(newString.toString()));
                    break;
                }
            }
        }

    }

    /**
     * Action to make for each Localizer failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerLocalizer : probleme lecture de ligne");
    }
}
