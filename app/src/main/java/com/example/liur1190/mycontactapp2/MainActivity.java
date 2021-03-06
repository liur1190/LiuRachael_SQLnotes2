package com.example.liur1190.mycontactapp2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    DatabaseHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_phone);
        editAddress = findViewById(R.id.editText_address);


        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper");

    }

    public void addData(View view) {
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString());

        if(isInserted==true) {
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Failed - contact not inserted", Toast.LENGTH_LONG).show();
        }
    }
    public void viewData(View view)
    {
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor" + res.getCount());
        if(res.getCount()==0)
        {
            showMessage("Error", "No data found in database");
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()) {
            //Append res column 0,1,2,3 tp the buffer
            buffer.append(res.getString(0)+ "\n" + res.getString(1)+ "\n" + res.getString(2)+ "\n" + res.getString(3) + "\n");
        }
        Log.d("MyContactApp", "MainActivity: viewData: assembled stringbuffer");
        showMessage("Data", buffer.toString());





    }
    public void showMessage(String title, String message){
        Log.d("MyContactApp", "MainActivity: showMessage: building alert dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
    public static final String EXTRA_MESSAGE = "com.example.liur1190.mycontactapp2.MESSAGE";



    public void searchRecord(View view){
        Log.d("MyContactApp", "MainActivity: launching SearchActivity");
        Intent intent = new Intent(this, SearchActivity.class);

        //creating the StringBuffer to search
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: SearchRecord: received cursor");

        if (res.getCount() == 0){
            showMessage("Error", "No data found in database");
            Log.d("MyContactApp", "MainActivity: SearchRecord: no data in database");
            return;
        }

        StringBuffer buffer1 = new StringBuffer();

        boolean isTrue = false;
        while (res.moveToNext()) {
            if (res.getString(1).equals(editName.getText().toString())) {
                isTrue = true;
                buffer1.append(res.getString(1) + "\n");
                buffer1.append(res.getString(2) + "\n");
                buffer1.append(res.getString(3) + "\n" + "\n");
            }
        }
        if (isTrue == false){
            buffer1.append("Entry does not exist");
        }

        Log.d("MyContactApp", "MainActivity: SearchRecord: created StringBuffer");

        intent.putExtra(EXTRA_MESSAGE, buffer1.toString());
        startActivity(intent);
    }
}
