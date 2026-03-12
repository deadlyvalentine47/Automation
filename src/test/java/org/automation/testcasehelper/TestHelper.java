package org.automation.testcasehelper;

import org.automation.factory.Event;
import org.automation.factory.EventFactory;
import org.automation.model.TestData;

public class TestHelper {
  public static void eventsExecutor(TestData testData) {
    testData
        .getEvents()
        .forEach(
            testEvent -> {
              final Event event = EventFactory.getEvent(testEvent.getEventType());
              try {
                  event.executeEvent(testEvent, testData.getCommonInfo(), testData);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              try {
                  event.validateInfo(testEvent, testData.getCommonInfo());
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });
  }
}