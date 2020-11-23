
import java.util.HashSet;
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
public class PlayerCharacter implements DatabaseObject {
    private static HashSet<Integer> takenIds = new HashSet();
    
    private int id; // player character id
    private int level;
    private int userId; // Id of the owning user account
    
    public PlayerCharacter(int _id, int _level, int _userId) {
        this.id = _id;
        this.level = _level;
        this.userId = _userId;
    }

    @Override
    public String toString() {
        return this.id + ",\t" +this.level + ",\t" + this.userId;
    }
    
    @Override
    public String toSql() {
        return "'" + this.id + "', '" +this.level + "', '" + this.userId + "'";
    }
    
    public static DatabaseObject parse(String _str) {
        String[] data = _str.replaceAll("\\s+","").split(",");
        int id = Integer.parseInt(data[0]);
        int level = Integer.parseInt(data[1]);
        int userId = Integer.parseInt(data[2]);
        return new PlayerCharacter(id, level, userId);
    }
    
    public static PlayerCharacter randomPlayerCharacter() {
        Random rand = new Random();
        int id;
        do {
            id = rand.nextInt(65536);
        } while (takenIds.contains(id));
        takenIds.add(id);
        int level = rand.nextInt(100)+1;
        int userId = User.getRandomTakenId();
        return new PlayerCharacter(id, level, userId);
    }
}
