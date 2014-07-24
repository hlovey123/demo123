package com.vanzo.manual;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainDirectory extends Activity implements OnItemClickListener{
    private ListView mListView;
    public static List<DataEntity> mListData;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_directory);
        //InputStream input = getResources().openRawResource(R.xml.data);
        InputStream input=this.getClass().getClassLoader().getResourceAsStream("data.xml"); 
        mListView = (ListView) findViewById(R.id.main_list);
        mAdapter = new MyAdapter(getApplicationContext());
        try {
            mListData = XmlParseUtil.getDatas(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public MyAdapter(MainDirectory maindirectory) {
        }

        public int getCount() {
            if (mListData == null) {
                return 0;
            }
            return mListData.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.main_listview, null);
            }
            TextView dir = (TextView) convertView.findViewById(R.id.directory);
            TextView page = (TextView) convertView.findViewById(R.id.pages);
            dir.setText(mListData.get(position).getName());
            page.setText(String.valueOf(mListData.get(position).getPage()));
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.putExtra("modeName", mListData.get(arg2).getName());
        intent.putExtra("page", mListData.get(arg2).getPage());
        intent.setClass(this, GestureActivity.class);
        startActivity(intent);
    }
}
