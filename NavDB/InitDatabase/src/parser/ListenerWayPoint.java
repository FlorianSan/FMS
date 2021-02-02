/*
 * Class Listener Localizer defines action to make for each WayPoint parsed line 
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
 * Class Listener Localizer defines action to make for each WayPoint parsed line
 *
 * @author yoann
 */
public class ListenerWayPoint implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
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
     * ListenerWayPoint Constructor
     *
     * @param con Database Connection
     * @param schemaWayPointPrimary Schema record WayPoint Primary
     * @param schemaWayPointContinuation Schema record WayPoint Continuation
     * @param schemaWayPointFlight Schema record WayPoint Flight
     */
    public ListenerWayPoint(Connection con, Schema schemaWayPointPrimary, Schema schemaWayPointContinuation, Schema schemaWayPointFlight) {
        this.con = con;
        this.schemaWayPointPrimary = schemaWayPointPrimary;
        this.schemaWayPointContinuation = schemaWayPointContinuation;
        this.schemaWayPointFlight = schemaWayPointFlight;
    }

    /**
     * Action to make for each WayPoint parsed line
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
        String subcode2 = l.getStringCellValue("SUB CODE2");
        if (l.getLineType().equals("primary") && ((code.equals("E") && subcode.equals("A")) || (code.equals("P") && subcode2.equals("C"))) && cont == 1) {
            //WayPoint Primary RECORD
//
//            System.out.println(l.getStringCellValue("S/T"));
//            System.out.println(l.getStringCellValue("CUST/AREA"));
//            System.out.println(l.getStringCellValue("SEC CODE"));
//            System.out.println(l.getStringCellValue("SUB CODE"));
//            System.out.println("Région code : " + l.getStringCellValue("REGN/ARPT CODE"));
//            System.out.println("Code icao (classement géographique) :" + l.getStringCellValue("ICAO CODE"));
//            System.out.println("Sub Code :" + l.getStringCellValue("SUB CODE2"));
//            System.out.println("Waypoint Identifiant :" + l.getStringCellValue("WAYPOINT IDENT"));
//            System.out.println(" Code ICAO (classement geographique) :" + l.getStringCellValue("ICAO CODE2"));
//            System.out.println("Continuité de l'enregistrement" + l.getStringCellValue("CONT NR"));

//            System.out.println("Type de Waypoint :" + l.getStringCellValue("TYPE"));
//
//
//            String typeWayPoint = l.getCell("TYPE").getStringValue();
//
//            switch (typeWayPoint.substring(0, 1)) {
//                case "C":
//                    System.out.println("Combined Named Intersection and RNAV");
//                    break;
//                case "I":
//                    System.out.println("Unnamed, Charted Intersection");
//                    break;
//                case "N":
//                    System.out.println("NDB Navaid as Waypoint");
//                    break;
//                case "R":
//                    System.out.println("Named Intersection");
//                    break;
//                case "U":
//                    System.out.println("Uncharted Airway Intersection");
//                    break;
//                case "V":
//                    System.out.println("VFR Waypoint");
//                    break;
//                case "W":
//                    System.out.println("RNAV Waypoint");
//                    break;
//                case " ":
//                    System.out.println("");
//            }
//
//            switch (typeWayPoint.substring(1, 2)) {
//                case "A":
//                    System.out.println("Final Approach Fix");
//                    break;
//                case "B":
//                    System.out.println("Initial and Final Approach Fix");
//                    break;
//                case "C":
//                    System.out.println("Final Approach Course Fix");
//                    break;
//                case "D":
//                    System.out.println("Intermediate Approach Fix");
//                    break;
//                case "E":
//                    System.out.println("Off-Route intersection in the FAA National Reference System");
//                    break;
//                case "F":
//                    System.out.println("Off-Route Intersection");
//                    break;
//                case "I":
//                    System.out.println("Initial Approach Fix");
//                    break;
//                case "K":
//                    System.out.println("Final Approach Course Fix at Initial Approach Fix");
//                    break;
//                case "L":
//                    System.out.println("Final Approach Course Fix at Intermediate Approach Fix");
//                    break;
//                case "M":
//                    System.out.println("Missed Approach Fix");
//                    break;
//                case "N":
//                    System.out.println("Initial Approach Fix and Missed Approach Fix");
//                    break;
//                case "O":
//                    System.out.println("Oceanic Entry/Exit Waypoint");
//                    break;
//                case "P":
//                    System.out.println("Pitch and Catch Point in the FAA High Altitude Redesign");
//                    break;
//                case "S":
//                    System.out.println("AACAA and SUA Waypoints in the FAA High Altitude Redesign");
//                    break;
//                case "U":
//                    System.out.println("FIR/UIR or Controlled Airspace Intersection");
//                    break;
//                case "V":
//                    System.out.println("Latitude/Longitude Intersection, Full Degree of Latitude");
//                    break;
//                case "W":
//                    System.out.println("Latitude/Longitude Intersection, Half Degree of Latitude");
//                    break;
//
//
//
//
//            }
//
//
//            System.out.println("Waypoint usage : " + l.getCell("USEAGE").getStringValue());
//            String usage = l.getCell("USEAGE").getStringValue();
//            switch (usage.substring(0, 1)) {
//                case "R":
//                    System.out.println("RNAC");
//                    break;
//
//            }
//            switch (usage.substring(1, 2)) {
//                case "B":
//                    System.out.println("HI and LO altitude");
//                    break;
//                case "H":
//                    System.out.println("HI altitude");
//                    break;
//                case "L":
//                    System.out.println("LO altitude");
//                    break;
//                case " ":
//                    System.out.println("Terminal Use Only");
//                    break;
//            }


//            System.out.println("Latitude :" + l.getStringCellValue("LATITUDE"));
//            System.out.println("Longitude :" + l.getStringCellValue("LONGITUDE"));
//            System.out.println("Variation magnetique :" + l.getStringCellValue("D MAG VAR"));
//            E Magnetic variation is East of TRUE North
//            W Magnetic variation is West of TRUE North
//            T The element defined in the current record is oriented to TRUE North in an area in which the local variation is not zero.


//            System.out.println("Waypoint elevation :" + l.getStringCellValue("WP ELEV"));
//            System.out.println("Référentiel geographique :" + l.getStringCellValue("DATUM CODE"));
//            System.out.println("Name Format Indicator(pour le waypointname :" + l.getStringCellValue("NAME IND"));
//            System.out.println("Waypoint name :" + l.getStringCellValue("NAME/DESC"));
//            System.out.println("Numéro de l'enregistrement :" + l.getStringCellValue("FILE RECORD NUMBER"));
//            System.out.println("Numéro de cycle :" + l.getStringCellValue("CYCLE"));


            String aeroport = l.getCell("REGN/ARPT CODE").getStringValue().trim();
            String datum = l.getCell("DATUM CODE").getStringValue().trim();
            String icaoAeroport = l.getCell("ICAO CODE").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE2").getStringValue().trim();
            String identifiant = l.getCell("WAYPOINT IDENT").getStringValue().trim();
            String latitude = l.getCell("LATITUDE").getStringValue().trim();
            String longitude = l.getCell("LONGITUDE").getStringValue().trim();
            String magneticVariation = l.getCell("D MAG VAR").getStringValue().trim();
            String nom = l.getCell("NAME/DESC").getStringValue().trim();
            String type = l.getCell("TYPE").getStringValue().trim();
            String useage = l.getCell("USEAGE").getStringValue().trim();
            String elevation = l.getCell("WP ELEV").getStringValue().trim();
            String nameIndicator = l.getCell("NAME IND").getStringValue().trim();

            WayPoint.addSql(this.con, aeroport, datum, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, nom, elevation, type, useage, nameIndicator);


        } else {
            //On extrait l'APPL Type pour aiguiller le nouveau parsage et on recopie la ligne
            String sub = l.getCell("SPACE2").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();
            for (int i = 0; i < l.getNumberOfCells(); i++) {
                newString = newString.append(l.getCell(i).getStringValue());
            }
            switch (sub) {
                case "P": {
                    //type flight
                    Parser p = new Parser(this.schemaWayPointFlight);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("flight")) {
                                //                            System.out.println("WayPoint identifiant :" + l.getStringCellValue("WAYPOINT IDENT"));
                                //                            System.out.println("FIR identifiant :" + l.getStringCellValue("FIR IDENT"));
                                //                            System.out.println("UIR identifiant :" + l.getStringCellValue("UIR IDENT"));
                                //                            System.out.println("Indicateur de date :" + l.getStringCellValue("S/E END"));
                                //                            System.out.println("Date de debut et de fin (GMT date) :" + l.getStringCellValue("START/END DATE"));
                                //                            System.out.println("");
                                //                            System.out.println("Numéro Cycle :" + l.getStringCellValue("CYCLE"));
                                //                            System.out.println("");
//
//                                    WayPoint n = ListenerWayPoint.this.listWayPoint.get(ListenerWayPoint.this.listWayPoint.size() - 1);
//                                    n.setFirIdentifier(l.getCell("FIR IDENT").getStringValue().trim());
//                                    n.setUirIdentifier(l.getCell("UIR IDENT").getStringValue().trim());
//                                    n.setSEIndicator(l.getCell("S/E END").getStringValue().trim());
//                                    n.setStartEndDate(l.getCell("START/END DATE").getStringValue().trim());

                                String icaoCode = l.getCell("ICAO CODE2").getStringValue().trim();
                                String identifiant = l.getCell("WAYPOINT IDENT").getStringValue().trim();
                                String firIdentifier = l.getCell("FIR IDENT").getStringValue().trim();
                                String UirIdentifier = l.getCell("UIR IDENT").getStringValue().trim();
                                String sEIndicator = l.getCell("S/E END").getStringValue().trim();
                                String sEDate = l.getCell("START/END DATE").getStringValue().trim();

                                WayPoint.addContinuationSql(ListenerWayPoint.this.con, icaoCode, identifiant, firIdentifier, UirIdentifier, sEIndicator, sEDate);



                            }

                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            System.out.println("ListenerWayPoint type flight : probleme ligne");
                        }
                    };
                    p.addParsingEventListener(a);
                    p.parse(new StringReader(newString.toString()));
                    break;
                }
                case "N": {
                    //type continuation NON PRESENT
                    Parser p = new Parser(this.schemaWayPointContinuation);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("continuation")) {
                                System.out.println("Notes sur l'enregistrement :" + l.getStringCellValue("NOTES ON CONTINUATION RECORD"));
                                System.out.println("Reserve :" + l.getStringCellValue("RESERVED"));
                                System.out.println("Numéro enregistrement :" + l.getStringCellValue("FILE RECORD NUMBER"));
                                System.out.println("Numéro cycle" + l.getStringCellValue("CYCLE"));
                                System.err.println("");
                            }

                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            System.out.println("ListenerWayPoint : type Continuation : probleme ligne");
                        }
                    };
                    p.addParsingEventListener(a);
                    p.parse(new StringReader(newString.toString()));
                    break;
                }
                default: {
                    //type modification
                    Parser p = new Parser(this.schemaWayPointPrimary);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("primary")) {
                                // VHF Navaid Primary Continuation

                                int index = Balises.lastRecord(ListenerWayPoint.this.con);

                                for (int i = 12; i < l.getNumberOfCells() - 2; i++) {
                                    String s = l.getCell(i).getStringValue().trim();
                                    if (!s.equals("")) {
                                        switch (l.getCell(i).getName()) {
                                            case "TYPE":
                                                WayPoint.modifySqlType(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "USEAGE":
                                                WayPoint.modifySqlUseage(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "LATITUDE":
                                                Balises.modifySqlLatitude(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "LONGITUDE":
                                                Balises.modifySqlLongitude(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "D MAG VAR":
                                                Balises.modifySqlMagneticVariation(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "WP ELEV":
                                                Balises.modifySqlElevation(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "DATUM CODE":
                                                Balises.modifySqlDatum(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "NAME IND":
                                                WayPoint.modifySqlNameIndicator(ListenerWayPoint.this.con, index, s);
                                                break;
                                            case "NAME/DESC":
                                                Balises.modifySqlName(ListenerWayPoint.this.con, index, s);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                            }

                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            System.out.println("ListenerWayPoint : type Modification : probleme ligne");
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
     * Action to make for each WayPoint failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerWayPoint : probleme parsage ligne");
    }
}
