package com.csx.runner;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("featurefiles")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.csx.stepdefinitions")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:featurefiles")
@ConfigurationParameter(key= FILTER_TAGS_PROPERTY_NAME,value = "")
@ConfigurationParameter(key= EXECUTION_DRY_RUN_PROPERTY_NAME,value = "false")
@ConfigurationParameter(key= PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME,value="true")
@ConfigurationParameter(key= EXECUTION_MODE_FEATURE_PROPERTY_NAME,value="concurrent")  //concurrent(Method level) ,same_thread (Class Level)
@ConfigurationParameter(key= PARALLEL_CONFIG_STRATEGY_PROPERTY_NAME,value="dynamic")     // dynamic (Junit-Cucumber Take Control) ,fixed-We can take Control over threads
@ConfigurationParameter(key= PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME,value="2")  // no.of threads to be created for a feature
@ConfigurationParameter(key= PARALLEL_CONFIG_FIXED_MAX_POOL_SIZE_PROPERTY_NAME,value="5")  // thread pool size or No of threads

public class CucumberTest {
}

