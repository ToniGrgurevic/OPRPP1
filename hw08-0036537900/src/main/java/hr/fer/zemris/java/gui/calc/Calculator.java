package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class that represents calculator GUI.
 * Most of operations are implemented in CalcModelImpl class.
 */
public class Calculator extends JFrame {

    CalcModelImpl model;

    private boolean inv;

    private Stack<Double> stack;

    
    Calculator() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Calculator");
        setLocation(20, 20);
        setSize(600, 300);
        stack = new Stack<>();
        inv = false;
        model = new CalcModelImpl();
        initGUI();
    }

    private void initGUI() {

        var cp = getContentPane();
        cp.setLayout(new CalcLayout(4));

        List<MultiOperationButton> invertableOperations = new ArrayList<>();

        initScreen(cp);

        initNumbers(cp);

        JButton prefix = new JButton("+/-");
        prefix.addActionListener(e -> model.swapSign());
        cp.add(prefix, new RCPosition(5, 4));

        JButton dot = new JButton(".");
        dot.addActionListener(e -> model.insertDecimalPoint());
        cp.add(dot, new RCPosition(5, 5));

        JButton equals = new JButton("=");

        equals.addActionListener(e ->{
              var result  = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
                model.setValue(result);
                model.clearActiveOperand();
                model.setPendingBinaryOperation(null);

        });
        cp.add(equals, new RCPosition(1, 6));


        initInstatntOperations(cp, invertableOperations, equals);
        initTwoOperandOperations(cp,invertableOperations,equals);




        JButton clr = new JButton("clr");
        clr.addActionListener(e -> model.clear());
        cp.add(clr, new RCPosition(1, 7));

        JButton reset = new JButton("reset");
        reset.addActionListener(e -> {
            model.clearAll();
            //stack.clear();
        });
        cp.add(reset, new RCPosition(2, 7));

        JButton push = new JButton("push");
        push.addActionListener(e -> stack.push(model.getValue()));
        cp.add(push, new RCPosition(3, 7));

        JButton pop = new JButton("pop");
        pop.addActionListener(e -> {
            if(stack.isEmpty())
                return;
            model.setValue(stack.pop());
        });
        cp.add(pop, new RCPosition(4, 7));

        JCheckBox inverseCheck = new JCheckBox("inv");

        inverseCheck.addActionListener(e -> {
            this.inv = !this.inv;
            for(var button : invertableOperations){
                button.setText(this.inv ? button.getInverseOperationName() : button.getOperationName());
            }
        });
        cp.add(inverseCheck, new RCPosition(5, 7));


    }

    private void initTwoOperandOperations(Container cp, List<MultiOperationButton> invertableOperations, JButton equals) {
        MultiOperationButton power = new MultiOperationButton("x^n", "x^(1/n)", Math::pow, (x, y) -> Math.pow(x, 1/y));
        invertableOperations.add(power);
        List<BasicOperationButton> operations = new ArrayList<>();
        operations.add(new BasicOperationButton("/", (x, y) -> x/y));
        operations.add(new BasicOperationButton("*", (x, y) -> x*y));
        operations.add(new BasicOperationButton("-", (x, y) -> x-y));
        operations.add(new BasicOperationButton("+", (x, y) -> x+y));

        ActionListener lisener = (e) ->{
            BasicOperationButton source = (BasicOperationButton) e.getSource();
            if(model.getPendingBinaryOperation() != null) {
                equals.doClick(1);
            }

            model.setActiveOperand(model.getValue());
            model.setPendingBinaryOperation(source.getCurentOperation(inv));

        };

        power.addActionListener(lisener);
        cp.add(power, new RCPosition(5, 1));

        for(int i=0;i<4;i++){
            BasicOperationButton button = operations.get(i);
            button.addActionListener(lisener);
            cp.add(button, new RCPosition(2 + i, 6));
        }




    }

    private void initInstatntOperations(Container cp, List<MultiOperationButton> operations, JButton equals) {
        operations.add(new MultiOperationButton("1/x", "1/x", (x, y) -> 1/x, (x, y) -> 1/x));
        operations.add(new MultiOperationButton("sin", "arcsin",  (x, y)-> Math.sin(x), (x, y) -> Math.asin(x)));
        operations.add(new MultiOperationButton("log", "10^x",  (x, y) -> Math.log(x)/Math.log(y), (x, y) -> Math.pow(y, x)));
        operations.add(new MultiOperationButton("cos", "arccos",  (x, y) -> Math.cos(x), (x, y) -> Math.acos(x)));
        operations.add(new MultiOperationButton("ln", "e^x",  (x, y) -> Math.log(x), (x, y) -> Math.pow(Math.E, x)));
        operations.add(new MultiOperationButton("tan", "arctan",  (x, y)-> Math.tan(x), (x, y) -> Math.atan(x)));
        operations.add(new MultiOperationButton("ctg", "arcctg",  (x, y) -> 1/Math.tan(x), (x, y) -> Math.PI/2 - Math.atan(x)));

        for(int i=0;i<7;i++){
            MultiOperationButton button = operations.get(i);

            if(i == 6)
                i++;

            cp.add(button, new RCPosition(2 + i/2,  i%2 + 1));

            button.addActionListener(e -> {
                MultiOperationButton source = (MultiOperationButton) e.getSource();
                if(model.getPendingBinaryOperation() != null){
                    equals.doClick(1);
                }

                model.setActiveOperand(model.getValue());
                model.setPendingBinaryOperation(source.getCurentOperation(inv));
                equals.doClick(1);


            });


        }
    }

    private void initScreen(Container cp) {
        JLabel display = new JLabel(model.toString());
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(Color.YELLOW);
        display.setOpaque(true);
        display.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        display.setFont(new Font("Arial", Font.BOLD, 30));
        CalcValueListener listener = new CalcValueListener() {
            @Override
            public void valueChanged(CalcModel model) {

                display.setText(model.toString());
            }
        };
        model.addCalcValueListener(listener);
        cp.add(display, new RCPosition(1, 1));
    }

    private void initNumbers(Container cp) {
        ActionListener insertNumb = e -> {
            JButton source = (JButton) e.getSource();
            model.insertDigit(Integer.parseInt(source.getText()));
        };
        JButton[] number = new JButton[10];

        for(int i=0;i<10;i++){
            number[i] = new JButton(String.valueOf(i));
            number[i].setFont(number[i].getFont().deriveFont(30f));
            number[i].addActionListener(insertNumb);
            int invertI = 9-i;
            if(i == 0)
                cp.add(number[i] , new RCPosition(5, 3));
            else
                cp.add(number[i] , new RCPosition(2 + invertI/3, 5 - invertI%3));
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }
}
