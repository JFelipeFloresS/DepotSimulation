/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.companies;

import models.depots.AbstractDepot;
import models.depots.BIGBetaDepot;

/**
 *
 * @author josef
 */
public class BIGBeta extends AbstractCompany {

    /**
     * Standard constructor for BIG-Beta
     * instantiates depots
     */
    public BIGBeta() {
        super();
        this.companyName = "BIG-Beta";
        depots = new BIGBetaDepot[NUMBER_OF_DEPOTS];
        for (int i = 0; i < depots.length; i++) {
            depots[i] = new AbstractDepot.DepotBuilder().buildBetaDepot();
        }
    }

    /**
     * Constructor for BIG-Beta from .txt file data
     * @param companyName 
     */
    public BIGBeta(String companyName) {
        super(companyName);
    }

    
}
