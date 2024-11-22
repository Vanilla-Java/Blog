package blog.vanillajava.throwable;

import net.openhft.chronicle.core.StackTrace;

import java.io.Closeable;

/**
 * A class that simulates a closeable resource and throws an exception if the resource is used after it has been closed.
 */
public abstract class TracingCloseable implements Closeable {
    private transient StackTrace closedHere;
    private volatile boolean isClosed = false;

    /**
     * Closes the resource and records the stack trace where it was closed.
     */
    @Override
    public void close() {
        if (isClosed) return;
        closedHere = new StackTrace("Resource closed here");
        isClosed = true;
    }

    /**
     * Simulates the usage of the resource. Throws an exception if the resource has already been closed.
     */
    protected void throwIfClosed() {
        if (isClosed) {
            throw new IllegalStateException("Attempted to use a closed resource.", closedHere);
        }
    }

    /**
     * Simulates the usage of the resource. Throws an exception if the resource has already been closed.
     */
    public void use() {
        throwIfClosed();
        // do something with the resource
    }

    /**
     * Main method to demonstrate the usage of the MyCloseable class.
     */
    public static void main(String[] args) throws InterruptedException {
        TracingCloseable resource = new TracingCloseable() {};

        // Start a thread to close the resource
        Thread closer = new Thread(() -> {
            resource.close(); // line 51
        }, "CloserThread");
        closer.start();
        closer.join();

        resource.use(); // Throws exception with stack trace as it's already closed
    }
}