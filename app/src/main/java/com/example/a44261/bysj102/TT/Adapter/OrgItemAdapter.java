package com.example.a44261.bysj102.TT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.Org;

import java.util.List;

public class OrgItemAdapter extends BaseAdapter {
    private Context context;
    private List<Org> list;

    public OrgItemAdapter(Context context, List<Org> list) {
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
            String org_item_school=list.get(position).getSchool();
            String org_item_orgname=list.get(position).getOrgname();
            MyUser org_item_organizer=list.get(position).getOrganizer();
            String org_item_orger_name=list.get(position).getOrger_name();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.org_item, null);//实例化一个布局文件

            TextView tv_org_item_school=convertView.findViewById(R.id.org_item_school);
            TextView tv_org_item_orgname=convertView.findViewById(R.id.org_item_orgname);
            TextView tv_org_item_organizer=convertView.findViewById(R.id.org_item_organizer);

            tv_org_item_school.setText(org_item_school);
            tv_org_item_orgname.setText(org_item_orgname);
            tv_org_item_organizer.setText(org_item_orger_name);
        }
        return convertView;
    }
}
