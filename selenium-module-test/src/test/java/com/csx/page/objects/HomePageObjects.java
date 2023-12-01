package com.csx.page.objects;


import com.csx.springConfig.annotation.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Page
public class HomePageObjects {

    @FindBy(name = "q")
    public WebElement searchBox;
    @FindBy(name = "btnK")
    public List<WebElement> searchBtns;

}
