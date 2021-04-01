package com.openclassrooms.realestatemanager.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPropertyList;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {

    //UI
    private RecyclerView propertiesRecyclerView;

    private AdapterRecyclerViewPropertyList adapter;

    private MainViewModel mainViewModel;

    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        configureViewModel();
        configureUI(root);
        getUser();
        return root;
    }



    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }


    private void configureUI(View root) {
        propertiesRecyclerView = root.findViewById(R.id.fragment_main_recycler_view);
        propertiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdapterRecyclerViewPropertyList();
        propertiesRecyclerView.setAdapter(adapter);
    }

    private void getUser() {
        mainViewModel.getUser().observe(getActivity(), this::updateCurrentUser);
    }

    private void updateCurrentUser(User user) {
        currentUser = user;
        getProperty();
    }

    private void getProperty() {
        mainViewModel.getProperty(currentUser.getId()).observe(getActivity(), this::updateUIWithProperties);
    }

    private void updateUIWithProperties(List<PropertyObj> propertyObjs) {
        adapter.updateData(propertyObjs);
    }

}
