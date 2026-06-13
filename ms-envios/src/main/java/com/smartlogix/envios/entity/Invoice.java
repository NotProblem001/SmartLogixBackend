package com.smartlogix.envios.entity;

public class Invoice {
    private String id;
    private double amount;
    private String status;
    private String accountId;

    public Invoice() {
    }

    public Invoice(String id, double amount, String status, String accountId) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
