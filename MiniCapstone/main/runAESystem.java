package main;

import main.database.config;
import main.products.productsInv;
import main.storeMethods.absMethods;
import java.util.Scanner;

import java.sql.*;

// import java.text.SimpleDateFormat;

public class runAESystem extends absMethods { //goal to make kinda reallistic by implementing loading using thread.sleep

    //global scanner
    static Scanner sc = new Scanner(System.in);

    //global class
    config aeDataBase;
    productsInv productsInv;
    Connection conn;

    //global variables
    boolean isValid = false;
    // int productID;
    String productID;
    String productName;

    public static void main(String[] args) {

        //instance of this class
        runAESystem runSystem = new runAESystem();
        runSystem.selectTask(); // method that will start the system
        
    }

    @Override
    public void selectTask() { //method that handles the selection choice

        System.out.println();

        isValid = false;

        System.out.println("\t\t\t\t\t\t\t\t\t\t  == HELLO ADMIN! ==");
        System.out.println("\t\t\t\t\t\t\t\t     == AE SYSTEM: AUTOMATED INVENTORY SYSTEM ==");
        System.out.println("\t\t\t\t\t\t\t       == Developed By: Bana-ag, Canlas, Tablante, Santuico ==\n");

        do{ //validation loop if the user inputs invalid option.

        System.out.println("\t\t\t\t\t\t\t\t             == What do you want to do? ==\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Add Products[1]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Update Product Quantity[2]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > View Overall Products[3]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > View Product Expiry Only[4]\n\n");
	
        System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
        String selectionChoice = sc.nextLine(); //handles the storage for the admin choice

        if(selectionChoice.equals("1")) { //conditional statement for the admin choice

            isValid = true;
            addItems(); //add items function

        }else if(selectionChoice.equals("2")) { 

            isValid = true;
            editQnt(); //edit products quantity with this function
            

        }else if(selectionChoice.equals("3")) { 

            isValid = true;
            viewOverAllItems(); //view the table with this function

        }else if(selectionChoice.equals("4")) { 

            isValid = true;
            viewItemsExpiry(); //function for viewing items expiry
            
        }else { 

            isValid = false;
            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n"); //for validation purposes

        }

        }while(isValid == false);
    }

    @Override
    public void addItems() { //adding items function

        System.out.println();
                System.out.println("=====================================================================================================================================================================================\n");
        System.out.println("\t\t\t\t\t\t\t\t                  == ADD PRODUCTS ==");

        try {

            aeDataBase = new config();
            aeDataBase.getConnection(); //establishing a connection to our data base
            
            productsInv = new productsInv(); //instance of the class of the class that is being encapsulated.

        
        do{ //loop for user validation if the user inputs invalid data.
            try {
                
                System.out.println("\t\t\t\t\t\t\t\t\t            PRODUCT INFO: \n");
                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Category (Ex. Frozen, Canned, etc)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                String productCategory = sc.nextLine().toUpperCase();

                productsInv.setProductCategory(productCategory);

                System.out.println();

                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Name (Ex. Tender Juicy Hotdog)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                productName = sc.nextLine().toUpperCase();

                productsInv.setProductName(productName); //calling setter method to set product name

                System.out.println();
                    
                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Quantity (Ex. 20)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                String productQnt = sc.nextLine();

                productsInv.setProductQuantity(productQnt); //calling setter method to set product quantity

                System.out.println();

                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Expiration Date in this format (YYYY-MM-DD)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                String productExp = sc.nextLine();

                java.sql.Date prodExp = java.sql.Date.valueOf(productExp); //converting the string value into a util.Date

                productsInv.setProductExpiration(prodExp); //calling setter method to set product expiration

                System.out.println();

                long millis = System.currentTimeMillis(); //getting the time and storing it into a long variable
                java.sql.Date dateAdded = new java.sql.Date(millis); //using java.sql.date we can get the date today, this will serve as the date where a product has been inserted.

                productsInv.setDateAdded(dateAdded); //calling the setter method to set the date added.
            
                long days = compareDates(productsInv.getDateAdded(), productsInv.getProductExpirationDate()) + 1; //method for getting the days by comparing the two dates
            
                productsInv.setDaysLeft(days);

                postMethod(); //calling the post function

                
            } catch (Exception e) {
                
                System.out.println();
                isValid = false;
                System.out.println("AEerror305: INVALID INPUT, TRY AGAIN!\n"); //validation purposes
            }

        }while(isValid == false);
            
        }catch (Exception e) {
        }
        
        
    }

    @Override
    public void editQnt() { //function for editing quantity

        aeDataBase = new config();
        conn = aeDataBase.getConnection(); //getting the connection from the data base

        try {
            PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM products"); //selecting from the table

		    ResultSet fetchProducts = selectQuery.executeQuery(); //fetching the products
            System.out.println();

            System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE ADDED", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");


            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_added"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        try {
        
        do{ //loop for user validation if the user inputs invalid data.
            try {

                System.out.println("\t\t\t\t\t\t\t\t                    EDIT QUANTITY: \n");
                System.out.print("\t\t\t\t\t\t\t\t           Enter the ID of the product: ");
                productID = sc.nextLine();

                System.out.println();
        
                PreparedStatement selectProduct = conn.prepareStatement("SELECT * FROM products WHERE product_id = '"+ productID +"' "); //selecting from the data base where the ID is equal to the admins id input, in order to display specific ID.
                ResultSet fetchID = selectProduct.executeQuery();

                
                    if(fetchID.isBeforeFirst()) {

                        System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE ADDED", "EXP DATE", "CATEGORY", "DAYS LEFT");
                        System.out.println();
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            
            
                        while(fetchID.next()) { //while it has a row to be fetch, display the row from the data base
            
                            System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchID.getInt("product_id") ,fetchID.getString("product_name"), fetchID.getInt("product_quantity"), fetchID.getDate("date_added"), fetchID.getString("product_exp"), fetchID.getString("category"), fetchID.getInt("days_left"));
                            System.out.println();
                          
                            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        }
            
                    do{ //prompting the admin to choose between these options

                        System.out.println();
                        System.out.println("\t\t\t\t\t\t\t\t                 What do you want to do?\n");
                        System.out.println("\t\t\t\t\t\t\t\t               > Add Quantity[1]\n");
                        System.out.println("\t\t\t\t\t\t\t\t               > Deduct Quantity[2]\n");
                        System.out.println("\t\t\t\t\t\t\t\t               > Exit[3]\n");
                
                        System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                        String choice = sc.nextLine();
            
                        if(choice.equals("1")) { 
                
                            isValid = true;
                            addQuantity();
                           
                
                        }else if(choice.equals("2")) { 
                
                            isValid = true;
                            subtractQuantity();
                            
                        }else if(choice.equals("3")) {
            
                            isValid = true;
                            selectTask();
                            System.out.println();
            
                        }else { 
                
                            isValid = false;
                            System.out.println();
                            System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");
                
                        }
                
                        }while(isValid == false);

                    }else {

                        isValid = false;
                        System.out.println("\t\t\t\t\t\t\t\t        AEerror305: INVALID INPUT, NO ID FOUND!!");
                        System.out.println();
    

                    }                

            } catch (Exception e) {
                
                System.out.println();
                isValid = false;
                System.out.println("AEerror305: INVALID INPUT, TRY AGAIN!\n");
            }

        }while(isValid == false);
            
        }catch (Exception e) {
        }
      
      
    }

    @Override
    public void viewOverAllItems() { //function for viewing the entire table

        System.out.println();
        
        aeDataBase = new config();
        conn = aeDataBase.getConnection();

		try {
            PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM products"); //selecting from the entire table

		    ResultSet fetchProducts = selectQuery.executeQuery(); //fetching the products
            System.out.println();

            System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE ADDED", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");


            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_added"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        do{

            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t                 What to do now?\n");
            System.out.println("\t\t\t\t\t\t\t\t               > Select Category[1]\n");
            System.out.println("\t\t\t\t\t\t\t\t               > View Table[2]\n");
            System.out.println("\t\t\t\t\t\t\t\t               > Exit[3]\n");
    
            System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
            String choice = sc.nextLine();

            if(choice.equals("1")) { 
    
                isValid = true;
                selectCategory();
               
    
            }else if(choice.equals("2")) { 
    
                isValid = true;
                viewOverAllItems();
                
            }else if(choice.equals("3")) {

                isValid = true;
                selectTask();
                System.out.println();

            }else { 
    
                isValid = false;
                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");
    
            }
    
            }while(isValid == false);

    }

    @Override
    public void viewItemsExpiry() { // function for viewing the items expiry 

        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        try {
            
            PreparedStatement selectProducts = conn.prepareStatement("SELECT * FROM products");
            ResultSet fetchProducts = selectProducts.executeQuery();

            System.out.println();

            System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE ADDED", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");


            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_added"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }
            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t            What to do now?\n");
                System.out.println("\t\t\t\t\t\t\t\t             > Show near expiring products (< 90 days)[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t             > Exit[2]\n");
    
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();
        
                if(adminChoice.equals("1")) { 
        
                    isValid = true;
                    show90DaysBelow();
        
                }else if(adminChoice.equals("2")) { 

                    isValid = true;
                    selectTask();
        
                }else { 
        
                    isValid = false;
                    System.out.println();
                    System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");
        
                }
        
                }while(isValid == false);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        
    }

    @Override
    public void exitSystem() { //function for exiting the system and ending the program
        
        
    }

    @Override
    public void postMethod() { //equivalent of $_POST in PHP, to post the data into the table.

        try {
            conn = aeDataBase.getConnection(); //connecting to db

            PreparedStatement checkNames = conn.prepareStatement("SELECT * FROM products WHERE product_name = ('"+ productsInv.getProductName() + "')");
            ResultSet check = checkNames.executeQuery();

            if(check.isBeforeFirst()) {

                isValid = false;
                System.out.println("\t\t\t\t\t\t\t\t          AEerror305: CANNOT ADD SAME PRODUCT!\n");

            }else { 
    
			PreparedStatement insertQuery = conn.prepareStatement( //making a query for inserting a product into the table

            "INSERT INTO products (product_name, product_quantity, product_exp, date_added, category, days_left) VALUES ('" + productsInv.getProductName()  + "', '" + productsInv.getProductQuantity() + "', '"+ productsInv.getProductExpirationDate() + "', '"+ productsInv.getDateAdded() +"', '"+ productsInv.getProductCategory() + "', '" + productsInv.getDaysLeft() + "')"); //inserting products into db

            insertQuery.executeUpdate(); //update the date base

            System.out.println("\t\t\t\t\t\t\t\t            ITEM INSERTED INTO THE TABLE.\n"); //updating the user that his/herp product is added into the db

            }

		} catch (Exception e) {

		}
        
        do{

		System.out.println("\t\t\t\t\t\t\t\t            What to do now?\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Insert More[1]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Exit[2]\n");

        System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
        String choice = sc.nextLine();

        if(choice.equals("1")) { 

            isValid = true;
            addItems();

        }else if(choice.equals("2")) { 

            isValid = true;
            selectTask();
            System.out.println();

        }else { 

            isValid = false;
            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");

        }

        }while(isValid == false);
        
    }

    @Override
    public void selectCategory() {

        System.out.println();
        
        System.out.print("\t\t\t\t\t\t\t\t        Enter the category: ");
        String getCategory = sc.nextLine().toUpperCase();

        try {

            PreparedStatement selectCategoryQuery = conn.prepareStatement("SELECT * FROM products WHERE category = ('"+ getCategory +"')"); //selecting from the table

            ResultSet fetchCategory = selectCategoryQuery.executeQuery(); //fetching the products
            System.out.println();

            if(fetchCategory.isBeforeFirst()) { 

                System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE ADDED", "EXP DATE", "CATEGORY", "DAYS LEFT");
                System.out.println();
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
    
    
                while(fetchCategory.next()) { //while it has a row to be fetch, display the row from the data base
    
                    System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchCategory.getInt("product_id") ,fetchCategory.getString("product_name"), fetchCategory.getInt("product_quantity"), fetchCategory.getDate("date_added"), fetchCategory.getString("product_exp"), fetchCategory.getString("category"), fetchCategory.getInt("days_left"));
                    System.out.println();
                  
                    System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                }

                do{

                    System.out.println();
                    System.out.println("\t\t\t\t\t\t\t\t            What to do now?\n");
                    System.out.println("\t\t\t\t\t\t\t\t             > Search Another Category[1]\n");
                    System.out.println("\t\t\t\t\t\t\t\t             > View Table[2]\n");
                    System.out.println("\t\t\t\t\t\t\t\t             > Exit[3]\n");
            
                    System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                    String adminChoice = sc.nextLine();
            
                    if(adminChoice.equals("1")) { 
            
                        isValid = true;
                        selectCategory();
            
                    }else if(adminChoice.equals("2")) { 
    
                        isValid = true;
                        viewOverAllItems();
            
                    }else if(adminChoice.equals("3")){
    
                        isValid = true;
                        selectTask();
                        System.out.println();
    
                    }else { 
            
                        isValid = false;
                        System.out.println();
                        System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");
            
                    }
            
                    }while(isValid == false);
    
            }else {

                isValid = false;
                System.out.println("\t\t\t\t\t\t\t           AEerror305: INVALID INPUT, NONE EXISTING CATEGORY!\n");

            }

            
        } catch (SQLException e) {

            e.printStackTrace();
        }
        
    }

    @Override
    public void addQuantity() {
        
        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        System.out.println();
        System.out.print("\t\t\t\t\t\t\t\t                Quantity to add: ");
        int updateAddQnt = sc.nextInt();

        try {
            
            Statement state = conn.createStatement();
            String selectQuery = "SELECT product_quantity FROM products WHERE product_id = '"+ productID +"'";

            ResultSet queryRes = state.executeQuery(selectQuery);
            queryRes.next();

            int prevQuantity = queryRes.getInt("product_quantity");
            int updatedQuantity = prevQuantity + updateAddQnt;
            
            String updateQuery = "UPDATE products SET product_quantity = '"+ updatedQuantity +"' WHERE product_id = '"+ productID +"'";
            state.executeUpdate(updateQuery);

            System.out.println();
            System.out.print("\t\t\t\t\t\t\t\t                QUANTITY UPDATED!\n");

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t      Do you still want to manage product quantity?\n");
                System.out.println("\t\t\t\t\t\t\t\t                > Yes[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t                > No[2]\n");
                System.out.print("\t\t\t\t\t\t\t\t       Enter based on corresponding number:  ");
                String choice = sc.nextLine();

                if(choice.equals("1")) {

                    isValid = true;
                    editQnt();

                }else if(choice.equals("2")) {

                    isValid = true;
                    selectTask();
                    System.out.println();
                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");

                }

            }while(isValid == false);


        } catch (SQLException e) {
            
            e.printStackTrace();
        } 
        
    }

    @Override
    public void subtractQuantity() {
        
        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        System.out.println();
        System.out.print("\t\t\t\t\t\t\t\t                Quantity to deduct: ");
        int updateAddQnt = sc.nextInt();

        try {
            
            Statement state = conn.createStatement();
            String selectQuery = "SELECT product_quantity FROM products WHERE product_id = '"+ productID +"'";

            ResultSet queryRes = state.executeQuery(selectQuery);
            queryRes.next();

            int prevQuantity = queryRes.getInt("product_quantity");
            int updatedQuantity = prevQuantity - updateAddQnt;
            
            String updateQuery = "UPDATE products SET product_quantity = '"+ updatedQuantity +"' WHERE product_id = '"+ productID +"'";
            state.executeUpdate(updateQuery);

            System.out.println();
            System.out.print("\t\t\t\t\t\t\t\t                QUANTITY UPDATED!\n");

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t      Do you still want to manage product quantity?\n");
                System.out.println("\t\t\t\t\t\t\t\t                > Yes[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t                > No[2]\n");
                System.out.print("\t\t\t\t\t\t\t\t       Enter based on corresponding number:  ");
                String choice = sc.next();

                if(choice.equals("1")) {

                    isValid = true;
                    editQnt();

                }else if(choice.equals("2")) {

                    isValid = true;
                    selectTask();
                    System.out.println();
                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");

                }

            }while(isValid == false);


        } catch (SQLException e) {
            
            e.printStackTrace();
        } 
        
    }

    @Override
    public long compareDates(java.sql.Date dateAdded, java.sql.Date expiryDate) {

        long daysDifference = (dateAdded.getTime() - expiryDate.getTime()) / 86400000;
		return Math.abs(daysDifference);
    }

    @Override
    public void show90DaysBelow() {
        
        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        try {

            int specificExpiryDate = 90; //3 months

            PreparedStatement selectProducts = conn.prepareStatement("SELECT * FROM products WHERE days_left < ('" + specificExpiryDate +"')");
            ResultSet fetchProducts = selectProducts.executeQuery();

            System.out.println();

            System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
            System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %20s%15s%15s%15s", " ID" ,"      PRODUCT NAME" , "EXP DATE", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");

            while(fetchProducts.next()) { //while it has a row to be fetch

                System.out.printf("\t\t\t\t\t   %20s%16s%17s%11s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getString("product_exp"), fetchProducts.getInt("days_left"));
                System.out.println();
            
                System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");
            }

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t            What to do now?\n");
                System.out.println("\t\t\t\t\t\t\t\t             > Back to prev. table[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t             > Back to home[2]\n");
        
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();
        
                if(adminChoice.equals("1")) { 
        
                    isValid = true;
                    viewItemsExpiry();
        
                }else if(adminChoice.equals("2")) { 

                    isValid = true;
                    selectTask();
        
                }else { 
        
                    isValid = false;
                    System.out.println();
                    System.out.println("\t\t\t\t\t\t\t\t          AEerror305: INVALID INPUT, TRY AGAIN!\n");
        
                }
        
                }while(isValid == false);

        } catch (Exception e) {
            
        }
        
    }



}