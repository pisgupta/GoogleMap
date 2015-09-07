package com.sebiz.mohali.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Pankaj on 9/3/2015.
 */
public class MyListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<LocationBaen> bean;
    ViewHolder viewHolder;
    Context mContext;

    public MyListAdapter(Context ct, ArrayList<LocationBaen> bean) {
        inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bean = bean;
        mContext = ct;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
            viewHolder.formated_address = (TextView) convertView.findViewById(R.id.formated_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.formated_address.setText(bean.get(position).getFormatted_address());

        viewHolder.formated_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,bean.get(position).getPlace_id(),Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView formated_address;
    }
}
