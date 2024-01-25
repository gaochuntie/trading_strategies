package org.example;

public interface Strategy {
    boolean shouldBuyGold(Trader trader, DayMarketState dayMarketState);

    public static boolean doesGoldInterestBiggerThanHandlingFee(double buy_in,DayMarketState dayMarketState) {
        return false;
    }
    public static boolean doesBchainInterestBiggerThanHandlingFee(double buy_in,DayMarketState dayMarketState) {
        return false;
    }

}
