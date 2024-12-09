package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

abstract class MarketData extends SelfDescribingMarshallable {

    long securityId;
    long time;

    // bid and ask quantities
    int bidQty0, bidQty1, bidQty2, bidQty3;
    int askQty0, askQty1, askQty2, askQty3;

    // bid and ask prices
    double bidPrice0, bidPrice1, bidPrice2, bidPrice3;
    double askPrice0, askPrice1, askPrice2, askPrice3;

    // Getters and setters not shown for clarity

}