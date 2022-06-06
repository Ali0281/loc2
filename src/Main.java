import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.run();
    }

    public static class MenuController {
        DataBase dataBase = DataBase.getInstance();

        public String createTable(Matcher matcher) {
            String username = matcher.group("username");
            int money = Integer.parseInt(matcher.group("money"));
            if (dataBase.getCircle() != null) return "We already have a founder";
            if (money < 5000) return "Money is not enough";
            startCircle(username, money - 5000);
            dataBase.addToMyMoney(5000);
            return "You now own a table";
        }

        private void startCircle(String username, int money) {
            dataBase.setCircle(new Circle());
            dataBase.getCircle().createATable();
            dataBase.getCircle().getTables().get(0).getUsers().add(new User(username, money));
        }

        public String invite(Matcher matcher) {
            String sender = matcher.group("sender");
            User user1 = User.findUserByName(sender);
            if (user1 == null) return "bug";
            String receiver = matcher.group("receiver");
            int money = Integer.parseInt(matcher.group("money"));
            if (User.findUserByName(receiver) != null) return "Username already taken";
            int level = dataBase.getCircle().getLevel(user1);
            while (true) {
                level++;
                if (dataBase.getCircle().getTables().size() <= level) dataBase.getCircle().createATable();
                if (dataBase.getCircle().getTables().get(level).getSize() == dataBase.getCircle().getTables().get(level).getUsers().size())
                    continue;
                dataBase.getCircle().getTables().get(level).getUsers().add(new User(receiver, money * 20 / 100, sender));
                break;
            }

            dataBase.addToMyMoney(((float) money) * 15 / 100);

            user1.setMoney(user1.getMoney() + ((float) money) * 5 / 100);
            user1.setInvites(user1.getInvites() + 1);

            dataBase.addToCircleOwner(((float) money) * 10 / 100);

            dataBase.getCircle().giveMoney(((float) money) * 50 / 100, level);

            if (user1.getInvites() >= 5) {
                promote(user1);
                user1.setInvites(0);
            }

            return "User added successfully in level " + level;
        }

        private void promote(User user1) {
            int level = dataBase.getCircle().getLevel(user1);
            if (level == 0 || level == 1) return;

            Table lowTable = dataBase.getCircle().getTables().get(level);
            Table highTable = dataBase.getCircle().getTables().get(level - 1);

            if (highTable.getUsers().size() >= highTable.getSize()) {
                User worstUser = highTable.getUsers().get(0);
                for (User user : highTable.getUsers()) {
                    if (worstUser.getInvites() > user.getInvites()
                            || (worstUser.getInvites() == user.getInvites()
                            && worstUser.getMoney() >= user.getMoney())) worstUser = user;
                }
                lowTable.getUsers().add(worstUser);
                highTable.getUsers().remove(worstUser);

            }
            highTable.getUsers().add(user1);
            lowTable.getUsers().remove(user1);
        }

        public String join(Matcher matcher) {
            String username = matcher.group("username");
            int money = Integer.parseInt(matcher.group("money"));
            if (User.findUserByName(username) != null) return "Username already taken";
            int level = dataBase.getCircle().getTables().size();
            dataBase.getCircle().createATable();

            dataBase.getCircle().getTables().get(dataBase.getCircle().getTables().size() - 1).getUsers().add(new User(username, money * 15 / 100));
            dataBase.addToCircleOwner(((float) money) * 10 / 100);
            dataBase.addToMyMoney(((float) money) * 25 / 100);

            dataBase.getCircle().giveMoney(((float) money) * 50 / 100, level);

            return "User added successfully in level " + level;
        }

        public int getLevelsCount() {
            return dataBase.getCircle().getTables().size();
        }

        public int getUsersCount() {
            return User.getUsers().size();
        }

        public String getUsersInALevel(Matcher matcher) {
            int level = Integer.parseInt(matcher.group("level"));
            ArrayList<Table> tables = DataBase.getInstance().getCircle().getTables();
            if (tables.size() <= level) return "No_such_level_found";
            return String.valueOf(tables.get(level).getUsers().size());
        }

        public String getIntroducer(Matcher matcher) {
            String username = matcher.group("username");
            User user = User.findUserByName(username);
            if (user == null) return "No_such_user_found";
            String res = user.getIntroducerName();
            if (res == null) return "No_introducer";
            return res;
        }

        public String getFriends(Matcher matcher) {
            String res = "";
            String username = matcher.group("username");
            User user = User.findUserByName(username);
            if (user == null) return ("No_such_user_found");
            for (Table table : DataBase.getInstance().getCircle().getTables()) {
                if (!(table.getUsers().contains(user))) continue;
                if (table.getUsers().size() == 1) {
                    return ("No_friend");
                } else {
                    for (int i = 0; i < table.getUsers().size(); i++) {
                        if (i == table.getUsers().indexOf(user) - 1)
                            res += (table.getUsers().get(i).getUsername() + " ");
                        if (i == table.getUsers().indexOf(user) + 1)
                            res += (table.getUsers().get(i).getUsername());
                    }
                    if (table.getUsers().indexOf(user) == table.getUsers().size() - 1)
                        res += (table.getUsers().get(0).getUsername());
                    return res;
                }
            }
            return res;
        }

        public String getCredit(Matcher matcher) {
            String username = matcher.group("username");
            User user = User.findUserByName(username);
            if (user == null) return "No_such_user_found";
            return String.valueOf((int) user.getMoney());
        }

        public String getUsersInSameLevel(Matcher matcher) {
            String res = "";

            String username = matcher.group("username");
            User user = User.findUserByName(username);
            if (user == null) return "No_such_user_found";
            for (Table table : dataBase.getCircle().getTables()) {
                if (!table.getUsers().contains(user)) continue;
                for (User temp : table.getUsers()) {
                    if (temp == user) continue;
                    res += temp.getUsername() + " ";
                }
                break;
            }
            if (res.equals("")) return "He_is_all_by_himself";
            return res;
        }

        public float getMyProfit() {
            return dataBase.getMyMoney();
        }
    }

    public static class Circle {
        private ArrayList<Table> tables = new ArrayList<>();

        public int getLevel(User user) {
            int level = 0;
            for (Table table : tables) {
                if (table.getUsers().contains(user)) return level;
                level++;
            }
            return -1;
        }

        public ArrayList<Table> getTables() {
            return tables;
        }

        public void setTables(ArrayList<Table> tables) {
            this.tables = tables;
        }

        public void createATable() {
            tables.add(new Table(tables.size()));
        }


        public void giveMoney(float money, int level) {
            for (int i = 0; i < level; i++) {
                float thisLevelMoney = money / level / tables.get(i).getUsers().size();
                for (User user : tables.get(i).getUsers()) {
                    user.setMoney(user.getMoney() + thisLevelMoney);
                }
            }
        }
    }

    public static class DataBase {
        private static DataBase instance;

        private float myMoney = 0;
        private Circle circle = null;

        private DataBase() {
        }

        public static DataBase getInstance() {
            if (instance == null) instance = new DataBase();
            return instance;
        }

        public float getMyMoney() {
            return myMoney;
        }

        public void setMyMoney(float myMoney) {
            this.myMoney = myMoney;
        }

        public Circle getCircle() {
            return circle;
        }

        public void setCircle(Circle circle) {
            this.circle = circle;
        }

        public void addToMyMoney(float amount) {
            myMoney += amount;
        }

        public void addToCircleOwner(float amount) {
            circle.getTables().get(0).getUsers().get(0).setMoney(circle.getTables().get(0).getUsers().get(0).getMoney() + amount);
        }
    }

    public static class Table {
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


    public static class User {
        private static ArrayList<String> usernames = new ArrayList<>();
        private static ArrayList<User> users = new ArrayList<>();

        private String username;
        private float money;
        private String introducerName = null;
        private int invites = 0;

        public User(String username, float money) {
            this.username = username;
            this.money = money;

            usernames.add(username);
            users.add(this);
        }

        public User(String username, float money, String introducerName) {
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

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }

    }

    public enum Commands {
        CREATE_TABLE("Create_a_table_for (?<username>\\S+) with_deposit_of (?<money>\\d+)"),
        INVITE("Invitation_request_from (?<sender>\\S+) for (?<receiver>\\S+) with_deposit_of (?<money>\\d+)"),
        JOIN("Join_request_for (?<username>\\S+) with_deposit_of (?<money>\\d+)"),
        LEVELS_COUNT("Number_of_levels"),
        USERS_COUNT("Number_of_users"),
        USERS_IN_A_LEVEL("Number_of_users_in_level (?<level>\\d+)"),
        GET_INTRODUCER("Introducer_of (?<username>\\S+)"),
        GET_FRIENDS("Friends_of (?<username>\\S+)"),
        GET_CREDIT("Credit_of (?<username>\\S+)"),
        GET_USERS_IN_SAME_LEVEL("Users_on_the_same_level_with (?<username>\\S+)"),
        GET_PROFIT("How_much_have_we_made_yet"),
        END("End"),
        ;


        private String regex;

        Commands(String regex) {
            this.regex = regex;
        }

        @Override
        public String toString() {
            return this.regex;
        }
    }

    public static class Menu {
        private final Scanner scanner = new Scanner(System.in);
        private String command;
        private Matcher matcher;
        private MenuController menuController = new MenuController();

        public void run() {
            while (true) {
                command = scanner.nextLine().trim();
                if ((matcher = getCommandMatcher(command, Commands.CREATE_TABLE.toString())) != null) {
                    System.out.println(menuController.createTable(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.INVITE.toString())) != null) {
                    System.out.println(menuController.invite(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.JOIN.toString())) != null) {
                    System.out.println(menuController.join(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.LEVELS_COUNT.toString())) != null) { // ok
                    System.out.println(menuController.getLevelsCount());
                } else if ((matcher = getCommandMatcher(command, Commands.USERS_COUNT.toString())) != null) { // ok
                    System.out.println(menuController.getUsersCount());
                } else if ((matcher = getCommandMatcher(command, Commands.USERS_IN_A_LEVEL.toString())) != null) {  // ok
                    System.out.println(menuController.getUsersInALevel(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.GET_INTRODUCER.toString())) != null) {  // ok
                    System.out.println(menuController.getIntroducer(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.GET_FRIENDS.toString())) != null) { // ok
                    System.out.println(menuController.getFriends(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.GET_CREDIT.toString())) != null) { // ok
                    System.out.println(menuController.getCredit(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.GET_USERS_IN_SAME_LEVEL.toString())) != null) { // ok
                    System.out.println(menuController.getUsersInSameLevel(matcher));
                } else if ((matcher = getCommandMatcher(command, Commands.GET_PROFIT.toString())) != null) { // ok
                    System.out.println((int) menuController.getMyProfit());
                } else if ((matcher = getCommandMatcher(command, Commands.END.toString())) != null) { // ok
                    break;
                } else {
                    System.out.println("invalid!");
                }
            }
        }

        protected Matcher getCommandMatcher(String input, String regex) {
            Matcher matcher = Pattern.compile(regex).matcher(input);

            if (matcher.matches())
                return matcher;

            return null;
        }
    }
}
