/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import factory.AvailableCompanies;
import models.dataAccess.TxtSimulationDAO;
import factory.CompanyFactory;
import java.util.ArrayList;
import java.util.Random;
import models.transactions.Transaction;
import models.companies.AbstractCompany;
import models.depots.AbstractDepot;
import view.SimulationMenu;

/**
 *
 * @author josef
 */
public class SimulationController {

    private AbstractCompany[] companies;
    private final ArrayList<Transaction> transactions;
    
    /**
     * Randomly generated simulation constructor
     * @param isInOrder are depot sales in order or random if false
     */
    public SimulationController(boolean isInOrder) {
        // instatiantion of variables for simulation
        System.out.println("Initialising simulation.");
        instantiateCompanies();
        this.transactions = new ArrayList<>();
        
        // execution of simulation
        if (isInOrder) executeTransactionsSimulationInOrder();
        else executeTransactionsSimulationRandomly();
        
        System.out.println("Simulation successful.");
        
        // writing simulation to txt file
        new TxtSimulationDAO().writeToFile(companies, transactions);
        
        // opening simulation menu for user
        SimulationMenu simulationMenu = new SimulationMenu(this);
    }
    
    /**
     * Simulation Controller constructor from file
     * @param companies
     * @param transactions 
     */
    public SimulationController(AbstractCompany[] companies, ArrayList<Transaction> transactions) {
        this.companies = companies;
        this.transactions = transactions;
        
        // opening simulation menu for user
        SimulationMenu simulationMenu = new SimulationMenu(this);
    }

    /**
     * @return abstract company array
     */
    public AbstractCompany[] getCompanies() {
        return companies;
    }

    /**
     * @return transactions array list
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    
    /**
     * Checks limit of products the buying depot can buy and the selling depot can sell
     * @param buyingDepot depot currently buying stock
     * @param sellingDepot depot currently selling stock
     * @return how many products depot is allowed to buy based on native products, external products storage and allowance availability
     */
    private static int getLimitOfProductsToBuy(AbstractDepot buyingDepot, AbstractDepot sellingDepot) {
        double delivery = sellingDepot.getDeliveryPrice();
        double product = sellingDepot.getProductPrice();
        double allowance = buyingDepot.getAllowance();
        
        int nativeProductsAvailable = sellingDepot.getNativeProductsAvailable();
        int externalProductsStorageAvailable = buyingDepot.getExternalProductsStorageAvailable();
        
        double moneyLeftAfterDelivery = allowance - delivery; // deducting delivery to total allowance left

        int maxProducts = (int) Math.floor(moneyLeftAfterDelivery / product); // max products based on allowance left

        /**
         * if there aren't enough products available in depot return either the storage available in buying depot or the native products available in the selling,
         * otherwise check storage availability
         */
        if (maxProducts > nativeProductsAvailable) { 
            if (nativeProductsAvailable <= externalProductsStorageAvailable) return nativeProductsAvailable;
            else return externalProductsStorageAvailable;
            
        } else {
            if (maxProducts <= externalProductsStorageAvailable) return maxProducts;
            else return externalProductsStorageAvailable;
            
        }
    }

    /**
     * Executes the transaction by adding and removing the stock and removing the allowance accordingly
     * @param currentDepot depot currently buying stock
     * @param sellingDepot depot currently selling stock
     * @return transaction object created based on the limits of products to buy
     */
    private static Transaction executeTransaction(AbstractDepot currentDepot, AbstractDepot sellingDepot) {

        // get amount of stock to be bought and creates a transaction object
        int productsToBuy = getLimitOfProductsToBuy(currentDepot, sellingDepot);
        Transaction transaction = new Transaction(sellingDepot.getProductType(), sellingDepot.getProductPrice(), sellingDepot.getDeliveryPrice(), productsToBuy, currentDepot.getCompanyName(), currentDepot.getDepotId().toString(), sellingDepot.getCompanyName(), sellingDepot.getDepotId().toString());
        
        // sets depots to buy and sell according to the defined transaction
        currentDepot.buyProduct(transaction);
        sellingDepot.sellProduct(transaction);
        return transaction;

    }

    /**
     * Creates AbstractCompany objects based on the CompanyFactory
     */
    private void instantiateCompanies() {
        System.out.println("Creating companies...");
        CompanyFactory factory = new CompanyFactory();
        this.companies = new AbstractCompany[AvailableCompanies.getLength()]; // same length as available companies
        
        // instantiates companies based on the available ones
        for (int i = 0; i < companies.length; i++) {
            companies[i] = factory.createCompany(AvailableCompanies.getAvailableCompany(i));
            System.out.println("Company " + companies[i].getCompanyName() + " and depots created successfully.\n");
        }
    }

    /**
     * The simulation itself in order of depot and company
     */
    private void executeTransactionsSimulationInOrder() {
        for (int buyingCompanyIndex = 0; buyingCompanyIndex < companies.length; buyingCompanyIndex++) { // company loop

            for (AbstractDepot buyingDepot : companies[buyingCompanyIndex].getDepots()) { // current buying company depots loop
                if (buyingDepot.getAllowance() <= 0 || buyingDepot.getExternalProductsStorageAvailable() <= 0) continue; // if current depot has no allowance or no external products storage left, skip to the next one
                
                for (int sellingCompanyIndex = 0; sellingCompanyIndex < companies.length; sellingCompanyIndex++) { // selling company loop
                    if (buyingCompanyIndex == sellingCompanyIndex) continue; // if it's the same company as the current one, skip to the next one
                    
                    for (AbstractDepot sellingDepot : companies[sellingCompanyIndex].getDepots()) { // current selling company depots loop
                        
                        // if current buying depot has no room left for storage or has no allowance left, skip to the next buying depot
                        if (buyingDepot.getAllowance() <= 2 || buyingDepot.getExternalProductsStorageAvailable() <= 0) break;

                        /**
                         * if current selling depot has stock available and its selling price is low enough
                         * execute transaction and add it to the Transaction ArrayList
                         */
                        if (sellingDepot.getNativeProductsAvailable() > 0 && buyingDepot.getAllowance() > sellingDepot.getTotalPriceOfSale(1)) {
                            Transaction currentTransaction = executeTransaction(buyingDepot, sellingDepot);
                            transactions.add(currentTransaction);
                        }

                    }
                }
            }
        }
    }
    
    /**
     * The simulation itself executing transactions with a random selling depot
     */
    private void executeTransactionsSimulationRandomly() {
        System.out.println("Transactions have been initiated.");
        for (int buyingCompanyIndex = 0; buyingCompanyIndex < companies.length; buyingCompanyIndex++) { // buying company loop
            for (AbstractDepot buyingDepot : companies[buyingCompanyIndex].getDepots()) { // current buying company depots loop
                
                
                int attempts = 0;
                
                /**
                 * loops through random depots while the buying depot has allowance for the minimum price for a product and a delivery 
                 * and storage is available
                 * there is also a cap on the attempts to find a selling depot
                 */
                while ((buyingDepot.getAllowance() >= 2 || buyingDepot.getExternalProductsStorageAvailable() >= 0) && attempts <= 80) {
                    attempts++;
                    
                    int sellingCompanyIndex = new Random().nextInt(companies.length);

                    // if it's the same company as the current one, skip to the next one or fallback to 0 if it's the last one
                    if (buyingCompanyIndex == sellingCompanyIndex) sellingCompanyIndex++;
                    if (sellingCompanyIndex >= companies.length) sellingCompanyIndex = 0;

                    // get a random depot index from the selling company depots
                    int depotIndex = new Random().nextInt(companies[sellingCompanyIndex].getDepots().length);
                    
                    // gets a random depot from a random company - different to the buying company
                    AbstractDepot sellingDepot = companies[sellingCompanyIndex].getDepots()[depotIndex];
                    
                    // if current selling depot has no stock available or its selling price is too high, skip to the next selling depot
                    if (sellingDepot.getNativeProductsAvailable() <= 0 || buyingDepot.getAllowance() <= sellingDepot.getTotalPriceOfSale(1)) continue;

                    // execute transaction and add it to the Transaction ArrayList
                    Transaction currentTransaction = executeTransaction(buyingDepot, sellingDepot);
                    transactions.add(currentTransaction);

                }
            }
        }
    }

    /**
     * Displays least profitable depot(s)
     */
    public void showLeastProfitableDepot() {
        ArrayList<AbstractDepot> leastProfitable = new ArrayList<>();
        
        for (AbstractCompany company : companies) { // company loop
            for (AbstractDepot depot : company.getDepots()) { // company depot loop
                
                if (leastProfitable.isEmpty()) { // add first depot
                    leastProfitable.add(depot);
                    continue;
                }
                
                if (depot.getBalance() < leastProfitable.get(0).getBalance()) { // set current depot as the least profitable
                    leastProfitable.removeAll(leastProfitable);
                    leastProfitable.add(depot);
                } else if (depot.getBalance() == leastProfitable.get(0).getBalance()) { // add current depot to the least profitable arraylist
                    leastProfitable.add(depot);
                }
                
            }
        }
        
        System.out.println("Least profitable depot(s):");
        leastProfitable.forEach(depot -> {
            System.out.println(depot);
        });
    }

    /**
     * Displays most profitable depot(s)
     */
    public void showMostProfitableDepot() {
        ArrayList<AbstractDepot> mostProfitable = new ArrayList<>();
        
        for (AbstractCompany company : companies) { // company loop
            for (AbstractDepot depot : company.getDepots()) { // company depot loop
                
                if (mostProfitable.isEmpty()) { // add first depot to list
                    mostProfitable.add(depot);
                    continue;
                }
                
                if (depot.getBalance() > mostProfitable.get(0).getBalance()) { // set current depot as the most profitable
                    mostProfitable.removeAll(mostProfitable);
                    mostProfitable.add(depot);
                } else if (depot.getBalance() == mostProfitable.get(0).getBalance()) { // add current depot to the most profitable arraylist
                    mostProfitable.add(depot);
                }
                
            }
        }
        
        System.out.println("Most profitable depot(s):");
        mostProfitable.forEach(depot -> {
            System.out.println(depot);
        });
        
    }

}
