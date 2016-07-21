package com.duongkk.speakingnotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private Switch mSwitchOnOff;
    private LinearLayout mLayoutRoot;
    private boolean isOn;
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
        mSwitchOnOff.setChecked(true);
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
        }else if(isOn){
            switch (id){
                case R.id.sw:{

                    break;
                }
            }
        }

    }
}
