package M;

public class DataBase {
    private static DataBase instance;

    private int myMoney = 0;
    private Circle circle = null;

    private DataBase() {
    }

    public static DataBase getInstance() {
        if (instance == null) instance = new DataBase();
        return instance;
    }

    public int getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(int myMoney) {
        this.myMoney = myMoney;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void addToMyMoney(int amount) {
        myMoney += amount;
    }

    public void addToCircleOwner(int amount) {
        circle.getTables().get(0).getUsers().get(0).setMoney(circle.getTables().get(0).getUsers().get(0).getMoney() + amount);
    }
}
