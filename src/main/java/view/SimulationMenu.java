/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.MainController;
import controller.SimulationController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.transactions.Transaction;
import models.companies.AbstractCompany;

/**
 *
 * @author josef
 */
public class SimulationMenu {
    
    private final SimulationController controller;
    private final BufferedReader br;
    
    /**
     * Constructor for SimulationMenu
     * @param controller SimulationController
     */
    public SimulationMenu(SimulationController controller) {
        this.controller = controller;
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the simulation menu!");
        
        // loop through options until user decides to exit the menu
        while(true) {
            mainMenuOptions();
        }
    }

    /**
     * Prompts the user to choose one of the options
     */
    private void mainMenuOptions() {
        System.out.println("Please choose an option:");
        System.out.println("1. (AT) All transactions");
        System.out.println("2. (ST) Specific company transactions");
        System.out.println("3. (D) Depot information on a specific company");
        System.out.println("4. (P) Show all profits");
        System.out.println("5. (MP) Show most profitable depots");
        System.out.println("6. (LP) Show least profitable depots");
        System.out.println("8. (NEW) Start a new simulation or open an existing one");
        System.out.println("9. (EXIT) Quit the menu");
        
        try {
            String choice = br.readLine();
            
            // acts according to user's choice
            switch(choice.toLowerCase()) {
                case "1":
                case "1.":
                case "at":
                    showAllTransactions();
                    break;
                case "2":
                case "2.":
                case "st":
                    System.out.println("Please choose what company to get transactions from:");
                    companyTransactionTypeChooser(specificCompanyChooser());
                    break;
                case "3":
                case "3.":
                case "d":
                    System.out.println("Please choose what company to get depot information from:");
                    depotInformationTypeChooser(specificCompanyChooser());
                    break;
                case "4":
                case "4.":
                case "p":
                    showAllProfits();
                    break;
                case "5":
                case "5.":
                case "mp":
                    controller.showMostProfitableDepot();
                    break;
                case "6":
                case "6.":
                case "lp":
                    controller.showLeastProfitableDepot();
                    break;
                case "8":
                case "8.":
                case "new":
                    MainController mainController = new MainController();
                    break;

                case "9":
                case "9.":
                case "exit":
                    exitMenu();
            }
        } catch (IOException ex) {
            Logger.getLogger(SimulationMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Show all transactions that happened in the simulation
     */
    private void showAllTransactions() {
        System.out.println("Here are all the transactions available:");
        controller.getTransactions().forEach(transaction -> {
            System.out.println(transaction);
        });
    }

    /**
     * Prompts the user to choose a company
     * @return company chosen by user
     */
    private AbstractCompany specificCompanyChooser() {
        
        for (int i = 1; i <= controller.getCompanies().length; i++) { // displays companies in the simulation controller
            System.out.println(i + ". " + controller.getCompanies()[i - 1].getCompanyName());
        }
        
        System.out.println("MAIN. To return to main menu.");
        System.out.println("EXIT. To quit the simulation.");
        
        try {
            String choice = br.readLine();
            
            if (choice.equalsIgnoreCase("main")) return null;
            if (choice.equalsIgnoreCase("exit")) exitMenu();
            
            int companyIndex = Integer.parseInt(choice) - 1;
            System.out.println("You chose " + controller.getCompanies()[companyIndex].getCompanyName());
            return controller.getCompanies()[companyIndex]; // return chosen company
            
            // return null if there are any errors
        } catch (IOException ex) {
            Logger.getLogger(SimulationMenu.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Invalid company chosen");
            return null;
        }
    }

    /**
     * Prompts user to choose what type of transaction to be displayed
     * @param specificCompany the company to display the transactions from
     */
    private void companyTransactionTypeChooser(AbstractCompany specificCompany) {
        if (specificCompany == null) return;
        
        System.out.println("Choose what transactions you would like to see:");
        System.out.println("1. All");
        System.out.println("2. Buying");
        System.out.println("3. Selling");
        System.out.println("MAIN. To return to main menu.");
        System.out.println("EXIT. To quit the simulation.");
        
        try {
            String choice = br.readLine();
            
            // acts according to user's choice
            switch(choice.toLowerCase()) {
                case "1":
                case "1.":
                case "all":
                    showAllCompanyTransactions(specificCompany.getCompanyName());
                    break;
                case "2":
                case "2.":
                case "buying":
                case "buy":
                case "bought":
                    showBuyingCompanyTransactions(specificCompany.getCompanyName());
                    break;
                case "3":
                case "3.":
                case "selling":
                case "sell":
                case "sold":
                    showSellingCompanyTransactions(specificCompany.getCompanyName());
                    break;
                case "main":
                    return;
                case "exit":
                    exitMenu();
                default:
                    System.out.println("Invalid input. Pleas try again.");
                    companyTransactionTypeChooser(specificCompany);
                    break;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SimulationMenu.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        // prompts user again after displaying the chosen transactions
        companyTransactionTypeChooser(specificCompany);
    }

    /**
     * Shows all transactions that the chosen company has taken part in
     * @param companyName chosen company's name
     */
    private void showAllCompanyTransactions(String companyName) {
        System.out.println("All transactions for " + companyName + ':');
        int totalTransactions = 0;
        for (Transaction transaction : controller.getTransactions()) {
            if (transaction.getBoughtBy().contains(companyName) || transaction.getSoldBy().contains(companyName)) {
                System.out.println(transaction);
                totalTransactions++;
            }
        }
        System.out.println("Total transactions: " + totalTransactions);
    }

    /**
     * Shows all transactions that the chosen company was the buyer
     * @param companyName chosen company's name
     */
    private void showBuyingCompanyTransactions(String companyName) {
        System.out.println("Buying transactions for " + companyName + ':');
        int totalTransactions = 0;
        for (Transaction transaction : controller.getTransactions()) {
            if (transaction.getBoughtBy().contains(companyName)) {
                System.out.println(transaction);
                totalTransactions++;
            }
        }
        System.out.println("Total buying transactions: " + totalTransactions);
    }

    /**
     * Shows all transactions that the chosen company was the seller
     * @param companyName chosen company's name
     */
    private void showSellingCompanyTransactions(String companyName) {
        System.out.println("Selling transactions for " + companyName + ':');
        int totalTransactions = 0;
        for (Transaction transaction : controller.getTransactions()) {
            if (transaction.getSoldBy().contains(companyName)) {
                System.out.println(transaction);
                totalTransactions++;
            }
        }
        System.out.println("Total selling transactions: " + totalTransactions);
    }

    /**
     * Prompts the user to choose what type of information to get on a company's depots
     * @param specificCompany chosen company
     */
    private void depotInformationTypeChooser(AbstractCompany specificCompany) {
        if (specificCompany == null) return;
        
        System.out.println("Choose what information you would like to see about " + specificCompany.getCompanyName() + " depots:");
        System.out.println("1. Own product stock");
        System.out.println("2. External product stock");
        System.out.println("3. Allowance left");
        System.out.println("4. All Money made");
        System.out.println("5. All Money spent");
        System.out.println("6. Final cash balance");
        System.out.println("MAIN. To return to main menu.");
        System.out.println("EXIT. To quit the simulation.");
        
        try {
            String choice = br.readLine();
            
            // acts according to user's choice
            switch(choice.toLowerCase()) {
                case "1":
                case "1.":
                    specificCompany.showOwnProductInStock();
                    break;
                case "2":
                case "2.":
                    specificCompany.showExternalProductsInStock();
                    break;
                case "3":
                case "3.":
                    specificCompany.showAllowanceLeft();
                    break;
                case "4":
                case "4.":
                    specificCompany.showAllMoneyMade();
                    break;
                case "5":
                case "5.":
                    specificCompany.showAllMoneySpent();
                    break;
                case "6":
                case "6.":
                    specificCompany.showAllFinalBalances();
                    break;
                case "main":
                    return;
                case "exit":
                    exitMenu();
                default:
                    System.out.println("Invalid input. Pleas try again.");
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(SimulationMenu.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        // prompts the user again after showing the information requested
        depotInformationTypeChooser(specificCompany);
        
    }
    
    /**
     * Displays all profits for all companies
     */
    private void showAllProfits() {
        for (AbstractCompany company : controller.getCompanies()) {
            System.out.println(company.getCompanyName() + ": ");
            company.showAllFinalBalances();
            System.out.println("");
        }
    }
    
    /**
     * Farewell message and exit the system
     */
    private void exitMenu() {
        System.out.println("Good bye.");
        System.exit(0);
    }

}
