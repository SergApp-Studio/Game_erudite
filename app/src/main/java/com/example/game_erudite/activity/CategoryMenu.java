package com.example.game_erudite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.game_erudite.R;
import com.example.game_erudite.constants.Constants;

import java.util.ArrayList;

public class CategoryMenu extends AppCompatActivity
        implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener{




    private Button btnBackInMA;
    private Button btnStart;

    private ImageButton imgBtnCategoryL,imgBtnCategoryR;
    private ImageButton imgBtnComplexityL,imgBtnComplexityR;

    private TextView tvCategory;
    private TextView tvСomplexity;
    private TextView tvMessageComplexity;

    // Счетчики для категорий и сложности, для работы с элементами из массива

    private ArrayList<String> categoryes;
    private ArrayList <String> complexityes;
    private ArrayList<String> messageForComplexity;

    private int counterCategory = 0;
    private int counterComplexity = 0;
    private String category,complexity;

    private SoundPool soundPool;
    private int spClicButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_category);

        Log.d(Constants.MY_LOG, "Сработал onCreate  Второй Активити");

        initVievs();
        initListeners();
        initSoundPool();
        initArrays();
        setDefaultValues();


    }


    private void initVievs() {
        btnBackInMA = findViewById(R.id.btnBackInMA);

        btnStart = findViewById(R.id.btnStart);

        imgBtnCategoryL = findViewById(R.id.imgBtnCategoryL);
        imgBtnCategoryR = findViewById(R.id.imgBtnCategoryR);
        tvCategory = findViewById(R.id.tvCategory);

        imgBtnComplexityL = findViewById(R.id.imgBtnComplexityL);
        imgBtnComplexityR = findViewById(R.id.imgBtnComplexityR);
        tvСomplexity = findViewById(R.id.tvСomplexity);
        tvMessageComplexity = findViewById(R.id.tvMessageComplexity);
    }

    private void initListeners() {
        btnBackInMA.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        imgBtnCategoryL.setOnClickListener(this);
        imgBtnCategoryR.setOnClickListener(this);
        imgBtnComplexityL.setOnClickListener(this);
        imgBtnComplexityR.setOnClickListener(this);
    }

    private void initSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(this);
        spClicButton = soundPool.load(this,R.raw.clic_button,1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBackInMA:
                soundPool.play(spClicButton,1,1,1,0,1);
                backToMainActivity();
                break;

            case R.id.btnStart:
                btnStartLogics();
                break;

            case R.id.imgBtnCategoryL:
                btnLeft_CategoryLogics();
                break;

            case R.id.imgBtnCategoryR:
                btnRight_CategoryLogics();
                break;

            case R.id.imgBtnComplexityL:
                btnLeft_ComplxLogics();
                break;

            case R.id.imgBtnComplexityR:
                btnRight_ComplxLogics();
                break;
        }
    }







    // Логика Кнопок выбора категорий и сложности

    private void btnStartLogics() {
        soundPool.play(spClicButton,1,1,1,0,1);
        category = categoryes.get(counterCategory);
        complexity = complexityes.get(counterComplexity);
        Intent intent = new Intent(this, GamePage.class);
        intent.putExtra("category",category);
        intent.putExtra("complexity",complexity);
        startActivity(intent);
    }

    private void btnLeft_CategoryLogics() {
        soundPool.play(spClicButton,1,1,1,0,1);
        if (counterCategory > 0){
            counterCategory-=1;
        tvCategory.setText(categoryes.get(counterCategory));}

        if (counterCategory == 0 ){
            imgBtnCategoryL.setVisibility(View.INVISIBLE);
        }
        if(counterCategory != (categoryes.size()-1)){
            imgBtnCategoryR.setVisibility(View.VISIBLE);
        }
    }

    private void btnRight_CategoryLogics() {
        soundPool.play(spClicButton,1,1,1,0,1);
        if(counterCategory != (categoryes.size()-1)){
            counterCategory+=1;
            tvCategory.setText(categoryes.get(counterCategory));}

        if (counterCategory > 0 ){
            imgBtnCategoryL.setVisibility(View.VISIBLE);
        }

        if(counterCategory == (categoryes.size()-1)){
            imgBtnCategoryR.setVisibility(View.INVISIBLE);

        }
    }

    private void btnLeft_ComplxLogics() {
        soundPool.play(spClicButton,1,1,1,0,1);
        if (counterComplexity > 0){
            counterComplexity-=1;
            tvСomplexity.setText(complexityes.get(counterComplexity));
            tvMessageComplexity.setText(messageForComplexity.get(counterComplexity));}

        if (counterComplexity == 0 ){
            imgBtnComplexityL.setVisibility(View.INVISIBLE);;
        }
        if(counterComplexity != (complexityes.size()-1)){
            imgBtnComplexityR.setVisibility(View.VISIBLE);;
        }
    }

    private void btnRight_ComplxLogics() {
        soundPool.play(spClicButton,1,1,1,0,1);
        if(counterComplexity != (complexityes.size()-1)){
            counterComplexity+=1;
            tvСomplexity.setText(complexityes.get(counterComplexity));
            tvMessageComplexity.setText(messageForComplexity.get(counterComplexity));}

        if (counterComplexity > 0 ){
            imgBtnComplexityL.setVisibility(View.VISIBLE);
        }

        if(counterComplexity == (complexityes.size()-1)){
            imgBtnComplexityR.setVisibility(View.INVISIBLE);
        }
    }


    //стандартные значения по умолчанию сначала в категориях стоят первые елементы
    private void setDefaultValues(){
        tvCategory.setText(categoryes.get(counterCategory));
        tvСomplexity.setText(complexityes.get(counterComplexity));

            imgBtnCategoryL.setVisibility(View.INVISIBLE);
            imgBtnComplexityL.setVisibility(View.INVISIBLE);

            tvMessageComplexity.setText(messageForComplexity.get(counterComplexity));


    }





//Категории и сложность
    private void initArrays() {
        categoryes = new ArrayList<>();

        categoryes.add("Случайные вопросы");
        categoryes.add("Природа");
        categoryes.add("География");
        categoryes.add("Фильмы");
        categoryes.add("Спорт");
        categoryes.add("Авто");
        categoryes.add("Творчество");


        complexityes = new ArrayList<>();

        complexityes.add(Constants.COMPLEXITY_LIGHT);
        complexityes.add(Constants.COMPLEXITY_HARD);



        messageForComplexity = new ArrayList<>();

        messageForComplexity.add("игра без учёта времени, количество возможных ошибок - 3 ");
        //messageForComplexity.add("игра без учёта времени, права на ошибку нет ");
        messageForComplexity.add("игра с учётом времени,права на ошибку нет,на ответ есть 20 секунд");
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

        Log.d(Constants.MY_LOG, "Сработал onStart  Второй Активити");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Constants.MY_LOG, "Сработал onResume  Второй Активити");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(Constants.MY_LOG, "Сработал onPause  Второй Активити");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(Constants.MY_LOG, "Сработал onStop  Второй Активити");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        Log.d(Constants.MY_LOG, "Сработал onDestroy  Второй Активити");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(Constants.MY_LOG, "Сработал onRestart  Второй Активити");
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
