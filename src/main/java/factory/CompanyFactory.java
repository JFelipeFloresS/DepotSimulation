/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import models.companies.AbstractCompany;

/**
 *
 * @author josef
 */
public class CompanyFactory implements CompanyAbstractFactory{
    
//    // defines available companies and their product types for easy scalability
//    public static final String[] AVAILABLE_COMPANIES = {"BIG-Alpha", "BIG-Beta", "BIG-Cappa"};
//    public static final String[] PRODUCT_TYPES = {"widget", "brace", "crate"};
    
    @Override
    public AbstractCompany createCompany(AvailableCompanies company) {
        System.out.println("Company " + company.getCompanyName() + " initialised");
        
        return company.getCompany();
    }
    
    @Override
    public AbstractCompany createCompanyFromFile(AvailableCompanies company) {
        return company.getCompany();
    }
    
}
