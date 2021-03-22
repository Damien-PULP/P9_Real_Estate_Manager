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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.view.adapter.AdapterRecyclerViewPropertyList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {

    //UI
    private RecyclerView propertiesRecyclerView;

    private AdapterRecyclerViewPropertyList adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        configureUI(root);
        return root;
    }

    private void configureUI(View root) {
        propertiesRecyclerView = root.findViewById(R.id.fragment_main_recycler_view);
        propertiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdapterRecyclerViewPropertyList();
        propertiesRecyclerView.setAdapter(adapter);

        /** PREPOPULATE
         * TODO Delete this */
        List<Property> properties = new ArrayList<>();
        Property property = new Property("House", 1000000f, 10, "Amazing house", "Sold", new Date(), new Date(), 0);
        properties.add(property);
        properties.add(property);
        properties.add(property);
        properties.add(property);

        adapter.updateData(properties);
    }

}
