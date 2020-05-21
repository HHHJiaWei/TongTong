package com.example.a44261.bysj102.TT.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.Act;
import com.example.a44261.bysj102.db.Org;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomeOrgActItemAdapter extends BaseAdapter {
    private Context context;
    private List<Org> list;
    private String actname,acttime,actplace,organization;
    private TextView tv_org_activity_item_actname,tv_org_activity_item_acttime,tv_org_activity_item_actplace,tv_org_activity_item_organization;

    public HomeOrgActItemAdapter(Context context, List<Org> list) {
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
        if (convertView==null) {
            String orgid = list.get(position).getObjectId();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.org_activity_item, null);//实例化一个布局文件

            tv_org_activity_item_actname = convertView.findViewById(R.id.org_activity_item_actname);
            tv_org_activity_item_acttime = convertView.findViewById(R.id.org_activity_item_acttime);
            tv_org_activity_item_actplace = convertView.findViewById(R.id.org_activity_item_actplace);
            tv_org_activity_item_organization = convertView.findViewById(R.id.org_activity_item_organization);

            BmobQuery<Act> query = new BmobQuery<Act>();
            Org org = new Org();
            org.setObjectId(orgid);
            query.addWhereRelatedTo("myacyivity", new BmobPointer(org));
            //        query.addWhereEqualTo("organizer", userobject);
            query.setLimit(50);
            query.order("school,orgname");
            query.findObjects(new FindListener<Act>() {
                @Override
                public void done(final List<Act> list, BmobException e) {
                    if (e == null) {

                        actname = list.get(position).getActname();
                        acttime = list.get(position).getActtime();
                        actplace = list.get(position).getActplace();
                        organization = list.get(position).getOrganization();

                        tv_org_activity_item_actname.setText(actname);
                        tv_org_activity_item_acttime.setText(acttime);
                        tv_org_activity_item_actplace.setText(actplace);
                        tv_org_activity_item_organization.setText(organization);

                    }
                }

            });


        }

        return convertView;
    }
}