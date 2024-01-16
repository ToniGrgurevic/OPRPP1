package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class BasicOperationButton extends JButton {
    private DoubleBinaryOperator operator;

    private String operationName;



    public BasicOperationButton(String operationName, DoubleBinaryOperator operator) {
        super(operationName);
        this.operator = operator;
        this.operationName = operationName;
    }

    public DoubleBinaryOperator getCurentOperation(boolean inverted){
        return operator;
    }

    public String getOperationName() {
        return operationName;
    }

    public DoubleBinaryOperator getOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiOperationButton)) return false;
        MultiOperationButton that = (MultiOperationButton) o;
        return Objects.equals(operator, that.getOperator()) && Objects.equals(operationName, that.getOperationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, operationName);
    }
}
