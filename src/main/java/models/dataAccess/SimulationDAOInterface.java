/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import models.transactions.Transaction;
import models.companies.AbstractCompany;

/**
 *
 * @author josef
 */
public interface SimulationDAOInterface {
    
    /**
     * Get data from file
     * @return HashMap: "companies" -> AbstractCompany[], "transactions" -> ArrayList<Transaction>
     */
    public HashMap<String, Object> getData();
    
    /**
     * Write data to new file
     * @param companies
     * @param transactions 
     */
    public void writeToFile(AbstractCompany[] companies, ArrayList<Transaction> transactions);
    
}
