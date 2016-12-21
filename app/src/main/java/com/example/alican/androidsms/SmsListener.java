package com.example.alican.androidsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * Created by Alican on 2016-12-15.
 * Good luck, Commander!
 */
public class SmsListener extends BroadcastReceiver{
    private String from, message;

    public SmsListener(){
        System.out.println("listener created");
    }//SmsListener

    /**
     * Executed when a message is received to the phone
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Message Received");
        SmsMessage[] received = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if(received.length < 1)
            return;

        System.out.println("message longer than 1");
        SmsMessage smsMessage = received[0];
        from = smsMessage.getOriginatingAddress();
        message = smsMessage.getMessageBody().toString();
        if(from.startsWith("+"))
            from = from.replace("+46", "0");
        else
            System.out.println("can't replace");

        System.out.println("from: " + from + " msg: " + message);
        ObservableObject.getInstance().updateValue(from, message);
    }//onReceive
}//class
