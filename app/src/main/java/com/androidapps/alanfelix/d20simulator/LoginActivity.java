package com.androidapps.alanfelix.d20simulator;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    /* Componentes */
    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView Message;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Pega referencias dos componentes */
        Name = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.email_sign_in_button);
        Message = (TextView)findViewById(R.id.login_message);
        mAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    /* Ao iniciar a activity */
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void createAccount(String userName, String userPassword) {
        mAuth.createUserWithEmailAndPassword(userName, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Usuário criado
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Criação de conta falhou por algum motivo
                            Message.setText(R.string.auth_signup_fail);
                        }
                    }
                });
    }

    private void validate(String userName, String userPassword) {
        if (!(userName.equals("")) && !(userPassword.equals(""))) {
            createAccount(userName, userPassword);
        } else {
            Message.setText(R.string.auth_null);
        }
    }
}

