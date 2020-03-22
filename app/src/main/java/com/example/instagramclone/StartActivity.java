package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity
{

    Button login;
    Button register;

    FirebaseUser firebaseUser;




    // onCreate occurs before onStart


    // to start a new activity (GUI) when a button in another activity is clicked
    // Intent myIntent = new Intent(currentActivity.this, NextActivity.class)
    // CurrentActivity.this.startActivity(myIntent)








    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);




        login = findViewById(R.id.login);
        register = findViewById(R.id.register);




        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));

            }
        });



        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));

            }
        });



    }



    @Override
    protected void onStart()
    {
        super.onStart();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // redirect if user is not null
        if (firebaseUser != null)
        {
            startActivity(new Intent(StartActivity.this, MainActivity.class));

            finish();
        }
    }

}
