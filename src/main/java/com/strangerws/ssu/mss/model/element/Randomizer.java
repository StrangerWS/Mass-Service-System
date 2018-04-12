package com.strangerws.ssu.mss.model.element;

import com.strangerws.ssu.mss.util.Type;

public class Randomizer {

    private static final double LAMBDA = 0.9d;
    private static final double NU = 0.95d;
    private final boolean forArrive;

    private Type randomType;

    public Randomizer(Type randomType, boolean forArrive) {
        this.randomType = randomType;
        this.forArrive = forArrive;
    }

    public double getTimestamp() {//for arrive or for serving boolean
        switch (randomType) {
            case EXPONENT:
                return forArrive ? (-1d / LAMBDA) * Math.log(Math.random()) : (-1d / NU) * Math.log(Math.random());
            default:
                return 0;
        }
    }
}
