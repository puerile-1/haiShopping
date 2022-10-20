package com.example.dzy_application;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class msg_listview extends AppCompatActivity {
    private List<msg> msgList = new ArrayList<msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg);
        initFruits(); // 初始化水果数据
        msg_adapter adapter = new msg_adapter(msg_listview.this, R.layout.msg_item, msgList);
        ListView listView = (ListView) findViewById(R.id.msg_listview);
        listView.setAdapter(adapter);
    }

    private void initFruits() {
        msg logistics = new msg(R.drawable.logistics,"交易物流","您的包裹开始运输","17:13");
        msgList.add(logistics);
        msg activity = new msg(R.drawable.activity,"活动优惠","好货限时买二免一，全场低至一元","12:15");
        msgList.add(activity);
        msg milk = new msg(R.drawable.milk,"新农旗舰店","好的，谢谢","9:15");
        msgList.add(milk);
        msg move = new msg(R.drawable.zgmove,"中国移动官方旗舰店","您已成功充值100元","7:15");
        msgList.add(move);
        msg server = new msg(R.drawable.server,"服务通知","您有28个淘金币可领取","6:15");
        msgList.add(server);
        msg msg = new msg(R.drawable.xxtz,"消息号通知","科颜氏必囤好物抢购中","昨日");
        msgList.add(msg);
    }
}