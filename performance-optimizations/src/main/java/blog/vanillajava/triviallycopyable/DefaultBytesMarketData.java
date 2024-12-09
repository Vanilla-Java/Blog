package blog.vanillajava.triviallycopyable;

/**
 * A concrete implementation of {@link MarketData} that uses reflection for determining
 * which fields to serialize and in what order.
 *
 * <p>This class provides a balance between ease of maintenance and efficiency. It is
 * more efficient than using {@code SelfDescribingMarshallable}, but not as efficient
 * as custom hand-rolled {@code readMarshallable} and {@code writeMarshallable} methods.</p>
 *
 * <p>Use this implementation when ease of maintenance is a higher priority than achieving
 * the absolute best performance.</p>
 */
public final class DefaultBytesMarketData extends MarketData {

    /**
     * Indicates whether the serialization format includes self-describing metadata.
     *
     * <p>This implementation disables self-describing messages to optimize performance,
     * assuming the message structure is consistent and known to both sender and receiver.</p>
     *
     * @return {@code false}, indicating that the message is not self-describing.
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}
