package com.kejun.trans.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.kejun.trans.R;
import com.kejun.trans.core.transmission.ConstValue;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.selectconn.SelectConnectionActivity;


public class InputWordDialog extends DialogFragment {
    public String tempstr;
    public InputWordDialog(){
        super();
        this.tempstr="";
    }
    public InputWordDialog(String tempstr){
        super();
        this.tempstr=tempstr;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)

    {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入您要发送的文字");
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.input_word_dialog,null);
        //布局控件等的安排
        final EditText editText=((EditText)view.findViewById(R.id.inputareaofword));
        editText.setText(tempstr);
        builder.setView(view);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null!=editText.getText()&&!editText.getText().toString().equals("")){
                    String text=editText.getText().toString();
                    Transmission transmission=new Transmission();
                    transmission.setMessage(text);
                    transmission.setType(ConstValue.TRANSMISSION_TEXT);
                    transmission.setSr(ConstValue.SEND);
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),SelectConnectionActivity.class);
                    intent.putExtra("Transmission",transmission);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("取消",null);

        return builder.create();
    }
}
