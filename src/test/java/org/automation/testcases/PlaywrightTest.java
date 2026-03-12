package org.automation.testcases;

import org.automation.Application;
import org.automation.dataprovider.DataProvider;
import org.automation.factory.playwright.PlaywrightBase;
import org.automation.model.CommonInfo;
import org.automation.model.TestData;
import org.automation.testcasehelper.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.Test;

import java.lang.reflect.Method;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("${env}")
public class PlaywrightTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private PlaywrightBase playwrightBase;

  @AfterClass(alwaysRun = true)
  public void closeBrowser() {
    playwrightBase.tearDown();
  }

  @BeforeMethod(alwaysRun = true)
  public void dataSetup(final Method method, final Object[] obj) {
    final TestData testData = (TestData) obj[0];
    testData.setCommonInfo(new CommonInfo());
  }

  @Test(dataProvider = "testData", dataProviderClass = DataProvider.class)
  public void playwrightTest(final TestData testData) {
    TestHelper.eventsExecutor(testData);
  }
}