@Edge
Feature: CSX Demo
A demonstration using csx.com

@DataExist
Scenario: I should be able to see the copyright information on csx.com on any browser type
  Given I am on a mobile browser
  When I navigate to csx.com
  Then I should see the copyright information
