package com.hua.myElga.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class LoginInfoService {

    private final RestTemplate restTemplate = new RestTemplate();

    //// Take client IP from the request ////
    public String getClientIp(HttpServletRequest request) {

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    //// Take browser from the request ////
    public String getBrowser(String userAgent) {

        if (userAgent == null) {
            return "Άγνωστο";
        }

        if (userAgent.contains("Edg/")) {
            return "Microsoft Edge";
        }

        if (userAgent.contains("Chrome/")) {
            return "Google Chrome";
        }

        if (userAgent.contains("Firefox/")) {
            return "Mozilla Firefox";
        }

        if (userAgent.contains("Safari/")) {
            return "Safari";
        }

        return "Άγνωστο";
    }

    //// Take platform from the request ////
    public String getPlatform(String userAgent) {

        if (userAgent == null) {
            return "Άγνωστο";
        }

        if (userAgent.contains("Windows")) {
            return "Windows";
        }

        if (userAgent.contains("Android")) {
            return "Android";
        }

        if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        }

        if (userAgent.contains("Macintosh")) {
            return "macOS";
        }

        if (userAgent.contains("Linux")) {
            return "Linux";
        }

        return "Άγνωστο";
    }

    //// Take login date ////
    public String getLoginDate() {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Athens"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return now.format(formatter);
    }

    //// Generate location from client's IP ////
    public String getLocation(String ip) {

        if (ip == null || ip.isBlank() || ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return "Τοπικό δίκτυο";
        }

        try {
            String url = "https://ipapi.co/" + ip + "/json/";

            Map<?, ?> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                return "Άγνωστη";
            }

            Object city = response.get("city");
            Object region = response.get("region");
            Object country = response.get("country_name");

            StringBuilder location = new StringBuilder();

            if (city != null) {
                location.append(city);
            }

            if (region != null) {
                if (!location.isEmpty()) {
                    location.append(", ");
                }
                location.append(region);
            }

            if (country != null) {
                if (!location.isEmpty()) {
                    location.append(", ");
                }
                location.append(country);
            }

            return location.isEmpty() ? "Άγνωστη" : location.toString();

        } catch (Exception e) {
            return "Άγνωστη";
        }
    }
}
