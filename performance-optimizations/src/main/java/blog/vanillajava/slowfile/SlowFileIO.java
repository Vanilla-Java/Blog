package blog.vanillajava.slowfile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.stream.Stream;

/*
Run 1, Write speed: 0.900 GB/sec, read speed 0.832 GB/sec
Run 2, Write speed: 0.918 GB/sec, read speed 1.208 GB/sec
Run 3, Write speed: 0.933 GB/sec, read speed 1.197 GB/sec
 */
/**
 * A benchmark program for testing write and read speeds of file IO.
 * The program writes a 2GB text file with repeated 50-character lines and then reads it back to measure performance.
 */
public class SlowFileIO {

    private static final String TEXT = "01234567890123456789012345678901234567890123456789"; // 50 characters
    private static final long FILE_SIZE_IN_BYTES = 2_000_000_000; // 2 GB
    public static final String FILE_NAME = "deleteme.txt";

    public static void main(String[] args) {
        File directory = determineWorkingDirectory();

        for (int run = 1; run <= 3; run++) {
            try {
                runWriterReaderBenchmark(directory, TEXT, run);
            } catch (IOException e) {
                System.err.printf("Run %d failed: %s%n", run, e.getMessage());
            }
        }
    }

    /**
     * Determines the working directory for the benchmark.
     * If "/dev/shm" is not available, defaults to the current directory.
     *
     * @return the working directory as a {@link File}
     */
    private static File determineWorkingDirectory() {
        File path = new File("/dev/shm");
        return path.isDirectory() ? path : new File(".");
    }

    /**
     * Runs a single benchmark iteration to measure write and read speeds.
     *
     * @param path the directory to perform the file IO operations
     * @param text the content to write repeatedly to the file
     * @param run the run number for benchmarking
     * @throws IOException if an IO error occurs
     */
    private static void runWriterReaderBenchmark(File path, String text, int run) throws IOException {
        File file = new File(path, FILE_NAME);

        // Measure write performance
        long start = System.nanoTime();
        writeToFile(file, text, FILE_SIZE_IN_BYTES);

        long mid = System.nanoTime();
        // Measure read performance
        long totalBytesRead = readFromFile(file);

        long end = System.nanoTime();
        // Ensure the file size matches expectations
        if (totalBytesRead != FILE_SIZE_IN_BYTES) {
            throw new IOException("File size mismatch: expected " + FILE_SIZE_IN_BYTES + " bytes, but read " + totalBytesRead);
        }

        // Delete the file after the benchmark
        if (!file.delete()) {
            System.err.println("Warning: Failed to delete benchmark file.");
        }

        // bytes per nanosecond == GB/second
        System.out.printf("Run %d, Write speed: %.3f GB/sec, read speed %.3f GB/sec%n",
                run,
                (double) FILE_SIZE_IN_BYTES / (mid - start),
                (double) FILE_SIZE_IN_BYTES / (end - mid));
    }

    /**
     * Writes a large file with repeated text lines.
     *
     * @param file            the file to write to
     * @param text            the text to write repeatedly
     * @param fileSizeInBytes the size of the lines of file in bytes
     * @throws IOException if an IO error occurs
     */
    private static void writeToFile(File file, String text, long fileSizeInBytes) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            long writtenBytes = 0;
            while (writtenBytes < fileSizeInBytes) {
                writer.println(text);
                writtenBytes += text.length();
            }
        }
    }

    /**
     * Reads a file and calculates the total size in bytes by summing the length of each line.
     *
     * @param file the file to read from
     * @return the total number of bytes read in all lines
     * @throws IOException if an IO error occurs
     */
    private static long readFromFile(File file) throws IOException {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.mapToLong(String::length).sum();
        }
    }
}
