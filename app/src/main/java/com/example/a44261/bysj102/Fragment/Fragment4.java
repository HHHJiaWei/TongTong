package com.example.a44261.bysj102.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.FragmentActivity;
import com.example.a44261.bysj102.IM.ui.UserInfoActivity;
import com.example.a44261.bysj102.LoginActivity;
import com.example.a44261.bysj102.MainActivity;
import com.example.a44261.bysj102.MyInformationActivity;
import com.example.a44261.bysj102.PhotoUtil;
import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.UserActivity;
import com.example.a44261.bysj102.UpdateInformationActivity;
import com.example.a44261.bysj102.UpdatePSDActivity;
import com.example.a44261.bysj102.db.MyUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static android.app.Activity.RESULT_OK;


public class Fragment4 extends Fragment {

    private String imagePath;//String头像路径
    private Bitmap bitmap;//Bitmap头像路径
    //修改头像属性
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int CAMERA_JAVA_REQUEST_CODE=3;
    protected static Uri tempUri;
    private ImageView iv_personal_icon;
    private String id;
    private TextView tv_nickname,tv_username,tv_main_title,tv_back,tv_right;
    private Button btn_tcdl,btn_xgmm;
    private LinearLayout ll_tv_infor,ll_img_touxiang,f4_ll_user;
    private MyUser myUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment4,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myUser=MyUser.getCurrentUser(MyUser.class);

        //动态申请相机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_JAVA_REQUEST_CODE);}

        initPhotoError();

        //获取组件
        iv_personal_icon=(ImageView)getActivity().findViewById(R.id.f4_img_touxiang);

        id=myUser.getObjectId();
        //初始化头像
        imagePath=myUser.getPhoto();
        //如果路径为空，则显示默认图片
        if(imagePath==null)
        {
            iv_personal_icon.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            //将String对象转换成Bitmap对象并设置成当前的头像
            try {
                byte[]bitmapArray;
                bitmapArray= Base64.decode(imagePath, Base64.DEFAULT);
                bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                iv_personal_icon.setImageBitmap(bitmap);
            } catch (Exception a) {
                a.printStackTrace();
            }
        }

        //iv_personal_icon用来监听修改头像事件
        iv_personal_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        tv_back = (TextView)getActivity().findViewById(R.id.tv_back);
        tv_main_title = (TextView)getActivity().findViewById(R.id.tv_main_title);
        tv_right = (TextView)getActivity().findViewById(R.id.tv_right);
        tv_username=(TextView)getActivity().findViewById(R.id.f4_tv_username);
        tv_nickname=(TextView)getActivity().findViewById(R.id.f4_tv_nickname);
        btn_tcdl=(Button)getActivity().findViewById(R.id.f4_btn_tcdl);
        btn_xgmm=(Button)getActivity().findViewById(R.id.f4_btn_xgmm);
        ll_tv_infor=(LinearLayout)getActivity().findViewById(R.id.f4_ll_tv_infor);
        f4_ll_user=(LinearLayout)getActivity().findViewById(R.id.f4_ll_user);

        tv_back.setText("");
        tv_main_title.setText("我");
        tv_right.setText("");
        tv_username.setText(myUser.getUsername());
        tv_nickname.setText(myUser.getNickname());

        ll_tv_infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyInformationActivity.class);
                startActivity(intent);
            }
        });
        btn_tcdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                BmobIM.getInstance().disConnect();
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        btn_xgmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),UpdatePSDActivity.class);
                startActivity(intent);
            }
        });
        f4_ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),UserActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 功能：修改头像函数
     */
    //显示修改头像的对话框
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    //裁剪图片方法实现
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    //保存裁剪之后的图片数据
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = PhotoUtil.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);//图片压缩
        byte[]bytes=bStream.toByteArray();
        imagePath= Base64.encodeToString(bytes,Base64.DEFAULT);
        Log.e("imagePath", imagePath+"");
        final MyUser person = new MyUser();
        if(imagePath != null){
            try {
                // 拿着imagePath上传了
                person.setPhoto(imagePath);
                person.update(id, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "头像更新成功:" + person.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "头像更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }


}
