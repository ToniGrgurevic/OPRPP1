package hr.fer.zemris.java.fractals.Newton;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.net.SecureCacheResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class Newton {

    private static final double CONVERGENCE_TRESHOLD = 0.001;

    private static final int MAX_ITER = 16 * 16;

    private static Complex[] roots;
    private static ComplexPolynomial polynomial;

    private static ComplexPolynomial derivedPolynomial;

    private static ComplexRootedPolynomial rootedPolynomial;



    public static void main(String[] args) {

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
        Scanner sc =new Scanner(System.in);
        int index =0;
        List<Complex> inputRoots = new ArrayList<>();

        while(true){
            System.out.print("Root " + (index +1) + ">");
            String line = sc.nextLine();
            if (line.equals("done")) {
                if (index < 2) {
                    System.out.println("Please enter at least 2 roots.");
                    continue;
                }
                break;
            };
            index++;
            try {
                inputRoots.add(Complex.parse(line));
            } catch (Exception e) {
                System.out.println("Invalid input");
                index--;
            }
        }

        roots =inputRoots.toArray(new Complex[0]);
        rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE,roots);
        polynomial = rootedPolynomial.toComplexPolynom();
        derivedPolynomial  =polynomial.derive();


        FractalViewer.show(new MojProducer());
    }

    public static class MojProducer implements IFractalProducer {
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {


            int offset = 0;
            short[] data = new short[width * height];
            for(int y = 0; y < height; y++) {
                if(cancel.get()) break;
                for(int x = 0; x < width; x++) {
                    double cre = x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;

                    Complex zn  = new Complex(cre,cim);
                    double module = 0;
                    int iters = 0;

                    do {
                        Complex numerator = polynomial.apply(zn);
                        Complex denominator = derivedPolynomial.apply(zn);
                        Complex znold = zn;
                        Complex fraction = numerator.divide(denominator);
                         zn = zn.sub(fraction);
                        module = znold.sub(zn).module();

                        iters++;
                    } while(iters < MAX_ITER && module > CONVERGENCE_TRESHOLD);

                    int index = rootedPolynomial.indexOfClosestRootFor(zn, 0.002);
                    data[offset] = (short)(index + 1);
                    offset++;
                }
            }
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        }

    }
}
