package com.csx.stepdefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CsxDemoStepDefinitions {

  private static final Logger LOGGER = LoggerFactory.getLogger(CsxDemoStepDefinitions.class);
  private static final String APPLICATION_PROPERTIES_PATH = "/src/test/resources/application.properties";
  private static Properties properties = null;

  @io.cucumber.java.Before
  public void propertiesFileLoad() throws Throwable {
    properties = initialProperties();
  }


  @Given("Using api Get Shipment Type and compare with Baseline response")
  public void GetAPIShipmentType() throws Exception {

    String apiBaseUrl = properties.getProperty("apiBase.shipment.Url.QA");


    final Response actualResponse = initialGetAPI(apiBaseUrl, "/shipment-types");

    net.minidev.json.parser.JSONParser parser = new net.minidev.json.parser.JSONParser();

    final Path expectedResponsePath = Paths.get("src", "test", "resources", "json", "UsermanagementShipment-types_responseBaseline.json");
    final String expectedResponseStr = FileUtils.readFileToString(expectedResponsePath.toFile(), StandardCharsets.UTF_8);

    Object actualResponseJsonObj = parser.parse(actualResponse.getBody().asString());
    Object expectedResponseJsonObj = parser.parse(expectedResponseStr);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualResponseTree = mapper.readTree(actualResponseJsonObj.toString());
    JsonNode expectedResponseTree = mapper.readTree(expectedResponseJsonObj.toString());

    Assertions.assertTrue(actualResponseTree.equals(expectedResponseTree)); // true

  }

  @Then("Using api Get Shipment Type and compare with Baseline response using API URL {string}")
  public void GetAPIShipmentTypeUsingExampleParameter(String apiBaseUrl) throws Exception {

    final Response actualResponse = initialGetAPI(apiBaseUrl, "/shipment-types");

    net.minidev.json.parser.JSONParser parser = new net.minidev.json.parser.JSONParser();

    final Path expectedResponsePath = Paths.get("src", "test", "resources", "json", "UsermanagementShipment-types_responseBaseline.json");
    final String expectedResponseStr = FileUtils.readFileToString(expectedResponsePath.toFile(), StandardCharsets.UTF_8);

    Object actualResponseJsonObj = parser.parse(actualResponse.getBody().asString());
    Object expectedResponseJsonObj = parser.parse(expectedResponseStr);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualResponseTree = mapper.readTree(actualResponseJsonObj.toString());
    JsonNode expectedResponseTree = mapper.readTree(expectedResponseJsonObj.toString());

    Assertions.assertTrue(actualResponseTree.equals(expectedResponseTree)); // true

  }

  @And("insert Comment to User {string} setup testdata") // This sample API test is for Secure API,
                                                         // where we can use Token in header
  public void InsertedDataForUserComment(String userID) throws Exception {

    String oClientID = properties.getProperty("oClientID.QA");
    String oClientSecret = properties.getProperty("oClientSecret.QA");
    String oGrantType = properties.getProperty("oGrantType.QA");
    String oAccessTokenURL = properties.getProperty("oAccessTokenURL.QA");
    String oCallbackURL = properties.getProperty("oCallbackURL.QA");
    String oAuthURL = properties.getProperty("oAuthURL.QA");
    String oScope = properties.getProperty("oScope.QA");
    String oState = properties.getProperty("oState.QA");
    String oClientAuth = properties.getProperty("oClientAuthentication.QA");

    Response tokenResponse = RestAssured.given().formParam("client_id", oClientID).formParam("client_secret", oClientSecret)
        .formParam("callback_url", oCallbackURL).formParam("auth_url", oAuthURL).formParam("scope", oScope).formParam("state", oState)
        .formParam("client_authentication", oClientAuth).formParam("grant_type", oGrantType).post(oAccessTokenURL);
    System.out.print(tokenResponse.jsonPath().prettify());

    String token = tokenResponse.jsonPath().get("access_token");

    String apiBaseUrl = properties.getProperty("apiBase.Url.QA");

    RestAssured.baseURI = apiBaseUrl;

    RestAssured.useRelaxedHTTPSValidation(); // Cert validation
    final RequestSpecification request = RestAssured.given().auth().oauth2(token);
    request.header("Content-Type", "application/json");
    request.header("accept", "application/json");
    final Response actualResponse = request.get("/user-profile/" + userID + "/comments");

    if (actualResponse.statusCode() == 201) { // insert code is 201

      Assertions.assertTrue(true);
    } else {
      Assertions.assertTrue(false);
    }

  }



  private Properties initialProperties() {
    Properties properties = null;

    try {
      properties = new Properties();
      FileInputStream fis = null;

      String userDir = System.getProperty("user.dir");
      fis = new FileInputStream(userDir + APPLICATION_PROPERTIES_PATH);

      properties.load(fis);
      fis.close();


    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

  public static Response initialGetAPI(String apiBaseUrl, String ServicesName) {
    RestAssured.baseURI = apiBaseUrl;

    RestAssured.useRelaxedHTTPSValidation(); // Cert validation
    final RequestSpecification request = RestAssured.given();
    request.header("Content-Type", "application/json");
    request.header("accept", "application/json");

    final Response actualResponse = request.get(ServicesName);

    return actualResponse;
  }

}
