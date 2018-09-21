package com.my.shishir.demoapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.shishir.demoapp.model.ProductData;

public class MapPresenterImpl implements MapContract.MapPresenter {

    private final MapContract.MapView mapView;
    private final ProductData productData;

    MapPresenterImpl(MapContract.MapView mapView, ProductData productData) {
        this.mapView = mapView;
        this.productData = productData;

        setData();
    }

    private void setData() {
        mapView.setAddress(productData.getLocation().getAddress());
        mapView.setDescription(productData.getDescription());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng markerLoc = new LatLng(productData.getLocation().getLat(), productData.getLocation().getLng());

        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerLoc)      // Sets the center of the map to Mountain View
                .zoom(15)               // Sets the zoom
                .bearing(90)            // Sets the orientation of the camera to east
                .tilt(30)               // Sets the tilt of the camera to 30 degrees
                .build();
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(productData.getLocation().getLat(), productData.getLocation().getLng()))
                .title(productData.getDescription()));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
