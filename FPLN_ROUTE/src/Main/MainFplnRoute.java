/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Communication.CommunicationManager;
import HMI.Menu;
import Model.Fpln;
import fr.dgac.ivy.IvyException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class
 * @author edouard.ladeira
 * @author jade.amah
 */
public class MainFplnRoute {

    public static CommunicationManager communicationManager;
    public static Fpln activeFpln;
    public static Scanner scanner;
   
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws SQLException, InterruptedException {
        
        System.err.close();
        String domain = "127.255.255.255:2010"; //default domain 127.255.255.255:2010 192.168.43.255:2010
        //Test ActiveAwy FL_AA Time=0.0 NumSeqActiveAwy=0
        
        // handle options in command line 
        int index;
        for (index = 0; index < args.length; index++) { 
            String opt = args[index];
            switch (opt) {
            case "-b":
                domain = args[index+1]; //on prend l'argument qui suit l'option -b, soit ici le domaine
                System.out.println(domain);
            break;
            default:
               if (!opt.isEmpty() && opt.charAt(0) == '-') {
                   System.err.println("Unknown option: '" + opt + "'");
               }
            }
        }
        
      
        //création du gestionnaire de communication Ivy
        try {
            communicationManager = new CommunicationManager(domain);
        } catch (IvyException ex) {
            Logger.getLogger(MainFplnRoute.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Impossible to connect to the domain "+domain);
        }
        
        //création de l'objet plan de vol
        activeFpln = new Fpln();
        
        //création de l'objet scanner
        scanner = new Scanner(System.in);
        
        //Lancement du menu
        Menu.manage(activeFpln, communicationManager, scanner);    
    }  
}
