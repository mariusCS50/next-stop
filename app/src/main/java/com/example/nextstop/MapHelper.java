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

import androidx.core.content.res.ResourcesCompat;

import com.example.nextstop.PublicTransportRoutes.Route;
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
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
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

    public MapHelper(Context context, MapView map) {
        this.context = context;
        this.map = map;
    }

    protected String readLocationsJson() {

        try (InputStream inputStream = context.getAssets().open("stations_locations.json")) {
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
        String locationsJson = readLocationsJson();
        locationItems = new Gson().fromJson(locationsJson, LocationItems.class);

        for (Location location : locationItems.locations) {
            Marker marker = new Marker(map);
            marker.setTitle(location.id);

            Drawable originalMarkerDrawable = context.getResources().getDrawable(R.drawable.bus_stop);
            marker.setIcon(resizeIcon(originalMarkerDrawable));

            marker.setPosition(new GeoPoint(location.geometry.coordinates.get(1), location.geometry.coordinates.get(0)));
            map.getOverlays().add(marker);

            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    map.getController().animateTo(marker.getPosition());
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    TextView textView = (TextView) bottomSheetLayout.findViewById(R.id.stationName);
                    String stationTitle = marker.getTitle();

                    textView.setText(stationTitle);

                    for (int i = 1; i <= 6; i++) {
                        int buttonId = context.getResources().getIdentifier("ruta" + i, "id", context.getPackageName());
                        ImageButton imageButton = (ImageButton) bottomSheetLayout.findViewById(buttonId);

                        if (location.routes.contains(i)) {
                            imageButton.setEnabled(true);
                            final boolean[] areMarkersVisible = {false};

                            int finalI = i;
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (areMarkersVisible[0]) {
                                        smallMarkers(locationItems.locations, finalI);
                                    } else {
                                        bigMarkers(locationItems.locations, finalI);
                                    }
                                    map.invalidate();
                                    areMarkersVisible[0] = !areMarkersVisible[0];
                                }
                            });
                        } else {
                            imageButton.setEnabled(false);
                        }
                    }
                    return true;
                }
            });
        }
    }

    protected Drawable resizeIcon(Drawable bruteDrawable){
        int markerWidth = 40;
        int markerHeight = 52;
        Bitmap originalBitmap = ((BitmapDrawable) bruteDrawable).getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, markerWidth, markerHeight, false);
        Drawable scaledDrawable = new BitmapDrawable(context.getResources(), scaledBitmap);
        return  scaledDrawable;
    }

    protected void bigMarkers(List<Location> locations, int i) {
        for (Location location : locations){
            if (location.routes.contains(i)){
                GeoPoint point = new GeoPoint(location.geometry.coordinates.get(1), location.geometry.coordinates.get(0));
                Drawable originalDrawable = context.getResources().getDrawable(org.osmdroid.library.R.drawable.marker_default);
                updateMarker(point, resizeIcon(originalDrawable));
            }

        }
    }

    protected void smallMarkers(List<Location> locations, int i) {
        for (Location location : locations){
            if (location.routes.contains(i)){
                GeoPoint point = new GeoPoint(location.geometry.coordinates.get(1), location.geometry.coordinates.get(0));
                Drawable originalDrawable = context.getResources().getDrawable(R.drawable.bus_stop);
                updateMarker(point, resizeIcon(originalDrawable));
            }

        }
    }

    protected void updateMarker(GeoPoint geoPoint, Drawable iconDrawable) {
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                if (marker.getPosition().equals(geoPoint)) {
                    marker.setIcon(iconDrawable);
                    break;
                }
            }
        }
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
                smallMarkers(locationItems.locations, 1);
                smallMarkers(locationItems.locations, 2);
                smallMarkers(locationItems.locations, 3);
                smallMarkers(locationItems.locations, 4);
                smallMarkers(locationItems.locations, 5);
                smallMarkers(locationItems.locations, 6);
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
        expandMapView.setOnClickListener((view) -> mapController.animateTo(mapCenter, 13.8, 2000L));
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
