package com.csx.page.objects;


import com.csx.springConfig.annotation.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Page
public class VisaPageObjects {
    private static final Logger logger = LoggerFactory.getLogger(VisaPageObjects.class);

    @FindBy(id ="first_4")
    public WebElement firstName;

    @FindBy(id ="last_4")
    public WebElement lastName;

    @FindBy(id = "input_46")
    public WebElement fromCountry;

    @FindBy(id = "input_47")
    public WebElement toCountry;

    @FindBy(id = "input_24_month")
    public WebElement month;

    @FindBy(id = "input_24_day")
    public WebElement day;

    @FindBy(id = "input_24_year")
    public WebElement year;

    @FindBy(id = "input_6")
    public WebElement email;

    @FindBy(id = "input_27_phone")
    public WebElement phone;

    @FindBy(id = "input_45")
    public WebElement comments;

    @FindBy(id = "submitBtn")
    public WebElement submit;

    @FindBy(id = "requestnumber")
    public WebElement requestNumber;


}
