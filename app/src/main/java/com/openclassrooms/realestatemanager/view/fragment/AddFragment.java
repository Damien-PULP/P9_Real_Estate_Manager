/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.User;
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
    private Button btnAddProperty;
    private RecyclerView recyclerViewPhotos;
    private AdapterRecyclerViewPhotosList adapter;
    // Dialog
    private TextInputLayout dialogInputDescription;

    private MainViewModel mainViewModel;

    private User currentUser;

    public static final int INPUT_FILE_REQUEST_CODE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_fragment, container, false);
        configureViewModel();
        getUser();
        configureUI(root);
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
        btnAddProperty = root.findViewById(R.id.add_fragment_valid_button);
        recyclerViewPhotos = root.findViewById(R.id.add_fragment_recycler_view_photos);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterRecyclerViewPhotosList();
        recyclerViewPhotos.setAdapter(adapter);

        btnAddPhotoProperty.setOnClickListener(v ->{
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

        if(!type.equals("") && pris != 0 && nbRoom != 0 && !description.equals("")){
            Property property = new Property(type, pris, nbRoom, area, description, "NOT_SELL", new Date(), null, currentUser.getId());
            Address address = new Address("USA", "New-York", "1000", "Mega streetzer", 666, 0);
            Photo photo = new Photo(null, description, 0);
            PointOfInterest pointOfInterest = new PointOfInterest("School", 0);
            List<Photo> photoList = new ArrayList<>();
            List<PointOfInterest> pointOfInterests = new ArrayList<>();
            photoList.add(photo);
            pointOfInterests.add(pointOfInterest);

            mainViewModel.insertProperty(property, address, photoList, pointOfInterests);
        }
    }

    private void showDialogEditPicture(Bitmap bitmap){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Define the description");
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_edit_picture, null);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setPositiveButton("Add", ((dialogInterface, i) -> {
            List<Photo> photos = Arrays.asList(new Photo(bitmap, "input", 0));
            adapter.updateData(photos);
        }));
        alertDialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        ImageView imgVPicture = dialog.findViewById(R.id.dialog_edit_picture);
        dialogInputDescription = dialog.findViewById(R.id.dialog_edit_picture_description_input);

        imgVPicture.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            try{
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                showDialogEditPicture(bitmap);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
