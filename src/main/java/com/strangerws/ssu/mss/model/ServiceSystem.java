package com.strangerws.ssu.mss.model;

import com.strangerws.ssu.mss.model.element.Device;
import com.strangerws.ssu.mss.model.element.Generator;
import com.strangerws.ssu.mss.model.element.Randomizer;
import com.strangerws.ssu.mss.model.element.Requirement;
import com.strangerws.ssu.mss.util.Type;
import javafx.util.Pair;

import java.util.*;

public class ServiceSystem {
    private Randomizer streamRandomizer;
    private Randomizer serviceRandomizer;
    List<Device> devices;
    private Generator generator;
    Queue<Requirement> queueWaiting;
    Queue<Requirement> queueServed;
    Map<Integer, Integer> exactCount = new HashMap<>();
    Pair<Integer, Integer> requirementsNow;

    double time = 0d;
    private double generationTime = 0d;
    private double serviceTime = Double.MAX_VALUE;
    private double exitTime = Double.MAX_VALUE;
    double expectation = 0d;
    private Requirement serving;

    public ServiceSystem(Type streamRule, Type serviceRule, int deviceCount, int queueLength, Generator generator) {

        streamRandomizer = new Randomizer(streamRule, true);
        serviceRandomizer = new Randomizer(serviceRule, false);
        queueWaiting = new ArrayDeque<>(queueLength);
        queueServed = new ArrayDeque<>();
        devices = new ArrayList<>(deviceCount);
        this.generator = generator;

        for (int i = 0; i < deviceCount; i++) {
            devices.add(new Device());
        }
    }

    public void run() {
        if (time >= generationTime) {
            generationCycle();
            requirementsNow = calculateRequirementsInSystem();
            serviceTime = time;
        }

        if (time >= serviceTime) {
            serviceCycle();
        }
        if (time >= exitTime) {
            exitCycle();
        }
        time = getNextActionTime();
    }

    private double getNextActionTime() {
        List<Double> times = new ArrayList<>();
        times.add(generationTime);
        times.add(serviceTime);
        times.add(exitTime);
        return Collections.min(times);
    }

    private void generationCycle() {
        if (!generator.getElements().isEmpty()) {
            Requirement tmp = generator.getElements().poll();
            tmp.setArriveTime(time);
            generationTime = time + streamRandomizer.getTimestamp();
            serving = tmp;
        }
    }

    private void serviceCycle() {
        if (serving != null) {
            for (Device device : devices) {
                if (!device.isBusy()) {
                    serving.setServiceTime(time);
                    exitTime = time + serviceRandomizer.getTimestamp();
                    device.setServing(serving);
                    serving = null;
                    break;
                }
            }
        }
        serviceTime = Double.MAX_VALUE;
    }

    private void exitCycle() {
        for (Device device : devices) {
            if (device.isBusy()) {
                Requirement tmp = device.finishServing();
                tmp.setExitTime(time);
                serviceTime = time;
                expectation += tmp.getExitTime() - tmp.getArriveTime();
                queueServed.add(tmp);
                exitTime = Double.MAX_VALUE;
            }
        }
    }

    private Pair<Integer, Integer> calculateRequirementsInSystem() {
        int requirementsInSystem = 0;

        requirementsInSystem += queueWaiting.size();
        for (Device device : devices) {
            if (device.isBusy()) requirementsInSystem++;
        }

        int times = 1;
        if (exactCount.containsKey(requirementsInSystem)) {
            times = exactCount.remove(requirementsInSystem);
            times++;

        }
        exactCount.put(requirementsInSystem, times);
        return new Pair<>(requirementsInSystem, times);
    }

}
