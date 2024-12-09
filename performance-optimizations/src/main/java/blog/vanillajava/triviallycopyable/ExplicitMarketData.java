package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;

/**
 * A concrete implementation of {@link MarketData} that provides explicit serialization
 * and deserialization methods for high performance.
 *
 * <p>This class avoids reflection and uses explicit field-level read and write operations
 * for efficiency. The serialization format is fixed and assumes the structure is consistent
 * between the sender and receiver.</p>
 *
 * <p>Use this implementation when field-level control over serialization is required, such as
 * in performance-critical applications.</p>
 */
public final class ExplicitMarketData extends MarketData {

    /**
     * Reads the serialized state of the object from the provided {@link BytesIn} source.
     *
     * <p>This method explicitly reads each field in the order they were written, ensuring
     * a consistent and predictable deserialization process.</p>
     *
     * @param bytes the {@link BytesIn} source containing the serialized data.
     */
    @Override
    public void readMarshallable(BytesIn bytes) {
        // Read the security ID and timestamp
        securityId = bytes.readLong();
        time = bytes.readLong();

        // Read bid quantities
        bidQty0 = bytes.readInt();
        bidQty1 = bytes.readInt();
        bidQty2 = bytes.readInt();
        bidQty3 = bytes.readInt();

        // Read ask quantities
        askQty0 = bytes.readInt();
        askQty1 = bytes.readInt();
        askQty2 = bytes.readInt();
        askQty3 = bytes.readInt();

        // Read bid prices
        bidPrice0 = bytes.readDouble();
        bidPrice1 = bytes.readDouble();
        bidPrice2 = bytes.readDouble();
        bidPrice3 = bytes.readDouble();

        // Read ask prices
        askPrice0 = bytes.readDouble();
        askPrice1 = bytes.readDouble();
        askPrice2 = bytes.readDouble();
        askPrice3 = bytes.readDouble();
    }

    /**
     * Writes the object's state to the provided {@link BytesOut} destination.
     *
     * <p>This method explicitly writes each field in a fixed order, ensuring a consistent
     * and predictable serialization process.</p>
     *
     * @param bytes the {@link BytesOut} destination to write the serialized data to.
     */
    @Override
    public void writeMarshallable(BytesOut bytes) {
        // Write the security ID and timestamp
        bytes.writeLong(securityId);
        bytes.writeLong(time);

        // Write bid quantities
        bytes.writeInt(bidQty0);
        bytes.writeInt(bidQty1);
        bytes.writeInt(bidQty2);
        bytes.writeInt(bidQty3);

        // Write ask quantities
        bytes.writeInt(askQty0);
        bytes.writeInt(askQty1);
        bytes.writeInt(askQty2);
        bytes.writeInt(askQty3);

        // Write bid prices
        bytes.writeDouble(bidPrice0);
        bytes.writeDouble(bidPrice1);
        bytes.writeDouble(bidPrice2);
        bytes.writeDouble(bidPrice3);

        // Write ask prices
        bytes.writeDouble(askPrice0);
        bytes.writeDouble(askPrice1);
        bytes.writeDouble(askPrice2);
        bytes.writeDouble(askPrice3);
    }

    /**
     * Indicates whether the serialization format includes self-describing metadata.
     *
     * <p>This implementation disables self-describing messages to optimize performance,
     * assuming the message structure is fixed and known to both sender and receiver.</p>
     *
     * @return {@code false}, indicating that the message is not self-describing.
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}
