package org.example;

/**
 *
 */
public class Trader {
    private double cash = 3000;
    private double gold = 0;
    private double bchain = 0;
    private Strategy strategy;
    double gold_fall_threshold = 0.05;
    double bchain_fall_threshold = 0.05;
    double gold_growth_threshold = 0.1;
    double bchain_growth_threshold = 0.1;
    DayMarketState history_gold_top = null;
    DayMarketState history_bchain_top = null;

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

    public void notifyMarketState(DayMarketState today) {
        System.out.println("----------------"+today.date+"----------------");

        // B UP
        if(DayMarketState.isGuessBchainPriceGoingUp(today) && (!DayMarketState.isGuessBchainPriceGoingDown(today))){
            System.out.println(today.date+"Bchain is going up");
            if (DayMarketState.isGuessGoldPriceGoingUp(today) && (!DayMarketState.isGuessGoldPriceGoingDown(today))) {
                //G UP
                System.out.println(today.date+"Gold is going up");
                if (DayMarketState.isGuessBchainPriceContinueGrowth_TotallyOverThreshold(today, bchain_growth_threshold, 10)) {
                    // B UPP
                    if (DayMarketState.isGuessGoldPriceContinueGrowth_TotallyOverThreshold(today, gold_growth_threshold, 10)) {
                        //G UPP B UPP
                        return;
                    }else{
                        //B UPP G UP
                        if (DayMarketState.isGuessGoldBuyAvaible(today, this.cash)>0) {
                            buyGold(this.cash, today);
                            return;
                        }
                    }
                }else{
                    //B UP
                    if (DayMarketState.isGuessGoldPriceContinueGrowth_TotallyOverThreshold(today, gold_growth_threshold, 10)) {
                        //B UP G UPP
                        if (DayMarketState.isGuessBchainBuyAvaible(today, this.cash)>0) {
                            buyBchain(this.cash, today);
                            return;
                        }
                    }else{
                        //B UP G UP
                        if (DayMarketState.isGuessBchainBuyAvaible(today, cash / 2)>0) {
                            if (DayMarketState.isGuessGoldBuyAvaible(today, cash / 2)>0) {
                                buyBchain(cash / 2, today);
                                buyGold(cash / 2, today);
                                return;
                            }
                        }
                        double income_gold = DayMarketState.isGuessGoldBuyAvaible(today, cash);
                        double income_bchain = DayMarketState.isGuessBchainBuyAvaible(today, cash);
                        if (income_gold > 0 && income_bchain > 0) {
                            if (income_gold > income_bchain) {
                                buyGold(cash, today);
                            } else {
                                buyBchain(cash, today);
                            }
                            return;
                        }
                        if (income_gold > 0) {
                            buyGold(cash, today);
                            return;
                        }
                        if (income_bchain > 0) {
                            buyBchain(cash, today);
                            return;
                        }

                    }
                }

            }else{
                // B UP G DOWN
                if (DayMarketState.isGuessGoldFallFromHistoryTopOverThreshold(today, history_gold_top, gold_fall_threshold)) {
                    sellGold(gold/2,today);
                }
                if(DayMarketState.isGuessBchainBuyAvaible(today, cash)>0){
                    buyBchain(cash,today);
                }
            }
        }else{
            //B DOWN
            //TODO
        }
    }

    /**
     * shouldn't call on first day
     *
     * @param cash_in
     * @param today
     */
    private void buyGold(double cash_in, DayMarketState today) {
        DayMarketState yesterday = today.previous;
        while (yesterday != null) {
            if (yesterday.gold_real == -1) {
                yesterday = yesterday.previous;
            } else {
                break;
            }
        }
        if (yesterday == null) {
            return;
        }
        this.gold += cash_in * (1 - DayMarketState.gold_handling_fee) / yesterday.gold_real;
        cash -= cash_in;
    }
    private void sellGold(double gold_out, DayMarketState today) {
        DayMarketState yesterday = today.previous;
        while (yesterday != null) {
            if (yesterday.gold_real == -1) {
                yesterday = yesterday.previous;
            } else {
                break;
            }
        }
        if (yesterday == null) {
            return;
        }
        if (gold_out>this.gold) {
            return;
        }
        this.gold -= gold_out;
        cash += gold_out * yesterday.gold_real;
    }
    private void sellBchain(double bchain_out, DayMarketState today) {
        DayMarketState yesterday = today.previous;
        if (yesterday == null) {
            return;
        }
        if (bchain_out>this.bchain) {
            return;
        }
        this.bchain -= bchain_out;
        cash += bchain_out * yesterday.bchain_real;
    }

    private void buyBchain(double cash_in, DayMarketState today) {
        DayMarketState yesterday = today.previous;
        if (yesterday == null) {
            return;
        }
        this.bchain += cash_in * (1 - DayMarketState.bchain_handling_fee) / yesterday.bchain_real;
        cash -= cash_in;
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
}
