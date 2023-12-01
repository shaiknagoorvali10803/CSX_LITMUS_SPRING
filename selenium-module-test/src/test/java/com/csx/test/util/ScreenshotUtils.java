package com.csx.test.util;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.csx.springConfig.annotation.Page;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Page
public class ScreenshotUtils {
    public static Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    @Autowired
    WebDriver driver;

    @Value("${screenshot.path}")
    private Path path;

    public void insertScreenshot1(Scenario scenario, String screenshotTitle){
        scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "screenShot");
     }
    public void insertScreenshot(String screenshotTitle){
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, MarkupHelper.createLabel(screenshotTitle, ExtentColor.GREEN), MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }
    public void addLog(String text){
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, text);
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createCodeBlock(text));
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createLabel(text,ExtentColor.BROWN));
    }
    public void addLog(Object object){
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS,MarkupHelper.createOrderedList(object));
    }

    public void addJsonLog(Object object){
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO,MarkupHelper.createJsonCodeBlock(object));
    }

    public String takeScreenShot() throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileCopyUtils.copy(sourceFile, this.path.resolve(dateName + ".png").toFile());
        return sourceFile.toString();
    }

    public String browser_TakeScreenShot() {
        String destination = null;
        String imgPath = null;
        String dateName = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        byte[] imag=((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        ByteArrayInputStream bais = new ByteArrayInputStream(imag);
        BufferedImage image = null;
        try {
            image = ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgPath = "\\Screenshots\\" + "screenShot" + dateName + ".png";
        destination = System.getProperty("user.dir") + imgPath;
        File finalDestination = new File(destination);
        finalDestination.getParentFile().mkdir();
        try {
            ImageIO.write(image, "png", finalDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imgPath;
    }

    public String getScreenshotBase64(){
        String screenshot=((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        return screenshot;
    }
}
