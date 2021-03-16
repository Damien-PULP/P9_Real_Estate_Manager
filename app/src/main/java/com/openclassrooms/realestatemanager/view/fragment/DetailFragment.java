package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;

public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        configureUI(root);
        return root;
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
    }
}
