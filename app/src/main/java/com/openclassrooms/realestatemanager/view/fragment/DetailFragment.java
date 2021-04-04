package com.openclassrooms.realestatemanager.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPhotosList;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.Objects;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    //UI
    private TextView txtTypeProperty;
    private TextView txtDescriptionProperty;
    private TextView txtLocationProperty;
    private TextView txtAreaProperty;
    private TextView txtNbRoomProperty;
    private TextView txtPrisProperty;
    private RecyclerView recyclerViewPhotosProperty;

    private AdapterRecyclerViewPhotosList adapter;

    private GoogleMap map;

    private FusedLocationProviderClient fusedLocationClient;

    private MainViewModel mainViewModel;
    private LiveData<PropertyObj> currentProperty;

    private double userLatitude;
    private double userLongitude;

    private double PropertyLocationLatitude;
    private double PropertyLocationLongitude;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        configureViewModel();
        configureUI(root);
        getData();
        return root;
    }

    private void getData() {
        Long idProperty = mainViewModel.getCurrentIndexPropertyDetail();

        if (idProperty != null) {
            currentProperty = mainViewModel.getAPropertyObj(idProperty);
            currentProperty.observe(getActivity(), this::updateUIWithData);
        }
    }

    private void updateUIWithData(PropertyObj propertyObj) {
        txtTypeProperty.setText(propertyObj.getProperty().getType());
        txtDescriptionProperty.setText(propertyObj.getProperty().getDescription());
        String location = propertyObj.getAddress().getCountry() + ", " +  propertyObj.getAddress().getCountry() + ", " +  propertyObj.getAddress().getPostalCode() + ", " +  propertyObj.getAddress().getStreet() + " " +  propertyObj.getAddress().getNumberStreet();
        txtLocationProperty.setText(location);
        txtPrisProperty.setText("$" + propertyObj.getProperty().getPris());
        txtAreaProperty.setText(propertyObj.getProperty().getArea() + " m2");
        txtNbRoomProperty.setText(propertyObj.getProperty().getNbRoom() + " rooms");
        //MAP Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detail_location_maps);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        PropertyLocationLatitude = propertyObj.getAddress().getLatLocation();
        PropertyLocationLongitude = propertyObj.getAddress().getLongLocation();

        adapter.updateData(propertyObj.getPhotos());
    }


    private void configureUI(View root) {
        if (root.findViewById(R.id.fragment_detail_app_bar) != null) {
            Toolbar toolbar = (Toolbar) root.findViewById(R.id.fragment_detail_toolbar);
            toolbar.setTitle("");
            // toolbar.inflateMenu(R.menu.menu_detail_edit);
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
            activity.setSupportActionBar(toolbar);
            //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) root.findViewById(R.id.fragment_detail_toolbar_layout);
            //toolBarLayout.setTitle("");
        }
        txtTypeProperty = root.findViewById(R.id.fragment_detail_type);
        txtDescriptionProperty = root.findViewById(R.id.fragment_detail_description);
        txtLocationProperty = root.findViewById(R.id.fragment_detail_location);
        txtAreaProperty = root.findViewById(R.id.fragment_detail_area);
        txtPrisProperty = root.findViewById(R.id.fragment_detail_pris);
        txtNbRoomProperty = root.findViewById(R.id.fragment_detail_nb_room);
        recyclerViewPhotosProperty = root.findViewById(R.id.fragment_detail_photos_recycler_view);
        recyclerViewPhotosProperty.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterRecyclerViewPhotosList(false);
        recyclerViewPhotosProperty.setAdapter(adapter);

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
        Log.d("MainActivity", "The id of property is : " + mainViewModel.getCurrentIndexPropertyDetail());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng locationOfUser = new LatLng(PropertyLocationLatitude, PropertyLocationLongitude);
        map.addMarker(new MarkerOptions().position(locationOfUser).title("Property"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationOfUser, 16));

    }

}
