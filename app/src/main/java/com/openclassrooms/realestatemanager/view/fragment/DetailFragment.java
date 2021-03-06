package com.openclassrooms.realestatemanager.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPhotosList;
import com.openclassrooms.realestatemanager.view.dialog.DialogCalculatorMortgage;
import com.openclassrooms.realestatemanager.view.dialog.DialogEditProperty;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    //UI
    private NestedScrollView content;
    private TextView noPropertySelected;
    private TextView txtStateProperty;
    private TextView txtTypeProperty;
    private TextView txtDescriptionProperty;
    private TextView txtLocationProperty;
    private TextView txtAreaProperty;
    private TextView txtNbRoomProperty;
    private TextView txtPrisProperty;
    private RecyclerView recyclerViewPhotosProperty;
    private ChipGroup chipGroupPointsOfInterestProperty;
    private Button btnCalculatorMortgage;
    //DIALOG
    private TextInputLayout dialogInputDescription;
    private DialogEditProperty dialogEditProperty;
    //MENU
    private ActionMenuItemView itemSellMenu;
    private ActionMenuItemView itemEditMenu;

    private AdapterRecyclerViewPhotosList adapter;

    private GoogleMap map;
    //DATA
    private double PropertyLocationLatitude;
    private double PropertyLocationLongitude;
    private MainViewModel mainViewModel;
    private LiveData<PropertyObj> currentProperty;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        configureViewModel();
        configureUI(root);
        getData();

        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }

    private void configureUI(View root) {
        if (root.findViewById(R.id.fragment_detail_app_bar) != null) {
            Toolbar toolbar = (Toolbar) root.findViewById(R.id.fragment_detail_toolbar);
            toolbar.setTitle("");
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
        }
        txtStateProperty = root.findViewById(R.id.fragment_detail_state_text);
        txtTypeProperty = root.findViewById(R.id.fragment_detail_type);
        txtDescriptionProperty = root.findViewById(R.id.fragment_detail_description);
        txtLocationProperty = root.findViewById(R.id.fragment_detail_location);
        txtAreaProperty = root.findViewById(R.id.fragment_detail_area);
        txtPrisProperty = root.findViewById(R.id.fragment_detail_pris);
        txtNbRoomProperty = root.findViewById(R.id.fragment_detail_nb_room);
        chipGroupPointsOfInterestProperty = root.findViewById(R.id.fragment_detail_point_of_interest_chip_group);
        recyclerViewPhotosProperty = root.findViewById(R.id.fragment_detail_photos_recycler_view);
        btnCalculatorMortgage = root.findViewById(R.id.fragment_detail_calculate_mortgage_button);

        recyclerViewPhotosProperty.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterRecyclerViewPhotosList(false, mainViewModel);
        recyclerViewPhotosProperty.setAdapter(adapter);

        content = root.findViewById(R.id.fragment_detail_content);
        noPropertySelected = root.findViewById(R.id.fragment_detail_no_item_selected);
        //MENU
        itemSellMenu = root.findViewById(R.id.fragment_detail_sell_button);
        itemEditMenu = root.findViewById(R.id.fragment_detail_edit_button);
    }
    private void updateUIWithData(PropertyObj propertyObj) {
        updateUIOnYesOrNoData(true);
        txtTypeProperty.setText(propertyObj.getProperty().getType());
        txtDescriptionProperty.setText(propertyObj.getProperty().getDescription());
        String location = propertyObj.getAddress().getCountry() + ", " +  propertyObj.getAddress().getCity() + ", " +  propertyObj.getAddress().getPostalCode() + ", " +  propertyObj.getAddress().getStreet() + " " +  propertyObj.getAddress().getNumberStreet();
        txtLocationProperty.setText(location);
        txtPrisProperty.setText("$" + propertyObj.getProperty().getPris());
        txtAreaProperty.setText(propertyObj.getProperty().getArea() + " m2");
        txtNbRoomProperty.setText(propertyObj.getProperty().getNbRoom() + " rooms");

        btnCalculatorMortgage.setOnClickListener(v -> {
            final DialogCalculatorMortgage dialog = new DialogCalculatorMortgage(getActivity(), propertyObj.getProperty().getPris());
            dialog.show();
        });
        if(propertyObj.getProperty().getState().equals("IS_SELL")){
            txtStateProperty.setVisibility(View.VISIBLE);
        }else{
            txtStateProperty.setVisibility(View.GONE);
        }

        List<PointOfInterest> pointOfInterests = propertyObj.getPointOfInterests();

        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null) {
            for (PointOfInterest pointOfInterest : pointOfInterests) {
                Chip chip = new Chip(mainActivity);
                chip.setText(pointOfInterest.getName());
                chipGroupPointsOfInterestProperty.addView(chip);
            }
            if(mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_detail) != null){
                //LAND VIEW
                //MAP Fragment
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detail_location_maps_land);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
            }else{
                //MAP Fragment
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detail_location_maps);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }

            }
        }

        PropertyLocationLatitude = propertyObj.getAddress().getLatLocation();
        PropertyLocationLongitude = propertyObj.getAddress().getLongLocation();

        adapter.updateData(propertyObj.getPhotos());

        //MENU
        if(propertyObj.getProperty().getState().equals("IS_SELL")){
            itemSellMenu.setOnClickListener(v->showDialogSellProperty(true));
        }else{
            itemSellMenu.setOnClickListener(v->showDialogSellProperty(false));
        }
        itemEditMenu.setOnClickListener(view -> {
            dialogEditProperty = new DialogEditProperty(getActivity(), getActivity(), propertyObj, this, mainViewModel);
            dialogEditProperty.show();
        });

    }

    private void updateUIOnYesOrNoData(boolean state){
        if(noPropertySelected != null) {
            if (state) {
                content.setVisibility(View.VISIBLE);
                noPropertySelected.setVisibility(View.GONE);
            } else {
                content.setVisibility(View.GONE);
                noPropertySelected.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getData() {
        Long idProperty = mainViewModel.getCurrentIndexPropertyDetail();

        if (idProperty != null) {
            currentProperty = mainViewModel.getAPropertyObj(idProperty);
            currentProperty.observe(Objects.requireNonNull(getActivity()), this::updateUIWithData);
        }else{
            updateUIOnYesOrNoData(false);
        }
    }

    private void showDialogSellProperty (boolean isSell){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        if(!isSell) {
            dialogBuilder.setTitle("Are you sure of sell ?");
            dialogBuilder.setPositiveButton("Sell", (dialogInterface, i) -> mainViewModel.updateStateOfProperty(mainViewModel.getCurrentIndexPropertyDetail(), "IS_SELL"));
        }else{
            dialogBuilder.setTitle("Are you sure you indicate this is unsold ?");
            dialogBuilder.setPositiveButton("Unsold", (dialogInterface, i) -> mainViewModel.updateStateOfProperty(mainViewModel.getCurrentIndexPropertyDetail(), "NOT_SELL"));
        }

        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
    private void showDialogEditPicture(Bitmap bitmap) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Define the description");
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_edit_picture, null);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setPositiveButton("Add", ((dialogInterface, i) -> {
            if (dialogInputDescription.getEditText() != null) {
                String description = dialogInputDescription.getEditText().getText().toString();
                Photo photo = new Photo(bitmap, description, 0);
                mainViewModel.addPhotoOfTheProperty(photo);
            }else{
                Log.e("AddFragment", "Error View");
            }
        }));
        alertDialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        ImageView imgVPicture = dialog.findViewById(R.id.dialog_edit_picture);
        imgVPicture.setImageBitmap(bitmap);
        dialogInputDescription = dialog.findViewById(R.id.dialog_edit_picture_description_input);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng locationOfUser = new LatLng(PropertyLocationLatitude, PropertyLocationLongitude);
        map.addMarker(new MarkerOptions().position(locationOfUser).title("Property"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationOfUser, 16));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                assert data != null;
                final Uri imageUri = data.getData();
                assert imageUri != null;
                final InputStream imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(imageUri);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap bitmapCompressed = Utils.compressImage(bitmap);
                showDialogEditPicture(bitmapCompressed);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
