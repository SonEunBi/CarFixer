package com.example.vehicle1.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.example.vehicle1.MainActivity;
import com.example.vehicle1.R;

public class SettingsFragment extends ListFragment {

    TextView text1;

    //listview 구성할 data
    String [] list = {"내용 추가","내용 추가","내용 추가","내용 추가","내용 추가"};

    public SettingsFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //view로 설정 및 view로 반환 : textView 이용할거라
        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);

        //connect to dashboardFragment xml id.
        text1 = view.findViewById(R.id.textView);

        //리스트뷰 설정- array define (context, layout, what data )
        MainActivity activity = (MainActivity)getActivity();    //적용할 context 추출
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,list );

        setListAdapter(adapter);            //listView id가 자동인 @id/list 인경우디ㅏ.


        return view;
    }//finish


    // listView 항목 클릭시 호출 - 버튼onclick 사용 불가(fragment니)
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        text1.setText("선택한 data : "+list[position]);
    }
}