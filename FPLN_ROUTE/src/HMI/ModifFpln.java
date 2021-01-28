package HMI;

import Communication.CommunicationManager;
import Model.Fpln;
import HMI.InputFpln;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import Model.Ndb;

/**
 * Class to manage the modification menu
 *
 * @author edouard.ladeira
 * @author jade.amah
 */
public class ModifFpln {

    private static Fpln tmpy = new Fpln(); //plan de vol temporaire
    private static ArrayList<String> modif = new ArrayList<>(Arrays.asList(new String[3])); //["section number of modification beginning", "section number of modification end", "new sections formated for ivy bus cf comManager"]
    private static String sectionChoice = ""; //section which concerned by modification (or before the insertion)
    private static String actionChoice = "";
    private static boolean modifReady = false; //passe à true lorsque la modification est NavDB-ok, repasse à false après envoi à LEGS
    private static boolean quit = false;
    private static int activeSection = 0;

    /**
     * Manage route modification
     * After modification entry, ask if it has to be activated (if not, modification is canceled)
     *
     * @param activeFpln
     * @param scanner
     * @param comManager
     * @throws java.sql.SQLException
     */
    public static void modifFpln(Fpln activeFpln, Scanner scanner, CommunicationManager comManager) throws SQLException {
        /*print les lignes du plan de vol ac des numeros
        demander quelle ligne modifier
         */
        int routeSize, i;
        String choice;
        while (!quit) { //(modifReady || !quit) {
            tmpy.copyFpln(activeFpln);
            System.out.print("\n");
            printActionMenu(comManager);
            selectAction(scanner, comManager);
            validateAction(scanner, comManager);
            i = comManager.getActiveSection();
            if (quit) {
                
            }
            else if ((sectionChoice.equals("") || Integer.valueOf(sectionChoice)>i) && !quit) {
                routeSize = tmpy.getRouteSize();
                if (sectionChoice.equals("")) {
                    System.out.println("\nNEW FPLN");
                    System.out.println("-------------------------");
                    System.out.println("FromAPT: "+tmpy.getAirportDep().getIdentifier());
                    System.out.println("FromAPT: "+tmpy.getAirportArr().getIdentifier()+"\n");
                } else {
                    System.out.println("\nNON SEQUENCED MODIFIED ROUTE");
                    System.out.println("-------------------------");
                }
                
                while (i < routeSize) {
                    System.out.println(i + " " + tmpy.getRoute().get(i).get(0) + " - " + tmpy.getRoute().get(i).get(1));
                    i++;
                }
                //System.out.println(modif);
                choice = printSelectModifActivation(scanner);
                if (choice.equals("1")) {
                    activeFpln.copyFpln(tmpy); //plan de vol temporaire devient le plan de vol actif
                    modifReady = true;
                    System.out.println("Successful modification !");
                } else {
                    System.out.println("Modification canceled !");
                }
            } else if (Integer.valueOf(sectionChoice)>i) {
                System.out.println("The relevant section was passed during the entry of the change.\nThe modification can not be taken into account !");
            }
        }
        quit = false;
    }

    //
    // Methods for Manage, Print, or Select options
    //

    /**
     * Print the modification menu
     * @param comManager
     */
    public static void printActionMenu(CommunicationManager comManager) {
        if (!comManager.isFlying()) {
            System.out.println("MODIF MENU (preflight)");
            System.out.println("-------------------------");
            System.out.println("1 - CHANGE OF THE LAST WPT IN SECTION");
            System.out.println("2 - INSERT WPTs BETWEEN SECTIONS");
            System.out.println("3 - CHANGE FPLN AIRPORTs");
            System.out.println("4 - Quit");
            System.out.println("-------------------------");
        } else {
            System.out.println("MODIF MENU (inflight");
            System.out.println("-------------------------");
            System.out.println("1 - CHANGE OF THE LAST WPT IN SECTION");
            System.out.println("2 - INSERT WPTs BETWEEN SECTIONS");
            System.out.println("3 - Quit");
            System.out.println("-------------------------");
        }
    }

    /**
     * Print the route (only non-sequenced part when in flight)
     *
     * @param comManager
     */
    public static void printNonSeqRoute(CommunicationManager comManager) {
        int i = 0;
        if (comManager.isFlying()) {
            activeSection = comManager.getActiveSection();
            i = activeSection + 1;
        }
        int routeSize = tmpy.getRouteSize();
        System.out.println("NON SEQUENCED ROUTE");
        System.out.println("-------------------------");
        while (i < routeSize) {
            System.out.println(i + " " + tmpy.getRoute().get(i).get(0) + " - " + tmpy.getRoute().get(i).get(1));
            i++;
        }
        System.out.println("-------------------------");
    }

    /**
     * Method to take into account, pilote pilote modification choice (Change or
     * Insertion)
     *
     * @param scanner
     * @param comManager
     */
    public static void selectAction(Scanner scanner, CommunicationManager comManager) {
        boolean select_valide = false;
        while (!select_valide) {
            System.out.print("Enter action to do: ");
            actionChoice = scanner.next();
            if (actionChoice.equals("1") || actionChoice.equals("2") || actionChoice.equals("3")) {
                select_valide = true;
            }
            else if (actionChoice.equals("4") && !comManager.isFlying()) {
                select_valide = true;
            } else {
                System.out.println("Input Error, bad number !");
            }
        }
    }

    /**
     * Take into account pilote choice
     *
     * @param scanner
     * @param comManager
     */
    public static void selectSection(Scanner scanner, CommunicationManager comManager) {
        boolean select_valide = false;
        int nMin, nMax;
        ArrayList<String> possibleSectionNb = new ArrayList<>();
        
        nMin = activeSection;
        if (comManager.isFlying()) {
            nMin += 1;
        }
        nMax = tmpy.getRouteSize();
        for (int i=nMin; i<nMax; i++) {
            possibleSectionNb.add(String.valueOf(i));
        }
        
        switch (actionChoice) {
            case "1":
                System.out.println("Which section do you want to modify ?");
                break;
            case "2":
                System.out.println("After which section do you want to insert the waypoint?");
                break;
            case "3":
                System.out.println("From which section do you want to enter the flight plan again ?");
                break;
            default:
                System.out.println("Programmation error");
                break;
        }
        
        while (!select_valide) {
            System.out.print("Enter the number of this section: ");
            sectionChoice = scanner.next();
            if (!possibleSectionNb.contains(sectionChoice)) {
                System.out.println("Input Error, bad number !");
            } else {
                select_valide = true;
            }
        }
    }

    /**
     * Print the section list of the remaining route
     * Get the choice of the section to reach
     * @param index
     * @param scanner
     * @return
     */
    public static int printSelectSectionToReachRoute(int index, Scanner scanner) {
        boolean select_valide = false;
        String result="";
        int size = tmpy.getRouteSize();
        ArrayList<String> possibleSectionNb = new ArrayList<>();

        for (int i=index; i<size; i++) {
            possibleSectionNb.add(String.valueOf(i));
        }
        
        System.out.println("\nCONNEXION MANAGEMENT");
        System.out.println("-------------------------");
        for (int i = index; i < size; i++) {
            System.out.println(i + " " + tmpy.getRoute().get(i).get(0) + " - " + tmpy.getRoute().get(i).get(1));
        }
        System.out.println("-------------------------");
        System.out.println("From which section do you want to reach the route?");
        
        while (!select_valide) {
            System.out.print("Enter the number of this section: ");
            result = scanner.next();
            if (!possibleSectionNb.contains(result)) {
                System.out.println("Input Error, bad number !");
            } else {
                select_valide = true;
            }
        }
        
        return Integer.valueOf(result);
    }
    
    /**
     * Print ways to reach the remaining route
     * Get the choice
     * @param scanner
     * @return
     */
    public static String printSelectModifActivation(Scanner scanner) {
        String choice;
        System.out.println("\n1- ACTIVATE MODIFICATION");
        System.out.println("2- CANCEL");
        System.out.println("-------------------------");
        System.out.print("Entry an option: ");
        choice = scanner.next();
        while ((!choice.equals("1")) && (!choice.equals("2"))) {
            System.out.print("Input Error, not a number ! Choose again: ");
            choice = scanner.next();
        }
        return choice;
    }
    
    /**
     * Print ways to reach the remaining route
     * Get the choice
     * @param scanner
     * @return
     */
    public static String printSelectConnnectionManageChoice(Scanner scanner) {
        String choice;
        System.out.println("\n1- Join directly the chosen section");
        System.out.println("2- Insert transitional WPT(s)");
        System.out.println("-------------------------");
        System.out.print("How to reach the selected section ?\nChoose an option: ");
        choice = scanner.next();
        while ((!choice.equals("1")) && (!choice.equals("2"))) {
            System.out.print("Input Error, not a number ! Choose again: ");
            choice = scanner.next();
        }
        return choice;
    }
    
    /**
     * Print possibilities list
     * Get the choice
     * @param mode:
     *  1: Print list of Awy when a waypoint (to insert) is entered
     *  2: Print list of possible Wpt to catch after the selection of the section to reach
     *  3: Print list of possible waypoint to replace one (change case)
     * @param possibilities
     * @param scanner
     * @return airway (or waypoint) identifier
     */
    public static String printSelectPossibilities(int mode, ArrayList<String> possibilities, Scanner scanner) {
        boolean select_valide = false;
        int i = 1;
        String choice = "";
        int size = possibilities.size();
        ArrayList<String> possibleSectionNb = new ArrayList<>();

        for (int j=1; j<=size; j++) {
            possibleSectionNb.add(String.valueOf(j));
        }
        
        //Print
        if (mode == 1) {
            System.out.println("\nPOSSIBLE AWY");
        } else if ((mode == 2) || (mode == 3)) {
            System.out.println("\nPOSSIBLE WPT");
        }
        System.out.println("-------------------------");
        while (i <= size) {
            System.out.println(i + " " + possibilities.get(i - 1));
            i++;
        }
        System.out.println("-------------------------");

        //Selection
        switch (mode) {
            case 1:
                System.out.print("Choose the AWY to go through.\nEnter its number: ");
                break;
            case 2:
                System.out.print("Choose the WPT to reach the route.\nEnter its number: ");
                break;
            case 3:
                System.out.print("Choose the new WPT.\nEnter its number: ");
                break;
            default:
                System.out.println("Programmation error !");
                break;
        }
        
        while (!select_valide) {
            System.out.print("Enter the number of this section: ");
            choice = scanner.next();
            if (!possibleSectionNb.contains(choice)) {
                System.out.println("Input Error, bad number !");
            } else {
                select_valide = true;
            }
        }
        return possibilities.get(Integer.valueOf(choice) - 1);
    }

    //
    // Methods to carry out modifications
    //
    /**
     * Execute pilote orders
     *
     * @param scanner
     * @param comManager
     * @throws java.sql.SQLException
     */
    public static void validateAction(Scanner scanner, CommunicationManager comManager) throws SQLException {
        switch (actionChoice) {
            case "1": //suppression et remplacement d'un segment
                System.out.print("\n");
                modifSectionFinalWpt(scanner, comManager);
                break;
            case "2": //insertion d'un segment
                System.out.print("\n");
                insertWpt(scanner, comManager);
                break;
            case "3":
                if (comManager.isFlying()) {
                    quit = true;
                } else {
                    changeApt(scanner, comManager);
                }
                break;
            case "4":
                quit = true;
                break;
            default:
                System.out.println("Programmation error !");
                break;
        }
    }

    /**
     * Manages pilote changes entry in the case of a change of section exit waypoint
     *
     * @param scanner
     * @param comManager
     */
    public static void modifSectionFinalWpt(Scanner scanner, CommunicationManager comManager) {
        boolean wptExist;
        int indexChg, indexIns, sectionToReach;
        int nbWptInsert = 0; //number of waypoints inserted
        String choice; 
        String wptId, newWptId, startWptId, endWptId, wptToReachId;
        String awyId, currentAwyId, awyToCatchId;
        ArrayList<String> possibleAwy = new ArrayList<>();
        ArrayList<String> possibleWpt = new ArrayList<>();

        modif.set(2, "");
        //choose which section to modified
        printNonSeqRoute(comManager);
        selectSection(scanner, comManager);
        indexChg = Integer.valueOf(sectionChoice);
        modif.set(0, String.valueOf(indexChg)); //update numStart

        //Manage final waypoint modification of the selected section 
        currentAwyId = tmpy.getRoute().get(indexChg).get(0);
        startWptId = tmpy.getRoute().get(indexChg).get(1); //Current entry wpt of the section to reach
        if (currentAwyId.equals("DIRECT")) {
            System.out.print("\nType the new WPT: ");
            newWptId = scanner.next();
            wptExist = Ndb.checkExist(newWptId, "route", "fixidentifiant");
            while (!wptExist) {
                 System.out.print("INVALID ENTRY WPT\nEnter an other WPT: ");
                 newWptId = scanner.next();
                 wptExist = Ndb.checkExist(newWptId, "route", "fixidentifiant");
            }
        } else {
            possibleWpt = Ndb.searchPossibleWpt(tmpy.getRoute().get(indexChg - 1).get(1), tmpy.getRoute().get(indexChg).get(1), currentAwyId);
            newWptId = printSelectPossibilities(3, possibleWpt, scanner);
        }
        updateModif(currentAwyId, newWptId);
        updateTmpyFpln("CHG", indexChg, currentAwyId, newWptId);
        System.out.println("Section successfully modified !");
        possibleWpt.clear();

        //Manage the connection between the modified section and the route
        //Choice of the section to reach
        sectionToReach = printSelectSectionToReachRoute(indexChg+1, scanner);
        awyToCatchId = tmpy.getRoute().get(sectionToReach).get(0);
        endWptId = tmpy.getRoute().get(sectionToReach).get(1);
        modif.set(1, String.valueOf(sectionToReach)); //updtae numEnd
        if ((sectionToReach - indexChg) > 1) {
            startWptId = tmpy.getRoute().get(sectionToReach-1).get(1);
            tmpy.removeRouteSection(indexChg + 1, sectionToReach - 1);
        }
        
        //In the selected section, choice of the wpt to reach
        if (awyToCatchId.equals("DIRECT")) {
            wptToReachId = endWptId;
        } else {
            possibleWpt = Ndb.searchReachableWpt(startWptId, endWptId, awyToCatchId);
            wptToReachId = printSelectPossibilities(2, possibleWpt, scanner);
        }
        
        choice = printSelectConnnectionManageChoice(scanner);
        if (choice.equals("1")) {//Join directly the chosen section
            manageRouteConnexion(indexChg + 1, awyToCatchId, wptToReachId, endWptId, scanner);
        }
        
        else if (choice.equals("2")) {//Insert transitional WPT(s)
            indexIns = indexChg + 1;
            possibleAwy.clear();
            while (choice.compareToIgnoreCase("END") != 0) {
                System.out.println("\nEnter a waypoint\nType DEL to delete the previous entry\nType END when you are done");
                wptId = scanner.next().toUpperCase();
                switch (wptId) {
                    case "END":
                        choice = wptId;
                        break;
                    case "DEL":
                        if (nbWptInsert == 0) {
                            System.out.println("You have not entered a WPT yet !");
                        } else {
                            indexIns--;
                            ArrayList<String> modifRouteList = new ArrayList<>(Arrays.asList(modif.get(2).split(", ")));
                            int sizeModif = modifRouteList.size();
                            System.out.println("Last entry "+modifRouteList.get(sizeModif-1)+" is deleted !");
                            modifRouteList.remove(sizeModif-1);
                            modif.set(2, String.join(", ", modifRouteList));
                            tmpy.removeRouteSection(indexIns);
                        }
                        break;
                    default:
                        wptExist = Ndb.checkExist(wptId, "route", "fixidentifiant");
                        if (!wptExist) {
                            System.out.println("INVALID ENTRY WPT");
                        } else {
                            possibleAwy = Ndb.searchReachableAwy(wptId, tmpy.getRoute().get(indexIns-1).get(1));
                            if (possibleAwy.size() == 1) {
                                updateModif(possibleAwy.get(0), wptId);
                                updateTmpyFpln("INS", indexIns, possibleAwy.get(0), wptId);
                            } else {
                                awyId = printSelectPossibilities(1, possibleAwy, scanner);
                                updateModif(awyId, wptId);
                                updateTmpyFpln("INS", indexIns, awyId, wptId);
                            }
                            indexIns++;
                            nbWptInsert++;
                        }
                        break;
                }
                //System.out.println(modif.get(2));
                //System.out.println(tmpy.getRoute());
                //System.out.println(indexIns);
                possibleAwy.clear();
            }
            //To reach the route (only if the next airway is not a "DIRECT"
            manageRouteConnexion(indexIns, awyToCatchId, wptToReachId, endWptId, scanner);
        }
    }

    /**
     * Manages pilote changes entry in the case of a change of waypoint insertion
     *
     * @param scanner
     * @param comManager
     */
    public static void insertWpt(Scanner scanner, CommunicationManager comManager) {
        boolean wptExist;
        String choice = "";
        int sectionToReach, indexIns;
        int nbWptInsert = 0; //number of waypoints inserted
        String wptId, startWptId, endWptId, wptToReachId;
        String awyId, startAwyId, awyToCatchId;
        ArrayList<String> possibleAwy = new ArrayList<>();
        ArrayList<String> possibleWpt = new ArrayList<>();

        modif.set(2, "");
        //choose after which secion to insert wpt(s)
        printNonSeqRoute(comManager);
        selectSection(scanner, comManager);
        indexIns = Integer.valueOf(sectionChoice);
        modif.set(0, String.valueOf(indexIns)); //update numStart
        startAwyId = tmpy.getRoute().get(indexIns).get(0);
        startWptId = tmpy.getRoute().get(indexIns).get(1);
        updateModif(startAwyId, startWptId);
        
        indexIns += 1;
        possibleAwy.clear();
        while (choice.compareToIgnoreCase("END") != 0) {
            System.out.println("\nEnter a waypoint\nType DEL to delete the previous entry\nType END when you are done");
            wptId = scanner.next().toUpperCase();
            switch (wptId) {
                case "END":
                    choice = wptId;
                    break;
                case "DEL":
                    if (nbWptInsert == 0) {
                        System.out.println("You have not entered a WPT yet !");
                    } else {
                        indexIns--;
                        ArrayList<String> modifRouteList = new ArrayList<>(Arrays.asList(modif.get(2).split(", ")));
                        int sizeModif = modifRouteList.size();
                        System.out.println("Last entry "+modifRouteList.get(sizeModif-1)+" is deleted !");
                        modifRouteList.remove(sizeModif-1);
                        modif.set(2, String.join(", ", modifRouteList));
                        tmpy.removeRouteSection(indexIns);
                    }
                    break;
                default:
                    wptExist = Ndb.checkExist(wptId, "route", "fixidentifiant");
                    if (!wptExist) {
                        System.out.println("INVALID ENTRY WPT");
                    } else {
                        possibleAwy = Ndb.searchReachableAwy(wptId, tmpy.getRoute().get(indexIns-1).get(1));
                        if (possibleAwy.size() == 1) {
                            updateModif(possibleAwy.get(0), wptId);
                            updateTmpyFpln("INS", indexIns, possibleAwy.get(0), wptId);
                        } else {
                            awyId = printSelectPossibilities(1, possibleAwy, scanner);
                            updateModif(awyId, wptId);
                            updateTmpyFpln("INS", indexIns, awyId, wptId);
                        }
                        indexIns++;
                        nbWptInsert++;
                    }
                    break;
            }
            //System.out.println(modif.get(2));
            //System.out.println(tmpy.getRoute());
            //System.out.println(indexIns);
            possibleAwy.clear();
        }
        
        //Choice of the section to reach
        sectionToReach = printSelectSectionToReachRoute(indexIns, scanner);
        awyToCatchId = tmpy.getRoute().get(sectionToReach).get(0);
        endWptId = tmpy.getRoute().get(sectionToReach).get(1);
        modif.set(1, String.valueOf(sectionToReach-nbWptInsert)); //updtae numEnd
        if ((sectionToReach - indexIns) > 0) {
            startWptId = tmpy.getRoute().get(sectionToReach-1).get(1);
            tmpy.removeRouteSection(indexIns, sectionToReach - 1);
        }

        //In the selected section, choice of the wpt to reach
        if (awyToCatchId.equals("DIRECT")) {
            wptToReachId = endWptId;
        } else {
            possibleWpt = Ndb.searchReachableWpt(startWptId, endWptId, awyToCatchId);
            wptToReachId = printSelectPossibilities(2, possibleWpt, scanner);
        }
        
        //To reach the route (only if the next airway is not a "DIRECT"
        manageRouteConnexion(indexIns, awyToCatchId, wptToReachId, endWptId, scanner);     
    }
    
    public static void changeApt(Scanner scanner, CommunicationManager comManager) throws SQLException {
        boolean select_valide = false;
        String choice = "";
        
        System.out.print("\nDo you want to change the departure APT ? (Y/N):");
        while (!select_valide) {
            choice = scanner.next().toUpperCase();
            if ((!choice.equals("Y")) && (!choice.equals("N"))) {
                System.out.println("Input Error, enter again !");
            } else {
                select_valide = true;
            }
        }
        if (choice.equals("Y")) {
            InputFpln.inputAirportDep(tmpy, scanner);
        }
        
        select_valide = false;
        System.out.print("\nDo you want to change the arrival APT ? (Y/N):");
        while (!select_valide) {
            choice = scanner.next().toUpperCase();
            if ((!choice.equals("Y")) && (!choice.equals("N"))) {
                System.out.println("Input Error, enter again !");
            } else {
                select_valide = true;
            }
        }
        if (choice.equals("Y")) {
            InputFpln.inputAirportArr(tmpy, scanner);
        }
        
        InputFpln.inputRoute(tmpy, scanner);
        sectionChoice = "";
    }

    /**
     * Method to manage connexion between the last waypoint inserted and the remaining route to reach
     * @param indexIns
     * @param awyToCatchId
     * @param wptToReachId
     * @param endWptId
     * @param scanner
     */
    public static void manageRouteConnexion(int indexIns, String awyToCatchId, String wptToReachId, String endWptId, Scanner scanner){
        String awyId, wptId;
        ArrayList<String> possibleAwy = new ArrayList<>();
        
        wptId = tmpy.getRoute().get(indexIns - 1).get(1);
        possibleAwy = Ndb.searchReachableAwy(wptId, wptToReachId);
        String tempAwy = possibleAwy.get(0); //first element of possibleAwy
        if ((possibleAwy.size() == 1) && (!awyToCatchId.equals(tempAwy))) {
            if (endWptId.equals(wptToReachId)) {//If the selected wpt to reach is the last one in the section
                updateModif(tempAwy, wptToReachId);
                updateTmpyFpln("CHG", indexIns, possibleAwy.get(0), wptToReachId);
            } else {
                updateModif(tempAwy, wptToReachId);
                updateModif(awyToCatchId, endWptId);
                updateTmpyFpln("INS", indexIns, possibleAwy.get(0), wptToReachId);
                updateTmpyFpln("CHG", indexIns + 1, awyToCatchId, endWptId);
            }
        } else if (!awyToCatchId.equals(tempAwy)) {
            awyId = printSelectPossibilities(1, possibleAwy, scanner);
            if (!awyToCatchId.equals(awyId)) {
                if (endWptId.equals(wptToReachId)) {//If the selected wpt to reach is the last one in the section
                    updateModif(awyId, wptToReachId);
                    updateTmpyFpln("CHG", indexIns, awyId, wptToReachId);
                } else {
                    updateModif(awyId, wptToReachId);
                    updateModif(awyToCatchId, endWptId);
                    updateTmpyFpln("INS", indexIns, awyId, wptToReachId);
                    updateTmpyFpln("CHG", indexIns + 1, awyToCatchId, endWptId);
                }
            }
        }
    }
    
    //
    // Method to manage class attributs
    //
    /**
     * Update modif attributs by inserting the new section
     *
     * @param awyId
     * @param wptId
     */
    public static void updateModif(String awyId, String wptId) {
        String newSectionString = awyId + "-" + wptId;
        if (modif.get(2).equals("")) {
            modif.set(2, newSectionString);
        } else {
            modif.set(2, modif.get(2) + ", " + newSectionString);
        }
    }

    /**
     * Update attribut tmpy
     *
     * @param mode - CHG : replacement of section number index by awyId-wptId -
     * INS : insertion of awyId-wptId at position number index in the arrayList
     * @param index
     * @param awyId
     * @param wptId
     */
    public static void updateTmpyFpln(String mode, int index, String awyId, String wptId) {
        ArrayList<String> newSection = new ArrayList<>(Arrays.asList(new String[2]));
        newSection.set(0, awyId);
        newSection.set(1, wptId);
        switch (mode) {
            case "CHG":
                tmpy.changeSectionInRoute(index, newSection);
                break;
            case "INS":
                tmpy.insertSectionInRoute(index, newSection);
                break;
            default:
                System.out.println("Programmation error !");
                break;
        }
    }

    /**
     * Get statement if a modification is available or not
     * @return modifReady
     */
    public static boolean isModifReady() {
        return modifReady;
    }

    /**
     * Set statement if a modification is available or not
     * @param newModifReady
     */
    public static void setModifReady(boolean newModifReady) {
        modifReady = newModifReady;
    }

    /**
     * Get the route modification object 
     * @return modif
     */
    public static ArrayList<String> getModif() {
        return modif;
    }
}
