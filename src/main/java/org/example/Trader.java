package org.example;

public class Trader {
    private double cash = 3000;
    private double gold = 0;
    private double bchain = 0;
    private Strategy strategy;
    public Trader(double cash, double gold, double bchain, Strategy strategy) {
        this.cash = cash;
        this.gold = gold;
        this.bchain = bchain;
        this.strategy = strategy;
    }
    public void notifyMarketState(DayMarketState dayMarketState) {

    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
