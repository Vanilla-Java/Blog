package blog.vanillajava.throwable;

import net.openhft.chronicle.core.StackTrace;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that simulates a critical task that needs to be monitored for execution delays.
 */
public class CriticalTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(CriticalTask.class.getName());

    private volatile long loopStartTime = Long.MIN_VALUE;
    private volatile boolean running = true;

    @Override
    public void run() {
        try {
            while (running) {
                loopStartTime = System.currentTimeMillis();
                doWork();
                loopStartTime = Long.MIN_VALUE; // Reset after work is completed
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in worker thread.", e);
        } finally {
            LOGGER.info("Worker thread has terminated.");
        }
    }

    /**
     * Simulates performing work with random durations.
     */
    private void doWork() {
        try {
            Thread.sleep(new Random().nextInt(40)); // Simulate workload
            Thread.sleep(new Random().nextInt(40)); // line 39
            Thread.sleep(new Random().nextInt(40)); // line 40
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warning("Worker thread was interrupted during work.");
        }
    }

    /**
     * Main method to start and monitor the task.
     */
    public static void main(String[] args) {
        CriticalTask task = new CriticalTask();
        Thread worker = new Thread(task, "WorkerThread");
        worker.start();

        try {
            monitorTask(task, worker, 1000, 50);
        } finally {
            task.running = false; // Ensure the task is stopped
            LOGGER.info("Main thread has terminated monitoring.");
        }
    }

    /**
     * Monitors the task for execution delays and logs stack traces if thresholds are exceeded.
     *
     * @param task        The task to monitor.
     * @param worker      The thread running the task.
     * @param durationMs  The total duration to monitor in milliseconds.
     * @param thresholdMs The threshold for execution delay in milliseconds.
     */
    private static void monitorTask(CriticalTask task, Thread worker, long durationMs, long thresholdMs) {
        long monitoringEndTime = System.currentTimeMillis() + durationMs;

        while (System.currentTimeMillis() < monitoringEndTime) {
            if (task.loopStartTime != Long.MIN_VALUE) {
                long executionTime = System.currentTimeMillis() - task.loopStartTime;
                if (executionTime > thresholdMs) {
                    LOGGER.log(Level.WARNING,
                            String.format("Execution exceeded threshold: %d ms (threshold: %d ms)", executionTime, thresholdMs),
                            StackTrace.forThread(worker));
                }
            }

            try {
                Thread.sleep(20); // Adjust monitoring interval as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warning("Monitoring thread was interrupted.");
                break;
            }
        }
    }
}
