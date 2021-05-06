package com.openclassrooms.realestatemanager.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.utils.SearchPropertyManager;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPropertyList;
import com.openclassrooms.realestatemanager.view.dialog.DialogSearchView;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

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

        activity = getActivity();
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
        progressBar.setVisibility(View.VISIBLE);

        closeFilterButton = root.findViewById(R.id.fragment_main_close_filter_button);
        closeFilterButton.setOnClickListener(view -> {
            getProperty();
            alertMsgFilterActiveView.setVisibility(View.GONE);
        });
        alertMsgFilterActiveView.setVisibility(View.GONE);
        adapter = new AdapterRecyclerViewPropertyList();
        propertiesRecyclerView.setAdapter(adapter);
        searchViewButton.setOnClickListener(v -> mainViewModel.getAllPointOfInterest().observe(Objects.requireNonNull(getActivity()), this::showDialogSearchView));
    }

    private void getUser() {
        assert mainViewModel.getUser() != null;
        mainViewModel.getUser().observe(Objects.requireNonNull(activity), this::updateCurrentUser);
    }
    private void updateCurrentUser(User user) {
        currentUser = user;
        getProperty();
    }

    private void getProperty() {
        mainViewModel.getProperty(currentUser.getId()).observe(activity, this::updateUIWithProperties);
    }
    private void updateUIWithProperties(List<PropertyObj> propertyObjs) {
        progressBar.setVisibility(View.GONE);
        adapter.updateData(propertyObjs);
    }

    private void showDialogSearchView(List<PointOfInterest> pointOfInterests) {
        final DialogSearchView alertDialogBuilder = new DialogSearchView(getContext(), this, pointOfInterests);
        alertDialogBuilder.show();
    }

    //For filter
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
