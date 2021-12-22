package com.example.finalproject;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NutritionFacts extends AppCompatActivity {

    EditText etext;
    Button btn;
    String url = "http://******" + "/mobile/NutritionFacts";
    ListView list;

    ArrayList<String> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_facts);
        etext = (EditText) findViewById(R.id.nutritionSearch);
        btn = (Button) findViewById(R.id.searchBtn);
        list = (ListView) findViewById(R.id.nutrition);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NutritionFacts.this,
                android.R.layout.simple_list_item_1, items);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNut();
                list.setAdapter(adapter);
            }
        });
    }

    public void getNut(){
        final String nutritionFacts = etext.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(login.this,response,Toast.LENGTH_LONG).show();
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NutritionFacts.this,"Error",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("search",nutritionFacts);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arr = jsonObject.getJSONArray("res");
            String title = String.format("%-40s", "영양성분 실량") + "Daily Value";
            items.add(title);
            for(int i =0; i<arr.length(); i++){
                JSONObject jobj = arr.getJSONObject(i);
                String label = jobj.getString("label");
                String mass = jobj.getString("mass");
                String unit = jobj.getString("unit");
                String percent = jobj.getString("percent");
                String punit = jobj.getString("punit");

                String result = String.format("%-40s",label +" : " + mass + unit) + percent + punit;
                items.add(result);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}