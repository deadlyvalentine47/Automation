package org.automation.factory.playwright;

import com.microsoft.playwright.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class PlaywrightBase {

    @Value("${playwright.headless:true}")
    protected boolean isHeadless;

    protected Playwright playwright;
    protected Browser browser;

    // Add this missing declaration
    protected BrowserContext context;

    protected Page page;

    @PostConstruct
    public void init() {
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(isHeadless)
            );

            // Now context will be resolved
            context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
//                    .setViewportSize(1920, 1080)
            );

            page = context.newPage();

            // Helpful script to bypass basic automation detection
            page.addInitScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        }
    }

    public void tearDown() {
        if (context != null) context.close(); // Close context too
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}