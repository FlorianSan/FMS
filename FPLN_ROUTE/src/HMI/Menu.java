/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HMI;

import Model.Fpln;
import java.sql.SQLException;
import java.util.Scanner;
import Communication.CommunicationManager;
import Model.Airport;
import java.util.ArrayList;


/**
 * Class to manage the application with the console
 * @author edouard.ladeira
 */
public class Menu {
    private static String menuChoice = "";
    private static boolean  quit = false;
    
    /**
     * Console management instead of HMI
     * @param fpln
     * @param comManager
     * @throws SQLException 
     */
    public static void manage(Fpln fpln, CommunicationManager comManager, Scanner scanner) throws SQLException {    
        while (!quit) {
            printMenu();
            select(scanner);
            validate(fpln, comManager, scanner);
        }
    } 
    
    /**
     * Print the primary menu
     */
    public static void printMenu() {  
        System.out.println("\n1 - FPLN");
        System.out.println("2 - READY");
        System.out.println("3 - INIT");
        System.out.println("4 - MODIF");
        System.out.println("5 - ROUTE");
        System.out.println("6 - Quit"); 
        System.out.println("-------------------------");
     }
    
    /**
     * Take into account pilote choice
     * @param scanner
     */
    public static void select(Scanner scanner) {
        boolean select_valide = false;
        ArrayList<String> options = new ArrayList<>();
        
        for (int i=1; i<=6; i++) {
            options.add(String.valueOf(i));
        }
        
        while (!select_valide) {
            System.out.print("Enter the number of this section: ");
            menuChoice = scanner.next();
            if (!options.contains(menuChoice)) {
                System.out.println("Input Error, bad number");
            } else {
                select_valide = true;
            }
        }
    }
    
    /**
     * Execute pilote orders
     * @param fpln
     * @param comManager
     * @param scanner
     * @throws java.sql.SQLException
     */
    public static void validate(Fpln fpln, CommunicationManager comManager, Scanner scanner) throws SQLException {
        switch(menuChoice){
            case "1": //flight plan creation
                InputFpln inputFpln = new InputFpln();
                inputFpln.inputFpln(fpln, scanner);
                
                //création du plan de vol utile pour la démonstration
//                fpln.setAirportDep("LFBO");
//                fpln.setAirportArr("LFPO");
//                fpln.addSection("DIRECT", "FISTO");
//                fpln.addSection("UY156", "PERIG");
//                //fpln.addSegment("DIRECT", "NORON");
//                //fpln.addSegment("DIRECT", "FOUCO");
//                fpln.addSection("UT210", "TUDRA");
//                //fpln.addSegment("DIRECT", "TUDRA");
//                fpln.addSection("UT158", "AMB");
//                fpln.addSection("DIRECT", "STAR");
//                System.out.println("Fpln successfully filled !");
                menuChoice = "";
                break;
                
            case "2": //Send ready message to LEGS
                if (ModifFpln.isModifReady()){
                    comManager.sendModifReady();
                }
                else {
                    comManager.sendReady();
                }
                menuChoice = "";
                break;
                
            case "3": //Send Init flight plan to LEGS
                if (ModifFpln.isModifReady()){
                    comManager.sendModifFpln(ModifFpln.getModif()); 
                }
                else{
                    String aptSimu = comManager.getAptSim();             
                    while ("".equals(aptSimu)) {
                        try {
                            comManager.sendErrorAptSim();
                            // faire une pause de 1 seconds
                            Thread.sleep(1000);
                            aptSimu = comManager.getAptSim();
                        } catch (InterruptedException e) {
                            System.out.println(e); 
                        }
                    }

                    Airport aptDep = fpln.getAirportDep();
                    if (aptSimu.equals(aptDep.getIdentifier())) {
                        comManager.sendInitFpln(fpln);
                        
                    }
                    else {
                        System.out.println("Flight Plan Error: departure airport isn't the simulation airport !");
                    }
                }
                menuChoice = "";
                break;
                
            case "4": //Allow the pilote to modify the flight plan
                String autopilot_mode = comManager.getAP_Mode();
                //autopilot_state = "" >> modif preflight || autopilote_state = "Managed" >> modif inflight
                if ((autopilot_mode.equals("NAV")) || (autopilot_mode.equals(""))) {
                    ModifFpln.modifFpln(fpln, scanner, comManager);
                }
                else if (autopilot_mode.equals("HDG")) {
                    System.out.println("Heading Mode: route modification is not allowed !");
                }
                menuChoice = "";
                break;
                
            case "5": //Display route : active segment, sequenced segment, the two next segments
                routeDisplay(fpln, comManager, scanner);
                menuChoice = "";
                break;
                
            case "6":
                 quit = true;
                 System.out.println("You left the simulation menu. Restart simulation if needed.\n");
                 break;
                 
            default:
                System.out.println("Programmation error");
                break;
        }
    }
    
    public static void routeDisplay(Fpln fpln, CommunicationManager comManager, Scanner scanner) {
        ArrayList<ArrayList<String>> routeToDisplay = fpln.getRoute();
        int routeSize = fpln.getRouteSize();
        int activeSection = comManager.getActiveSection();
        if (routeSize == 0) {
            System.out.println("\nNo flight plan has been entered yet !");
        } else {
            System.out.println("\nROUTE TAB");
            System.out.println("-------------------------");
            if(!comManager.isFlying()){
                for (int i=0; i<routeSize; i++){
                    System.out.println(routeToDisplay.get(i).get(0)+" - "+routeToDisplay.get(i).get(1));
                }
            }
            else {
                if (activeSection != 0) {
                    System.out.println("  (SEQ)  -- " + routeToDisplay.get(activeSection - 1).get(0) + " - " + routeToDisplay.get(activeSection - 1).get(1));
                }
                System.out.println("  (ACT)  -- " + routeToDisplay.get(activeSection).get(0) + " - " + routeToDisplay.get(activeSection).get(1));
                if (activeSection == (routeSize - 2)) {
                    System.out.println(" (NoSEQ) -- " + routeToDisplay.get(activeSection + 1).get(0) + " - " + routeToDisplay.get(activeSection + 1).get(1));
                }
                else if (activeSection <= (routeSize - 3)) {
                    System.out.println(" (NoSEQ) -- " + routeToDisplay.get(activeSection + 1).get(0) + " - " + routeToDisplay.get(activeSection + 1).get(1));
                    System.out.println(" (NoSEQ) -- " + routeToDisplay.get(activeSection + 2).get(0) + " - " + routeToDisplay.get(activeSection + 2).get(1));
                }
            }
            System.out.println("-------------------------");
            System.out.print("Enter 0 to quit ROUTE TAB: ");
            menuChoice = scanner.next();
        }
    }

    public static String getMenuChoice() {
        return menuChoice;
    }
}
