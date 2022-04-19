/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import models.companies.AbstractCompany;

/**
 * @author josef
 */
public interface CompanyAbstractFactory {

    /**
     * Get company based on type
     *
     * @param type one of the available companies
     * @return an instance of one of the available companies for randomly generated data
     */
    public abstract AbstractCompany createCompany(AvailableCompanies company);

    /**
     * Get company based on type
     * 
     * @param type one of the available companies
     * @return an instance of one of the available companies for file input data
     */
    public abstract AbstractCompany createCompanyFromFile(AvailableCompanies company);
}
