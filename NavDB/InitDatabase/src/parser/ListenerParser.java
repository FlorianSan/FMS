/*
 * Class Listener Localizer defines action to make for each parsed line 
 */
package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsapar.JSaParException;
import org.jsapar.Line;
import org.jsapar.input.LineErrorEvent;
import org.jsapar.input.LineParsedEvent;
import org.jsapar.input.Parser;
import org.jsapar.input.ParsingEventListener;
import org.jsapar.io.MaxErrorsExceededException;
import org.jsapar.schema.Schema;
import org.jsapar.schema.SchemaException;
import org.jsapar.schema.Xml2SchemaBuilder;

/**
 * Class Listener Localizer defines action to make for each parsed line
 *
 * @author yoann
 */
public class ListenerParser implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record VHF Primary
     */
    private Schema schemaVhfPrimary;
    /**
     * Schema record VHF Flight
     */
    private Schema schemaVhfFlight;
    /**
     * Schema record VHF Simulation
     */
    private Schema schemaVhfSimulation;
    /**
     * Schema record VHF Limitation
     */
    private Schema schemaVhfLimitation;
    /**
     * Schema record VHF Continuation
     */
    private Schema schemaVhfContinuation;
    /**
     * Schema record NDB Primary
     */
    private Schema schemaNdbPrimary;
    /**
     * Schema record NDB Continuation
     */
    private Schema schemaNdbContinuation;
    /**
     * Schema record NDB Flight
     */
    private Schema schemaNdbFlight;
    /**
     * Schema record NDB Simulation
     */
    private Schema schemaNdbSimulation;
    /**
     * Schema record WayPoint Primary
     */
    private Schema schemaWayPointPrimary;
    /**
     * Schema record WayPoint Continuation
     */
    private Schema schemaWayPointContinuation;
    /**
     * Schema record WayPoint Flight
     */
    private Schema schemaWayPointFlight;
    /**
     * Schema record Holding Pattern Primary
     */
    private Schema schemaHoldingPatternPrimary;
    /**
     * Schema record Holding Pattern Continuation
     */
    private Schema schemaHoldingPatternContinuation;
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
     * Schema record Runway Primary
     */
    private Schema schemaRunwayPrimary;
    /**
     * Schema record Runway Continuation
     */
    private Schema schemaRunwayContinuation;
    /**
     * Schema record Runway Simulation
     */
    private Schema schemaRunwaySimulation;
    /**
     * Schema record Localizer Primary
     */
    private Schema schemaLocalizerPrimary;
    /**
     * Schema record Localizer Continuation
     */
    private Schema schemaLocalizerContinuation;
    /**
     * Schema record Localizer Simulation
     */
    private Schema schemaLocalizerSimulation;
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
     * Schema record Enroute Airways Primary
     */
    private Schema schemaEnrouteAirwayPrimary;
    /**
     * Schema record Enroute Airways Continuation
     */
    private Schema schemaEnrouteAirwayContinuation;
    /**
     * Schema record Enroute Airways Flight
     */
    private Schema schemaEnrouteAirwayFlight;

    /**
     * Listener Parser Constructor
     *
     * @param con DataBase Connection
     */
    public ListenerParser(Connection con) {
        this.con = con;
        Reader fileSchemaVhfPrimary = null;
        Reader fileSchemaVhfFlight = null;
        Reader fileSchemaVhfContinuation = null;
        Reader fileSchemaVhfLimitation = null;
        Reader fileSchemaVhfSimulation = null;
        Reader fileSchemaNdbPrimary = null;
        Reader fileSchemaNdbContinuation = null;
        Reader fileSchemaNdbFlight = null;
        Reader fileSchemaNdbSimulation = null;
        Reader fileSchemaWayPointPrimary = null;
        Reader fileSchemaWayPointContinuation = null;
        Reader fileSchemaWayPointFlight = null;
        Reader fileSchemaHoldingPatternPrimary = null;
        Reader fileSchemaHoldingPatternContinuation = null;
        Reader fileSchemaAeroportPrimary = null;
        Reader fileSchemaAeroportContinuation = null;
        Reader fileSchemaAeroportFlight = null;
        Reader fileSchemaRunwayPrimary = null;
        Reader fileSchemaRunwayContinuation = null;
        Reader fileSchemaRunwaySimulation = null;
        Reader fileSchemaLocalizerPrimary = null;
        Reader fileSchemaLocalizerContinuation = null;
        Reader fileSchemaLocalizerSimulation = null;
        Reader fileSchemaProcedurePrimary = null;
        Reader fileSchemaProcedureContinuation = null;
        Reader fileSchemaProcedureFlight = null;
        Reader fileSchemaEnrouteAirwaysPrimary = null;
        Reader fileSchemaEnrouteAirwaysContinuation = null;
        Reader fileSchemaEnrouteAirwaysFlight = null;

        try {

            //VHF Navaid
            fileSchemaVhfPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("vhf_navaid_primary.xml"), "UTF8");
            fileSchemaVhfFlight = new InputStreamReader(ListenerParser.class.getResourceAsStream("vhf_navaid_flight.xml"), "UTF8");
            fileSchemaVhfContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("vhf_navaid_continuation.xml"), "UTF8");
            fileSchemaVhfLimitation = new InputStreamReader(ListenerParser.class.getResourceAsStream("vhf_navaid_limitation.xml"), "UTF8");
            fileSchemaVhfSimulation = new InputStreamReader(ListenerParser.class.getResourceAsStream("vhf_navaid_simulation.xml"), "UTF8");

            //NDB Navaid  
            fileSchemaNdbPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("ndb_navaid_primary.xml"), "UTF8");
            fileSchemaNdbContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("ndb_navaid_continuation.xml"), "UTF8");
            fileSchemaNdbFlight = new InputStreamReader(ListenerParser.class.getResourceAsStream("ndb_navaid_flight.xml"), "UTF8");
            fileSchemaNdbSimulation = new InputStreamReader(ListenerParser.class.getResourceAsStream("ndb_navaid_simulation.xml"), "UTF8");

            //Waypoint
            fileSchemaWayPointPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("waypoint_primary.xml"), "UTF8");
            fileSchemaWayPointContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("waypoint_continuation.xml"), "UTF8");
            fileSchemaWayPointFlight = new InputStreamReader(ListenerParser.class.getResourceAsStream("waypoint_flight.xml"), "UTF8");

            //Holding Pattern
            fileSchemaHoldingPatternPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("holding_point_primary.xml"), "UTF8");
            fileSchemaHoldingPatternContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("holding_point_continuation.xml"), "UTF8");

            //aeroport
            fileSchemaAeroportPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("aeroport_primary.xml"), "UTF8");
            fileSchemaAeroportContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("aeroport_continuation.xml"), "UTF8");
            fileSchemaAeroportFlight = new InputStreamReader(ListenerParser.class.getResourceAsStream("aeroport_flight.xml"), "UTF8");

            //runways
            fileSchemaRunwayPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("runway_primary.xml"), "UTF8");
            fileSchemaRunwayContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("runway_continuation.xml"), "UTF8");
            fileSchemaRunwaySimulation = new InputStreamReader(ListenerParser.class.getResourceAsStream("runway_simulation.xml"), "UTF8");

            //localizer
            fileSchemaLocalizerPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("localizer_primary.xml"), "UTF8");
            fileSchemaLocalizerContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("localizer_continuation.xml"), "UTF8");
            fileSchemaLocalizerSimulation = new InputStreamReader(ListenerParser.class.getResourceAsStream("localizer_simulation.xml"), "UTF8");

            //procedure
            fileSchemaProcedurePrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("procedure_primary.xml"), "UTF8");
            fileSchemaProcedureContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("procedure_continuation.xml"), "UTF8");
            fileSchemaProcedureFlight = new InputStreamReader(ListenerParser.class.getResourceAsStream("procedure_flight.xml"), "UTF8");

            //Enroute Airways
            fileSchemaEnrouteAirwaysPrimary = new InputStreamReader(ListenerParser.class.getResourceAsStream("enroute_airways_primary.xml"), "UTF8");
            fileSchemaEnrouteAirwaysContinuation = new InputStreamReader(ListenerParser.class.getResourceAsStream("enroute_airways_continuation.xml"), "UTF8");
            fileSchemaEnrouteAirwaysFlight = new InputStreamReader(ListenerParser.class.getResourceAsStream("enroute_airways_flight.xml"), "UTF8");

            Xml2SchemaBuilder builder = new Xml2SchemaBuilder();

            this.schemaVhfPrimary = builder.build(fileSchemaVhfPrimary);
            this.schemaVhfFlight = builder.build(fileSchemaVhfFlight);
            this.schemaVhfContinuation = builder.build(fileSchemaVhfContinuation);
            this.schemaVhfLimitation = builder.build(fileSchemaVhfLimitation);
            this.schemaVhfSimulation = builder.build(fileSchemaVhfSimulation);

            this.schemaNdbPrimary = builder.build(fileSchemaNdbPrimary);
            this.schemaNdbContinuation = builder.build(fileSchemaNdbContinuation);
            this.schemaNdbFlight = builder.build(fileSchemaNdbFlight);
            this.schemaNdbSimulation = builder.build(fileSchemaNdbSimulation);

            this.schemaWayPointPrimary = builder.build(fileSchemaWayPointPrimary);
            this.schemaWayPointContinuation = builder.build(fileSchemaWayPointContinuation);
            this.schemaWayPointFlight = builder.build(fileSchemaWayPointFlight);

            this.schemaHoldingPatternPrimary = builder.build(fileSchemaHoldingPatternPrimary);
            this.schemaHoldingPatternContinuation = builder.build(fileSchemaHoldingPatternContinuation);

            this.schemaAeroportPrimary = builder.build(fileSchemaAeroportPrimary);
            this.schemaAeroportContinuation = builder.build(fileSchemaAeroportContinuation);
            this.schemaAeroportFlight = builder.build(fileSchemaAeroportFlight);

            this.schemaRunwayPrimary = builder.build(fileSchemaRunwayPrimary);
            this.schemaRunwayContinuation = builder.build(fileSchemaRunwayContinuation);
            this.schemaRunwaySimulation = builder.build(fileSchemaRunwaySimulation);

            this.schemaLocalizerPrimary = builder.build(fileSchemaLocalizerPrimary);
            this.schemaLocalizerContinuation = builder.build(fileSchemaLocalizerContinuation);
            this.schemaLocalizerSimulation = builder.build(fileSchemaLocalizerSimulation);

            this.schemaProcedurePrimary = builder.build(fileSchemaProcedurePrimary);
            this.schemaProcedureContinuation = builder.build(fileSchemaProcedureContinuation);
            this.schemaProcedureFlight = builder.build(fileSchemaProcedureFlight);

            this.schemaEnrouteAirwayPrimary = builder.build(fileSchemaEnrouteAirwaysPrimary);
            this.schemaEnrouteAirwayContinuation = builder.build(fileSchemaEnrouteAirwaysContinuation);
            this.schemaEnrouteAirwayFlight = builder.build(fileSchemaEnrouteAirwaysFlight);

        } catch (SchemaException e) {
            System.out.println("ListenerParser : Probleme construction XML : " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("ListenerParser : probleme entree/sortie : " + ex.getMessage());
        } finally {
            if (fileSchemaVhfPrimary != null) {
                try {
                    fileSchemaVhfPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaVhfFlight != null) {
                try {
                    fileSchemaVhfFlight.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaVhfContinuation != null) {
                try {
                    fileSchemaVhfContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaVhfLimitation != null) {
                try {
                    fileSchemaVhfLimitation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaVhfSimulation != null) {
                try {
                    fileSchemaVhfSimulation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaNdbPrimary != null) {
                try {
                    fileSchemaNdbPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaNdbContinuation != null) {
                try {
                    fileSchemaNdbContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaNdbFlight != null) {
                try {
                    fileSchemaNdbFlight.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaNdbSimulation != null) {
                try {
                    fileSchemaNdbSimulation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaWayPointPrimary != null) {
                try {
                    fileSchemaWayPointPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaWayPointContinuation != null) {
                try {
                    fileSchemaWayPointContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaWayPointFlight != null) {
                try {
                    fileSchemaWayPointFlight.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaHoldingPatternPrimary != null) {
                try {
                    fileSchemaHoldingPatternPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaHoldingPatternContinuation != null) {
                try {
                    fileSchemaHoldingPatternContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaAeroportPrimary != null) {
                try {
                    fileSchemaAeroportPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaAeroportContinuation != null) {
                try {
                    fileSchemaAeroportContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaAeroportFlight != null) {
                try {
                    fileSchemaAeroportFlight.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaRunwayPrimary != null) {
                try {
                    fileSchemaRunwayPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaRunwayContinuation != null) {
                try {
                    fileSchemaRunwayContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaRunwaySimulation != null) {
                try {
                    fileSchemaRunwaySimulation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaLocalizerPrimary != null) {
                try {
                    fileSchemaLocalizerPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaLocalizerContinuation != null) {
                try {
                    fileSchemaLocalizerContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaLocalizerSimulation != null) {
                try {
                    fileSchemaLocalizerSimulation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaProcedurePrimary != null) {
                try {
                    fileSchemaProcedurePrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaProcedureContinuation != null) {
                try {
                    fileSchemaProcedureContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaProcedureFlight != null) {
                try {
                    fileSchemaProcedureFlight.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaEnrouteAirwaysPrimary != null) {
                try {
                    fileSchemaEnrouteAirwaysPrimary.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaEnrouteAirwaysContinuation != null) {
                try {
                    fileSchemaEnrouteAirwaysContinuation.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileSchemaEnrouteAirwaysFlight != null) {
                try {
                    fileSchemaEnrouteAirwaysFlight.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenerParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        }
    }

    /**
     * Action to make for each parsed line
     *
     * @param lpe Line Parsed Event
     * @throws JSaParException
     */
    @Override
    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
        Line l = lpe.getLine();
        String sT = l.getStringCellValue("S/T");
        String code = l.getStringCellValue("SEC CODE");
        String subcode = l.getStringCellValue("SUB CODE");
        String subcode2 = l.getStringCellValue("SUB CODE2");

        StringBuffer newString = new StringBuffer();
        for (int i = 0; i < l.getNumberOfCells(); i++) {
            newString = newString.append(l.getCell(i).getStringValue());
        }

        if (sT.equals("S")) {
            if (l.getLineType().equals("standard") && code.equals("D") && subcode.equals(" ")) {
                //Cas SEC=D and SUB=vide --> VHF Navaid

                Parser p = new Parser(this.schemaVhfPrimary);
                ParsingEventListener a = new ListenerVhf(this.con, this.schemaVhfPrimary, this.schemaVhfContinuation, this.schemaVhfFlight, this.schemaVhfLimitation, this.schemaVhfSimulation);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && code.equals("D") && subcode.equals("B")) {
                // Cas SEC=D and SUB=B --> NDB Navaid
                Parser p = new Parser(this.schemaNdbPrimary);
                ParsingEventListener a = new ListenerNdb(this.con, this.schemaNdbPrimary, this.schemaNdbContinuation, this.schemaNdbFlight, this.schemaNdbSimulation);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && code.equals("E") && subcode.equals("A")) {
                // Cas SEC=E and SUB=A --> Waypoint
                Parser p = new Parser(this.schemaWayPointPrimary);
                ParsingEventListener a = new ListenerWayPoint(this.con, this.schemaWayPointPrimary, this.schemaWayPointContinuation, this.schemaWayPointFlight);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && code.equals("E") && subcode.equals("P")) {
//                 Cas SEC=E and SUB=P --> Holding Pattern
                Parser p = new Parser(this.schemaHoldingPatternPrimary);
                ParsingEventListener a = new ListenerHoldingPattern(this.con, this.schemaHoldingPatternContinuation);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && code.equals("P") && subcode.equals(" ") && subcode2.equals("N")) {
                System.out.println("NDB AEROPORT NON PRIS EN CHARGE");

            } else if (l.getLineType().equals("standard") && code.equals("P") && subcode.equals(" ") && subcode2.equals("C")) {
                // Waypoint aeroport
//                System.out.println("WAYPOINT AEROPORT");
                Parser p = new Parser(this.schemaWayPointPrimary);
                ParsingEventListener a = new ListenerWayPoint(this.con, this.schemaWayPointPrimary, this.schemaWayPointContinuation, this.schemaWayPointFlight);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            } else if (l.getLineType().equals("standard") && code.equals("P") && subcode.equals(" ") && subcode2.equals("A")) {
                // aeroports
                Parser p = new Parser(this.schemaAeroportPrimary);
                ParsingEventListener a = new ListenerAeroport(this.con, this.schemaAeroportPrimary, this.schemaAeroportContinuation, this.schemaAeroportFlight);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && code.equals("P") && subcode.equals(" ") && subcode2.equals("G")) {
                // runways
                Parser p = new Parser(this.schemaRunwayPrimary);
                ParsingEventListener a = new ListenerPiste(this.con, this.schemaRunwayContinuation, this.schemaRunwaySimulation);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && code.equals("P") && subcode.equals(" ") && subcode2.equals("I")) {
                // localizer
                Parser p = new Parser(this.schemaLocalizerPrimary);
                ParsingEventListener a = new ListenerLocalizer(this.con, this.schemaLocalizerContinuation, this.schemaLocalizerSimulation);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (l.getLineType().equals("standard") && subcode.equals(" ") && ((code.equals("P") && subcode2.equals("D")) || (code.equals("P") && subcode2.equals("E")) || (code.equals("P") && subcode2.equals("F")))) {
                // procedures
                Parser p = new Parser(this.schemaProcedurePrimary);
                ParsingEventListener a = new ListenerProcedure(this.con, this.schemaProcedurePrimary, this.schemaProcedureContinuation, this.schemaProcedureFlight);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            } else if (l.getLineType().equals("standard") && code.equals("E") && subcode.equals("R")) {
                // Enroute Airways
                Parser p = new Parser(this.schemaEnrouteAirwayPrimary);
                ParsingEventListener a = new ListenerEnrouteAirways(this.con, this.schemaEnrouteAirwayPrimary, this.schemaEnrouteAirwayContinuation, this.schemaEnrouteAirwayFlight);
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            }

        }

    }

    /**
     * Action to make for each failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws MaxErrorsExceededException {
        System.out.println("Parsage général : un probleme est survenu");
    }
}
