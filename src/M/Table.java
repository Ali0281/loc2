package M;

import java.util.ArrayList;

public class Table {
    private int level;
    private int size;
    private ArrayList<User> users = new ArrayList<>();

    public Table(int level) {
        this.level = level;
        size = level == 0 ? 1 : level * level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
