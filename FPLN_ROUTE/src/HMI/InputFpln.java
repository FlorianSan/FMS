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
 * @author jade.amah
 */
public class InputFpln {
    /**
     * Gestion des input de toutes les caractéristiques du plan de vol
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
     * Entrée valide de l'aéroport de départ
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
     * Entrée valide de l'aéroport d'arrivée
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
     * Entrée valide de la route, vérifiée segment par segment
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public static void inputRoute(Fpln fpln, Scanner scanner) throws SQLException{
        ArrayList<Boolean> checkList= new ArrayList<>();
        boolean awyExist = false, prevWptInAwy = false, wptExist = false, wptInAwy = false;
        String choice = "", awyId, wptId, awyIdInput = "", wptIdInput = "";
        int cpt = 0, routeSize = 0; 
        
        fpln.clearRoute();
        //boucle d'entrée des segments de type AWY WPT un par un jusqu'à activer la route via le choice
        while(choice.compareToIgnoreCase("ACTIVATE")!=0){
            //System.out.println(fpln.getRoute());
            System.out.println("\nType AWY WPT to enter a section\nType DEL to delete the previous entry\nType ACTIVATE when you are done");
            awyId = scanner.next().toUpperCase();
            switch (awyId) {
                case "ACTIVATE":
                    if (awyIdInput.equals("DIRECT") && wptIdInput.equals("STAR")) {
                        choice = awyId;
                    } else {
                        System.out.println("==> IMPOSSIBLE ACTIVAION: You must finish by DIRECT-STAR section !");
                    }
                    break;
                case "DEL":
                    if (fpln.getRouteSize() == 0) {
                        System.out.println("==> The route does not yet have a section !");
                    } else {
                        fpln.removeRouteSection(routeSize-1);
                        System.out.println("==> Section "+awyIdInput+"-"+wptIdInput+" deleted !");
                    }
                    break;
                default:
                    while(awyExist==false || prevWptInAwy == false || wptExist==false || wptInAwy==false){
                        //System.out.println("Type AWY WPT E to Enter a segment\nType AWY WPT ACTIVATE if last segment");
                        if (cpt != 0) {
                            System.out.println("\nType AWY WPT to enter a section\nType DEL to delete the previous entry\nType ACTIVATE when you are done");
                            awyId = scanner.next().toUpperCase();
                        }
                        switch (awyId) {
                            case "ACTIVATE":
                                if (awyIdInput.equals("DIRECT") && wptIdInput.equals("STAR")) {
                                    choice = awyId;
                                    awyExist = true; //on repasse à faux pour tester le couple suivant
                                    prevWptInAwy = true;
                                    wptExist = true;  
                                    wptInAwy = true;
                                } else {
                                    System.out.println("==> IMPOSSIBLE ACTIVAION: The route must finish by a section DIRECT-STAR !");
                                }
                                break;
                            case "DEL":
                                if (fpln.getRouteSize() == 0) {
                                    System.out.println("==> You have not entered a section yet !");
                                } else {
                                    fpln.removeRouteSection(routeSize-1);
                                    System.out.println("==> Section "+awyIdInput+"-"+wptIdInput+" deleted !");
                                    awyExist = true; //on repasse à faux pour tester le couple suivant
                                    prevWptInAwy = true;
                                    wptExist = true;  
                                    wptInAwy = true;
                                }
                                break;
                            default:
                                wptId = scanner.next().toUpperCase();
                                if (fpln.getRouteSize() == 0 && !awyId.equals("DIRECT")) {
                                    System.out.println("==> INCORRECT ENTRY: The route must begin by a section DIRECT-WPT !");
                                }
                                else {
                                    checkList.addAll(fpln.addSection(awyId, wptId)); //on copie la liste de résultats 
                                    //System.out.println(checkList); //vérification pre-beta
                                    if((awyExist = checkList.get(0)) == false){ //si awyId n'existe pas
                                        System.out.println("==> INVALID ENTRY AWY");
                                    }
                                    if((prevWptInAwy = checkList.get(1)) == false){ //si wpt précédent n'est pas dans awy
                                        System.out.println("==> INVALID ENTRY PREVIOUS WPT IN AWY");
                                    }
                                    if((wptExist = checkList.get(2))==false){ //si wptId n'existe pas
                                        System.out.println("==> INVALID ENTRY WPT");
                                    }
                                    if((wptInAwy = checkList.get(3))==false){ //si wpt n'est pas dans awy
                                        System.out.println("==> INVALID ENTRY WPT IN AWY");
                                    }
                                    checkList.clear();
                                }
                                cpt++;
                                break;
                        }
                    }//fin tant que couple awy wpt non valide
                    // remise à zéro des booléens
                    awyExist = false; //on repasse à faux pour tester le couple suivant
                    prevWptInAwy = false;
                    wptExist = false;  
                    wptInAwy = false;
                    cpt = 0;

                    if (!awyId.equals("DEL") && !awyId.equals("ACTIVATE")) {
                        routeSize = fpln.getRouteSize();
                        awyIdInput = fpln.getRoute().get(routeSize-1).get(0); //récupération de l'awyId entré dans le fpln
                        wptIdInput = fpln.getRoute().get(routeSize-1).get(1); //récupération du wptId entré dans le fpln
                        System.out.println("==> Section " +awyIdInput+"-"+wptIdInput+" successfully added to route !");
                    }
                    break;
            }       
        }//fin tant que route non activée
        System.out.println("\nRoute successfully added to FPLN !"); //route activée
    }//fin de inputRoute
}//fin de la classe InputFpln
