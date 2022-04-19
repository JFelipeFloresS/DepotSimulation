/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.transactions;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 *
 * @author josef
 */
public class Transaction {
    
    private final UUID transactionId;
    private final double productsCost;
    private final double deliveryCost;
    private final int quantityBought;
    private final String boughtBy;
    private final String soldBy;
    private final String productType;

    /**
     * Constructor for transaction
     * @param productType
     * @param productsCost
     * @param deliveryCost
     * @param quantityBought
     * @param boughtByCompany
     * @param boughtByDepot
     * @param soldByCompany 
     * @param soldByDepot 
     */
    public Transaction(String productType, double productsCost, double deliveryCost, int quantityBought, String boughtByCompany, String boughtByDepot, String soldByCompany, String soldByDepot) {
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
        this.quantityBought = quantityBought;
        this.boughtBy = boughtByCompany + " " + boughtByDepot;
        this.soldBy = soldByCompany + " " + soldByDepot;
        this.productType = productType;
        this.transactionId = UUID.randomUUID();
        System.out.println("Transaction " + transactionId + " between " + this.boughtBy + " and " + this.soldBy + " succesfully executed.");
    }
    
    /**
     * Constructor for transaction with id
     * @param id
     * @param productType
     * @param productsCost
     * @param deliveryCost
     * @param quantityBought
     * @param boughtBy
     * @param soldBy
     */
    public Transaction(UUID id, String productType, double productsCost, double deliveryCost, int quantityBought, String boughtBy, String soldBy) {
        this.transactionId = id;
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
        this.quantityBought = quantityBought;
        this.boughtBy = boughtBy;
        this.soldBy = soldBy;
        this.productType = productType;
    }

    /**
     * @return transaction id
     */
    public UUID getTransactionId() {
        return transactionId;
    }

    /**
     * @return all products price
     */
    public double getProductsCost() {
        return productsCost;
    }

    /**
     * @return delivery cost
     */
    public double getDeliveryCost() {
        return deliveryCost;
    }

    /**
     * @return quantity bought
     */
    public int getQuantityBought() {
        return quantityBought;
    }

    /**
     * @return company name + " " + depot id
     */
    public String getBoughtBy() {
        return boughtBy;
    }

    /**
     * @return company name + " " + depot id
     */
    public String getSoldBy() {
        return soldBy;
    }

    /**
     * @return total products cost + delivery cost
     */
    public double getTotalCost() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return Double.valueOf(df.format(deliveryCost + productsCost));
    }

    /**
     * @return type of product for transaction
     */
    public String getProductType() {
        return productType;
    }

    @Override
    public String toString() {
        return "Transaction{" + "transactionId=" + transactionId + ", productType=" + productType + ", totalCost=" + getTotalCost() + ", productsCost=" + productsCost + ", deliveryCost=" + deliveryCost + ", quantityBought=" + quantityBought + ", boughtBy=" + boughtBy + ", soldBy=" + soldBy + '}';
    }
    
    
    
}
