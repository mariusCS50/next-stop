package com.example.nextstop.StationModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Location {
    @SerializedName("id")
    public String id;

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("routes")
    public ArrayList<Integer> routes;
}
