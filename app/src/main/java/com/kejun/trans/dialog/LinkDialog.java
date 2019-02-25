package com.kejun.trans.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.kejun.trans.MyApplication;
import com.kejun.trans.R;
import com.kejun.trans.model.Equipment;


public class LinkDialog extends DialogFragment {
    public  Equipment     equipment;
    private MyApplication myApplication;
    public LinkDialog(Equipment equipment,MyApplication myApplication) {
        this.equipment=equipment;
        this.myApplication=myApplication;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("设备连接");
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.link_dialog,null);
        //显示本机的信息
        ((TextView)view.findViewById(R.id.ipaddressinfo)).setText(equipment.getAddress());
        ((TextView)view.findViewById(R.id.webinfo)).setText("http://"+equipment.getAddress()+":"+equipment.getPort()+"/web");

        ((TextView)view.findViewById(R.id.app_port_info)).setText(equipment.getPort()+"");

        builder.setView(view);
        builder.setPositiveButton("连接其他设备", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 LinkOtherEquipmentDialog linkOtherEquipmentDialog=new LinkOtherEquipmentDialog(myApplication);
                linkOtherEquipmentDialog.show(getFragmentManager(),null);
            }
        });
        builder.setNegativeButton("取消",null);

        return builder.create();
    }
}
