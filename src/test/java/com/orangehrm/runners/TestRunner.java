package com.orangehrm.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.orangehrm.steps",
        plugin = {"pretty"}
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
