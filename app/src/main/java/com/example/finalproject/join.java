package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class join extends AppCompatActivity {

    String url = "http://146.56.182.55/mobile/join";
    EditText pw, pwCh, id, phoneNum, nickName;
    TextView pwStTv, pwChTv;
    Button joinBtn;


    boolean pwStability = false, pwCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        pw = (EditText) findViewById(R.id.pw);
        id = (EditText) findViewById(R.id.id);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        nickName = (EditText) findViewById(R.id.nickName);
        pwCh =(EditText) findViewById(R.id.pwCheck);
        pwStTv = (TextView) findViewById(R.id.pwStability);
        pwChTv = (TextView) findViewById(R.id.checkTextView);
        joinBtn = (Button) findViewById(R.id.submit);

        joinBtn.setOnClickListener(new View.OnClickListener() {         //Join Button OnClick
            @Override
            public void onClick(View view) {
                join();
            }
        });

        pw.addTextChangedListener(new TextWatcher() {              // 비밀번호 안정성 검사
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {       // EditText의 글자가 변하고 난 후
                String nowPw = pw.getText().toString();
                if(nowPw.length()==0){
                    pwStTv.setText("");
                    return;
                }
                boolean length = false, special = false
                        ,english = false, number = false;

                for(int i =0; i<nowPw.length(); i++){           // 비밀번호 안정성 길이 10이상, 특수문자, 숫자, 영어 포함
                    char a = nowPw.charAt(i);
                    if((a>=32 && a <=47) ||                     // java도 아스키코드 씀 근데 왜 2바이트임?
                            (a>=58 && a <=64) ||
                            (a>=91 && a <=96)||
                    (a>=123 && a <=126)){
                        special =true;
                    }
                    if((a>=48 && a <=57))number =true;
                    if((a>=97 && a <=122)) english = true;
                }
                if(nowPw.length()>=10) length = true;

                if(length && special && english && number){     //모든 조건 충족 시
                    pwStability =true;
                    pwStTv.setText("안전한 비밀번호");
                    pwStTv.setTextColor(0xFF000000);
                }else{                                          //하나라도 충족 안하면
                    pwStability=false;
                    pwStTv.setText("영문, 특수문자, 숫자를 포함해 10글자 이상 만들어주세요");
                    pwStTv.setTextColor(0xFFDF2248);
                }
            }
        });

        pwCh.addTextChangedListener(new TextWatcher() {              // 비밀번호 두 번 입력 검사
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String realPw = pw.getText().toString();
                String nowCheckPw = pwCh.getText().toString();// EditText의 글자가 변하고 난 후
                if(nowCheckPw.length() == 0){
                    pwChTv.setText("");
                    return;
                }

                if(!pwStability) return;
                else if(realPw.equals(nowCheckPw)){         // java는 == 쓰면 안됨 str.equals(str1)
                    pwChTv.setText("비밀번호 일치");
                    pwChTv.setTextColor(0xFF000000);
                    pwCheck = true;
                }else{
                    pwCheck = false;
                    pwChTv.setText("비밀번호 불일치");
                    pwChTv.setTextColor(0xFFDF2248);
                }
            }
        });
    }
    private void join(){

        if(!(pwStability&& pwCheck)){
            Toast.makeText(join.this,"비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
        }  // 비밀번호 검사를 통과하지 못했으면

        final String input_NickName = nickName.getText().toString().trim();
        final String input_PhoneNum = phoneNum.getText().toString().trim();
        final String input_id = id.getText().toString().trim();
        final String password = pw.getText().toString().trim();

        if(input_NickName.length()==0 || input_PhoneNum.length() ==0 || input_id.length() == 0 || password.length() == 0){
            Toast.makeText(join.this,"입력 되지 않은 항목이 있습니다", Toast.LENGTH_SHORT).show();
            return;
        }
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
                        Toast.makeText(join.this,"Error",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",input_id);
                params.put("pw",password);
                params.put("nickName",input_NickName);
                params.put("phoneNum",input_PhoneNum);

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
                Toast.makeText(join.this, "회원가입 성공",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(join.this,login.class);
                startActivity(intent);
                finish();
            }else if(jsonObject.getString("res").equals("no id")) {
                Toast.makeText(join.this, "아이디가 존재하지 않거나 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }else if(jsonObject.getString("res").equals("wrong pw")){
                Toast.makeText(join.this, "아이디가 존재하지 않거나 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}