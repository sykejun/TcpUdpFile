package com.kejun.trans.selectconn;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kejun.trans.MyApplication;
import com.kejun.trans.R;
import com.kejun.trans.SQLite.TransOperators;
import com.kejun.trans.core.transmission.Client.LanClient;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.model.Equipment;

import java.util.ArrayList;
import java.util.List;


public class SelectConnectionActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private List<Equipment>    equipmentList;
    MyItemDecoration myItemDecoration;
    MyApplication    myApplication;
    EquipmentAdapter adapter;
    public Transmission transmission;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_connection);
        setToolbar();
      //获取到文件的信息
        transmission=(Transmission) getIntent().getSerializableExtra("Transmission");

        myApplication=(MyApplication)getApplication();
        swipeRefresh = findViewById(R.id.swipe_refresh);
       // swipeRefresh.setColorSchemeColors(colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                initEquipment();
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
        initEquipment();
        RecyclerView recyclerView = findViewById(R.id.equipment_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EquipmentAdapter(equipmentList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyItemDecoration());

        adapter.setOnItemClickListener(new EquipmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(Equipment equipment) {
                //向数据库插进新文件
                TransOperators transOperators=new TransOperators(SelectConnectionActivity.this);
                transmission.setUserId(equipment.getId());
                transmission.setId((int)transOperators.insertTransWithReturnId(transmission));
                transmission.setTime(System.currentTimeMillis());
                LanClient lanClient=new LanClient(equipment.getAddress());
                lanClient.sendTransmisstion(transmission,myApplication.getMyEquipmentInfo());
                Toast.makeText(SelectConnectionActivity.this,"已发送！",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private void initEquipment(){
        equipmentList=new ArrayList<>();
        equipmentList=myApplication.getConnectEquipments();

    }

    class MyItemDecoration extends RecyclerView.ItemDecoration {
        private Paint mPaint;
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(50, 0, 50, 0);
        }
    }

    public void setToolbar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        try {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            //TODO:其他事件监听
        }
        return super.onOptionsItemSelected(item);
    }


}

