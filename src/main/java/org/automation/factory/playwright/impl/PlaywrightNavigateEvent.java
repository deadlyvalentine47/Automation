package org.automation.factory.playwright.impl;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import jakarta.annotation.PostConstruct;
import org.automation.factory.Event;
import org.automation.factory.EventFactory;
import org.automation.factory.playwright.PlaywrightBase;
import org.automation.model.CommonInfo;
import org.automation.model.EventType;
import org.automation.model.TestData;
import org.automation.model.TestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Component
public class PlaywrightNavigateEvent implements Event {

    @Autowired private PlaywrightBase playwrightBase;

    @PostConstruct
    public void register() {
        EventFactory.registerEvent(EventType.P_NAVIGATE, this);
    }

    @Override
    public void performDataSetup(TestEvent event, CommonInfo commonInfo) throws InterruptedException {

    }

    @Override
    public void executeEvent(TestEvent event, CommonInfo commonInfo, TestData testData) {
        Map<String, Object> info = event.getAdditionalInfo();
        String url = (String) info.get("url");
        String expectedTitle = (String) info.get("title");

        Page page = playwrightBase.getPage();
        page.navigate(url);

        // Playwright specific assertions
        assertThat(page).hasURL(url);
        assertThat(page).hasTitle(expectedTitle);

    }

    @Override
    public void validateInfo(TestEvent event, CommonInfo commonInfo) {}

    @Override
    public void dataCleanUp(TestEvent event, CommonInfo commonInfo) {

    }
}