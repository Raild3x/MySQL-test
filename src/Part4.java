/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Logan
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Part4 {
    
    
    public static void createFile() {
        try {
            File file = new File("Part_4.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File name " + file.getName() + " already exists");
            }
        } catch (IOException e) {
            System.out.println("Error creating file");
            e.printStackTrace();
        }
    }
 
    public static void writeToFile() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Overwrite data in txt file? Y or N");
        char response = scan.next().charAt(0);
        boolean newTuple = true;
 
        if (Character.toLowerCase(response) == 'y') {
            newTuple = false;
        }
        while (true) {
            Scanner inputData = new Scanner(System.in);
 
            System.out.println("Enter ID (String)");
            String ID = inputData.nextLine();
            if (ID != null) {
                System.out.println("Enter sex (F or M)");
                char sex = Character.toUpperCase(inputData.next().charAt(0));
                if (sex == 'F' || sex == 'M') {
                    System.out.println("Enter age (int)");
                    int age = inputData.nextInt();
                    if (age >= 0) {
                        try {
                            FileWriter writer = new FileWriter("Part_4.txt", newTuple);
                            newTuple = true;
                            BufferedWriter out = new BufferedWriter(writer);
                            writer.write(ID + ", " + sex + ", " + age + "\n");
                            writer.close();
                            System.out.println("Successful write to file");
                        } catch (IOException ex) {
                            System.out.println("Error writing to file");
                            Logger.getLogger(Part4.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
 
            System.out.println("Continue? Y or N");
            Scanner inputResponse = new Scanner(System.in);
            response = inputResponse.next().charAt(0);
 
            if (Character.toLowerCase(response) == 'n') {
                inputData.close();
                inputResponse.close();
                break;
            }
        }
    }
    
    // NEW AND IMPROVED METHODS // ----------------------------------------------------------
    public static File createFile(String fileName) {
        System.out.println("Attempting to create a new file of name: "+fileName);
        try {
            File file = new File(fileName + ".txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File name " + file.getName() + " already exists");
                System.out.println("Would you like to clear the file? Y / N");
                Scanner scan = new Scanner(System.in);
                char response = Character.toLowerCase(scan.next().charAt(0));
                if (response == 'y') {
                    PrintWriter writer = new PrintWriter(file);
                    writer.print("");
                    writer.close();
                    System.out.println("Successfully cleared " + file.getName());
                }
            }
            return file;
        } catch (IOException e) {
            System.out.println("Error creating file");
            e.printStackTrace();
        }
        return null;
    }
 
    public static void writeToFile(File file, ArrayList<DatabaseObject> list) {
        boolean running = true;
        while (running) {
            try {
                FileWriter writer = new FileWriter(file.getName(), true);
                BufferedWriter out = new BufferedWriter(writer);
                for(int i = 0; i < list.size(); i++) {
                    writer.write(list.get(i).toString() + "\n");
                }
                writer.close();
                System.out.println("Successful write to file");
               
            } catch (IOException e) {
                System.out.println("Error writing to file");
                Logger.getLogger(Part4.class
                        .getName()).log(Level.SEVERE, null, e);
            }
            running = false;
        }
    }
    
    public static ArrayList<DatabaseObject> generateRandomUsers(int _amt) {
        ArrayList<DatabaseObject> list = new ArrayList<>();
        System.out.println("Generating "+_amt+" random users.");
        for (int i = 0; i < _amt; i++) {
            User user = User.randomUser();
            //System.out.println("Generated new user with id: "+user.getId());
            list.add(user);
        }
        return list;
    }
    
    public static ArrayList<DatabaseObject> generateRandomPlayerCharacters(int _amt) {
        ArrayList<DatabaseObject> list = new ArrayList<>();
        System.out.println("Generating "+_amt+" random users.");
        for (int i = 0; i < _amt; i++) {
            PlayerCharacter pc = PlayerCharacter.randomPlayerCharacter();
            //System.out.println("Generated new user with id: "+user.getId());
            list.add(pc);
        }
        return list;
    }
 
    public static void main(String[] args) throws Exception {
        
        ArrayList<DatabaseObject> list = generateRandomUsers(2000);
        File file = createFile("RandomUserSet");
        writeToFile(file, list);
        
        list = generateRandomPlayerCharacters(2000);
        file = createFile("RandomPlayerCharacterSet");
        writeToFile(file, list);
        
        System.out.println("Testing database");
        DatabaseConnection con = new DatabaseConnection("jdbc:mysql://localhost:3306/RandomDataset");
        
        
        //Destroy the old tables
        con.drop("playercharacter");
        con.drop("user");
        //Rebuild the tables
        con.createUserTable();
        con.createPlayerCharacterTable();
        
        con.setTable("User");
        //con.clear();
        con.loadFileData("RandomUserSet.txt");
        con.setTable("PlayerCharacter");
        //con.clear();
        con.loadFileData("RandomPlayerCharacterSet.txt");
        
        
        //con.printTable("user");
        con.joinTables();
        //con.setTable("User");
        //con.select("31814");
        //con.setTable("PlayerCharacter");
        //con.select("14641");
        //con.RunSqlCommand("SELECT * from user; INNERJOIN playercharacter ON user.");
        //con.RunSqlCommand("select * from user");
        /*con.RunSqlCommand("select count(*) from user");
        con.delete("32799");
        con.RunSqlCommand("select count(*) from user");*/
    }
}
