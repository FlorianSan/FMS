package Communication;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import java.util.ArrayList;
import Model.Fpln;
import Model.Airport;
import HMI.Menu;
import HMI.ModifFpln;
import Main.MainFplnRoute;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to manage communication with other groups
 * @author edouard.ladeira
 * @author jade.amah
 */
public class CommunicationManager 
{
    private static Ivy bus; // bus to allow communication with other teams
    private static double currentTime; // helps building the timestamp of the messages we send
    private String currentFplnString; //Current route with string message format
    private String lastModifMsg; // Last modification 
    private boolean flying = false; //allow to know if we are inflight or no
    private int activeSection = 0; //airway active
    private String aptSim = ""; //simulation airport identifier
    private String AP_Mode = "";
    private static boolean modifMsgReceived = false;

    /**
     * constructor
     * @param domain
     * @throws fr.dgac.ivy.IvyException
     */
    public CommunicationManager(String domain) throws IvyException 
    {
        // initialize (set up the bus, name and ready message)
        bus = new Ivy("FPLN_ROUTE_APP", "FPLN_ROUTE Ready", null);
        
        currentTime = 0;
        
        // update current time value for message timestamping
        bus.bindMsg("^Time t=(.*)", new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                currentTime = new Double(strings[0]);
                //System.out.println(currentTime); //à commenter une fois test effectué le 19/11/2020
            }
        });
        
        // Send flight plan another time if not received by LEGS
        bus.bindMsg("^FL_ERROR Time=(.*) Error Receiving Flight Plan", new IvyMessageListener() 
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                System.out.println("FL_Error Receiving Flight Plan");
                sendInitFplnAgain();
                updateDisplay();
            }
        });
        
        //Send modified route another time if not received by LEGS
        bus.bindMsg("^FL_ERROR Time=(.*) Error Receiving Modified Route", new IvyMessageListener() 
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                System.out.println("FL_Error Receiving Modified Route");
                sendModifAgain();
                updateDisplay();
            }
        });
        
        // Listen to FPLN-LEGS to get the sequence number of the active airway
        // to show on which segment on the route is the A/C
        bus.bindMsg("FL_AA Time=(.*) NumSeqActiveAwy=(.*)", new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                activeSection = Integer.valueOf(strings[1]);
                updateRouteDisplay();
            }
        });
        
        //Listen to GUID-COMM to get the auto pilot state 
        //Display needs to match selected/managed mode
        bus.bindMsg("GC_AP Time=(.*) AP_State='(.*)' AP_Mode='(.*)'", new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                AP_Mode = strings[2];
                if (!AP_Mode.equals(strings[2])) {
                    AP_Mode = strings[2];
                    System.out.println("\n"+strings[2]+" mode\n");
                    updateDisplay();
                }
            }
        });
        
        //Listen to GUID-TRAJ to known when the flight start 
        //Allow to manage authorized modification 
        bus.bindMsg("^GT Flight_started", new IvyMessageListener() 
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                if (!flying) {
                    flying = true;
                    System.out.println("\nThe flight start");
                    updateDisplay();
                }
            }
        });
               
        // Listen to SIM-PARAM to get the simulation airport
        bus.bindMsg("^SP_AptId Identifier=(.*)", new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                aptSim = strings[0];
            }
        });
        
        // Listen to FPLN-LEGS ready message for modification
        bus.bindMsg("FL_ModifReady Time=(.*) Modified Flight Plan Ready", new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                Thread thread1 = new Thread(new WaintingRunnable(10)); //10 seconds = waiting time
                thread1.start();
            }
        });
        
        // Listen to FPLN-LEGS to get the route modification
        //FL_Modif Time=0  numStart=1 numEnd=3 ModifiedSection=AWY1-WPT1, AWY2-WPT2, AWY3-WPT3
        bus.bindMsg("FL_Modif Time=(.*)  numStart=(.*) numEnd=(.*) ModifiedSection=(.*)", new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                modifMsgReceived = true;
                int numStart = Integer.valueOf(strings[1]);
                int numEnd = Integer.valueOf(strings[2]);
                String modifiedRoute = strings[3];
                
                updateRoute(numStart, numEnd, modifiedRoute);
                System.out.println("\nRoute modification received from LEGS");
                updateDisplay();
            }
        });
        
        // start the bus on specified domain 
        bus.start(domain); 

        /* Stop the bus when main application asks */
        bus.bindMsg("^Stop",new IvyMessageListener()
        {
            @Override
            public void receive(IvyClient client, String strings[])
            {
                bus.stop();
            }
        });
        
    }

    
    // Communication Manager methods
    
    /**
     * Send a message regarding the ROUTE via Ivy bus
     * @param header, object of the message
     * @param message
     */
    private static void sendFR(String header, String message) 
    {
        try {
            bus.sendMsg("FR_" + header + " Time=" + currentTime + " " + message);
            System.out.println("\nMessage sent : FR_" + header + " Time=" + currentTime + " " + message);
        } catch (IvyException e) {
            System.err.println("\nImpossible to send the following message : '" + message + "'.");
        }
    }
    
    //
    //FLIGHT PLAN INITIALIZATION
    // 
    
    /**
     * Send a message on the bus once the flight plan is ready i.e READY button is clicked on the HMI.
     * "FR_Ready Time=currentTime Initial Flight Plan Ready"
     */
    public void sendReady()
    {
        sendFR("Ready", "Initial Flight Plan Ready");
    }

    /**
     * Send the flight plan on the bus once INIT button is clicked on the HMI
     * Message "FR_InitFlightPlan Time=currentTime FROM=identifiant TO=identifiant ROUTE=routeidentifiant-fixidentifiant, routeidentifiant-fixidentifiant, routeidentifiant-fixidentifiant
     * FROM = departure airport (identifiant in the Airport table of the NavDB)
     * TO = arrival airport (identifiant in the Airport table of the NavDB)
     * ROUTE (P1) = airway-waypoint (routeidentifiant of an airway in Route table of the NavDB, fixidentifiant of a waypoint in Route table of the NavDB (== to identifiant of Waypoint table))
     * @param fpln flight plan object
     */
    public void sendInitFpln(Fpln fpln)
    {
        String fplnString = createMsg(fpln);
        this.currentFplnString = fplnString;
        sendFR("InitFlightPlan", fplnString); 
        //"FROM=LFMN TO=LFRG ROUTE=DIRECT-OKTET, UM733-BULOL, UZ12-ATN, UM976-OKRIX, UH10-AGOGO, DIRECT-STAR"
        //"FROM=LFBO TO=LFPO ROUTE=DIRECT-FISTO, UY156-PERIG, UT210-TUDRA, UT158-AMB, DIRECT-STAR"
    }
    
    /**
     * Send the flight plan again by using the currentFplnString.
     */
    public void sendInitFplnAgain()
    {
        sendReady();
        sendFR("InitFlightPlan",this.currentFplnString);
    }

    /**
     * Send a error message when simulation airport is unknown.
     */
    public void sendErrorAptSim()
    {
        sendFR("ERROR", "Error Receiving Simulation Airport Identifier");
    }
    
    //
    //FLIGHT PLAN MODIFICATION
    //
    
    /**
     * Send message to informe LEGS that we do not received their modification.
     */
    public static void sendErrorModifReception()
    {
        sendFR("ERROR","Error Receiving Modified Route");
    }
    
    /**
     * Send a message on the bus once the modif is ready i.e READY button is clicked on the HMI.
     * Message "FR_ModifReady Time=currentTime Modified Flight Plan Ready"
     */
    public void sendModifReady(){
        sendFR("ModifReady", "Modified Flight Plan Ready");
    }
    
    /**
     * Send the modif on the bus once INIT button is clicked on the HMI
     * @param modif 
     * if change: ["CHG", "num of replaced segment", "new portion with format AWY-WPT"]
     *      Message "FR_CHG Time=currentTime numModifiedSegment=num newRoutePortion=AWY1-WPT1, AWY2-WPT2..."
     * if insertion: ["INS", "num of replaced segment", "new portion with format AWY-WPT"]
            Message "FR_INS Time=currentTime numModifiedSegment=num newRoutePortion=AWY1-WPT1, AWY2-WPT2..."
     */
    public void sendModifFpln(ArrayList<String> modif){      
        this.lastModifMsg = createMsg(modif); //on enregistre la dernière modification pour pouvoir la renvoyer s'il faut
        sendFR("Modif", this.lastModifMsg);
        ModifFpln.setModifReady(false);
    }
    
    /**
     * Send the modif again by using the lastModifMsg.
     */
    public void sendModifAgain()
    {
        sendModifReady();
        sendFR("Modif", this.lastModifMsg);
    }
    
    //
    // MESSAGE FORMATING
    //

    /**
     * Create the string message which will contain the flight plan
     * @param fpln flight plan object
     * @return String message 
     */
    public String createMsg(Fpln fpln) {
        String fplnString;
        Airport airportDep = fpln.getAirportDep();
        Airport airportArr = fpln.getAirportArr();
        ArrayList<ArrayList<String>> route = fpln.getRoute();
        int routeSize = fpln.getRouteSize();
        
        //Addition of departure and arrival airports
        fplnString = "FROM=" + airportDep.getIdentifier() + " TO=" + airportArr.getIdentifier() + " ROUTE=";
        
        //Adition of the route
        for(int i = 0; i < routeSize - 1; i++) {
            fplnString += (route.get(i)).get(0) + "-" + (route.get(i)).get(1) + ", ";
        }
        fplnString += (route.get(routeSize - 1)).get(0) + "-" + (route.get(routeSize - 1)).get(1);
        return fplnString; 
    }
    
    /**
     * Create the string message which will contain the new route portion of the modified flight plan
     * @param modif
     * @return String message "numModifiedSegment=num newRoutePortion=AWY1-WPT1, AWY2-WPT2..."
     */
    public String createMsg(ArrayList<String> modif) {
        String modifString;        

        modifString = "numStart="+modif.get(0)+" numEnd="+modif.get(1)+" ModifiedSection="+modif.get(2);
        return modifString; 
    }
    
    //
    // DISPLAY UPDATE (after active airway reception)
    //

    /**
     * Method to update information display
     * @param text
     */
    
    public void updateDisplay() {
        switch (Menu.getMenuChoice()) {
            case "":
                Menu.printMenu();
                System.out.print("Enter the number of this section: ");
                break;
            case "5":
                updateRouteDisplay();
                break;
            default:
                System.out.println("Programmation error");
                break;
        } 
    }
    
    /**
     * Method to update route display.
     */
    public void updateRouteDisplay() {
        ArrayList<ArrayList<String>> routeToDisplay = MainFplnRoute.activeFpln.getRoute();
        int routeSize = MainFplnRoute.activeFpln.getRouteSize();

        System.out.println("\n\nROUTE TAB");
        System.out.println("-------------------------");
        if (!flying) {
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
    }
 
    
    //
    // AIRCRAFT STATE RELATED METHODS
    //

    /**
     * Get flying state
     * @return flying state
     */
    public boolean isFlying() {
        return flying;
    }
    
    /**
     * Get airway active number
     * @return airway active number
     */
    public int getActiveSection() {
    	return activeSection;
    }
    
    /**
     * Get simulation airport identifier
     * @return simulation airport identifier
     */
    public String getAptSim() {
    	return aptSim;
    }

    /**
     * Get the autopilote state
     * @return Autopilote state
     */
    public String getAP_Mode() {
        return AP_Mode;
    }

    /**
     * Get the state of modification message reception 
     * @return modifMsgReceived
     */
    public static boolean isModifMsgReceived() {
        return modifMsgReceived;
    }

    /**
     * Set the state of modification message reception 
     * @param state
     */
    public static void setModifMsgReceived(boolean state) {
        CommunicationManager.modifMsgReceived = state;
    }
    
    

    //
    // UPDATE route (after modif reception from LEG)
    //

    /**
     * Method to update the flight plan after the reception of modification by LEGS
     * @param indexStart
     * @param indexEnd
     * @param modifRoute
     */
    
    public void updateRoute(int indexStart, int indexEnd, String modifRoute) {
        String modifiedRouteList[] = modifRoute.split(", ");
        int sizeModif = modifiedRouteList.length;
        MainFplnRoute.activeFpln.removeRouteSection(indexStart, indexEnd);
        int nbTemp = indexStart;

        for (int i=0; i<sizeModif; i++) {
            String sTemp[] = modifiedRouteList[i].split("-");
            ArrayList<String> sect = new ArrayList<>();
            sect.add(sTemp[0]);
            sect.add(sTemp[1]);
            MainFplnRoute.activeFpln.insertSectionInRoute(nbTemp, sect);
            nbTemp++;
        }
    }
    

    //
    // IVY RELATED METHODS
    //
    
    /**
     * Stop the communication through the bus.
     */
    public void stop() {
        bus.stop();
    }
    
    
    //
    // Sub-Class to manage the waiting (of modification message) after modification ready message from LEGS
    // It is call as Runnable target to create a thread which deals only the waiting periode.
    //
    
    private static class WaintingRunnable implements Runnable {
        private int waitingPeriod;

        public WaintingRunnable(int delai) {
          this.waitingPeriod = delai;
        }

        @Override
        public void run() {
            int clock = 0;
            //Wait during 10sec the LEGS modification message. After 10sec error message is sent
            while ((!CommunicationManager.isModifMsgReceived()) && (clock<waitingPeriod)){
                System.out.println(clock);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                clock++;    
            }
            if (!CommunicationManager.isModifMsgReceived()) {
                CommunicationManager.sendErrorModifReception();
            } else {
                CommunicationManager.setModifMsgReceived(false);
            }
        }
    }
}



