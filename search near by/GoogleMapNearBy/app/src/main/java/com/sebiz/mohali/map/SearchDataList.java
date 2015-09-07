package com.sebiz.mohali.map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Pankaj on 9/2/2015.
 */
public class SearchDataList extends Activity {
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_data);
        ArrayList<LocationBaen> bean = (ArrayList<LocationBaen>) getIntent().getSerializableExtra("listData");
        MyListAdapter ad = new MyListAdapter(SearchDataList.this,bean);
        lv = (ListView)findViewById(R.id.searchdatalist);
        lv.setAdapter(ad);

    }
}
