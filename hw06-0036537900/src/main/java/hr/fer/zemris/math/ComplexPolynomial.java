package hr.fer.zemris.math;

/**
 * Class represents complex polynomial and provides methods for working with complex polynomials.
 * Complex polynomials are immutable.Objects of this class are unmodifiable (read-only) after construction.
 * Complex polynomial is represented as sum of complex numbers multiplied with z^n where n is natural number.
 * For example, complex polynomial (7+2i)z^3+2z^2+5z+1 is represented as array of complex numbers [1,5,2,7+2i].
 */
public class ComplexPolynomial {
    private final Complex[] factors;

    /**
     * Constructor of ComplexPolynomial class.
     * Factors are given from the lowest to the higest (Z0,Z1,...,Zn)
     * @param factors array of complex numbers that represent complex polynomial
     */
    public ComplexPolynomial(Complex ...factors) {
        this.factors = factors;
    }


    /** returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
     * @return order of this polynom
     */
    public short order() {
        return (short) ( factors.length -1);
    }

    /** computes a new polynomial this*p
     * @param p complex polynomial
     * @return new polynomial this*p
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Complex[] newFactors = new Complex[this.order() + p.order() + 1];
        for (int i = 0; i < newFactors.length; i++) {
            newFactors[i] = new Complex(0,0);
        }
        for(int i = 0; i<this.factors.length ; i++){

            for(int j =0 ; j<p.factors.length;j++){
                Complex c = this.factors[i].multiply(p.factors[j]);
                newFactors[i+j] = newFactors[i+j].add(c);
            }
        }
        return new ComplexPolynomial(newFactors);
    }

    /**
     *  Method computes first derivative of this polynomial.
     * for example, for polynomial  (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
     * @return first derivative of this polynomial
     */
    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[this.order()];

        for(int i = 1; i<this.factors.length ; i++){
            newFactors[i-1] =new Complex(i,0).multiply(this.factors[i]);
        }
        return new ComplexPolynomial(newFactors);
    }
    /**
     * Method computes polynomial value at given point z
     * @param z complex number that we insert in polynomial
     * @return  value at given point z
     */
    public Complex apply(Complex z) {
        Complex result = new Complex(0,0);
        for(int i = 0; i<this.factors.length ; i++){
            result = result.add(this.factors[i].multiply(z.power(i)));
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = factors.length - 1; i >= 0; i--) {
            sb.append("(").append(factors[i]).append(")*z^").append(i);

            if(i != 0) sb.append(" + ");
        }
        return sb.toString();
    }
}
