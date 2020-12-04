package com.example.catalogfilms.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

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

import java.util.ArrayList;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MapActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        setTitle(R.string.app_activity_map);

        initMap();
        toDefaultCentre();

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

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                WRITE_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        });
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
        startActivity(intent);
    }

    private void toCatalogActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
