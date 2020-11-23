

// Java program to illustrate  
// Connecting to the Database 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.InvocationTargetException;
import java.sql.*; 
import java.util.ArrayList;
  
public class DatabaseConnection { 
    private static String filePath = "C:\\Users\\Logan\\Documents\\NetBeansProjects\\CSC471V2\\";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String user = "root";
    private static String password = "cheese22";
    private String tableName = "user";
    private String url;
    
    public DatabaseConnection(String _url) {
        this.url = _url;
    }
    
    public void setTable(String _tableName) {
        this.tableName = _tableName;
    }
    
    public void connect() {
        try { 
            Class.forName(this.driver);

            // Establishing Connection 
            Connection con = DriverManager.getConnection(url, user, password); 
  
            if (con != null)              
                System.out.println("CONNECTED");            
            else            
                System.out.println("NOT CONNECTED"); 
              
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    }
    
    public void insert(DatabaseObject obj) {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
              
            // Inserting data in database 
            String q1 = "insert into " + this.tableName + " values("+ obj.toSql() +")"; 
            int x = stmt.executeUpdate(q1); 
            if (x > 0) {          
                //System.out.println("Successfully Inserted");             
            } else {            
                System.out.println("Insert Failed to table: "+this.tableName); 
            }
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    }
    
    public void update(String id, String newAge) {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
          
            // Updating database 
            String q1 = "UPDATE " + this.tableName + " set age = '" + newAge +  
                    "' WHERE id = '" +id+ "'"; 
            int x = stmt.executeUpdate(q1); 
              
            if (x > 0)             
                System.out.println("Age Successfully Updated");             
            else            
                System.out.println("ERROR OCCURED UPDATING "+this.tableName); 
              
            con.close(); 
        } 
        catch(Exception e){ 
            System.out.println(e); 
        } 
    }
    
    public void clear() {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
                   
            // Delete all records in the table
            String q1 = "DELETE from " + this.tableName + ";"; 
                      
            int x = stmt.executeUpdate(q1); 
            if (x > 0)             
                System.out.println("All users successfully deleted.");             
            else
                System.out.println("ERROR OCCURED CLEARING "+this.tableName);   
            
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    }
    
    public void drop(String tblName) {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
                   
            // Delete all records in the table
            String q1 = "DROP TABLE " + tblName + ";"; 
                      
            int x = stmt.executeUpdate(q1); 
            if (x > 0)             
                System.out.println(tblName+" table has been successfully dropped.");             
            else
                System.out.println("ERROR OCCURED WHEN DROPPING "+tblName);   
            
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    } 
    
    public void delete(String id) {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
                   
            // Deleting from database 
            String q1 = "DELETE from " + this.tableName + " WHERE id = '" + id + "';"; 
                      
            int x = stmt.executeUpdate(q1); 
            if (x > 0)             
                System.out.println("One User Successfully Deleted");             
            else
                System.out.println("ERROR OCCURED :(");   
            
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    }
    
    public void select(String id) {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
            
            // SELECT query 
            String q1 = "select * from " + this.tableName + " WHERE " + this.tableName + "id = '" + id + "';"; 
            ResultSet rs = stmt.executeQuery(q1);
            if (rs.next()) {
                printResultSet(rs);
            } else { 
                System.out.println("No such user id is already registered"); 
            } 
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    }
    
    public void RunSqlCommand(String cmd) {
        try { 
            Class.forName(this.driver); 
            Connection con = DriverManager.getConnection(this.url, this.user, this.password); 
            Statement stmt = con.createStatement(); 
                      
            ResultSet rs = stmt.executeQuery(cmd); 
            if (rs.next()) { 
                System.out.println(rs.getString(1));
            } else { 
                System.out.println("No such user id is already registered"); 
            } 
            
            con.close(); 
        } catch(Exception e) { 
            System.out.println(e); 
        } 
    }
    
    // Parses the text in the file into an arraylist of strings
    public ArrayList<String> getFileData(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        ArrayList<String> dataSet = new ArrayList<>();
        String str;
        while ((str = br.readLine()) != null){
            dataSet.add(str);
        }
        return dataSet;
    }
    
    // This method is imperfect (not modular)
    // Takes an arraylist of strings and converts them into a usable table of respective database objects
    public DatabaseObject[] convertTextToObjects(ArrayList<String> dataSet) throws Exception {
        System.out.println("Converting array of Text to Objects");
        DatabaseObject[] users = new DatabaseObject[dataSet.size()];
        for (int i = 0; i < dataSet.size(); i++) {
            System.out.println(dataSet.get(i));
            users[i] = (DatabaseObject) Class.forName(this.tableName).getMethod("parse", String.class).invoke(null, dataSet.get(i));
        }
        return users;
    }
    
    
    // Inserts all data from a file into the database
    public void loadFileData(String fileName) throws Exception {
        ArrayList<String> dataSet = this.getFileData(filePath + fileName);
        DatabaseObject[] users = this.convertTextToObjects(dataSet);
        int i = 0;
        for (DatabaseObject user: users) {
            i++;
            System.out.println("Inserting "+i+"/2000");
            this.insert(user);//user.getId(), user.getName(), user.getAge(), user.getSex());
        }
    }
    
    public void createUserTable() {
         try (Connection con = DriverManager.getConnection(this.url, this.user, this.password)) {
            boolean tblExists = false;
            if (!tblExists) {
                String createSQL = "create table user(userid BIGINT NOT NULL PRIMARY KEY, name varchar(30), age TINYINT, sex VARCHAR(10));";
                PreparedStatement create = con.prepareStatement(createSQL);
                create.executeUpdate();
                tblExists = true;
                // create marker
                System.out.println("Table 'User' was created");
                con.close();

            } else {
                System.out.println("Table 'User' already exists");
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createPlayerCharacterTable() {
         try (Connection con = DriverManager.getConnection(this.url, this.user, this.password)) {
            boolean tblExists = false;
            if (!tblExists) {
                String createSQL = "create table playercharacter(playercharacterid BIGINT NOT NULL PRIMARY KEY, level INT, userid BIGINT, FOREIGN KEY (userid) REFERENCES user(userid));";
                PreparedStatement create = con.prepareStatement(createSQL);
                create.executeUpdate();
                tblExists = true;
                // create marker
                System.out.println("Table 'PlayerCharacter' was created");
                con.close();

            } else {
                System.out.println("Table 'PlayerCharacter' already exists");
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void joinTables() {
        System.out.println("Attempting to join tables");
        try (Connection con = DriverManager.getConnection(this.url, this.user, this.password)) {
            Statement stmt = con.createStatement();
            
            String query =  "SELECT * FROM user INNER JOIN playercharacter ON user.userid=playercharacter.userid WHERE user.age=playercharacter.level;";
            long startTime = System.currentTimeMillis();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("UnIndexed Execution Time: "+(System.currentTimeMillis()-startTime)+" milliseconds");
            
            
            String indexQuery =  "CREATE INDEX age_index ON user (age)";
            stmt.executeUpdate(indexQuery);
            
            startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(query);
            System.out.println("Indexed Execution Time: "+(System.currentTimeMillis()-startTime)+" milliseconds");
            System.out.println("Joined tables");
            
            String dropIndexQuery =  "DROP INDEX age_index ON user";
            stmt.executeUpdate(dropIndexQuery);
            
            //===============================================================================================//
            System.out.println("Test creating view");
            
            String viewName = "AgeEqualsLevel";
            query =  "CREATE VIEW "+viewName+" AS "
                    + "SELECT user.userid, user.age, user.sex, playercharacter.playercharacterid, playercharacter.level "
                    + "FROM user, playercharacter "
                    + "WHERE user.userid=playercharacter.userid AND user.age=playercharacter.level;";
            stmt.executeUpdate(query);
            
            this.printTable(viewName);
            query = "UPDATE "+viewName+" SET sex='M' WHERE age%2=0;";
            startTime = System.currentTimeMillis();
            stmt.executeUpdate(query);
            System.out.println("InView Execution Time: "+(System.currentTimeMillis()-startTime)+" milliseconds");
            this.printTable(viewName);
            
            query =  "DROP VIEW AgeEqualsLevel;";
            stmt.executeUpdate(query);
            
            con.close();

        } catch (SQLException ex) {
            System.out.println(ex);
            //Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void printResultSet(ResultSet rs) throws SQLException {
        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String s = "";
            for (int i = 1; i <= cols; i++) {
                s += (rs.getMetaData().getColumnName(i) + " : " + rs.getString(i) + "\t"); 
            }
            System.out.println(s);
        }
    }
    
    public void printTable(String tblName) {
        try (Connection con = DriverManager.getConnection(this.url, this.user, this.password)) {
            Statement stmt = con.createStatement();
            System.out.println("Printing Table: "+tblName);
            printResultSet(stmt.executeQuery("SELECT * FROM "+tblName+";"));
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            //Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} 
