/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import models.companies.AbstractCompany;
import models.companies.BIGAlpha;
import models.companies.BIGBeta;
import models.companies.BIGCappa;

/**
 *
 * @author josef
 */
public enum AvailableCompanies {
    
    // enum
    BIG_ALPHA (0, "BIG-Alpha", "widget"),
    BIG_BETA (1, "BIG-Beta", "brace"),
    BIG_CAPPA (2, "BIG-Cappa", "crate");
    
    // variables
    private final int index;
    private final String companyName;
    private final String productType;
    
    /**
     * Constructor for available companies
     * @param index index within the companies
     * @param name company name
     * @param type type of product
     */
    AvailableCompanies(int index, String name, String type) {
        
        this.index = index;
        this.companyName = name;
        this.productType = type;
        
    }

    /**
     * @return index within companies
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return name of company
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return type of product produced
     */
    public String getProductType() {
        return productType;
    }
    
    /**
     * @return how many available companies there are
     */
    public static int getLength() {
        return AvailableCompanies.values().length;
    }
    
    /**
     * Get company object from enum object
     * @return one of the available companies
     */
    public AbstractCompany getCompany() {
        switch(this.getCompanyName()) {
            case "BIG-Alpha":
                return new BIGAlpha();
            case "BIG-Beta":
                return new BIGBeta();
            case "BIG-Cappa":
                return new BIGCappa();
            default:
                System.out.println("Invalid company type");
                return null;
        }
    }
    
    /**
     * Get company object from enum object from file
     * @return one of the available companies
     */
    public AbstractCompany getCompanyFromFile() {
        switch(this.getCompanyName()) {
            case "BIG-Alpha":
                return new BIGAlpha(this.getCompanyName());
            case "BIG-Beta":
                return new BIGBeta(this.getCompanyName());
            case "BIG-Cappa":
                return new BIGCappa(this.getCompanyName());
            default:
                System.out.println("Invalid company type");
                return null;
        }
    }
    
    /**
     * Get company from index
     * @param index position of enum
     * @return enum object
     */
    public static AvailableCompanies getAvailableCompany (int index) {
        for (AvailableCompanies company : AvailableCompanies.values()) {
            if (company.getIndex() == index) {
                return company;
            }
        }
        return null;
    }
    
    /**
     * @return all product types available
     */
    public static String[] getProductTypes() {
        String[] types = new String[AvailableCompanies.values().length];
        for (int i = 0; i < AvailableCompanies.getLength(); i++) {
            types[i] = AvailableCompanies.getAvailableCompany(i).getProductType();
        }
        return types;
    }
    
}
