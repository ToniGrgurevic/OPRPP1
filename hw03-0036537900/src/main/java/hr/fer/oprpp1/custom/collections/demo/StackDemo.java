package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {




    /**
     * Calculeates the given expression and prints the result
     *
     * @param args one expression to be calculated
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Invalid number of arguments");
            return;
        }
        ObjectStack<Integer> stack = new ObjectStack<>();

        String[] expression = args[0].split(" ");
        for (String element : expression) {
            try {
                if (element.matches("-?\\d+")) {
                    stack.push(Integer.parseInt(element));
                } else {
                    int op2 = (int) stack.pop();
                    int op1 = (int) stack.pop();
                    switch (element) {
                        case "+" -> stack.push(op1 + op2);
                        case "-" -> stack.push(op1 - op2);
                        case "*" -> stack.push(op1 * op2);
                        case "/" -> {
                            if (op2 == 0) {
                                System.err.println("Can't divide by zero");
                                return;
                            }
                            stack.push(op1 / op2);
                        }
                        case "%" -> {
                            if (op2 == 0) {
                                System.err.println("Can't divide by zero");
                                return;
                            }
                            stack.push(op1 % op2);
                        }
                        default -> {
                            System.err.println("Invalid expression,Sign not recognized: '" + element + "'");
                            return;
                        }
                    }
                }
            }catch (EmptyStackException e){
                System.err.println("Invalid postfix expression");
            }

        }

        if(stack.size() != 1) {
            System.err.println("error in expression.End result of stack doesn't have only one value (result)");
            return;
        }
        System.out.println("Expression evaluates to " + stack.pop());
    }

}
