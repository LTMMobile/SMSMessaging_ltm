package com.ltm.messaging;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSMessagingActivity extends Activity {
    Button btnSendSMS_methode1 = null;
    Button btnSendSMS_methode2 = null;
    EditText txtTelNo = null;
    EditText txtMessage = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
 
        btnSendSMS_methode1 = (Button) findViewById(R.id.btnSendSMS);
        btnSendSMS_methode2 = (Button) findViewById(R.id.btnSendSMS_2);
        txtTelNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
 
        btnSendSMS_methode1.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                String phoneNo = txtTelNo.getText().toString();
                String message = txtMessage.getText().toString();                 
                if (phoneNo.length()>0 && message.length()>0)                
                    sendSMS1(phoneNo, message);                
                else
                    Toast.makeText(getBaseContext(), "Svp entrez le numéro et le message", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        btnSendSMS_methode2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) 
            {                
                String phoneNo = txtTelNo.getText().toString();
                String message = txtMessage.getText().toString();                 
                if (phoneNo.length()>0 && message.length()>0)                
                    sendSMS2(phoneNo, message);                
                else
                    Toast.makeText(getBaseContext(), "Svp entrez le numéro et le message", 
                        Toast.LENGTH_SHORT).show();
            }
        });
    } 
    
    //---sends an SMS message to another device--- Method 1
    private void sendSMS1(String telNumero, String message) {    
    	Log.v("LTM", "methode_1" );   
        
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telNumero, null, message, null, null);        
    }  
    
    //---sends an SMS message to another device--- Method 2
    private void sendSMS2(String telNumero, String message)
    {        
        String SENT = "com.ltm.messaging.SMS_SENT";
        String DELIVERED = "com.ltm.messaging.SMS_DELIVERED";
    	Log.v("LTM", "methode_2" );
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
 
        //---En cas de SMS envoyé---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS envoyé", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Pas de service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "PDU est à null", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter( SENT ));
 
        //---En cas de SMS ---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS reçu", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS non reçu", Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter( DELIVERED ));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telNumero, null, message, sentPI, deliveredPI);        
    }
}