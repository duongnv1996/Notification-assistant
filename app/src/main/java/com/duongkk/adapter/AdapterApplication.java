package com.duongkk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duongkk.models.Application;
import com.duongkk.speakingnotification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 7/20/2016.
 */
public class AdapterApplication extends RecyclerView.Adapter<AdapterApplication.ViewHolder> {
    List<Application> listApplications;
    SparseBooleanArray sparseBooleanArray;
    Context context;

    public AdapterApplication(Context context,List<Application> listApplications){
        this.listApplications= listApplications;
        this.context = context;
        sparseBooleanArray= new SparseBooleanArray();
        for (int i = 0; i < listApplications.size(); i++) {
            sparseBooleanArray.put(i,listApplications.get(i).isChecked());
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.application_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(holder!=null){
            final Application app = listApplications.get(position);
            holder.mIcon.setImageDrawable(app.getmIcon());
            holder.mTvPackage.setText(app.getmPackage());
            holder.mTvName.setText(app.getmName());
            holder.mCheckbox.setOnCheckedChangeListener(null);
            holder.mCheckbox.setChecked(sparseBooleanArray.get(position));

            holder.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(app.isChecked()){
                        setCheckedApp(false, position, app);
                        holder.mCheckbox.setChecked(false);
                    }else{
                        setCheckedApp(true, position, app);
                        holder.mCheckbox.setChecked(true);
                    }

                }
            });

            holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setCheckedApp(isChecked, position, app);
                }
            });
        }
    }

    private void setCheckedApp(boolean isChecked, int position, Application app) {
        sparseBooleanArray.put(position,isChecked);
        app.setChecked(isChecked);
        listApplications.set(position,app);
    }

    public List<Application> getListAppChecked(){
       List<Application> listAppChecked;
       listAppChecked =new ArrayList<>();
        for (Application app:listApplications) {
            if(app.isChecked()){
                listAppChecked.add(app);
            }
        }
       return listAppChecked;
    }
    @Override
    public int getItemCount() {
        return listApplications.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView mTvName;
        private TextView mTvPackage;
        private CheckBox mCheckbox;
        private ImageView mIcon;
        private LinearLayout mRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView)itemView.findViewById(R.id.tv_name);
            mTvPackage = (TextView)itemView.findViewById(R.id.tv_package);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mCheckbox = (CheckBox)itemView.findViewById(R.id.cb);
            mRoot = (LinearLayout)itemView.findViewById(R.id.card_application);
        }
    }
}
