package com.example.vehicle1.ui.centerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vehicle1.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {    private ArrayList<CenterList> arrayList;
    private Context context;
//어댑터에서 액티비티 액션 가져올 떄 context 필요함. 하지만 어댑터에는 context가 없음.

    public CustomAdapter(ArrayList<CenterList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centerlist_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.car_profile1);
        holder.car_name1.setText(arrayList.get(position).getCenterName());
        holder.car_address1.setText(arrayList.get(position).getAddress());
        holder.car_phone1.setText(arrayList.get(position).getPhone());

        holder.car_distance1.setText(String.valueOf(arrayList.get(position).getDistance()));

    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView car_profile1;
        TextView car_name1;
        TextView car_address1;
        TextView car_phone1;
        TextView car_distance1;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.car_profile1 = itemView.findViewById(R.id.car_profile1);
            this.car_phone1 = itemView.findViewById(R.id.car_phone1);
            this.car_address1 = itemView.findViewById(R.id.car_address1);
            this.car_name1 = itemView.findViewById(R.id.car_name1);
            this.car_distance1 = itemView.findViewById(R.id.car_distance1);
        }
    }
}
