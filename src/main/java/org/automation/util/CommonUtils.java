package org.automation.util;

import com.jayway.jsonpath.JsonPath;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.automation.model.CommonInfo;
import org.automation.model.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CommonUtils {
  @Autowired private JsonFileReader jsonFileReader;

  private static final String UUID_REGEX =
      "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
  private static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);

  public static void pause(final Integer timeoutInMillis) {
    try {
      Thread.sleep(timeoutInMillis);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T deserialize(final Response.Body body, final Class responseClass) {
    String bodyString = "";
    try {
      bodyString = IOUtils.toString(body.asInputStream());
    } catch (final Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
    final JsonFileReader jsonFileReader = new JsonFileReader();
    return jsonFileReader.read(bodyString, responseClass);
  }

  public static <T> T deserializeToList(final Response.Body body, final Class responseClass) {
    String bodyString = "";
    try {
      bodyString = IOUtils.toString(body.asInputStream());
    } catch (final Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
    final JsonFileReader jsonFileReader = new JsonFileReader();
    return jsonFileReader.readList(bodyString, responseClass);
  }

  public static String toJson(final Object body) {
    final JsonFileReader jsonFileReader = new JsonFileReader();
    return jsonFileReader.write(body);
  }

  public static String toJson(final Response.Body body) {
    String bodyString = "";
    try {
      bodyString = IOUtils.toString(body.asInputStream());
    } catch (final Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
    final JsonFileReader jsonFileReader = new JsonFileReader();
    return jsonFileReader.write(bodyString);
  }

  public static String convertToBase64(String filePath) {
    try {
      byte[] bytes = Files.readAllBytes(Paths.get(filePath));
      return Base64.getEncoder().encodeToString(bytes);
    } catch (IOException e) {
      log.error("Error while converting file to base64: " + filePath);
      throw new RuntimeException(e);
    }
  }

  public static boolean isStringUuid(String string) {
    return UUID_PATTERN.matcher(string).matches();
  }

  public static boolean isBase64Pdf(byte[] decodedByteArray) {
    if (decodedByteArray.length < 4) {
      return false; // PDF header (%PDF) is at least 4 bytes long
    }
    return decodedByteArray[0] == '%'
        && decodedByteArray[1] == 'P'
        && decodedByteArray[2] == 'D'
        && decodedByteArray[3] == 'F';
  }

  public String getOtpFromRecord(Response response) {
    final Object responseObject = CommonUtils.deserialize(response.body(), Object.class);
    log.info("SMS record response:: " + jsonFileReader.covertToJsonString(responseObject));
    List<Map<String, Object>> records = (List<Map<String, Object>>) responseObject;
    return ((Map<String, String>) records.get(0).get("body")).get("otp");
  }

  public static Object extractValueFromJsonPath(
          Map<String, Object> source,
          String jsonPath
  ) {
    try {
      return JsonPath.read(source, jsonPath);
    } catch (Exception e) {
      throw new IllegalArgumentException(
              "Failed to resolve JSONPath: " + jsonPath + " from source: " + source,
              e
      );
    }
  }

  public static Object resolveDynamicValue(
          Object value,
          CommonInfo commonInfo
  ) {
    if (value instanceof String str && str.startsWith("$")) {
      return extractValueFromJsonPath(
              commonInfo.getSteps(),
              str
      );
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  public static Object resolveDynamicObject(
          Object input,
          CommonInfo commonInfo
  ) {
    if (input instanceof Map<?, ?> map) {
      Map<String, Object> resolved = new HashMap<>();
      map.forEach((k, v) ->
              resolved.put(
                      String.valueOf(k),
                      resolveDynamicObject(v, commonInfo)
              )
      );
      return resolved;
    }

    if (input instanceof List<?> list) {
      return list.stream()
              .map(item -> resolveDynamicObject(item, commonInfo))
              .toList();
    }

    return resolveDynamicValue(input, commonInfo);
  }

  public static String resolveXCode(TestData testData, String xCode) {
    return testData.getExpectedResult().contains("401") ? "invalidToken" : xCode;
  }
}