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
}
