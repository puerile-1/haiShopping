package com.example.dzy_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class searchActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Button backbtn1 = (Button) findViewById(R.id.fanh);
        backbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        SearchView searchView = (SearchView) findViewById(R.id.com_search);
        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setIconified(false);//设置searchView处于展开状态
        searchView.onActionViewExpanded();// 当展开无输入内容的时候，没有关闭的图标
        searchView.setIconifiedByDefault(false);//默认为true在框内，设置false则在框外
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                if(query.equals("潮服")){
                    Intent intent = new Intent(searchActivity.this, ClothActivity.class);
                    startActivity(intent);
                }
                if(query.equals("潮鞋")){
                    Intent intent = new Intent(searchActivity.this, ShoesActivity.class);
                    startActivity(intent);
                }
                if(query.equals("手表")){
                    Intent intent = new Intent(searchActivity.this, WatchActivity.class);
                    startActivity(intent);
                }
                if(query.equals("箱包")){
                    Intent intent = new Intent(searchActivity.this, BagActivity.class);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调

                return true;
            }
        });
        /*EditText editText = (EditText)findViewById(R.id.com_search);
        Button yes = (Button) findViewById(R.id.yes);
        if(editText.getText().equals("a")){
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }*/

    }
}
