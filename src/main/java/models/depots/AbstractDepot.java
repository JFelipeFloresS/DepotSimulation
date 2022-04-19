/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.depots;

import factory.AvailableCompanies;
import factory.CompanyFactory;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;
import models.transactions.Transaction;

/**
 *
 * @author josef
 */
public abstract class AbstractDepot {
    
    // constants
    public static final int MINIMUM_NATIVE = 15;
    public static final int MAXIMUM_NATIVE = 40;
    public static final int MINIMUM_EXTERNAL = 2;
    public static final int MAXIMUM_EXTERNAL = 30;
    
    protected double allowance;
    protected double moneyMade;
    protected double moneySpent;
    protected HashMap<String, Integer> products;
    protected double productPrice;
    protected String productType;
    protected double deliveryPrice;
    protected String companyName;
    protected UUID depotId;

    /**
     * Standard constructor for AbstractDepot
     * @param allowance 
     * @param productPrice 
     * @param deliveryPrice 
     * @param widgets 
     * @param braces 
     * @param crates 
     * @param company 
     */
    public AbstractDepot(double allowance, double productPrice, double deliveryPrice, int widgets, int braces, int crates, AvailableCompanies company) {
        this.allowance = allowance;
        this.productPrice = productPrice;
        this.deliveryPrice = deliveryPrice;
        this.depotId = UUID.randomUUID();
        this.products = new HashMap<>();
        this.products.put("widget", widgets);
        this.products.put("brace", braces);
        this.products.put("crate", crates);
        this.moneyMade = 0;
        this.moneySpent = 0;
        this.companyName = company.getCompanyName();
        this.productType = company.getProductType();
        System.out.println(companyName + " depot " + depotId + " created.");
    }

    /**
     * Depot constructor with ID for data from file
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
    public AbstractDepot(UUID depotId, double allowance, int widgets, int braces, int crates, double moneyMade, double productPrice, double deliveryPrice, double moneySpent, AvailableCompanies company) {
        this.allowance = allowance;
        this.products = new HashMap<>();
        this.products.put("widget", widgets);
        this.products.put("brace", braces);
        this.products.put("crate", crates);
        this.moneyMade = moneyMade;
        this.moneySpent = moneySpent;
        this.productPrice = productPrice;
        this.deliveryPrice = deliveryPrice;
        this.companyName = company.getCompanyName();
        this.depotId = depotId;
        this.productType = company.getProductType();
    }
    
    
    /**
     * Deducts the stock of product and increases the money made
     * @param transaction transaction object
     */
    public void sellProduct(Transaction transaction) {
        this.products.put(this.productType, this.products.get(this.productType) - transaction.getQuantityBought());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        this.moneyMade += transaction.getTotalCost();
        this.moneyMade = Double.valueOf(df.format(this.moneyMade));
    }
    
    /**
     * Increases the amount of stock of the given product and deducts the allowance
     * @param transaction transaction object
     */
    public void buyProduct(Transaction transaction) {
        // increases the stock for the given product
        this.products.put(transaction.getProductType(), this.products.get(transaction.getProductType()) + transaction.getQuantityBought());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        this.allowance -= transaction.getTotalCost();
        this.moneySpent += Double.valueOf(df.format(transaction.getTotalCost()));
        this.allowance = Double.valueOf(df.format(this.allowance));
        this.moneySpent = Double.valueOf(df.format(this.moneySpent));
    }
    
    /**
     * Get external storage availability
     * Deduct all products to the maximum external and adds the native product
     * @return maximum external storage - current external storage
     */
    public int getExternalProductsStorageAvailable() {
        int storage = MAXIMUM_EXTERNAL - this.products.get("widget") - this.products.get("brace") - this.products.get("crate");
        storage += this.products.get(this.productType);
        return storage;
    }
    
    /**
     * @return native products stock - minimum stock
     */
    public int getNativeProductsAvailable() {
        return this.products.get(this.productType) - MINIMUM_NATIVE;
    }
    
    /**
     * Gets the price of deliver + products
     * @param quantity amount of products to be bought
     * @return total price of sale
     */
    public double getTotalPriceOfSale(int quantity) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return Double.valueOf(df.format(deliveryPrice + (productPrice * quantity)));
    }
    
    /**
     * @return allowance
     */
    public double getAllowance() {
        return allowance;
    }

    /**
     * @return price for one product
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * @return delivery price
     */
    public double getDeliveryPrice() {
        return deliveryPrice;
    }
    
    /**
     * @return type of native product
     */
    public String getProductType() {
        return productType;
    }
    
    /**
     * @return name of company the depot belongs to
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return UUID id
     */
    public UUID getDepotId() {
        return depotId;
    }
    
    @Override
    public String toString() {
        return companyName + " depot: {id=" + depotId + ", allowance=" + allowance + ", widgets=" + products.get("widget") + ", braces=" + products.get("brace") + ", crates=" + products.get("crate") + ", moneyMade=" + moneyMade + ", productPrice=" + productPrice + ", deliveryPrice=" + deliveryPrice + ", moneySpent=" + moneySpent + ", balance=" + getBalance() + '}';
    }

    /**
     * @return number of native products
     */
    public int getNativeStorage() {
        return this.products.get(this.productType);
    }
    
    /**
     * @return number of external products
     */
    public int getTotalExternalStorage() {
        int totalStorage = 0;
        for (Map.Entry<String, Integer> entry : this.products.entrySet()) {
            totalStorage += entry.getValue();
        }
        return totalStorage - this.products.get(this.productType);
    }

    /**
     * @return quantity of braces in stock
     */
    public int getBraces() {
        return this.products.get("brace");
    }
    
    /**
     * @return quantity of widgets in stock
     */
    public int getWidgets() {
        return this.products.get("widget");
    }
    
    /**
     * @return quantity of crates in stock
     */
    public int getCrates() {
        return this.products.get("crate");
    }

    /**
     * @return money made by selling stock
     */
    public double getMoneyMade() {
        return moneyMade;
    }

    /**
     * @return money spent on stock
     */
    public double getMoneySpent() {
        return moneySpent;
    }
    
    /**
     * @return money made - money spent
     */
    public double getBalance() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return Double.valueOf(df.format(moneyMade - moneySpent));
    }
    
    /**
     * Random double generator for depot creation
     * @param min min range
     * @param max max range (included)
     * @return random double
     */
    protected static double getRandomDoubleInRange(int min, int max) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String valueString = df.format(min + (((max + 1) - min) * new Random().nextDouble()));
        return Double.valueOf(valueString);
    }
    
    /**
     * Random native stock generator
     * @return native stock number within established parameters
     */
    protected static int getRandomNativeStock() {
        return new Random().nextInt((AbstractDepot.MAXIMUM_NATIVE - AbstractDepot.MINIMUM_NATIVE) + 1) + AbstractDepot.MINIMUM_NATIVE;
    }
    
    /**
     * Random external stock generator
     * @return external stock number within established parameters
     */
    protected static int getRandomExternalStock() {
        return new Random().nextInt((AbstractDepot.MAXIMUM_EXTERNAL - AbstractDepot.MINIMUM_EXTERNAL) + 1) + AbstractDepot.MINIMUM_EXTERNAL;
    }
    
    /**
     * Split a number randomly between 2
     * @param externalStock number to be split
     * @return array with 2 integers randomly split
     */
    protected static int[] splitExternalStock(int externalStock) {
        int a = new Random().nextInt(externalStock);
        int b = externalStock - a;
        
        return new int[]{a, b};
    }
    
    public int getProductStock(String type) {
        return this.products.get(type);
    }
    
    
    /**
     * Random depot builder
     */
    public static class DepotBuilder {
        
        private final double productPrice;
        private final double deliveryPrice;
        private final double allowance;
        private final int nativeStock;
        private final int externalStock;
        private final int[] externalArray;
        
        /**
         * Constructor that gets random values for the variables
         */
        public DepotBuilder() {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            // generates prices between 1 and 10
            productPrice = Double.valueOf(df.format(getRandomDoubleInRange(1, 10)));
            deliveryPrice = Double.valueOf(df.format(getRandomDoubleInRange(1, 10)));

            //generates an allowance between 50 and 100
            allowance = Double.valueOf(df.format(getRandomDoubleInRange(50, 100)));

            // generates stock based on maximum and minimum values
            nativeStock = getRandomNativeStock();
            externalStock = getRandomExternalStock();

            // randomly split generated external stock
            externalArray = splitExternalStock(externalStock);
        }
        
        /**
         * @return random BIG-Alpha depot
         */
        public BIGAlphaDepot buildAlphaDepot() {
            return new BIGAlphaDepot(allowance, productPrice, deliveryPrice, nativeStock, externalArray[0], externalArray[1], AvailableCompanies.BIG_ALPHA);
        }
        
        /**
         * @return random BIG-Beta depot
         */
        public BIGBetaDepot buildBetaDepot() {
            return new BIGBetaDepot(allowance, productPrice, deliveryPrice, externalArray[0], nativeStock, externalArray[1], AvailableCompanies.BIG_BETA);
        }
        
        /**
         * @return random BIG-Cappa depot 
         */
        public BIGCappaDepot buildCappaDepot() {
            return new BIGCappaDepot(allowance, productPrice, deliveryPrice, externalArray[0], externalArray[1], nativeStock, AvailableCompanies.BIG_CAPPA);
        }
        
    }

}
