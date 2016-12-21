package com.example.alican.androidsms;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import Model.Contact;

/**
 * Created by Alican on 2016-12-15.
 * Good luck, Commander!
 */
public class IndexActivity extends AppCompatActivity{
    private EditText phoneNumber;
    private Spinner phoneList;
    private Button chat;
    private ArrayList<Contact> contacts;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_window);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        phoneList = (Spinner) findViewById(R.id.contactList);
        chat = (Button) findViewById(R.id.start);
        contacts = new ArrayList<>();
        fillSpinner();
    }//onCreate

    /**
     * Starts a new activity and view to chat
     * @param view
     */
    public void startChat(View view){
        System.out.println("startChat");
        if(phoneNumber.getText().toString().equals(null) && phoneList.getSelectedItem() == null)
            Toast.makeText(getApplicationContext(), "Select or enter a phone number", Toast.LENGTH_LONG).show();
        else if(!phoneNumber.getText().toString().isEmpty()){
            System.out.println("1 else");
            phoneNumber.getText().toString();
            System.out.println(phoneNumber.getText().toString());

            //change to chat window
            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(i);
        }//else if
        else if(phoneList.getSelectedItem() != null){
            System.out.println("2 else");
            phoneList.getSelectedItem().toString();
            Contact con = contacts.get(phoneList.getSelectedItemPosition());
            System.out.println(con.getName() + " " + con.getNumber());

            //change to chat window
            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
            i.putExtra("number", con.getNumber());
            i.putExtra("name", con.getName());
            startActivity(i);
            //finish();
        }//else if
    }//startChat

    /**
     * requests permission to read the users contact list and fills the spinner
     * with the gathered data
     */
    private void fillSpinner(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                //something
            }//if
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }//else
        }//if
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                //something
            }//if
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }//else
        }//else

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(name + " " + phoneNumber);
            contacts.add(new Contact(phoneNumber, name));
        }//while
        phones.close();

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < contacts.size(); i++)
            list.add(contacts.get(i).getName());

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneList.setAdapter(adapter2);
    }//fillSpinner

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }
}//class
