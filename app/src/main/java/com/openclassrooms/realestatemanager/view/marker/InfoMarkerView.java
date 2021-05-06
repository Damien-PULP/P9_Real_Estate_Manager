/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.marker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PropertyObj;

import java.util.List;

/**
 * Custom the marker with data
 */
public class InfoMarkerView implements GoogleMap.InfoWindowAdapter {

    private final List<PropertyObj> properties;

    private final Context context;

    public InfoMarkerView(Context context, List<PropertyObj> properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.marker_view_property, null);
        ImageView icon = view.findViewById(R.id.marker_view_icon);
        TextView type = view.findViewById(R.id.marker_view_type);
        TextView pris = view.findViewById(R.id.marker_view_pris);
        TextView location = view.findViewById(R.id.marker_view_location);

        PropertyObj property = getPropertyById(marker.getTitle());
        if(property != null){
            if(property.getPhotos().size() > 0){
                icon.setImageBitmap(property.getPhotos().get(0).getBitmapPhoto());
            }
            type.setText(property.getProperty().getType());
            pris.setText(property.getProperty().getPris() + "$");
            location.setText(property.getAddress().toString());
        }
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return LayoutInflater.from(context).inflate(R.layout.marker_view_property, null);
    }

    private PropertyObj getPropertyById (String id){
        for(PropertyObj property : properties){
            if(String.valueOf(property.getProperty().getId()).equals(id)){
                return property;
            }
        }
        return null;
    }
}
