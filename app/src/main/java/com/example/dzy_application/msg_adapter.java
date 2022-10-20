package com.example.dzy_application;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class msg_adapter extends ArrayAdapter {
    private final int resourceId;

    public msg_adapter(Context context, int textViewResourceId, List<msg> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        msg msg = (msg) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView msgImage = (ImageView) view.findViewById(R.id.msg_img);//获取该布局内的图片视图
        TextView msgName = (TextView) view.findViewById(R.id.msg_name);//获取该布局内的文本视图
        TextView msgMsg = (TextView) view.findViewById(R.id.msg_msg);//获取该布局内的文本视图
        TextView msgTime = (TextView) view.findViewById(R.id.msg_time);//获取该布局内的文本视图
        System.out.println(msg.getMsg());
        msgImage.setImageResource(msg.getImage());//为图片视图设置图片资源
        msgName.setText(msg.getName());//为文本视图设置文本内容
        msgMsg.setText(msg.getMsg());//为图片视图设置图片资源
        msgTime.setText(msg.getTime());//为文本视图设置文本内容
        return view;
    }
}
