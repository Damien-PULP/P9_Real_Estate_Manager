/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.utils.GeoLocation;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPhotosList;
import com.openclassrooms.realestatemanager.view.fragment.DetailFragment;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogEditProperty extends AlertDialog implements GeoLocation.GeoLocationService {

    private final Activity activity;
    //UI
    private LinearLayout layoutParent;

    private ImageButton btnAddPhotoProperty;
    private TextInputLayout inputTypeProperty;
    private TextInputLayout inputPrisProperty;
    private TextInputLayout inputNumberRoomProperty;
    private TextInputLayout inputAreaProperty;
    private TextInputLayout inputDescriptionProperty;

    //Address dialog
    private TextInputLayout inputAddressCountryProperty;
    private TextInputLayout inputAddressCityProperty;
    private TextInputLayout inputAddressPostalCodeProperty;
    private TextInputLayout inputAddressStreetProperty;
    private TextInputLayout inputAddressNumberStreetProperty;

    private Button buttonAddressProperty;

    //ProgressBar for Loading
    private ProgressBar progressBar;

    private ChipGroup chipGroupPointOfInterestProperty;
    private TextInputLayout inputPointOfInterestProperty;

    private Button btnUpdateProperty;
    private RecyclerView recyclerViewPhotos;

    private AdapterRecyclerViewPhotosList adapter;

    //DATA
    private final MainViewModel mainViewModel;
    private final DetailFragment fragmentParent;
    private final PropertyObj property;
    private Address addressOfProperty;

    private final static int INPUT_FILE_REQUEST_CODE = 101;

    public DialogEditProperty(Context context, Activity activity, PropertyObj property, DetailFragment fragment, MainViewModel mainViewModel) {
        super(context);
        this.activity = activity;
        this.property = property;
        this.fragmentParent = fragment;
        this.mainViewModel = mainViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View content = LayoutInflater.from(getContext()).inflate(R.layout.add_fragment, null);
        setView(content);
        configureDialogUI(content);
        super.onCreate(savedInstanceState);
    }

    //Update UI with data
    private void configureDialogUI(View root) {
        // INIT
        layoutParent = root.findViewById(R.id.add_fragment_linear_layout);

        btnAddPhotoProperty = root.findViewById(R.id.add_fragment_add_photo_button);
        inputTypeProperty = root.findViewById(R.id.add_fragment_type_edit);
        inputPrisProperty = root.findViewById(R.id.add_fragment_pris_edit);
        inputNumberRoomProperty = root.findViewById(R.id.add_fragment_number_room_edit);
        inputAreaProperty = root.findViewById(R.id.add_fragment_area_edit);
        inputDescriptionProperty = root.findViewById(R.id.add_fragment_description_edit);

        buttonAddressProperty = root.findViewById(R.id.add_fragment_button_add_address);

        progressBar = root.findViewById(R.id.add_fragment_progressBar);

        chipGroupPointOfInterestProperty = root.findViewById(R.id.add_fragment_point_of_interest_chip_group);
        inputPointOfInterestProperty = root.findViewById(R.id.add_fragment_point_of_interest_input);

        btnUpdateProperty = root.findViewById(R.id.add_fragment_valid_button);
        recyclerViewPhotos = root.findViewById(R.id.add_fragment_recycler_view_photos);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterRecyclerViewPhotosList(true, mainViewModel);
        recyclerViewPhotos.setAdapter(adapter);

        updateUI();

    }
    private void updateUI() {
        //DATA
        for(Photo photo : property.getPhotos()){
            mainViewModel.addPhotoOfTheProperty(photo);
        }
        addressOfProperty  = property.getAddress();

        layoutParent.setPadding(8,12,8,8);
        Objects.requireNonNull(this.inputTypeProperty.getEditText()).setText(property.getProperty().getType());
        Objects.requireNonNull(this.inputPrisProperty.getEditText()).setText(String.valueOf(property.getProperty().getPris()));
        Objects.requireNonNull(this.inputNumberRoomProperty.getEditText()).setText(String.valueOf(property.getProperty().getNbRoom()));
        Objects.requireNonNull(this.inputAreaProperty.getEditText()).setText(String.valueOf(property.getProperty().getArea()));
        Objects.requireNonNull(this.inputDescriptionProperty.getEditText()).setText(property.getProperty().getDescription());
        this.buttonAddressProperty.setText(property.getAddress().toString());

        this.buttonAddressProperty.setOnClickListener(v-> showDialogEditAddress());
        this.btnUpdateProperty.setOnClickListener(v-> updateThisProperty());
        this.btnUpdateProperty.setText(R.string.update_property);

        adapter.updateData(mainViewModel.getPhotosOfTheProperty());

        for(PointOfInterest point : property.getPointOfInterests()){
            Chip chip = new Chip(getContext());
            chip.setText(point.getName());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(view -> chipGroupPointOfInterestProperty.removeView(chip));
            chipGroupPointOfInterestProperty.addView(chip);
        }
        inputPointOfInterestProperty.setEndIconOnClickListener(v-> {
            if(!Objects.requireNonNull(inputPointOfInterestProperty.getEditText()).getText().toString().equals("")) {
                Chip chip = new Chip(getContext());
                chip.setText(inputPointOfInterestProperty.getEditText().getText().toString());
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(view -> chipGroupPointOfInterestProperty.removeView(chip));
                chipGroupPointOfInterestProperty.addView(chip);
                Objects.requireNonNull(inputPointOfInterestProperty.getEditText()).setText("");
            }
        });

        btnAddPhotoProperty.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra(Intent.EXTRA_TITLE, activity.getResources().getString(R.string.select_a_picture));
            fragmentParent.startActivityForResult(photoPickerIntent, INPUT_FILE_REQUEST_CODE);
        });
    }
    //Update the property with the new information
    private void updateThisProperty() {

        String type = Objects.requireNonNull(inputTypeProperty.getEditText()).getText().toString();
        float pris = Float.parseFloat(Objects.requireNonNull(inputPrisProperty.getEditText()).getText().toString());
        int nbRoom = Integer.parseInt(Objects.requireNonNull(inputNumberRoomProperty.getEditText()).getText().toString());
        int area = Integer.parseInt(Objects.requireNonNull(inputAreaProperty.getEditText()).getText().toString());
        String description = Objects.requireNonNull(inputDescriptionProperty.getEditText()).getText().toString();

        List<PointOfInterest> pointsOfInterest = new ArrayList<>();
        for(int i= 0 ; i < chipGroupPointOfInterestProperty.getChildCount(); i++){
            Chip chip = (Chip) chipGroupPointOfInterestProperty.getChildAt(i);
            PointOfInterest pointOfInterest = new PointOfInterest(chip.getText().toString(), 0);
            pointsOfInterest.add(pointOfInterest);
        }

        if (!type.equals("") && pris != 0 && nbRoom != 0 && !description.equals("")) {
            if(addressOfProperty.getCountry() != null
                    && addressOfProperty.getCity() != null
                    && addressOfProperty.getPostalCode() != null
                    && addressOfProperty.getStreet() != null
                    && addressOfProperty.getNumberStreet() != null){

                Property propertyUpdated = this.property.getProperty();
                propertyUpdated.setType(type);
                propertyUpdated.setPris(pris);
                propertyUpdated.setNbRoom(nbRoom);
                propertyUpdated.setArea(area);
                propertyUpdated.setDescription(description);

                mainViewModel.updateProperty(propertyUpdated, addressOfProperty, mainViewModel.getPhotosOfTheProperty(), pointsOfInterest);

                showToastWithText(activity.getResources().getString(R.string.msg_property_is_updated));

                Intent intent = activity.getIntent();
                activity.finish();
                activity.startActivity(intent);
            }else{
                showToastWithText(activity.getResources().getString(R.string.msg_alert_complete_address));
            }
        }else{
            showToastWithText(activity.getResources().getString(R.string.msg_alert_complete_all_field));
        }
    }
    //Show a dialog for modify the address
    private void showDialogEditAddress() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.title_dialog_address);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_select_address, null);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setPositiveButton(R.string.valid_response, (((dialogInterface, i) -> {
            if(inputAddressCountryProperty != null && !Objects.requireNonNull(inputAddressNumberStreetProperty.getEditText()).getText().toString().equals("")){
                String addressCountry = Objects.requireNonNull(inputAddressCountryProperty.getEditText()).getText().toString();
                String addressCity = Objects.requireNonNull(inputAddressCityProperty.getEditText()).getText().toString();
                String addressPostalCode = Objects.requireNonNull(inputAddressPostalCodeProperty.getEditText()).getText().toString();
                String addressStreet = Objects.requireNonNull(inputAddressStreetProperty.getEditText()).getText().toString();
                int addressNumberStreet = Integer.parseInt(inputAddressNumberStreetProperty.getEditText().getText().toString());

                String address = addressNumberStreet + " " + addressStreet + ", " + addressPostalCode + ", " + addressCity + ", " + addressCountry;
                addressOfProperty = new Address(addressCountry, addressCity, addressPostalCode, addressStreet, addressNumberStreet, 0, 0, 0);

                GeoLocation.getLocationOfAddress(address, activity, progressBar, layoutParent, this);
            }else{
                showToastWithText(activity.getResources().getString(R.string.msg_alert_complete_address));
            }

        })));

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        //Address dialog
        inputAddressCountryProperty = dialog.findViewById(R.id.add_fragment_address_country_edit);
        inputAddressCityProperty = dialog.findViewById(R.id.add_fragment_address_city_edit);
        inputAddressPostalCodeProperty = dialog.findViewById(R.id.add_fragment_address_postal_code_edit);
        inputAddressStreetProperty = dialog.findViewById(R.id.add_fragment_address_street_edit);
        inputAddressNumberStreetProperty = dialog.findViewById(R.id.add_fragment_address_number_street_edit);

        Objects.requireNonNull(inputAddressCountryProperty.getEditText()).setText(property.getAddress().getCountry());
        Objects.requireNonNull(inputAddressCityProperty.getEditText()).setText(property.getAddress().getCity());
        Objects.requireNonNull(inputAddressPostalCodeProperty.getEditText()).setText(property.getAddress().getPostalCode());
        Objects.requireNonNull(inputAddressStreetProperty.getEditText()).setText(property.getAddress().getStreet());
        Objects.requireNonNull(inputAddressNumberStreetProperty.getEditText()).setText(String.valueOf(property.getAddress().getNumberStreet()));
    }
    //Show a toast
    private void showToastWithText(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
    //CALLBACK of get location
    @Override
    public void onSuccessGetLocation(double latitude, double longitude, String address) {
        addressOfProperty.setLatLocation(latitude);
        addressOfProperty.setLongLocation(longitude);
        buttonAddressProperty.setText(address);
    }
    @Override
    public void onFailureGetLocation() {
        showToastWithText(activity.getResources().getString(R.string.msg_alert_enter_verified_address));
    }
}
