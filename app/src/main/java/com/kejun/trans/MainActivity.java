package com.kejun.trans;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.kejun.trans.core.transmission.ConstValue;
import com.kejun.trans.core.transmission.Server.LanServer;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.dialog.InputWordDialog;
import com.kejun.trans.dialog.LinkDialog;
import com.kejun.trans.fileBrowser.FileBrowserActivity;
import com.kejun.trans.fileBrowser.OnFileBrowserResultListener;
import com.kejun.trans.fileBrowser.Utils;
import com.kejun.trans.model.Equipment;
import com.kejun.trans.selectconn.SelectConnectionActivity;
import com.kejun.trans.settings.SettingActivity;
import com.kejun.trans.transhistory.TransInfoActivity;
import com.kejun.trans.widget.RandomTextView.RandomTextView;

import java.io.File;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int IMAGE = 1;
    private static final int VIDEO = 2;

    RandomTextView  randomTextView;
    InputWordDialog inputWordDialog;

    FloatingActionButton[] floatingActionButtons = new FloatingActionButton[5];

    MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.BRODCAST_ADDRESS=new cn.silen_dev.lantransmission.model.GetBroadCast(this).getBroadcastAddress();

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        randomTextView = findViewById(R.id.random_textview);
        randomTextView.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
            @Override
            public void onRippleViewClicked(View view) {
                System.out.println(((TextView) view).getText());
            }
        });




        findFloatingActionButton();
        requestPermission();

        myApplication = (MyApplication) getApplication();
        myApplication.addEquipmentLinstener(new MyApplication.OnEquipmentLinstener() {
            @Override
            public void add(final Equipment equipment) {
                System.out.println("界面：" + equipment.getName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        randomTextView.addKeyWord(equipment.getName());
                        randomTextView.show();
                    }
                });


            }

            @Override
            public void remove(Equipment equipment) {

            }
        });

        LanServer lanServer = new LanServer(myApplication);
        lanServer.startLan();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Equipment equipment : myApplication.getConnectEquipments()) {
                    randomTextView.addKeyWord(equipment.getName());
                }
                randomTextView.show();
            }
        }, 300);


    }

    private void findFloatingActionButton() {
        floatingActionButtons[0] = findViewById(R.id.fab_item_file);
        floatingActionButtons[1] = findViewById(R.id.fab_item_text);
        floatingActionButtons[2] = findViewById(R.id.fab_item_video);
        floatingActionButtons[3] = findViewById(R.id.fab_item_image);
        floatingActionButtons[4] = findViewById(R.id.fab_item_clipboard);
        for (int i = 0; i < floatingActionButtons.length; i++) {
            floatingActionButtons[i].setOnClickListener(new FABClickListener());
        }
    }


    private class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_item_clipboard:
                    inputWordDialog = new InputWordDialog(GetClipBoardContent());
                    inputWordDialog.show(getSupportFragmentManager(), null);
                    break;
                case R.id.fab_item_text:
                    inputWordDialog = new InputWordDialog();
                    inputWordDialog.show(getSupportFragmentManager(), null);
                    break;
                case R.id.fab_item_video:
                    Intent intent2 = new Intent(Intent.ACTION_PICK,
                           MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, VIDEO);
                    break;
                case R.id.fab_item_image:
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);

                    break;
                case R.id.fab_item_file:
                    OnFileBrowserResultListener onFileBrowserResultListener = new OnFileBrowserResultListener() {
                        @Override
                        public void selectFile(File file) {
                            //提示发送的文件已经选择
                      //      Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "文件已经选择", Toast.LENGTH_SHORT).show();
                            transmissionFile(ConstValue.TRANSMISSION_FILE, file);

                        }

                        @Override
                        public void nothing() {
                            Toast.makeText(MainActivity.this, "Nothing selected!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    FileBrowserActivity.onFileBrowserResultListener = onFileBrowserResultListener;
                    startActivity(new Intent(MainActivity.this, FileBrowserActivity.class));
                    break;

            }
        }

    }


    private void transmissionFile(int type, File file) {
        Transmission transmission = new Transmission();
        transmission.setSendPath(file.getAbsolutePath());
        transmission.setFileName(file.getName());
        transmission.setMessage(transmission.getFileName());
        transmission.setType(type);
        try {
            transmission.setLength(Utils.getFileSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        transmission.setSr(ConstValue.SEND);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SelectConnectionActivity.class);
        intent.putExtra("Transmission", transmission);
        startActivity(intent);
    }

    //剪切板内容
    ClipboardManager clipboardManager;
    static String tempStr;

    public String GetClipBoardContent() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager == null) {
                    Log.i("cp", "clipboardManager==null");

                }
                if (clipboardManager.getText() != null) {
                    tempStr = clipboardManager.getText().toString();
                }
            }
        });
        return tempStr;
    }

    //获取图片和视频路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_SHORT).show();

            transmissionFile(ConstValue.TRANSMISSION_IMAGE, new File(imagePath));

            c.close();
        }
        if (requestCode == VIDEO && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String videoPath = c.getString(columnIndex);
            Toast.makeText(getApplicationContext(), videoPath, Toast.LENGTH_SHORT).show();
            transmissionFile(ConstValue.TRANSMISSION_VIDEO, new File(videoPath));
            c.close();
        }
        // TODO: 2019/2/15 测试环境
        /* if(requestCode==IMAGE&&resultCode==Activity.RESULT_OK&&data!=null){
           Uri selectImage = data.getData();
           String [] filePathColumns={MediaStore.Images.Media.DATA};
           Cursor cursor = getContentResolver().query(selectImage, filePathColumns, null, null, null);
           cursor.moveToFirst();
           int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
           String imagePath = cursor.getString(columnIndex);
           transmissionFile(ConstValue.TRANSMISSION_IMAGE,new File(imagePath));
           cursor.close();


       }



        if (requestCode==VIDEO&&resultCode== Activity.RESULT_OK&&data!=null){
            Uri selectedImage = data.getData();
            String [] filePathColumns={MediaStore.Images.Media.DATA};
            Cursor query = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            query.moveToFirst();
            int columnIndex = query.getColumnIndex(filePathColumns[0]);
            String videoPath = query.getString(columnIndex);
            Toast.makeText(getApplicationContext(),videoPath,Toast.LENGTH_SHORT).show();
            transmissionFile(ConstValue.TRANSMISSION_VIDEO,new File(videoPath));
            query.close();
            System.out.println(55);

        }*/

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_scan_qrcode:
                LinkDialog linkDialog = new LinkDialog(myApplication.getMyEquipmentInfo(), myApplication);
                linkDialog.show(getSupportFragmentManager(), null);
                break;
            case R.id.action_transmission_manager:
                startActivity(new Intent(MainActivity.this, TransInfoActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestPermission() {
        String pers[]={

        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 100);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            }
        }



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 300);

            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 300);
            }
        }

    }
}
