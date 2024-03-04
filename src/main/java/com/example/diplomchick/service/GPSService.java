package com.example.diplomchick.service;

import com.example.diplomchick.dto.Coordinates;
import com.example.diplomchick.dto.GeoLocation;
import com.example.diplomchick.dto.UserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GPSService {

    public UserInfo loadLocation() {

        try {

            URL url = new URL("http://ip-api.com/json/" + loadIP());// add here ip
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
            UserInfo userInfo = new UserInfo();
            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(geoLocation.getLatitude());
            coordinates.setLongitude(geoLocation.getLongitude());
            userInfo.setCoordinates(coordinates);
            userInfo.setIp(geoLocation.getIp());


            userInfo.setCity(geoLocation.getCity());
            userInfo.setCountry(geoLocation.getCountry());
            userInfo.setOrganization(geoLocation.getOrganization());
            userInfo.setAs(geoLocation.getAs());
            return userInfo;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //how can I load ip here??
    public String loadIP() throws IOException {

        URL url = new URL("https://api64.ipify.org?format=json");
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
        JsonNode jsonNode = objectMapper.readTree(String.valueOf(response));

        return jsonNode.get("ip").asText();

    }
}
