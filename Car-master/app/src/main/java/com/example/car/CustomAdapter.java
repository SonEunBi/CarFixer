package com.example.car;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CenterList> list;

    public CustomAdapter(ArrayList<CenterList> center, Activity activity) {
        list = center;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.centerlist_item, parent, false);

        ViewHolder vh = new ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CenterList centerList = (CenterList) list.get(position);
        holder.car_profile1.setImageResource(centerList.getCar_profile1());
        holder.car_name1.setText(centerList.getCar_name1());
        holder.car_address1.setText(centerList.getCar_address1());
        holder.car_phone1.setText(centerList.getCar_phone1());
        holder.car_distance1.setText(centerList.getCar_distance1());
    }

    //아이템 개수 반환
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView car_profile1;
        public TextView car_name1;
        public TextView car_address1;
        public TextView car_phone1;
        public TextView car_distance1;

        public ViewHolder(View view) {
            super(view);
            car_profile1 = (ImageView) view.findViewById(R.id.car_profile1);
            car_phone1 = (TextView) view.findViewById(R.id.car_phone1);
            car_address1 = (TextView) view.findViewById(R.id.car_address1);
            car_name1 = (TextView) view.findViewById(R.id.car_name1);
            car_distance1 = (TextView) view.findViewById(R.id.car_distance1);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "수리 정보를 해당 센터로 전송합니다", Toast.LENGTH_SHORT).show();
    }
}

//    //xml 파일을 뷰 객체로 바꾸는데 사용할 context, listview 에 띄울 데이터 객체 리스트 centerlist 선언
//    public CustomAdapter(Context context, ArrayList list) {
//        super(context, 0, list);
//        this.context = context;
//        this.list = list;
//    }


//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        final ViewHolder holder;
//        final Context context = parent.getContext();
//
//        if (convertView == null) {
//
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.centerlist_item, parent, false);
//            convertView = inflater.from(parent.getContext()).inflate(R.layout.centerlist_item, parent, false);

//화면에 표시될 view로부터 위젯에 대한 참조 획득
//            holder = new ViewHolder();
////        holder.car_profile1 = (ImageView) convertView.findViewById(R.id.car_profile1);
////        convertView.setTag(holder);
//            holder.car_phone1 = (TextView) convertView.findViewById(R.id.car_phone1);
//            holder.car_address1 = (TextView) convertView.findViewById(R.id.car_address1);
//            holder.car_name1 = (TextView) convertView.findViewById(R.id.car_name1);
//            holder.car_distance1 = (TextView) convertView.findViewById(R.id.car_distance1);
//
//            convertView.setTag(holder);
//
//        }
//        ImageView car_profile1 = (ImageView) convertView.findViewById(R.id.car_profile1);
//
//        TextView car_phone1 = (TextView) convertView.findViewById(R.id.car_phone1);
//        TextView car_address1 = (TextView) convertView.findViewById(R.id.car_address1);
//        TextView car_name1 = (TextView) convertView.findViewById(R.id.car_name1);
//        TextView car_distance1 = (TextView) convertView.findViewById(R.id.car_distance1);
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        CenterList centerList = (CenterList) list.get(position);
//////아이템 내 각 위젯에 데이터 반영
//        holder.car_name1.setText(centerList.getCenterName());
//        holder.car_address1.setText(centerList.getAddress());
//        holder.car_phone1.setText(centerList.getPhone());
//        holder.car_distance1.setText(centerList.getDistance());
//        holder.car_profile1.setImageResource(CenterImageMap.get(centerList.getProfile()))

//        car_profile1.setImageDrawable(centerList.getProfile());
//        car_name1.setText(centerList.getCenterName());
//        car_address1.setText(centerList.getAddress());
//        car_phone1.setText(centerList.getPhone());
//        car_distance1.setText(centerList.getDistance());

//        Glide.with(context)
//                .load(centerList.getProfile())
//                .centerCrop()
//                .apply(new RequestOptions().override(250, 350))
//                .into(holder.car_profile1);
//        return convertView;

//    public void addItem(Drawable icon, String centerName, String phone, String address, String distance) {
//        CenterList list = new CenterList();
//
//        list.setDistance(distance);
//        list.setAddress(address);
//        list.setCenterName(centerName);
//        list.setPhone(phone);
//        list.setProfile(icon);
//
//        Center.add(list);
//    }


//    public void addItem(int car_profile1, String car_name1, String car_phone1, String car_address1, String car_distance1) {
//        ArrayList<CenterList> list = new ArrayList<>();
//        CenterList list = new CenterList(R.drawable.car_center1, "현대자동차블루핸즈", "041-561-8204", "충청남도 천안시 동남구 병천면 충절로 1723", "3");
//
//        list.setDistance(car_distance1);
//        list.setAddress(car_address1);
//        list.setCenterName(car_name1);
//        list.setPhone(car_phone1);
//        list.setProfile(car_profile1);
//    }
//

