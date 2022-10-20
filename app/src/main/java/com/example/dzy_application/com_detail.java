package com.example.dzy_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dzy_application.database.MySQLConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class com_detail extends AppCompatActivity {
    private static Connection con = null;
    private static PreparedStatement pst = null;
    private Handler handler = new Handler() {
        @Override
        //书写handleMessage方法，接受信息
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){

            }
        }
    };
    private List<String> list = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_detail);//设置刚写的xml文件页面
        //下单
        Button buy = (Button) findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com_detail.this,purchase_Activity.class);
                startActivity(intent);
            }
        });
        //加入购物车
        list.add("简佩旗舰店 >");
        list.add("雾霾蓝马里奥卫衣男秋");
        list.add("88.88");
        list.add("1");
        list.add("0");
        list.add("0");
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com_detail.Threads_readSQL threads_readSQL = new com_detail.Threads_readSQL();
                threads_readSQL.start();
                Toast.makeText(com_detail.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
            }
        });
        //返回
        Button back = (Button) findViewById(R.id.fanh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com_detail.this, ClothActivity.class);
                startActivity(intent);
            }
        });
        RadioButton store_img = (RadioButton) findViewById(R.id.store_img);
        store_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store_img.setChecked(true);
            }
        });
        RadioButton ww_img = (RadioButton) findViewById(R.id.ww_img);
        ww_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ww_img.setChecked(true);
            }
        });
        RadioButton collection_img = (RadioButton) findViewById(R.id.collection_img);
        collection_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collection_img.setChecked(true);
            }
        });
    }
    class Threads_readSQL extends Thread {
        @Override
        public void run() {

            try {
                con = MySQLConnections.getConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String sql ="INSERT INTO gwc(store_name,com_name,com_price,com_num,checked1,checked2)\n" +
                        "VALUES(?,?,?,?,?,?)";
                if (con!=null) {

                    pst = con.prepareStatement(sql);
                    // 关闭事务自动提交 ,这一行必须加上
                    con.setAutoCommit(false);

                    //清空上次发送的信息
                    pst.setString(1, String.valueOf(list.get(0)));
                    pst.setString(2, String.valueOf(list.get(1)));
                    pst.setString(3, String.valueOf(list.get(2)));
                    pst.setInt(4, Integer.parseInt(list.get(3)));
                    pst.setBoolean(5,Boolean.valueOf(list.get(4)));
                    pst.setBoolean(6,Boolean.valueOf(list.get(5)));
                    pst.addBatch();
                    pst.executeBatch();
                    con.commit();
                    pst.close();

                }

            }catch (Exception e){
                System.out.println(e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this,"请输入正确的语句",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
