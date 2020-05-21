package com.example.a44261.bysj102.TYQ;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.TYQPublish;

import java.util.List;

public class EditTYQAdapter extends RecyclerView.Adapter<EditTYQAdapter.ViewHolder>{
    private List<TYQPublish> mList;
    private String imagePath,picPath;//String图片路径
    private Bitmap bitmap,picbitmap;//Bitmap图片路径

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView touxiang,picture;
        TextView name,shcool,content,time;

        public ViewHolder(View view){
            super(view);
            touxiang=(ImageView) view.findViewById(R.id.its_iv_touxiang);
            picture=(ImageView) view.findViewById(R.id.its_iv_picture);
            name=(TextView)view.findViewById(R.id.its_tv_name);
            shcool=(TextView)view.findViewById(R.id.its_tv_school);
            content=(TextView)view.findViewById(R.id.its_tv_content);
            time=(TextView)view.findViewById(R.id.its_tv_time);
        }
    }

    public EditTYQAdapter(List<TYQPublish>studentList){
        mList=studentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tyq_share,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TYQPublish student=mList.get(position);

        //初始化头像
        imagePath=student.getTouxiang();
        //如果路径为空，则显示默认图片
        if(imagePath==null)
        {
        }
        else{
            //将String对象转换成Bitmap对象并设置成当前的头像
            try {
                byte[]bitmapArray;
                bitmapArray= Base64.decode(imagePath, Base64.DEFAULT);
                bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                holder.touxiang.setImageBitmap(bitmap);
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
        //初始化图片
        picPath=student.getPicture();
        //如果路径为空，则显示默认图片
        if(picPath==null)
        {
        }
        else{
            //将String对象转换成Bitmap对象并设置成当前的头像
            try {
                byte[]bitmapArray;
                bitmapArray= Base64.decode(picPath, Base64.DEFAULT);
                picbitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                holder.picture.setImageBitmap(picbitmap);
            } catch (Exception a) {
                a.printStackTrace();
            }
        }

        holder.name.setText(student.getName());
        holder.shcool.setText(String.valueOf(student.getSchool()));
        holder.content.setText(student.getMessage());
        holder.time.setText(student.getUpdatedAt().toString());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
