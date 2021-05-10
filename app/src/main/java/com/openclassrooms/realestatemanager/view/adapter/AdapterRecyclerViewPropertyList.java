package com.openclassrooms.realestatemanager.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewPropertyList extends RecyclerView.Adapter<AdapterRecyclerViewPropertyList.ViewHolderProperty> {

    private List<PropertyObj> propertyList = new ArrayList<>();
    private Context context;

    private int currentSelection = 0;

    @NonNull
    @Override
    public ViewHolderProperty onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.item_property, parent, false);
        return new ViewHolderProperty(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderProperty holder, int position) {
        holder.bind(context, this, propertyList.get(position));
        holder.adaptTheTracking(currentSelection == position);

    }
    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public void updateData (List<PropertyObj> properties){
        this.propertyList = properties;
        notifyDataSetChanged();
    }

    //VIEW HOLDER of this adapter
    static class ViewHolderProperty extends RecyclerView.ViewHolder {

        private final LinearLayout item;
        private final ShapeableImageView icon;
        private final TextView type;
        private final TextView location;
        private final TextView pris;

        public ViewHolderProperty(@NonNull View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.item_property_image);
            this.item = itemView.findViewById(R.id.item_property);
            this.type = itemView.findViewById(R.id.item_property_type);
            this.location = itemView.findViewById(R.id.item_property_location);
            this.pris = itemView.findViewById(R.id.item_property_pris);
        }

        public void bind(final Context context, final AdapterRecyclerViewPropertyList adapter, PropertyObj propertyObj){
            item.setOnClickListener(view -> {
                MainActivity activity = (MainActivity) context;
                activity.setCurrentProperty(propertyObj.getProperty().getId());
                activity.switchFragment(1);
                adapter.currentSelection = getAdapterPosition();
                adapter.notifyDataSetChanged();
            });

            if(propertyObj.getPhotos().size() > 0){
                Bitmap bitmap =propertyObj.getPhotos().get(0).getBitmapPhoto();
                icon.setImageBitmap( Utils.createRoundedBorderOfBitmap(bitmap));
            }

            type.setText(propertyObj.getProperty().getType());
            String locationText = propertyObj.getAddress().getCountry() + ", " +  propertyObj.getAddress().getCity() + ", " +  propertyObj.getAddress().getPostalCode() + ", " +  propertyObj.getAddress().getStreet() + " " +  propertyObj.getAddress().getNumberStreet();
            location.setText(locationText);
            if (propertyObj.getProperty().getState().equals("NOT_SELL")){
                pris.setText("$" + (propertyObj.getProperty().getPris()));
                item.setAlpha(1f);
            }else{
                pris.setText(R.string.msg_property_is_sell);
                item.setAlpha(0.5f);
            }

        }
        public void adaptTheTracking (boolean isSelected){
            item.setSelected(isSelected);
        }
    }

}
