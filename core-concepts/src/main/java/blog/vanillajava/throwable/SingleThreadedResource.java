package blog.vanillajava.throwable;

import net.openhft.chronicle.core.StackTrace;

/**
 * A class that ensures a resource is accessed by only one thread when assertions are enabled.
 * If the resource is accessed by multiple threads, an exception is thrown with a stack trace
 * showing where the resource was first accessed.
 *
 * <p>Note: This class relies on Java assertions for checking. Ensure assertions
 * are enabled using the `-ea` JVM option. If assertions are disabled, the resource
 * will not enforce single-threaded access.</p>
 */
public class SingleThreadedResource {
    private volatile StackTrace firstUsageStackTrace;
    private Thread owningThread;

    /**
     * Uses the resource. Ensures that the resource is accessed
     * only by the thread that first used it.
     *
     * @throws IllegalStateException if the resource is accessed by a different thread
     *                                after being used by another thread.
     */
    public void use() {
        assert verifySingleThreadedAccess();
        // Add resource usage logic here
    }

    /**
     * Checks that the resource is accessed by only one thread. Records the initial thread
     * and its stack trace on the first access. If accessed by a different thread,
     * throws an exception with details of the initial access.
     *
     * @return true if the resource is accessed by the owning thread or this is the first access.
     * @throws IllegalStateException if the resource is accessed by multiple threads.
     */
    private boolean verifySingleThreadedAccess() {
        Thread currentThread = Thread.currentThread();
        if (owningThread == null) {
            // Record the first thread that uses this resource
            owningThread = currentThread;
            firstUsageStackTrace = new StackTrace("Resource first accessed here");
        } else if (owningThread != currentThread) {
            // Throw an exception if accessed by a different thread
            throw new IllegalStateException(
                String.format("Resource accessed by multiple threads: '%s' (first) and '%s' (current).",
                              owningThread.getName(), currentThread.getName()),
                firstUsageStackTrace
            );
        }
        return true;
    }

    /**
     * Main method demonstrating the use of SingleThreadedResource. This must be run with the -ea JVM option
     *
     * <p>Shows that an exception is thrown if the resource is accessed by a thread other than the initial owning thread.</p>
     *
     * @param args command-line arguments (not used).
     * @throws InterruptedException if the thread is interrupted while joining.
     */
    public static void main(String[] args) throws InterruptedException {
        SingleThreadedResource resource = new SingleThreadedResource();

        // First thread accesses the resource
        Thread thread1 = new Thread(resource::use, "Thread-1");
        thread1.start();
        thread1.join();

        // Main thread tries to access the same resource, causing an exception
        resource.use();
    }
}
