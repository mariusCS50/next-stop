package com.example.nextstop.PublicTransportRoutes;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class RouteGeometry {
    @SerializedName("coordinates")
    public List<List<Double>> coordinates;
}
