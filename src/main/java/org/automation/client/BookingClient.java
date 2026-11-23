package org.automation.client;

import feign.Response;
import org.automation.config.FeignLogger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
        name = "restful-booker",
        url = "${api.baseUrl}",
        configuration = {
                FeignLogger.class
        }
)
public interface BookingClient {

        @GetMapping("/ping")
        Response ping();

        @PostMapping("/auth")
        Response authenticate(@RequestBody Map<String, Object> request);

        @GetMapping("/booking")
        Response listBookings(
                @RequestParam(value = "firstname", required = false) String firstname,
                @RequestParam(value = "lastname", required = false) String lastname);

        @GetMapping("/booking/{id}")
        Response getBooking(@PathVariable("id") Object id);

        @PostMapping("/booking")
        Response createBooking(@RequestBody Map<String, Object> request);

        @PutMapping("/booking/{id}")
        Response updateBooking(
                @PathVariable("id") Object id,
                @RequestHeader("Cookie") String cookie,
                @RequestBody Map<String, Object> request);

        @DeleteMapping("/booking/{id}")
        Response deleteBooking(@PathVariable("id") Object id, @RequestHeader("Cookie") String cookie);
}
