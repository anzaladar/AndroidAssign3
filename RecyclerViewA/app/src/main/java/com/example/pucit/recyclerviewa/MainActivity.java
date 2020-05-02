package com.example.pucit.recyclerviewa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.TooManyListenersException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<books> viewItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.recycle);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RecyclerView.Adapter ad = new RVAdapter(this, this.viewItems);
        rv.setAdapter(ad);

        addMenuItems();

    }
    private void addMenuItems(){
        try {
            ArrayList<String> array = new ArrayList<String>();

            String json = "";
            InputStream is=getResources().openRawResource(R.raw.data);
            byte[] data=new byte[is.available()];
            while (is.read(data)!=-1){

            }
            json=new String(data);


            JSONObject root = new JSONObject(json);
            JSONArray books = root.getJSONArray("books");
            books bookr=null;


            for (int i = 0; i < books.length(); ++i) {
                JSONObject book = books.getJSONObject(i);
                String title = book.getString("title");
                String level = book.getString("level");
                String info = book.getString("info");
                String cover = book.getString("cover");
               // String author = book.getString("author");
                String url = book.getString("url");
             //   String authorUrl = book.getString("authorUrl");
                bookr = new books(title, level, info, cover,url);
                viewItems.add(bookr);
            }

        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    private String readJSONdataFile() throws IOException {
        InputStream is = null;
        StringBuilder builder = new StringBuilder();
        try {
            is = getResources().openRawResource(R.raw.data);

            String jsString = "";
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((jsString=buffer.readLine())!=null);
            {

                builder.append(jsString);
            }



        } finally {
            if (is != null) {
                is.close();
            }

        }
        return new String(builder);


    }
}
