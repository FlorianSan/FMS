/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;
/**
 * Class to manage and to get Airport Information
 * @author edouard.ladeira
 */

public class Airport {

    /**
     * Airport identifier
     */
    private String identifier;

    /**
     * Set Airport identifier
     * 
     * @param identifier Airport identifier
     */
    public void setIdentifier(String identifier) {
    	this.identifier = identifier;
    }

    /**
     * Get Airport identifier
     * 
     * @return Airport identifier
     */
    public String getIdentifier() {
    	return this.identifier;
    }
    
}
