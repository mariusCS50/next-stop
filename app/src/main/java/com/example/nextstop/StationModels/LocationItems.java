package com.example.nextstop.StationModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationItems {
    @SerializedName("features")
    public List<Location> locations;
}
