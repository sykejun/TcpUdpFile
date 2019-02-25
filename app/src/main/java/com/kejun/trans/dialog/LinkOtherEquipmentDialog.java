package com.kejun.trans.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kejun.trans.MyApplication;
import com.kejun.trans.R;
import com.kejun.trans.core.transmission.Client.LanClient;
import com.kejun.trans.model.Equipment;


public class LinkOtherEquipmentDialog extends DialogFragment {
    MyApplication myApplication;
    public LinkOtherEquipmentDialog(MyApplication myApplication) {
        super();
        this.myApplication=myApplication;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入您要连接设备的IP地址");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.link_other_equipment_dialog_layut, null);
        //布局控件等的安排
        final EditText editText = (EditText) view.findViewById(R.id.inputareaofip);
        editText.setText("127.0.0.1");

        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText() != null && !editText.getText().toString().equals(""))
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LanClient lanClient = new LanClient(editText.getText().toString());
                            lanClient.setOnLanCilentListener(new LanClient.OnLanCilentListener() {
                                @Override
                                public void getHelloReply(Equipment equipment) {
                                    myApplication.addEquipment(equipment);
                                }

                                @Override
                                public void failToLink() {
                                    Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_LONG).show();
                                }
                            });
                            lanClient.sendHello();
                        }
                    }).start();
            }
        });
        builder.setNegativeButton("取消", null);

        return builder.create();
    }
}
