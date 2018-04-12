package com.strangerws.ssu.mss;

import com.strangerws.ssu.mss.model.SeviceSystem;
import com.strangerws.ssu.mss.util.Type;

public class Main {
    public static final Type REQUIREMENT_STREAM = Type.EXPONENT;
    public static final Type SERVICE_TIME_RULE = Type.EXPONENT;
    public static final int DEVICES = 1;
    public static final int QUEUE_LENGTH = 4;
    public static final int REQUIREMENT_SOURCES = 5;
    public static final int TEST_COUNT = 1000;

    public static void main(String[] args) {
        SeviceSystem mss = new SeviceSystem(REQUIREMENT_STREAM, SERVICE_TIME_RULE, DEVICES, QUEUE_LENGTH, REQUIREMENT_SOURCES);
        mss.run();
    }
}
