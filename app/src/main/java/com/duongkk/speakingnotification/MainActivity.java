package com.duongkk.speakingnotification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.duongkk.speakingnotification.Utils.LogX;
import com.duongkk.speakingnotification.service.NotificationService;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private  TextView mTv;
    TextToSpeech tts;
    String pack;
    String title;
    String text;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             pack = intent.getStringExtra("package");
             title = intent.getStringExtra("title");
             text = intent.getStringExtra("text");
            AsynTasTranslate asynTasTranslate= new AsynTasTranslate();
          asynTasTranslate.execute(text);
            speak(text);
//            TranslateV2 t = new TranslateV2();
//            try {
//                TranslateV2.setKey(getString(R.string.key_api_translate));
//             TranslateV2.setHttpReferrer("http://android-er.blogspot.com/");
//               String result =  t.execute("Hello", Language.ENGLISH,Language.CHINESE);
//                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//            } catch (GoogleAPIException e) {
//                e.printStackTrace();
//            }

            mTv.setText(pack + " - " + title + "  -  "+text);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = (TextView)findViewById(R.id.tv);
        tts =new TextToSpeech(this,this);
        startService(new Intent(this, NotificationService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter("Msg"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        tts.shutdown();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.ENGLISH);
            if( result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                LogX.e("Not support Language!");
            }else{
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

        translatedText = Translate.execute(text, Language.ENGLISH);

        return translatedText;
    }

    class AsynTasTranslate extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... params) {
            String textInput = params[0];
            String result="";
            try {
                result= translate(textInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
