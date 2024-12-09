package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.NanoTime;
import net.openhft.chronicle.wire.converter.ShortText;

/**
 * Represents market data for financial instruments, including bid and ask prices and quantities.
 * This class is designed for high-performance serialization and deserialization using Chronicle Wire.
 * Fields are primarily primitive types for efficient memory layout and fast serialization.
 *
 * <p>Serialization uses annotations to apply data converters where needed:
 * <ul>
 *   <li>{@code @ShortText} for short text conversion of the security ID.</li>
 *   <li>{@code @NanoTime} for timestamp representation in nanoseconds.</li>
 * </ul>
 *
 * <p>This abstract class can be extended to define additional behaviors or specialized data fields.
 *
 * @see SelfDescribingMarshallable
 */
abstract class MarketData extends SelfDescribingMarshallable {

    /**
     * The unique identifier for the security, stored as a long.
     * This field uses the {@code @ShortText} converter for optimized serialization of short text values.
     */
    @ShortText
    long securityId;

    /**
     * The timestamp for the market data, represented in nanoseconds since the epoch.
     * This field uses the {@code @NanoTime} converter for efficient serialization of time data.
     */
    @NanoTime
    long time;

    /**
     * Bid quantities at different levels of the order book.
     * Each value is stored as a 32-bit integer for efficient representation.
     */
    int bidQty0, bidQty1, bidQty2, bidQty3;

    /**
     * Ask quantities at different levels of the order book.
     * Each value is stored as a 32-bit integer for efficient representation.
     */
    int askQty0, askQty1, askQty2, askQty3;

    /**
     * Bid prices at different levels of the order book.
     * Each value is stored as a 64-bit double for precision.
     */
    double bidPrice0, bidPrice1, bidPrice2, bidPrice3;

    /**
     * Ask prices at different levels of the order book.
     * Each value is stored as a 64-bit double for precision.
     */
    double askPrice0, askPrice1, askPrice2, askPrice3;

    // Note: Getters and setters are not shown for clarity. Consider adding them
    // if external access to these fields is required in a controlled manner.
}
