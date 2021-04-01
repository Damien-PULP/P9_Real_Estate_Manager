package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.Objects;

public class DetailFragment extends Fragment {

    //UI
    private TextView txtTypeProperty;
    private TextView txtDescriptionProperty;
    private TextView txtLocationProperty;
    private TextView txtAreaProperty;
    private TextView txtNbRoomProperty;
    private TextView txtPrisProperty;

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

    private void getData() {
        Long idProperty = mainViewModel.getCurrentIndexPropertyDetail();

        if(idProperty != null) {
            currentProperty = mainViewModel.getAPropertyObj(idProperty);
            currentProperty.observe(getActivity(), this::updateUIWithData);
        }
    }

    private void updateUIWithData(PropertyObj propertyObj) {
        txtTypeProperty.setText(propertyObj.getProperty().getType());
        txtDescriptionProperty.setText(propertyObj.getProperty().getDescription());
        txtLocationProperty.setText(propertyObj.getAddress().getCity());
        txtPrisProperty.setText("$" + propertyObj.getProperty().getPris());
        txtAreaProperty.setText(propertyObj.getProperty().getArea() + " m2");
        txtNbRoomProperty.setText(propertyObj.getProperty().getNbRoom() + " rooms");
    }


    private void configureUI(View root) {
        if(root.findViewById(R.id.fragment_detail_app_bar) != null) {
            Toolbar toolbar = (Toolbar) root.findViewById(R.id.fragment_detail_toolbar);
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
            activity.setSupportActionBar(toolbar);
            CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) root.findViewById(R.id.fragment_detail_toolbar_layout);
            toolBarLayout.setTitle("");
        }
        txtTypeProperty = root.findViewById(R.id.fragment_detail_type);
        txtDescriptionProperty = root.findViewById(R.id.fragment_detail_description);
        txtLocationProperty = root.findViewById(R.id.fragment_detail_location);
        txtAreaProperty = root.findViewById(R.id.fragment_detail_area);
        txtPrisProperty = root.findViewById(R.id.fragment_detail_pris);
        txtNbRoomProperty = root.findViewById(R.id.fragment_detail_nb_room);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
        Log.d("MainActivity", "The id of property is : " + mainViewModel.getCurrentIndexPropertyDetail());
    }

}
