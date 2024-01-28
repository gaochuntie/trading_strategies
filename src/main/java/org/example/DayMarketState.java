package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Opening price = yesterday real price
 * Closing price = today real price
 */
public class DayMarketState {
    public String date = String.valueOf(UUID.randomUUID());
    public double gold_real = -1;
    public double gold_guess = -1;
    public double bchain_real = -1;
    public double bchain_guess = -1;
    public int bchain_index = -1;
    DayMarketState next = null;
    DayMarketState previous = null;
    public static double gold_handling_fee = 0.01;
    public static double bchain_handling_fee = 0.01;

    public DayMarketState(String date, int bchain_index, double gold_real, double gold_guess, double bchain_real, double bchain_guess) {
        this.date = date;
        this.gold_real = gold_real;
        this.gold_guess = gold_guess;
        this.bchain_real = bchain_real;
        this.bchain_guess = bchain_guess;
        this.bchain_index = bchain_index;
    }

    public void setNext(DayMarketState next) {
        this.next = next;
    }

    public DayMarketState getNext() {
        return next;
    }

    public void setPrevious(DayMarketState previous) {
        this.previous = previous;
    }

    public DayMarketState getPrevious() {
        return previous;
    }

    public static boolean isGuessGoldPriceGoingUp(DayMarketState dayMarketState) {
        DayMarketState yesterday=dayMarketState.previous;
        while (yesterday!=null){
            if(yesterday.gold_real==-1){
                yesterday=yesterday.previous;
            }else {
                break;
            }
        }
        if (dayMarketState != null && yesterday != null) {
            if (dayMarketState.gold_guess > yesterday.gold_real) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessBchainPriceGoingUp(DayMarketState dayMarketState) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.bchain_guess > dayMarketState.previous.bchain_real) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessGoldPriceGoingDown(DayMarketState dayMarketState) {
        DayMarketState yesterday=dayMarketState.previous;
        while (yesterday!=null){
            if(yesterday.gold_real==-1){
                yesterday=yesterday.previous;
            }else {
                break;
            }
        }
        if (dayMarketState != null && yesterday != null) {
            if (dayMarketState.gold_guess < yesterday.gold_real) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessBchainPriceGoingDown(DayMarketState dayMarketState) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.bchain_guess < dayMarketState.previous.bchain_real) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessGoldPriceContinueGrowth_TotallyOverThreshold(DayMarketState dayMarketState, double threshold, int days) {
        List<Double> price_history = new ArrayList<>();
        DayMarketState pointer = dayMarketState.previous;
        price_history.add(dayMarketState.gold_guess);
        for (int i = 2; i <= days; i++) {
            if (pointer != null) {
                // market sleep
                if (pointer.gold_real == -1) {
                    i--;
                    pointer = pointer.previous;
                    continue;
                }
                //check is going up
                if (pointer.gold_real >= price_history.get(i - 2)) {
                    return false;
                }
                price_history.add(pointer.gold_real);
                pointer = pointer.previous;
            } else {
                return false;
            }
        }
        if (price_history.get(0) > price_history.get(days - 1) * (1 + threshold)) {
            return true;
        }
        return false;
    }

    public static boolean isGuessBchainPriceContinueGrowth_TotallyOverThreshold(DayMarketState dayMarketState, double threshold, int days) {
        List<Double> price_history = new ArrayList<>();
        DayMarketState pointer = dayMarketState.previous;
        price_history.add(dayMarketState.bchain_guess);
        for (int i = 2; i <= days; i++) {
            if (pointer != null) {
                // market sleep
                if (pointer.bchain_real == -1) {
                    i--;
                    pointer = pointer.previous;
                    continue;
                }
                //check is going up
                if (pointer.bchain_real >= price_history.get(i - 2)) {
                    return false;
                }
                price_history.add(pointer.bchain_real);
                pointer = pointer.previous;
            } else {
                return false;
            }
        }
        if (price_history.get(0) > price_history.get(days - 1) * (1 + threshold)) {
            return true;
        }
        return false;
    }

    public static boolean isGuessBchainPriceGrowthOverThreshold(DayMarketState dayMarketState, double threshold) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.bchain_guess > dayMarketState.previous.bchain_real * (1 + threshold)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessGoldPriceGrowthOverThreshold(DayMarketState dayMarketState, double threshold) {
        DayMarketState yesterday=dayMarketState.previous;
        while (yesterday!=null){
            if(yesterday.gold_real==-1){
                yesterday=yesterday.previous;
            }else {
                break;
            }
        }
        if (dayMarketState != null && yesterday != null) {
            if (dayMarketState.gold_guess > yesterday.gold_real * (1 + threshold)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessGoldFallOverThreshold(DayMarketState dayMarketState, double threshold) {
        DayMarketState yesterday=dayMarketState.previous;
        while (yesterday!=null){
            if(yesterday.gold_real==-1){
                yesterday=yesterday.previous;
            }else {
                break;
            }
        }
        if (dayMarketState != null && yesterday != null) {
            if (dayMarketState.gold_guess < yesterday.gold_real * (1 - threshold)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessBchainFallOverThreshold(DayMarketState dayMarketState, double threshold) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.bchain_guess < dayMarketState.previous.bchain_real * (1 - threshold)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessGoldFallFromHistoryTopOverThreshold(DayMarketState today, DayMarketState history_top, double threshold) {
        if (today != null && history_top != null) {
            if (history_top.gold_real <= 0) {
                return false;
            }
            if (today.gold_guess < history_top.gold_real * (1 - threshold)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuessBchainFallFromHistoryTopOverThreshold(DayMarketState today, DayMarketState history_top, double threshold) {
        if (today != null && history_top != null) {
            if (today.bchain_guess < history_top.bchain_real * (1 - threshold)) {
                return true;
            }
        }
        return false;
    }

    /**
     * can't be called at first day !!!!!!!!!!!!!!
     *
     * @param today
     * @param cash
     * @return
     */
    public static double isGuessGoldBuyAvaible(DayMarketState today, double cash) {
        if (cash <= 0 || today == null) {
            return -1;
        }
        DayMarketState yesterday = today.previous;
        while (yesterday != null) {
            if (yesterday.gold_real == -1) {
                yesterday = yesterday.previous;
            } else {
                break;
            }
        }
        if (yesterday != null) {
            return  cash*((gold_handling_fee) - ((1 - gold_handling_fee) * (today.gold_guess / yesterday.gold_real - 1))) ;

        }
        return -1;
    }
    public static double isGuessBchainBuyAvaible(DayMarketState today, double cash) {
        if (cash <= 0 || today == null) {
            return -1;
        }
        DayMarketState yesterday = today.previous;
        if (yesterday != null) {
            return ((bchain_handling_fee) - ((1 - bchain_handling_fee) * (today.bchain_guess / yesterday.bchain_real - 1)));

        }
        return -1;
    }
}
