package org.example;

public class Trader {
    private double cash = 3000;
    private double gold = 0;
    private double bchain = 0;

    public boolean buyGold(double price) {
        if (cash >= price) {
            gold += 1;
            cash -= price;
            return true;
        }
        return false;
    }
}
