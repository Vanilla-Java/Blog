package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.BytesUtil;

import static net.openhft.chronicle.bytes.BytesUtil.*;

/**
 * A concrete implementation of {@link MarketData} optimized for high-performance serialization
 * using the concept of trivially copyable objects.
 *
 * <p>This implementation leverages the {@link BytesUtil} utilities to perform bulk memory
 * operations, avoiding per-field serialization and deserialization. It uses fixed memory
 * offsets and lengths, enabling extremely fast operations by treating the object as a block
 * of bytes.</p>
 *
 * <p>Use this implementation when serialization performance is critical, and the memory
 * layout is known and consistent between sender and receiver.</p>
 *
 * @see BytesUtil
 */
public final class TriviallyCopyableMarketData extends MarketData {

    /**
     * The starting memory offset for serialization/deserialization operations.
     *
     * <p>This is computed at runtime for the {@link TriviallyCopyableMarketData} class and
     * represents the location in memory where the object's data begins.</p>
     */
    static final int START =
            triviallyCopyableStart(TriviallyCopyableMarketData.class);

    /**
     * The total length of the memory block to be serialized/deserialized.
     *
     * <p>This is computed at runtime for the {@link TriviallyCopyableMarketData} class
     * and represents the size of the object in bytes.</p>
     */
    static final int LENGTH =
            triviallyCopyableLength(TriviallyCopyableMarketData.class);

    /**
     * Reads the serialized state of the object from the provided {@link BytesIn} source
     * using a bulk memory operation.
     *
     * <p>This method uses the {@code unsafeReadObject} utility to copy a block of memory
     * into the object, bypassing per-field deserialization.</p>
     *
     * @param bytes the {@link BytesIn} source containing the serialized data.
     */
    @Override
    public void readMarshallable(BytesIn bytes) {
        // Perform a bulk memory read operation for high performance
        bytes.unsafeReadObject(this, START, LENGTH);
    }

    /**
     * Writes the object's state to the provided {@link BytesOut} destination using a bulk
     * memory operation.
     *
     * <p>This method uses the {@code unsafeWriteObject} utility to copy a block of memory
     * from the object, bypassing per-field serialization.</p>
     *
     * @param bytes the {@link BytesOut} destination to write the serialized data to.
     */
    @Override
    public void writeMarshallable(BytesOut bytes) {
        // Perform a bulk memory write operation for high performance
        bytes.unsafeWriteObject(this, START, LENGTH);
    }

    /**
     * Indicates whether the serialization format includes self-describing metadata.
     *
     * <p>This implementation disables self-describing messages to optimize performance,
     * assuming the message structure is predefined and consistent between sender and receiver.</p>
     *
     * @return {@code false}, indicating that the message is not self-describing.
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}
