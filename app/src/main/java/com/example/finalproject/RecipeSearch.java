package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeSearch extends AppCompatActivity {

    String address = "https://api.edamam.com/api/recipes/v2?type=public&app_id=******&app_key=*********&random=true&q=";
    // API address


    // Custom View를 위한 Array adapter 인자 클래스 생성
    public class RecipeInfo{

        public int calories;
        public String image;
        public String label;
        public String url;
        public int length;

        public RecipeInfo(int calories, String image, String label, String url,
                          int length){
            this.calories =calories;
            this.image =image;
            this.label=label;
            this.url=url;
            this.length =length;
        }
    }


    ArrayList<RecipeInfo> items = new ArrayList<RecipeInfo>();
    //CustomAdapter에 담을 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_search);
        setTitle("Recipe Search!");
        EditText et = (EditText) findViewById(R.id.search);
        Button btn = (Button) findViewById(R.id.searchBtn);
        ListView listView = (ListView)findViewById(R.id.list);
        CustomList adapter = new CustomList(this,items);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), "1", Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){  //버튼 리스너 등록
            @Override
            public void onClick(View v){
                String q = et.getText().toString();

                new Thread(){                          // Thread로 HTTP 통신
                    @Override
                    public void run(){
                        items.clear();
                        Log.i("Thread()", "run()");

                        try{
                            URL url = new URL(address+q);
                            Log.i("Thread()", "url()");
                            InputStream is = url.openStream();
                            InputStreamReader isr = new InputStreamReader(is);

                            StringBuffer buf = new StringBuffer();
                            StringBuffer buffer = new StringBuffer();
                            BufferedReader reader = new BufferedReader(isr);
                            String line = reader.readLine();
                            while (line != null) {
                                buffer.append(line + "\n");
                                line = reader.readLine();
                            }
                            //JSON parsing

                            String jsonData = buffer.toString();

                            // jsonData를 먼저 JSONObject 형태로 바꾼다.
                            JSONObject obj = new JSONObject(jsonData);
                            // obj의 "boxOfficeResult"의 JSONObject를 추출
                            //JSONObject boxOfficeResult = (JSONObject)obj.get("hits");
                            // boxOfficeResult의 JSONObject에서 "dailyBoxOfficeList"의 JSONArray 추출
                            JSONArray dailyBoxOfficeList = (JSONArray)obj.get("hits");

                            // 입력 받은 JSON Object를 각각의 List에 Parsing
                            for(int i =0; i<dailyBoxOfficeList.length(); i++){
                                JSONObject recipe = dailyBoxOfficeList.getJSONObject(i);
                                JSONObject recipe_real = (JSONObject)recipe.get("recipe");

                                int calory = (int)recipe_real.getDouble("calories");
                                String image = recipe_real.getString("image");
                                String label =recipe_real.getString("label");
                                String recLink =recipe_real.getString("url");
                                JSONArray ingredients = (JSONArray) recipe_real.get("ingredients");
                                int length =ingredients.length();
                                RecipeInfo temp = new RecipeInfo(calory,image,label,recLink,length);
                                items.add(temp);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }   // ArrayList의 갱신사항을 적용
                            });
                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }

                }.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){      // Custom List View OnClickListener 근데 안됨..
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String data = (String)parent.getItemAtPosition(position);
                Toast.makeText(RecipeSearch.this, data, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class CustomList extends ArrayAdapter<RecipeInfo> {
        public CustomList(Context context, ArrayList<RecipeInfo> RecInfo){
            super(context, 0, RecInfo);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){         // Custom List View 설정
            RecipeInfo Info = getItem(position);
            if(view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.listitem,parent,false);
            }
            ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView label = (TextView) view.findViewById(R.id.label);
            TextView calory = (TextView) view.findViewById(R.id.calory);
            TextView ingNum = (TextView) view.findViewById(R.id.ingNum);
            TextView search = (TextView) view.findViewById(R.id.search);

            label.setText(Info.label);
            calory.setText("칼로리 : " +Info.calories+"kcal");
            ingNum.setText("재료 수 : " + Info.length+"개"+"       Recipe Link : ");
            search.setText(Info.url);
            Glide.with(view).load(Info.image).into(image);          // url image loading library

            return view;
        }
    }

}
