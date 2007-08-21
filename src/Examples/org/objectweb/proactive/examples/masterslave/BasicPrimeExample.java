package org.objectweb.proactive.examples.masterslave;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.objectweb.proactive.extra.masterslave.TaskAlreadySubmittedException;
import org.objectweb.proactive.extra.masterslave.TaskException;
import org.objectweb.proactive.extra.masterslave.interfaces.SlaveMemory;
import org.objectweb.proactive.extra.masterslave.interfaces.Task;


/**
 * This simple test class is an example on how to use the Master/Slave API
 * The tasks wait for a period between 15 and 20 seconds (by ex)
 * The main program displays statistics about the speedup due to parallelization
 * @author fviale
 *
 */
public class BasicPrimeExample extends AbstractExample {
    private static final long DEFAULT_PRIME_NUMBER = 1397812341;
    private static final int DEFAULT_NUMBER_OF_INTERVALS = 15;
    public int number_of_intervals;
    public long prime_to_find;

    /**
     * Displays result of this test
     * @param results results of the test
     * @param startTime starting time of the test
     * @param endTime ending time of the test
     * @param nbSlaves number of slaves used during the test
     */
    public void displayResult(Collection<Boolean> results, long startTime,
        long endTime, int nbSlaves) {
        // Post processing, calculates the statistics
        boolean prime = true;

        for (Boolean result : results) {
            prime = prime && result;
        }

        long effective_time = (int) (endTime - startTime);
        //      Displaying the result
        System.out.println("" + prime_to_find +
            (prime ? " is prime." : " is not prime."));

        System.out.println("Calculation time (ms): " + effective_time);
    }

    /**
     * Creates the prime computation tasks to be solved
     * @return
     */
    public List<FindPrimeTask> createTasks() {
        List<FindPrimeTask> tasks = new ArrayList<FindPrimeTask>();

        // We don't need to check numbers greater than the square-root of the candidate in this algorithm
        long square_root_of_candidate = (long) Math.ceil(Math.sqrt(
                    prime_to_find));
        // 
        tasks.add(new FindPrimeTask(prime_to_find, 2,
                square_root_of_candidate / number_of_intervals));
        for (int i = 1; i < (number_of_intervals - 1); i++) {
            tasks.add(new FindPrimeTask(prime_to_find,
                    ((square_root_of_candidate / number_of_intervals) * i) + 1,
                    (square_root_of_candidate / number_of_intervals) * (i + 1)));
        }
        tasks.add(new FindPrimeTask(prime_to_find,
                (square_root_of_candidate / number_of_intervals) * (number_of_intervals -
                1), square_root_of_candidate));
        return tasks;
    }

    /**
     * @param args
     * @throws TaskException
     * @throws MalformedURLException
     * @throws TaskAlreadySubmittedException
     */
    public static void main(String[] args)
        throws TaskException, MalformedURLException,
            TaskAlreadySubmittedException {
        BasicPrimeExample instance = new BasicPrimeExample();
        //   Getting command line parameters and creating the master (see AbstractExample)
        instance.init(args);

        System.out.println("Primality test launched for n=" +
            instance.prime_to_find + " with " + instance.number_of_intervals +
            " intervals, using descriptor " + instance.descriptor_url);

        long startTime = System.currentTimeMillis();
        // Creating and Submitting the tasks
        instance.master.solve(instance.createTasks());

        // Collecting the results
        List<Boolean> results = instance.master.waitAllResults();
        long endTime = System.currentTimeMillis();

        // Displaying results, the slavepoolSize method displays the number of slaves used by the master
        instance.displayResult(results, startTime, endTime,
            instance.master.slavepoolSize());

        System.exit(0);
    }

    @Override
    protected void before_init() {
        command_options.addOption("p", true, "number to check for primality");
        command_options.addOption("i", true, "number of dividing intervals");

        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("BasicPrimeExample", command_options);
    }

    @Override
    protected void after_init() {
        String primeString = cmd.getOptionValue("p");
        if (primeString == null) {
            prime_to_find = DEFAULT_PRIME_NUMBER;
        } else {
            prime_to_find = Long.parseLong(primeString);
        }

        String intervalString = cmd.getOptionValue("i");
        if (intervalString == null) {
            number_of_intervals = DEFAULT_NUMBER_OF_INTERVALS;
        } else {
            number_of_intervals = Integer.parseInt(intervalString);
        }
    }

    /**
     * Task to find if any number in a specified interval divides the given candidate
     * @author fviale
     *
     */
    public static class FindPrimeTask implements Task<Boolean> {
        private long begin;
        private long end;
        private long candidate;

        public FindPrimeTask(long candidate, long begin, long end) {
            this.begin = begin;
            this.end = end;
            this.candidate = candidate;
        }

        /* (non-Javadoc)
         * @see org.objectweb.proactive.extra.masterslave.interfaces.Task#run(org.objectweb.proactive.extra.masterslave.interfaces.SlaveMemory)
         */
        public Boolean run(SlaveMemory memory) {
            for (long divider = begin; divider < end; divider++) {
                if ((candidate % divider) == 0) {
                    return new Boolean(false);
                }
            }
            return new Boolean(true);
        }
    }
}
