package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class MultiOperationButton extends BasicOperationButton {
    private DoubleBinaryOperator inverseOperator;


    private String inverseOperationName;


    public MultiOperationButton(String operationName, String inverseOperationName, DoubleBinaryOperator operator, DoubleBinaryOperator inverseOperator) {
        super(operationName, operator);

        this.inverseOperator = inverseOperator;

        this.inverseOperationName = inverseOperationName;
    }


    public String getInverseOperationName() {
        return inverseOperationName;
    }


    public DoubleBinaryOperator getInverseOperator() {
        return inverseOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiOperationButton)) return false;
        MultiOperationButton that = (MultiOperationButton) o;
        return Objects.equals(this.getOperator(), that.getOperator()) && Objects.equals(inverseOperator, that.inverseOperator) && Objects.equals(this.getOperationName(), that.getOperationName()) && Objects.equals(inverseOperationName, that.inverseOperationName);
    }

    @Override
    public DoubleBinaryOperator getCurentOperation(boolean inverted){
        if(inverted){
            return getInverseOperator();
        }
        return getOperator();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getOperator(), this.getInverseOperationName(), this.getOperationName(), this.getInverseOperator());
    }
}
