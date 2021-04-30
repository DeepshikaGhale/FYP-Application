package com.example.fyp_application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailaddress, signuppassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailaddress = findViewById(R.id.emailAddress);
        signuppassword = findViewById(R.id.password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    public void Clickonme(View view) {
        String email = emailaddress.getText().toString().trim();
        String password = signuppassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill the required index", Toast.LENGTH_SHORT).show();
        }
        else {
            createAccount(email, password);
        }
    }

    private void createAccount(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Toast.makeText(MainActivity.this, "Vayo", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, navigationbar.class));
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(MainActivity.this, "vayena", Toast.LENGTH_SHORT).show();
//                        }
//                        // ...
//                    }
//                });
    }

    public void direct(View view) {
        startActivity(new Intent(MainActivity.this, navigationbar.class));
    }

    public void gotoRegisterActivity(View view) {
        startActivity(new Intent(MainActivity.this, RegsiterActivity.class));
    }

    //TODO: make this main activity a splash screen and then make uncomment this, remember the splashacitvity is supposed to be mainactivity and
    // the one mainacitvity of code is supposed to be navigation bar
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null){
//            Intent registerIntent = new Intent(MainActivity.this, RegsiterActivity.class);
//            startActivity(registerIntent);
//            finish();
//        }else{
//            Intent mainIntent = new Intent(MainActivity.this, navigationbar.class);
//            startActivity(mainIntent);
//            finish();
//        }
//    }
}
