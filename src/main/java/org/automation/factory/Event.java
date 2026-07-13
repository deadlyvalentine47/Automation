package org.automation.factory;

import org.automation.model.CommonInfo;
import org.automation.model.TestData;
import org.automation.model.TestEvent;

public interface Event {
  void performDataSetup(TestEvent event, CommonInfo commonInfo) throws InterruptedException;

  void executeEvent(
      final TestEvent event, final CommonInfo commonInfo, TestData otsServiceTestData)
      throws InterruptedException;

  void validateInfo(final TestEvent event, final CommonInfo commonInfo) throws InterruptedException;

  void dataCleanUp(final TestEvent event, final CommonInfo commonInfo);
}