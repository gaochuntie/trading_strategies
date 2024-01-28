package org.example;

public class Trader {
    private double cash = 3000;
    private double gold = 0;
    private double bchain = 0;
    private Strategy strategy;
    double gold_fall_threshold = 0.05;
    double bchain_fall_threshold = 0.05;
    double gold_growth_threshold = 0.1;
    double bchain_growth_threshold = 0.1;

    public Trader(double cash, double gold, double bchain,
                  double gold_fall_threshold,
                  double gold_growth_threshold,
                  double bchain_fall_threshold,
                    double bchain_growth_threshold,
                  DayMarketState first_day) {

        this.cash = cash;
        this.gold = gold;
        this.bchain = bchain;
        this.gold_fall_threshold = gold_fall_threshold;
        this.gold_growth_threshold = gold_growth_threshold;
        this.bchain_fall_threshold = bchain_fall_threshold;
        this.bchain_growth_threshold = bchain_growth_threshold;

        initFirstDay(first_day);
    }

    private void initFirstDay(DayMarketState first_day) {
        this.gold += ((cash / 2) * (1 - DayMarketState.gold_handling_fee)) / (first_day.gold_real);
        this.bchain += ((cash / 2) * (1 - DayMarketState.bchain_handling_fee)) / (first_day.bchain_real);
        this.cash = 0;
    }
    public void notifyMarketState(DayMarketState dayMarketState) {

    }

    public double getBchain_fall_threshold() {
        return bchain_fall_threshold;
    }

    public double getGold_fall_threshold() {
        return gold_fall_threshold;
    }

    public double getBchain_growth_threshold() {
        return bchain_growth_threshold;
    }

    public double getGold_growth_threshold() {
        return gold_growth_threshold;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
