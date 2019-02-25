package com.kejun.trans.transhistory;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kejun.trans.MyApplication;
import com.kejun.trans.R;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.model.Equipment;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.kejun.trans.core.transmission.ConstValue.RECEIVE;
import static com.kejun.trans.core.transmission.ConstValue.SEND;
import static com.kejun.trans.core.transmission.ConstValue.STATUS_DONE;
import static com.kejun.trans.core.transmission.ConstValue.STATUS_ING;
import static com.kejun.trans.core.transmission.ConstValue.STATUS_NONE;
import static com.kejun.trans.core.transmission.ConstValue.TRANSMISSION_IMAGE;
import static com.kejun.trans.core.transmission.ConstValue.TRANSMISSION_VIDEO;


public class FileList_Adapter extends RecyclerView.Adapter<FileList_Adapter.ViewHolder> {
    private static final String              TAG =FileList_Adapter.class.getSimpleName();
    private              Context             context;
    private              List<Transmission>  filesList;
    private              OnItemClickListener onItemClickListener;

    private MyApplication myApplication;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public FileList_Adapter(List<Transmission> mfilesList,MyApplication myApplication) {
        filesList = mfilesList;
        this.myApplication=myApplication;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      public    CardView cardView;
      public   ImageView image;
      public  TextView fileName;
      public  TextView fileUser;
      public   TextView fileStatus;
      public  ImageView fileLoad;
      public   CheckBox check;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            image = view.findViewById(R.id.pictures);
            fileName =  view.findViewById(R.id.file_name);
            fileUser = view.findViewById(R.id.file_user);
            fileStatus =  view.findViewById(R.id.file_status);
            fileLoad = view.findViewById(R.id.file_load);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.activity_trans_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
          Transmission transmission = filesList.get(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { //recycler的item相应单击事件
//                if (null!=onItemClickListener){
//                    onItemClickListener.onClick(transmission);
//                }

//            }
//        });
        holder.fileName.setText(transmission.getFileName());
        Equipment equipment=myApplication.findEquipment(transmission.getUserId());
        holder.fileUser.setText(null==equipment?transmission.getUserId()+"":equipment.getName());
        switch (transmission.getStatus()) {
            case STATUS_DONE:
                holder.fileStatus.setText("已完成");
                break;
            case STATUS_ING:
                holder.fileStatus.setText("传输中");
                break;
            case STATUS_NONE:
                holder.fileStatus.setText("未完成");
                break;
            default:
                break;
        }

        String path="";
        switch (transmission.getSr()) {
            case SEND:
                holder.fileLoad.setImageResource(R.mipmap.upload);
                path=transmission.getSendPath();//获取路径
                break;
            case RECEIVE:
                path=transmission.getSavePath();
                holder.fileLoad.setImageResource(R.mipmap.dowload);
                break;
            default:
                break;
        }

        Bitmap bmp = null;
        switch (transmission.getType()) {
            case TRANSMISSION_IMAGE:
                // 加载本地图片
                Glide.with(context).load(path).into(holder.image);
                break;
            case TRANSMISSION_VIDEO:
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(path);
                    bmp = retriever.getFrameAtTime();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] image=baos.toByteArray();
                    Glide.with(context).load(image).into(holder.image);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public interface OnItemClickListener{
        void onClick(Transmission transmission);
    }

}


