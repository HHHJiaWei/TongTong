package com.example.a44261.bysj102.TYQ;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.PhotoUtil;
import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.TYQPublish;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class EditTYQActivity extends AppCompatActivity {

    private String id,share;
    private String imagePath;//String图片路径
    private Bitmap bitmap;//Bitmap图片路径
    private Bitmap photo;
    //修改头像属性
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int CAMERA_JAVA_REQUEST_CODE=3;
    protected static Uri tempUri;

    private ImageView mImageHeader;

    private EditText et_share;

    private TextView tv_back,tv_right;

    private MyUser myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tyq);
        myUser=MyUser.getCurrentUser(MyUser.class);
        id=myUser.getObjectId();
        //动态申请相机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_JAVA_REQUEST_CODE);}

        initPhotoError();

        tv_right=findViewById(R.id.etyq_tv_right);
        et_share=findViewById(R.id.etyq_et_share);
        tv_back=findViewById(R.id.etyq_tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置EditText的显示方式为多行文本输入
        et_share.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        et_share.setGravity(Gravity.TOP);
        //改变默认的单行模式
        et_share.setSingleLine(false);
        //水平滚动设置为False
        et_share.setHorizontallyScrolling(false);

        setupViews();
    }

    private void setupViews() {
        mImageHeader = (ImageView) findViewById(R.id.imageView4);
        mImageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share=et_share.getText().toString().trim();
                if(share==null||photo==null){
                    Toast.makeText(getApplication(), "分享内容不能为空！" , Toast.LENGTH_SHORT).show();
                }else {
                    uploadPic(photo);
                    finish();
                }
            }
        });
    }

    /**
     * 功能：修改头像函数
     */
    //显示修改头像的对话框
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传图片");
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
        intent.putExtra("crop", "true");//能够裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    //保存裁剪之后的图片数据
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            //photo = PhotoUtil.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            mImageHeader.setImageBitmap(photo);
            //uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        imagePath= Base64.encodeToString(bytes,Base64.DEFAULT);
        Log.e("imagePath", imagePath+"");
        final TYQPublish person = new TYQPublish();
        if(imagePath != null){
            try {
                // 拿着imagePath上传了
                person.setId(id);
                person.setTouxiang(myUser.getPhoto());
                person.setName(myUser.getUsername());
                person.setSchool(myUser.getSchool());
                person.setMessage(share);
                person.setPicture(imagePath);
                person.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplication(), "分享成功:" + person.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplication(), "分享失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Logger.e("Bomb","分享失败：" + e.getMessage());

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