package C;

import M.Circle;
import M.DataBase;
import M.Table;
import M.User;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class MenuController {
    DataBase dataBase = DataBase.getInstance();

    public String createTable(Matcher matcher) {
        String username = matcher.group("username");
        int money = Integer.parseInt(matcher.group("money"));
        if (dataBase.getCircle() != null) return "We already have a founder";
        if (money < 5000) return "Money is not enough";
        startCircle(username, money - 5000);
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
        String receiver = matcher.group("receiver");
        int money = Integer.parseInt(matcher.group("money"));
        if (User.findUserByName(receiver) != null) return "Username already taken";
        int level = dataBase.getCircle().getLevel(user1);
        while (true) {
            level++;
            if (dataBase.getCircle().getTables().size() < level + 1) dataBase.getCircle().createATable();
            if (dataBase.getCircle().getTables().get(level).getSize() == dataBase.getCircle().getTables().get(level).getUsers().size())
                continue;
            dataBase.getCircle().getTables().get(level).getUsers().add(new User(receiver, money * 20 / 100, sender));
            break;
        }

        dataBase.addToMyMoney(money * 15 / 100);

        user1.setMoney(user1.getMoney() + money * 5 / 100);
        user1.setInvites(user1.getInvites() + 1);

        dataBase.addToCircleOwner(money * 10 / 100);

        dataBase.getCircle().giveMoney(money * 50 / 100, level);

        /// invites!!


        return "User added successfully in level " + level;
    }

    public String join(Matcher matcher) {
        String username = matcher.group("username");
        int money = Integer.parseInt(matcher.group("money"));
        if (User.findUserByName(username) != null) return "Username already taken";
        int level = dataBase.getCircle().getTables().size();
        dataBase.getCircle().createATable();

        dataBase.getCircle().getTables().get(dataBase.getCircle().getTables().size() - 1).getUsers().add(new User(username, money * 15 / 100));
        dataBase.addToCircleOwner(money * 10 / 100);
        dataBase.addToMyMoney(money * 25 / 100);

        dataBase.getCircle().giveMoney(money * 50 / 100, level);

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
        if (tables.size() < level + 1) return "No_such_level_found";
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
        String username = matcher.group("username");
        User user = User.findUserByName(username);
        if (user == null) return "No_such_user_found";
        for (Table table : dataBase.getCircle().getTables()) {
            if (!table.getUsers().contains(user)) continue;
            String firstFriend = null;
            String secondFriend = null;

            int maxSize = table.getSize();
            int size = table.getUsers().size();
            int index = table.getUsers().indexOf(user) + 1;
            int previous = (index - 1 + maxSize) % maxSize;
            int next = (index + 1) % maxSize;

            if (!(size < previous)) firstFriend = table.getUsers().get(previous).getUsername();
            if (!(size < next)) secondFriend = table.getUsers().get(next).getUsername();
            if (firstFriend == null && secondFriend == null) return "No_friend";
            if (firstFriend == null) return secondFriend;
            if (secondFriend == null) return firstFriend;
            return firstFriend + " " + secondFriend;
        }
        return "bug";
    }

    public String getCredit(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.findUserByName(username);
        if (user == null) return "No_such_user_found";
        return String.valueOf(user.getMoney());
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

    public int getMyProfit() {
        return dataBase.getMyMoney();
    }
}
