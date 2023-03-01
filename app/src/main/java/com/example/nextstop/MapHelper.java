package com.example.nextstop;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nextstop.StationModels.Location;
import com.example.nextstop.StationModels.LocationItems;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class MapHelper {
    private final Context context;
    private final MapView map;
    private IMapController mapController;
    private ImageButton myLocationButton;

    public MapHelper(Context context, MapView map) {
        this.context = context;
        this.map = map;
    }

    protected String readLocationsJson() {

        try (InputStream inputStream = context.getAssets().open("stations_location.json")) {
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
        LocationItems locationItems = new Gson().fromJson(locationsJson, LocationItems.class);

        for (Location location : locationItems.locations) {
            Marker marker = new Marker(map);
            marker.setTitle(location.id);

            marker.setPosition(new GeoPoint(location.geometry.coordinates.get(1), location.geometry.coordinates.get(0)));
            map.getOverlays().add(marker);

            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    map.getController().animateTo(marker.getPosition());

                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(context.getApplicationContext())
                            .inflate(R.layout.bottom_sheet_layout,
                                    ((MainActivity)context).findViewById(R.id.bottomSheetContainer));
                    TextView textView = (TextView) bottomSheetView.findViewById(R.id.stationName);
                    textView.setText(marker.getTitle());

                    bottomSheetDialog.getWindow().setDimAmount(0);
                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                    return true;
                }
            });
        }
    }

    protected void initializeMyLocationOnMap() {
        MyLocationNewOverlay myLocation = new MyLocationNewOverlay(map);
        map.getOverlays().add(myLocation);
        myLocation.enableMyLocation();
        myLocation.enableFollowLocation();

        myLocationButton.setVisibility(View.VISIBLE);
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

        initializeZoomButtons();

        initializeCompass();

        initializeScaleBar();

        myLocationButton = ((MainActivity)context).findViewById(R.id.back_to_my_location);
        myLocationButton.setVisibility(View.INVISIBLE);

    }

    protected void initializeRotationGestures(){
        RotationGestureOverlay rotationGesture = new RotationGestureOverlay(map);
        rotationGesture.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(rotationGesture);
    }

    protected void initializeZoomButtons(){
        ImageButton zoomInButton = ((MainActivity)context).findViewById(R.id.zoom_in_button);
        ImageButton zoomOutButton = ((MainActivity)context).findViewById(R.id.zoom_out_button);
        zoomInButton.setOnClickListener((view) -> map.getController().zoomIn());
        zoomOutButton.setOnClickListener((view) -> map.getController().zoomOut());
    }

    protected void initializeCompass(){
        ImageButton compassButton = ((MainActivity)context).findViewById(R.id.compass_button);
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    protected void initializeScaleBar(){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setTextSize(30);
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
