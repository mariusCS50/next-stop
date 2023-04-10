package com.example.nextstop.PublicTransportRoutes;

import com.example.nextstop.StationModels.Geometry;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Route {
    @SerializedName("geometry")
    public RouteGeometry geometry;
}