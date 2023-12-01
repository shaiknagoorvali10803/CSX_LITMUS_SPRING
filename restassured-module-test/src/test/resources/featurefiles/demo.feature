@demoFeature
Feature: CSX Demo-Signal Insepction Test Hierarchy

  @DataExist
  Scenario Outline: Validate Hierarchy for all Roles
     Given Validate Director in Signal Insepction Test Hierarchy with the proxy "<ProxyUser>"
    Examples:
      |ProxyUser|
      |D3289    |
      |S4266    |
      |D3304    |
