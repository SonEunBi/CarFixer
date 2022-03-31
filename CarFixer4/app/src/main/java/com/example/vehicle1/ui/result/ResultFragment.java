//package com.example.vehicle1.ui.result;
//
//import android.content.Intent;
//import android.content.res.AssetManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.loader.content.CursorLoader;
//
//import com.example.vehicle1.R;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//public class ResultFragment extends Fragment {
//
//    private int GALLEY_CODE = 10;
//
//    Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//
//    startActivityForResult(intent,GALLEY_CODE);
//    TextView test1;
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_carresult, container, false);
//        test1 = root.findViewById(R.id.test1);
//        clickBtn(root);
//        return root;
//    }
//
//    private String getRealPathFromUri(Uri uri)
//    {
//        String[] proj=  {MediaStore.Images.Media.DATA};
//        CursorLoader  cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String url = cursor.getString(columnIndex);
//        cursor.close();
//        return  url;
//    }
//
//    public void clickBtn(View view) {
//        AssetManager assetManager = getContext().getAssets();
//        try {
//            InputStream is = assetManager.open("assets/test.json");
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader reader = new BufferedReader(isr);
//            StringBuffer buffer = new StringBuffer();
//            String line = reader.readLine();
//            while (line != null) {
//                buffer.append(line + "\n");
//                line = reader.readLine();
//            }
//            String jsonData = buffer.toString();
//            test1.setText(jsonData);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}