package com.example.diplomchick.service;

import com.example.diplomchick.dto.GeoLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GPSService {

    public String loadLocation() {

        try {
            // IP-адрес, для которого вы хотите получить геолокацию
            URL url = new URL("http://ip-api.com/json/"); // Используем IP Geolocation API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper objectMapper = new ObjectMapper();
            GeoLocation geoLocation = objectMapper.readValue(response.toString(), GeoLocation.class);

            return "Country: " + geoLocation.getCountry() + "\n" + "City: " + geoLocation.getCity();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No info";
    }
}
