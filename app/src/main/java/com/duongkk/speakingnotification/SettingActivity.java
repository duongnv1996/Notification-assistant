package com.duongkk.speakingnotification;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.duongkk.speakingnotification.Utils.Constants;
import com.duongkk.speakingnotification.Utils.SharedPref;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private Switch mSwitchOnOff;
    private LinearLayout mLayoutRoot;
    private boolean isOn;
    TextToSpeech tts;
    String pack;
    String title;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }


    private void initView() {


        mSwitchOnOff = (Switch)findViewById(R.id.sw);
        mLayoutRoot=(LinearLayout)findViewById(R.id.ll_root);
        mSwitchOnOff.setOnCheckedChangeListener(this);
        mSwitchOnOff.setChecked(SharedPref.getInstance(this).getBoolean(Constants.ENABLE_APP,true));
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.ll_sw){
            mSwitchOnOff.setChecked(!isOn);
        }else if(isOn){
            switch (id){
                case R.id.ll_list_app:{
                    startActivity(new Intent(SettingActivity.this,ApplicationActivity.class));
                    break;
                }
                case R.id.ll_name:{
                        break;
                }
                case R.id.ll_right:{
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    //startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    break;
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id=buttonView.getId();
        if(id==R.id.sw){
            if(isChecked){

                isOn=true;
                for (int i = 0; i < mLayoutRoot.getChildCount() ; i++) {
                    mLayoutRoot.getChildAt(i).setEnabled(true);
                    mLayoutRoot.getChildAt(i).setAlpha(1);
                }
            }else{

                isOn=false;
                for (int i = 0; i < mLayoutRoot.getChildCount() ; i++) {
                    mLayoutRoot.getChildAt(i).setEnabled(false);
                    mLayoutRoot.getChildAt(i).setClickable(false);
                    mLayoutRoot.getChildAt(i).setAlpha(0.5f);
                }
            }

            SharedPref.getInstance(this).putBoolean(Constants.ENABLE_APP,isOn);
        }else if(isOn){
            switch (id){
                case R.id.sw:{

                    break;
                }
            }
        }

    }



}
