package org.automation.testcases;

import org.automation.Application;
import org.automation.dataprovider.DataProvider;
import org.automation.model.CommonInfo;
import org.automation.model.TestData;
import org.automation.testcasehelper.TestHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("${env}")
public class ApiTest extends AbstractTestNGSpringContextTests {

  @BeforeMethod(alwaysRun = true)
  public void dataSetup(final Method method, final Object[] obj) {
    final TestData testData = (TestData) obj[0];
    testData.setCommonInfo(new CommonInfo());
  }

  @Test(dataProvider = "testData", dataProviderClass = DataProvider.class)
  public void apiTest(final TestData testData) {
    TestHelper.eventsExecutor(testData);
  }
}
