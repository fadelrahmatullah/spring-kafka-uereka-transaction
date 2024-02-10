package com.app.produce.service.impl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.produce.constant.Constants;
import com.app.produce.service.LocationService;
import com.core.app.vo.LocationVo;

import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Slf4j
@Service
public class LocationServiceImpl implements LocationService{

    @Override
    public LocationVo getLocation(String ipAddress) {

        if (StringUtils.isBlank(ipAddress)) {
            return null;
        }
        log.info("IP request param:{}", ipAddress);
        String responseData = null;
        JSONObject respon = null;
        try {
            
            String encodedIpAddress = URLEncoder.encode(ipAddress, StandardCharsets.UTF_8.toString());
            String apiUrl = Constants.IP_LOC + encodedIpAddress;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Enable automatic redirection
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            InputStream inputStream = connection.getInputStream();
            responseData = readFromStream(inputStream);
            
            respon = JSON.parseObject(responseData);
            LocationVo result = new LocationVo();

            if (respon == null) {
                return null;
            }

            if (StringUtils.isBlank(respon.getString("status")) 
                || !respon.getString("status").equals("success")) {
                return null;
            }

            result.setCity(respon.getString("city"));
            result.setCountry(respon.getString("country"));
            result.setLatitude(respon.getFloat("lat").toString());
            result.setLongitude(respon.getString("lon").toString());
            result.setRegion(respon.getString("region"));
            result.setAddress(respon.getString("as"));
            result.setTimezone(respon.getString("timezone"));
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    private String readFromStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
    
}
