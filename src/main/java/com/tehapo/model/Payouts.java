package com.tehapo.model;

import java.util.HashMap;
import java.util.Map;

public class Payouts {

    private static final Map<RollResult, Integer> payoutMap = new HashMap<RollResult, Integer>();

    static {
        payoutMap.put(new RollResult(ReelItem.GOLD_COIN, ReelItem.GOLD_COIN,
                ReelItem.GOLD_COIN), 10);
        payoutMap.put(new RollResult(ReelItem.SILVER_COIN,
                ReelItem.SILVER_COIN, ReelItem.SILVER_COIN), 5);
    }

    public static Integer getPayout(RollResult roll) {
        return payoutMap.get(roll);
    }

}
