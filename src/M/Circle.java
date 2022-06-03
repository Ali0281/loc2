package M;

import java.util.ArrayList;

public class Circle {
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


    public void giveMoney(int money, int level) {
        for (int i = 0; i < level; i++) {
            int thisLevelMoney = money / level / tables.get(i).getUsers().size();
            for (User user : tables.get(i).getUsers()) {
                user.setMoney(user.getMoney() + thisLevelMoney);
            }
        }
    }
}
