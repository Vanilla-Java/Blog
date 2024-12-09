package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.BytesStore;

/**
 * A concrete implementation of {@link MarketData} that provides direct memory-based
 * serialization and deserialization for optimal performance.
 *
 * <p>This implementation avoids reflection and field-by-field operations by directly
 * accessing the memory layout of the object using {@link BytesStore}. The fields are
 * serialized and deserialized based on their fixed positions in memory, enabling
 * extremely fast bulk operations.</p>
 *
 * <p>Use this implementation when maximum serialization performance is required, and
 * the memory layout is known and consistent between the sender and receiver.</p>
 */
public final class DirectMarketData extends MarketData {

    /**
     * Reads the serialized state of the object from the provided {@link BytesIn} source.
     *
     * <p>This method uses a direct memory-based approach to deserialize fields from their
     * fixed positions in memory. This eliminates the overhead of reflection or explicit
     * field-by-field reads.</p>
     *
     * @param bytes the {@link BytesIn} source containing the serialized data.
     */
    @Override
    public void readMarshallable(BytesIn bytes) {
        // Access the underlying byte store for direct memory operations
        BytesStore<?, ?> bs = ((Bytes<?>) bytes).bytesStore();
        long position = bytes.readPosition();

        // Skip the full length of the serialized data
        bytes.readSkip(112);

        // Read primitive fields directly from memory at fixed offsets
        securityId = bs.readLong(position + 0);
        time = bs.readLong(position + 8);
        bidQty0 = bs.readInt(position + 16);
        bidQty1 = bs.readInt(position + 20);
        bidQty2 = bs.readInt(position + 24);
        bidQty3 = bs.readInt(position + 28);
        askQty0 = bs.readInt(position + 32);
        askQty1 = bs.readInt(position + 36);
        askQty2 = bs.readInt(position + 40);
        askQty3 = bs.readInt(position + 44);
        bidPrice0 = bs.readDouble(position + 48);
        bidPrice1 = bs.readDouble(position + 56);
        bidPrice2 = bs.readDouble(position + 64);
        bidPrice3 = bs.readDouble(position + 72);
        askPrice0 = bs.readDouble(position + 80);
        askPrice1 = bs.readDouble(position + 88);
        askPrice2 = bs.readDouble(position + 96);
        askPrice3 = bs.readDouble(position + 104);
    }

    /**
     * Writes the object's state to the provided {@link BytesOut} destination.
     *
     * <p>This method uses a direct memory-based approach to serialize fields to fixed
     * positions in memory, ensuring consistent and efficient data layout.</p>
     *
     * @param bytes the {@link BytesOut} destination to write the serialized data to.
     */
    @Override
    public void writeMarshallable(BytesOut bytes) {
        // Access the underlying byte store for direct memory operations
        BytesStore<?, ?> bs = ((Bytes<?>) bytes).bytesStore();
        long position = bytes.writePosition();

        // Reserve space for the full length of the serialized data
        bytes.writeSkip(112);

        // Write primitive fields directly to memory at fixed offsets
        bs.writeLong(position + 0, securityId);
        bs.writeLong(position + 8, time);
        bs.writeInt(position + 16, bidQty0);
        bs.writeInt(position + 20, bidQty1);
        bs.writeInt(position + 24, bidQty2);
        bs.writeInt(position + 28, bidQty3);
        bs.writeInt(position + 32, askQty0);
        bs.writeInt(position + 36, askQty1);
        bs.writeInt(position + 40, askQty2);
        bs.writeInt(position + 44, askQty3);
        bs.writeDouble(position + 48, bidPrice0);
        bs.writeDouble(position + 56, bidPrice1);
        bs.writeDouble(position + 64, bidPrice2);
        bs.writeDouble(position + 72, bidPrice3);
        bs.writeDouble(position + 80, askPrice0);
        bs.writeDouble(position + 88, askPrice1);
        bs.writeDouble(position + 96, askPrice2);
        bs.writeDouble(position + 104, askPrice3);
    }

    /**
     * Indicates whether the serialization format includes self-describing metadata.
     *
     * <p>This implementation disables self-describing messages to maximize performance,
     * assuming the message structure is predefined and known to both sender and receiver.</p>
     *
     * @return {@code false}, indicating that the message is not self-describing.
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}
