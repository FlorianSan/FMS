/*
 * Class Listener Localizer defines action to make for each NDB parsed line 
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
 * Class Listener Localizer defines action to make for each NDB parsed line
 *
 * @author yoann
 */
public class ListenerNdb implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record Ndb Primary
     */
    private Schema schemaNdbPrimary;
    /**
     * Schema record Ndb Continuation
     */
    private Schema schemaNdbContinuation;
    /**
     * Schema record Ndb Flight
     */
    private Schema schemaNdbFlight;
    /**
     * Schema record Ndb Simulation
     */
    private Schema schemaNdbSimulation;

    /**
     * Constructeur de la classe
     *
     * @param schemaNdbPrimary Schema record Ndb Primary
     * @param schemaNdbContinuation Schema record Ndb Continuation
     * @param schemaNdbFlight Schema record Ndb Flight
     * @param schemaNdbSimulation Schema record Ndb Simulation
     */
    public ListenerNdb(Connection con, Schema schemaNdbPrimary, Schema schemaNdbContinuation, Schema schemaNdbFlight, Schema schemaNdbSimulation) {
        this.con = con;
        this.schemaNdbPrimary = schemaNdbPrimary;
        this.schemaNdbContinuation = schemaNdbContinuation;
        this.schemaNdbFlight = schemaNdbFlight;
        this.schemaNdbSimulation = schemaNdbSimulation;
    }

    /**
     * Action to make for each Ndb parsed line
     *
     * @param lpe Line Parsed Event
     * @throws JSaParException
     */
    @Override
    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
        Line l = lpe.getLine();
        String s = l.getCell("CONT NR").getStringValue();
        String code = l.getCell("SEC CODE").getStringValue();
        String subcode = l.getCell("SUB CODE").getStringValue();
        if (l.getLineType().equals("primary") && code.equals("D") && subcode.equals("B") && Integer.decode(s) == 1) {
            //NDB Primary RECORD

//            System.out.println(l.getCell("S/T").getStringValue().trim());
//            System.out.println(l.getCell("CUST/AREA").getStringValue().trim());
//            System.out.println(l.getCell("SEC CODE").getStringValue().trim());
//            System.out.println(l.getCell("SUB CODE").getStringValue().trim());
//
//            System.out.println("Identifiant de l'aeroport :" + l.getCell("ARPT IDENT").getStringValue().trim());
//            System.out.println("Code ICAO (classification geographique) :" + l.getCell("ICAO CODE").getStringValue().trim());
//            System.out.println("Identifiant NDB :" + l.getCell("NDB IDENT").getStringValue().trim());
//            System.out.println("Code ICAO (classification geographique) :" + l.getCell("ICAO CODE2").getStringValue().trim());
//            System.out.println("Continuite de l'enregistrement :" + l.getCell("CONT NR").getStringValue().trim());
//            System.out.println("Fréquence du NDB :" + l.getCell("NDB FREQ").getStringValue().trim());
//            System.out.println("Classe du NDB :" + l.getCell("NDB CLASS").getStringValue().trim());
//
//            // Navaid type 1
//            // navaid type 2
//            // Range Power H (200Watts or more) Blank (50 to 1999 W) M (25 to 50) L (less 25W)
//            // Additional Information  A (automatic weather) B (scheduled weather) W (no voice) blank (voice on frequency)
//            // Collocation B (BFO Operation)
//            String classNdb = l.getCell("NDB CLASS").getStringValue();
//
//
//            System.out.print("Navaid Type 1 :");
//            //Navaid Type 1
//            switch (classNdb.substring(0, 1)) {
//                case "H":
//                    System.out.println("NDB");
//                    break;
//                case "S":
//                    System.out.println("SABH");
//                    break;
//                case "M":
//                    System.out.println("Marine Beacon");
//                    break;
//                case " ":
//                    System.out.println(" ");
//                    break;
//            }
//
//            System.out.print("Navaid Type 2 :");
//            switch (classNdb.substring(1, 2)) {
//                case "I":
//                    System.out.println("Inner Marker");
//                    break;
//                case "M":
//                    System.out.println("Middle Marker");
//                    break;
//                case "O":
//                    System.out.println("Outer Marker");
//                    break;
//                case "C":
//                    System.out.println("Back Marker");
//                    break;
//                case " ":
//                    System.out.println(" ");
//                    break;
//            }
//
//
//            System.out.println("Latitude :" + l.getCell("NDB LATITUDE").getStringValue().trim());
//            System.out.println("Longitude :" + l.getCell("NDB LONGITUDE").getStringValue().trim());
//
//            System.out.println("Variation Magnetique :" + l.getCell("MAG VAR").getStringValue().trim());
//            // Variation magnetique (premier caractere + 4 chiffre)
//            //  E Magnetic variation is East of TRUE North
//            //  W Magnetic variation is West of TRUE North
//            //  T The element defined in the current record is oriented to TRUE North in an area in which the local variation is not zero.
//
//            System.out.println("Reference Geographique :" + l.getCell("DATUM CODE").getStringValue().trim());
//            if (!l.getCell("DATUM CODE").getStringValue().trim().equals("WGE") && !l.getCell("DATUM CODE").getStringValue().trim().equals("")) {
//                throw new RuntimeException("PB de reference geographique");
//            }
//
//            System.out.println("NDB navaid Name :" + l.getCell("NDB NAVAID NAME").getStringValue().trim());
//            System.out.println("File record number :" + l.getCell("FILE RECORD NUMBER").getStringValue().trim());
//            System.out.println("Cycle :" + l.getCell("CYCLE").getStringValue().trim());
//            System.out.println("");
//            System.out.println("");

            String aeroport = l.getCell("ARPT IDENT").getStringValue().trim();
            String classNdb = l.getCell("NDB CLASS").getStringValue();
            String datum = l.getCell("DATUM CODE").getStringValue().trim();
            String frequence = l.getCell("NDB FREQ").getStringValue().trim();

            String icaoAeroport = l.getCell("ICAO CODE").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE2").getStringValue().trim();
            String identifiant = l.getCell("NDB IDENT").getStringValue().trim();
            String latitude = l.getCell("NDB LATITUDE").getStringValue();
            String longitude = l.getCell("NDB LONGITUDE").getStringValue();
            String magneticVariation = l.getCell("MAG VAR").getStringValue();
            String ndbName = l.getCell("NDB NAVAID NAME").getStringValue().trim();

            Ndb.addSql(this.con, aeroport, classNdb, datum, frequence, icaoAeroport, icaoCode, identifiant, latitude, longitude, magneticVariation, ndbName);

        } else {
            //On extrait l'APPL Type pour aiguiller le nouveau parsage et on recopie la ligne
            String sub = l.getCell("NDB FREQ").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();
            for (int i = 0; i < l.getNumberOfCells(); i++) {
                newString = newString.append(l.getCell(i).getStringValue());
            }
            //aiguillage vers le nouveau parsage de la ligne newString
            switch (sub) {
                case "P": {
                    //type flight
                    Parser p = new Parser(this.schemaNdbFlight);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("flight")) {
//                                System.out.println("NDB identifiant :" + l.getStringCellValue("NDB IDENT"));
//                                System.out.println("FIR identifiant :" + l.getStringCellValue("FIR IDENT"));
//                                System.out.println("UIR identifiant :" + l.getStringCellValue("UIR IDENT"));
//                                System.out.println("Indicateur de date :" + l.getStringCellValue("S/E IND"));
//                                System.out.println("Date de debut et de fin (GMT date) :" + l.getStringCellValue("START/END DATE"));
//                                System.out.println("Numero enregistrement :" + l.getStringCellValue("FILE RECORD NUMBER"));
//                                System.out.println("Numéro Cycle :" + l.getStringCellValue("CYCLE"));
//                                System.out.println("");
                                String icaoCode = l.getCell("ICAO CODE2").getStringValue().trim();
                                String identifiant = l.getCell("NDB IDENT").getStringValue().trim();

                                String firIdentifier = l.getCell("FIR IDENT").getStringValue().trim();
                                String UirIdentifier = l.getCell("UIR IDENT").getStringValue().trim();
                                String sEIndicator = l.getCell("S/E IND").getStringValue().trim();
                                String sEDate = l.getCell("START/END DATE").getStringValue().trim();

                                Ndb.addContinuationSql(ListenerNdb.this.con, icaoCode, identifiant, firIdentifier, UirIdentifier, sEIndicator, sEDate);

                            }

                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            System.out.println("NdbPrimary type flight : probleme ligne");
                        }
                    };
                    p.addParsingEventListener(a);

                    p.parse(new StringReader(newString.toString()));

                    break;
                }
                case "N": {
                    //type continuation NON PRESENT
                    Parser p = new Parser(this.schemaNdbContinuation);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("continuation")) {
                                System.out.println("Notes sur l'enregistrement :" + l.getStringCellValue("NOTES ON CONTINUATION RECORD"));
                                System.out.println("NDB fréquence :" + l.getStringCellValue("NDB FREQ"));
                                System.out.println("Reserve :" + l.getStringCellValue("RESERVED"));
                                System.out.println("Numéro enregistrement :" + l.getStringCellValue("FILE RECORD NUMBER"));
                                System.out.println("Numéro cycle" + l.getStringCellValue("CYCLE"));
                                System.err.println("");
                            }

                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            System.out.println("ListenerNdbPrimary : type Continuation : probleme ligne");
                        }
                    };
                    p.addParsingEventListener(a);
                    p.parse(new StringReader(newString.toString()));
                    break;

                }
                case "S": {
                    //type simulation NON PRESENT
                    Parser p = new Parser(this.schemaNdbSimulation);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("simulation")) {
                                System.out.println("Facility characteristics :" + l.getStringCellValue("FAC CHAR"));
                                System.out.println("Facility elevation :" + l.getStringCellValue("FAC ELEV"));
                                System.out.println("Reserve :" + l.getStringCellValue("RESERVED"));
                                System.out.println("Numéro enregistrement :" + l.getStringCellValue("FILE RECORD NUMBER"));
                                System.out.println("Numéro cycle" + l.getStringCellValue("CYCLE"));
                                System.err.println("");

                            }
                        }

                        @Override
                        public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                            System.out.println("ListenerNdbPrimary : type Simulation : probleme ligne");
                        }
                    };
                    p.addParsingEventListener(a);
                    p.parse(new StringReader(newString.toString()));
                    break;
                }
                default: {
                    // type modification primary
                    Parser p = new Parser(this.schemaNdbPrimary);
                    ParsingEventListener a = new ParsingEventListener() {
                        @Override
                        public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                            Line l = lpe.getLine();
                            if (l.getLineType().equals("primary")) {
                                // VHF Navaid Primary Continuation
//                              System.out.print("CHAMP A MODIFIER : -- ");
//                                System.out.println("mod : " + icaoCode + " " + identifiant);
                                int index = Balises.lastRecord(con);
                                for (int i = 11; i < l.getNumberOfCells() - 2; i++) {
                                    String s = l.getCell(i).getStringValue().trim();
                                    if (!s.equals("")) {
////                                        System.out.println(l.getCell(i).getName() + s);
                                        switch (l.getCell(i).getName()) {
                                            case "NDB FREQ":
                                                Ndb.modifySqlFrequence(ListenerNdb.this.con, index, s);
                                                break;
                                            case "NDB CLASS":
                                                Ndb.modifySqlClass(ListenerNdb.this.con, index, s);
                                                break;
                                            case "NDB LATITUDE":
                                                Balises.modifySqlLatitude(ListenerNdb.this.con, index, s);
                                                break;
                                            case "NDB LONGITUDE":
                                                Balises.modifySqlLongitude(ListenerNdb.this.con, index, s);
                                                break;
                                            case "MAG VAR":
                                                Balises.modifySqlMagneticVariation(ListenerNdb.this.con, index, s);
                                                break;
                                            case "DATUM CODE":
                                                Balises.modifySqlDatum(ListenerNdb.this.con, index, s);
                                                break;
                                            case "NDB NAVAID NAME":
                                                Balises.modifySqlName(ListenerNdb.this.con, index, s);
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
                            System.out.println("ListenerNdbPrimary : type Modification : probleme ligne");
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
     * Action to make for each Ndb failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerNdbPrimary : probleme ligne");
    }
}
