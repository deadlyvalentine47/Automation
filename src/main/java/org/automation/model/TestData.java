package org.automation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestData {
  private String testCaseId;
  private String expectedResult;
  private String storyId;
  private String testDescription;
  private String testSpecification;
  private String steps;
  private String accessToken;
  private List<TestEvent> events;

  private CommonInfo commonInfo;
}