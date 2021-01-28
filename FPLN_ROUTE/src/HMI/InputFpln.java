/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HMI;

import Model.Fpln;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        System.out.println("-------------------------");
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
    public void inputAirportDep(Fpln fpln, Scanner scanner) throws SQLException{
        boolean exist = false;
        String fromAptId;
        
        while(exist==false){
            System.out.print("Type FromAPT and press Enter : ");
            fromAptId = scanner.next();
            if((exist=fpln.setAirportDep(fromAptId.toUpperCase()))==false){ //si exist est faux i.e si aptId n'est pas en NavDB
                System.out.println("INVALID ENTRY\n"); 
            }
        }
        String fromAptIdInput = fpln.getAirportDep().getIdentifier(); //vérification que la donnée est bien entrée
        System.out.println("==> FromAPT "+fromAptIdInput+" successfully added to FPLN\n");
    }

    /**
     * Entrée valide de l'aéroport d'arrivée
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public void inputAirportArr(Fpln fpln, Scanner scanner) throws SQLException{
        boolean exist = false;
        String toAptId;

        while(exist==false){
            System.out.print("Type ToAPT and press Enter : ");
            toAptId = scanner.next();
            System.out.println("          -----");
            if((exist=fpln.setAirportArr(toAptId.toUpperCase()))==false){ //si exist est faux i.e si aptId n'est pas en NavDB
                System.out.println("INVALID ENTRY\n"); 
            }
        }
        String toAptIdInput = fpln.getAirportArr().getIdentifier(); //vérification que la donnée est bien entrée
        System.out.println("==> ToAPT "+toAptIdInput+" successfully added to FPLN\n");
    }
    
    /**
     * Entrée valide de la route, vérifiée segment par segment
     * @param fpln
     * @param scanner
     * @throws SQLException 
     */
    public void inputRoute(Fpln fpln, Scanner scanner) throws SQLException{
        ArrayList<Boolean> checkList= new ArrayList<>();
        boolean awyExist = false;
        boolean prevWptInAwy = false;
        boolean wptExist = false;  
        boolean wptInAwy = false;
        
        String choice = "";
 
        String awyId;
        String wptId;
        
        int routeSize;
        
        //boucle d'entrée des segments de type AWY WPT un par un jusqu'à activer la route via le choice
        while(choice.compareToIgnoreCase("ACTIVATE")!=0){
            while(awyExist==false || prevWptInAwy == false || wptExist==false || wptInAwy==false){
                System.out.println("Type AWY WPT E to Enter a segment\nType AWY WPT ACTIVATE if last segment");
                //System.out.println("\nEnter a waypoint\nType DEL to delete the previous entry\nType END when you are done");
                awyId = scanner.next();
                wptId = scanner.next();
                
                checkList.addAll(fpln.addSegment(awyId.toUpperCase(), wptId.toUpperCase())); //on copie la liste de résultats 
                //System.out.println(checkList); //vérification pre-beta
                if((awyExist = checkList.get(0)) == false){ //si awyId n'existe pas
                    System.out.println("INVALID ENTRY AWY\n");
                }
                if((prevWptInAwy = checkList.get(1)) == false){ //si wpt précédent n'est pas dans awy
                    System.out.println("INVALID ENTRY PREVIOUS WPT IN AWY\n");
                }
                if((wptExist = checkList.get(2))==false){ //si wptId n'existe pas
                    System.out.println("INVALID ENTRY WPT\n");
                }
                if((wptInAwy = checkList.get(3))==false){ //si wpt n'est pas dans awy
                    System.out.println("INVALID ENTRY WPT IN AWY\n");
                }
                choice = scanner.next(); //soit on entre un autre couple avec E, soit on valide la route avec ACTIVATE
                checkList.clear();
            }//fin tant que couple awy wpt non valide
            // remise à zéro des booléens
            awyExist = false; //on repasse à faux pour tester le couple suivant
            wptExist = false;  
            wptInAwy = false;
            
            routeSize = fpln.getRouteSize();
            String awyIdInput = fpln.getRoute().get(routeSize-1).get(0); //récupération de l'awyId entré dans le fpln
            String wptIdInput = fpln.getRoute().get(routeSize-1).get(1); //récupération du wptId entré dans le fpln
            System.out.println("==> Segment "+awyIdInput+"-"+wptIdInput+" successfully added to route !\n");
        }//fin tant que route non activée
        System.out.println("Route successfully added to FPLN !\n"); //route activée
    }//fin de inputRoute
}//fin de la classe InputFpln
