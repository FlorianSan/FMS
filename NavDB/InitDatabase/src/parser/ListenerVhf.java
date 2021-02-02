/*
 * Class Listener Localizer defines action to make for each VHF parsed line 
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
 * Class Listener Localizer defines action to make for each VHF parsed line
 *
 * @author yoann
 */
public class ListenerVhf implements ParsingEventListener {

    /**
     * Database Connection
     */
    private Connection con;
    /**
     * Schema record VHF Primary
     */
    private Schema schemaVhfPrimary;
    /**
     * Schema record VHF Continuation
     */
    private Schema schemaVhfContinuation;
    /**
     * Schema record VHF Flight
     */
    private Schema schemaVhfFlight;
    /**
     * Schema record VHF Limitation
     */
    private Schema schemaVhfLimitation;
    /**
     * Schema record VHF Simulation
     */
    private Schema schemaVhfSimulation;

    /**
     * Listener VHF Constructor
     *
     * @param con DataBase Connection
     * @param schemaVhfPrimary Schema record VHF Primary
     * @param schemaVhfContinuation Schema record VHF Continuation
     * @param schemaVhfFlight Schema record VHF Flight
     * @param schemaVhfLimitation Schema record VHF Limitation
     * @param schemaVhfSimulation Schema record VHF Simulation
     */
    public ListenerVhf(Connection con, Schema schemaVhfPrimary, Schema schemaVhfContinuation, Schema schemaVhfFlight, Schema schemaVhfLimitation, Schema schemaVhfSimulation) {
        this.con = con;
        this.schemaVhfPrimary = schemaVhfPrimary;
        this.schemaVhfContinuation = schemaVhfContinuation;
        this.schemaVhfFlight = schemaVhfFlight;
        this.schemaVhfLimitation = schemaVhfLimitation;
        this.schemaVhfSimulation = schemaVhfSimulation;
    }

    /**
     * Action to make for each VHF parsed line
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
        if (l.getLineType().equals("primary") && code.equals("D") && subcode.equals(" ") && cont == 1) {

//            System.out.println("identifiant de l'aeroport : " + l.getCell("ARPT IDENT").getStringValue().trim());
//            System.out.println("code icao (classification geographique) :" + l.getCell("ICAO CODE").getStringValue().trim());
//            System.out.println("identifiant du VOR :" + l.getCell("VOR IDENT").getStringValue().trim());
//            System.out.println("code icao (classification geographique) :" + l.getCell("ICAO CODE2").getStringValue().trim());
//            System.out.println("continuité de l'enregistrement :" + l.getCell("CONT NR").getStringValue().trim());
//            System.out.println("frequence du vor :" + l.getCell("VOR FREQ").getStringValue().trim());
//            System.out.println("Classe du Navaid :" + l.getCell("VHF CLASS").getStringValue().trim());
            // Definition des classes
//            Navaid type 1 V (Vor)
//            Navaid type 2 D (DME) T(TACAN) M(MIL TACAN) I (ILS/DME) N (MLS/DME/N) P (MLS/DME/P)
//            Range/Power T (terminal) L (Low altitude) H (High altitude) U (undefined) C(ILS/TACAN)   
//            Additional Information D (Biased) A (automatic transcribed weather) B (schelduled weather) W (no voice) blank ( voice on frequency)
//            String vhfClass = l.getCell("VHF CLASS").getStringValue();
//            if (vhfClass.substring(0, 1).equals("V")) {
//                System.out.println("C'est un VOR");
//            }
//
//            if (vhfClass.substring(1, 2).equals("D")) {
//                System.out.println("C'est un DME aussi");
//            } else if (vhfClass.substring(1, 2).equals("T")) {
//                System.out.println("C'est un TACAN aussi");
//            } else if (vhfClass.substring(1, 2).equals("M")) {
//                System.out.println("C'est un MIL TACAN aussi");
//            } else if (vhfClass.substring(1, 2).equals("I")) {
//                System.out.println("C'est un ILS/DME");
//            } else if (vhfClass.substring(1, 2).equals("N")) {
//                System.out.println("C'est un MLS/DME/N");
//            } else if (vhfClass.substring(1, 2).equals("P")) {
//                System.out.println("C'est un MLS/DME/P");
//            }


//
//            System.out.println("Vor latitude :" + l.getCell("VOR LATITUDE").getStringValue().trim());
//            System.out.println("Vor longitude :" + l.getCell("VOR LONGITUDE").getStringValue().trim());
//            System.out.println("identifiant DME :" + l.getCell("DME IDENT").getStringValue().trim());
//            System.out.println("Dme latitude :" + l.getCell("DME LATITUDE").getStringValue().trim());
//            System.out.println("Dme longitude :" + l.getCell("DME LONGITUDE").getStringValue().trim());
//            System.out.println("Declinaison magnetique :" + l.getCell("STA DECL").getStringValue().trim());
            // Declinaison magnetique (premier caractere)
//            E Declination is East of True North
//            W Declination is West of True North
//            T Station is oriented to True North in an area in which the local variation is not zero.
//            G Station is oriented to Grid North

//
//            System.out.println("Dme elevation :" + l.getCell("DME ELEV").getStringValue().trim());
//            System.out.println("VHF Navaid facility usable ranges :" + l.getCell("MERIT").getStringValue().trim());
//            // VHF navaid facility
//            0 Terminal Use (generally within 25NM)
//            1 Low Altitude Use (generally within 40NM)
//            2 High Altitude Use (generally within 130NM)
//            3 Extended High Altitude Use (generally beyond 130NM)
//            7 Navaid not included in a civil international NOTAM system
//            9 Navaid Out of Service

            String vhfClass = l.getCell("VHF CLASS").getStringValue();

            String aeroport = l.getCell("ARPT IDENT").getStringValue().trim();
            String icaoAeroport = l.getCell("ICAO CODE").getStringValue().trim();
            String identifiantVor = l.getCell("VOR IDENT").getStringValue().trim();
            String icaoCode = l.getCell("ICAO CODE2").getStringValue().trim();
            String frequence = l.getCell("VOR FREQ").getStringValue().trim();
            String Name = l.getCell("VHF NAVAID NAME").getStringValue().trim();
            String datum = l.getCell("DATUM CODE").getStringValue().trim();
            String latitudeVor = l.getCell("VOR LATITUDE").getStringValue();
            String longitudeVor = l.getCell("VOR LONGITUDE").getStringValue();
            String identifiantDme = l.getCell("DME IDENT").getStringValue().trim();
            String latitudeDme = l.getCell("DME LATITUDE").getStringValue();
            String longitudeDme = l.getCell("DME LONGITUDE").getStringValue();
            String magneticVariation = l.getCell("STA DECL").getStringValue().trim();
            String frequenceProtection = l.getCell("FREQ PRO").getStringValue().trim();
            String biaisDme = l.getCell("DME BIAIS").getStringValue().trim();
            String navaidFacility = l.getCell("MERIT").getStringValue().trim();
            String dmeElevation = l.getCell("DME ELEV").getStringValue().trim();




            if (vhfClass.substring(0, 1).equals("V") && !vhfClass.substring(1, 2).equals("D")) {
                //VOR SEUL
//                System.out.println("VOR seul");

                Vor.addSql(this.con, icaoCode, identifiantVor, Name, latitudeVor, longitudeVor,
                        aeroport, icaoAeroport, magneticVariation, datum, frequence, frequenceProtection, navaidFacility);

            } else if (vhfClass.substring(0, 1).equals("V") && vhfClass.substring(1, 2).equals("D")) {
                //VOR DME
//                System.out.println("VOR DME");

                VorDme.addSql(this.con, icaoCode, identifiantVor, Name, latitudeVor, longitudeVor, aeroport, icaoAeroport, magneticVariation, datum, dmeElevation, identifiantDme, latitudeDme, longitudeDme, frequence, frequenceProtection, biaisDme, navaidFacility);

            } else if (vhfClass.substring(0, 1).equals(" ") && vhfClass.substring(1, 2).equals("D")) {
                //DME SEUL

                Dme.addSql(this.con, icaoCode, identifiantDme, Name, latitudeDme, longitudeDme,
                        aeroport, icaoAeroport, magneticVariation, datum, dmeElevation, frequence,
                        frequenceProtection, biaisDme, navaidFacility);
//                System.out.println("DME seul");

            } else if (vhfClass.substring(0, 1).equals(" ") && vhfClass.substring(1, 2).equals("T")) {
                //TACAN SEUL

                Tacan.addSql(this.con, icaoCode, identifiantDme, Name, latitudeDme, longitudeDme,
                        aeroport, icaoAeroport, magneticVariation, datum, dmeElevation, frequence,
                        frequenceProtection, biaisDme, navaidFacility);
//                System.out.println("TACAN seul");

            } else if (vhfClass.substring(0, 1).equals(" ") && vhfClass.substring(1, 2).equals("I")) {
                //ILS/DME SEUL
                IlsDme.addSql(this.con, icaoCode, identifiantDme, Name, latitudeDme, longitudeDme,
                        aeroport, icaoAeroport, magneticVariation, datum, dmeElevation, frequence,
                        frequenceProtection, biaisDme, navaidFacility);
//                System.out.println("ILS/DME");

            } else if (vhfClass.substring(0, 1).equals(" ") && vhfClass.substring(1, 2).equals("N")) {
                //MLS/DME/PSEUL -- NON PRESENT
                System.out.println("MLS/DME/P");
            } else if (vhfClass.substring(0, 1).equals(" ") && vhfClass.substring(1, 2).equals("P")) {
                //MLS/DME/P SEUL -- NON PRESENT
                System.out.println("MLS/DME/P");
            } else if (vhfClass.substring(0, 1).equals(" ") && vhfClass.substring(1, 2).equals("M")) {
                //MIL TACAN --NON PRESENT
                System.out.println("MIL TACAN");
            } else {
                System.out.println("**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-autre cas" + vhfClass);
            }


//            String aeroport = l.getCell("ARPT IDENT").getStringValue().trim();
//            String icaoCode = l.getCell("ICAO CODE").getStringValue().trim();
//            String vorIdentifiant = l.getCell("VOR IDENT").getStringValue().trim();
//
//            String icaoBalise = l.getCell("ICAO CODE2").getStringValue().trim();
//
//            String frequence = l.getCell("VOR FREQ").getStringValue().trim();
//
//
//
//
//            String nom = l.getCell("VHF NAVAID NAME").getStringValue().trim();
//            String datum = l.getCell("DATUM CODE").getStringValue().trim();
//
//            String vorlatitude = l.getCell("VOR LATITUDE").getStringValue();
//            String vorlongitude = l.getCell("VOR LONGITUDE").getStringValue();
//            String dmeIdentifiant = l.getCell("DME IDENT").getStringValue().trim();
//            String dmeLatitude = l.getCell("DME LATITUDE").getStringValue();
//            String dmeLongitude = l.getCell("DME LONGITUDE").getStringValue();
//            String declinaisonMagnetique = l.getCell("STA DECL").getStringValue().trim();
//
//
//            String frequenceProtection = l.getCell("FREQ PRO").getStringValue().trim();
//            String biaisDme = l.getCell("DME BIAIS").getStringValue().trim();
//            String navaidFacility = l.getCell("MERIT").getStringValue().trim();
//            String dmeElevation = l.getCell("DME ELEV").getStringValue().trim();


        } else {
            String sub = l.getCell("VOR FREQ").getStringValue();
            sub = sub.substring(0, 1);
            StringBuffer newString = new StringBuffer();
            for (int i = 0; i < l.getNumberOfCells(); i++) {
                newString = newString.append(l.getCell(i).getStringValue());
            }
            if (sub.equals("P")) {
                //VHF Navaid APPL TYPE = Flight Planning App Continuation
                Parser p = new Parser(this.schemaVhfFlight);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        Line l = lpe.getLine();
                        if (l.getLineType().equals("flight")) {
                            String firIdentifier = l.getCell("FIR IDENT").getStringValue().trim();
                            String UirIdentifier = l.getCell("UIR IDENT").getStringValue().trim();
                            String sEIndicator = l.getCell("S/E END").getStringValue().trim();
                            String sEDate = l.getCell("START/END DATE").getStringValue().trim();

                            int id = Balises.lastRecord(ListenerVhf.this.con);
//                            System.out.println("id :" + id);
                            Balises.addContinuationBalises(ListenerVhf.this.con, firIdentifier, UirIdentifier, sEIndicator, sEDate, id);
                        }
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        System.out.println("ListenerVhf Flight : probleme ligne");
                    }
                };
                p.addParsingEventListener(a);

                p.parse(new StringReader(newString.toString()));
            } else if (sub.equals("L")) {
                //VHF Navaid APPL TYPE = VHF Navaid Limitation -- NON PRESENT
                Parser p = new Parser(this.schemaVhfLimitation);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));

            } else if (sub.equals("N")) {
                //VHF Navaid APPL TYPE = Continuation  -- NON PRESENT
                Parser p = new Parser(this.schemaVhfContinuation);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            } else if (sub.equals("S")) {
                // Record Simulation -- NON PRESENT
                Parser p = new Parser(this.schemaVhfSimulation);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            } else {
                //VHF Navaid APPL TYPE = Primary Modification
                Parser p = new Parser(this.schemaVhfPrimary);
                ParsingEventListener a = new ParsingEventListener() {
                    @Override
                    public void lineParsedEvent(LineParsedEvent lpe) throws JSaParException {
                        Line l = lpe.getLine();
//                        System.out.println("ligne :" + l);
                        int id = Balises.lastRecord(ListenerVhf.this.con);
                        String type = Balises.lastType(ListenerVhf.this.con, id);
                        if (type.equals("vor")) {
                            for (int i = 12; i < l.getNumberOfCells() - 2; i++) {
                                String s = l.getCell(i).getStringValue().trim();
                                if (!s.equals("")) {
                                    switch (l.getCell(i).getName()) {
                                        case "VOR FREQ":
                                            Vor.modifySqlFrequence(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VOR LATITUDE":
                                            Balises.modifySqlLatitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VOR LONGITUDE":
                                            Balises.modifySqlLongitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "STA DECL":
                                            Balises.modifySqlMagneticVariation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME ELEV":
                                            Balises.modifySqlElevation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "MERIT":
                                            Vor.modifySqlMerit(ListenerVhf.this.con, id, s);
                                            break;
                                        case "FREQ PRO":
                                            Vor.modifySqlFrequenceProtection(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DATUM CODE":
                                            Balises.modifySqlDatum(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VHF NAVAID NAME":
                                            Balises.modifySqlName(ListenerVhf.this.con, id, s);
                                            break;
                                        default:
                                            break;

                                    }
                                }
                            }

                        } else if (type.equals("dme")) {
                            for (int i = 12; i < l.getNumberOfCells() - 2; i++) {
                                String s = l.getCell(i).getStringValue().trim();
                                if (!s.equals("")) {
                                    switch (l.getCell(i).getName()) {
                                        case "VOR FREQ":
                                            Dme.modifySqlFrequence(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LATITUDE":
                                            Balises.modifySqlLatitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LONGITUDE":
                                            Balises.modifySqlLongitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME BIAIS":
                                            Dme.modifySqlBiais(ListenerVhf.this.con, id, s);
                                            break;
                                        case "STA DECL":
                                            Balises.modifySqlMagneticVariation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME ELEV":
                                            Balises.modifySqlElevation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "MERIT":
                                            Dme.modifySqlMerit(ListenerVhf.this.con, id, s);
                                            break;
                                        case "FREQ PRO":
                                            Dme.modifySqlFrequenceProtection(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DATUM CODE":
                                            Balises.modifySqlDatum(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VHF NAVAID NAME":
                                            Balises.modifySqlName(ListenerVhf.this.con, id, s);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        } else if (type.equals("ils")) {
                            for (int i = 12; i < l.getNumberOfCells() - 2; i++) {
                                String s = l.getCell(i).getStringValue().trim();
                                if (!s.equals("")) {
                                    switch (l.getCell(i).getName()) {
                                        case "VOR FREQ":
                                            IlsDme.modifySqlFrequence(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LATITUDE":
                                            Balises.modifySqlLatitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LONGITUDE":
                                            Balises.modifySqlLongitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME BIAIS":
                                            IlsDme.modifySqlBiais(ListenerVhf.this.con, id, s);
                                            break;
                                        case "STA DECL":
                                            Balises.modifySqlMagneticVariation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME ELEV":
                                            Balises.modifySqlElevation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "MERIT":
                                            IlsDme.modifySqlMerit(ListenerVhf.this.con, id, s);
                                            break;
                                        case "FREQ PRO":
                                            IlsDme.modifySqlFrequenceProtection(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DATUM CODE":
                                            Balises.modifySqlDatum(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VHF NAVAID NAME":
                                            Balises.modifySqlName(ListenerVhf.this.con, id, s);
                                            break;

                                    }
                                }
                            }
                        } else if (type.equals("tacan")) {
                            for (int i = 12; i < l.getNumberOfCells() - 2; i++) {
                                String s = l.getCell(i).getStringValue().trim();
                                if (!s.equals("")) {
                                    switch (l.getCell(i).getName()) {
                                        case "VOR FREQ":
                                            Tacan.modifySqlFrequence(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LATITUDE":
                                            Balises.modifySqlLatitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LONGITUDE":
                                            Balises.modifySqlLongitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME BIAIS":
                                            Tacan.modifySqlBiais(ListenerVhf.this.con, id, s);
                                            break;
                                        case "STA DECL":
                                            Balises.modifySqlMagneticVariation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME ELEV":
                                            Balises.modifySqlElevation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "MERIT":
                                            Tacan.modifySqlMerit(ListenerVhf.this.con, id, s);
                                            break;
                                        case "FREQ PRO":
                                            Tacan.modifySqlFrequenceProtection(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DATUM CODE":
                                            Balises.modifySqlDatum(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VHF NAVAID NAME":
                                            Balises.modifySqlName(ListenerVhf.this.con, id, s);
                                            break;

                                    }
                                }
                            }
                        } else if (type.equals("vorDme")) {
                            for (int i = 12; i < l.getNumberOfCells() - 2; i++) {
                                String s = l.getCell(i).getStringValue().trim();
                                if (!s.equals("")) {
                                    switch (l.getCell(i).getName()) {
                                        case "VOR FREQ":
                                            VorDme.modifySqlFrequence(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VOR LATITUDE":
                                            Balises.modifySqlLatitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VOR LONGITUDE":
                                            Balises.modifySqlLongitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LATITUDE":
                                            VorDme.modifySqlDmeLatitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME LONGITUDE":
                                            VorDme.modifySqlDmeLongitude(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME BIAIS":
                                            VorDme.modifySqlBiais(ListenerVhf.this.con, id, s);
                                            break;
                                        case "STA DECL":
                                            Balises.modifySqlMagneticVariation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DME ELEV":
                                            Balises.modifySqlElevation(ListenerVhf.this.con, id, s);
                                            break;
                                        case "MERIT":
                                            VorDme.modifySqlMerit(ListenerVhf.this.con, id, s);
                                            break;
                                        case "FREQ PRO":
                                            VorDme.modifySqlFrequenceProtection(ListenerVhf.this.con, id, s);
                                            break;
                                        case "DATUM CODE":
                                            Balises.modifySqlDatum(ListenerVhf.this.con, id, s);
                                            break;
                                        case "VHF NAVAID NAME":
                                            Balises.modifySqlName(ListenerVhf.this.con, id, s);
                                            break;

                                    }
                                }
                            }
                        }




//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
                        System.out.println("ListenerVhf Modification : probleme ligne");
                    }
                };
                p.addParsingEventListener(a);
                p.parse(new StringReader(newString.toString()));
            }
        }
    }

    /**
     * Action to make for each VHF failed parsed line
     *
     * @param lee Line Error Event
     * @throws ParseException
     * @throws MaxErrorsExceededException
     */
    @Override
    public void lineErrorEvent(LineErrorEvent lee) throws ParseException, MaxErrorsExceededException {
        System.out.println("ListenerVhf : probleme parsage ligne");
    }
}
