package br.com.devmeeting.fragments;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.devmeeting.R;
import br.com.devmeeting.models.Event;
import br.com.devmeeting.networks.RSSReaderService;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    public static final int DEFAULT_MAP_ZOOM = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onStart() {
        this.googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        this.googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.setupGoogleMaps();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.setupGoogleMaps();
            }
        }
    }

    private void setupGoogleMaps() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            if (this.googleMap != null) {
                this.googleMap.setMyLocationEnabled(true);

                List<Event> eventList = new ArrayList<>();
                RSSReaderService rssReaderService = RSSReaderService.getInstance(this.getContext());
                eventList.addAll(rssReaderService.getAll());
                if (eventList.isEmpty()) {
                    rssReaderService.fetchAsync();
                } else {
                    this.addEventsMarks(eventList);
                }
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(
                    this.googleApiClient);
            if (location != null) {
                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_MAP_ZOOM));
            }
        }
    }

    public void updateMap(List<Event> eventList) {
        try {
            if (this.googleMap != null) {
                Geocoder geocoder = new Geocoder(this.getActivity());
                for (Event event : eventList) {
                    if (event.getLatitude() == null || event.getLongitude() == null) {
                        List<Address> addresses = geocoder.getFromLocationName(event.getAddress(),
                                BigInteger.ONE.intValue());

                        if (addresses.size() > 0) {
                            event.setLatitude(addresses.get(0).getLatitude());
                            event.setLongitude(addresses.get(0).getLongitude());
                        }
                    }
                }

                RSSReaderService rssReaderService = RSSReaderService.getInstance(this.getContext());
                rssReaderService.saveOrUpdateAll(eventList);
                this.addEventsMarks(eventList);
            }
        } catch (IOException e) {
            Toast.makeText(this.getActivity(), this.getString(R.string.update_map_error), Toast.LENGTH_LONG).show();
        }
    }

    private void addEventsMarks(List<Event> eventList) {
        for (Event event : eventList) {
            LatLng marker = new LatLng(event.getLatitude(), event.getLongitude());
            this.googleMap.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(event.getTitle()));
        }
    }
}