package com.csx.stepdefinitions;

import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.test.util.ReportLogUtils;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

@CucumberContextConfiguration
@SpringBootTest
public class CsxDemoStepDefinitions {
  private static final Logger LOGGER = LoggerFactory.getLogger(CsxDemoStepDefinitions.class);
  @LazyAutowired
  ReportLogUtils reportLogUtils;

  @Value("${auth_url}")
  String auth_url;

  @Value("${hierarchy_api_endpoint}")
  String hierarchy_api_endpoint;

  private static Properties properties = null;

  Scenario scenario;
  @Autowired
  ScenarioContext scenarioContext;

  @Before
  public void settingScenario(Scenario scenario) {
    this.scenario=scenario;
    scenarioContext.setScenario(scenario);
  }

  @Then("Validate Director in Signal Insepction Test Hierarchy with the proxy {string}")
  public void validateDirectorinfo(String ProxyUser) throws IOException, InterruptedException, JSONException {

    // Generate OAuth Token
    RestAssured.baseURI = auth_url;
    Response response2 = RestAssured.given().auth().preemptive().basic("wInHKyZUjw2HdLAFvZMUOiHVKzkafQTP", "8AffmsFDLxA3NkRY")
            .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").formParam("scope", "openid").when().post();
    reportLogUtils.addLog("OAuth Response: " + response2.asString());
    System.out.println("\nOAuth Response: " + response2.asString());
    ResponseBody body2 = response2.getBody();
    System.out.println("\nOAuth Response Body is: " + body2.asString());
    reportLogUtils.addLog("OAuth Response Body is: " + body2.asString());

    JSONObject jsonObject = new JSONObject(response2.getBody().asString());
    String accessToken = jsonObject.get("access_token").toString();
    reportLogUtils.addTextLog(scenario,"Access Token is : " + accessToken);
    String jsonBody = new String(Files.readAllBytes(Paths.get(".\\src\\test\\resources\\JSON\\DirectorHierarchyAPIPyload.json")));

    RestAssured.baseURI = hierarchy_api_endpoint;

    RequestSpecification httpRequest = RestAssured.given();
    httpRequest.header("Authorization", "Bearer " + accessToken);
    httpRequest.header("Content-Type", "application/json");
    httpRequest.header("accept", "application/json");
    httpRequest.header("proxiedRacf", ProxyUser);
    httpRequest.body(jsonBody);

    Response response = httpRequest.post();
    // Retrieve the body of the Response
    ResponseBody body = response.getBody();
    // Getting response as a string
    byte[] responseAsStringByte = body.asByteArray();
    // Creating a target file
    Files.write(Paths.get(".\\src\\\\test\\resources\\JSON\\DirectorHierarchyAPIResponse.json"), responseAsStringByte);
    //("Response Body is: " + body.asString());
    Thread.sleep(10000);
    JsonPath jsonResponseEvaluator = response.jsonPath();
    List<String> directorname = jsonResponseEvaluator.get("userHierarchyList.name");
    reportLogUtils.addListLog(scenario,"directornames" ,directorname);
  }
}
