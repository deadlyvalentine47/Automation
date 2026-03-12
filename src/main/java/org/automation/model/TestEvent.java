package org.automation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestEvent {
  private EventType eventType;
  private Object payload;
  private Object response;
  private Integer responseCode;
  private String description;
  private Map<String, Object> additionalInfo;
  private String failedStep;

  private String clientName;
  private String flowType;


  private Map<String, Object> queryCriteria;
  private Map<String, Object> updateData;
  private Object insertDocPayload;

  private String operation;
  private String dbPrefix;
  private String collectionName;
}