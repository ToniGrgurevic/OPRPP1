package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.*;


/* – a node representing a single for-loop construct. It inherits from Node class.
*Can have 3 or 4 elements.First must be ElementVariable,
*second, third and forth  can be ElementConstantInteger or ElementString or ElementVariable
* or ElementConstantDouble. If there is 4 elements, last one must be ElementStepExpression.
* */
public class ForLoopNode extends Node {



    private ElementVariable variable;
    private Element startExpression;
    private Element endExpression;

    /* can be null*/
    private Element stepExpression;


    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }


    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(" {$ FOR ");
        sb.append(variable.asText()).append(" ")
                .append(startExpression.asText()).append(" ")
                .append(endExpression.asText()).append(" ");
        if(stepExpression != null)
            sb.append(stepExpression.asText()).append(" ");
        sb.append(" $} ");


        for (var child : children.toArray()) {
            sb.append(child.toString());
        }
        sb.append(" {$END$} ");

        return sb.toString();

    }


    /**
     * Method checks if two ForLoopNodes are equal
     * @param obj Object to be compared with
     * @return returns true if two ForLoopNodes are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ForLoopNode other)) {
            return false;
        }
        if(!this.variable.equals(other.variable)) {
            return false;
        }
        if(!this.startExpression.equals(other.startExpression)) {
            return false;
        }
        if(!this.endExpression.equals(other.endExpression)) {
            return false;
        }
        if(this.stepExpression == null && other.stepExpression != null) {
            return false;
        }
        if(this.stepExpression != null && other.stepExpression == null) {
            return false;
        }
        if(this.stepExpression != null && other.stepExpression != null) {
            if(!this.stepExpression.equals(other.stepExpression)) {
                return false;
            }
        }
        if(this.numberOfChildren() != other.numberOfChildren()) {
            return false;
        }
        for(int i = 0; i < this.numberOfChildren(); i++) {
            if(!this.getChild(i).equals(other.getChild(i))) {
                return false;
            }
        }
        return true;
    }
}
