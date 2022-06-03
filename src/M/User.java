package M;

import java.util.ArrayList;

public class User {
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();

    private String username;
    private int money;
    private String introducerName = null;
    private int invites = 0;

    public User(String username, int money) {
        this.username = username;
        this.money = money;

        usernames.add(username);
        users.add(this);
    }

    public User(String username, int money, String introducerName) {
        this.username = username;
        this.money = money;
        this.introducerName = introducerName;

        usernames.add(username);
        users.add(this);
    }

    public int getInvites() {
        return invites;
    }

    public void setInvites(int invites) {
        this.invites = invites;
    }

    public String getIntroducerName() {
        return introducerName;
    }

    public void setIntroducerName(String introducerName) {
        this.introducerName = introducerName;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    public static void setUsernames(ArrayList<String> usernames) {
        User.usernames = usernames;
    }

    public static User findUserByName(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

}
