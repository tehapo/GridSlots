package com.tehapo.model;

public enum ReelItem {

    GOLD_COIN(0.3), SILVER_COIN(0.7);

    public final double frequency;

    private ReelItem(double frequency) {
        this.frequency = frequency;
    }

}
