package org.automation.client;

import feign.Response;
import org.automation.config.FeignLogger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(
        name = "example-service",
        url = "${example.baseUrl}",
        configuration = {
                FeignLogger.class
        }
)
public interface Client {

        @GetMapping("/client/config")
        Response getClientConfig(
                @RequestHeader(name = "x-code") String xCode,
                @RequestParam String clientName,
                @RequestParam String flowType);

        @PostMapping("/file/process")
        Response processFile(
                @RequestHeader(name = "x-code") String xCode,
                @RequestBody Map<String, Object> fileProcessRequest
        );

        @PostMapping("client/config")
        Response createClientConfig(
                @RequestHeader(name = "x-code") String xCode,
                @RequestBody Map<String, Object> createClientConfigRequest
        );

        @PutMapping("client/config")
        Response updateClientConfig(
                @RequestHeader(name = "x-code") String xCode,
                @RequestBody Map<String, Object> updateClientConfigRequest
        );
}