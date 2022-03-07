package com.example.vehicle1.ui.centerlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicle1.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    ImageView car_profile1;
    TextView car_name1;
    TextView car_address1;
    TextView car_phone1;
    TextView car_distance1;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        this.car_profile1 = itemView.findViewById(R.id.car_profile1);
        this.car_phone1= itemView.findViewById(R.id.car_phone1);
        this.car_address1 = itemView.findViewById(R.id.car_address1);
        this.car_name1 = itemView.findViewById(R.id.car_name1);
        this.car_distance1 = itemView.findViewById(R.id.car_distance1);
    }
    @NonNull

    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centerlist_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }
}
