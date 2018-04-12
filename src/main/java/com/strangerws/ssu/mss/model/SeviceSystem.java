package com.strangerws.ssu.mss.model;

import com.strangerws.ssu.mss.Main;
import com.strangerws.ssu.mss.model.element.Device;
import com.strangerws.ssu.mss.model.element.Randomizer;
import com.strangerws.ssu.mss.model.element.Requirement;
import com.strangerws.ssu.mss.util.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SeviceSystem {

    private Randomizer streamRandomizer;
    private Randomizer serviceRandomizer;
    private List<Device> devices;
    private Queue<Requirement> queueWaiting;
    private Queue<Requirement> queueServed;
    private Queue<Requirement> sourceQueue;

    private double time = 0d;
    private double generationTime = 0d;
    private double expectation = 0d;

    private int sources;
    private int remainingStream;

    public SeviceSystem(Type streamRule, Type serviceRule, int deviceCount, int queueLength, int sources) {
        remainingStream = Main.TEST_COUNT;

        streamRandomizer = new Randomizer(streamRule, true);
        serviceRandomizer = new Randomizer(serviceRule, false);
        queueWaiting = new ArrayDeque<>(queueLength);
        queueServed = new ArrayDeque<>();
        devices = new ArrayList<>(deviceCount);
        this.sources = sources;

        for (int i = 0; i < deviceCount; i++) {
            devices.add(new Device());
        }

        sourceQueue = new ArrayDeque<>(remainingStream);
        for (int i = 0; i < remainingStream; i++) {
            sourceQueue.add(new Requirement());
        }
    }

    public void run() {
        generationCycle();
        System.out.println("finished");
        calculate();
    }

    private void generationCycle() {
        for (int i = 0; i < sources; i++) {
            Requirement tmp = sourceQueue.poll();
            if (tmp == null) break;
            if (queueWaiting.size() < Main.QUEUE_LENGTH) {
                time += streamRandomizer.getTimestamp();
                tmp.setArriveTime(time);
                queueWaiting.add(tmp);
            }
            time += streamRandomizer.getTimestamp();
            generationTime = time + streamRandomizer.getTimestamp();
        }
        serviceCycle();
    }

    private void serviceCycle() {
        while (queueWaiting.size() > 0) {
            for (Device device : devices) {
                if (!device.isBusy()) {
                    Requirement tmp = queueWaiting.poll();
                    increaseTime(serviceRandomizer.getTimestamp());
                    tmp.setServiceTime(time);
                    device.setServing(tmp);
                }
            }
            exitCycle();
        }
    }

    private void exitCycle() {
        for (Device device : devices) {
            Requirement tmp = device.finishServing();
            increaseTime(serviceRandomizer.getTimestamp());
            tmp.setExitTime(time);
            expectation += tmp.getExitTime() - tmp.getArriveTime();
            queueServed.add(tmp);
        }
    }

    private void increaseTime(double timestamp) {
        time += timestamp;
        if (time >= generationTime) {
            generationCycle();
        }
    }

    private void calculate() {
        int served = queueServed.size();

        expectation /= served;

        System.out.println("Expectation: " + expectation + "\nServed " + served);
    }
}
