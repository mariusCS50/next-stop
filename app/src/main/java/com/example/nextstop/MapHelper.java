package com.example.nextstop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nextstop.PublicTransportRoutes.Route;
import com.example.nextstop.PublicTransportRoutes.RouteGeometry;
import com.example.nextstop.PublicTransportRoutes.RouteItems;
import com.example.nextstop.StationModels.Location;
import com.example.nextstop.StationModels.LocationItems;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import org.osmdroid.api.IMapController;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MapHelper {
    private final Context context;
    private final MapView map;
    private IMapController mapController;
    private ImageButton myLocationButton;
    private LinearLayout bottomSheetLayout;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private RoadManager roadManager;
    private LocationItems locationItems;
    final boolean[] areMarkersVisible = {false, false, false, false, false, false, false};

    public MapHelper(Context context, MapView map) {
        this.context = context;
        this.map = map;
    }

    protected String readJson(String fileName) {
        try (InputStream inputStream = context.getAssets().open(fileName)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    protected void addStations() {
        String locationsJson = readJson("stations_locations.json");
        locationItems = new Gson().fromJson(locationsJson, LocationItems.class);

        for (Location location : locationItems.locations) {
            Drawable originalMarkerDrawable = context.getResources().getDrawable(R.drawable.bus_stop);
            createMarker(location, originalMarkerDrawable);
        }
    }

    protected void createMarker(Location location, Drawable iconDrawable){
        Marker marker = new Marker(map);
        marker.setTitle(location.id);

        marker.setIcon(iconDrawable);

        marker.setPosition(new GeoPoint(location.geometry.coordinates.get(1), location.geometry.coordinates.get(0)));
        map.getOverlays().add(marker);

        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                map.getController().animateTo(marker.getPosition());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                resetButtonStates();
                TextView textView = (TextView) bottomSheetLayout.findViewById(R.id.stationName);
                String stationTitle = marker.getTitle();

                textView.setText(stationTitle);

                for (int i = 1; i <= 6; i++) {
                    int buttonId = context.getResources().getIdentifier("ruta" + i, "id", context.getPackageName());
                    ImageButton imageButton = (ImageButton) bottomSheetLayout.findViewById(buttonId);
                    int finalI = i;
                    if (location.routes.contains(i)) {
                        imageButton.setEnabled(true);
                        imageButton.setOnClickListener(new View.OnClickListener() {
                            boolean isActive = false;
                            @Override
                            public void onClick(View view) {
                                clearMarkers();
                                clearLines();
                                if (areMarkersVisible[finalI]) {
                                    areMarkersVisible[finalI] = false;
                                    updateMarkers(locationItems.locations);
                                } else {
                                    areMarkersVisible[finalI] = true;
                                    updateMarkers(locationItems.locations);
                                }

                                isActive = !isActive;
                                if (isActive) {
                                    int buttonStateId = context.getResources().getIdentifier("route_" + finalI + "_pressed", "drawable", context.getPackageName());
                                    imageButton.setBackgroundResource(buttonStateId);
                                } else {
                                    int buttonStateId = context.getResources().getIdentifier("route_" + finalI + "_enabled", "drawable", context.getPackageName());
                                    imageButton.setBackgroundResource(buttonStateId);
                                }
                                map.invalidate();
                            }
                        });
                    } else {
                        imageButton.setEnabled(false);
                        int buttonStateId = context.getResources().getIdentifier("route_" + finalI + "_disabled", "drawable", context.getPackageName());
                        imageButton.setBackgroundResource(buttonStateId);
                    }
                }
                return true;
            }
        });
    }

    protected void updateMarkers(List<Location> locations){
        for (int check = 1; check <= 6; check++)
            if (areMarkersVisible[check]) showRoute(check);
            else hideRoute(check);

        for (Location location : locations){
            boolean temp = false;
            for (int check = 1; check <= 6; check++)
                if (areMarkersVisible[check] && location.routes.contains(check))
                    temp = true;

            if (temp) {
                Drawable newDrawable = context.getResources().getDrawable(org.osmdroid.library.R.drawable.marker_default);
                showMarker(location, newDrawable);
            } else {
                GeoPoint point = new GeoPoint(location.geometry.coordinates.get(1), location.geometry.coordinates.get(0));
                hideMarker(point);
            }
        }
    }

    protected void showMarker(Location location, Drawable newIconDrawable) {
        createMarker(location, newIconDrawable);
    }

    protected void hideMarker(GeoPoint geoPoint) {
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                if (marker.getPosition().equals(geoPoint)) {
                    map.getOverlays().remove(marker);
                    break;
                }
            }
        }
    }

    protected void showRoute(int routeId){
        String routesJson = readJson("traseu_ruta_" + routeId + ".geojson");
        RouteItems routeItem = new Gson().fromJson(routesJson, RouteItems.class);

        List<GeoPoint> geoPoints = new ArrayList<>();
        for(List<Double> route : routeItem.route.get(0).geometry.coordinates)
        {
            GeoPoint point = new GeoPoint(route.get(1), route.get(0));
            geoPoints.add(point);
        }

        Polyline polyline = new Polyline();

        int colorId = context.getResources().getIdentifier("route" + routeId, "color", context.getPackageName());
        int customColor = ContextCompat.getColor(context, colorId);
        String colorString = "#" + Integer.toHexString(customColor).substring(2);
        polyline.setColor(Color.parseColor(colorString));

        String lineTitle = "ruta" + routeId;
        polyline.setTitle(lineTitle);
        polyline.setPoints(geoPoints);

        map.getOverlayManager().add(polyline);
        map.invalidate();
    }

    protected void hideRoute(int routeId){
        String lineTitle = "ruta" + routeId;
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Polyline) {
                Polyline polyline = (Polyline) overlay;
                if (polyline.getTitle().equals(lineTitle))
                    map.getOverlays().remove(polyline);
                break;
            }
        }
    }

    protected void clearMarkers() {
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                map.getOverlays().remove(marker);
            }
        }
    }

    protected void clearLines(){
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Polyline) {
                Polyline polyline = (Polyline) overlay;
                map.getOverlays().remove(polyline);
            }
        }
    }

    protected void resetButtonStates(){
        ImageButton route1 = bottomSheetLayout.findViewById(R.id.ruta1);
        route1.setBackgroundResource(R.drawable.route_1_enabled);
        ImageButton route2 = bottomSheetLayout.findViewById(R.id.ruta2);
        route2.setBackgroundResource(R.drawable.route_2_enabled);
        ImageButton route3 = bottomSheetLayout.findViewById(R.id.ruta3);
        route3.setBackgroundResource(R.drawable.route_3_enabled);
        ImageButton route4 = bottomSheetLayout.findViewById(R.id.ruta4);
        route4.setBackgroundResource(R.drawable.route_4_enabled);
        ImageButton route5 = bottomSheetLayout.findViewById(R.id.ruta5);
        route5.setBackgroundResource(R.drawable.route_5_enabled);
        ImageButton route6 = bottomSheetLayout.findViewById(R.id.ruta6);
        route6.setBackgroundResource(R.drawable.route_6_enabled);
    }

    protected void initializeMyLocationOnMap() {
        MyLocationNewOverlay myLocation = new MyLocationNewOverlay(map);

        Drawable customArrowDrawable = context.getResources().getDrawable(R.drawable.navigation_arrow);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) customArrowDrawable;
        Bitmap bitmapArrow = bitmapDrawable.getBitmap();

        myLocation.setDirectionIcon(bitmapArrow);
        map.getOverlays().add(myLocation);
        myLocation.enableMyLocation();
        myLocation.enableFollowLocation();

        myLocationButton.setOnClickListener(v -> {
            GeoPoint myLocationGeoPoint = myLocation.getMyLocation();
            mapController.animateTo(myLocationGeoPoint, 18.0, 2000L);
        });
    }

    protected void initializeDefaultMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);

        mapController = map.getController();
        mapController.setCenter(new GeoPoint(47.206602, 27.800557));
        mapController.setZoom(18.0);

        initializeRotationGestures();

        initializeButtons();

        initializeScaleBar();

        myLocationButton = ((MainActivity)context).findViewById(R.id.back_to_my_location);

        bottomSheetLayout = ((MainActivity)context).findViewById(R.id.bottomSheetLayout);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                addStations();
                clearLines();
                resetButtonStates();
                areMarkersVisible[1] = areMarkersVisible[2] = areMarkersVisible[3] = false;
                areMarkersVisible[4] = areMarkersVisible[5] = areMarkersVisible[6] = false;
                map.invalidate();
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(((MainActivity)context).getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);
    }

    protected void initializeRotationGestures(){
        RotationGestureOverlay rotationGesture = new RotationGestureOverlay(map);
        rotationGesture.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(rotationGesture);
    }

    protected void initializeButtons(){
        ImageButton zoomInButton = ((MainActivity)context).findViewById(R.id.zoom_in_button);
        ImageButton zoomOutButton = ((MainActivity)context).findViewById(R.id.zoom_out_button);
        zoomInButton.setOnClickListener((view) -> map.getController().zoomIn());
        zoomOutButton.setOnClickListener((view) -> map.getController().zoomOut());

        ImageButton expandMapView = ((MainActivity)context).findViewById(R.id.expand_map_button);
        GeoPoint mapCenter = new GeoPoint(47.215606, 27.795);
        expandMapView.setOnClickListener((view) -> {
            mapController.animateTo(mapCenter, 13.8, 2000L);
            map.setMapOrientation(0.0f);
        });
    }

    protected void initializeScaleBar(){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setTextSize(32);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);
    }

    public void onPause() {
        map.onPause();
    }

    public void onResume() {
        map.onResume();
    }
}
