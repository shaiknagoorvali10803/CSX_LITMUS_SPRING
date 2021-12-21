package com.csx.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, monochrome = true,
    glue = {"com.csx.stepdefinitions"}, features = {"classpath:featurefiles"})
public class CucumberTest {
}
