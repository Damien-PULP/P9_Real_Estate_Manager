package com.openclassrooms.realestatemanager.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AdapterRecyclerViewPropertyList extends RecyclerView.Adapter<AdapterRecyclerViewPropertyList.ViewHolderProperty> {

    private List<Property> propertyList = new ArrayList<>();
    private Context context;

    @Override
    public ViewHolderProperty onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.item_property, parent, false);
        return new ViewHolderProperty(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderProperty holder, int position) {
        holder.bind(context);
    }
    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public void updateData (List<Property> properties){
        this.propertyList = properties;
        notifyDataSetChanged();
    }

    static class ViewHolderProperty extends RecyclerView.ViewHolder {

        private LinearLayout item;

        public ViewHolderProperty(@NonNull View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.item_property);
        }

        public void bind(final Context context){
            // TODO Modify this way
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity = (MainActivity) context;
                    activity.switchFragment(1);
                }
            });
        }
    }

}
