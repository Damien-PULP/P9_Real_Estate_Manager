/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

public class AddFragment extends Fragment {

    //UI
    private ImageButton btnAddPhotoProperty;
    private TextInputLayout inputTypeProperty;
    private TextInputLayout inputPrisProperty;
    private TextInputLayout inputNumberRoomProperty;
    private TextInputLayout inputDescriptionProperty;
    private Button btnAddProperty;

    private MainViewModel mainViewModel;

    private User currentUser;

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
        inputDescriptionProperty = root.findViewById(R.id.add_fragment_description_edit);
        btnAddProperty = root.findViewById(R.id.add_fragment_valid_button);

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
        String description = inputDescriptionProperty.getEditText().getText().toString();

        if(!type.equals("") && pris != 0 && nbRoom != 0 && !description.equals("")){
            mainViewModel.insertProperty(type, pris, nbRoom, description, currentUser.getId());
        }
    }
}
