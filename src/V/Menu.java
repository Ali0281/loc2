package V;

import C.MenuController;
import M.DataBase;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {
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
            } else if ((matcher = getCommandMatcher(command, Commands.LEVELS_COUNT.toString())) != null) {
                System.out.println(menuController.getLevelsCount());
            } else if ((matcher = getCommandMatcher(command, Commands.USERS_COUNT.toString())) != null) {
                System.out.println(menuController.getUsersCount());
            } else if ((matcher = getCommandMatcher(command, Commands.USERS_IN_A_LEVEL.toString())) != null) {
                System.out.println(menuController.getUsersInALevel(matcher));
            } else if ((matcher = getCommandMatcher(command, Commands.GET_INTRODUCER.toString())) != null) {
                System.out.println(menuController.getIntroducer(matcher));
            } else if ((matcher = getCommandMatcher(command, Commands.GET_FRIENDS.toString())) != null) {
                System.out.println(menuController.getFriends(matcher));
            } else if ((matcher = getCommandMatcher(command, Commands.GET_CREDIT.toString())) != null) {
                System.out.println(menuController.getCredit(matcher));
            } else if ((matcher = getCommandMatcher(command, Commands.GET_USERS_IN_SAME_LEVEL.toString())) != null) {
                System.out.println(menuController.getUsersInSameLevel(matcher));
            } else if ((matcher = getCommandMatcher(command, Commands.GET_PROFIT.toString())) != null) {
                System.out.println(menuController.getMyProfit());
            } else if ((matcher = getCommandMatcher(command, Commands.END.toString())) != null) {
                break;
            }else {
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
