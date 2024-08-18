package com.example.hancafe.Controller.Activity.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.hancafe.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hancafe.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private SharedPreferences sharedPreferences;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    private Marker centerMarker;
    private static final String PREF_LATITUDE = "latitude";
    private static final String PREF_LONGITUDE = "longitude";
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int MAP_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Khởi tạo PlacesClient
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCpHpXxdhv7kP2vnkNE0tHH95d5CuxZekE");
        }
        // Khởi tạo PlacesClient
        placesClient = Places.createClient(this);

        // Initialize AutocompleteSupportFragment
        autocompleteFragment = new AutocompleteSupportFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.autocomplete_fragment_container, autocompleteFragment)
                .commit();

        // Đặt quốc gia là Việt Nam
        autocompleteFragment.setCountry("VN");

        // Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Thiết lập sự kiện lắng nghe khi chọn một địa điểm từ gợi ý
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Lấy thông tin chi tiết về địa điểm được chọn
                LatLng latLng = place.getLatLng();

                // Di chuyển camera đến vị trí được chọn
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));

                // Thêm Marker tại vị trí được chọn
                mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
            }

            @Override
            public void onError(@NonNull Status status) {
                // Xử lý lỗi nếu có
                Log.e("Autocomplete", "An error occurred: " + status);
            }
        });

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        // Lấy dữ liệu vị trí hiện tại từ intent
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
//            double currentLatitude = intent.getDoubleExtra("latitude", 0);
//            double currentLongitude = intent.getDoubleExtra("longitude", 0);
//
//            // Sử dụng vị trí hiện tại để làm gì đó, ví dụ: đặt marker hoặc di chuyển camera
//            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
//            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Vị trí của bạn"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra(PREF_LATITUDE, 0);
            double longitude = data.getDoubleExtra(PREF_LONGITUDE, 0);


            String address = getAddressFromLatLng(latitude, longitude);

            // Gửi địa chỉ qua Intent cho Fragment
            Intent resultIntent = new Intent();
            resultIntent.putExtra("address", address);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Nhận dữ liệu vị trí từ intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PREF_LATITUDE) && intent.hasExtra(PREF_LONGITUDE)) {
            double latitude = intent.getDoubleExtra(PREF_LATITUDE, 0);
            double longitude = intent.getDoubleExtra(PREF_LONGITUDE, 0);
            LatLng location = new LatLng(latitude, longitude);

            // Di chuyển camera đến vị trí hiện tại và thêm marker
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 19));
            mMap.addMarker(new MarkerOptions().position(location).title("Vị trí hiện tại"));
        }

        // Lấy kích thước màn hình
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Tính toán tọa độ của Marker ở giữa màn hình
        LatLng centerLatLng = mMap.getProjection().fromScreenLocation(new Point(screenWidth / 2, screenHeight / 2));

        // Đặt Marker ở giữa màn hình
        centerMarker = mMap.addMarker(new MarkerOptions().position(centerLatLng));

        // Bật các nút điều khiển phóng to/thu nhỏ
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);


//        // Đặt vị trí ban đầu và mức độ phóng to
//        LatLng defaultLocation = new LatLng(10.848187194361094, 106.78668615290215);
//        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Trường HVCNBCVT cơ sở tại TP.HCM"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Lưu tọa độ vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(PREF_LATITUDE, (float) latLng.latitude);
                editor.putFloat(PREF_LONGITUDE, (float) latLng.longitude);
                editor.apply();

                mMap.clear();
                // Di chuyển camera đến vị trí đã chọn
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // Convert LatLng to Address
                String address = getAddressFromLatLng(latLng.latitude, latLng.longitude);

//                // Mở gg map
//                Uri gmmIntentUri = Uri.parse("geo:" + latLng.latitude + "," + latLng.longitude + "?q=" + Uri.encode(address));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(mapIntent);
//                } else {
//                    // Handle situation where Google Maps app is not installed
//                    // You can prompt the user to install the app or use a web browser
//                }

                // Pass the address back to the calling Fragment
                Intent resultIntent = new Intent();
                resultIntent.putExtra(PREF_LATITUDE, latLng.latitude);
                resultIntent.putExtra(PREF_LONGITUDE, latLng.longitude);
                resultIntent.putExtra("address", address);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // Lấy tọa độ của Marker
                LatLng markerLatLng = marker.getPosition();

                // Chuyển đổi tọa độ thành địa chỉ
                String address = getAddressFromLatLng(markerLatLng.latitude, markerLatLng.longitude);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(PREF_LATITUDE, markerLatLng.latitude);
                resultIntent.putExtra(PREF_LONGITUDE, markerLatLng.longitude);
                resultIntent.putExtra("address", address);

                setResult(RESULT_OK, resultIntent);
                finish();

                return true;
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // Lấy tọa độ mới của Marker ở giữa màn hình
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;

                // Lấy tọa độ mới của Marker ở giữa màn hình
                LatLng newCenterLatLng = mMap.getProjection().fromScreenLocation(new Point(screenWidth / 2, screenHeight / 2));

                // Cập nhật vị trí của Marker ở giữa màn hình
                centerMarker.setPosition(newCenterLatLng);
                centerMarker.setDraggable(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (placesClient != null) {
            // Assuming placesClient has a shutdown or similar method (example purpose)
            // placesClient.shutdown();
        }
    }

    // Lấy vị trị hiện tại của mình thông qua gps
    private void showCurrentLocation() {
        // Initialize FusedLocationProviderClient
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get the last known location
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Get current latitude and longitude
                        double currentLatitude = location.getLatitude();
                        double currentLongitude = location.getLongitude();

                        // Move camera to the current location
                        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("MapsActivity", "Error getting location: " + e.getMessage());
                }
            });
        } else {
            // Permission not granted, request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                address = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }


}