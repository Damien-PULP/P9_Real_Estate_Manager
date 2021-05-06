/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogSearchView extends AlertDialog {

    private final List<PointOfInterest> pointOfInterestObj;
    //UI
    private Button isNotSold;
    private Button isSold;
    private TextInputLayout type;
    private TextInputLayout minSurface;
    private TextInputLayout maxSurface;
    private TextInputLayout minPris;
    private TextInputLayout maxPris;
    private TextView nonePointInterest;
    private ChipGroup pointOfInterest;
    private Button lastSaleDateYear;
    private Button lastSaleDateMonth;
    private Button lastSaleDateDay;
    private TextInputLayout city;
    //DATA
    private boolean isSoldBool = false;
    private String dateFilter = "none";

    private final WeakReference<CallbackSearchDialog> callbackRef;
    private final Context context;

    //CALLBACK of Search property with filter
    public interface CallbackSearchDialog {
        void OnSearchProperty(SearchPropertyModel searchPropertyModel);
    }

    public DialogSearchView(Context context, CallbackSearchDialog callback, List<PointOfInterest> pointOfInterests) {
        super(context);
        this.context = context;
        this.callbackRef = new WeakReference<>(callback);
        this.pointOfInterestObj = pointOfInterests;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View content = LayoutInflater.from(context).inflate(R.layout.dialog_search_view, null);
        setView(content);
        setTitle("Search a property");
        setButton(DialogInterface.BUTTON_POSITIVE, "Search", (dialogInterface, i) -> searchProperty());
        onConfigureDialog(content);
        super.onCreate(savedInstanceState);
    }

    //configure UI
    private void onConfigureDialog(View view) {
        this.isNotSold = view.findViewById(R.id.dialog_search_view_not_sold);
        this.isSold = view.findViewById(R.id.dialog_search_view_sold);
        this.type = view.findViewById(R.id.dialog_search_view_type);
        this.minSurface = view.findViewById(R.id.dialog_search_view_surface_min);
        this.maxSurface = view.findViewById(R.id.dialog_search_view_surface_max);
        this.minPris = view.findViewById(R.id.dialog_search_view_pris_min);
        this.maxPris = view.findViewById(R.id.dialog_search_view_pris_max);
        this.nonePointInterest = view.findViewById(R.id.dialog_search_view_none_point_of_interest);
        this.pointOfInterest = view.findViewById(R.id.dialog_search_view_chip_group_point_of_interest);
        this.lastSaleDateYear = view.findViewById(R.id.dialog_search_view_date_on_year);
        this.lastSaleDateMonth = view.findViewById(R.id.dialog_search_view_date_on_month);
        this.lastSaleDateDay = view.findViewById(R.id.dialog_search_view_date_on_week);

        this.city = view.findViewById(R.id.dialog_search_view_address_city);

        isNotSold.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        isNotSold.setOnClickListener(v->{
            isNotSold.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            isSold.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            isSoldBool = false;
        });
        isSold.setOnClickListener(v->{
            isSold.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            isNotSold.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            isSoldBool = true;
        });

        if(pointOfInterestObj.size() > 0) nonePointInterest.setVisibility(View.GONE);
        for(PointOfInterest point : pointOfInterestObj){
            Chip chip = new Chip(context);
            chip.setText(point.getName());
            chip.setOnClickListener(view1 -> chip.setBackgroundColor(context.getResources().getColor(R.color.colorAccent)));
            chip.setCheckable(true);
            pointOfInterest.addView(chip);
        }
        // Date Filter
        lastSaleDateYear.setOnClickListener(v->{
            dateFilter = "byYear";
            lastSaleDateYear.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            lastSaleDateMonth.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLessOpacity));
            lastSaleDateDay.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLessOpacity));
        });
        lastSaleDateMonth.setOnClickListener(v->{
            dateFilter = "byMonth";
            lastSaleDateYear.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLessOpacity));
            lastSaleDateMonth.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            lastSaleDateDay.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLessOpacity));
        });
        lastSaleDateDay.setOnClickListener(v->{
            dateFilter = "byDay";
            lastSaleDateYear.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLessOpacity));
            lastSaleDateMonth.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLessOpacity));
            lastSaleDateDay.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        });
    }
    //Get data of the input and start the searching
    private void searchProperty() {
        String typeProperty = Objects.requireNonNull(type.getEditText()).getText().toString();
        int minSurfaceProperty = 0;
        int maxSurfaceProperty = 0;
        if(!Objects.requireNonNull(minSurface.getEditText()).getText().toString().equals("")) minSurfaceProperty = Integer.parseInt(minSurface.getEditText().getText().toString());
        if(!Objects.requireNonNull(maxSurface.getEditText()).getText().toString().equals("")) maxSurfaceProperty = Integer.parseInt(maxSurface.getEditText().getText().toString());
        int minPrisProperty = 0;
        int maxPrisProperty = 0;
        if(!Objects.requireNonNull(minPris.getEditText()).getText().toString().equals("")) minPrisProperty = Integer.parseInt(minPris.getEditText().getText().toString());
        if(!Objects.requireNonNull(maxPris.getEditText()).getText().toString().equals("")) maxPrisProperty = Integer.parseInt(maxPris.getEditText().getText().toString());

        String cityProperty = Objects.requireNonNull(city.getEditText()).getText().toString();

        List<String> pointsOfInterestList = new ArrayList<>();
        for(int i= 0 ; i < pointOfInterest.getChildCount(); i++){
            Chip chip = (Chip) pointOfInterest.getChildAt(i);
            if(chip.isChecked()){
                pointsOfInterestList.add(chip.getText().toString());
            }
        }

        callbackRef.get().OnSearchProperty(new SearchPropertyModel(isSoldBool,typeProperty, minSurfaceProperty, maxSurfaceProperty, minPrisProperty, maxPrisProperty, pointsOfInterestList, dateFilter, cityProperty));
    }


}
