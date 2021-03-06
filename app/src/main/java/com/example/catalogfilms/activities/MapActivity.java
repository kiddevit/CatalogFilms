package com.example.catalogfilms.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.catalogfilms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MapActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Switch switchTracking;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    MyLocationNewOverlay myLocationNewOverlay = null;
    private MapView map = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(this.getPackageName()); // for map
        setContentView(R.layout.activity_map);
        setTitle(R.string.app_activity_map);

        initMap();
        toDefaultCentre();

        initSwitchTracking();
        subscribeOnSwitchTracking();

        initBottomMenu();
        subscribeOnBottomNavigationView();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    private void initMap() {
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        initLocationOverlay();
        addLocationOverlay();

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                WRITE_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        });
    }

    private void initLocationOverlay() {
        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(this.getBaseContext());
        gpsMyLocationProvider.setLocationUpdateMinDistance(100); // [m]  // Set the minimum distance for location updates
        gpsMyLocationProvider.setLocationUpdateMinTime(10000);   // [ms] // Set the minimum time interval for location updates
        MyLocationNewOverlay mMyLocationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, map);
        mMyLocationOverlay.setDrawAccuracyEnabled(true);
        this.myLocationNewOverlay = mMyLocationOverlay;
    }

    private void initSwitchTracking() {
        switchTracking = (Switch) findViewById(R.id.switch_tracking);
        switchTracking.setChecked(true);
    }

    public void subscribeOnSwitchTracking() {
        switchTracking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addLocationOverlay();
                } else {
                    removeLocationOverlay();
                }
            }
        });
    }

    public void addLocationOverlay() {
        if (!map.getOverlays().contains(myLocationNewOverlay)) {
            map.getOverlays().add(myLocationNewOverlay);
        }
    }

    public void removeLocationOverlay() {
        map.getOverlays().remove(myLocationNewOverlay);
    }

    private void toDefaultCentre() {
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(53.2120, 50.1746);
        mapController.setCenter(startPoint);
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void initBottomMenu() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.map_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);
    }

    private void subscribeOnBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.catalogue:
                                toCatalogActivity();
                                break;
                            case R.id.favorite:
                                toFavoriteMovieActivity();
                                break;
                            case R.id.map:
                                break;
                        }
                        return false;
                    }
                });
    }

    private void toFavoriteMovieActivity() {
        Intent intent = new Intent(this, FavouriteMovieActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void toCatalogActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
