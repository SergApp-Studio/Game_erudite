package com.example.game_erudite.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.game_erudite.R;
import com.example.game_erudite.Data.Questions_Loader;
import com.example.game_erudite.constants.Constants;
import com.example.game_erudite.dialogs.DialogTA;
import com.example.game_erudite.dialogs.Dialog_GameOver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GamePage extends AppCompatActivity
        implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener {



    private Button btnBack;
    private Button btnСheck;
    private Button btnAnswer1,btnAnswer2,btnAnswer3,btnAnswer4;
    private Button btnTimer;



    private TextView tvQuestion;
    private TextView tvScore;

    private ImageView ivLifes;

    private String userAnswer;
    private String answer_correct;
    private String category,complexity;

    //максимальная серия правильных ответов подряд
    private int max_series = 0;
    //максимальная серия для легкого режима
    int max_series_itsGame = 0;
    //общий счет
    private int gameScore = 0;
    private int lifes = 3;

    private Questions_Loader questions_loader;

    private SharedPreferences sharPref_score;

    private SoundPool soundPool;
    private int spClicButton;
    private int spCorectAnswer;
    private int spGameOver;
    private int spTimeOut;

    private Async_Timer asyncTimer;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_game);


        questions_loader = new Questions_Loader();
        sharPref_score = getSharedPreferences(Constants.PREFERENCES_SCORE, Context.MODE_PRIVATE);
        initViews();
        initListeners();
        initSoundPool();

        btnСheck.setEnabled(false);
        putDataFromSA();
        setComplexity();
        setDefaultValues();
        setQuestion();
    }




    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnСheck = findViewById(R.id.btnСheck);

        ivLifes = findViewById(R.id.ivLifes);

        btnTimer = findViewById(R.id.btnTimer);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);


        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
    }

    private void initListeners() {
        btnBack.setOnClickListener(this);
        btnСheck.setOnClickListener(this);
        btnTimer.setOnClickListener(this);

        btnAnswer1.setOnClickListener(this);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3.setOnClickListener(this);
        btnAnswer4.setOnClickListener(this);
    }
    private void initSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(this);
        spClicButton = soundPool.load(this,R.raw.clic_button,0);
        spCorectAnswer = soundPool.load(this,R.raw.corect_answer,1);
        spGameOver = soundPool.load(this,R.raw.game_over,1);
        spTimeOut = soundPool.load(this,R.raw.time_out,1);
    }
    private void setDefaultValues() {
        if(complexity.equals(Constants.COMPLEXITY_LIGHT)){
            ivLifes.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                soundPool.play(spClicButton,1,1,1,0,1);
                max_series = max_series_itsGame;
                saveScoreInShPr();
                createDialogExit();
                break;

            case R.id.btnAnswer1:
                actionsBtn1();
                break;

            case R.id.btnAnswer2:
                actionsBtn2();
                break;

            case R.id.btnAnswer3:
                actionsBtn3();
                break;

            case R.id.btnAnswer4:
                actionsBtn4();
                break;

            case R.id.btnСheck:
                btnСheckLogics();
                break ;
        }

    }


    private void putDataFromSA() {
        Bundle data = getIntent().getExtras();
        if(data != null){
            category = data.getString("category");
            complexity = data.getString("complexity");
        }
    }

    public void setQuestion() {

        //Получаем вопрос одной строкой и делим его на елементы
        List strings = new LinkedList();
        String string_quess;
        string_quess = questions_loader.getQuestion(getApplicationContext());

        if(string_quess.equals(Constants.END_QUESTION)){
            // Закончились вопросы в базе
            btnTimer.setVisibility(View.INVISIBLE);
            setEnButton();
            tvQuestion.setText(string_quess);
            saveScoreInShPr();
        }else {

            Log.d(Constants.MY_LOG, string_quess);

            for (String number : string_quess.split("&")) {
                strings.add(number); // заполняем Линкед лист
            }

            // Визуальная настройка кнопок
            btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_static));
            btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_static));
            btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_static));
            btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_static));

            setQuesOnScrean(strings);

            btnAnswer1.setEnabled(true);
            btnAnswer2.setEnabled(true);
            btnAnswer3.setEnabled(true);
            btnAnswer4.setEnabled(true);
        }

    }

    private void setQuesOnScrean(List strings) {
        // Установка вопроса на экран, и вариантов ответа по кнопкам
        tvQuestion.setText(String.valueOf(strings.get(0)));//в 0 ячейке всегда вопрос

        //
        //Алгоритм случайного расположения вариантов ответов по кнопкам
        //
        Random random = new Random();
        int temp_number = 0;
        int random_number = 0;
        ArrayList<Integer> numbers = new ArrayList<>();

        //  первый вариант ответа
        random_number = random.nextInt(4)+1;
        btnAnswer1.setText(String.valueOf(strings.get(random_number)));
        numbers.add(random_number);

        //  второй вариант ответа
        while(temp_number == 0){
            random_number = random.nextInt(4)+1;
            for (int i = 0; i < numbers.size(); i++) {
                if(numbers.get(i)==random_number){
                    break;
                }
                else if (i == (numbers.size()-1) && numbers.get(i)!= random_number){
                    temp_number = random_number;
                    numbers.add(temp_number);
                    btnAnswer2.setText(String.valueOf(strings.get(temp_number)));
                }
            }
        }
        // третий вариант ответа
        temp_number = 0;
        while(temp_number == 0){
            random_number = random.nextInt(4)+1;
            for (int i = 0; i < numbers.size(); i++) {
                if(numbers.get(i)==random_number){
                    break;
                }
                else if (i == (numbers.size()-1) && numbers.get(i)!= random_number){
                    temp_number = random_number;
                    numbers.add(temp_number);
                    btnAnswer3.setText(String.valueOf(strings.get(temp_number)));
                }
            }
        }

        // четвертый вариант ответа
        temp_number = 0;
        while(temp_number == 0){
            random_number = random.nextInt(4)+1;
            for (int i = 0; i < numbers.size(); i++) {
                if(numbers.get(i)==random_number){
                    break;
                }
                else if (i == (numbers.size()-1) && numbers.get(i)!= random_number){
                    temp_number = random_number;
                    numbers.add(temp_number);
                    btnAnswer4.setText(String.valueOf(strings.get(temp_number)));
                }
            }
        }
        answer_correct = String.valueOf(strings.get(5));// в 5 ячейке всегда правильный ответ
    }

    private void setComplexity() {
        if(complexity.equals(Constants.COMPLEXITY_HARD)) {
            btnTimer.setText("GO");
            startTimer();
        }else {
                btnTimer.setText("∞");
        }
    }

    private void startTimer() {
        asyncTimer = new Async_Timer();
        asyncTimer.execute();
    }





    private void actionsBtn1() {
        soundPool.play(spClicButton,1,1,1,0,1);
        btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_static));

        btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_press));
        userAnswer = btnAnswer1.getText().toString();
        btnСheck.setEnabled(true);
    }

    private void actionsBtn2() {
        soundPool.play(spClicButton,1,1,1,0,1);
        btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_static));

        btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_press));
        userAnswer = btnAnswer2.getText().toString();
        btnСheck.setEnabled(true);
    }

    private void actionsBtn3() {
        soundPool.play(spClicButton,1,1,1,0,1);
        btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_static));

        btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_press));
        userAnswer = btnAnswer3.getText().toString();
        btnСheck.setEnabled(true);
    }

    private void actionsBtn4() {
        soundPool.play(spClicButton,1,1,1,0,1);
        btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_static));
        btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_static));

        btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_press));
        userAnswer = btnAnswer4.getText().toString();
        btnСheck.setEnabled(true);
    }


    private void btnСheckLogics() {
        soundPool.play(spClicButton,1,1,1,0,1);
        if(complexity.equals(Constants.COMPLEXITY_HARD)){
            asyncTimer.cancel(false);
        }
        setEnButton();
        сheckUserAnswer();
    }

    private void setEnButton() {
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);
        btnСheck.setEnabled(false);
    }

    private void сheckUserAnswer() {
        if(userAnswer.equals(answer_correct)){
            soundPool.play(spCorectAnswer,1,1,1,0,1);
            setColorBtnCorrect();
            userScore_up();
            startAsync();
        }else {
            switch (complexity){
                case Constants.COMPLEXITY_LIGHT:
                    checkLifes();
                    break;
                case Constants.COMPLEXITY_MEDIUM:
                    gameOver();
                    break;
                case Constants.COMPLEXITY_HARD:
                    gameOver();
                    break;
            }

        }
    }


    private void gameOver() {
        soundPool.play(spGameOver,1,1,1,0,1);
        setColorBtnIncorrect();
        saveScoreInShPr();
        // Логика для неправильоного ответа например вычисление баллов и т д.
        createDialogGaOv();
    }

    private void checkLifes() {
        //настройка счетчика правильных ответов
        if(max_series > max_series_itsGame) {
            max_series_itsGame = max_series;
            max_series = 0;
        }
        if(lifes > 1){
            //Работа с дизайном жизней
            switch (lifes){
                case 3:
                    ivLifes.setImageResource(R.drawable.two_lifes);
                    break;
                case 2:
                    ivLifes.setImageResource(R.drawable.one_lifes);
                    break;
            }

            // логика отнятия жизни
            saveScoreInShPr();
            lifes--;
            setColorBtnIncorrect();
            startAsync();
        }else {
            max_series = max_series_itsGame;
            ivLifes.setImageResource(R.drawable.null_lifes);
            Log.d(Constants.MY_LOG, "Жизни закончились");
            gameOver();
        }
    }

    private void setColorBtnCorrect() {
        if (btnAnswer1.getText().toString().equals(userAnswer)){
            btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_corect));
        }else if (btnAnswer2.getText().toString().equals(userAnswer)){
            btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_corect));
        }else if (btnAnswer3.getText().toString().equals(userAnswer)){
            btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_corect));
        }else if (btnAnswer4.getText().toString().equals(userAnswer)){
            btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_corect));
        }
    }
    private void userScore_up() {
        double cof = 1.5;
        gameScore++;
        gameScore = (int)(  (max_series* cof) + gameScore);
        tvScore.setText(String.valueOf(gameScore));
        // подсчет правильных ответов подряд
        max_series+=1;
        max_series_itsGame = max_series;

    }
    public void startAsync() {
        Async_LoadQuestion myAsyncLoadQuestion = new Async_LoadQuestion();
        myAsyncLoadQuestion.execute();
    }


    private void setColorBtnIncorrect() {
        if (btnAnswer1.getText().toString().equals(answer_correct)){
            btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_corect));

            if(btnAnswer2.getText().toString().equals(userAnswer)){
                btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer3.getText().toString().equals(userAnswer)){
                btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer4.getText().toString().equals(userAnswer)){
                btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }


        }else if (btnAnswer2.getText().toString().equals(answer_correct)){
            btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_corect));

            if(btnAnswer1.getText().toString().equals(userAnswer)){
                btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer3.getText().toString().equals(userAnswer)){
                btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer4.getText().toString().equals(userAnswer)){
                btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }


        }else if (btnAnswer3.getText().toString().equals(answer_correct)){
            btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_corect));

            if(btnAnswer1.getText().toString().equals(userAnswer)){
                btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer2.getText().toString().equals(userAnswer)){
                btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer4.getText().toString().equals(userAnswer)){
                btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }


        }else if (btnAnswer4.getText().toString().equals(answer_correct)){
            btnAnswer4.setBackground(this.getResources().getDrawable(R.drawable.button_corect));

            if(btnAnswer1.getText().toString().equals(userAnswer)){
                btnAnswer1.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer2.getText().toString().equals(userAnswer)){
                btnAnswer2.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }else if (btnAnswer3.getText().toString().equals(userAnswer)){
                btnAnswer3.setBackground(this.getResources().getDrawable(R.drawable.button_incorrect));
            }
        }
    }
    private void saveScoreInShPr(){

        String score = sharPref_score.getString(Constants.SCORE,"default");
        if (Integer.parseInt(score) < gameScore) {
            SharedPreferences.Editor editor = sharPref_score.edit();
            editor.putString(Constants.SCORE, String.valueOf(gameScore));
            editor.apply();
        }
        String score_max_series = sharPref_score.getString(Constants.MAX_SERIES,"default");
        if (Integer.parseInt(score_max_series) < max_series) {
            SharedPreferences.Editor editor = sharPref_score.edit();
            editor.putString(Constants.MAX_SERIES, String.valueOf(max_series));
            editor.apply();
        }

    }
    private void createDialogGaOv() {
        FragmentManager manager = getSupportFragmentManager();
        Dialog_GameOver dialog_gameOver = new Dialog_GameOver();
        dialog_gameOver.setScore(String.valueOf(max_series),String.valueOf(gameScore));
        dialog_gameOver.show(manager,"DialogGO");
    }






    private void createDialogExit() {
        FragmentManager manager = getSupportFragmentManager();
        DialogTA dialogTA = new DialogTA();
        dialogTA.show(manager,"DialogTA");
    }
    @Override
    public void onBackPressed() {
        createDialogExit();
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(Constants.MY_LOG, "Сработал onStart  Третей Активити");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Constants.MY_LOG, "Сработал onResume  Третей Активити");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(Constants.MY_LOG, "Сработал onPause  Третей Активити");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(Constants.MY_LOG, "Сработал onStop  Третей Активити");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();

        if(complexity.equals(Constants.COMPLEXITY_HARD)){
        asyncTimer.cancel(false);
        }

        Log.d(Constants.MY_LOG, "Сработал onDestroy  Третей Активити");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(Constants.MY_LOG, "Сработал onRestart  Третей Активити");
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }


    class Async_LoadQuestion extends AsyncTask<Void,Integer,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 1; i >=1 ; i--) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(complexity.equals(Constants.COMPLEXITY_HARD)){
                startTimer();
            }
            setQuestion();
        }
    }


    class Async_Timer extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 20; i>=0; i--){
                try {
                    TimeUnit.SECONDS.sleep(1);
                    Log.d(Constants.MY_LOG,String.valueOf(i));
                    isCancelled();
                    if (isCancelled()) return null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            if (values[0]<=1){
//                btnTimer.setTextColor(getResources().getColor(R.color.color1to0));
//            }
            btnTimer.setText(String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setEnButton();
            soundPool.play(spTimeOut,1,1,1,0,1);
            saveScoreInShPr();
            createDialogGaOv();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}





