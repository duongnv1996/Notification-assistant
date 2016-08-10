package com.duongkk.speakingnotification.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.duongkk.speakingnotification.R;
import com.duongkk.speakingnotification.Utils.Constants;
import com.duongkk.speakingnotification.Utils.LogX;
import com.duongkk.speakingnotification.Utils.SharedPref;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.Locale;

/**
 * Created by MyPC on 6/23/2016.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService implements TextToSpeech.OnInitListener {
    Context context;
    TextToSpeech tts;
    //List<TextToSpeech.EngineInfo> list =TextToSpeech.getEngines();
    @Override
    public void onCreate() {
        super.onCreate();

        tts =new TextToSpeech(this,this,"com.google.android.tts");
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
     //   super.onNotificationPosted(sbn);
        if(SharedPref.getInstance(this).getBoolean(Constants.ENABLE_APP,true)) {
            String pack = sbn.getPackageName();
            LogX.e(pack.toString());
            String[] listPackageChecked = SharedPref.getInstance(getBaseContext()).getString(Constants.LIST_APP, "").split(getApplicationContext()
                    .getString(R.string.signal));
            LogX.e(listPackageChecked.toString());
            for (int i = 0; i < listPackageChecked.length; i++) {
                if(pack.equals(listPackageChecked[i])){
                    //String tag = sbn.getTag();
                    Bundle bundle = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        bundle = sbn.getNotification().extras;
                    }
                    String title = bundle.getString("android.title");
                    String text = bundle.getString("android.text").toString();
                    LogX.e("Package : " + pack);
                    //  LogX.e("tag : "+tag);
                    LogX.e("title : " + title);
                    LogX.e("text : " + text);
                    Intent intent = new Intent("Msg");
                    intent.putExtra("package", pack);
                    //  intent.putExtra("ticker", ticker);
                    intent.putExtra("title", title);
                    intent.putExtra("text", text);
                    AsynTasTranslate asynTasTranslate = new AsynTasTranslate();
                    asynTasTranslate.execute(text);
                    //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    break;
                }
            }

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
      //  super.onNotificationRemoved(sbn);
        LogX.e("Noti removed ");
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.getDefault());
            if( result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                LogX.e("Not support Language!");
            }else{
               // speak("Hello, This is demo of android application speaking notification. Let's check it out!");
            }
        }else{
            LogX.e("Fail to Init!");
        }
    }
    void speak(String text){
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    public String translate(String text) throws Exception{


        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId(getString(R.string.key_name_msd)); //Change this
        Translate.setClientSecret(getString(R.string.key_secret_msd)); //change


        String translatedText = "";

        translatedText = Translate.execute(text, Language.VIETNAMESE);

        return translatedText;
    }

    class AsynTasTranslate extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... params) {
            String textInput = params[0];
            String result="";
            try {
                result= translate(textInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                        TranslateV2 t = new TranslateV2();
//            try {
//                TranslateV2.setKey(getString(R.string.key_api_translate2));
//             TranslateV2.setHttpReferrer("https://www.facebook.com/duongnv1996");
//               result =  t.execute(textInput, Language.ENGLISH,Language.ENGLISH);
//                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//            } catch (GoogleAPIException e) {
//               LogX.e(e.toString());
//            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            LogX.e(s);
            speak(s);
        }
    }
}
