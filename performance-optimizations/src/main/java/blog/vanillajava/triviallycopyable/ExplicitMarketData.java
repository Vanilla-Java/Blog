package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;

public final class ExplicitMarketData extends MarketData {

    @Override
    public void readMarshallable(BytesIn bytes) {
        securityId = bytes.readLong();
        time = bytes.readLong();
        bidQty0 = bytes.readInt();
        bidQty1 = bytes.readInt();
        bidQty2 = bytes.readInt();
        bidQty3 = bytes.readInt();
        askQty0 = bytes.readInt();
        askQty1 = bytes.readInt();
        askQty2 = bytes.readInt();
        askQty3 = bytes.readInt();
        bidPrice0 = bytes.readDouble();
        bidPrice1 = bytes.readDouble();
        bidPrice2 = bytes.readDouble();
        bidPrice3 = bytes.readDouble();
        askPrice0 = bytes.readDouble();
        askPrice1 = bytes.readDouble();
        askPrice2 = bytes.readDouble();
        askPrice3 = bytes.readDouble();
    }

    @Override
    public void writeMarshallable(BytesOut bytes) {
        bytes.writeLong(securityId);
        bytes.writeLong(time);
        bytes.writeInt(bidQty0);
        bytes.writeInt(bidQty1);
        bytes.writeInt(bidQty2);
        bytes.writeInt(bidQty3);
        bytes.writeInt(askQty0);
        bytes.writeInt(askQty1);
        bytes.writeInt(askQty2);
        bytes.writeInt(askQty3);
        bytes.writeDouble(bidPrice0);
        bytes.writeDouble(bidPrice1);
        bytes.writeDouble(bidPrice2);
        bytes.writeDouble(bidPrice3);
        bytes.writeDouble(askPrice0);
        bytes.writeDouble(askPrice1);
        bytes.writeDouble(askPrice2);
        bytes.writeDouble(askPrice3);
    }

    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}