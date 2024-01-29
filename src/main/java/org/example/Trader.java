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
    DayMarketState track_daymarket = null;
    int growth_threshold_days = 5;

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
        track_daymarket = first_day;
        System.out.println("init first day");
        System.out.println("first gold price : " + first_day.gold_real);
        System.out.println("first bchain price : " + first_day.bchain_real);
        if (first_day.gold_real  >0) {
            this.gold += ((cash / 2) * (1 - DayMarketState.gold_handling_fee)) / (first_day.gold_real);
            cash -= cash / 2;
            if (first_day.bchain_real > 0) {
                this.bchain += ((cash) * (1 - DayMarketState.bchain_handling_fee)) / (first_day.bchain_real);
                cash -= cash;}
        }else{
            if (first_day.bchain_real > 0) {
                this.bchain += ((cash / 2) * (1 - DayMarketState.bchain_handling_fee)) / (first_day.bchain_real);
                cash -= cash / 2;}
        }

    }
    public void printAllGoods() {
        System.out.println("cash: " + cash);
        System.out.println("gold: " + gold);
        System.out.println("bchain: " + bchain);
        System.out.println("Total : " + (cash + (gold * track_daymarket.gold_real) + (bchain * track_daymarket.bchain_real)));
    }

    public void notifyMarketState(DayMarketState today) {
        track_daymarket = today;
        System.out.println("\n");
        System.out.println("----------------" + today.date + "----------------");

        // B UP
        if (DayMarketState.isGuessBchainPriceGoingUp(today) && (!DayMarketState.isGuessBchainPriceGoingDown(today))) {
            System.out.println(today.date + "Bchain is going up");
            if (DayMarketState.isGuessGoldPriceGoingUp(today) && (!DayMarketState.isGuessGoldPriceGoingDown(today))) {
                //G UP
                System.out.println(today.date + "Gold is going up");
                if (DayMarketState.isGuessBchainPriceContinueGrowth_TotallyOverThreshold(today, bchain_growth_threshold, growth_threshold_days)) {
                    // B UPP
                    if (DayMarketState.isGuessGoldPriceContinueGrowth_TotallyOverThreshold(today, gold_growth_threshold, growth_threshold_days)) {
                        System.out.println(today.date + "Bchain and Gold are going up continuing");
                        //G UPP B UPP
                        return;
                    } else {
                        //B UPP G UP
                        System.out.println(today.date + "Bchain is going up continuing, Gold is going up");
                        if (DayMarketState.isGuessGoldBuyAvaible(today, this.cash) > 0) {
                            buyGold(this.cash, today);
                            return;
                        }
                    }
                } else {
                    //B UP
                    System.out.println(today.date + "Bchain is going up");
                    if (DayMarketState.isGuessGoldPriceContinueGrowth_TotallyOverThreshold(today, gold_growth_threshold, growth_threshold_days)) {
                        //B UP G UPP
                        System.out.println(today.date + "Bchain is going up, Gold is going up continuing");
                        if (DayMarketState.isGuessBchainBuyAvaible(today, this.cash) > 0) {
                            buyBchain(this.cash, today);
                            return;
                        }
                    } else {
                        //B UP G UP
                        System.out.println(today.date + "Bchain is going up, Gold is going up");
                        if (DayMarketState.isGuessBchainBuyAvaible(today, cash / 2) > 0) {
                            if (DayMarketState.isGuessGoldBuyAvaible(today, cash / 2) > 0) {
                                System.out.println("buy both");
                                buyBchain(cash / 2, today);
                                buyGold(cash / 2, today);
                                return;
                            }
                        }
                        double income_gold = DayMarketState.isGuessGoldBuyAvaible(today, cash);
                        double income_bchain = DayMarketState.isGuessBchainBuyAvaible(today, cash);
                        if (income_gold > 0 && income_bchain > 0) {
                            if (income_gold > income_bchain) {
                                System.out.println("buy gold");
                                buyGold(cash, today);
                            } else {
                                System.out.println("buy bchain");
                                buyBchain(cash, today);
                            }
                            return;
                        }
                        if (income_gold > 0) {
                            System.out.println("buy gold");
                            buyGold(cash, today);
                            return;
                        }
                        if (income_bchain > 0) {
                            System.out.println("buy bchain");
                            buyBchain(cash, today);
                            return;
                        }

                    }
                }

            } else if ((!DayMarketState.isGuessGoldPriceGoingUp(today)) && (DayMarketState.isGuessGoldPriceGoingDown(today))) {
                // B UP G DOWN
                System.out.println(today.date + "Gold is going down,Bchain is going up");
                if (DayMarketState.isGuessGoldFallFromHistoryTopOverThreshold(today, history_gold_top, gold_fall_threshold)) {
                    sellGold(gold / 2, today);
                }
                if (DayMarketState.isGuessBchainBuyAvaible(today, cash) > 0) {
                    buyBchain(cash, today);
                }
            }
        } else if ((!DayMarketState.isGuessBchainPriceGoingUp(today)) && (DayMarketState.isGuessBchainPriceGoingDown(today))) {
            //B DOWN
            System.out.println(today.date + "Bchain is going down");
            if (DayMarketState.isGuessGoldPriceGoingDown(today) && (!DayMarketState.isGuessGoldPriceGoingUp(today))) {
                // B DOWN G DOWN
                System.out.println(today.date + "Gold is going down");
                if (DayMarketState.isGuessGoldFallFromHistoryTopOverThreshold(today, history_gold_top, gold_fall_threshold)) {
                    sellGold(gold / 2, today);
                }
                if (DayMarketState.isGuessBchainFallFromHistoryTopOverThreshold(today, history_bchain_top, bchain_fall_threshold)) {
                    sellBchain(bchain / 2, today);
                }

            } else if ((!DayMarketState.isGuessGoldPriceGoingDown(today)) && (DayMarketState.isGuessGoldPriceGoingUp(today))) {
                // B DOWN G UP
                System.out.println(today.date + "Gold is going up, Bchain is going down");
                if(DayMarketState.isGuessBchainFallFromHistoryTopOverThreshold(today, history_bchain_top, bchain_fall_threshold)) {
                    sellBchain(bchain / 2, today);
                }
                if (DayMarketState.isGuessGoldBuyAvaible(today, cash) > 0) {
                    buyGold(cash, today);
                }

            }
        }
    }

    /**
     * shouldn't call on first day
     *
     * @param cash_in
     * @param today
     */
    private void buyGold(double cash_in, DayMarketState today) {
        System.out.println("Try to buy gold");
        DayMarketState yesterday = today.previous;
        while (yesterday != null) {
            if (yesterday.gold_real == -1) {
                yesterday = yesterday.previous;
            } else {
                break;
            }
        }
        if (yesterday == null) {
            System.out.println("yesterday is null");
            return;
        }
        history_gold_top = today;
        this.gold += cash_in * (1 - DayMarketState.gold_handling_fee) / yesterday.gold_real;
        cash -= cash_in;
        System.out.println("buy gold success");
    }

    private void sellGold(double gold_out, DayMarketState today) {
        System.out.println("Try to sell gold");
        DayMarketState yesterday = today.previous;
        while (yesterday != null) {
            if (yesterday.gold_real == -1) {
                yesterday = yesterday.previous;
            } else {
                break;
            }
        }
        if (yesterday == null) {
            System.out.println("yesterday is null");
            return;
        }
        if (gold_out > this.gold) {
            return;
        }
        this.gold -= gold_out;
        cash += gold_out * yesterday.gold_real;
        System.out.println("sell gold success");
    }

    private void sellBchain(double bchain_out, DayMarketState today) {
        System.out.println("Try to sell bchain");
        DayMarketState yesterday = today.previous;
        if (yesterday == null) {
            System.out.println("yesterday is null");
            return;
        }
        if (bchain_out > this.bchain) {
            return;
        }
        this.bchain -= bchain_out;
        cash += bchain_out * yesterday.bchain_real;
        System.out.println("sell bchain success");
    }

    private void buyBchain(double cash_in, DayMarketState today) {
        System.out.println("Try to buy bchain");
        DayMarketState yesterday = today.previous;
        if (yesterday == null) {
            System.out.println("yesterday is null");
            return;
        }
        history_bchain_top = today;
        this.bchain += cash_in * (1 - DayMarketState.bchain_handling_fee) / yesterday.bchain_real;
        cash -= cash_in;
        System.out.println("buy bchain success");
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
