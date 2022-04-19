/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.companies;

import factory.AvailableCompanies;
import factory.CompanyFactory;
import java.util.ArrayList;
import java.util.HashMap;
import models.depots.AbstractDepot;

/**
 *
 * @author josef
 */
public abstract class AbstractCompany {
    
    protected AbstractDepot[] depots;
    protected String companyName;
    public static int NUMBER_OF_DEPOTS = 40;

    /**
     * Empty constructor
     */
    public AbstractCompany() {
    }
    
    /**
     * Constructor for data from file
     * @param companyName 
     */
    public AbstractCompany(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Instantiate AbstractDepot array and adds values from ArrayList
     * @param depots ArrayList of AbstractDepot to get values from
     */
    public void setDepots(ArrayList<AbstractDepot> depots) {
        this.depots = new AbstractDepot[depots.size()];
        for (int i = 0; i < depots.size(); i++) {
            this.depots[i] = depots.get(i);
        }
    }
    
    /**
     * @return AbstractDepot array
     */
    public AbstractDepot[] getDepots() {
        return depots;
    }

    /**
     * @return name of company
     */
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String toString() {
        String s = companyName + "{\n";
        for(AbstractDepot depot : depots) {
            s += depot.toString() + "\n";
        }
        return s + '}';
    }
    
    /**
     * Displays native products in all depots and total own products
     */
    public void showOwnProductInStock(){
        int totalProducts = 0;
        for (AbstractDepot depot : depots) {
            totalProducts += depot.getNativeStorage();
            System.out.println("Depot " + depot.getDepotId() + ": " + depot.getNativeStorage() + " " + depot.getProductType());
        }
        System.out.println("Total native products stored across depots: " + totalProducts);
    }
    
    /**
     * Displays external products in all depots and total external products
     */
    public void showExternalProductsInStock() {
        HashMap<String, Integer> products = new HashMap<>();
        for (String prodType : AvailableCompanies.getProductTypes()) {
            products.put(prodType, 0);
        }
        
        for (AbstractDepot depot : depots) {
            System.out.println("Depot " + depot.getDepotId() + ": ");
            
            for (String prodType : AvailableCompanies.getProductTypes()) {
                products.put(prodType, products.get(prodType) + depot.getProductStock(prodType));
                if (!depot.getProductType().equals(prodType)) System.out.println(prodType + "s stock: " + depot.getProductStock(prodType));
            }
            
            System.out.println("Total external stock: " + depot.getTotalExternalStorage() + "\n");
        }
        
        for (String prodType : AvailableCompanies.getProductTypes()) {
            if (!depots[0].getProductType().equals(prodType)) System.out.println("Total " + prodType + "s in stock: " + products.get(prodType));
        }
        
        int totalExternalProducts = products.get("widget") + products.get("brace") + products.get("crate") - products.get(depots[0].getProductType());
        System.out.println("Total external products: " + totalExternalProducts);
    }
    
    /**
     * Displays allowance left in all depots and total allowance left
     */
    public void showAllowanceLeft() {
        double totalAllowance = 0.0;
        
        for (AbstractDepot depot: depots) {
            totalAllowance += depot.getAllowance();
            System.out.println("Depot " + depot.getDepotId() + " allowance left: " + depot.getAllowance());
        }
        
        System.out.println("Total allowance left on depots: " + totalAllowance);
    }
    
    /**
     * Displays all money made from sales in all depots and total money made
     */
    public void showAllMoneyMade() {
        double totalMoney = 0.0;
        
        for (AbstractDepot depot : depots) {
            totalMoney += depot.getMoneyMade();
            System.out.println("Depot " + depot.getDepotId() + " money made: " + depot.getMoneyMade());
        }
        
        System.out.println("Total money made on depots: " + totalMoney);
    }
    
    /**
     * Displays all money spent on products in all depots and total money made
     */
    public void showAllMoneySpent() {
        double totalMoneySpent = 0.0;
        
        for (AbstractDepot depot : depots) {
            totalMoneySpent += depot.getMoneySpent();
            System.out.println("Depot " + depot.getDepotId() + " money made: " + depot.getMoneySpent());
        }
        
        System.out.println("Total money spent on depots: " + totalMoneySpent);
    }
    
    /**
     * Displays all profit made in all depots and total profit made
     */
    public void showAllFinalBalances() {
        double totalProfit = 0.0;
        
        for (AbstractDepot depot : depots) {
            totalProfit += depot.getBalance();
            System.out.println("Depot " + depot.getDepotId() + " balance: " + depot.getBalance());
        }
        
        System.out.println("Final balance on all depots: " + totalProfit);
    }
    
}
