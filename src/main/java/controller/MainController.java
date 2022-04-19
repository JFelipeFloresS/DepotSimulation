/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.HashMap;
import models.transactions.Transaction;
import models.companies.AbstractCompany;
import models.dataAccess.DataSource;
import models.dataAccess.TxtSimulationDAO;
import view.MainMenu;

/**
 *
 * @author josef
 */
public class MainController {

    private SimulationController simulation;
    private final MainMenu view;
    
    /**
     * instantiates the main menu
     */
    public MainController() {
        view = new MainMenu(this);
    }

    /**
     * Calls the simulation controller random constructor with in order sales
     * @param isInOrderSales false if random order sales
     */
    public void startNewSimulation(boolean isInOrderSales) {
        this.simulation = new SimulationController(isInOrderSales);
    }
    
    /**
     * Calls the simulation controller constructor based on existing data
     * @param path path of .txt file
     */
    public void openExistingSimulation(String path) {
        if (!DataSource.getInstance().setPath(path)) { // if not valid path, prompt user to try again
            System.out.println("Invalid source, please try again.");
        } else {
            HashMap<String, Object> data = new TxtSimulationDAO().getData();
            if (data == null) return;
            this.simulation = new SimulationController((AbstractCompany[])data.get("companies"), (ArrayList<Transaction>) data.get("transactions"));
        }
    }
    
}
