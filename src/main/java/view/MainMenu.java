/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.MainController;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
    
/**
 *
 * @author josef
 */
public class MainMenu {

    private final MainController controller;
    private final BufferedReader br;
    
    /**
     * Main menu constructor
     * @param controller MainController that calls the main menu
     */
    public MainMenu(MainController controller) {
        this.controller = controller;
        this.br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Welcome to the company transaction simulator!");
        
        while(true) {
            options();
        }
    }

    /**
     * Displays initial options and prompts user to choose
     */
    private void options() {
        System.out.println("Please choose your preference:");
        System.out.println("1. Create (new) simulation with in order sales");
        System.out.println("2. Create new simulation with (random) sales");
        System.out.println("3. (Open) an existing simulation to analyse.");
        System.out.println("9. Exit.");
        
        try {
            String choice = br.readLine();
            
            switch(choice.toLowerCase()) {
                case "1":
                case "1.":
                case "new":
                    this.controller.startNewSimulation(true);
                    break;
                case "2":
                case "2.":
                case "random":
                    this.controller.startNewSimulation(false);
                    break;
                case "3":
                case "3.":
                case "open":
                    openSimulationChoice();
                    break;
                case "9":
                case "9.":
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * File path prompt
     */
    public void openSimulationChoice() {
        System.out.println("Please enter the number on the list, path to the simulation file, \"return\" to go back to main menu or \"exit\" to exit the simulator.");
        
        try {
            
            // display found files and stores them within the ArrayList availableFiles
            ArrayList<String> availableFiles = getAvailableFiles();
            
            for (int fileIndex = 0; fileIndex < availableFiles.size(); fileIndex++) {
                System.out.println((fileIndex + 1) + ". " + availableFiles.get(fileIndex).substring(0, availableFiles.get(fileIndex).length() - 4));
            }
            
            String path = br.readLine(); // user input
            
            // check if path is in the list
            for (int i = 1; i <= availableFiles.size(); i++) {
                if (path.equals(String.valueOf(i)) || path.equals(String.valueOf(i) + ".")) {
                    System.out.println("Selected: " + availableFiles.get(i - 1));
                    this.controller.openExistingSimulation(availableFiles.get(i - 1));
                    return;
                }
            }
            switch(path.toLowerCase()) {
                case "return":
                    return;
                case "exit":
                    System.exit(0);
                default:
                    this.controller.openExistingSimulation(path);
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error, please try again");
        }
        
        openSimulationChoice();
    }

    /**
     * Gets Depot Simulation files in the current directory
     * @return ArrayList of available file names
     */
    public ArrayList<String> getAvailableFiles() {
        // get files from current directory
        String baseDir = "./";
        File fileName = new File(baseDir);
        File[] fileList = fileName.listFiles();

        String filePatternStart = "Depot_Simulation_";
        String filePatternEnd = ".txt";

        // display found files and stores them within the ArrayList availableFiles
        ArrayList<String> availableFiles = new ArrayList<>();
        int fileIndex = 0;
        for (File file: fileList) {
            if (file.toString().contains(filePatternStart) && file.toString().contains(filePatternEnd)) {
                fileIndex++;
                availableFiles.add(file.getName());
            }
        }
        return availableFiles;
    }
    
}
