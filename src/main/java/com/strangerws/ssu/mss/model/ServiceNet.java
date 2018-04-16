package com.strangerws.ssu.mss.model;

import com.strangerws.ssu.mss.Main;
import com.strangerws.ssu.mss.model.element.Generator;
import com.strangerws.ssu.mss.util.Type;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServiceNet {
    private final double probability = 0.35;
    private ServiceSystem[] systems;
    private Generator generator;
    private Map<Integer, Integer> exactCount;
    private final Random random = new Random();

    public ServiceNet() {
        exactCount = new HashMap<>();
        generator = new Generator(Main.TEST_COUNT);
        systems = new ServiceSystem[2];
        systems[0] = new ServiceSystem(Type.EXPONENT, Type.EXPONENT, 2, 0, generator);
        systems[1] = new ServiceSystem(Type.EXPONENT, Type.EXPONENT, 2, 0, generator);
    }

    public void run() {
        while (!generator.getElements().isEmpty()) {
            if (random.nextDouble() > probability) systems[0].run();
            else systems[1].run();
            calculateRequirementsInEachSystem();
        }

        calculate();
    }

    private void calculateRequirementsInEachSystem() {
        int key = 0;
        int value = 0;
        for (ServiceSystem system : systems) {
            Pair<Integer, Integer> tmp = system.requirementsNow;
            if (tmp != null) {
                key += tmp.getKey();
                value += tmp.getValue();
            }
        }

        exactCount.put(key, value);
    }


    private void calculate() {
        int served = systems[0].queueServed.size() + systems[1].queueServed.size();
        double requirements = 0;
        double expectation = systems[0].expectation + systems[1].expectation;
        double time = Math.max(systems[0].time, systems[1].time);

        expectation /= time;

        for (Map.Entry<Integer, Integer> entry : exactCount.entrySet()) {
            requirements += (double) entry.getKey() * entry.getValue() / served;
        }
        System.out.println("Served " + served);
        System.out.println("Average time in system: " + expectation);
        System.out.println("Average requirements in system: " + requirements);
        System.out.println("Decline probability: " + (double) served / Main.TEST_COUNT);
    }
}
