package com.example.a44261.bysj102.TT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.MyUser;

import java.util.List;

public class OrgmemberAdapter extends BaseAdapter {
    private Context context;
    private List<MyUser> list;

    public OrgmemberAdapter(Context context, List<MyUser> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            String org_member=list.get(position).getUsername();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.member_item, null);//实例化一个布局文件

            TextView tv_org_member=convertView.findViewById(R.id.org_member);

            tv_org_member.setText(org_member);

        }
        return convertView;
    }
}
