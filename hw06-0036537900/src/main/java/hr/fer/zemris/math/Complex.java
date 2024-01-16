package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents complex number and provides methods for working with complex numbers.
 * Complex numbers are immutable.Objects of this class are unmodifiable (read-only) after construction.
 */
public class Complex {


    private final double re;

    private final double im;

    public static final Complex ZERO = new Complex(0,0);
    public static final Complex ONE = new Complex(1,0);
    public static final Complex ONE_NEG = new Complex(-1,0);
    public static final Complex IM = new Complex(0,1);
    public static final Complex IM_NEG = new Complex(0,-1);

    /**
     * Constructor of Complex class
     * @param re real part of complex number
     * @param im imaginary part of complex number
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex() {
        this(0,0);
    }


    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public static Complex parse(String complexInput) {
        StringBuilder number = new StringBuilder();
        complexInput = complexInput.strip();
        int startLenght  =complexInput.length();
        double[] numbers = new double[]{0, 0};
        int indexNumber = 0;
        boolean negative = false;
        boolean imag = false;


        while(complexInput.length() > 0){
            while(!Character.isDigit(complexInput.charAt(0)) ){
                char c = complexInput.charAt(0);
                if(c == '-'){
                    negative = true;
                } else if(c == 'i' ){
                    indexNumber = 1;
                    imag = true;
                } else if(c != '+' && c != ' '){
                    throw new IllegalArgumentException("Invalid input");
                }
                complexInput = complexInput.substring(1);
                if(complexInput.length() == 0) break;
            }

            if(complexInput.length() == 0){
                if(imag){
                    numbers[indexNumber] = (negative ? -1 : 1);
                }
                break;
            };

            while(  Character.isDigit(complexInput.charAt(0)) || complexInput.charAt(0) == '.'){
                number.append(complexInput.charAt(0));
                complexInput = complexInput.substring(1);
                if(complexInput.length() == 0) break;
            }


            numbers[indexNumber++] = Double.parseDouble(number.toString());
            if(negative){
                numbers[indexNumber-1] *= -1;
            }
            negative = false;
            number.setLength(0);
        }



        for (int i = 0 ; i < numbers.length; i++) {
            if (numbers[i] == -0) numbers[i] = 0;
        }


        return new Complex(numbers[0], numbers[1]);
    }

    /**
     * Method module of complex number
     * @return module of complex number
     */
    public double module() {
        return Math.sqrt(re*re + im*im);
    }

    /**
     * Method returns new complex that is result of multiplication of this and given complex number
     * (a+bi)*(c+di) = (ac-bd) + (ad+bc)i
     * @param c complex number
     * @return  new complex that is result of multiplication of this and given complex number
     */
    public Complex multiply(Complex c) {
        return new Complex(this.re*c.re - this.im*c.im, this.re*c.im + this.im*c.re);
    }

    /**
     * Method returns new complex that is result of division of this and given complex number
     * @param c complex number we divide with
     * @return new complex that is result of division of this and given complex number c
     */
    public Complex divide(Complex c) {
        double divider = c.re*c.re + c.im*c.im;
        return new Complex((this.re*c.re + this.im*c.im)/divider, (this.im*c.re - this.re*c.im)/divider);
    }

    /**
     * Method returns new complex that is result of addition of this and given complex number
     * @param c complex number we add with
     * @return new complex that is result of addition of this and given complex number c
     */
    public Complex add(Complex c) {
        return  new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * Method returns new complex that is result of subtraction of this and given complex number
     * @param c complex number we subtract with
     * @return new complex that is result of subtraction of this and given complex number c
     */
    public Complex sub(Complex c) {
        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * Method returns new complex that is result of negation of this complex number
     * @return new complex that is result of negation of this complex number
     */
    public Complex negate() {
        return this.multiply(ONE_NEG);
    }

    /**
     * Method returns new complex that is result of power of this complex number
     * @param n power that we raise this complex number to.Must be non-negative integer
     * @return new complex that is result of power of this complex number (this^n)
     */
    public Complex power(int n) {
        if(n < 0) {
           throw new IllegalArgumentException("Power must be non-negative integer");
        }

        double module = Math.pow(this.module(), n);
        double angle = n * this.getAngle();
        return new Complex(module * Math.cos(angle), module * Math.sin(angle));
    }


    /**
     * Method returns list of complex numbers that are result of nth root of this complex number
     * @param n root that we take.Must be positive integer
     * @return list of complex numbers that are result of nth root of this complex number
     */
    public List<Complex> root(int n) {
        if(n <= 0) {
            throw new IllegalArgumentException("Root must be positive integer");
        }
        List<Complex> roots = new ArrayList<>();
        double module = Math.pow(this.module(), 1.0/n);
        double angle = this.getAngle();
        for(int i = 0; i < n; i++) {
            roots.add(new Complex(module * Math.cos((angle + 2*i*Math.PI)/n), module * Math.sin((angle + 2*i*Math.PI)/n)));
        }
        return roots;
    }

    public double getAngle() {
        return Math.atan2(im, re);
    }

    public static double angleOf(double im , double re) {
        return Math.atan2(im, re);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.1f", re));
        if(im < 0) {
            sb.append(String.format("-i%.1f", -im));
        } else if(im > 0) {
            sb.append(String.format("+i%.1f", im));
        }
        return sb.toString();
    }

}

