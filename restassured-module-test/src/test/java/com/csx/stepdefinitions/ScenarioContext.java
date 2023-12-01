package com.csx.stepdefinitions;

import com.csx.springConfig.annotation.GlueScopeBean;
import io.cucumber.java.Scenario;

@GlueScopeBean
public class ScenarioContext {

	protected Scenario scenario;

	public Scenario getScenario() {
		return scenario;
	}
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

}
