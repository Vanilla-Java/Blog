package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;

public final class ExplicitMarketData extends MarketData {

    @Override
    public void readMarshallable(BytesIn bytes) {
        securityId = bytes.readLong();
        time = bytes.readLong();
        bidQty0 = bytes.readDouble();
        bidQty1 = bytes.readDouble();
        bidQty2 = bytes.readDouble();
        bidQty3 = bytes.readDouble();
        askQty0 = bytes.readDouble();
        askQty1 = bytes.readDouble();
        askQty2 = bytes.readDouble();
        askQty3 = bytes.readDouble();
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
        bytes.writeDouble(bidQty0);
        bytes.writeDouble(bidQty1);
        bytes.writeDouble(bidQty2);
        bytes.writeDouble(bidQty3);
        bytes.writeDouble(askQty0);
        bytes.writeDouble(askQty1);
        bytes.writeDouble(askQty2);
        bytes.writeDouble(askQty3);
        bytes.writeDouble(bidPrice0);
        bytes.writeDouble(bidPrice1);
        bytes.writeDouble(bidPrice2);
        bytes.writeDouble(bidPrice3);
        bytes.writeDouble(askPrice0);
        bytes.writeDouble(askPrice1);
        bytes.writeDouble(askPrice2);
        bytes.writeDouble(askPrice3);
    }

}