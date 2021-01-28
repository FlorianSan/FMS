/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;
/**
 * Class to manage and to get Waypoint Information
 *
 * @author edoua
 */

public class Waypoint {

    /**
     * Waypoint identifier
     */
    private String identifier;

    /**
     * Set Waypoint identifier
     * 
     * @param identifier Waypoint identifier
     */
    public void setIdentifier(String identifier) {
    	this.identifier = identifier;
    }

    /**
     * Get Waypoint identifier
     * 
     * @return Waypoint identifier
     */
    public String getIdentifier() {
    	return this.identifier;
    }
}
