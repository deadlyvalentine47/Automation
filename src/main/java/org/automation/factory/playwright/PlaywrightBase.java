package org.automation.factory.playwright;

import com.microsoft.playwright.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Data
@Component
public class PlaywrightBase {

    @Value("${playwright.headless:true}")
    protected boolean isHeadless;

    @Value("${playwright.viewport.width:1920}")
    protected int viewportWidth;

    @Value("${playwright.viewport.height:1080}")
    protected int viewportHeight;

    protected Playwright playwright;
    protected Browser browser;

    // Add this missing declaration
    protected BrowserContext context;

    protected Page page;

    /** Starts the browser on first use so API-only runs never launch one. */
    public synchronized Page getPage() {
        if (page == null) {
            init();
        }
        return page;
    }

    public void init() {
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(isHeadless)
            );

            // Now context will be resolved
            context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .setViewportSize(viewportWidth, viewportHeight)
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