package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;

import static net.openhft.chronicle.bytes.BytesUtil.*;

public final class TriviallyCopyableMarketData extends MarketData {

    static final int START =
            triviallyCopyableStart(TriviallyCopyableMarketData.class);

    static final int LENGTH =
            triviallyCopyableLength(TriviallyCopyableMarketData.class);

    @Override
    public void readMarshallable(BytesIn bytes) {
        bytes.unsafeReadObject(this, START, LENGTH);
    }

    @Override
    public void writeMarshallable(BytesOut bytes) {
        bytes.unsafeWriteObject(this, START, LENGTH);
    }

    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}