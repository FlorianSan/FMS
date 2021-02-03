package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

/**
 * Class to create and manage the flight plan
 * @author edouard.ladeira
 */
public class Fpln {

    /**
     * Departure and arrival airport objects
     */
    private Airport airportDep = new Airport();
    private Airport airportArr = new Airport();
    /**
     * Route : List of sections (airway identifier and waypoint identifier couple)
     * airways and waypoints are not java object, only defined by a string (identifier)
     */
    private ArrayList<ArrayList<String>> route = new ArrayList<ArrayList<String>>();
    /** 
     * Number of segments in the route
     */
    private int routeSize = 0;
    

    /**
     * Add couple to list of airway identifier and waypoint identifier couples
     * 
     * @param awyId airway identifier
     * @param wptId waypoint identifier
     * @return list of boolean checkList: 
     *   -1st element : Airway existence
     *   -2nd element : Previous Waypoint existence in current Airway
     *   -3rd element : Waypoint existence 
     *   -4th element : Waypoint existence in Airway
     */
    public ArrayList<Boolean> addSection(String awyId, String wptId) {
        /**
         * checkList initialisation
         */
        ArrayList<Boolean> checkList = new ArrayList<>(Arrays.asList(new Boolean[4]));
        Collections.fill(checkList, Boolean.FALSE);
        boolean awyExist = false;
        boolean prevWptInAwy = false;
        boolean wptExist = false;
        boolean wptInAwy = false;
        
        /* Empty segment creation */
        ArrayList<String> segment = new ArrayList<>();

        /** 
         * Verifications in NDB:
         * Airway-Waypoint existence verification
         * Current and previous Waypoint affiliations in Airway verification
         * 
         * Update of checkList status
         */
        if (awyId.equals("DIRECT")) {
            //Special case when user enters the first/last route segment
            awyExist = true;
            checkList.set(0, awyExist);
            prevWptInAwy = true;
            checkList.set(1, prevWptInAwy);
            if (wptId.equals("STAR")) {
                //User has entered the last route segment 
                wptExist = true;
                checkList.set(2, wptExist);
                wptInAwy = true;
                checkList.set(3, wptInAwy);
                
                // couple filling
                segment.add(awyId);
                segment.add(wptId);

                // Adding the couple to the sequenceList
                this.route.add(segment);
                this.routeSize += 1;
            }
            else {
                //User has entered the first route segment
                wptExist = Ndb.checkExist(wptId, "waypoint", "identifiant");
                checkList.set(2, wptExist);
                if (wptExist) {
                    wptInAwy = true;
                    checkList.set(3, wptInAwy);
                    
                    // couple filling
                    segment.add(awyId);
                    segment.add(wptId);

                    // Adding the couple to the sequenceList
                    this.route.add(segment);
                    this.routeSize += 1;
                } 
            }         
        }
        else {
            //User has entered a classical route segment
            
            //Waypoint recovery of the previous route segment
            ArrayList<String> prevSegment = this.route.get(this.routeSize - 1);
            String prevWptId = prevSegment.get(1);
            
            //Airway existence verification
            awyExist = Ndb.checkExist(awyId, "route", "routeidentifiant");
            checkList.set(0, awyExist);
            wptExist = Ndb.checkExist(wptId, "route", "fixidentifiant");
            checkList.set(2, wptExist);
            if (awyExist) {
                //Verification of previous Waypoint affiliations in Airway
                prevWptInAwy = Ndb.checkWptInAwy(awyId, prevWptId);
                checkList.set(1, prevWptInAwy);
            }
            if (awyExist && wptExist) {
                //Verification of Waypoint affiliations in Airway
                wptInAwy = Ndb.checkWptInAwy(awyId, prevWptId);
                checkList.set(3, wptInAwy);
            }
            if (awyExist && prevWptInAwy && wptExist && wptInAwy) {  
                // couple filling
                segment.add(awyId);
                segment.add(wptId);

                // Adding the couple to the sequenceList
                this.route.add(segment);
                this.routeSize += 1;
            }
        }
        return checkList;
    }
    
        
    /**
     * Get departure Airport  
     * @return departure Airport
     */
    public Airport getAirportDep() {
    	return this.airportDep;
    }
    
    /**
     * Get arrival Airport  
     * @return departure Airport
     */
    public Airport getAirportArr() {
    	return this.airportArr;
    }
    
    /**
     * Get route  
     * @return route
     */
    public ArrayList<ArrayList<String>> getRoute() {
    	return this.route;
    }
    
    /**
     * Get number of segments in the route  
     * @return routeSize
     */
    public int getRouteSize() {
    	return this.routeSize;
    }
    
    /**
     * Set departure airport identifier
     * 
     * @param identifier Airport identifier
     * @return boolean
     */
    public boolean setAirportDep(String identifier) {
        //Airport existence verification in NDB
        boolean exist = Ndb.checkExist(identifier, "aeroport", "identifiant");
        if (exist) {
            this.airportDep.setIdentifier(identifier);
        }
        return exist;
    }

    /**
     * Set arrival airport identifier
     * 
     * @param identifier Airport identifier
     * @return boolean
     */
    public boolean setAirportArr(String identifier) {
        // Airport existence verification in NDB
        boolean exist = Ndb.checkExist(identifier, "aeroport", "identifiant");
        if (exist) {
            this.airportArr.setIdentifier(identifier);
        }
        return exist;
    }
    
    /**
     * Set route
     * @param newRoute
     */
    public void setRoute(ArrayList<ArrayList<String>> newRoute) {
        this.routeSize = newRoute.size();
        this.route.clear();
        this.route.addAll(newRoute);
    }
       
    /**
     * Copy a flight plan in another - generally active flight plan in temporary flight plan
     * @param fpln 
     */
    public void copyFpln(Fpln fpln) {
        this.setAirportDep(fpln.getAirportDep().getIdentifier());
        this.setAirportArr(fpln.getAirportArr().getIdentifier());
        this.setRoute(fpln.getRoute());
    }
    
    /**
     * Insert a section in the route just after the section number i
     * @param i
     * @param section
     */
    public void insertSectionInRoute(int i, ArrayList<String> section) {
        this.route.add(i, section);
        this.routeSize += 1;
    }
    
    /**
     * Change the section number i in the route
     * @param i
     * @param section
     */
    public void changeSectionInRoute(int i, ArrayList<String> section) {
        this.route.set(i, section);
    }
    
    /**
     * Update the route by deleting section number i
     * @param i
     */
    public void removeRouteSection(int i) {
        this.route.remove(i);
        this.routeSize -= 1;
    }
    
    /**
     * Update the route by deleting sections between indexStart (included) and indexEnd(included)
     * @param iStart
     * @param iEnd
     */
    public void removeRouteSection(int iStart, int iEnd) {
        for (int i=0; i<=(iEnd-iStart); i++) {
          this.route.remove(iStart);
        }
        this.routeSize -= (iEnd - iStart + 1);
    }
    
    /**
     * Erase all the route.
     */
    public void clearRoute() {
        this.route.clear();
        this.routeSize = 0;
    }
}
