package com.example.yzxing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2018/11/16.
 */

public class MyAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mcontext;
    private List<String> codeList;

    public MyAdapter(Context context, List<String> list) {
        layoutInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.codeList = list;
    }

    @Override
    public int getCount() {
        return codeList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        //提升加载效率
        if (convertView == null) {
            //将布局文件实例化转换为View
            view = layoutInflater.inflate(R.layout.person_information, null);
        }
        else{
            view = convertView;
        }

        //绑定布局
        //String address, String comp, String duty, String QQ,String Emial, String fax, String website, String name, String phonenumber, int imageId
        //获取联系人姓名到textView
        TextView textView = (TextView)view.findViewById(R.id.text_name);



        //设置内容
        //String address, String comp, String duty, String QQ,String Emial, String fax, String website, String name, String phonenumber, int imageId
        //设置联系人姓名到TextView控件
        textView.setText(codeList.get(position));



        return view;
    }
}
