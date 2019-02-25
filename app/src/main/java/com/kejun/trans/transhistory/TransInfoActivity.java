
package com.kejun.trans.transhistory;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.kejun.trans.MyApplication;
import com.kejun.trans.R;
import com.kejun.trans.SQLite.EquipOperators;
import com.kejun.trans.SQLite.TransOperators;
import com.kejun.trans.core.transmission.ConstValue;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.model.Equipment;
import com.kejun.trans.selectconn.EquipmentAdapter;

import java.util.ArrayList;
import java.util.List;


public class TransInfoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Transmission> transList_pic    = new ArrayList<>();
    private List<Transmission> transList_vedio  = new ArrayList<>();
    private List<Transmission> transList_word   = new ArrayList<>();
    private List<Transmission> transList_file   = new ArrayList<>();
    private List<Equipment>    transList_device = new ArrayList<>();

    private Toolbar toolbar;

    private FileList_Adapter                                           tAdapter;
    private WordList_Adapter wAdapter;
    private EquipmentAdapter                                           eAdapter;
    private WordList_Adapter fAdapter;

    private static final String TAG = TransInfoActivity.class.getSimpleName();

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_pic;
    private RadioButton rb_vedio;
    private RadioButton rb_file;
    private RadioButton rb_word;
    private RadioButton rb_device;

    private TransOperators transOperators;
    private EquipOperators equipOperators;

    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_info);
        setToolbar();

        myApplication=(MyApplication)getApplication();

        rg_tab_bar = findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);

        rb_pic = findViewById(R.id.rb_pic);
        rb_pic.setChecked(true);
        rb_vedio =  findViewById(R.id.rb_vedio);
        rb_file =  findViewById(R.id.rb_file);
        rb_word =  findViewById(R.id.rb_word);
        rb_device =  findViewById(R.id.rb_device);

        transOperators = new TransOperators(this);
        equipOperators = new EquipOperators(this);
       //获取所有关键字的集合
        transList_word = transOperators.getAllTrans(ConstValue.TRANSMISSION_TEXT);
        //获取所有图片的集合
        transList_pic = transOperators.getAllTrans(ConstValue.TRANSMISSION_IMAGE);
        //获取所有设备的集合
        transList_device = equipOperators.getAllEquipment();
        //获取所有文件的集合
        transList_file = transOperators.getAllTrans(ConstValue.TRANSMISSION_FILE);
        //获取所有视频的集合
        transList_vedio = transOperators.getAllTrans(ConstValue.TRANSMISSION_VIDEO);


        for (Transmission transmission : transOperators.getAllTrans()) {
            System.out.println(new Gson().toJson(transmission));
        }

        init_pic(transList_pic);
    }

    //为每个button设置点击监听事件
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        layoutManager = new LinearLayoutManager(this);
        recyclerView =  findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        switch (checkedId) {
            case R.id.rb_pic:
                init_pic(transList_pic);
                break;
            case R.id.rb_vedio:
                init_vedio(transList_vedio);
                break;
            case R.id.rb_file:
                init_file(transList_file);
                break;
            case R.id.rb_word:
                init_word(transList_word);
                break;
            case R.id.rb_device:
                init_device(transList_device);
                break;
            default:
                break;
        }
    }

    /**
     * 设置图片的的数据
     * @param transList_pic
     */
    public void init_pic(List<Transmission> transList_pic) {
        tAdapter = new FileList_Adapter(transList_pic,myApplication);
        recyclerView.setAdapter(tAdapter);
    }

    /**
     * 设置视频的数据
     * @param transList_vedio
     */
    public void init_vedio(List<Transmission> transList_vedio) {
        tAdapter = new FileList_Adapter(transList_vedio,myApplication);
        recyclerView.setAdapter(tAdapter);
    }

    /**
     * 设置传输的文件数据
     * @param transList_file
     */
    public void init_file(List<Transmission> transList_file) {
        fAdapter = new WordList_Adapter(transList_file,myApplication,this);
        recyclerView.setAdapter(fAdapter);
    }

    /**
     * 关键字的数据
     * @param transList_word
     */
    public void init_word(List<Transmission> transList_word) {
        wAdapter = new WordList_Adapter(transList_word,myApplication,this);
        recyclerView.setAdapter(wAdapter);
    }

    /**
     * 获取登陆过设备的数据
     * @param transList_device
     */
    public void init_device(List<Equipment> transList_device) {
        eAdapter = new EquipmentAdapter(transList_device);
        recyclerView.setAdapter(eAdapter);
    }

    public void setToolbar() {
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 用于返回主菜单的箭头
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

