package com.example.vehicle1.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.vehicle1.CalendarActivity;
import com.example.vehicle1.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
//implements View.OnClickListener
public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button addCalendar = (Button) root.findViewById(R.id.add_calendar);
        addCalendar.setOnClickListener(this);

        return root;
    }

    public void onClick(View v) {

        Button addCalendar = (Button) v;
        switch(addCalendar.getId()){
            //일정관리 버튼 클릭 시
            case R.id.add_calendar:
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
        }

    }
    public void onBackPressed() {
        goToMain();
    }

    //프래그먼트 종료
    private void goToMain() {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(HomeFragment.this).commit();
        fragmentManager.popBackStack();
    }
}