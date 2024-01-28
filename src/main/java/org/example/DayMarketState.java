package org.example;

import java.util.UUID;

public class DayMarketState {
    public String date = String.valueOf(UUID.randomUUID());
    public double gold_real=-1;
    public double gold_guess=-1;
    public double bchain_real=-1;
    public double bchain_guess=-1;
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
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.gold_guess> dayMarketState.previous.gold_real) {
                return true;
            }
        }
        return false;
    }
    public static boolean isGuessBchainPriceGoingUp(DayMarketState dayMarketState) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.bchain_guess> dayMarketState.previous.bchain_real) {
                return true;
            }
        }
        return false;
    }
    public static boolean isGuessGoldPriceGoingDown(DayMarketState dayMarketState) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.gold_guess< dayMarketState.previous.gold_real) {
                return true;
            }
        }
        return false;
    }
    public static boolean isGuessBchainPriceGoingDown(DayMarketState dayMarketState) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.bchain_guess< dayMarketState.previous.bchain_real) {
                return true;
            }
        }
        return false;
    }
    public static boolean isGuessGoldPriceGrowthOverThreshold(DayMarketState dayMarketState, double threshold) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.gold_guess > dayMarketState.previous.gold_real * (1 + threshold)) {
                return true;
            }
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
    public static boolean isGuessGoldFallOverThreshold(DayMarketState dayMarketState, double threshold) {
        if (dayMarketState != null && dayMarketState.previous != null) {
            if (dayMarketState.gold_guess < dayMarketState.previous.gold_real * (1 - threshold)) {
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
}
