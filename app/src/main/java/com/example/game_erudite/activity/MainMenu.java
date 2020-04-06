package com.example.game_erudite.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.game_erudite.Data.DataBaseHelper;
import com.example.game_erudite.R;
import com.example.game_erudite.constants.Constants;
import com.example.game_erudite.dialogs.DialogMA;

import java.io.IOException;

public class MainMenu extends AppCompatActivity
        implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener {



    private Button btnStartGame,btnRecords,btnExit;
    private SharedPreferences sharPref_score;

    private SoundPool soundPool;
    private int spClicButton;

    private DataBaseHelper dataBaseHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        createDataBase();
        sharPref_score = getSharedPreferences(Constants.PREFERENCES_SCORE, Context.MODE_PRIVATE);
        initViews();
        initListeners();
        initSoundPool();
    }

    private void createDataBase() {
        dataBaseHelper = new DataBaseHelper(this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }


    private void initViews() {
        btnStartGame = findViewById(R.id.btnStartGame);
        btnRecords = findViewById(R.id.btnRecords);
        btnExit = findViewById(R.id.btnExit);
    }

    private void initListeners() {
        btnStartGame.setOnClickListener(this);
        btnRecords.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    private void initSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(this);
        spClicButton = soundPool.load(this,R.raw.clic_button,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStartGame:
                btnStartGameLogic();
                break;

            case R.id.btnRecords:
                btnRecordsLogic();
                break;

            case R.id.btnExit:
                btnExitLogic();

                break;
        }
    }




    private void btnStartGameLogic() {
        soundPool.play(spClicButton,1,1,1,0,1);
        Intent intent = new Intent(this, CategoryMenu.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void btnRecordsLogic() {
        soundPool.play(spClicButton,1,1,1,0,1);
        Intent intent = new Intent(this, ScoreMenu.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void btnExitLogic() {
        soundPool.play(spClicButton,1,1,1,0,1);
        setDialogMA();
    }


    private void setDialogMA() {
        FragmentManager manager = getSupportFragmentManager();
        DialogMA dialogMA = new DialogMA();
        dialogMA.show(manager,"dialogMA");
    }


    @Override
    public void onBackPressed() {
        setDialogMA();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sharPref_score.getString(Constants.SCORE,"default").equals("default")) {
            SharedPreferences.Editor editor = sharPref_score.edit();
            editor.putString(Constants.SCORE, "0");
            editor.apply();
        }


        Log.d(Constants.MY_LOG, "Сработал onStart  Первой Активити");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Constants.MY_LOG, "Сработал onResume  Первой Активити");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(Constants.MY_LOG, "Сработал onPause  Первой Активити");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(Constants.MY_LOG, "Сработал onStop  Первой Активити");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        Log.d(Constants.MY_LOG, "Сработал onDestroy  Первой Активити");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(Constants.MY_LOG, "Сработал onRestart  Первой Активити");
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
    }


    //soundpool

}
