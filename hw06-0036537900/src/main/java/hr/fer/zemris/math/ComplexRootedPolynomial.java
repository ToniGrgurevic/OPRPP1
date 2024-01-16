package hr.fer.zemris.math;

/**
 * Class represents complex polynomial and provides methods for working with complex polynomials.
 * Complex polynomials are immutable.Objects of this class are unmodifiable (read-only) after construction.
 * Complex polynomal is represented with constant and  his zero points (mayimus/minimums)
 *  f(z) -> z0*(z-z1)*(z-z2)*...*(z-zn) where z0 is constant and z1,z2,...,zn are zero points
 */
public class ComplexRootedPolynomial {

    private final Complex constant;
    private final Complex[] roots;

    /**
     * Constructor of ComplexRootedPolynomial class.
     * @param constant constant of complex polynomial
     * @param roots array of complex numbers that represent zero points (roots of complex polynomial
     */
    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.constant = constant;
        this.roots = roots;
    }

    // computes polynomial value at given point z
    /**
     * Method calculates and returns polynomial value at given point z of this complex polynomial
     * @param z complex number
     * @return  value at given point z
     */
    public Complex apply(Complex z) {
        Complex result = new Complex(1,0).multiply(constant);
        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }
        return result;
    }


    /** converts this representation to ComplexPolynomial type
     * @return ComplexPolynomial type representation of this ComplexRootedPolynomial
     */
    public ComplexPolynomial toComplexPolynom() {
        Complex[] factors = new Complex[roots.length + 1];
        int broj = 0;

        for(int i = 0; i < roots.length + 1; i++){
            factors[i] = new Complex(0,0);
        }
        double limit = Math.pow(2,roots.length);
        while(broj < limit ){


            Complex c = new Complex(1,0).multiply(constant);
            int counter = 0;
            for(int i = 0; i < roots.length; i++){
                if((broj & (1 << i)) != 0){
                    c = c.multiply( roots[i].negate()) ;
                    counter++;
                }
            }
            factors[roots.length  - counter] = factors[roots.length  - counter].add(c);
            broj++;
        }
        return new ComplexPolynomial(factors);
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(constant.toString());
        for (Complex root : roots) {
            sb.append(" * (z-").append("(").append(root.toString()).append(")").append(")");
        }
        return sb.toString();
    }


    /**
     * Method finds index of closest root for given complex number z that is within
     * treshold; if there is no such root, returns -1
     * first root has index 0, second index 1, etc
     * @param z complex number
     * @param treshold treshold for max alowed  distance between complex number z and zero point of complex polynomial
     * @return index of closest root for given complex number z that is within treshold; if there is no such root, returns -1
        */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < roots.length; i++) {
            double distance = z.sub(roots[i]).module();
            if(distance  < min){
                min = distance;
                index = i;
            }
        }
        if(min > treshold){
            return -1;
        }
        return index;
    }


    public static void main(String[] args) {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial crp1 = new ComplexPolynomial(
                new Complex(1,0), Complex.ONE
        );
        ComplexPolynomial crp2 = new ComplexPolynomial(
                new Complex(1,0), Complex.ONE
        );
        ComplexPolynomial cp = crp.toComplexPolynom();
        System.out.println(crp);
        System.out.println(cp);
        System.out.println(cp.derive());
        System.out.println(crp2.multiply(crp1));
        System.out.println(crp2.apply(new Complex(5,0)));

    }
}
