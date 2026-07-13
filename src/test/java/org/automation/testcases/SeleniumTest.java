package org.automation.testcases;

import org.automation.Application;
import org.automation.dataprovider.DataProvider;
import org.automation.factory.selenium.SeleniumBase;
import org.automation.model.TestData;
import org.automation.testcasehelper.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("${env}")
public class SeleniumTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private SeleniumBase seleniumBase; // Injects driver and triggers @BeforeMethod in base

  @AfterClass(alwaysRun = true)
  public void closeBrowser() {
    seleniumBase.tearDown();
  }

  @Test(dataProvider = "testData", dataProviderClass = DataProvider.class)
  public void seleniumTest(final TestData testData) {
    TestHelper.eventsExecutor(testData);
  }
}