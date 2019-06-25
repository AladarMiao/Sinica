package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private static double[] helper;
    private int exp;
    private int size;


    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException("Has to be positive");
        }

        exp = T;
        helper = new double[exp];
        StdRandom.setSeed((long) (Math.random() * 314159));
        size = N;
        for (int i = 0; i < T; i++) {
            int count = 0;
            Percolation test = pf.make(N);
            int column;
            int row;
            while (!test.percolates()) {

                do {
                    column = StdRandom.uniform(N);
                    row = StdRandom.uniform(N);
                } while (test.isOpen(row, column));
                count++;
                test.open(row, column);

            }
            helper[i] = count / Math.pow(N, 2);
        }
    }

    // perform T independent experiments on an N-by-N grid

    public double mean() {
        return StdStats.mean(helper);
    }                                       // sample mean of percolation threshold

    public double stddev() {
        return StdStats.stddev(helper);
    }                                         // sample standard deviation of percolation threshold

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.pow(exp, 0.5);
    }                                  // low endpoint of 95% confidence interval

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.pow(exp, 0.5);
    }
}
