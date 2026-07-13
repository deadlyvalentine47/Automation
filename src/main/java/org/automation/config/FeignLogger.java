package org.automation.config;

import feign.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FeignLogger implements RequestInterceptor, ResponseInterceptor {

  private static final String REQUEST_BODY_START_PREFIX = "Request body::: ";
  private static final String NEW_LINE_CONSTANT = "\n";

  @Override
  public void apply(final RequestTemplate requestTemplate) {
    final byte[] bodyData = requestTemplate.body();
    final StringBuilder sb = new StringBuilder();
    sb.append(NEW_LINE_CONSTANT + "Request URL::: " + requestTemplate.url() + NEW_LINE_CONSTANT);
    sb.append("Request Headers::: " + requestTemplate.headers() + NEW_LINE_CONSTANT);
    if (bodyData != null && bodyData.length > 0) {
      // Convert the byte array to a String
      final String requestBody = Util.decodeOrDefault(bodyData, Util.UTF_8, null);
      sb.append(REQUEST_BODY_START_PREFIX + requestBody + NEW_LINE_CONSTANT);
    }
    log.info("Request Information::: " + sb);
  }

  @Override
  public Object aroundDecode(final InvocationContext invocationContext) throws IOException {
    try {
      final byte[] bodyData = invocationContext.response().body().asInputStream().readAllBytes();
      final StringBuilder sb = new StringBuilder();
      if (bodyData != null && bodyData.length > 0) {
        // Convert the byte array to a String
        final String responseBody = Util.decodeOrDefault(bodyData, Util.UTF_8, null);
        sb.append(NEW_LINE_CONSTANT + REQUEST_BODY_START_PREFIX + responseBody + NEW_LINE_CONSTANT);
      }
      log.info("Received Response Payload::: " + sb);
    } catch (final Exception e) {
      System.out.println("Failed to parse json response.");
    }
    return null;
  }
}