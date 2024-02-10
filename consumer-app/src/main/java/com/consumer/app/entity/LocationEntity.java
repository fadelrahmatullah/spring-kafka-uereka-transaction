package com.consumer.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_location", schema = "tp_tracking")
public class LocationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "region")
    private String region;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "latitude")
    private String latitude;
    
    @Column(name = "longitude")
    private String longitude;
    
    @Column(name = "task_id")
    private Long taskId;
    
    @Column(name = "process_id")
    private String processId;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "address")
    private String address;

}