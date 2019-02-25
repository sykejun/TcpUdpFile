package com.kejun.trans.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.kejun.trans.R;
import com.kejun.trans.fileBrowser.FileBrowserActivity;
import com.kejun.trans.fileBrowser.OnFileBrowserResultListener;

import java.io.File;


public class SettingFragment extends PreferenceFragment {
    Preference filePreference,videoPreference,imagePreference;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference_dependencies);
        sharedPreferences=getPreferenceManager().getSharedPreferences();
        filePreference=findPreference("setting_transmission_file");
        filePreference.setOnPreferenceClickListener(new MyPreferenceClickListener());

        videoPreference=findPreference("setting_transmission_video");
        videoPreference.setOnPreferenceClickListener(new MyPreferenceClickListener());

        imagePreference=findPreference("setting_transmission_image");
        imagePreference.setOnPreferenceClickListener(new MyPreferenceClickListener());

        initPreference();

    }

    private class MyPreferenceClickListener implements Preference.OnPreferenceClickListener{
        @Override
        public boolean onPreferenceClick(final Preference preference) {
            System.out.println(preference.getKey());
            Intent intent=new Intent();
            intent.setClass(getActivity(), FileBrowserActivity.class);
            intent.putExtra("mode",FileBrowserActivity.SELECT_DIRECTORY);
            intent.putExtra("path",getData(preference,null));
            FileBrowserActivity.onFileBrowserResultListener=new OnFileBrowserResultListener() {
                @Override
                public void selectFile(File file) {
                    saveData(preference,file);
                }

                @Override
                public void nothing() {

                }
            };
            startActivity(intent);
            return false;
        }
    }

    private void initPreference(){
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/TCPFile/File");
        mkdirForStore(file);
        File fileVideo=new File(file.getParentFile().getAbsolutePath()+"/Video");
        mkdirForStore(fileVideo);
        File fileImage=new File(file.getParentFile().getAbsolutePath()+"/Image");
        mkdirForStore(fileImage);

        getData(filePreference,file);
        getData(videoPreference,fileVideo);
        getData(imagePreference,fileImage);

    }
    private String getData(Preference preference,File initFile){
        if (!sharedPreferences.contains(preference.getKey())){
            saveData(preference,initFile);
            if (null!=initFile){
                return initFile.getAbsolutePath();
            }
        }else{
            String path=sharedPreferences.getString(preference.getKey(),initFile==null?"":initFile.getAbsolutePath());
            preference.setSummary(path);
            return path;
        }
        return null;
    }

    private void saveData(Preference preference,File file){
        preference.setSummary(file.getAbsolutePath());
        sharedPreferences.edit().putString(preference.getKey(),file.getAbsolutePath()).commit();
    }

    private void mkdirForStore(File file){
        if (!file.exists()){
            mkdirForStore(file.getParentFile());
            file.mkdir();
        }
    }
}
