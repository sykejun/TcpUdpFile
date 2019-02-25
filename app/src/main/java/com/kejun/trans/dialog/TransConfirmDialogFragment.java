package com.kejun.trans.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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


@SuppressLint("ValidFragment")
public class TransConfirmDialogFragment extends DialogFragment {
    private OnTransmissionConfirmResultListener onTransmissionConfirmResultListener;
    private Transmission                        transmission;
    private Equipment                           equipment;
    private TextView                            fileName;
    private TextView                            fileType;
    private TextView                            sender;
    private TextView                            sendIp;
    private TextView                            sendTime;
    private TextView                            catalogue;

    public TransConfirmDialogFragment(Transmission transmission, Equipment equipment) {
        super();
        this.transmission=transmission;
        this.equipment=equipment;
        this.setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_transconfirm, null);
        builder.setView(view);
        builder.setTitle("传输文件确认");
        fileName = view.findViewById(R.id.text_FileName);
        fileType = (TextView) view.findViewById(R.id.text_FileType);
        sender = (TextView) view.findViewById(R.id.text_Sender);
        sender.setText(equipment.getName());
        sendIp = (TextView) view.findViewById(R.id.text_SendIP);
        sendIp.setText(equipment.getAddress());
        sendTime = (TextView) view.findViewById(R.id.text_SendTime);
        sendTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(transmission.getTime())));
        catalogue = (TextView) view.findViewById(R.id.text_Catalogue);
        fileName.setText(transmission.getFileName());

        switch (transmission.getType()) {
            case TRANSMISSION_FILE:
                fileType.setText("文件");
                catalogue.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("setting_transmission_file", Environment.getExternalStorageDirectory().getAbsolutePath()));
                break;
            case TRANSMISSION_IMAGE:
                fileType.setText("图片");
                catalogue.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("setting_transmission_image", Environment.getExternalStorageDirectory().getAbsolutePath()));
                break;
            case TRANSMISSION_VIDEO:
                fileType.setText("视频");
                catalogue.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("setting_transmission_video", Environment.getExternalStorageDirectory().getAbsolutePath()));
                break;
            case TRANSMISSION_TEXT:
                fileType.setText("文本");
                break;
        }


        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), FileBrowserActivity.class);
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

        builder.setPositiveButton("确认传输", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onTransmissionConfirmResultListener!=null){
                    onTransmissionConfirmResultListener.send(catalogue.getText().toString());

                }
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消传输", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null!=onTransmissionConfirmResultListener){
                    onTransmissionConfirmResultListener.cancel();

                }
                getActivity().finish();
            }
        });
        /*Dialog dialog=builder.create();
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));*/
        return builder.create();
    }

    public void setOnTransmissionConfirmResultListener(OnTransmissionConfirmResultListener onTransmissionConfirmResultListener) {
        this.onTransmissionConfirmResultListener = onTransmissionConfirmResultListener;
    }

    public interface OnTransmissionConfirmResultListener{
        void send(String savePath);
        void cancel();
    }
}



