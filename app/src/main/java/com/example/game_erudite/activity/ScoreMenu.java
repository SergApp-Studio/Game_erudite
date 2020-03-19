package com.example.game_erudite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.game_erudite.R;
import com.example.game_erudite.constants.Constants;


public class ScoreMenu extends AppCompatActivity
        implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener{

    private Button btnBackInMA;
    private TextView tvMaxRecords;
    private String max_score;
    private String all_score;
    private String score;
    private SharedPreferences sharPref_score;

    private SoundPool soundPool;
    private int spClicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_score);



        tvMaxRecords = findViewById(R.id.tvMaxRecords);
        btnBackInMA = findViewById(R.id.btnBackInMA);
        btnBackInMA.setOnClickListener(this);
        initSoundPool();
        sharPref_score = getSharedPreferences(Constants.PREFERENCES_SCORE, Context.MODE_PRIVATE);

        setFildRecords();

    }

    private void initSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(this);
        spClicButton = soundPool.load(this,R.raw.clic_button,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBackInMA:
                soundPool.play(spClicButton,1,1,1,0,1);
                backToMainActivity();
                break;
        }
    }

    private void setFildRecords(){
        String score = sharPref_score.getString(Constants.SCORE,"default");
        tvMaxRecords.setText(score);
    }





    private void backToMainActivity() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(Constants.MY_LOG, "Сработал onStart   Активити Рекордов");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Constants.MY_LOG, "Сработал onResume  Активити Рекордов");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(Constants.MY_LOG, "Сработал onPause  Активити Рекордов");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(Constants.MY_LOG, "Сработал onStop  Активити Рекордов");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        Log.d(Constants.MY_LOG, "Сработал onDestroy  Активити Рекордов");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(Constants.MY_LOG, "Сработал onRestart  Активити Рекордов");
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
