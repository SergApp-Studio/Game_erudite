package com.example.game_erudite.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.game_erudite.constants.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
@AllArgsConstructor

public class Questions_Loader {

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private ArrayList <String> questions = new ArrayList<>();
    private ArrayList <Integer> questions_id = new ArrayList<>();
    Random random = new Random();




    public Questions_Loader() {
    }




    public String getQuestion(Context context){

        int random_id = 0;
        String string_question = "";
        // подключаемся к базе данных
        dataBaseHelper = new DataBaseHelper(context);

        database = dataBaseHelper.getWritableDatabase();
        //
        // Задаем первый вопрос с базы данных, записываем его айди в массив номеров, для дальнейшего несовпадания вопросов
        //
        if (questions_id.size() == 0){
            // Случайным образом выбираем  id вопроса
            random_id = random.nextInt(Constants.TOTAL_ID)+1;
            // Получаем вопрос
            cursor = database.rawQuery("SELECT * FROM questions", null);
            //Сравниваем данные и ищем совпадение со случайным id
            int cursor_now; // іd на котором в данный момент находится курсор
            while (cursor.moveToNext()){
                cursor_now = cursor.getInt(cursor.getColumnIndex("_id"));
                if (cursor_now == random_id ){
                    string_question = cursor.getString(cursor.getColumnIndex("question"));
                    questions_id.add(random_id);
                }
            }
        }
        else if (questions_id.size() == Constants.TOTAL_ID){
            //
            // Проверяем не закончились ли вопросы , для избежания ошибок
            //
            string_question = Constants.END_QUESTION;
            Log.d(Constants.MY_LOG, "вопросы закончились");

        }else {
            //
            // Процес проверки одинаковых вопросов
            //
            while (string_question.equals("")){
                // Случайным образом выбираем  id вопроса
                random_id = random.nextInt(Constants.TOTAL_ID)+1;
                for (int i = 0; i < questions_id.size(); i++)
                {
                    if (questions_id.get(i) == random_id)
                    {
                        Log.d(Constants.MY_LOG, "Повтор, запрос нового id");
                        break ;
                    }
                    else if (i == (questions_id.size()-1) && questions_id.get(i)!= random_id)
                    {
                        // Получаем вопрос
                        cursor = database.rawQuery("SELECT * FROM questions", null);
                        //Сравниваем данные и ищем совпадение со случайным id
                        int cursor_now; // іd на котором в данный момент находится курсор
                        while (cursor.moveToNext()){
                            cursor_now = cursor.getInt(cursor.getColumnIndex("_id"));
                            if (cursor_now == random_id ){
                                string_question = cursor.getString(cursor.getColumnIndex("question"));
                                questions_id.add(random_id);
                            }
                        }
                    }
                }
            }
        }
        // Возвращаем вопрос строкой
        return string_question ;
    }




    //
    // Старый вариант
    //


//    public String getQuestion(){
//
//        if (questions.size() == 0){
//            initQuestion();
//        }
//        int temp = 0;
//        String string_question = "";
//
//        //
//        // Задаем первый вопрос, записываем его номер в массив номеров, для дальнейшего несовпадания вопросов
//        //
//        if (questions_id.size() == 0){
//
//            temp = random.nextInt(questions.size());
//            questions_id.add(temp);
//            string_question = String.valueOf(questions.get(temp));
//        }
//        //
//        // Проверяем не закончились ли вопросы , для избежания ошибок
//        //
//        else if (questions_id.size() == questions.size()){
//
//            string_question = Constants.END_QUESTION;
//            Log.d(Constants.MY_LOG, "вопросы закончились");
//
//
//            //
//            // Процес проверки одинаковых вопросов
//            //
//        }else {
//
//            while (string_question.equals("")){
//
//                temp = random.nextInt(questions.size());//получаем случайное число, которое являеться номером вопроса
//                for (int i = 0; i < questions_id.size(); i++)
//                {
//
//                    if (questions_id.get(i) == temp)
//                    {
//                        Log.d(Constants.MY_LOG, "зашло в ИФ " +questions_id.size() +" " + questions.size());
//                        break ;
//                    }
//                    else if (i == (questions_id.size()-1) && questions_id.get(i)!= temp)
//                    {
//                        questions_id.add(temp);
//                        string_question = String.valueOf(questions.get(temp));
//                    }
//                }
//
//            }
//
//        }
//        return string_question ;
//    }
//
//
//
//
//
//    private void initQuestion(){
//        // Вопрос*Ответ1*Ответ2*Ответ3*Ответ4*ОтветПравильный
//        questions.add("Кто из президентов США написал свой собственный рассказ про Шерлока Холмса?&Джон Кеннеди&Франклин Рузвельт&Рональд Рейган&Дональд Трамп&Франклин Рузвельт");
//        questions.add("Какую пошлину ввели в XII  веке в Англии для того чтобы заставить мужчин пойти на войну?&Налог на тунеядство&Налог на трусость&Налог на отсутствие сапог&Налог на животных&Налог на трусость");
//        questions.add("Туристы, приезжающие на Майорку, обязаны заплатить налог…&На плавки&На пальмы.&На солнце&На песок&На солнце");
//        questions.add("Основой для «Сказки о рыбаке и рыбке Пушкина послужила сказка братьев Гримм «Рыбак и его жена». В ней немецкая «коллега» нашей старухи превратилась в:&Папу Римского&Королеву&Директора рыбзавода&Командира отряда водолазов&Папу Римского");
//        questions.add("Российский мультфильм, удостоенный «Оскара», — это…&«Простоквашино»&«Винни-Пух»&«Старик и море»&«Ну, погоди!»&«Старик и море»");
//        questions.add("Какого числа отмечают День солидарности трудящихся?&Седьмого ноября&Восьмого марта&Первого мая&Четвертого июня&Первого мая");
//        questions.add("Какого вида транспорта не существует?&Авиационный&Городской общественный&Пешеходный&Железнодорожный&Пешеходный");
//        questions.add("Как называется в геометрии линия, делящая угол пополам?&Биссектриса&Синусоида&Гипотенуза&Секущая&Биссектриса");
//        questions.add("Какое дерево характерно для саванн Африки?&Эвкалипт&Баобаб&Лиственница&Береза&Баобаб");
//        questions.add("Где периодически происходит сокращение штатов?&В США&В бюджете&В учреждениях&В налогах&В учреждениях");
//        questions.add("От чего яблоко падает недалеко?&От дома&От Ньютона&От яблони&От забора&От яблони");
//        questions.add("От какого сладкого лакомства заболел старик Хоттабыч?&Мороженое&Лимонад&Орехи&Шоколад&Мороженое");
//        questions.add("Как звали пушкинского Онегина?&Александр&Евгений&Иван&Михаил&Евгений");
//        questions.add("Что из перечисленного Кот Матроскин не предъявлял в качестве своих документов ?&Лапы&Усы&Хвост&Уши&Уши");
//        questions.add("Кто по профессии братья Жемчужниковы?&Актеры&Певцы&Писатели&Политики&Писатели");
//        questions.add("Какое ежегодное мероприятие в Рио-де-Жанейро привлекает туристов со всего мира?&Карнавал&Экономический форум&Кинофестиваль&Военный парад&Карнавал");
//        questions.add("Про кого С.Маршак написал стихотворение \"Усатый - полосатый\" ?&Про гусара&Про котенка&Про матроса&Про офицера&Про котенка");
//        questions.add("Кого, предположительно, можно обнаружить в тихом омуте?&Бубей&Червей&Чертей&Угрей&Чертей");
//    }



}
