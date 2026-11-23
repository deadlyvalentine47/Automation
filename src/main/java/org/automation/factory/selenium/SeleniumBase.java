package org.automation.factory.selenium;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
public class SeleniumBase {

    @Value("${selenium.headless:true}")
    protected boolean isHeadless;

    @Value("${selenium.viewport.width:1920}")
    protected int viewportWidth;

    @Value("${selenium.viewport.height:1080}")
    protected int viewportHeight;

    protected WebDriver driver;

    @PostConstruct
    public void init() {
        if (driver == null) {
            // 1. Manages the binary (Try clearing cache if still failing)
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // 2. CRITICAL FIX for 403 Errors
            options.addArguments("--remote-allow-origins=*");

            // Viewport applies to both headless and headed runs
            options.addArguments("--window-size=" + viewportWidth + "," + viewportHeight);

            // 3. Keep your existing stealth/headless options
            if (isHeadless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", java.util.Collections.singletonList("enable-automation"));

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Chrome enforces a minimum window size; reapply so the requested size wins
            driver.manage().window().setSize(new Dimension(viewportWidth, viewportHeight));
        }
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}