package org.automation.factory.selenium.impl;

import jakarta.annotation.PostConstruct;
import org.automation.factory.Event;
import org.automation.factory.EventFactory;
import org.automation.factory.selenium.SeleniumBase;
import org.automation.model.CommonInfo;
import org.automation.model.EventType;
import org.automation.model.TestData;
import org.automation.model.TestEvent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.time.Duration;
import java.util.Map;

@Component
public class SeleniumNavigateEvent implements Event {

    @Autowired private SeleniumBase seleniumBase;

    @PostConstruct
    public void register() {
        EventFactory.registerEvent(EventType.S_NAVIGATE, this);
    }

    @Override
    public void performDataSetup(TestEvent event, CommonInfo commonInfo) throws InterruptedException {

    }

    @Override
    public void executeEvent(TestEvent event, CommonInfo commonInfo, TestData testData) {
        Map<String, Object> info = event.getAdditionalInfo();
        String url = (String) info.get("url");
        String expectedTitle = (String) info.get("title");

        WebDriver driver = seleniumBase.getDriver();
        driver.get(url);

        // 1. Assertion for URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("swiggy.com"), "URL mismatch. Found: " + currentUrl);

        // 2. CRITICAL FIX: Explicitly wait for the title to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.titleIs(expectedTitle));
        } catch (Exception e) {
            // If it fails, we provide a clear error message with what was actually found
            Assert.assertEquals(driver.getTitle(), expectedTitle, "Title did not match after waiting 10s");
        }
    }
    @Override
    public void validateInfo(TestEvent event, CommonInfo commonInfo) throws InterruptedException {

    }

    @Override
    public void dataCleanUp(TestEvent event, CommonInfo commonInfo) {

    }
}