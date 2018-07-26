package com.semanientreprise.googlemapsproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDetails {
    String latitude,longitude;

    public MapDetails(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}