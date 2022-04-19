/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.companies;

import models.depots.AbstractDepot;
import models.depots.BIGAlphaDepot;

/**
 *
 * @author josef
 */
public class BIGAlpha extends AbstractCompany {

    /**
     * Standard constructor for BIG-Alpha
     * instantiates depots
     */
    public BIGAlpha() {
        super();
        this.companyName = "BIG-Alpha";
        depots = new BIGAlphaDepot[NUMBER_OF_DEPOTS];
        for (int i = 0; i < depots.length; i++) {
            depots[i] = new AbstractDepot.DepotBuilder().buildAlphaDepot();
        }
    }

    /**
     * Constructor for BIG-Alpha from .txt file data
     * @param companyName 
     */
    public BIGAlpha(String companyName) {
        super(companyName);
    }

}
