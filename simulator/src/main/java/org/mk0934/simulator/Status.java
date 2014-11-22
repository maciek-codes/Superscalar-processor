package org.mk0934.simulator;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */

public enum Status {
    EQ(0),
    LT(1),
    GT(2);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

