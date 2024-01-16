package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

public class Operations{

    public static final DoubleBinaryOperator ADD = Double::sum;
    public static final DoubleBinaryOperator SUB = (x, y) -> x - y;
    public static final DoubleBinaryOperator MUL = (x, y) -> x * y;
    public static final DoubleBinaryOperator DIV = (x, y) -> x / y;
    public static final DoubleBinaryOperator POW = Math::pow;
    public static final DoubleBinaryOperator ROOT = (x, y) -> Math.pow(x, 1/y);
    public static final DoubleBinaryOperator LOG = (x, y) -> Math.log(x) / Math.log(y);
    public static final DoubleBinaryOperator NTHROOT = (x, y) -> Math.pow(x, 1/y);
    public static final DoubleBinaryOperator INV = (x, y) -> 1/x;
    public static final DoubleBinaryOperator SIN = (x, y) -> Math.sin(x);
    public static final DoubleBinaryOperator COS = (x, y) -> Math.cos(x);
    public static final DoubleBinaryOperator TAN = (x, y) -> Math.tan(x);
    public static final DoubleBinaryOperator CTG = (x, y) -> 1/Math.tan(x);
    public static final DoubleBinaryOperator ASIN = (x, y) -> Math.asin(x);
    public static final DoubleBinaryOperator ACOS = (x, y) -> Math.acos(x);
    public static final DoubleBinaryOperator ATAN = (x, y) -> Math.atan(x);
    public static final DoubleBinaryOperator ACTG = (x, y) -> Math.atan(1/x);
    public static final DoubleBinaryOperator EXP = (x, y) -> Math.exp(x);
    public static final DoubleBinaryOperator LN = (x, y) -> Math.log(x);
    public static final DoubleBinaryOperator LOG10 = (x, y) -> Math.log10(x);
    public static final DoubleBinaryOperator RECIPROCAL = (x, y) -> 1/x;
    public static final DoubleBinaryOperator TENPOW = (x, y) -> Math.pow(10, x);
    public static final DoubleBinaryOperator SINH = (x, y) -> Math.sinh(x);
    public static final DoubleBinaryOperator COSH = (x, y) -> Math.cosh(x);

}
