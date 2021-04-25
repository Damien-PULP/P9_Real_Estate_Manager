package com.openclassrooms.realestatemanager.view.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.utils.SearchPropertyManager;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPropertyList;
import com.openclassrooms.realestatemanager.view.dialog.DialogSearchView;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements DialogSearchView.CallbackSearchDialog, SearchPropertyManager.SearchPropertyManagerCallback {

    //UI
    private RecyclerView propertiesRecyclerView;
    private ImageButton searchViewButton;
    private ImageButton closeFilterButton;
    private ConstraintLayout alertMsgFilterActiveView;
    private ProgressBar progressBar;

    private AdapterRecyclerViewPropertyList adapter;

    //DATA
    private MainViewModel mainViewModel;

    private User currentUser;
    private LifecycleOwner activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        this.activity = getActivity();
        configureViewModel();
        configureUI(root);
        getUser();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }


    private void configureUI(View root) {
        propertiesRecyclerView = root.findViewById(R.id.fragment_main_recycler_view);
        propertiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchViewButton = root.findViewById(R.id.fragment_main_search_button);
        alertMsgFilterActiveView = root.findViewById(R.id.fragment_main_alert_filter);
        progressBar = root.findViewById(R.id.fragment_main_progress_bar);

        closeFilterButton = root.findViewById(R.id.fragment_main_close_filter_button);
        closeFilterButton.setOnClickListener(view -> {
            getProperty();
            alertMsgFilterActiveView.setVisibility(View.GONE);
        });
        alertMsgFilterActiveView.setVisibility(View.GONE);
        adapter = new AdapterRecyclerViewPropertyList();
        propertiesRecyclerView.setAdapter(adapter);
        searchViewButton.setOnClickListener(v -> mainViewModel.getAllPointOfInterest().observe(getActivity(), this::showDialogSearchView));
    }

    private void showDialogSearchView(List<PointOfInterest> pointOfInterests) {
        final DialogSearchView alertDialogBuilder = new DialogSearchView(getContext(), this, pointOfInterests);
        alertDialogBuilder.show();
    }

    private void getUser() {
        mainViewModel.getUser().observe(getActivity(), this::updateCurrentUser);
    }

    private void updateCurrentUser(User user) {
        currentUser = user;
        getProperty();
    }

    private void getProperty() {
        mainViewModel.getProperty(currentUser.getId()).observe(activity, this::updateUIWithProperties);

    }

    private void updateUIWithProperties(List<PropertyObj> propertyObjs) {
        adapter.updateData(propertyObjs);
        //mainViewModel.getAPhoto(propertyObjs.get(0).getProperty().getId()).observe(Objects.requireNonNull(getActivity()), this::getAPhotoIsCompleted);
    }

    private void getAPhotoIsCompleted(Photo photo) {
        if(photo.getBitmapPhoto() != null) Log.d("MainFragment", "the photo is" + photo.getBitmapPhoto().toString());
    }

    @Override
    public void OnSearchProperty(SearchPropertyModel searchPropertyModel) {
        SearchPropertyManager searchPropertyManager = new SearchPropertyManager((MainActivity)getActivity(), mainViewModel, this, searchPropertyModel, progressBar);
        searchPropertyManager.searchPropertiesByFilter();
    }

    @Override
    public void onResearchIsCompleted(List<PropertyObj> propertyByFilter) {
        adapter.updateData(propertyByFilter);
        alertMsgFilterActiveView.setVisibility(View.VISIBLE);
    }
}
