package com.example.dzy_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class com_adapter extends ArrayAdapter {
    private final int resourceId;

    public com_adapter(@NonNull Context context, int resource) {
        super(context, resource);
        resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        return view;
    }
}
