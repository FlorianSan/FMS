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
     * @param scanner
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
//                InputFpln inputFpln = new InputFpln();
//                inputFpln.inputFpln(fpln, scanner);
                
                //Automatic flight plan input to simply integration tests
                fpln.setAirportDep("LFBO");
                fpln.setAirportArr("LFPO");
                fpln.addSection("DIRECT", "FISTO");
                fpln.addSection("UY156", "PERIG");
                //fpln.addSegment("DIRECT", "NORON");
                //fpln.addSegment("DIRECT", "FOUCO");
                fpln.addSection("UT210", "TUDRA");
                //fpln.addSegment("DIRECT", "TUDRA");
                fpln.addSection("UT158", "AMB");
                fpln.addSection("DIRECT", "STAR");
                System.out.println("Fpln successfully filled !");
                menuChoice = "";
                break;
                
            case "2": //Sends ready message to LEGS
                if (ModifFpln.isModifReady()){ //ready message for modification
                    comManager.sendModifReady();
                }
                else { //ready message for flight plan initialization
                    comManager.sendReady();
                }
                menuChoice = "";
                break;
                
            case "3": //sends messages to LEGS
                if (ModifFpln.isModifReady()){ //Send flight plan modification to LEGS
                    comManager.sendModifFpln(ModifFpln.getModif()); 
                }
                else{ //Sends init flight plan to LEGS
                    String aptSimu = comManager.getAptSim();             
                    while ("".equals(aptSimu)) {
                        try {
                            comManager.sendErrorAptSim();
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
                if ((autopilot_mode.equals("NAV")) || (autopilot_mode.equals(""))) {
                //autopilot_mode = "" >> preflight modif|| autopilot_mode = "Managed" >> inflight modif
                    ModifFpln.modifFpln(fpln, scanner, comManager);
                }
                else if (autopilot_mode.equals("HDG")) { //modification are not allowed in heading mode 
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
    
    /**
     * Displays the route : the previous section (N-1), the active section (N) and the two upcoming sections (N+1, N+2)
     * @param fpln
     * @param comManager
     * @param scanner
     */
    public static void routeDisplay(Fpln fpln, CommunicationManager comManager, Scanner scanner) {
        ArrayList<ArrayList<String>> routeToDisplay = fpln.getRoute();
        int routeSize = fpln.getRouteSize();
        int activeSection = comManager.getActiveSection();
        if (routeSize == 0) { //if no flight plan has been entered
            System.out.println("\nNo flight plan has been entered yet !");
        } else { //if a flight plan has been entered
            System.out.println("\nROUTE TAB");
            System.out.println("-------------------------");
            if(!comManager.isFlying()){ //if the flight does not yet started
                for (int i=0; i<routeSize; i++){
                    System.out.println(routeToDisplay.get(i).get(0)+" - "+routeToDisplay.get(i).get(1));
                }
            }
            else { //if the flight started
                if (activeSection != 0) { //if the active section is not the first section of the route
                    System.out.println("  (SEQ)  -- " + routeToDisplay.get(activeSection - 1).get(0) + " - " + routeToDisplay.get(activeSection - 1).get(1));
                }
                System.out.println("  (ACT)  -- " + routeToDisplay.get(activeSection).get(0) + " - " + routeToDisplay.get(activeSection).get(1));
                if (activeSection == (routeSize - 2)) { //if the active section is second last section of the route
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

    /**
     * Get the current pilot choice 
     * @return menuChoice
     */
    public static String getMenuChoice() {
        return menuChoice;
    }
}
