package com.example.a44261.bysj102.TT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.Act;

import java.util.List;

public class OrgActItemAdapter extends BaseAdapter {
    private Context context;
    private List<Act> list;

    public OrgActItemAdapter(Context context, List<Act> list) {
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
            String org_activity_item_actname=list.get(position).getActname();
            String org_activity_item_acttime=list.get(position).getActtime();
            String org_activity_item_actplace=list.get(position).getActplace();
            String org_activity_item_organization=list.get(position).getOrganization();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.org_activity_item, null);//实例化一个布局文件

            TextView tv_org_activity_item_actname=convertView.findViewById(R.id.org_activity_item_actname);
            TextView tv_org_activity_item_acttime=convertView.findViewById(R.id.org_activity_item_acttime);
            TextView tv_org_activity_item_actplace=convertView.findViewById(R.id.org_activity_item_actplace);
            TextView tv_org_activity_item_organization=convertView.findViewById(R.id.org_activity_item_organization);

            tv_org_activity_item_actname.setText(org_activity_item_actname);
            tv_org_activity_item_acttime.setText(org_activity_item_acttime);
            tv_org_activity_item_actplace.setText(org_activity_item_actplace);
            tv_org_activity_item_organization.setText(org_activity_item_organization);
        }
        return convertView;
    }
}