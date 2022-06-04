package V;

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
