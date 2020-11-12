public class Constants {
    public static String OP = "\\(?";
    public static String CP = "\\)?";
    public static String FLOAT = "-?\\d+(\\.\\d+)?";
    public static String LEADING_FLOAT = "^-?\\d+(\\.\\d+)?";
    public static String MAX = "max\\(\\-?\\d+(\\.\\d+)?,\\-?\\d+(\\.\\d+)?\\)";
    public static String MIN = "min\\(\\-?\\d+(\\.\\d+)?,\\-?\\d+(\\.\\d+)?\\)";
    public static String IF_ELSE = "(if\\(-?\\d+(\\.\\d+)?[><=]*?-?\\d+(\\.\\d+)??,-?\\d+(\\.\\d+)??,-?\\d+(\\.\\d+)?\\))((else\\(-?\\d+(\\.\\d+)?[><=]*-?\\d+(\\.\\d+)??,-?\\d+(\\.\\d+)??,-?\\d+(\\.\\d+)?\\))?)+";
    public static String OPERATOR = "[+\\-*\\/]";
    public static String ELSE = "else\\((-?\\d+(\\.\\d+)?)([><=]*)(-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)?,(-?\\d+(\\.\\d+)?)\\)";
    public static String IF = "if\\((-?\\d+(\\.\\d+)?)([><=]*)(-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)?,(-?\\d+(\\.\\d+)?)\\)";
    public static String ROOT_MATCHER = String.format("^(%s(%s|%s|%s|%s)%s)(%s(%s(%s|%s|%s|%s)%s))+$", OP, FLOAT, MIN, MAX, IF_ELSE,CP, OPERATOR, OP, FLOAT, MIN, MAX, IF_ELSE,CP);

}

