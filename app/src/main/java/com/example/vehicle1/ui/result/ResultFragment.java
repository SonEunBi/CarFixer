package com.example.vehicle1.ui.result;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vehicle1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResultFragment extends Fragment {

    TextView test1;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_carresult, container, false);
        test1 = root.findViewById(R.id.test1);
        clickBtn(root);
        return root;
    }

    public void clickBtn(View view) {
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStream is = assetManager.open("assets/test.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();

            }
            String jsonData = buffer.toString();
            test1.setText(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}