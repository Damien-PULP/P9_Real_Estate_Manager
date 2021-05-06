/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.fragment;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.utils.GeoLocation;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPhotosList;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment implements GeoLocation.GeoLocationService {

    //UI
    private LinearLayout layoutParent;

    private ImageButton btnAddPhotoProperty;
    private TextInputLayout inputTypeProperty;
    private TextInputLayout inputPrisProperty;
    private TextInputLayout inputNumberRoomProperty;
    private TextInputLayout inputAreaProperty;
    private TextInputLayout inputDescriptionProperty;

    private ChipGroup chipGroupPointOfInterestProperty;
    private TextInputLayout inputPointOfInterestProperty;

    private Button btnAddProperty;
    private RecyclerView recyclerViewPhotos;

    private AdapterRecyclerViewPhotosList adapter;
    //ProgressBar for Loading
    private ProgressBar progressBar;
    //Address dialog
    private TextInputLayout inputAddressCountryProperty;
    private TextInputLayout inputAddressCityProperty;
    private TextInputLayout inputAddressPostalCodeProperty;
    private TextInputLayout inputAddressStreetProperty;
    private TextInputLayout inputAddressNumberStreetProperty;

    private Button buttonAddressProperty;
    // Photos dialog
    private TextInputLayout dialogInputDescription;
    //Data
    private MainViewModel mainViewModel;
    private User currentUser;
    private Address addressOfProperty = new Address();
    //CONSTANT
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL_PROPERTY";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_fragment, container, false);
        configureViewModel();
        getUser();
        configureUI(root);
        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }

    private void getUser() {
        assert mainViewModel.getUser() != null;
        mainViewModel.getUser().observe(Objects.requireNonNull(getActivity()), this::updateCurrentUser);
    }
    private void updateCurrentUser(User user) {
        currentUser = user;
    }

    private void configureUI(View root) {
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

        inputPointOfInterestProperty.setEndIconOnClickListener(v-> {
            if(!Objects.requireNonNull(inputPointOfInterestProperty.getEditText()).getText().toString().equals("")) {
                Chip chip = new Chip(Objects.requireNonNull(getActivity()));
                chip.setText(inputPointOfInterestProperty.getEditText().getText().toString());
                chipGroupPointOfInterestProperty.addView(chip);
                inputPointOfInterestProperty.getEditText().setText("");
            }
        });

        buttonAddressProperty.setOnClickListener(v -> showDialogEditAddress());

        btnAddProperty = root.findViewById(R.id.add_fragment_valid_button);
        recyclerViewPhotos = root.findViewById(R.id.add_fragment_recycler_view_photos);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterRecyclerViewPhotosList(true, mainViewModel);
        recyclerViewPhotos.setAdapter(adapter);

        btnAddPhotoProperty.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.putExtra(Intent.EXTRA_TITLE, "Select a picture");
            startActivityForResult(photoPickerIntent, INPUT_FILE_REQUEST_CODE);
        });
        btnAddProperty.setOnClickListener(v -> insertProperty());
    }
    //Create a property
    private void insertProperty() {
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
                Property property = new Property(type, pris, nbRoom, area, description, "NOT_SELL", new Date(), null, currentUser.getId());
                mainViewModel.insertProperty(property, addressOfProperty, mainViewModel.getPhotosOfTheProperty(), pointsOfInterest);

                showToastWithText("The property are created !");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence nameChannel = "notification_chanel";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, nameChannel, importance);
                    channel.setDescription(description);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getContext()), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_home_24)
                            .setContentTitle("The " + property.getType() + " have been created in the database !")
                            .setContentText(addressOfProperty.toString() + "\n" + property.getDescription() + "\n" + property.getPris() + "$")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManager notificationManager = Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(111, builder.build());
                }

                MainActivity activity = (MainActivity) getActivity();
                if(activity != null) activity.switchFragment(0);
            }else{
                showToastWithText("Complete the address of the property");
            }
        }else{
            showToastWithText("Complete all field");
        }
    }
    //Show dialog address or picture
    private void showDialogEditAddress() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Enter the address");
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_select_address, null);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setPositiveButton("Valid", (((dialogInterface, i) -> {
            if(inputAddressCountryProperty != null && !Objects.requireNonNull(inputAddressNumberStreetProperty.getEditText()).getText().toString().equals("")){
                String addressCountry = Objects.requireNonNull(inputAddressCountryProperty.getEditText()).getText().toString();
                String addressCity = Objects.requireNonNull(inputAddressCityProperty.getEditText()).getText().toString();
                String addressPostalCode = Objects.requireNonNull(inputAddressPostalCodeProperty.getEditText()).getText().toString();
                String addressStreet = Objects.requireNonNull(inputAddressStreetProperty.getEditText()).getText().toString();
                int addressNumberStreet = Integer.parseInt(inputAddressNumberStreetProperty.getEditText().getText().toString());

                String address = addressNumberStreet + " " + addressStreet + ", " + addressPostalCode + ", " + addressCity + ", " + addressCountry;
                addressOfProperty = new Address(addressCountry, addressCity, addressPostalCode, addressStreet, addressNumberStreet, 0, 0, 0);

                GeoLocation.getLocationOfAddress(address, getActivity(), progressBar, layoutParent, this);
            }else{
                showToastWithText("Enter a competed address");
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
                adapter.updateData(mainViewModel.getPhotosOfTheProperty());
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

    private void showToastWithText(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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

    @Override
    public void onSuccessGetLocation(double latitude, double longitude, String address) {
        addressOfProperty.setLatLocation(latitude);
        addressOfProperty.setLongLocation(longitude);
        buttonAddressProperty.setText(address);
    }
    @Override
    public void onFailureGetLocation() {
        showToastWithText("Enter a verified address");
    }
}
