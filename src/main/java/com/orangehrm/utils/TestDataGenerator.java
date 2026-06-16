package com.orangehrm.utils;

import com.github.javafaker.Faker;

/**
 * Generates test data for scenarios.
 *
 * <p>Holds a single {@link Faker} instance reused for the lifetime of this
 * generator. Instances are created per step class, which PicoContainer scopes
 * to a single scenario — so the Faker is reused within a scenario but never
 * shared across parallel threads.
 */
public class TestDataGenerator {

    private final Faker faker = new Faker();

    /**
     * Generates a unique, readable job title.
     *
     * <p>Combines a Faker job title with a six-digit random suffix. The prefix
     * keeps the value human-readable; the suffix makes collisions against the
     * shared, persistent demo data effectively impossible, so the same title is
     * not rejected as a duplicate on re-runs.
     *
     * @return a job title such as {@code "Marketing Coordinator 483920"}
     */
    public String uniqueJobTitle() {
        return faker.job().title() + " " + faker.number().digits(6);
    }
}