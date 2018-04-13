package com.strangerws.ssu.mss.model;

import com.strangerws.ssu.mss.Main;
import com.strangerws.ssu.mss.model.element.Device;
import com.strangerws.ssu.mss.model.element.Generator;
import com.strangerws.ssu.mss.model.element.Randomizer;
import com.strangerws.ssu.mss.model.element.Requirement;
import com.strangerws.ssu.mss.util.Type;

import java.util.*;

public class ServiceSystem {
    private Randomizer streamRandomizer;
    private Randomizer serviceRandomizer;
    private List<Device> devices;
    private List<Generator> generators;
    private Queue<Requirement> queueWaiting;
    private Queue<Requirement> queueServed;
    private Map<Integer, Integer> exactCount;

    private double time = 0d;
    private double generationTime = 0d;
    private double serviceTime = Double.MAX_VALUE;
    private double exitTime = Double.MAX_VALUE;
    private double expectation = 0d;
    private boolean sourcesDrained;

    public ServiceSystem(Type streamRule, Type serviceRule, int deviceCount, int queueLength, int sources) {

        streamRandomizer = new Randomizer(streamRule, true);
        serviceRandomizer = new Randomizer(serviceRule, false);
        queueWaiting = new ArrayDeque<>(queueLength);
        queueServed = new ArrayDeque<>();
        devices = new ArrayList<>(deviceCount);
        generators = new ArrayList<>(sources);
        exactCount = new HashMap<>();

        for (int i = 0; i < deviceCount; i++) {
            devices.add(new Device());
        }

        for (int i = 0; i < sources; i++) {
            generators.add(new Generator(Main.TEST_COUNT / sources));
        }
    }

    public void run() {
        while (!sourcesDrained) {
            if (time >= generationTime) {
                generationCycle(getActiveGenerator());
                serviceCycle();
            }

            if (time >= serviceTime) {
                serviceCycle();
            }
            if (time >= exitTime) {
                exitCycle();
            }
            time += getNextActionTime();
            checkSourcesDrained();
        }
        System.out.println("finished");
        calculate();
    }

    private double getNextActionTime() {
        List<Double> times = new ArrayList<>();
        times.add(generationTime);
        times.add(serviceTime);
        times.add(exitTime);
        calculateRequirementsInSystem();
        return Collections.min(times);
    }

    private Generator getActiveGenerator() {
        double minTime = Double.MAX_VALUE;
        Generator min = null;
        for (Generator generator : generators) {
            if (generator.getNextTime() < minTime) {
                minTime = generator.getNextTime();
                min = generator;
            }
        }
        generationTime = minTime;
        return min;
    }

    private void checkSourcesDrained() {
        int drained = 0;

        for (Generator generator : generators) {
            if (generator.getElements().isEmpty()) {
                drained++;
            }
        }
        if (drained == generators.size()) {
            sourcesDrained = true;
        }
    }

    private void generationCycle(Generator generator) {
        if (!generator.getElements().isEmpty()) {
            Requirement tmp = generator.getElements().poll();
            if (queueWaiting.size() < Main.QUEUE_LENGTH) {
                tmp.setArriveTime(time);
                queueWaiting.add(tmp);
            }
            generator.setNextTime(time += streamRandomizer.getTimestamp());
        }
    }

    private void serviceCycle() {
        if (queueWaiting.size() > 0) {
            for (Device device : devices) {
                if (!device.isBusy()) {
                    Requirement tmp = queueWaiting.poll();
                    tmp.setServiceTime(time);
                    exitTime = time + serviceRandomizer.getTimestamp();
                    device.setServing(tmp);
                }
            }
        } else serviceTime = Double.MAX_VALUE;
    }

    private void exitCycle() {
        for (Device device : devices) {
            Requirement tmp = device.finishServing();
            tmp.setExitTime(time);
            serviceTime = time;
            expectation += tmp.getExitTime() - tmp.getArriveTime();
            queueServed.add(tmp);
        }
    }

    private void calculateRequirementsInSystem() {
        int requirementsInSystem = 0;

        requirementsInSystem += queueWaiting.size();
        for (Device device : devices) {
            if (device.isBusy()) requirementsInSystem++;
        }

        if (exactCount.containsKey(requirementsInSystem)) {
            int times = exactCount.remove(requirementsInSystem);
            exactCount.put(requirementsInSystem, ++times);
        } else {
            exactCount.put(requirementsInSystem, 1);
        }
    }

    private void calculate() {
        int served = queueServed.size();
        double requirements = 0;

        expectation /= time;
        System.out.println(exactCount.toString());

        for (Map.Entry<Integer, Integer> entry : exactCount.entrySet()) {
            requirements += (double) entry.getKey() * entry.getValue() / served;
        }
        System.out.println("Served " + served);
        System.out.println("Average time in system: " + expectation);
        System.out.println("Average requirements in system: " + requirements);
    }
}
