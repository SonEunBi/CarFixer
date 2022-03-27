package com.example.car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class center extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapter customAdapter;
    private ArrayList<CenterList> center = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_center, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.re_customview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        customAdapter = new CustomAdapter(center, getActivity());
        mRecyclerView.setAdapter(customAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    private void initDataset() {
        //for Test
        center = new ArrayList<>();
        center.add(new CenterList(R.drawable.carone,"만도카센터", "041-561-8204", "충청남도 천안시 동남구 병천면 충절로 1723", "km : 3"));
        center.add(new CenterList(R.drawable.cartwo, "병천현대써비스", "041-561-1256", "충청남도 천안시 동남구 병천면 충절로 1774", "km : 5"));
        center.add(new CenterList(R.drawable.carthree, "미래카센터", "041-558-2494", "충청남도 천안시 동남구 충절로 1885", "km : 5"));
        center.add(new CenterList(R.drawable.carfour, "공단카서비스", "070-7096-5373", "충청남도 천안시 동남구 충절로 1648", "km : 7"));
        center.add(new CenterList(R.drawable.carfive, "굿모닝키센터", "041-556-7874", "충청남도 천안시 동남구 병천면 병천1로 74", "km : 8"));
        center.add(new CenterList(R.drawable.carsix, "해피카센타", "041-561-0206", "충청남도 천안시 동남구 병천면 충절로 1580", "km:11"));
        center.add(new CenterList(R.drawable.carseven, "오륜대형카센터", " 041-536-7822", "충청남도 천안시 동남구 병천면 충절로 1300", "km:17"));

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

//        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
////        StorageReference storageRef = storage.getReferenceFromUrl("gs://graduation-project-74d71.appspot.com/").child("images/" + filename);
//        databaseReference = database.getReference("carcenter_01"); // DB 테이블 연결
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
//                    CenterList CenterList = snapshot.getValue(CenterList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
//                    arrayList.add(CenterList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
//                }
//                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // 디비를 가져오던중 에러 발생 시
//                Log.e("CenterFragment", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });
