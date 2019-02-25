package com.kejun.trans.transhistory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kejun.trans.MyApplication;
import com.kejun.trans.R;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.model.Equipment;

import java.util.List;

import static com.kejun.trans.core.transmission.ConstValue.RECEIVE;
import static com.kejun.trans.core.transmission.ConstValue.SEND;


public class File_Adapter extends RecyclerView.Adapter<File_Adapter.ViewHolder>{
    private Context            context;
    private List<Transmission> fileList;
    private MyApplication      myApplication;
//    private static final String TAG = "WordList_Adapter";
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        ImageView imageView;
        TextView text_word;
        TextView text_user;
        TextView fileStatus;
        ImageView load;

        public ViewHolder(View view)
        {
            super(view);
            cardView =(CardView) view;
            text_word=(TextView) view.findViewById(R.id.text_word);
            text_user=(TextView) view.findViewById(R.id.text_user);
            //fileStatus=(TextView) view.findViewById(R.id.file_status);
//            check=(CheckBox) view.findViewById(R.id.check);
        }
    }
    public File_Adapter(List<Transmission> mwordlist,MyApplication myApplication){
        fileList=mwordlist;
        this.myApplication=myApplication;
    }

    @NonNull
    @Override

    public File_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context=parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.trans_info_word,
                parent,false);
        return new File_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transmission transmission=fileList.get(position);
        holder.text_word.setText(transmission.getMessage());
        Equipment equipment=myApplication.findEquipment(transmission.getUserId());
        holder.text_user.setText(null==equipment?transmission.getUserId()+"":equipment.getName());
        switch (transmission.getSr()){
            case SEND:
                holder.load.setImageResource(R.mipmap.upload);
                break;
            case RECEIVE:
                holder.load.setImageResource(R.mipmap.dowload);
                break;
            default:
                break;
        }
    }
/*
    @Override
    public void onBindViewHolder(@NonNull WordList_Adapter.ViewHolder holder, int position) {
        Transmission transmission=fileList.get(position);
        holder.trans_word.setText(transmission.getMessage());
        holder.trans_word_user.setText(String.valueOf(transmission.getUserId()));
        switch (transmission.getSr()){
            case SEND:
                holder.load.setImageResource(R.mipmap.upload);
                break;
            case RECEIVE:
                holder.load.setImageResource(R.mipmap.dowload);
                break;
            default:
                break;
        }
    }*/

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}