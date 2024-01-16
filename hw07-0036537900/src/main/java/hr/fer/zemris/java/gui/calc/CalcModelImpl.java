package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {

    /**
     * Flag that indicates if the model is editable.
     */
    private boolean editable;

    /**
     * Flag that indicates if the model is negative.
     */
    private boolean negative;

    /**
     * string that represents current value.
     */
    private String inputString;

    /**
     * Value represented by the string inputString.
     */
    private double value;

    private String frozenValue;

    private Double activeOperand;

    private DoubleBinaryOperator pendingOperation;

    List<CalcValueListener> liseners;


    public CalcModelImpl() {
        this.editable = true;
        this.negative = false;
        this.inputString = "";
        this.value = 0;
        this.frozenValue = null;
        liseners = new ArrayList<>();
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        liseners.add(l);

    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        liseners.remove(l);
    }


    @Override
    public double getValue() {
        return  negative ? -1 * value : value;
    }

    /**
     * Method sets the value of the model and the input string and sets the negative flag.
     * @param value new value to be set
     */
    @Override
    public void setValue(double value) {
        this.value =Math.abs(value);
        this.inputString = Double.toString(Math.abs(this.value));

        this.negative = value < 0;
        editable = false;
        frozenValue = null;
        notifyListeners();
        this.frozenValue = inputString;

    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void clear() {
        inputString = "";
        value = 0;
        notifyListeners();
        editable = true;
        frozenValue = null;
    }

    @Override
    public void clearAll() {
        editable = true;
        negative = false;
        inputString = "";
        value = 0;
        frozenValue = null;
        activeOperand = null;
        notifyListeners();
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if(!editable){
            throw  new CalculatorInputException("Model is not editable");
        }
        negative = !negative;
        notifyListeners();
        frozenValue = null;
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if(!editable){
            throw  new CalculatorInputException("Model is not editable");
        }

        if(inputString.contains(".") || inputString.equals("")) {
                throw new CalculatorInputException("Input already contains decimal point");
        }
        inputString += ".";

        frozenValue = null;
        notifyListeners();
    }

    /**
     *Method adds digit to the input string.
     * @param digit digit to be inserted
     * @throws CalculatorInputException if the model is not editable or if the input is invalid
     */
    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {

        if(!editable){
            throw  new CalculatorInputException("Model is not editable");
        }

        try{
            String newInputString = inputString + digit;

            value = Double.parseDouble(newInputString);
            if(value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY){
                throw new CalculatorInputException("Input is too large");
            }
            inputString = newInputString;
        }catch (NumberFormatException e){
            throw new CalculatorInputException("Invalid input");
        }
        frozenValue = null;
        notifyListeners();
    }

    @Override
    public String toString() {
        if (frozenValue != null) {
            double frozen = Double.parseDouble(frozenValue);
            if (frozen - (int) frozen < 1E-6)
                return (negative ? "-" : "") + Integer.toString((int) frozen);
            return (negative ? "-" : "") + Double.toString(frozen);
        }

        if(inputString.equals("") || value == 0.0)
            return negative ? "-" + "0" : "0";
        else
            if(value - (int)value < 1E-6 )
                return (negative ? "-" : "") + Integer.toString((int)value);
            return (negative ? "-" : "") + Double.toString(value);
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if(!isActiveOperandSet())
            throw new IllegalStateException("Active operand is not set");
        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;;
        frozenValue = inputString;
        inputString = "";
        value = 0;
        this.editable = true;
    }

    @Override
    public void clearActiveOperand() {
        activeOperand = null;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return this.pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;

    }

    /**
     * Method that notifies all listeners that the value has changed.
     */
    public void notifyListeners() {
        for (CalcValueListener listener : liseners) {
            listener.valueChanged(this);
        }
    }
}
