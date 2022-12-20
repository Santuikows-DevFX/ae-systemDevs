package main.products;

public class productsInv { //encapsulate data

    private String product_name, product_category ; //private variables
    private int product_quantity;
    private java.sql.Date product_exp;
    private java.sql.Date date_added;
    private long days_left;

    //mutators

    public void setProductName(String product_name) { 

        this.product_name = product_name;

    }

    public void setProductCategory(String product_category) { 

        this.product_category = product_category;
    }

    public void setProductQuantity(int product_quantity) {

        this.product_quantity = product_quantity;

    }

    public void setDateAdded(java.sql.Date date_added) { 

        this.date_added = date_added;

    }

    public void setProductExpiration(java.sql.Date product_exp) { 

        this.product_exp = product_exp;

    }

    public void setDaysLeft(long days_left) {

        this.days_left = days_left;
    }

    //accessors

    public String getProductName() {

        return product_name;
    }

    public int getProductQuantity() {

        return product_quantity;
    }

    public java.sql.Date getDateAdded() { 

        return date_added;
    }

    public java.sql.Date getProductExpirationDate() {

        return product_exp;

    }

    public String getProductCategory() {

        return product_category;
    }

    public long getDaysLeft() {

        return days_left;
    }
    
}
