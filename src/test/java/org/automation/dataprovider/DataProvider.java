package org.automation.dataprovider;

import org.automation.model.TestData;
import org.automation.util.YamlFileReader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataProvider {
  private static final String TEST_CASES = "src/test/resources/testdata/";
  private YamlFileReader yamlFileReader;

  public DataProvider() {
    yamlFileReader = new YamlFileReader();
  }

  @org.testng.annotations.DataProvider(name = "testData")
  public Object[][] dataProvider(Method method) {
    Map<String, TestData> testDataMap = getTestDataMap(method.getName());

    String testCaseId = System.getProperty("testCaseId");
    if (testCaseId != null && !testCaseId.isEmpty()) {
      testDataMap =
              testDataMap.entrySet().stream()
                      .filter(
                              entry -> entry.getValue().getTestCaseId().equalsIgnoreCase(testCaseId))
                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Object[][] tests = new Object[testDataMap.size()][1];
    AtomicInteger i = new AtomicInteger();
    testDataMap
        .entrySet()
        .forEach(
            entry -> {
              int index = i.getAndIncrement();
              tests[index][0] = entry.getValue();
            });
    return tests;
  }

  public Map<String, TestData> getTestDataMap(final String methodName) {
    String testDataPath = TEST_CASES + methodName + ".yml";
    List<TestData> testCaseList =
        yamlFileReader.readList(new File(testDataPath), TestData.class);
    return testCaseList.stream()
        .collect(Collectors.toMap(TestData::getTestCaseId, p -> p));
  }
}