import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class MainApplicationTest {

    private static final double DELTA = 1e-15;

    @Test
    public void main() {
    }

    @Test
    public void evaluateBracketsCompletion() {
        assertFalse(MainApplication.evaluateBracketsCompletion("((2/3) + 2 * 45(3-2)"));
        assertTrue(MainApplication.evaluateBracketsCompletion("((2/3) + 2 * 45(3-2))"));
    }

    @Test
    public void contains() {
        assertFalse(MainApplication.contains('x', "Hello world".toCharArray()));
        assertTrue(MainApplication.contains('e', "Hello world".toCharArray()));
    }

    @Test
    public void extractCharacterFromStack() {
        Stack<Character> stack = new Stack<>();
        String eval = "45.2 568.25";
        for (Character c : eval.toCharArray()) {
            stack.push(c);
        }
        assertEquals("568.25", MainApplication.extractFloatFromStack(stack, ""));
    }

    @Test
    public void evaluatePostfixExpression() {
        assertEquals(-45, MainApplication.evaluatePostfixExpression("5 3 * 12 5 * -"), DELTA);
        assertEquals(-45, MainApplication.evaluatePostfixExpression("5 3*12 5*-"), DELTA);
    }

    @Test
    public void infixToPostfix() {
        assertEquals(" 5   3 *  12   5*-", MainApplication.infixToPostfix("(5 * 3) -12 * 5"));
        assertEquals(" 5 3 * 12 5*-", MainApplication.infixToPostfix("(5*3)-12*5"));
        assertEquals(" 5   3 *  3  /  14.4  3 -  *12   5*-", MainApplication.infixToPostfix("(5 * 3) /3 * (14.4 -3) -12 * 5"));
    }

    @Test
    public void precedence() {
        assertEquals(1, MainApplication.precedence('('));
        assertEquals(2, MainApplication.precedence('+'));
        assertEquals(3, MainApplication.precedence('/'));
        assertEquals(0, MainApplication.precedence(' '));
    }

    @Test
    public void isOperator() {
        assertTrue( MainApplication.isOperator('/'));
        assertFalse(MainApplication.isOperator(' '));
    }

    @Test
    public void getElseSegments() {
        String exp = "if(2<3,5,6)else(3=4,4,5)else(2=4,5,6)+2-if(2<3,5,6)else(3=4,4,5)else(2=4,5,6)";
        assertEquals(4, MainApplication.getElseSegments(exp, String.format("(%s)", Constants.ELSE)).size());
    }

    @Test
    public void evaluateConditionalStatement() throws Exception {
        assertEquals(1, MainApplication.evaluateConditionalStatement("if(23 > 11, 12, 22) ", true)[0], DELTA);
        assertEquals(12, MainApplication.evaluateConditionalStatement("if(23 > 11, 12, 22) ", true)[1], DELTA);
        assertEquals(0, MainApplication.evaluateConditionalStatement("if(23 < 11, 12, 22) ", true)[0], DELTA);
        assertEquals(22, MainApplication.evaluateConditionalStatement("if(23 < 11, 12, 22) ", true)[1], DELTA);

        assertEquals(1, MainApplication.evaluateConditionalStatement(" else(23 > 11, 12, 22) ", false)[0], DELTA);
        assertEquals(12, MainApplication.evaluateConditionalStatement(" else(23 > 11, 12, 22) ", false)[1], DELTA);
        assertEquals(0, MainApplication.evaluateConditionalStatement("else(23 < 11, 12, 22) ", false)[0], DELTA);
        assertEquals(22, MainApplication.evaluateConditionalStatement("else(23 < 11, 12, 22) ", false)[1], DELTA);
    }

    @Test
    public void replaceString() {
        String exp = "if(2<3,5,6)else(3=4,4,5)else(2=4,5,6)+2-if(2<3,5,6)else(3=4,4,5)else(2=4,5,6)";
        String res = "23+2-if(2<3,5,6)else(3=4,4,5)else(2=4,5,6)";
        assertEquals(res, MainApplication.replaceString(exp, "23", Constants.IF_ELSE));
    }

    @Test
    public void evaluateMinMaxFunctions() {
        assertEquals("45.0", MainApplication.evaluateMinMaxFunctions("max(45,5)"));
        assertEquals("N5.0", MainApplication.evaluateMinMaxFunctions("max(-45,-5)"));
        assertEquals("5.0", MainApplication.evaluateMinMaxFunctions("min(45,5)"));
        assertEquals("N45.0", MainApplication.evaluateMinMaxFunctions("min(-45,5)"));
    }

    @Test
    public void evaluateIfStatements() throws Exception {
        String exp = "if(2<3,5,6)else(3=4,4,5)else(2=4,5,6)+2-if(2>3,5,6)else(3=4,4,5)else(2=4,5,6)";
        String res = "5.0+2-6.0";
        assertEquals(res, MainApplication.evaluateIfStatements(exp));
        exp = "if(2<3,-5,6)else(3=4,4,5)else(2=4,5,6)+2-if(2>3,5,6)else(3=4,4,5)else(2=4,5,6)";
        res = "N5.0+2-6.0";
        assertEquals(res, MainApplication.evaluateIfStatements(exp));
    }
}
