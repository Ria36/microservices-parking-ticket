package org.ria.ticketservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ParkingLotClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String parkingLotBaseUrl;

    public ParkingLotClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        // ✅ Check ENV var, else fallback
        String envUrl = System.getenv("PARKING_LOT_SERVICE_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            this.parkingLotBaseUrl = envUrl;
        } else {
            // ✅ Default for Docker Compose (service name inside network)
            this.parkingLotBaseUrl = "http://parking-service:8080/spots";
        }

        System.out.println("✅ ParkingLotClient using URL: " + this.parkingLotBaseUrl);
    }

    public String getFirstAvailableSpot() {
        String url = parkingLotBaseUrl + "/available";
        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.isEmpty()) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(response);
            if (root.isArray() && !root.isEmpty()) {
                return root.get(0).get("id").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean occupySpot(String spotId) {
        String url = parkingLotBaseUrl + "/occupy/" + spotId;
        String response = restTemplate.postForObject(url, null, String.class);
        return response != null && response.contains("occupied");
    }

    public boolean freeSpot(String spotId) {
        String url = parkingLotBaseUrl + "/free/" + spotId;
        String response = restTemplate.postForObject(url, null, String.class);
        return response != null && response.contains("freed");
    }
}
