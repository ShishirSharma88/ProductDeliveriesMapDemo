package com.my.shishir.demoapp;

import com.google.android.gms.maps.GoogleMap;

interface MapContract {
    interface MapPresenter {
        void onMapReady(GoogleMap googleMap);
    }

    interface MapView {
        void setDescription(String description);
        void setAddress(String address);
    }

}
