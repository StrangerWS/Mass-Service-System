package com.strangerws.ssu.mss.model.element;

public class Device {

    private boolean busy = false;
    private Requirement serving;

    public boolean isBusy() {
        return busy;
    }

    public Device() {

    }

    public void setServing(Requirement requirement) {
        serving = requirement;
        busy = true;
    }

    public Requirement finishServing() {
        Requirement served = serving;
        serving = null;
        busy = false;
        return served;
    }
}
