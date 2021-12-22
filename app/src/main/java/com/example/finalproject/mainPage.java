package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class mainPage extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        setTitle("맛있겠다!");
        intent = new Intent(this, MusicService.class);

        Button nutrionFacts = (Button) findViewById(R.id.NutritionFacts);
        Button searchRecipe = (Button) findViewById(R.id.SearchRecipe);
        Button todayDiet = (Button) findViewById(R.id.TodayDiet);

        registerForContextMenu(nutrionFacts);
        registerForContextMenu(searchRecipe);
        registerForContextMenu(todayDiet);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.NutritionFacts:
                menu.add(0,1,0,"재료나 음식 이름을 입력하면");
                menu.add(0,2,0,"영양성분이 출력된다.");
                break;
            case R.id.SearchRecipe:
                menu.add(0,1,0,"찾고 싶은 recipe를 검색하면");
                menu.add(0,2,0,"recipe 링크와 열량 재료 수");
                menu.add(0,3,0,"등을 확인 할 수 있다.");
                break;
            case R.id.TodayDiet:
                menu.add(0,1,0,"본인이 필요한 라벨을 골라");
                menu.add(0,2,0,"알맞은 recipe를 검색가능");
                break;
        }
    }


    public void onToggle(View view){

        boolean on = ((ToggleButton) view).isChecked();
        if(on){
            startService(intent);
        }
        else{
            stopService(intent);
        }
    }

    public void OnClickNutritionFacts(View v){
        Intent NutritionFacts = new Intent(mainPage.this, NutritionFacts.class);
        startActivity(NutritionFacts);
    }

    public void OnClickSearchRecipe(View v){
        Intent RecipeSearch = new Intent(mainPage.this, RecipeSearch.class);
        startActivity(RecipeSearch);
    }
    public void OnClickTodayDiet(View v){
        Intent TodayDiet = new Intent(mainPage.this, TodayDiet.class);
        startActivity(TodayDiet);
    }
}

