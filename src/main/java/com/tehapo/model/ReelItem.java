package com.tehapo.model;

public enum ReelItem {

    GOLD_COIN(0.6), SILVER_COIN(0.4);

    public final double frequency;

    private ReelItem(double frequency) {
        this.frequency = frequency;
    }

}
