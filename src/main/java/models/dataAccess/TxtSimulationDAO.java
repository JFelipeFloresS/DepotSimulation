/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dataAccess;

import factory.AvailableCompanies;
import factory.CompanyFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.transactions.Transaction;
import models.companies.AbstractCompany;
import models.depots.AbstractDepot;
import models.depots.BIGAlphaDepot;
import models.depots.BIGBetaDepot;
import models.depots.BIGCappaDepot;

/**
 *
 * @author josef
 */
public class TxtSimulationDAO implements SimulationDAOInterface{
    
    DataSource fileSource = DataSource.getInstance();
    
    @Override
    public void writeToFile(AbstractCompany[] companies, ArrayList<Transaction> transactions) {
        
        BufferedWriter bw = null;
        try {
            // creates a file based on the date and time the simulation is executed
            LocalDateTime currentDate = LocalDateTime.now();
            bw = Files.newBufferedWriter(Paths.get("Depot_Simulation_" + currentDate.format(DateTimeFormatter.ofPattern("dd-MM-YYYY_HH-mm")) + ".txt"));
            bw.write("Company depots:");
            bw.newLine();
            for (AbstractCompany company : companies) { // company loop
                
                bw.write(company.getCompanyName() + ":"); // defines the name of the company
                bw.newLine();
                
                for (AbstractDepot depot : company.getDepots()) { // depot loop
                    bw.write(depot.toString()); // writes the depot onto the file
                    bw.newLine();
                }
                
                bw.newLine();
            }   bw.write("Transactions:");
            bw.newLine();
            for (Transaction transaction : transactions) { // transactions loop
                
                bw.write(transaction.toString()); // writes the transaction onto the file
                bw.newLine();
                
            }   bw.close();
            System.out.println("Simulation file created successfully");
        } catch (IOException ex) {
            Logger.getLogger(TxtSimulationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException ex) {
                Logger.getLogger(TxtSimulationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public HashMap<String, Object> getData() {
        
        // instantiate variables
        HashMap<String, Object> data = new HashMap<>();
        
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<AbstractCompany> companies = new ArrayList<>();
        
        CompanyFactory factory = new CompanyFactory();
        
        ArrayList<AbstractDepot> depots = new ArrayList<>();
        AbstractCompany currentCompany = null;
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileSource.getPath()));
            
            
            String line = br.readLine(); // skip first line
            while((line = br.readLine()) != null) { // loop through lines in file
                
                
                // checks if currentLine is one of the companies names
                for (AvailableCompanies company : AvailableCompanies.values()) {
                    if (line.equalsIgnoreCase(company.getCompanyName() + ":")) {
                        if (!depots.isEmpty()) { // if the current depot arraylist is not empty, adds the depots to the currentCompany and then the company to the companies arraylist
                            currentCompany.setDepots(depots);
                            companies.add(currentCompany);
                        }
                        // initiate next company
                        currentCompany = factory.createCompanyFromFile(company);
                        depots = new ArrayList<>();
                        break;
                    }
                }
                
                String[] lineSplit = line.split("=");
                
                // adds line to depots or transactions depending on what the line is
                if (line.contains("depot")) {
                    depots.add(createDepot(lineSplit, currentCompany));
                } else if(line.contains("transactionId")) {
                    transactions.add(createTransaction(lineSplit));
                }
                
            }
            
            // sets the depots to the last company and the company to the companies arraylist
            currentCompany.setDepots(depots);
            companies.add(currentCompany);
            
        } catch (FileNotFoundException ex) { // File doesn't exist error
            Logger.getLogger(TxtSimulationDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File " + fileSource.getPath() + "doesn't exist.");
            return null;
        } catch (IOException ex) { // Unable to reach file error
            Logger.getLogger(TxtSimulationDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unexpected error.");
            return null;
        } catch(Exception e) {
            System.out.println("Invalid file, please try a valid file.");  
            return null;
        } finally { // close buffered reader
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                Logger.getLogger(TxtSimulationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // put companies and transactions to data hashmap
        data.put("companies", abstractCompanyArrayListToArray(companies));
        data.put("transactions", transactions);
        
        return data;
    }
    
    /**
     * Creates depot with String array from file
     * @param lineSplit line split by "="
     * @param currentCompany company to set type of depot to
     * @return new depot from the current company
     */
    private AbstractDepot createDepot(String[] lineSplit, AbstractCompany currentCompany) {
        UUID id = UUID.fromString(lineSplit[1].split(",")[0]);
        double allowance = Double.valueOf(lineSplit[2].split(",")[0]);
        int widgets = Integer.parseInt(lineSplit[3].split(",")[0]);
        int braces = Integer.parseInt(lineSplit[4].split(",")[0]);
        int crates = Integer.parseInt(lineSplit[5].split(",")[0]);
        double moneyMade = Double.valueOf(lineSplit[6].split(",")[0]);
        double productPrice = Double.valueOf(lineSplit[7].split(",")[0]);
        double deliveryPrice = Double.valueOf(lineSplit[8].split(",")[0]);
        double moneySpent = Double.valueOf(lineSplit[9].split(",")[0]);

        switch(currentCompany.getCompanyName()) {
            case "BIG-Alpha":
                return new BIGAlphaDepot(id, allowance, widgets, braces, crates, moneyMade, productPrice, deliveryPrice, moneySpent, AvailableCompanies.BIG_ALPHA);
            case "BIG-Beta":
                return new BIGBetaDepot(id, allowance, widgets, braces, crates, moneyMade, productPrice, deliveryPrice, moneySpent, AvailableCompanies.BIG_BETA);
            case "BIG-Cappa":
                return new BIGCappaDepot(id, allowance, widgets, braces, crates, moneyMade, productPrice, deliveryPrice, moneySpent, AvailableCompanies.BIG_CAPPA);
            default:
                return null;
        }
    }
    
    /**
     * Creates transaction with String array from file
     * @param lineSplit line split by "="
     * @return transaction
     */
    private Transaction createTransaction(String[] lineSplit) {
        UUID id = UUID.fromString(lineSplit[1].split(",")[0]);
        String productType = lineSplit[2].split(",")[0]; 
        double productsCost = Double.valueOf(lineSplit[4].split(",")[0]); 
        double deliveryCost = Double.valueOf(lineSplit[5].split(",")[0]); 
        int quantityBought = Integer.parseInt(lineSplit[6].split(",")[0]); 
        String boughtBy = lineSplit[7].split(",")[0];
        String soldBy = lineSplit[8].split(",")[0];
        return new Transaction(id, productType, productsCost, deliveryCost, quantityBought, boughtBy, soldBy);
    }
    
    /**
     * Converts an AbstractCompany ArrayList to an AbstractCompany array
     * @param list ArrayList of AbstractCompany
     * @return AbstractCompany array
     */
    private AbstractCompany[] abstractCompanyArrayListToArray(ArrayList<AbstractCompany> list) {
        AbstractCompany[] companyResult = new AbstractCompany[list.size()];
        for (int i = 0; i < list.size(); i++) {
            companyResult[i] = list.get(i);
        }
        return companyResult;
    }
}
