package com.orangehrm.steps;

import com.orangehrm.base.DriverFactory;

/**
 * Holds state shared across step-definition classes within a single scenario.
 *
 * <p>PicoContainer creates one instance of this per scenario and injects it
 * into the hooks and every step class, so they all share the same driver.
 * A fresh instance is created for each scenario, giving scenario isolation.
 */
public class ScenarioContext {

    private final DriverFactory driverFactory;

    public ScenarioContext() {
        this.driverFactory = new DriverFactory();
    }

    public DriverFactory getDriverFactory() {
        return driverFactory;
    }
}
