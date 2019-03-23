package com.androidapps.alanfelix.d20simulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar;

import java.util.Random;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {

    /* VARIÁVEIS */
    int diceType;
    boolean stopAnim = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Random random = new Random();
                                int rollResult = random.nextInt(diceType);

                                diceValueLabel.setText(" " + Integer.toString(rollResult + 1));
                            }
                        }, 150);
            }
        });
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
