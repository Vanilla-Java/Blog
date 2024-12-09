package blog.vanillajava.triviallycopyable;

public final class DefaultBytesMarketData extends MarketData {
    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}