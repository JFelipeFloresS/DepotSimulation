/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.depots;

import factory.AvailableCompanies;
import java.util.UUID;

/**
 *
 * @author josef
 */
public class BIGCappaDepot extends AbstractDepot {

    /**
     * Constructor for randomly generated depot
     * @param allowance
     * @param productPrice
     * @param deliveryPrice
     * @param widgets
     * @param braces
     * @param crates
     * @param company 
     */
    public BIGCappaDepot(double allowance, double productPrice, double deliveryPrice, int widgets, int braces, int crates, AvailableCompanies company) {
        super(allowance, productPrice, deliveryPrice, widgets, braces, crates, company);
    }

    /**
     * Constructor for depot from file data
     * @param depotId
     * @param allowance
     * @param widgets
     * @param braces
     * @param crates
     * @param moneyMade
     * @param productPrice
     * @param deliveryPrice
     * @param moneySpent
     * @param company 
     */
    public BIGCappaDepot(UUID depotId, double allowance, int widgets, int braces, int crates, double moneyMade, double productPrice, double deliveryPrice, double moneySpent, AvailableCompanies company) {
        super(depotId, allowance, widgets, braces, crates, moneyMade, productPrice, deliveryPrice, moneySpent, company);
    }

    
}
