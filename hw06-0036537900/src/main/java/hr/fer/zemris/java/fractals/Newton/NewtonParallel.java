package hr.fer.zemris.java.fractals.Newton;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonParallel {

    private static final double CONVERGENCE_TRESHOLD = 0.001;

    private static final int MAX_ITER = 16 * 16;

    private static Complex[] roots;
    private static ComplexPolynomial polynomial;

    private static ComplexPolynomial derivedPolynomial;

    private static ComplexRootedPolynomial rootedPolynomial;

    private static int numberOfThreads = 0;
    private static int numberOfTracks = 0;


    public static void main(String[] args) {


        for (int i = 0; i < args.length; i++) {
            String argument = args[i];


            if (!argument.startsWith("--")) {
                if (argument.equals("-w")) {
                    argument = "--workers=" + args[i + 1];
                } else if (argument.equals("-t")) {
                    argument = "--tracks=" + args[i + 1];
                } else {
                    System.out.println("Invalid argument format => " + argument);
                    System.exit(1);
                }
                i++;
            }

            String[] parts = argument.split("=");


            if (parts[0].equals("--workers") || parts[0].equals("-w")) {
                if (numberOfThreads != 0) {
                    System.out.println("Workers already defined!");
                    System.exit(1);
                }
                numberOfThreads = Integer.parseInt(parts[1]);
            }
            if (parts[0].equals("--tracks") || parts[0].equals("-t")) {
                if (numberOfTracks != 0) {
                    System.out.println("Tracks already defined!");
                    System.exit(1);
                }
                int number = Integer.parseInt(parts[1]);
                if(number == 0 ){
                    System.out.println("Tracks can't be 0");
                    System.exit(1);
                }
                numberOfTracks =number;
            }
        }

        if (numberOfThreads == 0) numberOfThreads = Runtime.getRuntime().availableProcessors();
        if (numberOfTracks == 0) numberOfTracks = 4 * Runtime.getRuntime().availableProcessors();

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
        Scanner sc = new Scanner(System.in);
        int index = 0;
        List<Complex> inputRoots = new ArrayList<>();

        while (true) {
            System.out.print("Root " + (index + 1) + ">");
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

        roots = inputRoots.toArray(new Complex[0]);
        rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
        polynomial = rootedPolynomial.toComplexPolynom();
        derivedPolynomial = polynomial.derive();

        printActiveData();
        FractalViewer.show(new NewtonParallel.MojProducer());
    }

    private static void printActiveData() {
        System.out.println("Koristim paralelizaciju:");
        System.out.println("    Radnici: " + numberOfThreads);
        System.out.println("    Trake(poslovi): " + numberOfTracks);
    }


    public static class PosaoIzracuna implements Runnable {
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int yMin;
        int yMax;
        short[] data;
        AtomicBoolean cancel;

        public static PosaoIzracuna NO_JOB = new PosaoIzracuna();

        private PosaoIzracuna() {
        }

        public PosaoIzracuna(double reMin, double reMax, double imMin,
                             double imMax, int width, int height, int yMin, int yMax,
                             short[] data, AtomicBoolean cancel) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.data = data;
            this.cancel = cancel;
        }

        @Override
        public void run() {

            for (int y = yMin; y <= yMax; y++) {
                if (cancel.get()) break;
                for (int x = 0; x < width; x++) {
                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                    Complex zn = new Complex(cre, cim);
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
                    } while (iters < MAX_ITER && module > CONVERGENCE_TRESHOLD);

                    int index = rootedPolynomial.indexOfClosestRootFor(zn, 0.002);
                    data[y * width + x] = (short) (index + 1);
                }
            }
        }

    }


    public static class MojProducer implements IFractalProducer {
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

            short[] data = new short[width * height];
            int brojTraka = Math.max(height,numberOfTracks);
            int brojYPoTraci = height / brojTraka;

            final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

            Thread[] radnici = new Thread[brojTraka];
            for (int i = 0; i < radnici.length; i++) {
                radnici[i] = new Thread(() -> {
                    while (true) {
                        PosaoIzracuna p = null;
                        try {
                            p = queue.take();
                            if (p == PosaoIzracuna.NO_JOB) break;
                        } catch (InterruptedException e) {
                            continue;
                        }
                        p.run();
                    }
                });
            }
            for (int i = 0; i < radnici.length; i++) {
                radnici[i].start();
            }

            for (int i = 0; i < brojTraka; i++) {
                int yMin = i * brojYPoTraci;
                int yMax = (i + 1) * brojYPoTraci - 1;
                if (i == brojTraka - 1) {
                    yMax = height - 1;
                }
                PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data, cancel);
                while (true) {
                    try {
                        queue.put(posao);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }
            for (int i = 0; i < radnici.length; i++) {
                while (true) {
                    try {
                        queue.put(PosaoIzracuna.NO_JOB);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for (int i = 0; i < radnici.length; i++) {
                while (true) {
                    try {
                        radnici[i].join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            printActiveData();
            observer.acceptResult(data, (short) (polynomial.order() +1), requestNo);
        }
    }
}
