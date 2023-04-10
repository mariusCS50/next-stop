package com.example.nextstop.PublicTransportRoutes;

import com.example.nextstop.StationModels.Location;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteItems {
    @SerializedName("features")
    public List<Route> route;
}