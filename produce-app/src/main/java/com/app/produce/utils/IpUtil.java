package com.app.produce.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.app.produce.constant.Constants;

public class IpUtil {
    
    public static String getIpAddress(HttpServletRequest request) {

        String clientIp = (String)request.getAttribute("gwIp");
        String ip = request.getHeader("X-Forwarded-For");
        String gwip = request.getHeader("gwIp");

        if(!StringUtils.isEmpty(clientIp)){
            return clientIp;
        }

        if(!StringUtils.isEmpty(gwip)){
            return gwip;
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    public static String getIpPublicServiceAws(){

        URL url;
        try {
            url = new URL(Constants.GET_IP);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = br.readLine();
            System.out.println("Public IP Address: " + ip);
            
            br.close();
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

       
     
    }
    
}
