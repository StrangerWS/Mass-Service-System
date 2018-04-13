package com.strangerws.ssu.mss.model.element;

import java.util.ArrayDeque;
import java.util.Queue;

public class Generator {
    private double nextTime = 0;
    private Queue<Requirement> elements;

    public Queue<Requirement> getElements() {
        return elements;
    }

    public void setElements(Queue<Requirement> elements) {
        this.elements = elements;
    }

    public double getNextTime() {
        return nextTime;
    }

    public void setNextTime(double nextTime) {
        this.nextTime = nextTime;
    }

    public Generator(int elementsLength) {
        elements = new ArrayDeque<>(elementsLength);
        for (int i = 0; i < elementsLength; i++) {
            elements.add(new Requirement());
        }
    }

}
