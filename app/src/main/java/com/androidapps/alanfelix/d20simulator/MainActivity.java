package com.androidapps.alanfelix.d20simulator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {

    /* VARIÁVEIS */
    int diceType;
    boolean stopAnim = false;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SimpleDateFormat date;
    private Vibrator deviceVib;

    /* Chama a tela de login */
    private void callLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    /* Faz signout do app e retorna para a tela de login */
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(R.string.logout_message).setCancelable(true)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        callLoginScreen();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = dialog.create();
        alert.setTitle(R.string.logout_title);
        alert.show();
    }

    /* Adiciona o menu na toolbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        /* VIBRAÇÃO */
        deviceVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        /* TOOLBAR */
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* ANIMACOES */
        final Animation diceshake = AnimationUtils.loadAnimation(this, R.anim.diceshake);

        /* COMPONENTES */
        final TextView diceTypeLabel = (TextView) findViewById(R.id.diceTypeLabel);
        final TextView diceValueLabel = (TextView) findViewById(R.id.diceVal);
        final SeekBar diceTypeSeek = (SeekBar) findViewById(R.id.diceTypeScroll);

        /* INICIALIZA A SEEKBAR DO TIPO DO DADO */
        diceTypeSeek.setMax(8);
        diceTypeSeek.setProgress(6);

        diceTypeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                diceType = setDiceType(diceTypeSeek.getProgress());
                diceTypeLabel.setText(getString(R.string.dice_type_label) + " D" + diceType);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /* INICIALIZA A LABEL DO TIPO DO DADO */
        diceType = setDiceType(diceTypeSeek.getProgress());
        diceTypeLabel.setText("Tipo do dado: D" + diceType);

        /* INICIALIZA ROLL DO DADO */
        diceValueLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceValueLabel.startAnimation(diceshake);
                if (Build.VERSION.SDK_INT >= 26) {
                    deviceVib.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    deviceVib.vibrate(150);
                }
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Random random = new Random();
                                int rollResult = random.nextInt(diceType);

                                diceValueLabel.setText(" " + Integer.toString(rollResult + 1));

                                saveRollToDatabase(rollResult + 1, diceType);
                            }
                        }, 150);
            }
        });
    }

    /* Salva a jogada feita no banco de dados */
    private void saveRollToDatabase(int rollResult, int diceValue) {
        String userKey = mAuth.getUid();
        String rollId = mDatabase.push().getKey();
        String nowDate = date.format(new Date());
        Rolls newRoll = new Rolls(rollId, diceValue, rollResult, nowDate);

        mDatabase.child(userKey).child(rollId).setValue(newRoll);
    }

    private int setDiceType(int value) {
        int diceValue;

        switch(value) {
            case 0:
                diceValue = 4;
                break;
            case 1:
                diceValue = 6;
                break;
            case 2:
                diceValue = 8;
                break;
            case 3:
                diceValue = 10;
                break;
            case 4:
                diceValue = 12;
                break;
            case 5:
                diceValue = 16;
                break;
            case 6:
                diceValue = 20;
                break;
            case 7:
                diceValue = 30;
                break;
            case 8:
                diceValue = 100;
                break;
            default:
                diceValue = 6;
        }
        return diceValue;
    }
}
