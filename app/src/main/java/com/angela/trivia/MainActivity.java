package com.angela.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.angela.trivia.data.QuestionBank;
import com.angela.trivia.databinding.ActivityMainBinding;
import com.angela.trivia.format.Question;
import com.angela.trivia.format.Score;
import com.angela.trivia.preferences.Preferences;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Question> questionList;
    private ActivityMainBinding binding;
    private int questionIndex = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private Score score;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        score = new Score();

        preferences = new Preferences(MainActivity.this);

        questionList = new QuestionBank().getQuestions(questionArrayList -> {
                    binding.textQuestion.setText((CharSequence) questionList.get(questionIndex).getAnswer());
                    updateTextPointer((ArrayList<Question>) questionList);
                }
        );
        binding.nextButton.setOnClickListener(v -> {
            questionIndex = (questionIndex +1)% questionList.size();
            updateTextPointer((ArrayList<Question>) questionList);
            binding.falseButton.setEnabled(true);
            binding.trueButton.setEnabled(true);
            binding.nextButton.setText("next");
            updateQuestion();
            preferences.saveHighestScore(correctAnswers);
            binding.scoreText.setTextColor(Color.BLACK);

        });
        binding.trueButton.setOnClickListener(v -> {
            checkAnswer(true);
            binding.falseButton.setEnabled(false);
            binding.trueButton.setEnabled(false);
            if (score.getScore()>preferences.getHighestScore()){
                ////
                binding.scoreText.setTextColor(Color.GREEN);
                binding.textQuestion.setText("HIGHEST SCORE");
                binding.textQuestion.setTextColor(Color.GREEN);
            }
            updateQuestion();

        });

        binding.falseButton.setOnClickListener(View -> {
            checkAnswer(false);
            binding.falseButton.setEnabled(false);
            binding.trueButton.setEnabled(false);
            if (score.getScore()>preferences.getHighestScore()){
                binding.scoreText.setTextColor(Color.GREEN);
                binding.textQuestion.setText("HIGHEST SCORE");

            }
            updateQuestion();


        });

        binding.endButton.setOnClickListener(v -> {
            binding.textQuestion.setText("Final Score: "+correctAnswers);
            questionIndex = -1;
            correctAnswers = 0;
            totalQuestions = 0;
            binding.nextButton.setText("play again");
            binding.scoreText.setTextColor(Color.BLACK);
            binding.scoreText.setText("0");
            zoomInAnimationFinalScore();
            preferences.saveHighestScore(correctAnswers);

        });

        binding.highestScoreText.setText("Highest score: "+String.valueOf(preferences.getHighestScore()));
    }

    private void updateTextPointer(List<Question> questionList) {
        binding.textPointer.setText("Question: "+questionIndex+"/"+questionList.size());
    }

    private void updateQuestion() {
        String question = questionList.get(questionIndex).getAnswer();
        binding.textQuestion.setText(question);
        updateTextPointer((List<Question>) questionList);
    }


    private void checkAnswer(boolean userAnswer) {
        if (questionList.get(questionIndex).isAnswerTrue()==userAnswer){
            Snackbar.make(binding.questionCard, R.string.correct_message, Snackbar.LENGTH_SHORT)
                   .show();

            addCorrect();
            zoomInAnimation();

        } else if (questionList.get(questionIndex).isAnswerTrue()!=userAnswer) {
                Snackbar.make(binding.questionCard, R.string.incorrect_message, Snackbar.LENGTH_SHORT)
                        .show();
            deductPoints();
            shakingAnimation();

        }else {
            //default
        }

    }
    private void shakingAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        binding.questionCard.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textQuestion.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textQuestion.setTextColor(Color.BLACK);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void zoomInAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoom_in);
        binding.questionCard.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            binding.textQuestion.setTextColor(Color.GREEN);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               binding.textQuestion.setTextColor(Color.BLACK);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void zoomInAnimationFinalScore() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoom_in);
        binding.questionCard.setAnimation(shake);
    }
    private void deductPoints(){
        correctAnswers -= 100;
        score.setScore(correctAnswers);
        binding.scoreText.setText(String.valueOf(score.getScore()));
    }
    private void addCorrect(){
        correctAnswers+=100;
        score.setScore(correctAnswers);
        binding.scoreText.setText(String.valueOf(score.getScore()));
    }

}
