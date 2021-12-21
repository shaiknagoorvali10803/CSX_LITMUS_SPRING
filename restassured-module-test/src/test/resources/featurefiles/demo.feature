@demoFeature
Feature: CSX Demo
A demonstration using csx.com


  @GetShipmentTypesValidationUsingAPI @DataExist
  Scenario Outline: Get Shilpment type validate with baseline
  Given Using api Get Shipment Type and compare with Baseline response
  Then Using api Get Shipment Type and compare with Baseline response using API URL "<apiBaseUrl>"
  #And insert Comment to User "<UserID>" setup testdata
  
  Examples:
  |apiBaseUrl|UserID|
  |https://api-shipcsx-usermanagement-shipcsx-dev.go-dev.csx.com/sxrw-usermanagement/api/v1|Z9999|