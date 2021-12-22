package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity {

    EditText etextId,passwd;
    Button login , join;
    String url = "http://******/mobile";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etextId = (EditText) findViewById(R.id.id);
        passwd = (EditText) findViewById(R.id.passwd);
        login = (Button) findViewById(R.id.login);
        join =(Button) findViewById(R.id.join);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,join.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void loginUser(){

        final String input_id = etextId.getText().toString().trim();
        final String password = passwd.getText().toString().trim();

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
                        Toast.makeText(login.this,"Error",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",input_id);
                params.put("pw",password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("res").equals("ok")) {
                Toast.makeText(login.this, "로그인 성공", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(login.this,mainPage.class);
                startActivity(intent);
                finish();
            }else if(jsonObject.getString("res").equals("no id")) {
                Toast.makeText(login.this, "아이디가 존재하지 않거나 비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }else if(jsonObject.getString("res").equals("wrong pw")){
                Toast.makeText(login.this, "아이디가 존재하지 않거나 비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
