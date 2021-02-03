/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HMI;

import Model.Fpln;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to manage insertion of a flight plan 
 * @author edouard.ladeira
 * @author jade.amah
 */
public class InputFpln {
    /**
     * Manages flight plan parameters entries
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public void inputFpln(Fpln fpln, Scanner scanner) throws SQLException{
        System.out.println("\nFPLN TAB");
        System.out.print("-------------------------");
        inputAirportDep(fpln, scanner);
        inputAirportArr(fpln, scanner);
        inputRoute(fpln, scanner);
        System.out.println("Fpln successfully filled !");
    }
    
    /**
     * Manages departure airport entry (and checks airport existence) 
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public static void inputAirportDep(Fpln fpln, Scanner scanner) throws SQLException{
        boolean exist = false;
        String fromAptId;
        
        while(exist==false){
            System.out.print("\nType departure APT and press Enter : ");
            fromAptId = scanner.next();
            if((exist=fpln.setAirportDep(fromAptId.toUpperCase()))==false){ //si exist est faux i.e si aptId n'est pas en NavDB
                System.out.println("INVALID ENTRY"); 
            }
        }
        String fromAptIdInput = fpln.getAirportDep().getIdentifier(); //vérification que la donnée est bien entrée
        System.out.println("==> FromAPT "+fromAptIdInput+" successfully added to FPLN !");
    }

    /**
     * Manages arrival airport entry (and checks airport existence)
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public static void inputAirportArr(Fpln fpln, Scanner scanner) throws SQLException{
        boolean exist = false;
        String toAptId;

        while(exist==false){
            System.out.print("\nType arrival APT and press Enter : ");
            toAptId = scanner.next();
            if((exist=fpln.setAirportArr(toAptId.toUpperCase()))==false){ //si exist est faux i.e si aptId n'est pas en NavDB
                System.out.println("INVALID ENTRY"); 
            }
        }
        String toAptIdInput = fpln.getAirportArr().getIdentifier(); //vérification que la donnée est bien entrée
        System.out.println("==> ToAPT "+toAptIdInput+" successfully added to FPLN !");
    }
    
    /**
     * Manages route entry (and checks airways/waypoints existence)
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public static void inputRoute(Fpln fpln, Scanner scanner) throws SQLException{
        ArrayList<Boolean> checkList= new ArrayList<>();
        boolean awyExist = false, prevWptInAwy = false, wptExist = false, wptInAwy = false;
        String choice = "", awyId, wptId, awyIdInput = "", wptIdInput = "";
        int cpt = 0; //counter to find out if this is the first time we pass through the input verification loop
        int routeSize = 0; 
        
        fpln.clearRoute();
        /**
         * Loop which manages entry of sections (one by one until route activation):
         *  - Type AWY WPT to enter a section (ex "UY156 PERIG")
         *  - Type DEL to delete the previous entry
         *  - Type ACTIVATE when you are done
         * 
         * The route have to begin by a section "DIRECT WPT" (such as "DIRECT FISTO")
         * The route have to finish by a section "DIRECT STAR"
         */
        while(choice.compareToIgnoreCase("ACTIVATE")!=0){
            //System.out.println(fpln.getRoute());
            System.out.println("\nType AWY WPT to enter a section\nType DEL to delete the previous entry\nType ACTIVATE when you are done");
            awyId = scanner.next().toUpperCase();
            switch (awyId) {
                case "ACTIVATE": //Pilot types "ACTIVATE" to end route entry and to activate it
                    if (awyIdInput.equals("DIRECT") && wptIdInput.equals("STAR")) {
                        choice = awyId;
                    } else {
                        System.out.println("==> IMPOSSIBLE ACTIVAION: You must finish by DIRECT-STAR section !");
                    }
                    break;
                case "DEL": //Pilot types "DEL" to delete his previous entry
                    if (fpln.getRouteSize() == 0) {
                        System.out.println("==> The route does not yet have a section !");
                    } else {
                        fpln.removeRouteSection(routeSize-1);
                        System.out.println("==> Section "+awyIdInput+"-"+wptIdInput+" deleted !");
                    }
                    break;
                default:
                    /**
                     * Loop which manages input section verification until all the following points are validated :
                     * - input airway must exist
                     * - input airway must contain the final waypoint of the previous input section
                     * - input waypoint must exist
                     * - input airway must contain the input waypoint
                     */
                    while(awyExist==false || prevWptInAwy == false || wptExist==false || wptInAwy==false){
                        //System.out.println("Type AWY WPT E to Enter a segment\nType AWY WPT ACTIVATE if last segment");
                        if (cpt != 0) {
                            System.out.println("\nType AWY WPT to enter a section\nType DEL to delete the previous entry\nType ACTIVATE when you are done");
                            awyId = scanner.next().toUpperCase();
                        }
                        switch (awyId) {
                            case "ACTIVATE": //Pilot types "ACTIVATE" to end route entry and to activate it
                                if (awyIdInput.equals("DIRECT") && wptIdInput.equals("STAR")) {
                                    choice = awyId;
                                    //Booleans reset to be able to check the next section entry
                                    awyExist = true;
                                    prevWptInAwy = true;
                                    wptExist = true;  
                                    wptInAwy = true;
                                } else {
                                    System.out.println("==> IMPOSSIBLE ACTIVATION: The route must finish by a section DIRECT-STAR !");
                                }
                                break;
                            case "DEL": //Pilot types "DEL" to delete his previous entry
                                if (fpln.getRouteSize() == 0) {
                                    System.out.println("==> You have not entered a section yet !");
                                } else {
                                    fpln.removeRouteSection(routeSize-1);
                                    System.out.println("==> Section "+awyIdInput+"-"+wptIdInput+" deleted !");
                                    //Booleans reset to be able to check the next section entry
                                    awyExist = true;
                                    prevWptInAwy = true;
                                    wptExist = true;  
                                    wptInAwy = true;
                                }
                                break;
                            default: //Pilot entries a section (AWY WPT) and elements are verified
                                wptId = scanner.next().toUpperCase();
                                if (fpln.getRouteSize() == 0 && !awyId.equals("DIRECT")) {
                                    System.out.println("==> INCORRECT ENTRY: The route must begin by a section DIRECT-WPT !");
                                }
                                else {
                                    checkList.addAll(fpln.addSection(awyId, wptId)); //Result of the attempt to add the section to the route
                                    //System.out.println(checkList); //pre-beta verification
                                    
                                    if((awyExist = checkList.get(0)) == false){ //if input airway does not exist (or does not have the good format)
                                        System.out.println("==> INVALID ENTRY AWY");
                                    }
                                    if((prevWptInAwy = checkList.get(1)) == false){ //if input airway does not contain the final waypoint of the previous input section
                                        System.out.println("==> INVALID ENTRY PREVIOUS WPT IN AWY");
                                    }
                                    if((wptExist = checkList.get(2))==false){ //if input waypoint does not exist (or does not have the good format)
                                        System.out.println("==> INVALID ENTRY WPT");
                                    }
                                    if((wptInAwy = checkList.get(3))==false){ //if input airway does not contain the input waypoint
                                        System.out.println("==> INVALID ENTRY WPT IN AWY");
                                    }
                                    checkList.clear();
                                }
                                cpt++;
                                break;
                        }
                    }
                    //Booleans reset to be able to check the next section entry
                    awyExist = false;
                    prevWptInAwy = false;
                    wptExist = false;  
                    wptInAwy = false;
                    cpt = 0;
                    
                    //Print of confirmation message when a section is well added to the route
                    if (!awyId.equals("DEL") && !awyId.equals("ACTIVATE")) {
                        routeSize = fpln.getRouteSize();
                        awyIdInput = fpln.getRoute().get(routeSize-1).get(0); //get the airway idenifier of the input section 
                        wptIdInput = fpln.getRoute().get(routeSize-1).get(1); //get the waypoint idenifier of the input section 
                        System.out.println("==> Section " +awyIdInput+"-"+wptIdInput+" successfully added to route !");
                    }
                    break;
            }       
        }
        System.out.println("\nRoute successfully added to FPLN !"); //Print of confirmation message when the route is added to the flight plan
    }
}
