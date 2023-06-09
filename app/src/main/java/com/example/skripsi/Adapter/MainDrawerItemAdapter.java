package com.example.skripsi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.skripsi.Model.MainDrawerMenuModel;
import com.example.skripsi.R;

public class MainDrawerItemAdapter extends ArrayAdapter<MainDrawerMenuModel> {

    Context context;
    int resource;
    MainDrawerMenuModel data[];

    public MainDrawerItemAdapter(@NonNull Context context, int resource, MainDrawerMenuModel[] data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        MainDrawerMenuModel folder = data[position];

        imageViewIcon.setImageResource(folder.getIcon());
        textViewName.setText(folder.getName());

        return listItem;
    }
}
