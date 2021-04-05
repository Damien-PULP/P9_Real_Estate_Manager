/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPhotosList;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {

    //UI
    private ImageButton btnAddPhotoProperty;
    private TextInputLayout inputTypeProperty;
    private TextInputLayout inputPrisProperty;
    private TextInputLayout inputNumberRoomProperty;
    private TextInputLayout inputAreaProperty;
    private TextInputLayout inputDescriptionProperty;

    private TextInputLayout inputAddressCountryProperty;
    private TextInputLayout inputAddressCityProperty;
    private TextInputLayout inputAddressPostalCodeProperty;
    private TextInputLayout inputAddressStreetProperty;
    private TextInputLayout inputAddressNumberStreetProperty;

    private ChipGroup chipGroupPointOfInterestProperty;
    private TextInputLayout inputPointOfInterestProperty;

    private Button btnAddProperty;
    private RecyclerView recyclerViewPhotos;

    private AdapterRecyclerViewPhotosList adapter;
    // Dialog
    private TextInputLayout dialogInputDescription;

    private MainViewModel mainViewModel;

    private User currentUser;

    public static final int INPUT_FILE_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private double userLatitude;
    private double userLongitude;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_fragment, container, false);
        configureViewModel();
        getUser();
        configureUI(root);
        getCurrentLocation();
        return root;
    }

    private void getUser() {
        mainViewModel.getUser().observe(getActivity(), this::updateCurrentUser);
    }

    private void updateCurrentUser(User user) {
        currentUser = user;
    }

    private void configureUI(View root) {
        btnAddPhotoProperty = root.findViewById(R.id.add_fragment_add_photo_button);
        inputTypeProperty = root.findViewById(R.id.add_fragment_type_edit);
        inputPrisProperty = root.findViewById(R.id.add_fragment_pris_edit);
        inputNumberRoomProperty = root.findViewById(R.id.add_fragment_number_room_edit);
        inputAreaProperty = root.findViewById(R.id.add_fragment_area_edit);
        inputDescriptionProperty = root.findViewById(R.id.add_fragment_description_edit);

        inputAddressCountryProperty = root.findViewById(R.id.add_fragment_address_country_edit);
        inputAddressCityProperty = root.findViewById(R.id.add_fragment_address_city_edit);
        inputAddressPostalCodeProperty = root.findViewById(R.id.add_fragment_address_postal_code_edit);
        inputAddressStreetProperty = root.findViewById(R.id.add_fragment_address_street_edit);
        inputAddressNumberStreetProperty = root.findViewById(R.id.add_fragment_address_number_street_edit);

        chipGroupPointOfInterestProperty = root.findViewById(R.id.add_fragment_point_of_interest_chip_group);
        inputPointOfInterestProperty = root.findViewById(R.id.add_fragment_point_of_interest_input);

        inputPointOfInterestProperty.setEndIconOnClickListener(v-> {
            if(!inputPointOfInterestProperty.getEditText().getText().toString().equals("")) {
                Chip chip = new Chip(getActivity());
                chip.setText(inputPointOfInterestProperty.getEditText().getText().toString());
                chipGroupPointOfInterestProperty.addView(chip);
                inputPointOfInterestProperty.getEditText().setText("");
            }
        });

        btnAddProperty = root.findViewById(R.id.add_fragment_valid_button);
        recyclerViewPhotos = root.findViewById(R.id.add_fragment_recycler_view_photos);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterRecyclerViewPhotosList(true);
        recyclerViewPhotos.setAdapter(adapter);

        btnAddPhotoProperty.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra(Intent.EXTRA_TITLE, "Select a picture");

            startActivityForResult(photoPickerIntent, INPUT_FILE_REQUEST_CODE);
        });
        btnAddProperty.setOnClickListener(v -> insertProperty());
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }

    private void insertProperty() {
        String type = inputTypeProperty.getEditText().getText().toString();
        float pris = Float.parseFloat(inputPrisProperty.getEditText().getText().toString());
        int nbRoom = Integer.parseInt(inputNumberRoomProperty.getEditText().getText().toString());
        int area = Integer.parseInt(inputAreaProperty.getEditText().getText().toString());
        String description = inputDescriptionProperty.getEditText().getText().toString();
        //Address
        String addressCountry = inputAddressCountryProperty.getEditText().getText().toString();
        String addressCity = inputAddressCityProperty.getEditText().getText().toString();
        String addressPostalCode = inputAddressPostalCodeProperty.getEditText().getText().toString();
        String addressStreet = inputAddressStreetProperty.getEditText().getText().toString();
        int addressNumberStreet = Integer.parseInt(inputAddressNumberStreetProperty.getEditText().getText().toString());

        List<PointOfInterest> pointsOfInterest = new ArrayList<>();
        for(int i= 0 ; i < chipGroupPointOfInterestProperty.getChildCount(); i++){
            Chip chip = (Chip) chipGroupPointOfInterestProperty.getChildAt(i);
            PointOfInterest pointOfInterest = new PointOfInterest(chip.getText().toString(), 0);
            pointsOfInterest.add(pointOfInterest);
        }

        getCurrentLocation();

        if (!type.equals("") && pris != 0 && nbRoom != 0 && !description.equals("")) {
            Property property = new Property(type, pris, nbRoom, area, description, "NOT_SELL", new Date(), null, currentUser.getId());
            Address address = new Address(addressCountry, addressCity, addressPostalCode, addressStreet, addressNumberStreet, userLatitude, userLongitude, 0);

            mainViewModel.insertProperty(property, address, mainViewModel.getPhotosOfTheProperty(), pointsOfInterest);
        }
    }

    private void showDialogEditPicture(Bitmap bitmap) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Define the description");
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_edit_picture, null);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setPositiveButton("Add", ((dialogInterface, i) -> {
            if (dialogInputDescription.getEditText() != null) {
                String description = dialogInputDescription.getEditText().getText().toString();
                Photo photo = new Photo(bitmap, description, 0);
                mainViewModel.addPhotoOfTheProperty(photo);
                adapter.updateData(mainViewModel.getPhotosOfTheProperty());
            }
        }));
        alertDialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        ImageView imgVPicture = dialog.findViewById(R.id.dialog_edit_picture);
        imgVPicture.setImageBitmap(bitmap);
        dialogInputDescription = dialog.findViewById(R.id.dialog_edit_picture_description_input);


    }

    private void getCurrentLocation() {
        //GET LOCATION
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (mainViewModel.checkPermissionLocation((MainActivity) getActivity())) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap bitmapCompressed = Utils.compressImage(bitmap);
                showDialogEditPicture(bitmapCompressed);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
