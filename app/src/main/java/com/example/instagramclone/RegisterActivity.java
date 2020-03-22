package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{

    EditText userName;
    EditText fullName;
    EditText email;
    EditText password;
    Button register;
    TextView textLogin;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // connect variables with UI nodes
        userName = findViewById(R.id.username);
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        textLogin = findViewById(R.id.textLogin);



        auth = FirebaseAuth.getInstance();



        textLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });




        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();


                String userNameAsString = userName.getText().toString();
                String fullNameAsString = fullName.getText().toString();
                String emailAsString = email.getText().toString();
                String passwordAsString = password.getText().toString();






                if (TextUtils.isEmpty(userNameAsString) ||
                    TextUtils.isEmpty(fullNameAsString) ||
                    TextUtils.isEmpty(emailAsString) ||
                    TextUtils.isEmpty(passwordAsString))
                {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else if (passwordAsString.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register(userNameAsString, fullNameAsString, emailAsString, passwordAsString);

                }


            }
        });


    }







    private void register(final String userName, final String fullName, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);


                            HashMap<String, Object> hashMap = new HashMap<>();

                            hashMap.put("id", userid);
                            hashMap.put("userName", userName.toLowerCase());
                            hashMap.put("fullName", fullName);
                            hashMap.put("bio", "");
                            hashMap.put("imageURL", "https://firebasestorage.googleapis.com/v0/b/instagram-clone-ea6ff.appspot.com/o/placeholder.png?alt=media&token=68bc014d-29e3-49fa-96db-eb9c642cf034");







                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();


                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


                                        startActivity(intent);

                                    }
                                }
                            });
                        }
                        else
                        {
                            progressDialog.dismiss();

                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
    }




}
