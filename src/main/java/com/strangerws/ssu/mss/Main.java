package com.strangerws.ssu.mss;

import com.strangerws.ssu.mss.model.ServiceNet;
import com.strangerws.ssu.mss.util.Type;

public class Main {
    public static final Type REQUIREMENT_STREAM = Type.EXPONENT;
    public static final Type SERVICE_TIME_RULE = Type.EXPONENT;
    public static final int DEVICES = 1;
    public static final int QUEUE_LENGTH = 4;
    public static final int REQUIREMENT_SOURCES = 5;
    public static final int TEST_COUNT = 1000;

    public static void main(String[] args) {
        ServiceNet msn = new ServiceNet();
        msn.run();
    }
}
