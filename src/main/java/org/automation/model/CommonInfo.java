package org.automation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.automation.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class CommonInfo {
  private String otp;
  private String authId;
  private String accessToken;
  private String refreshToken;

  private Map<String, Object> dbRecord;

  /**
   * Stores step-wise request/response data
   * Structure:
   * steps -> stepName -> { request, response }
   */
  private Map<String, Object> steps = new HashMap<>();


  public void addStepResponse(String stepName, Object response) {
    steps.put(stepName, Map.of("response", response));
  }

  public Object getStepResponse(String stepName) {
    Object step = steps.get(stepName);
    if (step instanceof Map<?, ?> map) {
      return map.get("response");
    }
    return null;
  }

  public static Object resolveFromSteps(CommonInfo commonInfo, String jsonPath) {
    if (commonInfo.getSteps() == null) {
      throw new IllegalStateException("No steps data available");
    }

    log.info("Resolving value from steps using path: " + jsonPath);

    return CommonUtils.extractValueFromJsonPath(
            commonInfo.getSteps(),
            jsonPath
    );
  }
}