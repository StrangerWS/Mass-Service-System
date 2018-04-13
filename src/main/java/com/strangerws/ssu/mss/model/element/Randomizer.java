package com.strangerws.ssu.mss.model.element;

import com.strangerws.ssu.mss.util.Type;

import java.util.Random;

public class Randomizer {

    private static final double LAMBDA = 0.3d;
    private static final double NU = 0.35d;
    private final boolean forArrive;

    private Type randomType;

    public Randomizer(Type randomType, boolean forArrive) {
        this.randomType = randomType;
        this.forArrive = forArrive;
    }

    public double getTimestamp() {//for arrive or for serving boolean
        double result = 0;
        switch (randomType) {
            case EXPONENT:
                result = forArrive ? (-1d / LAMBDA) * Math.log(Math.random()) : (-1d / NU) * Math.log(Math.random());
                System.out.println(result);
                return result;
            default:
                return result;
        }
    }
}
