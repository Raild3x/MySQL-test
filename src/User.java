
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Logan
 */
public class User implements DatabaseObject {
    private static String[] names = new String[]{"Logan", "Alex", "Taylor", "Michael", "Meghan", "Dana", "Liam", "Ben", "Jackson", "David", "Christine", "Grace", "Cole"};
    private static HashSet<Integer> takenIds = new HashSet();  
            
    private int id;
    private String name;
    private int age;
    private char sex;
    
    public User(int _id, String _name, int _age, char _sex) {
        this.id = _id;
        this.name = _name;
        this.age = _age;
        this.sex = _sex;
    }
    
    public User(int _id, String _name, int _age, String _sex) {
        this.id = _id;
        this.name = _name;
        this.age = _age;
        this.sex = _sex.charAt(0);
    }
    
    @Override
    public String toString() {
        return this.id + ",\t" +this.name + ",\t" + this.age + ",\t" + this.sex;
    }
    
    public String toSql() {
        return "'" + this.id + "', '" +this.name + "', '" + this.age + "', '" + this.sex + "'";
    }
    
    public static DatabaseObject parse(String _str) {
        String[] data = _str.replaceAll("\\s+","").split(",");
        int id = Integer.parseInt(data[0]);
        String name = data[1];
        int age = Integer.parseInt(data[2]);
        char sex = data[3].charAt(0);
        return new User(id, name, age, sex);
    }
    
    public static User randomUser() {
        Random rand = new Random();
        int id;
        do {
            id = rand.nextInt(65536);
        } while (takenIds.contains(id));
        takenIds.add(id);
        String name = names[rand.nextInt(names.length)];
        int age = rand.nextInt(100)+1;
        char sex = rand.nextInt(2) == 1 ? 'M' : 'F';
        return new User(id, name, age, sex);
    }
    
    public static <E> E getRandomTakenId(){
        /*
         * Generate a random number using nextInt
         * method of the Random class.
         */
        Random random = new Random();
        
        //this will generate a random number between 0 and HashSet.size - 1
        int randomNumber = random.nextInt(takenIds.size());
 
        //get an iterator
        Iterator<? extends E> iterator = (Iterator<? extends E>) takenIds.iterator();
        
        int currentIndex = 0;
        E randomElement = null;
        
        //iterate the HashSet
        while(iterator.hasNext()){            
            randomElement = iterator.next();
            
            //if current index is equal to random number
            if(currentIndex == randomNumber)
                return randomElement;            
            
            //increase the current index
            currentIndex++;
        }
        
        return randomElement;
    }    
    
    // GETTERS
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public char getSex() {
        return this.sex;
    }
}
