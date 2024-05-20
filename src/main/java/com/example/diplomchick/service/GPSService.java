package com.example.diplomchick.service;

import com.example.diplomchick.model.Coordinates;
import com.example.diplomchick.dto.GeoLocation;
import com.example.diplomchick.model.UserInfo;
import com.example.diplomchick.repository.CoordinatesRepository;
import com.example.diplomchick.repository.UserInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

@Service
public class GPSService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    CoordinatesRepository coordinatesRepository;

    public UserInfo loadLocation(String ip) {

        try {
            if (ip.equals("0:0:0:0:0:0:0:1")) {
                ip = loadIP();
            }
            URL url = new URL("http://ip-api.com/json/" + ip);// add here ip
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
            coordinatesRepository.save(coordinates);
            userInfo.setDate(LocalDate.now());
            userInfo.setCoordinates(coordinates);
            userInfo.setIp(geoLocation.getIp());
            userInfo.setCity(geoLocation.getCity());
            userInfo.setCountry(geoLocation.getCountry());
            userInfo.setOrganization(geoLocation.getOrganization());
            userInfo.setAsNumber(geoLocation.getAsNumber());
            userInfoRepository.save(userInfo);
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


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
