package com.csx.test.util;


import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.csx.springConfig.annotation.Page;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Page
public class ReportLogUtils {

    public static Logger logger = LoggerFactory.getLogger(ReportLogUtils.class);

    public void addTextLog(Scenario scenario, String text){
        scenario.log(text);
    }
    public void addListLog( Scenario scenario,String text,List<String> values){
        scenario.log(text +"==>"+values);
    }
    public void addLog(String text){
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, text);
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createCodeBlock(text));
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createLabel(text,ExtentColor.BROWN));
    }
    public void addLog(String text,Object object){
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, text);
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createOrderedList(object));
    }

    public void addJsonLog(String text,Object object){
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, text);
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createJsonCodeBlock(object));
    }


}
