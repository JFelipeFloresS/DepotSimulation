/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.companies;

import models.depots.AbstractDepot;
import models.depots.BIGCappaDepot;

/**
 *
 * @author josef
 */
public class BIGCappa extends AbstractCompany {

    /**
     * Standard constructor for BIG-Cappa
     * instantiates depots
     */
    public BIGCappa() {
        super();
        this.companyName = "BIG-Cappa";
        depots = new BIGCappaDepot[NUMBER_OF_DEPOTS];
        for (int i = 0; i < depots.length; i++) {
            depots[i] = new AbstractDepot.DepotBuilder().buildCappaDepot();
        }
    }

    /**
     * Constructor for BIG-Cappa from .txt file data
     * @param companyName 
     */
    public BIGCappa(String companyName) {
        super(companyName);
    }

}
