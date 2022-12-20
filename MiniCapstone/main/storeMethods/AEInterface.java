package main.storeMethods;

public interface AEInterface {

    public abstract void selectTask();

    public abstract void addItems();

    public abstract void editQnt();

    public abstract void viewOverAllItems(); 

    public abstract void viewItemsExpiry();;

    public abstract void exitSystem();

    public void postMethod();

    public void selectCategory();

    public void addQuantity();

    public void subtractQuantity();

    public long compareDates(java.sql.Date dateAdded, java.sql.Date expiryDate);

    public void show90DaysBelow();
    
}
