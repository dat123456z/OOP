package model;
public class ExpenseManagerModel {
    private String category;
    private String description;
    private double amount;
    private java.sql.Date date; // Use java.sql.Date for database compatibility

    public ExpenseManagerModel(String category, String description, double amount, java.sql.Date date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

public void printInfo() {
    System.out.println("Category: " + category);
    System.out.println("Description: " + description);
    System.out.println("Amount: " + amount);
    System.out.println("Date: " + date);
}

    
}