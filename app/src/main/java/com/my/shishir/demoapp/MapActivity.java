package com.my.shishir.demoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.my.shishir.demoapp.model.ProductData;
import com.my.shishir.demoapp.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapContract.MapView {

    @BindView(R.id.text_description_map)
    TextView textDescription;

    @BindView(R.id.text_address_map)
    TextView textAddress;

    private MapPresenterImpl mapPresenterImpl;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        unbinder = ButterKnife.bind(this);

        SupportMapFragment supportMapFragment
                = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);

        mapPresenterImpl = new MapPresenterImpl(this, getProductDetail());
    }

    @SuppressLint("NewApi")
    private ProductData getProductDetail() {
        return (ProductData) getIntent().getSerializableExtra(Utility.KEY_PRODUCT_DETAIL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         mapPresenterImpl.onMapReady(googleMap);
    }

    @Override
    public void setDescription(String description) {
        textDescription.setText(description);
    }

    @Override
    public void setAddress(String address) {
        textAddress.setText(address);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
