package com.duongkk.speakingnotification.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;

import com.duongkk.speakingnotification.MainActivity;
import com.duongkk.speakingnotification.R;
import com.duongkk.speakingnotification.Utils.LogX;


/**
 * Created by DuongKK on 4/14/2016.
 */
public class SmsReceiver extends BroadcastReceiver {
    int notificationID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

        } else {

//
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, "date desc limit 1");
//        int indexPhone = cursor.getColumnIndex("address");
//        int indexContent = cursor.getColumnIndex("body");
//        if(indexContent <0 ){
//            return;
//        }else {
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
////            do {
//                // phan tich tung sms
//                String phoneNumber = cursor.getString(indexPhone);
//                String contentSms = cursor.getString(indexContent);
//                LogX.e("SMS ----> " + phoneNumber + " : " + contentSms);
//                    // loc sdt trong danh ba
//                    String contact = phoneNumber;
//                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
//                    Cursor cs = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, ContactsContract.PhoneLookup.NUMBER + "='" + phoneNumber + "'", null, null);
//                    if (cs.getCount() > 0) {
//                        cs.moveToFirst();
//                        contact = cs.getString(cs.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//                    }
//                if(contentSms.contains(Constants.API_ROOT))
//                    showNotification(context, contact, contentSms);
//                }
//                //  SystemClock.sleep(100); //sleep
//                //  }while (cursor.moveToNext());
//            }


            //test 2
//        Bundle bundle=intent.getExtras();
//        Object[] messages=(Object[])bundle.get("pdus");
//        SmsMessage[] sms=new SmsMessage[messages.length];
//        for(int n=0;n<messages.length;n++){
//            sms[n]=SmsMessage.createFromPdu((byte[]) messages[n]);
//        }
//        for(SmsMessage msg:sms) {
//            if(msg.getMessageBody().contains("http://guidiadiem.com")){
//                Toast.makeText(context,"Da nhan duoc tin nhan moi !",Toast.LENGTH_LONG).show();
//                showNotification(context,msg.getOriginatingAddress(),msg.getMessageBody());
//            }
//        }

            // test 3
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {
                    showNotification(context, "âs", "aaaaa");

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        // loc sdt trong danh ba
                    String contact = phoneNumber;
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                    Cursor cs = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, ContactsContract.PhoneLookup.NUMBER + "='" + phoneNumber + "'", null, null);
                    if (cs.getCount() > 0) {
                        cs.moveToFirst();
                        contact = cs.getString(cs.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    }
                        String message = currentMessage.getDisplayMessageBody();

                        LogX.e("SmsReceiver", "senderNum: " + contact + "; message: " + message);


                        // Show alert
                        //int duration = Toast.LENGTH_LONG;
//                        Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
//                        toast.show();
//                        if (message.contains("http://guidiadiem.com")) {

                            showNotification(context, contact, message);
                       // }
                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                LogX.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }
    }


    public void showNotification(Context context, String number, String content) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(context.getString(R.string.title_notification) + number);
        // builder.setStyle(NotificationCompat.InboxStyle)
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("OK", "OK");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }
}
