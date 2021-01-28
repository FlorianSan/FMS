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

public class Airway {

    /**
     * Airway identifier
     */
    private String identifier;

    /**
     * Set Airway identifier
     * 
     * @param identifier Airway identifier
     */
    public void setIdentifier(String identifier) {
    	this.identifier = identifier;
    }

    /**
     * Get Airway identifier
     * 
     * @return Airway identifier
     */
    public String getIdentifier() {
    	return this.identifier;
    }
}
