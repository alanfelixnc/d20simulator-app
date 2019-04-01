package com.androidapps.alanfelix.d20simulator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RollsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ListView lv;
    private FirebaseListAdapter adapter;
    private ProgressDialog Loading;

    /* Chama a tela de login */
    private void callLoginScreen() {
        Intent intent = new Intent(RollsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /* Faz signout do app e retorna para a tela de login */
    public void signOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RollsActivity.this);
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
        switch(item.getItemId()) {
            case R.id.logout:
                signOut();
                break;

            case R.id.view_rolls:
                //faznada
                break;

            default:
                //faznada
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolls);

        Loading = new ProgressDialog(this);

        /* TOOLBAR */
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        lv = (ListView) findViewById(R.id.rolls_list);

        Loading.setTitle("Buscando jogadas...");
        Loading.setMessage("Por favor, aguarde.");
        Loading.setCanceledOnTouchOutside(false);
        Loading.show();

        Handler pdCancel = new Handler();
        pdCancel.postDelayed(progressRunnable, 1200);

        populateView();
    }

    /* Popula a listview com dados do Firebase */
    private void populateView() {
        Query query = FirebaseDatabase.getInstance().getReference().child(mAuth.getUid());

        FirebaseListOptions<Rolls> options = new FirebaseListOptions.Builder<Rolls>()
                .setLayout(R.layout.roll)
                .setQuery(query, Rolls.class)
                .build();
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView rValue = v.findViewById(R.id.rollValue);
                TextView rType = v.findViewById(R.id.diceType);
                TextView rDate = v.findViewById(R.id.rollDate);

                Rolls newroll = (Rolls) model;

                String rollVal = getString(R.string.roll_val_title) + ": " + newroll.getRollValue();
                String diceVal = getString(R.string.roll_dice_title) + ": D" + newroll.getDiceType();
                String dateVal = getString(R.string.roll_date_title) + ": " + newroll.getRollDate();
                rValue.setText(rollVal);
                rType.setText(diceVal);
                rDate.setText(dateVal);
            }
        };
        lv.setAdapter(adapter);
    }

    /* Oculta o loading */
    Runnable progressRunnable = new Runnable() {

        @Override
        public void run() {
            Loading.dismiss();
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }
}
