package com.example.aburgess11.foodmood;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aburgess11.foodmood.models.Restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // the restaurant to display
    Restaurant restaurant;

    // the view objects
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvDistance) TextView tvDistance;
    @BindView(R.id.tvReviews) TextView tvReviews;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.ivBackdrop) ImageView ivBackDrop;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
//    @BindView(R.id.map) MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.framelayout, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);

        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("data"));
        Log.d("RestDetailsActivity", String.format("Showing details for '%s'", restaurant.getName()));

        tvName.setText(restaurant.getName());
        tvAddress.setText(restaurant.getAddress());
        tvReviews.setText(restaurant.getReviewCount());
        ratingBar.setRating((float) Double.parseDouble(restaurant.getRating()));

        Glide.with(ivBackDrop.getContext())
                .load(restaurant.getImageUrl())
                .centerCrop()
                .into(ivBackDrop);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        googleMap.setBuildingsEnabled(true);
    }
}
