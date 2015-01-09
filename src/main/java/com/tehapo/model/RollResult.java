package com.tehapo.model;

import java.util.Arrays;

public final class RollResult {

    public final ReelItem[] result;

    public RollResult(ReelItem... result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.result);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RollResult other = (RollResult) obj;
        if (!Arrays.equals(result, other.result))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RollResult [result=" + Arrays.toString(result) + "]";
    }

}
