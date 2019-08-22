/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private double[] percolationThreshold;
    private final static double ALPHA = 1.96;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("n <= 0 OR trials <= 0");
        }
        // Stopwatch stopwatch = new Stopwatch();
        this.trials = trials;
        this.percolationThreshold = new double[trials];
        while (trials-- > 0) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            // System.out.println(percolation.numberOfOpenSites());
            this.percolationThreshold[trials] = percolation.numberOfOpenSites() / (double) (n * n);
            // System.out.println(this.percolationThreshold[trials]);
        }

        // System.out.printf("usetime\t = %f\n", stopwatch.elapsedTime());
    }  // perform trials independent experiments on an n-by-n grid

    public double mean() {
        return StdStats.mean(this.percolationThreshold);
    }                     // sample mean of percolation threshold

    public double stddev() {
        return StdStats.stddev(this.percolationThreshold);
    }                      // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return mean() - ALPHA * stddev() / Math.sqrt(this.trials);
    }                // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return mean() + ALPHA * stddev() / Math.sqrt(this.trials);
    }                // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.printf("mean\t = %f\n", percolationStats.mean());
        System.out.printf("stddev\t = %f\n", percolationStats.stddev());
        System.out.printf("95%% confidence interval\t = [%f,%f]\n", percolationStats.confidenceLo(),
                          percolationStats.confidenceHi());
    }
}
