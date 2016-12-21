package com.example.alican.androidsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Model.ChatObservable;
import Model.SaveContactMessage;

/**
 * Created by Alican on 2016-12-15.
 * Good luck, Commander!
 */
public class ChatActivity extends AppCompatActivity implements Observer{
    private Button type, send;
    private EditText content;
    private ListView listView;
    private String messageType, number, name, message;
    private ArrayList<String> samtal, otherMessages;
    private SmsListener listener;
    private SaveContactMessage contactMessage;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.chat_window);

        type = (Button) findViewById(R.id.type);
        send = (Button) findViewById(R.id.send);
        content = (EditText) findViewById(R.id.message);
        listView = (ListView) findViewById(R.id.listView);

        if(savedInstanceState != null) {
            // Restore value of members from saved state
            messageType = savedInstanceState.getString("messageType");
            number = savedInstanceState.getString("number");
            name = savedInstanceState.getString("name");
            content.setText(savedInstanceState.getString("content"));
            samtal = savedInstanceState.getStringArrayList("samtal");
            initListView();
        }//if
        else{
            number = getIntent().getExtras().getString("number");
            name = getIntent().getExtras().getString("name");
            messageType = "SMS";
            samtal = new ArrayList<>();
        }//else

        System.out.println(name + " " + number + " STARTED CHAT WINDOW WITH");
        setTitle(name);

        listener = new SmsListener();
        contactMessage = new SaveContactMessage(this, number);
        otherMessages = new ArrayList<>();
        Thread t1 = new Thread(contactMessage);
        t1.start();

        ObservableObject.getInstance().addObserver(this);
        ChatObservable.getInstance().addObserver(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }//onCreate

    /**
     * sets the type of message to send
     * @param view
     */
    public void changeType(View view){
        if(messageType.equals("SMS")){
            type.setText("MMS");
            messageType = "MMS";
            Toast.makeText(getApplicationContext(), "Switched to MMS", Toast.LENGTH_LONG).show();
        }//if
        else{
            type.setText("SMS");
            messageType = "SMS";
            Toast.makeText(getApplicationContext(), "Switched to SMS", Toast.LENGTH_LONG).show();
        }//else
    }//changeType

    /**
     * onClick event for send message button
     * @param view
     */
    public void sendMessage(View view){
        if(content.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_LONG).show();
        }//if
        else{
            message = content.getText().toString();
            requestToSendMessage();
            fillListView("You", message);
            content.getText().clear();
        }//else
    }//sendMessage

    /**
     * Requests the phone to send the message
     * https://www.tutorialspoint.com/android/android_sending_sms.htm
     */
    private void requestToSendMessage(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                //something
                System.out.println("request permission");
            }//if
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }//else
        }//if
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                //something
                System.out.println("request permission");
            }//if
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }//else
        }//else
    }//sendSMSMessage

    /**
     * If the permission result is positive, it will send the message
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                    System.out.println("sent to " + number + " " + message);
                }//if
                else{
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                }//else
            }//case
        }//switch
    }//onRequest

    /**
     * fills the view with messages sent and received
     * @param from
     * @param message
     */
    public void fillListView(String from, String message){
        samtal.add(from + ": " + message);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, samtal);
        listView.setAdapter(arrayAdapter);
    }//fill

    /**
     * reachable method for observer and broadcastreceiver to update the view
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if(o instanceof String){
            String tmp = (String) o;
            System.out.println("tmp: " + tmp);
            String[] splitted = tmp.split("%");
            System.out.println("upd: " + splitted[0] + " " + splitted[1] + " numb: " + number);
            if(splitted[0].equals(number))
                fillListView(name, splitted[1]);
            else{
                System.out.println("message is from someone else");
                otherMessages.add(splitted[0] + ": " + splitted[1]);
            }
        }
        else if(o instanceof ArrayList){
            samtal = (ArrayList<String>) o;
            initListView();
        }
    }//update

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("messageType", messageType);
        savedInstanceState.putString("number", number);
        savedInstanceState.putString("name", name);
        savedInstanceState.putString("content", content.getText().toString());
        savedInstanceState.putStringArrayList("samtal", samtal);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }//onSaveInstanceState

    private void initListView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, samtal);
        listView.setAdapter(arrayAdapter);
    }//init

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//options

    @Override
    public void onStop(){
        super.onStop();
        contactMessage = new SaveContactMessage(samtal, this, number);
        Thread t1 = new Thread(contactMessage);
        t1.start();
    }
}//class
