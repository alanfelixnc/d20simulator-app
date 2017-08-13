package com.androidapps.alanfelix.d20simulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /* VARIÁVEIS */
    int diceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* COMPONENTES */
        Button diceRollButton = (Button) findViewById(R.id.diceRollButton);
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

        /* INICIALIZA BOTÃO */
        diceRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int rollResult = random.nextInt(diceType);

                diceValueLabel.setText(" " + Integer.toString(rollResult + 1));
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
