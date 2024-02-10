package com.core.app.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationVo {
    
    private String city;
    private String region;
    private String country;
    private String latitude;
    private String longitude;
    private String address;
    private String timezone;
}
