package com.kejun.trans.selectconn;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kejun.trans.R;
import com.kejun.trans.model.Equipment;

import java.util.List;

import static com.kejun.trans.core.transmission.ConstValue.RECEIVE;
import static com.kejun.trans.core.transmission.ConstValue.SEND;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private List<Equipment>     equipmentList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView equipmentName, equipmentAddress, equipmentType;
        private ImageView icon;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            equipmentName =  view.findViewById(R.id.text_equipmentName);
            equipmentAddress =  view.findViewById(R.id.text_equipmentAddress);
            equipmentType = view.findViewById(R.id.text_equipmentType);
            icon = view.findViewById(R.id.icon);
        }
    }

    public EquipmentAdapter(List<Equipment> equipmentList){
        this.equipmentList = equipmentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment,parent,
                false);
        final ViewHolder holder = new ViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //recycler的item相应单击事件
                int position = holder.getAdapterPosition();
                Equipment equipment = equipmentList.get(position);
                if (null!=onItemClickListener){
                    onItemClickListener.onClick(equipment);
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Equipment equipment = equipmentList.get(position);
        holder.equipmentName.setText(equipment.getName());
        holder.equipmentAddress.setText(equipment.getAddress());
        holder.equipmentType.setText(String.valueOf(equipment.getType()));
        switch (equipment.getType()){
            case SEND:
                holder.icon.setImageResource(R.mipmap.phone);
                break;
            case RECEIVE:
                holder.icon.setImageResource(R.mipmap.computer);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    public interface OnItemClickListener{
        void onClick(Equipment equipment);
    }
}
