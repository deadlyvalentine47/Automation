package org.automation.factory.api.impl;

import feign.Response;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.automation.client.BookingClient;
import org.automation.factory.Event;
import org.automation.factory.EventFactory;
import org.automation.model.ApiValidation;
import org.automation.model.CommonInfo;
import org.automation.model.EventType;
import org.automation.model.TestData;
import org.automation.model.TestEvent;
import org.automation.util.CommonUtils;
import org.automation.util.JsonFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ApiRequestEvent implements Event {

  private static final String LAST_RESPONSE = "__lastApiResponse";

  private static final String PRESENT = "#present";
  private static final String ABSENT = "#absent";

  @Autowired private BookingClient bookingClient;

  @PostConstruct
  public void register() {
    EventFactory.registerEvent(EventType.API_REQUEST, this);
  }

  @Override
  public void performDataSetup(TestEvent event, CommonInfo commonInfo) {}

  @Override
  public void executeEvent(TestEvent event, CommonInfo commonInfo, TestData testData) {
    final Map<String, Object> info = resolveInfo(event, commonInfo);
    final Map<String, Object> payload =
        asMap(CommonUtils.resolveDynamicObject(event.getPayload(), commonInfo));

    final Response response =
        switch (event.getOperation().toUpperCase()) {
          case "PING" -> bookingClient.ping();
          case "AUTH" -> bookingClient.authenticate(payload);
          case "LIST" ->
              bookingClient.listBookings(
                  asString(info.get("firstname")), asString(info.get("lastname")));
          case "GET" -> bookingClient.getBooking(info.get("id"));
          case "CREATE" -> bookingClient.createBooking(payload);
          case "UPDATE" ->
              bookingClient.updateBooking(
                  info.get("id"), asCookie(info.get("cookie")), payload);
          case "DELETE" -> bookingClient.deleteBooking(info.get("id"), asCookie(info.get("cookie")));
          default ->
              throw new IllegalArgumentException("Unsupported operation: " + event.getOperation());
        };

    final Map<String, Object> result = new LinkedHashMap<>();
    result.put("status", response.status());
    result.put("response", parse(response));

    log.info("{} {} -> {}", event.getOperation(), event.getDescription(), response.status());

    commonInfo.getSteps().put(LAST_RESPONSE, result);
    if (event.getSaveAs() != null && !event.getSaveAs().isBlank()) {
      commonInfo.getSteps().put(event.getSaveAs(), result);
    }
  }

  @Override
  public void validateInfo(TestEvent event, CommonInfo commonInfo) {
    final Map<String, Object> result = asMap(commonInfo.getSteps().get(LAST_RESPONSE));
    Assert.assertNotNull(result, "No response recorded for: " + event.getDescription());

    if (event.getResponseCode() != null) {
      Assert.assertEquals(
          result.get("status"),
          event.getResponseCode(),
          "Unexpected status for: " + event.getDescription());
    }

    final List<ApiValidation> validations = event.getValidations();
    if (validations == null) {
      return;
    }
    for (final ApiValidation validation : validations) {
      assertValidation(result.get("response"), validation, event.getDescription());
    }
  }

  private void assertValidation(
      final Object response, final ApiValidation validation, final String description) {
    Object actual;
    try {
      actual = JsonPath.read(response, validation.getPath());
    } catch (final Exception e) {
      actual = null;
    }

    final Object expected = validation.getExpected();
    final String message =
        "Validation failed for " + validation.getPath() + " in: " + description;

    if (PRESENT.equalsIgnoreCase(asString(expected))) {
      Assert.assertNotNull(actual, message);
      return;
    }
    if (ABSENT.equalsIgnoreCase(asString(expected))) {
      Assert.assertNull(actual, message);
      return;
    }
    Assert.assertEquals(actual, expected, message);
  }

  @Override
  public void dataCleanUp(TestEvent event, CommonInfo commonInfo) {}

  private Map<String, Object> resolveInfo(TestEvent event, CommonInfo commonInfo) {
    return asMap(CommonUtils.resolveDynamicObject(event.getAdditionalInfo(), commonInfo));
  }

  private Object parse(Response response) {
    try {
      final String body = IOUtils.toString(response.body().asInputStream());
      return body.isBlank() ? null : new JsonFileReader().read(body, Object.class);
    } catch (final Exception e) {
      throw new IllegalStateException("Unable to read response body", e);
    }
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> asMap(Object value) {
    return value instanceof Map<?, ?> ? (Map<String, Object>) value : Map.of();
  }

  private static String asString(Object value) {
    return value == null ? null : String.valueOf(value);
  }

  private static String asCookie(Object token) {
    final String value = asString(token);
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.startsWith("token=") ? value : "token=" + value;
  }
}
