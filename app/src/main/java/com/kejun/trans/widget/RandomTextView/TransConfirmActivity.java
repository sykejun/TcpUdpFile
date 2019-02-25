package com.kejun.trans.widget.RandomTextView;

import android.content.Intent;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kejun.trans.R;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.fileBrowser.FileBrowserActivity;
import com.kejun.trans.fileBrowser.OnFileBrowserResultListener;
import com.kejun.trans.model.Equipment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kejun.trans.core.transmission.ConstValue.TRANSMISSION_FILE;
import static com.kejun.trans.core.transmission.ConstValue.TRANSMISSION_IMAGE;
import static com.kejun.trans.core.transmission.ConstValue.TRANSMISSION_TEXT;
import static com.kejun.trans.core.transmission.ConstValue.TRANSMISSION_VIDEO;


public class TransConfirmActivity extends AppCompatActivity {
    private Transmission transmission;
    private Equipment    equipment;
    private TextView     fileName;
    private TextView     fileType;
    private TextView     sender;
    private TextView     sendIp;
    private TextView     sendTime;
    private TextView     catalogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_transconfirm);
        fileName = (TextView)findViewById(R.id.text_FileName);
        fileType = (TextView)findViewById(R.id.text_FileType);
        sender = (TextView)findViewById(R.id.text_Sender);
        sender.setText(equipment.getName());
        sendIp = (TextView)findViewById(R.id.text_SendIP);
        sendIp.setText(equipment.getAddress());
        sendTime = (TextView)findViewById(R.id.text_SendTime);
        sendTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(transmission.getTime())));
        catalogue = (TextView)findViewById(R.id.text_Catalogue);
        fileName.setText(transmission.getFileName());

        switch (transmission.getType()) {
            case TRANSMISSION_FILE:
                fileType.setText("文件");
                catalogue.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("setting_transmission_file", Environment.getExternalStorageDirectory().getAbsolutePath()));
            case TRANSMISSION_IMAGE:
                fileType.setText("图片");
                catalogue.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("setting_transmission_image", Environment.getExternalStorageDirectory().getAbsolutePath()));

            case TRANSMISSION_VIDEO:
                fileType.setText("视频");
                catalogue.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("setting_transmission_video", Environment.getExternalStorageDirectory().getAbsolutePath()));

            case TRANSMISSION_TEXT:
                fileType.setText("文本");
        }


        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(), FileBrowserActivity.class);
                intent.putExtra("mode",FileBrowserActivity.SELECT_DIRECTORY);
                intent.putExtra("path",catalogue.getText());
                FileBrowserActivity.onFileBrowserResultListener=new OnFileBrowserResultListener() {
                    @Override
                    public void selectFile(File file) {
                        catalogue.setText(file.getAbsolutePath());
                    }

                    @Override
                    public void nothing() {

                    }
                };
                startActivity(intent);
            }
        });
    }

}
