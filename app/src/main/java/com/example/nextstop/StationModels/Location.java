package com.example.nextstop.StationModels;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("id")
    public String id;

    @SerializedName("geometry")
    public Geometry geometry;
}
