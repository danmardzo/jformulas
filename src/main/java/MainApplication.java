import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApplication {


    public static void main(String[] args) throws Exception {
        System.out.println("Root matcher is " + Constants.ROOT_MATCHER);

        String complicatedFloat = "N32 * 4.5 + (3 - 3 / 6) - 4 / 2 ";

        System.out.println("Original statement is " + complicatedFloat);

        System.out.println("Postfix is " + infixToPostfix(complicatedFloat));

        System.out.println("Result is " + evaluatePostfixExpression(infixToPostfix(complicatedFloat)));

        String text = "-8 + min(-22,10)-max(12.2,45)+if(12<2,12.4,2.2)else(3>3,22,6)else(3>3,22,6)else(3=3,100,6)+2+if(12<2,12,22)else(3>3,22,6)else(3>3,22,6)else(3>3,22,6)*4-if(12>2,12,22)else(3>3,22,6)else(3>3,22,6)else(3>3,22,6)";

        text = evaluateMinMaxFunctions(text);

        text = evaluateIfStatements(text);

        System.out.println("The processed statement is " + text);

        System.out.println("PostFix is " + infixToPostfix(text));

        System.out.println("Solution : " + evaluatePostfixExpression(infixToPostfix(text)));

    }

    static String evaluateIfStatements(String exp) throws Exception {
        exp = exp.replace(" ", "");
        String original = exp;
        String ifElseRegex = String.format("(%s)", Constants.IF_ELSE);

        Pattern pattern = Pattern.compile(ifElseRegex);
        Matcher matcher = pattern.matcher(exp);
        List<String> ifElseMatches = new ArrayList<>();

        while (matcher.find()) {
            ifElseMatches.add(matcher.group(0));
        }

        float[] iResp = {0, 0};
        int replacements = 0;
        for (int i = 0; i < ifElseMatches.size(); i++) {
            iResp = evaluateConditionalStatement(ifElseMatches.get(i), true);

            if (iResp[0] == 1) {
                original = replaceString(original, String.valueOf(iResp[1]).replace("-", "N"), ifElseRegex);
                replacements += 1;
                continue;
            } else {
                List<String> elseStatements = getElseSegments(ifElseMatches.get(i), Constants.ELSE);
                for (int i1 = 0; i1 < elseStatements.size(); i1++) {
                    iResp = evaluateConditionalStatement(elseStatements.get(i1), false);
                    if (iResp[0] == 1) {
                        original = replaceString(original, String.valueOf(iResp[1]).replace("-", "N"), ifElseRegex);
                        replacements += 1;
                        break;
                    }
                }
            }

            if (replacements != ifElseMatches.size()) {
                original = replaceString(original, String.valueOf(iResp[1]).replace("-", "N"), ifElseRegex);
            }
        }

        return original;
    }

    static String evaluateMinMaxFunctions(String exp) {
        List<String> maxMatches = new ArrayList<>();
        List<String> minMatches = new ArrayList<>();
        exp = exp.replace(" ", "");
        String original = exp;
        String maxRegex = Constants.MAX;
        String maxSplitterRegex = "max\\((-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)\\)";
        String minRegex = Constants.MIN;
        String minSplitterRegex = "min\\((-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)\\)";

        Pattern maxSplitterPattern = Pattern.compile(maxSplitterRegex);
        Pattern minSplitterPattern = Pattern.compile(minSplitterRegex);

        Pattern maxPattern = Pattern.compile(maxRegex);
        Matcher maxMatcher = maxPattern.matcher(exp);
        Pattern minPattern = Pattern.compile(minRegex);
        Matcher minMatcher = minPattern.matcher(exp);

        while (maxMatcher.find()) {
            maxMatches.add(maxMatcher.group(0));
        }
        while (minMatcher.find()) {
            minMatches.add(minMatcher.group(0));
        }

        for (String current : maxMatches) {
            Matcher splitterMatcher = maxSplitterPattern.matcher(current);
            if (splitterMatcher.find()) {
                float x = Float.valueOf(splitterMatcher.group(1));
                float y = Float.valueOf(splitterMatcher.group(3));
                original = replaceString(original, String.valueOf(Math.max(x, y)).replace("-", "N"), maxRegex);
            }
        }
        for (String current : minMatches) {
            Matcher splitterMatcher = minSplitterPattern.matcher(current);
            if (splitterMatcher.find()) {
                float a = Float.valueOf(splitterMatcher.group(1));
                float b = Float.valueOf(splitterMatcher.group(3));
                original = replaceString(original, String.valueOf(Math.min(a, b)).replace("-", "N"), minRegex);
            }
        }

        return original;
    }

    static String replaceString(String input, String replacement, String ifElseRegex) {
        input = input.replace(" ", "");
        Pattern pattern = Pattern.compile(ifElseRegex);
        Matcher matcher = pattern.matcher(input);

        return matcher.replaceFirst(replacement);
    }

    static float[] evaluateConditionalStatement(String exp, boolean isIf) throws Exception {
        try {
            exp = exp.replace(" ", "");
            String ifRegex = isIf ? "(" + Constants.IF + ")" : "(" + Constants.ELSE + ")";
            Pattern ifPattern = Pattern.compile(ifRegex);
            Matcher matcher = ifPattern.matcher(exp);

            if (matcher.find()) {
                float a = Float.valueOf(matcher.group(2));
                String b = matcher.group(4);
                float c = Float.valueOf(matcher.group(5));
                float d = Float.valueOf(matcher.group(7));
                float e = Float.valueOf(matcher.group(9));

                float[] result = {0, e};
                switch (b) {
                    case "=":
                        if (a == c) {
                            result[0] = 1;
                            result[1] = d;
                        }
                        return result;
                    case "<=":
                        if (a <= c) {
                            result[0] = 1;
                            result[1] = d;
                        }
                        return result;
                    case "<":
                        if (a < c) {
                            result[0] = 1;
                            result[1] = d;
                        }
                        return result;
                    case ">":
                        if (a > c) {
                            result[0] = 1;
                            result[1] = d;
                        }
                        return result;
                    case ">=":
                        if (a >= c) {
                            result[0] = 1;
                            result[1] = d;
                        }
                        return result;
                    default:
                        throw new Exception("sign error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        throw new Exception("sign error");
    }

    static List<String> getElseSegments(String input, String elseRegex) {
        input = input.replace(" ", "");
        Pattern pattern = Pattern.compile(elseRegex);
        List<String> elseMatches = new ArrayList<>();

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            elseMatches.add(matcher.group(0));
        }

        return elseMatches;
    }

    static boolean isOperator(char i) {
        return precedence(i) > 0;
    }

    static int precedence(char i) {
        if (i == '(' || i == ')') return 1;
        else if (i == '-' || i == '+') return 2;
        else if (i == '*' || i == '/') return 3;
        else return 0;
    }

    static String infixToPostfix(String infix) {
        // get leading float if any, replace - with N
        Pattern pattern = Pattern.compile(Constants.LEADING_FLOAT);
        String leadingFloat;
        Matcher matcher = pattern.matcher(infix);

        if(matcher.find()){
            leadingFloat = matcher.group(0);
            infix = replaceString(infix, leadingFloat.replace("-", "N"), Constants.LEADING_FLOAT);
        }

        String postfix = "";
        // debugging the stack
        // stack.forEach(System.out::println);
        Stack<Character> operator = new Stack<>();
        char popped;

        for (int i = 0; i < infix.length(); i++) {
            char get = infix.charAt(i);

            if (!isOperator(get)) {
                postfix += get;
            } else if (get == ')') {
                postfix += " ";
                while ((popped = operator.pop()) != '(') postfix += popped;
            } else {
                postfix += " ";
                while (!operator.isEmpty() && get != '(' && precedence(operator.peek()) >= precedence(get)){
                    postfix += operator.pop();
                }
                operator.push(get);
            }
        }
        while (!operator.isEmpty()) postfix += operator.pop();
        return postfix;
    }

    static boolean evaluateBracketsCompletion(String input) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < input.length(); i++) {
            char get = input.charAt(i);
            // opening bracket, push to the stack
            if (get == '(') {
                stack.push('(');
            } else if (get == ')') {
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    static float evaluatePostfixExpression(String exp) {
        exp = exp.replace("  ", " ");
        exp = exp.replace(" *", "*");
        exp = exp.replace("* ", "*");
        exp = exp.replace(" +", "+");
        exp = exp.replace("+ ", "+");
        exp = exp.replace(" -", "-");
        exp = exp.replace("- ", "-");
        exp = exp.replace(" /", "/");
        exp = exp.replace("/ ", "/");

        char[] operands = new char[]{'-', '+', '/', '*'};
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (!contains(c, operands)) {
                stack.push(c);
            } else {
                String op1 = "", op2 = "";
                op1 = extractFloatFromStack(stack, op1);
                op2 = extractFloatFromStack(stack, op2);

                float val1 = Float.valueOf(op1);
                float val2 = Float.valueOf(op2);

                switch (c) {
                    case '+':
                        for (char c1 : (" " + (val2 + val1) + " ").replace("-", "N").toCharArray()) {
                            stack.push(c1);
                        }
                        break;

                    case '-':
                        for (char c1 : (" " + (val2 - val1) + " ").replace("-", "N").toCharArray()) {
                            stack.push(c1);
                        }
                        break;

                    case '/':
                        for (char c1 : (" " + (val2 / val1) + " ").replace("-", "N").toCharArray()) {
                            stack.push(c1);
                        }
                        break;

                    case '*':
                        for (char c1 : (" " + (val2 * val1) + " ").replace("-", "N").toCharArray()) {
                            stack.push(c1);
                        }
                        break;
                }
            }
        }

        String result = "";
        while (!stack.empty()) result = stack.pop() + result;
        return Float.valueOf(result.replace("N", "-"));
    }

    static String extractFloatFromStack(Stack<Character> stack, String op2) {
        while (!stack.empty()) {
            Character y = stack.pop();
            if (y.equals(' ') && !op2.isBlank()) {
                break;
            } else {
                op2 = y + op2;
            }
        }

        op2 = op2.replace("N", "-");
        return op2;
    }

    static boolean contains(char c, char[] array) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }
}
