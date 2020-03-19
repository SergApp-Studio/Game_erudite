package com.example.game_erudite.categoryes;

import android.util.Log;

import com.example.game_erudite.constants.Constants;

import java.util.ArrayList;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
@AllArgsConstructor

public class Questions_BD {

    private ArrayList <String> questions = new ArrayList<>();
    private ArrayList <Integer> numbers_questions = new ArrayList<>();

    private static final String MY_LOG = "MainMenu";

    public Questions_BD() {
    }
    Random random = new Random();





    public String getQuestion(){

        if (questions.size() == 0){
            initQuestion();
        }
        int temp = 0;
        String string_question = "";

        //
        // Задаем первый вопрос, записываем его номер в массив номеров, для дальнейшего несовпадания вопросов
        //
        if (numbers_questions.size() == 0){

            temp = random.nextInt(questions.size());
            numbers_questions.add(temp);
            string_question = String.valueOf(questions.get(temp));
        }
        //
        // Проверяем не закончились ли вопросы , для избежания ошибок
        //
        else if (numbers_questions.size() == questions.size()){

            string_question = Constants.END_QUESTION;
            Log.d(MY_LOG, "вопросы закончились");


            //
            // Процес проверки одинаковых вопросов
            //
        }else {

            while (string_question.equals("")){

                temp = random.nextInt(questions.size());//получаем случайное число, которое являеться номером вопроса
                for (int i = 0; i < numbers_questions.size(); i++)
                {

                    if (numbers_questions.get(i) == temp)
                    {
                        Log.d(MY_LOG, "зашло в ИФ " +numbers_questions.size() +" " + questions.size());
                        break ;
                    }
                    else if (i == (numbers_questions.size()-1) && numbers_questions.get(i)!= temp)
                    {
                        numbers_questions.add(temp);
                        string_question = String.valueOf(questions.get(temp));
                    }
                }

            }

        }
        return string_question ;
    }





    private void initQuestion(){

        // Вопрос*Ответ1*Ответ2*Ответ3*Ответ4*ОтветПравильный
        questions.add("Кто из президентов США написал свой собственный рассказ про Шерлока Холмса?&Джон Кеннеди&Франклин Рузвельт&Рональд Рейган&Дональд Трамп&Франклин Рузвельт");
        questions.add("Какую пошлину ввели в XII  веке в Англии для того чтобы заставить мужчин пойти на войну?&Налог на тунеядство&Налог на трусость&Налог на отсутствие сапог&Налог на животных&Налог на трусость");
        questions.add("Туристы, приезжающие на Майорку, обязаны заплатить налог…&На плавки&На пальмы.&На солнце&На песок&На солнце");
        questions.add("Основой для «Сказки о рыбаке и рыбке Пушкина послужила сказка братьев Гримм «Рыбак и его жена». В ней немецкая «коллега» нашей старухи превратилась в:&Папу Римского&Королеву&Директора рыбзавода&Командира отряда водолазов&Папу Римского");
        questions.add("Российский мультфильм, удостоенный «Оскара», — это…&«Простоквашино»&«Винни-Пух»&«Старик и море»&«Ну, погоди!»&«Старик и море»");
        questions.add("Какого числа отмечают День солидарности трудящихся?&Седьмого ноября&Восьмого марта&Первого мая&Четвертого июня&Первого мая");
        questions.add("Какого вида транспорта не существует?&Авиационный&Городской общественный&Пешеходный&Железнодорожный&Пешеходный");
        questions.add("Как называется в геометрии линия, делящая угол пополам?&Биссектриса&Синусоида&Гипотенуза&Секущая&Биссектриса");
        questions.add("Какое дерево характерно для саванн Африки?&Эвкалипт&Баобаб&Лиственница&Береза&Баобаб");
        questions.add("Где периодически происходит сокращение штатов?&В США&В бюджете&В учреждениях&В налогах&В учреждениях");
        questions.add("От чего яблоко падает недалеко?&От дома&От Ньютона&От яблони&От забора&От яблони");
        questions.add("От какого сладкого лакомства заболел старик Хоттабыч?&Мороженое&Лимонад&Орехи&Шоколад&Мороженое");
        questions.add("Как звали пушкинского Онегина?&Александр&Евгений&Иван&Михаил&Евгений");
        questions.add("Что из перечисленного Кот Матроскин не предъявлял в качестве своих документов ?&Лапы&Усы&Хвост&Уши&Уши");
        questions.add("Кто по профессии братья Жемчужниковы?&Актеры&Певцы&Писатели&Политики&Писатели");
        questions.add("Какое ежегодное мероприятие в Рио-де-Жанейро привлекает туристов со всего мира?&Карнавал&Экономический форум&Кинофестиваль&Военный парад&Карнавал");
        questions.add("Про кого С.Маршак написал стихотворение \"Усатый - полосатый\" ?&Про гусара&Про котенка&Про матроса&Про офицера&Про котенка");
        questions.add("Кого, предположительно, можно обнаружить в тихом омуте?&Бубей&Червей&Чертей&Угрей&Чертей");


    }



}
