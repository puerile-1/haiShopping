package com.example.dzy_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dzy_application.database.MySQLConnections;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class purchase_Activity extends AppCompatActivity {
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mDialogBehavior;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase);//设置刚写的xml文件页面
        ImageView title_img = (ImageView) findViewById(R.id.title_img);
        title_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(purchase_Activity.this,com_detail.class);
                startActivity(intent);
            }
        });
        list.add("简佩旗舰店 >");
        list.add("雾霾蓝马里奥卫衣男秋");
        list.add("88.88");
        list.add("100减15");
        list.add("1");
        findViewById(R.id.jies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSheetDialog1();
                bottomSheetDialog.show();
            }
        });

    }
    private void showSheetDialog1() {
        View view = View.inflate(purchase_Activity.this, R.layout.dialog_bottomsheet, null);


        bottomSheetDialog = new BottomSheetDialog(purchase_Activity.this, R.style.DialogTheme);
        bottomSheetDialog.setContentView(view);
        mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getPeekHeight());
        Button back = (Button) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),purchase_Activity.class);
                startActivity(intent);
            }
        });
        Button pay = (Button) view.findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchase_Activity.Threads_readSQL threads_readSQL = new purchase_Activity.Threads_readSQL();
                threads_readSQL.start();
                Toast.makeText(purchase_Activity.this, "支付成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(),com_detail.class);
                startActivity(intent);
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
                String sql ="INSERT INTO orders(store_name,com_name,com_price,com_manj,com_num)\n" +
                        "VALUES(?,?,?,?,?)";
                if (con!=null) {

                    pst = con.prepareStatement(sql);
                    // 关闭事务自动提交 ,这一行必须加上
                    con.setAutoCommit(false);

                    //清空上次发送的信息
                    pst.setString(1, String.valueOf(list.get(0)));
                    pst.setString(2, String.valueOf(list.get(1)));
                    pst.setString(3, String.valueOf(list.get(2)));
                    pst.setString(4, String.valueOf(list.get(3)));
                    pst.setInt(5, Integer.parseInt(list.get(4)));
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

    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected int getPeekHeight() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3;
    }


}
