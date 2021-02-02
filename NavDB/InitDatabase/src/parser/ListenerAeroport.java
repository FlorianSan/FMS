/*
 * Class Listener Airport defines action to make for each Airport parsed line 
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
 * Class Listener Airport defines action to make for each Airport parsed line
 * @author yoann
 */
public class ListenerAeroport implements ParsingEventListener {

    /**
     * Database connection
     */
    private Connection con;
    /**
     * Schema record Airport Continuation
     */
    private Schema schemaAeroportContinuation;
    /**
     * Schema record Airport Flight
     */
    private Schema schemaAeroportFlight;
    /**
     * Schema record Airport Primary
     */
    private Schema schemaAeroportPrimary;

    /**
     * Listener Aeroport Constructor
     * @param con Database Connection
     * @param schemaAeroportPrimary Schema record Airport Primary
     * @param schemaAeroportContinuation Schema record Airport Continuation
     * @param schemaAeroportFlight Schema record Airport Flight
     */
    public ListenerAeroport(Connection con, Schema schemaAeroportPrimary, Schema schemaAeroportContinuation, Schema schemaAeroportFlight) {
        this.con = con;
        this.schemaAeroportContinuation = schemaAeroportContinuation;
        this.schemaAeroportFlight = schemaAeroportFlight;
        this.schemaAeroportPrimary = schemaAeroportPrimary;
    }

    /**
     * Action to make for each parsed line
     * @param lpe Line Parsed Event
     * @throws JSaParException 
     */
    @Override
    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
        Line l = lpe.getLine();
        int cont = l.getIntCellValue("CONT NR");
        String code = l.getStringCellValue("SEC CODE");
        String subcode = l.getStringCellValue("SUB CODE");
        if (l.getLineType().equals("primary") && code.equals("P") && subcode.equals("A") && cont == 1) {

            String identifiant = l.getCell("ARPT IDENT").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE").getStringValue().trim();
            String ataIata = l.getCell("ATA/IATA").getStringValue().trim();
            String speedLimitAltitude = l.getCell("SPEED LIMIT ALTITUDE").getStringValue().trim();
            String longestRwy = l.getCell("LONGEST RWY").getStringValue().trim();
            String ifr = l.getCell("IFR").getStringValue().trim();
            String longRwy = l.getCell("LONG RWY").getStringValue().trim();
            String latitude = l.getCell("LATITUDE").getStringValue().trim();
            String longitude = l.getCell("LONGITUDE").getStringValue().trim();
            String magneticVariation = l.getCell("MAG VAR").getStringValue().trim();
            String elevation = l.getCell("ELEV").getStringValue().trim();
            String speedLimit = l.getCell("SPEED LIMIT").getStringValue().trim();
            String recdVhf = l.getCell("RECD VHF").getStringValue().trim();
            String icaoCodeVhf = l.getCell("ICAO CODE2").getStringValue().trim();
            String transAltitude = l.getCell("TRANS ALTITUDE").getStringValue().trim();
            String transLevel = l.getCell("TRANS LEVEL").getStringValue().trim();
            String publicMilitaire = l.getCell("PUB/MIL").getStringValue().trim();
            String timeZone = l.getCell("TIME ZONE").getStringValue().trim();
            String dayTime = l.getCell("DAY TIME").getStringValue().trim();
            String MTInd = l.getCell("MT/IND").getStringValue().trim();
            String datum = l.getCell("DATUM CODE").getStringValue().trim();
            String airportName = l.getCell("AIRPORT NAME").getStringValue().trim();

            Aeroport.addSql(ListenerAeroport.this.con,
                    icaoCode, identifiant, ataIata, speedLimitAltitude, longestRwy, ifr,
                    longRwy, latitude, longitude, magneticVariation, elevation, speedLimit, recdVhf,
                    icaoCodeVhf, transAltitude, transLevel, publicMilitaire, timeZone, dayTime, MTInd,
                    datum, airportName);

        } else {
            // continuite des enregistrements
            String sub = l.getCell("SPEED LIMIT ALTITUDE").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();

            for (int i = 0; i < l.getNumberOfCells(); i++) {
                newString = newString.append(l.getCell(i).getStringValue());
            }
            if (sub.equals("P")) {
                Parser p = new Parser(this.schemaAeroportFlight);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        Line l = lpe.getLine();
                        if (l.getLineType().equals("flight")) {

                            String firIdentifier = l.getCell("FIR IDENT").getStringValue().trim();
                            String uirIdentifier = l.getCell("UIR IDENT").getStringValue().trim();
                            String sEIndicator = l.getCell("S/E END").getStringValue().trim();
                            String sEDate = l.getCell("START/END DATE").getStringValue().trim();
                            String asInd = l.getCell("AS IND").getStringValue().trim();
                            String asArptIdent = l.getCell("AS ARPT IDENT").getStringValue().trim();
                            String asIcaoCode = l.getCell("AS ICAO CODE").getStringValue().trim();
                            int id = Aeroport.lastRecord(ListenerAeroport.this.con);
                            Aeroport.addSqlContinuation(ListenerAeroport.this.con, firIdentifier, uirIdentifier, sEIndicator, sEDate, asInd, asArptIdent, asIcaoCode, id);


                        }

                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        System.out.println("ListenerAeroport type flight : probleme ligne");
                    }
                };
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            } else if (sub.equals("N")) {
                System.out.println("type continuation");
                Parser p = new Parser(this.schemaAeroportContinuation);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        throw new UnsupportedOperationException("Continuation ListenerAeroport Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        throw new UnsupportedOperationException("Continuation ListenerAeroport Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));



            } else {
                Parser p = new Parser(this.schemaAeroportPrimary);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        throw new UnsupportedOperationException("Modification ListenerAeroport Not supported yet."); 
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        throw new UnsupportedOperationException("Erreur de parsage Modification ListenerAeroport Not supported yet.");
                    }
                };
            }
        }
    }

    /**
     * Action to make for each failed parsed line
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException 
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerAeroport : erreur de parsage ligne");
    }
}
